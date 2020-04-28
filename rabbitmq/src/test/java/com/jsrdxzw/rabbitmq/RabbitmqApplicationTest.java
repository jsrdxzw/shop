package com.jsrdxzw.rabbitmq;

import com.jsrdxzw.proto.MessageHolder;
import com.jsrdxzw.rabbitmq.broker.ProducerClient;
import com.jsrdxzw.rabbitmq.entity.BrokerMessage;
import com.jsrdxzw.rabbitmq.enums.BrokerMessageStatus;
import com.jsrdxzw.rabbitmq.repository.BrokerMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class RabbitmqApplicationTest {
    @Autowired
    private ProducerClient producerClient;
    @Autowired
    private BrokerMessageRepository brokerMessageRepository;

    @Test
    public void test_send_ok() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "jsrdxzw");
        MessageHolder.Message message = MessageHolder.Message.newBuilder()
                .setMessageId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setDelayMills(5000)
                .setMessageType(MessageHolder.MessageType.RELIANT)
                .setTopic("exchange-1")
                .setRoutingKey("springboot.jsrdxzw")
                .putAllAttributes(map)
                .build();
        producerClient.send(message);
    }

    @Test
    public void test_send_error() throws InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("name", "jsrdxbt");
        MessageHolder.Message message = MessageHolder.Message.newBuilder()
                .setMessageId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setDelayMills(5000)
                .setMessageType(MessageHolder.MessageType.RELIANT)
                .setTopic("exchange-1")
                .setRoutingKey("aeqew.123")
                .putAllAttributes(map)
                .build();
        producerClient.send(message);
    }

    @Test
    public void test_repo() {
        List<BrokerMessage> brokerMessagesByStatusAndNextRetryBefore = brokerMessageRepository.findBrokerMessagesByStatusAndNextRetryBefore(BrokerMessageStatus.SENDING.getCode(),
                LocalDateTime.now());
        System.out.println(brokerMessagesByStatusAndNextRetryBefore);
    }
}
