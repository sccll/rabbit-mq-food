package scc.food.restaurant.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.common.config.DefaultRabbitConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author celine
 * @since 2021/8/3
 */
@Configuration
public class RabbitConfig extends DefaultRabbitConfig {

//    // ----------------------------------  餐厅交换机和订单队列绑定 --------------------------------------
//
//    /**
//     * 声明一个交换机，起名 DrinkExchange
//     */
//    DirectExchange restaurantExchange() {
//        return new DirectExchange("restaurantExchange");
//    }
//
//    public Queue restaurantQueue() {
//        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
//        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
//
//        // 设置队列消息的过期时间, 重新给队列设置参数控制台会报错，需要把之前创建好的队列删除，再重启服务
//        Map<String, Object> map = new HashMap<>();
//        // 队列中的消息未被消费则10秒后过期清除【推荐和死信队列一起使用】， 消费没有应答的不被删除， 直接停掉restaurant服务模拟
//        map.put("x-message-ttl", 10000);
//
//        // 10s不处理消息，队列被删除，不推荐使用
//        // map.put("x-expire", 10000);
//
//        // restaurant添加下面的属性之后，被成为死信队列
//        map.put("x-dead-letter-exchange", "deadExchange");
//
//        // 模拟 队列未消费的（Ready）最大个数达到5， 之后的消息是死信，进入死信队列
//        // map.put("x-max-length", 5);
//
//
//        return new Queue("restaurantQueue", true, false, false, map);
//    }
//
//    Binding bindingRestaurantOrderDirect() {
//        return BindingBuilder.bind(restaurantQueue()).to(restaurantExchange()).with("key.restaurant");
//    }
//
//
//    // ----------------------------- 设置接受死信交换机和队列【就是普通的】 -------------------------------------------

    @Bean
    TopicExchange deadExchange() {
        return new TopicExchange("deadExchange");
    }

    @Bean
    public Queue deadQueue() {
        return new Queue("deadQueue", true);
    }

    @Bean
    Binding bindingDeadDirect() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("#");
    }

}
