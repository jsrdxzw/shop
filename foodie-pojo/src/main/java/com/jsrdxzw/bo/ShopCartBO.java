package com.jsrdxzw.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/12
 * @Description:
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopCartBO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
