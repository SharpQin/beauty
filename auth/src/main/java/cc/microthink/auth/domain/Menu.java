package cc.microthink.auth.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Menu.
 */
@Table("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Column("title")
    private String title;

//    @NotNull(message = "must not be null")
    @Size(max = 60)
    @Column("link")
    private String link;

//    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("method")
    private String method;

//    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Column("auth_key")
    private String authKey;

    @Column("showed")
    private Boolean showed;

    @Column("parent")
    private Long parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Menu name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public Menu title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public Menu link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMethod() {
        return this.method;
    }

    public Menu method(String method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuthKey() {
        return this.authKey;
    }

    public Menu authKey(String authKey) {
        this.setAuthKey(authKey);
        return this;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Boolean getShowed() {
        return this.showed;
    }

    public Menu showed(Boolean showed) {
        this.setShowed(showed);
        return this;
    }

    public void setShowed(Boolean showed) {
        this.showed = showed;
    }

    public Long getParent() {
        return this.parent;
    }

    public Menu parent(Long parent) {
        this.setParent(parent);
        return this;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", link='" + getLink() + "'" +
            ", method='" + getMethod() + "'" +
            ", authKey='" + getAuthKey() + "'" +
            ", showed='" + getShowed() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
