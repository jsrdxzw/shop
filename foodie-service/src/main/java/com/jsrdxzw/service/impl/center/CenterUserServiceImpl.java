package com.jsrdxzw.service.impl.center;

import com.jsrdxzw.bo.center.CenterUserBO;
import com.jsrdxzw.mapper.ShopUserMapper;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    private final ShopUserMapper shopUserMapper;

    public CenterUserServiceImpl(ShopUserMapper shopUserMapper) {
        this.shopUserMapper = shopUserMapper;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public ShopUser queryUserInfo(String userId) {
        ShopUser user = shopUserMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
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
}
