package com.jsrdxzw.service.center;

import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.OrderStatusCountVO;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 订单状态，商家发货
     *
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的order, 确保该用户下该订单是有效的
     *
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态确认收货
     *
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);

    /**
     * 查询用户订单数
     *
     * @param userId
     * @return
     */
    OrderStatusCountVO getOrderStatusCount(String userId);

    /**
     * 获得分页的订单动向
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getMyOrderTrend(String userId, int page, int pageSize);
}
