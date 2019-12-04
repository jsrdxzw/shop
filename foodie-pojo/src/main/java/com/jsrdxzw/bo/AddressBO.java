package com.jsrdxzw.bo;

import lombok.Data;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/17
 * @Description: 新增或者修改地址的BO
 */
@Data
public class AddressBO {
    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
