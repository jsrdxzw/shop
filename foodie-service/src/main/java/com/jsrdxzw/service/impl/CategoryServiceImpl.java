package com.jsrdxzw.service.impl;

import com.jsrdxzw.mapper.CategoryMapper;
import com.jsrdxzw.mapper.CategoryMapperCustom;
import com.jsrdxzw.pojo.Category;
import com.jsrdxzw.service.CategoryService;
import com.jsrdxzw.utils.JsonUtils;
import com.jsrdxzw.utils.RedisOperator;
import com.jsrdxzw.vo.CategoryVO;
import com.jsrdxzw.vo.NewItemsVO;
import org.apache.commons.lang3.StringUtils;
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
    private final RedisOperator redisOperator;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryMapperCustom categoryMapperCustom, RedisOperator redisOperator) {
        this.categoryMapper = categoryMapper;
        this.categoryMapperCustom = categoryMapperCustom;
        this.redisOperator = redisOperator;
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
        String subCatListString = redisOperator.get("subCat:" + rootCatId);
        List<CategoryVO> subCatList;
        if (StringUtils.isBlank(subCatListString)) {
            subCatList = categoryMapperCustom.getSubCatList(rootCatId);
            // 解决缓存穿透问题
            if (subCatList != null && subCatList.size() > 0) {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(subCatList));
            } else {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(subCatList), 300);
            }
        } else {
            subCatList = JsonUtils.jsonToList(subCatListString, CategoryVO.class);
        }
        return subCatList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItems(Integer rootCatId) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("rootCatId", rootCatId);
        return categoryMapperCustom.getSixNewItems(map);
    }
}
