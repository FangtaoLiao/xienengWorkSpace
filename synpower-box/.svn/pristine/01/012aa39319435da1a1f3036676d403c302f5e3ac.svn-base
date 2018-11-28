package com.synpower.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

/**
 ****************************************************************************
 * @Package: com.synpowertech.utils
 * @ClassName: JsonSmartUtil
 * @Description: json-smart实现map的序列化与反序列化，性能最好。
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月1日下午1:56:05 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class JsonSmartUtil {

	/**
	 * @Title: bytes2Map
	 * @Description: 将字节数组转化成map
	 * @param bytes
	 * @return: Map<String,Map<String,String>>
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月1日下午1:55:34
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> bytesToMap(byte[] bytes) {

		Map<String, Object>map = null;
		try {
			map = (Map<String, Object>) JSONValue.parseStrict((new String(bytes, "UTF-8")));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return map;

	}

	/**
	  * @Title:  bytes2str 
	  * @Description: 字节数组转化字符串
	  * @param bytes
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月10日下午3:43:47
	 */
	public static String bytesTostr(byte[] bytes) {

		String str = null;
		try {
			str = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;

	}

	/**
	 * @Title: map2Bytes
	 * @Description: map序列化为字节数组
	 * @param map
	 * @return: byte[]
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月1日下午1:54:41
	 */
	public static byte[] map2Bytes(Map<String, Map<String, String>> map) {

		String str = JSONObject.toJSONString(map);
		byte[] result = null;
		try {
			result = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
