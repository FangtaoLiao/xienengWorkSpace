package com.synpowertech.dataCollectionJar.timedTask;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.DeviceCache;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.TimedTask
 * @ClassName: RefreshCacheTask
 * @Description: 定时刷新缓存任务，包括设备列表，设备点位映射关系等
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月6日下午2:33:49   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class RefreshCacheTask implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(RefreshCacheTask.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		DeviceCache.refreshDeviceCache();
		logger.debug("RefreshCacheTask ends!");
	}

	
}
