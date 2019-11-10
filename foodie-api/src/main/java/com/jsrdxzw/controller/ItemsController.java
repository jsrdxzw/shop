package com.jsrdxzw.controller;

import com.jsrdxzw.pojo.Items;
import com.jsrdxzw.pojo.ItemsImg;
import com.jsrdxzw.pojo.ItemsParam;
import com.jsrdxzw.pojo.ItemsSpec;
import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.ItemInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemsController extends BaseController {
    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(value = "商品id", name = "itemId", required = true)
            @PathVariable String itemId
    ) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg("");
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return JSONResult.ok(itemInfoVO);
    }


    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId
    ) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg("");
        }
        return JSONResult.ok(itemService.queryCommentCounts(itemId));
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg("");
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId,
                level,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }

}
