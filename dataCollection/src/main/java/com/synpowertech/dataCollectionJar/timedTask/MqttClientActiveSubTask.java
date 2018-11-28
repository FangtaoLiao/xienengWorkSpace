package com.synpowertech.dataCollectionJar.timedTask;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.mqttJsonClient.MqttActiveConnectClient;


public class MqttClientActiveSubTask implements Job{
	
	private static final Logger logger = LoggerFactory.getLogger(MqttClientActiveSubTask.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		//需新增的连接客户端数，可能为负数
		int newClientCount = DeviceCache.newClientCount - DeviceCache.clientList.size();
		
		String clientIdTemp = null;
		//新增客户端
		if (newClientCount > 0) {
			for (int i = 0; i < newClientCount; i++) {
				//先查看栈中是否有回收的连接实例
				try {
					if (DeviceCache.clientStack.size() > 0) {
						MqttActiveConnectClient popClient = DeviceCache.clientStack.pop();
						//弹栈连接实例
						clientIdTemp = popClient.clientId;
						popClient.mqttClientInstance.connect(popClient.options);
						//更新连接实例缓存
						DeviceCache.clientList.add(popClient);
					} else {
						//新建连接实例
						clientIdTemp = "synpower/activeSub" + (DeviceCache.clientList.size() + i);
						//更新连接实例缓存
						DeviceCache.clientList.add(new MqttActiveConnectClient(clientIdTemp));
					}
					logger.info("增加连接：" + clientIdTemp);
				} catch (MqttException e) {
					logger.error("get an error when adding the connection of {}:{}",clientIdTemp,e);
				}
				
			}
		}
		
		//删减客户端
		if (newClientCount < 0) {
			for (int i = 0; i < -newClientCount; i++) {
				try {
					MqttActiveConnectClient pushClient = DeviceCache.clientList.get(DeviceCache.clientList.size() -1 );
					//断连最后一个客户端
					pushClient.mqttClientInstance.disconnect();
					clientIdTemp = pushClient.clientId;
					//保存在栈中
					DeviceCache.clientStack.push(pushClient);
					//更新连接实例缓存
					// DeviceCache.clientList.remove(DeviceCache.clientList.size()-1);
					DeviceCache.clientList.remove(pushClient);
					logger.info("删除连接：" + clientIdTemp);
				} catch (MqttException e) {
					logger.error("get an error when removing the connection of {}:{}",clientIdTemp,e);
				}
			}
		}
		
		//刷新订阅
		for (int i = 0; i < DeviceCache.clientList.size(); i++) {
			try {
				MqttActiveConnectClient activeConnectClient = DeviceCache.clientList.get(i);
				clientIdTemp = activeConnectClient.clientId;
				activeConnectClient.mqttClientInstance.subscribe(DeviceCache.subTopicList.get(i), DeviceCache.subQosList.get(i));
			} catch (MqttException e) {
				logger.error("get an error when resubscribing the connection of {}:{}",clientIdTemp,e);
			}
		}
		
		logger.info("success to MqttClientActiveSubTask");
		logger.info("DeviceCache.clientStack.size()：" + DeviceCache.clientStack.size());
		logger.info("DeviceCache.clientList.size()：" + DeviceCache.clientList.size());
		
	}
	
	/**
	  * @Title:  refreshClients 
	  * @Description:  TODO 下发点表时若缓存未刷新，手动刷新
	  * @return: boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年6月11日下午2:17:52
	 */
	public static boolean refreshClients(){
		//需新增的连接客户端数，可能为负数
		int newClientCount = DeviceCache.newClientCount - DeviceCache.clientList.size();
		
		String clientIdTemp = null;
		//新增客户端
		if (newClientCount > 0) {
			for (int i = 0; i < newClientCount; i++) {
				//先查看栈中是否有回收的连接实例
				try {
					if (DeviceCache.clientStack.size() > 0) {
						MqttActiveConnectClient popClient = DeviceCache.clientStack.pop();
						//弹栈连接实例
						clientIdTemp = popClient.clientId;
						popClient.mqttClientInstance.connect(popClient.options);
						//更新连接实例缓存
						DeviceCache.clientList.add(popClient);
					} else {
						//新建连接实例
						clientIdTemp = "synpower/activeSub" + (DeviceCache.clientList.size() + i);
						//更新连接实例缓存
						DeviceCache.clientList.add(new MqttActiveConnectClient(clientIdTemp));
					}
					logger.info("增加连接：" + clientIdTemp);
				} catch (MqttException e) {
					logger.error("get an error when adding the connection of {}:{}",clientIdTemp,e);
				}
				
			}
		}
		
		//删减客户端
		if (newClientCount < 0) {
			for (int i = 0; i < -newClientCount; i++) {
				try {
					MqttActiveConnectClient pushClient = DeviceCache.clientList.get(DeviceCache.clientList.size() -1 );
					//断连最后一个客户端
					pushClient.mqttClientInstance.disconnect();
					clientIdTemp = pushClient.clientId;
					//保存在栈中
					DeviceCache.clientStack.push(pushClient);
					//更新连接实例缓存
					// DeviceCache.clientList.remove(DeviceCache.clientList.size()-1);
					DeviceCache.clientList.remove(pushClient);
					logger.info("删除连接：" + clientIdTemp);
				} catch (MqttException e) {
					logger.error("get an error when removing the connection of {}:{}",clientIdTemp,e);
				}
			}
		}
		
		//刷新订阅
		for (int i = 0; i < DeviceCache.clientList.size(); i++) {
			try {
				MqttActiveConnectClient activeConnectClient = DeviceCache.clientList.get(i);
				clientIdTemp = activeConnectClient.clientId;
				activeConnectClient.mqttClientInstance.subscribe(DeviceCache.subTopicList.get(i), DeviceCache.subQosList.get(i));
			} catch (MqttException e) {
				logger.error("get an error when resubscribing the connection of {}:{}",clientIdTemp,e);
			}
		}
		
		logger.info("success to MqttClientActiveSubTask");
		logger.info("DeviceCache.clientStack.size()：" + DeviceCache.clientStack.size());
		logger.info("DeviceCache.clientList.size()：" + DeviceCache.clientList.size());
		return true;
	}
}
