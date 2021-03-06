package cc.microthink.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A Role.
 */
@Table("au_role")
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

    /**
     * Authority Split by comma: e.g. auth:do1,auth:do2
     */
    @Size(max = 2048)
    @Column("authorities")
    private String authorities;

    @Transient
    private String[] auths;

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

    public String getAuthorities() {
        return authorities;
    }

    public Role authorities(String authorities) {
        this.setAuthorities(authorities);
        return this;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String[] getAuths() {
        return this.auths;
    }

    public void setAuths(String[] auths) {
        this.auths = auths;
    }

    public void changeForView() {
        if (this.authorities != null && this.authorities.length() > 0) {
            this.auths = this.authorities.split(",");
        }
        else {
            this.auths = new String[]{};
        }
    }

    public void changeForSave() {
        if (this.auths != null && this.auths.length > 0) {
            this.authorities = Arrays.stream(this.auths).collect(Collectors.joining(","));
        }
        else {
            this.authorities = "";
        }
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
            ", authorities='" + getAuthorities() + "'" +
            "}";
    }
}
