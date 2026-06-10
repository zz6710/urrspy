package com.kayak.dps.app.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtils {
    private static final Logger log = LoggerFactory.getLogger(AESUtils.class);
    /**
     * 算法名称/加密模式/数据填充方式
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * AES加解密默认密钥(16位)
     */
    private static final String KEY = "1234567890abcdef";

    /**
     * AES
     */
    private static final String AES = "AES";


    /**
     * 加密
     * @param content 要加密的字符串
     * @return Base64
     */
    public static String AESEncrypted(String content){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(128);
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(), AES));
            byte[] out = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            //采用base64算法进行转码，避免出现中文乱码
            // base64加密之后，会有换行符，会导致解密失败，要去掉
            return Base64.getEncoder().encodeToString(out).replace("\r\n","");
        }catch (Exception e){
            log.error("AESEncrypted({} , {})加密异常", content, KEY, e);
        }

        return null;
    }


    /**
     * 解密
     * @param encryptStr 要解密的字符串
     * @return decryptBytes
     */
    public static  String AESDecrypted(String encryptStr) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), AES));

            //采用base64算法进行转码，避免出现中文乱码
            byte[] encryptBytes = Base64.getDecoder().decode(encryptStr);
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes);
        }catch (Exception e){
            log.error("AESDecrypted({} , {})解密异常", encryptStr, KEY, e);
        }

        return null;
    }

    public static void main (String[] args) throws Exception{
        String str = "ROOT";

        String encrypt = AESEncrypted(str);
        System.out.println("加密后：" + encrypt);

        String decrypt = AESDecrypted(encrypt);
        System.out.println("解密后：" + decrypt);

    }
}
