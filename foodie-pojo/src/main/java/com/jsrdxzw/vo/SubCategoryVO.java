package com.jsrdxzw.vo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 三级分类VO
 */
@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
