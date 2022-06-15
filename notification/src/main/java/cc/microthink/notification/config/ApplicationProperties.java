package cc.microthink.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Notification.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final ApplicationProperties.Email email = new ApplicationProperties.Email();

    public ApplicationProperties() {
    }

    public Email getEmail() {
        return email;
    }

    //@Value("{email.sender}")
    //private String emailSender;

    public static class Email {

        /**
         * Indicator of active/inactive to send email.
         */
        private boolean enable;

        private String sender = "";

        public Email() {
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

}
