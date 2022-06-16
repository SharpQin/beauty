package cc.microthink.customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Size(max = 30)
    @Column(name = "city", length = 30)
    private String city;

    @NotNull
    @Size(max = 120)
    @Column(name = "details", length = 120, nullable = false)
    private String details;

    @Column(name = "default_addr")
    private Boolean defaultAddr;

    @ManyToOne
    @JsonIgnoreProperties(value = { "addresses", "accounts" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetails() {
        return this.details;
    }

    public Address details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getDefaultAddr() {
        return this.defaultAddr;
    }

    public Address defaultAddr(Boolean defaultAddr) {
        this.setDefaultAddr(defaultAddr);
        return this;
    }

    public void setDefaultAddr(Boolean defaultAddr) {
        this.defaultAddr = defaultAddr;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", details='" + getDetails() + "'" +
            ", defaultAddr='" + getDefaultAddr() + "'" +
            "}";
    }
}
