package com.jsrdxzw.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

/**
 * @author xuzhiwei
 * @date 2020/04/06
 */
public class ProtoUtils {
    public static String toJson(Message message) {
        String result = "";
        try {
            result = JsonFormat.printer().includingDefaultValueFields().print(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T extends Message.Builder, K extends Message> K parseJson(T builder, String json) {
        JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();
        try {
            parser.merge(json, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return (K) builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        MessageHolder.Message message = MessageHolder.Message.newBuilder()
                .setMessageType(MessageHolder.MessageType.CONFIRM)
                .setMessageId("123")
                .setTopic("com.jsrdxzw")
                .setRoutingKey("hello")
                .build();
        byte[] bytes = message.toByteArray();
        MessageHolder.Message deserialize = MessageHolder.Message.parseFrom(bytes);
        System.out.println(toJson(deserialize));
    }
}
