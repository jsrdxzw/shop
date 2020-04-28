package com.jsrdxzw.service.impl;

import com.jsrdxzw.bo.ShopCartBO;
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
import com.jsrdxzw.utils.DateUtil;
import com.jsrdxzw.vo.MerchantOrderVO;
import com.jsrdxzw.vo.OrderVO;
import org.n3r.idworker.Sid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public OrderVO createOrder(List<ShopCartBO> shopCartList, SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 邮费设置为0
        int postAmount = 0;

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
        List<ShopCartBO> tobeRemovedShopCartList = new ArrayList<>();
        for (String specId : itemSpecIds.split(",")) {
            ShopCartBO shopCartBO = getBuyCounts(shopCartList, specId);
            tobeRemovedShopCartList.add(shopCartBO);
            int buyCount = shopCartBO.getBuyCounts();

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

        // 构建商户订单，传给支付中心
        MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
        merchantOrderVO.setMerchantOrderId(orderId);
        merchantOrderVO.setMerchantUserId(userId);
        merchantOrderVO.setAmount(realPayAmount + postAmount);
        merchantOrderVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrderVO(merchantOrderVO);
        orderVO.setToBeRemovedShopCartList(tobeRemovedShopCartList);

        return orderVO;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus status = new OrderStatus();
        status.setOrderId(orderId);
        status.setOrderStatus(orderStatus);
        status.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(status);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        //判断时间是否超时，一天则关闭交易
        OrderStatus status = new OrderStatus();
        status.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(status);
        for (OrderStatus orderStatus : list) {
            Date createdTime = orderStatus.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                doCloseOrder(orderStatus.getOrderId());
            }
        }
    }

    private void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }

    /**
     * 从redis获取商品数目
     * @param shopCartList
     * @param specId
     * @return
     */
    @NonNull
    private ShopCartBO getBuyCounts(List<ShopCartBO> shopCartList, String specId) {
        for (ShopCartBO shopCart : shopCartList) {
            if (shopCart.getSpecId().equals(specId)) {
                return shopCart;
            }
        }
        throw new RuntimeException("商品数目肯定有");
    }
}
