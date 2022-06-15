package cc.microthink.notification.service;

import cc.microthink.common.message.notify.NotifyMessage;
import cc.microthink.notification.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * Send email for notification.
 * Configure in applicaiton.yml:
 * spring:
 *    mail: xx
 *    username: xx@xx.xx
 *    password: xx
 *    ...
 *
 *  application:
 *    email:
 *      email: true
 *      sender: xx@xx.xx
 */
@Service("email")
public class EmailNotify implements INotify{

    private Logger log = LoggerFactory.getLogger(EmailNotify.class);

    private final ApplicationProperties properties;

    private final JavaMailSender emailSender;

    public EmailNotify(ApplicationProperties properties, JavaMailSender emailSender) {
        this.properties = properties;
        this.emailSender = emailSender;
    }

    @Override
    public boolean notify(NotifyMessage notifyMessage) {
        if (log.isDebugEnabled()) {
            log.debug("notify: application.email.enable:{}", properties.getEmail().isEnable());
            log.debug("notify: application.email.sender:{}", properties.getEmail().getSender());
        }
        if (properties.getEmail().isEnable()) {
            log.warn("notify: sending email is not enabled and It's dummy to success to send a email.");
            return true;
        }

        MimeMessage message = emailSender.createMimeMessage();
        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(properties.getEmail().getSender());
            helper.setTo(notifyMessage.getPrincipal().getEmail());
            helper.setSubject(notifyMessage.getContent().getSubject());
            helper.setText(notifyMessage.getContent().getContent(), true);

            emailSender.send(message);
            log.info("notify: Success to send mail to {}", notifyMessage.getPrincipal().getEmail());
            return true;
        }
        catch (Exception e)
        {
            log.error("Fail to send email to " + notifyMessage.getPrincipal().getEmail(), e);
        }
        return false;
    }

    @Override
    public String getType() {
        return NotifyMessage.NOTIFY_EMAIL;
    }

}
