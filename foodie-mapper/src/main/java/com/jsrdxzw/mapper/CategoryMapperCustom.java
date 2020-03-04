package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.CategoryVO;
import com.jsrdxzw.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/09
 * @Description: 自定义mapper，加一些连接查询等复杂查询
 */
public interface CategoryMapperCustom {
    List<CategoryVO> getSubCatList(int rootCatId);

    /**
     * @param map 传入的查询参数
     * @return
     */
    List<NewItemsVO> getSixNewItems(@Param("paramsMap") Map<String, Object> map);

}
