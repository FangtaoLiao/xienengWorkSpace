package com.synpower.quartz;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.synpower.handle.WebSocketPushHandler;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.serviceImpl.SMSServiceImpl;
import com.synpower.util.RabbitMessage;
import com.synpower.util.ServiceUtil;
import com.synpower.util.SystemCache;

/*****************************************************************************
 * @Package: com.synpower.quartz ClassName: TimedTasks
 * @Description: 定时任务
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月15日下午5:23:41 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Component
public class TimedTasks {
	private Logger logger = Logger.getLogger(TimedTasks.class);

	@Autowired
	private SystemCache systemCache;
	@Autowired
	private StorageData storageData;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private UpdateWeather updateWeather;
	@Autowired
	private UpdateYSToken updateToken;
	@Autowired
	private SMSServiceImpl smsService;

	/**
	 * @Title: storageData
	 * @Description: 定时入库
	 * @throws Exception: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月29日上午11:15:18
	 */
	public void storageData() throws Exception {
		logger.info("  定时数据入库任务开启。");
		storageData.serviceDistribution();
		logger.info("  更新电站历史收益。");
		systemCache.updateHistoryDataPlant();
		logger.info("更新能效电站月历史和年历史");
		systemCache.updateHistoryDataOfEEPlant();
		clearLogTask();

	}

	/**
	 * @Title: refreshSystemCacheEveryDay
	 * @Description: 每天刷新一次系统缓存
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月23日上午11:45:32
	 */
	public void refreshSystemCacheEveryDay() {
		// 电表最新历史数据
		systemCache.updateElectricBeforDatas();
		systemCache.updatePlantEclecCap();
	}

	/**
	 * @throws ServiceException
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 * @throws IOException
	 * @throws SmsException
	 * @throws Exception
	 * @Title: refreshSystemCacheCurrent
	 * @Description: 每5秒刷新一次系统缓存
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月23日上午11:55:56
	 */
	public void refreshSystemCacheCurrent()
			throws ServiceException, SmsException, IOException, SessionException, SessionTimeoutException {
		// 电站实时收益
		systemCache.getNowDayIncome();
		// 设备状态
		systemCache.updateDeviceOfStatus();
		systemCache.updatePlantSOC();
		// System.out.println(systemCache.currentDataOfPlant);
		// System.out.println(systemCache.deviceOfStatus);
	}

	/**
	 * @Title: refreshSystemCacheCurrent
	 * @Description: 每15分钟刷新一次系统缓存
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月23日上午11:55:56
	 */
	public void refreshSystemCacheEveryHour() {
		// 清理回执消息缓存
		RabbitMessage.clearCache();
		// 清理超时连接
		refreshWebSocketEveryHour();
	}

	/**
	 * 
	 * @Title: sendDailyReport
	 * @Description: 日报发送（早九点）
	 * @throws ServiceException
	 * @throws SmsException
	 * @throws IOException
	 * @throws SessionException
	 * @throws SessionTimeoutException: void
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月9日下午3:12:27
	 */
	public void sendDailyReport()
			throws ServiceException, SmsException, IOException, SessionException, SessionTimeoutException {
		// 发送日报
		// smsService.sendDailyReport();
	}

	/**
	 * @throws IOException
	 * @throws Exception
	 * @Title: refreshWebSocketEveryHour
	 * @Description: 清理websocket过期连接
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月29日上午11:16:03
	 */
	public void refreshWebSocketEveryHour() {
		List<WebSocketSession> users = WebSocketPushHandler.users;
		long nowTime = System.currentTimeMillis();
		if (!users.isEmpty()) {
			for (int i = 0; i < users.size(); i++) {
				WebSocketSession user = users.get(i);
				long connTime = Long.parseLong(String.valueOf(user.getAttributes().get("connTime")));
				String sn = String.valueOf(user.getAttributes().get("sn"));
				long timeOut = Integer.parseInt(configParam.getWebsocketTimeOut()) * 1000;
				if (nowTime - connTime > timeOut) {
					logger.info("移除超时连接  " + sn);
					if (user.isOpen()) {
						try {
							user.close();
						} catch (IOException e) {
							logger.error(" sn: " + sn + "关闭连接时出现异常。", e);
						}
					}
					if (!users.isEmpty()) {
						users.remove(user);
					}
				}
				if (i != 0) {
					--i;
				}
			}
		}
	}

	/**
	 * @Title: refreshWeatherEachTwoHours
	 * @Description: 每两个小时更新天气预报
	 * @throws ServiceException: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月23日上午9:44:47
	 */
	public void refreshWeatherEachTwoHours() throws ServiceException {
		// 设备集合
		systemCache.updateDeviceList();
		// 设备类型分类集合
		systemCache.updateDeviceMap();
		// 设备guid
		systemCache.getGuidForDevice();
		// 设备按数采分类
		systemCache.updateDeviceOfCollecter();
		// 更新天气
		updateWeather.updateWeather();
	}

	/**
	 * 
	 * @Title: refreshYS7TokenId
	 * @Description: 5天更新萤石云token
	 * @throws ServiceException: void
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月24日下午2:30:01
	 */
	public void refreshYS7TokenId() throws ServiceException {
		updateToken.updateYS7Token();
	}

	public void clearLogTask() {
		logger.info("定时日志清理任务开启。");

		String str = this.getClass().getClassLoader().getResource("").getPath();

		str = str.substring(0, str.indexOf("box-web")) + "synpowerLog";

		File f = new File(str);

		int logMaxTime = Integer.parseInt(configParam.getLogMaxSurvivalTime());

		int i = ServiceUtil.deleteLogFiles(0, logMaxTime, f);

		logger.info("日志清理完成! 共删除" + i + "个文件。");
	}

	/**
	 * 刷新能效电站最大最小值缓存
	 * 
	 * @author ybj
	 * @date 2018年9月3日下午5:59:21
	 * @Description -_-
	 */
	public void refreshEEMaxAndMinFactor() {
		systemCache.updateMinAndMaxFactorOfEEPlant();
	}
}
