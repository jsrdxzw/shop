package com.jsrdxzw.controller;

import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemsController {
    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public JSONResult search(String keywords, String sort, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        // es page 默认从0开始
        PagedGridResult res = itemService.searchItems(keywords, sort, page - 1, pageSize);
        return JSONResult.ok(res);
    }
}
