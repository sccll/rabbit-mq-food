package scc.food.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scc.food.order.service.OrderHeaderService;
import scc.food.order.vo.OrderHeaderCreateVO;

/**
 * @author celine
 * @since 2021/8/3
 */
@RestController
@RequestMapping("/order")
public class OrderHeaderController {

    private final OrderHeaderService orderHeaderService;

    public OrderHeaderController(OrderHeaderService orderHeaderService) {
        this.orderHeaderService = orderHeaderService;
    }

    @GetMapping("/create")
    public void createOrderHeader () {
        OrderHeaderCreateVO order = OrderHeaderCreateVO.builder()
                .accountId(1)
                .address("北京")
                .productId(1)
                .build();
        orderHeaderService.createOrderHeader(order);
    }
}
