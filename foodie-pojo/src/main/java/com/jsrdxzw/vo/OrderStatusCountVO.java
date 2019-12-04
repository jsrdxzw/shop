package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
@Data
public class OrderStatusCountVO {
    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;
}
