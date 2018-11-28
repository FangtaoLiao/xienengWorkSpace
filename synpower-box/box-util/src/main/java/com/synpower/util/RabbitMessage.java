package com.synpower.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;


/*****************************************************************************
 * @Package: com.synpower.util
 * ClassName: RabbitMessage
 * @Description: 用于接收数采端对指令下发的回执信息
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月29日上午9:22:50   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public class RabbitMessage {
	
	/**
	 * 存放回执信息
	 */
	public static Map<String, List<Map<String, String>>>rabbitMessage=new ConcurrentHashMap<>();
	/**
	 * 存放待清理过期的回执消息队列
	 */
	public static List<String> seesionClearQueue=Collections.synchronizedList(new ArrayList<>());
	/**
	 * 存放下发电表的回执信息
	 */
	public static Map<String, Map<String, Object>> rabbitDataModeMessage = new ConcurrentHashMap<>();
	/**
	 * 存放待清理过期的下发电表回执消息队列
	 */
	public static List<String> seesionClearQueueDataMode=Collections.synchronizedList(new ArrayList<>());
	/** 
	  * @Title:  addElement 
	  * @Description:  添加静态变量元素
	  * @param key
	  * @param element: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:22:40
	*/ 
	public synchronized static void  addElement(String key,List<Map<String, String>> element){
		rabbitMessage.put(key, element);
	}
	/** 
	  * @Title:  addElement 
	  * @Description:  添加下发电表中静态变量元素
	  * @param key
	  * @param element: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:22:40
	*/ 
	public synchronized static void  addDataModeElement(String key,Map<String, Object> element){
		rabbitDataModeMessage.put(key, element);
		System.out.println(RabbitMessage.rabbitDataModeMessage.hashCode());
		System.out.println("添加时："+rabbitDataModeMessage);
	}
	/** 
	  * @Title:  daleteElement 
	  * @Description:  删除静态变量元素 
	  * @param key: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:22:28
	*/ 
	public synchronized static void daleteElement(String key){
		rabbitMessage.remove(key);
	}
	/** 
	  * @Title:  rabbitDataModeMessage 
	  * @Description:  删除下发电表中静态变量元素 
	  * @param key: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:22:28
	*/ 
	public synchronized static void daleteDataModeElement(String key){
		rabbitDataModeMessage.remove(key);
	}
	/** 
	  * @Title:  getReturnMessage 
	  * @Description:  根据sessionId得到数采端回执的信息  默认超时时间3s 
	  * @param sessionId: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:23:43
	*/ 
	public static List<Map<String, String>> getReturnMessage(String sessionId){
		int timeOut=Integer.parseInt(PropertiesUtil.getValue("config.properties","RABBIT_RECEIVE_TIME_OUT"));
		long startTime=System.currentTimeMillis();
		List<Map<String, String>> result=null;
		while(System.currentTimeMillis()-startTime<=timeOut&&result==null){
			result=RabbitMessage.rabbitMessage.get(sessionId);
		}
		if (result!=null&&!result.isEmpty()) {
			RabbitMessage.rabbitMessage.remove(sessionId);
		}else{
			RabbitMessage.seesionClearQueue.add(sessionId);
		}
		return result;
	}
	/** 
	  * @Title:  getReturnDataModeMessage 
	  * @Description:  根据collSn得到数采端下发电表回执的信息  默认超时时间3s 
	  * @param sessionId: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日上午9:23:43
	*/ 
	public static Map<String, Object> getReturnDataModeMessage(String collSn){
//		int timeOut=Integer.parseInt(PropertiesUtil.getValue("config.properties","RABBIT_RECEIVE_TIME_OUT"));
		int timeOut=30000;
		long startTime=System.currentTimeMillis();
		Map<String, Object> result=null;
		while(System.currentTimeMillis()-startTime<=timeOut&&result==null){
			System.out.println(RabbitMessage.rabbitDataModeMessage.hashCode());
			result=RabbitMessage.rabbitDataModeMessage.get(collSn);
		}
		if (result!=null&&!result.isEmpty()) {
			RabbitMessage.rabbitDataModeMessage.remove(collSn);
		}else{
			RabbitMessage.seesionClearQueueDataMode.add(collSn);
		}
		return result;
	}
	/** 
	  * @Title:  clearCache 
	  * @Description: 清除缓存中无效的信息
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月29日下午2:06:43
	*/ 
	public static void  clearCache(){
		for (int i = 0; i < RabbitMessage.seesionClearQueue.size(); i++) {
			String key=RabbitMessage.seesionClearQueue.get(i);
			RabbitMessage.rabbitMessage.remove(key);
			RabbitMessage.seesionClearQueue.remove(i);
			i--;
		}
	}
}
