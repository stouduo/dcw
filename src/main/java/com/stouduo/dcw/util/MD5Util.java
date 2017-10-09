package com.stouduo.dcw.util;

import java.security.MessageDigest;
import java.util.Date;

public class MD5Util {

    private static final String SALT = "STOUDUO_DCW";
    private static final String TOKEN_SALT = "STOUDUO_DCW_EMAIL_TOKEN";
    public static String encode(String password) {
        return encode(password,SALT);
    }

    public static String getToken(String email){
       return  encode(email+new Date().toString(),TOKEN_SALT);
    }

    public static String encode(String password,String salt) {
        password = password + salt;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}