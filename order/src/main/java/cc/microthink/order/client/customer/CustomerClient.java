package cc.microthink.order.client.customer;

import cc.microthink.common.dto.customer.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer")
public interface CustomerClient {

    @GetMapping("/mki/customer")
    CustomerDTO getCustomerInfo(@RequestParam(value="includeAddress",defaultValue="false")boolean includeAddress, @RequestParam(value="includeAccount",defaultValue="false")boolean includeAccount);
}
