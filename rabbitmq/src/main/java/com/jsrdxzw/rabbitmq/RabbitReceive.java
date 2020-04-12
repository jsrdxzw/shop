package com.jsrdxzw.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitReceive {


    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "queue-1", durable = "true"),
                    exchange = @Exchange(name = "exchange-1", type = "topic"),
                    key = "springboot.*")
    )
    @RabbitHandler
    public void onMessage(Message<?> message, Channel channel) throws IOException {
        System.out.println("消费消息：" + message.getPayload());
        // 手工确认
        long deliveryTag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, true);
    }
}
