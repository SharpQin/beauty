package cc.microthink.payment.web.rest.market;

import cc.microthink.payment.service.callback.IPaymentCallback;
import cc.microthink.payment.service.callback.PaymentCallbacksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pcr")
public class MKPaymentCallbackResource {

    private Logger log = LoggerFactory.getLogger(MKPaymentCallbackResource.class);

    private final PaymentCallbacksService callbacksService;

    public MKPaymentCallbackResource(PaymentCallbacksService callbacksService) {
        this.callbacksService = callbacksService;
    }

    @PostMapping("/{thirdPart}/callback")
    public String callback(@PathVariable("thirdPart") String thirdPart, HttpServletRequest request, @RequestBody byte[] requestBody) {

        IPaymentCallback paymentCallback = callbacksService.getPaymentCallback(thirdPart);
        if (paymentCallback == null) {
            log.error("callback: No PaymentCallback for {}", thirdPart);
            return "fail";
        }

        return (String)paymentCallback.doCallback(request, requestBody);
    }

}
