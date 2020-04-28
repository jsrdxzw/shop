package com.jsrdxzw.rabbitmq.broker;

import com.jsrdxzw.proto.MessageHolder;
import com.jsrdxzw.rabbitmq.entity.BrokerMessage;
import com.jsrdxzw.rabbitmq.enums.BrokerMessageStatus;
import com.jsrdxzw.rabbitmq.service.MessageStoreService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xuzhiwei
 * @date 2020/04/05
 */
@Async("rabbitmq_client_sender")
@Component
// TODO 使用rabbitMq的原生异步去操作
public class RabbitMQSenderAsyncTask {

    public static final int INTERVAL_MINUTES = 1;

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    public void rapidSend(MessageHolder.Message message) {
        sendKernel(message);
    }

    public void confirmSend(MessageHolder.Message message) {
        sendKernel(message);
    }

    /**
     * 可靠性发送
     * 不允许消息丢失
     *
     * @param message
     */
    public void reliantSend(MessageHolder.Message message) {

        BrokerMessage bm = messageStoreService.findById(message.getMessageId());
        if (bm == null) {
            LocalDateTime now = LocalDateTime.now();
            // tryCount在最开始发送的时候不需要设置
            BrokerMessage brokerMessage = BrokerMessage.builder()
                    .messageId(message.getMessageId())
                    .status(BrokerMessageStatus.SENDING.getCode())
                    .nextRetry(now.plusMinutes(INTERVAL_MINUTES))
                    .message(message).build();
            messageStoreService.insert(brokerMessage);
        }
        // 真正开始发送消息
        sendKernel(message);
    }

    public void sendKernel(MessageHolder.Message message) {
        RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
        // 添加基于protobuf的序列化和反序列化
        CorrelationData correlationData = new CorrelationData(String.format("%s#%s#%s", message.getMessageId(), System.currentTimeMillis(), message.getMessageType()));
        rabbitTemplate.convertAndSend(message.getTopic(), message.getRoutingKey(), message, correlationData);
    }
}
