package com.jsrdxzw.controller;

import com.jsrdxzw.bo.ShopCartBO;
import com.jsrdxzw.bo.UserBO;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.UserService;
import com.jsrdxzw.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description:
 */
@Api(value = "注册登录", tags = {"用于注册和登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final UserService userService;
    private final RedisOperator redisOperator;

    public PassportController(UserService userService, RedisOperator redisOperator) {
        this.userService = userService;
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "用户名是否已经存在", notes = "用户名是否已经存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExists(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        } else {
            return JSONResult.ok();
        }
    }

    @ApiOperation(value = "用户名注册", notes = "用户名注册", httpMethod = "POST")
    @PostMapping("/register")
    public JSONResult register(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPwd)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return JSONResult.errorMsg("密码长度不能小于" + MIN_PASSWORD_LENGTH);
        }
        if (!password.equals(confirmPwd)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }
        ShopUser user = userService.createUser(userBO);
        ShopUser result = userDataMasking(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        ShopUser user = userService.queryUserForLogin(username, MD5Util.getMD5Str(password));
        if (user == null) {
            return JSONResult.errorMsg("用户名或密码不正确");
        }
        ShopUser result = userDataMasking(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);
        // TODO 生成用户token，存入redis会话
        // 同步购物车数据
        syncRedisShopCart(user.getId(), request, response);
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");
        return JSONResult.ok();
    }

    private void syncRedisShopCart(String userId, HttpServletRequest request, HttpServletResponse response) {
        String key = FOODIE_SHOPCART + ":" + userId;
        String redisShopCartJson = redisOperator.get(key);
        String cookieShopCartJson = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
        if (StringUtils.isBlank(redisShopCartJson)) {
            if (StringUtils.isNotBlank(cookieShopCartJson)) {
                redisOperator.set(key, cookieShopCartJson);
            }
        } else {
            // 合并cookie和redis的购物车数据
            if (StringUtils.isNotBlank(cookieShopCartJson)) {
                // 已经存在的把cookie对应的数量覆盖到redis中（京东）
                List<ShopCartBO> shopCartRedisList = JsonUtils.jsonToList(redisShopCartJson, ShopCartBO.class);
                List<ShopCartBO> shopCartCookieList = JsonUtils.jsonToList(cookieShopCartJson, ShopCartBO.class);
                if (shopCartRedisList == null || shopCartCookieList == null) {
                    return;
                }
                // 相同的商品删除list，用于最后的合并
                List<ShopCartBO> pendingDeleteList = new ArrayList<>();

                for (ShopCartBO redisShopCart : shopCartRedisList) {
                    String redisSpecId = redisShopCart.getSpecId();
                    for (ShopCartBO cookieShopCart : shopCartCookieList) {
                        String cookieSpecId = cookieShopCart.getSpecId();
                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量
                            redisShopCart.setBuyCounts(cookieShopCart.getBuyCounts());
                            pendingDeleteList.add(cookieShopCart);
                        }
                    }
                }
                shopCartCookieList.removeAll(pendingDeleteList);
                shopCartRedisList.addAll(shopCartCookieList);
                String json = JsonUtils.objectToJson(shopCartRedisList);
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, json, true);
                redisOperator.set(key, json);
            } else {
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, redisShopCartJson, true);
            }
        }
    }

}
