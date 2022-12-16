package cc.microthink.payment.service.callback;

import cc.microthink.payment.message.out.PaymentEventOutService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component("wechat")
public class WeChatPaymentCallback implements IPaymentCallback {

    private final static String SUCCESS = "success";

    private final PaymentEventOutService messageService;

    private final PaymentCallbacksService callbacksService;

    public WeChatPaymentCallback(PaymentEventOutService messageService, PaymentCallbacksService callbacksService) {
        this.messageService = messageService;
        this.callbacksService = callbacksService;
    }

    @PostConstruct
    public void registerMyself() {
        this.callbacksService.registerPaymentCallback("wechat", this);
    }

    @Override
    public Object doCallback(HttpServletRequest request, byte[] requestBody) {

        String orderSerial = request.getParameter("orderSerial");
        String custId = request.getParameter("custId");
        String money = request.getParameter("money");
        String status = request.getParameter("status");


        String result;
        if (SUCCESS.equals(status)) {
            result = "success";

        }
        else {
            result = "fail";
        }

        //TODO
        messageService.sendPaymentResultEvent();

        return result;
    }

}
