package com.synpower.serviceImpl;

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
import com.synpower.service.DeviceService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.UserService;
import com.synpower.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlantInfoServiceImpl implements PlantInfoService {
	private Logger logger = Logger.getLogger(AlarmServiceImpl.class);
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private SysOrgMapper soMapper;
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysPersonalSetMapper personalSetMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private SysAddressMapper addressMapper;
	@Autowired
	private SysUserPlantMapper userPlantMapper;
	@Autowired
	private PlantInfoServiceImpl plantInfoServiceImpl;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private PlantTypeAMapper plantTypeAMapper;
	@Autowired
	private PlantTypeBMapper plantTypeBMapper;
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private SysWeatherMapper weatherMapper;
	@Autowired
	private SystemCache systemCache;
	@Autowired
	private SysPowerPriceMapper sysPowerPriceMapper;
	@Autowired
	private SysPowerPriceDetailMapper sysPowerPriceDetailMapper;
	@Autowired
	private CollJsonDevSetMapper collJsonDevSetMapper;
	@Autowired
	private DeviceTypeMapper typeMapper;

	/**
	 * @Title: listPlantInfo
	 * @Description: 电站设置模块中电站列表显示
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws ServiceException
	 * @throws SessionTimeoutException: MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月2日下午5:41:51
	 */
	@Override
	public MessageBean listPlantInfo(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
		List<SysOrg> orgList = soMapper.getAllOrg();
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("limit") + "");
		// 获取显示第几页的数据
		Integer alarm_page_num = Integer.parseInt(map.get("offset") + "");
		// 获取排序方式
		String order = (String) map.get("order");
		// 获取排序列名称
		String sort = (String) map.get("sort");
		// 获取电站名称
		String plantName = (String) map.get("plantName");
		// 获取电站地址
		String plantAddr = (String) map.get("plantAddr");
		// 获取电站的组织id
		String orgId = (String) map.get("orgId");
		List<String> reList = new ArrayList<>();
		reList.add(((SysOrg) u.getUserOrg()).getId() + "");
		reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("plantName", plantName);
		countMap.put("plantAddr", plantAddr);
		countMap.put("orgId", orgId);
		countMap.put("countList", reList);
		int count = plantInfoMapper.countPlantInfo(countMap);
		int min = (alarm_page_num - 1) * each_page_num;
		if (min < 0) {
			min = 0;
		}
		int max = each_page_num;
		if (max > count) {
			max = count;
		}
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("min", min);
		parameterMap.put("max", max);
		parameterMap.put("sort", sort);
		parameterMap.put("order", order);
		parameterMap.put("reList", reList);
		parameterMap.put("plantName", plantName);
		parameterMap.put("plantAddr", plantAddr);
		parameterMap.put("orgId", orgId);
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<PlantInfo> list = plantInfoMapper.listPlantInfo(parameterMap);
		for (PlantInfo plantInfo : list) {
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("id", plantInfo.getId() + "");
			resultMap.put("plantName", plantInfo.getPlantName());
			resultMap.put("plantType",
					plantInfo.getPlantTypeAInfo().getTypeName() + "," + plantInfo.getPlantTypeBInfo().getTypeName());
			resultMap.put("capacity", plantInfo.getCapacity() + plantInfo.getUnit());
			resultMap.put("gridConnectedDate",
					Util.longToDateString(plantInfo.getGridConnectedDate(), "yyyy-MM-dd hh:mm:ss "));
			resultList.add(resultMap);
		}
		SysOrg so = soMapper.getOrgName(((SysOrg) u.getUserOrg()).getId() + "");
		Map<String, String> orgMap = new HashMap<String, String>();
		orgMap.put("name", so.getOrgName());
		orgMap.put("value", so.getId() + "");
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("total", count);
		reMap.put("rows", resultList);
		reMap.put("orgName", orgMap);
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(reMap);
		return msg;
	}

	/**
	 * @Title: getPlantInfo
	 * @Description: 电站设置模块中的电站基本信息
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月2日下午5:43:41
	 */
	@Override
	public MessageBean getPlantInfo(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String plantId = map.get("plantId") + "";
		// 根据电站id查询出电站的基本信息
		PlantInfo plant = plantInfoMapper.getPlantInfo(plantId);
		// 电站照片
		List<String> photoList = new ArrayList<String>();
		String plantPhoto = plant.getPlantPhoto();
		if (Util.isNotBlank(plantPhoto)) {
			String[] photos = plantPhoto.split(";");
			photoList = Arrays.asList(photos);
		}
		Map<String, Object> plantMap = new HashMap<String, Object>();
		// 组装前台数据
		plantMap.put("plantId", plant.getId() + "");
		plantMap.put("plantName", plant.getPlantName());
		plantMap.put("gridConnectedDate", Util.longToDateString(plant.getGridConnectedDate(), "yyyy-MM-dd"));
		plantMap.put("capacity", plant.getCapacity() + "");
		plantMap.put("capUnit", plant.getUnit() + "");
		Map<String, Object> typeAMap = new HashMap<String, Object>(1);
		typeAMap.put("name", plant.getPlantTypeAInfo().getTypeName());
		typeAMap.put("value", plant.getPlantTypeAInfo().getId());
		plantMap.put("plantTypeA", typeAMap);
		Map<String, Object> typeBMap = new HashMap<String, Object>(1);
		typeBMap.put("name", plant.getPlantTypeBInfo().getTypeName());
		typeBMap.put("value", plant.getPlantTypeBInfo().getId());
		plantMap.put("plantTypeB", typeBMap);
		// 查询plantTypeA的下拉菜单
		List<Object> allTypeA = new ArrayList<Object>(3);
		List<PlantTypeA> allPlantType = plantTypeAMapper.getAllPlantType();
		for (PlantTypeA plantTypeA : allPlantType) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", plantTypeA.getTypeName());
			tempMap.put("value", plantTypeA.getId());
			allTypeA.add(tempMap);
		}
		// 查询plantTypeB的下拉菜单
		List<Object> allTypeB = new ArrayList<Object>(3);
		List<PlantTypeB> allPlantTypeB = plantTypeBMapper.getAllPlantType();
		for (PlantTypeB plantTypeB : allPlantTypeB) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", plantTypeB.getTypeName());
			tempMap.put("value", plantTypeB.getId());
			allTypeB.add(tempMap);
		}
		plantMap.put("allTypeA", allTypeA);
		plantMap.put("allTypeB", allTypeB);
		plantMap.put("plantAddr", plant.getPlantAddr());
		plantMap.put("loaction", plant.getLoaction());
		plantMap.put("plantFullAddress", plant.getPlantFullAddress());
		plantMap.put("plantArea", plant.getPlantArea());
		plantMap.put("status", plant.getPlantStatus());
		plantMap.put("plantIntroduction", plant.getPlantIntroduction());
		SysOrg org = orgMapper.getOrgById(String.valueOf(plant.getOrgId()));
		plantMap.put("orgName", org.getOrgName());
		plantMap.put("orgId", org.getId());
		List<Object> resultList = new ArrayList<Object>();
		List<SysUser> userList = userMapper.getUserByUid(Integer.valueOf(plantId));
		for (SysUser sysUser : userList) {
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("userId", sysUser.getId() + "");
			userMap.put("loginId", sysUser.getLoginId() + "");
			userMap.put("userName", sysUser.getUserName());
			userMap.put("userTel", sysUser.getUserTel());
			userMap.put("password", sysUser.getPassword());
			userMap.put("status", sysUser.getUserStatus());
			resultList.add(userMap);
		}
		plantMap.put("user", resultList);
		plantMap.put("photo", photoList);
//		String powerPrice = priceMapper.getPriceByPlant(plantId);
//		if (powerPrice==null) {
//			plantMap.put("plantPrice", "--");
//		} else {
//			plantMap.put("plantPrice", powerPrice);
//		}
		String priceId = plant.getPriceId() + "";
		if (Util.isNotBlank(priceId)) {
			SysPowerPrice sysPowerPrice = sysPowerPriceMapper.getPlantPrice(priceId);
			if (sysPowerPrice != null) {
				plantMap.put("plantPrice", sysPowerPrice.getPriceName());
				plantMap.put("priceId", sysPowerPrice.getId());
			} else {
				plantMap.put("plantPrice", "--");
			}
		} else {
			plantMap.put("plantPrice", "--");
		}
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("plantInfo", plantMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}

	/**
	 * @Title: updatePlantInfo
	 * @Description: 电站设置模块中的修改电站信息
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       MessageBean
	 * @throws ParseException
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月2日下午5:44:37
	 */
	@Transactional
	@Override
	public MessageBean updatePlantInfo(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		PlantInfo plant = new PlantInfo();
		plant.setId(Integer.parseInt(map.get("id") + ""));
		plant.setPlantName(map.get("plantName") + "");
		String date = map.get("gridConnectedDate") + "";
		// 将日期格式装换成毫秒数
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		plant.setGridConnectedDate(calendar.getTimeInMillis());
		plant.setCapacity(Double.parseDouble(map.get("capacity") + ""));
		plant.setPlantTypeA(Integer.parseInt(map.get("plantTypeA") + ""));
		plant.setPlantTypeB(Integer.parseInt(map.get("plantTypeB") + ""));
		plant.setPlantAddr(map.get("plantAddr") + "");
		plant.setLoaction(map.get("loaction") + "");
		plant.setPlantFullAddress(map.get("plantFullAddress") + "");
		plant.setPlantArea(Integer.parseInt(map.get("plantArea") + ""));
		plant.setPlantIntroduction(map.get("plantIntroduction") + "");
		plant.setLastModifyTime(System.currentTimeMillis());
		plant.setLastModifyUser(Integer.parseInt(u.getId()));
		plant.setPriceId(Integer.valueOf(map.get("plantPrice") + ""));
		plant.setUnit(map.get("capUnit") + "");
		plant.setCapacityBase(plant.calcCapacityBase());
		int result = plantInfoMapper.updatePlantInfo(plant);
//		String pid = map.get("id")+"";
//		String powerPrice = map.get("plantPrice")+"";
//		Map<String,Object> paraMap = new HashMap<>();
//		paraMap.put("pid", pid);
//		paraMap.put("powerPrice", powerPrice);
//		int pResult = priceMapper.updatePrice(paraMap);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("administrative");
		if (list.size() != 0) {
			// 将list按照level字段进行升序排列
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map arg0, Map arg1) {
					if (Integer.parseInt(arg0.get("level") + "") > Integer.parseInt(arg1.get("level") + "")) {
						return 1;
					}
					if (Integer.parseInt(arg0.get("level") + "") == Integer.parseInt(arg1.get("level") + "")) {
						return 0;
					}
					return -1;
				}
			});
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> paramMap = new HashMap<String, Object>(5);
				Map<String, Object> map2 = list.get(i);
				String country = map2.get("value") + "";
				String[] countryLocation = country.split(",");
				paramMap.put("areaName", map2.get("name") + "");
				paramMap.put("locationX", countryLocation[0]);
				paramMap.put("locationY", countryLocation[1]);
				paramMap.put("id", map.get("id"));
				paramMap.put("level", map2.get("level") + "");
				int addrResult = plantInfoMapper.updatePlantAddr(paramMap);
				if (addrResult == 0) {
					logger.error(StringUtil.appendStr("修改电站经纬度失败 操作者", logUserName));
					throw new ServiceException("修改电站经纬度失败");
				}
				logger.info(StringUtil.appendStr("修改电站经纬度记录 电站id:", plant.getId(), "操作者", logUserName));
			}
			logger.info("重新刷新缓存");
			systemCache.reInit();
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.UPDATE_SUCCUESS);
		} else {
			logger.info("重新刷新缓存");
			systemCache.reInit();
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.UPDATE_SUCCUESS);
		}
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public MessageBean insertPlantInfo(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, IOException, ParseException {
		MessageBean msg = new MessageBean();
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		PlantInfo plant = new PlantInfo();
		// 将前台传过来的数据装载成plant实体类
		plant.setPlantName(map.get("plantName") + "");
		String date = map.get("gridConnectedDate") + "";
		// 将日期格式装换成毫秒数
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		plant.setGridConnectedDate(calendar.getTimeInMillis());
		plant.setCapacity(Double.parseDouble(map.get("capacity") + ""));
		plant.setPlantTypeA(Integer.parseInt(map.get("plantTypeA") + ""));
		plant.setPlantTypeB(Integer.parseInt(map.get("plantTypeB") + ""));
		plant.setPlantAddr(map.get("plantAddr") + "");
		String location = map.get("loaction") + "";
		// 验证location的唯一性，若有则加0.0002
		List<PlantInfo> plantInfoList = plantInfoMapper.getPlantLocation(location);
		if (Util.isNotBlank(plantInfoList)) {
			// 经度
			double longitude = 0;
			// 纬度
			double latitude = 0;
			String[] locations = location.split(",");
			longitude = Util.addFloat(Double.parseDouble(locations[0]), 0.0002, 0);
			latitude = Util.addFloat(Double.parseDouble(locations[1]), 0.0002, 0);
			location = longitude + "," + latitude;
		}
		plant.setLoaction(location);
		plant.setPlantFullAddress(map.get("plantFullAddress") + "");
		plant.setPlantArea(Integer.parseInt(map.get("plantArea") + ""));
		if (u.getUserType().equals("1")) {
			plant.setContacts(u.getId());
		}
		plant.setPlantValid("0");
		plant.setPlantStatus("1");
		plant.setPlantIntroduction(map.get("plantIntroduction") + "");
		plant.setLastModifyTime(System.currentTimeMillis());
		plant.setLastModifyUser(Integer.parseInt(u.getId()));
		plant.setUnit(map.get("capUnit") + "");
		plant.setOrgId(Integer.parseInt(map.get("orgId") + ""));
		plant.setCreateTime(System.currentTimeMillis());
		plant.setPriceId(Integer.valueOf(map.get("plantPrice") + ""));
		plant.setCapacityBase(plant.calcCapacityBase());
		int result = plantInfoMapper.insertPlantInfo(plant);
		if (result > 0) {
			Integer plantId = plant.getId();
			logger.info(StringUtil.appendStr("插入电站记录成功 :", plant, "操作者", logUserName));
			Map<String, Integer> reusltMap = new HashMap<>();
			reusltMap.put("plantId", plantId);
//			String value = map.get("plantPrice")+"";
//			if(!Util.isNotBlank(value)){
//				// 从配置文件中获取电站价格的默认值
//				value = PropertiesUtil.getValue("config.properties", "POWERPRICE");
//			}
//			Map<String, Object> priceMap = new HashMap<String,Object>();
//			priceMap.put("value", value);
//			priceMap.put("plantId", plantId);
//			priceMap.put("valid", "0");
//			priceMap.put("status", "1");

			// 新增电站价格
//			int priceResult = priceMapper.insertPrice(priceMap);
//			if(priceResult > 0){
//				logger.info(StringUtil.appendStr("新增电站价格成功!电站id:",plantId,"价格",value,"操作者",logUserName));
			// 接收前台传来的集合格式
			List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("administrative");
			// 将list按照level字段进行升序排列
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				public int compare(Map arg0, Map arg1) {
					if (Integer.parseInt(arg0.get("level") + "") > Integer.parseInt(arg1.get("level") + "")) {
						return 1;
					}
					if (Integer.parseInt(arg0.get("level") + "") == Integer.parseInt(arg1.get("level") + "")) {
						return 0;
					}
					return -1;
				}
			});
			SysAddress addressFather = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map2 = list.get(i);
				SysAddress address = new SysAddress();
				String country = map2.get("value") + "";
				String[] countryLocation = country.split(",");
				address.setAreaName(map2.get("name") + "");
				address.setLocationX(countryLocation[0]);
				address.setLocationY(countryLocation[1]);
				address.setPlantId(plant.getId());
				if (i == 0) {
					address.setFatherId(0);
				} else {
					address.setFatherId(addressFather.getId());
				}
				int level = Integer.parseInt(map2.get("level") + "");
				address.setLevel(level);
				int re = addressMapper.insertPlantAddr(address);
				if (re == 0) {
					logger.error(StringUtil.appendStr("新增电站经纬度失败 操作者", logUserName));
					throw new ServiceException("新增电站经纬度失败");
				}
				logger.info(
						StringUtil.appendStr("新增电站经纬度记录 电站id:", plant.getId(), "地区名称", address, "操作者", logUserName));
				addressFather = address;
			}
			// 刷新缓存
			systemCache.reInit();
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(reusltMap);
			msg.setMsg(Msg.ADD_SUCCUESS);
//			}else{
//				logger.error(StringUtil.appendStr("插入电价记录失败  电价:",value,"操作者",logUserName));
//				throw new ServiceException(Msg.CREATE_FAILED);
//			}
		} else {
			logger.error(StringUtil.appendStr("插入电站记录失败  :", plant, "操作者", logUserName));
			throw new ServiceException(Msg.CREATE_FAILED);
		}
		return msg;
	}

	/**
	 * @Title: deletePlantUser
	 * @Description: 电站设置模块中的删除业主信息
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月2日下午5:45:38
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public MessageBean deletePlantUser(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		// 将前端请求数据装载为map
		Map<String, Object> map = Util.parseURL(str);
		// 根据tokenId获取到User对象
		User u = session.getAttribute(map.get("tokenId") + "");
		// 装载删除所需要的参数
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("lastModifyTime", System.currentTimeMillis());
		parameterMap.put("lastModifyUser", u.getId());
		parameterMap.put("id", map.get("id"));
		int result = userMapper.deletePlantUser(parameterMap);
		// 组装数据
		MessageBean msg = new MessageBean();
		if (result != 0) {
			logger.error(u.getId() + "删除用户信息成功");
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.DELETE_SUCCUESS);
		} else {
			throw new ServiceException(Msg.DELETE_FAILED);
		}
		return msg;
	}

	/**
	 * @Title: updatePlantUser
	 * @Description: 电站设置模块中的修改业主信息
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月2日下午5:46:38
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public MessageBean updatePlantUser(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		// 将前台数据装载成map
		Map<String, Object> map = Util.parseURL(str);
		// 根据tokenId获取到User对象
		User u = session.getAttribute(map.get("tokenId") + "");
		// 装载修改业务所需要的参数
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("lastModifyTime", System.currentTimeMillis());
		parameterMap.put("lastModifyUser", u.getId());
		parameterMap.put("id", map.get("userId"));
		parameterMap.put("plantId", map.get("plantId"));
		parameterMap.put("userName", map.get("userName"));
		parameterMap.put("userTel", map.get("userTel"));
		parameterMap.put("password", map.get("password"));
		parameterMap.put("email", map.get("email"));
		parameterMap.put("loginId", map.get("loginId"));
		parameterMap.put("status", map.get("status"));
		int result = userMapper.updatePlantUser(parameterMap);
		// int result2 = userMapper.updateUser4Plant(parameterMap);
		// 组装数据
		MessageBean msg = new MessageBean();
		if (result != 0) {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.UPDATE_SUCCUESS);
		} else {
			throw new ServiceException(Msg.UPDATE_FAILED);
		}
		return msg;
	}

	/**
	 * @Title: getSubsetOrg
	 * @Description: 获取当前用户组织下面的组织名称
	 * @param str
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       MessageBean
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年11月3日下午5:39:20
	 */
	@Override
	public MessageBean getSubsetOrg(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 得到所有组织列表
		List<SysOrg> orgList = soMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		reList = ServiceUtil.getTree(orgList, ((SysOrg) u.getUserOrg()).getId() + "", reList);
		MessageBean msg = new MessageBean();
		if (reList != null && !reList.isEmpty()) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("reList", reList);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			List<SysOrg> list = soMapper.getSubsetOrg(parameterMap);
			for (SysOrg sysOrg : list) {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("name", sysOrg.getOrgName());
				resultMap.put("value", sysOrg.getId() + "");
				resultList.add(resultMap);
			}
			if (list == null && list.isEmpty()) {
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(null);
				msg.setMsg(Msg.NO_SUBSET);
			} else {
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(resultList);
			}
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(null);
			msg.setMsg(Msg.NO_SUBSET);
		}
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public MessageBean uploadPlantPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		String imgUrls = request.getParameter("imgUrl");
		User u = session.getAttribute(request.getParameter("tokenId"));
		// 当前登录人的id和名称
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		String plantId = request.getParameter("plantId");
		if (Util.isNotBlank(imgUrls)) {
			PlantInfo plantInfo = plantInfoMapper.getPlantById(plantId);
			// 删除照片路径
			String[] imgArr = StringUtils.split(StringUtil.checkBlank(plantInfo.getPlantPhoto()), ";");
			String[] imgUrl = imgUrls.split(";");
			if (imgArr.length == 0) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			boolean b = true;
			for (int i = 0; i < imgUrl.length; i++) {
				for (int j = 0; j < imgArr.length; j++) {
					if (imgArr[j] != null) {
						if (imgArr[j].equals(imgUrl[i])) {
							imgArr[j] = null;
							b = false;
						}
					}
				}
			}
			if (b) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			String tempUrl = "";
			for (int i = 0; i < imgArr.length; i++) {
				if (null != imgArr[i]) {
					tempUrl += imgArr[i] + ";";
				}
			}
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("url", tempUrl);
			map.put("plantId", plantId);
			int res = plantInfoMapper.insertPlantPhoto(map);
			if (res > 0) {
				for (String string : imgUrl) {
					string = configParam.getPlantPhotoURL() + string.substring(string.lastIndexOf("/"));
					File file = new File(string);
					if (file.exists()) {
						file.delete();
					}
					logger.info("  图片删除成功!   操作人:" + u.getId() + "_" + u.getUserName() + "电站名称: "
							+ plantInfo.getPlantName());
				}
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_PHOTO_SUCCUESS);
			} else {
				logger.error(
						"  图片删除失败!    操作人:" + u.getId() + "_" + u.getUserName() + " 电站名称: " + plantInfo.getPlantName());
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
			}
		}
		String uploadPath = configParam.getPlantPhotoURL();
		List<String> path = DownloadUtils.uploadPhoto(request, uploadPath, configParam.getPlantPhotoRequestURL());
		if (!Util.isNotBlank(path)) {
			if (Util.isNotBlank(imgUrls)) {
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			} else {
				msg.setCode(Header.STATUS_SUCESS);
			}
		} else {
			PlantInfo plantInfo = plantInfoMapper.getPlantById(plantId);
			String plantPhoto = plantInfo.getPlantPhoto();
			Map<String, String> map = new HashMap<>(2);
			StringBuffer buffer = new StringBuffer();
			if (Util.isNotBlank(plantPhoto)) {
				String[] arr = plantPhoto.split(";");
				int num = arr.length + path.size();
				if (num > 3) {
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
				buffer.append(plantPhoto);
			} else {
				if (path.size() > 3) {
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i)).append(";");
			}
			map.put("url", buffer.toString());
			map.put("plantId", plantId);
			int res = plantInfoMapper.insertPlantPhoto(map);
			if (res != 0) {
				logger.info(StringUtil.appendStr(" 电站图片上传成功! 操作者:", logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			} else {
				logger.error(StringUtil.appendStr(" 电站图片上传失败! 操作者:", logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public MessageBean uploadPlantPhotos(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public MessageBean deletePlantPhoto(String str, Session session, HttpServletRequest req)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		// 获取要移除的照片Url
		String imgUrl = map.get("imgUrl") + "";
		String plantId = map.get("plantId") + "";
		String[] imgs = imgUrl.split(",");
		List<String> sendImgList = Arrays.asList(imgs);
		List<String> sendImgListf = new ArrayList<String>(sendImgList);
		// 获取电站对应图片
		String photo = plantInfoMapper.getPhoto(plantId);
		if (!"".equals(photo) && photo != null) {
			String[] split = photo.split(",");
			List<String> originImgList = Arrays.asList(split);
			List<String> originImgListf = new ArrayList<String>(originImgList);
			originImgListf.removeAll(sendImgListf);
			String substring = originImgListf.toString();
			String finalImgs = substring.substring(substring.indexOf("[") + 1, substring.lastIndexOf("]"));
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("plantId", plantId);
			parameterMap.put("url", finalImgs);
			int result = plantInfoMapper.insertPlantPhoto(parameterMap);
			if (result > 0) {
				for (int i = 0; i < sendImgList.size(); i++) {
					File file = new File(sendImgList.get(i));
					if (file.exists()) {
						file.delete();
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.DELETE_PHOTO_SUCCUESS);
					} else {
						msg.setCode(Header.STATUS_FAILED);
						msg.setMsg(msg.getMsg() + "第" + (i + 1) + "张图片不存在");
					}
				}
			}
		}

		return msg;
	}

	/**
	 * @Title: getPlantsForUser
	 * @Description: 获取当前登录用户可以操作的电站 type:0 为所在组织拥有的电站列表 1为其可以看见的电站的列表
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月6日下午3:10:27
	 */
	@Override
	public List<Integer> getPlantsForUser(String tokenId, Session session, int type)
			throws SessionException, SessionTimeoutException {
		User u = session.getAttribute(tokenId);
		// 用户类型，0企业用户，1个人用户
		String userType = u.getUserType();

		List<Integer> plantListCopy = new ArrayList<>();

		if ("0".equals(userType)) {
			// 得到所有组织列表
			List<SysOrg> orgList = orgMapper.getAllOrg();
			SysOrg org = ((SysOrg) u.getUserOrg());
			List<String> reList = new ArrayList<>();
			// 查出当前登录人的组织id有哪些
			reList.add(((SysOrg) u.getUserOrg()).getId() + "");
			reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
			List<Integer> plantList = plantInfoMapper.getPlantsForUser(reList);
			plantListCopy.addAll(plantList);
			if (type == 1) {
				List<String> temp = personalSetMapper.getPlantForUser(org.getId() + "");
				if (plantList != null && !plantList.isEmpty()) {
					if (temp != null && !temp.isEmpty()) {
						for (String string : temp) {
							plantListCopy.add(Integer.parseInt(string));
						}
					}
				} else {
					if (temp != null && !temp.isEmpty()) {
						plantListCopy = new ArrayList<Integer>();
						for (String string : temp) {
							plantListCopy.add(Integer.parseInt(string));
						}
					}
				}
			}
		} else if ("1".equals(userType)) {
			List<Integer> tempList = userPlantMapper.getUserPlant(u.getId());
			if (Util.isNotBlank(tempList)) {
				plantListCopy = tempList;
			}
		}

		return plantListCopy;
	}

	public List<Integer> getPlantsForUser(User u, int type) throws SessionException, SessionTimeoutException {
		String userType = u.getUserType();
		List<Integer> plantList = new ArrayList<>();
		if ("0".equals(userType)) {
			// 得到所有组织列表
			List<SysOrg> orgList = orgMapper.getAllOrg();
			SysOrg org = ((SysOrg) u.getUserOrg());
			List<String> reList = new ArrayList<>();
			// 查出当前登录人的组织id有哪些
			reList.add(((SysOrg) u.getUserOrg()).getId() + "");
			reList = ServiceUtil.getTree(orgList, org.getId() + "", reList);
			plantList = plantInfoMapper.getPlantsForUser(reList);

			// type=1时还要加上个性化定制可以让该用户看见的电站
			if (type == 1) {
				List<String> temp = personalSetMapper.getPlantForUser(org.getId() + "");
				if (plantList != null && !plantList.isEmpty()) {
					if (temp != null && !temp.isEmpty()) {
						for (String string : temp) {
							plantList.add(Integer.parseInt(string));
						}
					}
				} else {
					if (temp != null && !temp.isEmpty()) {
						plantList = new ArrayList<Integer>();
						for (String string : temp) {
							plantList.add(Integer.parseInt(string));
						}
					}
				}
			}
		} else if ("1".equals(userType)) {
			List<Integer> tempList = userPlantMapper.getUserPlant(u.getId());
			if (Util.isNotBlank(tempList)) {
				plantList = tempList;
			}
		}
		if (!Util.isNotBlank(plantList)) {
			plantList = new ArrayList<>();
		}
		return plantList;
	}

	public List<Integer> getInherentPlant(String orgId) throws SessionException, SessionTimeoutException {
		// 得到所有组织列表
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		// 查出当前登录人的组织id有哪些
		reList.add(orgId);
		reList = ServiceUtil.getTree(orgList, orgId, reList);
		List<Integer> plantList = plantInfoMapper.getPlantsForUser(reList);
		return plantList;
	}

	public List<String> getShowPlant(String orgId) throws SessionException, SessionTimeoutException {
		List<String> temp = personalSetMapper.getPlantForUser(orgId);
		return temp;
	}

	@Override
	public MessageBean getPlantAscription(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String orgId = map.get("orgId") + "";
		if ("null".equals(orgId) || "".equals(orgId)) {
			orgId = ((SysOrg) u.getUserOrg()).getId() + "";
		}
		/**
		 * 解析前端的必要参数
		 */
		String areaId = String.valueOf(map.get("areaId"));
		String tokenId = String.valueOf(map.get("tokenId"));
		String serviceType = String.valueOf(map.get("serviceType"));
		// type:1上钻 2 下钻
		int type = 2;
		if (!"".equals(map.get("type") + "") && null != map.get("type")) {
			type = Integer.parseInt(map.get("type") + "");
		}
		if (areaId == null || "null".equals(areaId) || "".equals(areaId)) {
			areaId = configParam.getLocation();
		}
		SysAddress address = addressMapper.getAreaByName(areaId);

		/** 获取当前登录用户所有的可见电站列表 */
		Map<String, Object> resultMap = new HashMap<>();
		List<Integer> plantList = getPlantsForUser(tokenId, session, 1);
		if (plantList != null && !plantList.isEmpty()) {
			List<SysAddress> addressList = addressMapper.getAdrForScreen(plantList);
			if (type == 1) {

				/** 加载当前节点的所有同父节点 */
				resultMap.put("kidsArea", ServiceUtil.getRegionTree(addressList, address.getFatherName()));

				/** 加载当前节点的父节点 */
				SysAddress fatherAddress = addressMapper.getAreaByName(address.getFatherName());
				RegionChildNode regionChildNode = new RegionChildNode();
				regionChildNode.setName(fatherAddress.getAreaName());
				regionChildNode.setId(fatherAddress.getName());
				regionChildNode
						.setLocation(new String[] { fatherAddress.getLocationY(), fatherAddress.getLocationX() });
				regionChildNode.setFatherId(fatherAddress.getFatherName());
				resultMap.put("areaInfo", regionChildNode);

			} else if (type == 2) {

				/** 加载当前节点的所有子节点 */
				resultMap.put("kidsArea", ServiceUtil.getRegionTree(addressList, address.getName()));

				/** 加载当前节点 */
				RegionChildNode regionChildNode = new RegionChildNode();
				regionChildNode.setName(address.getAreaName());
				regionChildNode.setId(address.getName());
				regionChildNode.setLocation(new String[] { address.getLocationY(), address.getLocationX() });
				regionChildNode.setFatherId(address.getFatherName());
				resultMap.put("areaInfo", regionChildNode);
			}

			/** 加载区域的电站集合 */
			if (serviceType != null && !"null".equals(serviceType) && "2".equals(serviceType)) {

				/** 电站选择集合 */
				RegionChildNode nowChildNode = (RegionChildNode) resultMap.get("areaInfo");
				map.clear();
				map.put("name", nowChildNode.getId());
				map.put("plants", plantList);
				List<PlantInfo> list = addressMapper.getPlantsByArea(map);
				List<Map<String, Object>> resultList = new ArrayList<>();
				if (list != null && !plantList.isEmpty()) {
					for (PlantInfo plantInfo : list) {
						Map<String, Object> tempMap = new HashMap<>();
						tempMap.put("id", plantInfo.getId());
						tempMap.put("name", plantInfo.getPlantName());
						tempMap.put("showType", 0);
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
		if (!"null".equals(orgId) && !"".equals(orgId)) {
			List<Integer> list = getInherentPlant(orgId);
			for (Integer integer : list) {
				List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get("plantsInfo");
				for (Map<String, Object> map2 : resultList) {
					if (map2.get("id").equals(integer)) {
						map2.put("showType", 1);
					}
				}
			}
			List<String> list2 = getShowPlant(orgId);
			for (String string : list2) {
				List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap.get("plantsInfo");
				for (Map<String, Object> map2 : resultList) {
					if ((String.valueOf(map2.get("id"))).equals(string)) {
						map2.put("showType", 2);
					}
				}
			}
		}
		msg.setBody(resultMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public MessageBean insertPlantAscription(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		String tokenId = String.valueOf(map.get("tokenId"));
		String orgId = map.get("orgId") + "";
		String plantId = String.valueOf(map.get("plantId"));
		String[] ids = null;
		if (!"".equals(plantId)) {
			ids = plantId.split(",");
		}
		List<SysOrg> orgList = soMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		reList = ServiceUtil.getFatherTreeList(orgList, orgId, reList);
		// 将传入组织可见的电站删除
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uId", orgId);
		paramMap.put("type", 1);
		int result = personalSetMapper.deleteUserRecord(paramMap);
		boolean reFlag = true;
		if (ids != null && ids.length != 0) {
			for (int i = 0; i < ids.length; i++) {
				List<Integer> tempList = new ArrayList<Integer>();
				for (int j = 0; j < reList.size(); j++) {
					boolean flag = true;
					// 查当前组织可见和固有的电站
					List<Integer> list = getAllPlant(reList.get(j) + "");
					for (Integer integer : list) {
						// 判断当前传进来的电站id在该组织是否已经拥有
						if (ids[i].equals(String.valueOf(integer))) {
							flag = false;
							break;
						}
					}
					if (flag) {
						tempList.add(Integer.valueOf(reList.get(j)));
					}
				}
				if (Util.isNotBlank(tempList)) {
					Map<String, Object> parameterMap = new HashMap<String, Object>();
					parameterMap.put("orgs", tempList);
					parameterMap.put("plantId", ids[i]);
					parameterMap.put("type", 1);
					parameterMap.put("createTime", System.currentTimeMillis());
					int re = personalSetMapper.insertUserRecord(parameterMap);
					if (re == 0) {
						reFlag = false;
					}
				}
			}
		}
		if (reFlag) {
			logger.info(StringUtil.appendStr(" 电站归属设置成功! 操作者:", logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody("设置归属成功！");
		}
		return msg;
	}

	@Override
	public MessageBean getPlantTreeByOrg(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 当前登录用户的组织
		SysOrg orgNow = ((SysOrg) u.getUserOrg());
		// 当前登录用户的组织id
		String orgId = orgNow.getId().intValue() + "";
		List<SysOrg> orgList = orgMapper.getAllOrg();
		// 装载根节点
		List resultList = new ArrayList<>();
		Map<String, Object> fatherMap = new HashMap<String, Object>();
		for (int i = 0; i < orgList.size(); i++) {
			SysOrg org = orgList.get(i);
			if (orgId.equals(org.getId() + "")) {
				fatherMap.put("check", "false");
				fatherMap.put("id", String.valueOf(org.getId()));
				fatherMap.put("icon", "");
				fatherMap.put("isParent", "true");
				fatherMap.put("name", org.getOrgName());
				fatherMap.put("open", "true");
				fatherMap.put("pId", "0");
				fatherMap.put("orgType", org.getOrgType());
				fatherMap.put("icon", "/images/setting/file.png");
				fatherMap.put("isPlant", false);
				orgList.remove(i);
				fatherMap.put("children", getChildTree(org.getId() + "", orgList));
				resultList.add(fatherMap);
				break;
			}
		}
		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public List getChildTree(String orgId, List<SysOrg> orgList) {
		List childList = new ArrayList<>();
		for (int i = 0; i < orgList.size(); i++) {
			SysOrg org = orgList.get(i);
			if (orgId.equals(org.getFatherId() + "")) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put("id", String.valueOf(org.getId()));
				childMap.put("pId", String.valueOf(org.getFatherId()));
				childMap.put("iconSkin", "icon01");
				childMap.put("name", org.getOrgName());
				childMap.put("orgType", org.getOrgType());
				childMap.put("icon", "/images/setting/file2.png");
				childMap.put("children", getChildTree(org.getId() + "", orgList));
				childMap.put("isPlant", false);
				childList.add(childMap);
			}
		}
		List<PlantInfo> plantList = plantInfoMapper.getPlantByOrgId(orgId);
		for (PlantInfo plantInfo : plantList) {
			Map<String, Object> childMap = new HashMap<String, Object>();
			childMap.put("id", String.valueOf(plantInfo.getId()));
			childMap.put("pId", String.valueOf(plantInfo.getOrgId()));
			childMap.put("name", plantInfo.getPlantName());
			childMap.put("icon", "/images/setting/plantManage/power2.png");
			childMap.put("iconSkin", "icon01");
			childMap.put("isPlant", true);
			childList.add(childMap);
		}
		return childList;
	}

	@Override
	public MessageBean getPlantTreeByArea(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = map.get("tokenId") + "";
		// 获取当前用户可见的电站id
		List<Integer> plantList = getPlantsForUser(tokenId, session, 1);
		List<SysAddress> addressList = addressMapper.getAreaByPlantId(plantList);
		// 装载根节点
		List resultList = new ArrayList<>();
		Map<String, Object> fatherMap = new HashMap<String, Object>();
		for (int i = 0; i < addressList.size(); i++) {
			SysAddress address = addressList.get(i);
			if (address.getFatherName().equals("0")) {
				fatherMap.put("check", "false");
				fatherMap.put("id", address.getName());
				fatherMap.put("isParent", "true");
				fatherMap.put("name", address.getAreaName());
				fatherMap.put("open", "true");
				fatherMap.put("pId", "0");
				fatherMap.put("icon", "/images/setting/file.png");
				addressList.remove(i);
				fatherMap.put("children", getPlantChildTree(address.getName(), addressList));
				fatherMap.put("isPlant", false);
				resultList.add(fatherMap);
				break;
			}
		}
		msg.setBody(resultList);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	public List getPlantChildTree(String name, List<SysAddress> addressList) {
		List childList = new ArrayList<>();
		for (int i = 0; i < addressList.size(); i++) {
			SysAddress address = addressList.get(i);
			if (name.equals(address.getFatherName())) {
				if (address.getLevel() != 4) {
					Map<String, Object> childMap = new HashMap<String, Object>();
					childMap.put("id", address.getName());
					childMap.put("pId", address.getFatherName());
					childMap.put("iconSkin", "icon01");
					childMap.put("name", address.getAreaName());
					childMap.put("icon", "/images/setting/file2.png");
					childMap.put("children", getPlantChildTree(address.getName(), addressList));
					childMap.put("isPlant", false);
					childList.add(childMap);
				} else {
					List<PlantInfo> plantList = addressMapper.getPlantsByAreaName(address.getName());
					List resultPlantList = new ArrayList<>();
					for (PlantInfo plantInfo : plantList) {
						Map<String, Object> childPlantMap = new HashMap<String, Object>();
						childPlantMap.put("id", String.valueOf(plantInfo.getId()));
						childPlantMap.put("pId", address.getName());
						childPlantMap.put("name", plantInfo.getPlantName());
						childPlantMap.put("icon", "/images/setting/plantManage/power2.png");
						childPlantMap.put("iconSkin", "icon01");
						childPlantMap.put("isPlant", true);
						resultPlantList.add(childPlantMap);
					}
					Map<String, Object> childMap = new HashMap<String, Object>();
					childMap.put("id", address.getName());
					childMap.put("pId", address.getFatherName());
					childMap.put("iconSkin", "icon01");
					childMap.put("name", address.getAreaName());
					childMap.put("icon", "/images/setting/file2.png");
					childMap.put("children", resultPlantList);
					childMap.put("isPlant", false);
					childList.add(childMap);
				}
			}
		}
		return childList;
	}

	/**
	 * @Title: getAllPlant
	 * @Description: 得到该组织固有和可见的电站id
	 * @param orgId
	 * @return: List<Integer>
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年1月12日上午10:30:21
	 */
	public List<Integer> getAllPlant(String orgId) {
		List<Integer> plantList = new ArrayList<>();
		// 得到所有组织列表
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		reList.add(orgId);
		reList = ServiceUtil.getTree(orgList, orgId, reList);
		plantList = plantInfoMapper.getPlantsForUser(reList);
		List<String> temp = personalSetMapper.getPlantForUser(orgId);
		if (plantList != null && !plantList.isEmpty()) {
			if (temp != null && !temp.isEmpty()) {
				for (String string : temp) {
					plantList.add(Integer.parseInt(string));
				}
			}
		} else {
			if (temp != null && !temp.isEmpty()) {
				plantList = new ArrayList<Integer>();
				for (String string : temp) {
					plantList.add(Integer.parseInt(string));
				}
			}
		}
		if (!Util.isNotBlank(plantList)) {
			plantList = new ArrayList<>();
		}
		return plantList;
	}

	@Override
	public MessageBean getPlantType(String jsonData)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		List<PlantTypeA> types = plantTypeAMapper.getAllPlantType();
		List<Map<String, Object>> resultListA = new ArrayList<>();
		if (Util.isNotBlank(types)) {
			for (int i = 0; i < types.size(); i++) {
				PlantTypeA type = types.get(i);
				Map<String, Object> tempMap = new HashMap<>(2);
				tempMap.put("id", type.getId());
				tempMap.put("name", type.getTypeName());
				resultListA.add(tempMap);
			}
		}
		List<PlantTypeB> typeb = plantTypeBMapper.getAllPlantType();
		List<Map<String, Object>> resultListB = new ArrayList<>();
		if (Util.isNotBlank(typeb)) {
			for (int i = 0; i < typeb.size(); i++) {
				PlantTypeB type = typeb.get(i);
				Map<String, Object> tempMap = new HashMap<>(2);
				tempMap.put("id", type.getId());
				tempMap.put("name", type.getTypeName());
				resultListB.add(tempMap);
			}
		}
		List result = new ArrayList<>(2);
		result.add(resultListA);
		result.add(resultListB);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(result);
		return msg;
	}

	@Override
	public MessageBean deleteWXPlantPhoto(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, String> map = Util.parseURLCode(jsonData);
		String plantId = map.get("plantId");
		String tokenId = map.get("tokenId");
		String imgURL = map.get("pic");
		PlantInfo plantInfo = plantInfoMapper.getPlantById(plantId);
		User u = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		// 删除照片路径
		String[] imgArr = StringUtils.split(StringUtil.checkBlank(plantInfo.getPlantPhoto()), ";");

		if (imgArr.length == 0) {
			logger.warn(StringUtil.appendStr("图片不存在!   操作人:", logUserName, "电站名称:", plantInfo.getPlantName(), "图片路径",
					imgURL));
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.NOT_FIND_PHOTO);
			return msg;
		}
		// 存在改路径就移除
		boolean b = true;
		for (int i = 0; i < imgArr.length; i++) {
			if (imgArr[i].equals(imgURL)) {
				imgArr[i] = null;
				b = false;
				break;
			}
		}
		if (b) {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.NOT_FIND_PHOTO);
			return msg;
		}
		String tempUrl = "";
		for (int i = 0; i < imgArr.length; i++) {
			if (null != imgArr[i]) {
				tempUrl += imgArr[i] + ";";
			}
		}
		/*
		 * if (!"".equals(tempUrl)) {
		 * tempUrl=tempUrl.substring(0,tempUrl.lastIndexOf(";"));
		 * 
		 * //tempUrl=tempUrl.replaceAll("\\\\", "\\\\\\\\"); }
		 */
		// 重新设置电站照片拼接字符串

		// 保存新的电站信息
		// updatePlantInfo(plantInfo);
		map.clear();
		map.put("url", tempUrl);
		map.put("plantId", plantId);
		int res = plantInfoMapper.insertPlantPhoto(map);
		if (res > 0) {
			imgURL = configParam.getPlantPhotoURL() + imgURL.substring(imgURL.lastIndexOf("/"));
			File file = new File(imgURL);
			if (file.exists()) {
				file.delete();
			}
			logger.info(StringUtil.appendStr("图片删除成功!   操作人:", logUserName, "电站名称:", plantInfo.getPlantName(), "图片路径",
					imgURL));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.DELETE_PHOTO_SUCCUESS);
		} else {
			logger.error(StringUtil.appendStr("图片删除失败!   操作人:", logUserName, "电站名称:", plantInfo.getPlantName(), "图片路径",
					imgURL));
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.NOT_FIND_PHOTO);
		}
		return msg;
	}

	@Override
	public MessageBean wxplantInfo(String jsonData) throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String plantId = String.valueOf(map.get("plantId"));
		PlantInfo plantInfo = plantInfoMapper.getPlantById(plantId);
		List<CollDevice> devices = deviceMapper.getPlantCollector(Integer.parseInt(plantId));
		Map<String, Object> addr = new HashMap<>(4);
		List<SysAddress> addrs = addressMapper.getPlantArea(Integer.parseInt(plantId));
		List<Map<String, Object>> collector = new ArrayList<>();
		List<Integer> ids = userPlantMapper.getPlantUser(plantId);
		List<SysUser> users = null;
		if (Util.isNotBlank(ids)) {
			users = userMapper.getUsersByIds(ids);
		}
		/** 业主 */
		List<Map<String, Object>> owner = new ArrayList<>();
		if (Util.isNotBlank(users)) {
			for (SysUser u : users) {
				Map<String, Object> param = new HashMap<>(3);
				param.put("id", u.getId());
				param.put("userName", u.getUserName());
				param.put("userTel", u.getUserTel());
				owner.add(param);
			}
		}
		/** 数采 */
		List<Map<String, String>> administrative = new ArrayList<>();
		if (Util.isNotBlank(devices)) {
			for (CollDevice device : devices) {
				Map<String, Object> param = new HashMap<>(3);
				param.put("id", device.getId());
				param.put("no", device.getDeviceSn());
				int num = deviceMapper.getCollectInveter(device.getDeviceSn());
				param.put("maxNum", num);
				param.put("inventerNum", num);
				param.put("collName", device.getDeviceName());
				// 端口id由于业务需求暂时写死数据
				String[] port = { "1", "2" };
				List<Object> resultList = new ArrayList<Object>();
				for (int i = 0, size = port.length; i < size; i++) {
					List<Object> collDeviceList = new ArrayList<Object>();
					Map<String, Object> collDeviceMap = new HashMap<String, Object>();
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("comId", port[i]);
					paramMap.put("collId", device.getId() + "");
					List<CollJsonDevSet> collJsonDevSetList = collJsonDevSetMapper.getJsonDevSetByComId(paramMap);
					for (int j = 0, size1 = collJsonDevSetList.size(); j < size1; j++) {
						Map<String, Object> tempMap = new HashMap<String, Object>();
						CollJsonDevSet collJsonDevSet = collJsonDevSetList.get(j);
						// 根据id查询出数采下连设备信息
						CollDevice collDevice = deviceMapper.getDeviceById(collJsonDevSet.getDevId() + "");
						DeviceDetailStringInverter mode = collDevice.getDeviceDetailStringInverter();
						tempMap.put("addr", collJsonDevSet.getSlaveId());
						tempMap.put("devName", collDevice.getDeviceName());
						tempMap.put("id", collDevice.getId());
						tempMap.put("model_id", collDevice.getDeviceModelId());
						tempMap.put("model", mode.getModel());
						String typeId = collDevice.getDeviceType() + "";
						tempMap.put("type_id", typeId);
						tempMap.put("type", typeMapper.getNameById(typeId));
						tempMap.put("deviceSn", collDevice.getDeviceSn());
						collDeviceList.add(tempMap);
					}
					collDeviceMap.put("datas", collDeviceList);
					collDeviceMap.put("spMax", "5");
					collDeviceMap.put("spName", "串口" + port[i]);
					collDeviceMap.put("com_id", port[i]);
					resultList.add(collDeviceMap);
				}
				param.put("serialPort", resultList);
				collector.add(param);
			}
		}
		/** 地址经纬度 */
		if (Util.isNotBlank(addrs)) {
			for (SysAddress address : addrs) {
				Map<String, String> tempMap = new HashMap<>(3);
				tempMap.put("name", address.getAreaName());
				tempMap.put("lat", address.getLocationY());
				tempMap.put("lng", address.getLocationX());
				administrative.add(tempMap);
			}
		}
		addr.put("administrative", administrative);
		addr.put("administrativeAddr", plantInfo.getPlantAddr());
		addr.put("detail", plantInfo.getPlantFullAddress());
		addr.put("location", plantInfo.getLoaction());
		/** 组装返回前台参数 */
		map.clear();
		map.put("area", plantInfo.getPlantArea());
		map.put("capacity", plantInfo.getCapacity());
		map.put("capacityUnit", plantInfo.getUnit());
		map.put("location", plantInfo.getLoaction());
		map.put("name", plantInfo.getPlantName());
		String photo = StringUtil.checkBlank(plantInfo.getPlantPhoto());
		String[] photos = null;
		if (Util.isNotBlank(photo)) {
			photos = StringUtils.split(photo, ";");
		}
		map.put("pic", photos);
		int type[] = { plantInfo.getPlantTypeA(), plantInfo.getPlantTypeB() };
		map.put("type", type);
		SysPowerPrice price = sysPowerPriceMapper.selectByPrimaryKey(plantInfo.getPriceId());
		map.put("price", price == null ? "无电价设置" : price.getPriceName());
		map.put("price_id", plantInfo.getPriceId());
		map.put("collector", collector);
		map.put("owner", owner);
		map.put("addr", addr);
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public MessageBean insertPlantUser(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		// 当前登录人的id和名称
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		// 获取前端的数据，并组装成user对象
		SysUser user = new SysUser();
		boolean useTel = Boolean.valueOf(map.get("useTel") + "");
		// 判断是否用手机号作为登录账号
		if (useTel) {
			user.setLoginId(map.get("userTel") + "");
		} else {
			user.setLoginId(map.get("loginId") + "");
		}
		user.setPassword(map.get("password") + "");
		user.setUserName(map.get("userName") + "");
		user.setGender("1");
		user.setUserTel(map.get("userTel") + "");
		user.setEmail(map.get("email") + "");
		user.setOrgId(Integer.parseInt(map.get("orgId") + ""));
		user.setPinyinSearch(ChineseToEnglish.getPingYin(map.get("userName") + ""));
		user.setLastModifyTime(System.currentTimeMillis());
		user.setLastModifyUser(Integer.parseInt(u.getId()));
		user.setCreateTime(System.currentTimeMillis());
		user.setUserValid("0");
		user.setUserStatus(map.get("status") + "");
		user.setUserType("1");
		// 新增用户
		int result = userMapper.inserNewUser(user);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roleId", 2);
		paramMap.put("userId", user.getId());
		paramMap.put("createTime", System.currentTimeMillis());
		paramMap.put("createUser", u.getId());
		// 给新增的用户赋予电站业主的角色
		int roleResult = userRoleMapper.insertUserRole(paramMap);
		SysUserPlant userPlant = new SysUserPlant();
		userPlant.setUserId(user.getId());
		userPlant.setPlantId(Integer.valueOf(String.valueOf(map.get("plantId"))));
		userPlant.setCreateTime(System.currentTimeMillis());
		userPlant.setCreateUser(Integer.valueOf(u.getId()));
		// 给新增用户关联电站
		int userPlantResult = userPlantMapper.saveUserPlant(userPlant);
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("userId", user.getId() + "");
		// 组装数据
		MessageBean msg = new MessageBean();
		if (result != 0 && roleResult != 0 && userPlantResult != 0) {
			logger.info(StringUtil.appendStr(" 业主新增成功!", user.toString(), paramMap, "操作者:", logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.REGISTER_SUCCUESS);
		} else {
			logger.error(StringUtil.appendStr(" 业主新增失败! ", user.toString(), paramMap, "操作者:", logUserName));
			throw new ServiceException(Msg.REGISTER_FAILED);
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean wxupdatePlantInfo(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String typeCopy = String.valueOf(map.get("type"));
		if (Util.isNotBlank(typeCopy)) {
			int type = Integer.parseInt(typeCopy);
			String tokenId = String.valueOf(map.get("tokenId"));
			User u = session.getAttribute(tokenId);
			int uID = Integer.parseInt(u.getId());
			String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
			/** 更改时参数装载 */
			PlantInfo plantInfo = plantInfoMapper.getPlantInfo(String.valueOf(map.get("plantId")));
			int plantId = plantInfo.getId();
			//plantInfo.setId(plantId);
			switch (type) {
			/** 电站名字 */
			case 1:
				String plantName = String.valueOf(map.get("name"));
				plantInfo.setPlantName(plantName);
				plantInfo.setPinyinSearch(ChineseToEnglish.getPinYinHeadChar(plantName));
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);
				int result = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result > 0) {
					logger.info(StringUtil.appendStr(" 更改电站名字成功! id:", plantId, "name", plantName, "操作者", logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(
							StringUtil.appendStr(" 更改电站名字失败! id:", plantId, "name", plantName, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			/** 电站类型 */
			case 2:
				List<Integer> types = (List<Integer>) map.get("name");
				plantInfo.setPlantTypeA(types.get(0));
				plantInfo.setPlantTypeB(types.get(1));
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);
				int result2 = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result2 > 0) {
					logger.info("重新刷新缓存");
					systemCache.reInit();
					logger.info(StringUtil.appendStr(" 更改电站类型成功! id:", plantId, "type", types, "操作者", logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(StringUtil.appendStr(" 更改电站类型失败! id:", plantId, "type", types, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			/** 电站容量 */
			case 3:
				double capacity = Double.parseDouble(String.valueOf(map.get("name")));
				plantInfo.setCapacity(capacity);
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);

				plantInfo.setCapacityBase(plantInfo.calcCapacityBase());

				int result3 = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result3 > 0) {
					logger.info(
							StringUtil.appendStr(" 更改电站容量成功! id:", plantId, "capacity", capacity, "操作者", logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(
							StringUtil.appendStr(" 更改电站容量失败! id:", plantId, "capacity", capacity, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			/** 电站占地面积 */
			case 4:
				int plantArea = Integer.parseInt(String.valueOf(map.get("name")));
				plantInfo.setPlantArea(plantArea);
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);
				int result4 = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result4 > 0) {
					logger.info(StringUtil.appendStr(" 更改电站面积成功! id:", plantId, "plantArea", plantArea, "操作者",
							logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(StringUtil.appendStr(" 更改电站面积失败! id:", plantId, "plantArea", plantArea, "操作者",
							logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			/** 电站各级经纬度及电站经纬度 */
			case 5:
				Map<String, Object> param = (Map<String, Object>) map.get("name");
				String administrativeAddr = String.valueOf(param.get("administrativeAddr"));
				String location = String.valueOf(param.get("location"));
				/** 修改电站地址 */
				plantInfo.setPlantAddr(administrativeAddr);
				String[] locationTemp = StringUtils.split(location, ",");
				plantInfo.setLoaction(locationTemp[1] + "," + locationTemp[0]);
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);
				/** 更改电站的经纬度地址 */
				int result5 = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result5 > 0) {
					logger.info(StringUtil.appendStr("更改电站的经纬度地址成功!电站id:", plantId, "操作者", logUserName));
					/** 删除电站的各级经纬度 */
					result5 = addressMapper.delPlantAddr(plantId);
					if (result5 > 0) {
						logger.info(StringUtil.appendStr("删除电站的各级经纬度成功!电站id:", plantId, "操作者", logUserName));
						List<Map<String, Object>> locations = (List<Map<String, Object>>) param.get("locationArr");
						List<SysAddress> addres = new ArrayList<>();
						if (Util.isNotBlank(locations)) {
							SysAddress addressFather = null;
							for (int i = 0; i < locations.size(); i++) {
								Map<String, Object> locationMap = locations.get(i);
								SysAddress address = new SysAddress();
								address.setAreaName(String.valueOf(locationMap.get("name")));
								address.setLevel(i + 1);
								address.setLocationX(locationMap.get("lng") + "");
								address.setLocationY(locationMap.get("lat") + "");
								address.setPlantId(plantId);
								if (i == 0) {
									address.setFatherId(0);
								} else {
									address.setFatherId(addressFather.getId());
								}
								addressMapper.insertPlantAddr(address);
								logger.info(StringUtil.appendStr("新增电站经纬度记录 电站id:", plantId, "地区名称",
										locationMap.get("areaName"), "操作者", logUserName));
								addressFather = address;
							}
							msg.setCode(Header.STATUS_SUCESS);
							msg.setMsg(Msg.REPORT_SET_SUCCESS);
						} else {
							logger.error(StringUtil.appendStr("更改电站各级经纬度参数不全 id:", plantId, "操作者", logUserName));
							throw new ServiceException(Msg.PARAM_NOT_COMPLETE);
						}
					} else {
						logger.error(StringUtil.appendStr("删除电站各级经纬度失败 id:", plantId, "操作者", logUserName));
						throw new ServiceException(Msg.REPORT_SET_FAILED);
					}
				} else {
					logger.error(StringUtil.appendStr("更改电站的经纬度地址 失败id:", plantId, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			/** 电站的详细地址 */
			case 6:
				String plantAddr = String.valueOf(map.get("name"));
				plantInfo.setPlantFullAddress(plantAddr);
				plantInfo.setLastModifyTime(System.currentTimeMillis());
				plantInfo.setLastModifyUser(uID);
				int result6 = plantInfoMapper.updatePlantInfoSelective(plantInfo);
				if (result6 > 0) {
					logger.info(
							StringUtil.appendStr("更改电站的经纬度地址成功!电站id:", plantId, "详细地址", plantAddr, "操作者", logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(
							StringUtil.appendStr("更改电站的详细地址 失败id:", plantId, "详细地址", plantAddr, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			case 7:
				Integer price = Integer.parseInt(String.valueOf(map.get("name")));
				plantInfo.setPriceId(price);
				int result7 = plantInfoMapper.updatePlantPrice(plantInfo);
				if (result7 > 0) {
					logger.info(StringUtil.appendStr("更改电站价格成功!电站id:", plantId, "价格", price, "操作者", logUserName));
					logger.info("重新刷新缓存");
					systemCache.reInit();
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				} else {
					logger.error(StringUtil.appendStr("更改电站价格成功址 失败id:", plantId, "价格", price, "操作者", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
				break;
			default:
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_SUPPORT_TYPE);
				break;
			}
		} else {
			throw new ServiceException(Msg.PARAM_NOT_COMPLETE);
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean wxinsertPlantInfo(String jsonData, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		PlantInfo plantInfo = new PlantInfo();
		long time = System.currentTimeMillis();
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		/** 电站名字 */
		String name = String.valueOf(map.get("name"));
		plantInfo.setPlantName(name);
		plantInfo.setPinyinSearch(ChineseToEnglish.getPinYinHeadChar(name));
		/*** price */
		Integer price = Integer.parseInt(String.valueOf(map.get("price_id")));
		/** capacity */
		double capacity = Double.parseDouble(String.valueOf(map.get("capacity")));
		plantInfo.setCapacity(capacity);
		plantInfo.setUnit("kW");
		Integer area = Integer.parseInt(String.valueOf(map.get("area")));
		plantInfo.setPlantArea(area);
		List<Map<String, String>> collector = (List<Map<String, String>>) map.get("collector");
		/** 获取第一个数采的公司作为电站公司归属 */
		CollDevice collect = deviceMapper.getCollectBySn(collector.get(0).get("no"));
		int orgId = collect.getOrgId();
		plantInfo.setOrgId(orgId);
		if ("1".equals(u.getUserType())) {
			plantInfo.setContacts(u.getId());
		}
		plantInfo.setCreateTime(time);
		plantInfo.setGridConnectedDate(time);
		List<Integer> types = (List<Integer>) map.get("type");
		plantInfo.setPlantTypeA(types.get(0));
		plantInfo.setPlantTypeB(types.get(1));
		plantInfo.setPlantValid("0");
		plantInfo.setPlantStatus("1");
		plantInfo.setPriceId(price);
		Map<String, Object> locationParam = (Map<String, Object>) map.get("addr");
		plantInfo.setPlantFullAddress(StringUtil.checkBlank(String.valueOf(locationParam.get("detail"))));
		String location = String.valueOf(locationParam.get("location"));
		String[] locationTemp = StringUtils.split(location, ",");
		plantInfo.setLoaction(locationTemp[1] + "," + locationTemp[0]);
		plantInfo.setCapacityBase(plantInfo.calcCapacityBase());
		plantInfo.setPlantAddr(String.valueOf(locationParam.get("administrativeAddr")));
		/** 插入电站记录 */
		int status = plantInfoMapper.insertPlantSelective(plantInfo);
		if (status > 0) {
			logger.info(StringUtil.appendStr("插入电站记录成功 :", plantInfo.toString(), "操作者", logUserName));
			int plantId = plantInfo.getId();
			List<Map<String, Object>> administrative = (List<Map<String, Object>>) locationParam.get("administrative");
			if (Util.isNotBlank(administrative)) {
				SysAddress addressFather = null;
				/** 插入电站各级经纬度 */
				for (int i = 0; i < administrative.size(); i++) {
					Map<String, Object> locationMap = administrative.get(i);
					SysAddress address = new SysAddress();
					address.setAreaName(String.valueOf(locationMap.get("name")));
					address.setLevel(i + 1);
					address.setLocationX(String.valueOf(locationMap.get("lng")));
					address.setLocationY(String.valueOf(locationMap.get("lat")));
					address.setPlantId(plantId);
					if (i == 0) {
						address.setFatherId(0);
					} else {
						address.setFatherId(addressFather.getId());
					}
					addressMapper.insertPlantAddr(address);
					logger.info(StringUtil.appendStr("新增电站经纬度记录 电站id:", plantId, "地区名称", address, "操作者", logUserName));
					addressFather = address;
				}
				/*
				 * Map<String, Object>priceMap=new HashMap<>(4); priceMap.put("value", price);
				 * priceMap.put("plantId", plantId); priceMap.put("valid", "0");
				 * priceMap.put("status", "1"); status=priceMapper.insertPrice(priceMap); if
				 * (status>0) {
				 * logger.info(StringUtil.appendStr("新增电站价格成功!电站id:",plantId,"价格",price,"操作者",
				 * logUserName)); }else {
				 * logger.error(StringUtil.appendStr("新增电站价格成功址 失败id:",plantId,"价格",price,"操作者",
				 * logUserName)); throw new ServiceException(Msg.CREATE_FAILED); }
				 */
				/** 当操作者是业主账户时 增加其所属电站 并校验业主是否有归属组织 */
				if ("1".equals(u.getUserType())) {
					SysUserPlant userPlant = new SysUserPlant();
					userPlant.setCreateTime(time);
					userPlant.setPlantId(plantId);
					userPlant.setUserId(Integer.parseInt(u.getId()));
					userPlant.setCreateUser(Integer.parseInt(u.getId()));
					status = userPlantMapper.saveUserPlant(userPlant);
					if (status > 0) {
						logger.info(StringUtil.appendStr("新增业主账户成功! 操作者:", logUserName));
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.ADD_SUCCUESS);
					} else {
						logger.error(StringUtil.appendStr("新增业主账户失败! 操作者:", logUserName));
						throw new ServiceException(Msg.CREATE_FAILED);
					}
					SysUser user = userMapper.getUserInfoById(u.getId());
					if (user.getOrgId() == null || user.getOrgId() == 0) {
						Map<String, Object> userMap = new HashMap<>();
						userMap.put("userId", user.getId());
						userMap.put("orgId", orgId);
						userMap.put("lastModifyUser", user.getId());
						userMap.put("lastModifyTime", String.valueOf(time));
						status = userMapper.updateUserOrg(userMap);
						if (status > 0) {
							logger.info(StringUtil.appendStr("修改当前账户组织成功!orgId", orgId, "操作者:" + logUserName));
						} else {
							logger.error(StringUtil.appendStr("修改当前账户组织失败!orgId", orgId, "操作者:" + logUserName));
							throw new ServiceException(Msg.CREATE_FAILED);
						}
					}
				}
				/** 插入电站业主 */
				List<Map<String, String>> ownerList = (List<Map<String, String>>) map.get("owner");
				/** 传递事务 由其他业务处理新增业主 */
				if (Util.isNotBlank(ownerList)) {
					for (Map<String, String> owner : ownerList) {
						owner.put("tokenId", tokenId);
						owner.put("plantId", String.valueOf(plantId));
						String data = Util.MapToJson(owner);
						userService.savePlantOwner(data, session);
					}
				}

				/** 激活数采 */
				/** 传递事务 由其他业务处理新增数采 */
				for (Map<String, String> device : collector) {
					// 通过二维码判断是自研数采还是畅洋数采
					String version = String.valueOf(device.get("no").charAt(3));
					if ("1".equals(version)) {
						device.put("tokenId", tokenId);
						device.put("plantId", String.valueOf(plantId));
						device.put("serviceType", "0");
						String data = Util.MapToJson(device);
						deviceService.updateDevice(data, session);
					} else if ("2".equals(version)) {
						device.put("tokenId", tokenId);
						device.put("updateType", "0");
						device.put("plantId", plantId + "");
						String data = Util.MapToJson(device);
						deviceService.updateDeviceNew(data, session);
					}
				}
				logger.info("刷新缓存");
				systemCache.reInit();
				map.clear();
				map.put("plantId", String.valueOf(plantId));
				List plantList = plantInfoServiceImpl.getPlantsForUser(tokenId, session, 1);
				int plantNum = 0;
				if (Util.isNotBlank(plantList)) {
					plantNum = plantList.size();
				}
				map.put("plantNum", plantNum);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(map);
				msg.setMsg(Msg.REPORT_SET_SUCCESS);
			} else {
				logger.error(StringUtil.appendStr("新建电站经纬度信息不全!", administrative, "操作者", logUserName));
				throw new ServiceException(Msg.CREATE_FAILED);
			}
		} else {
			logger.error(StringUtil.appendStr("插入电站记录失败  :", plantInfo, "操作者", logUserName));
			throw new ServiceException(Msg.CREATE_FAILED);
		}
		return msg;
	}

	public SysWeather getPlantTodayWeather(int plantId) throws ServiceException {
		String pId = String.valueOf(plantId);
		Map<String, Object> map = new HashMap<>(2);
		map.put("plantId", plantId);
		map.put("dataTime", Util.getBeforDayMin(0));
		SysWeather weather = weatherMapper.getPlantNowWeather(map);
		if (weather == null) {
			weather = new SysWeather();
			Map<String, Object> weatherMap = ApiUtil
					.getWeatherByPlantId(plantInfoMapper.getPlantById(pId).getLoaction(), configParam.getBaiduAppAk());
			String statu = String.valueOf(weatherMap.get("status"));
			if ("success".equalsIgnoreCase(statu)) {
				List<Map<String, Object>> result = (List<Map<String, Object>>) weatherMap.get("results");
				List<Map<String, String>> teMaps = (List<Map<String, String>>) result.get(0).get("weather_data");
				String weatherPic = teMaps.get(0).get("dayPictureUrl");
				weatherPic = "/images/weather" + weatherPic.substring(weatherPic.lastIndexOf("/"));
				weather.setWeather(teMaps.get(0).get("weather"));
				weather.setDayPictureUrl(weatherPic);
			}
		}
		return weather;
	}

	@Override
	public List<SysWeather> getPlantThreeWeather(int plantId) throws ServiceException {
		List<SysWeather> list = weatherMapper.getPlantThreeWeather(plantId);
		if (!Util.isNotBlank(list)) {
			list = new ArrayList<>(3);
			Map<String, Object> weathersMap = ApiUtil.getWeatherByPlantId(
					plantInfoMapper.getPlantById(String.valueOf(plantId)).getLoaction(), configParam.getBaiduAppAk());
			String statu = String.valueOf(weathersMap.get("status"));
			if ("success".equalsIgnoreCase(statu)) {
				List<Map<String, Object>> result = (List<Map<String, Object>>) weathersMap.get("results");
				List<Map<String, String>> teMaps = (List<Map<String, String>>) result.get(0).get("weather_data");
				for (int i = 0; i < 3; i++) {
					SysWeather weather = new SysWeather();
					Map<String, String> weatherMap = teMaps.get(i);
					if (i == 0) {
						String weatherPic = weatherMap.get("dayPictureUrl");
						weatherPic = "/images/weather" + weatherPic.substring(weatherPic.lastIndexOf("/"));
						weather.setDataTime(System.currentTimeMillis());
						weather.setPlantId(plantId);
						weather.setDayPictureUrl(weatherPic);
						weather.setWind(weatherMap.get("wind"));
						String temperature = weatherMap.get("temperature");
						temperature = temperature.substring(0, temperature.lastIndexOf("℃"));
						weather.setTemperature(temperature);
						String currentTemp = weatherMap.get("date");
						String currentTemp2 = StringUtils.split(currentTemp, "：")[1];
						currentTemp2 = currentTemp2.substring(0, currentTemp2.lastIndexOf("℃"));
						weather.setCurrentTemperature(currentTemp2);
					} else {
						String weatherPic = weatherMap.get("dayPictureUrl");
						weatherPic = "/images/weather" + weatherPic.substring(weatherPic.lastIndexOf("/"));
						weather.setDataTime(Util.getBeforDayMin(-i));
						weather.setPlantId(plantId);
						weather.setDayPictureUrl(weatherPic);
						weather.setWind(weatherMap.get("wind"));
						String temperature = weatherMap.get("temperature");
						temperature = temperature.substring(0, temperature.lastIndexOf("℃"));
						weather.setTemperature(temperature);
					}
					list.add(weather);
				}
			}
		}
		return list;
	}

	@Override
	public MessageBean getPlantWeather(String jsonData, Session session) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		int plantId = Integer.parseInt(String.valueOf(map.get("plantId")));
		String serviceType = String.valueOf(map.get("serviceType"));
		if (!Util.isNotBlank(serviceType)) {
			serviceType = "0";
		}
		if ("0".equals(serviceType)) {
			List<SysWeather> resultList = new ArrayList<>(1);
			resultList.add(getPlantTodayWeather(plantId));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultList);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(getPlantThreeWeather(plantId));
		}
		return msg;
	}

	@Override
	public MessageBean getDefaultPowerPrice() {
		MessageBean msg = new MessageBean();
		Map<String, String> map = new HashMap<>(1);
		map.put("powerPrice", configParam.getPowerPrice());
		msg.setBody(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getUserType(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, Object> map = Util.parseURL(str);
		String userTel = map.get("userTel") + "";
		List<SysUser> list = userMapper.getUserByTel(userTel);
		// 返回type（1：检查没有账号;2：检查为业主用户；3：检查为企业用户，那么不能变成业主用户；4：检查该账号已经是本电站业主）
		String result = "1";
		if (Util.isNotBlank(list)) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				SysUser user = list.get(i);
				String userType = user.getUserType();
				if (userType.equals("0")) {
					result = "3";
				} else if (userType.equals("1")) {
					result = "2";
//					resultMap.put("name", user.getUserName());
//					resultMap.put("userTel", user.getUserTel());
					resultMap.put("userId", user.getId() + "");
				}
			}
			resultMap.put("type", result);
		} else {
			result = "1";
			resultMap.put("type", result);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean insertPlantOwner(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId") + "");
		String logUserName = StringUtil.appendStrNoBlank(u.getId(), "_", u.getUserName());
		String userId = map.get("userId") + "";
		String plantId = map.get("plantId") + "";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", userId);
		paramMap.put("plantId", plantId);
		List<SysUserPlant> userPlantList = userPlantMapper.selectPlantUser(paramMap);
		if (!Util.isNotBlank(userPlantList)) {
			SysUserPlant userPlant = new SysUserPlant();
			userPlant.setUserId(Integer.valueOf(userId));
			userPlant.setPlantId(Integer.valueOf(plantId));
			userPlant.setCreateTime(System.currentTimeMillis());
			userPlant.setCreateUser(Integer.valueOf(u.getId()));
			// 给新增用户关联电站
			int userPlantResult = userPlantMapper.saveUserPlant(userPlant);
			if (userPlantResult > 0) {
				logger.info(StringUtil.appendStr("新增电站业主成功 id:", userId, "操作者", logUserName));
				msg.setBody(Header.STATUS_SUCESS);
				msg.setMsg(Msg.ADD_SUCCUESS);
			} else {
				logger.error(StringUtil.appendStr("新增电站业主失败 id:", userId, "操作者", logUserName));
				throw new ServiceException(Msg.ADD_FAILED);
			}
		} else {
			logger.error(StringUtil.appendStr("此账号是该电站业主 id:", userId, "操作者", logUserName));
			throw new ServiceException("此账号是该电站业主");
		}
		return msg;
	}

	@Override
	public Map<String, List<Integer>> getPlantsTypeForUser(String tokenId, Session session, int type)
			throws SessionException, SessionTimeoutException, ServiceException {
		List<Integer> plantList = getPlantsForUser(tokenId, session, type);
		Map<String, List<Integer>> map = new HashMap<>();
		if (Util.isNotBlank(plantList)) {
			for (Integer pId : plantList) {
				String PlantType = systemCache.plantTypeList.get(String.valueOf(pId));
				if (map.containsKey(PlantType)) {
					List<Integer> tempList = map.get(PlantType);
					tempList.add(pId);
				} else {
					List<Integer> tempList = new ArrayList<>();
					tempList.add(pId);
					map.put(PlantType, tempList);
				}
			}
		}
		return map;
	}

	/**
	 * @Title: getPlantTypeForUser
	 * @Description: 获取用户的电站类型最后分类 1：光伏；2：储能；3：光储
	 * @param tokenId
	 * @param session
	 * @param type
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       String
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月29日下午5:23:23
	 */
	@Override
	public String getPlantTypeForUser(String tokenId, Session session, int type)
			throws SessionException, SessionTimeoutException, ServiceException {
		List<Integer> plantList = getPlantsForUser(tokenId, session, type);
		Map<String, List<Integer>> map = new HashMap<>();
		boolean pv = false, energy = false, other = false;
		if (Util.isNotBlank(plantList)) {
			for (Integer pId : plantList) {
				String PlantType = systemCache.plantTypeList.get(String.valueOf(pId));
				switch (PlantType) {
				case "1":
					pv = true;
					break;
				case "2":
					energy = true;
					break;
				case "3":
					other = true;
					break;
				default:
					break;
				}
			}
		}
		String result = "-1";
		if ((pv && energy) || other) {
			result = "3";
		} else if (pv && !energy) {
			result = "1";
		} else if (!pv && energy) {
			result = "2";
		} else if (!pv && !energy && !other) {
			// 当用户没有电站时，默认加载光伏电站
			result = "1";
		}
		return result;
	}

	/**
	 * @Title: getPlantTypeForUser
	 * @Description: TODO
	 * @param u
	 * @param             type=1时还要加上个性化定制可以让该用户看见的电站
	 * @param serviceType 1获取用户电站类型 0获取用户电站类型集合
	 * @return
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 * @throws ServiceException:       String
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年4月8日上午10:21:31
	 */
	@Override
	public String getPlantTypeForUser(User u, int type, int serviceType)
			throws SessionException, SessionTimeoutException, ServiceException {
		List<Integer> plantList = getPlantsForUser(u, type);
		Map<String, List<Integer>> map = new HashMap<>();

		boolean pv = false, energy = false, other = false, nx = false;
		if (Util.isNotBlank(plantList)) {
			for (Integer pId : plantList) {
				String PlantType = systemCache.plantTypeList.get(String.valueOf(pId));
				switch (PlantType) {
				// 光伏
				case "1":
					pv = true;
					break;
				// 储能
				case "2":
					energy = true;
					break;
				// 光储
				case "3":
					other = true;
					break;
				// 能效
				case "4":
					nx = true;
					break;
				default:
					break;
				}
			}
		}
		String result = "";
		if (serviceType == 1) {
			result = "-1";
			if ((pv && energy) || other) {
				result = "3";
			} else if (pv && !energy) {
				result = "1";
			} else if (!pv && energy) {
				result = "2";
			} else if (nx == true) {
				result = "4";
			} else if (!pv && !energy && !other) {
				// 当用户没有电站时，默认加载光伏电站
				result = "1";
			}
		} else {
			if (pv) {
				result += "1,";
			}
			if (energy) {
				result += "2,";
			}
			if (other) {
				result += "3,";
			}
			if (nx) {
				result += "4,";
			}
			if (StringUtil.isBlank(result)) {
				result = "";
			} else {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	@Override
	public String getPlantTypeForUserWX(User u, int type)
			throws SessionException, SessionTimeoutException, ServiceException {
		List<Integer> plantList = getPlantsForUser(u, type);
		Map<String, List<Integer>> map = new HashMap<>();
		boolean pv = false, energy = false, other = false;
		if (Util.isNotBlank(plantList)) {
			for (Integer pId : plantList) {
				String PlantType = systemCache.plantTypeList.get(String.valueOf(pId));
				switch (PlantType) {
				case "1":
					pv = true;
					break;
				case "2":
					energy = true;
					break;
				case "3":
					other = true;
					break;
				default:
					break;
				}
			}
		}
		String result = "-1";
		if (energy || other) {
			result = "3";
		} else {
			result = "1";
		}
		return result;
	}

	@Override
	public MessageBean getCnPlantPrice(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Util.parseURL(str);
		String plantType = map.get("plantType") + "";
		List<SysPowerPrice> list = sysPowerPriceMapper.getCnPlantPrice(plantType);
		if (Util.isNotBlank(list)) {
			for (int i = 0, size = list.size(); i < size; i++) {
				SysPowerPrice powerPrice = list.get(i);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", powerPrice.getId());
				resultMap.put("name", powerPrice.getPriceName());
				resultList.add(resultMap);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		return msg;
	}

	@Override
	public MessageBean getPlantPriceDetail(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = Util.parseURL(str);
		String priceId = map.get("id") + "";
		List<SysPowerPriceDetail> list = sysPowerPriceDetailMapper.getPlantPriceDetail(priceId);
		if (Util.isNotBlank(list)) {
			for (int i = 0, size = list.size(); i < size; i++) {
				SysPowerPriceDetail sysPowerPriceDetail = list.get(i);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("neg",
						sysPowerPriceDetail.getNegPrice() == 0.0 ? "--" : sysPowerPriceDetail.getNegPrice() + "");
				resultMap.put("pos", sysPowerPriceDetail.getPosPrice());
				resultMap.put("time", sysPowerPriceDetail.getStartTime() + "~" + sysPowerPriceDetail.getEndTime());
				resultList.add(resultMap);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		return msg;
	}
}
