package com.jsrdxzw.controller;

import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.center.MyOrdersService;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.RedisOperator;
import com.jsrdxzw.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private MyOrdersService myOrdersService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 支付中心的地址
     */
    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    /**
     * 微信支付成功回调url,需要使用内网穿透访问本地
     */
    public static final String PAY_RETURN_URL = "http://5vc9db.natappfree.cc/orders/notifyMerchantOrderPaid";

    /**
     * 返回User对象，同步token到redis
     *
     * @return
     */
    protected UsersVO convertToUsersVO(ShopUser user) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN + ":" + usersVO.getId(), uniqueToken);
        return usersVO;
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
