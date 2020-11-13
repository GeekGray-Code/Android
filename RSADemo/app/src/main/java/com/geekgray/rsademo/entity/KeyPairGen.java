package com.geekgray.rsademo.entity;

public class KeyPairGen
{
    /** 指定加密算法为RSA */
    public   final String ALGORITHM = "RSA";
    /** 密钥长度，用来初始化 */
    private  int KeySize = 1024;
    /** 指定公钥存放文件 */
    public  final String PUBLIC_KEY_FILE = "PublicKey";
    /** 指定私钥存放文件 */
    public  final String PRIVATE_KEY_FILE = "PrivateKey";
    /* 公钥钥内容 base64 code */
    private String publicKeyBase64;
    /* 私钥钥内容 base64 code */
    private String privateKeyBase64;

    public KeyPairGen()
    {

    }

    public int getKeySize()
    {
        return KeySize;
    }

    public void setKeySize(int keySize)
    {
        this.KeySize = keySize;
    }

    public String getPublicKeyBase64()
    {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64)
    {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPrivateKeyBase64()
    {
        return privateKeyBase64;
    }

    public void setPrivateKeyBase64(String privateKeyBase64)
    {
        this.privateKeyBase64 = privateKeyBase64;
    }

    @Override
    public String toString()
    {
        return "KeyPairGen{" +
                "KeySize=" + KeySize +
                ", publicKeyBase64='" + publicKeyBase64 + '\'' +
                ", privateKeyBase64='" + privateKeyBase64 + '\'' +
                '}';
    }
}

