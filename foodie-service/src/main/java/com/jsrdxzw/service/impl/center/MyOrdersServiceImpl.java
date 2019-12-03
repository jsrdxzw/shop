package com.jsrdxzw.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.jsrdxzw.enums.OrderStatusEnum;
import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.mapper.OrderStatusMapper;
import com.jsrdxzw.mapper.OrdersMapper;
import com.jsrdxzw.mapper.OrdersMapperCustom;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.service.center.MyOrdersService;
import com.jsrdxzw.service.impl.BaseService;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.MyOrdersVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {

    private final OrdersMapperCustom ordersMapperCustom;
    private final OrderStatusMapper orderStatusMapper;
    private final OrdersMapper ordersMapper;

    public MyOrdersServiceImpl(OrdersMapperCustom ordersMapperCustom, OrderStatusMapper orderStatusMapper, OrdersMapper ordersMapper) {
        this.ordersMapperCustom = ordersMapperCustom;
        this.orderStatusMapper = orderStatusMapper;
        this.ordersMapper = ordersMapper;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("orderStatus", orderStatus);
        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(paramsMap);
        return setterPagedGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByExampleSelective(orderStatus, example);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectByPrimaryKey(orders);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        return orderStatusMapper.updateByExampleSelective(orderStatus, example) == 1;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);
        return ordersMapper.updateByExampleSelective(orders, example) == 1;
    }

}
