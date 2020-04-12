package com.jsrdxzw.rabbitmq.api;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -1316477823317407151L;

    @Builder.Default
    private String messageId = UUID.randomUUID().toString();

    @NonNull
    private String topic;

    private String routingKey;

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 延迟消息的参数配置，可以使用rabbitmq的插件
     */
    private int delayMills;

    /**
     * 消息类型
     */
    @Builder.Default
    private MessageType messageType = MessageType.CONFIRM;

}
