package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/11
 * @Description: 用于展示商品搜索列表的VO
 */
@Data
public class searchItemsVO {
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;

    /**
     * 涉及金额最好以分为单位
     */
    private Integer price;
}
