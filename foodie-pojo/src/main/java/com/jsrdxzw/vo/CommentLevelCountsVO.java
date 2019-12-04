package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:  用于展示商品评价数量的vo
 */
@Data
public class CommentLevelCountsVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
