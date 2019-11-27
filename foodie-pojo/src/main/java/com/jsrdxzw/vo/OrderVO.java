package com.jsrdxzw.vo;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/25
 * @Description:
 */
public class OrderVO {
    private String orderId;
    private MerchantOrderVO merchantOrderVO;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrderVO getMerchantOrderVO() {
        return merchantOrderVO;
    }

    public void setMerchantOrderVO(MerchantOrderVO merchantOrderVO) {
        this.merchantOrderVO = merchantOrderVO;
    }
}
