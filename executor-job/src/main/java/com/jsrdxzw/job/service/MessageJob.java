package com.jsrdxzw.job.service;

import com.jsrdxzw.rabbitmq.broker.RabbitMQSenderAsyncTask;
import com.jsrdxzw.rabbitmq.entity.BrokerMessage;
import com.jsrdxzw.rabbitmq.service.MessageStoreService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xuzhiwei
 * @date 2020/04/12
 */
@Component
public class MessageJob {

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitMQSenderAsyncTask rabbitMQSenderAsyncTask;

    public static final int MAX_RETRY_COUNT = 3;

    /**
     * 可靠性消息的补偿任务,和rabbitmq做可靠性消息投递
     *
     * @param param
     * @return
     */
    @XxlJob("retryMessage")
    public ReturnT<String> retryMessage(String param) {
        List<BrokerMessage> messages = messageStoreService.findMayRetryMessage();
        XxlJobLogger.log("抓取数据集合:{}", messages);
        // 需要去重试
        messages.forEach(message -> {
            if (message.getTryCount() >= MAX_RETRY_COUNT) {
                messageStoreService.failure(message.getMessageId());
                XxlJobLogger.log("消息重试最终失败:{}", message);
            } else {
                // 更新tryCount
                message.setTryCount(message.getTryCount() + 1);
                message.setNextRetry(message.getNextRetry().plusMinutes(1));
                XxlJobLogger.log("消息重试更新:{}", message);
                messageStoreService.update(message);
                rabbitMQSenderAsyncTask.sendKernel(message.getMessage());
            }
        });
        return ReturnT.SUCCESS;
    }
}
