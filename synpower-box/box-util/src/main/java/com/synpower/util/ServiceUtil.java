package com.synpower.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.synpower.bean.CollDevice;
import com.synpower.bean.DataBms;
import com.synpower.bean.DataBoxChange;
import com.synpower.bean.DataCentralInverter;
import com.synpower.bean.DataElectricMeter;
import com.synpower.bean.DataPcs;
import com.synpower.bean.SysAddress;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysRights;
import com.synpower.bean.SysRole;
import com.synpower.constant.DTC;
import com.synpower.lang.ServiceException;

public class ServiceUtil {
	/**
	 * @param list
	 * @Title: getTree
	 * @Description: 权限树状解析
	 * @return: Node
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年10月27日上午10:03:17
	 */
	public static List<Node> parseRights(List<SysRights> list) {
		List<Node> resultList = new ArrayList<>();

		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				SysRights right = list.get(i);
				if ("0".equals(right.getFatherId() + "")) {
					Node rootNode = new Node();
					rootNode.setId(right.getRightsMark());
					rootNode.setSn(right.getId().intValue());
					rootNode.setRightsName(right.getRightsName());
					rootNode = getTree(list, rootNode);
					resultList.add(rootNode);
					// 减少递归搜索范围
					// list.remove(i);
					// i--;
				}
			}
		}
		return resultList;
	}

	public static Node getTree(List<SysRights> list, Node node) {
		List<Node> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysRights right = list.get(i);
			int fatherId = node.getSn();
			if (fatherId == right.getFatherId().intValue()) {
				Node childNode = new Node();
				childNode.setId(right.getRightsMark());
				childNode.setSn(right.getId().intValue());
				childNode.setRightsName(right.getRightsName());
				childList.add(childNode);
				// 减少递归搜索范围
				// list.remove(i);
				// i--;
			}
		}
		if (!list.isEmpty()) {
			node.setChild(childList);
			for (int i = 0; i < childList.size(); i++) {
				getTree(list, childList.get(i));
			}
		}
		return node;
	}

	public static void checkTelNo(String telNo) throws ServiceException {
		if (StringUtils.isBlank(telNo)) {
			throw new ServiceException("手机号不能为空");
		}
		String rex = "^1[3|4|5|6|7|8|9][0-9]{9}$";
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(telNo);
		boolean flag = matcher.matches();
		if (!flag) {
			throw new ServiceException("请输入正确手机号");
		}
	}

	/**
	 * @param      list<SysOrg> 待解析的集合 node 当前节点 resultList结果集 nodeNum 层级
	 * @param node
	 * @Title: getTree
	 * @Description: 组织树状解析
	 * @return: Node
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年10月27日上午10:03:17
	 */
	public static List<ZTreeNode> getTree(List<SysOrg> list, ZTreeNode node, List<ZTreeNode> resultList, int nodeNum) {
		List<ZTreeNode> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			String fatherId = node.getId();
			// 计数第几个节点 用于节点是否展开
			int count = 1;
			if (fatherId.equals(org.getFatherId() + "")) {
				// 添加节点
				ZTreeNode childNode = new ZTreeNode();
				childNode.setId(org.getId() + "");
				childNode.setName(org.getOrgName());
				childNode.setpId(fatherId + "");
				childNode.setIconSkin("ztree" + org.getId());
				// 减少递归搜索范围
				list.remove(i);
				i--;
				// 控制节点展开接选中
				if (nodeNum == 2 && count == 1) {
					childNode.setChecked(true);
					childNode.setOpen(true);
					count++;
					// 控制节点展开
				} else if (nodeNum == 2) {
					childNode.setOpen(true);
				}
				resultList.add(childNode);
				childList.add(childNode);
			}
			// 节点层级只加一次
			if (count == 2) {
				nodeNum++;
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getTree(list, childList.get(i), resultList, nodeNum);
			}
		} else {
			// 添加无子节点的节点属性
			for (int i = 0; i < resultList.size(); i++) {
				ZTreeNode nodeTemp = resultList.get(i);
				if (node.getId() == nodeTemp.getId()) {
					nodeTemp.setIsParent(true);
				}
			}
		}
		return resultList;
	}

	/**
	 * @param list<SysOrg> 待解析的集合 node 当前节点 resultList结果集 nodeNum 层级
	 * @Title: getTree
	 * @Description: 组织树状解析
	 * @return: Node
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年10月27日16:33:56
	 */
	public static List<String> getTree(List<SysOrg> list, String orgId, List<String> resultList) {
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getFatherId() + "")) {
				// 添加节点
				resultList.add(org.getId() + "");
				childList.add(org.getId() + "");
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getTree(list, childList.get(i), resultList);
			}
		}
		return resultList;
	}

	/**
	 * @param list
	 * @param orgId
	 * @param resultList
	 * @Title: getTreeSup
	 * @Description: TODO
	 * @return: List<String>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午3:01:32
	 */
	public static List<String> getTreeSup(List<CollDevice> list, String orgId, List<String> resultList) {
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			CollDevice org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getSupid() + "")) {
				// 添加节点
				resultList.add(org.getId() + "");
				childList.add(org.getId() + "");
				// 减少递归搜索范围
				list.remove(i);
				i--;
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getTreeSup(list, childList.get(i), resultList);
			}
		}
		return resultList;
	}

	/**
	 * @param list<SysOrg> 待解析的集合 node 当前节点 resultList结果集 nodeNum 层级
	 * @Title: getTree
	 * @Description: 组织树状解析
	 * @return: Node
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年10月27日16:33:56
	 */
	public static List<String> getTreeofArea(List<SysAddress> list, String areaId, List<String> resultList) {
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysAddress addr = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (areaId.equals(addr.getFatherId() + "")) {
				// 添加节点
				resultList.add(addr.getId() + "");
				childList.add(addr.getId() + "");
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getTreeofArea(list, childList.get(i), resultList);
			}
		}
		return resultList;
	}

	/**
	 * @param list<SysOrg> 待解析的集合 node 当前节点 resultList结果集 nodeNum 层级
	 * @Title: getTree
	 * @Description: 组织树状解析
	 * @return: Node
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月8日11:10:18
	 */
	public static List<String> getRoleIdList(List<SysRole> list, String roleId, List<String> resultList) {
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysRole role = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (roleId.equals(role.getFatherId() + "")) {
				// 添加节点
				resultList.add(role.getId() + "");
				childList.add(role.getId() + "");
				// 减少递归搜索范围
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getRoleIdList(list, childList.get(i), resultList);
			}
		}
		return resultList;
	}

	/**
	 * @param list       待解析的集合 node 当前节点 resultList结果集 nodeNum 层级
	 * @param orgId
	 * @param resultList
	 * @Title: getFatherTree
	 * @Description: 获取fatherId
	 * @return: List<String> 返回fatherId，只有最上面的一个
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月7日下午1:55:29
	 */
	public static List<String> getFatherTree(List<SysOrg> list, String orgId, List<String> resultList) {
		List<String> fatherList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getId() + "")) {
				fatherList.add(org.getFatherId() + "");
//					if("0".equals(org.getFatherId()+"")){
				// 添加节点
				resultList.add(org.getId() + "");
//					}
				// 减少递归搜索范围
				list.remove(i);
				i--;
			}
		}
		if (!list.isEmpty()) {
			for (int i = 0; i < fatherList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getFatherTree(list, fatherList.get(i), resultList);
			}
		}
		return resultList;
	}

	public static List<SysOrg> getOrgFatherTree(List<SysOrg> list, String orgId, List<SysOrg> resultList) {
		List<SysOrg> fatherList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getId() + "")) {
				fatherList.add(org);
//					if("0".equals(org.getFatherId()+"")){
				// 添加节点
				resultList.add(org);
//					}
			}
		}
		if (!list.isEmpty()) {
			for (int i = 0; i < fatherList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getOrgFatherTree(list, String.valueOf(fatherList.get(i).getFatherId()), resultList);
			}
		}
		return resultList;
	}

	public static List<String> getFatherTreeList(List<SysOrg> list, String orgId, List<String> resultList) {
		List<String> fatherList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getId() + "")) {
				fatherList.add(org.getFatherId() + "");
//					if("0".equals(org.getFatherId()+"")){
				// 添加节点
				resultList.add(org.getId() + "");
//					}
				// 减少递归搜索范围
				list.remove(i);
				i--;
			}
		}
		if (!list.isEmpty()) {
			for (int i = 0; i < fatherList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getFatherTree(list, fatherList.get(i), resultList);
			}
		}
		return resultList;
	}

	public static List<Map<String, String>> getOrgList(List<SysOrg> list, String orgId,
			List<Map<String, String>> resultList) {
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysOrg org = list.get(i);
			// 计数第几个节点 用于节点是否展开
			if (orgId.equals(org.getFatherId() + "")) {
				// 添加节点
				Map<String, String> mapTemp = new TreeMap<>();
				mapTemp.put("orgName", org.getOrgName() + "");
				mapTemp.put("orgCode", org.getOrgCode() + "");
				mapTemp.put("orgValiCode", org.getOrgValiCode() + "");
				mapTemp.put("orgAgentorName", org.getOrgAgentorName() + "");
				mapTemp.put("orgAngetorTel", org.getOrgAngetorTel() + "");
				mapTemp.put("orgStatus", org.getOrgStatus() + "");

				resultList.add(mapTemp);
				childList.add(org.getId() + "");
				// 减少递归搜索范围
				list.remove(i);
				i--;
			}
		}

		if (!list.isEmpty()) {
			for (int i = 0; i < childList.size(); i++) {
				// 当前节点下所有子节点继续递归解析
				getOrgList(list, childList.get(i), resultList);
			}
		}
		return resultList;
	}

	/**
	 * @param param
	 * @return
	 * @throws IOException:     String
	 * @throws ServiceException
	 * @Title: getValidateSMS
	 * @Description: 短信验证码模板内容填充
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月8日上午11:06:41
	 */
	public static String getValidateSMS(String param) throws ServiceException {
		String[] params = { param };
		return PropertiesUtil.getSMSTemplate("validateSMS", params);
	}

	/**
	 * @return
	 * @throws ServiceException: String
	 * @Title: getDailyReportSMS
	 * @Description: TODO
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月4日下午1:36:04
	 */
	public static String getDailyReportSingle(String[] params) throws ServiceException {
		// String[] params={param};
		return PropertiesUtil.getSMSTemplate("dailyReportSingle", params);
	}

	public static String getDailyReportMulti(String[] params) throws ServiceException {
		// String[] params={param};
		return PropertiesUtil.getSMSTemplate("dailyReportMulti", params);
	}

	/**
	 * @Title: getSunRise
	 * @Description: 得到日出时间并往前偏移成5的倍数
	 * @return: String
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日上午11:25:06
	 */
	public static String getSunRise(double longitude, double latitude) {
		String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
		String riseTime = SunRiseSet.getSunrise(longitude, latitude, nowTime, 8);
		String[] riseTimes = StringUtils.split(riseTime, ":");
		int riseMin = Integer.parseInt(riseTimes[1]);
		if (riseMin % 5 != 0) {
			riseMin = riseMin - riseMin % 5;
		}
		riseTime = riseTimes[0] + ":" + (riseMin < 10 ? "0" + riseMin : riseMin + "");
		return riseTime;
	}

	public static String getSunRise(String nowTime, double longitude, double latitude) {
		String riseTime = SunRiseSet.getSunrise(longitude, latitude, nowTime, 8);
		String[] riseTimes = StringUtils.split(riseTime, ":");
		int riseMin = Integer.parseInt(riseTimes[1]);
		if (riseMin % 5 != 0) {
			riseMin = riseMin - riseMin % 5;
		}
		riseTime = riseTimes[0] + ":" + (riseMin < 10 ? "0" + riseMin : riseMin + "");
		return riseTime;
	}

	/**
	 * @Title: getSunSet
	 * @Description: 得到日落时间并往后偏移成5的倍数
	 * @return: String
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日上午11:25:06
	 */
	public static String getSunSet(double longitude, double latitude) {
		String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
		String setTime = SunRiseSet.getSunset(longitude, latitude, nowTime, 8);
		String[] setTimes = StringUtils.split(setTime, ":");
		int setMin = Integer.parseInt(setTimes[1]);
		if (setMin % 5 != 0) {
			setMin = setMin + (5 - setMin % 5);
		}
		setTime = setTimes[0] + ":" + (setMin < 10 ? "0" + setMin : setMin + "");
		return setTime;
	}

	public static String getSunSet(String nowTime, double longitude, double latitude) {
		String setTime = SunRiseSet.getSunset(longitude, latitude, nowTime, 8);
		String[] setTimes = StringUtils.split(setTime, ":");
		int setMin = Integer.parseInt(setTimes[1]);
		if (setMin % 5 != 0) {
			setMin = setMin + (5 - setMin % 5);
		}
		setTime = setTimes[0] + ":" + (setMin < 10 ? "0" + setMin : setMin + "");
		return setTime;
	}

	/**
	 * @Title: getTimeSegment
	 * @Description: TODO
	 * @return: List<String>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日上午11:40:16
	 */
	public static List<Long> getLongTimeSegment(String riseTime, String setTime) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] setTimes = StringUtils.split(setTime, ":");
		String[] riseTimes = StringUtils.split(riseTime, ":");
		cal.set(year, month - 1, day, Integer.parseInt(riseTimes[0]), Integer.parseInt(riseTimes[1]), 0);
		cal.set(Calendar.MILLISECOND, 0);
		long startTime = cal.getTimeInMillis();
		cal.set(year, month - 1, day, Integer.parseInt(setTimes[0]), Integer.parseInt(setTimes[1]), 59);
		long endTime = cal.getTimeInMillis();
		final int seg = 5 * 60 * 1000;// 五分钟
		List<Long> list = new ArrayList<Long>();
		for (long time = startTime; time <= endTime; time += seg) {
			list.add(new Date(time).getTime());
		}
		return list;
	}

	public static List<Long> getLongTimeSegmentAnyDay(String riseTime, String setTime) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] setDays = StringUtils.split(StringUtils.split(setTime, " ")[0], "-");
		String[] riseDays = StringUtils.split(StringUtils.split(riseTime, " ")[0], "-");
		String[] setTimes = StringUtils.split(StringUtils.split(setTime, " ")[1], ":");
		String[] riseTimes = StringUtils.split(StringUtils.split(riseTime, " ")[1], ":");
		cal.set(year, Integer.parseInt(riseDays[1]) - 1, Integer.parseInt(riseDays[2]), Integer.parseInt(riseTimes[0]),
				Integer.parseInt(riseTimes[1]), 0);
		cal.set(Calendar.MILLISECOND, 0);
		long startTime = cal.getTimeInMillis();
		cal.set(year, Integer.parseInt(setDays[1]) - 1, Integer.parseInt(setDays[2]), Integer.parseInt(setTimes[0]),
				Integer.parseInt(setTimes[1]), 59);
		long endTime = cal.getTimeInMillis();
		final int seg = 5 * 60 * 1000;// 五分钟
		List<Long> list = new ArrayList<Long>();
		for (long time = startTime; time <= endTime; time += seg) {
			list.add(new Date(time).getTime());
		}
		return list;
	}

	public static List<String> getStringTimeSegment(String riseTime, String setTime) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] setTimes = StringUtils.split(setTime, ":");
		String[] riseTimes = StringUtils.split(riseTime, ":");
		cal.set(year, month - 1, day, Integer.parseInt(riseTimes[0]), Integer.parseInt(riseTimes[1]), 0);
		cal.set(Calendar.MILLISECOND, 0);
		long startTime = cal.getTimeInMillis();
		cal.set(year, month - 1, day, Integer.parseInt(setTimes[0]), Integer.parseInt(setTimes[1]), 59);
		long endTime = cal.getTimeInMillis();
		final int seg = 5 * 60 * 1000;// 五分钟
		List<String> list = new ArrayList<String>();
		for (long time = startTime; time <= endTime; time += seg) {
			list.add(sdf.format(new Date(time).getTime()));
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(getStringTimeSegment("00:00", "23:59"));
	}

	public static List<String> getStringTimeSegmentAnyDay(String riseTime, String setTime) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] setDays = StringUtils.split(StringUtils.split(setTime, " ")[0], "-");
		String[] riseDays = StringUtils.split(StringUtils.split(riseTime, " ")[0], "-");
		String[] setTimes = StringUtils.split(StringUtils.split(setTime, " ")[1], ":");
		String[] riseTimes = StringUtils.split(StringUtils.split(riseTime, " ")[1], ":");
		cal.set(year, Integer.parseInt(riseDays[1]) - 1, Integer.parseInt(riseDays[2]), Integer.parseInt(riseTimes[0]),
				Integer.parseInt(riseTimes[1]), 0);
		cal.set(Calendar.MILLISECOND, 0);
		long startTime = cal.getTimeInMillis();
		cal.set(year, Integer.parseInt(setDays[1]) - 1, Integer.parseInt(setDays[2]), Integer.parseInt(setTimes[0]),
				Integer.parseInt(setTimes[1]), 59);
		long endTime = cal.getTimeInMillis();
		final int seg = 5 * 60 * 1000;// 五分钟
		List<String> list = new ArrayList<String>();
		for (long time = startTime; time <= endTime; time += seg) {
			list.add(sdf.format(new Date(time).getTime()));
		}
		return list;
	}

	/**
	 * @param deviceList
	 * @Title: classifyDeviceForPlant
	 * @Description: 将电站所属的设备按设备类型分类, 当有电表时不统计逆变器
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日下午2:20:22
	 */
	public static Map<Integer, List<Integer>> classifyDeviceForPlant(List<CollDevice> deviceList) {
		Map<Integer, List<Integer>> result = new HashMap<>(10);
		for (int i = 0; i < deviceList.size(); i++) {
			CollDevice temp = deviceList.get(i);
			Integer deviceType = temp.getDeviceType();
			// 目前只统计1集中式逆变器2 组串式逆变器3电表
			if (deviceType == 1 || deviceType == 2 || deviceType == 3 || deviceType == 9) {
				// List<Integer> tempMap=result.get(plantId);
				if (result.containsKey(3) && deviceType == 3) {
					// 当此电站拥有电表时,以电表为准
					result.put(1, null);
					result.put(2, null);
					List<Integer> devices = result.get(3);
					devices.add(temp.getId());
					result.put(deviceType, devices);
				} else if (result.containsKey(3) && deviceType != 3) {
					continue;
				} else {
					if (result.containsKey(deviceType)) {
						List<Integer> devices = result.get(deviceType);
						devices.add(temp.getId());
						result.put(deviceType, devices);
					} else {
						List<Integer> devices = new ArrayList<>(1);
						devices.add(temp.getId());
						result.put(deviceType, devices);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param nowData
	 * @throws ParseException
	 * @Title: loadParamForCurrentPower
	 * @Description: 将数据装载到日出日落对应的时间点位上去(单电站)
	 * @return: String[]
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日下午4:04:49
	 */
	public static Map<String, Object> loadParamForCurrentPower(String riseTime, String setTime, List<Object[]> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			Object[] obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(new BigInteger(obj[0] + "").longValue())).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Object value = obj[0];
			String temp = StringUtil.checkBlank(String.valueOf(obj[1]));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
				}
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String
							.valueOf(Util.getDecimalFomat2().format(Double.parseDouble(String.valueOf(obj[1]))));
					break;
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
				maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(StringUtil.checkBlank(valueArr[i]) == "" ? "0" : valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		/*
		 * for (int j = 0; j < nowLocation+1; j++) { String val=valueArr[j]; if
		 * (val==null) { boolean b=true; String valu=null; for (int i = j; i <
		 * nowLocation+1; i++) { String va=valueArr[i]; if (va!=null) { valu=va;
		 * b=false; break; } } if (!b) { if (j>0) { for (int k = j; k >0; k--) { String
		 * compareVal=valueArr[k]; if (null!=compareVal) {
		 * valueArr[j]=Util.divideDouble(Util.addFloat(Double.parseDouble(compareVal),
		 * Double.parseDouble(valu), 0),2.0,1)+""; break; } } }else{ valueArr[j]="0"; }
		 * } } }
		 */
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
//                    String compareVal = valueArr[k];
//                    if (null != compareVal) {
//                        valueArr[j] = compareVal;
//                        break;
//                    }
					valueArr[j] = "0";
				}
			}
		}
		resultMap.put("yData1", valueArr);
		resultMap.put("maxValue", maxValue);
		resultMap.put("unit", unit);
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentListMulti(String riseTime, String setTime,
			List<Object[]> nowData, int nowLocation, String listName, String[] units, int separator) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			Object[] obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(new BigInteger(obj[0] + "").longValue())).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Object value = obj[0];
			String temp = StringUtil.checkBlank(String.valueOf(obj[1]));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
				}
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String
							.valueOf(Util.getDecimalFomat2().format(Double.parseDouble(String.valueOf(obj[1]))));
					break;
				}
			}
		}
		String unit = units[0];
		/*
		 * if (maxValue>separator*separator) { unit=units[2]; for (int i = 0; i <
		 * length; i++) { double tempValue=Double.parseDouble(valueArr[i]);
		 * valueArr[i]=(new BigDecimal(tempValue/(separator*separator)).setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue())+""; maxValue=new
		 * BigDecimal(maxValue/(separator*separator)).setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue(); } }else if (maxValue>separator) {
		 * unit=units[1]; for (int i = 0; i < length; i++) { double
		 * tempValue=Double.parseDouble(StringUtil.checkBlank(valueArr[i])==""?"0":
		 * valueArr[i]); valueArr[i]=(new BigDecimal(tempValue/separator).setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue())+""; } maxValue=new
		 * BigDecimal(maxValue/separator).setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue(); }
		 */
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		resultMap.put("value", valueArr);
		resultMap.put("maxValue", maxValue);
		resultMap.put("unit", unit);
		resultMap.put("name", listName);
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPowerForDevices(String riseTime, String setTime,
			List<Map<String, Object>> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0, minValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> obj = nowData.get(i);
			long key = (long) (obj.get("dataTime"));
			String object = String.valueOf(obj.get("activePower"));
			double tempPower = Double.parseDouble("".equals(object) ? "0" : "null".equals(object) ? "0" : object);
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = calendar.getTime().getTime();
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = calendar.getTime().getTime();
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String.valueOf(
							Util.addFloat(Double.parseDouble(null == valueArr[j] ? "0" : valueArr[j]), tempPower, 1));
					break;
				}
			}
		}
		/*
		 * for (int j = 0; j < nowLocation+1; j++) { String val=valueArr[j]; if
		 * (val==null) { boolean b=true; String valu=null; for (int i = j; i <
		 * nowLocation+1; i++) { String va=valueArr[i]; if (va!=null) { valu=va;
		 * b=false; break; } } if (b) { if (j>0) { for (int k = j; k >0; k--) { String
		 * compareVal=valueArr[k]; if (null!=compareVal&&(k-j)<5) {
		 * valueArr[j]=Util.divideDouble(Util.addFloat(Double.parseDouble(compareVal),
		 * Double.parseDouble(valu), 0),2.0,1)+""; break; } } }else{ valueArr[j]="0"; }
		 * } } }
		 */
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			double tempPower = Double.parseDouble(null == val ? "0" : "null".equals(val) ? "0" : val);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}

		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		resultMap.put("yData1", valueArr);
		resultMap.put("maxValue", maxValue);
		resultMap.put("minValue", minValue);
		resultMap.put("unit", unit);
		return resultMap;
	}

	/**
	 * @Title: getNowTimeInTimes
	 * @Description: 计算当前时间 处于5分钟间隔的 点位
	 * @return: String
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月16日下午5:36:41
	 */
	public static String getNowTimeInTimes() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String[] times = StringUtils.split(sdf.format(date), ":");
		int min = Integer.parseInt(times[1]);
		int s = Integer.parseInt(times[2]);
		if (min % 5 != 0) {
			if (min % 5 >= 3) {
				min = min + (5 - min % 5);
			} else if (min % 5 == 2 && s > 30) {
				min = min + (5 - min % 5);
			} else {
				min = min - min % 5;
			}
		}
		if (min == 60) {
			min = 0;
			times[0] = String.valueOf(Integer.parseInt(times[0]) + 1);
		}
		return times[0] + ":" + (min < 10 ? "0" + min : min + "");
	}

	/**
	 * @param valueArr
	 * @Title: getParabola
	 * @Description: 根据功率实时曲线得到抛物线集合
	 * @return: double[]
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月17日上午11:16:30
	 */
	public static String[] getParabola(String[] valueArr, double maxValue) {
		/**
		 * 计算方程 y=a*x*x+b*x+c
		 */
		// 获取最大值
		// 最后一个点的X坐标
		int endX = valueArr.length - 1;
		// 最后一个点的Y坐标
		int endY = 0;
		// 第一个点的X坐标
		int startX = 0;
		// 第一个点的Y坐标
		double startY = Double.parseDouble(Util.isNotBlank(valueArr[0]) ? valueArr[0] : "0");
		// 中间点的X坐标
		double midX = endX / 2.0;
		// 中间点的Y坐标,在最大值的基础上放大一倍
		double midY = maxValue * 2;
		// 求因子a
		double a = (midY / midX - startY / midX + startY / endX) / (midX - endX);
		// 求因子b
		double b = (-startY - a * endX * endX) / endX;
		String[] yData2 = new String[endX + 1];
		// 得到每个X轴对应的Y值
		for (int i = 0; i < yData2.length; i++) {
			BigDecimal d = new BigDecimal(a * i * i + b * i + startY);
			// 保留3位 四舍五入
			yData2[i] = d.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
		}
		return yData2;
	}

	public static int getNowLocationInTimes(String curTime, List<String> timeList) {
		int result = -1;
		boolean b = false;
		for (int i = 0; i < timeList.size(); i++) {
			if (curTime.equals(timeList.get(i))) {
				result = i;
				b = true;
				break;
			}
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
		Date date = new Date();
		int hour = Integer.parseInt(simpleDateFormat.format(date));
		String hm[] = timeList.get(0).split(":");
		int compareHour = Integer.parseInt(hm[0]);
		if (b == false && result == -1 && hour <= compareHour) {
			result = 0;
		} else if (b == false && result == -1) {
			result = timeList.size() - 1;
		}
		return result;
	}

	/**
	 * @param units 单位集合 value 需要转换的值
	 * @param value
	 * @Title: changeUnit
	 * @Description: 转换单位
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月17日下午3:08:37
	 */
	public static Map<String, Object> changeUnit(String[] units, double value) {
		String unit = units[0];
		for (int i = 0; i < units.length - 1; i++) {
			if (value > 1000000) {
				unit = units[i + 1];
				value = (new BigDecimal(value / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("value", value);
		resultMap.put("unit", unit);
		return resultMap;
	}

	/**
	 * @param units 单位集合 value 需要转换的值 separator间隔
	 * @param value
	 * @Title: changeUnit
	 * @Description: 转换单位
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月17日下午3:08:37
	 */
	public static Map<String, Object> changeUnit(String[] units, double value, int separator) {
		String unit = units[0];
		for (int i = 0; i < units.length - 1; i++) {
			if (value > separator) {
				unit = units[i + 1];
				value = (new BigDecimal(value / separator).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			} else {
				break;
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("value", value);
		resultMap.put("unit", unit);
		return resultMap;
	}

	/**
	 * @param units
	 * @param value
	 * @param separator
	 * @param nowUnit
	 * @Title: changeUnit
	 * @Description: 根据当前单位 nowUnit 将value换算成对应单位
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月20日下午1:49:32
	 */
	public static Map<String, Object> changeUnit(String[] units, double value, int separator, String nowUnit) {
		String unit = units[0];
		Map<String, Object> resultMap = new HashMap<>();
		if (value != -1.0) {
			for (int i = 1; i < units.length; i++) {
				if (nowUnit.equals(units[i])) {
					value = (new BigDecimal(value / separator).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					unit = units[i];
					break;
				} else {
					separator = separator * separator;
				}
				resultMap.put("value", value);
			}
		} else {
			resultMap.put("value", "0");
		}
		resultMap.put("unit", unit);
		return resultMap;
	}

	public static List<RegionNode> getRegionTree(List<SysAddress> list, String fatherName) {
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
					tempNode.setLevel(adrTemp.getLevel());
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
					childList.add(tempNode);
					map.put(key, 1);
				}
			}
		}
		return childList;
	}

	/**
	 * @param pId
	 * @Title: getPlantStatus
	 * @Description: 电站 状态码:0:运行，1：待机，2：故障，3：中断 4 异常 设备 状态码: 逆变器 0正常 1待机 2故障 3 通讯中断
	 *               电表 0 正常 3 通讯中断 数采 0通讯中 3通讯中断
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月23日下午6:00:03
	 */
	public static int getPlantStatus(String pId) {
		// 默认为正常
		int result = 0;
		int plantId = Integer.parseInt(pId);
		Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
		// <电站id,<设备类型id,该电站该类列设备id列表>>
		// Map<Integer, Map<Integer, List<Integer>>> deviceList
		// =SystemCache.deviceOfPlant;
		Map<Integer, Map<Integer, List<Integer>>> deviceList = SystemCache.deviceOfPlantForStatus;

		// 正常汇流箱数量
		int normalCombinerBoxCount = 0;

		// 该电站设备状态列表 除数采外
		List<Integer> statusList = new ArrayList<>();
		// 如果系统无设备
		if (Util.isNotBlank(deviceList)) {
			Map<Integer, List<Integer>> devices = deviceList.get(plantId);
			// 如果电站无设备
			if (Util.isNotBlank(devices)) {
				// 整合设备集
				for (Entry<Integer, List<Integer>> entry : devices.entrySet()) {

					// 设备id
					Integer key = entry.getKey();

					// 如果为数采 就不管
					if (key == DTC.DATA_COLLECTOR) {
						continue;
					}

					// 设备id列表
					List<Integer> values = entry.getValue();

					if (Util.isNotBlank(values)) {

						if (key == DTC.CENTRAL_INVERTER || key == DTC.STRING_INVERTER || key == DTC.ELECTRIC_METER
								|| key == DTC.PCS || key == DTC.BMS || key == DTC.COMBINER_BOX) {
							for (Integer integer : values) {
								Integer status = deviceOfStatus.get(String.valueOf(integer));
								if (status == null) {
									status = 3;
								}

								// 得到正常运行的汇流箱数量
								if (key == DTC.COMBINER_BOX && status == 0) {
									normalCombinerBoxCount++;
								}

								statusList.add(status);
							}
						}
					}

				}
				if (statusList != null && !statusList.isEmpty()) {

					// 断连设备数量
					int breakLen = 0;
					// 待机设备数量
					int standby = 0;
					// 总设备数量
					int deviceLen = statusList.size();

					for (int i = 0; i < statusList.size(); i++) {
						int status = statusList.get(i);
						if (status == 3) {
							breakLen++;
						}
					}

					// 如果所有设备都断连则判定断连
					if (breakLen == deviceLen) {
						result = 3;
						return personalized(pId, result);
					}

					// 如果是部分设备断连则判断为异常
					// if (breakLen > 0) {
					// result = 4;
					// return personalized(pId, result);
					// }

					// 只要有设备故障则判定电站故障
					for (int i = 0; i < deviceLen; i++) {
						int status = statusList.get(i);

						if (status == 2) {
							result = 2;
							return personalized(pId, result);
						}

					}

					// 只要有设备断连不是完全断连则判定异常
					// if (breakLen > 0) {
					// result = 4;
					// return personalized(pId, result);
					// }

					// 只要有设备异常则判定电站异常
					// for (int i = 0; i < deviceLen; i++) {
					// int status = statusList.get(i);
					// if (status == 4) {
					// result = 4;
					// return personalized(pId, result);
					// }
					// }

					// 所有设备待机则电站待机
					for (int i = 0; i < deviceLen; i++) {
						int status = statusList.get(i);
						if (status == 1) {
							++standby;
						}
					}
					if (standby == (deviceLen - breakLen - normalCombinerBoxCount)) {
						result = 1;
						return personalized(pId, result);
					}

					// 如果不是所有设备待机且没故障设备为异常
					// if (standby > 0) {
					// result = 4;
					// return personalized(pId, result);
					// }

				} else {
					result = 3;
				}
			} else {
				result = 3;
			}
		} else {
			result = 3;
		}
		return personalized(pId, result);
	}

	/**
	 * 电站状态个性化定制
	 *
	 * @param pId
	 * @param result
	 * @return
	 * @author ybj
	 * @Description
	 * @Date 2018年8月10日
	 */
	private static int personalized(String pId, int result) {
		/**
		 * 先将陕西省的三个电站默认设置成运行状态(除待机状态外)，以后再将下面的if判断删除
		 */
//		if (pId.equals("17") || pId.equals("19") || pId.equals("20")) {
//			if (result != 1) {
//				return 0;
//			}
//		}
		return result;
	}

	public static String getPlantStatusString(int type) {
		// 电站 状态码:0:正常发电中，1：待机，2：故障，3：中断 4 异常
		// String[] status = new String[] { "运行", "待机", "故障", "中断", "异常" };
		String[] status = new String[] { "运行", "待机", "故障", "中断" };
		return status[type];
	}

	public static Map<String, Object> loadParamForCurrentPowers(String riseTime, String setTime,
			List<DataCentralInverter> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			DataCentralInverter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getActivePower()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
//				for (int k = j; k >= 0; k--) {
//					String compareVal = valueArr[k];
//					if (null != compareVal) {
//						valueArr[j] = compareVal;
//						break;
//					}
//				}
				valueArr[j] = "0";
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower2(String riseTime, String setTime,
			List<DataCentralInverter> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			DataCentralInverter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getDailyEnergy()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}

			// int timeSize=nowLocation+1;
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "度";
		if (maxValue > 100000000) {
			unit = "亿度";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 100000000).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue()) + "";
			}
			maxValue = new BigDecimal(maxValue / 100000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 10000) {
			unit = "万度";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "发电量");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower3(String riseTime, String setTime,
			List<DataElectricMeter> nowData, int nowLocation) throws ServiceException {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			DataElectricMeter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				throw new ServiceException();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getEmActivePower()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			// int length=timeList.size();
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "有功功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower4(String riseTime, String setTime,
			List<DataElectricMeter> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataElectricMeter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getEmReactivePower()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}

		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "无功功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower5(String riseTime, String setTime, List<DataPcs> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataPcs obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getuAcA()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "伏";
		if (maxValue > 1000) {
			unit = "千伏";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "交流电压");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower6(String riseTime, String setTime, List<DataPcs> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataPcs obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getuBat()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "伏";
		if (maxValue > 1000) {
			unit = "千伏";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "直流电压");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower7(String riseTime, String setTime, List<DataPcs> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataPcs obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getActivePower()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "交流功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower8(String riseTime, String setTime, List<DataPcs> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataPcs obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getPowerBat()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "直流功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower9(String riseTime, String setTime, List<DataBms> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBms obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getCurrBus()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "安";
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "电流");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower10(String riseTime, String setTime, List<DataBms> nowData,
			int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBms obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getBmsLeftCapacity()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "度";
		if (maxValue > 100000000) {
			unit = "亿度";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 100000000).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue()) + "";
			}
			maxValue = new BigDecimal(maxValue / 100000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 10000) {
			unit = "万度";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 10000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "可放电量");
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower11(String riseTime, String setTime,
			List<DataBoxChange> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBoxChange obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getActivePower()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "功率");
		resultMap.put("yIndex", 0);
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower12(String riseTime, String setTime,
			List<DataBoxChange> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBoxChange obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getuLineAb()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "伏";
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "AB线电压");
		resultMap.put("yIndex", 1);
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower13(String riseTime, String setTime,
			List<DataBoxChange> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBoxChange obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getuLineBc()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "伏";
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "BC线电压");
		resultMap.put("yIndex", 1);
		return resultMap;
	}

	public static Map<String, Object> loadParamForCurrentPower14(String riseTime, String setTime,
			List<DataBoxChange> nowData, int nowLocation) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int length = nowLocation + 1;
		int size = nowData.size();
		for (int i = 0; i < size; i++) {
			DataBoxChange obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = StringUtil.checkBlank(String.valueOf(obj.getuLineCa()));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					DecimalFormat decimalFormat = new DecimalFormat("###0.######");
					valueArr[j] = decimalFormat.format(tempPower);
					break;
				}
			}
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
				for (int k = j; k >= 0; k--) {
					String compareVal = valueArr[k];
					if (null != compareVal) {
						valueArr[j] = compareVal;
						break;
					}
				}
			}
		}
		String unit = "伏";
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", "CA线电压");
		resultMap.put("yIndex", 1);
		return resultMap;
	}

	/**
	 * @author ybj
	 * @date 2018年11月7日 下午5:45:45
	 * @Description -_-
	 * @param riseTime
	 * @param setTime
	 * @param nowData
	 * @param nowLocation
	 * @return
	 */
	public static List<Map<String, Object>> loadParamForCurrentPower15(List<String> timeList,
			List<Map<String, Object>> dataList, int nowLocation) {
		double[] radiations = new double[nowLocation];
		double[] temps = new double[nowLocation];
		for (Map<String, Object> map : dataList) {
			int indexOf = timeList.indexOf(String.valueOf(map.get("data_time")));
			if (indexOf != -1 && indexOf < nowLocation) {
				radiations[indexOf] = Util.roundDouble(Util.getDouble(map.get("radiation_total_one")));
				temps[indexOf] = Util.roundDouble(Util.getDouble(map.get("temp_ambient")));
			}
		}

		Map<String, Object> radiasMap = new HashMap<>(4);
		radiasMap.put("name", "总辐射");
		radiasMap.put("unit", "W/m²");
		radiasMap.put("value", radiations);

		Map<String, Object> tempsMap = new HashMap<>(4);
		tempsMap.put("name", "温度");
		tempsMap.put("unit", "℃");
		tempsMap.put("value", temps);

		List<Map<String, Object>> resultList = new ArrayList<>(3);
		resultList.add(radiasMap);
		resultList.add(tempsMap);

		return resultList;
	}

	public static Map<String, Object> loadParamForPower(String riseTime, String setTime,
			List<DataCentralInverter> nowData) throws ServiceException {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = "0";
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		for (int i = 0; i < nowData.size(); i++) {
			DataCentralInverter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				throw new ServiceException();
			}
			double tempPower = Double
					.parseDouble("null".equals(obj.getActivePower() + "") ? "0" : (obj.getActivePower() + ""));
			tempPower = Double.parseDouble(Util.getDecimalFomat2().format(tempPower));
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < timeList.size(); j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String.valueOf(tempPower);
					break;
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < valueArr.length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < valueArr.length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("maxValue", maxValue);
		resultMap.put("name", "交流功率");
		return resultMap;
	}

	public static Map<String, Object> loadParamForPower2(String riseTime, String setTime,
			List<DataCentralInverter> nowData) throws ServiceException {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = "0";
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		for (int i = 0; i < nowData.size(); i++) {
			DataCentralInverter obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(obj.getDataTime())).getTime();
			} catch (ParseException e) {
				throw new ServiceException();
			}
			double tempPower = Double
					.parseDouble("null".equals(obj.getMpptTotalEnergy() + "") ? "0" : (obj.getMpptTotalEnergy() + ""));
			tempPower = Double.parseDouble(Util.getDecimalFomat2().format(tempPower));
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < timeList.size(); j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime().getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String.valueOf(tempPower);
					break;
				}
			}
		}
		String unit = "千瓦";
		if (maxValue > 1000000) {
			unit = "吉瓦";
			for (int i = 0; i < valueArr.length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if (maxValue > 5000) {
			unit = "兆瓦";
			for (int i = 0; i < valueArr.length; i++) {
				double tempValue = Double.parseDouble(valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
						+ "";
			}
			maxValue = new BigDecimal(maxValue / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("maxValue", maxValue);
		resultMap.put("name", "直流功率");
		return resultMap;
	}

	// public static String uploadFile(HttpServletRequest request,File file,String
	// title) throws IllegalStateException, IOException{
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		String pathDir = File.separator + "files";
//		pathDir += File.separator+title;
//		MultipartFile multipartFile = multipartRequest.getFile("upfile");
//		String logoRealPathDir = request.getSession().getServletContext().getRealPath(pathDir);
//		File logoSaveFile = new File(logoRealPathDir);
//		if (!logoSaveFile.exists()) {
//			logoSaveFile.mkdirs();
//		}
//		/** 获取文件的后缀 **/
//		String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
//		/** 拼成完整的文件保存路径加文件 **/
//		String name = +System.currentTimeMillis() + suffix;
//		String fileName = logoRealPathDir + File.separator + name;
//		file = new File(fileName);
//		String data = file.getPath();
//		multipartFile.transferTo(file);
//		String path = file.getPath();
//		path=path.substring(path.indexOf("files"));
//		return path;
//	}
	public static Map<String, Object> loadParamForTimeList(String startTime, String endTime, List<Object[]> nowData,
			String name, String unit, String deviceName) {
		List<Long> timeList = ServiceUtil.getLongTimeSegmentAnyDay(startTime, endTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = "0";
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		try {
			for (int i = 0; i < nowData.size(); i++) {
				Object[] obj = nowData.get(i);
				long key = sdf.parse(sdf.format(new BigInteger(obj[0] + "").longValue())).getTime();
				String value = Util.isNotBlank(String.valueOf(obj[1])) ? String.valueOf(obj[1]) : "0";
				double tempPower = Double.parseDouble(value);
				if (maxValue < tempPower) {
					maxValue = tempPower;
				}
				int length = timeList.size();
				for (int j = 0; j < length; j++) {
					// 取当前点位的范围 前2分30秒和后2分30秒的时间
					// 取当前点位的范围 前2分30秒和后2分30秒的时间
					calendar.setTime(new Date(timeList.get(j)));
					calendar.add(Calendar.MINUTE, -2);
					calendar.add(Calendar.SECOND, -30);
					long minTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
					calendar.setTime(new Date(timeList.get(j)));
					calendar.add(Calendar.MINUTE, 2);
					calendar.add(Calendar.SECOND, 30);
					long maxTime = 0;
					maxTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
					if (key >= minTime && key <= maxTime) {
						valueArr[j] = value;
						break;
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultMap.put("value", valueArr);
		resultMap.put("unit", unit);
		resultMap.put("name", name);
		resultMap.put("deviceName", deviceName);
		resultMap.put("maxValue", maxValue);
		return resultMap;
	}

	public static String getDeviceStatus(int deviceType, int status) {
		String reString = null;
		switch (deviceType) {
		case 1:
			switch (status) {
			case 0:
				reString = "发电中";
				break;
			case 1:
				reString = "待机";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 2:
			switch (status) {
			case 0:
				reString = "发电中";
				break;
			case 1:
				reString = "待机";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 3:
			switch (status) {
			case 0:
				reString = "通讯中";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 5:
			switch (status) {
			case 0:
				reString = "通讯中";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 9:
			switch (status) {
			case 0:
				reString = "正常";
				break;
			case 1:
				reString = "待机";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 10:
			switch (status) {
			case 0:
				reString = "正常";
				break;
			case 1:
				reString = "待机";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 11:
			switch (status) {
			case 0:
				reString = "正常";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 12:
			switch (status) {
			case 0:
				reString = "正常";
				break;
			case 2:
				reString = "故障";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			case 4:
				reString = "异常";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		case 13:
			switch (status) {
			case 0:
				reString = "正常";
				break;
			case 3:
				reString = " 通讯中断";
				break;
			default:
				reString = " 通讯中断";
				break;
			}
			break;
		default:
			break;
		}
		return reString;
	}

	/**
	 * @param num        返回记录数
	 * @param logMaxTime 日志最大存活时间
	 * @param logFolder  日志文件夹
	 * @Title: deleteLogFiles
	 * @Description: 日志清理
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年2月24日上午11:27:32
	 */
	public static int deleteLogFiles(int num, int logMaxTime, File logFolder) {
		File[] list = logFolder.listFiles();
		long nowDay = new Date().getTime();
		int i = 0;
		if (!Util.isNotBlank(list)) {
			return i;
		}
		for (File file : list) {
			if (file.isDirectory()) {
				i += deleteLogFiles(i, logMaxTime, file);
			} else {
				long fileTime = file.lastModified();
				// 计算相差天数
				int days = (int) ((nowDay - fileTime) / 86400000);
				// 清理超过时间的日志文件
				if (days >= logMaxTime) {
					file.delete();
					i++;
				}
			}
		}
		return i;
	}

	public static long getTimeByStr(String time) {
		String temp[] = time.split(":");
		long l = Long.valueOf(Integer.parseInt(temp[0]) * 60 * 60 * 1000 + Integer.parseInt(temp[1]) * 60 * 1000
				+ Integer.parseInt(temp[2]) * 1000);
		return l;
	}

	/**
	 * @param obj
	 * @Title: ConvertObjToMap
	 * @Description: 实体对象转Map
	 * @return: Map
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月30日上午10:38:35
	 */
	public static Map ConvertObjToMap(Object obj) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Field f = obj.getClass().getDeclaredField(fields[i].getName());
				// 获取变量时必须设置访问权限
				f.setAccessible(true);
				Object o = f.get(obj);
				reMap.put(fields[i].getName(), o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reMap;
	}

	/**
	 * @param riseTime    起始时间 HH:mm
	 * @param setTime     结束时间 HH:mm
	 * @param nowData     待装载的数据集合
	 * @param nowLocation 当前点位
	 * @param listName    曲线名字
	 * @param units       单位换算的集合由小到大 例如 {"千瓦","兆瓦","吉瓦"}
	 * @param separator   单位换算进制
	 * @Title: loadParamForCurrentList
	 * @Description: 此方法只适用于装载一天的数据不适用跨天
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年4月3日上午10:32:11
	 */
	public static Map<String, Object> loadParamForCurrentList(String riseTime, String setTime, List<Object[]> nowData,
			int nowLocation, String listName, String[] units, int separator) {
		List<Long> timeList = ServiceUtil.getLongTimeSegment(riseTime, setTime);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] valueArr = new String[timeList.size()];
		for (int i = 0; i < valueArr.length; i++) {
			valueArr[i] = null;
		}
		Map<String, Object> resultMap = new HashMap<>(3);
		double maxValue = 0;
		int size = nowData.size();
		int length = nowLocation + 1;
		for (int i = 0; i < size; i++) {
			Object[] obj = nowData.get(i);
			long key = 0;
			try {
				key = sdf.parse(sdf.format(new BigInteger(obj[0] + "").longValue())).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Object value = obj[0];
			String temp = StringUtil.checkBlank(String.valueOf(obj[1]));
			double tempPower = Double.parseDouble("".equals(temp) ? "0" : temp);
			if (maxValue < tempPower) {
				maxValue = tempPower;
			}
			for (int j = 0; j < length; j++) {
				// 取当前点位的范围 前2分30秒和后2分30秒的时间
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, -2);
				calendar.add(Calendar.SECOND, -30);
				long minTime = 0;
				try {
					minTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
				}
				calendar.setTime(new Date(timeList.get(j)));
				calendar.add(Calendar.MINUTE, 2);
				calendar.add(Calendar.SECOND, 30);
				long maxTime = 0;
				try {
					maxTime = sdf.parse(sdf.format(calendar.getTime())).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
				}
				if (key >= minTime && key <= maxTime) {
					valueArr[j] = String
							.valueOf(Util.getDecimalFomat2().format(Double.parseDouble(String.valueOf(obj[1]))));
					break;
				}
			}
		}
		String unit = units[0];
		if (maxValue > separator * separator) {
			unit = units[2];
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(StringUtil.checkBlank(valueArr[i]) == "" ? "0" : valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / (separator * separator)).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue()) + "";
				maxValue = new BigDecimal(maxValue / (separator * separator)).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
		} else if (maxValue > separator) {
			unit = units[1];
			for (int i = 0; i < length; i++) {
				double tempValue = Double.parseDouble(StringUtil.checkBlank(valueArr[i]) == "" ? "0" : valueArr[i]);
				valueArr[i] = (new BigDecimal(tempValue / separator).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue()) + "";
			}
			maxValue = new BigDecimal(maxValue / separator).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		if ("".equals(StringUtil.checkBlank(valueArr[0]))) {
			valueArr[0] = "0";
		}
		for (int j = 0; j < length; j++) {
			String val = valueArr[j];
			if (val == null) {
//				for (int k = j; k >= 0; k--) {
//					String compareVal = valueArr[k];
//					if (null != compareVal) {
//						valueArr[j] = compareVal;
//						break;
//					}
//				}
				valueArr[j] = "0";
			}
		}

		// ===ybj
		// 抛物线
		String[] yData2 = ServiceUtil.getParabola(valueArr, maxValue);
		resultMap.put("yData2", yData2);
		resultMap.put("curData2", yData2[nowLocation]);
		// ===

		resultMap.put("xData", ServiceUtil.getStringTimeSegment(riseTime, setTime));
		resultMap.put("yData1", valueArr);
		resultMap.put("maxValue", maxValue);
		resultMap.put("unit", unit);
		resultMap.put("name", listName);
		return resultMap;
	}

}
