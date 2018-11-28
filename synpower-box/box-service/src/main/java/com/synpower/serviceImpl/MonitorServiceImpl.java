package com.synpower.serviceImpl;

import com.synpower.bean.*;
import com.synpower.constant.PTAC;
import com.synpower.dao.*;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.MonitorService;
import com.synpower.service.ScreenService;
import com.synpower.service.StatisticalGainService;
import com.synpower.service.WXmonitorService;
import com.synpower.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MonitorServiceImpl implements MonitorService {
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysScreenMapper screenMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private ElectricStorageDataMapper electricMapper;
	@Autowired
	private StatisticalGainService statisticalGainService;
	@Autowired
	private PowerPriceMapper priceMapper;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private InventerStorageDataMapper inventerStorageDataMapper;
	@Autowired
	private PlantInfoServiceImpl plantInfoServiceImpl;
	@Autowired
	private WXmonitorService wxmonitorService;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private EnergyStorageDataMapper energyMapper;

	/**
	 * @param str     前台传来的字符串
	 * @param session
	 * @return
	 * @throws SessionException
	 * @throws ServiceException
	 * @throws SessionTimeoutException: MessageBean
	 * @Title: getBasicInfo
	 * @Description: 电站基础信息(电站总数和总装机容量)
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月21日下午3:27:04
	 */
	@Override
	public MessageBean getBasicInfo(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(map.get("tokenId") + "");
		// 查询当前用户所关联的电站id
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);

		// 光储区分 此处只需要光伏
		// 光储区分 此处只需要光伏
		List<String> pvList = SystemCache.plantTypeMap.get(String.valueOf(PTAC.PHOTOVOLTAIC));
		if (Util.isNotBlank(pvList)) {
			List<Integer> pvListTemp = new ArrayList<>();
			for (String pv : pvList) {
				pvListTemp.add(Integer.parseInt(pv));
			}
			plantIdList.retainAll(pvListTemp);
		}
		
		if (Util.isNotBlank(plantIdList)) {
			List<PlantInfo> list = plantMapper.getPlantByIds(plantIdList);
			// 获取当前登录用户和该用户组织下的组织id
			List<Double> capacityList = new ArrayList<Double>();
			if (list != null) {
				for (PlantInfo plantInfo : list) {
					double capacity = Util.getDouble(plantInfo.getCapacity());
					if ("MW".equals(plantInfo.getUnit())) {
						capacityList.add(Util.multFloat(capacity, 1000, 0));
					} else {
						capacityList.add(capacity);
					}
				}
			}
			// 查询电站总数
			int totalPlant = plantIdList.size();
			resultMap.put("totalNum", totalPlant);
			resultMap.put("totalNumUnit", "座");
			getTotalPlant(parameterMap, resultMap, u, capacityList);
			// 组装前台数据
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}

	/**
	 * @param parameterMap 参数集合
	 * @param resultMap    结果集合
	 * @param              u: 当前登录用户
	 * @Title: getTotalPlant
	 * @Description: 获取当前用户组织下的电站总数和总装机容量
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
						if (screen.getCapacityUnit().equals("MW")) {
							totalCapacity = Util.multFloat(screen.getPlantArtificialCapacity(), 1000, 0) + "";
						}
						totalCapacity = Util.roundDouble(totalCapacity);
						resultMap.put("totalCap", totalCapacity);
						resultMap.put("unity", "KW");
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
		resultMap.put("totalCap", totalCapacity);
		resultMap.put("unity", "KW");
//		if(totalCapacity != 0){
//			//进行单位换算
//			if(Double.valueOf(totalCapacity)>=1000){
//				//调用util的除法，保留2位小数
//				double totalCapacityD = Util.divideDouble(Double.valueOf(totalCapacity), 1000, 1);
//				resultMap.put("totalCap", totalCapacityD);
//				resultMap.put("unity", "MW");
//			}else{
//				resultMap.put("totalCap", totalCapacity);
//				resultMap.put("unity", "kW");
//			}
//		}else{
//			resultMap.put("totalCap", totalCapacity);
//			resultMap.put("unity", "kW");
//		}
		return resultMap;
	}

	@Override
	public MessageBean getPriceBarByTime(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		Map<String, Object> body = new HashMap<String, Object>();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		// 根据tokenId获取到当前登录的用户
		User u = session.getAttribute(tokenId);
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		if (Util.isNotBlank(plantIdList)) {
			Map<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("reList", plantIdList);
			// 存放查询出来的发电量的数据
			Map<String, Double> powerMap = new TreeMap<String, Double>();
			// 存放查询出来的收益的数据
			Map<String, Double> priceMap = new TreeMap<String, Double>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 获取当天的日期
			String today = sdf.format(new Date());
			List<Double> powerResult = new ArrayList<Double>();
			List<Double> priceResult = new ArrayList<Double>();
			List<Object> xData = new ArrayList<Object>();
			Map<String, String> startMap = new TreeMap<String, String>();
			Map<String, String> endMap = new TreeMap<String, String>();
			// 如果维度没有或者维度为日；那么都用日维度
			if ((dimension == null || dimension.equals("null")) || dimension.equals("day")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y-%m-%d");
				powerPriceMap.put("sdf", "yyyy-MM-dd");
				// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTime(countMap).get("time");
					Date dBegin = sdf.parse(createTime);
					Date dEnd = sdf.parse(today);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					// 判断当前日期与电站接入日期之间的天数，若不足15天则从接入日期开始到当前日期结束
					if (listDate.size() >= 15) {
						// 获取7天日期
						String[] days = new String[15];
						for (int i = 14; i >= 0; i--) {
							days[14 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					} else {
						String[] days = new String[listDate.size()];
						for (int i = 0; i < listDate.size(); i++) {
							days[i] = sdf.format(listDate.get(i));
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					}
					// 如果有起始日期没有结束日期，那么从开始日期起往后推30天，若推到当前天数不足30天，以当前天数为结束
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					// 获取起始日期到当前日期的天数
					long distance = TimeUtil.getDistanceDay(startTime);
					// 判断距离是否有30天
					if (distance < 30) {
						// 获取相差的日期
						String[] days = new String[Integer.valueOf(distance + "") + 1];
						for (int i = 0; i <= Integer.valueOf(distance + ""); i++) {
							days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					} else {
						// 获取相差的日期
						String[] days = new String[30];
						for (int i = 0; i <= 29; i++) {
							days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					}
					// 如果有结束日期没有起始日期，那么从结束日期向前推30天
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTime(countMap).get("time");
					Date dBegin = sdf.parse(createTime);
					Date dEnd = sdf.parse(endTime);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
					if (listDate.size() >= 30) {
						// 获取相差的日期
						String[] days = new String[30];
						for (int i = 29; i >= 0; i--) {
							days[29 - i] = TimeUtil.getSpecifiedDayBefore(endTime, i);
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					} else {
						String[] days = new String[listDate.size()];
						for (int i = 0; i < listDate.size(); i++) {
							days[i] = sdf.format(listDate.get(i));
						}
						// 给两个集合设置初始值
						for (String string : days) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						updateDateFormat(days, xData);
						getStartEnd(days, startMap, endMap);
					}
					// 如果起始日期和结束日期都不为空，
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					Date dBegin = sdf.parse(startTime);
					Date dEnd = sdf.parse(endTime);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 维度为月
			} else if (dimension.equals("month")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y-%m");
				powerPriceMap.put("sdf", "yyyy-MM");
				// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByMon(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
					int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
					if (distanceMon >= 6) {
						// 获取6个月的月份
						String[] mon = new String[6];
						for (int i = 5; i >= 0; i--) {
							mon[5 - i] = TimeUtil.getPastMonAndYear(i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					} else {
						int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
						String[] mon = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					}
					// 如果有起始日期没有结束日期，那么从开始日期起往后推12个月，若推到当前月数不足12个月，以当前月数为结束
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String todayMon = TimeUtil.getPastMonAndYear(0);
					int distance = TimeUtil.countMonths(startTime, todayMon);
					if (distance < 12) {
						String[] mon = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					} else {
						// 获取相差的日期
						String[] mon = new String[12];
						for (int i = 0; i <= 11; i++) {
							mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					}
					// 如果有结束日期没有起始日期，那么从结束日期向前推12个月
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByMon(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
					int distanceMon = TimeUtil.countMonths(createTime, endTime);
					// 判断当前日期与电站接入日期之间的月数，若不足12个月则从接入日期开始到当前日期结束
					if (distanceMon >= 12) {
						// 获取相差的日期
						String[] mon = new String[12];
						for (int i = 11; i >= 0; i--) {
							mon[11 - i] = TimeUtil.getSpecifiedMonBefore(endTime, i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					} else {
						int distance = TimeUtil.countMonths(createTime, endTime);
						String[] mon = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
						}
						// 给两个集合设置初始值
						for (String string : mon) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : mon) {
							xData.add(string);
						}
						getStartEnd(mon, startMap, endMap);
					}
					// 如果起始日期和结束日期都不为空，
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					int distance = TimeUtil.countMonths(startTime, endTime);
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
			} else if (dimension.equals("year")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y");
				powerPriceMap.put("sdf", "yyyy");
				// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByYear(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
					int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
					if (distanceYear >= 5) {
						// 获取5年的年份
						String[] year = new String[5];
						for (int i = 4; i >= 0; i--) {
							year[4 - i] = TimeUtil.getPastYear(i);
						}
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					} else {
						String[] year = new String[distanceYear + 1];
						for (int i = 0; i <= distanceYear; i++) {
							year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
						}
						// 给两个集合设置初始值
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					}
					// 如果有起始日期没有结束日期，那么从开始日期起往后推25年，若推到当前月数不足25年，以当前年份为结束
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
					int distanceYear = TimeUtil.getDateLength(startTime, sdf1.format(new Date()));
					if (distanceYear < 25) {
						String[] year = new String[distanceYear + 1];
						for (int i = 0; i <= distanceYear; i++) {
							year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					} else {
						// 获取相差的日期
						String[] year = new String[25];
						for (int i = 0; i <= 24; i++) {
							year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
						}
						// 给两个集合设置初始值
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					}
					// 如果有结束日期没有起始日期，那么从结束日期向前推25年
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByYear(countMap).get("time");
					int distanceMon = TimeUtil.getDateLength(createTime, endTime);
					// 判断当前日期与电站接入日期之间的年数，若不足25个月则从接入日期开始到当前日期结束
					if (distanceMon >= 25) {
						// 获取相差的日期
						String[] year = new String[25];
						for (int i = 24; i >= 0; i--) {
							year[24 - i] = TimeUtil.getSpecifiedYearBefore(endTime, i);
						}
						// 给两个集合设置初始值
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					} else {
						String[] year = new String[distanceMon + 1];
						for (int i = 0; i <= distanceMon; i++) {
							year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
						}
						// 给两个集合设置初始值
						for (String string : year) {
							powerMap.put(string, 0.0);
							priceMap.put(string, 0.0);
						}
						// 查询出符合条件的数据
						getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
						for (String string : year) {
							xData.add(string);
						}
						getStartEnd(year, startMap, endMap);
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					int distanceYear = TimeUtil.getDateLength(startTime, endTime);
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}

			}
			// 组装前台数据
			List<Object> yData = new ArrayList<Object>();
			body.put("xData", xData);
			if (Collections.max(powerResult) > 1000000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.divideDouble(powerResult.get(i), 1000000, 1));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "GWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
				// 判断发电量的最大值是否超过一万，若超过则单位换算
			} else if (Collections.max(powerResult) > 10000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.divideDouble(powerResult.get(i), 1000, 1));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "MWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
			} else {
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.getRetainedDecimal(powerResult.get(i)));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "kWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
			}
			// 判断收益的最大值是否超过一万，若超过则单位换算
			if (Collections.max(priceResult) > 10000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < priceResult.size(); i++) {
					priceResult.set(i, Util.divideDouble(priceResult.get(i), 10000, 1));
				}
				Map<String, Object> priceResultMap = new HashMap<String, Object>();
				priceResultMap.put("name", "收益");
				priceResultMap.put("value", priceResult);
				yData.add(priceResultMap);
				body.put("unity2", "万元");
				body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
			} else {
				for (int i = 0; i < priceResult.size(); i++) {
					priceResult.set(i, Util.getRetainedDecimal(priceResult.get(i)));
				}
				Map<String, Object> priceResultMap = new HashMap<String, Object>();
				priceResultMap.put("name", "收益");
				priceResultMap.put("value", priceResult);
				yData.add(priceResultMap);
				body.put("unity2", "元");
				body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
			}
			body.put("yData", yData);
			body.put("start", startMap.get("startTime"));
			body.put("end", endMap.get("endTime"));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(body);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(body);
		}
		return msg;
	}

	public void getPowerPriceBar(Map<String, Object> countMap, String[] days, Map<String, Double> powerMap,
			Map<String, Double> priceMap, List<Double> powerResult, List<Double> priceResult,
			Map<String, String> powerPriceMap) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		// 查询出当前公司下所有的电站
//		List<PlantInfo> plantList = plantMapper.getScreenPlantCount(countMap);
		List<Integer> plantIdList = (List<Integer>) countMap.get("reList");
		// List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
		List<PlantInfo> plantList = plantMapper.getPlantByIdsSolarOnly(plantIdList);
		for (PlantInfo plantInfo : plantList) {
			powerPriceMap.put("plantId", plantInfo.getId() + "");
			powerPriceMap.put("startTime", days[0]);
			powerPriceMap.put("endTime", days[days.length - 1]);
			List<Map<String, Object>> GeneratingList = inventerStorageDataMapper.getPowerPriceByDay(powerPriceMap);
			for (Map<String, Object> map2 : GeneratingList) {
				Double oldNum = powerMap.get(map2.get("time") + "");
				powerMap.put(map2.get("time") + "", Util.addFloat(oldNum, Double.valueOf(map2.get("sum") + ""), 0));
				Double oldPrice = priceMap.get(map2.get("time") + "");
				priceMap.put(map2.get("time") + "",
						Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
			}
			// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
			int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
			if (a > 0) {
				Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantInfo.getId() + "");
				if (todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")) {
					Double oldNum = powerMap.get(sdf.format(System.currentTimeMillis()));
					powerMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("power") + ""), 0));
				}
				if (todayPricePower.get("inCome") != "null" && !todayPricePower.get("inCome").equals("")) {
					Double oldNum = priceMap.get(sdf.format(System.currentTimeMillis()));
					priceMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("inCome") + ""), 0));
				}
			}
		}
		for (int i = 0; i < days.length; i++) {
			powerResult.add(Double.valueOf(powerMap.get(days[i]) + ""));
			priceResult.add(Double.valueOf(priceMap.get(days[i]) + ""));

		}
	}

	public void updateDateFormat(String[] days, List<Object> xData) throws ParseException {
		for (String string : days) {
			String week = TimeUtil.getWeek(string);
			String[] monthDay = string.split("-");
			String resultDay = monthDay[1] + "-" + monthDay[2] + " " + week;
			xData.add(resultDay);
		}
	}

	public void updateDateFormat2(String[] days, List<Object> xData) throws ParseException {
		for (String string : days) {
			String week = TimeUtil.getWeek(string);
			String[] monthDay = string.split("-");
			String resultDay = monthDay[1] + "-" + monthDay[2];
			xData.add(resultDay);
		}
	}

	public void getStartEnd(String[] time, Map<String, String> startMap, Map<String, String> endMap) {
		for (int i = 0; i < time.length; i++) {
			if (i == 0) {
				startMap.put("startTime", time[i]);
			}
			if (i == time.length - 1) {
				endMap.put("endTime", time[i]);
			}
		}
	}

	@Override
	public MessageBean getCurveSearchDate(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
//		List<SysOrg>orgList=orgMapper.getAllOrg();
//		//获取当前组织下的子组织
//		List<String>reList=new ArrayList<>();
//		reList.add(((SysOrg)u.getUserOrg()).getId()+"");
//		reList = ServiceUtil.getTree(orgList, ((SysOrg)u.getUserOrg()).getId()+"",reList);
		String tokenId = String.valueOf(map.get("tokenId"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);

		if (Util.isNotBlank(plantIdList)) {
			// 过滤出需要的类型
			String plantType = map.get("plantType").toString();
			ArrayList<Integer> tPlantIdList = new ArrayList<>();
			List<String> pList = SystemCache.plantTypeMap.get(plantType);
			if (Util.isNotBlank(pList)) {
				pList.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
				plantIdList.retainAll(tPlantIdList);
			}

			Map<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("reList", plantIdList);
			Map<String, String> createTime = plantMapper.getCreateTime(countMap);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(createTime.get("time"));
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
		}
		return msg;
	}

	@Override
	public MessageBean getProSearch(String str)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		String plantId = String.valueOf(map.get("plantId"));
		// 判断电站类型
		PlantInfo plantInfo = plantMapper.getPlantById(plantId);
		Integer plantTypeA = plantInfo.getPlantTypeA();
		// 存放查询出来的发电量的数据
		Map<String, Double> powerMap = new TreeMap<String, Double>();
		// 存放查询出来的收益的数据
		Map<String, Double> priceMap = new TreeMap<String, Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当天的日期
		String today = sdf.format(new Date());
		// 存放最后结果的集合
		List<Double> powerResult = new ArrayList<Double>();
		List<Double> priceResult = new ArrayList<Double>();
		List<Object> xData = new ArrayList<Object>();
		Map<String, String> startMap = new TreeMap<String, String>();
		Map<String, String> endMap = new TreeMap<String, String>();
		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("plantId", plantId);
		// 如果维度没有或者维度为日；那么都用日维度
		if ((dimension == null || dimension.equals("null")) || dimension.equals("0")) {
			paramMap.put("date", "%Y-%m-%d");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m-%d");
			powerPriceMap.put("sdf", "yyyy-MM-dd");
			// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(today);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
				if (listDate.size() >= 7) {
					// 获取7天日期
					String[] days = new String[7];
					for (int i = 6; i >= 0; i--) {
						days[6 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推30天，若推到当前天数不足30天，以当前天数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 获取起始日期到当前日期的天数
				long distance = TimeUtil.getDistanceDay(startTime);
				// 判断距离是否有30天
				if (distance < 30) {
					// 获取相差的日期
					String[] days = new String[Integer.valueOf(distance + "") + 1];
					for (int i = 0; i <= Integer.valueOf(distance + ""); i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 0; i <= 29; i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推30天
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断结束日期与电站接入日期之间的天数，若不足30天则从接入日期开始到结束日期结束
				if (listDate.size() >= 30) {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 29; i >= 0; i--) {
						days[29 - i] = TimeUtil.getSpecifiedDayBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				Date dBegin = sdf.parse(startTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				String[] days = new String[listDate.size()];
				for (int i = 0; i < listDate.size(); i++) {
					days[i] = sdf.format(listDate.get(i));
				}
				// 给两个集合设置初始值
				for (String string : days) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				updateDateFormat(days, xData);
				getStartEnd(days, startMap, endMap);
			}
			// 维度为月
		} else if (dimension.equals("1")) {
			paramMap.put("date", "%Y-%m");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m");
			powerPriceMap.put("sdf", "yyyy-MM");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
				// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
				if (distanceMon >= 6) {
					// 获取6个月的月份
					String[] mon = new String[6];
					for (int i = 5; i >= 0; i--) {
						mon[5 - i] = TimeUtil.getPastMonAndYear(i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推12个月，若推到当前月数不足12个月，以当前月数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				String todayMon = TimeUtil.getPastMonAndYear(0);
				int distance = TimeUtil.countMonths(startTime, todayMon);
				if (distance < 12) {
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 0; i <= 11; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推12个月
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, endTime);
				// 判断当前日期与电站接入日期之间的月数，若不足12个月则从接入日期开始到当前日期结束
				if (distanceMon >= 12) {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 11; i >= 0; i--) {
						mon[11 - i] = TimeUtil.getSpecifiedMonBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, endTime);
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distance = TimeUtil.countMonths(startTime, endTime);
				String[] mon = new String[distance + 1];
				for (int i = 0; i <= distance; i++) {
					mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : mon) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : mon) {
					xData.add(string);
				}
				getStartEnd(mon, startMap, endMap);
			}
		} else if (dimension.equals("2")) {
			paramMap.put("date", "%Y");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y");
			powerPriceMap.put("sdf", "yyyy");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
				if (distanceYear >= 5) {
					// 获取5年的年份
					String[] year = new String[5];
					for (int i = 4; i >= 0; i--) {
						year[4 - i] = TimeUtil.getPastYear(i);
					}
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推25年，若推到当前月数不足25年，以当前年份为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(startTime, sdf1.format(new Date()));
				if (distanceYear < 25) {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 0; i <= 24; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推25年
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				int distanceMon = TimeUtil.getDateLength(createTime, endTime);
				// 判断当前日期与电站接入日期之间的年数，若不足25个月则从接入日期开始到当前日期结束
				if (distanceMon >= 25) {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 24; i >= 0; i--) {
						year[24 - i] = TimeUtil.getSpecifiedYearBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceMon + 1];
					for (int i = 0; i <= distanceMon; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distanceYear = TimeUtil.getDateLength(startTime, endTime);
				String[] year = new String[distanceYear + 1];
				for (int i = 0; i <= distanceYear; i++) {
					year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : year) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : year) {
					xData.add(string);
				}
				getStartEnd(year, startMap, endMap);
			}

		}
		// 组装前台数据
		Map<String, Object> body = new HashMap<String, Object>();
		List<Object> yData = new ArrayList<Object>();
		body.put("xData", xData);
		// 判断发电量的最大值是否超过一亿，若超过则单位换算
		if (Collections.max(powerResult) > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.divideDouble(powerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			yData.add(powerResultMap);
			body.put("unity1", "亿度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (Collections.max(powerResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.divideDouble(powerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			yData.add(powerResultMap);
			body.put("unity1", "万度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
		} else {
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.getRetainedDecimal(powerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			yData.add(powerResultMap);
			body.put("unity1", "度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
		}
		// 判断收益的最大值是否超过一万，若超过则单位换算
		if (Collections.max(priceResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.divideDouble(priceResult.get(i), 10000, 1));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "万元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		} else {
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.getRetainedDecimal(priceResult.get(i)));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		}
		body.put("yData", yData);
		body.put("start", startMap.get("startTime"));
		body.put("end", endMap.get("endTime"));
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getProSearch2(String str)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		String plantId = String.valueOf(map.get("plantId"));
		// 存放查询出来的发电量的数据
		Map<String, Double> inPowerMap = new TreeMap<String, Double>();
		Map<String, Double> outPowerMap = new TreeMap<String, Double>();
		// 存放查询出来的收益的数据
		Map<String, Double> priceMap = new TreeMap<String, Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当天的日期
		String today = sdf.format(new Date());
		// 存放最后结果的集合
		List<Double> inPowerResult = new ArrayList<Double>();
		List<Double> outPowerResult = new ArrayList<Double>();
		List<Double> priceResult = new ArrayList<Double>();
		List<Object> xData = new ArrayList<Object>();
		Map<String, String> startMap = new TreeMap<String, String>();
		Map<String, String> endMap = new TreeMap<String, String>();
		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("plantId", plantId);
		// 如果维度没有或者维度为日；那么都用日维度
		if ((dimension == null || dimension.equals("null")) || dimension.equals("0")) {
			paramMap.put("date", "%Y-%m-%d");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m-%d");
			powerPriceMap.put("sdf", "yyyy-MM-dd");
			// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(today);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
				if (listDate.size() >= 7) {
					// 获取7天日期
					String[] days = new String[7];
					for (int i = 6; i >= 0; i--) {
						days[6 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推30天，若推到当前天数不足30天，以当前天数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 获取起始日期到当前日期的天数
				long distance = TimeUtil.getDistanceDay(startTime);
				// 判断距离是否有30天
				if (distance < 30) {
					// 获取相差的日期
					String[] days = new String[Integer.valueOf(distance + "") + 1];
					for (int i = 0; i <= Integer.valueOf(distance + ""); i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 0; i <= 29; i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推30天
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断结束日期与电站接入日期之间的天数，若不足30天则从接入日期开始到结束日期结束
				if (listDate.size() >= 30) {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 29; i >= 0; i--) {
						days[29 - i] = TimeUtil.getSpecifiedDayBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				Date dBegin = sdf.parse(startTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				String[] days = new String[listDate.size()];
				for (int i = 0; i < listDate.size(); i++) {
					days[i] = sdf.format(listDate.get(i));
				}
				// 给两个集合设置初始值
				for (String string : days) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				updateDateFormat(days, xData);
				getStartEnd(days, startMap, endMap);
			}
			// 维度为月
		} else if (dimension.equals("1")) {
			paramMap.put("date", "%Y-%m");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m");
			powerPriceMap.put("sdf", "yyyy-MM");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
				// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
				if (distanceMon >= 6) {
					// 获取6个月的月份
					String[] mon = new String[6];
					for (int i = 5; i >= 0; i--) {
						mon[5 - i] = TimeUtil.getPastMonAndYear(i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推12个月，若推到当前月数不足12个月，以当前月数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				String todayMon = TimeUtil.getPastMonAndYear(0);
				int distance = TimeUtil.countMonths(startTime, todayMon);
				if (distance < 12) {
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 0; i <= 11; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推12个月
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, endTime);
				// 判断当前日期与电站接入日期之间的月数，若不足12个月则从接入日期开始到当前日期结束
				if (distanceMon >= 12) {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 11; i >= 0; i--) {
						mon[11 - i] = TimeUtil.getSpecifiedMonBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, endTime);
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distance = TimeUtil.countMonths(startTime, endTime);
				String[] mon = new String[distance + 1];
				for (int i = 0; i <= distance; i++) {
					mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : mon) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				for (String string : mon) {
					xData.add(string);
				}
				getStartEnd(mon, startMap, endMap);
			}
		} else if (dimension.equals("2")) {
			paramMap.put("date", "%Y");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y");
			powerPriceMap.put("sdf", "yyyy");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
				if (distanceYear >= 5) {
					// 获取5年的年份
					String[] year = new String[5];
					for (int i = 4; i >= 0; i--) {
						year[4 - i] = TimeUtil.getPastYear(i);
					}
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推25年，若推到当前月数不足25年，以当前年份为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(startTime, sdf1.format(new Date()));
				if (distanceYear < 25) {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 0; i <= 24; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推25年
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				int distanceMon = TimeUtil.getDateLength(createTime, endTime);
				// 判断当前日期与电站接入日期之间的年数，若不足25个月则从接入日期开始到当前日期结束
				if (distanceMon >= 25) {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 24; i >= 0; i--) {
						year[24 - i] = TimeUtil.getSpecifiedYearBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceMon + 1];
					for (int i = 0; i <= distanceMon; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distanceYear = TimeUtil.getDateLength(startTime, endTime);
				String[] year = new String[distanceYear + 1];
				for (int i = 0; i <= distanceYear; i++) {
					year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : year) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				for (String string : year) {
					xData.add(string);
				}
				getStartEnd(year, startMap, endMap);
			}

		}
		// 组装前台数据
		Map<String, Object> body = new HashMap<String, Object>();
		List<Object> yData = new ArrayList<Object>();
		body.put("xData", xData);

		// 判断充电量的最大值是否超过一亿，若超过则单位换算
		double maxValue = Util.isNotBlank(inPowerResult) ? Collections.max(inPowerResult) : 0.0;
		if (maxValue > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.divideDouble(inPowerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "亿度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (maxValue > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.divideDouble(inPowerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "万度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
		} else {
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.getRetainedDecimal(inPowerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
		}
		// 判断放电量的最大值是否超过一亿，若超过则单位换算
		if (Collections.max(outPowerResult) > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.divideDouble(outPowerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "亿度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (Collections.max(outPowerResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.divideDouble(outPowerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "万度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
		} else {
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.getRetainedDecimal(outPowerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
		}

		// 判断收益的最大值是否超过一万，若超过则单位换算
		if (Collections.max(priceResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.divideDouble(priceResult.get(i), 10000, 1));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("type", "line");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "万元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		} else {
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.getRetainedDecimal(priceResult.get(i)));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("type", "line");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		}
		body.put("yData", yData);
		body.put("start", startMap.get("startTime"));
		body.put("end", endMap.get("endTime"));
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		msg.setMsg(Msg.SELECT_SUCCUESS);

		return msg;
	}

	public MessageBean getProSearchForStoredMulti(String str, List<PlantInfo> plantList)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		String plantId = String.valueOf(map.get("plantId"));
		// 存放查询出来的发电量的数据
		Map<String, Double> inPowerMap = new TreeMap<String, Double>();
		Map<String, Double> outPowerMap = new TreeMap<String, Double>();
		// 存放查询出来的收益的数据
		Map<String, Double> priceMap = new TreeMap<String, Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当天的日期
		String today = sdf.format(new Date());
		// 存放最后结果的集合
		List<Double> inPowerResult = new ArrayList<Double>();
		List<Double> outPowerResult = new ArrayList<Double>();
		List<Double> priceResult = new ArrayList<Double>();
		List<Object> xData = new ArrayList<Object>();
		Map<String, String> startMap = new TreeMap<String, String>();
		Map<String, String> endMap = new TreeMap<String, String>();
		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("plantId", plantId);
		// 如果维度没有或者维度为日；那么都用日维度
		if ((dimension == null || dimension.equals("null")) || dimension.equals("0")) {
			paramMap.put("date", "%Y-%m-%d");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m-%d");
			powerPriceMap.put("sdf", "yyyy-MM-dd");
			// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(today);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
				if (listDate.size() >= 7) {
					// 获取7天日期
					String[] days = new String[7];
					for (int i = 6; i >= 0; i--) {
						days[6 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergyMulti(plantId, days, inPowerMap, outPowerMap, priceMap,
							inPowerResult, outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推30天，若推到当前天数不足30天，以当前天数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 获取起始日期到当前日期的天数
				long distance = TimeUtil.getDistanceDay(startTime);
				// 判断距离是否有30天
				if (distance < 30) {
					// 获取相差的日期
					String[] days = new String[Integer.valueOf(distance + "") + 1];
					for (int i = 0; i <= Integer.valueOf(distance + ""); i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 0; i <= 29; i++) {
						days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推30天
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断结束日期与电站接入日期之间的天数，若不足30天则从接入日期开始到结束日期结束
				if (listDate.size() >= 30) {
					// 获取相差的日期
					String[] days = new String[30];
					for (int i = 29; i >= 0; i--) {
						days[29 - i] = TimeUtil.getSpecifiedDayBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				Date dBegin = sdf.parse(startTime);
				Date dEnd = sdf.parse(endTime);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				String[] days = new String[listDate.size()];
				for (int i = 0; i < listDate.size(); i++) {
					days[i] = sdf.format(listDate.get(i));
				}
				// 给两个集合设置初始值
				for (String string : days) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, days, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				updateDateFormat(days, xData);
				getStartEnd(days, startMap, endMap);
			}
			// 维度为月
		} else if (dimension.equals("1")) {
			paramMap.put("date", "%Y-%m");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m");
			powerPriceMap.put("sdf", "yyyy-MM");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
				// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
				if (distanceMon >= 6) {
					// 获取6个月的月份
					String[] mon = new String[6];
					for (int i = 5; i >= 0; i--) {
						mon[5 - i] = TimeUtil.getPastMonAndYear(i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推12个月，若推到当前月数不足12个月，以当前月数为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				String todayMon = TimeUtil.getPastMonAndYear(0);
				int distance = TimeUtil.countMonths(startTime, todayMon);
				if (distance < 12) {
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 0; i <= 11; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推12个月
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, endTime);
				// 判断当前日期与电站接入日期之间的月数，若不足12个月则从接入日期开始到当前日期结束
				if (distanceMon >= 12) {
					// 获取相差的日期
					String[] mon = new String[12];
					for (int i = 11; i >= 0; i--) {
						mon[11 - i] = TimeUtil.getSpecifiedMonBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, endTime);
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
				// 如果起始日期和结束日期都不为空，
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distance = TimeUtil.countMonths(startTime, endTime);
				String[] mon = new String[distance + 1];
				for (int i = 0; i <= distance; i++) {
					mon[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : mon) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, mon, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				for (String string : mon) {
					xData.add(string);
				}
				getStartEnd(mon, startMap, endMap);
			}
		} else if (dimension.equals("2")) {
			paramMap.put("date", "%Y");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y");
			powerPriceMap.put("sdf", "yyyy");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
				if (distanceYear >= 5) {
					// 获取5年的年份
					String[] year = new String[5];
					for (int i = 4; i >= 0; i--) {
						year[4 - i] = TimeUtil.getPastYear(i);
					}
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推25年，若推到当前月数不足25年，以当前年份为结束
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(startTime, sdf1.format(new Date()));
				if (distanceYear < 25) {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 0; i <= 24; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
				// 如果有结束日期没有起始日期，那么从结束日期向前推25年
			} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				int distanceMon = TimeUtil.getDateLength(createTime, endTime);
				// 判断当前日期与电站接入日期之间的年数，若不足25个月则从接入日期开始到当前日期结束
				if (distanceMon >= 25) {
					// 获取相差的日期
					String[] year = new String[25];
					for (int i = 24; i >= 0; i--) {
						year[24 - i] = TimeUtil.getSpecifiedYearBefore(endTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceMon + 1];
					for (int i = 0; i <= distanceMon; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						inPowerMap.put(string, 0.0);
						outPowerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
							outPowerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
			} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
					&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
				int distanceYear = TimeUtil.getDateLength(startTime, endTime);
				String[] year = new String[distanceYear + 1];
				for (int i = 0; i <= distanceYear; i++) {
					year[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
				}
				// 给两个集合设置初始值
				for (String string : year) {
					inPowerMap.put(string, 0.0);
					outPowerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingleforStroedEnergy(plantId, year, inPowerMap, outPowerMap, priceMap, inPowerResult,
						outPowerResult, priceResult, powerPriceMap);
				for (String string : year) {
					xData.add(string);
				}
				getStartEnd(year, startMap, endMap);
			}

		}
		// 组装前台数据
		Map<String, Object> body = new HashMap<String, Object>();
		List<Object> yData = new ArrayList<Object>();
		body.put("xData", xData);

		// 判断充电量的最大值是否超过一亿，若超过则单位换算
		double maxValue = Util.isNotBlank(inPowerResult) ? Collections.max(inPowerResult) : 0.0;
		if (maxValue > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.divideDouble(inPowerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "亿度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (maxValue > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.divideDouble(inPowerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "万度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
		} else {
			for (int i = 0; i < inPowerResult.size(); i++) {
				inPowerResult.set(i, Util.getRetainedDecimal(inPowerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "充电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", inPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(inPowerResult)));
		}
		// 判断放电量的最大值是否超过一亿，若超过则单位换算
		if (Collections.max(outPowerResult) > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.divideDouble(outPowerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "亿度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (Collections.max(outPowerResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.divideDouble(outPowerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "万度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
		} else {
			for (int i = 0; i < outPowerResult.size(); i++) {
				outPowerResult.set(i, Util.getRetainedDecimal(outPowerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "放电量");
			powerResultMap.put("type", "bar");
			powerResultMap.put("value", outPowerResult);
			yData.add(powerResultMap);
			body.put("unity1", "度");
			body.put("maxData1", Util.getRetainedDecimal(Collections.max(outPowerResult)));
		}

		// 判断收益的最大值是否超过一万，若超过则单位换算
		if (Collections.max(priceResult) > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.divideDouble(priceResult.get(i), 10000, 1));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("type", "line");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "万元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		} else {
			for (int i = 0; i < priceResult.size(); i++) {
				priceResult.set(i, Util.getRetainedDecimal(priceResult.get(i)));
			}
			Map<String, Object> priceResultMap = new HashMap<String, Object>();
			priceResultMap.put("name", "收益");
			priceResultMap.put("type", "line");
			priceResultMap.put("value", priceResult);
			yData.add(priceResultMap);
			body.put("unity2", "元");
			body.put("maxData2", Util.getRetainedDecimal(Collections.max(priceResult)));
		}
		body.put("yData", yData);
		body.put("start", startMap.get("startTime"));
		body.put("end", endMap.get("endTime"));
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		msg.setMsg(Msg.SELECT_SUCCUESS);

		return msg;
	}

	/**
	 * @param
	 * @param days        日期数组
	 * @param powerMap    存放发电量的map
	 * @param priceMap    存放收益的map
	 * @param powerResult 存放发电量的list
	 * @param             priceResult: 存放收益的list
	 * @Title: getPowerPriceSingle
	 * @Description: 单电站查询符合条件的数据
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月24日下午1:59:05
	 */
	public void getPowerPriceSingle(String plantId, String[] days, Map<String, Double> powerMap,
			Map<String, Double> priceMap, List<Double> powerResult, List<Double> priceResult,
			Map<String, String> powerPriceMap) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		powerPriceMap.put("plantId", plantId);
		powerPriceMap.put("startTime", days[0]);
		powerPriceMap.put("endTime", days[days.length - 1]);
		List<Map<String, Object>> GeneratingList = inventerStorageDataMapper.getPowerPriceByDay(powerPriceMap);
		for (Map<String, Object> map2 : GeneratingList) {
			Double oldNum = powerMap.get(map2.get("time") + "");
			powerMap.put(map2.get("time") + "", Util.addFloat(oldNum, Double.valueOf(map2.get("sum") + ""), 0));
			Double oldPrice = priceMap.get(map2.get("time") + "");
			priceMap.put(map2.get("time") + "", Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
		}
		// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
		int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
		if (a > 0) {
			Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
			if (todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")) {
				Double oldNum = powerMap.get(sdf.format(System.currentTimeMillis()));
				powerMap.put(sdf.format(System.currentTimeMillis()),
						Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("power") + ""), 0));
			}
			if (todayPricePower.get("inCome") != "null" && !todayPricePower.get("inCome").equals("")) {
				Double oldNum = priceMap.get(sdf.format(System.currentTimeMillis()));
				priceMap.put(sdf.format(System.currentTimeMillis()),
						Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("inCome") + ""), 0));
			}
		}
		for (int i = 0; i < days.length; i++) {
			powerResult.add(Double.valueOf(powerMap.get(days[i]) + ""));
			priceResult.add(Double.valueOf(priceMap.get(days[i]) + ""));
		}
	}

	public void getPowerPriceSingleforStroedEnergy(String plantId, String[] days, Map<String, Double> inPowerMap,
			Map<String, Double> outPowerMap, Map<String, Double> priceMap, List<Double> inPowerResult,
			List<Double> outPowerResult, List<Double> priceResult, Map<String, String> powerPriceMap) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		powerPriceMap.put("plantId", plantId);
		powerPriceMap.put("startTime", days[0]);
		powerPriceMap.put("endTime", days[days.length - 1]);
		List<Map<String, Object>> GeneratingList = energyMapper.getInOutEnergyAndIncome(powerPriceMap);
		if (GeneratingList.size() > 0) {
			for (Map<String, Object> map2 : GeneratingList) {
				Double oldNum = inPowerMap.get(map2.get("time") + "");
				Double oldNum2 = outPowerMap.get(map2.get("time") + "");
				// 充、放电
				if (map2.get("inEnergy") == null) {
					inPowerMap.put(map2.get("time") + "", Util.addFloat(oldNum, 0.0, 0));
				} else {
					inPowerMap.put(map2.get("time") + "",
							Util.addFloat(oldNum, Double.valueOf(map2.get("inEnergy") + ""), 0));
				}
				if (map2.get("outEnergy") == null) {
					outPowerMap.put(map2.get("time") + "", Util.addFloat(oldNum2, 0.0, 0));
				} else {
					outPowerMap.put(map2.get("time") + "",
							Util.addFloat(oldNum2, Double.valueOf(map2.get("outEnergy") + ""), 0));
				}
				Double oldPrice = priceMap.get(map2.get("time") + "");
				// 收益
				priceMap.put(map2.get("time") + "",
						Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
			}
		}
		// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
		int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
		if (a > 0) {
			Map<String, Double> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
			Double oldNum = inPowerMap.get(sdf.format(System.currentTimeMillis()));
			inPowerMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("dailyNegEnergy") + ""), 0));
			Double oldNum2 = outPowerMap.get(sdf.format(System.currentTimeMillis()));
			outPowerMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum2, Double.valueOf(todayPricePower.get("dailyPosEnergy") + ""), 0));
			Double oldNum3 = priceMap.get(sdf.format(System.currentTimeMillis()));
			priceMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum3, Double.valueOf(todayPricePower.get("dailyPrice") + ""), 0));
		}
		for (int i = 0; i < days.length; i++) {
			inPowerResult.add(Double.valueOf(inPowerMap.get(days[i]) + ""));
			outPowerResult.add(Double.valueOf(outPowerMap.get(days[i]) + ""));
			priceResult.add(Double.valueOf(priceMap.get(days[i]) + ""));
		}
	}

	public void getPowerPriceSingleforStroedEnergyMulti(String plantId, String[] days, Map<String, Double> inPowerMap,
			Map<String, Double> outPowerMap, Map<String, Double> priceMap, List<Double> inPowerResult,
			List<Double> outPowerResult, List<Double> priceResult, Map<String, String> powerPriceMap) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		powerPriceMap.put("plantId", plantId);
		powerPriceMap.put("startTime", days[0]);
		powerPriceMap.put("endTime", days[days.length - 1]);
		// List<Map<String, Object>> GeneratingList =
		// inventerStorageDataMapper.getPowerPriceByDay(powerPriceMap);
		List<Map<String, Object>> GeneratingList = energyMapper.getInOutEnergyAndIncome(powerPriceMap);
		for (Map<String, Object> map2 : GeneratingList) {
			Double oldNum = inPowerMap.get(map2.get("time") + "");
			Double oldNum2 = outPowerMap.get(map2.get("time") + "");
			// 充、放电
			if (map2.get("inEnergy") == null) {
				inPowerMap.put(map2.get("time") + "", Util.addFloat(oldNum, 0.0, 0));
			} else {
				inPowerMap.put(map2.get("time") + "",
						Util.addFloat(oldNum, Double.valueOf(map2.get("inEnergy") + ""), 0));
			}
			if (map2.get("outEnergy") == null) {
				outPowerMap.put(map2.get("time") + "", Util.addFloat(oldNum2, 0.0, 0));
			} else {
				outPowerMap.put(map2.get("time") + "",
						Util.addFloat(oldNum2, Double.valueOf(map2.get("outEnergy") + ""), 0));
			}
			Double oldPrice = priceMap.get(map2.get("time") + "");
			// 收益
			priceMap.put(map2.get("time") + "", Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
		}
		// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
		int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
		if (a > 0) {
			Map<String, Double> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
			Double oldNum = inPowerMap.get(sdf.format(System.currentTimeMillis()));
			inPowerMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("dailyNegEnergy") + ""), 0));
			Double oldNum2 = outPowerMap.get(sdf.format(System.currentTimeMillis()));
			outPowerMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum2, Double.valueOf(todayPricePower.get("dailyPosEnergy") + ""), 0));
			Double oldNum3 = priceMap.get(sdf.format(System.currentTimeMillis()));
			priceMap.put(sdf.format(System.currentTimeMillis()),
					Util.addFloat(oldNum3, Double.valueOf(todayPricePower.get("dailyPrice") + ""), 0));
		}
		for (int i = 0; i < days.length; i++) {
			inPowerResult.add(Double.valueOf(inPowerMap.get(days[i]) + ""));
			outPowerResult.add(Double.valueOf(outPowerMap.get(days[i]) + ""));
			priceResult.add(Double.valueOf(priceMap.get(days[i]) + ""));
		}
	}

	@Override
	public MessageBean getSinglePower(String str) throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 根据tokenId获取到当前登录的用户
		// 获取前台数据
		String date = String.valueOf(map.get("date"));
		String power = String.valueOf(map.get("power"));
		String plantId = String.valueOf(map.get("plantId"));
		if ((date == null || date.equals("null")) && (power == null || power.equals("null"))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			date = sdf.format(System.currentTimeMillis());
			power = getSinglePowerByMon(plantId, date) + "";
		}
		// 获取上一个月的日期
		String beforeDate = TimeUtil.getSpecifiedMonBefore(date, 1);
		Double beforePower = getSinglePowerByMon(plantId, beforeDate);
		if (beforePower == null && beforePower.equals("")) {
			beforePower = 0.0;
		}
		String[] dates = date.split("-");
		// 获取传入月份的天数
		int dateNum = TimeUtil.getMonthOfDay(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
		String[] beforeDates = beforeDate.split("-");
		// 获取上一个月份的天数
		int beforeDateNum = TimeUtil.getMonthOfDay(Integer.parseInt(beforeDates[0]), Integer.parseInt(beforeDates[1]));
		PlantInfo plant = plantMapper.getPlantInfo(plantId);
		Double result = Util.divideDouble(Util.divideDouble(Double.parseDouble(power), dateNum, 0), plant.getCapacity(),
				1);
		Double beforeResult = Util.divideDouble(Util.divideDouble(beforePower, beforeDateNum, 0), plant.getCapacity(),
				1);
		BigDecimal data1 = new BigDecimal(result);
		BigDecimal data2 = new BigDecimal(beforeResult);
		String res = "";
		if (data1.compareTo(data2) < 0) {
			res = "-" + result;
		} else if (data1.compareTo(data2) > 0) {
			res = "+" + result;
		} else {
			res = "=" + result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("palntRank", res + "小时");
		resultMap.put("PPR", res + "度");
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}

	public Double getSinglePowerByMon(String plantId, String beforeDate) {
		Double result = 0.0;
		Map<String, Object> powerPriceMap = new HashMap<String, Object>();
		powerPriceMap.put("plantId", plantId);
		powerPriceMap.put("date", beforeDate);
		List<Map<String, Object>> GeneratingList = inventerStorageDataMapper.getSinglePowerByMon(powerPriceMap);
		Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantId);
		Double todayPower = 0.0;
		if (todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")) {
			todayPower = Double.valueOf(todayPricePower.get("power") + "");
		}
		for (Map<String, Object> map2 : GeneratingList) {
			result = Util.addFloat(Double.parseDouble(map2.get("sum") + ""), todayPower, 0);
		}
		return result;
	}

	@Override
	public MessageBean getPlantStatus(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);

		// 光储区分 此处只需要光伏
		List<String> pvList = SystemCache.plantTypeMap.get(String.valueOf(PTAC.PHOTOVOLTAIC));
		if (Util.isNotBlank(pvList)) {
			List<Integer> pvListTemp = new ArrayList<>();
			for (String pv : pvList) {
				pvListTemp.add(Integer.parseInt(pv));
			}
			plantIdList.retainAll(pvListTemp);
		}

		if (Util.isNotBlank(plantIdList)) {
			Map<String, Double> totalMap = screenService.getPowerTotal(plantIdList);
			resultMap.put("totalPower", totalMap.get("power"));
			resultMap.put("totalPowerUnit", "度");
			resultMap.put("totalPrice", totalMap.get("inCome"));
			resultMap.put("totalPriceUnit", "元");
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}

	@Override
	public MessageBean getTodayInfo(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("time", System.currentTimeMillis());
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);

		// 现只返回光伏数据
		ArrayList<Integer> tPlantIdList = new ArrayList<>();
		SystemCache.plantTypeMap.get(String.valueOf(PTAC.PHOTOVOLTAIC))
				.forEach(e -> tPlantIdList.add(Integer.valueOf(e)));
		plantIdList.retainAll(tPlantIdList);

		if (Util.isNotBlank(plantIdList)) {
			List<Integer> statusList = new ArrayList<Integer>();
			double todayEnergy = 0.00;
			double todayPrice = 0.00;
			// 查询出当前公司下所有的电站
			List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
			if (Util.isNotBlank(plantList)) {
				for (PlantInfo plantInfo : plantList) {
					String plantId = plantInfo.getId() + "";
					statusList.add(ServiceUtil.getPlantStatus(plantId));
//					Map<String, Map<String, Double>> todayInfo = SystemCache.currentDataOfPlant;
//					Map<String, Double> todayPricePower = todayInfo.get(plantId);
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
			resultMap.put("genDay", Util.getRetainedDecimal(todayEnergy));
			resultMap.put("genDayUnit", "度");
			resultMap.put("profitDay", Util.getRetainedDecimal(todayPrice));
			resultMap.put("profitDayUnit", "元");
			// 正常发电
			int gen = 0;
			// 待机
			int standby = 0;
			// 故障
			int fault = 0;
			// 中断、断链
			int breaks = 0;
			int exception = 0;
			for (Integer integer : statusList) {
				switch (integer) {
				case 0:
					gen++;
					break;
				case 1:
					standby++;
					break;
				case 2:
					fault++;
					break;
				case 3:
					breaks++;
					break;
				case 4:
					exception++;
					break;
				default:
					break;
				}
			}
			// 组装前台数据
			resultMap.put("gen", gen);
			resultMap.put("standby", standby);
			resultMap.put("fault", fault);
			resultMap.put("break", breaks);
			resultMap.put("abnormal", exception);
			resultMap.put("unit", "座");
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}

	/**
	 * @param list org集合
	 * @Title: getPowerTotal
	 * @Description: 获取累计发电量和收益
	 * @return: Double
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月18日上午10:13:40
	 */
	@Override
	public Map<String, Double> getPowerPriceTotal(List<String> list) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("reList", list);
		Map<String, Double> map = priceMapper.getPowerPriceTotal(paramMap);
		return map;
	}

	@Override
	public MessageBean getSingelDevicess(String str, Session session, HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String plantId = String.valueOf(map.get("plantId"));
		Map<String, Object> result = new HashMap<>();
		/**
		 * 解析datatable参数
		 */
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("length") + "");
		// 获取起始位置
		Integer alarm_page_num = Integer.parseInt(map.get("start") + "");
		List<Map<String, String>> tempList = (List<Map<String, String>>) map.get("orderName");
		StringBuffer orderParam = new StringBuffer();
		if (tempList != null && !tempList.isEmpty()) {
			for (int i = 0; i < tempList.size(); i++) {
				for (String key : tempList.get(i).keySet()) {
					String value = tempList.get(i).get(key);
					key = "deviceName".equals(key) ? "device_name" : key;
					orderParam.append(key).append(" ").append(value).append(",");
				}
			}
			orderParam = orderParam.deleteCharAt(orderParam.lastIndexOf(","));
		}
		/**
		 * 组装SQL查询参数
		 */
		int count = deviceMapper.getDevCount(plantId);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		int start = Integer.parseInt(map.get("start") + "");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("plantId", plantId);
		paramMap.put("min", alarm_page_num);
		paramMap.put("max", each_page_num);
		paramMap.put("order", orderParam.toString());
		List<CollDevice> deviceList = deviceMapper.listDevicesSort(paramMap);
		if (deviceList != null && !deviceList.isEmpty()) {
			Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
			for (CollDevice collDevice : deviceList) {
				Map<String, Object> temMap = new HashMap<String, Object>();
				temMap.put("deviceId", collDevice.getId() + "");
				temMap.put("deviceName", collDevice.getDeviceName());
				temMap.put("deviceType", collDevice.getDeviceType());
				temMap.put("power", "NaN");
				temMap.put("genDay", "NaN");
				// 判断是不是逆变器
				if (collDevice.getDeviceType() == 1 || collDevice.getDeviceType() == 2) {
					String deviceId = String.valueOf(collDevice.getId());
					temMap.put("genDay", getNowdayIncomeOfDevice(deviceId));
					int status = deviceOfStatus.get(deviceId);
					switch (status) {
					case 0:
						temMap.put("type", 0);
						temMap.put("status", "正常");
						break;
					case 1:
						temMap.put("type", 1);
						temMap.put("status", "待机");
						break;
					case 2:
						temMap.put("type", 2);
						temMap.put("status", "故障");
						break;
					case 3:
						temMap.put("type", 3);
						temMap.put("status", "通讯中断");
						break;
					default:
						break;
					}
					Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
					String guid = guidMap.get(collDevice.getId() + "").get("power");
					Map<String, String> currentParam = cacheUtil.getMap(deviceId);
					if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
						temMap.put("power", "NaN");
					} else {
						temMap.put("power",
								currentParam.containsKey(guid)
										? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
										: "NaN");

					}
					// 判断是不是电表
				} else if (collDevice.getDeviceType() == 3) {
					switch (deviceOfStatus.get(collDevice.getId() + "")) {
					case 0:
						temMap.put("type", 0);
						temMap.put("status", "正常");
						break;
					case 3:
						temMap.put("type", 3);
						temMap.put("status", "通讯中断");
						break;
					default:
						break;
					}
					// 判断是不是数据采集器
				} else if (collDevice.getDeviceType() == 5) {
					switch (deviceOfStatus.get(collDevice.getId() + "")) {
					case 0:
						temMap.put("type", 0);
						temMap.put("status", "通讯中");
						break;
					case 3:
						temMap.put("type", 3);
						temMap.put("status", "通讯中断");
						break;
					default:
						break;
					}
				}
				resultList.add(temMap);
			}
		}
		result.put("draw", String.valueOf(map.get("draw")));
		result.put("recordsTotal", count);
		result.put("recordsFiltered", count);
		result.put("data", resultList);
		MessageBean msg = new MessageBean();
		msg.setBody(result);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getProSearchDate(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		//String plantId = Util.;
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("plantId", map.get("plantId"));
		paramMap.put("date", "%Y-%m-%d");
		Map<String, String> createTime = plantMapper.getCreateTimeById(paramMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(createTime.get("time"));
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getProSearchWX(String str)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		String plantId = String.valueOf(map.get("plantId"));
		// 存放查询出来的发电量的数据
		Map<String, Double> powerMap = new TreeMap<String, Double>();
		// 存放查询出来的收益的数据
		Map<String, Double> priceMap = new TreeMap<String, Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当天的日期
		String today = sdf.format(new Date());
		// 存放最后结果的集合
		List<Double> powerResult = new ArrayList<Double>();
		List<Double> priceResult = new ArrayList<Double>();
		List<Object> xData = new ArrayList<Object>();
		Map<String, String> startMap = new TreeMap<String, String>();
		Map<String, String> endMap = new TreeMap<String, String>();
		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("plantId", plantId);
		// 如果维度没有或者维度为日；那么都用日维度
		if ((dimension == null || dimension.equals("null")) || dimension.equals("0")) {
			paramMap.put("date", "%Y-%m-%d");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m-%d");
			powerPriceMap.put("sdf", "yyyy-MM-dd");
			// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
			if ((startTime == null || startTime.equals("null")) && (endTime == null || endTime.equals("null"))) {
				// 查询该电站的创建时间
				String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(today);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
				if (listDate.size() >= 7) {
					// 获取7天日期
					String[] days = new String[7];
					for (int i = 6; i >= 0; i--) {
						days[6 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat2(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceSingle(plantId, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat2(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 如果有起始日期没有结束日期，那么从开始日期起往后推30天，若推到当前天数不足30天，以当前天数为结束
			}
			// 维度为月
		} else if (dimension.equals("1")) {
			paramMap.put("date", "%Y-%m");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y-%m");
			powerPriceMap.put("sdf", "yyyy-MM");
			// 查询该电站的创建时间
			String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
			// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
			if (distanceMon >= 12) {
				// 获取6个月的月份
				String[] mon = new String[12];
				for (int i = 11; i >= 0; i--) {
					mon[11 - i] = TimeUtil.getPastMonAndYear(i);
				}
				// 给两个集合设置初始值
				for (String string : mon) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : mon) {
					xData.add(string);
				}
				getStartEnd(mon, startMap, endMap);
			} else {
				int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
				String[] mon = new String[distance + 1];
				for (int i = 0; i <= distance; i++) {
					mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
				}
				// 给两个集合设置初始值
				for (String string : mon) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : mon) {
					xData.add(string);
				}
				getStartEnd(mon, startMap, endMap);
			}
			// 如果有起始日期没有结束日期，那么从开始日期起往后推12个月，若推到当前月数不足12个月，以当前月数为结束
		} else if (dimension.equals("2")) {
			paramMap.put("date", "%Y");
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			powerPriceMap.put("date", "%Y");
			powerPriceMap.put("sdf", "yyyy");
			// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
			String createTime = plantMapper.getCreateTimeById(paramMap).get("time");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
			int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
			if (distanceYear >= 5) {
				// 获取5年的年份
				String[] year = new String[5];
				for (int i = 4; i >= 0; i--) {
					year[4 - i] = TimeUtil.getPastYear(i);
				}
				for (String string : year) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : year) {
					xData.add(string);
				}
				getStartEnd(year, startMap, endMap);
			} else {
				String[] year = new String[distanceYear + 1];
				for (int i = 0; i <= distanceYear; i++) {
					year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
				}
				// 给两个集合设置初始值
				for (String string : year) {
					powerMap.put(string, 0.0);
					priceMap.put(string, 0.0);
				}
				// 查询出符合条件的数据
				getPowerPriceSingle(plantId, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
				for (String string : year) {
					xData.add(string);
				}
				getStartEnd(year, startMap, endMap);
			}

		}
		// 组装前台数据
		Map<String, Object> body = new HashMap<String, Object>();
		List<Object> yData = new ArrayList<Object>();
		body.put("xData", xData);
		double maxValue = Util.isNotBlank(powerResult) ? Collections.max(powerResult) : 0.0;
		// 判断发电量的最大值是否超过一亿，若超过则单位换算
		if (maxValue > 100000000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.divideDouble(powerResult.get(i), 100000000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			powerResultMap.put("unit", "亿度");
			yData.add(powerResultMap);
			// 判断发电量的最大值是否超过一万，若超过则单位换算
		} else if (maxValue > 10000) {
			// 当数据过大的时候进行单位的换算
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.divideDouble(powerResult.get(i), 10000, 1));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			powerResultMap.put("unit", "万度");
			yData.add(powerResultMap);
		} else {
			for (int i = 0; i < powerResult.size(); i++) {
				powerResult.set(i, Util.getRetainedDecimal(powerResult.get(i)));
			}
			Map<String, Object> powerResultMap = new HashMap<String, Object>();
			powerResultMap.put("name", "发电量");
			powerResultMap.put("value", powerResult);
			powerResultMap.put("unit", "度");
			yData.add(powerResultMap);
		}
		body.put("maxValue", Collections.max(powerResult));
		body.put("yData", yData);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getPriceBarByTimeWx(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		Map<String, Object> body = new HashMap<String, Object>();
		// 将前台数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 根据tokenId获取到当前登录的用户
		User u = session.getAttribute(map.get("tokenId") + "");
		String tokenId = String.valueOf(map.get("tokenId"));
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		if (Util.isNotBlank(plantIdList)) {
			Map<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("reList", plantIdList);
			// 存放查询出来的发电量的数据
			Map<String, Double> powerMap = new TreeMap<String, Double>();
			// 存放查询出来的收益的数据
			Map<String, Double> priceMap = new TreeMap<String, Double>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 获取当天的日期
			String today = sdf.format(new Date());
			List<Double> powerResult = new ArrayList<Double>();
			List<Double> priceResult = new ArrayList<Double>();
			List<Object> xData = new ArrayList<Object>();
			Map<String, String> startMap = new TreeMap<String, String>();
			Map<String, String> endMap = new TreeMap<String, String>();
			// 如果维度没有或者维度为日；那么都用日维度
			if ((dimension == null || dimension.equals("null")) || dimension.equals("day")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y-%m-%d");
				powerPriceMap.put("sdf", "yyyy-MM-dd");
				// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推6天，共7天
				String createTime = plantMapper.getCreateTime(countMap).get("time");
				Date dBegin = sdf.parse(createTime);
				Date dEnd = sdf.parse(today);
				List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
				// 判断当前日期与电站接入日期之间的天数，若不足10天则从接入日期开始到当前日期结束
				if (listDate.size() >= 7) {
					// 获取7天日期
					String[] days = new String[7];
					for (int i = 6; i >= 0; i--) {
						days[6 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat2(days, xData);
					getStartEnd(days, startMap, endMap);
				} else {
					String[] days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
					// 给两个集合设置初始值
					for (String string : days) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, days, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					updateDateFormat(days, xData);
					getStartEnd(days, startMap, endMap);
				}
				// 维度为月
			} else if (dimension.equals("month")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y-%m");
				powerPriceMap.put("sdf", "yyyy-MM");
				String createTime = plantMapper.getCreateTimeByMon(countMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
				int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
				// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
				if (distanceMon >= 12) {
					// 获取6个月的月份
					String[] mon = new String[12];
					for (int i = 11; i >= 0; i--) {
						mon[11 - i] = TimeUtil.getPastMonAndYear(i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				} else {
					int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					String[] mon = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						mon[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : mon) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, mon, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : mon) {
						xData.add(string);
					}
					getStartEnd(mon, startMap, endMap);
				}
			} else if (dimension.equals("year")) {
				Map<String, String> powerPriceMap = new HashMap<String, String>();
				powerPriceMap.put("date", "%Y");
				powerPriceMap.put("sdf", "yyyy");
				// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
				String createTime = plantMapper.getCreateTimeByYear(countMap).get("time");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
				int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
				if (distanceYear >= 5) {
					// 获取5年的年份
					String[] year = new String[5];
					for (int i = 4; i >= 0; i--) {
						year[4 - i] = TimeUtil.getPastYear(i);
					}
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				} else {
					String[] year = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						year[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
					}
					// 给两个集合设置初始值
					for (String string : year) {
						powerMap.put(string, 0.0);
						priceMap.put(string, 0.0);
					}
					// 查询出符合条件的数据
					getPowerPriceBar(countMap, year, powerMap, priceMap, powerResult, priceResult, powerPriceMap);
					for (String string : year) {
						xData.add(string);
					}
					getStartEnd(year, startMap, endMap);
				}
			}
			// 组装前台数据
			List<Object> yData = new ArrayList<Object>();
			body.put("xData", xData);
			double maxValue = Util.isNotBlank(powerResult) ? Collections.max(powerResult) : 0.0;
			if (maxValue > 1000000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.divideDouble(powerResult.get(i), 1000000, 1));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "GWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
				// 判断发电量的最大值是否超过一万，若超过则单位换算
			} else if (maxValue > 10000) {
				// 当数据过大的时候进行单位的换算
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.divideDouble(powerResult.get(i), 1000, 1));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "MWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
			} else {
				for (int i = 0; i < powerResult.size(); i++) {
					powerResult.set(i, Util.getRetainedDecimal(powerResult.get(i)));
				}
				Map<String, Object> powerResultMap = new HashMap<String, Object>();
				powerResultMap.put("name", "发电量");
				powerResultMap.put("value", powerResult);
				yData.add(powerResultMap);
				body.put("unity1", "kWh");
				body.put("maxData1", Util.getRetainedDecimal(Collections.max(powerResult)));
			}
			body.put("yData", yData);
			body.put("start", startMap.get("startTime"));
			body.put("end", endMap.get("endTime"));
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
	public String getNowdayIncomeOfDevice(String deviceId) {
		String result = "0";
		/**
		 *
		 */
		int status = SystemCache.deviceOfStatus.get(deviceId);
		if (status == 1 || status == 0) {
			Map<String, String> currentParam = cacheUtil.getMap(deviceId);
			Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
			String guid = guidMap.get(deviceId).get("energy");
			if (currentParam != null && !currentParam.isEmpty()) {
				result = currentParam.containsKey(guid)
						? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
						: "0";
			}
		} else {
			Map<String, Object> paramMap = new HashMap<>(3);
			paramMap.put("maxTime", Util.getBeforStorageDayMax(0));
			paramMap.put("minTime", Util.getBeforStorageDayMin(0));
			paramMap.put("devices", new String[] { deviceId });
			InventerStorageData dataTemp = inventerStorageDataMapper.getDataStringForStorage(paramMap);
			if (dataTemp != null) {
				result = String.valueOf(Util.getDecimalFomat2().format(dataTemp.getDailyEnergy()));
			}
		}
		return result;
	}

	@Override
	public MessageBean getTodayChargeEle(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);

		// 区分光储
		Integer plantType = Integer.valueOf(String.valueOf(map.get("plantType")));

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("time", System.currentTimeMillis());
		// 今日充电量
		double charge = 0;
		// 今日放电量
		double disCharge = 0;
		// 发电量
		double genDay = 0;
		// 电站状态list
		List<Integer> statusList = new ArrayList<Integer>();
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		Map<String, List<Integer>> plantIdMap = plantInfoServiceImpl.getPlantsTypeForUser(tokenId, session, 1);
		if (Util.isNotBlank(plantIdMap)) {
			if (PTAC.PHOTOVOLTAIC == plantType) {
				// 光伏电站id
				List<Integer> pvPlantId = plantIdMap.get("1");
				if (Util.isNotBlank(pvPlantId)) {
					// 查询出当前公司下所有的光伏电站
					List<PlantInfo> plantList = plantMapper.getPlantByIds(pvPlantId);
					if (Util.isNotBlank(plantList)) {
						for (int i = 0, size = plantList.size(); i < size; i++) {
							PlantInfo plantInfo = plantList.get(i);
							String plantId = plantInfo.getId() + "";
							// 电站状态
							statusList.add(ServiceUtil.getPlantStatus(plantId));
							Map<String, Object> tempMap = statisticalGainService.getNowDayIncome(plantId);
							genDay = Util.addFloat(genDay, Double.valueOf(tempMap.get("power") + ""), 0);
						}
					}
				}
			}
			if (PTAC.STOREDENERGY == plantType) {
				// 储能电站id
				List<Integer> storagePlantId = plantIdMap.get("2");
				if (Util.isNotBlank(storagePlantId)) {
					// 查询出当前公司下所有的光伏电站
					List<PlantInfo> plantList = plantMapper.getPlantByIds(storagePlantId);
					if (Util.isNotBlank(plantList)) {
						for (int i = 0, size = plantList.size(); i < size; i++) {
							PlantInfo plantInfo = plantList.get(i);
							String plantId = plantInfo.getId() + "";
							// 电站状态
							statusList.add(ServiceUtil.getPlantStatus(plantId));
							Map<String, Object> tempMap = statisticalGainService.getNowDayIncome(plantId);
							charge = Util.addFloat(charge, Double.valueOf(tempMap.get("dailyNegEnergy") + ""), 0);
							disCharge = Util.addFloat(disCharge, Double.valueOf(tempMap.get("dailyPosEnergy") + ""), 0);
						}
					}
				}
			}
			// 正常发电
			int gen = 0;
			// 待机
			int standby = 0;
			// 故障
			int fault = 0;
			// 中断、断链
			int breaks = 0;
			// 异常
			int abnormal = 0;
			for (Integer integer : statusList) {
				switch (integer) {
				case 0:
					gen++;
					break;
				case 1:
					standby++;
					break;
				case 2:
					fault++;
					break;
				case 3:
					breaks++;
					break;
				case 4:
					abnormal++;
					break;
				default:
					break;
				}
			}
			// 组装前台数据
			resultMap.put("gen", gen);
			resultMap.put("standby", standby);
			resultMap.put("fault", fault);
			resultMap.put("break", breaks);
			resultMap.put("abnormal", abnormal);
			resultMap.put("charge", charge);
			resultMap.put("disCharge", disCharge);
			resultMap.put("genDay", genDay);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}

	@Override
	public MessageBean getTotalPhotovoltaic(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 区分光储
		Integer plantTpye = Integer.valueOf(String.valueOf(map.get("plantType")));
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		Map<String, List<Integer>> plantIdMap = plantInfoServiceImpl.getPlantsTypeForUser(tokenId, session, 1);
		// 储能总容量
		double totalCap = 0;
		// 总存储电量
		double totalElectricity = 0;
		// 累计发电量(历史发电量+今日发电量)
		double totalGen = 0;
		// 光伏总容量
		double totalPhoCap = 0;
		// 今日发电量
		double genDay = 0;
		if (Util.isNotBlank(plantIdMap)) {
			if (PTAC.PHOTOVOLTAIC == plantTpye) {
				// 光伏电站id
				List<Integer> pvPlantId = plantIdMap.get("1");
				if (Util.isNotBlank(pvPlantId)) {
					// 查询出当前公司下所有的光伏电站
					List<PlantInfo> plantList = plantMapper.getPlantByIds(pvPlantId);
					if (Util.isNotBlank(plantList)) {
						for (int i = 0, size = plantList.size(); i < size; i++) {
							PlantInfo plantInfo = plantList.get(i);
							String plantId = plantInfo.getId() + "";
							// 累加光伏总容量
							if (plantInfo.getUnit().equalsIgnoreCase("MW")) {
								totalPhoCap = Util.addFloat(totalPhoCap,
										Util.multFloat(plantInfo.getCapacity(), 1000, 0), 0);
							} else if (plantInfo.getUnit().equalsIgnoreCase("GW")) {
								totalPhoCap = Util.addFloat(totalPhoCap,
										Util.multFloat(plantInfo.getCapacity(), 1000000, 0), 0);
							} else {
								totalPhoCap = Util.addFloat(totalPhoCap, plantInfo.getCapacity(), 0);
							}
							// 累加发电量
							Map<String, Object> tempMap = statisticalGainService.getHistoryIncome(plantId);
							totalGen = Util.addFloat(totalGen, Double.valueOf(tempMap.get("power") + ""), 0);
							// 今日发电量
							Map<String, Object> genDayMap = statisticalGainService.getNowDayIncome(plantId);
							genDay = Util.addFloat(genDay, Double.valueOf(tempMap.get("power") + ""), 0);
							// 该电站累计发电量 = 历史发电量 + 今日发电量
							totalGen = Util.addFloat(totalGen, genDay, 0);
						}
					}
				}
			}
			if (PTAC.STOREDENERGY == plantTpye) {
				// 储能电站id
				List<Integer> storagePlantId = plantIdMap.get("2");
				if (Util.isNotBlank(storagePlantId)) {
					// 查询出当前公司下所有的储能电站
					List<PlantInfo> plantList = plantMapper.getPlantByIds(storagePlantId);
					if (Util.isNotBlank(plantList)) {
						for (int i = 0, size = plantList.size(); i < size; i++) {
							PlantInfo plantInfo = plantList.get(i);
							String plantId = plantInfo.getId() + "";
							// 累加储能总容量
							if (plantInfo.getUnit().equalsIgnoreCase("MWh")) {
								totalCap = Util.addFloat(totalCap, Util.multFloat(plantInfo.getCapacity(), 1000, 0), 0);
							} else if (plantInfo.getUnit().equalsIgnoreCase("GWh")) {
								totalCap = Util.addFloat(totalCap, Util.multFloat(plantInfo.getCapacity(), 1000000, 0),
										0);
							} else {
								totalCap = Util.addFloat(totalCap, plantInfo.getCapacity(), 0);
							}
							// 累加总存储电量
							Map<String, Object> tempMap = statisticalGainService.getNowDayIncome(plantId);
							totalElectricity = Util.addFloat(totalElectricity,
									Double.valueOf(tempMap.get("totalNegPrice") + ""), 0);
						}
					}
				}
				// 组装前台数据
				resultMap.put("totalCap", totalCap);
				resultMap.put("totalElectricity", totalElectricity);
				resultMap.put("totalGen", totalGen);
				resultMap.put("totalPhoCap", totalPhoCap);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultMap);
				msg.setMsg(Msg.SELECT_SUCCUESS);
			}
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}

	@Override
	public MessageBean getElectricityProfit(String str, Session session)
			throws SessionException, ServiceException, SessionTimeoutException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		// 通过tokenId获取当前登陆的用户
		String tokenId = String.valueOf(map.get("tokenId"));
		/** 先获取xData */
		// 获取前台参数
		String dimension = String.valueOf(map.get("dimension"));
		String startTime = String.valueOf(map.get("startTime"));
		String endTime = String.valueOf(map.get("endTime"));
		// 存放发电量
		Map<String, Double> powerMap = new TreeMap<String, Double>();
		// 存放充电量
		Map<String, Double> chargeMap = new TreeMap<String, Double>();
		// 存放放电量
		Map<String, Double> dischargeMap = new TreeMap<String, Double>();
		// 存放光伏收益
		Map<String, Double> pvPriceMap = new TreeMap<String, Double>();
		// 存放储能收益
		Map<String, Double> storagePriceMap = new TreeMap<String, Double>();
		// 存放横坐标日期
		List<Object> xData = new ArrayList<Object>();
		// 存放纵坐标
		List<Object> yData = new ArrayList<Object>();
		// 天数数组
		String[] days = null;
		// 当前登录用户拥有的所有电站id集合
		List<Integer> plantIdList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
		if (Util.isNotBlank(plantIdList)) {
			Map<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("reList", plantIdList);
			// 获取当天的日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			Map<String, String> startMap = new TreeMap<String, String>();
			Map<String, String> endMap = new TreeMap<String, String>();
			Map<String, String> powerPriceMap = new HashMap<String, String>();
			// 如果维度没有或者维度为日；那么用日维度
			if ((dimension == null || dimension.equals("null")) || dimension.equals("day")) {
				powerPriceMap.put("date", "%Y-%m-%d");
				powerPriceMap.put("sdf", "yyyy-MM-dd");
				// 如果起始时间和结束时间都没有，那么默认从当前时间起向前推14天，共15天
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTime(countMap).get("time");
					Date dBegin = sdf.parse(createTime);
					Date dEnd = sdf.parse(today);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					// 判断当前日期与电站接入日期之间的天数，若不足15天则从接入日期开始到当前日期结束
					if (listDate.size() >= 15) {
						// 获取15天日期
						days = new String[15];
						for (int i = 14; i >= 0; i--) {
							days[14 - i] = TimeUtil.getSpecifiedDayBefore(today, i);
						}
					} else {
						days = new String[listDate.size()];
						for (int i = 0; i < listDate.size(); i++) {
							days[i] = sdf.format(listDate.get(i));
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					// 获取起始日期到当前日期的天数
					long distance = TimeUtil.getDistanceDay(startTime);
					// 判断距离是否有30天
					if (distance < 30) {
						// 获取相差的日期
						days = new String[Integer.valueOf(distance + "") + 1];
						for (int i = 0; i <= Integer.valueOf(distance + ""); i++) {
							days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
						}
					} else {
						// 获取相差的日期
						days = new String[30];
						for (int i = 0; i <= 29; i++) {
							days[i] = TimeUtil.getSpecifiedDayAfter(startTime, i);
						}
					}
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTime(countMap).get("time");
					Date dBegin = sdf.parse(createTime);
					Date dEnd = sdf.parse(endTime);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					// 判断当前日期与电站接入日期之间的天数，若不足7天则从接入日期开始到当前日期结束
					if (listDate.size() >= 30) {
						// 获取相差的日期
						days = new String[30];
						for (int i = 29; i >= 0; i--) {
							days[29 - i] = TimeUtil.getSpecifiedDayBefore(endTime, i);
						}
					} else {
						days = new String[listDate.size()];
						for (int i = 0; i < listDate.size(); i++) {
							days[i] = sdf.format(listDate.get(i));
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					Date dBegin = sdf.parse(startTime);
					Date dEnd = sdf.parse(endTime);
					List<Date> listDate = TimeUtil.getDatesBetweenTwoDate(dBegin, dEnd);
					days = new String[listDate.size()];
					for (int i = 0; i < listDate.size(); i++) {
						days[i] = sdf.format(listDate.get(i));
					}
				}
			} else if (dimension.equals("month")) {
				powerPriceMap.put("date", "%Y-%m");
				powerPriceMap.put("sdf", "yyyy-MM");
				// 如果起始时间和结束时间都没有，那么默认从当月起向前推5个月，共6个月
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByMon(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
					int distanceMon = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
					// 判断当前日期与电站接入日期之间的月数，若不足6个月则从接入日期开始到当前日期结束
					if (distanceMon >= 6) {
						// 获取6个月的月份
						days = new String[6];
						for (int i = 5; i >= 0; i--) {
							days[5 - i] = TimeUtil.getPastMonAndYear(i);
						}
					} else {
						int distance = TimeUtil.countMonths(createTime, sdf1.format(new Date()));
						days = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							days[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String todayMon = TimeUtil.getPastMonAndYear(0);
					int distance = TimeUtil.countMonths(startTime, todayMon);
					if (distance < 12) {
						days = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							days[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
						}
					} else {
						// 获取相差的日期
						days = new String[12];
						for (int i = 0; i <= 11; i++) {
							days[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
						}
					}
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByMon(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
					int distanceMon = TimeUtil.countMonths(createTime, endTime);
					// 判断当前日期与电站接入日期之间的月数，若不足12个月则从接入日期开始到当前日期结束
					if (distanceMon >= 12) {
						// 获取相差的日期
						days = new String[12];
						for (int i = 11; i >= 0; i--) {
							days[11 - i] = TimeUtil.getSpecifiedMonBefore(endTime, i);
						}
					} else {
						int distance = TimeUtil.countMonths(createTime, endTime);
						days = new String[distance + 1];
						for (int i = 0; i <= distance; i++) {
							days[i] = TimeUtil.getSpecifiedMonAfter(createTime, i);
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					int distance = TimeUtil.countMonths(startTime, endTime);
					days = new String[distance + 1];
					for (int i = 0; i <= distance; i++) {
						days[i] = TimeUtil.getSpecifiedMonAfter(startTime, i);
					}
				}
			} else if (dimension.equals("year")) {
				powerPriceMap.put("date", "%Y");
				powerPriceMap.put("sdf", "yyyy");
				if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByYear(countMap).get("time");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
					int distanceYear = TimeUtil.getDateLength(createTime, sdf1.format(new Date()));
					if (distanceYear >= 5) {
						// 获取5年的年份
						days = new String[5];
						for (int i = 4; i >= 0; i--) {
							days[4 - i] = TimeUtil.getPastYear(i);
						}
					} else {
						days = new String[distanceYear + 1];
						for (int i = 0; i <= distanceYear; i++) {
							days[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime == null || endTime.equals("null") || endTime.equals(""))) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
					int distanceYear = TimeUtil.getDateLength(startTime, sdf1.format(new Date()));
					if (distanceYear < 25) {
						days = new String[distanceYear + 1];
						for (int i = 0; i <= distanceYear; i++) {
							days[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
						}
					} else {
						// 获取相差的日期
						days = new String[25];
						for (int i = 0; i <= 24; i++) {
							days[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
						}
					}
				} else if ((startTime == null || startTime.equals("null") || startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					String createTime = plantMapper.getCreateTimeByYear(countMap).get("time");
					int distanceMon = TimeUtil.getDateLength(createTime, endTime);
					// 判断当前日期与电站接入日期之间的年数，若不足25个月则从接入日期开始到当前日期结束
					if (distanceMon >= 25) {
						// 获取相差的日期
						days = new String[25];
						for (int i = 24; i >= 0; i--) {
							days[24 - i] = TimeUtil.getSpecifiedYearBefore(endTime, i);
						}
					} else {
						days = new String[distanceMon + 1];
						for (int i = 0; i <= distanceMon; i++) {
							days[i] = TimeUtil.getSpecifiedYearAfter(createTime, i);
						}
					}
				} else if ((startTime != null && !startTime.equals("null") && !startTime.equals(""))
						&& (endTime != null && !endTime.equals("null") && !endTime.equals(""))) {
					int distanceYear = TimeUtil.getDateLength(startTime, endTime);
					days = new String[distanceYear + 1];
					for (int i = 0; i <= distanceYear; i++) {
						days[i] = TimeUtil.getSpecifiedYearAfter(startTime, i);
					}
				}
			}
			// 给五个Map附上初始值
			for (int i = 0, length = days.length; i < length; i++) {
				powerMap.put(days[i], 0.0);
				chargeMap.put(days[i], 0.0);
				dischargeMap.put(days[i], 0.0);
				pvPriceMap.put(days[i], 0.0);
				storagePriceMap.put(days[i], 0.0);
			}
			/** 得到日期后，光伏电站获取日期对应的发电量和收益，储能电站获取对应的充电量、放电量和收益 */
			Map<String, List<Integer>> plantIdMap = plantInfoServiceImpl.getPlantsTypeForUser(tokenId, session, 1);
			if (Util.isNotBlank(plantIdMap)) {
				//// 光伏电站id
				// List<Integer> pvPlantId = plantIdMap.get("1");
				// if (Util.isNotBlank(pvPlantId)) {
				// yData = getPvPowerPrice(pvPlantId, powerPriceMap, days, powerMap, pvPriceMap,
				//// yData);
				// }
				// 储能电站
				List<Integer> storagePlantId = plantIdMap.get("2");
				if (Util.isNotBlank(storagePlantId)) {
					yData = getstoragePowerPrice(storagePlantId, powerPriceMap, days, chargeMap, dischargeMap,
							storagePriceMap, yData);
				}
			}
		}
		List priceList = new ArrayList<>();
		for (int i = 0, length = days.length; i < length; i++) {
			priceList.add(Util.addFloat(pvPriceMap.get(days[i]), storagePriceMap.get(days[i]), 0));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>(3);
		tempMap.put("name", "收益");
		tempMap.put("type", "line");
		tempMap.put("value", priceList);
		yData.add(tempMap);

		Map<String, String> startMap = new TreeMap<String, String>();
		Map<String, String> endMap = new TreeMap<String, String>();
		getStartEnd(days, startMap, endMap);
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("yData", yData);
		body.put("start", startMap.get("startTime"));
		body.put("end", endMap.get("endTime"));
		body.put("xData", days);
		body.put("unity1", "kWh");
		body.put("unity2", "元");
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(body);
		return msg;
	}

	public List<Object> getPvPowerPrice(List<Integer> pvPlantId, Map<String, String> powerPriceMap, String[] days,
			Map<String, Double> powerMap, Map<String, Double> pvPriceMap, List<Object> yData) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		for (int i = 0, size = pvPlantId.size(); i < size; i++) {
			powerPriceMap.put("plantId", pvPlantId.get(i) + "");
			powerPriceMap.put("startTime", days[0]);
			powerPriceMap.put("endTime", days[days.length - 1]);
			List<Map<String, Object>> GeneratingList = inventerStorageDataMapper.getPowerPriceByDay(powerPriceMap);
			for (Map<String, Object> map2 : GeneratingList) {
				Double oldNum = powerMap.get(map2.get("time") + "");
				powerMap.put(map2.get("time") + "", Util.addFloat(oldNum, Double.valueOf(map2.get("sum") + ""), 0));
				Double oldPrice = pvPriceMap.get(map2.get("time") + "");
				pvPriceMap.put(map2.get("time") + "",
						Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice") + ""), 0));
			}
			// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
			int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
			if (a > 0) {
				Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(pvPlantId.get(i) + "");
				if (todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")) {
					Double oldNum = powerMap.get(sdf.format(System.currentTimeMillis()));
					powerMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("power") + ""), 0));
				}
				if (todayPricePower.get("inCome") != "null" && !todayPricePower.get("inCome").equals("")) {
					Double oldNum = pvPriceMap.get(sdf.format(System.currentTimeMillis()));
					pvPriceMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldNum, Double.valueOf(todayPricePower.get("inCome") + ""), 0));
				}
			}
		}
		List powerResult = new ArrayList<>();
//		List priceResult = new ArrayList<>();
		for (int i = 0, length = days.length; i < length; i++) {
			powerResult.add(Double.valueOf(powerMap.get(days[i]) + ""));
//			priceResult.add(Double.valueOf(pvPriceMap.get(days[i])+""));
		}
		Map<String, Object> pvMap1 = new HashMap<String, Object>(3);
		pvMap1.put("name", "发电量");
		pvMap1.put("type", "bar");
		pvMap1.put("value", powerResult);
//		Map<String, Object> pvMap2 = new HashMap<String,Object>(3);
//		pvMap2.put("name", "收益");
//		pvMap2.put("type", "line");
//		pvMap2.put("value", priceResult);
		yData.add(pvMap1);
//		yData.add(pvMap2);
		return yData;
	}

	public List<Object> getstoragePowerPrice(List<Integer> storagePlantId, Map<String, String> powerPriceMap,
			String[] days, Map<String, Double> chargeMap, Map<String, Double> dischargeMap,
			Map<String, Double> storagePriceMap, List<Object> yData) {
		SimpleDateFormat sdf = new SimpleDateFormat(powerPriceMap.get("sdf"));
		for (int i = 0, size = storagePlantId.size(); i < size; i++) {
			powerPriceMap.put("plantId", storagePlantId.get(i) + "");
			powerPriceMap.put("startTime", days[0]);
			powerPriceMap.put("endTime", days[days.length - 1]);
			List<Map<String, Object>> storagePowerPriceList = energyMapper.getStorageNegPosPrice(powerPriceMap);
			if (Util.isNotBlank(storagePowerPriceList)) {
				for (Map<String, Object> map2 : storagePowerPriceList) {
					Double oldNeg = chargeMap.get(map2.get("time") + "");
					chargeMap.put(map2.get("time") + "",
							Util.addFloat(oldNeg, Double.valueOf(map2.get("neg") + ""), 0));
					Double oldPos = dischargeMap.get(map2.get("time") + "");
					dischargeMap.put(map2.get("time") + "",
							Util.addFloat(oldPos, Double.valueOf(map2.get("pos") + ""), 0));
					Double oldPrice = storagePriceMap.get(map2.get("time") + "");
					storagePriceMap.put(map2.get("time") + "",
							Util.addFloat(oldPrice, Double.valueOf(map2.get("price") + ""), 0));
				}
				// 判断要显示的日期是否包含当天，若包含则获取到当天的实时数据
				int a = Arrays.binarySearch(days, sdf.format(System.currentTimeMillis()));
				if (a > 0) {
					Map<String, Object> todayPricePower = statisticalGainService
							.getNowDayIncome(storagePlantId.get(i) + "");
					Double oldNeg = chargeMap.get(sdf.format(System.currentTimeMillis()));
					chargeMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldNeg, Double.valueOf(todayPricePower.get("dailyNegEnergy") + ""), 0));
					Double oldPos = dischargeMap.get(sdf.format(System.currentTimeMillis()));
					dischargeMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldPos, Double.valueOf(todayPricePower.get("dailyPosEnergy") + ""), 0));
					Double oldPrice = storagePriceMap.get(sdf.format(System.currentTimeMillis()));
					storagePriceMap.put(sdf.format(System.currentTimeMillis()),
							Util.addFloat(oldPrice, Double.valueOf(todayPricePower.get("dailyPrice") + ""), 0));
				}
			}
		}
		List negResult = new ArrayList<>();
		List posResult = new ArrayList<>();
//		List priceResult = new ArrayList<>();
		for (int i = 0, length = days.length; i < length; i++) {
			negResult.add(Double.valueOf(chargeMap.get(days[i]) + ""));
			posResult.add(Double.valueOf(dischargeMap.get(days[i]) + ""));
//			priceResult.add(Double.valueOf(storagePriceMap.get(days[i])+""));
		}
		Map<String, Object> pvMap1 = new HashMap<String, Object>(3);
		pvMap1.put("name", "充电量");
		pvMap1.put("type", "bar");
		pvMap1.put("value", negResult);
		Map<String, Object> pvMap2 = new HashMap<String, Object>(3);
		pvMap2.put("name", "放电量");
		pvMap2.put("type", "bar");
		pvMap2.put("value", posResult);
//		Map<String, Object> pvMap3 = new HashMap<String,Object>(3);
//		pvMap3.put("name", "储能收益");
//		pvMap3.put("type", "line");
//		pvMap3.put("value", priceResult);
		yData.add(pvMap1);
		yData.add(pvMap2);
//		yData.add(pvMap3);
		return yData;
	}
}
