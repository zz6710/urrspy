package com.kayak.dps.utils;


import com.kayak.xsql.XsqlUtils;
import com.kayakwise.kcloud.db.encrypt.IEncrypt;

/**
 * kcloud.db数据库配置druid加解密
 * @author Ty
 * @since 2023-06-19 11:26:45
 */
public class DruidIEncryptUtil implements IEncrypt {
    @Override
    public String encrypt(String password) throws Exception {
        return null;
    }

    @Override
    public String decrypt(String keyAndPassword) throws Exception {
        String[] split = keyAndPassword.split("&&");
        String publicKey = split[0];
        String password = split[1];
        return XsqlUtils.parsePwd(publicKey, password);
    }
}
