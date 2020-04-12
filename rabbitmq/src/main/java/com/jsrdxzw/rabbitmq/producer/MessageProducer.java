package com.jsrdxzw.rabbitmq.producer;

import com.jsrdxzw.proto.MessageHolder;

import java.util.List;

public interface MessageProducer {

    /**
     * 发送消息
     *
     * @param message
     */
    void send(MessageHolder.Message message);

    /**
     * 批量发送messages
     *
     * @param messages
     */
    void send(List<MessageHolder.Message> messages);

    /**
     * 发送消息
     *
     * @param message
     * @param sendCallback 回调
     */
    void send(MessageHolder.Message message, SendCallback sendCallback);

    /**
     * 批量发送messages
     *
     * @param messages
     * @param sendCallback 回调
     */
    default void send(List<MessageHolder.Message> messages, SendCallback sendCallback) {
        try {
            send(messages);
            sendCallback.onSuccess();
        } catch (Exception e) {
            sendCallback.onFailure();
        }
    }
}
