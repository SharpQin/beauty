package cc.microthink.customer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Note: Not effective, just example for exercise
 */
@Configuration
public class KafkaConfiguration {

    private Logger log = LoggerFactory.getLogger(KafkaConfiguration.class);

    public DefaultErrorHandler getErrorHandler() {
        return new DefaultErrorHandler((record, exception) -> {
            // recover after 3 failures, with no back off - e.g. send to a dead-letter topic

            log.info("getErrorHandler: record:{}", record);
            log.info("getErrorHandler: exception:{}", exception);

        }, new FixedBackOff(3000L, 2L));
    }

//    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        //factory.setConsumerFactory(consumerFactory());
        //factory.getContainerProperties().setAckOnError(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        //new DefaultErrorHandler(new FixedBackOff(3000L, 1L))
        factory.setCommonErrorHandler(getErrorHandler());

        return factory;
    }

    private static class ListenerContainerCustomizerCustom implements ListenerContainerCustomizer<AbstractMessageListenerContainer> {

        private Logger log = LoggerFactory.getLogger(KafkaConfiguration.class);

        public DefaultErrorHandler getErrorHandler() {
            return new DefaultErrorHandler((record, exception) -> {
                // recover after 3 failures, with no back off - e.g. send to a dead-letter topic

                log.info("===getErrorHandler: record:{}", record);
                log.info("===getErrorHandler: exception:{}", exception);

            }, new FixedBackOff(3000L, 2L));
        }

        @Override
        public void configure(AbstractMessageListenerContainer container, String destinationName, String group) {

            container.setCommonErrorHandler(getErrorHandler());
            log.info(String.format("---ListenerContainerCustomizerCustom.configure: HELLO from container %s, destination: %s, group: %s", container, destinationName, group));
        }
    }

    @Bean
    public ListenerContainerCustomizer<AbstractMessageListenerContainer> listenerContainerCustomizer() {
        log.info(String.format("---DEBUG: Bean %s has bean created.", "listenerContainerCustomizer"));
        return new ListenerContainerCustomizerCustom();
    }

}
