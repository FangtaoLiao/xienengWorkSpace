package com.synpowertech.dataCollectionJar.mqttJsonClient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.synpowertech.dataCollectionJar.initialization.SynConstant;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.newColl
 * @ClassName: MqttActiveConnectClient
 * @Description: 自研数采动态连接实例
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年4月13日上午10:03:16   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class MqttActiveConnectClient {

	public MqttClient mqttClientInstance;
	public MqttConnectOptions options;
	//public MqttActiveConnectClient client;
	// 连接代理服务器唯一标识
	public String clientId;

	public MqttActiveConnectClient(String clientId) throws MqttException {
		super();
		this.clientId = clientId;
		
		//连接设置
		setConnOptions();
		//开始连接
		start(clientId);
	}

	public MqttActiveConnectClient() {
		super();
	}

	public void setConnOptions() {
		// MQTT的连接设置
		options = new MqttConnectOptions();
		// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
		options.setCleanSession(SynConstant.CLEANSESSION);
		// 设置连接的用户名
		options.setUserName(SynConstant.USERNAME);
		// 设置连接的密码
		options.setPassword(SynConstant.PWD.toCharArray());
		// 设置不自动重连,使用默认1,2,4,8秒递增重连机制
		options.setAutomaticReconnect(true);
		// 设置超时时间 单位为秒
		options.setConnectionTimeout(SynConstant.TIMEOUT);
		// 设置会话心跳时间 单位为秒 服务器会每隔1.5*30秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
		options.setKeepAliveInterval(SynConstant.ALIVEINTERVAL);
	}

	public void start(String connectId) throws MqttException {
		// host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
		mqttClientInstance = new MqttClient(SynConstant.HOST, connectId, new MemoryPersistence());
		// 设置回调
		mqttClientInstance.setCallback(new PushCallback());
		//直接连接
		mqttClientInstance.connect(options);
	}

	// 发布消息
	public Boolean publish(String collectorSn, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = mqttClientInstance.getTopic("syn_" + collectorSn + "_sub").publish(message);
		// waitForCompletion使得异步消息变成同步
		token.waitForCompletion();
		return token.isComplete();
	}

	public Boolean publish(String collectorSn,int qos,String message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = mqttClientInstance.getTopic("syn_" + collectorSn + "_sub").publish(message.getBytes(), qos, false);
		// waitForCompletion使得异步消息变成同步
		token.waitForCompletion();
		return token.isComplete();
	}

	public Boolean publish(String topic, String message, int qos, boolean retained)throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = mqttClientInstance.getTopic(topic).publish(message.getBytes(), qos, retained);
		// waitForCompletion使得异步消息变成同步
		token.waitForCompletion();
		return token.isComplete();
	}
	
	/**
	  * @Title:  publishCtrlMsg 
	  * @Description:  TODO 新数采发布遥控调信息或者新数采下发设置信息
	  * @param collectorSn
	  * @param message
	  * @return
	  * @throws MqttPersistenceException
	  * @throws MqttException: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月19日上午11:43:07
	 */
	public Boolean publishCtrlMsg(String collectorSn,String message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = mqttClientInstance.getTopic("syn_" + collectorSn + "_sub").publish(message.getBytes(), 0, false);
		// waitForCompletion使得异步消息变成同步
		token.waitForCompletion();
		return token.isComplete();
	}
	
	/**
	  * @Title:  publishSetTimeMsg 
	  * @Description:  TODO 发布对时消息，默认使用Qos0
	  * @param collectorSn
	  * @param message
	  * @return
	  * @throws MqttPersistenceException
	  * @throws MqttException: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日下午3:03:34
	 */
	public Boolean publishSetTimeMsg(String collectorSn,String message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = mqttClientInstance.getTopic("syn_" + collectorSn + "_sub").publish(message.getBytes(), 0, false);
		// waitForCompletion使得异步消息变成同步
		//token.waitForCompletion();
		return token.isComplete();
	}

}