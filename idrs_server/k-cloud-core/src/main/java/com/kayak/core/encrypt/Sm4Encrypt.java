package com.kayak.core.encrypt;


import com.kayak.core.util.SM4Util;

public class Sm4Encrypt implements IEncrypt {
    @Override
    public String encrypt(String secretKey,String password) throws Exception {
        return ENCRYPT(secretKey,password);
    }

    @Override
    public String decrypt(String secretKey,String password) throws Exception {
        return DECRYPT(secretKey,password);
    }

    /**
     * 加密
     */
    public static String ENCRYPT(String secretKey,String password) throws Exception{
        SM4Util sm4Util=new SM4Util();
        sm4Util.setSecretKey(secretKey);
        return sm4Util.encryptData_ECB(password);
    }

    /**
     * 解密
     */
    public static String DECRYPT(String secretKey,String password) throws Exception{
        //需先使用国密算法生成对称秘钥串.具体方法直接看<国密算法.pdf>此处不做赘述
        String sms4_key = "c2377e75808a76153251983aab4e24fd";
        SM4Util sm4Util=new SM4Util();
        sm4Util.setSecretKey(secretKey);
        return sm4Util.decryptData_ECB(password);
    }

    public static void main(String[] args) {
        try {
            System.out.println("明文:kcloud");
            String cipher=Sm4Encrypt.ENCRYPT("UISwD9fW6cFh9SNS","kcloud");
            System.out.println("密文:"+cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
