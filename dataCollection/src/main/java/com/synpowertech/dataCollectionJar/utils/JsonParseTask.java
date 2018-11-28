package com.synpowertech.dataCollectionJar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: JsonParseTask
 * @Description: 接收到的基于mqtt协议json格式字符串解析任务
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年4月8日下午3:02:59 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class JsonParseTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(JsonParseTask.class);

	private String collsn;
	private String jsonStr;

	public JsonParseTask(String collsn, String jsonStr) {
		super();
		this.collsn = collsn;
		this.jsonStr = jsonStr;
	}

	public JsonParseTask() {

	}

	public void run() {

		logger.debug("新数采进入解析***");
		// 执行数据解析任务,包含Session的信息是遥控遥调结果信息，其他是遥测遥信遥脉信息
		try {
			if (!jsonStr.contains("ctrl_key")) {
				logger.debug("新数采解析进入方法*******************");
				JsonParseUtil.jsonStr2Map(collsn, jsonStr);
			} else {
				JsonParseUtil.jsonResultStr2Map(collsn, jsonStr);
			}
		} catch (Exception e) {
			logger.warn("jsonStr parse error:{}", e.getClass());
			e.printStackTrace();
		}
	}

}
