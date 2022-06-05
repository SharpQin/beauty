package cc.microthink.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cc.microthink.auth.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authority.class);
        Authority menu1 = new Authority();
        menu1.setId(1L);
        Authority menu2 = new Authority();
        menu2.setId(menu1.getId());
        assertThat(menu1).isEqualTo(menu2);
        menu2.setId(2L);
        assertThat(menu1).isNotEqualTo(menu2);
        menu1.setId(null);
        assertThat(menu1).isNotEqualTo(menu2);
    }
}
