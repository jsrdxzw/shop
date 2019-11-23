package com.jsrdxzw.service;

import com.jsrdxzw.bo.SubmitOrderBO;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/23
 * @Description:
 */
public interface OrderService {
    /**
     * 创建订单
     *
     * @param submitOrderBO
     * @return
     */
    String createOrder(SubmitOrderBO submitOrderBO);
}
