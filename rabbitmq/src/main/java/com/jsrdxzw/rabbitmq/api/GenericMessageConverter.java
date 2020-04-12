package com.jsrdxzw.rabbitmq.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jsrdxzw.proto.MessageHolder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author xuzhiwei
 * @date 2020/04/06
 */
@Component
public class GenericMessageConverter implements MessageConverter {

    private static final String DEFAULT_EXPIRE = String.valueOf(24 * 60 * 60 * 1000);

    /**
     * serialize
     *
     * @param object
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @NonNull
    @Override
    public Message toMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) throws MessageConversionException {
        MessageHolder.Message body = (MessageHolder.Message) object;
        messageProperties.setExpiration(DEFAULT_EXPIRE);
        return new Message(body.toByteArray(), messageProperties);
    }

    /**
     * deserialize
     *
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message) throws MessageConversionException {
        byte[] bytes = message.getBody();
        try {
            return MessageHolder.Message.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new MessageConversionException("message deserialize error " + e);
        }
    }
}
