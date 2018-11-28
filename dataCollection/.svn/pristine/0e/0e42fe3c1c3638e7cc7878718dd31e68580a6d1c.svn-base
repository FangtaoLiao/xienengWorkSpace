package com.synpowertech.dataCollectionJar.timedTask;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.timedTask
 * @ClassName: ExamplePlantTask
 * @Description: 示例电站redis数据操作任务
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年1月19日下午2:27:54   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class ExamplePlantTask implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(ExamplePlantTask.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		//示例电站数采数据
		Map<String, String> redisDataOfColl = JedisUtil.getMap("3");
		if (redisDataOfColl != null) {
			for (Integer idTemp : DeviceCache.exampleCollList) {
				JedisUtil.setMapPipelined(String.valueOf(idTemp), redisDataOfColl);
			}
		}
		
		//示例电站逆变器奇数id数据
		Map<String, String> redisDataOfInverterOdd = JedisUtil.getMap("1");
		if (redisDataOfInverterOdd != null) {
			DeviceCache.exampleInverterList.stream().filter(devId -> devId % 2 == 1).forEach(devIdOdd -> JedisUtil.setMapPipelined(String.valueOf(devIdOdd), redisDataOfInverterOdd));
		}
		
		//示例电站逆变器奇数id数据
		Map<String, String> redisDataOfInverterEven = JedisUtil.getMap("2");
		if (redisDataOfInverterEven != null) {
			DeviceCache.exampleInverterList.stream().filter(devId -> devId % 2 == 0).forEach(devIdEven -> JedisUtil.setMapPipelined(String.valueOf(devIdEven), redisDataOfInverterEven));
		}
		
		logger.debug("ExamplePlantTask ends!");
	}

	
}
