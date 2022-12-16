package cc.microthink.payment.service.callback;

import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface IPaymentCallback {

    Object doCallback(HttpServletRequest request, @RequestBody byte[] requestBody);

}
