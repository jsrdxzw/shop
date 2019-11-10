package com.jsrdxzw.enums;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 商品评价等级的枚举
 */
public enum CommentLevel {
    GOOD(1, "好评"), NORMAL(2, "中评"), BAD(3, "差评");

    public final int type;
    public final String value;

    CommentLevel(int type, String value) {
        this.type = type;
        this.value = value;
    }
}
