package com.synpowertech.dataCollectionJar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.utils
 * @ClassName: PropertyUtil
 * @Description: properties文件获取工具类 ,在该类被加载的时候，它就会自动读取指定位置的配置文件内容并保存到静态属性中，高效且方便，一次加载，可多次使用。(方法5)
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月2日上午11:20:13   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class PropertyUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	private static Properties props;
	static {
		loadProps();
	}

	synchronized static private void loadProps() {
		logger.info("start to load properties's text......");
		props = new Properties();
		InputStream in = null;
		try {
			//第一种，通过类加载器进行获取properties文件流
			in = PropertyUtil.class.getClassLoader().getResourceAsStream("spring-mqtt.properties");
			//第二种，通过类进行获取properties文件流
			// in = PropertyUtil.class.getResourceAsStream("/synpowertech.properties");
			props.load(in);
		} catch (FileNotFoundException e) {
			logger.error("spring-mqtt.properties can not be found!:{}",e);
		} catch (IOException e) {
			logger.error("there is an IOException of getting properties：{}",e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("spring-mqtt.properties of closing the InputStream:{} ",e);
			}
		}
		logger.info("load the properties's text succefully...........");
		logger.debug("properties's text：{}",props);
	}

	/**
	  * @Title:  getProperty 
	  * @Description:  静态方法直接调用获取key的值
	  * @param key
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月2日上午11:23:12
	 */
	public static String getProperty(String key) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key);
	}

	/**
	  * @Title:  getProperty 
	  * @Description:  获取属性值
	  * @param key
	  * @param defaultValue
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:45:07
	 */
	public static String getProperty(String key, String defaultValue) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}
}