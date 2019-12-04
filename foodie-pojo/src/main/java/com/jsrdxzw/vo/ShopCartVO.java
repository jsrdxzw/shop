package com.jsrdxzw.vo;


import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/14
 * @Description:
 */
@Data
public class ShopCartVO {
    private String itemId;
    private String itemName;
    private String itemImgUrl;
    private String specId;
    private String specName;
    private Integer priceDiscount;
    private Integer priceNormal;
}
