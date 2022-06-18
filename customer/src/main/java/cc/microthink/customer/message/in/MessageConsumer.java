package cc.microthink.customer.message.in;

import cc.microthink.common.message.user.MessageMKUser;
import cc.microthink.customer.domain.Customer;
import cc.microthink.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
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
     * DB processing
     * @param messageUser
     */
    private void processMKUser(MessageMKUser messageUser) {
        if (messageUser.isCreate()) {
            Customer customer = new Customer(messageUser.getUser());
            //save or update base on orm framework
            customerService.save(customer);
        }
        else if (messageUser.isUpdate()) {
            Customer customer = new Customer(messageUser.getUser());
            customerService.update(customer);
        }
        else if (messageUser.isDelete()) {
            customerService.delete(messageUser.getUser().getId());
        }
    }

    /**
     * Consume event when create a new MKUser
     * @return
     */
    @Bean
    public Consumer<Message<MessageMKUser>> onMKUserActionConsumer() {
        return message -> {

            /* //autoCommitOffset=false and commit acknowledgment manually
            Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
            log.debug("onMKUserActionConsumer: acknowledgment:{}", acknowledgment);
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
            */

            MessageMKUser messageUser = message.getPayload();
            log.info("onMKUserActionConsumer: messageUser:{}", messageUser);
            processMKUser(messageUser);
        };
    }

    /**
     * Consume dlq message
     * @return
     */
    @Bean
    public Consumer<MessageMKUser> onMKUserFailConsumer() {
        return messageMKUser -> {
            log.debug("###### onMKUserFailConsumer ######");
            log.debug("onMKUserFailConsumer: action:{}, id:{}, name:{}", messageMKUser.getAction(), messageMKUser.getUser().getId(),messageMKUser.getUser().getLogin());
            log.debug("onMKUserFailConsume: messageMKUser{}", messageMKUser);
            processMKUser(messageMKUser);
        };
    }
}
