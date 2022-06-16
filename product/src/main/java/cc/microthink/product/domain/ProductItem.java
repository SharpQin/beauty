package cc.microthink.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductItem.
 */
@Entity
@Table(name = "product_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "image")
    private String image;

    @Column(name = "dsc")
    private String dsc;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "category" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public ProductItem amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getImage() {
        return this.image;
    }

    public ProductItem image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDsc() {
        return this.dsc;
    }

    public ProductItem dsc(String dsc) {
        this.setDsc(dsc);
        return this;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductItem)) {
            return false;
        }
        return id != null && id.equals(((ProductItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", image='" + getImage() + "'" +
            ", dsc='" + getDsc() + "'" +
            "}";
    }
}
