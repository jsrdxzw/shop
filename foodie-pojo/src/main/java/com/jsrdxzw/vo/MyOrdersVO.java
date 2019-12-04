package com.jsrdxzw.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/01
 * @Description:
 */
@Data
public class MyOrdersVO {
    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;
    private List<MySubOrdersItemVO> subOrderItemList;
}
