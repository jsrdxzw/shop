package com.jsrdxzw.controller;

import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.center.MyOrdersService;
import com.jsrdxzw.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";

    @Autowired
    private MyOrdersService myOrdersService;

    /**
     * 支付中心的地址
     */
    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    /**
     * 微信支付成功回调url,需要使用内网穿透访问本地
     */
    public static final String PAY_RETURN_URL = "http://5vc9db.natappfree.cc/orders/notifyMerchantOrderPaid";

    protected ShopUser userDataMasking(ShopUser user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
        user.setRealname(null);
        return user;
    }

    /**
     * 用于验证用户和订单是否有关联
     *
     * @return
     */
    protected JSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok(orders);
    }

//    public static final String IMAGE_USER_FACE_LOCATION = "/Users/xuzhiwei/开发文件/foodie/".replaceAll("/", File.separator);

//    public static final String[] UPDATE_IMAGE_FORMAT = {"png", "jpg", "jpeg"};
}
