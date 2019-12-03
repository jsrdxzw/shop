package com.jsrdxzw.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.jsrdxzw.bo.center.OrderItemsCommentBO;
import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.mapper.ItemsCommentsMapperCustom;
import com.jsrdxzw.mapper.OrderItemsMapper;
import com.jsrdxzw.mapper.OrderStatusMapper;
import com.jsrdxzw.mapper.OrdersMapper;
import com.jsrdxzw.pojo.OrderItems;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.service.center.MyCommentsService;
import com.jsrdxzw.service.impl.BaseService;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.MyCommentVO;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    private final OrderItemsMapper orderItemsMapper;
    private final ItemsCommentsMapperCustom commentsMapperCustom;
    private final OrdersMapper ordersMapper;
    private final OrderStatusMapper orderStatusMapper;
    private final Sid sid;

    public MyCommentsServiceImpl(OrderItemsMapper orderItemsMapper, ItemsCommentsMapperCustom commentsMapperCustom, OrdersMapper ordersMapper, OrderStatusMapper orderStatusMapper, Sid sid) {
        this.orderItemsMapper = orderItemsMapper;
        this.commentsMapperCustom = commentsMapperCustom;
        this.ordersMapper = ordersMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.sid = sid;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComments(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        for (OrderItemsCommentBO comment : commentList) {
            comment.setCommentId(sid.nextShort());
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("commentList", commentList);
        commentsMapperCustom.saveComments(map);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>(1);
        paramsMap.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> result = commentsMapperCustom.queryMyComments(paramsMap);
        return setterPagedGrid(result, page);
    }
}
