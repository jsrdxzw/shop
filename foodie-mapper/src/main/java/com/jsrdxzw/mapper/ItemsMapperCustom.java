package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
public interface ItemsMapperCustom {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
}
