package com.geekgray.rsademo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geekgray.rsademo.R;
import com.geekgray.rsademo.entity.KeyPairGen;
import com.geekgray.rsademo.utils.Base64Utils;
import com.geekgray.rsademo.utils.RSA;
import com.geekgray.rsademo.utils.RSAUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/*

 */
public class MainActivity1 extends AppCompatActivity
{

    /**
     * 文本
     */
    @BindView(R.id.ed_text)
    EditText edText;
    /**
     * 加密
     */
    @BindView(R.id.rsa_btn_encrypt)
    Button rsaBtnEncrypt;
    @BindView(R.id.tv_text_ras_encrypt)
    TextView tvTextRasEncrypt;
    @BindView(R.id.rsa_btn_decrypt)
    Button rsaBtnDecrypt;
    @BindView(R.id.tv_text_ras_decrypt)
    TextView tvTextRasDecrypt;
    /**
     * 解密
     */
    @BindView(R.id.base64_btn_encrypt)
    Button base64BtnEncrypt;
    @BindView(R.id.tv_text_base64_encrypt)
    TextView tvTextBase64Encrypt;
    @BindView(R.id.base64_btn_decrypt)
    Button base64BtnDecrypt;
    @BindView(R.id.tv_text_base64_decrypt)
    TextView tvTextBase64Decrypt;

    /**
     * 签名 与 验证
     */
    @BindView(R.id.rsa_sign_btn_encrypt)
    Button rsaSignBtnEncrypt;
    @BindView(R.id.tv_text_ras_sign_encrypt)
    TextView tvTextRasSignEncrypt;
    @BindView(R.id.rsa_sign_btn_decrypt)
    Button rsaSignBtnDecrypt;
    @BindView(R.id.tv_text_ras_sign_decrypt)
    TextView tvTextRasSignDecrypt;

    /**
     * 存储/读取加密文件
     */
    @BindView(R.id.ed_text_filename)
    EditText edTextFileName;
    @BindView(R.id.savefile_btn_ecrypt)
    Button saveFileBtn;
    @BindView(R.id.read_btn_ecrypt)
    Button readFileBtn;
    @BindView(R.id.tv_text_readfile)
    TextView tvTextReadFile;

    /**
     * 生成公钥和私钥
     */
    @BindView(R.id.generate_keybit_spinner)
    Spinner generateKeyBitSpinner;
    @BindView(R.id.generate_rsa_publickey_btn)
    Button generateRsaPublicKeyBtn;
    @BindView(R.id.tv_text_generate_rsa_publickey)
    TextView tvTextGenerateRsaPublicKey;
    @BindView(R.id.generate_rsa_privatekey_btn)
    Button generateRsaPrivateKeyBtn;
    @BindView(R.id.tv_text_generate_rsa_privatekey)
    TextView tvTextGenerateRsaPrivateKey;


    private static String content = null;
    private String fileName = null;

    /* 密钥对内容 base64 code */
    private static String PUCLIC_KEY = "";
    private static String PRIVATE_KEY = "";

    KeyPairGen keyPairGen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);

        setListener();
    }


    /**
     * 获取明文输入框edText输入的内容
     */
    private void getContent()
    {
        content = edText.getText().toString().trim();
    }

    /*
     * @Author GeekGray
     * @Description //TODO
     * @Date
     * @Param
     * @return
     **/
    private void generateKeyPair() throws NoSuchAlgorithmException
    {
        keyPairGen = RSAUtils.genKeyPair(keyPairGen);
        PUCLIC_KEY = keyPairGen.getPublicKeyBase64();
        PRIVATE_KEY = keyPairGen.getPrivateKeyBase64();
    }


    private void setListener()
    {

        /*
         * 选择生成密钥对的位数
         */
        generateKeyBitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                keyPairGen = new KeyPairGen();
                String[] keyBits = getResources().getStringArray(R.array.key_bits);
                keyPairGen.setKeySize(Integer.parseInt(keyBits[pos]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        /*
         * 生成密钥对
         */
        generateRsaPublicKeyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    generateKeyPair();
                    String textContent = "生成密钥对位数: " + keyPairGen.getKeySize() + "\n\n" +

                            "publicKeyBase64.length(): " + keyPairGen.getPublicKeyBase64().length() + "\n\n" +
                            "publicKeyBase64: " + keyPairGen.getPublicKeyBase64() + "\n\n\n" +
                            "privateKeyBase64.length(): " + keyPairGen.getPrivateKeyBase64().length() + "\n\n" +
                            "privateKeyBase64: " + keyPairGen.getPrivateKeyBase64();
                    tvTextGenerateRsaPublicKey.setText(textContent);
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }


            }
        });

        /*
         * 签名
         */
        rsaSignBtnEncrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                String sign = RSA.sign(content, PRIVATE_KEY, "utf-8");
                tvTextRasSignEncrypt.setText(sign);
            }
        });

        /**
         * 验证
         */
        rsaSignBtnDecrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                String sign = tvTextRasSignEncrypt.getText().toString().trim();
//                content = "143SDFFSFSDFS423";
                boolean verify = RSA.verify(content, sign, PUCLIC_KEY, "utf-8");
                tvTextRasSignDecrypt.setText(verify + "");
            }
        });

        //==========================================================================================

        /**
         * RAS加密
         */
        rsaBtnEncrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                Toast.makeText(MainActivity1.this, "RSA加密", Toast.LENGTH_SHORT).show();

                /**加密内容*/
                try
                {
//                    PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
//                    /**公钥加密*/
//                    byte[] encryptByte = RSAUtils.encryptData(content.getBytes(), publicKey);
//                    String afterencrypt = Base64Utils.encode(encryptByte);


                    String afterencrypt = RSAUtils.encrypt(PUCLIC_KEY, content);
                    Log.d("TAG", "afterencrypt=" + afterencrypt);
                    tvTextRasEncrypt.setText(afterencrypt);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        /**
         * RAS解密
         */
        rsaBtnDecrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                Toast.makeText(MainActivity1.this, "RSA解密", Toast.LENGTH_SHORT).show();
                String jiami = tvTextRasEncrypt.getText().toString().trim();
                Log.d("TAG", "jiami=" + jiami);
                /**服务端使用私钥解密*/
                try
                {
//                    PrivateKey privateKey = RSAUtils.loadPrivateKey(PRIVATE_KEY);
//                    /**私钥解密*/
//                    byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(jiami), privateKey);
//                    String decryptStr = new String(decryptByte);
                    String decryptStr = RSAUtils.decrypt(PRIVATE_KEY, jiami);
                    Log.d("TAG", "decryptStr=" + decryptStr);
                    tvTextRasDecrypt.setText(decryptStr);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //==========================================================================================
        /**
         * Base64加密
         */
        base64BtnEncrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                Toast.makeText(MainActivity1.this, "Base64编码", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "content=" + content);
                String encode = Base64Utils.encode(content.getBytes());
                Log.d("TAG", "encode=" + encode);
                tvTextBase64Encrypt.setText(encode);
            }
        });

        /**
         * Base64解密
         */
        base64BtnDecrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getContent();
                Toast.makeText(MainActivity1.this, "Base64解码", Toast.LENGTH_SHORT).show();
                String str = tvTextBase64Encrypt.getText().toString().trim();
                Log.d("TAG", "str=" + str);
                byte[] decode = Base64Utils.decode(str);
                String decryptStr = new String(decode);
                Log.d("TAG", "decryptStr=" + decryptStr);
                tvTextBase64Decrypt.setText(decryptStr);
            }
        });

        saveFileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    fileName = edTextFileName.getText().toString().trim() + ".txt";
                    //用MODE_PRIVAT模式，用输入的文件名创建一个 rsaencrypt.txt文件,若输入为空则用默认文件名
                    if (edTextFileName.getText().toString().trim().isEmpty())
                    {
                        fileName = "rsaencrypt.txt";
                    }
                    FileOutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);

//                    写入内容
                    String fileContent =
                            "加密算法: RSA加密算法"+ "\n\n" +
                            "生成密钥对位数: " + keyPairGen.getKeySize() + "\n\n" +

                            "公钥长度: " + keyPairGen.getPublicKeyBase64().length() + "\n" +
                            "公钥Base64编码: " + keyPairGen.getPublicKeyBase64() + "\n\n\n" +
                            "私钥长度: " + keyPairGen.getPrivateKeyBase64().length() + "\n" +
                            "私钥Base64编码: " + keyPairGen.getPrivateKeyBase64() + "\n\n" +

                            "明文：" + edText.getText().toString() + "\n\n" +
//                            "Base64编码：" + tvTextBase64Encrypt.getText().toString() + "\n\n" +
//                            "Base64解码：" + tvTextBase64Decrypt.getText().toString() + "\n\n" +
                            "RSA加密：" + tvTextRasEncrypt.getText().toString() + "\n\n" +
                            "RSA解密：" + tvTextRasDecrypt.getText().toString() + "\n\n" +
                            "RSA数字签名：" + tvTextRasSignEncrypt.getText().toString() + "\n\n" +
                            "RSA验证：" + tvTextRasSignDecrypt.getText().toString() + "\n\n\n" +
                            "存储文件: " + fileName + "\n\n" +
                            "存储类型: 内部存储" + "\n\n" +
                            "存储文件路径：" + getFilesDir().getAbsolutePath() + "/" + fileName + "\n\n" +
                            "author: GeekGray" + "\n" +
                            "date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) + "\n" +
                            "version: 1.0.2";

                    //写入方法和冲刷方法
                    outputStream.write(fileContent.getBytes());
                    outputStream.flush();
                    Toast.makeText(MainActivity1.this, "保存完成", Toast.LENGTH_SHORT).show();
                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        readFileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //用StringBuilder来接收数据，而不是用String+=的方法。
                StringBuilder sb = new StringBuilder();
                //每次读取1024个byte的数据
                byte[] bytes = new byte[1024];
                FileInputStream inputStream = null;
                try
                {
                    inputStream = openFileInput(fileName);
                    int len = 0;
                    while ((len = inputStream.read(bytes)) != -1)
                    {
                        sb.append(new String(bytes, 0, len));
                    }
                    Log.e("读取到的数据", sb.toString());
                    tvTextReadFile.setText(sb.toString());
                }
                catch (FileNotFoundException e1)
                {
                    e1.printStackTrace();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }
}
