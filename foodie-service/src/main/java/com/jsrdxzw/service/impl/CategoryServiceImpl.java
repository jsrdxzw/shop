package com.jsrdxzw.service.impl;

import com.jsrdxzw.mapper.CategoryMapper;
import com.jsrdxzw.mapper.CategoryMapperCustom;
import com.jsrdxzw.pojo.Category;
import com.jsrdxzw.service.CategoryService;
import com.jsrdxzw.vo.CategoryVO;
import com.jsrdxzw.vo.NewItemsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/09
 * @Description:
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final int ROOT_TYPE = 1;
    private final CategoryMapper categoryMapper;
    private final CategoryMapperCustom categoryMapperCustom;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryMapperCustom categoryMapperCustom) {
        this.categoryMapper = categoryMapper;
        this.categoryMapperCustom = categoryMapperCustom;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("type", ROOT_TYPE);

        return categoryMapper.selectByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItems(Integer rootCatId) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("rootCatId", rootCatId);
        return categoryMapperCustom.getSixNewItems(map);
    }
}
