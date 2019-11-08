package com.jsrdxzw.enums;

public enum YesOrNo {
    NO(0, "否"), YES(1, "是");
    public int type;
    public String value;

    YesOrNo(int type, String value) {
        this.type = type;
        this.value = value;
    }
}
