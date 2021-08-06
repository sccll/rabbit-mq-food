package scc.food.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.OrderHeader;
import scc.food.common.enums.OrderHeaderStatusEnum;
import scc.food.order.service.OrderHeaderService;
import scc.food.order.mapper.OrderHeaderMapper;
import scc.food.order.vo.OrderHeaderCreateVO;

import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 */
@Service
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements OrderHeaderService {

    private final RabbitTemplate rabbitTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    public OrderHeaderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createOrderHeader(OrderHeaderCreateVO orderVo) {
        OrderHeader orderHeader = OrderHeader.builder()
                .accountId(orderVo.getAccountId())
                .address(orderVo.getAddress())
                .productId(orderVo.getProductId())
                .status(OrderHeaderStatusEnum.ORDER_CREATING.getName())
                .date(LocalDateTime.now())
                .build();
        baseMapper.insert(orderHeader);
        // 订单创建完成之后需要给餐厅发送消息
        // 准备餐厅订单数据
        OrderMessageDTO dto = OrderMessageDTO.builder()
                .orderId(orderHeader.getOrderId())
                .productId(orderVo.getProductId())
                .accountId(orderVo.getAccountId())
                .build();
        // Rabbitmq接受和发送的都是字符，需要序列化操作， 使用json格式， 使用fastjson
        // 为什么不使用send发消息而使用convertAndSend， 因为convertAndSend底层是调用send，并且做了消息的转换
        // 添加相关数据, 消息确认机制从在
        CorrelationData data = new CorrelationData(orderHeader.getOrderId().toString());
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("restaurantExchange", "key.restaurant", JSON.toJSONString(dto), data);
        }
    }
}
