package scc.food.settlement.config;

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

    // ----------------------------------  结算和订单队列绑定[Fanout] --------------------------------------

    /**
     * 声明一个交换机，起名 settlementExchange
     */
    @Bean
    FanoutExchange settlementOrderExchange() {
        return new FanoutExchange("settlementOrderExchange");
    }

    @Bean
    public Queue settlementQueue() {
        // 队列是否持久化 true
        return new Queue("settlementQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(settlementQueue()).to(settlementOrderExchange());
    }

}
