package com.ngbj.browse.network.retrofit.utils;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.socks.library.KLog;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 项目名称：ABirdRouter3.2.0
 * 类描述：
 * 创建人：niaogebiji
 * 创建时间：2018/9/19 下午7:21
 * 修改人：niaogebiji
 * 修改时间：2018/9/19 下午7:21
 * 修改备注：
 */
public class Sha1SignUtils {


    /**
     * 将对象重新签名
     */
    public static Map<String,Object> reSign(Map<String,Object> requestParams) {
        if (requestParams == null) {
            return requestParams;
        }

        //map -- jsonString -- jsonObjct

        String value = JSONObject.toJSONString(requestParams, SerializerFeature.MapSortField);
        JSONObject jsonObject = JSONObject.parseObject(value);
        String parms = toUrlString(jsonObject);
        KLog.d("parms:" + parms);
        String auth_signature9 = hmac_Sha1("9dhagki89232rhew2ejwksssdj3d", parms);
//        AppLog.i("加密串9:" + auth_signature9);
        requestParams.put("auth_signature", auth_signature9);
        return requestParams;
    }


    private static String hmac_Sha1(String key, String datas) {
        String reString = "";

        try {
            byte[] data = key.getBytes("UTF-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = datas.getBytes("UTF-8");
            //完成 Mac 操作
            byte[] text1 = mac.doFinal(text);

            reString = convertToHex(text1);

        } catch (Exception e) {
        }
        return reString;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }



    public static String authPassword(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
            localMessageDigest.update(paramString.getBytes());
            String str = bytes2Hex(localMessageDigest.digest()).toUpperCase();
            return str;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        }
        return "";
    }


    public static String getHmacMd5Str(String s, String keyString) {
        String sEncodedString = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }


    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }


    /**
     * 按字母排序
     *
     * @param jsonObject
     * @return
     */
    public static String toUrlString(JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        Map<String, String> map = new HashMap<>();
        for (String key : keys) {
            map.put(key, jsonObject.getString(key));
        }

        //这里将map.entrySet()转换成list
        SortedMap<String, String> sort = new TreeMap<String, String>(map);
        Set<Map.Entry<String, String>> entry1 = sort.entrySet();
        Iterator<Map.Entry<String, String>> it = entry1.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
//            AppLog.i("排序之后:"+entry.getKey()+" 值"+entry.getValue());
            if (!TextUtils.isEmpty(entry.getKey())) {
                stringBuilder.append(entry.getKey()).append('=').append(entry.getValue());
            }
        }


//        for (String key : keys) {
//            if (!TextUtils.isEmpty(key)) {
//                stringBuilder.append(key).append('=').append(jsonObject.get(key)).append('&');
//            }
//        }
//        return stringBuilder.deleteCharAt(stringBuilder.length()).toString();
        return stringBuilder.toString();
    }

    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strDes;
    }

    //    public static String bytes2Hex(byte[] bts) {
//        String des = "";
//        String tmp = null;
//        for (int i = 0; i < bts.length; i++) {
//            tmp = (Integer.toHexString(bts[i] & 0xFF));
//            if (tmp.length() == 1) {
//                des += "0";
//            }
//            des += tmp;
//        }
//        return des;
//    }
    public static String bytes2Hex(byte[] paramArrayOfByte) {
        String str1 = "";
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length)
                return str1;
            String str2 = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            if (str2.length() == 1)
                str1 = str1 + "0";
            str1 = str1 + str2;
        }
    }




//    /**
//     * 获取 hmacSha1
//     *
//     * @param base
//     * @param key
//     * @return
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidKeyException
//     */
//    public static String hmacSha1(String base, String key) throws NoSuchAlgorithmException, InvalidKeyException {
//        if (TextUtils.isEmpty(base) || TextUtils.isEmpty(key)) {
//            return "";
//        }
//        String type = "test-HmacSHA1";
//        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
//        Mac mac = Mac.getInstance(type);
//        mac.init(secret);
//        byte[] digest = mac.doFinal(base.getBytes());
//
//        return Base64.encodeToString(digest, Base64.DEFAULT);
//
//    }


    private static final String LOG_TAG = "HMACTest";
    private static final String app_key = "sxcrqns9";
    private static final String REGISTER_HMAC_KEY = "w1qsqbshohjioqejwswww1zhnupd";



    public static String junitTest(String str) {
        // String string = REGISTER_HMAC_KEY;
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            //转小写
            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        return "";

    }
}
