package com.jsrdxzw.controller;

import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.enums.OrderStatusEnum;
import com.jsrdxzw.enums.PayMethod;
import com.jsrdxzw.service.OrderService;
import com.jsrdxzw.utils.CookieUtils;
import com.jsrdxzw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/22
 * @Description:
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("/orders")
@RestController
public class OrdersController extends BaseController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        if (!PayMethod.isValidatePay(submitOrderBO.getPayMethod())) {
            return JSONResult.errorMsg("支付方法不支持");
        }
        // 创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        // 移除购物车中已经提交的商品
        // TODO 整合redis之后完善购物车中的商品，并且同步到前端
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        return JSONResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public HttpStatus notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK;
    }
}
