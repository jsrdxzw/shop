package com.jsrdxzw.service.center;

import com.jsrdxzw.bo.center.OrderItemsCommentBO;
import com.jsrdxzw.pojo.OrderItems;
import com.jsrdxzw.utils.PagedGridResult;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的待评价商品
     *
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComments(String orderId);

    /**
     * 保存用户的订单评价信息
     *
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    /**
     * 查询我的评价分页
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
