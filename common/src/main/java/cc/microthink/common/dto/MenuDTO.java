package cc.microthink.common.dto;

import java.util.Objects;

public class MenuDTO {

    private String link;

    private String method;

    private String authKey;

    public MenuDTO() {}

    public MenuDTO(String link, String method, String authKey) {
        this.link = link;
        this.method = method;
        this.authKey = authKey;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDTO menuDTO = (MenuDTO) o;
        return authKey.equals(menuDTO.authKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authKey);
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
            "link='" + link + '\'' +
            ", method='" + method + '\'' +
            ", authKey='" + authKey + '\'' +
            '}';
    }
}
