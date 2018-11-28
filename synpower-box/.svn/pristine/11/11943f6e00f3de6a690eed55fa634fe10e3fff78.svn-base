package com.synpower.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.CollDevice;
import com.synpower.bean.DeviceDetailCamera;
import com.synpower.bean.DeviceDetailCentralInverter;
import com.synpower.bean.DeviceDetailModule;
import com.synpower.bean.DeviceDetailStringInverter;
import com.synpower.bean.DeviceType;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.DeviceDetailCameraMapper;
import com.synpower.dao.DeviceTypeMapper;
import com.synpower.dao.DeviceDetailCentralInverterMapper;
import com.synpower.dao.DeviceDetailModuleMapper;
import com.synpower.dao.DeviceDetailStringInverterMapper;
import com.synpower.dao.PlantInfoMapper;
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
import com.synpower.service.EquipmentService;
import com.synpower.util.DownloadUtils;
import com.synpower.util.ReadExcel;
import com.synpower.util.StringUtil;
import com.synpower.util.Util;

import jxl.read.biff.BiffException;
@Service
public class EquipmentServiceImpl implements EquipmentService{
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private DeviceTypeMapper deviceMapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private DeviceDetailCentralInverterMapper inverterMapper;
	@Autowired
	private DeviceDetailStringInverterMapper stringInverterMapper;
	@Autowired
	private CollDeviceMapper collDeviceMapper;
	@Autowired
	private DeviceDetailModuleMapper subassemblyMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private DeviceDetailCameraMapper cameraModelMapper;
	@Override
	public MessageBean getEquipmentInfo(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//查询设备类型
		List<DeviceType> deviceList = deviceMapper.getDeviceTypeInfo();
		for (DeviceType deviceType : deviceList) {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("name", deviceType.getTypeName());
			map.put("value", deviceType.getId());
			resultList.add(map);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		return msg;
	}
	@Override
	public MessageBean listEquipmentInfo(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("length")+"");
		// 获取起始位置
		Integer alarm_page_num = Integer.parseInt(map.get("start")+"");
		// 获取状态
		String status = (String) map.get("status");
		// 获取设备厂家
		String manufacturer = (String) map.get("manufacturer");
		// 获取设备型号
		String model = (String) map.get("model");
		List resultList = new ArrayList();
		Map<String, Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("min", alarm_page_num);
		parameterMap.put("max", each_page_num);
		parameterMap.put("status", status);
		parameterMap.put("manufacturer", manufacturer);
		parameterMap.put("model", model);
		int count = inverterMapper.countInverterInfo(parameterMap);
		List<Map> inverterList = inverterMapper.listInverterInfo(parameterMap);
		if(Util.isNotBlank(inverterList)){
			for (Map map2 : inverterList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", map2.get("id"));
				resultMap.put("deviceType", map2.get("device_type"));
				resultMap.put("modelIdentity", map2.get("model_identity"));
				resultMap.put("typeName", map2.get("type_name"));
				resultMap.put("manufacturer", map2.get("manufacturer"));
				resultMap.put("model", map2.get("model"));
				resultMap.put("power", map2.get("power"));
				resultMap.put("efficiency", map2.get("nominal_conversion_efficiency"));
				resultMap.put("channels", map2.get("mppt_channels"));
				resultMap.put("max", map2.get("max_input_channels"));
				resultMap.put("photo", map2.get("photo_path"));
				resultMap.put("status", map2.get("valid"));
				resultList.add(resultMap);
			}
		}
		Map<String, Object> reMap = new HashMap<String,Object>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal",count);
		reMap.put("recordsFiltered", count);
		reMap.put("data", resultList);
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(reMap);
		return msg;
	}
	@Override
	public MessageBean getEquipmentBasic(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		String deviceId = map.get("deviceId")+"";
		Map inverterMap = inverterMapper.getEquipmentBasic(deviceId);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("id", inverterMap.get("id"));
		resultMap.put("modelIdentity", inverterMap.get("model_identity"));
		resultMap.put("typeName", inverterMap.get("type_name"));
		resultMap.put("manufacturer", inverterMap.get("manufacturer"));
		resultMap.put("model", inverterMap.get("model"));
		resultMap.put("power", inverterMap.get("power"));
		resultMap.put("efficiency", inverterMap.get("nominal_conversion_efficiency"));
		resultMap.put("channels", inverterMap.get("mppt_channels"));
		resultMap.put("max", inverterMap.get("max_input_channels"));
		resultMap.put("photo", inverterMap.get("photo_path"));
		//组装前台数据
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateStatus(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String status = map.get("status")+"";
		String deviceId = map.get("deviceId")+"";
		String deviceType = map.get("deviceType")+"";
		if(deviceType.equals("1")){
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("status", status);
			paramMap.put("deviceId", deviceId);
			int result = inverterMapper.updateStatus(paramMap);
			if(result != 0){
				if(status.equals("0")){
					logger.info(StringUtil.appendStr(" 停用逆变器成功! 操作者:",logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.DISABLE_SUCCUESS);
				}else if(status.equals("1")){
					logger.info(StringUtil.appendStr(" 启用逆变器成功! 操作者:",logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ENABLE_SUCCUESS);
				}
			}else{
				logger.error(StringUtil.appendStr(" 修改逆变器状态失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPDATE_FAILED);
			}
		}else if(deviceType.equals("2")){
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("status", status);
			paramMap.put("deviceId", deviceId);
			int result = stringInverterMapper.updateStatus(paramMap);
			if(result != 0){
				if(status.equals("0")){
					logger.info(StringUtil.appendStr(" 停用逆变器成功! 操作者:",logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.DISABLE_SUCCUESS);
				}else if(status.equals("1")){
					logger.info(StringUtil.appendStr(" 启用逆变器成功! 操作者:",logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ENABLE_SUCCUESS);
				}
			}else{
				logger.error(StringUtil.appendStr(" 修改逆变器状态失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPDATE_FAILED);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateEquipmentBasic(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String deviceType = String.valueOf(map.get("deviceType"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", map.get("deviceId"));
		paramMap.put("modelIdentity", map.get("modelIdentity"));
		paramMap.put("manufacturer", map.get("manufacturer"));
		paramMap.put("model", map.get("model"));
		paramMap.put("power", map.get("power"));
		paramMap.put("efficiency", map.get("efficiency")+"");
		paramMap.put("channels", map.get("channels"));
		paramMap.put("max", map.get("max"));
		if(deviceType.equals("1")){
			int result = inverterMapper.updateEquipmentBasic(paramMap);
			if(result != 0){
				logger.info(StringUtil.appendStr(" 修改逆变器数据成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPDATE_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 修改逆变器数据失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPDATE_FAILED);
			}
		}else if(deviceType.equals("2")){
			int result = stringInverterMapper.updateEquipmentBasic(paramMap);
			if(result != 0){
				logger.info(StringUtil.appendStr(" 修改逆变器数据成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPDATE_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 修改逆变器数据失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPDATE_FAILED);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateEquipmentPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		String imgUrl = request.getParameter("imgUrl");
		User u = session.getAttribute(request.getParameter("tokenId"));
		String deviceType = request.getParameter("deviceType");
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String deviceId = request.getParameter("deviceId");
		if(null != imgUrl && !"".equals(imgUrl) && !"null".equals(imgUrl)){
			//将原先的图片设置为空
			int result = 0;
			if(deviceType.equals("1")){
				result = inverterMapper.updatePhotoNull(deviceId);
			}else if(deviceType.equals("2")){
				result = stringInverterMapper.updatePhotoNull(deviceId);
			}
			if(result == 0){
				logger.error(StringUtil.appendStr(" 图片置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getEquipmentPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" 图片文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" 图片文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getEquipmentPhotoUrl();
		String iconRequestUrl = configParam.getEquipmentPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("deviceId", deviceId);
			parameterMap.put("url", buffer.toString());
			int re = 0;
			if(deviceType.equals("1")){
				DeviceDetailCentralInverter inverter = inverterMapper.getInverterById(deviceId);
				if(Util.isNotBlank(inverter.getPhotoPath())){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传一张图片，请核实后上传");
					return msg;
				}
				re = inverterMapper.updatePhoto(parameterMap);
			}else if(deviceType.equals("2")){
				DeviceDetailStringInverter stringInverter = stringInverterMapper.getStringInverterById(deviceId);
				if(Util.isNotBlank(stringInverter.getPhotoPath())){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传一张图片，请核实后上传");
					return msg;
				}
				re = stringInverterMapper.updatePhoto(parameterMap);
			}
			if(re != 0){
				logger.info(StringUtil.appendStr(" 图片上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片修改成功");
			}else{
				logger.error(StringUtil.appendStr(" 图片上传失败! 操作者:",logUserName));
				throw new ServiceException("图片修改失败");
			}
		}
		return msg;
	}
	@Override
	public void inverterExport(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("manufacturer", request.getParameter("manufacturer"));
		paramMap.put("model", request.getParameter("model"));
		paramMap.put("status", request.getParameter("status"));
		List<Map<String,Object>> list = inverterMapper.getInverterInfo(paramMap);
		List<Map<String,Object>> stringList = stringInverterMapper.getInverterInfo(paramMap);
		list.addAll(stringList);
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("设备型号标识");
    	titleList.add("设备类型");
    	titleList.add("设备厂商");
    	titleList.add("设备型号");
    	titleList.add("功率");
    	titleList.add("标称转换效率");
    	titleList.add("MTTP路数");
    	titleList.add("最大输入路数");
    	String fileName="逆变器设备数据";
    	paramList.add("model_identity");
    	paramList.add("type_name");
    	paramList.add("manufacturer");
    	paramList.add("model");
    	paramList.add("power");
    	paramList.add("nominal_conversion_efficiency");
    	paramList.add("mppt_channels");
    	paramList.add("max_input_channels");
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Override
	public void interverExcelTemplate(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("设备型号标识");
    	titleList.add("设备类型");
    	titleList.add("设备厂商");
    	titleList.add("设备型号");
    	titleList.add("功率");
    	titleList.add("标称转换效率");
    	titleList.add("MTTP路数");
    	titleList.add("最大输入路数");
    	String fileName="逆变器数据模板";
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean readInterverExcel(HttpServletRequest request, Session session)
			throws ServiceException, SessionException, SessionTimeoutException,BiffException, IOException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		String pathDir = configParam.getIconURL();
		List<String> path=DownloadUtils.uploadExcel(request, pathDir);
		String url = "";
		for (String string : path) {
			url+=string;
		}
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		ReadExcel excel = new ReadExcel(url);
		excel.readExcel();
		List list = excel.getList();
		File destFile = new File(url);
		if (destFile.exists()) {
			destFile.delete();
			logger.info(StringUtil.appendStr(" excel删除成功! 操作者:",logUserName));
		}
		//存放集中式逆变器
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(10);
		//存放组串式逆变器
		List<Map<String, Object>> paramStringList = new ArrayList<Map<String,Object>>(10);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> paramMap = new HashMap<String,Object>();
			String[] string = (String[]) list.get(i);
			paramMap.put("modelIdentity", string[0]);
			String deviceType = string[1];
//			if(string[1].equals("集中式逆变器")){
//				paramMap.put("deviceType", "1");
//			}else if(string[1].equals("组串式逆变器")){
//				paramMap.put("deviceType", "2");
//			}else{
//				paramMap.put("deviceType", "1");
//			}
			paramMap.put("manufacturer", string[2]);
			paramMap.put("model", string[3]);
			paramMap.put("power", string[4]);
			paramMap.put("nominalConversionEfficiency", string[5]);
			paramMap.put("mpptChannels", string[6]);
			paramMap.put("maxInputChannels", string[7]);
			paramMap.put("valid", "0");
			if(deviceType.equals("集中式逆变器")){
				paramMap.put("deviceType", "1");
				paramList.add(paramMap);
			}else if(deviceType.equals("组串式逆变器")){
				paramMap.put("deviceType", "2");
				paramStringList.add(paramMap);
			}else{
				throw new ServiceException("设备类型填写有误");
			}
		}
		try {
			int result = inverterMapper.insertInverter(paramList);
			result = stringInverterMapper.insertInverter(paramStringList);
			if(result != 0){
				logger.info(StringUtil.appendStr(" 逆变器数据导入成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(Msg.ADD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 逆变器数据导入失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.ADD_FAILED);
			}
		} catch (Exception e) {
			logger.error(StringUtil.appendStr(" 逆变器数据导入失败! 操作者:",logUserName));
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("导入数据格式有误，请注意检查");
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean insertInterver(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		Map<String, String> paramMap = new HashMap<String,String>();
		String deviceType = String.valueOf(map.get("deviceType"));
		DeviceDetailCentralInverter inverter = new DeviceDetailCentralInverter();
		inverter.setDeviceType(Integer.valueOf(deviceType));
		inverter.setModelIdentity(String.valueOf(map.get("modelIdentity")));
		inverter.setManufacturer(String.valueOf(map.get("manufacturer")));
		inverter.setPower(String.valueOf(map.get("power")));
		inverter.setMpptChannels(Integer.valueOf(map.get("channels")+""));
		inverter.setModel(String.valueOf(map.get("model")));
		inverter.setNominalConversionEfficiency(String.valueOf(map.get("efficiency")));
		inverter.setMaxInputChannels(Integer.valueOf(map.get("max")+""));
		inverter.setValid(String.valueOf(map.get("status")));
		int result = 0;
		if("1".equals(deviceType)){
			result = inverterMapper.insertInverterByMap(inverter);
		}else if("2".equals(deviceType)){
			result = stringInverterMapper.insertInverterByMap(inverter);
		}
		Map<String, String> resultMap = new HashMap<String,String>(1);
		resultMap.put("deviceId", String.valueOf(inverter.getId()));
		if(result != 0){
			logger.info(StringUtil.appendStr(" 新增逆变器数据成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.ADD_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr(" 新增逆变器数据失败! 操作者:",logUserName));
			throw new ServiceException(Msg.ADD_FAILED);
		}
		return msg;
	}
	@Override
	public MessageBean listEquipmentSub(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("length")+"");
		// 获取起始位置
		Integer alarm_page_num = Integer.parseInt(map.get("start")+"");
		// 获取状态
		String status = (String) map.get("status");
		// 获取设备厂家
		String manufacturer = (String) map.get("manufacturer");
		// 获取设备型号
		String model = (String) map.get("model");
		List resultList = new ArrayList();
		Map<String, Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("min", alarm_page_num);
		parameterMap.put("max", each_page_num);
		parameterMap.put("status", status);
		parameterMap.put("manufacturer", manufacturer);
		parameterMap.put("model", model);
		int count = subassemblyMapper.countSubassembly(parameterMap);
		List<Map> subassemblyList = subassemblyMapper.listSubassembly(parameterMap);
		if(Util.isNotBlank(subassemblyList)){
			for (Map map2 : subassemblyList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", map2.get("id"));
				resultMap.put("modelIdentity", map2.get("model_identity"));
				resultMap.put("manufacturer", map2.get("manufacturer"));
				resultMap.put("model", map2.get("model"));
				resultMap.put("subassemblyType", map2.get("subassembly_type"));
				resultMap.put("power", map2.get("power"));
				resultMap.put("efficiency", map2.get("efficiency"));
				resultMap.put("voc", map2.get("voc"));
				resultMap.put("isc", map2.get("isc"));
				resultMap.put("vm", map2.get("vm"));
				resultMap.put("im", map2.get("im"));
				resultMap.put("ff", map2.get("ff"));
				resultMap.put("pmaxTemperatureCoefficient", map2.get("pmax_temperature_coefficient"));
				resultMap.put("vocTemperatureCoefficient", map2.get("voc_temperature_coefficient"));
				resultMap.put("iscTemperatureCoefficient", map2.get("isc_temperature_coefficient"));
				resultMap.put("workingTemperature", map2.get("working_temperature"));
				resultMap.put("photo", map2.get("photo_path"));
				resultMap.put("status", map2.get("valid"));
				resultList.add(resultMap);
			}
		}
		Map<String, Object> reMap = new HashMap<String,Object>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal",count);
		reMap.put("recordsFiltered", count);
		reMap.put("data", resultList);
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(reMap);
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateSubStatus(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String status = map.get("status")+"";
		String deviceId = map.get("deviceId")+"";
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", status);
		paramMap.put("deviceId", deviceId);
		int result = subassemblyMapper.updateSubStatus(paramMap);
		if(result != 0){
			if(status.equals("0")){
				logger.info(StringUtil.appendStr(" 停用组件成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DISABLE_SUCCUESS);
			}else if(status.equals("1")){
				logger.info(StringUtil.appendStr(" 启用组件成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.ENABLE_SUCCUESS);
			}
		}else{
			logger.error(StringUtil.appendStr(" 修改组件状态失败! 操作者:",logUserName));
			throw new ServiceException(Msg.UPDATE_SUCCUESS);
		}
		return msg;
	}
	@Override
	public MessageBean getSubBasic(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		String deviceId = map.get("deviceId")+"";
		Map subMap = subassemblyMapper.getSubBasic(deviceId);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("id", subMap.get("id"));
		resultMap.put("modelIdentity", subMap.get("model_identity"));
		resultMap.put("manufacturer", subMap.get("manufacturer"));
		resultMap.put("model", subMap.get("model"));
		resultMap.put("subassemblyType", subMap.get("subassembly_type"));
		resultMap.put("power", subMap.get("power"));
		resultMap.put("efficiency", subMap.get("efficiency"));
		resultMap.put("voc", subMap.get("voc"));
		resultMap.put("isc", subMap.get("isc"));
		resultMap.put("vm", subMap.get("vm"));
		resultMap.put("im", subMap.get("im"));
		resultMap.put("ff", subMap.get("ff"));
		resultMap.put("pmaxTemperatureCoefficient", subMap.get("pmax_temperature_coefficient"));
		resultMap.put("vocTemperatureCoefficient", subMap.get("voc_temperature_coefficient"));
		resultMap.put("iscTemperatureCoefficient", subMap.get("isc_temperature_coefficient"));
		resultMap.put("workingTemperature", subMap.get("working_temperature"));
		resultMap.put("photo", subMap.get("photo_path"));
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateSubBasic(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", map.get("id"));
		paramMap.put("modelIdentity", map.get("modelIdentity"));
		paramMap.put("manufacturer", map.get("manufacturer"));
		paramMap.put("model", map.get("model"));
		paramMap.put("power", map.get("power"));
		paramMap.put("efficiency", map.get("efficiency")+"%");
		paramMap.put("voc", map.get("voc"));
		paramMap.put("isc", map.get("isc"));
		paramMap.put("vm", map.get("vm"));
		paramMap.put("im", map.get("im"));
		paramMap.put("ff", map.get("ff"));
		paramMap.put("pmaxTemperatureCoefficient", map.get("pmaxTemperatureCoefficient"));
		paramMap.put("vocTemperatureCoefficient", map.get("vocTemperatureCoefficient"));
		paramMap.put("iscTemperatureCoefficient", map.get("iscTemperatureCoefficient"));
		paramMap.put("workingTemperature", map.get("workingTemperature"));
		int result = subassemblyMapper.updateSubBasic(paramMap);
		if(result != 0){
			logger.info(StringUtil.appendStr(" 修改组件数据成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.UPDATE_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr(" 修改组件数据失败! 操作者:",logUserName));
			throw new ServiceException(Msg.UPDATE_SUCCUESS);
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateSubPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		String imgUrl = request.getParameter("imgUrl");
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String deviceId = request.getParameter("deviceId");
		if(null != imgUrl && !"".equals(imgUrl) && !"null".equals(imgUrl)){
			//将原先的图片设置为空
			int result = subassemblyMapper.updatePhotoNull(deviceId);
			if(result == 0){
				logger.error(StringUtil.appendStr(" 图片置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getEquipmentPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" 图片文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" 图片文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getEquipmentPhotoUrl();
		String iconRequestUrl = configParam.getEquipmentPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			DeviceDetailModule sub = subassemblyMapper.getSubById(deviceId);
			if(Util.isNotBlank(sub.getPhotoPath())){
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg("只能上传一张图片，请核实后上传");
				return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("deviceId", deviceId);
			parameterMap.put("url", buffer.toString());
			int re = subassemblyMapper.updatePhoto(parameterMap);
			if(re != 0){
				logger.info(StringUtil.appendStr(" 图片上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片修改成功");
			}else{
				logger.error(StringUtil.appendStr(" 图片上传失败! 操作者:",logUserName));
				throw new ServiceException("图片修改失败");
			}
		}
		return msg;
	}
	@Override
	public void subExport(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("manufacturer", request.getParameter("manufacturer"));
		paramMap.put("model", request.getParameter("model"));
		paramMap.put("status", request.getParameter("status"));
		List<Map<String,Object>> list = subassemblyMapper.getSubInfo(paramMap);
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("型号标识");
    	titleList.add("制造商");
    	titleList.add("组件型号");
    	titleList.add("组件类型");
    	titleList.add("功率[Wp]");
    	titleList.add("效率[%]");
    	titleList.add("Voc[V]");
    	titleList.add("Isc[A]");
    	titleList.add("Vm[V]");
    	titleList.add("Im[A]");
    	titleList.add("FF[%]");
    	titleList.add("Pmax温度系数[%]");
    	titleList.add("Voc温度系数[%]");
    	titleList.add("Isc温度系数[%]");
    	titleList.add("工作温度[oC]");
    	String fileName="组件设备数据";
    	paramList.add("model_identity");
    	paramList.add("manufacturer");
    	paramList.add("model");
    	paramList.add("subassembly_type");
    	paramList.add("power");
    	paramList.add("efficiency");
    	paramList.add("voc");
    	paramList.add("isc");
    	paramList.add("vm");
    	paramList.add("im");
    	paramList.add("ff");
    	paramList.add("pmax_temperature_coefficient");
    	paramList.add("voc_temperature_coefficient");
    	paramList.add("isc_temperature_coefficient");
    	paramList.add("working_temperature");
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Override
	public void subExcelTemplate(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();;
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("型号标识");
    	titleList.add("制造商");
    	titleList.add("组件型号");
    	titleList.add("组件类型");
    	titleList.add("功率[Wp]");
    	titleList.add("效率[%]");
    	titleList.add("Voc[V]");
    	titleList.add("Isc[A]");
    	titleList.add("Vm[V]");
    	titleList.add("Im[A]");
    	titleList.add("FF[%]");
    	titleList.add("Pmax温度系数[%]");
    	titleList.add("Voc温度系数[%]");
    	titleList.add("Isc温度系数[%]");
    	titleList.add("工作温度[oC]");
    	String fileName="组件数据模板";
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean readSubExcel(HttpServletRequest request, Session session)
			throws ServiceException, SessionException, SessionTimeoutException, BiffException, IOException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String pathDir = configParam.getIconURL();
		List<String> path=DownloadUtils.uploadExcel(request, pathDir);
		String url = "";
		for (String string : path) {
			url+=string;
		}
		ReadExcel excel = new ReadExcel(url);
		excel.readExcel();
		List list = excel.getList();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(10);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> paramMap = new HashMap<String,Object>();
			String[] string = (String[]) list.get(i);
			paramMap.put("modelIdentity", string[0]);
			paramMap.put("deviceType", "7");
			paramMap.put("manufacturer", string[1]);
			paramMap.put("model", string[2]);
			paramMap.put("subassemblyType", string[3]);
			paramMap.put("power", string[4]);
			paramMap.put("efficiency", string[5]);
			paramMap.put("voc", string[6]);
			paramMap.put("isc", string[7]);
			paramMap.put("vm", string[8]);
			paramMap.put("im", string[9]);
			paramMap.put("ff", string[10]);
			paramMap.put("pmaxTemperatureCoefficient", string[11]);
			paramMap.put("vocTemperatureCoefficient", string[12]);
			paramMap.put("iscTemperatureCoefficient", string[13]);
			paramMap.put("workingTemperature", string[14]);
			paramMap.put("valid", "0");
			paramList.add(paramMap);
		}
		try {
			int result = subassemblyMapper.insertSub(paramList);
			if(result != 0){
				File destFile = new File(url);
				if (destFile.exists()) {
					destFile.delete();
					logger.info(StringUtil.appendStr(" excel删除成功! 操作者:",logUserName));
				}
				logger.info(StringUtil.appendStr(" 组件数据导入成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(Msg.ADD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 组件数据导入失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.ADD_FAILED);
			}
		} catch (Exception e) {
			logger.error(StringUtil.appendStr(" 组件数据导入失败! 操作者:",logUserName));
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("导入数据格式有误，请注意检查");
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean insertSub(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		DeviceDetailModule sub = new DeviceDetailModule();
		sub.setModelIdentity(String.valueOf(map.get("modelIdentity")));
		sub.setManufacturer(String.valueOf(map.get("manufacturer")));
		sub.setModel(String.valueOf(map.get("model")));
		sub.setDeviceType(7);
		sub.setSubassemblyType(String.valueOf(map.get("subassemblyType")));
		sub.setPower(Float.valueOf(map.get("power")+""));
		sub.setEfficiency(String.valueOf(map.get("efficiency")));
		sub.setVoc(Float.valueOf(map.get("voc")+""));
		sub.setIsc(Float.valueOf(map.get("isc")+""));
		sub.setVm(Float.valueOf(map.get("vm")+""));
		sub.setIm(Float.valueOf(map.get("im")+""));
		sub.setFf(Float.valueOf(map.get("ff")+""));
		sub.setPmaxTemperatureCoefficient(Float.valueOf(map.get("pmaxTemperatureCoefficient")+""));
		sub.setVocTemperatureCoefficient(Float.valueOf(map.get("vocTemperatureCoefficient")+""));
		sub.setIscTemperatureCoefficient(Float.valueOf(map.get("iscTemperatureCoefficient")+""));
		sub.setWorkingTemperature(String.valueOf(map.get("workingTemperature")));
		sub.setValid(String.valueOf(map.get("status")));
		int result = subassemblyMapper.insertSubByBean(sub);
		Map<String, String> resultMap = new HashMap<String,String>(1);
		resultMap.put("deviceId", String.valueOf(sub.getId()));
		if(result != 0){
			logger.info(StringUtil.appendStr(" 新增组件数据成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.ADD_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr(" 新增组件数据失败! 操作者:",logUserName));
			throw new ServiceException(Msg.ADD_FAILED);
		}
		return msg;
	}
	@Override
	public MessageBean listCamera(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		// 获取每页显示几条数据
		Integer each_page_num = Integer.parseInt(map.get("length")+"");
		// 获取起始位置
		Integer alarm_page_num = Integer.parseInt(map.get("start")+"");
		// 获取状态
		String status = (String) map.get("status");
		// 获取设备厂家
		String manufacturer = (String) map.get("manufacturer");
		// 获取设备型号
		String model = (String) map.get("model");
		List resultList = new ArrayList();
		Map<String, Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("min", alarm_page_num);
		parameterMap.put("max", each_page_num);
		parameterMap.put("status", status);
		parameterMap.put("manufacturer", manufacturer);
		parameterMap.put("model", model);
		int count = cameraModelMapper.countCamera(parameterMap);
		List<DeviceDetailCamera> camera = cameraModelMapper.listCamera(parameterMap);
		if(Util.isNotBlank(camera)){
			for (DeviceDetailCamera cameraModel : camera) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("id", cameraModel.getId());
				resultMap.put("modelIdentity", cameraModel.getModelIdentity());
				resultMap.put("manufacturer", cameraModel.getManufacture());
				resultMap.put("cameraModel", cameraModel.getModelName());
				resultMap.put("supportInfra", cameraModel.getSupportInfra());
				resultMap.put("guardLevel", cameraModel.getGuardLevel());
				resultMap.put("cameraResolution", cameraModel.getCameraResolution());
				resultMap.put("network", cameraModel.getNetwork());
				resultMap.put("maxResolution", cameraModel.getMaxResolution());
				resultMap.put("inStorage", cameraModel.getInStorage());
				resultMap.put("cameraPhoto", cameraModel.getCameraPhoto());
				resultMap.put("status", cameraModel.getStatus());
				resultList.add(resultMap);
			}
		}
		Map<String, Object> reMap = new HashMap<String,Object>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal",count);
		reMap.put("recordsFiltered", count);
		reMap.put("data", resultList);
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(reMap);
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateCameraStatus(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String status = map.get("status")+"";
		String deviceId = map.get("deviceId")+"";
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", status);
		paramMap.put("deviceId", deviceId);
		int result = cameraModelMapper.updateStatus(paramMap);
		if(result != 0){
			if(status.equals("0")){
				logger.info(StringUtil.appendStr(" 停用摄像头成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DISABLE_SUCCUESS);
			}else if(status.equals("1")){
				logger.info(StringUtil.appendStr(" 启用摄像头成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.ENABLE_SUCCUESS);
			}
		}else{
			logger.error(StringUtil.appendStr(" 修改摄像头状态失败! 操作者:",logUserName));
			throw new ServiceException(Msg.UPDATE_SUCCUESS);
		}
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateCambasic(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", map.get("id"));
		paramMap.put("modelIdentity", map.get("modelIdentity"));
		paramMap.put("manufacturer", map.get("manufacturer"));
		paramMap.put("cameraModel", map.get("cameraModel"));
		paramMap.put("supportInfra", map.get("supportInfra"));
		paramMap.put("guardLevel", map.get("guardLevel")+"");
		paramMap.put("cameraResolution", map.get("cameraResolution"));
		paramMap.put("network", map.get("network"));
		paramMap.put("maxResolution", map.get("maxResolution"));
		paramMap.put("inStorage", map.get("inStorage"));
		int result = cameraModelMapper.updateEquipmentBasic(paramMap);
		if(result != 0){
			logger.info(StringUtil.appendStr(" 修改摄像头数据成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.UPDATE_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr(" 修改摄像头数据失败! 操作者:",logUserName));
			throw new ServiceException(Msg.UPDATE_SUCCUESS);
		}
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateCameraPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String deviceId = request.getParameter("deviceId");
		String imgUrl = request.getParameter("imgUrl");
		if(null != imgUrl && !"".equals(imgUrl) && !"null".equals(imgUrl)){
			//将用户原先的头像设置为空
			int result = cameraModelMapper.updatePhotoNull(deviceId);
			if(result == 0){
				logger.error(StringUtil.appendStr(" 图片置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getEquipmentPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" 图片文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" 图片文件删除失败! 操作者:",logUserName));
			}
		}
		String pathDir = configParam.getEquipmentPhotoUrl();
		String equipmentRequestUrl = configParam.getEquipmentPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,equipmentRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			DeviceDetailCamera camera = cameraModelMapper.getCameraById(deviceId);
			if(Util.isNotBlank(camera.getCameraPhoto())){
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg("只能上传一张图片，请核实后上传");
				return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("deviceId", deviceId);
			parameterMap.put("url", buffer.toString());
			int re = cameraModelMapper.insertCameraPhoto(parameterMap);
			if(re != 0){
				logger.info(StringUtil.appendStr(" 图片上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片修改成功");
			}else{
				logger.error(StringUtil.appendStr(" 图片上传失败! 操作者:",logUserName));
				throw new ServiceException("图片修改失败");
			}
		}
		return msg;
	}
	@Override
	public void cameraExport(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put("manufacturer", request.getParameter("manufacturer"));
		paramMap.put("model", request.getParameter("model"));
		paramMap.put("status", request.getParameter("status"));
		List<Map<String,Object>> list = cameraModelMapper.getCameraInfo(paramMap);
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("型号标识");
    	titleList.add("厂商");
    	titleList.add("型号");
    	titleList.add("是否支持红外");
    	titleList.add("防护等级");
    	titleList.add("像素");
    	titleList.add("网络功能");
    	titleList.add("最高分辨率");
    	titleList.add("内存容量");
    	String fileName="摄像头设备数据";
    	paramList.add("modelIdentity");
    	paramList.add("manufacture");
    	paramList.add("modelName");
    	paramList.add("supportInfra");
    	paramList.add("guardLevel");
    	paramList.add("cameraResolution");
    	paramList.add("network");
    	paramList.add("maxResolution");
    	paramList.add("inStorage");
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Override
	public void cameraExcelTemplate(HttpServletRequest request, HttpServletResponse response, Session session)
			throws SessionException, SessionTimeoutException, ServiceException, ServletException, IOException {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List titleList=new ArrayList<>();
    	List paramList=new ArrayList<>();
    	titleList.add("型号标识");
    	titleList.add("厂商");
    	titleList.add("型号");
    	titleList.add("是否支持红外");
    	titleList.add("防护等级");
    	titleList.add("像素");
    	titleList.add("网络功能");
    	titleList.add("最高分辨率");
    	titleList.add("内存容量");
    	String fileName="摄像头数据模板";
    	DownloadUtils.exportExcel(request, response, list, titleList, fileName,paramList);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean readCameraExcel(HttpServletRequest request, Session session)
			throws ServiceException, SessionException, SessionTimeoutException, BiffException, IOException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String pathDir = configParam.getIconURL();
		List<String> path=DownloadUtils.uploadExcel(request, pathDir);
		String url = "";
		for (String string : path) {
			url+=string;
		}
		ReadExcel excel = new ReadExcel(url);
		excel.readExcel();
		List list = excel.getList();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(10);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> paramMap = new HashMap<String,Object>();
			String[] string = (String[]) list.get(i);
			paramMap.put("modelIdentity", string[0]);
			paramMap.put("manufacture", string[1]);
			paramMap.put("modelName", string[2]);
			if(string[3].equals("支持")){
				paramMap.put("supportInfra", "1");
			}else{
				paramMap.put("supportInfra", "0");
			}
			paramMap.put("guardLevel", string[4]);
			paramMap.put("cameraResolution", string[5]);
			paramMap.put("network", string[6]);
			paramMap.put("maxResolution", string[7]);
			paramMap.put("inStorage", string[8]);
			paramMap.put("status", "1");
			paramMap.put("valid", "1");
			paramList.add(paramMap);
		}
		try {
			int result = cameraModelMapper.insertCamera(paramList);
			if(result != 0){
				File destFile = new File(url);
				if (destFile.exists()) {
					destFile.delete();
					logger.info(StringUtil.appendStr(" excel删除成功! 操作者:",logUserName));
				}
				logger.info(StringUtil.appendStr(" 摄像头数据导入成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setBody(Msg.ADD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 摄像头数据导入失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.ADD_FAILED);
			}
		} catch (Exception e) {
			logger.error(StringUtil.appendStr(" 摄像头数据导入失败! 操作者:",logUserName));
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("导入数据格式有误，请注意检查");
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean insertCamera(String str, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		DeviceDetailCamera camera = new DeviceDetailCamera();
		camera.setModelIdentity(String.valueOf(map.get("modelIdentity")));
		camera.setManufacture(String.valueOf(map.get("manufacturer")));
		camera.setModelName(String.valueOf(map.get("cameraModel")));
		camera.setSupportInfra(String.valueOf(map.get("supportInfra")));
		camera.setGuardLevel(String.valueOf(map.get("guardLevel")));
		camera.setCameraResolution(Integer.valueOf(map.get("cameraResolution")+""));
		camera.setNetwork(String.valueOf(map.get("network")));
		camera.setMaxResolution(String.valueOf(map.get("maxResolution")));
		camera.setInStorage(String.valueOf(map.get("inStorage")));
		camera.setStatus(String.valueOf(map.get("status")));
		camera.setValid("0");
		int result = cameraModelMapper.insertCameraByBean(camera);
		Map<String, String> resultMap = new HashMap<String,String>(1);
		resultMap.put("deviceId", String.valueOf(camera.getId()));
		if(result != 0){
			logger.info(StringUtil.appendStr(" 新增摄像头数据成功! 操作者:",logUserName));
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.ADD_SUCCUESS);
		}else{
			logger.error(StringUtil.appendStr(" 新增摄像头数据失败! 操作者:",logUserName));
			throw new ServiceException(Msg.ADD_FAILED);
		}
		return msg;
	}
}
