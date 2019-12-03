package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
public interface ItemsCommentsMapperCustom {
    void saveComments(Map<String, Object> paramsMap);

    List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> paramsMap);
}
