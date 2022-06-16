package cc.microthink.common.message.user;

import java.time.Instant;

public class MessageMKUser {

    public static final String ACTION_CREATE = "CRE";
    public static final String ACTION_UPDATE = "UPD";
    public static final String ACTION_DELETE = "DEL";

    private MKUser user;

    private String action;

    public MessageMKUser() {}

    public MessageMKUser(MKUser user, String action) {
        this.user = user;
        this.action = action;
    }

    public MKUser getUser() {
        return user;
    }

    public void setUser(MKUser user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isCreate() {
        return ACTION_CREATE.equals(this.action);
    }

    public boolean isUpdate() {
        return ACTION_UPDATE.equals(this.action);
    }

    public boolean isDelete() {
        return ACTION_DELETE.equals(this.action);
    }

    public static class MKUser {

        private Long id;

        private String login;

        private String nickName;

        private String email;

        private String phone;

        private String langKey;

        private String imageUrl;

        private Instant createdDate;

        public MKUser() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLangKey() {
            return langKey;
        }

        public void setLangKey(String langKey) {
            this.langKey = langKey;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Instant getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
        }

        @Override
        public String toString() {
            return "MKUser{" +
                    "id=" + id +
                    ", login='" + login + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", langKey='" + langKey + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", createdDate=" + createdDate +
                    '}';
        }
    }

}
