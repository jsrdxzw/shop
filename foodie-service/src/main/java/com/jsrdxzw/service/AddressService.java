package com.jsrdxzw.service;

import com.jsrdxzw.bo.AddressBO;
import com.jsrdxzw.pojo.UserAddress;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/16
 * @Description:
 */
public interface AddressService {

    /**
     * 根据用户id查找用户地址列表
     *
     * @param userId 用户id
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * @param addressBO 用户新增地址
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     *
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除对应的用户地址信息
     *
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    void updateUserAddressDefault(String userId, String addressId);

    UserAddress queryUserAddress(String userId, String addressId);
}
