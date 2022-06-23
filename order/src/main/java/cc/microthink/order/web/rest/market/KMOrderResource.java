package cc.microthink.order.web.rest.market;

import cc.microthink.order.service.MKOrderService;
import cc.microthink.order.service.dto.CreateOrderDTO;
import cc.microthink.order.service.dto.CreateOrderResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mki")
public class KMOrderResource {

    private final MKOrderService orderService;

    public KMOrderResource(MKOrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping("/order/cre")
    public CreateOrderResult createOrder(@RequestBody CreateOrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping("/order/cancel")
    public String cancelOrder(Long orderId) {
        boolean result = orderService.cancelOrder(orderId);
        return String.valueOf(result);
    }

}
