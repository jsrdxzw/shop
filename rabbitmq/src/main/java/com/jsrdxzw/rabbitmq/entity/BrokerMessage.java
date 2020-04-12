package com.jsrdxzw.rabbitmq.entity;

import com.jsrdxzw.proto.MessageHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xuzhiwei
 * @date 2020/04/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "broker_message", indexes = {
        @Index(name = "status_nextRetry", columnList = "status,nextRetry")
})
public class BrokerMessage {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String messageId;

    @Convert(converter = MessageJsonConvert.class)
    @Column(nullable = false, length = 1000)
    private MessageHolder.Message message;

    @Builder.Default
    private Integer tryCount = 0;

    @Builder.Default
    private String status = "";

    @Column(nullable = false)
    private LocalDateTime nextRetry;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

}
