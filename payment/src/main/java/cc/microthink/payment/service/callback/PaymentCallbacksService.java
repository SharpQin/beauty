package cc.microthink.payment.service.callback;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentCallbacksService {

    private Map<String, IPaymentCallback> callbackMap = new HashMap<>(5);


    public IPaymentCallback getPaymentCallback(String thirdPart) {
        return callbackMap.get(thirdPart);
    }

    public void registerPaymentCallback(String thirdPart, IPaymentCallback paymentCallback) {
        this.callbackMap.put(thirdPart, paymentCallback);
    }

}
