package com.synpower.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * 字符类型操作工具
 * 
 * @author dellpc
 *
 */
public class StringUtil {

	public static String createUUID() {
		return UUID.randomUUID().toString();
	}

	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("文件保存路径为:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();

	}

	/**
	 * 把LIST转化成字符串
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i));
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	/**
	 * 把字符串分割成list
	 * @param string
	 * @param separator
	 * @return
	 */

	public static List<String> string2List(String string, String separator) {

		if (StringUtils.isBlank(string)) {
			return null;
		}
		String[] str = string.split(separator);
		List<String> strings = Arrays.asList(str);

		return strings;

	}
	public static String createSessionId(){
		StringBuffer time=new StringBuffer();
		time.append(System.currentTimeMillis());
		time.insert(0,(int)(Math.random()*10));
		time.append((int)(Math.random()*10));
		return time.toString();
	}
	public static boolean isBlank(String str){
		if (null==str||"".equals(str)||"null".equals(str)) {
			return true;
		}
		return false;
	}
	public static String checkBlank(String str){
		return isBlank(str)?"":str;
	}
	public static String appendStr(Object ... obj){
		StringBuffer buff=new StringBuffer();
		if (Util.isNotBlank(obj)) {
			for (Object object : obj) {
				buff.append(" ").append(object).append(" ");
			}
		}
		return buff.toString();
	}
	public static String appendStrNoBlank(Object ... obj){
		StringBuffer buff=new StringBuffer();
		if (Util.isNotBlank(obj)) {
			for (Object object : obj) {
				buff.append(object);
			}
		}
		return buff.toString();
	}
}
