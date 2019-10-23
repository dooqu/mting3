package cn.xylink.mting.utils;

import android.util.Base64;

import java.security.MessageDigest;

/**
 * Created by wjn on 2019/2/28.
 */

public class SafeUtils {
    public static String getRsaString(String data, String key) throws Exception {
        byte[] encdata = RSACoder.encryptByPublicKey1(data.getBytes(),
                key);
        String encdatastr = Base64.encodeToString(encdata, Base64.DEFAULT);
        return encdatastr;
    }

    /**
     * String转MD5
     *
     * @param inStr
     * @return
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

}
