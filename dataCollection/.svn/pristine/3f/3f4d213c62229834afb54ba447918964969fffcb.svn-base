package com.synpowertech.dataCollectionJar.timedTask;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil2Fix60ktl;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.timedTask
 * @ClassName: FixDataTask
 * @Description: 手动计算使用60ktl点表设备的日发电量
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年3月19日下午4:47:59 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class FixDataTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(FixDataTask.class);

	public static final String FIELD = "60ktl_a_YC_48_0";

	// 现在总发电量字段
	public static final String SOURCEFIELD = "60ktl_a_YC_47_0";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		// Set<Entry<Integer, Float>> entrySet =
		// DeviceCache.dailyEnergyMap.entrySet();
		Map<String, String> map = JedisUtil2Fix60ktl.getMap("60ktl_a_YC_48_0");

		if (map != null) {
			Set<Entry<String, String>> entrySet = map.entrySet();

			for (Entry<String, String> entry : entrySet) {
				String devId = entry.getKey();
				// 得到目前总发电量
				String value = JedisUtil.getStringInMap(devId, SOURCEFIELD);
				if (value != null) {
					JedisUtil.setStringInMap(devId, FIELD,String.valueOf(Float.parseFloat(value) - Float.parseFloat(entry.getValue())));
				}
			}
		}

		logger.debug("60ktl_a_YC_48_0 process ends!");
	}

}
