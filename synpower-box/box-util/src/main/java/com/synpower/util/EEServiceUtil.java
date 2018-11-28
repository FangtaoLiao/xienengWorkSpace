package com.synpower.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author ybj
 * @date 2018年9月6日下午12:43:55
 * @Description -_-能耗系统服务工具类 主要是吧服务中通用的代码提出来
 */
public class EEServiceUtil {

	private EEServiceUtil() {
	}

	public static void addAppidAndSecret(Map<String, String> map, String requestType) {

		Properties prop = new Properties();
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String appid2 = prop.getProperty("appid2");
		String secret2 = prop.getProperty("secret2");
		String appid3 = prop.getProperty("appid3");
		String secret3 = prop.getProperty("secret3");

		if ("3".equals(requestType)) {
			map.put("appid", appid3);
			map.put("secret", secret3);
			// 默认为小电能控
		} else {
			map.put("appid", appid2);
			map.put("secret", secret2);
		}
	}

}
