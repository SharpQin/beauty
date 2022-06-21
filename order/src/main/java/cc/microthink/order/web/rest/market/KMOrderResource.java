package cc.microthink.order.web.rest.market;

import cc.microthink.order.service.MKOrderService;
import cc.microthink.order.service.dto.CreateOrderDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mki")
public class KMOrderResource {

    private final MKOrderService orderService;

    public KMOrderResource(MKOrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping("/order/cre")
    public String createOrder(@RequestBody CreateOrderDTO orderDTO) {
        orderService.createOrder(orderDTO);
        return "success";
    }

}
