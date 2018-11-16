package com.shoneworn.libcore.utils;

/**
 * Created by chenxiangxiang on 2018/11/16.
 */

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Coder {
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5Coder() {
    }

    public static String md5(String inStr) {
        byte[] inStrBytes = inStr.getBytes();

        try {
            MessageDigest MD = MessageDigest.getInstance("MD5");
            MD.update(inStrBytes);
            byte[] mdByte = MD.digest();
            char[] str = new char[mdByte.length * 2];
            int k = 0;

            for(int i = 0; i < mdByte.length; ++i) {
                byte temp = mdByte[i];
                str[k++] = hexDigits[temp >>> 4 & 15];
                str[k++] = hexDigits[temp & 15];
            }

            return new String(str);
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static String encode(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] messageDigest = md5.digest();
            StringBuilder hexString = new StringBuilder();
            byte[] var4 = messageDigest;
            int var5 = messageDigest.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte b = var4[var6];
                hexString.append(String.format("%02X", new Object[]{Byte.valueOf(b)}));
            }

            return hexString.toString().toLowerCase();
        } catch (Exception var8) {
            var8.printStackTrace();
            return "";
        }
    }

    public static String encode(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream inputStream = new FileInputStream(file);
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
            byte[] buffer = new byte[4096];

            while(digestInputStream.read(buffer) > -1) {
                ;
            }

            MessageDigest digest = digestInputStream.getMessageDigest();
            digestInputStream.close();
            byte[] md5 = digest.digest();
            StringBuilder sb = new StringBuilder();
            byte[] var8 = md5;
            int var9 = md5.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                byte b = var8[var10];
                sb.append(String.format("%02X", new Object[]{Byte.valueOf(b)}));
            }

            return sb.toString().toLowerCase();
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }
}
