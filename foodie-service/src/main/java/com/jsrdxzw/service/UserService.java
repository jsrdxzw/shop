package com.jsrdxzw.service;

import com.jsrdxzw.bo.UserBO;
import com.jsrdxzw.pojo.ShopUser;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description:
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     *
     * @param userBO 前端传来的用户信息
     * @return 新用户
     */
    ShopUser createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    ShopUser queryUserForLogin(String username, String password);
}
