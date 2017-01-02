package com.power.spring.utils;

import java.security.MessageDigest;

/**
 * Created by ShenLi on 2014/12/26.
 */
public class MD5Utils {

    public static byte[] getMD5(String content) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = digest.digest(content.getBytes("utf8"));
        return bytes;
    }
}
