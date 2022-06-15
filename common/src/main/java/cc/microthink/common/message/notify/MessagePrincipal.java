package cc.microthink.common.message.notify;

import java.io.Serializable;

public class MessagePrincipal implements Serializable {

    private Long custId;

    private String custName;

    private String email;

    private String phone;

    private String weixin;

    public static MessagePrincipal ofEmail(Long custId, String custName, String email) {
        MessagePrincipal principal = new MessagePrincipal(custId, custName);
        principal.setEmail(email);
        return principal;
    }

    public static MessagePrincipal ofSms(Long custId, String custName, String phone) {
        MessagePrincipal principal = new MessagePrincipal(custId, custName);
        principal.setPhone(phone);
        return principal;
    }

    public static MessagePrincipal ofWeixin(Long custId, String custName, String weixin) {
        MessagePrincipal principal = new MessagePrincipal(custId, custName);
        principal.setWeixin(weixin);
        return principal;
    }

    public MessagePrincipal() {}

    public MessagePrincipal(Long custId, String custName) {
        this.custId = custId;
        this.custName = custName;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    @Override
    public String toString() {
        return "MessagePrincipal{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", weixin='" + weixin + '\'' +
                '}';
    }
}
