package com.jsrdxzw.vo;

import com.jsrdxzw.bo.ShopCartBO;
import lombok.Data;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/25
 * @Description:
 */
@Data
public class OrderVO {
    private String orderId;
    private MerchantOrderVO merchantOrderVO;
    private List<ShopCartBO> toBeRemovedShopCartList;
}
