package com.synpowertech.dataCollectionJar.initialization;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.utils.ThreadPool;
import com.synpowertech.dataCollectionJar.utils.XmlParseTask;
/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.initialization
 * @ClassName: MqttMessageConsumer
 * @Description: Mqtt订阅的消息接收和处理的类
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月16日下午3:14:45   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */

public class MqttMessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MqttMessageConsumer.class);

	// static int count = 1;
	public void messageArrived(String message) throws UnsupportedEncodingException {
		//用于测试消息处理效率
	/*	if (count % 5000 == 1) {
			logger.info( count + "时间节点：" + new Date().getTime());
			if (count == Integer.MAX_VALUE) {
				count =1;
			}
		}
		count++;*/

		String xmlStr = new String(message.getBytes(), "UTF-8");
		//TODO 前期数据分析使用，正式版本取消
		logger.info(xmlStr);
		if (xmlStr == null || "".equals(xmlStr)) {
			return;
		}
		ThreadPool.executeXmlParse_task(new XmlParseTask(xmlStr));
	}
}