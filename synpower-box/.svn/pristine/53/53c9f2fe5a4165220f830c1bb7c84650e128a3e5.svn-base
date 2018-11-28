package com.synpower.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.CollDevice;
import com.synpower.bean.CollSignalLabel;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.ReportOrder;
import com.synpower.bean.ReportOrderStoredOnly;
import com.synpower.bean.StorageReportOrder;
import com.synpower.bean.SysAddress;
import com.synpower.bean.SysOrg;
import com.synpower.constant.DTC;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.CollSignalLabelMapper;
import com.synpower.dao.DataCentralInverterMapper;
import com.synpower.dao.EnergyStorageDataMapper;
import com.synpower.dao.InventerStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysAddressMapper;
import com.synpower.dao.SysOrgMapper;
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
import com.synpower.service.PlantInfoService;
import com.synpower.service.PlantReportService;
import com.synpower.util.ListUtils;
import com.synpower.util.RegionNode;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.UnitUtil;
import com.synpower.util.Util;
import org.springframework.util.CollectionUtils;

/*****************************************************************************
 * @Package: com.synpower.serviceImpl ClassName: PlantReportServiceImpl
 * @Description: 电站运行报表
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年12月6日下午4:44:04 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Service
public class PlantReportServiceImpl implements PlantReportService {
	private Logger logger = Logger.getLogger(PlantReportServiceImpl.class);
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private InventerStorageDataMapper inventerStorageDataMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysAddressMapper addressMapper;
	@Autowired
	private SysPersonalSetMapper personalSetMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private CollSignalLabelMapper signalLabelMapper;
	@Autowired
	private DataCentralInverterMapper centralInverterMapper;
	@Autowired
	private EnergyStorageDataMapper energyMapper;

	@Override
	public MessageBean fuzzySearchPlant(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String plantName = String.valueOf(map.get("plantName"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String storedPlantOnly = String.valueOf(map.get("storedPlantOnly"));
		List<Map<String, String>> reultList = new ArrayList<>();

		// 过滤出需要的类型
		String plantType = String.valueOf(map.get("plantType"));
		ArrayList<Integer> tPlantIdList = new ArrayList<>();
		List<String> pList = SystemCache.plantTypeMap.get(plantType);
		if (!CollectionUtils.isEmpty(pList)) {
			pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
		}

		if (plantName != null && plantName != "null") {
			List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);
			if (plants != null && !plants.isEmpty()) {
				map.clear();
				map.put("plantName", plantName);
				map.put("list", plants);
				List<PlantInfo> plantList = null;
				if (Util.isNotBlank(storedPlantOnly) && "1".equals(storedPlantOnly)) {
					plantList = plantInfoMapper.getPlantsForSearchStoredOnly(map);
				} else {
					plantList = plantInfoMapper.getPlantsForSearch(map);
				}
				if (plantList != null && !plantList.isEmpty()) {
					for (PlantInfo plantInfo : plantList) {
						Integer plantId = plantInfo.getId();
						if (Util.isEmpty(plantType)) {
							Map<String, String> tempMap = new HashMap<>();
							tempMap.put("plantId", String.valueOf(plantId));
							tempMap.put("plantName", plantInfo.getPlantName());
							tempMap.put("valueTime", String.valueOf(plantInfo.getCreateTime()));
							reultList.add(tempMap);
						} else {
							if (!CollectionUtils.isEmpty(tPlantIdList)) {
								if (tPlantIdList.contains(plantId)) {
									Map<String, String> tempMap = new HashMap<>();
									tempMap.put("plantId", String.valueOf(plantId));
									tempMap.put("plantName", plantInfo.getPlantName());
									tempMap.put("valueTime", String.valueOf(plantInfo.getCreateTime()));
									reultList.add(tempMap);
								}
							}
						}
					}
				}
			}
		}
		msg.setBody(reultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean fuzzySearchPlantByAreaId(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String plantName = String.valueOf(map.get("plantName"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String areaId = String.valueOf(map.get("areaId"));

		if (areaId == null || "null".equals(areaId) || "".equals(areaId)) {
			areaId = configParam.getLocation();
		}
		List<Map<String, String>> resultList = new ArrayList<>();
		if (plantName != null && plantName != "null") {
			List<Integer> plant = plantInfoService.getPlantsForUser(tokenId, session, 1);
			map.clear();
			map.put("name", areaId);
			map.put("plants", plant);
			List<Integer> plants = addressMapper.getPlantsIdByArea(map);
			if (plants != null && !plants.isEmpty()) {
				map.clear();
				map.put("plantName", plantName);
				map.put("list", plants);
				List<PlantInfo> plantList = plantInfoMapper.getPlantsForSearch(map);
				if (plantList != null && !plantList.isEmpty()) {
					for (PlantInfo plantInfo : plantList) {
						Integer planId = plantInfo.getId();
						Map<String, String> tempMap = new HashMap<>();
						tempMap.put("plantId", String.valueOf(planId));
						tempMap.put("plantName", plantInfo.getPlantName());
						tempMap.put("valueTime", String.valueOf(plantInfo.getCreateTime()));
						resultList.add(tempMap);
					}
				}
			}
		}

		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	/**
	 * @throws ServiceException
	 * @Title: runReportTable
	 * @Description: 电站运行报表折线图
	 * @return: MessageBean
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月6日下午5:22:20
	 */
	@Override
	public MessageBean runReportLine(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 电站
		String plantId = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(plantId);
		// 电站类型id
		Integer typeId = plantInfo.getPlantTypeA();
		// 电站容量换算
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000, 0);
		} else if ("GW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000000, 0);
		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		List<Map<String, Object>> yDaya = new ArrayList(3);
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		}

		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", plantId);
		// 光伏电站计算
		if (typeId == 1) {
			List<Map<String, Object>> reportList = inventerStorageDataMapper.getPlantRunReportLine(map);
			String[] name = new String[] { "发电量", "收益" };
			String[] unit = new String[] { "kWh", "元" };
			if (reportList != null && !reportList.isEmpty()) {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					switch (i) {
					case 0:
						for (int j = 0; j < xData.size(); j++) {
							String tempTime = xData.get(j);
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String dataTime = reportMap.get("dataTime").toString();
								String dataValue = Util.roundDouble(reportMap.get("energy").toString());
								if (dataTime.equals(tempTime)) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;
//					case 1:
//						for (int j = 0; j < xData.size(); j++) {
//							String tempTime=xData.get(j);
//							boolean b=true;
//							for (int k = 0; k < reportList.size(); k++) {
//								Map<String, Object>reportMap=reportList.get(k);
//								String dataTime=reportMap.get("dataTime").toString();
//								String dataValue=String.valueOf(Util.divideDouble(reportMap.get("energy").toString(), capacity, 1));
//								if (dataTime.equals(tempTime)) {
//									value.add(dataValue);
//									b=false;
//									break;
//								}
//							}
//							if (b) {
//								value.add("0");
//							}
//						}
//						break;
					case 1:
						for (int j = 0; j < xData.size(); j++) {
							String tempTime = xData.get(j);
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String dataTime = reportMap.get("dataTime").toString();
								String dataValue = Util.roundDouble(reportMap.get("price").toString());
								if (dataTime.equals(tempTime)) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;

					default:
						break;
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}

			} else {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					for (int j = 0; j < xData.size(); j++) {
						value.add("0");
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}
			}
			map.clear();
			map.put("xData", xData.toArray());
			map.put("yData", yDaya.toArray());
			msg.setBody(map);
			msg.setCode(Header.STATUS_SUCESS);
			// 储能电站计算
		} else if (typeId == 2) {
			List<Map<String, Object>> reportList = energyMapper.getPlantRunReportLine(map);
			String[] name = new String[] { "充电量", "放电量", "收益" };
			String[] unit = new String[] { "kWh", "kWh", "元" };
			if (reportList != null && !reportList.isEmpty()) {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					switch (i) {
					case 0:
						for (int j = 0; j < xData.size(); j++) {
							String tempTime = xData.get(j);
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String dataTime = reportMap.get("dataTime").toString();
								String dataValue = Util.roundDouble(reportMap.get("posEnergy").toString());
								if (dataTime.equals(tempTime)) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;
					case 1:
						for (int j = 0; j < xData.size(); j++) {
							String tempTime = xData.get(j);
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String dataTime = reportMap.get("dataTime").toString();
								String dataValue = String
										.valueOf(Util.divideDouble(reportMap.get("negEnergy").toString(), capacity, 1));
								if (dataTime.equals(tempTime)) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;
					case 2:
						for (int j = 0; j < xData.size(); j++) {
							String tempTime = xData.get(j);
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String dataTime = reportMap.get("dataTime").toString();
								String dataValue = Util.roundDouble(reportMap.get("price").toString());
								if (dataTime.equals(tempTime)) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;

					default:
						break;
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}

			} else {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					for (int j = 0; j < xData.size(); j++) {
						value.add("0");
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}
			}
			map.clear();
			map.put("xData", xData.toArray());
			map.put("yData", yDaya.toArray());
			msg.setBody(map);
			msg.setCode(Header.STATUS_SUCESS);
		}
		return msg;
	}

	/**
	 * @throws ServiceException
	 * @Title: runReportTable
	 * @Description: 电站运行报表table
	 * @return: MessageBean
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月6日下午5:22:20
	 */
	@Override
	public MessageBean runReportTable(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String draw = String.valueOf(map.get("draw"));
		// 电站
		String plantId = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		List<Map<String, String>> tempList = "null".equals(map.get("orderName")) ? null
				: "".equals(map.get("orderName")) ? null : (List<Map<String, String>>) map.get("orderName");
		String[] sortnameArr = null;
		boolean[] typeArr = null;
		String formatRegex = null;
		if (tempList != null && !tempList.isEmpty()) {
			typeArr = new boolean[tempList.size()];
			sortnameArr = new String[tempList.size()];
			int count = 0;
			for (int i = 0; i < tempList.size(); i++) {
				for (String key : tempList.get(i).keySet()) {
					String value = tempList.get(i).get(key);
					sortnameArr[count] = key;
					if ("desc".equalsIgnoreCase(value)) {
						typeArr[count] = false;
						count++;
					} else {
						typeArr[count] = true;
						count++;
					}
				}
			}
		}
		// PlantInfo plantInfo=plantInfoMapper.getPlantInfo(plantId);
		// double capacity=plantInfo.getCapacity();
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(plantId);
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
//		if ("MW".equalsIgnoreCase(plantUnit)) {
//			capacity=Util.multFloat(capacity, 1000, 0);
//		}else if ("GW".equalsIgnoreCase(plantUnit)) {
//			capacity=Util.multFloat(capacity, 1000000, 0);
//		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			formatRegex = "yyyy/M";
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		}

		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", plantId);
		List<Map<String, Object>> reportList = inventerStorageDataMapper.getPlantRunReportLine(map);
		List<ReportOrder> resultList = new ArrayList<>();
		if (reportList != null && !reportList.isEmpty()) {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				boolean b = true;
				for (int k = 0; k < reportList.size(); k++) {
					Map<String, Object> reportMap = reportList.get(k);
					ReportOrder reportOrder = new ReportOrder();
					String dataTime = reportMap.get("dataTime").toString();
					if (dataTime.equals(tempTime)) {
						String dataValue = Util.roundDouble(reportMap.get("energy").toString());
						reportOrder.setFormatStr(formatRegex);
						reportOrder.setCo2(Util.multDouble(dataValue, configParam.getCo2(), 1));
						reportOrder.setTree(Util.multDouble(dataValue, configParam.getTree(), 1));
						reportOrder.setCoal(Util.multDouble(dataValue, configParam.getCoal(), 1));

						// ybj 电量除于电站容量为ppr和plantRank 所以两者单位要统一 电量单位为kwh 所以容量单位必须为kh 所以在此需要转换为kw
						double capacitytemp = UnitUtil.elecUnit2KW(capacity, plantUnit);

						reportOrder.setPpr(Util.divideDouble(dataValue, capacitytemp, 1));

						reportOrder.setGen(Double.parseDouble(dataValue));
						reportOrder.setIncome(Double.parseDouble(reportMap.get("price").toString()));
						reportOrder.setPlantRank(Util.divideDouble(dataValue, capacitytemp, 1));
						reportOrder.setTime(dataTime);
						resultList.add(reportOrder);
						b = false;
						break;
					}
				}
				if (b) {
					ReportOrder reportOrder = new ReportOrder();
					reportOrder.setFormatStr(formatRegex);
					reportOrder.setCo2(-999999.99999);
					reportOrder.setTree(-999999.99999);
					reportOrder.setCoal(-999999.99999);
					reportOrder.setPpr(-999999.99999);
					reportOrder.setGen(-999999.99999);
					reportOrder.setIncome(-999999.99999);
					reportOrder.setPlantRank(-999999.99999);
					reportOrder.setTime(tempTime);
					resultList.add(reportOrder);
				}
			}

		} else {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				ReportOrder reportOrder = new ReportOrder();
				reportOrder.setFormatStr(formatRegex);
				reportOrder.setCo2(-999999.99999);
				reportOrder.setTree(-999999.99999);
				reportOrder.setCoal(-999999.99999);
				reportOrder.setPpr(-999999.99999);
				reportOrder.setGen(-999999.99999);
				reportOrder.setIncome(-999999.99999);
				reportOrder.setPlantRank(-999999.99999);
				reportOrder.setTime(tempTime);
				resultList.add(reportOrder);
			}
		}
		if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
			ListUtils.sort(resultList, sortnameArr, typeArr);
		}
		int count = resultList.size();
		ReportOrder maxRepoert = new ReportOrder();
		maxRepoert.setFormatStr(formatRegex);
		maxRepoert.setTime("max");
		ReportOrder minRepoert = new ReportOrder();
		minRepoert.setFormatStr(formatRegex);
		minRepoert.setTime("min");
		ReportOrder avgRepoert = new ReportOrder();
		avgRepoert.setFormatStr(formatRegex);
		avgRepoert.setTime("avg");
		List<List<Double>> list = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty()) {
			for (int i = 0; i < 7; i++) {
				list.add(new ArrayList<>());
			}
			for (int j = 0; j < count; j++) {
				ReportOrder orderTemp = resultList.get(j);
				list.get(0).add(Double.parseDouble("--".equals(orderTemp.getGen()) ? "0" : orderTemp.getGen()));
				list.get(1).add(Double.parseDouble("--".equals(orderTemp.getIncome()) ? "0" : orderTemp.getIncome()));
				list.get(2).add(Double.parseDouble("--".equals(orderTemp.getPpr()) ? "0" : orderTemp.getPpr()));
				list.get(3).add(
						Double.parseDouble("--".equals(orderTemp.getPlantRank()) ? "0" : orderTemp.getPlantRank()));
				list.get(4).add(Double.parseDouble("--".equals(orderTemp.getCoal()) ? "0" : orderTemp.getCoal()));
				list.get(5).add(Double.parseDouble("--".equals(orderTemp.getTree()) ? "0" : orderTemp.getTree()));
				list.get(6).add(Double.parseDouble("--".equals(orderTemp.getCo2()) ? "0" : orderTemp.getCo2()));

			}
			List<DoubleSummaryStatistics> orderUtil = new ArrayList<>();
			for (int i = 0; i < 7; i++) {
				DoubleSummaryStatistics stats = list.get(i).stream().mapToDouble((x) -> x).summaryStatistics();
				orderUtil.add(stats);
			}
			/** 最大值 */
			maxRepoert.setGen(orderUtil.get(0).getMax());
			maxRepoert.setIncome(orderUtil.get(1).getMax());
			maxRepoert.setPpr(orderUtil.get(2).getMax());
			maxRepoert.setPlantRank(orderUtil.get(3).getMax());
			maxRepoert.setCoal(orderUtil.get(4).getMax());
			maxRepoert.setTree(orderUtil.get(5).getMax());
			maxRepoert.setCo2(orderUtil.get(6).getMax());
			/** 最小值 */
			minRepoert.setGen(orderUtil.get(0).getMin());
			minRepoert.setIncome(orderUtil.get(1).getMin());
			minRepoert.setPpr(orderUtil.get(2).getMin());
			minRepoert.setPlantRank(orderUtil.get(3).getMin());
			minRepoert.setCoal(orderUtil.get(4).getMin());
			minRepoert.setTree(orderUtil.get(5).getMin());
			minRepoert.setCo2(orderUtil.get(6).getMin());
			/** 平均值 */
			avgRepoert.setGen(Util.roundDouble(orderUtil.get(0).getAverage()));
			avgRepoert.setIncome(Util.roundDouble(orderUtil.get(1).getAverage()));
			avgRepoert.setPpr(Util.roundDouble(orderUtil.get(2).getAverage()));
			avgRepoert.setPlantRank(Util.roundDouble(orderUtil.get(3).getAverage()));
			avgRepoert.setCoal(Util.roundDouble(orderUtil.get(4).getAverage()));
			avgRepoert.setTree(Util.roundDouble(orderUtil.get(5).getAverage()));
			avgRepoert.setCo2(Util.roundDouble(orderUtil.get(6).getAverage()));
		}
		resultList.add(0, avgRepoert);
		resultList.add(0, minRepoert);
		resultList.add(0, maxRepoert);
		map.clear();
		map.put("data", resultList);
		map.put("capacity", capacity + plantUnit);
		map.put("draw", draw);
		map.put("plantName", plantInfo.getPlantName());
		map.put("recordsTotal", resultList.size());
		map.put("recordsFiltered", resultList.size());
		Map<String, Object> time = new HashMap<>();
		Map<String, Object> electriNumUsed = new HashMap<>();
		Map<String, Object> electriMoneyUsed = new HashMap<>();
		Map<String, Object> electriNumOn = new HashMap<>();
		Map<String, Object> electriMoneyOn = new HashMap<>();
		Map<String, Object> electriMoneyOn2 = new HashMap<>();
		Map<String, Object> electriMoneyOn3 = new HashMap<>();
		Map<String, Object> electriMoneyOn4 = new HashMap<>();
		List<Map<String, Object>> columns = new ArrayList<>();
		time.put("data", "time");
		time.put("title", "时间");
		electriNumUsed.put("data", "gen");
		electriNumUsed.put("title", "发电量（kWh）");
		electriMoneyUsed.put("data", "income");
		electriMoneyUsed.put("title", "收益估计量（元）");
		electriNumOn.put("data", "ppr");
		electriNumOn.put("title", "等效利用小时数（h）");
		electriMoneyOn.put("data", "plantRank");
		electriMoneyOn.put("title", "单kW发电量（kWh）");
		electriMoneyOn2.put("data", "coal");
		electriMoneyOn2.put("title", "节约标准煤（kg）");
		electriMoneyOn3.put("data", "tree");
		electriMoneyOn3.put("title", "等效植树（棵）");
		electriMoneyOn4.put("data", "co2");
		electriMoneyOn4.put("title", "co2减排（kg）");
		columns.add(time);
		columns.add(electriNumUsed);
		columns.add(electriMoneyUsed);
		columns.add(electriNumOn);
		columns.add(electriMoneyOn);
		columns.add(electriMoneyOn2);
		columns.add(electriMoneyOn3);
		columns.add(electriMoneyOn4);
		map.put("columns", columns);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	/**
	 * @Title: getAreaDistribution
	 * @Description: type 1地图上钻 2地图下钻 serviceType 是否加载电站信息 1只展示区域 2 区域及电站
	 * @return: MessageBean
	 * @lastEditor: SP0007
	 * @lastEdit: 2017年12月11日下午4:51:19
	 */
	public MessageBean getAreaDistribution(Map<String, Object> map, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/**
		 * 解析前端的必要参数
		 */
		String areaId = String.valueOf(map.get("areaId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String serviceType = String.valueOf(map.get("serviceType"));
		String typeCopy = String.valueOf(map.get("type"));
		String plantType = String.valueOf(map.get("plantType"));

		// type:1上钻 2 下钻
		int type = 2;
		if (!"".equals(typeCopy) && !"null".equals(typeCopy)) {
			type = Integer.parseInt(typeCopy);
		}
		if (areaId == null || "null".equals(areaId) || "".equals(areaId)) {
			areaId = configParam.getLocation();
		}
		SysAddress address = addressMapper.getAreaByName(areaId);

		/** 获取当前登录用户所有的可见电站列表 */
		Map<String, Object> resultMap = new HashMap<>();
		List<Integer> plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);

		if (plantList != null && !plantList.isEmpty()) {

			if (Util.isNotBlank(plantType)) {
				// 过滤出需要的类型
				ArrayList<Integer> tPlantIdList = new ArrayList<>();
				List<String> pList = SystemCache.plantTypeMap.get(plantType);
				if (Util.isNotBlank(plantList)) {
					pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
					plantList.retainAll(tPlantIdList);
				}

				List<Integer> tempList = new ArrayList<>();
				for (Integer plantId : plantList) {
					String tempType = SystemCache.plantTypeList.get(String.valueOf(plantId));
					if (tempType.equals(plantType)) {
						tempList.add(plantId);
					}

				}
				plantList = tempList;
			}
			List<SysAddress> addressList = addressMapper.getAdrForScreen(plantList);
			if (type == 1) {

				/** 加载当前节点的所有同父节点 */
				resultMap.put("kidsArea", getRegionTree(addressList, address.getFatherName(), plantList));

				/** 加载当前节点的父节点 */
				SysAddress fatherAddress = addressMapper.getAreaByName(address.getFatherName());
				RegionNode regionNode = new RegionNode();
				regionNode.setName(fatherAddress.getAreaName());
				regionNode.setId(fatherAddress.getName());
				regionNode.setLocation(new String[] { fatherAddress.getLocationY(), fatherAddress.getLocationX() });
				regionNode.setFatherId(fatherAddress.getFatherName());
				map.clear();
				map.put("name", regionNode.getId());
				map.put("plants", plantList);
				List<PlantInfo> tempCount = addressMapper.getPlantsByArea(map);
				regionNode.setCount(tempCount.size());
				resultMap.put("areaInfo", regionNode);

			} else if (type == 2) {

				/** 加载当前节点的所有子节点 */
				resultMap.put("kidsArea", getRegionTree(addressList, address.getName(), plantList));

				/** 加载当前节点 */
				RegionNode regionNode = new RegionNode();
				regionNode.setName(address.getAreaName());
				regionNode.setId(address.getName());
				regionNode.setLocation(new String[] { address.getLocationY(), address.getLocationX() });
				regionNode.setFatherId(address.getFatherName());
				map.clear();
				map.put("name", regionNode.getId());
				map.put("plants", plantList);
				List<PlantInfo> tempCount = addressMapper.getPlantsByArea(map);
				regionNode.setCount(tempCount.size());
				resultMap.put("areaInfo", regionNode);
			}

			/** 加载区域的电站集合 */
			if (serviceType != null && !"null".equals(serviceType) && "2".equals(serviceType)) {

				/** 电站选择集合 */
				RegionNode nowNode = (RegionNode) resultMap.get("areaInfo");
				map.clear();
				map.put("name", nowNode.getId());
				map.put("plants", plantList);
				List<PlantInfo> list = addressMapper.getPlantsByArea(map);
				List<Map<String, Object>> resultList = new ArrayList<>();
				if (list != null && !plantList.isEmpty()) {
					for (PlantInfo plantInfo : list) {
						Map<String, Object> tempMap = new HashMap<>();
						tempMap.put("id", plantInfo.getId());
						tempMap.put("name", plantInfo.getPlantName());
						String[] location = plantInfo.getLoaction().split(",");
						tempMap.put("location", new String[] { location[1], location[0] });
						tempMap.put("status", ServiceUtil.getPlantStatus(String.valueOf(plantInfo.getId())));
						resultList.add(tempMap);
					}
				}
				resultMap.put("plantsInfo", resultList);
			}
		} else {
			resultMap.put("areaInfo", null);
			resultMap.put("kidsArea", null);
			resultMap.put("plantsInfo", null);
		}
		msg.setBody(resultMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public List<Integer> getPlantListByArea(Map<String, Object> map, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		String areaId = String.valueOf(map.get("areaId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String serviceType = String.valueOf(map.get("serviceType"));
		String typeCopy = String.valueOf(map.get("type"));
		// type:1上钻 2 下钻
		int type = 2;
		if (!"".equals(typeCopy) && !"null".equals(typeCopy)) {
			type = Integer.parseInt(typeCopy);
		}
		if (areaId == null || "null".equals(areaId) || "".equals(areaId)) {
			areaId = configParam.getLocation();
		}
		/** 电站选择集合 */
		SysAddress address = addressMapper.getAreaByName(areaId);
		map.clear();
		if (type == 1) {
			SysAddress fatherAddress = addressMapper.getAreaByName(address.getFatherName());
			map.put("name", fatherAddress.getName());
		} else if (type == 2) {
			map.put("name", address.getName());
		}
		List<Integer> resultList = null;
		List<Integer> plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);
		map.put("plants", plantList);
		List<PlantInfo> list = addressMapper.getPlantsByArea(map);
		if (list != null && !plantList.isEmpty()) {
			resultList = new ArrayList<>();
			for (PlantInfo plantInfo : list) {
				resultList.add(plantInfo.getId());
			}
		}
		return resultList;
	}

	public List<RegionNode> getRegionTree(List<SysAddress> list, String fatherName, List<Integer> plantList) {
		List<RegionNode> childList = new ArrayList<>();
		Map<String, Integer> map = new HashMap<>(10);
		for (int i = 0; i < list.size(); i++) {
			SysAddress adrTemp = list.get(i);
			String fatherKey = adrTemp.getFatherName();
			String key = adrTemp.getLocationX() + adrTemp.getLocationY() + adrTemp.getLevel();
			if (fatherName.equals(fatherKey)) {
				if (!map.containsKey(key)) {
					RegionNode tempNode = new RegionNode();
					tempNode.setId(adrTemp.getName());
					tempNode.setName(adrTemp.getAreaName());
					tempNode.setType(0);
					tempNode.setFatherId(adrTemp.getFatherName());
					boolean b = false;
					for (int j = 0; j < list.size(); j++) {
						if (list.get(j).getFatherName().equals(adrTemp.getName())) {
							b = true;
							break;
						}
					}
					tempNode.setHasChild(b);
					tempNode.setLocation(new String[] { adrTemp.getLocationY(), adrTemp.getLocationX() });
					Map<String, Object> paramMap = new HashMap<>(2);
					paramMap.put("name", tempNode.getId());
					paramMap.put("plants", plantList);
					List<PlantInfo> tempCount = addressMapper.getPlantsByArea(paramMap);
					tempNode.setCount(tempCount.size());
					childList.add(tempNode);
					map.put(key, 1);
				}
			}
		}
		return childList;
	}

	@Override
	@Transactional(rollbackFor = ServiceException.class)
	public MessageBean saveUserSet(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String plant = String.valueOf(map.get("plants"));
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		String uId = u.getId();
		if (plant != null && !"null".equals(plant) && !"".equals(plant)) {
			String[] plants = StringUtils.split(plant, ",");
			if (plants != null && plants.length > 0) {

				/** 清空用户的历史个性化设置 */
				map.clear();
				map.put("uId", uId);
				map.put("type", 3);
				personalSetMapper.deleteUserRecord(map);

				/** 保存用户的报表个性化设置 */
				map.put("obejectIds", plants);
				map.put("createTime", System.currentTimeMillis());
				int status = personalSetMapper.saveUserRecord(map);
				if (status > 0) {
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg(Msg.REPORT_SET_FAILED);
				}
			}
		} else {
			/** 用户只清空设置不保存新的设置 */
			map.clear();
			map.put("uId", uId);
			map.put("type", 3);
			personalSetMapper.deleteUserRecord(map);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.REPORT_SET_SUCCESS);
		}
		return msg;
	}

	@Override
	public MessageBean runReportsLine(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		String[] name = new String[] { "发电量", "单KW发电量", "收益" };
		String[] unit = new String[] { "kWh", "kWh", "元" };
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String draw = String.valueOf(map.get("draw"));
		String plants = String.valueOf(map.get("areaId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String dimension = String.valueOf(map.get("dimension"));
		String range = String.valueOf(map.get("range"));
		List<String> xData = new ArrayList();
		List<Map<String, Object>> yDaya = new ArrayList(3);
		List<Integer> plantList = null;
		if (StringUtil.isBlank(plants)) {
			map.clear();
			map.put("belongId", session.getAttribute(tokenId).getId());
			map.put("type", 3);
			List<String> objectList = personalSetMapper.getObjectsByBelong(map);
			if (objectList == null || objectList.isEmpty()) {
				plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);
			} else {
				plantList = new ArrayList<Integer>(objectList.size());
				for (String str : objectList) {
					plantList.add(Integer.parseInt(str));
				}
			}
		} else {
			String[] plant = StringUtils.split(plants, ",");
			plantList = new ArrayList<>(plant.length);
			for (String string : plant) {
				plantList.add(Integer.parseInt(string));
			}
		}
		if (plantList != null && !plantList.isEmpty()) {
			map.put("plants", plantList);

			String formatCondit = null;
			if (range == null || range == "null" || StringUtils.isBlank(range)) {
				range = Util.timeFormate(Util.getBeforDayMax(1), "yyyy/M/d");
			}
			switch (dimension) {
			case "day":
				formatCondit = "%Y/%c/%e";
				break;
			case "month":
				formatCondit = "%Y/%c";
				break;
			case "year":
				formatCondit = "%Y";
				break;
			default:
				formatCondit = "%Y/%c/%e";
				break;
			}
			map.put("formatCondit", formatCondit);
			map.put("range", range);
			List<Map<String, Object>> reportList = inventerStorageDataMapper.getPlantRunReports(map);
			List<ReportOrder> resultList = new ArrayList<>();
			List<PlantInfo> plantsList = plantInfoMapper.getPlantByIds(plantList);
			List<ReportOrder> orderList = new ArrayList<>();
			for (int k = 0; k < plantsList.size(); k++) {
				PlantInfo plant = plantsList.get(k);
				ReportOrder reportOrder = new ReportOrder();
				// PlantInfo plantInfo=plantInfoMapper.getPlantInfo(plantId);
				double capacityCopy = plant.getCapacity();
				String plantUnit = plant.getUnit();
				if ("MW".equalsIgnoreCase(plantUnit)) {
					capacityCopy = Util.multFloat(capacityCopy, 1000, 0);
				} else if ("GW".equalsIgnoreCase(plantUnit)) {
					capacityCopy = Util.multFloat(capacityCopy, 1000000, 0);
				}
				reportOrder.setCapacity(capacityCopy);
				reportOrder.setId(plant.getId());
				reportOrder.setName(plant.getPlantName());
				orderList.add(reportOrder);
			}
			if (orderList != null && !orderList.isEmpty()) {
				ListUtils.sort(orderList, new String[] { "capacity" }, new boolean[] { false });
			}

			for (int j = 0; j < orderList.size(); j++) {
				xData.add(orderList.get(j).getName());
			}
			if (orderList != null && !orderList.isEmpty()) {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					switch (i) {
					case 0:
						for (int j = 0; j < orderList.size(); j++) {
							int plantId = orderList.get(j).getId();
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String tempId = reportMap.get("plantId").toString();
								String dataValue = Util.roundDouble(reportMap.get("energy").toString());
								if (tempId.equals(String.valueOf(plantId))) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;
					case 1:
						for (int j = 0; j < orderList.size(); j++) {
							int plantId = orderList.get(j).getId();
							double capacity = orderList.get(j).getCapacity();
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String tempId = reportMap.get("plantId").toString();
								String dataValue = String
										.valueOf(Util.divideDouble(reportMap.get("energy").toString(), capacity, 1));
								if (tempId.equals(String.valueOf(plantId))) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;
					case 2:
						for (int j = 0; j < orderList.size(); j++) {
							int plantId = orderList.get(j).getId();
							boolean b = true;
							for (int k = 0; k < reportList.size(); k++) {
								Map<String, Object> reportMap = reportList.get(k);
								String tempId = reportMap.get("plantId").toString();
								String dataValue = Util.roundDouble(reportMap.get("price").toString());
								if (tempId.equals(String.valueOf(plantId))) {
									value.add(dataValue);
									b = false;
									break;
								}
							}
							if (b) {
								value.add("0");
							}
						}
						break;

					default:
						break;
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}

			} else {
				for (int i = 0; i < unit.length; i++) {
					Map<String, Object> tempMap = new HashMap<>();
					tempMap.put("name", name[i]);
					tempMap.put("unit", unit[i]);
					List<String> value = new ArrayList<>();
					for (int j = 0; j < xData.size(); j++) {
						value.add("0");
					}
					tempMap.put("value", value);
					yDaya.add(tempMap);
				}
			}
		} else {
			for (int i = 0; i < unit.length; i++) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("name", name[i]);
				tempMap.put("unit", unit[i]);
				List<String> value = new ArrayList<>();
				for (int j = 0; j < xData.size(); j++) {
					value.add("0");
				}
				tempMap.put("value", value);
				yDaya.add(tempMap);
			}
		}
		map.clear();
		map.put("xData", xData.toArray());
		map.put("yData", yDaya.toArray());
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean runReportsTable(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String draw = String.valueOf(map.get("draw"));
		String plants = String.valueOf(map.get("areaId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String dimension = String.valueOf(map.get("dimension"));
		User u = session.getAttribute(tokenId);
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		String range = String.valueOf(map.get("range"));
		List<Map<String, String>> tempList = "null".equals(map.get("orderName")) ? null
				: "".equals(map.get("orderName")) ? null : (List<Map<String, String>>) map.get("orderName");
		String[] sortnameArr = null;
		boolean[] typeArr = null;
		if (tempList != null && !tempList.isEmpty()) {
			typeArr = new boolean[tempList.size()];
			sortnameArr = new String[tempList.size()];
			int count = 0;
			for (int i = 0; i < tempList.size(); i++) {
				for (String key : tempList.get(i).keySet()) {
					String value = tempList.get(i).get(key);
					sortnameArr[count] = key;
					if ("desc".equalsIgnoreCase(value)) {
						typeArr[count] = false;
						count++;
					} else {
						typeArr[count] = true;
						count++;
					}
				}
			}
		}
		List<ReportOrder> resultList = new ArrayList<>();
		List<String> xData = new ArrayList();
		List<Map<String, Object>> yDaya = new ArrayList(3);
		/** 用户是否存在个性化设置 */
		List<Integer> plantList = null;
		if (StringUtil.isBlank(plants)) {
			map.clear();
			map.put("belongId", session.getAttribute(tokenId).getId());
			map.put("type", 3);
			List<String> objectList = personalSetMapper.getObjectsByBelong(map);
			if (objectList == null || objectList.isEmpty()) {
				plantList = plantInfoService.getPlantsForUser(tokenId, session, 1);
			} else {
				plantList = new ArrayList<Integer>(objectList.size());
				for (String str : objectList) {
					plantList.add(Integer.parseInt(str));
				}
			}
		} else {
			String[] plant = StringUtils.split(plants, ",");
			plantList = new ArrayList<Integer>(plant.length);
			for (int i = 0; i < plant.length; i++) {
				plantList.add(Integer.parseInt(plant[i]));
			}
		}
		if (plantList != null && !plantList.isEmpty()) {
			map.put("plants", plantList);

			String formatCondit = null;
			if (range == null || range == "null" || StringUtils.isBlank(range)) {
				range = Util.timeFormate(Util.getBeforDayMax(1), "yyyy/M/d");
			}
			switch (dimension) {
			case "day":
				formatCondit = "%Y/%c/%e";
				break;
			case "month":
				formatCondit = "%Y/%c";
				break;
			case "year":
				formatCondit = "%Y";
				break;
			default:
				formatCondit = "%Y/%c/%e";
				break;
			}
			map.put("formatCondit", formatCondit);
			map.put("range", range);
			List<Map<String, Object>> reportList = inventerStorageDataMapper.getPlantRunReports(map);
			List<PlantInfo> plantsList = plantInfoMapper.getPlantByIds(plantList);
			if (reportList != null && !reportList.isEmpty()) {
				for (int j = 0; j < plantsList.size(); j++) {
					PlantInfo plant = plantsList.get(j);
					String plantId = String.valueOf(plant.getId());
					Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;
					int inventerNum = 0;
					Map<Integer, List<Integer>> plantDevice = deviceOfPlant.containsKey(plant.getId())
							? deviceOfPlant.get(plant.getId())
							: null;
					if (plantDevice != null) {
						inventerNum = plantDevice.containsKey(1) ? plantDevice.get(1).size() : 0;
						inventerNum += plantDevice.containsKey(2) ? plantDevice.get(2).size() : 0;
					}
					// double capacity=plant.getCapacity();
					// PlantInfo plantInfo=plantInfoMapper.getPlantInfo(plantId);
					double capacity = plant.getCapacity();
					String plantUnit = plant.getUnit();
					if ("MW".equalsIgnoreCase(plantUnit)) {
						capacity = Util.multFloat(capacity, 1000, 0);
					} else if ("GW".equalsIgnoreCase(plantUnit)) {
						capacity = Util.multFloat(capacity, 1000000, 0);
					}
					boolean b = true;
					for (int k = 0; k < reportList.size(); k++) {
						Map<String, Object> reportMap = reportList.get(k);
						ReportOrder reportOrder = new ReportOrder();
						String id = reportMap.get("plantId").toString();
						if (plantId.equals(id)) {
							String dataValue = Util.roundDouble(reportMap.get("energy").toString());
							reportOrder.setCo2(Util.multDouble(dataValue, configParam.getCo2(), 1));
							reportOrder.setTree(Util.multDouble(dataValue, configParam.getTree(), 1));
							reportOrder.setCoal(Util.multDouble(dataValue, configParam.getCoal(), 1));
							reportOrder.setPpr(Util.divideDouble(dataValue, capacity, 1));
							reportOrder.setGen(Double.parseDouble(dataValue));
							reportOrder.setIncome(Double.parseDouble(reportMap.get("price").toString()));
							reportOrder.setPlantRank(Util.divideDouble(dataValue, capacity, 1));
							reportOrder.setName(plant.getPlantName());
							reportOrder.setCapacity(capacity);
							reportOrder.setInventerNum(inventerNum);
							reportOrder.setFormatStr("yyyy-MM-dd");
							resultList.add(reportOrder);
							b = false;
							break;
						}
					}
					if (b) {
						ReportOrder reportOrder = new ReportOrder();
						reportOrder.setCo2(-999999.99999);
						reportOrder.setTree(-999999.99999);
						reportOrder.setCoal(-999999.99999);
						reportOrder.setPpr(-999999.99999);
						reportOrder.setGen(-999999.99999);
						reportOrder.setIncome(-999999.99999);
						reportOrder.setPlantRank(-999999.99999);
						reportOrder.setName(plant.getPlantName());
						reportOrder.setCapacity(capacity);
						reportOrder.setInventerNum(inventerNum);
						reportOrder.setFormatStr("yyyy-MM-dd");
						resultList.add(reportOrder);
					}
				}

			} else {
				for (int j = 0; j < plantsList.size(); j++) {
					PlantInfo plant = plantsList.get(j);
					String plantId = String.valueOf(plant.getId());
					Map<Integer, Map<Integer, List<Integer>>> deviceOfPlant = SystemCache.deviceOfPlant;
					int inventerNum = 0;
					Map<Integer, List<Integer>> plantDevice = deviceOfPlant.containsKey(plant.getId())
							? deviceOfPlant.get(plant.getId())
							: null;
					if (plantDevice != null) {
						inventerNum = plantDevice.containsKey(1) ? plantDevice.get(1).size() : 0;
						inventerNum += plantDevice.containsKey(2) ? plantDevice.get(2).size() : 0;
					}
					double capacity = plant.getCapacity();
					String plantUnit = plant.getUnit();
					if ("MW".equalsIgnoreCase(plantUnit)) {
						capacity = Util.multFloat(capacity, 1000, 0);
					} else if ("GW".equalsIgnoreCase(plantUnit)) {
						capacity = Util.multFloat(capacity, 1000000, 0);
					}
					ReportOrder reportOrder = new ReportOrder();
					reportOrder.setCo2(-999999.99999);
					reportOrder.setTree(-999999.99999);
					reportOrder.setCoal(-999999.99999);
					reportOrder.setPpr(-999999.99999);
					reportOrder.setGen(-999999.99999);
					reportOrder.setIncome(-999999.99999);
					reportOrder.setPlantRank(-999999.99999);
					reportOrder.setName(plant.getPlantName());
					reportOrder.setCapacity(capacity);
					reportOrder.setInventerNum(inventerNum);
					reportOrder.setFormatStr("yyyy-MM-dd");
					resultList.add(reportOrder);
				}
			}
		}
		/**
		 * 求最大值最小值平均值
		 */
		int count = resultList.size();
		ReportOrder maxRepoert = new ReportOrder();
		maxRepoert.setName("最大值");
		maxRepoert.setFormatStr("yyyy-MM-dd");
		ReportOrder minRepoert = new ReportOrder();
		minRepoert.setName("最小值");
		minRepoert.setFormatStr("yyyy-MM-dd");
		ReportOrder avgRepoert = new ReportOrder();
		avgRepoert.setName("平均值");
		avgRepoert.setFormatStr("yyyy-MM-dd");
		List<List<Double>> list = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
			ListUtils.sort(resultList, sortnameArr, typeArr);
		}
		if (resultList != null && !resultList.isEmpty()) {
			for (int i = 0; i < 9; i++) {
				list.add(new ArrayList<>());
			}
			for (int j = 0; j < count; j++) {
				ReportOrder orderTemp = resultList.get(j);
				list.get(0).add(orderTemp.getCapacity());
				list.get(1).add(orderTemp.getInventerNum() * 1.0);
				list.get(2).add(Double.parseDouble("--".equals(orderTemp.getGen()) ? "0" : orderTemp.getGen()));
				list.get(3).add(Double.parseDouble("--".equals(orderTemp.getIncome()) ? "0" : orderTemp.getIncome()));
				list.get(4).add(Double.parseDouble("--".equals(orderTemp.getPpr()) ? "0" : orderTemp.getPpr()));
				list.get(5).add(
						Double.parseDouble("--".equals(orderTemp.getPlantRank()) ? "0" : orderTemp.getPlantRank()));
				list.get(6).add(Double.parseDouble("--".equals(orderTemp.getCoal()) ? "0" : orderTemp.getCoal()));
				list.get(7).add(Double.parseDouble("--".equals(orderTemp.getTree()) ? "0" : orderTemp.getTree()));
				list.get(8).add(Double.parseDouble("--".equals(orderTemp.getCo2()) ? "0" : orderTemp.getCo2()));

			}
			List<DoubleSummaryStatistics> orderUtil = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				DoubleSummaryStatistics stats = list.get(i).stream().mapToDouble((x) -> x).summaryStatistics();
				orderUtil.add(stats);
			}
			/** 最大值 */
			maxRepoert.setCapacity(orderUtil.get(0).getMax());
			maxRepoert.setInventerNum((int) orderUtil.get(1).getMax());
			maxRepoert.setGen(orderUtil.get(2).getMax());
			maxRepoert.setIncome(orderUtil.get(3).getMax());
			maxRepoert.setPpr(orderUtil.get(4).getMax());
			maxRepoert.setPlantRank(orderUtil.get(5).getMax());
			maxRepoert.setCoal(orderUtil.get(6).getMax());
			maxRepoert.setTree(orderUtil.get(7).getMax());
			maxRepoert.setCo2(orderUtil.get(8).getMax());
			/** 最小值 */
			minRepoert.setCapacity(orderUtil.get(0).getMin());
			minRepoert.setInventerNum((int) orderUtil.get(1).getMin());
			minRepoert.setGen(orderUtil.get(2).getMin());
			minRepoert.setIncome(orderUtil.get(3).getMin());
			minRepoert.setPpr(orderUtil.get(4).getMin());
			minRepoert.setPlantRank(orderUtil.get(5).getMin());
			minRepoert.setCoal(orderUtil.get(6).getMin());
			minRepoert.setTree(orderUtil.get(7).getMin());
			minRepoert.setCo2(orderUtil.get(8).getMin());
			/** 平均值 */
			avgRepoert.setCapacity(Util.roundDouble(orderUtil.get(0).getAverage()));
			avgRepoert.setInventerNum((int) orderUtil.get(1).getAverage());
			avgRepoert.setGen(Util.roundDouble(orderUtil.get(2).getAverage()));
			avgRepoert.setIncome(Util.roundDouble(orderUtil.get(3).getAverage()));
			avgRepoert.setPpr(Util.roundDouble(orderUtil.get(4).getAverage()));
			avgRepoert.setPlantRank(Util.roundDouble(orderUtil.get(5).getAverage()));
			avgRepoert.setCoal(Util.roundDouble(orderUtil.get(6).getAverage()));
			avgRepoert.setTree(Util.roundDouble(orderUtil.get(7).getAverage()));
			avgRepoert.setCo2(Util.roundDouble(orderUtil.get(8).getAverage()));
		}
		map.clear();
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		int maxValue = length + start;
		if (start > count) {
			start = count;
		}
		if (maxValue > count) {
			maxValue = count;
		}
		if (maxValue > 0) {
			resultList = resultList.subList(start, maxValue);
		}
		resultList.add(0, avgRepoert);
		resultList.add(0, minRepoert);
		resultList.add(0, maxRepoert);
		map.put("data", resultList);
		map.put("areaName", "中国");
		map.put("orgName", ((SysOrg) u.getUserOrg()).getOrgName());
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Transactional(rollbackFor = ServiceException.class)
	@Override
	public MessageBean savePersonalSet(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		String inventers = String.valueOf(map.get("inventers"));
		String plantId = String.valueOf(map.get("plantId"));
		String fieldLabel = String.valueOf(map.get("guids"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		String areaNames = String.valueOf(map.get("areaNames"));
		User u = session.getAttribute(tokenId);
		String uId = u.getId();
		String[] guidList = "null".equals(fieldLabel) ? null
				: "".equals(fieldLabel) ? null : StringUtils.split(fieldLabel, ",");
		String[] inventerList = "null".equals(inventers) ? null
				: "".equals(inventers) ? null : StringUtils.split(inventers, ",");
		// 清空用户历史设置
		map.clear();
		map.put("belongId", uId);
		map.put("list", new int[] { 4, 5, 6, 7 });
		personalSetMapper.deleteHistoryReportSet(map);
		if (!"".equals(plantId) && !"null".equals(plantId)) {
			long createTime = System.currentTimeMillis();
			map.clear();
			map.put("uId", uId);
			map.put("obejectIds", new String[] { plantId });
			map.put("type", 4);
			map.put("createTime", createTime);
			// 存电站
			personalSetMapper.saveUserRecord(map);
			// 存设备
			if (inventerList != null && inventerList.length > 0) {
				map.put("obejectIds", inventerList);
				map.put("type", 5);
				personalSetMapper.saveUserRecord(map);
				// 存信号点
				if (guidList != null && guidList.length > 0) {
					map.put("obejectIds", guidList);
					map.put("type", 6);
					personalSetMapper.saveUserRecord(map);
				} else {
					throw new ServiceException("请完善设置选项!");
				}
			} else {
				throw new ServiceException("请完善设置选项!");
			}
			// 保存时间
			if (!"null".equals(startTime) && !"null".equals(endTime) && !"".equals(startTime) && !"".equals(endTime)) {
				map.put("obejectIds", new String[] { startTime, endTime, areaNames });
				map.put("type", 7);
				personalSetMapper.saveUserRecord(map);
			} else {
				throw new ServiceException("请完善设置选项!");
			}
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.ALARM_SET_SUCCESS);
			logger.info(" 用户:" + uId + "_" + u.getUserName() + " 保存历史报表设置成功。");
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.ALARM_SET_FAILED);
			logger.warn(" 用户:" + uId + "_" + u.getUserName() + " 保存历史报表设置失败。");
		}
		return msg;
	}

	@Override
	public MessageBean getDeviceForPlant(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String plantd = String.valueOf(map.get("plantId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		String uID = session.getAttribute(tokenId).getId();
		if ("null".equals(plantd) || "".equals(plantd)) {

			/** 如果传入参数为空则查找用户是否有历史设置 */
			map.clear();
			map.put("belongId", uID);
			map.put("type", 4);
			List<String> chooseList = personalSetMapper.getObjectsByBelong(map);
			if (chooseList != null && !chooseList.isEmpty()) {
				plantd = chooseList.get(0);
			}
		}
		if (!"null".equals(plantd) && !"".equals(plantd)) {
			List<CollDevice> deviceList = deviceMapper.getDeviceListByPid(plantd);
			map.put("type", 5);
			List<String> chooseList = personalSetMapper.getObjectsByBelong(map);
			if (deviceList != null && !deviceList.isEmpty()) {
				for (CollDevice device : deviceList) {
					if (device.getDeviceType() == 1 || device.getDeviceType() == 2
							|| device.getDeviceType() == DTC.ENV_MONITOR) {
						Map<String, String> tempMap = new HashMap<String, String>();
						tempMap.put("id", String.valueOf(device.getId()));
						tempMap.put("name", device.getDeviceName());
						if (chooseList != null && !chooseList.isEmpty()) {
							boolean b = true;
							for (String id : chooseList) {
								if (Integer.parseInt(id) == device.getId()) {
									b = false;
									tempMap.put("checked", String.valueOf(true));
									break;
								}
								if (b) {
									tempMap.put("checked", String.valueOf(false));
								}
							}
						}
						resultList.add(tempMap);
					}
				}
			}
		}
		msg.setBody(resultList.toArray());
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getYXGuidForInventers(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		String inventers = String.valueOf(map.get("inventers"));
		String[] inventerList = "null".equals(inventers) ? null
				: "".equals(inventers) ? null : StringUtils.split(inventers, ",");
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		if (inventerList == null || inventerList.length == 0) {

			/** 如果传入参数为空则查找用户是否有历史设置 */
			map.clear();
			map.put("belongId", session.getAttribute(tokenId).getId());
			map.put("type", 5);
			List<String> temp = personalSetMapper.getObjectsByBelong(map);
			if (temp != null && !temp.isEmpty()) {
				inventerList = new String[temp.size()];
				for (int i = 0; i < temp.size(); i++) {
					inventerList[i] = temp.get(i);
				}
			}
		}
		if (inventerList != null && inventerList.length > 0) {
			List<Integer> typeList = deviceMapper.getDeviceTypeByIds(Arrays.asList(inventerList));
			List<List<CollSignalLabel>> tempList = new ArrayList();
			for (Integer integer : typeList) {
				tempList.add(signalLabelMapper.getFieldByType(String.valueOf(integer)));
			}
			map.clear();
			map.put("belongId", session.getAttribute(tokenId).getId());
			map.put("type", 6);
			List<String> choose = personalSetMapper.getObjectsByBelong(map);
			for (int i = 0; i < tempList.size(); i++) {
				List<CollSignalLabel> fieldList = tempList.get(i);
				for (int j = 0; j < fieldList.size(); j++) {
					CollSignalLabel signalLabel = fieldList.get(j);
					String field = signalLabel.getField();
					boolean b = true;
					for (int k = 0; k < resultList.size(); k++) {
						Map<String, String> temp = resultList.get(k);
						if (field.equals(String.valueOf(temp.get("field")))) {
							b = false;
							break;
						}
					}
					if (b) {
						Map<String, String> temp = new HashMap<String, String>(3);
						temp.put("field", signalLabel.getField());
						temp.put("fieldLabel", signalLabel.getFieldLabel());
						temp.put("unit", signalLabel.getUnit());
						if (choose != null && !choose.isEmpty()) {
							boolean b2 = true;
							for (String str : choose) {
								if (str.equals(signalLabel.getField())) {
									temp.put("checked", String.valueOf(true));
									b2 = false;
									break;
								}
							}
							if (b2) {
								temp.put("checked", String.valueOf(false));
							}
						} else {
							temp.put("checked", String.valueOf(false));
						}
						resultList.add(temp);
					}
				}
			}
		}
		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getHistoryLine(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		String inventers = String.valueOf(map.get("inventers"));
		String fieldLabel = String.valueOf(map.get("field"));
		String timeStarts = String.valueOf(map.get("startTime"));
		String timeEnds = String.valueOf(map.get("endTime"));
		String[] fieldLabellist = "null".equals(fieldLabel) ? null
				: "".equals(fieldLabel) ? null : StringUtils.split(fieldLabel, ",");
		String[] inventerList = "null".equals(inventers) ? null
				: "".equals(inventers) ? null : StringUtils.split(inventers, ",");
		Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
		Map<String, List<Map<String, Object>>> dataMap2 = new HashMap<String, List<Map<String, Object>>>();
		Map<Integer, String> paramSql = new HashMap<Integer, String>();
		if ("null".equals(timeStarts) || "".equals(timeStarts)) {
			timeStarts = Util.timeFormate(Util.getBeforDayMin(0), "yyyy-MM-dd HH:mm");
		}
		if ("null".equals(timeEnds) || "".equals(timeEnds)) {
			timeEnds = Util.timeFormate(Util.getBeforDayMax(0), "yyyy-MM-dd HH:mm");
		}
		String startTime = String.valueOf(Util.strFormate(timeStarts, "yyyy-MM-dd HH:mm"));
		String endTime = String.valueOf(Util.strFormate(timeEnds, "yyyy-MM-dd HH:mm"));
		if (inventerList == null) {

			/** 如果传入参数为空则查找用户是否有历史设置 */
			map.clear();
			map.put("belongId", session.getAttribute(tokenId).getId());
			map.put("type", 5);
			List<String> inventer = personalSetMapper.getObjectsByBelong(map);
			if (null != inventer && !inventer.isEmpty()) {
				inventerList = new String[inventer.size()];
				for (int i = 0; i < inventer.size(); i++) {
					inventerList[i] = inventer.get(i);
				}
				map.put("type", 6);
				List<String> field = personalSetMapper.getObjectsByBelong(map);
				if (null != field && !field.isEmpty()) {
					fieldLabellist = new String[field.size()];
					for (int i = 0; i < field.size(); i++) {
						fieldLabellist[i] = field.get(i);
					}
				}
				map.put("type", 7);
				List<String> timeList = personalSetMapper.getObjectsByBelong(map);
				timeStarts = timeList.get(0);
				endTime = timeList.get(1);
				startTime = String.valueOf(Util.strFormate(timeStarts, "yyyy-MM-dd HH:mm"));
				endTime = String.valueOf(Util.strFormate(endTime, "yyyy-MM-dd HH:mm"));
			}
		}
		if (inventerList != null && fieldLabellist != null && fieldLabellist.length > 0 && inventerList.length > 0) {
			List<Integer> typeList = deviceMapper.getDeviceTypeByIds(Arrays.asList(inventerList));
			List<List<CollSignalLabel>> tempList = new ArrayList();
			for (Integer integer : typeList) {
				map.clear();
				map.put("type", String.valueOf(integer));
				map.put("fieldList", fieldLabellist);
				tempList.add(signalLabelMapper.getFieldForHistory(map));
			}

			for (int i = 0; i < tempList.size(); i++) {
				List<CollSignalLabel> fieldList = tempList.get(i);
				CollSignalLabel signalLabelTemplate = fieldList.get(0);
				Integer deviceType = signalLabelTemplate.getDeviceType();
				StringBuffer buffer = new StringBuffer();
				for (CollSignalLabel signalLabel : fieldList) {
					buffer.append(signalLabel.getField()).append(",");
				}
				buffer = buffer.deleteCharAt(buffer.lastIndexOf(","));
				paramSql.put(deviceType, buffer.toString());
				map.put("paramSql", buffer.toString());
				map.put("startTime", startTime);
				map.put("endTime", endTime);

				switch (deviceType) {
				case DTC.CENTRAL_INVERTER:
					map.put("table", "data_central_inverter");
					break;
				case DTC.STRING_INVERTER:
					map.put("table", "data_string_inverter");
					break;
				case DTC.ENV_MONITOR:
					map.put("table", "data_env_monitor");
					break;
				default:
					break;
				}
				for (String id : inventerList) {
					Integer type = deviceMapper.getDeviceType(id);
					map.put("deviceId", id);
					dataMap.put(id, centralInverterMapper.getDataForHistory(map));
				}
			}
		}
		// 生成时间点
		List<String> xData = ServiceUtil.getStringTimeSegmentAnyDay(timeStarts, timeEnds);
		List yData = new ArrayList();
		if (!paramSql.isEmpty()) {

			for (Integer key : paramSql.keySet()) {
				if (!dataMap.isEmpty()) {
					String[] params = StringUtils.split(paramSql.get(key), ",");
					for (String param : params) {
						for (String deviceId : dataMap.keySet()) {
							List<Map<String, Object>> tempMap = dataMap.get(deviceId);
							List<Object[]> orderData = new ArrayList<Object[]>();
							for (int i = 0; i < tempMap.size(); i++) {
								Map<String, Object> result = tempMap.get(i);
								orderData.add(new Object[] { result.get("data_time"), result.get(param) });
							}
							map.clear();
							map.put("type", key);
							map.put("field", param);
							CollSignalLabel label = signalLabelMapper.getFieldLabelByField(map);
							CollDevice device = deviceMapper.getDeviceById(deviceId);
							yData.add(ServiceUtil.loadParamForTimeList(timeStarts, timeEnds, orderData,
									label.getFieldLabel(), label.getUnit(), device.getDeviceName()));
						}
					}
				}

			}
		}
		map.clear();
		map.put("xData", xData);
		map.put("yData", yData);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);

		long codeEndTime = System.currentTimeMillis();
		return msg;
	}

	@Override
	public MessageBean getHistoryTime(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		/** 解析前端参数 */
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		map.clear();
		map.put("belongId", session.getAttribute(tokenId).getId());
		map.put("type", 7);
		List<String> temp = personalSetMapper.getObjectsByBelong(map);
		map.clear();
		if (temp != null && !temp.isEmpty() && temp.size() == 3) {
			map.put("startTime", temp.get(0));
			map.put("endTime", temp.get(1));
			map.put("areaNames", temp.get(2));
		} else {
			map.put("startTime", null);
			map.put("endTime", null);
			map.put("areaNames", null);
		}
		msg.setBody(temp);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean reviewData(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		map.clear();
		map.put("belongId", session.getAttribute(tokenId).getId());
		map.put("type", 3);
		/** 用户是否存在个性化设置 */
		List<String> objectList = personalSetMapper.getObjectsByBelong(map);
		List<Map<String, String>> resultList = new ArrayList<>();
		if (objectList != null && !objectList.isEmpty()) {
			List<PlantInfo> plants = plantInfoMapper.getPlantByIds(objectList);
			for (PlantInfo plantInfo : plants) {
				Map<String, String> tempMap = new HashMap<>();
				tempMap.put("id", String.valueOf(plantInfo.getId()));
				tempMap.put("name", plantInfo.getPlantName());
				resultList.add(tempMap);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		return msg;
	}

	public static void main(String[] args) {
//		List<ReportOrder> list = new ArrayList<ReportOrder>();
//		ReportOrder reportOrder=new ReportOrder();
//		reportOrder.setCo2(12.54);
//		ReportOrder reportOrder1=new ReportOrder();
//		reportOrder1.setCo2(24.2);
//		ReportOrder reportOrder2=new ReportOrder();
//		reportOrder2.setCo2(18.214);
//		list.add(reportOrder);
//		list.add(reportOrder1);
//		list.add(reportOrder2);
//		for (ReportOrder report : list) {
//			System.out.println(report.getCo2());
//		}
//		System.out.println("---------------");
//		String []sortnameArr={"co2"};
//		boolean [] typeArr={true};
//		ListUtils.sort(list, sortnameArr, typeArr);
//		for (ReportOrder report : list) {
//			System.out.println(report.getCo2());
//		}
//		System.out.println("--------------");
//		String []sortnameArr1={"co2"};
//		boolean [] typeArr1={false};
//		ListUtils.sort(list, sortnameArr1, typeArr1);
//		for (ReportOrder report : list) {
//			System.out.println(report.getCo2());
//		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(sdf.parse("2018-03-21").getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public MessageBean storageRunReportsTable(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String draw = String.valueOf(map.get("draw"));
		// 电站
		String plantId = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		List<Map<String, String>> tempList = "null".equals(map.get("orderName")) ? null
				: "".equals(map.get("orderName")) ? null : (List<Map<String, String>>) map.get("orderName");
		String[] sortnameArr = null;
		boolean[] typeArr = null;
		String formatRegex = null;
		if (tempList != null && !tempList.isEmpty()) {
			typeArr = new boolean[tempList.size()];
			sortnameArr = new String[tempList.size()];
			int count = 0;
			for (int i = 0; i < tempList.size(); i++) {
				for (String key : tempList.get(i).keySet()) {
					String value = tempList.get(i).get(key);
					sortnameArr[count] = key;
					if ("desc".equalsIgnoreCase(value)) {
						typeArr[count] = false;
						count++;
					} else {
						typeArr[count] = true;
						count++;
					}
				}
			}
		}
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(plantId);
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000, 0);
		} else if ("GW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000000, 0);
		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			formatRegex = "yyyy/M";
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		}
		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", plantId);
		List<Map<String, Object>> reportList = energyMapper.getStoragePlantRunReport(map);
		List<StorageReportOrder> resultList = new ArrayList<>();
		if (Util.isNotBlank(reportList)) {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				boolean b = true;
				for (int k = 0; k < reportList.size(); k++) {
					Map<String, Object> reportMap = reportList.get(k);
					StorageReportOrder storageReportOrder = new StorageReportOrder();
					String dataTime = reportMap.get("dataTime").toString();
					if (dataTime.equals(tempTime)) {
						storageReportOrder.setFormatStr(formatRegex);
						// 充电量
						Double engEnergy = Double.valueOf(reportMap.get("engEnergy").toString());
						// 放电量
						Double posEnergy = Double.valueOf(reportMap.get("posEnergy").toString());
						// 转换效率 = 放电量/充电量*100%
						Double conversion = Util.multFloat(Util.divideDouble(posEnergy, engEnergy, 0), 100, 1);
						// 收益
						Double price = Double.valueOf(reportMap.get("price").toString());
						storageReportOrder.setTime(dataTime);
						storageReportOrder.setCharge(engEnergy);
						storageReportOrder.setDischarge(posEnergy);
						storageReportOrder.setIncome(price);
						storageReportOrder.setConversion(conversion);
						resultList.add(storageReportOrder);
						b = false;
						break;
					}
				}
				if (b) {
					StorageReportOrder storageReportOrder = new StorageReportOrder();
					storageReportOrder.setFormatStr(formatRegex);
					storageReportOrder.setTime(tempTime);
					storageReportOrder.setCharge(-999999.99999);
					storageReportOrder.setDischarge(-999999.99999);
					storageReportOrder.setIncome(-999999.99999);
					storageReportOrder.setConversion(-999999.99999);
					resultList.add(storageReportOrder);
				}
			}
		} else {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				StorageReportOrder storageReportOrder = new StorageReportOrder();
				storageReportOrder.setFormatStr(formatRegex);
				storageReportOrder.setTime(tempTime);
				storageReportOrder.setCharge(-999999.99999);
				storageReportOrder.setDischarge(-999999.99999);
				storageReportOrder.setIncome(-999999.99999);
				storageReportOrder.setConversion(-999999.99999);
				resultList.add(storageReportOrder);
			}
		}
		int count = resultList.size();
		StorageReportOrder maxRepoert = new StorageReportOrder();
		StorageReportOrder minRepoert = new StorageReportOrder();
		StorageReportOrder avgRepoert = new StorageReportOrder();
		maxRepoert.setFormatStr(formatRegex);
		maxRepoert.setTime("max");
		minRepoert.setFormatStr(formatRegex);
		minRepoert.setTime("min");
		avgRepoert.setFormatStr(formatRegex);
		avgRepoert.setTime("avg");
		List<List<Double>> list = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
			for (int i = 0; i < 5; i++) {
				list.add(new ArrayList<>());
			}
			for (int j = 0; j < count; j++) {
				StorageReportOrder orderTemp = resultList.get(j);
				list.get(0).add(Double.parseDouble("--".equals(orderTemp.getCharge()) ? "0" : orderTemp.getCharge()));
				list.get(1).add(
						Double.parseDouble("--".equals(orderTemp.getDischarge()) ? "0" : orderTemp.getDischarge()));
				list.get(2).add(Double.parseDouble("--".equals(orderTemp.getIncome()) ? "0" : orderTemp.getIncome()));
				list.get(3).add(Double.parseDouble(
						"-999999.99999".equals(orderTemp.conversion + "") ? "0" : orderTemp.conversion + ""));
			}
			List<DoubleSummaryStatistics> orderUtil = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				DoubleSummaryStatistics stats = list.get(i).stream().mapToDouble((x) -> x).summaryStatistics();
				orderUtil.add(stats);
			}
			/** 最大值 */
			maxRepoert.setCharge(orderUtil.get(0).getMax());
			maxRepoert.setDischarge(orderUtil.get(1).getMax());
			maxRepoert.setIncome(orderUtil.get(2).getMax());
			maxRepoert.setConversion(orderUtil.get(3).getMax());
			/** 最小值 */
			minRepoert.setCharge(orderUtil.get(0).getMin());
			minRepoert.setDischarge(orderUtil.get(1).getMin());
			minRepoert.setIncome(orderUtil.get(2).getMin());
			minRepoert.setConversion(orderUtil.get(3).getMin());
			/** 平均值 */
			avgRepoert.setCharge(Util.roundDouble(orderUtil.get(0).getAverage()));
			avgRepoert.setDischarge(Util.roundDouble(orderUtil.get(1).getAverage()));
			avgRepoert.setIncome(Util.roundDouble(orderUtil.get(2).getAverage()));
			avgRepoert.setConversion(Util.roundDouble(orderUtil.get(3).getAverage()));
		}

		resultList.add(0, avgRepoert);
		resultList.add(0, minRepoert);
		resultList.add(0, maxRepoert);
		map.clear();
		map.put("data", resultList);
		map.put("capacity", capacity + plantInfo.getUnit());
		map.put("draw", draw);
		map.put("plantName", plantInfo.getPlantName());
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		Map<String, Object> time = new HashMap<>();
		Map<String, Object> electriNumUsed = new HashMap<>();
		Map<String, Object> electriMoneyUsed = new HashMap<>();
		Map<String, Object> electriNumOn = new HashMap<>();
		Map<String, Object> electriMoneyOn = new HashMap<>();
		List<Map<String, Object>> columns = new ArrayList<>();
		time.put("data", "time");
		time.put("title", "时间");
		electriNumUsed.put("data", "charge");
		electriNumUsed.put("title", "充电量（度）");
		electriMoneyUsed.put("data", "discharge");
		electriMoneyUsed.put("title", "放电量（度）");
		electriNumOn.put("data", "income");
		electriNumOn.put("title", "收益（元）");
		electriMoneyOn.put("data", "conversion");
		electriMoneyOn.put("title", "转换效率");
		columns.add(time);
		columns.add(electriNumUsed);
		columns.add(electriMoneyUsed);
		columns.add(electriNumOn);
		columns.add(electriMoneyOn);
		map.put("columns", columns);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean electricReportLine(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 电站
		String id = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(id);
		// 电站类型id
		Integer typeId = plantInfo.getPlantTypeA();
		// 电站容量换算
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000, 0);
		} else if ("GW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000000, 0);
		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		List<Map<String, Object>> yDaya = new ArrayList(6);
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		}

		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", id);
		List<Map<String, Object>> reportList = energyMapper.getElectriRunReportLine(map);
		String[] name = new String[] { "用电量", "用电电费", "上网电量", "上网费用", "下网电量", "下网费用" };
		String[] unit = new String[] { "度", "元", "度", "元", "度", "元" };
		if (reportList != null && !reportList.isEmpty()) {
			for (int i = 0; i < unit.length; i++) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("name", name[i]);
				tempMap.put("unit", unit[i]);
				List<String> value = new ArrayList<>();
				switch (i) {
				case 0:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							// String dataValue=Util.roundDouble(reportMap.get("energy").toString());
							String dataValue = "0";
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;
				case 1:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							// String
							// dataValue=String.valueOf(Util.divideDouble(reportMap.get("energy").toString(),
							// capacity, 1));
							String dataValue = "0";
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;
				case 2:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							String dataValue;
							if (reportMap.get("upEnergy") == null) {
								dataValue = "0";
							} else {
								dataValue = Util.roundDouble(reportMap.get("upEnergy").toString());
							}
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;
				case 3:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							String dataValue;
							if (reportMap.get("upPrice") == null) {
								dataValue = "0";
							} else {
								dataValue = Util.roundDouble(reportMap.get("upPrice").toString());
							}
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;
				case 4:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							String dataValue;
							if (reportMap.get("downEnergy") == null) {
								dataValue = "0";
							} else {
								dataValue = Util.roundDouble(reportMap.get("downEnergy").toString());
							}
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;
				case 5:
					for (int j = 0; j < xData.size(); j++) {
						String tempTime = xData.get(j);
						boolean b = true;
						for (int k = 0; k < reportList.size(); k++) {
							Map<String, Object> reportMap = reportList.get(k);
							String dataTime = reportMap.get("dataTime").toString();
							String dataValue;
							if (reportMap.get("downPrice") == null) {
								dataValue = "0";
							} else {
								dataValue = Util.roundDouble(reportMap.get("downPrice").toString());
							}
							if (dataTime.equals(tempTime)) {
								value.add(dataValue);
								b = false;
								break;
							}
						}
						if (b) {
							value.add("0");
						}
					}
					break;

				default:
					break;
				}
				tempMap.put("value", value);
				yDaya.add(tempMap);
			}

		} else {
			for (int i = 0; i < unit.length; i++) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("name", name[i]);
				tempMap.put("unit", unit[i]);
				List<String> value = new ArrayList<>();
				for (int j = 0; j < xData.size(); j++) {
					value.add("0");
				}
				tempMap.put("value", value);
				yDaya.add(tempMap);
			}
		}
		map.clear();
		map.put("xData", xData.toArray());
		map.put("yData", yDaya.toArray());
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean electricReportTable(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String draw = String.valueOf(map.get("draw"));
		// 电站
		String plantId = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		List<Map<String, String>> tempList = "null".equals(map.get("orderName")) ? null
				: "".equals(map.get("orderName")) ? null : (List<Map<String, String>>) map.get("orderName");
		String[] sortnameArr = null;
		boolean[] typeArr = null;
		String formatRegex = null;
		if (tempList != null && !tempList.isEmpty()) {
			typeArr = new boolean[tempList.size()];
			sortnameArr = new String[tempList.size()];
			int count = 0;
			for (int i = 0; i < tempList.size(); i++) {
				for (String key : tempList.get(i).keySet()) {
					String value = tempList.get(i).get(key);
					sortnameArr[count] = key;
					if ("desc".equalsIgnoreCase(value)) {
						typeArr[count] = false;
						count++;
					} else {
						typeArr[count] = true;
						count++;
					}
				}
			}
		}
		// PlantInfo plantInfo=plantInfoMapper.getPlantInfo(plantId);
		// double capacity=plantInfo.getCapacity();
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(plantId);
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000, 0);
		} else if ("GW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000000, 0);
		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			formatRegex = "yyyy/M";
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			formatRegex = "yyyy/M/d";
			break;
		}

		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", plantId);
		// List<Map<String,
		// Object>>reportList=energyStorageDataMapper.getStoragePlantRunReport(map);
		// List<StorageReportOrder>resultList=new ArrayList<>();
		// if(Util.isNotBlank(reportList)){
		List<Map<String, Object>> reportList = energyMapper.getElectriRunReportLine(map);
		List<ReportOrderStoredOnly> resultList = new ArrayList<>();
		if (reportList != null && !reportList.isEmpty()) {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				boolean b = true;
				for (int k = 0; k < reportList.size(); k++) {
					Map<String, Object> reportMap = reportList.get(k);
					ReportOrderStoredOnly reportOrder = new ReportOrderStoredOnly();
					String dataTime = reportMap.get("dataTime").toString();
					if (dataTime.equals(tempTime)) {
						String upEnergy = Util.roundDouble(reportMap.get("upEnergy").toString());
						String upPrice = Util.roundDouble(reportMap.get("upPrice").toString());
						String downEnergy = Util.roundDouble(reportMap.get("downEnergy").toString());
						String downPrice = Util.roundDouble(reportMap.get("downPrice").toString());
						reportOrder.setFormatStr(formatRegex);
						reportOrder.setElectriNumUsed(-999999.99999);
						reportOrder.setElectriMoneyUsed(-999999.99999);
						reportOrder.setElectriNumOn(Double.parseDouble(upEnergy));
						reportOrder.setElectriMoneyOn(Double.parseDouble(upPrice));
						reportOrder.setElectriNumOff(Double.parseDouble(downEnergy));
						reportOrder.setElectriMoneyOff(Double.parseDouble(downPrice));
						reportOrder.setTime(dataTime);
						resultList.add(reportOrder);
						b = false;
						break;
					}
				}
				if (b) {
					ReportOrderStoredOnly reportOrder = new ReportOrderStoredOnly();
					reportOrder.setFormatStr(formatRegex);
					reportOrder.setTime(tempTime);
					reportOrder.setElectriNumUsed(-999999.99999);
					reportOrder.setElectriMoneyUsed(-999999.99999);
					reportOrder.setElectriNumOn(-999999.99999);
					reportOrder.setElectriMoneyOn(-999999.99999);
					reportOrder.setElectriNumOff(-999999.99999);
					reportOrder.setElectriMoneyOff(-999999.99999);
					resultList.add(reportOrder);
				}
			}
		} else {
			for (int j = 0; j < xData.size(); j++) {
				String tempTime = xData.get(j);
				ReportOrderStoredOnly reportOrder = new ReportOrderStoredOnly();
				reportOrder.setFormatStr(formatRegex);
				reportOrder.setElectriNumUsed(-999999.99999);
				reportOrder.setElectriMoneyUsed(-999999.99999);
				reportOrder.setElectriNumOn(-999999.99999);
				reportOrder.setElectriMoneyOn(-999999.99999);
				reportOrder.setElectriNumOff(-999999.99999);
				reportOrder.setElectriMoneyOff(-999999.99999);
				reportOrder.setTime(tempTime);
				resultList.add(reportOrder);
			}
		}
		if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
			ListUtils.sort(resultList, sortnameArr, typeArr);
		}
		int count = resultList.size();
		ReportOrderStoredOnly maxRepoert = new ReportOrderStoredOnly();
		maxRepoert.setFormatStr(formatRegex);
		maxRepoert.setTime("max");
		ReportOrderStoredOnly minRepoert = new ReportOrderStoredOnly();
		minRepoert.setFormatStr(formatRegex);
		minRepoert.setTime("min");
		ReportOrderStoredOnly avgRepoert = new ReportOrderStoredOnly();
		avgRepoert.setFormatStr(formatRegex);
		avgRepoert.setTime("avg");
		List<List<Double>> list = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty()) {
			for (int i = 0; i < 6; i++) {
				list.add(new ArrayList<>());
			}
			for (int j = 0; j < count; j++) {
				ReportOrderStoredOnly orderTemp = resultList.get(j);
				list.get(0).add(Double
						.parseDouble("--".equals(orderTemp.getElectriNumUsed()) ? "0" : orderTemp.getElectriNumUsed()));
				list.get(1).add(Double.parseDouble(
						"--".equals(orderTemp.getElectriMoneyUsed()) ? "0" : orderTemp.getElectriMoneyUsed()));
				list.get(2).add(Double
						.parseDouble("--".equals(orderTemp.getElectriNumOn()) ? "0" : orderTemp.getElectriNumOn()));
				list.get(3).add(Double
						.parseDouble("--".equals(orderTemp.getElectriMoneyOn()) ? "0" : orderTemp.getElectriMoneyOn()));
				list.get(4).add(Double
						.parseDouble("--".equals(orderTemp.getElectriNumOff()) ? "0" : orderTemp.getElectriNumOff()));
				list.get(5).add(Double.parseDouble(
						"--".equals(orderTemp.getElectriMoneyOff()) ? "0" : orderTemp.getElectriMoneyOff()));
			}
			List<DoubleSummaryStatistics> orderUtil = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				DoubleSummaryStatistics stats = list.get(i).stream().mapToDouble((x) -> x).summaryStatistics();
				orderUtil.add(stats);
			}
			/** 最大值 */
			maxRepoert.setElectriNumUsed(orderUtil.get(0).getMax());
			maxRepoert.setElectriMoneyUsed(orderUtil.get(1).getMax());
			maxRepoert.setElectriNumOn(orderUtil.get(2).getMax());
			maxRepoert.setElectriMoneyOn(orderUtil.get(3).getMax());
			maxRepoert.setElectriNumOff(orderUtil.get(4).getMax());
			maxRepoert.setElectriMoneyOff(orderUtil.get(5).getMax());
			/** 最小值 */
			minRepoert.setElectriNumUsed(orderUtil.get(0).getMin());
			minRepoert.setElectriMoneyUsed(orderUtil.get(1).getMin());
			minRepoert.setElectriNumOn(orderUtil.get(2).getMin());
			minRepoert.setElectriMoneyOn(orderUtil.get(3).getMin());
			minRepoert.setElectriNumOff(orderUtil.get(4).getMin());
			minRepoert.setElectriMoneyOff(orderUtil.get(5).getMin());
			/** 平均值 */
			avgRepoert.setElectriNumUsed(Util.roundDouble(orderUtil.get(0).getAverage()));
			avgRepoert.setElectriMoneyUsed(Util.roundDouble(orderUtil.get(1).getAverage()));
			avgRepoert.setElectriNumOn(Util.roundDouble(orderUtil.get(2).getAverage()));
			avgRepoert.setElectriMoneyOn(Util.roundDouble(orderUtil.get(3).getAverage()));
			avgRepoert.setElectriNumOff(Util.roundDouble(orderUtil.get(4).getAverage()));
			avgRepoert.setElectriMoneyOff(Util.roundDouble(orderUtil.get(5).getAverage()));
		}
		resultList.add(0, avgRepoert);
		resultList.add(0, minRepoert);
		resultList.add(0, maxRepoert);
		map.clear();
		map.put("data", resultList);
		map.put("capacity", capacity + plantInfo.getUnit());
		map.put("draw", draw);
		map.put("plantName", plantInfo.getPlantName());
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		Map<String, Object> time = new HashMap<>();
		Map<String, Object> electriNumUsed = new HashMap<>();
		Map<String, Object> electriMoneyUsed = new HashMap<>();
		Map<String, Object> electriNumOn = new HashMap<>();
		Map<String, Object> electriMoneyOn = new HashMap<>();
		Map<String, Object> electriNumOff = new HashMap<>();
		Map<String, Object> electriMoneyOff = new HashMap<>();
		List<Map<String, Object>> columns = new ArrayList<>();
		time.put("data", "time");
		time.put("title", "时间");
		electriNumUsed.put("data", "electriNumUsed");
		electriNumUsed.put("title", "用电量（度）");
		electriMoneyUsed.put("data", "electriMoneyUsed");
		electriMoneyUsed.put("title", "用电电费（元）");
		electriNumOn.put("data", "electriNumOn");
		electriNumOn.put("title", "上网电量（度）");
		electriMoneyOn.put("data", "electriMoneyOn");
		electriMoneyOn.put("title", "上网费用（元）");
		electriNumOff.put("data", "electriNumOff");
		electriNumOff.put("title", "下网电量（度）");
		electriMoneyOff.put("data", "electriMoneyOff");
		electriMoneyOff.put("title", "下网费用（元）");
		columns.add(time);
		columns.add(electriNumUsed);
		columns.add(electriMoneyUsed);
		columns.add(electriNumOn);
		columns.add(electriMoneyOn);
		columns.add(electriNumOff);
		columns.add(electriMoneyOff);
		map.put("columns", columns);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean storageRunReportsLine(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 电站
		String plantId = String.valueOf(map.get("plantId"));
		// 维度
		String dimension = String.valueOf(map.get("dimension"));
		// 范围
		String range = String.valueOf(map.get("range"));
		PlantInfo plantInfo = plantInfoMapper.getPlantInfo(plantId);
		// 电站类型id
		Integer typeId = plantInfo.getPlantTypeA();
		// 电站容量换算
		double capacity = plantInfo.getCapacity();
		String plantUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000, 0);
		} else if ("GW".equalsIgnoreCase(plantUnit)) {
			capacity = Util.multFloat(capacity, 1000000, 0);
		}
		// 格式化参数
		String formatCondit = null;
		String formatGroup = null;
		if (range == null || range == "null" || StringUtils.isBlank(range)) {
			if ("month".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy/M");
			} else if ("year".equals(dimension)) {
				range = Util.timeFormate(new Date().getTime(), "yyyy");
			}
		}
		List<String> xData = null;
		List<Map<String, Object>> yDaya = new ArrayList(3);
		switch (dimension) {
		case "month":
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		case "year":
			formatGroup = "%Y/%c";
			formatCondit = "%Y";
			xData = Util.getEverydayByYear(range);
			break;
		default:
			formatGroup = "%Y/%c/%e";
			formatCondit = "%Y/%c";
			xData = Util.getEverydayByMonth(range);
			break;
		}

		map.clear();
		map.put("formatCondit", formatCondit);
		map.put("formatGroup", formatGroup);
		map.put("range", range);
		map.put("plantId", plantId);
		return null;
	}
}
