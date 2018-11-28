package com.synpowertech.dataCollectionJar.utils;

//import static org.hamcrest.CoreMatchers.nullValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.Basic.Return;
/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: TimeUtil4Xml
 * @Description:mqtt信息中xml格式时间转化类
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月16日下午3:49:59   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class TimeUtil4Xml {

	private static final Logger logger = LoggerFactory.getLogger(TimeUtil4Xml.class);
	public static final int DIVISOR =  5*60*1000;
	
	// 2016-11-22T03:39:30Z,simpleDateForMate这样写有线程安全问题
	//static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	  * @Title:  xmlTime2Str 
	  * @Description:  mqtt信息中xml时间格式转化成毫秒数字符串
	  * @param xmlTimeStr
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:46:09
	 */
	public static String xmlTime2Str(String xmlTimeStr){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		Date date = null;
		try {
			date = format.parse(xmlTimeStr);
			return Long.toString(date.getTime());
		} catch (ParseException e) {
			logger.warn("xmlTime2Str failed:{}",e);
		}
		return null;
	}

	/**
	  * @Title:  getXmlTimeStr 
	  * @Description:  获取当前时间的mqtt信息格式字符串
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:47:18
	 */
	public static String getXmlTimeStr() {

		// 2016-11-22T03:39:30Z
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String XmlTimeStr = null;
		try {
			XmlTimeStr = format.format(new Date());
		} catch (Exception e) {
			logger.warn("getXmlTimeStr failed:{}",e);
		}
		return XmlTimeStr;
	}
	
	/**
	  * @Title:  getMsec 
	  * @Description: 获取接收到当前数据报文时间毫秒数
	  * @return: Long
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月24日下午2:41:58
	 */
	public static Long getMsec() {
		return new Date().getTime();
	}
	
	/**
	  * @Title:  getEarlierDataStoreMsec 
	  * @Description:  获取补采数据最邻近的前一个存储时间节点，默认五分钟定时入库 
	  * @param dataTime
	  * @return: Long
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月25日下午7:20:23
	 */
	public static Long getEarlierDataStoreMsec(Long dataTime) {
		double remainder = dataTime % DIVISOR;
		return  (long) (Math.floor(remainder)*DIVISOR);
	}
	
	/**
	  * @Title:  getLaterDataStoreMsec 
	  * @Description:  获取补采数据最邻近的后一个存储时间节点，默认五分钟定时入库 
	  * @param dataTime
	  * @return: Long
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月25日下午7:26:45
	 */
	public static Long getLaterDataStoreMsec(Long dataTime) {
		double remainder = dataTime % DIVISOR;
		return  (long) (Math.ceil(remainder)*DIVISOR);
	}

}
