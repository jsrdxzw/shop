package com.jsrdxzw.enums;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/23
 * @Description:
 */
public enum PayMethod {
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final int type;
    public final String value;

    PayMethod(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static boolean isValidatePay(int type) {
        return WEIXIN.type == type || ALIPAY.type == type;
    }
}
