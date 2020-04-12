package com.jsrdxzw.rabbitmq.api;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 装饰器模式
 *
 * @author xuzhiwei
 * @date 2020/04/06
 */
@Component
public class RabbitMessageConvert implements MessageConverter {

    @Autowired
    private GenericMessageConverter genericMessageConverter;

    public static final String DEFAULT_EXPIRE = String.valueOf(24 * 60 * 60 * 1000);

    @NonNull
    @Override
    public Message toMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) throws MessageConversionException {
        messageProperties.setExpiration(DEFAULT_EXPIRE);
        return genericMessageConverter.toMessage(object, messageProperties);
    }

    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message) throws MessageConversionException {
        return genericMessageConverter.fromMessage(message);
    }
}
