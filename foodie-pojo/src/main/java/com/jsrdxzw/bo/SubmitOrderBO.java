package com.jsrdxzw.bo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/22
 * @Description:
 */
@Data
public class SubmitOrderBO {
    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
