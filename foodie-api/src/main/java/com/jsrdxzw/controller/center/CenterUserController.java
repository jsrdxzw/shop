package com.jsrdxzw.controller.center;

import com.jsrdxzw.bo.center.CenterUserBO;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.center.CenterUserService;
import com.jsrdxzw.utils.CookieUtils;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserController {

    private final CenterUserService centerUserService;

    public CenterUserController(CenterUserService centerUserService) {
        this.centerUserService = centerUserService;
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(@RequestParam String userId, @RequestBody @Valid CenterUserBO centerUserBO,
                             BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }
        ShopUser user = centerUserService.updateUserInfo(userId, centerUserBO);
        ShopUser safeUser = userDataMasking(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(safeUser), true);
        // TODO 后续整合redis做分布式会话
        return JSONResult.ok();
    }

    private ShopUser userDataMasking(ShopUser user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
        user.setRealname(null);
        return user;
    }

    private Map<String, String> getErrors(BindingResult result) {
        List<FieldError> errorList = result.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : errorList) {
            // 发生验证错误对应的属性
            String field = fieldError.getField();
            // 错误信息
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        }
        return map;
    }
}
