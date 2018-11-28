package com.synpowertech.dataCollectionJar.timedTask;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.synpowertech.dataCollectionJar.dao.DataStringInverterMapper;
import com.synpowertech.dataCollectionJar.domain.DataStringInverterSimple;
import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil2Fix60ktl;
import com.synpowertech.dataCollectionJar.initialization.MqttMessagePublisher;
import com.synpowertech.dataCollectionJar.utils.CacheMappingUtil;
import com.synpowertech.dataCollectionJar.utils.JsonParseUtil;
import com.synpowertech.dataCollectionJar.utils.XmlParseUtil;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.TimedTask
 * @ClassName: SyncTimeTask
 * @Description: 定时与数据采集节点下设备对时，保证数据实时性，同时记录使用60ktl点表设备今天日发电量
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月6日下午2:42:22   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class SyncTimeTask implements Job ,InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(SyncTimeTask.class);
	
	// @Autowired
	// private DeviceMapper deviceMapper;
	@Autowired
	private DataStringInverterMapper dataStringInverterMapperTemp;
	
	static DataStringInverterMapper dataStringInverterMapper;
	
	public void afterPropertiesSet() throws Exception {
		dataStringInverterMapper = dataStringInverterMapperTemp;
		logger.info("fix60ktl-dailyEnergyMap-task  prepares succefully！");
	}
	
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		Set<String> keySet = DeviceCache.collector2devSet.keySet();
		//①旧数采对时
		Set<String> keySet = DeviceCache.oldCollectorIdSet;
		if (keySet.size() != 0) {
			for (String collectorId : keySet) {
				MqttMessagePublisher.setTime(XmlParseUtil.setTimeXml(collectorId));
			}
		}
		logger.info("oldCollector SyncTimeTask ends！");
		
		//TODO ②新数采对时
		List<String> newCollSnList = DeviceCache.newCollectorIdList;
		if (newCollSnList.size() != 0) {
			Integer clientIdx = null;
			String setTimeJson = null;
			for (String collSn : newCollSnList) {
				clientIdx = CacheMappingUtil.collSn2ClientIdx(collSn);
				if (clientIdx != null) {
					try {
						setTimeJson = JsonParseUtil.setTimeJson();
						DeviceCache.clientList.get(clientIdx).publishSetTimeMsg(collSn, setTimeJson);
					} catch (Exception e) {
						logger.info("newCollector syncTimeTask error:{},{}",collSn,e.getClass());
					}
					logger.info(collSn + ":" + setTimeJson);
				}
			}
		}
		logger.info("newCollector SyncTimeTask ends！");
		
		//③23点天黑，记录使用60KTL的设备的今日的总发电量
//		List<Integer> select60ktlAList = deviceMapper.select60ktlA();
		List<DataStringInverterSimple> selectTimeResult = dataStringInverterMapper.selectTimeResult();
		
		if (selectTimeResult != null) {
			//HashMap<Integer, Float> dailyEnergyMapTemp = new HashMap<Integer, Float>(4);
			//selectTimeResult.stream().forEach(object -> dailyEnergyMapTemp.put(object.getDeviceId(), object.getTotalEnergy()));
			//DeviceCache.dailyEnergyMap = dailyEnergyMapTemp;
			DeviceCache.dailyEnergyMap = new HashMap<String, String>(5);
			selectTimeResult.stream().forEach(object -> DeviceCache.dailyEnergyMap.put(String.valueOf(object.getDeviceId()), String.valueOf(object.getTotalEnergy())));
			JedisUtil2Fix60ktl.setMap("60ktl_a_YC_48_0", DeviceCache.dailyEnergyMap);
		}
		logger.info("60ktl前一天发电量缓存DeviceCache.dailyEnergyMap：{}",DeviceCache.dailyEnergyMap.toString());
		logger.info("success to create 60ktlAList maxTime totalEnergyMap ！");

	}
}
