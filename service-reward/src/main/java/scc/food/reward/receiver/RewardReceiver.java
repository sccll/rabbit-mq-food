package scc.food.reward.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Reward;
import scc.food.common.enums.RewardStatusEnum;
import scc.food.reward.service.RewardService;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class RewardReceiver {

    private final RewardService rewardService;
    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public RewardReceiver(RewardService rewardService, RabbitTemplate rabbitTemplate) {
        this.rewardService = rewardService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * RabbitHandler绑定在方法上，视为队列名的多个消费实现
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 异常
     */
    @RabbitHandler
    @RabbitListener(queues = {"rewardQueue"})
    public void process(Channel channel, Message message) throws IOException {
        try {
            System.out.println("--------------消费者开始消费--------------");
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            Reward reward = Reward.builder()
                    .orderId(orderMessage.getOrderId())
                    .status(RewardStatusEnum.SUCCESS.getName())
                    .amount(orderMessage.getPrice())
                    .date(LocalDateTime.now())
                    .build();
            rewardService.insertReward(reward);
            orderMessage.setRewardId(reward.getRewardId());
            log.info("handleOrderService:settlementOrder:{}", orderMessage);
            // 发送订单确认
            rabbitTemplate.convertAndSend("rewardExchange", "key.order", JSON.toJSONString(orderMessage));
            System.out.println("--------------消费者完成消费--------------");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("--------------消费者完成应答--------------");
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
