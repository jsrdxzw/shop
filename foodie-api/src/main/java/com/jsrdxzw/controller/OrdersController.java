package com.jsrdxzw.controller;

import com.jsrdxzw.bo.ShopCartBO;
import com.jsrdxzw.bo.SubmitOrderBO;
import com.jsrdxzw.enums.OrderStatusEnum;
import com.jsrdxzw.enums.PayMethod;
import com.jsrdxzw.pojo.OrderStatus;
import com.jsrdxzw.service.OrderService;
import com.jsrdxzw.utils.CookieUtils;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import com.jsrdxzw.utils.RedisOperator;
import com.jsrdxzw.vo.MerchantOrderVO;
import com.jsrdxzw.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private final RedisOperator redisOperator;

    @Autowired
    private RedissonClient redissonClient;

    public OrdersController(OrderService orderService, RestTemplate restTemplate, RedisOperator redisOperator) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "获取订单token", notes = "获取订单token", httpMethod = "GET")
    @GetMapping("/getOrderToken")
    public JSONResult getOrderToken(HttpSession session) {
        String token = UUID.randomUUID().toString();
        // 一个浏览器对应一个sessionID，其实就是JSESSIONID
        redisOperator.set("ORDER_TOKEN_" + session.getId(), token, 600);
        return JSONResult.ok(session.getId());
    }

    /**
     * 防止重复下单，需要做接口幂等性处理
     *
     * @param submitOrderBO
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {

        // 接口幂等性，防止多次提交
        String orderTokenKey = "ORDER_TOKEN_" + request.getSession().getId();
        String lockKey = "ORDER_LOCK_" + request.getSession().getId();

        RLock lock = redissonClient.getLock(lockKey);
        // 一直会等待直到过期时间30s到，会自动释放锁
        lock.lock(30, TimeUnit.SECONDS);

        // 防止并发，这里需要锁住
        try {
            String orderToken = redisOperator.get(orderTokenKey);
            if (StringUtils.isBlank(orderToken)) {
                return JSONResult.errorMsg("orderToken不存在");
            }
            if (!orderToken.equals(submitOrderBO.getToken())) {
                return JSONResult.errorMsg("orderToken不正确");
            }
            // 第一次成功，要删除token
            redisOperator.del(orderTokenKey);
        } finally {
            lock.unlock();
        }

        //订单逻辑..
        if (!PayMethod.isValidatePay(submitOrderBO.getPayMethod())) {
            return JSONResult.errorMsg("支付方法不支持");
        }
        String key = FOODIE_SHOPCART + ":" + submitOrderBO.getUserId();
        String shopCartJson = redisOperator.get(key);
        if (StringUtils.isBlank(shopCartJson)) {
            return JSONResult.errorMsg("购物车没有内容");
        }
        // 用户购物车的所有数据
        List<ShopCartBO> shopCartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);

        // 创建订单
        OrderVO orderVO = orderService.createOrder(shopCartList, submitOrderBO);
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
        // 整合redis之后完善购物车中的商品，并且同步到前端Cookie
        // 清理已经创建订单的购物车数据
        shopCartList.removeAll(orderVO.getToBeRemovedShopCartList());
        String value = JsonUtils.objectToJson(shopCartList);
        redisOperator.set(key, value);
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, value, true);

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
