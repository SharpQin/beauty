package cc.microthink.order.message.in;

import cc.microthink.common.message.order.OrderResMessage;
import cc.microthink.order.service.MKOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ProductMessageConsumer {

    private final MKOrderService orderService;

    public ProductMessageConsumer(MKOrderService orderService) {
        this.orderService = orderService;
    }

    private Consumer<Message<OrderResMessage>> onOrderProdResProcess() {
        return message -> {
            String msgId = message.getHeaders().getId().toString();
            OrderResMessage resMessage = message.getPayload();
            if (resMessage.isCreate()) {
                orderService.msgConsumeCreateOrderResult(msgId, resMessage.getOrderId(), resMessage.isSuccess());
            }
            else if (resMessage.isCancel()) {
                orderService.msgConsumeCancelOrderResult(msgId, resMessage.getOrderId());
            }
        };
    }

    @Bean
    public Consumer<Message<OrderResMessage>> onOrderProdResConsumer() {
        return onOrderProdResProcess();
    }


    @Bean
    public Consumer<Message<OrderResMessage>> onOrderProdResFailConsumer() {
        return onOrderProdResProcess();
    }

}
