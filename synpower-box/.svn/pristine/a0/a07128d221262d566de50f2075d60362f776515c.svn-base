package com.synpower.serviceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.DataCentralInverter;
import com.synpower.bean.DataPcs;
import com.synpower.bean.DataStringInverter;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.PlantListOrder;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysScreen;
import com.synpower.bean.SysUser;
import com.synpower.bean.SysWeather;
import com.synpower.constant.PTAC;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.DataCentralInverterMapper;
import com.synpower.dao.DataPcsMapper;
import com.synpower.dao.DataStringInverterMapper;
import com.synpower.dao.InventerStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.PowerPriceMapper;
import com.synpower.dao.SysAddressMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysScreenMapper;
import com.synpower.dao.SysUserMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.MonitorService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.ScreenService;
import com.synpower.service.StatisticalGainService;
import com.synpower.service.WXmonitorService;
import com.synpower.util.ListUtils;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;

@Service
public class WXmonitorServiceImpl implements WXmonitorService {
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysAddressMapper addMapper;
	@Autowired
	private PowerPriceMapper priceMapper;
	@Autowired
	private StatisticalGainService statisticalGainService;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysScreenMapper screenMapper;
	@Autowired
	private PlantInfoServiceImpl plantInfoServiceImpl;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private DataPcsMapper dataPcsMapper;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private DataStringInverterMapper dataStringInverterMapper;
	@Autowired
	private DataCentralInverterMapper centralInventerMapper;
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private InventerStorageDataMapper inventerMapper;
	@Autowired
	private PlantInfoService plantService;

	@Override
	public MessageBean genCurve(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = null;
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String plantType = u.getPlantType();
		if (plantType.equals("2") || plantType.equals("3")) {
			msg = monitorService.getElectricityProfit(str, session);
		} else if (plantType.equals("1")) {
			msg = monitorService.getPriceBarByTimeWx(str, session);
		}
		return msg;
	}

	@Override
	public MessageBean getStaticInfo(String str, Session session, HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException, IOException {
		MessageBean msg = new MessageBean();
		Map<String, Object> body = new HashMap<String, Object>();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 通过tokenId得到当前登录的用户
		User u = session.getAttribute(map.get("tokenId") + "");
		String plantType = u.getPlantType();
		String tokenId = String.valueOf(map.get("tokenId"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);

		if (Util.isNotBlank(plantIdList)) {
			if ("3".equals(plantType)) {
				// 获取公司名称
				String userType = u.getUserType();
				if ("0".equals(userType)) {
					MessageBean msg1 = screenService.getOrgIntroduction(str, session);
					Map introduction = (Map) msg1.getBody();
					body.put("companyName", introduction.get("title"));
				}
				body.putAll(getPVStaticInfo(str, session, request));
				// 获取电站状态
				MessageBean msg5 = monitorService.getTodayInfo(str, session);
				Map<String, Object> resultMap = (Map<String, Object>) msg5.getBody();
				body.put("plantStatus", resultMap);
				// 获取电站总数
				int totalPlant = plantIdList.size();
				body.put("plantTotal", totalPlant);
			} else {
				// 获取公司名称
				String userType = u.getUserType();
				if ("0".equals(userType)) {
					MessageBean msg1 = screenService.getOrgIntroduction(str, session);
					Map introduction = (Map) msg1.getBody();
					body.put("companyName", introduction.get("title"));
				}
				// 获取社会贡献
				MessageBean msg2 = screenService.getContribution(str, session);
				List<Object> contribution = (List<Object>) msg2.getBody();
				body.put("contribution", contribution);
				// 获取设备分布
				MessageBean msg3 = screenService.getPlantDistribution(str, session, request);
				Map distribution = (Map) msg3.getBody();
				body.put("deviceDis", distribution);
				// 获取电站规模
				Map plantCount = getPlantCountWx(str, session, plantIdList);
				body.put("plantScale", plantCount.get("datas"));
				body.put("totalCapacity", plantCount.get("totalCapacity"));
				// 获取电站状态
				MessageBean msg5 = monitorService.getTodayInfo(str, session);
				Map<String, Object> resultMap = (Map<String, Object>) msg5.getBody();
				body.put("plantStatus", resultMap);
				// 获取电站总数
				int totalPlant = plantIdList.size();
				body.put("plantTotal", totalPlant);
			}
			msg.setBody(body);
			msg.setCode(Header.STATUS_SUCESS);
		} else {
			msg.setBody(body);
			msg.setCode(Header.STATUS_SUCESS);
		}
		return msg;
	}

	@Override
	public Map<String, Object> getPVStaticInfo(String str, Session session, HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException, IOException {
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 通过tokenId得到当前登录的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		Map<String, List<Integer>> plantTypeList = plantInfoService.getPlantsTypeForUser(tokenId, session, 1);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(2);
		Map<String, Object> resultMap = new HashMap<>(2);
		Map<String, Object> pvMap = new HashMap<>(2);
		Map<String, Object> energyMap = new HashMap<>(2);
		pvMap.put("name", "光伏");
		pvMap.put("value", 0);
		energyMap.put("name", "储能");
		energyMap.put("value", 0);
		Double pvCapacity = 0.0, energyCapacity = 0.0, totalPrice = 0.0;
		int size = 0;
		if (Util.isNotBlank(plantTypeList)) {
			List<Integer> pvList = plantTypeList.get("1");
			List<Integer> energyList = plantTypeList.get("2");
			if (Util.isNotBlank(pvList)) {
				pvMap.put("光伏", pvList.size());
				List<PlantInfo> pvPlantInfos = plantMapper.getPlantByIds(pvList);
				if (Util.isNotBlank(pvPlantInfos)) {
					for (PlantInfo plantInfo : pvPlantInfos) {
						String unit = plantInfo.getUnit();
						double capacityTemp = plantInfo.getCapacity();
						if ("MW".equalsIgnoreCase(unit)) {
							capacityTemp = Util.multFloat(capacityTemp, 1000, 0);
						} else if ("GW".equalsIgnoreCase(unit)) {
							capacityTemp = Util.multFloat(capacityTemp, 1000_000, 0);
						}
						pvCapacity = Util.addFloat(pvCapacity, capacityTemp, 0);
						totalPrice = Util.addFloat(totalPrice, Double.parseDouble(
								statisticalGainService.getHistoryIncome(String.valueOf(plantInfo.getId())).get("inCome")
										+ ""),
								0);
					}
					size += pvPlantInfos.size();
					pvMap.put("value", pvPlantInfos.size());
				}
			}
			if (Util.isNotBlank(energyList)) {
				pvMap.put("储能", energyList.size());
				List<PlantInfo> pvPlantInfos = plantMapper.getPlantByIds(energyList);
				if (Util.isNotBlank(pvPlantInfos)) {
					for (PlantInfo plantInfo : pvPlantInfos) {
						String unit = plantInfo.getUnit();
						double capacityTemp = plantInfo.getCapacity();
						if ("MWh".equalsIgnoreCase(unit)) {
							capacityTemp = Util.multFloat(capacityTemp, 1000, 0);
							// capacity=Util.addFloat(capacity, capacityTemp, 0);
						} else if ("GWh".equalsIgnoreCase(unit)) {
							capacityTemp = Util.multFloat(capacityTemp, 1000_000, 0);
							// capacity=Util.addFloat(capacity, capacityTemp, 0);
						}
						energyCapacity = Util.addFloat(energyCapacity, capacityTemp, 0);
						totalPrice = Util
								.addFloat(totalPrice,
										Double.parseDouble(statisticalGainService
												.getNowDayIncome(String.valueOf(plantInfo.getId())).get("totalPrice")
												+ ""),
										0);
					}
					size += pvPlantInfos.size();
					energyMap.put("value", pvPlantInfos.size());
				}
			}
		}
		result.add(pvMap);
		result.add(energyMap);
		List<Map<String, Object>> sizeAndIncome = new ArrayList<Map<String, Object>>(3);
		Map<String, Object> sizeAndIncomeMap1 = new HashMap<>(3);
		sizeAndIncomeMap1.put("name", "储能容量");
		sizeAndIncomeMap1.put("unity", "度");
		sizeAndIncomeMap1.put("value", energyCapacity);
		Map<String, Object> sizeAndIncomeMap2 = new HashMap<>(3);
		sizeAndIncomeMap2.put("name", "光伏容量");
		sizeAndIncomeMap2.put("unity", "千瓦");
		sizeAndIncomeMap2.put("value", pvCapacity);
		Map<String, Object> sizeAndIncomeMap3 = new HashMap<>(3);
		sizeAndIncomeMap3.put("name", "累计收益");
		sizeAndIncomeMap3.put("unity", "元");
		sizeAndIncomeMap3.put("value", totalPrice);
		sizeAndIncome.add(sizeAndIncomeMap1);
		sizeAndIncome.add(sizeAndIncomeMap2);
		sizeAndIncome.add(sizeAndIncomeMap3);
		resultMap.put("sizeAndIncome", sizeAndIncome);
		resultMap.put("plantScale", result);
		resultMap.put("totalCapacity", "共" + size + "座");
		return resultMap;

	}

	public Map getPlantCountWx(String str, Session session, List<Integer> plantIdList)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String tokenId = String.valueOf(map.get("tokenId"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		// 获取当前登录用户和该用户组织下的组织id
		List<PlantInfo> list = plantMapper.getPlantByIds(plantIdList);
		List<Double> capacityList = new ArrayList<Double>();
		for (PlantInfo plantInfo : list) {
			if (plantInfo.getUnit().equals("MW")) {
				capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000, 0));
			} else {
				capacityList.add(plantInfo.getCapacity());
			}
		}
		int first = 0;
		int second = 0;
		int third = 0;
		int fourth = 0;
		for (Double num : capacityList) {
			if (num < 5) {
				first++;
			} else if (num >= 5 && num < 10) {
				second++;
			} else if (num >= 10 && num < 1000) {
				third++;
			} else if (num >= 1000 && num < 10000) {
				fourth++;
			}
		}
		// 查询电站总数
		int totalPlant = plantIdList.size();
		resultMap.put("totalPlant", totalPlant + "座");
		getTotalPlant(parameterMap, resultMap, u, capacityList);
		// 组装前台数据
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> firstMap = new HashMap<String, Object>();
		firstMap.put("name", "5kW以下");
		firstMap.put("value", first);
		firstMap.put("color", "#3380c3");
		resultList.add(firstMap);
		Map<String, Object> secondMap = new HashMap<String, Object>();
		secondMap.put("name", "5-10kW");
		secondMap.put("value", second);
		secondMap.put("color", "#c8e8f7");
		resultList.add(secondMap);
		Map<String, Object> thirdMap = new HashMap<String, Object>();
		thirdMap.put("name", "10kW-1MW");
		thirdMap.put("value", third);
		thirdMap.put("color", "#89ccf1");
		resultList.add(thirdMap);
		Map<String, Object> fourthMap = new HashMap<String, Object>();
		fourthMap.put("name", "1MW-10MW");
		fourthMap.put("value", fourth);
		fourthMap.put("color", "#3e9fd9");
		resultList.add(fourthMap);
		resultMap.put("datas", resultList);
		return resultMap;
	}

	/**
	 * @Title: getTotalPlant
	 * @Description: 获取当前用户组织下的电站总数和总装机容量
	 * @param parameterMap 参数集合
	 * @param resultMap    结果集合
	 * @param              u: 当前登录用户
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月21日下午2:50:19
	 */
	public void getTotalPlant(Map<String, Object> parameterMap, Map<String, Object> resultMap, User u,
			List<Double> capacityList) {
		String userType = u.getUserType();
		if ("0".equals(userType)) {
			// 得到所有组织列表
			List<SysOrg> orgList = orgMapper.getAllOrg();
			SysOrg org = ((SysOrg) u.getUserOrg());
			List<String> reList = new ArrayList<>();
			// 查出当前登录人的组织id有哪些
			reList.add(((SysOrg) u.getUserOrg()).getId() + "");
			reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
			parameterMap.put("reList", reList);
			String totalCapacity = null;
			SysScreen screen = screenMapper.getScreenByOrgId(((SysOrg) u.getUserOrg()).getId() + "");
			if (screen != null) {
				if (screen.getPlantAutomaticCalculation() == null
						|| screen.getPlantAutomaticCalculation().equals("null")
						|| screen.getPlantAutomaticCalculation().equals("")) {
					resultMap = getCapa(resultMap, capacityList);
				} else {
					if ("0".equals(screen.getPlantAutomaticCalculation())) {
						totalCapacity = screen.getPlantArtificialCapacity() + "";
						double capacity = screen.getPlantArtificialCapacity();
						if ("MW".equals(screen.getCapacityUnit())) {
							capacity = Util.multDouble(totalCapacity, "1000", 1);
						} else if ("GW".equals(screen.getCapacityUnit())) {
							capacity = Util.multDouble(totalCapacity, "1000000", 1);
						}
						totalCapacity = capacity + "";
						resultMap.put("totalCapacity", totalCapacity);
					} else if (screen.getPlantAutomaticCalculation().equals("1")) {
						resultMap = getCapa(resultMap, capacityList);
					}
				}
			} else {
				resultMap = getCapa(resultMap, capacityList);
			}
		} else if ("1".equals(userType)) {
			resultMap = getCapa(resultMap, capacityList);
		}
	}

	public Map getCapa(Map<String, Object> resultMap, List<Double> capacityList) {
		// 查询总装机容量
		Double totalCapacity = 0d;
		for (Double num : capacityList) {
			totalCapacity = Util.addFloat(totalCapacity, num, 0);
		}
		totalCapacity = Util.roundDouble(totalCapacity);
		if (totalCapacity != 0) {
			resultMap.put("totalCapacity", totalCapacity + "");
		} else {
			resultMap.put("totalCapacity", "0");
		}
		return resultMap;
	}

	@Override
	public MessageBean getDynamic(String str, Session session, HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> body = new HashMap<String, Object>();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		String plantTypeWX = u.getPlantType();
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		if ("1".equals(plantTypeWX)) {
			if (Util.isNotBlank(plantIdList)) {
				double todayEnergy = 0.00;
				double todayPrice = 0.00;
				// 查询出当前公司下所有的电站
				List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
				if (Util.isNotBlank(plantList)) {
					for (PlantInfo plantInfo : plantList) {
						String plantId = plantInfo.getId() + "";
						Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
						if (Util.isNotBlank(todayPricePower)) {
							String power = todayPricePower.get("power") + "";
							String inCome = todayPricePower.get("inCome") + "";
							// 得到今日发电量和今日收益
							if (Util.isNotBlank(power)) {
								todayEnergy = Util.addFloat(todayEnergy, Double.valueOf(power), 0);
							}
							if (Util.isNotBlank(inCome)) {
								todayPrice = Util.addFloat(todayPrice, Double.valueOf(inCome), 0);
							}
						}
					}
				}
				resultMap.put("genToday", Util.getRetainedDecimal(todayEnergy));
				resultMap.put("proToday", Util.getRetainedDecimal(todayPrice));
				Map<String, Double> totalMap = screenService.getPowerTotal(plantIdList);
				resultMap.put("genTotal", totalMap.get("power"));
				resultMap.put("proTotal", totalMap.get("inCome"));
			}
		} else {
			double totalEnergy = 0.0, totalPrice = 0.0;
			double daliyEnergy = 0.0, daliyPrice = 0.0;
			double totalPosEnergy = 0.0, daliyPosEnergy = 0.0;
			if (Util.isNotBlank(plantIdList)) {
				// 查询出当前公司下所有的电站
				List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
				if (Util.isNotBlank(plantList)) {
					for (PlantInfo plantInfo : plantList) {
						String plantId = plantInfo.getId() + "";
						String plantType = SystemCache.plantTypeList.get(plantId);
						Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
						if (Util.isNotBlank(todayPricePower)) {
							if ("1".equals(plantType)) {
								String power = todayPricePower.get("power") + "";
								String inCome = todayPricePower.get("inCome") + "";
								// 得到今日发电量和今日收益
								if (Util.isNotBlank(power)) {
									daliyEnergy = Util.addFloat(daliyEnergy, Double.valueOf(power), 0);
								}
								if (Util.isNotBlank(inCome)) {
									daliyPrice = Util.addFloat(daliyPrice, Double.valueOf(inCome), 0);
								}
							} else if ("2".equals(plantType)) {
								String totalPosEnergyTemp = todayPricePower.get("totalPosEnergy") + "";
								String daliyPosEnergyTemp = todayPricePower.get("dailyPosEnergy") + "";
								String totalPriceTemp = todayPricePower.get("totalPrice") + "";
								if (Util.isNotBlank(totalPosEnergyTemp)) {
									totalPosEnergy = Util.addFloat(totalPosEnergy, Double.valueOf(totalPosEnergyTemp),
											0);
								}
								if (Util.isNotBlank(daliyPosEnergyTemp)) {
									daliyPosEnergy = Util.addFloat(daliyPosEnergy, Double.valueOf(daliyPosEnergyTemp),
											0);
								}
								if (Util.isNotBlank(totalPriceTemp)) {
									totalPrice = Util.addFloat(totalPrice, Double.valueOf(totalPriceTemp), 0);
								}
							}
						}
						if ("1".equals(plantType)) {
							Map<String, Object> totalPricePower = statisticalGainService.getHistoryIncome(plantId);
							if (Util.isNotBlank(totalPricePower)) {
								if ("1".equals(plantType)) {
									String power = todayPricePower.get("power") + "";
									String inCome = todayPricePower.get("inCome") + "";
									// 得到今日发电量和今日收益
									if (Util.isNotBlank(power)) {
										totalEnergy = Util.addFloat(totalEnergy, Double.valueOf(power), 0);
									}
									if (Util.isNotBlank(inCome)) {
										totalPrice = Util.addFloat(totalPrice, Double.valueOf(inCome), 0);
									}
								}
							}
						}
					}
				}
			}
			resultMap.put("disToday", Util.getDecimalFomat2().format(daliyPosEnergy));
			resultMap.put("disTotal", Util.getDecimalFomat2().format(totalPosEnergy));
			resultMap.put("genToday", Util.getDecimalFomat2().format(daliyEnergy));
			resultMap.put("proToday", Util.getDecimalFomat2().format(daliyPrice));
			resultMap.put("genTotal", Util.getDecimalFomat2().format(totalEnergy));
			resultMap.put("proTotal", Util.getDecimalFomat2().format(totalPrice));
		}
		body.put("genPro", resultMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	/**
	 * @Title: getUnit
	 * @Description: 单位换算
	 * @param value   需要比较的值
	 * @param compare 跟什么值比较
	 * @param hex     进制
	 * @param hexUnit 换算后的单位
	 * @param unit    没有换算的单位
	 * @return: Map<String,Object> value：最后的值；unit：最后的单位
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年12月18日上午11:23:30
	 */
	@Override
	public Map<String, Object> getUnit(Double value, int compare, int hex, String hexUnit, String unit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (value > compare) {
			Double newTodayPrice = Util.divideDouble(value, hex, 1);
			resultMap.put("value", newTodayPrice);
			resultMap.put("unit", hexUnit);
		} else {
			Double newTodayPrice = Util.divideDouble(value, 1, 1);
			resultMap.put("value", newTodayPrice);
			resultMap.put("unit", unit);
		}
		return resultMap;
	}

	@Override
	public MessageBean getPlantMap(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		String searchNum = map.get("searchNum") + "";
		// 通过tokenId获取当前登陆的用户
		User u = session.getAttribute(map.get("tokenId") + "");
		String tokenId = String.valueOf(map.get("tokenId"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		String plantId = map.get("plantId") + "";
		List<Object> resultList = new ArrayList<>();
		if (plantId.equals("null") || "".equals(plantId)) {
			// 查询出当前公司下所有的电站
			List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
			if (!searchNum.equals("4") && !searchNum.equals("null")) {
				for (PlantInfo plantInfo : plantList) {
					int type = ServiceUtil.getPlantStatus(plantInfo.getId() + "");
					if (searchNum.equals(String.valueOf(type))) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						String location = plantInfo.getLoaction();
						String[] latiLongi = location.split(",");
						resultMap.put("latitude", latiLongi[1]);
						resultMap.put("longitude", latiLongi[0]);
						resultMap.put("plantId", plantInfo.getId());
						resultMap.put("type", type);
						resultList.add(resultMap);
					}
				}
			} else {
				for (PlantInfo plantInfo : plantList) {
					int type = ServiceUtil.getPlantStatus(plantInfo.getId() + "");
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String location = plantInfo.getLoaction();
					String[] latiLongi = location.split(",");
					resultMap.put("latitude", latiLongi[1]);
					resultMap.put("longitude", latiLongi[0]);
					resultMap.put("plantId", plantInfo.getId());
					resultMap.put("type", type);
					resultList.add(resultMap);
				}
			}
		} else {
			int type = ServiceUtil.getPlantStatus(plantId);
			PlantInfo plant = plantMapper.getPlantById(plantId);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String location = plant.getLoaction();
			String[] latiLongi = location.split(",");
			resultMap.put("latitude", latiLongi[1]);
			resultMap.put("longitude", latiLongi[0]);
			resultMap.put("plantId", plant.getId());
			resultMap.put("type", type);
			resultList.add(resultMap);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		return msg;
	}

	@Override
	public MessageBean getMapPlant(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		// 获取电站Id
		String plantId = map.get("plantId") + "";
		PlantInfo plantInfo = plantMapper.getPlantById(plantId);
		PlantListOrder plantsOrder = new PlantListOrder();
		plantsOrder.setAddr(plantInfo.getPlantAddr());
		// String loaction = plantInfo.getLoaction();
		// String[] location = loaction.split(",");
		// Double d01 = Double.parseDouble(location[0]);
		// Double d02 = Double.parseDouble(location[1]);
		// double distance = GetDistance(d2,d1,d02,d01);
		// plantsOrder.setDistribution(distance);
		plantsOrder.setName(plantInfo.getPlantName());
		String photo = StringUtil.checkBlank(plantInfo.getPlantPhoto());
		String[] photos = StringUtils.split(photo, ";");
		String pic = null;
		/** 微信小程序默认返回第一张图片 */
		if (Util.isNotBlank(photos)) {
			pic = photos[0];
		}
		plantsOrder.setPic(pic);
		plantsOrder.setPlantId(plantInfo.getId());
		plantsOrder.setType(ServiceUtil.getPlantStatus(plantId));
		String plantType = SystemCache.plantTypeList.get(plantId);
		String util = plantInfo.getUnit();
		double capatity = plantInfo.getCapacity();
		plantsOrder.setPlantType(Integer.parseInt(plantType));
		// 处理储能电站与光伏电站的区别
		if ("1".equals(plantType)) {
			if ("MW".equalsIgnoreCase(util)) {
				capatity = Util.multFloat(capatity, 1000, 1);
			} else if ("GW".equalsIgnoreCase(util)) {
				capatity = Util.multFloat(capatity, 1000000, 1);
			}
			plantsOrder.setCapacity(capatity);
			plantsOrder.setUnit("kw");
			Map<String, Object> nowDayIncome = statisticalGainService.getNowDayIncome(plantId);
			String genDay = "0";
			if (Util.isNotBlank(nowDayIncome)) {
				genDay = String.valueOf(nowDayIncome.get("power"));
			}
			plantsOrder.setGenDay(Double.parseDouble(genDay));
			plantsOrder.setGenTodayUnit("kW");
		} else if ("2".equals(plantType)) {
			if ("MWH".equalsIgnoreCase(util)) {
				capatity = Util.multFloat(capatity, 1000, 1);
			} else if ("GWH".equalsIgnoreCase(util)) {
				capatity = Util.multFloat(capatity, 1000000, 1);
			}
			plantsOrder.setEnergyStorage(capatity);
			plantsOrder.setEnergyStorageUnit("kWh");
			plantsOrder.setSoc(SystemCache.plantSOC.get(plantId));
		}
		SysWeather plantWeather = plantInfoService.getPlantTodayWeather(plantInfo.getId());
		plantsOrder.setWeather(plantWeather.getWeather());
		plantsOrder.setWeatherPic(plantWeather.getDayPictureUrl());
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(plantsOrder);
		return msg;
	}

	@Override
	public MessageBean powerCurve(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		// 该用户平台下所有电站
		Map<String, List<Integer>> plantList = plantInfoService.getPlantsTypeForUser(tokenId, session, 1);

		// 时间刻度列表
		List<String> timeList = ServiceUtil.getStringTimeSegment("00:00", "23:59");
		String curTime = timeList.get(0);

		int nowLocation = 0;
		curTime = ServiceUtil.getNowTimeInTimes();

		// 当前时间在时间刻度列表的位置或者叫做长度
		nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
		String[] units = new String[] { "千瓦", "兆瓦", "吉瓦" };

		List<Object[]> nowData = new ArrayList<>();
		// 当前的功率
		double curPower = 0.0;
		// 遍历电站
		if (Util.isNotBlank(plantList)) {
			// 得到光伏电站
			List<Integer> pvList = plantList.get("1");
			// 得到储能电站
			List<Integer> energyList = plantList.get("2");
			// 集中式逆变器
			List<Integer> deviceOne = new ArrayList<>();
			// 组串式逆变器
			List<Integer> deviceTwo = new ArrayList<>();
			// PCS
			List<Integer> deviceNine = new ArrayList<>();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			boolean curStatus = false;

			if (nowLocation < timeList.size()) {
				curStatus = true;
			}

			if (Util.isNotBlank(pvList)) {
				for (Integer pvId : pvList) {
					Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(pvId);
					if (Util.isNotBlank(devices)) {
						List<Integer> typeOne = devices.get(1);
						List<Integer> typeTwo = devices.get(2);
						if (Util.isNotBlank(typeOne)) {
							deviceOne.addAll(typeOne);
						}
						if (Util.isNotBlank(typeTwo)) {
							deviceTwo.addAll(typeTwo);
						}
						String tempPower = statisticalGainService.getNowDayIncome(String.valueOf(pvId))
								.get("activcePower") + "";
						if (Util.isNotBlank(tempPower)) {
							curPower = Util.addFloat(curPower, Double.parseDouble(tempPower), 0);
						}
					}
				}
			}

			if (Util.isNotBlank(energyList)) {
				for (Integer energyId : energyList) {
					Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(energyId);
					if (Util.isNotBlank(devices)) {
						List<Integer> typeNine = devices.get(9);
						if (Util.isNotBlank(typeNine)) {
							deviceNine.addAll(typeNine);
						}
					}
					if (curStatus) {
						if (SystemCache.plantCurrentEnergyPower.containsKey(String.valueOf(energyId))) {
							curPower = Util.addFloat(curPower,
									SystemCache.plantCurrentEnergyPower.get(String.valueOf(energyId)), 0);
						}
					}
				}
			}

			Map<String, Object> paramMap = new HashMap<>(4);
			String time = Util.getCurrentTimeStr("yyyy-MM-dd");
			try {
				paramMap.put("minTime", sdf.parse(time + " 00:00:00.000").getTime());
				paramMap.put("maxTime", sdf.parse(time + " 23:59:59.999").getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (Util.isNotBlank(deviceOne)) {
				paramMap.put("devices", deviceOne);
				List<DataCentralInverter> centralList = centralInventerMapper.getTimesPower(paramMap);
				if (centralList != null && !centralList.isEmpty()) {
					for (int i = 0, len = centralList.size(); i < len; i++) {
						long dataTime = centralList.get(i).getDataTime();
						float power = centralList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				}
			}
			if (curStatus) {
				nowData.add(new Object[] { System.currentTimeMillis(), curPower });
			}

			List<DataStringInverter> centralList = null;
			if (Util.isNotBlank(deviceTwo)) {
				paramMap.put("devices", deviceTwo);
				centralList = dataStringInverterMapper.getTimesPower(paramMap);
			}
			if (centralList != null && !centralList.isEmpty()) {
				if (nowData == null || nowData.isEmpty()) {
					for (int i = 0; i < centralList.size(); i++) {
						long dataTime = centralList.get(i).getDataTime();
						float power = centralList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				} else {
					for (int i = 0, length = centralList.size(); i < length; i++) {
						DataStringInverter inverter = centralList.get(i);
						long dataTime = inverter.getDataTime();
						boolean b = false;
						for (int j = 0, len = nowData.size(); j < len; j++) {
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

			List<DataPcs> pcsList = null;
			if (Util.isNotBlank(deviceNine)) {
				paramMap.put("devices", deviceNine);
				pcsList = dataPcsMapper.getTimesPower(paramMap);
			}
			if (Util.isNotBlank(pcsList)) {
				if (nowData == null || nowData.isEmpty()) {
					for (int i = 0, len = pcsList.size(); i < len; i++) {
						long dataTime = pcsList.get(i).getDataTime();
						float power = pcsList.get(i).getActivePower();
						Object[] tempStr = { dataTime, power };
						nowData.add(tempStr);
					}
				} else {
					for (int i = 0, len = pcsList.size(); i < len; i++) {
						DataPcs pcs = pcsList.get(i);
						long dataTime = pcs.getDataTime();
						boolean b = false;
						for (int j = 0, length = nowData.size(); j < length; j++) {
							Object[] obj = nowData.get(i);
							if (dataTime == Long.parseLong(obj[0] + "")) {
								double totalPower = Util.addFloat(Float.parseFloat(obj[1] + ""), pcs.getActivePower(),
										1);
								nowData.remove(i);
								nowData.add(i, new Object[] { dataTime, totalPower });
								b = true;
								break;
							}
						}
						if (!b) {
							nowData.add(new Object[] { dataTime, pcs.getActivePower() });
						}
					}
				}
			}
		}

		Map<String, Object> result = null;

		/*
		 * //获取电站设备集合 List<CollDevice>deviceList=deviceMapper.getDeviceListByPid(pId);
		 * //按照设备类型分类 Map<Integer,
		 * List<Integer>>deviceMap=ServiceUtil.classifyDeviceForPlant(deviceList);
		 */
		// 将对应电站的对应时间点的功率装载到时间点位上
		// 功率
		result = ServiceUtil.loadParamForCurrentList("00:00", "23:59", nowData, nowLocation, "电站实时功率", units, 1000);
		result.put("curTime", curTime);
		result.put("curPower", curPower);
		msg.setBody(result);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getList(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		MessageBean msg = new MessageBean();
		String tokenId = map.get("tokenId") + "";
		/*
		 * User u = session.getAttribute(map.get("tokenId")+""); List<SysOrg> orgList =
		 * orgMapper.getAllOrg(); List<String>reList=new ArrayList<>();
		 * //获取当前登录用户及下属的组织id reList.add(((SysOrg)u.getUserOrg()).getId()+""); reList =
		 * ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
		 */
		List<Integer> list = plantService.getPlantsForUser(tokenId, session, 1);
		List<Map<String, Object>> ListFront = new ArrayList<>();
		if (Util.isNotBlank(list)) {
			// 每页条数
			Integer length = Integer.parseInt(map.get("length") + "");
			// 起始位置
			Integer offset = Integer.parseInt(map.get("offset") + "");
			// 筛选类型
			String searchNum = map.get("searchNum") + "";
			int statusI = 0;
			if ("".equals(searchNum) || searchNum == null) {
				statusI = 2;
			} else {
				statusI = Integer.parseInt(searchNum);
			}
			// 搜索条件
			String searchCon = map.get("searchCon") + "";
			// 搜索条件
			String phoneLocation = map.get("phoneLocation") + "";
			// 所属下级电站ids
			map.clear();
			// map.put("reList", reList);
			// List<Integer> list = addMapper.getChildPidsws(map);
			// 各电站总发电量
			List<Double> powerList = new ArrayList<>();
			List<Double> powerNowList = new ArrayList<>();
			List<Integer> statusList = new ArrayList<>();
			List<Integer> l0 = new ArrayList<>();
			List<Integer> l1 = new ArrayList<>();
			List<Integer> l2 = new ArrayList<>();
			List<Integer> l3 = new ArrayList<>();
			if (list.size() > 0) {
				for (Integer pId : list) {
					// 历史发电量、今日发电量、电站状态
					Map<String, Object> nowDayIncome = statisticalGainService.getNowDayIncome(String.valueOf(pId));
					BigDecimal bg = new BigDecimal(Double.parseDouble(nowDayIncome.get("power") + ""));
					double dayIncome = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					powerNowList.add(dayIncome);
					statusList.add(ServiceUtil.getPlantStatus(pId + ""));
					int plantStatus = ServiceUtil.getPlantStatus(pId + "");
					switch (plantStatus) {
					case 0:
						l0.add(pId);
						break;
					case 1:
						l1.add(pId);
						break;
					case 2:
						l2.add(pId);
						break;
					case 3:
						l3.add(pId);
						break;
					default:
						break;
					}
				}

				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("list", list);
				paramMap.put("length", length);
				paramMap.put("offset", offset);
				paramMap.put("searchNum", searchNum);
				paramMap.put("searchCon", searchCon);
				String[] split = phoneLocation.split(",");
				Double d1 = Double.parseDouble(split[0]);
				Double d2 = Double.parseDouble(split[1]);
				List<PlantInfo> flist = plantMapper.getEachPlantInfoPage(paramMap);
				// 封装排序所需实体参数
				List<PlantListOrder> resultList = new ArrayList<>();
				if (flist != null && !flist.isEmpty()) {
					for (int i = 0; i < flist.size(); i++) {
						PlantListOrder plantsOrder = new PlantListOrder();
						plantsOrder.setCapacity(flist.get(i).getCapacity());
						plantsOrder.setUnit(flist.get(i).getUnit());
						plantsOrder.setAddr(flist.get(i).getPlantAddr());
						String loaction = flist.get(i).getLoaction();
						String[] location = loaction.split(",");
						Double d01 = Double.parseDouble(location[0]);
						Double d02 = Double.parseDouble(location[1]);
						double distance = GetDistance(d2, d1, d02, d01);
						plantsOrder.setDistribution(distance);
						plantsOrder.setGenToday(powerNowList.get(i));
						plantsOrder.setName(flist.get(i).getPlantName());
						// String photo = plantMapper.getPhoto(flist.get(i).getId()+"");
						String photo = StringUtil.checkBlank(flist.get(i).getPlantPhoto());
						String[] photos = StringUtils.split(photo, ";");
						String pic = null;
						/** 微信小程序默认返回第一张图片 */
						if (Util.isNotBlank(photos)) {
							pic = photos[0];
						}
						plantsOrder.setPic(pic);
						plantsOrder.setPlantId(flist.get(i).getId());
						plantsOrder.setType(ServiceUtil.getPlantStatus(flist.get(i).getId() + ""));
						resultList.add(plantsOrder);
					}
					// 根据查询状态值查询电站信息
					String[] sortnameArr = null;
					boolean[] typeArr = null;
					switch (statusI) {
					case 0:
						sortnameArr = new String[] { "distribution" };
						typeArr = new boolean[] { true };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 1:
						sortnameArr = new String[] { "distribution" };
						typeArr = new boolean[] { false };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 2:
						sortnameArr = new String[] { "capacity" };
						typeArr = new boolean[] { false };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 3:
						sortnameArr = new String[] { "capacity" };
						typeArr = new boolean[] { true };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 4:
						sortnameArr = new String[] { "genToday" };
						typeArr = new boolean[] { false };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 5:
						sortnameArr = new String[] { "genToday" };
						typeArr = new boolean[] { true };
						if (resultList != null && !resultList.isEmpty() && sortnameArr != null
								&& sortnameArr.length > 0) {
							ListUtils.sort(resultList, sortnameArr, typeArr);
						}
						break;
					case 6:
						if (l0.size() > 0) {
							paramMap.put("statusList", l0);
							flist = plantMapper.getPlantsByStatus(paramMap);
						} else {
							flist = null;
						}
						break;
					case 7:
						if (l1.size() > 0) {
							paramMap.put("statusList", l1);
							flist = plantMapper.getPlantsByStatus(paramMap);
						} else {
							flist = null;
						}
						break;
					case 8:
						if (l2.size() > 0) {
							paramMap.put("statusList", l2);
							flist = plantMapper.getPlantsByStatus(paramMap);
						} else {
							flist = null;
						}
						break;
					case 9:
						if (l3.size() > 0) {
							paramMap.put("statusList", l3);
							flist = plantMapper.getPlantsByStatus(paramMap);
						} else {
							flist = null;
						}
						break;
					default:
						break;
					}
				}

				// 电站对象信息
				if (statusI < 6 && resultList.size() > 0) {
					for (int i = 0; i < resultList.size(); i++) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("plantId", resultList.get(i).getPlantId());
						resultMap.put("name", resultList.get(i).getName());
						resultMap.put("addr", resultList.get(i).getAddr());
						String unit = flist.get(i).getUnit();
						resultMap.put("capacity", resultList.get(i).getCapacity() + resultList.get(i).getUnit());
						// BigDecimal bg = new BigDecimal(resultList.get(i).getDistribution()/1000);
						// double distance = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						// resultMap.put("distribution", distance+"公里");
						resultMap.put("type", resultList.get(i).getType());
						resultMap.put("pic", resultList.get(i).getPic());
						double genToday = resultList.get(i).getGenToday();
						String[] units = { "度", "万度" };
						Map<String, Object> changeUnit = ServiceUtil.changeUnit(units, genToday, 10000);
						resultMap.put("genToday", String.valueOf(changeUnit.get("value")) + changeUnit.get("unit"));
						ListFront.add(resultMap);
					}
				} else if (statusI > 5 && flist != null) {
					for (int i = 0; i < flist.size(); i++) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("plantId", flist.get(i).getId());
						resultMap.put("name", flist.get(i).getPlantName());
						resultMap.put("addr", flist.get(i).getPlantAddr());
						String unit = flist.get(i).getUnit();
						resultMap.put("capacity", resultList.get(i).getCapacity() + resultList.get(i).getUnit());
						String location2 = flist.get(i).getLoaction();
						String[] split2 = location2.split(",");
						Double d01 = Double.parseDouble(split2[0]);
						Double d02 = Double.parseDouble(split2[1]);
						double distance = GetDistance(d2, d1, d02, d01);
						BigDecimal bg = new BigDecimal(distance / 1000);
						double dis = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						resultMap.put("distribution", dis + "公里");
						resultMap.put("type", statusList.get(i));
						String photos = flist.get(i).getPlantPhoto();
						String photo = null;
						if (Util.isNotBlank(photos)) {
							String[] photoArr = StringUtils.split(photos, ";");
							if (Util.isNotBlank(photoArr)) {
								photo = photoArr[0];
							}
						}
						resultMap.put("pic", photo);
						double genToday = resultList.get(i).getGenToday();
						String[] units = { "度", "万度" };
						Map<String, Object> changeUnit = ServiceUtil.changeUnit(units, genToday, 10000);
						resultMap.put("genToday", String.valueOf(changeUnit.get("value")) + changeUnit.get("unit"));
						ListFront.add(resultMap);
					}
				}

			}
			msg.setBody(ListFront);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("获取电站列表成功");
		} else {
			msg.setBody(ListFront);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("获取电站列表失败");
		}

		return msg;
	}

	/**
	 * 
	 * @Title: GetDistance
	 * @Description: 前小后大
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return: double
	 * @lastEditor: SP0012
	 * @lastEdit: 2017年12月18日下午2:36:39
	 */
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000);
		return s;
	}

	private static double EARTH_RADIUS = 6371.393;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	@Override
	public MessageBean searchPlantByKeyword(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {

		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = map.get("tokenId") + "";
		/*
		 * User u = session.getAttribute(map.get("tokenId")+""); List<SysOrg> orgList =
		 * orgMapper.getAllOrg(); List<String>reList=new ArrayList<>();
		 * //获取当前登录用户及下属的组织id reList.add(((SysOrg)u.getUserOrg()).getId()+""); reList =
		 * ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
		 */
		List<Integer> list = plantService.getPlantsForUser(tokenId, session, 1);
		List<PlantListOrder> ListFront = new ArrayList<>();
		if (list.size() > 0) {
			// 关键字
			String keyword = map.get("keyword") + "";
			// 此时位置
			String phoneLocation = map.get("phoneLocation") + "";
			// map.put("reList", reList);
			// List<Integer> list = addMapper.getChildPidsws(map);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("keyword", keyword);
			parameterMap.put("list", list);
			Double d1 = null, d2 = null;
			if (Util.isNotBlank(phoneLocation)) {
				String[] split = phoneLocation.split(",");
				d1 = Double.parseDouble(split[0]);
				d2 = Double.parseDouble(split[1]);
			}
			List<Integer> statusList = new ArrayList<>();
			if (list.size() > 0) {
				// 各电站总发电量
				List<PlantInfo> flist = plantMapper.getPlantsByCon(parameterMap);
				for (int i = 0; i < flist.size(); i++) {
					PlantInfo plantInfo = flist.get(i);
					String plantId = String.valueOf(plantInfo.getId());
					PlantListOrder plantsOrder = new PlantListOrder();
					plantsOrder.setAddr(plantInfo.getPlantAddr());
					String loaction = plantInfo.getLoaction();
					String[] location = loaction.split(",");
					Double d01 = Double.parseDouble(location[0]);
					Double d02 = Double.parseDouble(location[1]);
					double distance = -0.99999999;
					if (d1 != null && d2 != null) {
						distance = GetDistance(d2, d1, d02, d01);
					}
					plantsOrder.setDistribution(distance);
					plantsOrder.setName(plantInfo.getPlantName());
					String photo = StringUtil.checkBlank(plantInfo.getPlantPhoto());
					String[] photos = StringUtils.split(photo, ";");
					String pic = null;
					/** 微信小程序默认返回第一张图片 */
					if (Util.isNotBlank(photos)) {
						pic = photos[0];
					}
					plantsOrder.setPic(pic);
					plantsOrder.setPlantId(plantInfo.getId());
					plantsOrder.setType(ServiceUtil.getPlantStatus(plantId));
					String plantType = SystemCache.plantTypeList.get(plantId);
					String util = plantInfo.getUnit();
					double capatity = plantInfo.getCapacity();
					plantsOrder.setPlantType(Integer.parseInt(plantType));
					// 处理储能电站与光伏电站的区别
					if ("1".equals(plantType)) {
						if ("MW".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000, 1);
						} else if ("GW".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000000, 1);
						}
						plantsOrder.setCapacity(capatity);
						plantsOrder.setUnit("kw");
						Map<String, Object> nowDayIncome = statisticalGainService.getNowDayIncome(plantId);
						String genDay = "0";
						if (Util.isNotBlank(nowDayIncome)) {
							genDay = String.valueOf(nowDayIncome.get("power"));
						}
						plantsOrder.setGenDay(Double.parseDouble(genDay));
						plantsOrder.setGenTodayUnit("kW");
					} else if ("2".equals(plantType)) {
						if ("MWH".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000, 1);
						} else if ("GWH".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000000, 1);
						}
						plantsOrder.setEnergyStorage(capatity);
						plantsOrder.setEnergyStorageUnit("kWh");
						plantsOrder.setSoc(SystemCache.plantSOC.get(plantId));
					}
					SysWeather plantWeather = plantInfoService.getPlantTodayWeather(plantInfo.getId());
					plantsOrder.setWeather(plantWeather.getWeather());
					plantsOrder.setWeatherPic(plantWeather.getDayPictureUrl());
					ListFront.add(plantsOrder);
				}
			}
			msg.setBody(ListFront);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("搜索获取电站列表");
		} else {
			msg.setBody(ListFront);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("搜索获取电站列表失败");
		}

		return msg;
	}

	@Override
	public MessageBean getUserInfo(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		SysUser user = userMapper.getUserInfoById(u.getId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("userId", user.getId());
		resultMap.put("userName", user.getUserName());
		resultMap.put("userTel", user.getUserTel());
		resultMap.put("orgName", "");
		resultMap.put("orgCode", "暂无");
		if ("0".equals(user.getUserType())) {
			resultMap.put("orgName", user.getUserOrg().getOrgName());
			resultMap.put("orgCode", user.getUserOrg().getOrgCode());
		}
		resultMap.put("avatarUrl", user.getIconUrl());
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}

	@Override
	public MessageBean getWXPlantList(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = map.get("tokenId") + "";
		// 得到电站集合
		List<Integer> list = plantService.getPlantsForUser(tokenId, session, 1);

		// 过滤出需要的电站
		// 光伏
		List<String> pList = SystemCache.plantTypeMap.get(PTAC.PHOTOVOLTAIC + "");
		// 储能
		List<String> sList = SystemCache.plantTypeMap.get(PTAC.STOREDENERGY + "");
		// 光储
		List<String> psList = new ArrayList<>();
		
		if (Util.isNotBlank(pList)) {
			psList.addAll(pList);
		}
		if (Util.isNotBlank(sList)) {
			psList.addAll(sList);
		}

		List<Integer> plantList = new ArrayList<>();

		psList.forEach(e -> plantList.add(Integer.valueOf(e)));

		list.retainAll(plantList);

		if (Util.isNotBlank(list)) {
			/** 处理筛选条件--电站状态限制 **/
			// 每页条数
			Integer length = Integer.parseInt(map.get("length") + "");
			// 起始位置
			Integer offset = Integer.parseInt(map.get("offset") + "");
			// 筛选类型
			String searchNum = map.get("searchNum") + "";
			int searchType = 0;
			if ("".equals(searchNum) || searchNum == null) {
				//默认
				searchType = 99;
			} else {
				searchType = Integer.parseInt(searchNum);
			}
			// 手机位置
			String phoneLocation = map.get("phoneLocation") + "";
			List<PlantListOrder> result = null;
			switch (searchType) {
			// 距离升序 已经废弃 但是留着 万一以后又要
			case 0:
				result = loadPlantListForWX(list, phoneLocation);
				ListUtils.sort(result, true, new String[] { "distribution" });

				break;
				// 距离降序 已经废弃 但是留着 万一以后又要
			case 1:
				result = loadPlantListForWX(list, phoneLocation);
				ListUtils.sort(result, false, new String[] { "distribution" });
				break;
				//光伏由大到小
			case 2:
				List<Integer> pvList = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("1".equals(plantType)) {
						pvList.add(pid);
					}
				}
				result = loadPlantListForWX(pvList, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, false, new String[] { "capacity" });
				}
				break;
				//光伏由小到大
			case 3:
				List<Integer> pvList2 = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("1".equals(plantType)) {
						pvList2.add(pid);
					}
				}
				result = loadPlantListForWX(pvList2, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, true, new String[] { "capacity" });
				}
				break;
				//储能由小到大
			case 4:
				List<Integer> energyList = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("2".equals(plantType)) {
						energyList.add(pid);
					}
				}
				result = loadPlantListForWX(energyList, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, false, new String[] { "energyStorage" });
				}
				break;
				//储能由小到大
			case 5:
				List<Integer> energyList2 = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("2".equals(plantType)) {
						energyList2.add(pid);
					}
				}
				result = loadPlantListForWX(energyList2, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, true, new String[] { "energyStorage" });
				}
				break;
				//发电由高到底
			case 6:
				List<Integer> pvList3 = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("1".equals(plantType)) {
						pvList3.add(pid);
					}
				}
				result = loadPlantListForWX(pvList3, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, false, new String[] { "genToday" });
				}
				break;
				//发电由低到高
			case 7:
				List<Integer> pvList4 = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("1".equals(plantType)) {
						pvList4.add(pid);
					}
				}
				result = loadPlantListForWX(pvList4, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, true, new String[] { "genToday" });
				}
				break;
				//SOC由低到高
			case 8:
				List<Integer> energyList3 = new ArrayList<>();
				for (Integer pid : list) {
					String plantType = SystemCache.plantTypeList.get(String.valueOf(pid));
					if ("2".equals(plantType)) {
						energyList3.add(pid);
					}
				}
				result = loadPlantListForWX(energyList3, phoneLocation);
				if (Util.isNotBlank(result)) {
					ListUtils.sort(result, true, new String[] { "soc" });
				}
				break;
				//只看运行
			case 9:
				List<Integer> status = new ArrayList<>();
				for (Integer pid : list) {
					String plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pid)) + "";
					if ("0".equals(plantStatus)) {
						status.add(pid);
					}
				}
				result = loadPlantListForWX(status, phoneLocation);
				break;
				//只看待机
			case 10:
				List<Integer> status2 = new ArrayList<>();
				for (Integer pid : list) {
					String plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pid)) + "";
					if ("1".equals(plantStatus)) {
						status2.add(pid);
					}
				}
				result = loadPlantListForWX(status2, phoneLocation);
				break;
				//只看故障
			case 11:
				List<Integer> status3 = new ArrayList<>();
				for (Integer pid : list) {
					String plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pid)) + "";
					if ("2".equals(plantStatus)) {
						status3.add(pid);
					}
				}
				result = loadPlantListForWX(status3, phoneLocation);
				break;
				//只看断连
			case 12:
				List<Integer> status4 = new ArrayList<>();
				for (Integer pid : list) {
					String plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pid)) + "";
					if ("3".equals(plantStatus)) {
						status4.add(pid);
					}
				}
				result = loadPlantListForWX(status4, phoneLocation);
				break;
				//只看异常
			case 13:
				List<Integer> status5 = new ArrayList<>();
				for (Integer pid : list) {
					String plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pid)) + "";
					if ("4".equals(plantStatus)) {
						status5.add(pid);
					}
				}
				result = loadPlantListForWX(status5, phoneLocation);
				break;
				//默认
			default:
				result = loadPlantListForWX(list, phoneLocation);
				break;
			}
			if (Util.isNotBlank(result)) {
				int maxValue = length + offset;
				int len = result.size();
				if (offset > len) {
					offset = len;
				}
				if (maxValue > len) {
					maxValue = len;
				}
				if (maxValue > 0) {
					result = result.subList(offset, maxValue);
				}
			}
			msg.setBody(result);
			msg.setCode(Header.STATUS_SUCESS);
		} else {
			msg.setBody(Header.STATUS_SUCESS);
			msg.setMsg(Msg.NO_DATA);
		}
		return msg;
	}

	public List<PlantListOrder> loadPlantListForWX(List<Integer> plantIds, String phoneLocation)
			throws ServiceException {
		Double d1 = null, d2 = null;
		if (Util.isNotBlank(phoneLocation)) {
			String[] split = phoneLocation.split(",");
			d1 = Double.parseDouble(split[0]);
			d2 = Double.parseDouble(split[1]);
		}
		List<PlantListOrder> resultList = null;
		if (Util.isNotBlank(plantIds)) {
			resultList = new ArrayList<>(plantIds.size());
			List<PlantInfo> plants = plantMapper.getPlantByIds(plantIds);
			if (Util.isNotBlank(plants)) {
				for (PlantInfo plantInfo : plants) {
					String plantId = String.valueOf(plantInfo.getId());
					PlantListOrder plantsOrder = new PlantListOrder();
					plantsOrder.setAddr(plantInfo.getPlantAddr());
					String loaction = plantInfo.getLoaction();
					String[] location = loaction.split(",");
					Double d01 = Double.parseDouble(location[0]);
					Double d02 = Double.parseDouble(location[1]);
					double distance = -0.99999999;
					if (d1 != null && d2 != null) {
						distance = GetDistance(d2, d1, d02, d01);
					}
					plantsOrder.setDistribution(distance);
					plantsOrder.setName(plantInfo.getPlantName());
					String photo = StringUtil.checkBlank(plantInfo.getPlantPhoto());
					String[] photos = StringUtils.split(photo, ";");
					String pic = null;
					/** 微信小程序默认返回第一张图片 */
					if (Util.isNotBlank(photos)) {
						pic = photos[0];
					}
					plantsOrder.setPic(pic);
					plantsOrder.setPlantId(plantInfo.getId());
					plantsOrder.setType(ServiceUtil.getPlantStatus(plantId));
					String plantType = SystemCache.plantTypeList.get(plantId);
					String util = plantInfo.getUnit();
					double capatity = plantInfo.getCapacity();
					plantsOrder.setPlantType(Integer.parseInt(plantType));
					// 处理储能电站与光伏电站的区别
					if ("1".equals(plantType)) {
						if ("MW".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000, 1);
						} else if ("GW".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000000, 1);
						}
						plantsOrder.setCapacity(capatity);
						plantsOrder.setUnit("kw");
						Map<String, Object> nowDayIncome = statisticalGainService.getNowDayIncome(plantId);
						String genDay = "0";
						if (Util.isNotBlank(nowDayIncome)) {
							genDay = String.valueOf(nowDayIncome.get("power"));

						}
						plantsOrder.setGenToday(
								Double.parseDouble(Util.getDecimalFomat2().format(Double.parseDouble(genDay))));
						plantsOrder.setGenTodayUnit("度");
					} else if ("2".equals(plantType)) {
						if ("MWH".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000, 1);
						} else if ("GWH".equalsIgnoreCase(util)) {
							capatity = Util.multFloat(capatity, 1000000, 1);
						}
						plantsOrder.setEnergyStorage(capatity);
						plantsOrder.setEnergyStorageUnit("kWh");
						plantsOrder.setSoc(SystemCache.plantSOC.get(plantId));
					}
					SysWeather plantWeather = plantInfoService.getPlantTodayWeather(plantInfo.getId());
					plantsOrder.setWeather(plantWeather.getWeather());
					plantsOrder.setWeatherPic(plantWeather.getDayPictureUrl());
					resultList.add(plantsOrder);
				}
			}
		}
		return resultList;
	}

	public static void main(String[] args) {
		final String baseFinalStr = "baseStr";
		String baseStr = "baseStr";
		String str1 = "baseStr01";
		String str2 = "baseStr" + "01";
		String str3 = baseStr + "01";
		String str4 = baseFinalStr + "01";
		String str5 = new String("baseStr01").intern();
		Integer i = 100;
		Integer i2 = 100;
		System.out.println(str1 == str2);// #3 true
		System.out.println(str1 == str3);// #4 false
		System.out.println(str1 == str4);// #5 true
		System.out.println(str1 == str5);// #6 true
		System.out.println(i == i2);
		PlantListOrder plantsOrder = new PlantListOrder();
		System.out.println(plantsOrder.getStatus());
	}
}
