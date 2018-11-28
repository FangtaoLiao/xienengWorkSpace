package com.synpower.listener;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.synpower.util.JsonSmartUtil;
import com.synpower.util.RabbitMessage;
import com.synpower.util.Util;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.rabbitMq
 * @ClassName: RabbitMqConsumer
 * @Description: rabitMq消息接收测试类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月10日上午11:46:05 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class RabbitMqConsumer implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);

	public RabbitMqConsumer() {
		logger.info("rabbit客户端实例化成功");
	}

	public void onMessage(Message message) {
		/**
		 * Map{ sessionId : value: List{ Map{ .... } } }
		 * 
		 */
		byte[] bytes = message.getBody();
		String msg = JsonSmartUtil.bytesTostr(bytes);
		logger.info(" rabbitMq接受到信息: " + msg);
		System.out.println(" rabbitMq接受到信息: " + msg);
		Map<String, Object> reciveMap = JsonSmartUtil.bytesToMap(bytes);
		System.out.println(reciveMap);
		if (Util.isNotBlank(String.valueOf(reciveMap.get("sessionId")))) {
			RabbitMessage.addElement(String.valueOf(reciveMap.get("sessionId")),
					(List<Map<String, String>>) reciveMap.get("value"));
		} else {
			RabbitMessage.addDataModeElement(String.valueOf(reciveMap.get("collSn")), reciveMap);
		}
		logger.info(" RabbitMessage缓存消息插入成功");
	}

}
