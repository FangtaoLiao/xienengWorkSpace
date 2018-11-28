package com.synpower.serviceImpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.CollDevice;
import com.synpower.bean.CollModelDetailMqtt;
import com.synpower.bean.CollYkytExpand;
import com.synpower.bean.CollYxExpand;
import com.synpower.bean.DeviceControlData;
import com.synpower.bean.DeviceDetailStringInverter;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysPhoto;
import com.synpower.bean.SysUser;
import com.synpower.constant.DTC;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.CollModelDetailMqttMapper;
import com.synpower.dao.CollYkytExpandMapper;
import com.synpower.dao.CollYxExpandMapper;
import com.synpower.dao.DeviceControlDataMapper;
import com.synpower.dao.DeviceSeriesModuleDetailMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.PowerPriceMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.MonitorPlantService;
import com.synpower.service.MonitorService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.ScreenService;
import com.synpower.service.StatisticalGainService;
import com.synpower.service.WXMonitorPlantService;
import com.synpower.util.CacheUtil;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;
import com.synpower.util.ApiUtil;

/*****************************************************************************
 * @Package: com.synpower.serviceImpl ClassName: WXMonitorServiceImpl
 * @Description: 微信监控模块业务处理类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年12月16日上午10:47:34 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@Service
public class WXMonitorPlantServiceImpl implements WXMonitorPlantService {
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private StatisticalGainService statisticalGainService;
	@Autowired
	private PowerPriceMapper priceMapper;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private CollModelDetailMqttMapper mqttMapper;
	@Autowired
	private CollYkytExpandMapper ykytExpandMapper;
	@Autowired
	private DeviceControlDataMapper controlMapper;
	@Autowired
	private MonitorPlantService monitorPlantService;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private DeviceSeriesModuleDetailMapper deviceSeriesModuleDetailMapper;
	@Autowired
	private CollYxExpandMapper collYxExpandMapper;

	/**
	 * @Title: getCurrentPowerWX
	 * @Description: TODO
	 * @return: Map<String,Object>
	 * @throws ServiceException
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月16日上午11:06:58
	 */
	@Override
	public Map<String, Object> getCurrentPowerWX(String jsonData) throws ServiceException {
		/**
		 * 实时功率曲线 功率kpi
		 */
		Map<String, Object> map = Util.parseURL(jsonData);
		PlantInfo plantInfo = plantMapper.getPlantInfo(map.get("plantId") + "");
		String pId = plantInfo.getId() + "";
		String nowTime = String.valueOf(map.get("dataTime"));
		if (!Util.isNotBlank(nowTime)) {
			nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
		}
		Map<String, Object> result;
		try {
			result = statisticalGainService.getPowerAndDataTime(pId, nowTime, plantInfo);
		} catch (ParseException e) {
			throw new ServiceException(Msg.TYPE_TIME_ERROR);
		}
		result.put("capacity", plantInfo.getCapacity() + plantInfo.getUnit());
		return result;
	}

	/**
	 * @Title: singlePlant
	 * @Description: 单电站动态信息
	 * @return: Map<String,Object>
	 * @throws ServiceException
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月16日上午11:06:58
	 */
	@Override
	public MessageBean singlePlant(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String pId = map.get("plantId") + "";
		PlantInfo plantInfo = plantMapper.getPlantById(pId);
		Map<String, Object> genIncome = new HashMap<>(3);
//		Map<String, Object>genIncomMap=statisticalGainService.getNowDayIncome(pId);
		// Map<String, Object>changeMap=ServiceUtil.changeUnit(new
		// String[]{"度","万度","亿度"},Double.parseDouble("null".equals(genIncomMap.get("power")+"")?"0":genIncomMap.get("power")+""),1000);
		// 今日发电
		Map<String, Object> todayMap = statisticalGainService.getNowDayIncome(pId);
		String plantType = SystemCache.plantTypeList.get(pId);
		if ("1".equals(plantType)) {
			genIncome.put("genToday", todayMap.get("power"));
			// 今日发电单位
			genIncome.put("genUToday", "度");
			// 今日收益
			// 单位换算
			// changeMap.clear();
			// changeMap=ServiceUtil.changeUnit(new
			// String[]{"元","万元","亿"},Double.parseDouble("null".equals(genIncomMap.get("inCome")+"")?"0":genIncomMap.get("inCome")+""),10000);
			// String
			// proToday="null".equals(genIncomMap.get("inCome")+"")?"0":genIncomMap.get("inCome")+"";
			genIncome.put("proToday", todayMap.get("inCome"));
			// 今日单位
			genIncome.put("proUToday", "元");
			// Map<String, Double>historyMap=priceMapper.getHistoryIncome(pId);
			// changeMap.clear();
			// changeMap=ServiceUtil.changeUnit(new
			// String[]{"度","万度","亿度"},historyMap==null?0.0:historyMap.isEmpty()?0.0:historyMap.get("power"),10000);
			Map<String, Object> tempMap = statisticalGainService.getHistoryIncome(pId);
			genIncome.put("genTotal", tempMap.get("power"));
			genIncome.put("genUTotal", "度");
			// changeMap.clear();
			// changeMap=ServiceUtil.changeUnit(new
			// String[]{"元","万元","亿"},historyMap==null?0.0:historyMap.isEmpty()?0.0:historyMap.get("price"),10000);
			// 总收益
			genIncome.put("proTotal", tempMap.get("inCome"));
			genIncome.put("proUTotal", "元");
		} else if ("2".equals(plantType)) {
			genIncome.put("disToday", todayMap.get("dailyPosEnergy"));
			// 今日收益
			genIncome.put("proToday", todayMap.get("dailyPrice"));
			// 累计收益
			genIncome.put("proTotal", todayMap.get("totalPrice"));
			// 累计充电
			genIncome.put("chargeTotal", todayMap.get("totalNegEnergy"));
			// 累计放电
			genIncome.put("disTotal", todayMap.get("totalPosEnergy"));
			genIncome.put("soc", SystemCache.plantSOC.get(pId));

		}
		msg.setBody(genIncome);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public MessageBean storageSinglePlant(String pId, MessageBean msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map todayMap = statisticalGainService.getNowDayIncome(pId);
		// 今日放电
		resultMap.put("disToday", todayMap.get("dailyPosEnergy"));
		// 今日收益
		resultMap.put("proToday", todayMap.get("dailyPrice"));
		// 累计收益
		resultMap.put("proTotal", todayMap.get("totalPrice"));
		// 累计充电
		resultMap.put("chargeTotal", todayMap.get("totalNegEnergy"));
		// 累计放电
		resultMap.put("disTotal", todayMap.get("totalPosEnergy"));
		resultMap.put("soc", SystemCache.plantSOC.get(pId));
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}

	@Override
	public MessageBean singleDynamic(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String pId = String.valueOf(map.get("plantId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		PlantInfo plantInfo = plantMapper.getPlantInfo(pId);
		String plantType = SystemCache.plantTypeList.get(pId);
		map.clear();
		map.put("addr", plantInfo.getPlantAddr()
				+ (Util.isNotBlank(plantInfo.getPlantFullAddress()) ? plantInfo.getPlantFullAddress() : ""));
		map.put("capacity", plantInfo.getCapacity() + "" + plantInfo.getUnit());
		String photo = plantInfo.getPlantPhoto();
		String[] photos = null;
		if (Util.isNotBlank(photo)) {
			photos = StringUtils.split(photo, ";");
		}
		String[] locaiton = plantInfo.getLoaction().split(",");
		String temp = locaiton[0];
		locaiton[0] = locaiton[1];
		locaiton[1] = temp;
		List<String> reList = new ArrayList<>();
		// 查询当前组织下总组件块数
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		reList.add(pId);
		parameterMap.put("reList", reList);
		// Map<String, Double> mapParam =;
		MessageBean msgTemp = singlePlant(jsonData);
		// 查询当前组织下总电站面积
		String totalArea = plantMapper.getTotalArea(parameterMap);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("plants", reList);
		map2.put("types", new int[] { 1, 2 });
		List<Integer> deviceList = deviceMapper.getDeviceIdByPlants(map2);
		int totalCount = 0;
		if (Util.isNotBlank(deviceList)) {
			totalCount = deviceSeriesModuleDetailMapper.getModuleCountForDevices(deviceList);
		}
		parameterMap.put("types", new int[] { 1, 2 });
		Integer inverterCount = 0;
		Map<Integer, Map<Integer, List<Integer>>> plantDevives = SystemCache.deviceOfPlant;
		if (Util.isNotBlank(plantDevives)) {
			Map<Integer, List<Integer>> devices = plantDevives.get(Integer.parseInt(pId));
			if (Util.isNotBlank(devices)) {
				List<Integer> deviceOne = devices.get(1);
				List<Integer> deviceTwo = devices.get(2);
				if (Util.isNotBlank(deviceOne)) {
					inverterCount += deviceOne.size();
				}
				if (Util.isNotBlank(deviceTwo)) {
					inverterCount += deviceTwo.size();
				}
			}
		}
		map2.put("module", totalCount + "块");
		map2.put("area", totalArea + "平方米");
		map2.put("inverter", inverterCount + "台");
		/** 设备状态 */
		Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(Integer.parseInt(pId));
		int dCount = 0, normal = 0, breakNum = 0, standby = 0, fault = 0, exception = 0;
		Map<String, Integer> status = SystemCache.deviceOfStatus;
		if (Util.isNotBlank(devices)) {
			for (Integer key : devices.keySet()) {
				List<Integer> tempList = devices.get(key);
				if (tempList != null && !tempList.isEmpty()) {
					dCount += tempList.size();
					for (Integer deviceId : tempList) {
						int type = status.containsKey(String.valueOf(deviceId)) ? status.get(String.valueOf(deviceId))
								: -1;
						switch (type) {
						case 0:
							normal++;
							break;
						case 1:
							standby++;
							break;
						case 2:
							fault++;
							break;
						case 3:
							breakNum++;
							break;
						case 4:
							exception++;
							break;
						default:
							break;
						}
					}
				}
			}
		}
		if ("1".equals(plantType)) {
			Map<String, Object> plantIncome = (Map<String, Object>) msgTemp.getBody();
			// 得到累计发电量
			Double totalPower = (Double) plantIncome.get("genTotal");
			// 从配置文件中获取二氧化碳减排参数
			String CO2 = configParam.getCo2();
			// 从配置文件中获取等效植树量参数
			String tree = configParam.getTree();
			// 从配置文件中获取标准煤节约参数
			String coal = configParam.getCoal();
			Double numCo2 = Util.multFloat(totalPower, Double.valueOf(CO2), 1);
			Double numTree = Util.multFloat(totalPower, Double.valueOf(tree), 1);
			Double numCoal = Util.multFloat(totalPower, Double.valueOf(coal), 1);
			List<Object> contribution = new ArrayList<Object>();
			Map<String, Object> co2Map = new HashMap<String, Object>();
			co2Map.put("name", "CO²减排");
			co2Map.put("value", numCo2);
			co2Map.put("unit", "kg");
			contribution.add(co2Map);
			Map<String, Object> treeMap = new HashMap<String, Object>();
			treeMap.put("name", "等效植树");
			treeMap.put("value", Util.roundDoubleToInt(numTree));
			treeMap.put("unit", "棵");
			contribution.add(treeMap);
			Map<String, Object> coalMap = new HashMap<String, Object>();
			coalMap.put("name", "节约标准煤");
			coalMap.put("value", numCoal);
			coalMap.put("unit", "kg");
			contribution.add(coalMap);
			map.put("contribution", contribution);
		} else if ("2".equals(plantType)) {
			List<Integer> pcsIdList = Util.isNotBlank(devices) ? devices.get(9) : null;
			List<Integer> bmsIdList = Util.isNotBlank(devices) ? devices.get(10) : null;
			List<Map<String, String>> componentModuleList = new ArrayList<Map<String, String>>();
			Map<String, String> pcsMap = new HashMap<String, String>();
			pcsMap.put("name", "PCS");
			pcsMap.put("unit", "台");
			pcsMap.put("value", Util.isNotBlank(pcsIdList) ? pcsIdList.size() + "" : "0");
			componentModuleList.add(pcsMap);
			Map<String, String> bmsMap = new HashMap<String, String>();
			bmsMap.put("name", "电池簇");
			bmsMap.put("unit", "块");
			bmsMap.put("value", Util.isNotBlank(bmsIdList) ? bmsIdList.size() + "" : "0");
			componentModuleList.add(bmsMap);
			Map<String, String> areaMap = new HashMap<String, String>();
			areaMap.put("name", "占地面积");
			areaMap.put("unit", "平方米");
			areaMap.put("value", totalArea);
			componentModuleList.add(areaMap);
			map.put("componentModule", componentModuleList);
		}
		Map<String, Object> cMap = new HashMap<>();
		cMap.put("standby", standby);
		cMap.put("fault", fault);
		cMap.put("break", breakNum);
		cMap.put("gen", normal);
		cMap.put("abnormal", exception);
		map.put("weather", plantInfoService.getPlantTodayWeather(Integer.parseInt(pId)));
		map.put("deviceStatus", cMap);
		map.put("deviceTotal", dCount);
		map.put("deviceDis", map2);
		map.put("img", photos);
		map.put("location", locaiton);
		map.put("plantName", plantInfo.getPlantName());
		SysUser contactUser = plantInfo.getContactUser();
		map.put("contacts", contactUser == null ? "--" : contactUser.getUserName());
		map.put("contactsTel", contactUser == null ? "--" : contactUser.getUserTel());
		map.put("plantStatus", ServiceUtil.getPlantStatus(pId));
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public MessageBean storageSingleDynamic(String jsonData, Session session, MessageBean msg, PlantInfo plantInfo)
			throws ServiceException {
		Map<String, Object> map = Util.parseURL(jsonData);
		String pId = String.valueOf(map.get("plantId"));
		/** 设备状态 */
		Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(Integer.parseInt(pId));
		int dCount = 0, normal = 0, breakNum = 0, standby = 0, fault = 0, abnormal = 0;
		Map<String, Integer> status = SystemCache.deviceOfStatus;
		if (devices != null) {
			for (Integer key : devices.keySet()) {
				List<Integer> tempList = devices.get(key);
				if (tempList != null && !tempList.isEmpty()) {
					dCount += tempList.size();
					for (Integer deviceId : tempList) {
						int type = status.containsKey(String.valueOf(deviceId)) ? status.get(String.valueOf(deviceId))
								: -1;
						switch (type) {
						case 0:
							normal++;
							break;
						case 1:
							standby++;
							break;
						case 2:
							fault++;
							break;
						case 3:
							breakNum++;
							break;
						case 4:
							abnormal++;
							break;
						default:
							break;
						}
					}
				}
			}
		}
		/** 组件模块 */
		List<Integer> pcsIdList = devices.get(9);
		List<Integer> bmsIdList = devices.get(10);
		List<Map<String, String>> componentModuleList = new ArrayList<Map<String, String>>();
		Map<String, String> pcsMap = new HashMap<String, String>();
		pcsMap.put("name", "PCS");
		pcsMap.put("unit", "台");
		pcsMap.put("value", Util.isNotBlank(pcsIdList) ? pcsIdList.size() + "" : "0");
		componentModuleList.add(pcsMap);
		Map<String, String> bmsMap = new HashMap<String, String>();
		bmsMap.put("name", "电池簇");
		bmsMap.put("unit", "块");
		bmsMap.put("value", Util.isNotBlank(bmsIdList) ? bmsIdList.size() + "" : "0");
		componentModuleList.add(bmsMap);
		Map<String, String> areaMap = new HashMap<String, String>();
		areaMap.put("name", "占地面积");
		areaMap.put("unit", "平方米");
		areaMap.put("value", Util.isNotBlank(bmsIdList) ? bmsIdList.size() + "" : "0");
		componentModuleList.add(areaMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> deviceStatusMap = new HashMap<String, Object>();
		deviceStatusMap.put("gen", normal);
		deviceStatusMap.put("standby", standby);
		deviceStatusMap.put("fault", fault);
		deviceStatusMap.put("break", breakNum);
		deviceStatusMap.put("abnormal", abnormal);
		resultMap.put("deviceStatus", deviceStatusMap);
		resultMap.put("componentModule", componentModuleList);
		resultMap.put("plantName", plantInfo.getPlantName());
		resultMap.put("deviceTotal", dCount);
		SysUser contactUser = plantInfo.getContactUser();
		resultMap.put("contacts", contactUser == null ? "--" : contactUser.getUserName());
		resultMap.put("contactsTel", contactUser == null ? "--" : contactUser.getUserTel());
		resultMap.put("addr", plantInfo.getPlantAddr() + plantInfo.getPlantFullAddress());
		resultMap.put("weather", plantInfoService.getPlantTodayWeather(Integer.parseInt(pId)));
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}

	@Override
	public MessageBean singleGenCurve(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException, ParseException {
		Map<String, Object> map = Util.parseURL(jsonData);
		PlantInfo plantInfo = plantMapper.getPlantById(map.get("plantId") + "");
		int type = plantInfo.getPlantTypeA();
		// 光伏电站
		if (type == 1) {
			return monitorService.getProSearchWX(jsonData);
			// 储能电站
		} else if (type == 2) {
			return monitorPlantService.getSingleElecPro(jsonData);
			// 光储电站（暂时不做）
		} else {
			return null;
		}
	}

	public MessageBean getStorageProSearchWX(Map map) {
		String dimension = String.valueOf(map.get("dimension"));
		String plantId = String.valueOf(map.get("plantId"));
		// 存放查询出来的充电量的数据
		Map<String, Double> negEnergy = new TreeMap<String, Double>();
		// 存放查询出来的放电量的数据
		Map<String, Double> posEnergy = new TreeMap<String, Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当天的日期
		String today = sdf.format(new Date());
		return null;
	}

	@Override
	public MessageBean device(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		String plantId = String.valueOf(map.get("plantId"));
		User u = session.getAttribute(tokenId);
		Map<String, Object> result = new HashMap<>();
		/**
		 * 组装SQL查询参数
		 */
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// List<Integer>plantList=plantInfoService.getPlantsForUser(tokenId, session,
		// 1);
		List<Integer> plantList = null;
		if (Util.isNotBlank(plantId)) {
			plantList = new ArrayList<>();
			plantList.add(Integer.parseInt(plantId));
		}
		if (plantList != null && !plantList.isEmpty()) {
			paramMap.put("plants", plantList);
			List<CollDevice> deviceList = deviceMapper.listDevicesSortWX(paramMap);
			Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
			if (deviceList != null && !deviceList.isEmpty()) {
				Map<String, Integer> statusMap = SystemCache.deviceOfStatus;
				for (CollDevice collDevice : deviceList) {
					Map<String, Object> temMap = new HashMap<String, Object>();
					// 判断是不是逆变器
					String deviceId = String.valueOf(collDevice.getId());
					if (collDevice.getDeviceType() == 1 || collDevice.getDeviceType() == 2) {
						temMap.put("deviceId", collDevice.getId() + "");
						temMap.put("name", collDevice.getDeviceName());
						temMap.put("deviceType", collDevice.getDeviceType());
						temMap.put("devicePower", "NaN");
						temMap.put("type", deviceOfStatus.get(deviceId));
						Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
						String guid = guidMap.get(deviceId).get("energy");
						Map<String, String> currentParam = cacheUtil.getMap(deviceId);
						temMap.put("status", statusMap.get(deviceId));
						String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
						if (currentParam == null || currentParam.isEmpty() || guid == null
								|| StringUtils.isBlank(guid)) {
							temMap.put("devicePower", "NaN");
						} else {
							temMap.put("devicePower",
									monitorService.getNowdayIncomeOfDevice(deviceId) + StringUtil.checkBlank(unit));
						}
						resultList.add(temMap);
						// 判断是不是电表
					} else if (collDevice.getDeviceType() == 3) {
						temMap.put("deviceId", collDevice.getId() + "");
						temMap.put("name", collDevice.getDeviceName());
						temMap.put("deviceType", collDevice.getDeviceType());
						temMap.put("devicePower", "NaN");
						temMap.put("type", deviceOfStatus.get(deviceId));
						temMap.put("status", statusMap.get(deviceId));
						temMap.put("type", deviceOfStatus.get(deviceId));
						resultList.add(temMap);
						// 判断是不是数据采集器
					} else if (collDevice.getDeviceType() == 5) {
						temMap.put("deviceId", collDevice.getId() + "");
						temMap.put("name", collDevice.getDeviceName());
						temMap.put("deviceType", collDevice.getDeviceType());
						temMap.put("devicePower", "NaN");
						temMap.put("type", deviceOfStatus.get(deviceId));
						temMap.put("status", statusMap.get(deviceId));
						temMap.put("type", deviceOfStatus.get(deviceId));
						resultList.add(temMap);
					}

				}
			}
		}
		map.clear();
		List res = new ArrayList();
		map.put("groupData", resultList);
		map.put("groupName", "");
		res.add(map);
		MessageBean msg = new MessageBean();
		msg.setBody(res);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean deviceWX(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		List res = new ArrayList();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		String plantId = String.valueOf(map.get("plantId"));
		User u = session.getAttribute(tokenId);
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> resultList1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList3 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList4 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList5 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList6 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList7 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList8 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList9 = new ArrayList<Map<String, Object>>();
		String stringInverterUnit = "";
		String meterUnit = "";
		String pcsUnit = "";
		String bmsUnit = "";
		String boxUnit = "";
		String centralInverterUnit = "";
		String boxChangeUnit = "";
		String envMonitorUnit = "";
		List<CollDevice> deviceList = deviceMapper.listDevicesWX(plantId);
		Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
		if (Util.isNotBlank(deviceList)) {
			for (int i = 0, size = deviceList.size(); i < size; i++) {
				CollDevice collDevice = deviceList.get(i);
				Map<String, Object> temMap = new HashMap<String, Object>();
				// 判断是不是逆变器
				String deviceId = String.valueOf(collDevice.getId());
				int type = collDevice.getDeviceType();
				if (type == 2) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("energy");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
					stringInverterUnit = unit;
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "NaN");
					} else {
						temMap.put("devicePower", monitorService.getNowdayIncomeOfDevice(deviceId));
					}
					resultList1.add(temMap);
					// 判断是不是电表
				} else if (type == 3) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("power");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
					meterUnit = unit;
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "");
					} else {
						temMap.put("devicePower", monitorService.getNowdayIncomeOfDevice(deviceId));
					}
					resultList2.add(temMap);
					// 判断是不是数据采集器
				} else if (type == 5) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "");
					temMap.put("type", deviceOfStatus.get(deviceId));
					temMap.put("status", deviceOfStatus.get(deviceId));
					resultList3.add(temMap);
					// 判断是不是PCS
				} else if (type == 9) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("power");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
					pcsUnit = unit;
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "");
					} else {
						temMap.put("devicePower", monitorService.getNowdayIncomeOfDevice(deviceId));
					}
					resultList4.add(temMap);
				} else if (type == 10) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("soc");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
					bmsUnit = unit;
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "");
					} else {
						temMap.put("devicePower", monitorService.getNowdayIncomeOfDevice(deviceId));
					}
					resultList5.add(temMap);
				} else if (type == 11) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("energy");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "");
					} else {
						String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
						boxUnit = unit;
						String devicePower = currentParam.containsKey(guid)
								? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
								: "0";
						temMap.put("devicePower", devicePower);
					}
					resultList6.add(temMap);
					// 判断是不是集中式逆变器
				} else if (type == 1) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("energy");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
					centralInverterUnit = unit;
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "NaN");
					} else {
						temMap.put("devicePower", monitorService.getNowdayIncomeOfDevice(deviceId));
					}
					resultList7.add(temMap);
					// 判断是不是箱变
				} else if (type == 12) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "NaN");
					temMap.put("status", deviceOfStatus.get(deviceId));
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(deviceId).get("power");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("devicePower", "");
					} else {
						String unit = mqttMapper.getDetailByGuid(guid).getDataUnit();
						boxChangeUnit = unit;
						String devicePower = currentParam.containsKey(guid)
								? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
								: "0";
						temMap.put("devicePower", devicePower);
					}
					resultList8.add(temMap);
					// 判断是不是环境监测器
				} else if (type == DTC.ENV_MONITOR) {
					temMap.put("deviceId", collDevice.getId() + "");
					temMap.put("name", collDevice.getDeviceName());
					temMap.put("deviceType", collDevice.getDeviceType());
					temMap.put("devicePower", "");
					temMap.put("status", deviceOfStatus.get(deviceId));
					resultList9.add(temMap);
				}
			}
		}
		map.clear();
		// 按顺序装载数据
		Map<String, Object> map3 = new HashMap<String, Object>(4);
		map3.put("groupData", resultList3);
		map3.put("groupName", "数采");
		map3.put("groupUnit", "");
		res.add(map3);
		Map<String, Object> map1 = new HashMap<String, Object>(4);
		map1.put("groupData", resultList1);
		map1.put("groupName", "逆变器");
		map1.put("groupUnit", stringInverterUnit);
		res.add(map1);
		Map<String, Object> map7 = new HashMap<String, Object>(4);
		map7.put("groupData", resultList7);
		map7.put("groupName", "集中式逆变器");
		map7.put("groupUnit", centralInverterUnit);
		res.add(map7);
		Map<String, Object> map6 = new HashMap<String, Object>(4);
		map6.put("groupData", resultList6);
		map6.put("groupName", "汇流箱");
		map6.put("groupUnit", boxUnit);
		res.add(map6);
		Map<String, Object> map8 = new HashMap<String, Object>(4);
		map8.put("groupData", resultList8);
		map8.put("groupName", "箱变");
		map8.put("groupUnit", boxChangeUnit);
		res.add(map8);
		Map<String, Object> map2 = new HashMap<String, Object>(4);
		map2.put("groupData", resultList2);
		map2.put("groupName", "电表");
		map2.put("groupUnit", meterUnit);
		res.add(map2);
		Map<String, Object> map4 = new HashMap<String, Object>(4);
		map4.put("groupData", resultList4);
		map4.put("groupName", "PCS");
		map4.put("groupUnit", pcsUnit);
		res.add(map4);
		Map<String, Object> map5 = new HashMap<String, Object>(4);
		map5.put("groupData", resultList5);
		map5.put("groupName", "BMS");
		map5.put("groupUnit", bmsUnit);
		res.add(map5);
		Map<String, Object> map9 = new HashMap<String, Object>(4);
		map9.put("groupData", resultList9);
		map9.put("groupName", "环境监测器");
		map9.put("groupUnit", envMonitorUnit);
		res.add(map9);
		MessageBean msg = new MessageBean();
		msg.setBody(res);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean deviceDetail(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String type = String.valueOf(map.get("type"));
		String deviceId = String.valueOf(map.get("deviceId"));
		String uid = session.getAttribute(String.valueOf(map.get("tokenId"))).getId();
		CollDevice device = deviceMapper.getDeviceById(deviceId);
		String dataModel = String.valueOf(device.getDataMode());
		DeviceDetailStringInverter mode = device.getDeviceDetailStringInverter();
		Map<String, String> basicInfo = new HashMap<>(9);
		map.clear();
		basicInfo.put("connTime", Util.timeFormate(device.getConnTime(), "yyyy-MM-dd"));
		basicInfo.put("modal", mode == null ? "" : mode.getModel());
		basicInfo.put("brand", mode == null ? "" : mode.getManufacturer());
		basicInfo.put("power",
				mode == null ? "NaN" : Util.isNotBlank(mode.getPower()) ? mode.getPower() + "千瓦" : "NaN");
		basicInfo.put("sn", device.getDeviceSn());
//		Map<String, Integer> m = SystemCache.deviceOfStatus;
		int status = SystemCache.deviceOfStatus.get(deviceId);
		basicInfo.put("type", String.valueOf(status));
		int deviceType = device.getDeviceType();
		basicInfo.put("status", ServiceUtil.getDeviceStatus(Integer.parseInt(type), status));
		List<List<Object>> reultList = new ArrayList<>();

		// 数据模块
		List<Object> tempList = new ArrayList<>();
		Map<String, String> guidMap = cacheUtil.getMap(deviceId);
		if (guidMap != null && !guidMap.isEmpty()) {
			// 逆变器
			if (1 == deviceType || 2 == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
			} else if (3 == deviceType) {
				String[] params = new String[] { "em_ua", "em_ub", "em_uc", "em_uab", "em_ubc", "em_uca", "em_ia",
						"em_ib", "em_ic", "em_frequency", "em_active_power", "em_reactive_power", "em_power_factor" };
				Map<String, Object> paramMap = new ConcurrentHashMap<>();
				paramMap.put("tableName", "data_electric_meter");
				paramMap.put("deviceId", dataModel);
				paramMap.put("list", params);
				List<CollModelDetailMqtt> powerGuidList = mqttMapper.getSignalValueOfElectric(paramMap);
				for (CollModelDetailMqtt collModelDetailMqtt : powerGuidList) {
					Map<String, String> tempMap = new HashMap<>();
					String guid = collModelDetailMqtt.getSignalGuid();
					tempMap.put("name", collModelDetailMqtt.getSignalName());
					String value = guidMap.get(guid);
					if (value == null || value.isEmpty()) {
						tempMap.put("value", "-- " + StringUtil.checkBlank(collModelDetailMqtt.getDataUnit()));
					} else {
						tempMap.put("value", value + " " + StringUtil.checkBlank(collModelDetailMqtt.getDataUnit()));
					}
					tempList.add(tempMap);
				}
				reultList.add(tempList);
				// PCS数据
			} else if (9 == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
				// BMS数据
			} else if (10 == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
				// 汇流箱数据
			} else if (11 == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
				// 箱变数据
			} else if (12 == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
			} else if (DTC.ENV_MONITOR == deviceType) {
				reultList = getDeviceNum(reultList, dataModel, guidMap);
			}
			// 逆变器数据为空
		} else if (1 == deviceType || 2 == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
			// PCS数据为空
		} else if (9 == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
			// BMS数据为空
		} else if (10 == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
			// 汇流箱数据为空
		} else if (11 == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
			// 箱变数据为空
		} else if (12 == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
		} else if (DTC.ENV_MONITOR == deviceType) {
			reultList = getDeviceNumIsNull(reultList, dataModel);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("deviceType", deviceType);

		List<Object> tempList2 = new ArrayList<>();
		List<CollModelDetailMqtt> yxList = mqttMapper.getYXValueOfDevice(dataModel);
		Map<String, String> mqtt = cacheUtil.getMap(deviceId);
		if (mqtt != null && !mqtt.isEmpty()) {
			if (1 == deviceType || 2 == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			} else if (9 == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			} else if (10 == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			} else if (11 == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			} else if (12 == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			} else if (DTC.ENV_MONITOR == deviceType) {
				tempList2 = getDeviceYxNum(tempList2, yxList, mqtt);
			}
		} else {
			if (1 == deviceType || 2 == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			} else if (9 == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			} else if (10 == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			} else if (11 == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			} else if (12 == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			} else if (DTC.ENV_MONITOR == deviceType) {
				tempList2 = getDeviceYxNumIsNull(tempList2, yxList);
			}
		}
		data.put("showData", reultList);
		/** 远程监控 */
		Map<String, Object> monitor = new HashMap<>(10);
		List<CollModelDetailMqtt> yt = mqttMapper.getYTValueOfDevice(dataModel);
		List<CollModelDetailMqtt> yk = mqttMapper.getYKValueOfDevice(dataModel);
		List<Object> setBtns = new ArrayList<>();
		List<Object> setDatas = new ArrayList<>();
		if (yt != null && !yt.isEmpty()) {
			for (int i = 0; i < yt.size(); i++) {
				CollModelDetailMqtt ytBean = yt.get(i);
				Map<String, String> tempMap = new HashMap<>();
				tempMap.put("label", ytBean.getSignalName());
				tempMap.put("guid", device.getId().intValue() + "_" + ytBean.getSignalGuid());
				tempMap.put("unit", ytBean.getDataUnit());
				tempMap.put("validMax", ytBean.getMaxval() + "");
				tempMap.put("validMin", ytBean.getMinval() + "");
				String value = null;
				DeviceControlData controlData = controlMapper.getValueOfGuid(ytBean.getSignalGuid());
				if (controlData != null) {
					value = controlData.getValue() + "";
				}
				tempMap.put("value", value);
				setDatas.add(tempMap);
			}
		}
		String ykGuid = null, ykValue = null;
		if (yk != null && !yk.isEmpty()) {
			for (int i = 0; i < yk.size(); i++) {
				String ykId = yk.get(i).getSignalGuid();
				List<CollYkytExpand> ykList = ykytExpandMapper.getValuesOfYK(ykId);
				List<String> ykGuids = new ArrayList<>();
				if (ykList != null && !ykList.isEmpty()) {
					for (CollYkytExpand collYkytExpand : ykList) {
						Map<String, String> tempMap = new HashMap<>(2);
						String guid = collYkytExpand.getSignalGuid();
						tempMap.put("guid", device.getId().intValue() + "_" + guid);
						String label = collYkytExpand.getSignalName();
						switch (label) {
						case "开机":
							tempMap.put("tipTitle", configParam.getBootTipTitle());
							tempMap.put("tipContent", configParam.getBootTipContent());
							break;
						case "关机":
							tempMap.put("tipTitle", configParam.getShutdownTipTitle());
							tempMap.put("tipContent", configParam.getShutdownTipContent());
							break;
						default:
							tempMap.put("tipTitle", "远程操作");
							tempMap.put("tipContent", "您将对操作造成的一切后果承担所有责任！");
							break;
						}
						tempMap.put("label", label);
						tempMap.put("value", collYkytExpand.getStatusValue() + "");
						setBtns.add(tempMap);
						ykGuids.add(device.getId().intValue() + "_" + guid);
					}
					map.clear();
					// map.put("operator", uid);
					map.put("list", ykGuids);
					DeviceControlData ykData = controlMapper.getValueOfYKGuid(map);
					if (ykData != null) {
						ykGuid = ykData.getSignalGuid();
						ykValue = String.valueOf(ykData.getValue());
					}
				}
			}
		}
		monitor.put("ykGuid", ykGuid);
		monitor.put("setDatas", setDatas);
		monitor.put("setBtns", setBtns);
		monitor.put("ykValue", ykValue);
		map.clear();
		map.put("basicMsg", basicInfo);
		map.put("data", data);
		map.put("monitor", monitor);
		map.put("status", tempList2);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public List getDeviceNum(List list, String dataModel, Map<String, String> guidMap) {
		List<Object> tempList = new ArrayList<>();
		List<Object> tempList2 = new ArrayList<>();
		List<Object> tempList3 = new ArrayList<>();
		List<CollModelDetailMqtt> mqttList = mqttMapper.getSignalValueOfInveter(dataModel);
		for (int i = 0; i < mqttList.size(); i++) {
			String guid = mqttList.get(i).getSignalGuid();
			String name = mqttList.get(i).getSignalName();
			String unit = mqttList.get(i).getDataUnit();
			String value = guidMap.get(guid);
			value = Util.getDecimalFomat2().format(Double
					.parseDouble(value == null ? "0" : "".equals(value) ? "0" : "null".equals(value) ? "0" : value));
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("name", name);
			if (value == null || "".equals(value)) {
				tempMap.put("value", "-- " + StringUtil.checkBlank(unit));
			} else {
				tempMap.put("value", value + " " + StringUtil.checkBlank(unit));
			}
			String guidType = mqttList.get(i).getVisibility();
			switch (guidType) {
			case "2":
				tempList.add(tempMap);
				break;
			case "3":
				tempList2.add(tempMap);
				break;
			default:
				tempList3.add(tempMap);
				break;
			}
		}
		list.add(tempList);
		list.add(tempList2);
		list.add(tempList3);
		return list;
	}

	public List getDeviceNumIsNull(List list, String dataModel) {
		List<Object> tempList = new ArrayList<>();
		List<Object> tempList2 = new ArrayList<>();
		List<Object> tempList3 = new ArrayList<>();
		List<CollModelDetailMqtt> mqttList = mqttMapper.getSignalValueOfInveter(dataModel);
		for (int i = 0, size = mqttList.size(); i < size; i++) {
			CollModelDetailMqtt mqtt = mqttList.get(i);
			String guid = mqtt.getSignalGuid();
			String name = mqtt.getSignalName();
			String unit = mqtt.getDataUnit();
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("name", name);
			tempMap.put("value", "-- " + StringUtil.checkBlank(unit));
			String guidType = mqtt.getVisibility();
			switch (guidType) {
			case "2":
				tempList.add(tempMap);
				break;
			case "3":
				tempList2.add(tempMap);
				break;
			default:
				tempList3.add(tempMap);
				break;
			}
		}
		list.add(tempList);
		list.add(tempList2);
		list.add(tempList3);
		return list;
	}

	public List getDeviceYxNum(List list, List<CollModelDetailMqtt> yxList, Map<String, String> mqtt) {
		CollYxExpand collYxExpand = new CollYxExpand();
		for (int i = 0, size = yxList.size(); i < size; i++) {
			CollModelDetailMqtt collModelDetailMqtt = yxList.get(i);
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("name", collModelDetailMqtt.getSignalName());
			String value = mqtt.get(collModelDetailMqtt.getSignalGuid());
			if (!Util.isNotBlank(value)) {
				value = "0";
			}
			if (!"0".equals(value) && !"1".equals(value)) {
				collYxExpand.setSignalGuid(collModelDetailMqtt.getSignalGuid());
				collYxExpand.setStatusValue(Integer.parseInt(value));
				value = String.valueOf(collYxExpandMapper.getYxValueCount(collYxExpand));
			}
			tempMap.put("value", value);
			list.add(tempMap);
		}
		return list;
	}

	public List getDeviceYxNumIsNull(List list, List<CollModelDetailMqtt> yxList) {
		for (int i = 0, size = yxList.size(); i < size; i++) {
			CollModelDetailMqtt collModelDetailMqtt = yxList.get(i);
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("name", collModelDetailMqtt.getSignalName());
			tempMap.put("value", "0");
			list.add(tempMap);
		}
		return list;
	}

	@Override
	public MessageBean updateValueOfGuid(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		return monitorPlantService.updateValueOfGuid(jsonData, session);
	}
}
