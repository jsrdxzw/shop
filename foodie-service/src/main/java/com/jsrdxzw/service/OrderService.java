package com.jsrdxzw.service;

import com.jsrdxzw.bo.ShopCartBO;
import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.vo.OrderVO;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/23
 * @Description:
 */
public interface OrderService {
    /**
     * 创建订单
     *
     *
     * @param shopCartList
     * @param submitOrderBO
     * @return
     */
    OrderVO createOrder(List<ShopCartBO> shopCartList, SubmitOrderBO submitOrderBO);

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
