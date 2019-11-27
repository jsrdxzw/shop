package com.jsrdxzw.controller;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
public class BaseController {
    static final Integer COMMENT_PAGE_SIZE = 10;
    static final Integer PAGE_SIZE = 20;
    static final String FOODIE_SHOPCART = "shopcart";

    /**
     * 支付中心的地址
     */
    static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    /**
     * 微信支付成功回调url,需要使用内网穿透访问本地
     */
    static final String PAY_RETURN_URL = "http://5vc9db.natappfree.cc/orders/notifyMerchantOrderPaid";
}
