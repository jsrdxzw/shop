package com.jsrdxzw.service.impl;

import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.enums.OrderStatusEnum;
import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.mapper.OrderItemsMapper;
import com.jsrdxzw.mapper.OrderStatusMapper;
import com.jsrdxzw.mapper.OrdersMapper;
import com.jsrdxzw.pojo.*;
import com.jsrdxzw.service.AddressService;
import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/23
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersMapper ordersMapper;
    private final AddressService addressService;
    private final ItemService itemService;
    private final OrderItemsMapper orderItemsMapper;
    private final OrderStatusMapper orderStatusMapper;
    private final Sid sid;

    public OrderServiceImpl(OrdersMapper ordersMapper, AddressService addressService, ItemService itemService, OrderItemsMapper orderItemsMapper, OrderStatusMapper orderStatusMapper, Sid sid) {
        this.ordersMapper = ordersMapper;
        this.addressService = addressService;
        this.itemService = itemService;
        this.orderItemsMapper = orderItemsMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.sid = sid;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer postAmount = 0;

        UserAddress address = addressService.queryUserAddress(userId, addressId);

        // 1.新订单数据的保存
        Orders order = new Orders();
        String orderId = sid.nextShort();
        order.setId(orderId);
        order.setUserId(submitOrderBO.getUserId());
        order.setReceiverName(address.getReceiver());
        order.setReceiverMobile(address.getMobile());
        order.setReceiverAddress(address.getProvince() + " " + address.getCity() + " " + address.getDistrict() + " " + address.getDetail());
        // order.setTotalAmount();
        // order.setRealPayAmount();
        order.setPostAmount(postAmount);
        order.setPayMethod(payMethod);
        // 是否评价
        order.setIsComment(YesOrNo.NO.type);
        order.setIsDelete(YesOrNo.NO.type);
        order.setCreatedTime(new Date());
        order.setUpdatedTime(new Date());
        order.setLeftMsg(leftMsg);

        // 2.循环保存订单商品
        // 原价
        int totalAmount = 0;
        // 实际支付
        int realPayAmount = 0;
        for (String specId : itemSpecIds.split(",")) {
            // TODO 整合redis后重新从redis中获取
            int buyCount = 1;
            // 商品规格信息
            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCount;

            String itemId = itemsSpec.getItemId();
            // 商品信息
            Items items = itemService.queryItemById(itemId);
            String mainImg = itemService.queryItemMainImgById(itemId);

            // 循环保存子订单数据,这里防止订单以后会变动，所以保存的是order的商品快照
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(sid.nextShort());
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(items.getItemName());
            subOrderItem.setItemImg(mainImg);
            subOrderItem.setBuyCounts(buyCount);
            subOrderItem.setItemSpecId(specId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 扣除库存
            itemService.decreaseItemStock(specId, buyCount);
        }

        order.setTotalAmount(totalAmount);
        order.setRealPayAmount(realPayAmount);
        ordersMapper.insert(order);

        // 保存订单状态
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        return orderId;
    }
}
