package com.jsrdxzw.controller;

import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.service.CarouselService;
import com.jsrdxzw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 01027698
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {

    private final CarouselService carouselService;

    public IndexController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        return JSONResult.ok(carouselService.queryAll(YesOrNo.YES.type));
    }
}
