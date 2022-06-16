package cc.microthink.customer.message.in;

import cc.microthink.common.message.user.MessageMKUser;
import cc.microthink.customer.domain.Customer;
import cc.microthink.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MessageConsumer {

    private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    private final CustomerService customerService;

    public MessageConsumer(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Consume event when create a new MKUser
     * @return
     */
    @Bean
    public Consumer<MessageMKUser> onMKUserActionConsumer() {
        return messageUser -> {
            log.info("onMKUserActionConsumer: messageUser:{}", messageUser);
            if (messageUser.isCreate()) {
                Customer customer = new Customer(messageUser.getUser());
                customerService.save(customer);
            }
            else if (messageUser.isUpdate()) {
                Customer customer = new Customer(messageUser.getUser());
                customerService.update(customer);
            }
            else if (messageUser.isDelete()) {
                customerService.delete(messageUser.getUser().getId());
            }
        };
    }
}
