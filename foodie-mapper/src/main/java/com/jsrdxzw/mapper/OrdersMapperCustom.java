package com.jsrdxzw.mapper;

import com.jsrdxzw.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
public interface OrdersMapperCustom {

    /**
     * 查询个人订单
     *
     * @param map
     * @return
     */
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
}
