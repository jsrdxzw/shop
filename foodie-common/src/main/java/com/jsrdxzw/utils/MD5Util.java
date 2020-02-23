package com.jsrdxzw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description:
 */
public class MD5Util {

    public static String getMD5Str(String value) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.getEncoder().encodeToString(md5.digest(value.getBytes()));
    }
}
