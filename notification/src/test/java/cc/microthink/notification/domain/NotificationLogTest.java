package cc.microthink.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cc.microthink.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationLog.class);
        NotificationLog notificationLog1 = new NotificationLog();
        notificationLog1.setId(1L);
        NotificationLog notificationLog2 = new NotificationLog();
        notificationLog2.setId(notificationLog1.getId());
        assertThat(notificationLog1).isEqualTo(notificationLog2);
        notificationLog2.setId(2L);
        assertThat(notificationLog1).isNotEqualTo(notificationLog2);
        notificationLog1.setId(null);
        assertThat(notificationLog1).isNotEqualTo(notificationLog2);
    }
}
