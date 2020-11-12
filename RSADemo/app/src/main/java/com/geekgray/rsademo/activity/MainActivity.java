package com.geekgray.rsademo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geekgray.rsademo.R;
import com.geekgray.rsademo.utils.Base64Utils;
import com.geekgray.rsademo.utils.RSA;
import com.geekgray.rsademo.utils.RSAUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/*

 */
public class MainActivity extends Activity
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


    private static String content = null;
    /* 密钥内容 base64 code */
    private static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+iRXVDOsZikPvaIwzTDhcKx3r\n" +
            "SZNbB/H/MrdUj/GkiSgDL7bTXjyb0cAwefD+/JxXBy6EMuPzBMt7flTWNXGBUNvw\n" +
            "HpaUPicdVAH4h8V0PvUiQKG/pS6oynMvARjZIHWBg8VEqaTcBdpuq+1jhtDxhuBM\n" +
            "SFpt7b8gpWo//BG0ZQIDAQAB";

    private static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL6JFdUM6xmKQ+9o\n" +
            "jDNMOFwrHetJk1sH8f8yt1SP8aSJKAMvttNePJvRwDB58P78nFcHLoQy4/MEy3t+\n" +
            "VNY1cYFQ2/AelpQ+Jx1UAfiHxXQ+9SJAob+lLqjKcy8BGNkgdYGDxUSppNwF2m6r\n" +
            "7WOG0PGG4ExIWm3tvyClaj/8EbRlAgMBAAECgYAX3y8IEWVHPuaCEVQ3fR42lgRa\n" +
            "nU5EAnvUYHNNufcpiTGlLI44bz8iuqXcrPp/yACCetjeIU4j/X7NCyfv6qQ8ux/0\n" +
            "WdGY4WZtc9EV38vgxzlfOHWrtJ1qVBX6vbs8TZabaz9XSaE+u+akhGACf5dHm4HN\n" +
            "uhwDIvtu0AwBzwMIBQJBAN5BI8q0S5EI3nu4Bi3ZzssFRwh9TD8Fa91TPntFGi0J\n" +
            "q3iGTq2qb2j3TKOn0lZBVg82yicNlxklOemwWEqxDlcCQQDbdw2+2y9MNVJSxOvO\n" +
            "wEKdzcvimB1m7896GcWRpOp6/BBZyj8A20QztpEmJ5v9V8sFIjiVqdWlWWar7Lqr\n" +
            "9SWjAkBRcQ87hSu3nsdgEIP7IzgavvlTjA53fXYUKR/ZLe40mLmDtbt4+d5PWWd1\n" +
            "BNcXkmOFua8D9n/qz/BTyLHh1NWLAkEAl6MA6lhDq+JDyVCqpaYN4T7qmtwDpLYZ\n" +
            "owHfkqxiHyu+mGu3cH4P97MzQyunCjr42ck1U6OPLLpCyJO+v0WZBQJASReT45oU\n" +
            "Xvp/eK/JEdMu68GFzDp9gbsKpRRNv2/fL+ZCRzEWzkElfDWmJy5g/FhkEatgfAuZ\n" +
            "Cxl0w8M0aLiBQw==";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        initView();
        setListener();
    }

    private void initView()
    {
        /**
         * 文本
         */
        edText = (EditText) findViewById(R.id.ed_text);
        /**
         * 加密
         */
//    @BindView(R.id.rsa_btn_encrypt)
        rsaBtnEncrypt = (Button) findViewById(R.id.rsa_btn_encrypt);
//    @BindView(R.id.tv_text_ras_encrypt)
        tvTextRasEncrypt = (TextView) findViewById(R.id.tv_text_ras_encrypt);
        ;
//    @BindView(R.id.rsa_btn_decrypt)
        rsaBtnDecrypt = (Button) findViewById(R.id.rsa_btn_decrypt);
        ;
//    @BindView(R.id.tv_text_ras_decrypt)
        tvTextRasDecrypt = (TextView) findViewById(R.id.tv_text_ras_decrypt);
        ;
        /**
         * 解密
         */
//    @BindView(R.id.base64_btn_encrypt)
        base64BtnEncrypt = (Button) findViewById(R.id.base64_btn_encrypt);
        ;
//    @BindView(R.id.tv_text_base64_encrypt)
        tvTextBase64Encrypt = (TextView) findViewById(R.id.tv_text_base64_encrypt);
        ;
//    @BindView(R.id.base64_btn_decrypt)
        base64BtnDecrypt = (Button) findViewById(R.id.base64_btn_decrypt);
        ;
//    @BindView(R.id.tv_text_base64_decrypt)
        tvTextBase64Decrypt = (TextView) findViewById(R.id.tv_text_base64_decrypt);
        ;

        /**
         * 签名 与 验证
         */
//    @BindView(R.id.rsa_sign_btn_jiami)
        rsaSignBtnEncrypt = (Button) findViewById(R.id.rsa_sign_btn_encrypt);
        ;
//    @BindView(R.id.tv_text_ras_sign_encrypt)
        tvTextRasSignEncrypt = (TextView) findViewById(R.id.tv_text_ras_sign_encrypt);
        ;
//    @BindView(R.id.rsa_sign_btn_decrypt)
        rsaSignBtnDecrypt = (Button) findViewById(R.id.rsa_sign_btn_decrypt);
        ;
//    @BindView(R.id.tv_text_ras_sign_decrypt)
        tvTextRasSignDecrypt = (TextView) findViewById(R.id.tv_text_ras_sign_decrypt);


        /**
         * 存储/读取加密文件
         */


    }

    /**
     * 或者edText输入的内容
     */
    private void getContent()
    {
//       content="SFDDDDFSFSFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFFSFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSFSFSDFSDFSDFSDFSDFSFSDFSFSSDFSDFSDFSDFSFSDFSFSDF";
//       content = "143SDFFSFSDFS423";
        content = edText.getText().toString().trim();
    }


    private void setListener()
    {
        //==========================================================================================
        /**
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
                Toast.makeText(MainActivity.this, "RSA加密", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "RSA解密", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Base64加密", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Base64解密", Toast.LENGTH_SHORT).show();
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
                    //用MODE_PRIVAT模式，创建一个 rsaencrypt.txt文件
                    FileOutputStream outputStream = openFileOutput("rsaencrypt.txt", MODE_PRIVATE);
                    //写入方法和冲刷方法
                    String fileContent="明文："+edText.getText().toString()+"\n\n"+
                            "Base64加密："+tvTextBase64Encrypt.getText().toString()+"\n\n"+
                            "Base64解密："+tvTextBase64Decrypt.getText().toString()+"\n\n"+
                            "Rsa加密："+tvTextRasEncrypt.getText().toString()+"\n\n"+
                            "Rsa解密："+tvTextRasDecrypt.getText().toString()+"\n\n"+
                            "Rsa数字签名："+tvTextRasSignEncrypt.getText().toString()+"\n\n"+
                            "Rsa验证："+tvTextRasSignDecrypt.getText().toString();
                    outputStream.write(fileContent.getBytes());
                    outputStream.flush();
                    Toast.makeText(MainActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
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
                    inputStream = openFileInput("rsaencrypt.txt");
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
