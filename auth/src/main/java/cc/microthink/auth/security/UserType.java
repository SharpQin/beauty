package cc.microthink.auth.security;

public class UserType {

    private static final String TYPE_ID_MANAGER = "MANAGER";

    private static final String TYPE_ID_MARKET = "MARKET";

    public static UserType of(String typeId) {
        return new UserType(typeId);
    }

    public static UserType ofManager() {
        return new UserType(TYPE_ID_MANAGER);
    }

    public static UserType ofMarket() {
        return new UserType(TYPE_ID_MARKET);
    }

    private String typeId;

    public UserType(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean isManager() {
        return TYPE_ID_MANAGER.equals(this.typeId);
    }

    public boolean isMarket() {
        return TYPE_ID_MARKET.equals(this.typeId);
    }
}
