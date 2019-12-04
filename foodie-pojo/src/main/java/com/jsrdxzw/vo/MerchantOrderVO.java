package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/25
 * @Description:
 */
@Data
public class MerchantOrderVO {
    private String merchantOrderId;
    private String merchantUserId;
    private Integer amount;
    private Integer payMethod;
    private String returnUrl;
}
