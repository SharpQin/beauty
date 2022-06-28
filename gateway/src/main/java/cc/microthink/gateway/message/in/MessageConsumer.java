package cc.microthink.gateway.message.in;

import cc.microthink.gateway.security.authority.AuthorityMatcherProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MessageConsumer {

    private Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    private final AuthorityMatcherProvider authorityMatcherProvider;

    public MessageConsumer(AuthorityMatcherProvider authorityMatcherProvider) {
        this.authorityMatcherProvider = authorityMatcherProvider;
    }

    @Bean
    public Consumer<Message> onAuthUpdate() {
        return message -> {
            log.info("---onAuthUpdate---");
            authorityMatcherProvider.refresh();
        };
    }

}
