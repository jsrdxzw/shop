package com.jsrdxzw.controller;

import com.jsrdxzw.bo.ShopCartBO;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import com.jsrdxzw.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/12
 * @Description:
 */
@Api(value = "购物车接口", tags = {"购物车接口相关的api"})
@RequestMapping("/shopcart")
@RestController
public class ShopCartController extends BaseController {

    private final RedisOperator redisOperator;

    public ShopCartController(RedisOperator redisOperator) {
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam String userId,
            @RequestBody ShopCartBO shopCartBO
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        // 同步购物车数据到redis缓存
        // 需要判断当前购物车存在的商品，如果存在则加1
        String key = FOODIE_SHOPCART + ":" + userId;
        String shopCartJson = redisOperator.get(key);
        List<ShopCartBO> shopCartList = new ArrayList<>();
        if (StringUtils.isNotBlank(shopCartJson)) {
            // 该user下已经有购物车了
            shopCartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            if (shopCartList == null) {
                return JSONResult.ok();
            }
            // 判断是否已经有了该商品
            boolean isHave = false;
            for (ShopCartBO shopCart : shopCartList) {
                if (shopCart.getSpecId().equals(shopCartBO.getSpecId())) {
                    shopCart.setBuyCounts(shopCart.getBuyCounts() + shopCartBO.getBuyCounts());
                    isHave = true;
                }
            }
            if (!isHave) {
                shopCartList.add(shopCartBO);
            }
        } else {
            shopCartList.add(shopCartBO);
        }
        redisOperator.set(key, JsonUtils.objectToJson(shopCartList));
        return JSONResult.ok();
    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "GET")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        String key = FOODIE_SHOPCART + ":" + userId;
        String shopCartJson = redisOperator.get(key);
        List<ShopCartBO> shopCartList;
        if (StringUtils.isNotBlank(shopCartJson)) {
            shopCartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            if (shopCartList == null) {
                return JSONResult.ok();
            }
            for (ShopCartBO shopCart : shopCartList) {
                if (shopCart.getSpecId().equals(itemSpecId)) {
                    shopCartList.remove(shopCart);
                    break;
                }
            }
            redisOperator.set(key, JsonUtils.objectToJson(shopCartList));
        }
        return JSONResult.ok();
    }

}

