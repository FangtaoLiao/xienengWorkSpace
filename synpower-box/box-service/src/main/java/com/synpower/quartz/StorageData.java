package com.synpower.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.DataElectricMeter;
import com.synpower.bean.ElectricStorageData;
import com.synpower.bean.EnergyStorageData;
import com.synpower.bean.InventerStorageData;
import com.synpower.bean.SysPowerPriceDetail;
import com.synpower.constant.DTC;
import com.synpower.constant.PTAC;
import com.synpower.dao.DataElectricMeterMapper;
import com.synpower.dao.DataPcsMapper;
import com.synpower.dao.ElectricStorageDataMapper;
import com.synpower.dao.EnergyStorageDataMapper;
import com.synpower.dao.InventerStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysPowerPriceDetailMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.util.PropertiesUtil;
import com.synpower.util.ServiceUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;

@Component
public class StorageData {
	private Logger logger = Logger.getLogger(StorageData.class);
	@Autowired
	private InventerStorageDataMapper inventerStorageDataMapper;
	@Autowired
	private EnergyStorageDataMapper energyStorageDataMapper;
	@Autowired
	private DataElectricMeterMapper dataElectricMeterMapper;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private DataPcsMapper dataPcsMapper;
	@Autowired
	private ElectricStorageDataMapper electricStorageDataMapper;
	@Autowired
	private SysPowerPriceDetailMapper sysPowerPriceDetailMapper;
	@Autowired
	private SystemCache systemCache;

	/**
	 * @Title: serviceDistribution
	 * @Description: 入库业务分发 光伏电站中有电表时以电站为准;当电站中没有电表时,计算逆变器收益
	 *               储能电站中有电表时以电站为准;当电站中没有电表时,计算PCS收益 分时电价
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月14日上午10:14:35
	 */
	public void serviceDistribution() {

		Map<Integer, Map<Integer, List<Integer>>> plantDevice = SystemCache.deviceOfPlant;
		Map<String, List<String>> plantTypeMap = SystemCache.plantTypeMap;
		if (Util.isNotBlank(plantDevice) && Util.isNotBlank(plantTypeMap)) {
			// 开始业务分发
			Integer maxScope = 7;
			try {
				maxScope = Integer.parseInt(PropertiesUtil.getValue("config.properties", "STORAGE_MAX_SCOPE"));
			} catch (Exception e) {
				logger.error(" 定时数据入库 读取配置文件失败。将按照默认值7天入库!");
			}
			Map<String, Object> paramMap = new HashMap<>();
			List<Integer> inventerPlants = new ArrayList<>();
			List<Integer> energyPlants = new ArrayList<>();
			// 能效
			List<Integer> eEPlans = new ArrayList<>();
			List<InventerStorageData> inventerList = new ArrayList<>();
			List<EnergyStorageData> energyStorageDataList = new ArrayList<>();
			// 能效
			List<ElectricStorageData> energyEfficList = new ArrayList<>();
			for (int i = maxScope; i > 0; i--) {
				paramMap.put("maxTime", Util.getBeforDayMax(i));

				paramMap.put("data_time", Util.getBeforDayMax(i));
				paramMap.put("minTime", Util.getBeforDayMin(i));
				/** 根据电站类型分发业务 */
				for (Entry<String, List<String>> entry : plantTypeMap.entrySet()) {
					String plantType = entry.getKey();
					switch (Integer.parseInt(plantType)) {
					/** 光伏电站 */
					case PTAC.PHOTOVOLTAIC:
						List<String> pvPlantList = entry.getValue();
						if (Util.isNotBlank(pvPlantList)) {
							int plantLen = pvPlantList.size();
							for (int j = 0; j < plantLen; j++) {
								int plantId = Integer.parseInt(pvPlantList.get(j));
								Map<Integer, List<Integer>> devices = plantDevice.get(plantId);
								if (Util.isNotBlank(devices)) {
									calculatePVPlantIncome(inventerList, devices, plantId, paramMap, inventerPlants);
								}
							}
						}
						break;
					/** 储能电站 */
					case PTAC.STOREDENERGY:
						List<String> energyPlantList = entry.getValue();
						if (Util.isNotBlank(energyPlantList)) {
							int plantLen = energyPlantList.size();
							for (int j = 0; j < plantLen; j++) {
								int plantId = Integer.parseInt(energyPlantList.get(j));
								Map<Integer, List<Integer>> devices = plantDevice.get(plantId);
								boolean b = false;
								Map<String, Object> lastRecordMap = null;
								if (i == maxScope) {
									lastRecordMap = new HashMap<>();
									lastRecordMap.put("dataTime", Util.getBeforDayMax(maxScope + 1));
									b = true;
								}
								if (Util.isNotBlank(devices)) {
									calculateEnergyPlantIncome(energyStorageDataList, devices, plantId, paramMap,
											energyPlants, lastRecordMap, b, i);
								}
							}
						}
						break;
					/**
					 * @author ybj
					 * @date 2018/8/23 9:50
					 * @description 能效电站晚上12点存库
					 */
					case PTAC.ENERGYEFFICPLANT:
						List<String> ECPlantList = entry.getValue();
						if (Util.isNotBlank(ECPlantList)) {
							int plantLen = ECPlantList.size();
							for (int j = 0; j < plantLen; j++) {
								int plantId = Integer.parseInt(ECPlantList.get(j));
								Map<Integer, List<Integer>> devices = plantDevice.get(plantId);
								if (Util.isNotBlank(devices)) {
									calculateEEPlantIncome(energyEfficList, devices, plantId, paramMap, eEPlans);
								}
							}
						}
						break;
					default:
						break;
					}
				}
			}
			// 数据整理完毕,开始批量入库
			paramMap.put("plants", inventerPlants);
			inventerStorageData(paramMap, inventerList, maxScope);
			paramMap.put("plants", energyPlants);
			energyStorageData(paramMap, energyStorageDataList, maxScope);
			paramMap.put("plants", eEPlans);
			energyEfficStorageData(paramMap, energyEfficList, maxScope);
		} else {
			logger.info(" 无电站或无设备接入不执行入库流程!");
		}

	}

	/*
	 * ====================================== 入库计算流程
	 * ======================================================
	 */

	/**
	 * @Title: calculatePVPlantIncome
	 * @Description: 计算光伏电站的收益
	 * @param inventerList
	 * @param devices
	 * @param planId
	 * @param paramMap
	 * @param              inventerPlants: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日上午9:53:03
	 */
	public void calculatePVPlantIncome(List<InventerStorageData> inventerList, Map<Integer, List<Integer>> devices,
			int plantId, Map<String, Object> paramMap, List<Integer> inventerPlants) {
		List<SysPowerPriceDetail> prices = plantInfoMapper.getPlantById(String.valueOf(plantId)).getPowerPriceDetails();
		double price = Double.parseDouble(configParam.getPowerPrice());
		if (Util.isNotBlank(prices)) {
			price = Double.parseDouble(prices.get(0).getPosPrice() + "");
		}
		List<Integer> typeOne = devices.containsKey(1) ? devices.get(1) : null;
		List<Integer> typeTwo = devices.containsKey(2) ? devices.get(2) : null;
		List<Integer> typeThree = devices.containsKey(3) ? devices.get(3) : null;
		InventerStorageData inventerStorageData = null;
		inventerPlants.add(plantId);
		if (typeThree != null && !typeThree.isEmpty()) {
			// 电表入库
			paramMap.put("devices", typeThree);
			List<DataElectricMeter> nowDataList = dataElectricMeterMapper.getDataElectricForStorage(paramMap);
			InventerStorageData electricMeterData = null;
			if (Util.isNotBlank(nowDataList)) {
				List<DataElectricMeter> beforDataList = dataElectricMeterMapper
						.getBeforDataElectricForStorage(paramMap);
				int nowDataSize = nowDataList.size();
				int beforDataSzie = Util.isNotBlank(beforDataList) ? beforDataList.size() : 0;
				// 相减获取增量数据
				for (int j = 0; j < nowDataSize; j++) {
					DataElectricMeter nowData = nowDataList.get(j);
					for (int k = 0; k < beforDataSzie; k++) {
						DataElectricMeter beforData = beforDataList.get(k);
						// 如果当日数据出现空或者0都视为无效数据赋值为0不做加减
						if (nowData.getDeviceId().equals(beforData.getDeviceId())) {
							String hisPosEnergy = beforData.getEmPosActiveEnergy() == null ? "0"
									: beforData.getEmPosActiveEnergy() + "";
							String hisNegEnergy = beforData.getEmNegActiveEnergy() == null ? "0"
									: beforData.getEmNegActiveEnergy() + "";
							String nowPosEnergy = nowData.getEmPosActiveEnergy() == null ? "0"
									: nowData.getEmPosActiveEnergy() + "";
							String nowNegEnergy = nowData.getEmNegActiveEnergy() == null ? "0"
									: nowData.getEmNegActiveEnergy() + "";
							// 用一下两个参数暂时存储总的上网下网电量 这里算收益不管下网电量 2018年3月27日16:36:54
							nowData.setEmPosJianActiveEnergy(Float.parseFloat(nowPosEnergy));
							nowData.setEmPosJianActiveEnergy(Float.parseFloat(nowNegEnergy));
							// 存储今日的上下网电量
							nowData.setEmPosActiveEnergy(Float.parseFloat(String.valueOf(
									"0".equals(nowPosEnergy) ? 0 : Util.subFloat(nowPosEnergy, hisPosEnergy, 0))));
							nowData.setEmNegActiveEnergy(Float.parseFloat(String.valueOf(
									"0".equals(nowNegEnergy) ? 0 : Util.subFloat(nowNegEnergy, hisNegEnergy, 0))));
							break;
						}
					}
				}
				// 累加计算收益
				for (int j = 0; j < nowDataSize; j++) {
					DataElectricMeter nowData = nowDataList.get(j);
					if (nowData != null) {
						if (electricMeterData == null) {
							electricMeterData = new InventerStorageData();
							electricMeterData.setDailyEnergy(0.0);
							electricMeterData.setTotalEnergy(0.0);
							electricMeterData.setDailyPrice(0.0);
							electricMeterData.setTotalPrice(0.0);
						}
						String daily = String.valueOf(nowData.getEmPosActiveEnergy());
						String total = String.valueOf(nowData.getEmPosJianActiveEnergy());
						// electricMeterData.setDailyPrice(Util.isNotBlank(daily)?Util.multDouble(daily,
						// String.valueOf(price), 1):0);
						// electricMeterData.setTotalPrice(Util.isNotBlank(total)?Util.multDouble(total,
						// String.valueOf(price), 1):0);
						electricMeterData.setDailyEnergy(
								(Util.addFloat(Double.parseDouble(daily), electricMeterData.getDailyEnergy(), 0)));
						electricMeterData.setTotalEnergy(
								(Util.addFloat(Double.parseDouble(total), electricMeterData.getTotalEnergy(), 0)));
					}
				}
			}
			// 添加到入库列表中
			if (electricMeterData != null) {
				String daily = String.valueOf(electricMeterData.getDailyEnergy());
				String total = String.valueOf(electricMeterData.getTotalEnergy());
				electricMeterData.setDailyPrice(Util.multDouble(daily, String.valueOf(price), 1));
				electricMeterData.setTotalPrice(Util.multDouble(total, String.valueOf(price), 1));
				electricMeterData.setPlantId(plantId);
				electricMeterData.setDataTime(Long.parseLong(paramMap.get("data_time") + ""));
				inventerList.add(electricMeterData);
			}
		} else {
			if (typeOne != null && !typeOne.isEmpty()) {
				paramMap.put("devices", typeOne);
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
						inventerStorageData.setDailyEnergy(Util.addFloat(
								inventerStorageData.getDailyEnergy() == null ? 0
										: inventerStorageData.getDailyEnergy().doubleValue(),
								dataTemp.getDailyEnergy() == null ? 0 : dataTemp.getDailyEnergy().doubleValue(), 0));
						inventerStorageData.setTotalEnergy(Util.addFloat(
								inventerStorageData.getTotalEnergy() == null ? 0
										: inventerStorageData.getTotalEnergy().doubleValue(),
								dataTemp.getTotalEnergy() == null ? 0 : dataTemp.getTotalEnergy().doubleValue(), 0));
						inventerStorageData.setPlantId(plantId);
					}
				}
			}
			if (inventerStorageData != null && inventerStorageData.getDailyEnergy() != null) {
				inventerStorageData
						.setTotalPrice(Util.multFloat(inventerStorageData.getTotalEnergy().doubleValue(), price, 0));
				inventerStorageData
						.setDailyPrice(Util.multFloat(inventerStorageData.getDailyEnergy().doubleValue(), price, 0));
				inventerStorageData.setDataTime(Long.parseLong(paramMap.get("data_time") + ""));
				inventerStorageData.setPlantId(plantId);
				inventerList.add(inventerStorageData);
			}
		}
	}

	/**
	 * @Title: calculateEnergyPlantIncome
	 * @Description: 储能电站收益计算
	 * @param energyStorageDataList
	 * @param devices
	 * @param plantId
	 * @param paramMap
	 * @param                       energyPlants: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日上午10:11:28
	 */

	public void calculateEnergyPlantIncome(List<EnergyStorageData> energyStorageDataList,
			Map<Integer, List<Integer>> devices, int plantId, Map<String, Object> paramMap, List<Integer> energyPlants,
			Map<String, Object> lastRecordMap, boolean b, int index) {
		List<SysPowerPriceDetail> prices = plantInfoMapper.getPlantById(String.valueOf(plantId)).getPowerPriceDetails();
		if (Util.isNotBlank(prices)) {
			// 电表
			List<Integer> typeThree = devices.containsKey(3) ? devices.get(3) : null;
			// pcs设备列表
			List<Integer> typeNine = devices.containsKey(9) ? devices.get(9) : null;
			EnergyStorageData energyStorageData = null;
			energyPlants.add(plantId);
			if (Util.isNotBlank(typeThree) || Util.isNotBlank(typeNine)) {
				/** 获取电表增量数据 */
				int eletricLen = 0;
				double jianPosEnergy = 0, jianNegEnergy = 0, pingPosEnergy = 0, pingNegEnergy = 0, fengPosEnergy = 0,
						fengNegEnergy = 0, guPosEnergy = 0, guNegEnergy = 0;
				double jianPosPrice = 0, jianNegPrice = 0, pingPosPrice = 0, pingNegPrice = 0, fengPosPrice = 0,
						fengNegPrice = 0, guPosPrice = 0, guNegPrice = 0;
				double dailyPosEnergy = 0, dailyPosPrice = 0, dailyNegEnergy = 0, dailyNegPrice = 0, dailyEnergy = 0,
						dailyPrice = 0;
				double totalPosEnergy = 0, totalNegEnergy = 0, totalPosPrice = 0, totalNegPrice = 0;
				;
				double totalEnergy = 0, totalPrice = 0;
				//
				Map<String, Object> paramMap2 = new HashMap<>();
				paramMap2.put("startTime", Util.getBeforDayMin(index + 1));
				paramMap2.put("endTime", Util.getBeforDayMax(index + 1));
				List<DataElectricMeter> beforEletricData = null;
				if (Util.isNotBlank(typeThree)) {
					eletricLen = typeThree.size();
					paramMap2.put("devices", typeThree);
					beforEletricData = dataElectricMeterMapper.getLastHistoryDataElectric(paramMap2);
				} else if (Util.isNotBlank(typeNine)) {
					eletricLen = typeNine.size();
					paramMap2.put("devices", typeNine);
					beforEletricData = dataPcsMapper.getLastHistoryDataPcs(paramMap2);
				}
				for (int i = 0; i < eletricLen; i++) {
					int deviceId = -1;
					if (Util.isNotBlank(typeThree)) {
						deviceId = typeThree.get(i);
					} else {
						deviceId = typeNine.get(i);
					}
					paramMap.put("deviceId", deviceId);
					List<DataElectricMeter> nowList = null;
					if (Util.isNotBlank(typeThree)) {
						nowList = dataElectricMeterMapper.getEnergyPlantDataForStorage(paramMap);
					} else if (Util.isNotBlank(typeNine)) {
						nowList = dataPcsMapper.getEnergyPlantDataForStorage(paramMap);
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
								} else if (nowTime > startTime && nowTime < endTime) {
									switch (priceType) {
									case 1:
										jianPosEnergy = Util.addFloats(0, jianPosEnergy, tempPos);
										jianNegEnergy = Util.addFloats(0, jianNegEnergy, tempNeg);
										jianPosPrice = Util.addFloat(jianPosPrice, Util.multFloat(tempPos, pricePos, 0),
												0);
										jianNegPrice = Util.addFloat(jianNegPrice, Util.multFloat(tempNeg, priceNeg, 0),
												0);
										break;
									case 2:
										fengPosEnergy = Util.addFloats(0, fengPosEnergy, tempPos);
										fengNegEnergy = Util.addFloats(0, fengNegEnergy, tempNeg);
										fengPosPrice = Util.addFloat(fengPosPrice, Util.multFloat(tempPos, pricePos, 0),
												0);
										fengNegPrice = Util.addFloat(fengNegPrice, Util.multFloat(tempNeg, priceNeg, 0),
												0);
										break;
									case 3:
										pingPosEnergy = Util.addFloats(0, pingPosEnergy, tempPos);
										pingNegEnergy = Util.addFloats(0, pingNegEnergy, tempNeg);
										pingPosPrice = Util.addFloat(pingPosPrice, Util.multFloat(tempPos, pricePos, 0),
												0);
										pingNegPrice = Util.addFloat(pingNegPrice, Util.multFloat(tempNeg, priceNeg, 0),
												0);
										break;
									case 4:
										guPosEnergy = Util.addFloats(0, guPosEnergy, tempPos);
										guNegEnergy = Util.addFloats(0, guNegEnergy, tempNeg);
										guPosPrice = Util.addFloat(guPosPrice, Util.multFloat(tempPos, pricePos, 0), 0);
										guNegPrice = Util.addFloat(guNegPrice, Util.multFloat(tempNeg, priceNeg, 0), 0);
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
				if (b) {
					lastRecordMap.put("plantId", plantId);
					lastRecord = energyStorageDataMapper.getPlantLastRecord(lastRecordMap);
				} else {
					int len = energyStorageDataList.size();
					for (int i = len - 1; i >= 0; i--) {
						if (energyStorageDataList.get(i).getPlantId() == plantId) {
							lastRecord = energyStorageDataList.get(i);
							break;
						}
					}

				}
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
				// 处理总收益发电量
				energyStorageData = new EnergyStorageData(jianPosEnergy, jianPosPrice, jianNegEnergy, jianNegPrice,
						fengPosEnergy, fengPosPrice, fengNegEnergy, fengNegPrice, pingPosEnergy, pingPosPrice,
						pingNegEnergy, pingNegPrice, guPosEnergy, guPosPrice, guNegEnergy, guNegPrice, dailyPosEnergy,
						dailyPosPrice, dailyNegEnergy, dailyNegPrice, dailyEnergy, dailyPrice, totalPosEnergy,
						totalPosPrice, totalNegEnergy, totalNegPrice, totalEnergy, totalPrice,
						Long.parseLong(String.valueOf(paramMap.get("data_time"))), plantId);
				energyStorageDataList.add(energyStorageData);
			}
		}
	}

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月28日下午5:02:32
	 * @Description -_-计算能效电站存储
	 */
	public void calculateEEPlantIncome(List<ElectricStorageData> energyEfficList, Map<Integer, List<Integer>> devices,
			int plantId, Map<String, Object> paramMap, List<Integer> eEPlans) {

		long startTime = (long) paramMap.get("minTime");
		long endTime = (long) paramMap.get("maxTime");
		Map<String, Double> elect = systemCache.getElect(plantId, startTime, endTime, 1);

		ElectricStorageData elec = new ElectricStorageData(elect.get("yjian"), elect.get("jprice"), elect.get("yfeng"),
				elect.get("fprice"), elect.get("yping"), elect.get("pprice"), elect.get("ygu"), elect.get("gprice"),
				elect.get("yg"), elect.get("price"), elect.get("wg"), (Long) paramMap.get("data_time"), plantId,
				elect.get("wjian"), elect.get("wfeng"), elect.get("wping"), elect.get("wgu"));

		// 加入存库列表中
		energyEfficList.add(elec);

		// 记录加入存库列表中的电站id
		eEPlans.add(plantId);

	}

	/**
	 * @Title: inventerStorageData
	 * @Description: 光伏电站批量导入
	 * @param paramMap
	 * @param dataList
	 * @param          maxScope: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月15日下午2:52:42
	 */

	/*
	 * ============================================入库数据库处理==========================
	 * ======================================
	 */

	@Transactional
	public void inventerStorageData(Map<String, Object> paramMap, List<InventerStorageData> dataList,
			Integer maxScope) {
		if (dataList == null || dataList.isEmpty()) {
			logger.info(
					" 光伏电站数据入库在指定时间段内无数据产生,不做任何操作! 时间段: " + paramMap.get("minTime") + "-" + paramMap.get("maxTime"));
		} else {
			// 清洗历史数据
			paramMap.put("minTime", Util.getBeforDayMin(maxScope));
			paramMap.put("maxTime", Util.getBeforDayMax(1));
			int status = inventerStorageDataMapper.deteleDataForStotage(paramMap);
			logger.info(" 光伏电站前" + maxScope + "天数据清理成功!开始重新计算入库。。。");
			// 数据重新入库
			paramMap.put("list", dataList);
			status = inventerStorageDataMapper.insertDataForStotage(paramMap);
			logger.info(" 光伏电站数据入库成功!");
		}
	}

	/**
	 * @Title: energyStorageData
	 * @Description: 储能电站入库
	 * @param paramMap
	 * @param dataList
	 * @param          maxScope: void
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日上午10:12:49
	 */
	@Transactional
	public void energyStorageData(Map<String, Object> paramMap, List<EnergyStorageData> dataList, Integer maxScope) {
		if (dataList == null || dataList.isEmpty()) {
			logger.info(
					" 储能电站数据入库在指定时间段内无数据产生,不做任何操作! 时间段: " + paramMap.get("minTime") + "-" + paramMap.get("maxTime"));
		} else {
			// 清洗历史数据
			paramMap.put("minTime", Util.getBeforDayMin(maxScope));
			paramMap.put("maxTime", Util.getBeforDayMax(1));
			int status = energyStorageDataMapper.deteleDataForStotage(paramMap);
			logger.info(" 储能电站前" + maxScope + "天数据清理成功!开始重新计算入库。。。");
			// 数据重新入库
			// paramMap.put("list", dataList);
			status = energyStorageDataMapper.insertDataForStotage(dataList);
			logger.info(" 储能电站数据入库成功!");
		}
	}

	/**
	 * @author ybj
	 * @date 2018年8月29日下午2:33:42
	 * @Description -_-能效电站入库
	 * @param paramMap
	 * @param dataList
	 * @param maxScope
	 */
	@Transactional
	public void energyEfficStorageData(Map<String, Object> paramMap, List<ElectricStorageData> dataList,
			Integer maxScope) {
		if (dataList == null || dataList.isEmpty()) {
			logger.info(" 能效数据入库在指定时间段内无数据产生,不做任何操作! 时间段: " + paramMap.get("minTime") + "-" + paramMap.get("maxTime"));
		} else {
			// 清洗历史数据
			paramMap.put("minTime", Util.getBeforDayMin(maxScope));
			paramMap.put("maxTime", Util.getBeforDayMax(1));
			int status = electricStorageDataMapper.deteleDataForStotage(paramMap);
			logger.info(" 能效电站前" + maxScope + "天数据清理成功!开始重新计算入库。。。");
			// 数据重新入库
			// paramMap.put("list", dataList);
			status = electricStorageDataMapper.insertDataForStotage(dataList);
			logger.info(" 能效电站数据入库成功!");
		}
	}
}
