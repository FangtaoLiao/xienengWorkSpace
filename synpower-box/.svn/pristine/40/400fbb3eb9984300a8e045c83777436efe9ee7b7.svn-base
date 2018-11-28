package com.synpower.serviceImpl;

import com.synpower.bean.*;
import com.synpower.dao.*;
import com.synpower.lang.ConfigParam;
import com.synpower.service.StatisticalGainService;
import com.synpower.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*****************************************************************************
 * @Package: com.synpower.serviceImpl ClassName: StatisticalGainServiceImpl
 * @Description: 收益统计业务工具类
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月17日下午2:22:32 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Service
public class StatisticalGainServiceImpl implements StatisticalGainService {
	@Autowired
	private DataStringInverterMapper stringInventerMapper;
	@Autowired
	private DataCentralInverterMapper centralInventerMapper;
	@Autowired
	private DataElectricMeterMapper dataElectricMeterMapper;
	@Autowired
	private InventerStorageDataMapper inventerStorageDataMapper;
	@Autowired
	private CollDeviceMapper collDeviceMapper;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private SystemCache systemCache;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private DataPcsMapper pcsMapper;

	/**
	 * @Title: getPowerAndDataTime
	 * @Description: 装载功率到对应的时间点位上去
	 * @return: Map<String, Object>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月17日14:23:16
	 */
	public Map<String, Object> getPowerAndDataTime(String riseTime, String setTime,
			Map<Integer, List<Integer>> deviceMap, String pId, int nowLocation) {
		List<Integer> typeOne = deviceMap.containsKey(1) ? deviceMap.get(1) : null;
		List<Integer> typeTwo = deviceMap.containsKey(2) ? deviceMap.get(2) : null;
		List<Integer> typeThree = deviceMap.containsKey(3) ? deviceMap.get(3) : null;
		Map<String, Object> paramMap = new HashMap<>(4);
		List<Integer> devices = null;
		paramMap.put("maxTime", Util.getBeforStorageDayMax(0));
		paramMap.put("minTime", Util.getBeforStorageDayMin(0));
		List<Object[]> nowData = new ArrayList<>(10);
		if (typeOne != null && !typeOne.isEmpty()) {
			paramMap.put("devices", typeOne);
			devices = typeOne;
			List<DataCentralInverter> centralList = centralInventerMapper.getTimesPower(paramMap);
			if (centralList != null && !centralList.isEmpty()) {
				for (int i = 0; i < centralList.size(); i++) {
					long dataTime = centralList.get(i).getDataTime();
					float power = centralList.get(i).getActivePower();
					Object[] tempStr = { dataTime, power };
					nowData.add(tempStr);
				}
			}
		}
		if (typeTwo != null && !typeTwo.isEmpty()) {
			paramMap.put("devices", typeTwo);
			devices = typeTwo;
			List<DataStringInverter> centralList = stringInventerMapper.getTimesPower(paramMap);
			if (centralList != null && !centralList.isEmpty()) {
				if (nowData == null || nowData.isEmpty()) {
					for (int i = 0; i < centralList.size(); i++) {
						long dataTime = centralList.get(i).getDataTime();
						float power = centralList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				} else {
					for (int i = 0; i < centralList.size(); i++) {
						DataStringInverter inverter = centralList.get(i);
						long dataTime = centralList.get(i).getDataTime();
						boolean b = false;
						for (int j = 0; j < nowData.size(); j++) {
							Object[] obj = nowData.get(i);
							if (dataTime == Long.parseLong(obj[0] + "")) {
								double totalPower = Util.addFloat(Float.parseFloat(obj[1] + ""),
										inverter.getActivePower(), 1);
								nowData.remove(i);
								nowData.add(i, new Object[] { dataTime, totalPower });
								b = true;
								break;
							}
						}
						if (!b) {
							nowData.add(new Object[] { dataTime, inverter.getActivePower() });
						}
					}
				}
			}
		}
		Map<String, Object> result = ServiceUtil.loadParamForCurrentPower(riseTime, setTime, nowData, nowLocation);
		result.put("devices", devices);
		return result;
	}

	public Map<String, Object> getPowerAndDataTime(String pId, String time, PlantInfo plantInfo) throws ParseException {
		String today = Util.getCurrentTimeStr("yyyy-MM-dd");
		String[] location = StringUtils.split(plantInfo.getLoaction(), ",");
		Map<Integer, List<Integer>> deviceMap = systemCache.deviceOfPlant.get(Integer.parseInt(pId));
		List<Integer> typeOne = Util.isNotBlank(deviceMap) ? deviceMap.containsKey(1) ? deviceMap.get(1) : null : null;
		List<Integer> typeTwo = Util.isNotBlank(deviceMap) ? deviceMap.containsKey(2) ? deviceMap.get(2) : null : null;
		List<Integer> typeNine = Util.isNotBlank(deviceMap) ? deviceMap.containsKey(9) ? deviceMap.get(9) : null : null;
		Map<String, Object> paramMap = new HashMap<>(4);
		List<Integer> devices = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		paramMap.put("minTime", sdf.parse(time + " 00:00:00.000").getTime());
		paramMap.put("maxTime", sdf.parse(time + " 23:59:59.999").getTime());
		List<Object[]> nowData = new ArrayList<>();
		Map<String, Object> result = null;
		// 判断电站类型
		Map<String, String> plantTypeList = systemCache.plantTypeList;
		String plantType = plantTypeList.get(pId);
		if ("1".equals(plantType)) {
			String riseTime = ServiceUtil.getSunRise(time, Double.parseDouble(location[0]),
					Double.parseDouble(location[1]));
			String setTime = ServiceUtil.getSunSet(time, Double.parseDouble(location[0]),
					Double.parseDouble(location[1]));
			List<String> timeList = ServiceUtil.getStringTimeSegment(riseTime, setTime);
			/*
			 * //获取电站设备集合 List<CollDevice>deviceList=deviceMapper.getDeviceListByPid(pId);
			 * //按照设备类型分类 Map<Integer,
			 * List<Integer>>deviceMap=ServiceUtil.classifyDeviceForPlant(deviceList);
			 */
			// 将对应电站的对应时间点的功率装载到时间点位上
			// 功率
			String curTime = timeList.get(0);
			int nowLocation = 0;
			if (time.equals(today)) {
				curTime = ServiceUtil.getNowTimeInTimes();
				nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
			} else {
				curTime = "";
				nowLocation = timeList.size() - 1;
			}
			if (typeOne != null && !typeOne.isEmpty()) {
				paramMap.put("devices", typeOne);
				devices = typeOne;
				List<DataCentralInverter> centralList = centralInventerMapper.getTimesPower(paramMap);
				if (centralList != null && !centralList.isEmpty()) {
					for (int i = 0; i < centralList.size(); i++) {
						long dataTime = centralList.get(i).getDataTime();
						float power = centralList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				}
			}
			if (typeTwo != null && !typeTwo.isEmpty()) {
				paramMap.put("devices", typeTwo);
				devices = typeTwo;
				List<DataStringInverter> centralList = stringInventerMapper.getTimesPower(paramMap);
				if (centralList != null && !centralList.isEmpty()) {
					if (nowData == null || nowData.isEmpty()) {
						for (int i = 0; i < centralList.size(); i++) {
							long dataTime = centralList.get(i).getDataTime();
							float power = centralList.get(i).getActivePower();
							Object[] tempStr = { dataTime, power };
							nowData.add(tempStr);
						}
					} else {
						for (int i = 0; i < centralList.size(); i++) {
							DataStringInverter inverter = centralList.get(i);
							long dataTime = centralList.get(i).getDataTime();
							boolean b = false;
							for (int j = 0; j < nowData.size(); j++) {
								Object[] obj = nowData.get(i);
								if (dataTime == Long.parseLong(obj[0] + "")) {
									double totalPower = Util.addFloat(Float.parseFloat(obj[1] + ""),
											inverter.getActivePower(), 1);
									nowData.remove(i);
									nowData.add(i, new Object[] { dataTime, totalPower });
									b = true;
									break;
								}
							}
							if (!b) {
								nowData.add(new Object[] { dataTime, inverter.getActivePower() });
							}
						}
					}
				}
			}
			String[] units = new String[] { "千瓦", "兆瓦", "吉瓦" };
			result = ServiceUtil.loadParamForCurrentList(riseTime, setTime, nowData, nowLocation, "电站实时功率", units,
					1000);
			String[] yData1 = (String[]) result.get("yData1");
			// 单位
			String unit = String.valueOf(result.get("unit"));
			if (nowLocation < yData1.length) {
				String str = String.valueOf(getNowDayIncome(pId).get("activcePower"));
				double power = "".equals(StringUtil.checkBlank(str)) ? 0 : Double.parseDouble(str);
				if (units[1].equals(unit)) {
					yData1[nowLocation] = String
							.valueOf(new BigDecimal(power / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				} else if (units[2].equals(unit)) {
					yData1[nowLocation] = String.valueOf(
							new BigDecimal(power / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
			}
			double value = Double.valueOf(yData1[nowLocation]);
			result.put("yData1", yData1);
			// 时间轴
			Object[] xData = timeList.toArray();
			result.put("xData", xData);
			// 当前时间
			result.put("curTime", curTime);
			double maxValue = Double.parseDouble(result.get("maxValue") + "");
			if (maxValue == 0.0) {
				maxValue = 0.5;
			}
			if (maxValue < value) {
				maxValue = value;
			}
			/*
			 * if (maxValue<plantInfo.getCapacity().doubleValue()) {
			 * maxValue=plantInfo.getCapacity().doubleValue(); }
			 */
			String[] yData2 = ServiceUtil.getParabola(yData1, maxValue);
			// 抛物线
			result.put("yData2", yData2);
			// 当前抛物线高度
			result.put("curData2", yData2[nowLocation]);
			result.put("unit", unit);
			result.put("curPower", yData1[nowLocation] + "" + unit);
		} else if ("2".equals(plantType)) {
			List<String> timeList = ServiceUtil.getStringTimeSegment("00:00", "23:59");
			/*
			 * //获取电站设备集合 List<CollDevice>deviceList=deviceMapper.getDeviceListByPid(pId);
			 * //按照设备类型分类 Map<Integer,
			 * List<Integer>>deviceMap=ServiceUtil.classifyDeviceForPlant(deviceList);
			 */
			// 将对应电站的对应时间点的功率装载到时间点位上
			// 功率
			String curTime = timeList.get(0);
			int nowLocation = 0;
			if (time.equals(today)) {
				curTime = ServiceUtil.getNowTimeInTimes();
				nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
			} else {
				curTime = "";
				nowLocation = timeList.size() - 1;
			}
			if (typeNine != null && !typeNine.isEmpty()) {
				paramMap.put("devices", typeNine);
				devices = typeNine;
				List<DataPcs> activePowerList = pcsMapper.getTimesPower(paramMap);
				if (activePowerList != null && !activePowerList.isEmpty()) {
					for (int i = 0; i < activePowerList.size(); i++) {
						long dataTime = activePowerList.get(i).getDataTime();
						float power = activePowerList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				}
			}
			String[] units = new String[] { "度", "万度", "亿度" };
			result = ServiceUtil.loadParamForCurrentList("00:00", "23:59", nowData, nowLocation, "电站实时功率", units,
					10000);
			String[] yData1 = (String[]) result.get("yData1");
			// 单位
			String unit = String.valueOf(result.get("unit"));
			if (nowLocation < yData1.length) {
				double power = systemCache.plantCurrentEnergyPowerBat.get(pId);
				if (units[1].equals(unit)) {
					yData1[nowLocation] = String
							.valueOf(new BigDecimal(power / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				} else if (units[2].equals(unit)) {
					yData1[nowLocation] = String.valueOf(
							new BigDecimal(power / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
			}
			double value = Double.valueOf(yData1[nowLocation]);
			result.put("yData1", yData1);
			// 时间轴
			Object[] xData = timeList.toArray();
			result.put("xData", xData);
			// 当前时间
			result.put("curTime", curTime);
			result.put("curPower", yData1[nowLocation] + "" + unit);
		}
		result.put("devices", devices);
		return result;
	}

	/**
	 * @Title: serviceDistribution
	 * @Description: 按一定时间段统计收益
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月17日下午2:26:02
	 */
	@Override
	public Map getNowDayIncome(String pId) {
		Map result = new HashMap<>();
		String plantType = systemCache.plantTypeList.get(pId);
		switch (plantType) {
		case "1":
			int status = ServiceUtil.getPlantStatus(pId);
			if (status == 0 || status == 1) {
				Map<String, Double> currData = systemCache.currentDataOfPlant.get(pId);
				result.put("power", currData == null ? "0" : Util.getDecimalFomat2().format(currData.get("energy")));
				result.put("inCome", currData == null ? "0" : Util.getDecimalFomat2().format(currData.get("price")));
				result.put("activcePower",
						currData == null ? "0" : Util.getDecimalFomat2().format(currData.get("power")));
			} else {
				result = getNowDayIncomeByDb(pId);
			}
			break;
		case "2":
			result = systemCache.currentDataOfPlant.get(pId);
		default:
			break;
		}
		return result;
	}

	public Map<String, Object> getNowDayIncomeByDb(String pId) {
		Map<String, Object> result = new HashMap<>(2);
		result.put("inCome", "0");
		result.put("power", "0");
		// 得到所有设备集合
		Map<Integer, Map<Integer, List<Integer>>> deviceLists = systemCache.deviceOfPlant;
		if (Util.isNotBlank(deviceLists)) {
			Map<Integer, List<Integer>> devices = deviceLists.get(Integer.parseInt(pId));
			if (Util.isNotBlank(devices)) {
				// 开始业务分发
				Map<String, Object> paramMap = new HashMap<>(2);
				double inCome = 0;
				paramMap.put("maxTime", Util.getBeforDayMax(0));
				paramMap.put("minTime", Util.getBeforDayMin(0));
				List<SysPowerPriceDetail> prices = plantInfoMapper.getPlantById(pId).getPowerPriceDetails();
				double price = Double.parseDouble(configParam.getPowerPrice());
				if (Util.isNotBlank(prices)) {
					price = Double.parseDouble(prices.get(0).getPosPrice() + "");
				}
				//集中式逆变器
				List<Integer> typeOne = devices.containsKey(1) ? devices.get(1) : null;
				//组串式逆变器
				List<Integer> typeTwo = devices.containsKey(2) ? devices.get(2) : null;
				//电表
				List<Integer> typeThree = devices.containsKey(3) ? devices.get(3) : null;
				
				if (typeThree != null && !typeThree.isEmpty()) {
					int treeSize = typeThree.size();
					double energy = 0, energyPrice = 0, power = 0;
					paramMap.put("list", typeThree);
					List<DataElectricMeter> electricNowDatas = dataElectricMeterMapper.getTotalDataElectric(paramMap);
					if (Util.isNotBlank(electricNowDatas)) {
						int nowSize = electricNowDatas.size();
						for (int j = 0; j < nowSize; j++) {
							DataElectricMeter tempData = electricNowDatas.get(j);
							// 今日最大正向电度数
							double nowPosEnergy = Double.parseDouble(
									tempData.getEmPosActiveEnergy() == null ? tempData.getEmPosActiveEnergy() + ""
											: "0");
							// 今日反向电度数
							double nowNegEnergy = Double.parseDouble(
									tempData.getEmNegActiveEnergy() == null ? tempData.getEmNegActiveEnergy() + ""
											: "0");
							List<DataElectricMeter> electricBeforDatas = systemCache.electricBeforDatas;
							if (Util.isNotBlank(electricBeforDatas)) {
								int beforSize = electricBeforDatas.size();
								int deviceId = tempData.getDeviceId();
								for (int i = 0; i < beforSize; i++) {
									DataElectricMeter beforData = electricBeforDatas.get(i);
									if (beforData.getDeviceId().equals(deviceId)) {
										double hisPosEnergy = Double
												.parseDouble(beforData.getEmPosActiveEnergy() == null
														? beforData.getEmPosActiveEnergy() + ""
														: "0");
										// 今日反向电度数
										// double
										// hisNegEnergy=Double.parseDouble(beforData.getEmNegActiveEnergy()==null?beforData.getEmNegActiveEnergy()+"":"0");
										nowPosEnergy = Util.subFloat(nowPosEnergy, hisPosEnergy, 0);
										// nowNegEnergy=Util.subFloat(nowNegEnergy, hisNegEnergy, 0);
										break;
									}
								}
							}

							energy = Util.addFloat(energy, nowPosEnergy, 0);
						}
					}
					result.put("inCome", Util.multFloat(energy, price, 1));
					result.put("power", energy);
				} else {
					InventerStorageData inventerStorageData = null;
					if (typeOne != null && !typeOne.isEmpty()) {
						paramMap.put("devices", typeOne);
						//TODO
						InventerStorageData dataTemp = inventerStorageDataMapper.getDataCentralForStorage(paramMap);
						if (dataTemp != null && dataTemp.getDailyEnergy() != null) {
							inventerStorageData = dataTemp;
						}
					}
					if (typeTwo != null && !typeTwo.isEmpty()) {
						paramMap.put("devices", typeTwo);
						InventerStorageData dataTemp = inventerStorageDataMapper.getDataStringForStorage(paramMap);
						if (dataTemp != null && dataTemp.getDailyEnergy() != null) {
							if (inventerStorageData == null) {
								inventerStorageData = dataTemp;
							} else {
								inventerStorageData.setDailyEnergy(
										Util.addFloat(inventerStorageData.getDailyEnergy().doubleValue(),
												dataTemp.getDailyEnergy().doubleValue(), 0));
								inventerStorageData.setTotalEnergy(
										Util.addFloat(inventerStorageData.getTotalEnergy().doubleValue(),
												dataTemp.getTotalEnergy().doubleValue(), 0));
							}
						}
					}
					if (inventerStorageData != null && inventerStorageData.getDailyEnergy() != null) {
						inventerStorageData.setDailyPrice(
								Util.multFloat(inventerStorageData.getDailyEnergy().doubleValue(), price, 0));
						result.put("inCome", inventerStorageData.getDailyPrice());
						result.put("power", inventerStorageData.getDailyEnergy());
					}
				}
				result.put("activcePower", "0");
			} else {
				result.put("inCome", "0");
				result.put("power", "0");
				result.put("activcePower", "0");
			}
		} else {
			result.put("inCome", "0");
			result.put("power", "0");
			result.put("activcePower", "0");
		}
		return result;
	}

	@Override
	public Map<String, Object> getHistoryIncome(String pId) {
		Map<String, Object> result = new HashMap<>();
		// 电站历史收益
		Map<String, Double> historyData = systemCache.historyDataOfPlant.get(pId);
		
		// 电站今日收益
		Map<String, Object> currData = getNowDayIncome(pId);
		double hisPower = 0, hisPrice = 0, currPower = 0, currPrice = 0, totalPower = 0, totalPrice = 0;
		if (Util.isNotBlank(historyData)) {
			hisPower = historyData.get("power");
			hisPrice = historyData.get("price");
		}
		if (Util.isNotBlank(currData)) {
			String tempPower = String.valueOf(currData.get("power"));
			String tempPrice = String.valueOf(currData.get("inCome"));
			currPower = "null".equals(tempPower) ? 0 : Double.parseDouble(tempPower);
			currPrice = "null".equals(tempPrice) ? 0 : Double.parseDouble(tempPrice);
		}
		totalPower = Util.addFloat(hisPower, currPower, 1);
		totalPrice = Util.addFloat(hisPrice, currPrice, 1);
		result.put("power", totalPower);
		result.put("inCome", totalPrice);
		return result;
	}

	/**
	 * @Author lz
	 * @Description: 多加入日收益, 并格式化
	 * @param: [pId]
	 * @return: {java.util.Map<java.lang.String,java.lang.Object>}
	 * @Date: 2018/11/8 20:15
	 **/
	@Override
	public Map<String, Object> getHistoryIncome2(String pId) {
		Map<String, Object> result = new HashMap<>();
		// 电站历史收益
		Map<String, Double> historyData = systemCache.historyDataOfPlant.get(pId);

		// 电站今日收益
		Map<String, Object> currData = getNowDayIncome(pId);
		double hisPower = 0, hisPrice = 0, currPower = 0, currPrice = 0, totalPower = 0, totalPrice = 0;
		if (Util.isNotBlank(historyData)) {
			hisPower = historyData.get("power");
			hisPrice = historyData.get("price");
		}
		if (Util.isNotBlank(currData)) {
			String tempPower = String.valueOf(currData.get("power"));
			String tempPrice = String.valueOf(currData.get("inCome"));
			currPower = "null".equals(tempPower) ? 0 : Double.parseDouble(tempPower);
			currPrice = "null".equals(tempPrice) ? 0 : Double.parseDouble(tempPrice);
		}
		totalPower = Util.addFloat(hisPower, currPower, 1);
		totalPrice = Util.addFloat(hisPrice, currPrice, 1);

		totalPower = Util.getDecimalValue(totalPower, 2, BigDecimal.ROUND_HALF_UP);
		totalPrice = Util.getDecimalValue(totalPrice, 2, BigDecimal.ROUND_HALF_UP);
		currPower = Util.getDecimalValue(currPower, 2, BigDecimal.ROUND_HALF_UP);
		currPrice = Util.getDecimalValue(currPrice, 2, BigDecimal.ROUND_HALF_UP);

		result.put("totalPower", totalPower);
		result.put("totalPrice", totalPrice);
		result.put("currPower", currPower);
		result.put("currPrice", currPrice);
		return result;
	}
	@Override
	public List<Map<String, Object>> getPowerAndDataTimeForMulti(String string, String string2,
			Map<Integer, List<Integer>> deviceMap, String pId, int nowLocation, String time) throws ParseException {
		List<Integer> typeOne = deviceMap.containsKey(1) ? deviceMap.get(1) : null;
		List<Integer> typeTwo = deviceMap.containsKey(2) ? deviceMap.get(2) : null;
		List<Integer> typeThree = deviceMap.containsKey(3) ? deviceMap.get(3) : null;
		List<Integer> typeNine = deviceMap.containsKey(9) ? deviceMap.get(9) : null;
		Map<String, Object> paramMap = new HashMap<>(4);
		List<Integer> devices = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		paramMap.put("minTime", sdf.parse(time + " 00:00:00.000").getTime());
		paramMap.put("maxTime", sdf.parse(time + " 23:59:59.999").getTime());
		List<Object[]> nowData = new ArrayList<>(10);
		List<Object[]> nowData2 = new ArrayList<>(10);
		List<Object[]> nowData3 = new ArrayList<>(10);
		// 判断电站类型
		Map<String, String> plantTypeList = systemCache.plantTypeList;
		String plantType = plantTypeList.get(pId);
		/*
		 * if ("1".equals(plantType)) { if (typeOne!=null&&!typeOne.isEmpty()) {
		 * paramMap.put("devices", typeOne); devices=typeOne; List<DataCentralInverter>
		 * centralList=centralInventerMapper.getTimesPower(paramMap); if
		 * (centralList!=null&&!centralList.isEmpty()) { for (int i = 0; i <
		 * centralList.size(); i++) { long dataTime=centralList.get(i).getDataTime();
		 * float power=centralList.get(i).getActivePower(); Object []
		 * tempStr={dataTime,power}; nowData.add(tempStr); } } } if
		 * (typeTwo!=null&&!typeTwo.isEmpty()) { paramMap.put("devices", typeTwo);
		 * devices=typeTwo; List<DataStringInverter>
		 * centralList=stringInventerMapper.getTimesPower(paramMap); if
		 * (centralList!=null&&!centralList.isEmpty()) { if
		 * (nowData==null||nowData.isEmpty()) { for (int i = 0; i < centralList.size();
		 * i++) { long dataTime=centralList.get(i).getDataTime(); float
		 * power=centralList.get(i).getActivePower(); Object []
		 * tempStr={dataTime,power}; nowData.add(tempStr); } }else { for (int i = 0; i <
		 * centralList.size(); i++) { DataStringInverter inverter=centralList.get(i);
		 * long dataTime=centralList.get(i).getDataTime(); boolean b=false; for (int j =
		 * 0; j < nowData.size(); j++) { Object[] obj=nowData.get(i); if
		 * (dataTime==Long.parseLong(obj[0]+"")) { double
		 * totalPower=Util.addFloat(Float.parseFloat(obj[1]+""),
		 * inverter.getActivePower(), 1); nowData.remove(i); nowData.add(i, new Object
		 * []{dataTime,totalPower}); b=true; break; } } if (!b) { nowData.add(new Object
		 * []{dataTime,inverter.getActivePower()}); } } } } } } else
		 * if("2".equals(plantType)) {
		 */
		if (typeNine != null && !typeNine.isEmpty()) {
			paramMap.put("devices", typeNine);
			devices = typeNine;
			List<DataPcs> activePowerList = pcsMapper.getTimesPowerForMulti(paramMap);
			if (activePowerList != null && !activePowerList.isEmpty()) {
				for (int i = 0; i < activePowerList.size(); i++) {
					long dataTime = activePowerList.get(i).getDataTime();
					float power = activePowerList.get(i).getActivePower();
					float powerBat = activePowerList.get(i).getPowerBat();
					// Object [] tempStr={dataTime,power,powerBat,0};
					Object[] tempStr = { dataTime, powerBat };
					Object[] tempStr2 = { dataTime, power };
					Object[] tempStr3 = { dataTime, 0 };
					nowData.add(tempStr);
					nowData2.add(tempStr2);
					nowData3.add(tempStr3);
				}
			}
		}
		// }
		// List<Map<String,
		// Object>>result=ServiceUtil.loadParamForCurrentPowerForMulti("00:00","23:59",nowData,nowLocation);
		String[] units = { "度", "千度", "亿度" };
		Map<String, Object> result = ServiceUtil.loadParamForCurrentListMulti("00:00", "23:59", nowData, nowLocation,
				"充放电功率", units, 1000);
		result.put("type", "line");
		Map<String, Object> result2 = ServiceUtil.loadParamForCurrentListMulti("00:00", "23:59", nowData2, nowLocation,
				"电网功率", units, 1000);
		result2.put("type", "line");
		Map<String, Object> result3 = ServiceUtil.loadParamForCurrentListMulti("00:00", "23:59", nowData3, nowLocation,
				"用电功率", units, 1000);
		result3.put("type", "line");
		List<Map<String, Object>> finalList = new ArrayList<>();
		finalList.add(result);
		finalList.add(result2);
		finalList.add(result3);
		return finalList;
	}
}