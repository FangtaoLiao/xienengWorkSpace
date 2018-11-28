package com.synpowertech.dataCollectionJar.initialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.utils.PropertyUtil;
import com.synpowertech.dataCollectionJar.utils.Util4j;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.constant
 * @ClassName: Constant
 * @Description: 获取系统相关参数常量的值，如果没有设置就调用默认的
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月3日下午5:58:17 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class SynConstant {

	private static final Logger logger = LoggerFactory.getLogger(SynConstant.class);
	// 系统要发布的主题
	public static final String MqttYkYt = Util4j.getStr(PropertyUtil.getProperty("Mqtt.ykyt"), "ncyt_sub_yk_yt");
	public static final String MqttSetTime = Util4j.getStr(PropertyUtil.getProperty("Mqtt.setTime"),"ncyt_sub_SetTime");
	public static final int YcRecordListSize = Util4j.getIntValue4ListSize(PropertyUtil.getProperty("YcRecordList.size"),2500);

	//动态连接所需参数
	public static final String HOST = Util4j.getStr(PropertyUtil.getProperty("Mqtt.host"), "tcp://116.62.60.33:1883");
	public static final String USERNAME = Util4j.getStr(PropertyUtil.getProperty("Mqtt.username"), "admin");
	public static final String PWD = Util4j.getStr(PropertyUtil.getProperty("Mqtt.password"), "Synpower-2017");
	public static final Integer TIMEOUT = Util4j.getIntValue(PropertyUtil.getProperty("Mqtt.timeout"), 10);
	public static final Boolean CLEANSESSION = Util4j.getBoolean(PropertyUtil.getProperty("Mqtt.cleanSession"), true);
	public static final Integer ALIVEINTERVAL = Util4j.getIntValue(PropertyUtil.getProperty("Mqtt.aliveInterval"), 100);
//	public static final String CLIENTID1 = Util4j.getStr(PropertyUtil.getProperty("Mqtt.clientId4ActiveSub1"), "synpower/activeSub1");
//	public static final String CLIENTID2 = Util4j.getStr(PropertyUtil.getProperty("Mqtt.clientId4ActiveSub2"), "synpower/activeSub2");
//	public static final String CLIENTID3 = Util4j.getStr(PropertyUtil.getProperty("Mqtt.clientId4ActiveSub3"), "synpower/activeSub3");
	public static final Integer MAXSUBDEVSIZE = Util4j.getIntValue(PropertyUtil.getProperty("Mqtt.maxSubDevSize"), 100);
	
	public SynConstant() {
		logger.info("SynConstants are created successfully!");
	}

}
