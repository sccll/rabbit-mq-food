package scc.food.order.config;

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

    // ----------------------------------  餐厅交换机和订单队列绑定[Direct] --------------------------------------

    /**
     * 声明一个交换机，起名 restaurantExchange， 默认持久化
     * @Bean 声明式配置， 加入Spring容器管理
     */
    @Bean
    DirectExchange restaurantExchange() {
        return new DirectExchange("restaurantExchange");
    }

    @Bean
    public Queue orderQueue() {
        // 队列是否持久化 true
        return new Queue("orderQueue");
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(orderQueue()).to(restaurantExchange()).with("key.order");
    }

    // ----------------------------------- 骑手交换机和订单绑定[Direct] --------------------------------------

    @Bean
    DirectExchange deliveryExchange() {
        return new DirectExchange("deliveryExchange");
    }


    @Bean
    Binding bindingDeliveryOrderDirect() {
        return BindingBuilder.bind(orderQueue()).to(deliveryExchange()).with("key.order");
    }

    // ----------------------------------- 结算和订单绑定【Fanout】, key不重要，发给绑定的所有队列 --------------------------------------

    /**
     * Fanout使用的场景是广播， 一定要注意，Fanout时， Order和settlement服务一定不能使用相同的交换机，否则Order发的消息Order也会接受到
     * @return FanoutExchange
     */
    @Bean
    FanoutExchange settlementExchange() {
        return new FanoutExchange("settlementExchange");
    }

    @Bean
    Binding bindingSettlementOrderDirect() {
        return BindingBuilder.bind(orderQueue()).to(settlementExchange());
    }

    // ----------------------------------- 积分和订单绑定[Topic] --------------------------------------


    @Bean
    TopicExchange rewardExchange() {
        return new TopicExchange("rewardExchange");
    }

    @Bean
    Binding bindingRewardOrderDirect() {
        return BindingBuilder.bind(orderQueue()).to(rewardExchange()).with("key.order");
    }

}
