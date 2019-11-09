package com.jsrdxzw.controller;

import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.service.CarouselService;
import com.jsrdxzw.service.CategoryService;
import com.jsrdxzw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final CategoryService categoryService;

    public IndexController(CarouselService carouselService, CategoryService categoryService) {
        this.carouselService = carouselService;
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        return JSONResult.ok(carouselService.queryAll(YesOrNo.YES.type));
    }

    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats() {
        return JSONResult.ok(categoryService.queryAllRootLevelCat());
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId
    ) {
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        return JSONResult.ok(categoryService.getSubCatList(rootCatId));
    }
}
