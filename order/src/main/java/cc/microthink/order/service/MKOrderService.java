package cc.microthink.order.service;

import cc.microthink.common.dto.customer.CustomerDTO;
import cc.microthink.common.dto.product.ProductDTO;
import cc.microthink.order.client.customer.CustomerClient;
import cc.microthink.order.client.product.ProductClient;
import cc.microthink.order.domain.Order;
import cc.microthink.order.domain.OrderItem;
import cc.microthink.order.domain.enumeration.OrderStatus;
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
import java.util.UUID;

@Service
@Transactional
public class MKOrderService {

    private Logger log = LoggerFactory.getLogger(MKOrderService.class);

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    private final CustomerClient customerClient;

    private final OrderEventOutService eventOutService;

    public MKOrderService(OrderRepository orderRepository,
                          @Qualifier("cc.microthink.order.client.product.ProductClient") ProductClient productClient,
                          CustomerClient customerClient, OrderEventOutService eventOutService) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.customerClient = customerClient;
        this.eventOutService = eventOutService;
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
        if (log.isInfoEnabled()) {
            log.info("createOrder: sendOrderCreatedEvent result:{}", success);
        }
        return new CreateOrderResult(savedOrder.getId(), savedOrder.getSerialNo().toString(), productDTO.getName(), savedOrder.getStatus().toString());
    }

    public boolean cancelOrder(Long orderId) {
        log.debug("---cancelOrder---");
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
        orderRepository.save(order);

        boolean success = eventOutService.sendOrderCancelEvent(order);
        if (log.isInfoEnabled()) {
            log.info("cancelOrder: sendOrderCancelEvent result:{}", success);
        }
        //TODO check: success of sendOrderCancelEvent
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
        }
        orderRepository.save(order);

        //TODO set flag by msgId
    }

    public void msgConsumeCancelOrderResult(String msgId, Long orderId) {
        log.debug("---msgConsumeCancelOrderResult---");
        Order order = orderRepository.getById(orderId);
        //TODO Set cancel flag
        orderRepository.save(order);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cancelNotPaymentOrders() {
        //TODO
    }

}
