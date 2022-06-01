package cc.microthink.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Role.
 */
@Table("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Column("name")
    private String name;

    @Column("dsc")
    private String dsc;

    @Transient
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Role() {}

    public Role(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDsc() {
        return this.dsc;
    }

    public Role dsc(String dsc) {
        this.setDsc(dsc);
        return this;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Role permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Role addPermissions(Permission permission) {
        this.permissions.add(permission);
        permission.getRoles().add(this);
        return this;
    }

    public Role removePermissions(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dsc='" + getDsc() + "'" +
            "}";
    }
}
