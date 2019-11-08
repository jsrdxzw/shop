package com.jsrdxzw.service.impl;

import com.jsrdxzw.bo.UserBO;
import com.jsrdxzw.enums.Gender;
import com.jsrdxzw.mapper.ShopUserMapper;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.service.UserService;
import com.jsrdxzw.utils.DateUtil;
import com.jsrdxzw.utils.MD5Util;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    private final ShopUserMapper userMapper;
    private final Sid sid;

    private static final String USER_FACE = "https://bpic.588ku.com/element_pic/19/04/09/05da1a456b1dedc06f6d58b16c62f4bf.jpg!/fw/253/quality/90/unsharp/true/compress/true";

    public UserServiceImpl(ShopUserMapper userMapper, Sid sid) {
        this.userMapper = userMapper;
        this.sid = sid;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(ShopUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        ShopUser shopUser = userMapper.selectOneByExample(example);
        return shopUser != null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShopUser createUser(UserBO userBO) {
        ShopUser user = new ShopUser();
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Util.getMD5Str(userBO.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 默认用户名称同用户名
        user.setNickname(userBO.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Gender.UNKNOWN.getType());
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        // 分布式系统，需要全局的id
        user.setId(sid.nextShort());
        userMapper.insertSelective(user);
        return user;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public ShopUser queryUserForLogin(String username, String password) {
        Example example = new Example(ShopUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username).andEqualTo("password",password);
        return userMapper.selectOneByExample(example);
    }
}
