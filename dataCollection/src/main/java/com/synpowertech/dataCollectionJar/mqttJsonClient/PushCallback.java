package com.synpowertech.dataCollectionJar.mqttJsonClient;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.utils.JsonParseTask;
import com.synpowertech.dataCollectionJar.utils.ThreadPool;
import com.synpowertech.dataCollectionJar.utils.XmlParseTask;  
  
/**  
 * 发布消息的回调类  
 *   
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。  
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。  
 * 必须在回调类中实现三个方法：  
 *   
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。  
 *   
 *  public void connectionLost(Throwable cause)在断开连接时调用。  
 *   
 *  public void deliveryComplete(MqttDeliveryToken token))  
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。  
 *  由 MqttClient.connect 激活此回调。  
 *   
 */  

public class PushCallback implements MqttCallback {  

	private static final Logger logger = LoggerFactory.getLogger(PushCallback.class);
	
	public void connectionLost(Throwable cause) {
		logger.error("其中一个动态连接断开,正在重连......cause:{}",cause);
	}

    public void deliveryComplete(IMqttDeliveryToken token) {
    	//系列统一，封装处给信息
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
    	
    	String collsn = "";
    	
    	String jsonStr = new String(message.getPayload(), "UTF-8");
		//TODO 前期数据分析使用，正式版本取消
		logger.info(jsonStr);
		if (jsonStr == null || "".equals(jsonStr)) {
			return;
		}
		//TODO 直接过滤，包含新数采sn才进入解析
		if (StringUtils.isNotBlank(topic)) {
			
			String[] topics = topic.split("_");
			if (topics.length == 3) {
				collsn = topics[1];
			}
		}
		//确认是新数采发布的主题才解析
		if (StringUtils.isNotBlank(collsn) && DeviceCache.newCollectorIdList.contains(collsn)) {
			
			ThreadPool.executeXmlParse_task(new JsonParseTask(collsn,jsonStr));
		}
    }  
}