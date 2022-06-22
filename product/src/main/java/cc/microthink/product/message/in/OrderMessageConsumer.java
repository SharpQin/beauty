package cc.microthink.product.message.in;

import cc.microthink.common.message.order.OrderMessage;
import cc.microthink.common.message.order.OrderResMessage;
import cc.microthink.product.service.MKProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderMessageConsumer {

    private final MKProductService productService;

    public OrderMessageConsumer(MKProductService productService) {
        this.productService = productService;
    }

    private Function<Message<OrderMessage>, OrderResMessage> onMessageProcess() {
        return message -> {
            String messageId = message.getHeaders().getId().toString();
            OrderMessage orderMessage = message.getPayload();

            OrderResMessage resMessage = new OrderResMessage(orderMessage.getAction(), orderMessage.getOrderId(), orderMessage.getSerialNo());
            if (orderMessage.isCreate())  {
                boolean success = productService.consumeProduct(messageId, orderMessage);
                resMessage.setSuccess(success);
            }
            else if (orderMessage.isCancel()) {
                productService.recoverProduct(messageId, orderMessage);
                resMessage.setSuccess(true);
            }
            return resMessage;
        };
    }

    @Bean
    public Function<Message<OrderMessage>, OrderResMessage> onOrderEventConsumer() {
        return onMessageProcess();
    }

    @Bean
    public Function<Message<OrderMessage>, OrderResMessage> onOrderEventFailConsumer() {
        return onMessageProcess();
    }

}
