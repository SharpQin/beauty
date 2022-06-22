package cc.microthink.customer.web.rest.market;

import cc.microthink.common.dto.customer.CustomerDTO;
import cc.microthink.customer.service.MKCustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mki")
public class MKCustomerResource {

    private final MKCustomerService customerService;

    public MKCustomerResource(MKCustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer")
    public CustomerDTO getCustomerInfo(@RequestParam(value="includeAddress",defaultValue="false")boolean includeAddress, @RequestParam(value="includeAccount",defaultValue="false")boolean includeAccount) {
        return customerService.getCustomerInfo(includeAddress, includeAccount);
    }

}
