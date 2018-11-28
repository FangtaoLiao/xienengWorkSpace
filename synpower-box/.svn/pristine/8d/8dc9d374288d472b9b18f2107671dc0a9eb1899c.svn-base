package com.synpower.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author ybj
 * @date 2018年8月27日下午6:17:26
 * @Description -_-线程池工具类
 */
public class ThreadPool {

	private static class SingletonHolder {
		public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	}

	public ThreadPool() {
	}

	public static void execute(Runnable runable) {
		SingletonHolder.cachedThreadPool.execute(runable);
	}

}
