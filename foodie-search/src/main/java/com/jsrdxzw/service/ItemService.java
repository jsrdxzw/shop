package com.jsrdxzw.service;

import com.jsrdxzw.utils.PagedGridResult;

public interface ItemService {
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
}
