package com.tj.download.utills;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jun on 17/8/6.
 */

public class Md5Utils {

    public static String generateCode(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        //get the md5 algorithm
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(url.getBytes());

            byte[] cipher = digest.digest();

            //transfer
            for (byte b : cipher) {
                // transfer 16
                String hexStr = Integer.toHexString(b&0xff);
                buffer.append(hexStr.length()==1?"0"+hexStr:hexStr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
