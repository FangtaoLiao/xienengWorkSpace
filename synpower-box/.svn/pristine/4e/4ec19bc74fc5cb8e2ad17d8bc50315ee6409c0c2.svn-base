package com.synpower.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.initialization
 * @ClassName: RabbitMqProducer
 * @Description: RabbitMq消息发送的类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月16日下午3:16:33 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class RabbitMqProducer {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMqProducer.class);
	/*
	 * @Resource(name = "amqpTemplate") private static AmqpTemplate
	 * amqpTemplate;
	 */

	private static AmqpTemplate amqpTemplate;

	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		RabbitMqProducer.amqpTemplate = amqpTemplate;
		logger.info("rabbitMqProducer inits succefully！");
	}

	/**
	 * @Title: sendMessage
	 * @Description: abbbitMq发送消息的方法
	 * @param message
	 * @throws IOException:
	 *             void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月16日下午3:16:57
	 */
	public static void sendMessage(Object message) throws IOException {
		// amqpTemplate.convertAndSend("ykYtResultKey", message);
		//发送消息到默认交换机，默认通道(xml中配置)
		amqpTemplate.convertAndSend(message);
		logger.info("rabbitMqProducer sends a message succefully！");
	}

}
