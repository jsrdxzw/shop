package com.jsrdxzw.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 用于展示商品评价的VO
 */
@Data
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
