package com.synpowertech.dataCollectionJar.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.utils.XmlParseUtil;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: XmlParseTask
 * @Description: 接收到的基于mqtt协议xml格式字符串解析任务
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月16日下午3:50:45   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class XmlParseTask implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(XmlParseTask.class);
	
	String xmlStr;
	
	public XmlParseTask(String xmlStr) {
		super();
		this.xmlStr = xmlStr;
	}
	
	public XmlParseTask() {

	}
	
	public void run() {
		
		logger.debug("旧数采进入解析***");
		//执行数据解析任务,包含Session的信息是遥控遥调结果信息，其他是遥测遥信遥脉信息
		if (!xmlStr.contains("Session")) {
			logger.debug("旧数采解析进入方法*******************");
			//遥测遥信遥脉
			XmlParseUtil.xmlStr2Map(xmlStr);
		} else {
			try {
				XmlParseUtil.xmlResultStr2Map(xmlStr);
				
			} catch (IOException e) {
				logger.error("xmlResultStr2Map get an exception:{}",e);
			}
		}
	}
	
}
