package com.jsrdxzw.rabbitmq.broker;

import com.jsrdxzw.proto.MessageHolder;
import com.jsrdxzw.rabbitmq.api.MessageType;
import com.jsrdxzw.rabbitmq.producer.MessageProducer;
import com.jsrdxzw.rabbitmq.producer.SendCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xuzhiwei
 * @date 2020/04/05
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitMQSenderAsyncTask asyncTask;

    @Override
    public void send(MessageHolder.Message message) {
        MessageType messageType = MessageType.fromName(message.getMessageType().name());
        switch (messageType) {
            case RAPID:
                asyncTask.rapidSend(message);
                break;
            case CONFIRM:
                asyncTask.confirmSend(message);
                break;
            case RELIANT:
                asyncTask.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<MessageHolder.Message> messages) {

    }

    @Override
    public void send(MessageHolder.Message message, SendCallback sendCallback) {

    }


}
