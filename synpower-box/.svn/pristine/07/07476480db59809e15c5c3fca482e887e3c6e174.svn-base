package com.synpower.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Taylor
 *
 */
public class AESUtil {
	private static Logger log=LoggerFactory.getLogger(AESUtil.class);
	/** 
	  * @Title: encrypt 
	  * @Description: AES 加密
	  * @param: @param content 需要加密的内容
	  * @param: @param password 加密密钥
	  * @param: @param type 128位或256位
	  * @param: @return
	  * @return: String
	  * @lastEditor: Taylor
	  * @lastEdit: 2017年7月17日下午4:29:43
	*/ 
	private static String encrypt(String content, String password,int type) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(type, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] byteCode = cipher.doFinal(byteContent);// 加密
			
			return parseByte2HexStr(byteCode); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	  * @Title: decrypt 
	  * @Description: AES 解密 
	  * @param: @param code 密文
	  * @param: @param password 解密密钥
	  * @param: @param type
	  * @param: @return
	  * @return: String
	  * @lastEditor: Taylor
	  * @lastEdit: 2017年7月17日下午4:30:33
	*/ 
	private static String decrypt(String code, String password, int type) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(type, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] byteCode = parseHexStr2Byte(code);
			byte[] content = cipher.doFinal(byteCode);//解密
			try {
				return new String(content, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	/**
	 * AES256位加密
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt256(String content, String password) {
		return AESUtil.encrypt(content, password, 256);
	}

	/**
	 * AES256位解密
	 * 
	 * @param code
	 * @param password
	 * @return
	 */
	public static String decrypt256(String code, String password) {
		return AESUtil.decrypt(code, password, 256);
	}

	/**
	 * AES128位加密
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt128(String content, String password) {
		return AESUtil.encrypt(content, password, 128);
	}

	/**
	 * AES128位解密
	 * 
	 * @param code
	 * @param password
	 * @return
	 */
	public static String decrypt128(String code, String password) {
		return AESUtil.decrypt(code, password, 128);
	}
/*

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String s = encrypt256("admin","synpower@2017");
		System.out.println(s);
		String s1 = decrypt256(s,"synpower@2017");
		System.out.println(s1);
	}*/
}
