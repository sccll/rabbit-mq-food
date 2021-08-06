package scc.food.settlement.receiver;

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
import scc.food.common.entity.Settlement;
import scc.food.common.enums.SettlementStatusEnum;
import scc.food.settlement.service.SettlementService;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class SettlementReceiver {

    private final SettlementService settlementService;
    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public SettlementReceiver(SettlementService settlementService, RabbitTemplate rabbitTemplate) {
        this.settlementService = settlementService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * RabbitHandler绑定在方法上，视为队列名的多个消费实现
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 异常
     */
    @RabbitHandler
    @RabbitListener(queues = {"settlementQueue"})
    public void process(Channel channel, Message message) throws IOException {
        try {
            System.out.println("--------------消费者开始消费--------------");
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            // 反序列化
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            log.info("handleOrderService:orderSettlement:{}", orderMessage);
            Settlement settlement = Settlement.builder()
                    .amount(orderMessage.getPrice())
                    .date(LocalDateTime.now())
                    .orderId(orderMessage.getOrderId())
                    .status(SettlementStatusEnum.SUCCESS.getName())
                    .transactionId("15791457894564")
                    .build();
            settlementService.insertSettlement(settlement);
            orderMessage.setSettlementId(settlement.getSettlementId());
            // 发送订单确认
            rabbitTemplate.convertAndSend("settlementExchange", "key.order", JSON.toJSONString(orderMessage));
            System.out.println("--------------消费者完成消费--------------");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("--------------消费者完成应答--------------");
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
