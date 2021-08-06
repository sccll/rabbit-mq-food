package scc.food.delivery.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.common.config.DefaultRabbitConfig;

/**
 * @author celine
 * @since 2021/8/3
 */
@Configuration
public class RabbitConfig extends DefaultRabbitConfig {

    // ----------------------------------  配送员和订单队列绑定 --------------------------------------

    /**
     * 声明一个交换机，起名 deliveryExchange
     */
    @Bean
    DirectExchange deliveryExchange() {
        return new DirectExchange("deliveryExchange");
    }

    @Bean
    public Queue deliveryQueue() {
        // 队列是否持久化 true
        return new Queue("deliveryQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(deliveryQueue()).to(deliveryExchange()).with("key.delivery");
    }

}
