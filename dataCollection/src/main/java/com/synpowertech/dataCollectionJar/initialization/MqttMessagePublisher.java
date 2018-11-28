package com.synpowertech.dataCollectionJar.initialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.initialization
 * @ClassName: MqttMessagePublisher
 * @Description: 下发mqtt信息到代理服务器的类
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月16日下午3:15:34   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class MqttMessagePublisher {  
	// @Resource
	// private MqttPahoMessageHandler mqtt;
    
	private static final Logger logger = LoggerFactory.getLogger(MqttMessagePublisher.class);

	//用做发送对时
    private static MqttPahoMessageHandler handler4topicSetTime;
    //用做发布遥控遥调信息
    private static MqttPahoMessageHandler handler4topicYkYt;  

    public  MqttPahoMessageHandler getHandler4topicSetTime() {
		return handler4topicSetTime;
	}

	public  void setHandler4topicSetTime(MqttPahoMessageHandler handler4topicSetTime) {
		MqttMessagePublisher.handler4topicSetTime = handler4topicSetTime;
	}

	public  MqttPahoMessageHandler getHandler4topicYkYt() {
		return handler4topicYkYt;
	}

	public  void setHandler4topicYkYt(MqttPahoMessageHandler handler4topicYkYt) {
		MqttMessagePublisher.handler4topicYkYt = handler4topicYkYt;
	}


	
	public void initMethod() {
		try {
			//设置对时相关参数
			handler4topicSetTime.setDefaultQos(0);
			handler4topicSetTime.setAsync(false);
			handler4topicSetTime.setDefaultRetained(false);
			handler4topicSetTime.setDefaultTopic(SynConstant.MqttSetTime);
			
			//设置遥控遥调相关参数
			//
			handler4topicYkYt.setDefaultQos(0);
			handler4topicYkYt.setAsync(false);
			handler4topicYkYt.setDefaultRetained(false);
			handler4topicYkYt.setDefaultTopic(SynConstant.MqttYkYt);
			
			logger.info("MqttMessagePublisher inits succefully！");
			
		} catch (Exception e) {
			logger.error("MqttMessagePublisher inits unsuccefully:{}",e);
		}
	}
	
	
	
	public static boolean setTime(String xmlStr) {
		Message<String> message = MessageBuilder.withPayload(xmlStr).build();
		try {
			//同步发送
			//message.wait();
			handler4topicSetTime.handleMessage(message);
			logger.info("the message to set time is sent successfully!");
			return true;
		} catch (Exception e) {
			logger.error("the message to set time is sent unsuccessfully:{}",e);
		}

		return false;
	}
	
	public static boolean ykYt(String xmlStr){  
		Message<String> message = MessageBuilder.withPayload(xmlStr).build();
		try {
			handler4topicYkYt.handleMessage(message);
			logger.info("the message to ykYt is sent successfully!:{}",xmlStr);
			return true;

		} catch (Exception e) {
			logger.error("the message to ykYt is sent unsuccessfully:{}",e);
		}

		return false;
	}
}  