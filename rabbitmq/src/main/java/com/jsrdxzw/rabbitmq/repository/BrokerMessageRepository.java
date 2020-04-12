package com.jsrdxzw.rabbitmq.repository;

import com.jsrdxzw.rabbitmq.entity.BrokerMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xuzhiwei
 * @date 2020/04/11
 */
public interface BrokerMessageRepository extends JpaRepository<BrokerMessage, String> {

    /**
     * 满足重发条件的message
     *
     * @param status
     * @param time
     * @return
     */
    List<BrokerMessage> findBrokerMessagesByStatusAndNextRetryBefore(String status, LocalDateTime time);
}
