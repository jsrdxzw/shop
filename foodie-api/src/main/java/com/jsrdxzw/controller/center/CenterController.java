package com.jsrdxzw.controller.center;

import com.jsrdxzw.service.center.CenterUserService;
import com.jsrdxzw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
@Api(value = "center-用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("/center")
public class CenterController {

    private final CenterUserService centerUserService;

    public CenterController(CenterUserService centerUserService) {
        this.centerUserService = centerUserService;
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public JSONResult userInfo(@RequestParam String userId) {
        return JSONResult.ok(centerUserService.queryUserInfo(userId));
    }

}
