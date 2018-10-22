package com.pyy.springboot.producer;


import com.pyy.springboot.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData:" + correlationData);
            System.err.println("ack:" + ack);
            if(!ack) {
                System.err.println("异常处理...");
            }else {
                // 更新数据库对应的消息状态：已发送
            }
        }
    };


    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.err.println("return exchange:" + exchange + " , routingKey:" + routingKey + ", replyCode:" + replyCode + ", replyText:" + replyText);
        }
    };

    public void send(Object message, Map<String, Object> headerProperties) throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders(headerProperties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("userid" + System.currentTimeMillis());// id + 时间戳 全局唯一 实际消息的id
        rabbitTemplate.convertAndSend("pyy.exchange", "springboot.hello", msg, correlationData);

        //rabbitTemplate.convertAndSend("pyy.exchange", "fasdfsf.hello", msg, correlationData);

    }

    public void sendOrder(Order order) throws Exception {

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("userid" + System.currentTimeMillis());// id + 时间戳 全局唯一 实际消息的id
        rabbitTemplate.convertAndSend("exchange-order", "springboot.order", order, correlationData);
    }
}