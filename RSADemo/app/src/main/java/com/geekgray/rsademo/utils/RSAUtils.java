package com.geekgray.rsademo.utils;

import com.geekgray.rsademo.entity.KeyPairGen;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * author : GeekGray
 * email  : GeekGray@163.com
 * date   : 2020/11/13 10:49
 * desc   :
 */
public final class RSAUtils
{
    /*用RSA生成instance*/
    private static String RSA = "RSA/ECB/PKCS1Padding";


//    /**
//     * 随机生成RSA密钥对(默认密钥长度为1024)
//     *
//     * @return
//     */
//    public static KeyPair generateRSAKeyPair()
//    {
//        return generateRSAKeyPair(1024);
//    }
//
//    /**
//     * 随机生成RSA密钥对
//     *
//     * @param keyLength 密钥长度，范围：512～2048<br>
//     *                  一般1024
//     *
//     * @return
//     */
//    public static KeyPair generateRSAKeyPair(int keyLength)
//    {
//        try
//        {
//            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
//            kpg.initialize(keyLength);
//            return kpg.genKeyPair();
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /*
     * @Author GeekGray
     * @Description 随机生成密钥对并写入文件RSA
     * @Date
     * @return
     **/
    private static void generateKeyPair() throws Exception
    {
        KeyPairGen keyPairGen = new KeyPairGen();

        //     /** RSA算法要求有一个可信任的随机数源 */
        //     SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyPairGen.ALGORITHM);

        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        //     keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(keyPairGen.getKeySize());

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();

        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        ObjectOutputStream oos1 = null;
        ObjectOutputStream oos2 = null;
        try
        {
            /** 用对象流将生成的密钥写入文件 */
            oos1 = new ObjectOutputStream(new FileOutputStream(keyPairGen.PUBLIC_KEY_FILE));
            oos2 = new ObjectOutputStream(new FileOutputStream(keyPairGen.PRIVATE_KEY_FILE));
            oos1.writeObject(publicKey);
            oos2.writeObject(privateKey);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            /** 清空缓存，关闭文件输出流 */
            oos1.close();
            oos2.close();
        }
    }

    public static KeyPairGen genKeyPair(KeyPairGen keyPairGen) throws NoSuchAlgorithmException
    {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();

        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyPairGen.ALGORITHM);

        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        keyPairGenerator.initialize(keyPairGen.getKeySize(), secureRandom);
        //keyPairGenerator.initialize(KEYSIZE);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();

        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();

//        String publicKeyBase64 = new BASE64Encoder().encode(publicKeyBytes);
//        String privateKeyBase64 = new BASE64Encoder().encode(privateKeyBytes);

//        String publicKeyBase64 = Base64.encode(publicKeyBytes);//Base64类
//        String privateKeyBase64 = Base64.encode(privateKeyBytes);

        keyPairGen.setPublicKeyBase64(Base64Utils.encode(publicKeyBytes));//Base64Utils类
        keyPairGen.setPrivateKeyBase64(Base64Utils.encode(privateKeyBytes));

        return keyPairGen;
    }


    /**
     * RSA使用公钥对明文进行加密
     *
     * @param publicKey 公钥字符串
     * @param plainText 明文
     *
     * @return
     */
    public static String encrypt(String publicKey, String plainText)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));//通过公钥字符串生成公钥
            byte[] bytes = plainText.getBytes();//把明文字符串写进字节流
            ByteArrayInputStream read = new ByteArrayInputStream(bytes);//创建输入流
            ByteArrayOutputStream write = new ByteArrayOutputStream();
            byte[] buf = new byte[117];
            int len = 0;
            while ((len = read.read(buf)) != -1)
            {
                byte[] buf1 = null;
                if (buf.length == len)
                {
                    buf1 = buf;
                }
                else
                {
                    buf1 = new byte[len];
                    for (int i = 0; i < len; i++)
                    {
                        buf1[i] = buf[i];
                    }
                }
                byte[] bytes1 = cipher.doFinal(buf1);
                write.write(bytes1);
            }
            return Base64.encode(write.toByteArray());
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对密文进行解密
     *
     * @param privateKey 私钥
     * @param enStr      密文
     *
     * @return
     */
    public static String decrypt(String privateKey, String enStr)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));

            byte[] bytes = Base64.decode(enStr);
            ByteArrayInputStream read = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream write = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = read.read(buf)) != -1)
            {
                byte[] buf1 = null;
                if (buf.length == len)
                {
                    buf1 = buf;
                }
                else
                {
                    buf1 = new byte[len];
                    for (int i = 0; i < len; i++)
                    {
                        buf1[i] = buf[i];
                    }
                }
                byte[] bytes1 = cipher.doFinal(buf1);
                write.write(bytes1);
            }
            return new String(write.toByteArray());
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     *
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception
    {
        byte[] keyBytes;

        keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     *
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception
    {
        byte[] keyBytes;

        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }


    //=============================================================================================

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param
     * @param
     *
     * @return 加密后的byte型数据
     */
    public static byte[] encryptData(byte[] data, PublicKey publicKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(RSA);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     *
     * @return
     */
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException
    {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException
    {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 使用N、e值还原公钥
     *
     * @param modulus
     * @param publicExponent
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 使用N、d值还原私钥
     *
     * @param modulus
     * @param privateExponent
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     *
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception
    {
        try
        {
            byte[] buffer = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此算法");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("公钥非法");
        }
        catch (NullPointerException e)
        {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     *
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception
    {
        try
        {
            byte[] buffer = Base64Utils.decode(privateKeyStr);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此算法");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("私钥非法");
        }
        catch (NullPointerException e)
        {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     *
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception
    {
        try
        {
            return loadPublicKey(readKey(in));
        }
        catch (IOException e)
        {
            throw new Exception("公钥数据流读取错误");
        }
        catch (NullPointerException e)
        {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param
     *
     * @return 是否成功
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(InputStream in) throws Exception
    {
        try
        {
            return loadPrivateKey(readKey(in));
        }
        catch (IOException e)
        {
            throw new Exception("私钥数据读取错误");
        }
        catch (NullPointerException e)
        {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     *
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null)
        {
            if (readLine.charAt(0) == '-')
            {
                continue;
            }
            else
            {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }

    /**
     * 打印公钥信息
     *
     * @param publicKey
     */
    public static void printPublicKeyInfo(PublicKey publicKey)
    {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        System.out.println("----------RSAPublicKey----------");
        System.out.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
        System.out.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());
        System.out.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());
    }

    public static void printPrivateKeyInfo(PrivateKey privateKey)
    {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
        System.out.println("----------RSAPrivateKey ----------");
        System.out.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
        System.out.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());
        System.out.println("PrivatecExponent=" + rsaPrivateKey.getPrivateExponent().toString());

    }

}
