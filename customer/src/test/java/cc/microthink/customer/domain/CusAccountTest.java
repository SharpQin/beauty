package cc.microthink.customer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cc.microthink.customer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CusAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CusAccount.class);
        CusAccount cusAccount1 = new CusAccount();
        cusAccount1.setId(1L);
        CusAccount cusAccount2 = new CusAccount();
        cusAccount2.setId(cusAccount1.getId());
        assertThat(cusAccount1).isEqualTo(cusAccount2);
        cusAccount2.setId(2L);
        assertThat(cusAccount1).isNotEqualTo(cusAccount2);
        cusAccount1.setId(null);
        assertThat(cusAccount1).isNotEqualTo(cusAccount2);
    }
}
