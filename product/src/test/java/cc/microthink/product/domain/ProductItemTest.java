package cc.microthink.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cc.microthink.product.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductItem.class);
        ProductItem productItem1 = new ProductItem();
        productItem1.setId(1L);
        ProductItem productItem2 = new ProductItem();
        productItem2.setId(productItem1.getId());
        assertThat(productItem1).isEqualTo(productItem2);
        productItem2.setId(2L);
        assertThat(productItem1).isNotEqualTo(productItem2);
        productItem1.setId(null);
        assertThat(productItem1).isNotEqualTo(productItem2);
    }
}
