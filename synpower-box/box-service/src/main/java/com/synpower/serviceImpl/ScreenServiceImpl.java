package com.synpower.serviceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.CollDevice;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysAddress;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysScreen;
import com.synpower.dao.DeviceDetailCameraMapper;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.DeviceSeriesModuleDetailMapper;
import com.synpower.dao.InventerStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.PowerPriceMapper;
import com.synpower.dao.SysAddressMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysScreenMapper;
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
import com.synpower.service.ScreenService;
import com.synpower.service.StatisticalGainService;
import com.synpower.util.RegionNode;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;
import com.synpower.util.ApiUtil;
import com.synpower.util.PropertiesUtil;

@Service
public class ScreenServiceImpl implements ScreenService {
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysScreenMapper screenMapper;
	@Autowired
	private InventerStorageDataMapper inventerStorageDataMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private StatisticalGainService statisticalGainService;
	@Autowired
	private PowerPriceMapper priceMapper;
	@Autowired
	private SysAddressMapper addMapper;
	@Autowired
	private MonitorServiceImpl monitor;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private MonitorServiceImpl monitorService;
	@Autowired
	private DeviceSeriesModuleDetailMapper deviceSeriesModuleDetailMapper;
	@Autowired
	private DeviceDetailCameraMapper cameraMapper;
	@Autowired
	private PlantInfoServiceImpl plantInfoServiceImpl;

	public Map<String, Object> getCommonPart(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		// 查出当前登录人的组织id有哪些
		reList.add(((SysOrg) u.getUserOrg()).getId() + "");
		reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
		resultMap.put("user", u);
		resultMap.put("list", reList);
		resultMap.put("orgList", orgList);
		return resultMap;
	}

	/**
	 * @Title: getPlantCount
	 * @Description: 电站规模
	 * @param str
	 * @return
	 * @throws ServiceException
	 * @throws SessionException
	 * @throws SessionTimeoutException: MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月7日上午9:50:38
	 */
	@Override
	public MessageBean getPlantCount(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(map.get("tokenId") + "");
		// 查询当前用户所关联的电站id
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		if (Util.isNotBlank(plantIdList)) {
			List<PlantInfo> list = plantMapper.getPlantByIds(plantIdList);
			// 获取当前登录用户和该用户组织下的组织id
			List<Double> capacityList = new ArrayList<Double>();
			if (list != null) {
				for (PlantInfo plantInfo : list) {
					if (plantInfo.getUnit().equalsIgnoreCase("MW")) {
						capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000, 0));
					} else if (plantInfo.getUnit().equalsIgnoreCase("GW")) {
						capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000000, 0));
					} else {
						capacityList.add(plantInfo.getCapacity());
					}
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
			resultList.add(firstMap);
			Map<String, Object> secondMap = new HashMap<String, Object>();
			secondMap.put("name", "5-10kW");
			secondMap.put("value", second);
			resultList.add(secondMap);
			Map<String, Object> thirdMap = new HashMap<String, Object>();
			thirdMap.put("name", "10kW-1MW");
			thirdMap.put("value", third);
			resultList.add(thirdMap);
			Map<String, Object> fourthMap = new HashMap<String, Object>();
			fourthMap.put("name", "1MW-10MW");
			fourthMap.put("value", fourth);
			resultList.add(fourthMap);
			resultMap.put("datas", resultList);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}

		return msg;
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
					if (screen.getPlantAutomaticCalculation().equals("0")) {
						totalCapacity = screen.getPlantArtificialCapacity() + "";
						totalCapacity = Util.roundDouble(totalCapacity);
						resultMap.put("totalCapacity", totalCapacity + screen.getCapacityUnit());
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
			// 进行单位换算
			Map<String, Object> changeMap = ServiceUtil.changeUnit(new String[] { "kW", "MW", "GW" }, totalCapacity,
					1000);
			resultMap.put("totalCapacity",
					String.valueOf(changeMap.get("value")) + String.valueOf(changeMap.get("unit")));
		} else {
			resultMap.put("totalCapacity", "0kW");
		}
		return resultMap;
	}

	/**
	 * @Title: getOrgIntroduction
	 * @Description: 公司简介
	 * @param str
	 * @return
	 * @throws ServiceException
	 * @throws SessionException
	 * @throws SessionTimeoutException: MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月7日下午3:14:40
	 */
	@Override
	public MessageBean getOrgIntroduction(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		SysOrg so = orgMapper.getOrgIntroduction(((SysOrg) (u.getUserOrg())).getId() + "");
		Map<String, String> resultMap = new HashMap<String, String>();
		if (null != so) {
			resultMap.put("title", so.getOrgName());
			resultMap.put("description", so.getOrgDesc());
			resultMap.put("image", so.getOrgPhoto());
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_NULL_ERROR);
			msg.setMsg(Msg.ORG_INFO_IS_NULL);
		}
		return msg;
	}

	/**
	 * @Title: listPlantDistribution
	 * @Description: 电站分布
	 * @param str
	 * @return
	 * @throws ServiceException
	 * @throws SessionException
	 * @throws SessionTimeoutException: MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月8日下午3:47:30
	 */
	@Override
	public MessageBean listPlantDistribution(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String tokenId = String.valueOf(map.get("tokenId"));
		String userType = u.getUserType();
		if ("0".equals(userType)) {
			// 得到所有组织列表
			List<SysOrg> orgList = orgMapper.getAllOrg();
			List<String> reList = new ArrayList<>();
			SysOrg org = ((SysOrg) u.getUserOrg());
			// 查出当前登录人的组织id有哪些
			reList.add(((SysOrg) u.getUserOrg()).getId() + "");
			reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
			// 查出当前登录人的组织fatherId
			List<SysOrg> fatherList = new ArrayList<>();
			fatherList = ServiceUtil.getOrgFatherTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", fatherList);
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			String screenId = null;
			String orgType = org.getOrgType();
			if ("0".equals(orgType)) {
				screenId = String.valueOf(org.getId());
			} else {
				if (Util.isNotBlank(fatherList)) {
					int size = fatherList.size();
					for (int i = 0; i < size; i++) {
						SysOrg orgTemp = fatherList.get(i);
						if ("0".equals(orgTemp.getOrgType())) {
							screenId = String.valueOf(orgTemp.getId());
							break;
						}
					}
				} else {
					screenId = String.valueOf(org.getId());
				}
				// 获得区域级别
			}
			SysScreen screen = screenMapper.getScreenByOrgId(screenId);
			if (screen != null) {
				if (screen.getLevel() != null) {
					parameterMap.put("level", screen.getLevel());
				} else {
					parameterMap.put("level", 2);
				}
			} else {
				parameterMap.put("level", 2);
			}
			List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			if (Util.isNotBlank(plantIdList)) {
				parameterMap.put("reList", plantIdList);
				List<Map<String, Object>> listMap = plantMapper.listPlantDistribution(parameterMap);
				for (Map<String, Object> map2 : listMap) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("name", map2.get("name"));
					if (Double.valueOf(map2.get("sum") + "") > 1000) {
						Double sum = Util.divideDouble(Double.valueOf(map2.get("sum") + ""), 1000, 1);
						resultMap.put("value", sum);
						resultMap.put("unit", "MW");
					} else {
						resultMap.put("value", map2.get("sum"));
						resultMap.put("unit", "kW");
					}
					resultList.add(resultMap);
				}
			}
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultList);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else if ("1".equals(userType)) {
			// 查询当前用户所关联的电站id
			List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			if (plantIdList.size() > 0) {
				List<Map<String, Object>> listMap = plantMapper.listPlantDistributionByPlantId(plantIdList);
				// 组装前台数据
				for (Map<String, Object> map2 : listMap) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("name", map2.get("name"));
					if (Double.valueOf(map2.get("sum") + "") > 1000) {
						Double sum = Util.divideDouble(Double.valueOf(map2.get("sum") + ""), 1000, 1);
						resultMap.put("value", sum);
						resultMap.put("unit", "MW");
					} else {
						resultMap.put("value", map2.get("sum"));
						resultMap.put("unit", "kW");
					}
					resultList.add(resultMap);
				}
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultList);
				msg.setMsg(Msg.SELECT_SUCCUESS);
			} else {
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultList);
			}

		}
		return msg;
	}

	@Override
	public MessageBean getBasicStatics(String jsonData, Session session, HttpServletRequest request)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String, Object>(1);
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		User u = session.getAttribute(tokenId);
		// 当前用户对应orgId
		List<Integer> list = plantInfoService.getPlantsForUser(tokenId, session, 1);
		double result = 0;
		if (Util.isNotBlank(list)) {
			result = getPowerTotal(list).get("power");
		}
		resultMap.put("totalGen", result);
		resultMap.put("totalGenUnity", "kWh");
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
//		MessageBean msg = new MessageBean();
//		Map<String, Object> map = Util.parseURL(jsonData);
//		User u = session.getAttribute(map.get("tokenId")+"");
//		/*//得到所有组织列表
//		List<SysOrg>orgList = orgMapper.getAllOrg();
//		List<String> reList = new ArrayList<>(10);
//		//获取当前用户所在组织及其子组织ids
//		reList.add(((SysOrg)u.getUserOrg()).getId()+"");
//		reList = ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);*/
//		//List<Integer> plants=plantMapper.getPlantsForOrg(reList);
//		String tokenId = map.get("tokenId")+"";
//		//当前用户对应orgId
//		String orgId = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
//		List<Integer> list = plantInfoService.getPlantsForUser(tokenId, session, 0);
//		Map<String, Object> map2 = new HashMap<>();
//		//获取logo、名称
//		SysOrg sysOrg = orgMapper.getlogo(orgId);			
//		String orgName = sysOrg.getScreenName();
//		String orgLogo = sysOrg.getScreenLogo();	
//		map2.put("logoName", orgName);
//		map2.put("logoImg", orgLogo);
//		long currentTimeMillis = System.currentTimeMillis();
//		map2.put("screenTime", currentTimeMillis);
//		//指定格式返回当前时间
//		double result=0;
//		if (Util.isNotBlank(list)) {
//			String temp=deviceMapper.getPower(list);
//			//Map<String, Object>paramMap=new HashMap<>(2);
//			//paramMap.put("reList", reList);
//			//List<PlantInfo> plantList = plantMapper.getScreenPlantCount(paramMap);
//			Map<String, Double> historyMap = new HashMap<String, Double>(5);
//			historyMap.put("totalPower", 0.0);
//			List<PlantInfo> plantList = plantMapper.getPlantByIds(list);
//			/*for (PlantInfo plantInfo : plantList) {
//				int isAmmeter = 0;
//				int isInverter =0;
//				List<CollDevice> deviceList = deviceMapper.listDevicesByPlantId(plantInfo.getId()+"");
//				for (CollDevice collDevice : deviceList) {
//					//记录是否有电表
//					if(collDevice.getDeviceType() == 3){
//						isAmmeter++;
//					}
//					if(collDevice.getDeviceType() == 1 || collDevice.getDeviceType() == 2){
//						isInverter++;
//					}
//				}
//				//先判断有没有电表，若没有再判断有没有逆变器
//				if(isAmmeter != 0){
//					
//				}else{
//					if(isInverter != 0){
//						Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantInfo.getId()+"");
//						List<InventerStorageData> inventerList = inventerMapper.getEnergyPrice(plantInfo.getId()+"");
//						for (InventerStorageData inventerStorageData : inventerList) {
//							historyMap.put("totalPower", Util.addFloat(historyMap.get("totalPower"), inventerStorageData.getTotalEnergy(), 0));
//							if(todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")){
//								if(historyMap.containsKey("totalPower")){
//									historyMap.put("totalPower", Util.addFloat(historyMap.get("totalPower"), Double.valueOf(todayPricePower.get("power")+""), 0));
//								}
//							}
//						}
//					}
//				}
//			}*/
////			result=getPowerTotal(list).get("power");
//		} 
//		
////		map2.put("totalGen", result);
////		map2.put("totalGenUnity", "kWh");
//		msg.setBody(map2);
//		msg.setCode(Header.STATUS_SUCESS);
//		msg.setMsg(Msg.GET_SCREEN_INFO_SUCCUESS);
//		return msg;
	}

	@Override
	public MessageBean getBusiness(String jsonData, Session session, HttpServletRequest request)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// String tokenId = map.get("tokenId")+"";
		User u = session.getAttribute(map.get("tokenId") + "");
		// 当前用户对应orgId
		String orgId = ((SysOrg) u.getUserOrg()).getId().intValue() + "";
		// mapper.getFatherId(orgId);
		/*
		 * List<SysOrg>orgList=orgMapper.getAllOrg(); List<String> li = new
		 * ArrayList<>(); //获取该组织最顶层父id li = ServiceUtil.getFatherTree(orgList,
		 * orgId,li); String id = (String) li.get(0);
		 * 
		 * List<String> reList = new ArrayList<>();
		 * reList.add(((SysOrg)u.getUserOrg()).getId()+""); reList =
		 * ServiceUtil.getTree(orgList,id,reList);
		 */
		map.clear();
		map.put("reList", orgId);
		SysOrg sysOrg = orgMapper.getEverryNum(map);
		// 分布得到监控、运维、售后、角色及总的人数
		Integer controlNum = 0;
		Integer serviceNum = 0;
		Integer businessNum = 0;
		Integer roleNum = 0;
		String control = sysOrg.getControlNum();
		String service = sysOrg.getServiceNum();
		String business = sysOrg.getBusinessNum();
		String role = sysOrg.getRoleNum();
		if (Util.isNotBlank(control)) {
			controlNum = Integer.parseInt(control);
		}
		if (Util.isNotBlank(service)) {
			serviceNum = Integer.parseInt(service);
		}
		if (Util.isNotBlank(business)) {
			businessNum = Integer.parseInt(business);
		}
		if (Util.isNotBlank(role)) {
			roleNum = Integer.parseInt(role);
		}
		Integer total = controlNum + serviceNum + businessNum + roleNum;
		// 封装人数分布并返回结果
		Map<String, Object> map2 = new HashMap<>();
		map2.put("controlName", sysOrg.getControlName());
		map2.put("controlNum", controlNum);
		map2.put("serviceName", sysOrg.getServiceName());
		map2.put("serviceNum", serviceNum);
		map2.put("businessName", sysOrg.getBusinessName());
		map2.put("businessNum", businessNum);
		map2.put("roleName", sysOrg.getRoleName());
		map2.put("roleNum", roleNum);
		map2.put("totalNum", total);
		msg.setBody(map2);
		msg.setMsg(Msg.PLANT_OPERATE_INFO_SUCCUESS);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getPlantDistribution(String jsonData, Session session, HttpServletRequest request)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		/*
		 * String tokenId =String.valueOf(map.get("tokenId")); User u =
		 * session.getAttribute(tokenId); //获取组件块功率 Integer eachPower = (Integer)
		 * map.get("eachPower"); eachPower = 5;
		 */
		// 当前用户orgId
		// String orgId = ((SysOrg)u.getUserOrg()).getId().intValue()+"";
		// 得到所有组织列表
		// List<SysOrg>orgList=orgMapper.getAllOrg();
		// List<String>reList=new ArrayList<>();
		// 查出当前登录人的组织id有哪些
		// reList.add(((SysOrg)u.getUserOrg()).getId()+"");
		// reList = ServiceUtil.getTree(orgList,
		// ((SysOrg)u.getUserOrg()).getId()+"",reList);
		String tokenId = map.get("tokenId") + "";

		// 查询当前组织下总组件块数
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);
		Map<String, Object> map2 = new HashMap<>();
		if (plants.size() > 0) {
			parameterMap.put("plants", plants);
			parameterMap.put("types", new int[] { 1, 2 });
			List<Integer> deviceList = deviceMapper.getDeviceIdByPlants(parameterMap);
			int totalCount = 0;
			if (Util.isNotBlank(deviceList)) {
				totalCount = deviceSeriesModuleDetailMapper.getModuleCountForDevices(deviceList);
			}
			parameterMap.put("reList", plants);
			// 查询当前组织下总电站面积
			String totalArea = plantMapper.getTotalArea(parameterMap);

			Integer inverterCount = deviceMapper.getInverterCount4P(parameterMap);
			map2.put("partCount", totalCount);
			if (totalArea == null) {
				map2.put("plantArea", "--");
			}
			map2.put("plantArea", totalArea);
			map2.put("inverterCount", inverterCount);
			msg.setBody(map2);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.DEVICE_DISTRIBUTION_SUCCUESS);
		} else {
			msg.setBody(map2);
			msg.setCode(Header.STATUS_SUCESS);
		}

		return msg;
	}

	@Override
	public MessageBean getPowerBar(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 获取当前登录用户和该用户组织下的组织id
		// Map<String, Object> commonMap =getCommonPart(str, session);
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = map.get("tokenId") + "";
//		List<Integer> list = plantInfoService.getPlantsForUser(tokenId, session, 1);
		Map<String, List<Integer>> plantIdMap = plantInfoServiceImpl.getPlantsTypeForUser(tokenId, session, 1);
		List<Integer> list = plantIdMap.get("1");
		Map<String, Object> body = new HashMap<String, Object>();
		if (list.size() > 0) {
			// 获取12个月的月份
			String[] mon = new String[12];
			for (int i = 11; i >= 0; i--) {
				mon[11 - i] = TimeUtil.getPastMonAndYear(i);
			}
			Calendar cal = Calendar.getInstance();
			// 得到当前时间的年份
			int yearNow = cal.get(Calendar.YEAR);
			// 得到当前时间的月份
			int monthNow = cal.get(Calendar.MONTH) + 1;
			// Map<String, Object> countMap = new HashMap<String,Object>();
			// countMap.put("reList", commonMap.get("list"));
			// 存放查询出来的发电量的数据
			Map<String, Double> generatingMap = new TreeMap<String, Double>();
			// 存放查询出来的收益的数据
			Map<String, Double> priceMap = new TreeMap<String, Double>();
			// 存放查询出来的当年发电量的数据
			Map<String, Double> powerYear = new TreeMap<String, Double>();
			powerYear.put(yearNow + "", 0.0);
			// 存放查询出来的当月发电量的数据
			Map<String, Double> powerMonth = new TreeMap<String, Double>();
			powerMonth.put(monthNow + "", 0.0);
			// 给发电量和收益的map集合中设置初始值
			for (String string : mon) {
				generatingMap.put(string, 0.0);
			}
			for (String string : mon) {
				priceMap.put(string, 0.0);
			}
			Map<String, String> powerPriceMap = new TreeMap<String, String>();
			Map<String, Object> timeMap = new TreeMap<String, Object>();
			// 查询出当前公司下所有的电站
			// List<PlantInfo> plantList = plantMapper.getScreenPlantCount(countMap);
			List<PlantInfo> plantList = plantMapper.getPlantByIds(list);
			for (PlantInfo plantInfo : plantList) {
				powerPriceMap.put("date", "%Y-%m");
				powerPriceMap.put("plantId", plantInfo.getId() + "");
				powerPriceMap.put("startTime", mon[0]);
				powerPriceMap.put("endTime", mon[mon.length - 1]);
				List<Map<String, Object>> GeneratingList = inventerStorageDataMapper.getPowerPriceByDay(powerPriceMap);
				for (Map<String, Object> map2 : GeneratingList) {
					Double oldNum = generatingMap.get(map2.get("time") + "");
					generatingMap.put(map2.get("time") + "",
							Util.addFloat(oldNum, Double.valueOf(map2.get("sum") + ""), 0));
					Double oldPrice = priceMap.get(map2.get("time") + "");
					priceMap.put(map2.get("time") + "",
							Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
				}
				timeMap.put("date", "%Y");
				timeMap.put("plantId", plantInfo.getId() + "");
				timeMap.put("time", yearNow);
				String yearPower = inventerStorageDataMapper.getPowerByYear2(timeMap);
				if (yearPower == null || yearPower.equals("")) {
					yearPower = "0.0";
				}
				powerYear.put(yearNow + "", Util.addFloat(powerYear.get(yearNow + ""), Double.valueOf(yearPower), 0));
				timeMap.put("date", "%Y-%c");
				timeMap.put("plantId", plantInfo.getId() + "");
				timeMap.put("time", yearNow + "-" + monthNow);
				String monthPower = inventerStorageDataMapper.getPowerByYear2(timeMap);
				if (monthPower == null || monthPower.equals("")) {
					monthPower = "0.0";
				}
				powerMonth.put(monthNow + "",
						Util.addFloat(powerMonth.get(monthNow + ""), Double.valueOf(monthPower), 0));
				// 获取当天实时的数据，将数据加到当月上面
				Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantInfo.getId() + "");
				if (todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")) {
					Double oldNum = generatingMap.get(mon[mon.length - 1]);
					generatingMap.put(mon[mon.length - 1],
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("power") + ""), 0));
					powerYear.put(yearNow + "", Util.addFloat(powerYear.get(yearNow + ""),
							Double.valueOf(todayPricePower.get("power") + ""), 0));
					powerMonth.put(monthNow + "", Util.addFloat(powerMonth.get(monthNow + ""),
							Double.valueOf(todayPricePower.get("power") + ""), 0));
				}
				if (todayPricePower.get("inCome") != "null" && !todayPricePower.get("inCome").equals("")) {
					Double oldNum = priceMap.get(mon[mon.length - 1]);
					priceMap.put(mon[mon.length - 1],
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("inCome") + ""), 0));
				}
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			Map<String, Object> month = new HashMap<String, Object>();
			Map<String, Object> year = new HashMap<String, Object>();
			List<Double> generatingResult = new ArrayList<Double>();
			List<Double> priceResult = new ArrayList<Double>();
			for (int i = 0; i < mon.length; i++) {
				generatingResult.add(Double.valueOf(generatingMap.get(mon[i]) + ""));
				priceResult.add(Double.valueOf(priceMap.get(mon[i]) + ""));
			}
			// 判断发电量的最大值是否超过一万，若超过则单位换算
			if (Collections.max(generatingResult) > 10000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < generatingResult.size(); i++) {
					generatingResult.set(i, Util.divideDouble(generatingResult.get(i), 1000, 1));
				}
				datas.put("yData1", generatingResult);
				datas.put("unity1", "MWh");
				datas.put("maxData1", Util.getRetainedDecimal(Collections.max(generatingResult)));
			} else {
				for (int i = 0; i < generatingResult.size(); i++) {
					generatingResult.set(i, Util.getRetainedDecimal(generatingResult.get(i)));
				}
				datas.put("yData1", generatingResult);
				datas.put("unity1", "kWh");
				datas.put("maxData1", Util.getRetainedDecimal(Collections.max(generatingResult)));
			}
			// 判断收益的最大值是否超过一万，若超过则单位换算
			if (Collections.max(priceResult) > 10000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < priceResult.size(); i++) {
					priceResult.set(i, Util.divideDouble(priceResult.get(i), 10000, 1));
				}
				datas.put("yData2", priceResult);
				datas.put("unity2", "万元");
				datas.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
			} else {
				for (int i = 0; i < priceResult.size(); i++) {
					priceResult.set(i, Util.getRetainedDecimal(priceResult.get(i)));
				}
				datas.put("yData2", priceResult);
				datas.put("unity2", "元");
				datas.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
			}
			// 判断年度发电量的值是否超过一万，若超过则单位换算
			if (powerYear.get(yearNow + "") > 10000) {
				powerYear.put(yearNow + "", Util.divideDouble(powerYear.get(yearNow + ""), 1000, 1));
				year.put("value", powerYear.get(yearNow + ""));
				year.put("unity", "MWh");
			} else {
				year.put("value", Util.getRetainedDecimal(powerYear.get(yearNow + "")));
				year.put("unity", "kWh");
			}
			// 判断月度发电量的值是否超过一万，若超过则单位换算
			if (powerMonth.get(monthNow + "") > 10000) {
				powerMonth.put(monthNow + "", Util.divideDouble(powerMonth.get(monthNow + ""), 1000, 1));
				month.put("value", powerMonth.get(monthNow + ""));
				month.put("unity", "MWh");
			} else {
				month.put("value", Util.getRetainedDecimal(powerMonth.get(monthNow + "")));
				month.put("unity", "kWh");
			}
			for (int i = 0; i < mon.length; i++) {
				mon[i] = (mon[i].split("-"))[1] + "月";
			}
			datas.put("xData", mon);
			body.put("datas", datas);
			body.put("year", year);
			body.put("month", month);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(body);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(body);
		}

		return msg;
	}

	@Override
	public MessageBean getContribution(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException, IOException {
		MessageBean msg = new MessageBean();
		// 获取当前登录用户和该用户组织下的组织id
//		Map<String, Object> commonMap =getCommonPart(str, session);
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		// 得到所有组织列表
//		List<SysOrg>orgList=orgMapper.getAllOrg();
//		List<String>reList=new ArrayList<>();
//		//查出当前登录人的组织id有哪些
//		reList.add(((SysOrg)u.getUserOrg()).getId()+"");
//		reList = ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
//		Map<String, Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("reList", reList);
//		List<PlantInfo> plantList = plantMapper.getPlantByOrgIds(paramMap);
		// 查询当前用户所关联的电站id
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		List<Object> resultlist = new ArrayList<Object>();
		if (Util.isNotBlank(plantIdList)) {
			/*
			 * List<PlantInfo> list = plantMapper.getPlantByIds(plantIdList); List<String>
			 * paramList = new ArrayList<String>(); for (PlantInfo plantInfo : list) {
			 * paramList.add(plantInfo.getId()+""); }
			 */
			// 得到累计发电量
			Double totalPower = getPowerTotal(plantIdList).get("power");
			// 从配置文件中获取二氧化碳减排参数
			String CO2 = configParam.getCo2();
			// 从配置文件中获取等效植树量参数
			String tree = configParam.getTree();
			// 从配置文件中获取标准煤节约参数
			String coal = configParam.getCoal();
			Double numCo2 = Util.multFloat(totalPower, Double.valueOf(CO2), 1);
			Double numTree = Util.multFloat(totalPower, Double.valueOf(tree), 1);
			Double numCoal = Util.multFloat(totalPower, Double.valueOf(coal), 1);
			Map<String, Object> co2Map = new HashMap<String, Object>();
			co2Map.put("name", "CO²减排");
			co2Map.put("value", numCo2);
			co2Map.put("unity", "kg");
			resultlist.add(co2Map);
			Map<String, Object> treeMap = new HashMap<String, Object>();
			treeMap.put("name", "等效植树");
			treeMap.put("value", Util.roundDoubleToInt(numTree));
			treeMap.put("unity", "棵");
			resultlist.add(treeMap);
			Map<String, Object> coalMap = new HashMap<String, Object>();
			coalMap.put("name", "节约标准煤");
			coalMap.put("value", numCoal);
			coalMap.put("unity", "kg");
			resultlist.add(coalMap);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultlist);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultlist);
		}
		return msg;
	}

	/**
	 * @Title: getPowerTotal
	 * @Description: 获取累计发电量和收益
	 * @param list org集合
	 * @return: Double
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月18日上午10:13:40
	 */
	public Map<String, Double> getPowerTotal(List<Integer> list) {

		Map<String, Double> resultMap = new HashMap<>(2);
		double power = 0, price = 0;
		if (Util.isNotBlank(list)) {
			for (Integer pId : list) {
				Map<String, Object> totalMap = statisticalGainService.getHistoryIncome(String.valueOf(pId));
				power = Util.addFloat(power, Double.parseDouble(String.valueOf(totalMap.get("power"))), 0);
				price = Util.addFloat(price, Double.parseDouble(String.valueOf(totalMap.get("inCome"))), 0);
			}
		}
		resultMap.put("power", Util.roundDouble(power));
		resultMap.put("inCome", Util.roundDouble(price));
		return resultMap;

	}

	public MessageBean dynamicStatics(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String pId = map.get("id") + "";
		/**
		 * 实时功率曲线 功率kpi
		 */
		Map<String, Object> energyStatic = new HashMap<>(3);
		Map<String, Object> genIncome = new HashMap<>(3);
		Map<String, Object> activePower = new HashMap<>(6);
		PlantInfo plantInfo = plantMapper.getPlantInfo(pId);
		String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
		String[] location = StringUtils.split(plantInfo.getLoaction(), ",");
		String riseTime = ServiceUtil.getSunRise(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
		String setTime = ServiceUtil.getSunSet(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
		List<String> timeList = ServiceUtil.getStringTimeSegment(riseTime, setTime);
		map.clear();
		// 获取电站设备集合
		List<CollDevice> deviceList = deviceMapper.getDeviceListByPid(pId);
		// 按照设备类型分类
		Map<Integer, List<Integer>> deviceMap = ServiceUtil.classifyDeviceForPlant(deviceList);
		// 将对应电站的对应时间点的功率装载到时间点位上
		// 功率
		String curTime = ServiceUtil.getNowTimeInTimes();
		int nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
		Map<String, Object> result = statisticalGainService.getPowerAndDataTime(riseTime, setTime, deviceMap, pId,
				nowLocation);
		String[] yData1 = (String[]) result.get("yData1");
		// 单位
		String unit = String.valueOf(result.get("unit"));
		if (nowLocation < yData1.length) {
			String str = String.valueOf(statisticalGainService.getNowDayIncome(pId).get("activcePower"));
			double power = "".equals(StringUtil.checkBlank(str)) ? 0 : Double.parseDouble(str);
			if ("兆瓦".equals(unit)) {
				yData1[nowLocation] = String
						.valueOf(new BigDecimal(power / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			} else if ("吉瓦".equals(unit)) {
				yData1[nowLocation] = String
						.valueOf(new BigDecimal(power / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		activePower.put("yData1", yData1);
		activePower.put("unit", unit);
		// 时间轴
		Object[] xData = timeList.toArray();
		activePower.put("xData", xData);
		// 当前时间
		activePower.put("curTime", curTime);
		double maxValue = Double.parseDouble(result.get("maxValue") + "");
		double value = Double.valueOf(yData1[nowLocation]);
		if (maxValue == 0.0) {
			maxValue = 0.5;
		}
		if (maxValue < value) {
			maxValue = value;
		}
		String[] yData2 = ServiceUtil.getParabola(yData1, maxValue);
		// 抛物线
		activePower.put("yData2", yData2);
		// 当前抛物线高度
		activePower.put("curData2", yData2[nowLocation]);
		/**
		 * 仪盘表
		 */
		// 当前功率值
		// 所有设备集合
		List<Integer> devices = (List<Integer>) result.get("devices");
		// 装机电量与当前发电量单位同步
		double capacity = plantInfo.getCapacity();
		if ("兆瓦".equalsIgnoreCase(unit)) {
			value = Util.multDouble(String.valueOf(value), "1000", 1);
		} else if ("吉瓦".equalsIgnoreCase(unit)) {
			value = Util.multDouble(String.valueOf(value), "1000000", 1);
		}
		String captityUnit = plantInfo.getUnit();
		if ("MW".equalsIgnoreCase(captityUnit)) {
			value = Util.divideDouble(value, 1000, 1);
		} else if ("GW".equalsIgnoreCase(captityUnit)) {
			value = Util.divideDouble(value, 1000000, 1);
		}
		energyStatic.put("unit", captityUnit);
		energyStatic.put("capacity", capacity);
		energyStatic.put("value", value);
		// 0:正常发电中，1：待机，2：故障，3：中断 4异常
		int type = ServiceUtil.getPlantStatus(pId);
		String[] status = new String[] { "正常发电中", "待机", "故障", "中断", "异常" };
		energyStatic.put("res", status[type]);
		energyStatic.put("type", type);
		/**
		 * 发电量与收益
		 */
		Map<String, Object> genIncomMap = statisticalGainService.getNowDayIncome(pId);

		// 今日发电

		genIncome.put("genToday", Util.roundDouble(genIncomMap.get("power"), 2));
		// 今日发电单位
		genIncome.put("genTodayUnity", "度");
		// 今日收益
		// 单位换算
		genIncome.put("income", Util.roundDouble(genIncomMap.get("inCome"), 2));

		// 今日单位
		genIncome.put("incomeUnity", "元");
		// 总发电量
		Map<String, Object> tempMap = statisticalGainService.getHistoryIncome(pId);
		genIncome.put("totalGen", Util.roundDouble(tempMap.get("power"), 2));

		genIncome.put("totalGenUnity", "度");

		// 总收益
		genIncome.put("totalIncome", Util.roundDouble(tempMap.get("inCome"), 2));

		// 总收益单位
		genIncome.put("totalIncomeUnity", "元");
		map.put("activePower", activePower);
		map.put("energyStatic", energyStatic);
		map.put("genIncome", genIncome);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getAllPlant(Session session, String jsonData, HttpServletRequest request)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId") + "");
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		// 判断是否有选中的组织id，若没有则获取当前登录用户的组织id
		reList.add(((SysOrg) u.getUserOrg()).getId() + "");
		reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
		Map<String, Object> newMap = new HashMap<String, Object>();
		// System.out.println(reList);
		newMap.put("reList", reList);
		List<PlantInfo> listMap = plantMapper.getAreaPlant(newMap);
		List<Map<String, Object>> ListFront = new ArrayList<Map<String, Object>>();
		for (PlantInfo plantInfo : listMap) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("id", plantInfo.getId());
			// String sp = mapResult.get("loaction").toString();
			String[] split = plantInfo.getLoaction().split(",");
			String temp = split[1];
			split[1] = split[0];
			split[0] = temp;
			resultMap.put("location", split);
			resultMap.put("name", plantInfo.getPlantName());
			resultMap.put("type", 1);
			ListFront.add(resultMap);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(ListFront);
		msg.setMsg("获取所有电站信息成功");
		return msg;
	}

	@Override
	public MessageBean districtManage(Session session, String jsonData, HttpServletRequest request)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 获得当前用户
		// User u=session.getAttribute(map.get("tokenId")+"");
		// List<SysOrg> orgList = orgMapper.getAllOrg();
		/*
		 * List<String> reList = new ArrayList<>(); //获取当前用户所在组织及其子组织ids
		 * reList.add(((SysOrg)u.getUserOrg()).getId()+""); reList =
		 * ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
		 */
		String tokenId = map.get("tokenId") + "";
		List<Integer> list = plantInfoService.getPlantsForUser(tokenId, session, 1);
		List<Map<String, Object>> ListFront = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			// 区域id
			String dId = map.get("id") + "";
			// 当前区域类型
			Integer tId = null;
			if (map.get("status") != null) {
				tId = Integer.parseInt(map.get("status") + "");
			}
			map.clear();
			List<String> locationList = new ArrayList<>();
			// 为单电站弹窗信息时
			if (tId == 2) {
				MessageBean msg2 = dynamicStatics(jsonData, session);
				return msg2;
				// 区域id为空且类型区域类型为0时 返回根节点信息
			} else if (tId == 0 && dId != null && !dId.equals("")) {
				// 得到组织所对应的电站集合
				// List<Integer> plants=plantMapper.getPlantsForOrg(reList);
				List<SysAddress> adrs = addMapper.getAdrForScreen(list);
				List<RegionNode> result = ServiceUtil.getRegionTree(adrs, dId);
				// ListFront.add(map);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(result);
				msg.setMsg("成功获取区域管理信息");
				return msg;
				// 返回当前节点的区域信息
			} else if (tId == 1) {
				// List<Integer> plants=plantMapper.getPlantsForOrg(list);
				List<SysAddress> adrs = addMapper.getAdrForScreen(list);
				List<Integer> idsList = new ArrayList<>(10);
				for (int i = 0; i < adrs.size(); i++) {
					if (dId.equals(adrs.get(i).getName())) {
						idsList.add(adrs.get(i).getPlantId());
					}
				}
				// 获取当前区域下电站id集合
				// 通过电站ids获取电站信息
				Map<String, Object> newMap = new HashMap<String, Object>();
				List<PlantInfo> listMap = plantMapper.getNowPlants(idsList);
				// 组装数据、返回前台
				for (PlantInfo plantInfo : listMap) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("id", plantInfo.getId());
					String[] split = plantInfo.getLoaction().split(",");
					String temp = split[0];
					split[0] = split[1];
					split[1] = temp;
					resultMap.put("location", split);
					resultMap.put("name", plantInfo.getPlantName());
					resultMap.put("type", 2);
					map.put("hasChild", false);
					ListFront.add(resultMap);
				}
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(ListFront);
				msg.setMsg("成功获取区域管理信息");
				return msg;
			} else {
				SysAddress sysAddress = addMapper.getBackInfo();
				locationList.add(sysAddress.getLocationY());
				locationList.add(sysAddress.getLocationX());
				map.put("location", locationList);
				map.put("id", sysAddress.getLocationX() + sysAddress.getLocationY() + sysAddress.getLevel());
				map.put("name", sysAddress.getAreaName());
				map.put("type", 0);
				map.put("hasChild", true);
				map.put("level", sysAddress.getLevel());
				ListFront.add(map);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(ListFront);
				msg.setMsg("成功获取区域管理信息");
				return msg;
			}
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(ListFront);
			return msg;
		}

	}

	/**
	 * @Title: plantInfo
	 * @Description: 弹窗静态信息
	 * @param jsonData
	 * @param session
	 * @return: MessageBean
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日上午11:09:21
	 */
	@Override
	public MessageBean plantInfo(String jsonData) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		PlantInfo plantInfo = plantMapper.getPlantInfo(String.valueOf(map.get("id")));
		// map.clear();
		map.put("address", plantInfo.getPlantAddr() + plantInfo.getPlantFullAddress());
		map.put("capacity", plantInfo.getCapacity());
		String pId = map.get("id") + "";
		map.put("recentWeather", plantInfoService.getPlantThreeWeather(Integer.parseInt(pId)));
		String photo = plantInfo.getPlantPhoto();
		String[] photos = null;
		if (Util.isNotBlank(photo)) {
			photos = StringUtils.split(photo, ";");
		}
		List<CollDevice> li = deviceMapper.getSerialList(pId);
		Map<String, Object> cameraMap = new HashMap<>();
		List<Map<String, Object>> clist = new ArrayList<>();
		if (Util.isNotBlank(li)) {
			for (CollDevice cameraInfo : li) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("id", cameraInfo.getId());
				eMap.put("name", cameraInfo.getDeviceName());
				eMap.put("sn", cameraInfo.getDeviceSn());
				eMap.put("channel", 1);
				clist.add(eMap);
			}
		}
		cameraMap.put("cameraInfos", clist);
		String appKey = PropertiesUtil.getValue("config.properties", "YS_APP_KEY");
		cameraMap.put("appKey", appKey);
		String token = new String(SystemCache.accessToken);
		cameraMap.put("accessToken", token);
		map.put("img", photos);
		map.put("location", plantInfo.getLoaction());
		map.put("name", plantInfo.getPlantName());
		map.put("remark", plantInfo.getPlantIntroduction());
		map.put("unit", plantInfo.getUnit());
		map.put("cameraInfos", cameraMap);
		map.put("singlePlantType", plantInfo.getPlantTypeA());
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public Map<String, Object> getCurrentPower(String jsonData) throws ServiceException {
		/**
		 * 实时功率曲线 功率kpi
		 */
		Map<String, Object> map = Util.parseURL(jsonData);
		PlantInfo plantInfo = plantMapper.getPlantInfo(map.get("plantId") + "");
		String pId = plantInfo.getId() + "";
		String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
		String[] location = StringUtils.split(plantInfo.getLoaction(), ",");
		String riseTime = ServiceUtil.getSunRise(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
		String setTime = ServiceUtil.getSunSet(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
		List<String> timeList = ServiceUtil.getStringTimeSegment(riseTime, setTime);
		Map<String, Object> activePower = new HashMap<>(6);
		// 获取电站设备集合
		List<CollDevice> deviceList = deviceMapper.getDeviceListByPid(pId);
		// 按照设备类型分类
		Map<Integer, List<Integer>> deviceMap = ServiceUtil.classifyDeviceForPlant(deviceList);
		// 将对应电站的对应时间点的功率装载到时间点位上
		// 功率
		String curTime = ServiceUtil.getNowTimeInTimes();
		int nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
		Map<String, Object> result = statisticalGainService.getPowerAndDataTime(riseTime, setTime, deviceMap, pId,
				nowLocation);
		String[] yData1 = (String[]) result.get("yData1");
		// 单位
		String unit = String.valueOf(result.get("unit"));
		if (nowLocation < yData1.length) {
			String str = String.valueOf(statisticalGainService.getNowDayIncome(pId).get("activcePower"));
			double power = "".equals(StringUtil.checkBlank(str)) ? 0 : Double.parseDouble(str);
			if ("兆瓦".equals(unit)) {
				yData1[nowLocation] = String
						.valueOf(new BigDecimal(power / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			} else if ("吉瓦".equals(unit)) {
				yData1[nowLocation] = String
						.valueOf(new BigDecimal(power / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		activePower.put("yData1", yData1);
		// 时间轴
		Object[] xData = timeList.toArray();
		activePower.put("xData", xData);
		// 当前时间
		activePower.put("curTime", curTime);
		double maxValue = Double.parseDouble(result.get("maxValue") + "");
		if (maxValue == 0.0) {
			maxValue = 0.5;
		}
		/*
		 * if (maxValue<plantInfo.getCapacity().doubleValue()) {
		 * maxValue=plantInfo.getCapacity().doubleValue(); }
		 */
		String[] yData2 = ServiceUtil.getParabola(yData1, maxValue);
		// 抛物线
		activePower.put("yData2", yData2);
		// 当前抛物线高度
		activePower.put("curData2", yData2[nowLocation]);
		activePower.put("unit", unit);
		return activePower;
	}

	@Override
	public MessageBean getLogoAndName(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String, Object>(1);
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = map.get("tokenId") + "";
		User u = session.getAttribute(tokenId);
		// 当前用户对应orgId
		String orgId = ((SysOrg) u.getUserOrg()).getId().intValue() + "";
		// 获取logo、名称
		SysOrg sysOrg = orgMapper.getlogo(orgId);
		String orgName = sysOrg.getScreenName();
		String orgLogo = sysOrg.getScreenLogo();
		resultMap.put("logoName", orgName);
		resultMap.put("logoImg", orgLogo);
		resultMap.put("screenTime", System.currentTimeMillis());
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
}
