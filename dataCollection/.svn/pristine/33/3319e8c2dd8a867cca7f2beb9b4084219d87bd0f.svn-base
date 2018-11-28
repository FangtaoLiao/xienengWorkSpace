package com.synpowertech.dataCollectionJar.timedTask;

import java.io.File;
import java.net.URL;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.utils.Util;

public class CleanLogTask implements Job {

	private static final int LOG_MAX_TIME = 7;

	private static final Logger log = LoggerFactory.getLogger(JedisUtil.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("[TimingTask]   定时日志清理任务开启。");
		String path = System.getProperty("user.dir")+File.separator+"logs";
		File f = new File(path);
		log.info("*************"+path);
		int i = Util.deleteLogFiles(0, LOG_MAX_TIME, f);
		log.info("[TimingTask]   日志清理完成! 共删除" + i + "个文件。");
	}

}
