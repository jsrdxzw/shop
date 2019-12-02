package com.jsrdxzw.service.impl;

import com.github.pagehelper.PageInfo;
import com.jsrdxzw.utils.PagedGridResult;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
public class CommonService {

    protected PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

}
