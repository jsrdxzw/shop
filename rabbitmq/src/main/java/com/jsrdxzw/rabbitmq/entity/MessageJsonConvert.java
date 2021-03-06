package com.jsrdxzw.rabbitmq.entity;

import com.jsrdxzw.proto.MessageHolder;
import com.jsrdxzw.proto.ProtoUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author xuzhiwei
 * @date 2020/04/12
 */
@Converter
public class MessageJsonConvert implements AttributeConverter<MessageHolder.Message, String> {
    @Override
    public String convertToDatabaseColumn(MessageHolder.Message message) {
        return ProtoUtils.toJson(message);
    }

    @Override
    public MessageHolder.Message convertToEntityAttribute(String s) {
        return ProtoUtils.parseJson(MessageHolder.Message.newBuilder(), s);
    }
}
