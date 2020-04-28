package com.jsrdxzw.rabbitmq.broker;

import com.jsrdxzw.proto.MessageHolder;
import com.jsrdxzw.rabbitmq.api.MessageType;
import com.jsrdxzw.rabbitmq.api.RabbitMessageConvert;
import com.jsrdxzw.rabbitmq.service.MessageStoreService;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.jsrdxzw.rabbitmq.api.MessageType.RAPID;

/**
 * 池化
 * 1. 每一个topic对应一个rabbitTemplate，提高发送的效率
 * 2. 不同的topic可以做定制化
 *
 * @author xuzhiwei
 * @date 2020/04/05
 */
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final Map<String, RabbitTemplate> rabbitTemplateMap = new ConcurrentHashMap<>();

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitMessageConvert rabbitMessageConvert;

    public RabbitTemplate getTemplate(MessageHolder.Message message) {
        Objects.requireNonNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(topic);
        rabbitTemplate.setRetryTemplate(new RetryTemplate());
        rabbitTemplate.setRoutingKey(message.getRoutingKey());
        rabbitTemplate.setMessageConverter(rabbitMessageConvert);
        if (MessageType.fromName(message.getMessageType().name()) != RAPID) {
            rabbitTemplate.setConfirmCallback(this);
            rabbitTemplate.setReturnCallback(this);
            rabbitTemplate.setMandatory(true);
        } else {
            rabbitTemplate.containerAckMode(AcknowledgeMode.AUTO);
        }
        rabbitTemplateMap.put(topic, rabbitTemplate);
        return rabbitTemplate;
    }

    // 发送端的确认消息
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        // 具体的生产端消息应答
        Objects.requireNonNull(correlationData);
        Objects.requireNonNull(correlationData.getId());
        String[] collect = correlationData.getId().split("#");
        String messageId = collect[0];
        long sendTime = Long.parseLong(collect[1]);
        String messageType = collect[2];
        if (ack) {
            // broker返回成功，则需要更新一下发送状态
            if (messageType.equals(MessageHolder.MessageType.RELIANT.toString())) {
                messageStoreService.success(messageId);
            }
            System.out.println("send message is ok messageId:" + messageId + " time:" + sendTime);
        } else {
            // 有可能MQ已经满了,或者exchange找不到
            System.out.println("send error");
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("发送返回了。。");
    }
}
