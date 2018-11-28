package com.synpower.serviceImpl;

import com.synpower.bean.*;
import com.synpower.dao.*;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.AlarmService;
import com.synpower.service.PlantInfoService;
import com.synpower.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl implements AlarmService {
	private Logger logger = Logger.getLogger(AlarmServiceImpl.class);
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private SysAddressMapper addressMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private CollYxExpandMapper yxMapper;
	@Autowired
	private SysPersonalSetMapper personalSetMapper;
	@Autowired
	private DataCentralInverterMapper centralInverterMapper;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private DataYxMapper dataYxMapper;
	@Autowired
	private AlertorSettingMapper alertorMapper;
	@Autowired
	private DeviceDetailAlertorMapper deviceDetailAlertorMapper;
	@Autowired
	private CacheUtil4 cacheUtil4;
	@Autowired
	private SysOrgMapper orgMapper;

	@Override
	public MessageBean alarmList(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 前端解析 */
		Map<String, Object> map = Util.parseURL(jsonData);

		// rangeType 查找的范围类型 string //1 区域 2 电站
		// serviceType 告警分类 number //1 实时 2 关注
		String rangeType = String.valueOf(map.get("rangeType"));
		String serviceType = String.valueOf(map.get("serviceType"));

		String tokenId = String.valueOf(map.get("tokenId"));
		String areaId = String.valueOf(map.get("areaId"));
		String draw = String.valueOf(map.get("draw"));
		String level = String.valueOf(map.get("level"));
		String status = String.valueOf(map.get("status"));
		User u = session.getAttribute(tokenId);
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		List<Map<String, String>> alamList = null;
		//List<Map<String, Object>> alamListWX = null;
		int count = 0, attentionCount = 0, monthCount = 0, listCount = 0, recoveryCount = 0, todayCount = 0;

		if (!Util.isNotBlank(serviceType)) {
			serviceType = "1";
		}

		/** 得到所有可见的电站集合 */
		List<Integer> plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);

		//光储区分
		String plantType = String.valueOf(map.get("plantType")) ;
		ArrayList<Integer> tPlantIdList = new ArrayList<>();
		List<String> pList = SystemCache.plantTypeMap.get(plantType);
		if (Util.isNotBlank(plantList)){
			pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
			plantList.retainAll(tPlantIdList);
		}

		/** 默认访问全国 的实时告警 */
		if (!Util.isNotBlank(areaId)) {
			areaId = configParam.getLocation();
		}

		if (!Util.isNotBlank(rangeType)) {
			rangeType = "1";
		}

		List<Integer> plantId = null;

		if ("1".equals(rangeType)) {
			map.clear();
			map.put("name", areaId);
			map.put("plants", plantList);
			plantId = addressMapper.getPlantsIdByArea(map);
		} else {
			plantId = new ArrayList<>();
			plantId.add(Integer.parseInt(areaId));
		}

		map.clear();
		map.put("types", new int[] { 1, 2, 3, 4, 5, 9, 10, 11, 12 });
		map.put("plants", plantId);
		List<Integer> deviceId = deviceMapper.getDeviceIdByPlants(map);
		
		if (deviceId != null && !deviceId.isEmpty()) {
			map.clear();
			map.put("list", deviceId);
			map.put("level", level);
			map.put("status", status);
			map.put("startTime", Util.getFirstDayOfMonth());
			// 本月告警
			monthCount = yxMapper.getAlarmListCount(map);
			
			// 今日告警
			map.put("startTime", Util.getBeforDayMin(0));
			todayCount = yxMapper.getAlarmListCount(map);
			
			map.put("startTime", null);
			listCount = yxMapper.getAlarmListCount(map);
			
		}
		/**
		 * 告警关注列表
		 */
		map.clear();
		map.put("belongId", session.getAttribute(tokenId).getId());
		map.put("type", 2);
		
		List<String> objectList = personalSetMapper.getObjectsByBelong(map);
		if (objectList == null || objectList.isEmpty()) {
			recoveryCount = 0;
			attentionCount = 0;
		} else {
			map.clear();
//			map.put("list", null);
			map.put("level", level);
			map.put("status", "0");
			// 恢复的告警总数
			
//			recoveryCount = yxMapper.getAttentionAlarmCount(map);
			recoveryCount = 0;
			
			map.put("status", status);
			// 总关注的告警条数
			attentionCount = objectList.size();
		}
		map.put("status", status);
		map.put("min", start);
		map.put("max", length);
		map.put("level", level);
		// serviceType 告警分类 number //1 实时 2 关注
		if ("1".equals(serviceType)) {
			count = listCount;
			if (deviceId != null && !deviceId.isEmpty()) {
				map.put("list", deviceId);
				alamList = yxMapper.getAlarmList(map);
			}
		} else if ("2".equals(serviceType)) {
			//更正告警无法显示
			
			for(int i=0;i<objectList.size();i++) {
				objectList.set(i, objectList.get(i).split("\\*")[3]);
			}
			
			map.put("attentionAlarmList", objectList);
			count = attentionCount;
			if (attentionCount != 0) {
				alamList = yxMapper.getAlarmList(map);
			}
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.NOT_SUPPORT_TYPE);
			return msg;
		}
		map.clear();
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		map.put("data", alamList);
		map.put("recoveryCount", recoveryCount);
		map.put("monthCount", monthCount);
		map.put("attentionCount", attentionCount);
		map.put("todayCount", todayCount);
		// map.put("singlePlantType", plantInfoService.getPlantTypeForUser(tokenId,
		// session, 1));
		if (!"1".equals(rangeType)) {
			map.put("singlePlantType", SystemCache.plantTypeList.get(areaId));
		}
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public MessageBean alarmAttention(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String uId = u.getId();
		// id为 data_yx.id,'_',device_id,'_',signal_guid,'_',data_yx.status_value
		String guid = String.valueOf(map.get("id"));
		String type = String.valueOf(map.get("type"));
		map.clear();
		map.put("uId", uId);
		map.put("type", 2);
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		/** 保存用户的个性化设置 */
		// 1：关注；0：取消关注
		if ("1".equals(type)) {
			map.put("obejectIds", new String[] { guid });
			map.put("createTime", System.currentTimeMillis());
			int i = personalSetMapper.saveUserRecord(map);
			if (i > 0) {
				msg.setMsg(Msg.ALARM_SET_SUCCESS);
				msg.setCode(Header.STATUS_SUCESS);
				logger.info(StringUtil.appendStr(" 告警关注设置成功 操作者 :", logUserName + " 关注信号点+:" + guid));
			} else {
				msg.setMsg(Msg.ALARM_SET_FAILED);
				msg.setCode(Header.STATUS_FAILED);
				logger.error(StringUtil.appendStr(" 告警关注设置失败 操作者 :", logUserName + " 关注信号点+:" + guid));
			}
		} else if ("0".equals(type)) {
			map.put("obejectId", guid);
			int i = personalSetMapper.deleteUserOneRecord(map);
			if (i > 0) {
				msg.setMsg(Msg.ALARM_SET_SUCCESS);
				msg.setCode(Header.STATUS_SUCESS);
				logger.info(StringUtil.appendStr(" 取消告警关注设置成功 操作者 :", logUserName + " 关注信号点+:" + guid));
			} else {
				msg.setMsg(Msg.ALARM_SET_FAILED);
				msg.setCode(Header.STATUS_FAILED);
				logger.error(StringUtil.appendStr(" 取消告警关注设置失败 操作者 :", logUserName + " 关注信号点+:" + guid));
			}
		} else {
			msg.setMsg(Msg.NOT_SUPPORT_TYPE);
			msg.setCode(Header.STATUS_FAILED);
		}
		return msg;
	}

	@Override
	public MessageBean delAllAlarmAttention(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String uId = u.getId();
		String guid = String.valueOf(map.get("id"));
		map.clear();
		map.put("uId", uId);
		map.put("type", 2);
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		/** 保存用户的个性化设置 */
		int i = personalSetMapper.deleteUserRecord(map);
		if (i > 0) {
			msg.setMsg(Msg.ALARM_SET_SUCCESS);
			msg.setCode(Header.STATUS_SUCESS);
			logger.info(StringUtil.appendStr(" 清空告警设置成功 操作者 :", logUserName + " 关注信号点+:" + guid));
		} else {
			msg.setMsg(Msg.ALARM_SET_FAILED);
			msg.setCode(Header.STATUS_FAILED);
			logger.error(StringUtil.appendStr(" 清空告警设置失败 操作者 :", logUserName + " 关注信号点+:" + guid));
		}
		return msg;
	}

	@Override
	public MessageBean alarmLine(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String deviceId = String.valueOf(map.get("deviceId"));
		String id = String.valueOf(map.get("id"));
		String changeTime = String.valueOf(map.get("changeTime"));
		String loginId = session.getAttribute(String.valueOf(map.get("tokenId"))).getId();
		long timeStart = TimeUtil.getBeforHour(changeTime, 3);
		long timeEnd = TimeUtil.getBeforHour(changeTime, -3);
		String startTime = TimeUtil.getTimeFiveBefor(Util.timeFormate(timeStart, "HH:mm"));
		String endTime = TimeUtil.getTimeFiveEnd(Util.timeFormate(timeEnd, "HH:mm"));
		CollDevice device = deviceMapper.getDeviceById(deviceId);
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(String.valueOf(device.getPlantId()));
		int deviceType = device.getDeviceType();
		List<Map> resultList = new ArrayList<>();
		map.clear();
		map.put("deviceId", deviceId);
		map.put("startTime", timeStart);
		map.put("endTime", timeEnd);
		double maxValue = 0;
		if (deviceType == 1) {
			List<DataCentralInverter> dataList = centralInverterMapper.getPowerOfCentralInvnter(map);
			Map<String, Object> ydata1 = ServiceUtil.loadParamForPower(startTime, endTime, dataList);
			maxValue = Double.parseDouble(String.valueOf(ydata1.get("maxValue")));
			resultList.add(ydata1);
			Map<String, Object> ydata2 = ServiceUtil.loadParamForPower2(startTime, endTime, dataList);
			double tempValue = Double.parseDouble(String.valueOf(ydata1.get("maxValue")));
			if (maxValue < tempValue) {
				maxValue = tempValue;
			}
			resultList.add(ydata2);
		}
		if (deviceType == 2) {
			List<DataCentralInverter> dataList = centralInverterMapper.getPowerOfStringInvnter(map);
			Map<String, Object> ydata1 = ServiceUtil.loadParamForPower(startTime, endTime, dataList);
			maxValue = Double.parseDouble(String.valueOf(ydata1.get("maxValue")));
			resultList.add(ydata1);
			Map<String, Object> ydata2 = ServiceUtil.loadParamForPower2(startTime, endTime, dataList);
			double tempValue = Double.parseDouble(String.valueOf(ydata1.get("maxValue")));
			if (maxValue < tempValue) {
				maxValue = tempValue;
			}
			resultList.add(ydata2);
		}
		// 是否关注 1 已关注 0未关注
		int attention = 0;
		map.clear();
		map.put("belongId", loginId);
		map.put("type", 2);
		List<String> attentionList = personalSetMapper.getObjectsByBelong(map);
		if (attentionList != null && !attentionList.isEmpty()) {
			boolean b = attentionList.contains(id);
			if (b) {
				attention = 1;
			}
		}
		List<String> xData = ServiceUtil.getStringTimeSegment(startTime, endTime);
		map.clear();
		map.put("attention", attention);
		Map<String, Object> power = new HashMap<>();
		power.put("xData", xData);
		power.put("yData", resultList);
		map.put("power", power);
		map.put("plantName", plantInfo.getPlantName());
		String[] localtion = plantInfo.getLoaction().split(",");
		map.put("location", new String[] { localtion[1], localtion[0] });
		map.put("plantId", plantInfo.getId());
		map.put("deviceName", device.getDeviceName());
		map.put("deviceId", device.getId());
		map.put("maxValue", maxValue == 0.0 ? 1 : maxValue);
		DataYx dataByYxId = dataYxMapper.getDataByYxId(id);
		if (dataByYxId != null) {
			map.put("currStatus", dataByYxId.getStatus());
		}
		map.put("changeTime", TimeUtil.getTimeToLine(changeTime));
		map.put("singlePlantType", SystemCache.plantTypeList.get(String.valueOf(plantInfo.getId())));
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getalarmLight(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		AlertorSetting alertor = alertorMapper.getAlertorInfoByUserId(u.getId());
		if (alertor != null) {
			Map<String, Object> resultMap = new HashMap<String, Object>(3);
			resultMap.put("light", alertor.getDeviceCloseEver());
			resultMap.put("ring", alertor.getSoundCloseEver());
			resultMap.put("serial", alertor.getSerialPort());
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			Map<String, String> paramMap = new HashMap<String, String>(4);
			paramMap.put("light", "0");
			paramMap.put("ring", "0");
			paramMap.put("userId", u.getId());
			paramMap.put("deviceDetailId", "1");
			int result = alertorMapper.insertAlertorInfo(paramMap);
			if (result > 0) {
				AlertorSetting newAlertor = alertorMapper.getAlertorInfoByUserId(u.getId());
				Map<String, Object> resultMap = new HashMap<String, Object>(3);
				resultMap.put("light", alertor.getDeviceCloseEver());
				resultMap.put("ring", alertor.getSoundCloseEver());
				resultMap.put("serial", alertor.getSerialPort());
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultMap);
				msg.setMsg(Msg.SELECT_SUCCUESS);
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.UPDATE_FAILED);
			}
		}
		return msg;
	}

	@Override
	public MessageBean setalarmLight(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		String userId = u.getId();
		// 查询当前账户有没有告警灯，有则修改告警灯信息，没有则新增告警灯信息
		AlertorSetting alertor = alertorMapper.getAlertorInfoByUserId(userId);
		if (alertor != null) {
			Map<String, String> paramMap = new HashMap<String, String>(4);
			paramMap.put("light", map.get("light") + "");
			paramMap.put("ring", map.get("ring") + "");
			paramMap.put("serial", map.get("serial") + "");
			paramMap.put("userId", userId);
			int result = alertorMapper.updateAlertorInfoByUserId(paramMap);
			if (result > 0) {
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPDATE_SUCCUESS);
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.UPDATE_FAILED);
			}
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("该账户下没有告警灯信息");
		}
		return msg;
	}

	@Override
	public MessageBean getAlertorOrder(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		AlertorSetting alertor = alertorMapper.getAlertorInfoByUserId(u.getId());
		if (alertor != null) {
			DeviceDetailAlertor deviceDetailAlertor = deviceDetailAlertorMapper
					.getOrderById(alertor.getDeviceDetailId());
			if (deviceDetailAlertor != null) {
				List<Map<String, String>> reusltList = new ArrayList<Map<String, String>>();
				Map<String, String> OrderMap1 = new HashMap<String, String>();
				OrderMap1.put("name", "2-0");
				OrderMap1.put("value", deviceDetailAlertor.getCloseSoundInstructions());
				reusltList.add(OrderMap1);
				Map<String, String> OrderMap2 = new HashMap<String, String>();
				OrderMap2.put("name", "2-1");
				OrderMap2.put("value", deviceDetailAlertor.getOpenSoundInstructions());
				reusltList.add(OrderMap2);
				Map<String, String> OrderMap3 = new HashMap<String, String>();
				OrderMap3.put("name", "1-0");
				OrderMap3.put("value", deviceDetailAlertor.getCloseRedLightInstructions());
				reusltList.add(OrderMap3);
				Map<String, String> OrderMap4 = new HashMap<String, String>();
				OrderMap4.put("name", "1-1");
				OrderMap4.put("value", deviceDetailAlertor.getOpenRedLightInstructions());
				reusltList.add(OrderMap4);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(reusltList);
				msg.setMsg(Msg.SELECT_SUCCUESS);
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg("该账户下没有告警灯指令");
			}
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("该账户下没有告警灯信息");
		}
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public MessageBean getConfirmAlarm(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
		List<SysOrg> orgList = orgMapper.getAllOrg();
		SysOrg org = ((SysOrg) u.getUserOrg());
		List<String> reList = new ArrayList<>();
		// 查出当前登录人的组织id有哪些
		reList.add(((SysOrg) u.getUserOrg()).getId() + "");
		reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("reList", reList);
		List<PlantInfo> plantList = plantInfoMapper.getPlantByOrgIds(paramMap);
		for (int i = 0, size = plantList.size(); i < size; i++) {
			PlantInfo plant = plantList.get(i);
			String s = cacheUtil4.set(plant.getId() + "", "0");
			if (!"OK".equalsIgnoreCase(s)) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.UPDATE_FAILED);
				return msg;
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.UPDATE_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getAlarmData(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
		List<SysOrg> orgList = orgMapper.getAllOrg();
		SysOrg org = ((SysOrg) u.getUserOrg());
		List<String> reList = new ArrayList<>();
		// 查出当前登录人的组织id有哪些
		reList.add(((SysOrg) u.getUserOrg()).getId() + "");
		reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("reList", reList);
		List<PlantInfo> plantList = plantInfoMapper.getPlantByOrgIds(paramMap);
		boolean flag = false;
		for (int i = 0, size = plantList.size(); i < size; i++) {
			PlantInfo plant = plantList.get(i);
			String value = cacheUtil4.getString(plant.getId() + "");
			if ("1".equals(value)) {
				flag = true;
			}
		}
		if (flag) {
			resultMap.put("isAlarm", 1);
		} else {
			resultMap.put("isAlarm", 0);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
}
