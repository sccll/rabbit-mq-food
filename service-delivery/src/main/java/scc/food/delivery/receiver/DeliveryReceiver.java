package scc.food.delivery.receiver;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Delivery;
import scc.food.common.enums.DeliveryStatusEnum;
import scc.food.delivery.service.DeliveryService;

import java.io.IOException;
import java.util.List;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class DeliveryReceiver {

    private final DeliveryService deliveryService;
    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public DeliveryReceiver(DeliveryService deliveryService, RabbitTemplate rabbitTemplate) {
        this.deliveryService = deliveryService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * RabbitHandler绑定在方法上，视为队列名的多个消费实现
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 异常
     */
    @RabbitHandler
    @RabbitListener(queues = {"deliveryQueue"})
    public void process(Channel channel, Message message) throws IOException {
        try {
            System.out.println("--------------消费者开始消费--------------");
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            // 查找所有可以接单的骑手
            List<Delivery> deliveryList = deliveryService.getBaseMapper().selectList(Wrappers.<Delivery>lambdaQuery()
                    .eq(Delivery::getStatus, DeliveryStatusEnum.AVAILABLE.getName()));
            orderMessage.setDeliveryId(deliveryList.get(0).getDeliveryId());
            log.info("onMessage:restaurantOrderMessage:{}", orderMessage);
            // 发送订单确认
            rabbitTemplate.convertAndSend("deliveryExchange", "key.order", JSON.toJSONString(orderMessage));
            System.out.println("--------------消费者完成消费--------------");
            // DeliveryTag 发送的序号， 表示该Channel发送的第几条消息
            // multiple 是true表示确认多条消息， false表示确认单条消息
            // 发送端确认机制，发送端发送消息到Exchange成功，basicAck方法会被调用，
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("--------------消费者完成应答--------------");
        } catch (Exception e) {
            // 发送端确认机制，发送失败到Exchange
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
