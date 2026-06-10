package com.kayak.core.encrypt;

public interface IEncrypt {
    public String encrypt(String secretKey,String password) throws Exception;

    public String decrypt(String secretKey,String password) throws Exception;

}
