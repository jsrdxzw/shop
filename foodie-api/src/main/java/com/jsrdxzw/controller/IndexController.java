package com.jsrdxzw.controller;

import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.pojo.Carousel;
import com.jsrdxzw.service.CarouselService;
import com.jsrdxzw.service.CategoryService;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import com.jsrdxzw.utils.RedisOperator;
import com.jsrdxzw.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 01027698
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {

    public static final String CAROUSEL = "carousel";
    private final CarouselService carouselService;
    private final CategoryService categoryService;
    private final RedisOperator redisOperator;

    public IndexController(CarouselService carouselService, CategoryService categoryService, RedisOperator redisOperator) {
        this.carouselService = carouselService;
        this.categoryService = categoryService;
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        String carousel = redisOperator.get(CAROUSEL);
        List<Carousel> list;
        if (StringUtils.isBlank(carousel)) {
            list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set(CAROUSEL, JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(carousel, Carousel.class);
        }
        return JSONResult.ok(list);
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

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId
    ) {
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        return JSONResult.ok(categoryService.getSixNewItems(rootCatId));
    }
}
