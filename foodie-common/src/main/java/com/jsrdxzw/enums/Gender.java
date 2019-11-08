package com.jsrdxzw.enums;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description: 用户的性别
 */
public enum Gender {
    WOMAN(0, "女"), MAN(1, "男"), UNKNOWN(2, "保密");

    private final int type;
    private final String value;

    Gender(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
