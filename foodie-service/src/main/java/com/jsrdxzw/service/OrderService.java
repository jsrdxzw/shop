package com.jsrdxzw.service;

import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.vo.OrderVO;

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
    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();
}
