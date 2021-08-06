package scc.food.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author celine
 * @since 2021/8/3
 * RabbitMQ 发送端消息确认回调函数
 */
@Slf4j
@ConditionalOnProperty(prefix = "spring.rabbitmq")
public class DefaultRabbitConfig {

    /**
     * 左边两个图标的意思， 左箭头， 交给Spring容器进行管理； 右箭头， 从spring容器中取出这个Bean
     * @Autowired 添加不添加无所谓， 都是从spring容器中取出来
     * @param connectionFactory 链接工厂
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate createRabbitTemplate(@Autowired ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        // 设置开启Mandatory,路由失败才能触发消息返回机制的回调函数【不触发，消息没有路由会被丢弃】
        rabbitTemplate.setMandatory(true);

        /**
         * 发送端确认机制【publisher Confirm】两步： 1，确认是否到达交换机， 2， 确认是否到达队列
         * ConfirmCallback
         *  如果消息没有到exchange， 则confirm回调， ack = false
         *  如果消息到达exchange， 则confirm回调， ack = true
         *
         *  ReturnCallback
         *    exchange到达queue成功则不回调ReturnCallback
         *    exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("---------------setConfirmCallback------------------");
            // 因为这个确认消息方法是异步的， 不知道确认的消息到底是哪一条，需要开启publisher-confirm-type: correlated
            // correlationData【相关数据】， 发送消息的时候传过来的设置的id
            log.info("ConfirmCallbackMsg，correlationData:{} ,ack:{}, cause{}", correlationData, ack, cause);
        });

        rabbitTemplate.setReturnsCallback((returnCallback) -> {
            System.out.println("---------------setReturnsCallback------------------");
            log.error("消息丢失，exchange没有到queue；ReturnCallbackMsg，message:{} ,replyCode:{}, replyText:{} exchange:{}routingKey:{}",
                    returnCallback.getMessage(), returnCallback.getReplyCode(), returnCallback.getReplyText(), returnCallback.getExchange(), returnCallback.getRoutingKey());
            System.out.println("ReturnsCallback 交换机 " +returnCallback.getExchange());
            System.out.println("ReturnsCallback 回应消息 " +returnCallback.getReplyText());
            System.out.println("ReturnsCallback 路由键 " +returnCallback.getRoutingKey());
            System.out.println("ReturnsCallback 消息 " +returnCallback.getMessage());
            System.out.println("ReturnsCallback 回应码 " +returnCallback.getReplyCode());

        });
        return rabbitTemplate;
    }

}
