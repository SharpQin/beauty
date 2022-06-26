package cc.microthink.order.service;

import cc.microthink.common.dto.customer.CustomerDTO;
import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.order.client.customer.CustomerClient;
import cc.microthink.order.client.product.ProductClient;
import cc.microthink.order.domain.Order;
import cc.microthink.order.domain.OrderItem;
import cc.microthink.order.domain.enumeration.OrderCancelReason;
import cc.microthink.order.domain.enumeration.OrderItemStatus;
import cc.microthink.order.domain.enumeration.OrderStatus;
import cc.microthink.order.lock.DistributedLock;
import cc.microthink.order.lock.DistributedLocker;
import cc.microthink.order.lock.annotation.DistributedKeyLock;
import cc.microthink.order.message.out.OrderEventOutService;
import cc.microthink.order.repository.OrderRepository;
import cc.microthink.order.security.SecurityUtils;
import cc.microthink.order.service.dto.CreateOrderDTO;
import cc.microthink.order.service.dto.CreateOrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class MKOrderService {

    private Logger log = LoggerFactory.getLogger(MKOrderService.class);

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    private final CustomerClient customerClient;

    private final OrderEventOutService eventOutService;

    private final DistributedLocker distributedLocker;

    public MKOrderService(OrderRepository orderRepository,
                          @Qualifier("cc.microthink.order.client.product.ProductClient") ProductClient productClient,
                          CustomerClient customerClient, OrderEventOutService eventOutService, DistributedLocker distributedLocker) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.customerClient = customerClient;
        this.eventOutService = eventOutService;
        this.distributedLocker = distributedLocker;
    }

    public CreateOrderResult createOrder(CreateOrderDTO orderDTO) {
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            log.info("getProductById: loginUser:{}", SecurityUtils.getCurrentUserLogin().get());
        }
        ProductDTO productDTO = this.productClient.getProductById(orderDTO.getProductId());
        log.info("createOrder: productDTO.name:{}", productDTO.getName());

        //check product stock
        if (productDTO.getStock() < orderDTO.getCount()) {
            throw new RuntimeException("Not enough stock.");
        }

        CustomerDTO customerDTO = customerClient.getCustomerInfo(false, false);
        log.info("createOrder: customerDTO.name:{}", customerDTO.getNickName());

        BigDecimal totalPrice = productDTO.getPrice();
        Order order = new Order(UUID.randomUUID(), totalPrice, customerDTO.getId(), orderDTO.getRemark());
        //order items
        BigDecimal itemPrice = productDTO.getPrice().multiply(BigDecimal.valueOf(orderDTO.getCount()));
        OrderItem orderItem = new OrderItem(productDTO.getId(), orderDTO.getCount(), itemPrice, productDTO.getPrice());
        order.addItems(orderItem);
        Order savedOrder = orderRepository.save(order);
        boolean success = eventOutService.sendOrderCreatedEvent(savedOrder);

        log.info("createOrder: sendOrderCreatedEvent result:{}", success);

        return new CreateOrderResult(savedOrder.getId(), savedOrder.getSerialNo().toString(), productDTO.getName(), savedOrder.getStatus().toString());
    }

    @DistributedKeyLock(key = "#orderId", prefix = "Cancel_Order_")
    public boolean cancelOrder(Long orderId, OrderCancelReason cancelReason) {
        log.debug("cancelOrder: orderId:{}, cancelReason:{}", orderId, cancelReason);
        if (orderId == null || orderId == 0) {
            log.warn("cancelOrder: Empty orderId and ignore it.");
            return true;
        }

        log.warn("#### cancelOrder: Get a locker: " + orderId);

        Order order = orderRepository.getById(orderId);
        if (order.isPending()) {
            log.warn("cancelOrder: Order can't be cancelled as its pending. orderId:{}", orderId);
            return false;
            //throw exception?
        }
        if (order.isCancelled()) {
            log.warn("cancelOrder: Order has be cancelled already. orderId:{}", orderId);
            return false;
        }
        if (order.isCompleted()) {
            log.info("cancelOrder: The completed Order will be cancelled. orderId:{}", orderId);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(cancelReason);
        orderRepository.save(order);

        //TODO check: success of sendOrderCancelEvent
        boolean success = eventOutService.sendOrderCancelEvent(order);
        log.info("cancelOrder: sendOrderCancelEvent result:{}", success);

        return true;
    }

    public void msgConsumeCreateOrderResult(String msgId, Long orderId, boolean success) {
        log.debug("---msgConsumeCreateOrderResult---");
        //TODO check msgId

        Order order = orderRepository.getById(orderId);
        if (success) {
            order.setStatus(OrderStatus.CREATED);
        }
        else {
            order.setStatus(OrderStatus.CANCELLED);
            order.setCancelReason(OrderCancelReason.OUT_OF_STOCK);
            order.cancelItems(OrderItemStatus.OUT_OF_STOCK);
        }
        orderRepository.save(order);

        //TODO set flag by msgId
    }

    public void msgConsumeCancelOrderResult(String msgId, Long orderId) {
        log.debug("---msgConsumeCancelOrderResult---");
        Order order = orderRepository.getById(orderId);
        //Set items status to BACK_ORDER
        order.cancelItems(OrderItemStatus.BACK_ORDER);
        orderRepository.save(order);
    }

    /**
     * Check time out orders.
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void cancelNotPaymentOrders() {
        log.debug("---cancelNotPaymentOrders begin---");
        //running just only one server.
        //one server running and lock
        boolean successLock = false;
        DistributedLock lock = this.distributedLocker.getLock("Schedule_Cancel_Order");
        try {
            successLock = lock.tryLock(200, TimeUnit.MILLISECONDS);
            if (successLock) {
                log.debug("---cancelNotPaymentOrders: Success to get a distributedLock.");
                LocalDateTime beforeTime = LocalDateTime.now().minusMinutes(30);
                List<Order> orderList = orderRepository.findByStatusAndCreatedTimeLessThan(OrderStatus.CREATED, beforeTime.toInstant(ZoneOffset.UTC));
                for (Order order : orderList) {
                    cancelOrder(order.getId(), OrderCancelReason.TIME_OUT);
                }

                Thread.sleep(3000);
            }
            else {
                log.warn("===> cancelNotPaymentOrders: Fail to get a distributedLock.");
            }
        }
        catch (Exception e) {
            log.error("cancelNotPaymentOrders: Fail to scheduled task for cancelling orders.", e);
        }
        finally {
            if (successLock) {
                lock.unlock();
                log.warn("---cancelNotPaymentOrders: lock.release---");
            }
        }
        log.debug("---cancelNotPaymentOrders end---");
    }

}
