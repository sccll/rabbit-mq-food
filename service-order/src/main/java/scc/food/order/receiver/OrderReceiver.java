package scc.food.order.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.OrderHeader;
import scc.food.common.enums.OrderHeaderStatusEnum;
import scc.food.order.service.OrderHeaderService;

import java.io.IOException;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Component
public class OrderReceiver {

    private final OrderHeaderService orderHeaderService;
    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public OrderReceiver(OrderHeaderService orderHeaderService, RabbitTemplate rabbitTemplate) {
        this.orderHeaderService = orderHeaderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * RabbitHandler绑定在方法上，视为队列名的多个消费实现
     * RabbitListener 添加在类上，需要在方法上添加RabbitHandler注解， 放在方法上不需要添加RabbitHandler注解【推荐】
     * isDefault = true （如果类中有很多方法）表示监听的队列都进入这个方法
     * RabbitListener 中有有一个注解bindings， 可以直接生命交换机队列和绑定， 不需要在RabbitConfig中绑定了
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 异常
     */
    @RabbitHandler(isDefault = true)
    @RabbitListener(queues = {"orderQueue"})
    public void process(Channel channel, Message message) throws IOException {
        try {
            System.out.println("--------------消费者开始消费--------------");
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            // order为生产者传过来的消息
            OrderHeader order = orderHeaderService.getById(orderMessage.getOrderId());

            // 问题？ 所有的服务都绑定订单，我们怎么知道这个消息是哪一个为服务发过来的？
            // 解答： 可以设置类型，通过类型或订单状态判断

            String orderStatus = order.getStatus();
            if (OrderHeaderStatusEnum.ORDER_CREATING.getName().equals(orderStatus)) {
                if (orderMessage.getConfirmed() && null != orderMessage.getPrice()) {
                    // 新创建的订单商家同意了且设定了价格
                    order.setStatus(OrderHeaderStatusEnum.RESTAURANT_CONFIRMED.getName());
                    order.setPrice(orderMessage.getPrice());
                    orderHeaderService.updateById(order);
                    // 商家确认订单， 通知骑手取货
                    rabbitTemplate.convertAndSend("deliveryExchange", "key.delivery", JSON.toJSONString(orderMessage));
                } else {
                    // 商家不接单， 订单取消
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.RESTAURANT_CONFIRMED.getName().equals(orderStatus)) {
                // 如果餐厅已经确认且有骑手接单
                if (null != orderMessage.getDeliveryId()) {
                    order.setStatus(OrderHeaderStatusEnum.DELIVERY_CONFIRMED.getName());
                    order.setDeliveryId(orderMessage.getDeliveryId());
                    orderHeaderService.updateById(order);
                    // 发送结算， 结算使用Fanout扇形分发消息机制， routing.key不重要
                    // 特别注意， fanout分发机制使得 order和settlement不能是同一个交换机Exchange，不然order会接受消息
                    rabbitTemplate.convertAndSend("settlementOrderExchange", "key.settlement", JSON.toJSONString(orderMessage));
                } else {
                    // 没有骑手接单， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.DELIVERY_CONFIRMED.getName().equals(orderStatus)) {
                // 骑手确认，有结算
                if (null != orderMessage.getSettlementId()) {
                    order.setStatus(OrderHeaderStatusEnum.SETTLEMENT_CONFIRMED.getName());
                    order.setSettlementId(orderMessage.getSettlementId());
                    orderHeaderService.updateById(order);
                    // 结算完成之后，发送积分清算
                    rabbitTemplate.convertAndSend("rewardExchange", "key.reward", JSON.toJSONString(orderMessage));
                } else {
                    // 没有结算， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.SETTLEMENT_CONFIRMED.getName().equals(orderStatus)) {
                if (null != orderMessage.getRewardId()) {
                    order.setStatus(OrderHeaderStatusEnum.ORDER_CREATED.getName());
                    order.setRewardId(orderMessage.getRewardId());
                } else {
                    // 没有， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                }
                orderHeaderService.updateById(order);
            }

            System.out.println("--------------消费者完成消费--------------");
            // DeliveryTag 发送的序号， 表示该Channel发送的第几条消息
            // multiple 是true表示确认多条消息， false表示确认单条消息【推荐】
            // 发送端确认机制，发送端发送消息到Exchange成功，basicAck方法会被调用，
            // 配置开启手动应答， 系统没有ack， 则unacked = 1， 重启服务后ready = 1(重回队列等待其他相应队列消费)
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("--------------消费者完成应答--------------");
        } catch (Exception e) {
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列，不推荐开启重回队列，因为第一次异常，基本上下次也是异常，容易死循环
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
