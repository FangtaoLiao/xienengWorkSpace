package com.synpower.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.EnergyMonitorException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.User;
import com.synpower.service.EnergyMonitorService;
import com.synpower.util.MessageBeanUtil;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;

/**
 * 
 * @author ybj
 * @date 2018年8月21日
 * @Description -_-能效-实时监测
 *
 */
@RestController
@RequestMapping("/energyMonitor")
public class EnergyMonitorController extends ErrorHandler {

	@Autowired
	private EnergyMonitorService energyMonitorService;

	/**
	 * 月峰谷电量统计
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 */
	@PostMapping("/getElecKPI")
	public MessageBean getElecKPI(@RequestBody String json)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(json);
		User u = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		// 当前月份时间 eg:2018-08
		String time = String.valueOf(map.get("time"));
		// 得到当前时间戳
		long currMonth = Util.strFormate(time, "yyyy-MM");
		// 得到下一个月当前时间时间戳
		long nextMonth = TimeUtil.getNextOrLastTime(Calendar.MONTH, currMonth, 1);
		List<Map<String, Object>> result = energyMonitorService.getElecKPI(u, currMonth, nextMonth);
		return MessageBeanUtil.getOkMB(result);
	}

	/**
	 * 用电统计
	 *
	 * @return
	 */
	@PostMapping("/getUseElec")
	public MessageBean getUseElec(@RequestBody String json)
			throws ServiceException, SessionException, SessionTimeoutException, EnergyMonitorException {
		Map<String, Object> map = Util.parseURL(json);
		User user = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		HashMap<String, Object> useElecMap = energyMonitorService.getUseElec(user);
		return MessageBeanUtil.getOkMB(useElecMap);
	}

	/**
	 * 用电负荷趋势
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 */
	@PostMapping("/getElecLoadKpi")
	public MessageBean getElecLoadKpi(@RequestBody String json)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(json);
		User u = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		// 开始时间 如果没传值 就设置当天
		String startTimeTemp = String.valueOf(map.get("startTime"));
		String endTimeTemp = String.valueOf(map.get("endTime"));
		long startTime = 0l;
		long endTime = 0l;
		// 结束时间 如果没设置就显示当天 因为包括结束时间当天 所以应该吧结束时间加一天 就相当于包括了结束时间当天
		// 如果时间不为空 就转换为时间戳
		if (Util.isNotBlank(startTimeTemp) || Util.isNotBlank(startTimeTemp)) {
			startTime = Util.strFormate(startTimeTemp, "yyyy-MM-dd");
			endTime = Util.strFormate(endTimeTemp, "yyyy-MM-dd");
			// 如果时间为空 默认本月
		} else {
			endTime = startTime = TimeUtil.getCurrDayStartTime(0);
		}

		// 把结束时间加一天 在把毫秒减一 就是当天最后一刻 即改天结束时间
		endTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, endTime, 1);
		endTime = TimeUtil.getNextOrLastTime(Calendar.MILLISECOND, endTime, -1);

		Map<String, Object> result = energyMonitorService.getElecLoadKpi(u, startTime, endTime);

		return MessageBeanUtil.getOkMB(result);
	}

	/**
	 * 用电量趋势
	 * 
	 * @return
	 */
	@PostMapping("/getElecUseKpi")
	public MessageBean getElecUseKpi(@RequestBody String json)
			throws ServiceException, SessionException, SessionTimeoutException, EnergyMonitorException {
		Map<String, Object> map = Util.parseURL(json);
		User user = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		Map<String, Object> parMap = Util.parseURL(json);
		HashMap<String, Object> useElecMap = energyMonitorService.getElecUseKpi(user, parMap);
		return MessageBeanUtil.getOkMB(useElecMap);
	}

	/**
	 * 电能质量
	 * 
	 * @author ybj
	 * @Date 2018/8/23 14:37
	 * @return
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 * @throws ServiceException
	 */
	@PostMapping("/getElecQualityKPI")
	public MessageBean getElecQualityKPI(@RequestBody String json)
			throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(json);
		User u = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		Map<String, Object> elecQualityKPI = energyMonitorService.getElecQualityKPI(u);
		return MessageBeanUtil.getOkMB(elecQualityKPI);
	}

	/**
	 * 获取企业基本信息
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 */
	@PostMapping("/getEnterpriseInfo")
	public MessageBean getEnterpriseInfo(@RequestBody String json)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(json);
		User u = getSession().getAttribute(String.valueOf(map.get("tokenId")));
		Map<String, Object> result = energyMonitorService.getEnterpriseInfo(u);
		return MessageBeanUtil.getOkMB(result);
	}

}
