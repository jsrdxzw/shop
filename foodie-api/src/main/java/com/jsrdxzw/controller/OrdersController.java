package com.jsrdxzw.controller;

import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.enums.OrderStatusEnum;
import com.jsrdxzw.enums.PayMethod;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.service.OrderService;
import com.jsrdxzw.utils.CookieUtils;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.vo.MerchantOrderVO;
import com.jsrdxzw.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate;

    public OrdersController(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        if (!PayMethod.isValidatePay(submitOrderBO.getPayMethod())) {
            return JSONResult.errorMsg("支付方法不支持");
        }
        // 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        MerchantOrderVO merchantOrderVO = orderVO.getMerchantOrderVO();
        merchantOrderVO.setReturnUrl(PAY_RETURN_URL);
        // 方便测试，所有支付改为1分钱
        merchantOrderVO.setAmount(1);

        // 向支付中心发送创建订单请求
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId", "6213108-909734628");
        httpHeaders.add("password", "3a6e-wo0f-19tg-pk0a");
        HttpEntity<MerchantOrderVO> httpEntity = new HttpEntity<>(merchantOrderVO, httpHeaders);

        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(PAYMENT_URL, httpEntity, JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult == null || HttpStatus.OK.value() != paymentResult.getStatus()) {
            return JSONResult.errorMsg("支付中心订单创建失败");
        }

        // 移除购物车中已经提交的商品
        // TODO 整合redis之后完善购物车中的商品，并且同步到前端
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);


        return JSONResult.ok(orderVO.getOrderId());
    }

    /**
     * 支付中心支付成功后的回调api
     *
     * @param merchantOrderId
     * @return
     */
    @PostMapping("/notifyMerchantOrderPaid")
    public HttpStatus notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK;
    }

    @PostMapping("/getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {
        OrderStatus status = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(status);
    }
}
