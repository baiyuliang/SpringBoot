package com.byl.springboottest.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class SaltUtil {

    public static String HASHALGORITHMNAME = "md5";//加密方式
    public static int HASHITERATIONS = 1024;//加密次数

    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

    public static String encryptPassword(String username, String password) {
        String salt= randomSalt();
        String newPassword = new SimpleHash(HASHALGORITHMNAME, password,
                ByteSource.Util.bytes(username +salt),
                HASHITERATIONS
        ).toHex();
        System.out.println("salt>>"+salt);
        System.out.println("newPassword>>"+newPassword);
        return newPassword;
    }
}
