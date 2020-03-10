package com.jsrdxzw.vo;

import lombok.Data;

@Data
public class UsersVO {
    private String id;
    private String username;
    private String nickname;
    private String face;
    private Integer sex;
    private String userUniqueToken;
}