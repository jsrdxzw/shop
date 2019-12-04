package com.jsrdxzw.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
@Data
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    private List<SimpleItemVO> simpleItemList;
}
