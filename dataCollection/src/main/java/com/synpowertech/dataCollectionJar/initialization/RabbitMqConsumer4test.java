package com.synpowertech.dataCollectionJar.initialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.synpowertech.dataCollectionJar.utils.JsonSmartUtil;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.rabbitMq
 * @ClassName: RabbitMqConsumer
 * @Description: rabitMq消息接收测试类
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月10日上午11:46:05   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
//@Component
public class RabbitMqConsumer4test implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumer4test.class);

	public RabbitMqConsumer4test() {
		logger.info("rabbit客户端实例化成功");
	}

	public void onMessage(Message message) {
		logger.debug("rabbitMq接受到信息：" +message);
		logger.debug("rabbitMq接受到信息：" +message.getBody());
		logger.debug("rabbitMq接受到信息：" +JsonSmartUtil.bytes2str(message.getBody()));
	}

}
