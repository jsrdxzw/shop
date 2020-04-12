package com.jsrdxzw.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认消息是否被broker收到
     */
    public final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, s) -> {
        System.out.println("消费结果:" + ack + " 消费id：" + correlationData.getId());
    };

    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders mh = new MessageHeaders(properties);
        Message<?> msg = MessageBuilder.createMessage(message, mh);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        String id = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend("exchange-1",
                "springboot.rabbit",
                msg,
                message1 -> {
                    System.out.println("post to do " + message1 + " id :" + id);
                    return message1;
                },
                new CorrelationData(id));
    }
}
