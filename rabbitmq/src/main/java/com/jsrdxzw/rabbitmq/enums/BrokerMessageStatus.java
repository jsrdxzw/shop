package com.jsrdxzw.rabbitmq.enums;

import lombok.Getter;

/**
 * @author xuzhiwei
 * @date 2020/04/11
 */
@Getter
public enum BrokerMessageStatus {
    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2"),

    // 繁忙的时候
    SEND_FAIL_A_MOMENT("3");

    private String code;

    BrokerMessageStatus(String code) {
        this.code = code;
    }
}
