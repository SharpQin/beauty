package cc.microthink.payment.web.rest.market;

import cc.microthink.payment.service.MKPaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mki")
public class MKPaymentResource {

    private final MKPaymentService service;

    public MKPaymentResource(MKPaymentService service) {
        this.service = service;
    }

    @PostMapping("/dopayment")
    public void doPayment() {

    }

}
