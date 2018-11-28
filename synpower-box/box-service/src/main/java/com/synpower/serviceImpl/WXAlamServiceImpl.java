package com.synpower.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.CollYxExpandMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysAddressMapper;
import com.synpower.dao.SysPersonalSetMapper;
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
import com.synpower.service.PlantReportService;
import com.synpower.service.WXAlarmService;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;

@Service
public class WXAlamServiceImpl implements WXAlarmService {
	private Logger logger = Logger.getLogger(WXAlamServiceImpl.class);
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private CollYxExpandMapper yxMapper;
	@Autowired
	private SysAddressMapper addressMapper;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private SysPersonalSetMapper personalSetMapper;
	@Autowired
	private PlantReportService plantReportService;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	private String string;

	@Override
	public MessageBean getAlarm(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 前端解析 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String serviceType = String.valueOf(map.get("alarmType"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String typeCopy = String.valueOf(map.get("type"));
		String plantId = String.valueOf(map.get("plantId"));
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("offset"))) ? "0" : String.valueOf(map.get("offset")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		int type = Integer.parseInt(Util.isNotBlank(typeCopy) ? typeCopy : "-1");
		String alarmLevel = null;
		switch (type) {
		case 0:
			alarmLevel = "重要";
			break;
		case 1:
			alarmLevel = "一般";
			break;
		case 2:
			alarmLevel = "提示";
			break;
		default:
			alarmLevel = "";
			break;
		}
		User u = session.getAttribute(tokenId);
		List<Map<String, Object>> alamList = null;
		if ("null".equals(serviceType) || "".equals(serviceType)) {
			serviceType = "0";
		}
		/** 得到所有可见的电站集合 */
		List<Integer> plantList = null;
		if ("null".equals(plantId) || "".equals(plantId)) {
			plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);
		} else {
			plantList = new ArrayList<>();
			plantList.add(Integer.parseInt(plantId));
		}
		map.clear();
		map.put("types", new int[] { 1, 2, 3, 5 });
		map.put("plants", plantList);
		List<Integer> deviceId = deviceMapper.getDeviceIdByPlants(map);
		/**
		 * 告警关注列表
		 */
		map.clear();
		map.put("belongId", session.getAttribute(tokenId).getId());
		map.put("type", 2);
		map.put("min", start);
		map.put("max", length);
		map.put("level", alarmLevel);
		List<String> objectList = personalSetMapper.getObjectsByBelong(map);
		List<Map<String, String>> countList = null;
		if ("0".equals(serviceType)) {
			if (deviceId != null && !deviceId.isEmpty()) {
				map.put("list", deviceId);
				alamList = yxMapper.getAlarmListWX(map);
				countList = yxMapper.getAlarmCountWX(map);
				String[] levelList = new String[] { "0", "1", "2" };
				if (countList == null || countList.isEmpty()) {
					countList = new ArrayList<>();
					for (int j = 0; j < levelList.length; j++) {
						Map<String, String> temp = new HashMap<>();
						temp.put("alarmLevel", levelList[j]);
						temp.put("count", "0");
						countList.add(temp);
					}
				} else {
					for (int i = 0; i < levelList.length; i++) {
						boolean b = true;
						String obj = levelList[i];
						for (Map<String, String> item : countList) {
							if (obj.equals(item.get("alarmLevel"))) {
								b = false;
								break;
							}
						}
						if (b) {
							Map<String, String> temp = new HashMap<>();
							temp.put("alarmLevel", obj);
							temp.put("count", "0");
							countList.add(temp);
						}
					}
				}
			}
		} else if ("1".equals(serviceType) && !objectList.isEmpty()) {

//				map.put("list", null);
			for (int i = 0; i < objectList.size(); i++) {
				objectList.set(i, objectList.get(i).split("\\*")[3]);
			}
			map.put("attentionAlarmList", objectList);
			alamList = yxMapper.getAlarmListWX(map);
		}
		List<Map<String, Object>> resultList = new ArrayList();
		if (alamList != null && !alamList.isEmpty()) {
			for (Map<String, Object> dataTemp : alamList) {
				long changeTime = (long) (dataTemp.get("changeTime"));
				String time = Util.timeFormate(changeTime, "yyyy-MM-dd");
				boolean b = true;
				for (Map<String, Object> map2 : resultList) {
					String key = String.valueOf(map2.get("date"));
					if (time.equals(key)) {
						List<Map<String, Object>> temp = (List<Map<String, Object>>) map2.get("data");
						dataTemp.put("changeTimeLabel", Util.timeFormate(changeTime, "HH:mm"));
						temp.add(dataTemp);
						map2.put("data", temp);
						b = false;
						break;
					}
				}
				if (b) {
					Map<String, Object> newMap = new HashMap<String, Object>(2);
					List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
					dataTemp.put("changeTimeLabel", Util.timeFormate(changeTime, "HH:mm"));
					temp.add(dataTemp);
					newMap.put("date", time);
					newMap.put("data", temp);
					resultList.add(newMap);
				}
			}
		}
		map.clear();
		map.put("row", resultList);
		map.put("countList", countList);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean followEvent(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String uId = u.getId();
		String guid = String.valueOf(map.get("causeId"));
		String fllow = String.valueOf(map.get("fllow"));
		map.clear();
		map.put("uId", uId);
		map.put("type", 2);

		int i = 0;
		if ("1".equals(fllow)) {
			map.put("obejectId", guid);
			i = personalSetMapper.deleteUserOneRecord(map);
		} else if ("0".equals(fllow)) {
			map.put("obejectIds", new String[] { guid });
			map.put("createTime", System.currentTimeMillis());
			i = personalSetMapper.saveUserRecord(map);
		} else {
			msg.setMsg(Msg.NOT_SUPPORT_TYPE);
			msg.setCode(Header.STATUS_FAILED);
			return msg;
		}
		if (i > 0) {
			msg.setMsg(Msg.ALARM_SET_SUCCESS);
			msg.setCode(Header.STATUS_SUCESS);
			logger.info(" 告警关注设置成功 操作者 :" + u.getId() + "_" + u.getUserName() + " 关注信号点+:" + guid);
		} else {
			msg.setMsg(Msg.ALARM_SET_FAILED);
			msg.setCode(Header.STATUS_FAILED);
			logger.info(" 告警关注设置失败 操作者 :" + u.getId() + "_" + u.getUserName() + " 关注信号点+:" + guid);
		}
		return msg;
	}

	@Override
	public MessageBean detail(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String id = String.valueOf(map.get("causeId"));
		String changeTime = String.valueOf(map.get("changeTime"));
		String loginId = session.getAttribute(String.valueOf(map.get("tokenId"))).getId();
		map.clear();
		map.put("id", id.split("\\*")[3]);
		map.put("changeTime", changeTime);
		// 获取遥信详情
		Map<String, Object> yx = yxMapper.getAlarmById(map);

		yx.put("id", id);

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
		yx.put("attention", attention);
		yx.put("changeTime", Util.timeFormate((long) (yx.get("changeTime")), "yyyy-MM-dd HH:mm:ss.SSS"));
		String plantType = SystemCache.plantTypeList.get(yx.get("plantId").toString());
		yx.put("plantType", plantType);
		msg.setBody(yx);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

}
