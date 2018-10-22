package com.pyy.springboot.consumer;


import com.pyy.springboot.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pyy.queue", durable = "true"),
            exchange = @Exchange(value = "pyy.exchange", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
            key = "springboot.#"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel)throws Exception {
        System.out.println("消费端payLoad:" + message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        // 手动ack  false：不批量
        channel.basicAck(deliveryTag, false);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(
                    value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.exchange.key}"
    ))
    @RabbitHandler
    public void onMessage(@Payload com.pyy.springboot.entity.Order order, Channel channel,
                          @Headers Map<String, Object> headers)throws Exception {
        System.out.println("消费端payLoad:" + order.getId() );

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        // 手动ack  false：不批量
        channel.basicAck(deliveryTag, false);
    }
}