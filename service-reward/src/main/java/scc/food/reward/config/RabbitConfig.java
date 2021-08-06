package scc.food.reward.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.common.config.DefaultRabbitConfig;

/**
 * @author celine
 * @since 2021/8/3
 */
@Configuration
public class RabbitConfig extends DefaultRabbitConfig {

    // ----------------------------------  餐厅交换机和订单队列绑定[TOPIC] --------------------------------------

    /**
     * 声明一个交换机，起名 rewardExchange
     */
    @Bean
    TopicExchange rewardExchange() {
        return new TopicExchange("rewardExchange");
    }

    @Bean
    public Queue rewardQueue() {
        // 队列是否持久化 true
        return new Queue("rewardQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(rewardQueue()).to(rewardExchange()).with("key.reward");
    }

}
