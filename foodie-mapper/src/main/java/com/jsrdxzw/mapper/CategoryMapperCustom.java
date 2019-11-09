package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.CategoryVO;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/09
 * @Description: 自定义mapper，加一些连接查询等复杂查询
 */
public interface CategoryMapperCustom{
    List<CategoryVO> getSubCatList(int rootCatId);
}
