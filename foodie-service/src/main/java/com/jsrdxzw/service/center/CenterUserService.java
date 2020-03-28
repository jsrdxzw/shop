package com.jsrdxzw.service.center;

import com.jsrdxzw.bo.center.CenterUserBO;
import com.jsrdxzw.pojo.ShopUser;

import java.io.InputStream;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
public interface CenterUserService {

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    ShopUser queryUserInfo(String userId);

    ShopUser updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 更新用户头像
     *
     * @return
     */
    ShopUser updateUserFace(String userId, String faceUrl);

    void uploadUserFace(String bucketName, String objectName, InputStream inputStream);
}
