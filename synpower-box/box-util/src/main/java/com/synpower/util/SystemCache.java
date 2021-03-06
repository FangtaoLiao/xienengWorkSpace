package com.synpower.util;

import com.synpower.bean.*;
import com.synpower.constant.DTC;
import com.synpower.constant.DevJoinType;
import com.synpower.constant.PTAC;
import com.synpower.dao.*;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/*****************************************************************************
 * @Package: com.synpower.util ClassName: SystemCache
 * @Description: 系统数据缓存 主要用于实时收益电量功率缓存 设备状态
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月21日上午10:36:41 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public class SystemCache {
	private Logger logger = Logger.getLogger(SystemCache.class);
	@Autowired
	private ConfigParam configParam;
	/**
	 * 业务支持数据操作接口
	 */
	private static InventerStorageDataMapper inventerStorageDataMapper;
	private static DataElectricMeterMapper dataElectricMeterMapper;
	private static CollDeviceMapper collDeviceMapper;
	private static CacheUtil cacheUtil;
	private static CollModelDetailMqttMapper collModelDetailMqttMapper;
	private static CollYxExpandMapper collYxExpandMapper;
	private static PlantInfoMapper plantInfoMapper;
	private static DataPcsMapper dataPcsMapper;
	private static EnergyStorageDataMapper energyStorageDataMapper;
	private static ElecCapDecMapper elecCapDecMapper;
	private static ElectricStorageDataMapper electricStorageDataMapper;
	private static SysPowerPriceDetailMapper sysPowerPriceDetailMapper;
	private static CollSignalLabelMapper collSignalLabelMapper;

	/**
	 * 电站实时功率和收益<br>
	 * <电站id,<{energy,power,price},value>>
	 */
	public static Map<String, Map<String, Double>> currentDataOfPlant = null;
	/**
	 * 电站历史收益 <br>
	 * <电站id,<{power,price},value>>
	 */
	public static Map<String, Map<String, Double>> historyDataOfPlant = null;
	/**
	 * 每个储能电表历史最新记录 <br>
	 * 今天之前最大
	 */
	public static List<DataElectricMeter> electricBeforDatas = null;
	/**
	 * 每个PCS历史最新记录<br>
	 * 昨天一点到今天一点
	 */
	public static List<DataElectricMeter> pcsBeforDatas = null;
	/**
	 * 设备对应的功率 电能点位<br>
	 * 设备guid点表缓存<设备id,<点作用,点标识guid>>
	 */
	public static Map<String, Map<String, String>> devicesGuid = null;
	/**
	 * 根据设备类型分类<br>
	 * <设备类型id,该类型设备列表>
	 */
	public static Map<Integer, List<Integer>> deviceMap = null;
	/**
	 * 电站的设备分类集合 <br>
	 * <电站id,<设备类型id,该电站该类列设备id列表>>
	 */
	public static Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = null;

	/**
	 * @Author lz
	 * @Description: 新增加入电站状态判断时需要的设备缓存
	 * @Date: 2018/10/25 14:38
	 **/
	public static Map<Integer, Map<Integer, List<Integer>>> deviceOfPlantForStatus = null;
	/**
	 * 设备状态<br>
	 * <设备id,状态码>
	 */
	public static Map<String, Integer> deviceOfStatus = null;

	/**
	 * 根据父设备（数采和pcs）分类<br>
	 * <父设备id,(子设备id_子设备类型(data_type)_子设备点表模型(data_mode))列表>
	 */
	public static Map<String, List<String>> deviceOfCollecter = null;
	/**
	 * 设备列表集合表coll_device的所有字段
	 */
	public static List<CollDevice> deviceList = null;
	/**
	 * 视频播放token
	 */
	public static String accessToken = null;
	/**
	 * 电站类型分类 eg:<<"1",<"1","2","3","4">>,<"2",<"1","2","3","4">>> <br>
	 * 这是所有电站类型和电站id列表的映射<br>
	 * <电站类型,电站id列表>
	 */
	public static Map<String, List<String>> plantTypeMap = null;
	/**
	 * 电站类型 这是所有电站id和电站类型的映射<br>
	 * <电站id,电站类型id>
	 */
	public static Map<String, String> plantTypeList = null;
	/**
	 * 电站SOC <br>
	 * <电站id,value>
	 */
	public static Map<String, String> plantSOC = null;
	/**
	 * 储能电站实时电量<br>
	 * <电站id,value>
	 */
	public static Map<String, String> plantCurrentEnergy = null;
	/**
	 * 储能电站实时电池输入功率<br>
	 * <电站id,value>
	 */
	public static Map<String, Double> plantCurrentEnergyPowerBat = null;
	/**
	 * 储能电站实时电网功率 <br>
	 * <电站id,value>
	 */
	public static Map<String, Double> plantCurrentEnergyPower = null;

	/**
	 ************************************* 能效缓存***********************************************
	 **/
	/**
	 * @Author lz 电站申报容量Map<plantId, Map<year, Map<month,Map<Integer,Double>>>>
	 *         0：申报容量，1：申报单价，2：基础电费
	 **/
	public static Map<String, Map<String, Map<String, Map<Integer, Double>>>> plantEclecCap = null;

	/**
	 * @author ybj 能效电站点表历史最新记录 Map<点表id，电表实体> 暂时没用
	 */
	public static Map<Integer, DataElectricMeter> elecEEBeforDatas = null;

	/**
	 * @author ybj 能效电站月缓存和年缓存<plantID,<[0,1],<[],value>>>
	 */
	public static Map<Integer, Map<Integer, Map<String, Double>>> historyDataOfEEPlant = null;

	/**
	 * @author ybj 能效电站最大值和最小值功率因数缓存<pantId,<[min-最小值，max-最大值],value>>
	 */
	public static Map<Integer, Map<String, Double>> minAndMinFactorOfEEPlant = null;

	/**
	 * 静态变量注入
	 */
	public InventerStorageDataMapper getInventerStorageDataMapper() {
		return inventerStorageDataMapper;
	}

	public void setInventerStorageDataMapper(InventerStorageDataMapper inventerStorageDataMapper) {
		this.inventerStorageDataMapper = inventerStorageDataMapper;
	}

	public void setDataElectricMeterMapper(DataElectricMeterMapper dataElectricMeterMapper) {
		SystemCache.dataElectricMeterMapper = dataElectricMeterMapper;
	}

	public DataElectricMeterMapper getDataElectricMeterMapper() {
		return dataElectricMeterMapper;
	}

	public CollDeviceMapper getCollDeviceMapper() {
		return collDeviceMapper;
	}

	public void setCollDeviceMapper(CollDeviceMapper collDeviceMapper) {
		this.collDeviceMapper = collDeviceMapper;
	}

	public void setCollModelDetailMqttMapper(CollModelDetailMqttMapper collModelDetailMqttMapper) {
		this.collModelDetailMqttMapper = collModelDetailMqttMapper;
	}

	public CollModelDetailMqttMapper getCollModelDetailMqttMapper() {
		return collModelDetailMqttMapper;
	}

	public CacheUtil getCacheUtil() {
		return cacheUtil;
	}

	public void setCacheUtil(CacheUtil cacheUtil) {
		this.cacheUtil = cacheUtil;
	}

	public void setCollYxExpandMapper(CollYxExpandMapper collYxExpandMapper) {
		this.collYxExpandMapper = collYxExpandMapper;
	}

	public CollYxExpandMapper getCollYxExpandMapper() {
		return collYxExpandMapper;
	}

	public void setPlantInfoMapper(PlantInfoMapper plantInfoMapper) {
		this.plantInfoMapper = plantInfoMapper;
	}

	public PlantInfoMapper getPlantInfoMapper() {
		return plantInfoMapper;
	}

	public void setDataPcsMapper(DataPcsMapper dataPcsMapper) {
		SystemCache.dataPcsMapper = dataPcsMapper;
	}

	public DataPcsMapper getDataPcsMapper() {
		return dataPcsMapper;
	}

	public void setEnergyStorageDataMapper(EnergyStorageDataMapper energyStorageDataMapper) {
		SystemCache.energyStorageDataMapper = energyStorageDataMapper;
	}

	public EnergyStorageDataMapper getEnergyStorageDataMapper() {
		return energyStorageDataMapper;
	}

	public void setElecCapDecMapper(ElecCapDecMapper elecCapDecMapper) {
		SystemCache.elecCapDecMapper = elecCapDecMapper;
	}

	public ElecCapDecMapper getElecCapDecMapper() {
		return elecCapDecMapper;
	}

	public static ElectricStorageDataMapper getElectricStorageDataMapper() {
		return electricStorageDataMapper;
	}

	public static void setElectricStorageDataMapper(ElectricStorageDataMapper electricStorageDataMapper) {
		SystemCache.electricStorageDataMapper = electricStorageDataMapper;
	}

	public SysPowerPriceDetailMapper getSysPowerPriceDetailMapper() {
		return sysPowerPriceDetailMapper;
	}

	public void setSysPowerPriceDetailMapper(SysPowerPriceDetailMapper sysPowerPriceDetailMapper) {
		this.sysPowerPriceDetailMapper = sysPowerPriceDetailMapper;
	}

	public void setCollSignalLabelMapper(CollSignalLabelMapper collSignalLabelMapper) {
		SystemCache.collSignalLabelMapper = collSignalLabelMapper;
	}

	/**
	 * 初始化
	 *
	 * @throws ServiceException
	 */
	public void init() throws ServiceException, SessionTimeoutException, SessionException {
		logger.debug("start loading SystemCache ...");
		// 最新电表的最后一条数据更新
		/*
		 * Map<String, Object>paramMap=new HashMap<>(2); paramMap.put("startTime",
		 * Util.getBeforDayMin(1)); paramMap.put("endTime", Util.getBeforDayMax(1));
		 * electricBeforDatas=dataElectricMeterMapper.getLastHistoryDataElectric(
		 * paramMap);
		 * electricBeforDatas=dataElectricMeterMapper.getLastHistoryDataElectric(
		 * paramMap);
		 */

		updatePlantType();

		updateDeviceList();

		updateDeviceMap();

		getGuidForDevice();

		updateElectricBeforDatas();

//		updateElecEEBeforeDatas();

		updateDeviceOfCollecter();

		updateDeviceOfStatus();

		getNowDayIncome();

		updateHistoryDataPlant();

		updatePlantSOC();

		updatePlantEclecCap();

		updateHistoryDataOfEEPlant();

		updateMinAndMaxFactorOfEEPlant();

		logger.info("SystemCache loading success .");
	}

	/**
	 * @author ybj
	 * @date 2018年9月3日下午6:08:32
	 * @Description -_- 更新能效电站的最大值和最小值功率因数
	 */
	public void updateMinAndMaxFactorOfEEPlant() {
		logger.debug("更新电站的最大最小功率因数开始......");

		Map<Integer, Map<String, Double>> minAndMinFactorOfEEPlantTemp = new HashMap<>();
		// 能效电站id列表
		List<String> plantIds = plantTypeMap.get(String.valueOf(PTAC.ENERGYEFFICPLANT));
		long startTime = TimeUtil.getMon0();
		long endTime = TimeUtil.getNextOrLastTime(Calendar.MONTH, startTime, 1);

		Map<String, Object> map = new HashMap<>();

		map.put("startTime", startTime);
		map.put("endTime", endTime);

		if (Util.isNotBlank(plantIds)) {
			// 循环电站里列表
			for (String plantId : plantIds) {
				// 得到能效电站的电表id列表
				Map<Integer, List<Integer>> devices = deviceOfPlant.get(Integer.parseInt(plantId));

				// 默认是没有的
				map.put("deviceId", -1);

				if (Util.isNotBlank(devices)) {
					List<Integer> list = devices.get(DTC.ELECTRIC_METER);
					// 现在默认一个点表 以后改
					if (Util.isNotBlank(list)) {
						map.put("deviceId", list.get(0));
					}
				}

				Map<String, Double> minAndMaxFactor = dataElectricMeterMapper.getMinAndMaxFactor(map);

				minAndMinFactorOfEEPlantTemp.put(Integer.parseInt(plantId), minAndMaxFactor);

			}
		}

		minAndMinFactorOfEEPlant = minAndMinFactorOfEEPlantTemp;

		logger.debug("更新电站的最大最小功率因数完成......");
	}

	/**
	 * @author ybj
	 * @date 2018年8月28日下午7:38:29
	 * @Description -_-更新能效电站月历史和年历史
	 */
	public void updateHistoryDataOfEEPlant() {
		// <电站id,<{0-月，1-年},<{wg,yg,price,jian,feng,ping,gu},value>>>
		Map<Integer, Map<Integer, Map<String, Double>>> historyDataOfEEPlantCopy = null;

		if (Util.isNotBlank(plantTypeMap)) {

			historyDataOfEEPlantCopy = new ConcurrentHashMap<>(plantTypeMap.size());

			Set<Entry<String, List<String>>> entrySet = plantTypeMap.entrySet();
			for (Entry<String, List<String>> entry : entrySet) {
				switch (Integer.parseInt(entry.getKey())) {
				// 能效
				case PTAC.ENERGYEFFICPLANT:
					// 本月开始时间
					long currMonth = TimeUtil.getMon0();
					// 本月结束时间
					long nextMonth = TimeUtil.getNextOrLastTime(Calendar.MONDAY, currMonth, 1);
					// 本年开始时间
					long currYear = TimeUtil.getYear0();
					// 本年结束时间
					long nextYear = TimeUtil.getNextOrLastTime(Calendar.YEAR, currYear, 1);

					// 参数集
					Map<String, Object> map = new HashMap<>();

					for (String plantId : entry.getValue()) {
						// ==========================================月信息
						map.put("curr", currMonth);
						map.put("next", nextMonth);
						// 电站id
						map.put("plantId", plantId);

						Map<Integer, Map<String, Double>> value = new HashMap<>();

						// 得到每平台月相应信息
						Map<String, Double> month = electricStorageDataMapper.getElecKPI(map);

						if (Util.isNotBlank(month)) {
							value.put(0, month);
						}
						// ==========================================年信息
						map.clear();
						map.put("curr", currYear);
						map.put("next", nextYear);
						map.put("plantId", plantId);
						Map<String, Double> year = electricStorageDataMapper.getElecKPI(map);
						if (Util.isNotBlank(year)) {
							value.put(1, year);
						}
						// ==========================================
						historyDataOfEEPlantCopy.put(Integer.parseInt(plantId), value);
					}
					break;

				default:
					break;
				}
			}
		}

		historyDataOfEEPlant = historyDataOfEEPlantCopy;
		logger.info("更新能效电站月历史和年历史");
	}

	/**
	 * @author ybj
	 * @date 2018年8月28日下午2:45:47
	 * @Description -_-能效电表最新历史记录
	 */
	public void updateElecEEBeforeDatas() {
		logger.debug("clear the EnergyEffic beforDatas of SystemCache  .");

		Map<String, Object> paramMap = new HashMap<>(2);

		paramMap.put("startTime", Util.getBeforDayMin(1));

		paramMap.put("endTime", Util.getBeforDayMax(1));

		// 得到能效电站id
		List<String> plantIds = plantTypeMap.get(String.valueOf(PTAC.ENERGYEFFICPLANT));

		List<Integer> devices = new ArrayList<>();

		// 得到储能电站的所有设备
		for (String plantId : plantIds) {

			Map<Integer, List<Integer>> map = deviceOfPlant.get(Integer.parseInt(plantId));

			// 得到单个电站的电表设备列表
			List<Integer> devicess = map.get(DTC.ELECTRIC_METER);
			// 加入到总的电表列表里面
			if (Util.isNotBlank(devicess)) {
				devices.addAll(devicess);
			}

		}

		paramMap.put("devices", devices);

		List<DataElectricMeter> lastHistoryDataElectric = dataElectricMeterMapper.getLastHistoryDataElectric(paramMap);

		elecEEBeforDatas = new ConcurrentHashMap<>(lastHistoryDataElectric.size());

		for (DataElectricMeter dataElectricMeter : lastHistoryDataElectric) {
			elecEEBeforDatas.put(dataElectricMeter.getDeviceId(), dataElectricMeter);
		}

		logger.debug("the EnergyEffic beforDatas of SystemCache loading success .");
	}

	public void reInit() throws ServiceException {
		logger.debug("start loading SystemCache ...");
		updatePlantType();
		updateDeviceList();
		updateDeviceMap();
		getGuidForDevice();
		updateDeviceOfCollecter();
		updateDeviceOfStatus();
		updatePlantSOC();
		logger.info("SystemCache loading success .");
	}

	/**
	 * 得到设备guid点表缓存devicesGuid<设备id,<点作用,点标识guid>>
	 */
	public void getGuidForDevice() {

		logger.debug("clear the devicesGuid of SystemCache");
		// <设备id,<点作用,点标识guid>>
		Map<String, Map<String, String>> devicesGuidCopy = new ConcurrentHashMap<>();

		// 参数map
		Map<String, Object> paramMap = new ConcurrentHashMap<>();

		for (Integer key : deviceMap.keySet()) {

			// 该类型的的设备id集合
			List<Integer> idList = deviceMap.get(key);

			/**
			 * 集中式逆变器
			 */
			if (key == 1) {

				// 得到所有集中式逆变器的coll_model集合
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);

				paramMap.clear();

				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_central_inverter");
				paramMap.put("field", "active_power");
				// 得到集中式逆变器的功率点位

				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (powerGuidList != null && !powerGuidList.isEmpty()) {
					int size = powerGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						// <点作用,点标识guid>
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("power", mqtt.getSignalGuid());
						String dId = String.valueOf(mqtt.getdId().intValue());
						// 装载对应的功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "daily_energy");
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (energyGuidList != null && !energyGuidList.isEmpty()) {
					int size = energyGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("energy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("energy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "dc_power");
				List<CollModelDetailMqtt> mpptPowerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (mpptPowerGuidList != null && !mpptPowerGuidList.isEmpty()) {
					int size = mpptPowerGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = mpptPowerGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("dcPower", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("dcPower", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				/**
				 * 组串式逆变器
				 */
			} else if (key == 2) {

				boolean b = idList.contains(494);

				paramMap.clear();
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_string_inverter");
				paramMap.put("field", "active_power");
				// 得到集中式逆变器的功率点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (powerGuidList != null && !powerGuidList.isEmpty()) {
					int size = powerGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("power", mqtt.getSignalGuid());
						String dId = String.valueOf(mqtt.getdId().intValue());
						// 装载对应的功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "daily_energy");
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (energyGuidList != null && !energyGuidList.isEmpty()) {
					int size = energyGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("energy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("energy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "mppt_power");
				List<CollModelDetailMqtt> mpptPowerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (mpptPowerGuidList != null && !mpptPowerGuidList.isEmpty()) {
					int size = mpptPowerGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = mpptPowerGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("mpptPower", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("mpptPower", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}

					System.out.println(devicesGuidCopy);
					boolean ba = devicesGuidCopy.containsKey(494);
				}
				/**
				 * 电表
				 */
			} else if (key == 3) {
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.clear();
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_electric_meter");
				paramMap.put("field", "em_active_power");
				// 得到电表的功率点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (powerGuidList != null && !powerGuidList.isEmpty()) {
					int size = powerGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						String dId = String.valueOf(idList.get(i).intValue());
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("power", mqtt.getSignalGuid());
						// 装载对应的功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				// 正向有功电能量
				paramMap.put("field", "em_pos_active_energy");
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (energyGuidList != null && !energyGuidList.isEmpty()) {
					int size = energyGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("posEnergy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("posEnergy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				// 反向有功电能量
				paramMap.put("field", "em_neg_active_energy");
				List<CollModelDetailMqtt> energyJianGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (energyJianGuidList != null && !energyJianGuidList.isEmpty()) {
					for (int i = 0; i < energyJianGuidList.size(); i++) {
						CollModelDetailMqtt mqtt = energyJianGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("negEnergy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("negEnergy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				/**
				 * 储能变流器（Power Conversion System——PCS）
				 */
			} else if (key == 9) {
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.clear();
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_pcs");
				paramMap.put("field", "active_power");
				// 得到PCS的功率点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(powerGuidList)) {
					for (int i = 0, size = powerGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						String dId = String.valueOf(idList.get(i).intValue());
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("power", mqtt.getSignalGuid());
						// 装载对应的功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "total_energy_out");
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(energyGuidList)) {
					for (int i = 0, size = energyGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("posEnergy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("posEnergy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "total_energy_in");
				List<CollModelDetailMqtt> negEnergyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(negEnergyGuidList)) {
					for (int i = 0, size = negEnergyGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = negEnergyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("negEnergy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("negEnergy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "power_bat");
				List<CollModelDetailMqtt> powerBatList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(powerBatList)) {
					for (int i = 0, size = powerBatList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = powerBatList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("powerBat", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("powerBat", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				// 电池管理系统（BATTERY MANAGEMENT SYSTEM--BMS）
			} else if (key == 10) {
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.clear();
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_bms");
				paramMap.put("field", "soc_cell_cluster");
				// 得到BMS的soc点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(powerGuidList)) {
					for (int i = 0, size = powerGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						String dId = String.valueOf(idList.get(i).intValue());
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("soc", mqtt.getSignalGuid());
						// 装载对应的功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "bms_left_capacity");
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(energyGuidList)) {
					for (int i = 0, size = energyGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("energy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("energy", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "bms_capacity");
				List<CollModelDetailMqtt> capacityGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(capacityGuidList)) {
					for (int i = 0, size = capacityGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = capacityGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的发电量
							guidMap.put("capacity", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("capacity", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				/**
				 * 汇流箱
				 */
			} else if (key == 11) {
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.clear();
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_combiner_box");
				paramMap.put("field", "u_box");
				// 得到汇流箱的电压点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(powerGuidList)) {
					for (int i = 0, size = powerGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						String dId = String.valueOf(idList.get(i).intValue());
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("voltage", mqtt.getSignalGuid());
						// 装载对应的电压
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "i_total");
				// 得到汇流箱的电流点位
				List<CollModelDetailMqtt> energyGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(energyGuidList)) {
					for (int i = 0, size = energyGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = energyGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的电流
							guidMap.put("electricCurrent", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("electricCurrent", mqtt.getSignalGuid());
							// 装载对应的发电量
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
				paramMap.put("field", "power_total");
				// 得到汇流箱的功率点位
				List<CollModelDetailMqtt> capacityGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(capacityGuidList)) {
					for (int i = 0, size = capacityGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = capacityGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的功率
							guidMap.put("energy", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("energy", mqtt.getSignalGuid());
							// 装载对应的功率
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
			}
			/**
			 * 箱式变压器
			 */
			else if (key == 12) {
				List<Integer> modelList = collDeviceMapper.getmodelListById(idList);
				paramMap.clear();
				paramMap.put("deviceList", idList);
				paramMap.put("modelList", modelList);
				paramMap.put("tableName", "data_env_monitor");
				paramMap.put("field", "active_power");
				// 得到箱式变压器的有功功率点位
				List<CollModelDetailMqtt> powerGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (Util.isNotBlank(powerGuidList)) {
					for (int i = 0, size = powerGuidList.size(); i < size; i++) {
						CollModelDetailMqtt mqtt = powerGuidList.get(i);
						String dId = String.valueOf(idList.get(i).intValue());
						Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
						guidMap.put("power", mqtt.getSignalGuid());
						// 装载对应的有功功率
						devicesGuidCopy.put(dId, guidMap);
					}
				}
				paramMap.put("field", "u_ac_a");
				List<CollModelDetailMqtt> voltageGuidList = collModelDetailMqttMapper.getGuidForDevice(paramMap);
				if (voltageGuidList != null && !voltageGuidList.isEmpty()) {
					int size = voltageGuidList.size();
					for (int i = 0; i < size; i++) {
						CollModelDetailMqtt mqtt = voltageGuidList.get(i);
						String dId = String.valueOf(mqtt.getdId().intValue());
						if (devicesGuidCopy.containsKey(dId)) {
							Map<String, String> guidMap = devicesGuidCopy.get(dId);
							// 装载对应的电压
							guidMap.put("voltage", mqtt.getSignalGuid());
						} else {
							Map<String, String> guidMap = Collections.synchronizedMap(new HashMap<>());
							guidMap.put("voltage", mqtt.getSignalGuid());
							// 装载对应的电压
							devicesGuidCopy.put(dId, guidMap);
						}
					}
				}
			} else if (key == DTC.ENV_MONITOR) {
				List<Map<String, String>> field2GuidList = collSignalLabelMapper.getField2Guid(key);
				Map<String, String> field2GuidMap = new HashMap<>();
				for (Map<String, String> map : field2GuidList) {
					field2GuidMap.put(map.get("field"), map.get("signal_guid"));
				}
				for (Integer dId : idList) {
					devicesGuidCopy.put(String.valueOf(dId), field2GuidMap);
				}
			}
		}
		devicesGuid = devicesGuidCopy;
		logger.debug("the devicesGuid of SystemCache loading success .");
	}

	/**
	 * @param deviceList: void
	 * @Title: getDeviceOfPlant
	 * @Description: 获取电站的设备集合
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月22日下午3:20:28
	 *            根据集合列表deviceList得到deviceOfPlant<电站id,<设备类型id,给电站该类列设备id列表>>
	 */
	public void getDeviceOfPlant(List<CollDevice> deviceList) {
		logger.debug(" clear the deviceOfPlant and deviceOfPlantForStatusCopy of SystemCache");

		/**
		 * <电站id,<设备类型id,给电站该类列设备id列表>>
		 */
		Map<Integer, Map<Integer, List<Integer>>> deviceOfPlantCopy = new ConcurrentHashMap<>();
		/**
		 * <电站id,<设备类型id,给电站该类列设备id列表>> 用于电站状态判断
		 */
		Map<Integer, Map<Integer, List<Integer>>> deviceOfPlantForStatusCopy = new ConcurrentHashMap<>();

		for (CollDevice collDevice : deviceList) {
			// 设备类型 1.集中式逆变器 2.组串式逆变器 3.电表 ..
			Integer deviceType = collDevice.getDeviceType();
			// 电站id
			Integer plantId = collDevice.getPlantId();
			Integer collDeviceId = collDevice.getId();
			Map<Integer, List<Integer>> typeMap = deviceOfPlantCopy.get(plantId);
			if (typeMap == null) {
				typeMap = new HashMap<>();
				deviceOfPlantCopy.put(plantId, typeMap);
			}
			List<Integer> typeList = typeMap.get(deviceType);
			if (CollectionUtils.isEmpty(typeList)) {
				typeList = new ArrayList<>();
				typeMap.put(deviceType, typeList);
			}
			typeList.add(collDeviceId);

			Map<Integer, List<Integer>> typeMap2 = deviceOfPlantForStatusCopy.get(plantId);
			if (typeMap2 == null) {
				typeMap2 = new HashMap<>();
				deviceOfPlantForStatusCopy.put(plantId, typeMap2);
			}
			List<Integer> typeList2 = typeMap2.get(deviceType);
			if (CollectionUtils.isEmpty(typeList2)) {
				typeList2 = new ArrayList<>();
				typeMap2.put(deviceType, typeList2);
			}
			// 1允许参与电站判断
			if (DevJoinType.JOIN_PLANT_Y.equals(collDevice.getJoinPlant())) {
				typeList2.add(collDeviceId);
			}
		}
		deviceOfPlant = deviceOfPlantCopy;
		deviceOfPlantForStatus = deviceOfPlantForStatusCopy;
		logger.debug(" the deviceOfPlant and deviceOfPlantForStatusCopy of SystemCache loading success .");

		//// 所有设备数量
		// int size = deviceList.size();
		//// 遍历每台设备
		// for (int i = 0; i < size; i++) {
		// // 每个设备
		// CollDevice temp = deviceList.get(i);
		// // 设备类型 1.集中式逆变器 2.组串式逆变器 3.电表 ..
		// Integer deviceType = temp.getDeviceType();
		// // 电站id
		// Integer plantId = temp.getPlantId();
		//
		// if (deviceOfPlantCopy.containsKey(plantId)) {
		// Map<Integer, List<Integer>> tempMap = deviceOfPlantCopy.get(plantId);
		// if (tempMap.containsKey(deviceType)) {
		// List<Integer> devices = tempMap.get(deviceType);
		// devices.add(temp.getId());
		// tempMap.put(deviceType, devices);
		// } else {
		// List<Integer> devices = new ArrayList<>(1);
		// devices.add(temp.getId());
		// tempMap.put(deviceType, devices);
		// deviceOfPlantCopy.put(plantId, tempMap);
		// }
		//
		//
		// } else {
		// Map<Integer, List<Integer>> tempMap = new HashMap<>(1);
		// List<Integer> devices = new ArrayList<>(1);
		// devices.add(temp.getId());
		// tempMap.put(deviceType, devices);
		// deviceOfPlantCopy.put(plantId, tempMap);
		// }
		// }
	}

	/**
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 * @throws NumberFormatException
	 * @Title: serviceDistribution
	 * @Description: 收益, 功率 缓存<br>
	 *               得到currentDataOfPlant<电站id,<{energy,power,price},value>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月22日13:56:34
	 */
	public void getNowDayIncome() throws NumberFormatException, SessionException, SessionTimeoutException {

		logger.debug(" clear the getNowDayIncome of SystemCache");

		Map<String, Map<String, Double>> currentDataOfPlantCopy = new ConcurrentHashMap<>();

		if (Util.isNotBlank(plantTypeMap)) {

			Iterator<Entry<String, List<String>>> entries = plantTypeMap.entrySet().iterator();

			/**
			 * 遍历电站类型分类
			 */
			while (entries.hasNext()) {

				Entry<String, List<String>> entry = entries.next();
				// 电站类型
				String plantType = entry.getKey();
				// 该电站类型列表
				List<String> ids = entry.getValue();

				if (Util.isNotBlank(ids)) {

					// 该类型电站数量
					int idSize = ids.size();

					switch (plantType) {
					// 光伏电站
					case "1":
						for (int i = 0; i < idSize; i++) {

							String pId = ids.get(i);
							// 电站id
							int plantId = Integer.parseInt(pId);
							/**
							 * 得到该电站收益
							 */
							Map<String, Double> tempMap = getPVPlantIncome(plantId);

							if (Util.isNotBlank(tempMap)) {

								/**
								 * <电站id,<key{energy,power,price},value>>
								 */
								currentDataOfPlantCopy.put(pId, tempMap);
							}

						}
						break;

					// 储能电站
					case "2":
						for (int i = 0; i < idSize; i++) {
							String pId = ids.get(i);
							int plantId = Integer.parseInt(pId);
							Map<String, Double> tempMap = getEnergyPlantIncome(plantId);
							if (Util.isNotBlank(tempMap)) {
								currentDataOfPlantCopy.put(pId, tempMap);
							}
						}
						break;

					// 能效电站
					case "4":
						for (int i = 0; i < idSize; i++) {
							String pId = ids.get(i);
							Map<String, Double> tempMap = getElect(Integer.parseInt(pId), -1l, -1l, 0);
							if (Util.isNotBlank(tempMap)) {
								currentDataOfPlantCopy.put(pId, tempMap);
							}
						}
						break;
					default:
						break;
					}
				}

			}

		}

		currentDataOfPlant = currentDataOfPlantCopy;

		logger.debug(" the getNowDayIncome of SystemCache loading success .");

	}

	/**
	 * 根据集合列表deviceList得到deviceOfPlant<电站id,<设备类型id,给电站该类列设备id列表>><br>
	 * 根据集合列表deviceList得到deviceMap<设备类型id,该类型设备列表>
	 */
	public void updateDeviceMap() {

		logger.debug("clear the deviceMap of SystemCache");
		// <设备类型id,该类型设备列表>
		Map<Integer, List<Integer>> deviceMapCopy = new ConcurrentHashMap<>();

		// 得到所有设备列表
		List<CollDevice> deviceLists = deviceList;

		getDeviceOfPlant(deviceLists);

		// 得到所有设备数量
		int deviceSize = deviceLists.size();

		// 遍历设备
		for (int i = 0; i < deviceSize; i++) {
			// 每个设备
			CollDevice temp = deviceLists.get(i);

			Integer deviceType = temp.getDeviceType();

			if (deviceMapCopy.containsKey(deviceType)) {
				deviceMapCopy.get(deviceType).add(temp.getId());
			} else {
				List<Integer> list = new ArrayList<>();
				list.add(temp.getId());
				deviceMapCopy.put(deviceType, list);
			}

		}
		deviceMap = deviceMapCopy;

		logger.debug("the deviceMap of SystemCache loading success .");
	}

	/**
	 * 得到electricMeter数据electricBeforDatas 今天之前最大<br>
	 * 得到pcs数据pcsBeforDatas 今天之前最大
	 */
	public void updateElectricBeforDatas() {

		logger.debug("clear the beforDatas of SystemCache  .");

		Map<String, Object> paramMap = new HashMap<>(2);

		paramMap.put("startTime", Util.getBeforStorageDayMin(1));

		paramMap.put("endTime", Util.getBeforStorageDayMax(1));

		electricBeforDatas = dataElectricMeterMapper.getLastHistoryDataElectric(paramMap);

		pcsBeforDatas = dataPcsMapper.getLastHistoryDataPcs(paramMap);

		logger.debug("the beforDatas of SystemCache loading success .");

	}

	/**
	 * sql:select * from coll_device where device_valid=0 and plant_id is not null
	 * <br>
	 * 得到数据库中所有的设备列表deviceList
	 */
	public void updateDeviceList() {

		logger.debug("clear the deviceList of SystemCache  .");

		deviceList = collDeviceMapper.getCollDevicesForStorage();

		logger.debug("the deviceList of SystemCache loading success .");

	}

	/**
	 * 得到根据父设备分类的列表deviceOfCollecter<父设备id,子设备id_子设备类型(data_type)_子设备点表模型(data_mode)>
	 */
	public void updateDeviceOfCollecter() {

		logger.debug(" clear the deviceOfCollecter of SystemCache  .");
		// <父设备id,(子设备id_子设备类型(data_type)_子设备点表模型(data_mode))列表>
		Map<String, List<String>> deviceOfCollecterCopy = new ConcurrentHashMap<>();
		// 总设备个数
		int deviceSize = deviceList.size();
		// 遍历设备然后构造Map<String, List<String>> <父设备id,字符串空列表>
		for (int i = 0; i < deviceSize; i++) {

			CollDevice device = deviceList.get(i);
			// 父设备id
			String supId = String.valueOf(device.getSupid());
			// 设备id
			String deviceId = String.valueOf(device.getId());
			// 设备类型
			String deviceType = String.valueOf(device.getDeviceType());

			// 5是采集器 9是pcs
			if ("0".equals(supId) && "5".equals(deviceType) || "9".equals(deviceType)) {
				if (!deviceOfCollecterCopy.containsKey(deviceId)) {
					List<String> list = Collections.synchronizedList(new ArrayList<>());
					deviceOfCollecterCopy.put(deviceId, list);
				}
			}

		}

		deviceSize = deviceList.size();

		for (int i = 0; i < deviceSize; i++) {

			CollDevice device = deviceList.get(i);

			String supId = String.valueOf(device.getSupid());

			String deviceId = String.valueOf(device.getId());

			String deviceType = String.valueOf(device.getDeviceType());

			if ("1".equals(deviceType) || "2".equals(deviceType) || "3".equals(deviceType) || "4".equals(deviceType)
					|| "9".equals(deviceType) || "10".equals(deviceType) || "11".equals(deviceType)
					|| "12".equals(deviceType) || String.valueOf(DTC.ENV_MONITOR).equals(deviceType)) {
				// 根据父设备分类
				if (deviceOfCollecterCopy.containsKey(supId)) {
					deviceOfCollecterCopy.get(supId)
							.add(deviceId + "_" + deviceType + "_" + device.getDataMode().intValue());
				}

			}

		}

		deviceOfCollecter = deviceOfCollecterCopy;

		logger.debug(" the deviceOfCollecter of SystemCache  loading success.");
	}

	/**
	 * @throws ServiceException
	 * @throws IOException
	 * @Title updateDeviceOfStatus
	 * @Description 得到所有设备的状态deviceOfStatus<设备id状态码><br>
	 *              设备 状态码: <br>
	 *              逆变器: 0正常 1待机 2故障 3通讯中断 4异常 <br>
	 *              PCS 0正常 1待机 2故障 3通讯中断 4异常 <br>
	 *              BMS: 0正常 1待机 2故障 3通讯中断 4异常 汇流箱 0正常 2故障 3通讯中断 4异常<br>
	 *              箱变: 0正常 2故障 3通讯中断 4异常 电表 0正常 3通讯中断 数采 0通讯中 3通讯中断<br>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月23日下午3:37:39
	 */
	public void updateDeviceOfStatus() throws ServiceException {

		logger.debug(" clear the deviceOfCollecter of SystemCache  .");

		// 设备连接超时时间
		int timeOut = Integer.parseInt(configParam.getDeviceConnTimeOut());

		// <设备id,状态值>
		Map<String, Integer> deviceOfStatusCopy = new ConcurrentHashMap<>();

		// 遍历根据父设备id分类的map
		for (Entry<String, List<String>> entry : deviceOfCollecter.entrySet()) {
			// 父设备id
			String key = entry.getKey();

			// 从缓存中得到该设备的点集
			Map<String, String> tempMap = cacheUtil.getMap(key);

			/**
			 * 点集不为空
			 */
			if (tempMap != null && !tempMap.isEmpty()) {

				// 数采状态判断
				String connStatus = tempMap.get("conn_status");

				// 子设备id列表
				List<String> devices = entry.getValue();

				// 如果数采断连则其所有采集的设备都处理为断连
				// 若connStatus的值为0那么判断设备通讯中断，值为1则为通讯正常
				// 中断
				if (!Util.isNotBlank(connStatus) || connStatus.equals("0")) {
					deviceOfStatusCopy.put(key, 3);
					// cacheUtil.del(key);
					for (String deviceId : devices) {
						deviceId = deviceId.split("_")[0];
						deviceOfStatusCopy.put(deviceId, 3);
						// cacheUtil.del(deviceId);
					}
					// 未中断
				} else {

					deviceOfStatusCopy.put(key, 0);

					for (String deviceIds : devices) {

						String deviceId = deviceIds.split("_")[0];

						String deviceType = deviceIds.split("_")[1];

						String dataMode = deviceIds.split("_")[2];

						if ("1".equals(deviceType) || "2".equals(deviceType)) {

							List<CollYxExpand> faultPoint = collYxExpandMapper.getYxFaultPoint(dataMode);

							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);

							if (deviceMap == null || deviceMap.isEmpty()) {
								deviceOfStatusCopy.put(deviceId, 3);
								// cacheUtil.del(deviceId);
							} else {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
									deviceOfStatusCopy.put(deviceId, 3);
								} else {
									boolean b = true;
									boolean ex = true;
									boolean st = true;
									if (faultPoint != null && !faultPoint.isEmpty()) {
										for (int i = 0; i < faultPoint.size(); i++) {
											String point = faultPoint.get(i).getSignalGuid();
											String value = deviceMap.get(point);
											if (value == null || StringUtils.isBlank(value)) {
												deviceOfStatusCopy.put(deviceId, 2);
												b = false;
												break;
											} else {
												int valueCopy = Integer.parseInt(value);
												if (valueCopy == faultPoint.get(i).getStatusValue()) {
													deviceOfStatusCopy.put(deviceId, 2);
													b = false;
													break;
												}
											}
										}
									}
									if (faultPoint == null || faultPoint.isEmpty() || b) {
										// 若无故障点则判断有无异常点
										List<CollYxExpand> exceptionPoint = collYxExpandMapper
												.getYxExceptionPoint(dataMode);
										if (exceptionPoint != null && !exceptionPoint.isEmpty()) {
											for (int i = 0, size = exceptionPoint.size(); i < size; i++) {
												String point = exceptionPoint.get(i).getSignalGuid();
												String value = deviceMap.get(point);
												if (value == null || StringUtils.isBlank(value)) {
													deviceOfStatusCopy.put(deviceId, 4);
													ex = false;
													break;
												} else {
													int valueCopy = Integer.parseInt(value);
													if (valueCopy == exceptionPoint.get(i).getStatusValue()) {
														deviceOfStatusCopy.put(deviceId, 4);
														ex = false;
														break;
													}
												}
											}
										}
										if (exceptionPoint == null || exceptionPoint.isEmpty() || ex) {
											// 若无异常点，则判断有无待机点
											List<CollYxExpand> standbyPoint = collYxExpandMapper
													.getYxStandbyPoint(dataMode);
											if (standbyPoint != null && !standbyPoint.isEmpty()) {
												for (int i = 0, size = standbyPoint.size(); i < size; i++) {
													String point = standbyPoint.get(i).getSignalGuid();
													String value = deviceMap.get(point);
													if (value == null || StringUtils.isBlank(value)) {
														deviceOfStatusCopy.put(deviceId, 1);
														st = false;
														break;
													} else {
														int valueCopy = Integer.parseInt(value);
														if (valueCopy == standbyPoint.get(i).getStatusValue()) {
															deviceOfStatusCopy.put(deviceId, 1);
															st = false;
															break;
														}
													}
												}
											}
											if (standbyPoint == null || standbyPoint.isEmpty() || st) {
												// 若无待机点，则判断功率是否为0
												Map<String, Map<String, String>> guidMap = devicesGuid;
												String power = deviceMap.get(guidMap.get(deviceId).get("power"));
												if (power == null || "0".equals(power)) {
													deviceOfStatusCopy.put(deviceId, 1);
												} else {
													deviceOfStatusCopy.put(deviceId, 0);
												}
											}
										}
									}
								}
							}
						} else if ("3".equals(deviceType)) {
							// 电表
							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
							if (deviceMap == null || deviceMap.isEmpty()) {
								deviceOfStatusCopy.put(deviceId, 3);
								// cacheUtil.del(deviceId);
							} else {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
									deviceOfStatusCopy.put(deviceId, 3);
								} else {
									deviceOfStatusCopy.put(deviceId, 0);
								}
							}
						} else if ("9".equals(deviceType)) {
							// PCS
							List<CollYxExpand> faultPoint = collYxExpandMapper.getYxFaultPoint(dataMode);
							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
							if (deviceMap == null || deviceMap.isEmpty()) {
								deviceOfStatusCopy.put(deviceId, 3);
								// cacheUtil.del(deviceId);
							} else {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
									deviceOfStatusCopy.put(deviceId, 3);
								} else {
									boolean b = true;
									boolean ex = true;
									boolean st = true;
									if (faultPoint != null && !faultPoint.isEmpty()) {
										for (int i = 0; i < faultPoint.size(); i++) {
											String point = faultPoint.get(i).getSignalGuid();
											String value = deviceMap.get(point);
											if (value == null || StringUtils.isBlank(value)) {
												deviceOfStatusCopy.put(deviceId, 2);
												b = false;
												break;
											} else {
												int valueCopy = Integer.parseInt(value);
												if (valueCopy == faultPoint.get(i).getStatusValue()) {
													deviceOfStatusCopy.put(deviceId, 2);
													b = false;
													break;
												}
											}
										}
									}
									if (faultPoint == null || faultPoint.isEmpty() || b) {
										// 若无故障点则判断有无异常点
										List<CollYxExpand> exceptionPoint = collYxExpandMapper
												.getYxExceptionPoint(dataMode);
										if (exceptionPoint != null && !exceptionPoint.isEmpty()) {
											for (int i = 0, size = exceptionPoint.size(); i < size; i++) {
												String point = exceptionPoint.get(i).getSignalGuid();
												String value = deviceMap.get(point);
												if (value == null || StringUtils.isBlank(value)) {
													deviceOfStatusCopy.put(deviceId, 4);
													ex = false;
													break;
												} else {
													int valueCopy = Integer.parseInt(value);
													if (valueCopy == exceptionPoint.get(i).getStatusValue()) {
														deviceOfStatusCopy.put(deviceId, 4);
														ex = false;
														break;
													}
												}
											}
										}
										if (exceptionPoint == null || exceptionPoint.isEmpty() || ex) {
											// 若无异常点，则判断有无待机点
											List<CollYxExpand> standbyPoint = collYxExpandMapper
													.getYxStandbyPoint(dataMode);
											if (standbyPoint != null && !standbyPoint.isEmpty()) {
												for (int i = 0, size = standbyPoint.size(); i < size; i++) {
													String point = standbyPoint.get(i).getSignalGuid();
													String value = deviceMap.get(point);
													if (value == null || StringUtils.isBlank(value)) {
														deviceOfStatusCopy.put(deviceId, 1);
														st = false;
														break;
													} else {
														int valueCopy = Integer.parseInt(value);
														if (valueCopy == standbyPoint.get(i).getStatusValue()) {
															deviceOfStatusCopy.put(deviceId, 1);
															st = false;
															break;
														}
													}
												}
											}
											if (standbyPoint == null || standbyPoint.isEmpty() || st) {
												// 若无待机点，则判断功率是否为0
												Map<String, Map<String, String>> guidMap = devicesGuid;
												String power = deviceMap.get(guidMap.get(deviceId).get("power"));
												if (power == null || "0".equals(power)) {
													deviceOfStatusCopy.put(deviceId, 1);
												} else {
													deviceOfStatusCopy.put(deviceId, 0);
												}
											}
										}
									}
								}
							}
						} else if ("10".equals(deviceType)) {
							// BMS
							List<CollYxExpand> faultPoint = collYxExpandMapper.getYxFaultPoint(dataMode);
							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
							if (deviceMap == null || deviceMap.isEmpty()) {
								deviceOfStatusCopy.put(deviceId, 3);
								// cacheUtil.del(deviceId);
							} else {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
									deviceOfStatusCopy.put(deviceId, 3);
								} else {
									boolean b = true;
									boolean ex = true;
									boolean st = true;
									if (faultPoint != null && !faultPoint.isEmpty()) {
										for (int i = 0; i < faultPoint.size(); i++) {
											String point = faultPoint.get(i).getSignalGuid();
											String value = deviceMap.get(point);
											if (value == null || StringUtils.isBlank(value)) {
												deviceOfStatusCopy.put(deviceId, 2);
												b = false;
												break;
											} else {
												int valueCopy = Integer.parseInt(value);
												if (valueCopy == faultPoint.get(i).getStatusValue()) {
													deviceOfStatusCopy.put(deviceId, 2);
													b = false;
													break;
												}
											}
										}
									}
									if (faultPoint == null || faultPoint.isEmpty() || b) {
										// 若无故障点则判断有无异常点
										List<CollYxExpand> exceptionPoint = collYxExpandMapper
												.getYxExceptionPoint(dataMode);
										if (exceptionPoint != null && !exceptionPoint.isEmpty()) {
											for (int i = 0, size = exceptionPoint.size(); i < size; i++) {
												String point = exceptionPoint.get(i).getSignalGuid();
												String value = deviceMap.get(point);
												if (value == null || StringUtils.isBlank(value)) {
													deviceOfStatusCopy.put(deviceId, 4);
													ex = false;
													break;
												} else {
													int valueCopy = Integer.parseInt(value);
													if (valueCopy == exceptionPoint.get(i).getStatusValue()) {
														deviceOfStatusCopy.put(deviceId, 4);
														ex = false;
														break;
													}
												}
											}
										}
										if (exceptionPoint == null || exceptionPoint.isEmpty() || ex) {
											// 若无异常点，则判断有无待机点
											List<CollYxExpand> standbyPoint = collYxExpandMapper
													.getYxStandbyPoint(dataMode);
											if (standbyPoint != null && !standbyPoint.isEmpty()) {
												for (int i = 0, size = standbyPoint.size(); i < size; i++) {
													String point = standbyPoint.get(i).getSignalGuid();
													String value = deviceMap.get(point);
													if (value == null || StringUtils.isBlank(value)) {
														deviceOfStatusCopy.put(deviceId, 1);
														st = false;
														break;
													} else {
														int valueCopy = Integer.parseInt(value);
														if (valueCopy == standbyPoint.get(i).getStatusValue()) {
															deviceOfStatusCopy.put(deviceId, 1);
															st = false;
															break;
														}
													}
												}
											}
											if (standbyPoint == null || standbyPoint.isEmpty() || st) {
												// 若无待机点，则判断功率是否为0
												Map<String, Map<String, String>> guidMap = devicesGuid;
												String power = deviceMap.get(guidMap.get(deviceId).get("power"));
												if (power == null || "0".equals(power)) {
													deviceOfStatusCopy.put(deviceId, 1);
												} else {
													deviceOfStatusCopy.put(deviceId, 0);
												}
											}
										}
									}
								}
							}
						} else if ("11".equals(deviceType)) {
							if (deviceId.equals("213") || deviceId.equals("230")) {
								// 汇流箱
								List<CollYxExpand> faultPoint = collYxExpandMapper.getYxFaultPoint(dataMode);
								Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
								if (deviceMap == null || deviceMap.isEmpty()) {
									deviceOfStatusCopy.put(deviceId, 3);
									// cacheUtil.del(deviceId);
								} else {
									String deviceConnStatus = deviceMap.get("conn_status");
									if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
										deviceOfStatusCopy.put(deviceId, 3);
									} else {
										boolean b = true;
										boolean ex = true;
										if (faultPoint != null && !faultPoint.isEmpty()) {
											for (int i = 0; i < faultPoint.size(); i++) {
												String point = faultPoint.get(i).getSignalGuid();
												String value = deviceMap.get(point);
												if (value == null || StringUtils.isBlank(value)) {
													deviceOfStatusCopy.put(deviceId, 2);
													b = false;
													break;
												} else {
													int valueCopy = Integer.parseInt(value);
													if (valueCopy == faultPoint.get(i).getStatusValue()) {
														deviceOfStatusCopy.put(deviceId, 2);
														b = false;
														break;
													}
												}
											}
										}
										if (faultPoint == null || faultPoint.isEmpty() || b) {
											// 若无故障点则判断有无异常点
											List<CollYxExpand> exceptionPoint = collYxExpandMapper
													.getYxExceptionPoint(dataMode);
											if (exceptionPoint != null && !exceptionPoint.isEmpty()) {
												for (int i = 0, size = exceptionPoint.size(); i < size; i++) {
													String point = exceptionPoint.get(i).getSignalGuid();
													String value = deviceMap.get(point);
													if (value == null || StringUtils.isBlank(value)) {
														deviceOfStatusCopy.put(deviceId, 4);
														ex = false;
														break;
													} else {
														int valueCopy = Integer.parseInt(value);
														if (valueCopy == exceptionPoint.get(i).getStatusValue()) {
															deviceOfStatusCopy.put(deviceId, 4);
															ex = false;
															break;
														}
													}
												}
											} else {
												deviceOfStatusCopy.put(deviceId, 0);
											}
										}
									}
								}
							} else {
								deviceOfStatusCopy.put(deviceId, 0);
							}
						} else if ("12".equals(deviceType)) {
							// 箱变
							List<CollYxExpand> faultPoint = collYxExpandMapper.getYxFaultPoint(dataMode);
							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
							if (deviceMap == null || deviceMap.isEmpty()) {
								deviceOfStatusCopy.put(deviceId, 3);
								// cacheUtil.del(deviceId);
							} else {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (!Util.isNotBlank(deviceConnStatus) || deviceConnStatus.equals("0")) {
									deviceOfStatusCopy.put(deviceId, 3);
								} else {
									boolean b = true;
									boolean ex = true;
									if (faultPoint != null && !faultPoint.isEmpty()) {
										for (int i = 0; i < faultPoint.size(); i++) {
											String point = faultPoint.get(i).getSignalGuid();
											String value = deviceMap.get(point);
											if (value == null || StringUtils.isBlank(value)) {
												deviceOfStatusCopy.put(deviceId, 2);
												b = false;
												break;
											} else {
												int valueCopy = Integer.parseInt(value);
												if (valueCopy == faultPoint.get(i).getStatusValue()) {
													deviceOfStatusCopy.put(deviceId, 2);
													b = false;
													break;
												}
											}
										}
									}
									if (faultPoint == null || faultPoint.isEmpty() || b) {
										// 若无故障点则判断有无异常点
										List<CollYxExpand> exceptionPoint = collYxExpandMapper
												.getYxExceptionPoint(dataMode);
										if (exceptionPoint != null && !exceptionPoint.isEmpty()) {
											for (int i = 0, size = exceptionPoint.size(); i < size; i++) {
												String point = exceptionPoint.get(i).getSignalGuid();
												String value = deviceMap.get(point);
												if (value == null || StringUtils.isBlank(value)) {
													deviceOfStatusCopy.put(deviceId, 4);
													ex = false;
													break;
												} else {
													int valueCopy = Integer.parseInt(value);
													if (valueCopy == exceptionPoint.get(i).getStatusValue()) {
														deviceOfStatusCopy.put(deviceId, 4);
														ex = false;
														break;
													}
												}
											}
										} else {
											deviceOfStatusCopy.put(deviceId, 0);
										}
									}
								}
							}
							// 环境监测器 ybj
						} else if (String.valueOf(DTC.ENV_MONITOR).equals(deviceType)) {
							Map<String, String> deviceMap = cacheUtil.getMap(deviceId);
							// deviceConnStatus 1表示正常 0表示断连
							// deviceOfStatusCopy 0标示正常 3表示通讯断连
							if (Util.isNotBlank(deviceMap)) {
								String deviceConnStatus = deviceMap.get("conn_status");
								if (Util.isNotBlank(deviceConnStatus)) {
									if ("0".equals(deviceConnStatus)) {
										deviceOfStatusCopy.put(deviceId, 3);
									} else if ("1".equals(deviceConnStatus)) {
										deviceOfStatusCopy.put(deviceId, 0);
									}
								}
							} else {
								deviceOfStatusCopy.put(deviceId, 3);
							}
						}
					}
				}
				/**
				 * 点集为空
				 */
			} else {
				// 数采状态判断
				List<String> devices = entry.getValue();
				// 如果数采断连则其所有采集的设备都处理为断连
				deviceOfStatusCopy.put(key, 3);
//				 cacheUtil.del(key);等会把注释消掉
				for (String deviceId : devices) {
					deviceId = deviceId.split("_")[0];
					deviceOfStatusCopy.put(deviceId, 3);
					// cacheUtil.del(deviceId);
				}

			}
		}
		deviceOfStatus = deviceOfStatusCopy;
	}

	/**
	 * 得到各电站历史收益historyDataOfPlant<电站id,<{power,price},value>>
	 */
	public void updateHistoryDataPlant() {

		// <电站id,<{power,price},value>>
		Map<String, Map<String, Double>> historyDataOfPlantCopy = null;

		if (Util.isNotBlank(plantTypeMap)) {

			historyDataOfPlantCopy = new ConcurrentHashMap<>(plantTypeMap.size());

			Set<Entry<String, List<String>>> entrySet = plantTypeMap.entrySet();
			for (Entry<String, List<String>> entry : entrySet) {
				switch (Integer.parseInt(entry.getKey())) {
				// 光伏
				case PTAC.PHOTOVOLTAIC:
					for (String plantId : entry.getValue()) {

						Map<String, Double> historyMap = inventerStorageDataMapper.getHistoryIncome(plantId);

						Map<String, Double> historyTemp = new HashMap<>(2);

						historyTemp.put("power",
								historyMap == null ? 0.0 : historyMap.isEmpty() ? 0.0 : historyMap.get("power"));

						historyTemp.put("price",
								historyMap == null ? 0.0 : historyMap.isEmpty() ? 0.0 : historyMap.get("price"));

						historyDataOfPlantCopy.put(plantId, historyTemp);
					}
					break;
				default:
					break;
				}
			}

			historyDataOfPlant = historyDataOfPlantCopy;

		}

	}

	/**
	 * 得到这是电站类型和电站id列表的映射plantTypeMap <br>
	 * 得到电站id和电站类型id的映射plantTypeList
	 */
	public void updatePlantType() {

		/**
		 * 根据device_type_a: 1.光伏电站 2.储能电站 3.光储电站 类型得到每种类型的电站id eg: 1类型电站有1,2,3,4号电站
		 * 用逗号隔开 这是电站类型和ids数组的映射
		 */
		/**
		 * SELECT pi.plant_type_a type,GROUP_CONCAT(pi.id) ids from plant_info pi where
		 * pi.plant_valid='0' GROUP BY pi.plant_type_a
		 */
		List<Map<Integer, String>> plantList = plantInfoMapper.getPlantIdsByType();

		/**
		 * 把上面的List<Map<Integer, String>> eg:<<1,"1,2,3,4">,<1,"1,2,3,4">>变为 Map<String,
		 * List<String>> eg:<<"1",<"1","2","3","4">>,<"1",<"1","2","3","4">>>,
		 * 这是电站类型和电站id列表的映射
		 */
		Map<String, List<String>> plantTypeMapCopy = new ConcurrentHashMap<>();

		/**
		 * 把上面的List<Map<Integer, String>> eg:<<1,"1,2,3,4">> 转换为 Map<String, String> eg:
		 * <<1,"1">,<2,"1">,<3,"1">,<4,"1">> 这是电站id和电站类型的映射
		 */
		Map<String, String> plantTypeListCopy = new ConcurrentHashMap<>();

		if (Util.isNotBlank(plantList)) {
			int size = plantList.size();
			// 遍历每种类型
			for (int i = 0; i < size; i++) {
				Map<Integer, String> tempMap = plantList.get(i);
				// 得到该类型key 1.光伏电站 2.储能电站 3.光储电站 4.能效电站
				String key = String.valueOf(tempMap.get("type"));
				// 得到该类型的ids字符串 eg: 1,2,3,4
				String value = tempMap.get("ids");

				if (Util.isNotBlank(value)) {
					// 得到该类型发电站id数组
					String[] values = value.split(",");

					if (Util.isNotBlank(values)) {
						// 把id数组转换成id列表
						List<String> list = Arrays.asList(values);

						plantTypeMapCopy.put(key, list);

						for (int j = 0; j < values.length; j++) {
							plantTypeListCopy.put(values[j], key);
						}

					}

				}
			}
		}

		plantTypeMap = plantTypeMapCopy;

		plantTypeList = plantTypeListCopy;

	}

	/**
	 * 该电站收益<key{energy,power,price},收益>
	 */
	public Map<String, Double> getPVPlantIncome(int planId) {

		/**
		 * 该电站的<设备类型id,设备id列表>
		 */
		Map<Integer, List<Integer>> devices = deviceOfPlant.get(planId);

		double energy = 0, energyPrice = 0, power = 0;
		/**
		 * <key{energy,power,price},value>
		 */
		Map<String, Double> temp = new HashMap<>(2);

		if (Util.isNotBlank(devices)) {
			List<SysPowerPriceDetail> prices = plantInfoMapper.getPlantById(String.valueOf(planId))
					.getPowerPriceDetails();
			double price = Double.parseDouble(configParam.getPowerPrice());
			if (Util.isNotBlank(prices)) {
				price = Double.parseDouble(prices.get(0).getPosPrice() + "");
			}
			// 集中式逆变器
			List<Integer> typeOne = devices.get(1);
			// 组串式逆变器
			List<Integer> typeTwo = devices.get(2);
			// 电表
			List<Integer> typeThree = devices.get(3);

			long currStartTime = TimeUtil.getCurrDayStartTime(0);
			long currEndTime = TimeUtil.getCurrDayEndTime(0);

			// 电表实时功率和放电量装载
			if (typeThree != null && !typeThree.isEmpty()) {
				int threeSize = typeThree.size();
				for (int i = 0; i < threeSize; i++) {
					String deviceId = typeThree.get(i) + "";
					Map<String, String> tempMap = cacheUtil.getMap(deviceId);
					if (Util.isNotBlank(tempMap)) {
						Map<String, String> deviceGuid = devicesGuid.get(deviceId);
						// 今日正向电度数
						double todayEnergy = 0;
						if (Util.isNotBlank(deviceGuid)) {
							String tempTodayPosEnergy = tempMap.get(deviceGuid.get("posEnergy"));
							double todayPosEnergy = Double
									.parseDouble(Util.isNotBlank(tempTodayPosEnergy) ? tempTodayPosEnergy : "0");
							// 今日反向电度数
							String tempTodayNegEnergy = tempMap.get(deviceGuid.get("negEnergy"));
							// double
							// todayNegEnergy=Double.parseDouble(Util.isNotBlank(tempTodayNegEnergy)?tempTodayNegEnergy:"0");
							// 今日结余电度数
							todayEnergy = todayPosEnergy;
						}
						int beforSize = Util.isNotBlank(electricBeforDatas) ? electricBeforDatas.size() : 0;
						for (int j = 0; j < beforSize; j++) {
							DataElectricMeter tempData = electricBeforDatas.get(i);
							int dId = Integer.parseInt(deviceId);
							if (tempData.getDeviceId() == dId) {
								// 历史最大正向电度数
								double hisPosEnergy = Double.parseDouble(
										tempData.getEmPosActiveEnergy() == null ? tempData.getEmPosActiveEnergy() + ""
												: "0");
								// 今日反向电度数
								// double
								// hisNegEnergy=Double.parseDouble(tempData.getEmNegActiveEnergy()==null?tempData.getEmNegActiveEnergy()+"":"0");
								// todayEnergy=Util.subFloat(Util.addFloat(todayEnergy, hisNegEnergy,
								// 0),hisPosEnergy, 0);
								todayEnergy = Util.subFloat(todayEnergy, hisPosEnergy, 0);
								break;
							}

						}
						energy = Util.addFloat(todayEnergy, energy, 0);
					}
				}
				if (typeOne != null && !typeOne.isEmpty()) {
					int oneSize = typeOne.size();
					for (int i = 0; i < oneSize; i++) {
						String deviceId = typeOne.get(i) + "";
						Map<String, String> tempMap = cacheUtil.getMap(deviceId);
						Map<String, String> guid = devicesGuid.get(deviceId);
						if (tempMap != null && !tempMap.isEmpty()) {
							power = Util.addFloat(power,
									tempMap.containsKey(guid.get("power"))
											? Double.parseDouble(tempMap.get(guid.get("power")))
											: 0.0,
									0);
						}
					}
				}
				if (typeTwo != null && !typeTwo.isEmpty()) {
					int twoSize = typeTwo.size();
					for (int i = 0; i < twoSize; i++) {
						String deviceId = typeTwo.get(i) + "";
						Map<String, String> tempMap = cacheUtil.getMap(deviceId);
						Map<String, String> guid = devicesGuid.get(deviceId);
						if (tempMap != null && !tempMap.isEmpty()) {
							power = Util.addFloat(power,
									tempMap.containsKey(guid.get("power"))
											? Double.parseDouble(tempMap.get(guid.get("power")))
											: 0.0,
									0);
						}
					}
				}
				if (typeOne == null && typeTwo == null) {
					power = 0;
				}
				temp.put("energy", Util.roundDouble(energy));
				temp.put("price", Util.roundDouble(energyPrice));
				temp.put("power", Util.multFloat(energy, price, 1));
			} else {
				// 逆变器实时功率和放电量装载
				if (typeOne != null && !typeOne.isEmpty()) {
					int oneSize = typeOne.size();
					for (int i = 0; i < oneSize; i++) {
						String deviceId = typeOne.get(i) + "";
						Map<String, String> tempMap = cacheUtil.getMap(deviceId);
						Map<String, String> guid = devicesGuid.get(deviceId);
						if (tempMap != null && !tempMap.isEmpty()) {
							String timeStirng = tempMap.get("data_time");
							long time = Long.parseLong(timeStirng == null ? "-1" : timeStirng);

							if (time != -1 && time >= currStartTime && time <= currEndTime) {
								energy = Util.addFloat(energy,
										tempMap.containsKey(guid.get("energy"))
												? Double.parseDouble(tempMap.get(guid.get("energy")))
												: 0.0,
										0);

								power = Util.addFloat(power,
										tempMap.containsKey(guid.get("power"))
												? Double.parseDouble(tempMap.get(guid.get("power")))
												: 0.0,
										0);
							}

						}
					}
				}
				if (typeTwo != null && !typeTwo.isEmpty()) {
					int twoSize = typeTwo.size();
					for (int i = 0; i < twoSize; i++) {
						String deviceId = typeTwo.get(i) + "";
						Map<String, String> tempMap = cacheUtil.getMap(deviceId);
						Map<String, String> guid = devicesGuid.get(deviceId);
						if (tempMap != null && !tempMap.isEmpty()) {
							String timeStirng = tempMap.get("data_time");
							long time = Long.parseLong(timeStirng == null ? "-1" : timeStirng);

							if (time != -1 && time >= currStartTime && time <= currEndTime) {
								energy = Util.addFloat(energy,
										tempMap.containsKey(guid.get("energy"))
												? Double.parseDouble(tempMap.get(guid.get("energy")))
												: 0.0,
										0);
								power = Util.addFloat(power,
										tempMap.containsKey(guid.get("power"))
												? Double.parseDouble(tempMap.get(guid.get("power")))
												: 0.0,
										0);
							}
						}
					}
				}
				if (typeOne == null && typeTwo == null) {
					energy = 0;
					power = 0;
				}
			}
			temp.put("energy", energy);
			temp.put("power", power);
			temp.put("price", Util.multFloat(energy, price, 1));
		}
		return temp;
	}

	public Map<String, Double> getEnergyPlantIncome(int plantId) {
		Map<String, Double> temp = new HashMap<>(28);
		double jianPosEnergy = 0, jianNegEnergy = 0, pingPosEnergy = 0, pingNegEnergy = 0, fengPosEnergy = 0,
				fengNegEnergy = 0, guPosEnergy = 0, guNegEnergy = 0;
		double jianPosPrice = 0, jianNegPrice = 0, pingPosPrice = 0, pingNegPrice = 0, fengPosPrice = 0,
				fengNegPrice = 0, guPosPrice = 0, guNegPrice = 0;
		double dailyPosEnergy = 0, dailyPosPrice = 0, dailyNegEnergy = 0, dailyNegPrice = 0, dailyEnergy = 0,
				dailyPrice = 0;
		double totalPosEnergy = 0, totalNegEnergy = 0, totalPosPrice = 0, totalNegPrice = 0;
		;
		double totalEnergy = 0, totalPrice = 0;
		List<SysPowerPriceDetail> prices = plantInfoMapper.getPlantById(String.valueOf(plantId)).getPowerPriceDetails();
		if (Util.isNotBlank(prices)) {
			Map<Integer, List<Integer>> devices = deviceOfPlant.get(plantId);
			if (Util.isNotBlank(devices)) {
				List<Integer> typeThree = devices.containsKey(3) ? devices.get(3) : null;
				List<Integer> typeNine = devices.containsKey(9) ? devices.get(9) : null;
				Map<String, Object> paramMap = new HashMap<>();
				if (Util.isNotBlank(typeThree) || Util.isNotBlank(typeNine)) {
					int eletricLen = 0;
					//
					paramMap.put("startTime", Util.getBeforDayMin(1));
					paramMap.put("endTime", Util.getBeforDayMax(1));
					List<DataElectricMeter> beforEletricData = null;
					if (Util.isNotBlank(typeThree)) {
						eletricLen = typeThree.size();
						paramMap.put("devices", typeThree);
						beforEletricData = SystemCache.electricBeforDatas;
						// dataElectricMeterMapper.getLastHistoryDataElectric(paramMap)
					} else if (Util.isNotBlank(typeNine)) {
						eletricLen = typeNine.size();
						paramMap.put("devices", typeNine);
						beforEletricData = pcsBeforDatas;
						// dataPcsMapper.getLastHistoryDataPcs(paramMap);
					}
					paramMap.remove("devices");
					paramMap.put("minTime", Util.getBeforDayMin(0));
					paramMap.put("maxTime", Util.getBeforDayMax(0));
					for (int i = 0; i < eletricLen; i++) {
						int deviceId = -1;
						int deviceStatus = 0;
						if (Util.isNotBlank(typeThree)) {
							deviceId = typeThree.get(i);
							deviceStatus = deviceOfStatus.get(String.valueOf(deviceId));
						} else {
							deviceId = typeNine.get(i);
							deviceStatus = deviceOfStatus.get(String.valueOf(deviceId));
						}
						paramMap.put("deviceId", deviceId);
						List<DataElectricMeter> nowList = null;
						if (Util.isNotBlank(typeThree)) {
							nowList = dataElectricMeterMapper.getEnergyPlantDataForStorage(paramMap);
						} else if (Util.isNotBlank(typeNine)) {
							nowList = dataPcsMapper.getEnergyPlantDataForStorage(paramMap);
							// 如果是在线设备，把redis最新的一条记录追加上
							boolean onlineStatus = false;
							switch (deviceStatus) {
							case 0:
								onlineStatus = true;
								break;
							case 1:
								onlineStatus = true;
								break;
							default:
								break;
							}
							if (onlineStatus) {
								String dID = String.valueOf(deviceId);
								Map<String, String> tempMap = cacheUtil.getMap(dID);
								Map<String, String> guid = devicesGuid.get(dID);
								if (Util.isNotBlank(tempMap) && Util.isNotBlank(guid)) {
									String posGuid = guid.get("posEnergy");
									String negGuid = guid.get("negEnergy");
									double posEnergy = tempMap.containsKey(posGuid)
											? Double.parseDouble(tempMap.get(posGuid))
											: 0.0;
									double negEnergy = tempMap.containsKey(negGuid)
											? Double.parseDouble(tempMap.get(negGuid))
											: 0.0;
									long dataTime = Long.parseLong(tempMap.get("data_time"));
									// 只要有一个不为0则判定为有效数据
									if (posEnergy != 0.0 || negEnergy != 0.0) {
										DataElectricMeter newData = new DataElectricMeter();
										newData.setEmPosActiveEnergy(Float.parseFloat(String.valueOf(posEnergy)));
										newData.setEmNegActiveEnergy(Float.parseFloat(String.valueOf(negEnergy)));
										newData.setDeviceId(deviceId);
										newData.setDataTime(dataTime);
										if (Util.isNotBlank(nowList)) {
											nowList.add(newData);
										} else {
											nowList = new ArrayList<>(1);
											nowList.add(newData);
										}
									}

								}
							}
						}
						if (Util.isNotBlank(nowList)) {
							int nowDataLen = nowList.size();
							// 得到2-nowDataLen的增量数据
							for (int j = nowDataLen - 1; j > 0; j--) {
								DataElectricMeter beforData = nowList.get(j - 1);
								DataElectricMeter nowData = nowList.get(j);
								nowData.setEmPosActiveEnergy(
										Float.valueOf(Util.subFloat(nowData.getEmPosActiveEnergy() + "",
												beforData.getEmPosActiveEnergy() + "", 0) + ""));
								nowData.setEmNegActiveEnergy(
										Float.valueOf(Util.subFloat(nowData.getEmNegActiveEnergy() + "",
												beforData.getEmNegActiveEnergy() + "", 0) + ""));
							}
							// 处理第一条数据
							if (Util.isNotBlank(beforEletricData)) {
								int beforLen = beforEletricData.size();
								for (int j = 0; j < beforLen; j++) {
									if (beforEletricData.get(j).getDeviceId() == deviceId) {
										DataElectricMeter nowData = nowList.get(0);
										nowData.setEmPosActiveEnergy(
												Float.valueOf(Util.subFloat(nowData.getEmPosActiveEnergy() + "",
														beforEletricData.get(j).getEmPosActiveEnergy() + "", 0) + ""));
										nowData.setEmNegActiveEnergy(
												Float.valueOf(Util.subFloat(nowData.getEmNegActiveEnergy() + "",
														beforEletricData.get(j).getEmNegActiveEnergy() + "", 0) + ""));
									}
								}
							}
							int priceLen = prices.size();
							for (int j = 0; j < nowDataLen; j++) {
								DataElectricMeter dataElectricMeter = nowList.get(j);
								long nowTime = ServiceUtil
										.getTimeByStr(TimeUtil.formatTime(dataElectricMeter.getDataTime(), "HH:mm:ss"));
								for (int k = 0; k < priceLen; k++) {
									SysPowerPriceDetail priceDetail = prices.get(k);
									long startTime = ServiceUtil.getTimeByStr(priceDetail.getStartTime());
									long endTime = ServiceUtil.getTimeByStr(priceDetail.getEndTime());
									int priceType = priceDetail.getPriceType();
									double tempPos = dataElectricMeter.getEmPosActiveEnergy(),
											tempNeg = dataElectricMeter.getEmNegActiveEnergy();
									double pricePos = Double.parseDouble(String.valueOf(priceDetail.getPosPrice()));
									double priceNeg = Double.parseDouble(String.valueOf(priceDetail.getNegPrice()));
									// 当时间段跨天需要拆分成两个时间段
									if (endTime < startTime) {
										long startTime2 = ServiceUtil.getTimeByStr("00:00:00");
										long endTime2 = ServiceUtil.getTimeByStr("23:59:59");
										if ((nowTime > startTime && nowTime < endTime2)
												|| (nowTime > startTime2 && nowTime < endTime)) {
											switch (priceType) {
											case 1:
												jianPosEnergy = Util.addFloats(0, jianPosEnergy, tempPos);
												jianNegEnergy = Util.addFloats(0, jianNegEnergy, tempNeg);
												jianPosPrice = Util.addFloat(jianPosPrice,
														Util.multFloat(tempPos, pricePos, 0), 0);
												jianNegPrice = Util.addFloat(jianNegPrice,
														Util.multFloat(tempNeg, priceNeg, 0), 0);
												break;
											case 2:
												fengPosEnergy = Util.addFloats(0, fengPosEnergy, tempPos);
												fengNegEnergy = Util.addFloats(0, fengNegEnergy, tempNeg);
												fengPosPrice = Util.addFloat(fengPosPrice,
														Util.multFloat(tempPos, pricePos, 0), 0);
												fengNegPrice = Util.addFloat(fengNegPrice,
														Util.multFloat(tempNeg, priceNeg, 0), 0);
												break;
											case 3:
												pingPosEnergy = Util.addFloats(0, pingPosEnergy, tempPos);
												pingNegEnergy = Util.addFloats(0, pingNegEnergy, tempNeg);
												pingPosPrice = Util.addFloat(pingPosPrice,
														Util.multFloat(tempPos, pricePos, 0), 0);
												pingNegPrice = Util.addFloat(pingNegPrice,
														Util.multFloat(tempNeg, priceNeg, 0), 0);
												break;
											case 4:
												guPosEnergy = Util.addFloats(0, guPosEnergy, tempPos);
												guNegEnergy = Util.addFloats(0, guNegEnergy, tempNeg);
												guPosPrice = Util.addFloat(guPosPrice,
														Util.multFloat(tempPos, pricePos, 0), 0);
												guNegPrice = Util.addFloat(guNegPrice,
														Util.multFloat(tempNeg, priceNeg, 0), 0);
												break;

											default:
												break;
											}
											break;
										}
									} else if (nowTime > startTime && nowTime < endTime) {
										switch (priceType) {
										case 1:
											jianPosEnergy = Util.addFloats(0, jianPosEnergy, tempPos);
											jianNegEnergy = Util.addFloats(0, jianNegEnergy, tempNeg);
											jianPosPrice = Util.addFloat(jianPosPrice,
													Util.multFloat(tempPos, pricePos, 0), 0);
											jianNegPrice = Util.addFloat(jianNegPrice,
													Util.multFloat(tempNeg, priceNeg, 0), 0);
											break;
										case 2:
											fengPosEnergy = Util.addFloats(0, fengPosEnergy, tempPos);
											fengNegEnergy = Util.addFloats(0, fengNegEnergy, tempNeg);
											fengPosPrice = Util.addFloat(fengPosPrice,
													Util.multFloat(tempPos, pricePos, 0), 0);
											fengNegPrice = Util.addFloat(fengNegPrice,
													Util.multFloat(tempNeg, priceNeg, 0), 0);
											break;
										case 3:
											pingPosEnergy = Util.addFloats(0, pingPosEnergy, tempPos);
											pingNegEnergy = Util.addFloats(0, pingNegEnergy, tempNeg);
											pingPosPrice = Util.addFloat(pingPosPrice,
													Util.multFloat(tempPos, pricePos, 0), 0);
											pingNegPrice = Util.addFloat(pingNegPrice,
													Util.multFloat(tempNeg, priceNeg, 0), 0);
											break;
										case 4:
											guPosEnergy = Util.addFloats(0, guPosEnergy, tempPos);
											guNegEnergy = Util.addFloats(0, guNegEnergy, tempNeg);
											guPosPrice = Util.addFloat(guPosPrice, Util.multFloat(tempPos, pricePos, 0),
													0);
											guNegPrice = Util.addFloat(guNegPrice, Util.multFloat(tempNeg, priceNeg, 0),
													0);
											break;

										default:
											break;
										}
										break;
									}
								}
							}
						}
					}
					dailyPosEnergy = Util.addFloats(1, jianPosEnergy, fengPosEnergy, pingPosEnergy, guPosEnergy);
					dailyPosPrice = Util.addFloats(1, jianPosPrice, fengPosPrice, pingPosPrice, guPosPrice);
					dailyNegEnergy = Util.addFloats(1, jianNegEnergy, fengNegEnergy, pingNegEnergy, guNegEnergy);
					dailyNegPrice = Util.addFloats(1, jianNegPrice, fengNegPrice, pingNegPrice, guNegPrice);
					dailyEnergy = Util.subFloat(dailyPosEnergy, dailyNegEnergy, 1);
					dailyPrice = Util.subFloat(dailyPosPrice, dailyNegPrice, 1);
					EnergyStorageData lastRecord = null;
					Map<String, Object> lastRecordMap = lastRecordMap = new HashMap<>();
					lastRecordMap.put("dataTime", Util.getBeforDayMax(1));
					lastRecordMap.put("plantId", plantId);
					lastRecord = energyStorageDataMapper.getPlantLastRecord(lastRecordMap);
					if (lastRecord != null) {
						totalPosEnergy = Util.addFloat(lastRecord.getTotalPosEnergy(), dailyPosEnergy, 1);
						totalPosPrice = Util.addFloat(lastRecord.getTotalPosPrice(), dailyPosPrice, 1);
						totalNegEnergy = Util.addFloat(lastRecord.getTotalNegEnergy(), dailyNegEnergy, 1);
						totalNegPrice = Util.addFloat(lastRecord.getTotalNegPrice(), dailyNegPrice, 1);
						totalEnergy = Util.subFloat(totalPosEnergy, totalNegEnergy, 1);
						totalPrice = Util.addFloat(lastRecord.getTotalPrice(), dailyPrice, 1);
					} else {
						// 第一次初始化数据
						totalPosEnergy = dailyPosEnergy;
						totalPosPrice = dailyPosPrice;
						totalNegEnergy = dailyNegEnergy;
						totalNegPrice = dailyNegPrice;
						totalEnergy = dailyEnergy;
						totalPrice = dailyPrice;
					}
				}
			}
		}
		EnergyStorageData energyStorageData = new EnergyStorageData(jianPosEnergy, jianPosPrice, jianNegEnergy,
				jianNegPrice, fengPosEnergy, fengPosPrice, fengNegEnergy, fengNegPrice, pingPosEnergy, pingPosPrice,
				pingNegEnergy, pingNegPrice, guPosEnergy, guPosPrice, guNegEnergy, guNegPrice, dailyPosEnergy,
				dailyPosPrice, dailyNegEnergy, dailyNegPrice, dailyEnergy, dailyPrice, totalPosEnergy, totalPosPrice,
				totalNegEnergy, totalNegPrice, totalEnergy, totalPrice, System.currentTimeMillis(), plantId);
		temp = ServiceUtil.ConvertObjToMap(energyStorageData);
		return temp;
	}

	/**
	 * 得到该电站的SOC
	 * plantSOC<电站id,value>，电量plantCurrentEnergy<电站id,value>,电池输入功率plantCurrentEnergyPower<电站id,value>，电网功率plantCurrentEnergyPowerBat<电站id,value>
	 */
	public void updatePlantSOC() {

		// 电站SOC 剩余电量<电站id,value>
		Map<String, String> plantSOCCopy = new ConcurrentHashMap<>();
		// 储能电站实时电量<电站id,value>
		Map<String, String> plantCurrentEnergyCopy = new ConcurrentHashMap<>();
		// 储能电站实时电池输入功率<电站id,value>
		Map<String, Double> plantCurrentEnergyPowerCopy = new ConcurrentHashMap<>();
		// 储能电站实时电网功率<电站id,value>
		Map<String, Double> plantCurrentEnergyPowerBatCopy = new ConcurrentHashMap<>();

		if (Util.isNotBlank(plantTypeMap) && Util.isNotBlank(deviceOfPlant)) {
			Iterator<Entry<String, List<String>>> entries = plantTypeMap.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String, List<String>> entry = entries.next();
				String plantType = entry.getKey();
				List<String> ids = entry.getValue();
				if (Util.isNotBlank(ids)) {

					if ("2".equals(plantType) || "3".equals(plantType)) {

						for (int i = 0, idSize = ids.size(); i < idSize; i++) {
							String pId = ids.get(i);
							int plantId = Integer.parseInt(pId);
							// 本电站 <设备类型id,该设备类型id列表>
							Map<Integer, List<Integer>> devices = deviceOfPlant.get(plantId);
							if (Util.isNotBlank(devices)) {
								// 计算SOC
								List<Integer> bmsList = devices.get(10);
								if (Util.isNotBlank(bmsList)) {
									double energy = 0, capacity = 0;
									for (int j = 0, len = bmsList.size(); j < len; j++) {
										String deviceId = String.valueOf(bmsList.get(j));
										Map<String, String> deviceGuid = devicesGuid.get(deviceId);
										/**
										 * reid中得到该设备实时点表信息
										 */
										Map<String, String> tempMap = cacheUtil.getMap(deviceId);
										if (Util.isNotBlank(deviceGuid) && Util.isNotBlank(tempMap)) {
											String tempEnergy = tempMap.get(deviceGuid.get("energy"));
											energy = Util.addFloat(energy,
													Double.parseDouble(Util.isNotBlank(tempEnergy) ? tempEnergy : "0"),
													0);
											String tempCapacity = tempMap.get(deviceGuid.get("capacity"));
											capacity = Util.addFloat(capacity, Double.parseDouble(
													Util.isNotBlank(tempCapacity) ? tempCapacity : "0"), 0);
										}
									}
									if (energy == 0 && capacity == 0) {
										plantSOCCopy.put(pId, "0%");
										plantCurrentEnergyCopy.put(pId, "0");
									} else {
										plantSOCCopy.put(pId, Util.getDecimalFomat2()
												.format(Util.divideDouble(energy, capacity, 0) * 100) + "%");
										plantCurrentEnergyCopy.put(pId, Util.getDecimalFomat2().format(energy));
									}
								} else {
									plantSOCCopy.put(pId, "0%");
									plantCurrentEnergyCopy.put(pId, "0");
								}
								List<Integer> pcsList = devices.get(9);
								double acticePower = 0, powerBat = 0;
								if (Util.isNotBlank(pcsList)) {
									for (int j = 0, len = pcsList.size(); j < len; j++) {
										String deviceId = String.valueOf(pcsList.get(j));
										Map<String, String> deviceGuid = devicesGuid.get(deviceId);
										Map<String, String> tempMap = cacheUtil.getMap(deviceId);
										if (Util.isNotBlank(deviceGuid) && Util.isNotBlank(tempMap)) {
											String tempEnergy = tempMap.get(deviceGuid.get("energy"));
											acticePower = Util.addFloat(acticePower,
													Double.parseDouble(Util.isNotBlank(tempEnergy) ? tempEnergy : "0"),
													0);
											String tempCapacity = tempMap.get(deviceGuid.get("capacity"));
											powerBat = Util.addFloat(powerBat, Double.parseDouble(
													Util.isNotBlank(tempCapacity) ? tempCapacity : "0"), 0);
										}
									}
								}
								plantCurrentEnergyPowerBatCopy.put(pId, powerBat);
								plantCurrentEnergyPowerCopy.put(pId, acticePower);
							} else {
								// 赋默认值
								plantSOCCopy.put(pId, "0%");
								plantCurrentEnergyCopy.put(pId, "0");
								plantCurrentEnergyPowerBatCopy.put(pId, 0.0);
								plantCurrentEnergyPowerCopy.put(pId, 0.0);
							}
						}
					}
				}
			}
		}

		plantSOC = plantSOCCopy;

		plantCurrentEnergy = plantCurrentEnergyCopy;

		plantCurrentEnergyPower = plantCurrentEnergyPowerCopy;

		plantCurrentEnergyPowerBat = plantCurrentEnergyPowerBatCopy;

	}

	/**
	 * @return void
	 * @Author lz
	 * @Description PlantEclecCap缓存
	 * @Date 11:36 2018/8/28
	 * @Param []
	 **/
	public void updatePlantEclecCap() {
		// 用于循环取值的key
		String power = "month_";// 申报值
		String price = "month_price_";// 申报单价
		ConcurrentHashMap<String, Map<String, Map<String, Map<Integer, Double>>>> plantElecCapCopy = new ConcurrentHashMap();
		HashMap yearElec = new HashMap<String, Map<String, Map<Integer, Double>>>();
		List<Map<String, Object>> elecList = elecCapDecMapper.selectAll();
		elecList.forEach((Map<String, Object> elec) -> {
			ConcurrentHashMap monElec = new ConcurrentHashMap<String, Map<Integer, Double>>();
			for (int i = 1; i <= 12; i++) {
				ConcurrentHashMap elecAndPrice = new ConcurrentHashMap<Integer, Double>();
				Double rPower = Util.getDouble(elec.get(power + i));
				Double rPrice = Util.getDouble(elec.get(price + i));
				elecAndPrice.put(0, rPower);
				elecAndPrice.put(1, rPrice);
				elecAndPrice.put(2, rPower * rPrice);
				monElec.put(i + "", elecAndPrice);
			}
			yearElec.put(elec.get("year"), monElec);
			String plantId = elec.get("plant_id").toString();
			Map<String, Map<String, Map<Integer, Double>>> pYearElec = plantElecCapCopy.get(plantId);
			if (CollectionUtils.isEmpty(pYearElec)) {
				plantElecCapCopy.put(elec.get("plant_id").toString(), yearElec);
			} else {
				pYearElec.putAll(yearElec);
			}
		});
		plantEclecCap = plantElecCapCopy;
	}

	/**
	 * @param      startTime,endTime 如果时间某一个为-1 默认查询当天时间
	 * @param type 如果为1结果集中加入当天各时段价格 其它值无用
	 * @return
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 * @author ybj
	 * @date 2018年8月23日下午7:25:09
	 * @Description -_-查询当天能量 最大减最小
	 */
    public Map<String, Double> getElect(int plantId, long startTime, long endTime, int type) {

        // 电价由三部分构成 ： 1 申报容量电价 2 基本电价 3 惩罚奖励电价

        // 如果时间某一个为-1 默认查询当天时间
        if (startTime == -1d || startTime == -1d) {
            startTime = TimeUtil.getCurrDayStartTime(0);
            endTime = TimeUtil.getCurrDayEndTime(0);
        }

        // 根据平台id得到电价
        List<SysPowerPriceDetail> prices = sysPowerPriceDetailMapper.getPlantPriceDetailByPlantId(plantId);
        // 如果没有电价 打印日志并退出
        if (!Util.isNotBlank(prices)) {
            logger.error("电站id为" + plantId + "的电站还没有设置电费价格");
        }

        // 电价格式化
        double jprice = 0d;
        double fprice = 0d;
        double pprice = 0d;
        double gprice = 0d;

        for (SysPowerPriceDetail sysPowerPriceDetail : prices) {
            switch (sysPowerPriceDetail.getPriceType()) {
                case 1:
                    jprice += sysPowerPriceDetail.getPosPrice();
                    break;
                case 2:
                    fprice += sysPowerPriceDetail.getPosPrice();
                    break;
                case 3:
                    pprice += sysPowerPriceDetail.getPosPrice();
                    break;
                case 4:
                    gprice += sysPowerPriceDetail.getPosPrice();
                    break;
                default:
                    break;
            }
        }

        // 参数封装
        Map<String, Object> map = null;

        // 结果集
        Map<String, Double> result = null;

        // 有功
        double yg = 0d;
        // 无功
        double wg = 0d;
        // 尖
        double yjian = 0d;
        // 峰
        double yfeng = 0d;
        // 平
        double yping = 0d;
        // 谷
        double ygu = 0d;
        // 无尖
        double wjian = 0d;
        // 无峰
        double wfeng = 0d;
        // 无平
        double wping = 0d;
        // 无谷
        double wgu = 0d;
        // 第二部分电价
        double price = 0d;

        // 得到该电站所有类型-电站类表
        Map<Integer, List<Integer>> types = SystemCache.deviceOfPlant.get(plantId);

        // 结果集实例化
        result = new HashMap<>();
        if (Util.isNotBlank(types)) {
            // 得到电表列表
            List<Integer> list = types.get(DTC.ELECTRIC_METER);
            if (Util.isNotBlank(list)) {
                // 参数示例话
                map = new HashMap<>();
                map.put("startTime", startTime);
                map.put("endTime", endTime);

                // 遍历该电站所有电表
                for (Integer integer : list) {

                    map.put("deviceId", integer);

                    Map<String, Object> currElec = dataElectricMeterMapper.getElec(map);
                    if (!Util.isNotBlank(currElec)) {
                        continue;
                    }

                    yg += Util.getDouble(currElec.get("yg"));
                    wg += Util.getDouble(currElec.get("wg"));
                    yjian += Util.getDouble(currElec.get("yjian"));
                    yfeng += Util.getDouble(currElec.get("yfeng"));
                    yping += Util.getDouble(currElec.get("yping"));
                    ygu += Util.getDouble(currElec.get("ygu"));
                    wjian += Util.getDouble(currElec.get("wjian"));
                    wfeng += Util.getDouble(currElec.get("wfeng"));
                    wping += Util.getDouble(currElec.get("wping"));
                    wgu += Util.getDouble(currElec.get("wgu"));
                }
            }
        }

        price += yjian * jprice + yfeng * fprice + yping * pprice + ygu * gprice;

        // 结果集封装
        result.put("yg", yg);
        result.put("wg", wg);
        result.put("yjian", yjian);
        result.put("yfeng", yfeng);
        result.put("yping", yping);
        result.put("ygu", ygu);
        result.put("wjian", wjian);
        result.put("wfeng", wfeng);
        result.put("wping", wping);
        result.put("wgu", wgu);
        result.put("price", price);

        if (type == 1) {
            result.put("jprice", jprice);
            result.put("fprice", fprice);
            result.put("pprice", pprice);
            result.put("gprice", gprice);
        }

        return result;

    }

}
