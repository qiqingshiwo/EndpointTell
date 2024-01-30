package org.example;

import cn.hutool.core.util.StrUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class jiemi
{


    /**
     * 定义AES加密填充方式和密钥长度，这个不可以修改
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    private static final Integer KEY_SIZE = 256;

    /**
     * 敏感数据前缀
     */
    public static final String DESENSITIZED_DATA_PREFIX = "CU##";

    /**
     * BASE64编码
     *
     * @param bytes
     * @return
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * BASE64解码
     *
     * @param base64Code
     * @return
     */
    public static byte[] base64Decode(String base64Code) {
        return Base64.getDecoder().decode(base64Code);
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     * @author 89003028
     * @date 2019年10月15日 下午6:41:33
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, true);
    }

    /**
     * 加密
     *
     * @param data
     * @param isWithPrefix 加密密文是否包含前缀
     * @return
     * @throws Exception
     * @author 89003028
     * @date 2019年10月15日 下午6:41:33
     */
    public static String encrypt(String data, String key, boolean isWithPrefix) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            if (isWithPrefix) {
                // 判断是否是加密数据，是则直接返回原数据
                if (data.startsWith(DESENSITIZED_DATA_PREFIX)) {
                    return data;
                }
                return DESENSITIZED_DATA_PREFIX + base64Encode(aesEncryptToBytes(data, key));
            } else {
                return base64Encode(aesEncryptToBytes(data, key));
            }
        } catch (Exception e) {
            return data;
        }
    }

    public static void main(String[] args) {
        String decrypt = decrypt("CU##bm4zD2mzF/h4Jtb2M0ceVzk1N2gltrpLjQNIBYYHxdTWsL662IaR/KG9wFzO9SJSek6Z8fZwgOgkXdyCgpGirSI+DAhxHtglwBLRG9xWxK2HwW8UMScAIZPj1/Hb+wJ1Abf1Lkgv1jjdiadNx9ZnjFxkIwDW99oNoimCMFyGWeOhumfkBEeDxLnI55xiv0Af", "4AC3456A893H56D82AD0CCF48303524C");
        System.out.println(decrypt);
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @throws Exception
     * @author 89003028
     * @date 2019年10月15日 下午6:41:45
     */
    public static String decrypt(String data, String key) {
        return decrypt(data, key, true);
    }

    /**
     * 解密
     *
     * @param data         待解密数据
     * @param isWithPrefix 是否有前缀
     * @return
     * @throws Exception
     * @author 89003028
     * @date 2019年10月15日 下午6:41:45
     */
    public static String decrypt(String data, String key, boolean isWithPrefix) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            if (isWithPrefix) {
                // 判断是否是加密数据，是则直接返回原数据
                if (data.length() <= DESENSITIZED_DATA_PREFIX.length() || !data.startsWith(DESENSITIZED_DATA_PREFIX)) {
                    return data;
                }
                data = data.substring(DESENSITIZED_DATA_PREFIX.length());
            }

            return aesDecryptByBytes(base64Decode(data), key);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * AES加密
     *
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(KEY_SIZE);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);//NOSONAR
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES解密
     *
     * @param encryptBytes
     * @param decryptKey
     * @return
     * @throws Exception
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(KEY_SIZE);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);//NOSONAR
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
}