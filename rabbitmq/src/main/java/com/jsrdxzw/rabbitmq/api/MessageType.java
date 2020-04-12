package com.jsrdxzw.rabbitmq.api;

import java.util.Arrays;

public enum MessageType {

    /**
     * 迅速消息：不需要保证消息可靠性，也不需要confirm确认
     */
    RAPID(0),

    /**
     * 确认消息：不需要持久化消息，需要confirm确认
     */
    CONFIRM(1),

    /**
     * 可靠性消息：不允许消息丢失，消息本地持久化
     * 保证数据库和所发消息是原子性（最终一致性）
     */
    RELIANT(2),

    /**
     * 未知类型
     */
    UNKNOWN(3);

    private int code;

    MessageType(int code) {
        this.code = code;
    }

    public static MessageType fromCode(int code) {
        return Arrays.stream(MessageType.values()).filter(it -> it.code == code).findFirst().orElse(UNKNOWN);
    }

    public static MessageType fromName(String name) {
        return Arrays.stream(MessageType.values()).filter(it -> it.name().equals(name)).findFirst().orElse(UNKNOWN);
    }

    public static void main(String[] args) {
        MessageType reliant = MessageType.fromName("RELIANT");
        System.out.println(reliant);
    }

}
