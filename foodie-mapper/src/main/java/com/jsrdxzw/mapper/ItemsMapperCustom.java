package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.ItemCommentVO;
import com.jsrdxzw.vo.ShopCartVO;
import com.jsrdxzw.vo.searchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
public interface ItemsMapperCustom {
    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    List<searchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    List<searchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList") List<String> specIds);

}
