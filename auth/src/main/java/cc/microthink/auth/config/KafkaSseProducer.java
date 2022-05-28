package cc.microthink.auth.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaSseProducer {
    String CHANNELNAME = "binding-out-sse";

    @Output(CHANNELNAME)
    MessageChannel output();
}
