package com.jsrdxzw.service.impl.center;

import com.aliyun.oss.OSS;
import com.jsrdxzw.bo.center.CenterUserBO;
import com.jsrdxzw.mapper.ShopUserMapper;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    private final ShopUserMapper shopUserMapper;
    private final OSS ossClient;

    public CenterUserServiceImpl(ShopUserMapper shopUserMapper, OSS ossClient) {
        this.shopUserMapper = shopUserMapper;
        this.ossClient = ossClient;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public ShopUser queryUserInfo(String userId) {
        ShopUser user = shopUserMapper.selectByPrimaryKey(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ShopUser updateUserInfo(String userId, CenterUserBO centerUserBO) {
        ShopUser target = new ShopUser();
        BeanUtils.copyProperties(centerUserBO, target);
        target.setId(userId);
        target.setUpdatedTime(new Date());
        shopUserMapper.updateByPrimaryKeySelective(target);
        return queryUserInfo(userId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ShopUser updateUserFace(String userId, String faceUrl) {
        ShopUser user = new ShopUser();
        user.setId(userId);
        user.setFace(faceUrl);
        user.setUpdatedTime(new Date());
        shopUserMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    @Override
    public void uploadUserFace(String bucketName, String objectName, InputStream inputStream) {
        ossClient.putObject(bucketName, objectName, inputStream);
    }
}
