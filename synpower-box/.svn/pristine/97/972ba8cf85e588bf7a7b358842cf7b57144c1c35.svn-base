package com.synpower.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.lang.ServiceException;
import com.synpower.service.CacheClearService;
import com.synpower.util.IYkYtService;
import com.synpower.util.SystemCache;
import com.synpower.util.ThreadPool;

/**
 * 缓存服务清理类实现
 * 
 * @author ybj
 * @date 2018年8月20日
 * @Description -_-
 */
@Service
public class CacheClearServiceImpl implements CacheClearService {

	private static boolean flagTwo = true;

	@Autowired
	private IYkYtService iService;
	@Autowired
	private SystemCache systemCache;

	/**
	 *
	 * @author ybj
	 * @date 2018年8月27日下午6:20:10
	 * @Description -_- 清理采集服务器和本服务器的所有缓存
	 * @return
	 */
	@Override
	public boolean clearAll() {
		// 清理采集缓存
		boolean flagOne = iService.refreshCache();
		// 清理处理服务器缓存

		ThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					systemCache.init();
				} catch (Exception e) {
					e.printStackTrace();
					flagTwo = false;
				}
			}
		});

		return flagOne && flagTwo;
	}

	@Override
	public void clearPVPlantHistory() {
		ThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				systemCache.updateHistoryDataPlant();
			}
		});
	}

}
