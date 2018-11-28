package com.synpowertech.dataCollectionJar.utils;

import java.io.File;
import java.util.Date;

public class Util {

	private Util() {
	}

	public static int deleteLogFiles(int num, int logMaxTime, File f) {
		File[] list = f.listFiles();
		long nowDay = new Date().getTime();
		int i = 0;
		for (File file : list) {
			if (file.isDirectory()) {
				i += deleteLogFiles(i, logMaxTime, file);
			} else {
				long fileTime = file.lastModified();
				// 计算相差天数
				int days = (int) ((nowDay - fileTime) / 86400000);
				// 清理超过七天的日志文件
				if (days >= logMaxTime) {
					file.delete();
					i++;
				}
			}
		}
		return i;
	}
}
