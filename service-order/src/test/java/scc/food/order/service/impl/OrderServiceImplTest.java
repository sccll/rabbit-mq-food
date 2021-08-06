package scc.food.order.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import scc.food.common.entity.OrderHeader;
import scc.food.order.mapper.OrderHeaderMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author celine
 * @since 2021/8/3
 */
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @DisplayName("列表查询接口测试")
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<OrderHeader> orderList = orderHeaderMapper.selectList(null);
        assertEquals(1, orderList.size());
        orderList.forEach(System.out::println);
    }

    @DisplayName("模拟生产者发送消息")
    @Test
    public void rabbitTest() {
        rabbitTemplate.convertAndSend("restaurantExchange", "key.order", "餐厅订单服务");
    }
}