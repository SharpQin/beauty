package cc.microthink.product.domain;

import cc.microthink.product.domain.enumeration.ProductStatus;
import cc.microthink.product.domain.enumeration.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Product.
 */


@Document(indexName = "product_index")
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements SerializableId {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Field(type = FieldType.Keyword, name = "name")
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Field(type = FieldType.Double, name = "price")
    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Field(type = FieldType.Text, name = "type")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Field(type = FieldType.Text, name = "image")
    @Column(name = "image")
    private String image;

    @Field(type = FieldType.Date, name = "releaseDate")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Field(type = FieldType.Date, name = "liveTime")
    @Column(name = "live_time")
    private Instant liveTime;

    @Field(type = FieldType.Date, name = "createdTime")
    @Column(name = "created_time")
    private Instant createdTime;

    @Field(type = FieldType.Date, name = "updatedTime")
    @Column(name = "updated_time")
    private Instant updatedTime;

    @Field(type = FieldType.Integer, name = "stock")
    @Column(name = "stock")
    private Integer stock;

    @Field(type = FieldType.Boolean, name = "showed")
    @Column(name = "showed")
    private Boolean showed;

    @Field(type = FieldType.Text, name = "status")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Field(type = FieldType.Text, name = "dsc")
    @Column(name = "dsc")
    private String dsc;

    @Field(type = FieldType.Nested, includeInParent = true)
    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductItem> items = new HashSet<>();

    @Field(type = FieldType.Nested, includeInParent = true)
    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private ProductCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductType getType() {
        return this.type;
    }

    public Product type(ProductType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getImage() {
        return this.image;
    }

    public Product image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public Product releaseDate(LocalDate releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Instant getLiveTime() {
        return this.liveTime;
    }

    public Product liveTime(Instant liveTime) {
        this.setLiveTime(liveTime);
        return this;
    }

    public void setLiveTime(Instant liveTime) {
        this.liveTime = liveTime;
    }

    public Instant getCreatedTime() {
        return this.createdTime;
    }

    public Product createdTime(Instant createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return this.updatedTime;
    }

    public Product updatedTime(Instant updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Product stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getShowed() {
        return this.showed;
    }

    public Product showed(Boolean showed) {
        this.setShowed(showed);
        return this;
    }

    public void setShowed(Boolean showed) {
        this.showed = showed;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getDsc() {
        return this.dsc;
    }

    public Product dsc(String dsc) {
        this.setDsc(dsc);
        return this;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public Set<ProductItem> getItems() {
        return this.items;
    }

    public void setItems(Set<ProductItem> productItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setProduct(null));
        }
        if (productItems != null) {
            productItems.forEach(i -> i.setProduct(this));
        }
        this.items = productItems;
    }

    public Product items(Set<ProductItem> productItems) {
        this.setItems(productItems);
        return this;
    }

    public Product addItems(ProductItem productItem) {
        this.items.add(productItem);
        productItem.setProduct(this);
        return this;
    }

    public Product removeItems(ProductItem productItem) {
        this.items.remove(productItem);
        productItem.setProduct(null);
        return this;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public void setCategory(ProductCategory productCategory) {
        this.category = productCategory;
    }

    public Product category(ProductCategory productCategory) {
        this.setCategory(productCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", type='" + getType() + "'" +
            ", image='" + getImage() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", liveTime='" + getLiveTime() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", stock=" + getStock() +
            ", showed='" + getShowed() + "'" +
            ", status='" + getStatus() + "'" +
            ", dsc='" + getDsc() + "'" +
            "}";
    }
}
