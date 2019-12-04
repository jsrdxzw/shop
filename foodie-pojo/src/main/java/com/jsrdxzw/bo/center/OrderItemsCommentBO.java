package com.jsrdxzw.bo.center;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
@Data
public class OrderItemsCommentBO {
    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;
}
