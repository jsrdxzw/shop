package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
@Data
public class MySubOrdersItemVO {
    private String itemId;
    private String itemName;
    private String itemImg;
    private String itemSpecId;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
