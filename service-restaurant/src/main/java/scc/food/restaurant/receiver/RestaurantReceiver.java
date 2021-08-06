package scc.food.restaurant.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Product;
import scc.food.common.entity.Restaurant;
import scc.food.common.enums.ProductStatusEnum;
import scc.food.common.enums.RestaurantStatusEnum;
import scc.food.restaurant.service.ProductService;
import scc.food.restaurant.service.RestaurantService;

import java.io.IOException;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class RestaurantReceiver {

    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    public RestaurantReceiver(RestaurantService restaurantService, ProductService productService, RabbitTemplate rabbitTemplate) {
        this.restaurantService = restaurantService;
        this.productService = productService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * RabbitHandler绑定在方法上，视为队列名的多个消费实现
     * @param channel 通道
     * @param message 消息体
     * @throws IOException 异常
     */
    @RabbitListener(
            // queues = {"restaurantQueue"},
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "restaurantQueue",
                                    arguments = {
                                        @Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Integer"),
                                        @Argument(name = "x-dead-letter-exchange", value = "deadExchange"),
                                        @Argument(name = "x-dead-letter-routing-key", value = "#")
                                    }),
                            exchange = @Exchange(name = "restaurantExchange"),
                            key = "key.restaurant"
                    )
            }
    )
    public void process(Channel channel, Message message) throws IOException {
        try {
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            System.out.println("--------------消费者开始消费--------------");
            // 外卖产品信息
            Product product = productService.getById(orderMessage.getProductId());
            log.info("onMessage:product:{}", product);
            // 生产外卖产品的餐厅信息
            Restaurant restaurant = restaurantService.getById(product.getRestaurantId());
            log.info("onMessage:restaurant:{}", restaurant);
            // 餐厅营业中且售卖产品
            if (RestaurantStatusEnum.OPEN.getName().equals(restaurant.getStatus()) && ProductStatusEnum.AVAILABLE.getName().equals(product.getStatus())) {
                orderMessage.setConfirmed(true);
                orderMessage.setPrice(product.getPrice());
            } else {
                orderMessage.setConfirmed(false);
            }
            log.info("sendMessage:restaurantOrderMessage:{}", orderMessage);
            // 发送订单确认
            // rabbitTemplate.convertAndSend("restaurantExchange", "key.order", JSON.toJSONString(orderMessage));
            System.out.println("--------------消费者完成消费--------------");
            // channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("--------------消费者完成应答--------------");
        } catch (Exception e) {
            // 消息处理失败，false不重回队列，也会被进入死信队列【前提是：restaurantQueue设置了死信队列】
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
