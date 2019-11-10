package com.jsrdxzw.service;

import com.jsrdxzw.pojo.Category;
import com.jsrdxzw.vo.CategoryVO;
import com.jsrdxzw.vo.NewItemsVO;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/09
 * @Description:
 */
public interface CategoryService {

    /**
     * 查询所有的一级分类
     *
     * @return 所有的一级分类
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类查询子分类
     *
     * @param rootCatId 一级分类 id
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页一级分类下的最新6个数据
     *
     * @param rootCatId 一级分类 id
     * @return
     */
    List<NewItemsVO> getSixNewItems(Integer rootCatId);
}
