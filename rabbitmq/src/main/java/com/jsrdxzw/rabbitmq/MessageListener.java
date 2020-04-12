package com.jsrdxzw.rabbitmq;

import com.jsrdxzw.rabbitmq.api.Message;

public interface MessageListener {

    /**
     * 监听函数
     *
     * @param message 消息
     */
    void onMessage(Message message);
}
