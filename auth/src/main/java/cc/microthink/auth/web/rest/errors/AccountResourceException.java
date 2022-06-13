package cc.microthink.auth.web.rest.errors;

public class AccountResourceException extends RuntimeException {

    public AccountResourceException(String message) {
        super(message);
    }
}
