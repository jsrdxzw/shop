package com.jsrdxzw.rabbitmq.service;

import com.jsrdxzw.rabbitmq.entity.BrokerMessage;
import com.jsrdxzw.rabbitmq.enums.BrokerMessageStatus;
import com.jsrdxzw.rabbitmq.repository.BrokerMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xuzhiwei
 * @date 2020/04/11
 */
@Service
public class MessageStoreService {
    @Autowired
    private BrokerMessageRepository brokerMessageRepository;

    public BrokerMessage insert(BrokerMessage brokerMessage) {
        return brokerMessageRepository.save(brokerMessage);
    }

    public void update(BrokerMessage brokerMessage) {
        brokerMessageRepository.save(brokerMessage);
    }

    public BrokerMessage findById(String messageId) {
        return brokerMessageRepository.findById(messageId).orElse(null);
    }

    public void success(String messageId) {
        brokerMessageRepository.findById(messageId).ifPresent(it -> {
            it.setStatus(BrokerMessageStatus.SEND_OK.getCode());
            brokerMessageRepository.save(it);
        });
    }

    public void failure(String messageId) {
        brokerMessageRepository.findById(messageId).ifPresent(it -> {
            it.setStatus(BrokerMessageStatus.SEND_FAIL.getCode());
            brokerMessageRepository.save(it);
        });
    }

    public List<BrokerMessage> findMayRetryMessage() {
        return brokerMessageRepository.findBrokerMessagesByStatusAndNextRetryBefore(
                BrokerMessageStatus.SENDING.getCode(),
                LocalDateTime.now()
        );
    }
}
