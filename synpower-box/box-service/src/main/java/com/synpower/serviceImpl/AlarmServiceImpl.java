package com.synpower.serviceImpl;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private CacheUtil0 cacheUtil0;

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

	/**
	 *
	 * @param jsonData
	 * @param session
	 * @return 当前告警的详情
	 * @throws ServiceException
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 */
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

    //获取实时告警列表
    @Override
    public MessageBean recentAlarmList(Map<String, Object> jsonMap) {
		List<Map<String, String>> listMap = (List<Map<String, String>>) jsonMap.get("orderName");
        Map orderMap = new HashMap();
        orderMap = listMap.get(0);
		List<String> list = new ArrayList<>();
		List<Integer> plantIds = new ArrayList<>();
		System.out.print(jsonMap.toString());
		String s = (String) jsonMap.get("start");
		Integer start = Integer.parseInt(s);
		Integer length = 20;
		String plant = (String) jsonMap.get("plantIds");
		if(plant!=null&&plant!="") {
			System.out.println(plant);
			list.add(plant);
			plantIds = list.stream().map(Integer::parseInt).collect(Collectors.toList());//转为integer数组
			List<Map<String,Object>> subList = recentAlarmListByplantId(plantIds, orderMap);
			Integer total = subList.size();
			return returnSubList(subList,total,length,start);
		}
//		for (Integer i: plantIds){
//			System.out.print(i);
//		}
		//根据电站Id排序并获取告警列表
	  	List<Map<String,Object>> subList =recentAllAlarmList(orderMap);
        Integer total = subList.size();
		return returnSubList(subList,total,length,start);
	}

    /**
     * @@Description 返回分页后的告警列表信息
     * @param subList
     * @param total
     * @param length
     * @param start
     * @return
     */
	public MessageBean returnSubList(List<Map<String,Object>> subList,Integer total,Integer length,Integer start){
	    System.out.print(total+""+length+""+start+"");
	    if((start*length)<total) {
            subList = subList.subList((start - 1) * length, (start * length));
            Map<String,Object> result = new HashMap<>();
            Integer page = total / length + 1;
            result.put("data",subList);
            result.put("recordsTotal",total);
            result.put("recordsPage",page);
            MessageBean messageBean = MessageBeanUtil.getOkMB(result);
            return messageBean;
        }
        subList = subList.subList((start - 1) * length, total);
	    Map<String,Object> result = new HashMap<>();
	    result.put("data",subList);
	    result.put("recordsTotal",total);
        Integer page = total / length + 1;
	    result.put("recordsPage",page);
        MessageBean messageBean = MessageBeanUtil.getOkMB(result);
        return messageBean;
    }

    /**
     * @Author lz,SP0025
     * @Description: 根据电站id, 和排序获取实时告警列表
     * @param: [plantIds, orderMap]
     * @return: {java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     * @Date: 2018/11/30 13:41
     **/
    private List<Map<String, Object>> recentAlarmListByplantId(List<Integer> plantIds, Map<String, String> orderMap) {
        //list<设备编号>
        List<Integer> allDevIds = new ArrayList<>();
        //map<电站编号,map<设备类型，list<设备编号>>
        Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;  //从设备缓存取出设备id
        for (Integer plantId : plantIds) {
            for (List<Integer> devIds : deviceOfPlant.get(plantId).values()) {
                allDevIds.addAll(devIds);
            }
        }
        List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
        return getAlarmListMap(strDevs,orderMap);
    }

	/**
	 * @Description  获取全部电站全部设备的告警信息列表
	 * @param orderMap
	 * @return
	 */
	private List<Map<String, Object>> recentAllAlarmList(Map<String, String> orderMap) {
		//list<设备编号>
		System.out.println("all");
		List<Integer> allDevIds = new ArrayList<>();
		//map<电站编号,map<设备类型，list<设备编号>>
		Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;  //从设备缓存取出设备id
		for(Integer plantIds:deviceOfPlant.keySet()){
			for(List<Integer> devIds : deviceOfPlant.get(plantIds).values()){
				allDevIds.addAll(devIds);
			}
		}
		List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
		System.out.println(strDevs.toString());
		return getAlarmListMap(strDevs,orderMap);
	}



	/**
	 * @Description  获取告警列表
	 * @param strDevs
	 * @param orderMap
	 * @return
	 */
	public List<Map<String, Object>> getAlarmListMap(List<String> strDevs,Map<String, String> orderMap){
		//map<设备编号,map<遥信id,告警json>>
		Map<String, Map<String, String>> allKeysMapValue = cacheUtil0.getAllKeysMapValue(strDevs);//根据设备id取出redis中的告警列表
		Map<String,List<Map<String,Object>>> listMap = new HashMap<>();

		//先遍历告警列表的键，键为设备id
		Map<String, String> stringStringMap = new HashMap<>() ;
		List<Map<String,Object>> list = new ArrayList<>();
		for(String keys : allKeysMapValue.keySet()) {
			//List<Map<String,Object>> list = new ArrayList<>();
			stringStringMap.clear();
			stringStringMap	= allKeysMapValue.get(keys);
			//map转list<map>
			//取出该设备所有遥信信息

			for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
				Map<String, Object> listGetMap = new HashMap<>();
				//listGetMap.put(entry.getKey(), entry.getValue());
				listGetMap.put("yx_id", entry.getKey());
				JSONObject jsonObject = JSONObject.parseObject(entry.getValue());
				listGetMap.put("task_status", jsonObject.get("task_status"));
				listGetMap.put("data_time_f", jsonObject.get("data_time_f"));
				listGetMap.put("data_time", jsonObject.get("data_time"));
				listGetMap.put("level", jsonObject.get("level"));
				listGetMap.put("status_value", jsonObject.get("status_value"));
				listGetMap.put("sys_time", jsonObject.get("sys_time"));
				listGetMap.put("device_id", jsonObject.get("device_id"));
				listGetMap.put("signal_name", jsonObject.get("signal_name"));
				listGetMap.put("plant_id", jsonObject.get("plant_id"));
				listGetMap.put("plant_name", jsonObject.get("plant_name"));
				listGetMap.put("device_name", jsonObject.get("device_name"));
				listGetMap.put("area_id",jsonObject.get("area_id"));
				listGetMap.put("location",jsonObject.get("location"));
				listGetMap.put("singlePlantType",jsonObject.get("singlePlantType"));
				listGetMap.put("status",jsonObject.get("status_name"));
				listGetMap.put("alarm_id",jsonObject.get("alarm_id"));
				list.add(listGetMap);

			}
			System.out.println("keys:"+keys+" v:"+list);
			//将设备id与该设备遥信信息列表存入map
			list = sortList(list,orderMap);
		}
		return list;
	}


    /**
     * @@Description 返回重要、次要、提示告警信息列表
     * @param
     * @param
     * @return
     */
    public MessageBean recentAlarmListByLevel(Map<String, Object> jsonMap) {
        List<Map<String, String>> listMap = (List<Map<String, String>>) jsonMap.get("orderName");
        Map orderMap = new HashMap();
        orderMap = listMap.get(0);
        List<String> list = new ArrayList<>();
        List<Integer> plantIds = new ArrayList<>();
        if(jsonMap.get("plantIds")!=null&&jsonMap.get("plantIds")!="") {
            list.add((String) jsonMap.get("plantIds"));
            plantIds = list.stream().map(Integer::parseInt).collect(Collectors.toList());//转为integer数组
        }
        //根据电站Id排序并获取告警列表
        System.out.print(jsonMap.toString());
        String s = (String) jsonMap.get("start");
        Integer start = Integer.parseInt(s);
        Integer length = 20;
        List<Map<String,Object>> subList = getAlarmListByLevel(plantIds, orderMap,jsonMap.get("level").toString());
        Integer total = subList.size();
        return returnSubList(subList,total,length,start);

    }
    public List<Map<String,Object>> getAlarmListByLevel(List<Integer> plantIds,Map<String,String> orderMap,String level){
        List<Integer> allDevIds = new ArrayList<>();
        //map<电站编号,map<设备类型，list<设备编号>>
        Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;  //从设备缓存取出设备id
        //根据电站id获取该电站的所有设备告警信息
        if(plantIds!=null){
            System.out.print("第几："+plantIds.get(0));
            for(Integer plantId:plantIds){
                for(List<Integer> devIds : deviceOfPlant.get(plantId).values()){
                    allDevIds.addAll(devIds);
                }
            }
            List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
            System.out.println(strDevs.toString());
            return getAlarmListMap(strDevs,orderMap,level);
        }
        //全部电站
        System.out.println("全部");
            for(Integer plantId:deviceOfPlant.keySet()){
                for(List<Integer> devIds : deviceOfPlant.get(plantId).values()){
                    allDevIds.addAll(devIds);
                }
            }
            List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
            System.out.println(strDevs.toString());
            return getAlarmListMap(strDevs,orderMap,level);
    }

    //获取不同等级的告警列表
    public List<Map<String, Object>> getAlarmListMap(List<String> strDevs,Map<String, String> orderMap,String level){
        //map<设备编号,map<遥信id,告警json>>
        Map<String, Map<String, String>> allKeysMapValue = cacheUtil0.getAllKeysMapValue(strDevs);//根据设备id取出redis中的告警列表
        Map<String,List<Map<String,Object>>> listMap = new HashMap<>();

        //先遍历告警列表的键，键为设备id
        Map<String, String> stringStringMap = new HashMap<>() ;
        List<Map<String,Object>> list = new ArrayList<>();
        for(String keys : allKeysMapValue.keySet()) {
            //List<Map<String,Object>> list = new ArrayList<>();
            stringStringMap.clear();
            stringStringMap	= allKeysMapValue.get(keys);
            //map转list<map>
            //取出该设备所有遥信信息

            for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                Map<String, Object> listGetMap = new HashMap<>();
                //listGetMap.put(entry.getKey(), entry.getValue());
                listGetMap.put("yx_id", entry.getKey());
                JSONObject jsonObject = JSONObject.parseObject(entry.getValue());
                if(level.equals(jsonObject.get("level"))) {
                    listGetMap.put("task_status", jsonObject.get("task_status"));
                    listGetMap.put("data_time_f", jsonObject.get("data_time_f"));
                    listGetMap.put("data_time", jsonObject.get("data_time"));
                    listGetMap.put("level", jsonObject.get("level"));
                    listGetMap.put("status_value", jsonObject.get("status_value"));
                    listGetMap.put("sys_time", jsonObject.get("sys_time"));
                    listGetMap.put("device_id", jsonObject.get("device_id"));
                    listGetMap.put("signal_name", jsonObject.get("signal_name"));
                    listGetMap.put("plant_id", jsonObject.get("plant_id"));
                    listGetMap.put("plant_name", jsonObject.get("plant_name"));
                    listGetMap.put("device_name", jsonObject.get("device_name"));
                    listGetMap.put("area_id",jsonObject.get("area_id"));
                    list.add(listGetMap);
                }
            }
            System.out.println("keys:"+keys+" v:"+list);
            //将设备id与该设备遥信信息列表存入map
            list = sortList(list,orderMap);
        }
        return list;
    }
	//按照时间排序
    private List<Map<String, Object>> sortList(List<Map<String, Object>> list, Map<String, String> orderMap) {
        if (!CollectionUtils.isEmpty(orderMap)) {
            String orderName = orderMap.get("orderName");
            System.out.print(orderName);
            if (!Util.isEmpty(orderName)) {
                String order = null;
                String orderTemp = orderMap.get("order");
                if (!Util.isEmpty(orderTemp))
                    order = orderTemp.toUpperCase();
                if ("ASC".equals(order)) {
                    //时间戳将就按double处理有问题在再改
                    Collections.sort(list, Comparator.comparingDouble(e -> Util.getDouble(e.get(orderName))));
                } else {
                    Collections.sort(list, Comparator.comparingDouble(e -> -Util.getDouble(e.get(orderName))));
                }
            }
        }
        return list;
    }

    /**
     * @author SP0025
     *@Description 获取所有电站信息
     *
     */
    public MessageBean getPlantInfo(){
        List<Integer> allDevIds = new ArrayList<>();
        //map<电站编号,map<设备类型，list<设备编号>>
        Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;  //从设备缓存取出设备id
        for(Integer plantIds:deviceOfPlant.keySet()){
            for(List<Integer> devIds : deviceOfPlant.get(plantIds).values()){
                allDevIds.addAll(devIds);
            }
        }
        List<String> strDevs = allDevIds.stream().map(String::valueOf).collect(Collectors.toList());
        Map<String, Map<String, String>> allKeysMapValue = cacheUtil0.getAllKeysMapValue(strDevs);//根据设备id取出redis中的告警列表
        //先遍历告警列表的键，键为设备id
        Map<String, String> stringStringMap = new HashMap<>() ;
        List<Map<String,Object>> list = new ArrayList<>();
        for(String keys : allKeysMapValue.keySet()) {
            //List<Map<String,Object>> list = new ArrayList<>();
            stringStringMap.clear();
            stringStringMap	= allKeysMapValue.get(keys);
            //map转list<map>
            //取出该设备的所有信息
            for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                Map<String, Object> listGetMap = new HashMap<>();
                //listGetMap.put(entry.getKey(), entry.getValue());
                listGetMap.put("yx_id", entry.getKey());
                JSONObject jsonObject = JSONObject.parseObject(entry.getValue());
                    listGetMap.put("device_id", jsonObject.get("device_id"));
                    listGetMap.put("signal_name", jsonObject.get("signal_name"));
                    listGetMap.put("plant_id", jsonObject.get("plant_id"));
                    listGetMap.put("plant_name", jsonObject.get("plant_name"));
                    listGetMap.put("device_name", jsonObject.get("device_name"));
                    list.add(listGetMap);
                }
    }
    Map<String,Object> result = new HashMap();
        result.put("data",list);
        result.put("total",list.size());
    MessageBean messageBean = MessageBeanUtil.getOkMB(result);
    return messageBean;
}
}