package com.synpower.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Taylor
 *
 */
public class MD5 {
	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

    /**
     * 加密
     * @param content
     * @return
     */
    public static String getMD5Code(String content) {
        String resultString = null;
        try {
            resultString = new String(content);
            MessageDigest md = MessageDigest.getInstance("MD5");            
            resultString = byteToHexString(md.digest(content.getBytes()));//md.digest() 返回值为存放哈希值结果的byte数组
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}