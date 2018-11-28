package com.synpowertech.dataCollectionJar.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollection
 * @ClassName: ThreadPool
 * @Description: synpower.properties
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月2日上午11:16:49   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class ThreadPool {
	
	private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);
	
	private static final int coreSize = Runtime.getRuntime().availableProcessors();//获取cpu核数
	private static ThreadPoolExecutor xmlParse_task = null;
	
	public static void executeXmlParse_task(Runnable runnable) {
		if (xmlParse_task == null) {
			initImportPool();
		}
		xmlParse_task.execute(runnable);
	}

	public static synchronized void initImportPool() {
		if (xmlParse_task == null) {
			//xmlParse_task = new ThreadPoolExecutor(coreSize * 2, coreSize * 2,	3L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000));
			//xmlParse_task = new ThreadPoolExecutor(coreSize * 2, Integer.MAX_VALUE,	3L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
			xmlParse_task = new ThreadPoolExecutor(coreSize * 2, 130000,3L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
			logger.info("ThreadPool inits successfully！");
		}
	}
}
