package com.synpowertech.dataCollectionJar.initialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.initialization
 * @ClassName: Main2Start
 * @Description: 数据采集系统的启动类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月13日下午3:16:41 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************

 */
public class Main2Start {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Main2Start.class);
		logger.info("SystemInitialization is starting!");
		try {
			@SuppressWarnings("unused")
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		} catch (BeansException e) {
			logger.error("SystemInitialization is exceptional:{}",e);
		}
		logger.info("SystemInitialization ends!");
	}
}
