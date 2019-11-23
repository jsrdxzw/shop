package com.jsrdxzw.enums;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/23
 * @Description:
 */
public enum OrderStatusEnum {
    WAIT_PAY(10, "代付款"),
    WAIT_DELIVER(20, "已付款，待发货"),
    WAIT_RECEIVE(30, "已发货，待收货"),
    SUCCESS(40, "交易成功"),
    CLOSE(50, "交易关闭");

    public final int type;
    public final String value;

    OrderStatusEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }
}
