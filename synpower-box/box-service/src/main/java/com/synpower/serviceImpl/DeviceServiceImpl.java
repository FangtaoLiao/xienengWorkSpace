package com.synpower.serviceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSInput;

import com.alibaba.fastjson.JSONObject;
import com.synpower.bean.DeviceDetailCamera;
import com.synpower.bean.CollDevice;
import com.synpower.bean.CollJsonDevSet;
import com.synpower.bean.CollModel;
import com.synpower.bean.CollModelDetailMqtt;
import com.synpower.bean.CollectorMessageStructure;
import com.synpower.bean.DeviceDetailCentralInverter;
import com.synpower.bean.DeviceDetailCombinerBox;
import com.synpower.bean.DeviceDetailDataCollector;
import com.synpower.bean.DeviceDetailEnvMonitor;
import com.synpower.bean.DeviceDetailModule;
import com.synpower.bean.DeviceDetailStringInverter;
import com.synpower.bean.DeviceSeriesModuleDetail;
import com.synpower.bean.DeviceType;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysSerialPort;
import com.synpower.constant.DTC;
import com.synpower.dao.DeviceDetailCameraMapper;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.CollJsonDevSetMapper;
import com.synpower.dao.CollModelDetailMqttMapper;
import com.synpower.dao.CollModelMapper;
import com.synpower.dao.CollSignalLabelMapper;
import com.synpower.dao.CollectorMessageStructureMapper;
import com.synpower.dao.DeviceDetailCentralInverterMapper;
import com.synpower.dao.DeviceDetailCombinerBoxMapper;
import com.synpower.dao.DeviceDetailDataCollectorMapper;
import com.synpower.dao.DeviceDetailEnvMonitorMapper;
import com.synpower.dao.DeviceDetailModuleMapper;
import com.synpower.dao.DeviceDetailStringInverterMapper;
import com.synpower.dao.DeviceSeriesModuleDetailMapper;
import com.synpower.dao.DeviceTypeMapper;
import com.synpower.dao.FieldSignalGuidMappingMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysSerialPortMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.Result;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.DeviceService;
import com.synpower.util.IYkYtService;
import com.synpower.util.RabbitMessage;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;

@Service
public class DeviceServiceImpl implements DeviceService {
	private Logger logger = Logger.getLogger(DeviceServiceImpl.class);
	@Autowired
	private CollDeviceMapper deviceMapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private CollectorMessageStructureMapper collectorMessageStructureMapper;
	@Autowired
	private DeviceDetailDataCollectorMapper collectorMapper;
	@Autowired
	private CollModelMapper modelMapper;
	@Autowired
	private DeviceDetailCentralInverterMapper inverterMappper;
	@Autowired
	private DeviceTypeMapper typeMapper;
	@Autowired
	private DeviceDetailStringInverterMapper strInverterMapper;
	@Autowired
	private DeviceSeriesModuleDetailMapper moduleMapper;
	@Autowired
	private CollModelDetailMqttMapper mqttMapper;
	@Autowired
	private FieldSignalGuidMappingMapper guidMapper;
	@Autowired
	private CollSignalLabelMapper labelMapper;
	@Autowired
	private DeviceDetailDataCollectorMapper collMapper;
	@Autowired
	private DeviceDetailCameraMapper cameraMapper;
	@Autowired
	private CollectorMessageStructureMapper cmsMapper;
	@Autowired
	private SystemCache systemCache;
	@Autowired
	private DeviceDetailModuleMapper deviceModuleMapper;
	@Autowired
	private CollJsonDevSetMapper collJsonDevSetMapper;
	@Autowired
	private SysSerialPortMapper sysSerialPortMapper;
	@Autowired
	private DeviceDetailCombinerBoxMapper deviceDetailCombinerBoxMapper;
	@Autowired
	private IYkYtService iYkYtService;
	@Autowired
	private DeviceDetailEnvMonitorMapper deviceDetailEnvMonitorMapper;

	@Override
	public MessageBean deviceService(Map<String, Object> map, Session session)
			throws SessionException, ServiceException {
		MessageBean msg = new MessageBean();
		String pId = map.containsKey("pId") ? map.get("pId") + "" : null;
		String order = map.containsKey("order") ? map.get("order") + "" : null;
		int count = deviceMapper.countDeviceByPid(pId);
		Map<String, Object> paramMap = Util.getPageMaxRange(map, count);
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		paramMap.put("min", start);
		paramMap.put("max", length);
		paramMap.put("pId", pId);
		paramMap.put("order", order);
		List<CollDevice> list = deviceMapper.getDevicesByPid(paramMap);
		Result result = new Result();
		result.setRows(list.toArray());
		result.setTotal(count);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(result);
		return msg;
	}

	@Override
	public MessageBean getCollectors(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String plantId = map.get("plantId") + "";
		String oriOrgId = map.get("orgId") + "";
		// 当前电站所属组织
		String deviceName = map.get("deviceName") + "";
		String status = map.get("status") + "";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("types", new Integer[] { 5 });
		// 每页条数、起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		paramMap.put("length", length);
		paramMap.put("offset", start);
		paramMap.put("deviceName", deviceName);
		paramMap.put("status", status);
		List<CollDevice> devices = null;
		Integer totalCount = 0;
		if (Util.isNotBlank(plantId)) {
			// 根据电站id得到所有逆变器集合
			paramMap.put("pid", plantId);
			devices = deviceMapper.getPlantDevice(paramMap);
			totalCount = deviceMapper.getTotalCountPlant(paramMap);

		} else {
			// 根据组织id得到逆变器集合
			List<SysOrg> orgList = orgMapper.getAllOrg();
			List<String> reList = new ArrayList<>();
			reList.add(oriOrgId + "");
			reList = ServiceUtil.getTree(orgList, oriOrgId + "", reList);
			paramMap.put("reList", reList);
			devices = deviceMapper.getOrgDevice(paramMap);
			totalCount = deviceMapper.getTotalCountPage(paramMap);
		}

		/*
		 * List<CollDevice>devices=deviceMapper.getOrgDevice(paramMap); Integer
		 * totalCount = deviceMapper.getTotalCountPage(paramMap);
		 */

		List<Map<String, Object>> resultList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (Util.isNotBlank(devices)) {
			for (int i = 0; i < devices.size(); i++) {
				CollDevice device = devices.get(i);
				Map<String, Object> tempMap = new HashMap<>();
				Integer deviceModelId = device.getDeviceModelId();
				String model = collectorMapper.getModel(deviceModelId);
				tempMap.put("deviceId", String.valueOf(device.getId()));
				tempMap.put("deviceName", device.getDeviceName());
				String connTimes = sdf.format(device.getConnTime());
				tempMap.put("connTime", device.getConnTime() == null ? "NaN" : connTimes);
				tempMap.put("modelId", model == null ? "NaN" : model);
				tempMap.put("modelRealId", deviceModelId);
				tempMap.put("status", device.getDeviceStatus());
				tempMap.put("sn", device.getDeviceSn());
				Integer plantId2 = device.getPlantId();
				// 该数采所属电站id
				PlantInfo plant2 = null;
				if (plantId2 != null) {
					plant2 = plantInfoMapper.getPlantById(String.valueOf(plantId2));
					if (plant2 == null) {
						tempMap.put("plantId", "");
						tempMap.put("plantName", "未分配");
					} else {
						tempMap.put("plantId", plantId2);
						tempMap.put("plantName", plant2.getPlantName());
					}
				} else {
					tempMap.put("plantId", "");
					tempMap.put("plantName", "未分配");
				}
				// tempMap.put("plantName", plant2==null?"未分配":plant2.getPlantName());
				tempMap.put("orgId", device.getOrgId());
				tempMap.put("orgName", orgMapper.getOrgName(String.valueOf(device.getOrgId())).getOrgName());
				resultList.add(tempMap);
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalCount);
		reMap.put("recordsFiltered", totalCount);
		reMap.put("data", resultList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getInventerNum(String jsonData, Session session) throws ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, String> map = Util.parseURLCode(jsonData);
		String qrcode = map.get("sn");
		// serviceType：0为新增；1为修改
		String serviceType = String.valueOf(map.get("serviceType"));
		Map<String, Object> resultMap = new HashMap<>(2);
		if ("0".equals(serviceType)) {
			String version = String.valueOf(qrcode.charAt(3));
			if (version.equals("1")) {
				CollDevice device = deviceMapper.getCollectBySn(qrcode);
				if (device == null) {
					throw new ServiceException(Msg.NOT_FIND_DEVICE);
				} else {
					if (!StringUtil.isBlank(String.valueOf(device.getPlantId()))) {
						throw new ServiceException(Msg.UNALE_DEVICE);
					} else {
						map.clear();
						resultMap.put("collName", device.getDeviceName());
						resultMap.put("num", String.valueOf(deviceMapper.getCollectInveter(qrcode)));
						resultMap.put("id", device.getId());
						msg.setBody(resultMap);
						msg.setCode(Header.STATUS_SUCESS);
					}
				}
			} else if (version.equals("2")) {
				// 根据sn号查询数采的具体信息
				CollDevice device = deviceMapper.getCollectBySn(qrcode);
				if (device == null) {
					throw new ServiceException(Msg.NOT_FIND_DEVICE);
				} else {
					if (!StringUtil.isBlank(String.valueOf(device.getPlantId()))) {
						throw new ServiceException(Msg.UNALE_DEVICE);
					} else {
						map.clear();
						resultMap.put("collName", device.getDeviceName());
						resultMap.put("id", device.getId());
						resultMap.put("num", device.getMaxNum());
						List<Object> resultList = new ArrayList<Object>();
//						List<SysSerialPort> sysSerialPortList = sysSerialPortMapper.getPortBycollId(device.getId());
						// 端口id由于业务需求暂时写死数据
						String[] port = { "1", "2" };
						for (int i = 0, size = port.length; i < size; i++) {
							List<Object> collDeviceList = new ArrayList<Object>();
							Map<String, Object> collDeviceMap = new HashMap<String, Object>();
//							SysSerialPort port = sysSerialPortList.get(i);
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("comId", port[i]);
							paramMap.put("collId", device.getId() + "");
							List<CollJsonDevSet> collJsonDevSetList = collJsonDevSetMapper
									.getJsonDevSetByComId(paramMap);
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
						resultMap.put("serialPort", resultList);
						msg.setBody(resultMap);
						msg.setCode(Header.STATUS_SUCESS);
					}
				}
			}
		} else if ("1".equals(serviceType)) {
			map.clear();
			CollDevice device = deviceMapper.getCollectBySn(qrcode);
			if (device == null) {
				throw new ServiceException(Msg.NOT_FIND_DEVICE);
			} else {
				resultMap.put("num", String.valueOf(deviceMapper.getCollectInveter(qrcode)));
				resultMap.put("id", device.getId());
				msg.setBody(resultMap);
				msg.setCode(Header.STATUS_SUCESS);
			}
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateDeviceNew(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = (String) map.get("tokenId");
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 获取数采id
		String collId = map.get("id") + "";
		// 获取要删除的设备id集合
		List deleteList = (List) map.get("deletes");
		if (Util.isNotBlank(deleteList)) {
			// 删除数采与下连设备间的关系
			int result = collJsonDevSetMapper.updateDevices(deleteList);
			if (result > 0) {
				logger.info(StringUtil.appendStr("删除数采与设备关系成功! 数采id:", collId, "设备id:", deleteList.toString(), "操作者:",
						logUserName));
				// 删除数采的下连设备
				int deviceResult = deviceMapper.delDevices(deleteList);
				if (deviceResult > 0) {
					logger.info(StringUtil.appendStr("删除设备成功! 设备id:", deleteList.toString(), "操作者:", logUserName));
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.DELETE_SUCCUESS);
				} else {
					logger.error(StringUtil.appendStr("删除设备失败! 设备id:", deleteList.toString(), "操作者:", logUserName));
					throw new ServiceException(Msg.DELETE_FAILED);
				}
			} else {
				logger.error(StringUtil.appendStr("删除数采与设备关系失败! 数采id:", collId, "设备id:", deleteList.toString(), "操作者:",
						logUserName));
				throw new ServiceException(Msg.DELETE_FAILED);
			}
		}
		// 获取数采名称
		String collName = map.get("collName") + "";
		// 获取电站id
		String plantId = map.get("plantId") + "";
		// 获取数采的下连设备
		List<Map<String, List>> serialPort = (List<Map<String, List>>) map.get("serialPort");
		// updateType 0:新增;1:修改
		String updateType = map.get("updateType") + "";
		if ("0".equals(updateType)) {
			// 激活数采和修改数采信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("plantId", plantId);
			paramMap.put("deviceId", collId);
			paramMap.put("valid", "0");
			paramMap.put("collName", collName);
			int status = deviceMapper.updateCollectValidAndName(paramMap);
			if (status > 0) {
				logger.info(StringUtil.appendStr("激活数采成功! 数采id:", collId, "操作者:", logUserName));
				for (Map<String, List> map2 : serialPort) {
					List<Map<String, Object>> datas = map2.get("datas");
					// 获取串口id
					String comId = map2.get("com_id") + "";
					for (Map<String, Object> map3 : datas) {
						// 获得下连设备的id
						String deviceId = map3.get("id") + "";
						// 1、如果deviceId为不为空代表：修改设备信息；
						// 2、如果deviceId为空代表：新增设备信息；
						if (Util.isNotBlank(deviceId)) {
							paramMap.clear();
							paramMap.put("deviceId", deviceId);
							paramMap.put("valid", "0");
							paramMap.put("plantId", plantId);
							paramMap.put("devName", map3.get("devName") + "");
							paramMap.put("modelId", map3.get("model_id") + "");
							paramMap.put("typeId", map3.get("type_id") + "");
							// 激活下连设备并修改设备信息
							status = deviceMapper.updateCollectorDeviceInfo(paramMap);
							if (status > 0) {
								logger.info(
										StringUtil.appendStr("激活设备和修改设备信息成功! 设备id:", deviceId, "操作者:", logUserName));
								// 修改设备通讯地址
								paramMap.clear();
								paramMap.put("slaveId", map3.get("addr") + "");
								paramMap.put("deviceId", deviceId);
								status = collJsonDevSetMapper.updateSlaveId(paramMap);
								if (status > 0) {
									logger.info(
											StringUtil.appendStr("修改设备通讯地址成功! 设备id:", deviceId, "操作者:", logUserName));
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.UPDATE_SUCCUESS);
								} else {
									logger.error(
											StringUtil.appendStr("修改设备通讯地址失败! 设备id:", deviceId, "操作者:", logUserName));
									throw new ServiceException(Msg.UPDATE_FAILED);
								}
							} else {
								logger.error(
										StringUtil.appendStr("激活设备和修改设备信息失败! 设备id:", deviceId, "操作者:", logUserName));
								throw new ServiceException(Msg.REPORT_SET_FAILED);
							}
						} else {
							String modelId = map3.get("model_id") + "";
							String typeId = map3.get("type_id") + "";
							// 获取dataMode
							paramMap.clear();
							paramMap.put("deviceModelId", modelId);
							paramMap.put("typeId", typeId);
							int dataModeId = modelMapper.getDateModelId(paramMap);
							// 新增下连设备
							CollDevice device = new CollDevice();
							device.setDeviceName(String.valueOf(map3.get("devName")));
							device.setDeviceModelId(Integer.parseInt(String.valueOf(modelId)));
							device.setDeviceSn(String.valueOf(map3.get("deviceSn")));
							device.setDeviceType(Integer.parseInt(String.valueOf(typeId)));
							device.setConnTime(System.currentTimeMillis());
//							device.setOrgId(Integer.parseInt(String.valueOf(map3.get("orgId"))));
							device.setSupid(Integer.parseInt(collId));
							device.setDeviceValid("0");
							device.setDeviceStatus("1");
							device.setDataMode(Integer.parseInt(String.valueOf(dataModeId)));
							device.setConnProtocol(2);
							status = deviceMapper.addDevice(device);
							if (status > 0) {
								logger.info(StringUtil.appendStr("新增设备成功! 设备id:", deviceId, "操作者:", logUserName));
								// 新增设备和数采的关联关系
								CollJsonDevSet collJsonDevSet = new CollJsonDevSet();
								collJsonDevSet.setCollId(Integer.parseInt(collId));
								collJsonDevSet.setComId(comId);
								collJsonDevSet.setDevId(device.getId());
								collJsonDevSet.setSlaveId(map3.get("addr") + "");
								collJsonDevSet.setValid("1");
								status = collJsonDevSetMapper.insertCollJsonDevSet(collJsonDevSet);
								if (status > 0) {
									logger.info(StringUtil.appendStr("新增数采和设备间的关系成功! 设备id:", deviceId, "操作者:",
											logUserName));
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.UPDATE_SUCCUESS);
								} else {
									logger.error(StringUtil.appendStr("新增数采和设备间的关系失败! 设备id:", deviceId, "操作者:",
											logUserName));
									throw new ServiceException(Msg.ADD_FAILED);
								}
							} else {
								logger.error(StringUtil.appendStr("新增设备失败! 设备id:", deviceId, "操作者:", logUserName));
								throw new ServiceException(Msg.ADD_FAILED);
							}
						}
					}
				}
			} else {
				logger.error(StringUtil.appendStr("激活数采失败! 数采id:", collId, "操作者:", logUserName));
				throw new ServiceException(Msg.REPORT_SET_FAILED);
			}
		} else if ("1".equals(updateType)) {
			// 修改数采信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("plantId", plantId);
			paramMap.put("deviceId", collId);
			paramMap.put("collName", collName);
			int status = deviceMapper.updateCollectValid(paramMap);
			if (status > 0) {
				logger.info(StringUtil.appendStr("修改数采信息成功! 数采id:", collId, "操作者:", logUserName));
				for (Map<String, List> map2 : serialPort) {
					List<Map<String, Object>> datas = map2.get("datas");
					// 获取串口id
					String comId = map2.get("com_id") + "";
					for (Map<String, Object> map3 : datas) {
						// 获得下连设备的id
						String deviceId = map3.get("id") + "";
						// 1、如果deviceId为不为空代表：修改设备信息；
						// 2、如果deviceId为空代表：新增设备信息；
						if (Util.isNotBlank(deviceId)) {
							paramMap.clear();
							paramMap.put("deviceId", deviceId);
							paramMap.put("plantId", plantId);
							paramMap.put("devName", map3.get("devName") + "");
							paramMap.put("modelId", map3.get("model_id") + "");
							paramMap.put("typeId", map3.get("type_id") + "");
							// 激活下连设备并修改设备信息
							status = deviceMapper.updateDeviceInfo(paramMap);
							if (status > 0) {
								logger.info(StringUtil.appendStr("修改设备信息成功! 数采id:", collId, "操作者:", logUserName));
								// 修改设备通讯地址
								paramMap.clear();
								paramMap.put("slaveId", map3.get("addr") + "");
								paramMap.put("deviceId", deviceId);
								status = collJsonDevSetMapper.updateSlaveId(paramMap);
								if (status > 0) {
									logger.info(StringUtil.appendStr("修改设备信息成功! 设备id:", deviceId, "操作者:", logUserName));
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.UPDATE_SUCCUESS);
								} else {
									logger.error(
											StringUtil.appendStr("修改设备信息失败! 设备id:", deviceId, "操作者:", logUserName));
									throw new ServiceException(Msg.UPDATE_FAILED);
								}
							} else {
								logger.error(StringUtil.appendStr("修改设备信息失败! 数采id:", collId, "操作者:", logUserName));
								throw new ServiceException(Msg.UPDATE_DEVICE_FAILED);
							}
						} else {
							String modelId = map3.get("model_id") + "";
							String typeId = map3.get("type_id") + "";
							// 获取dataMode
							paramMap.clear();
							paramMap.put("deviceModelId", modelId);
							paramMap.put("typeId", typeId);
							int dataModeId = modelMapper.getDateModelId(paramMap);
							// 新增下连设备
							CollDevice device = new CollDevice();
							device.setDeviceName(String.valueOf(map3.get("devName")));
							device.setDeviceModelId(Integer.parseInt(String.valueOf(modelId)));
							device.setDeviceSn(String.valueOf(map3.get("deviceSn")));
							device.setDeviceType(Integer.parseInt(String.valueOf(typeId)));
							device.setConnTime(System.currentTimeMillis());
//							device.setOrgId(Integer.parseInt(String.valueOf(map3.get("orgId"))));
							device.setSupid(Integer.parseInt(collId));
							device.setDeviceValid("0");
							device.setDeviceStatus("1");
							device.setDataMode(Integer.parseInt(String.valueOf(dataModeId)));
							device.setConnProtocol(2);
							status = deviceMapper.addDevice(device);
							if (status > 0) {
								logger.info(StringUtil.appendStr("新增设备成功! 设备id:", deviceId, "操作者:", logUserName));
								// 新增设备和数采的关联关系
								CollJsonDevSet collJsonDevSet = new CollJsonDevSet();
								collJsonDevSet.setCollId(Integer.parseInt(collId));
								collJsonDevSet.setComId(comId);
								collJsonDevSet.setDevId(device.getId());
								collJsonDevSet.setSlaveId(map3.get("addr") + "");
								collJsonDevSet.setValid("1");
								status = collJsonDevSetMapper.insertCollJsonDevSet(collJsonDevSet);
								if (status > 0) {
									logger.info(StringUtil.appendStr("新增数采和设备间的关系成功! 设备id:", deviceId, "操作者:",
											logUserName));
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.UPDATE_SUCCUESS);
								} else {
									logger.error(StringUtil.appendStr("新增数采和设备间的关系失败! 设备id:", deviceId, "操作者:",
											logUserName));
									throw new ServiceException(Msg.ADD_FAILED);
								}
							} else {
								logger.error(StringUtil.appendStr("新增设备失败! 设备id:", deviceId, "操作者:", logUserName));
								throw new ServiceException(Msg.ADD_FAILED);
							}
						}
					}
				}
			} else {
				logger.error(StringUtil.appendStr("修改数采信息失败! 数采id:", collId, "操作者:", logUserName));
				throw new ServiceException(Msg.UPDATE_COLL_FAILED);
			}
		}
		logger.info("刷新缓存");
		systemCache.reInit();
		return msg;
	}

	/**
	 * @Title: updateDevice
	 * @Description: 修改数采
	 * @param jsonData
	 * @param serviceType 0:新增;1:修改
	 * @return
	 * @throws SessionException
	 * @throws ServiceException
	 * @throws SessionTimeoutException: MessageBean
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月15日下午1:35:43
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateDevice(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String serviceType = (String) map.get("serviceType");
		if (!Util.isNotBlank(serviceType)) {
			serviceType = "1";
		}
		String plantId = (String) map.get("plantId");
		int maxNum = Integer.parseInt(String.valueOf(map.get("maxNum")));
		int inventerNum = Integer.parseInt(String.valueOf(map.get("inventerNum")));
		String id = (String) map.get("id");
		String tokenId = (String) map.get("tokenId");
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		if ("0".equals(serviceType)) {
			map.clear();
			map.put("plantId", plantId);
			map.put("deviceId", id);
			map.put("valid", "0");
			/** 激活数采 */
			int status = deviceMapper.updateCollectInfo(map);
			if (status > 0) {
				logger.info(StringUtil.appendStr("激活数采成功! 数采id:", id, "操作者:", logUserName));
				/** 数采下挂靠了设备继续操作 */
				if (maxNum > 0) {
					map.clear();
					map.put("collectorId", id);
					map.put("valid", "0");
					map.put("plantId", plantId);
					status = deviceMapper.updateCollectorDeviceValid(map);
					if (status > 0) {
						logger.info(StringUtil.appendStr("激活数采下设备成功! 数采id:", id, "操作者:", logUserName));
						if (maxNum != inventerNum) {
							Map<String, Object> param = new HashMap<>(3);
							param.put("collectorId", id);
							param.put("start", inventerNum);
							param.put("length", maxNum - inventerNum);
							/** 删除部分数采的解析记录 */
							status = collectorMessageStructureMapper.delAnyParse(param);
							if (status > 0) {
								logger.info(StringUtil.appendStr("删除部分解析记录成功! 数采id:", id, "start", inventerNum,
										"length", maxNum - inventerNum, "操作者:", logUserName));
								List<Integer> devices = collectorMessageStructureMapper
										.getDelDeviceIds(Integer.parseInt(id));
								if (Util.isNotBlank(devices)) {
									status = deviceMapper.delDevices(devices);
									if (status > 0) {
										logger.info(StringUtil.appendStr("删除设备成功! devices", devices.toString(), "操作者:",
												logUserName));
										msg.setCode(Header.STATUS_SUCESS);
										msg.setMsg(Msg.REPORT_SET_SUCCESS);
									} else {
										logger.error(StringUtil.appendStr("删除设备失败! devices", devices.toString(), "操作者:",
												logUserName));
										throw new ServiceException(Msg.REPORT_SET_FAILED);
									}
								} else {
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.REPORT_SET_SUCCESS);
								}
							} else {
								logger.error(StringUtil.appendStr("删除部分解析记录失败! 数采id:", id, "start", inventerNum,
										"length", maxNum - inventerNum, "操作者:", logUserName));
								throw new ServiceException(Msg.REPORT_SET_FAILED);
							}
						} else {
							logger.info(StringUtil.appendStr("操作数采无任何变动! 数采id:", id, "操作者:", logUserName));
							msg.setCode(Header.STATUS_SUCESS);
							msg.setMsg(Msg.REPORT_SET_SUCCESS);
						}
					} else {
						logger.error(StringUtil.appendStr("激活数采下设备失败! 数采id:", id, "操作者:", logUserName));
						throw new ServiceException(Msg.REPORT_SET_FAILED);
					}
				} else {
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.REPORT_SET_SUCCESS);
				}
			} else {
				logger.error(StringUtil.appendStr("激活数采失败! 数采id:", id, "操作者:", logUserName));
				throw new ServiceException(Msg.REPORT_SET_FAILED);
			}
		} else if ("1".equals(serviceType)) {
			CollDevice device = deviceMapper.getDeviceById(id);
			if ("2".equals(device.getDeviceValid())) {
				map.clear();
				map.put("plantId", plantId);
				map.put("deviceId", id);
				map.put("valid", "0");
				/** 激活数采 */
				int status = deviceMapper.updateCollectInfo(map);
				if (status > 0) {
					logger.info(StringUtil.appendStr("激活数采成功! 数采id:", id, "操作者:", logUserName));
					/** 数采下挂靠了设备继续操作 */
					if (maxNum > 0) {
						map.clear();
						map.put("collectorId", id);
						map.put("valid", "0");
						map.put("plantId", plantId);
						status = deviceMapper.updateCollectorDeviceValid(map);
						if (status > 0) {
							logger.info(StringUtil.appendStr("激活数采下设备成功! 数采id:", id, "操作者:", logUserName));
							if (maxNum != inventerNum) {
								Map<String, Object> param = new HashMap<>(3);
								param.put("collectorId", id);
								param.put("start", inventerNum);
								param.put("length", maxNum - inventerNum);
								/** 删除部分数采的解析记录 */
								status = collectorMessageStructureMapper.delAnyParse(param);
								if (status > 0) {
									logger.info(StringUtil.appendStr("删除部分解析记录成功! 数采id:", id, "start", inventerNum,
											"length", maxNum - inventerNum, "操作者:", logUserName));
									List<Integer> devices = collectorMessageStructureMapper
											.getDelDeviceIds(Integer.parseInt(id));
									if (Util.isNotBlank(devices)) {
										status = deviceMapper.delDevices(devices);
										if (status > 0) {
											logger.info(StringUtil.appendStr("删除设备成功! devices", devices.toString(),
													"操作者:", logUserName));
											msg.setCode(Header.STATUS_SUCESS);
											msg.setMsg(Msg.REPORT_SET_SUCCESS);
										} else {
											logger.error(StringUtil.appendStr("删除设备失败! devices", devices.toString(),
													"操作者:", logUserName));
											throw new ServiceException(Msg.REPORT_SET_FAILED);
										}
									} else {
										msg.setCode(Header.STATUS_SUCESS);
										msg.setMsg(Msg.REPORT_SET_SUCCESS);
									}
								} else {
									logger.error(StringUtil.appendStr("删除部分解析记录失败! 数采id:", id, "start", inventerNum,
											"length", maxNum - inventerNum, "操作者:", logUserName));
									throw new ServiceException(Msg.REPORT_SET_FAILED);
								}
							} else {
								logger.info(StringUtil.appendStr("操作数采无任何变动! 数采id:", id, "操作者:", logUserName));
								msg.setCode(Header.STATUS_SUCESS);
								msg.setMsg(Msg.REPORT_SET_SUCCESS);
							}
						} else {
							logger.error(StringUtil.appendStr("激活数采下设备失败! 数采id:", id, "操作者:", logUserName));
							throw new ServiceException(Msg.REPORT_SET_FAILED);
						}
					} else {
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.REPORT_SET_SUCCESS);
					}
				} else {
					logger.error(StringUtil.appendStr("激活数采失败! 数采id:", id, "操作者:", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
			}
			if (maxNum != inventerNum) {
				Map<String, Object> param = new HashMap<>(3);
				param.put("collectorId", id);
				param.put("start", inventerNum);
				param.put("length", maxNum - inventerNum);
				int status = collectorMessageStructureMapper.delAnyParse(param);
				if (status > 0) {
					logger.info(StringUtil.appendStr("删除部分解析记录成功! 数采id:", id, "start", inventerNum, "length",
							maxNum - inventerNum, "操作者:", logUserName));
					List<Integer> devices = collectorMessageStructureMapper.getDelDeviceIds(Integer.parseInt(id));
					if (Util.isNotBlank(devices)) {
						status = deviceMapper.delDevices(devices);
						if (status > 0) {
							logger.info(
									StringUtil.appendStr("删除设备成功! devices", devices.toString(), "操作者:", logUserName));
							msg.setCode(Header.STATUS_SUCESS);
							msg.setMsg(Msg.REPORT_SET_SUCCESS);
						} else {
							logger.error(
									StringUtil.appendStr("删除设备失败! devices", devices.toString(), "操作者:", logUserName));
							throw new ServiceException(Msg.REPORT_SET_FAILED);
						}
					} else {
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.REPORT_SET_SUCCESS);
					}
				} else {
					logger.error(StringUtil.appendStr("删除部分解析记录失败! 数采id:", id, "start", inventerNum, "length",
							maxNum - inventerNum, "操作者:", logUserName));
					throw new ServiceException(Msg.REPORT_SET_FAILED);
				}
			} else {
				logger.info(StringUtil.appendStr("操作数采无任何变动! 数采id:", id, "操作者:", logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.REPORT_SET_SUCCESS);
			}
		} else {
			throw new ServiceException(Msg.NOT_SUPPORT_TYPE);
		}
		logger.info("刷新缓存");
		systemCache.reInit();
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean deleteDevice(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, String> map = Util.parseURLCode(jsonData);
		String tokenId = map.get("tokenId");
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		/** 数采Id */
		int collectorId = Integer.parseInt(map.get("id"));
		List<Integer> device = new ArrayList<>(1);
		device.add(collectorId);
		/** 删除数采 */
		int result = deviceMapper.delDevices(device);
		if (result > 0) {
			/** 删除数采关联的设备 */
			logger.info(StringUtil.appendStr("删除数采成功! 数采id:", collectorId, "操作者:", logUserName));
			result = deviceMapper.delCollectorDevice(collectorId);
			/** 删除数采的解析列表 */
			logger.info(StringUtil.appendStr("删除数采下设备成功! 数采id:", collectorId, "操作者:", logUserName));
			collectorMessageStructureMapper.delCollectorParse(collectorId);
			msg.setMsg(Msg.DELETE_SUCCUESS);
			msg.setCode(Header.STATUS_SUCESS);
			// 刷新缓存
			systemCache.reInit();
		} else {
			logger.error(StringUtil.appendStr("删除数采失败! 数采id:", collectorId, "操作者:", logUserName));
			throw new ServiceException(Msg.DELETE_FAILED);
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateCollector(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {

		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		String deviceId = map.get("deviceId") + "";
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		String status = map.get("status") + "";
		Map<String, Object> paraMap = new HashMap<>();
		// 查询该数采下设备
		List<String> reList = new ArrayList<>();
		// List<String>reList2=new ArrayList<>();
		List<CollDevice> deviceList = deviceMapper.getAllDevice();
		reList.add(deviceId);
		reList = ServiceUtil.getTreeSup(deviceList, deviceId, reList);
		// reList2 = ServiceUtil.getTreeSup(deviceList, deviceId,reList2);
		if (Util.isNotBlank(status)) {
			Integer statusNum = Integer.parseInt(status);
			// 1启用 0停用
			if (statusNum == 0) {
				paraMap.put("deviceId", deviceId);
				paraMap.put("statusNum", statusNum);
				Integer result = deviceMapper.updateCollectorStatus(paraMap);
				if (result > 0) {
					logger.info(" 关闭数采状态成功! 操作人: " + logUserName + "_数采id" + deviceId);
					if (reList.size() == 1) {
						// 数采下未挂载设备
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.COLL_SANS_DEVICE_SUCCESS);
					} else {
						// 关闭该数采下挂载设备
						paraMap.clear();
						paraMap.put("deviceId", reList);
						paraMap.put("statusNum", statusNum);
						Integer result1 = deviceMapper.updateCollectorDeviceStatus(paraMap);
						// 更改解析表中设备valid
						paraMap.put("pareseIds", deviceId);
						paraMap.put("statusAfter", 1);
						Integer result2 = cmsMapper.updatePareseStatusCollectorChange(paraMap);
						if (result1 > 0 && result2 > 0) {
							logger.info(" 关闭数采及下属设备状态成功! 操作人: " + logUserName + "_数采id" + deviceId + "_挂载设备id："
									+ reList.toString());
							msg.setCode(Header.STATUS_SUCESS);
							msg.setMsg(Msg.CLOSE_DEVICE_UNDER_COLL_SUCCESS);
						} else {
							logger.error(" 关闭数采及下属设备状态失败! 操作人: " + logUserName + "_数采id" + deviceId + "_挂载设备id："
									+ reList.toString());
							throw new ServiceException(Msg.CLOSE_DEVICE_UNDER_COLL_FAILD);
						}
					}
					// 刷新缓存
					systemCache.reInit();
				} else {
					logger.error(" 关闭数采失败! 操作人: " + logUserName + "_数采id" + deviceId);
					throw new ServiceException(Msg.CLOSE_COLL_FAILD);
				}
			} else if (statusNum == 1) {
				paraMap.put("deviceId", deviceId);
				paraMap.put("statusNum", statusNum);
				Integer result0 = deviceMapper.updateCollectorStatus(paraMap);
				if (result0 > 0) {
					logger.info(" 数采开启成功! 操作人: " + logUserName + "_数采id" + deviceId);
					if (reList.size() == 1) {
						// 数采下未挂载设备
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.OPEN_COLL_SANS_DEVICE_SUCCESS);
					} else {
						// 开启该数采下挂载设备
						paraMap.clear();
						paraMap.put("deviceId", reList);
						paraMap.put("statusNum", statusNum);
						Integer result1 = deviceMapper.updateCollectorDeviceStatus(paraMap);
						// 更改解析表中设备valid
						paraMap.put("pareseIds", deviceId);
						paraMap.put("statusAfter", 0);
						Integer result2 = cmsMapper.updatePareseStatusCollectorChange(paraMap);
						if (result1 > 0 && result2 > 0) {
							logger.info(" 开启数采及下属设备状态成功! 操作人: " + logUserName + "_数采id" + deviceId + "_挂载设备id："
									+ reList.toString());
							msg.setCode(Header.STATUS_SUCESS);
							msg.setMsg(Msg.OPEN_COLL_ET_DEVICE_SUCCESS);
						} else {
							logger.error(" 开启数采及下属设备状态失败! 操作人: " + logUserName + "_数采id" + deviceId + "_挂载设备id："
									+ reList.toString());
							throw new ServiceException(Msg.OPEN_COLL_ET_DEVICE_FAILD);
						}
					}
					// 刷新缓存
					systemCache.reInit();
				} else {
					logger.error(" 数采开启失败! 操作人: " + logUserName + "_数采id" + deviceId);
					throw new ServiceException(Msg.OPEN_COLL_FAILD);
				}
			} else {
				throw new ServiceException(Msg.NOT_SUPPORT_TYPE);
			}
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateCollAndSubDev(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 数采id、编码、名称、所属组织、所属电站
		String collId = map.get("collId") + "";
		String collSN = map.get("collSN") + "";
		String collName = map.get("collName") + "";
		String orgId = map.get("orgId") + "";
		String modelId = map.get("modelId") + "";
		String plantId = map.get("plantId") + "";
		String maxNum = map.get("maxLength") + "";
		String version = String.valueOf(collSN.charAt(3));
		Map<String, Object> paramMap = new HashMap<>();
		// 更新待删除设备信息
		String deletedIds = map.get("deletedIds") + "";
		if (Util.isNotBlank(deletedIds)) {
			String[] split = deletedIds.split(",");
			List<String> reList = Arrays.asList(split);
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("reList", reList);
			// 删除设备信息
			Integer deleteResult = deviceMapper.deleteDevices(delMap);
			// 删除解析表设备信息
			Integer deleteResult2 = 0;
			if ("1".equals(version)) {
				deleteResult2 = cmsMapper.delSubDeviceforParse(delMap);
			} else if ("2".equals(version)) {
				deleteResult2 = collJsonDevSetMapper.updateDevices(reList);
			}
			if (deleteResult > 0 && deleteResult2 > 0) {
				logger.info("删除数采下设备成功,操作人:" + logUserName + "_该数采id：" + collId + "_待删除设备id为：" + reList.toString());
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_DEVICE_UNDER_COLL_SUCCESS);
			} else {
				logger.error("删除数采下设备失败,操作人:" + logUserName + "_该数采id：" + collId + "_待删除设备id为：" + reList.toString());
				throw new ServiceException(Msg.DELETE_DEVICE_UNDER_COLL_FAILD);
			}
		}
		Map<String, Object> exMap = new HashMap<>();
		exMap.put("id", collId);
		exMap.put("sn", collSN);
		String existSN = null;
		if (!StringUtil.isBlank(collSN)) {
			existSN = deviceMapper.getSNExist4Update(exMap);
		}
		// 验证数采sn唯一性
		if (existSN == null) {
			// 更改数采信息
			paramMap.put("modelId", modelId);
			paramMap.put("collId", collId);
			paramMap.put("collName", collName);
			paramMap.put("orgId", orgId);
			paramMap.put("collSN", collSN);
			paramMap.put("connTime", System.currentTimeMillis());
			paramMap.put("maxNum", maxNum);
			String validAfter = null;
			Integer result = 0;
			// 根据有无电站id设置valid值
			if (StringUtil.isBlank(plantId) || plantId == null || "null".equals(plantId)) {
				validAfter = "2";
				paramMap.put("plantId", null);
				result = deviceMapper.updateCollectorInfoOutPid(paramMap);
			} else {
				validAfter = "0";
				paramMap.put("plantId", Integer.parseInt(plantId));
				result = deviceMapper.updateCollectorInfo(paramMap);
			}
			if (result > 0) {
				// 更改后数采下设备信息
				List<Map<String, Object>> subDevice = (List<Map<String, Object>>) map.get("subDevice");
				if (subDevice.size() > 0) {
					if ("1".equals(version)) {
						for (Map<String, Object> map2 : subDevice) {
							String subdeviceName = String.valueOf(map2.get("deviceName"));
							String subDeviceSN = String.valueOf(map2.get("deviceSN"));
							// 新建挂载设备信息
							CollDevice device = new CollDevice();
							if (StringUtil.isBlank(subdeviceName)) {
								device.setDeviceName(null);
							} else {
								device.setDeviceName(subdeviceName);
							}
							if (StringUtil.isBlank(subDeviceSN)) {

								device.setDeviceSn(null);
							} else {
								device.setDeviceSn(subDeviceSN);
							}
							device.setDeviceModelId(Integer.parseInt(String.valueOf(map2.get("modelId"))));
							device.setOrgId(Integer.parseInt(orgId));
							if (StringUtil.isBlank(plantId) || plantId == null || "null".equals(plantId)) {
								device.setPlantId(null);
							} else {
								device.setPlantId(Integer.parseInt(plantId));
							}
							device.setConnTime(System.currentTimeMillis());
							device.setSupid(Integer.parseInt(collId));
							device.setDeviceValid(validAfter);
							device.setDeviceStatus("1");
							device.setDeviceType(Integer.parseInt(String.valueOf(map2.get("typeId"))));
							device.setConnProtocol(2);
							device.setDataMode(Integer.parseInt(String.valueOf(map2.get("modeId"))));

							exMap.clear();
							exMap.put("id", String.valueOf(map2.get("deviceId")));
							exMap.put("sn", subDeviceSN);
							// 无上级数采id时，进行新建数采下设备
							map2.put("collId", collId);
							if (StringUtil.isBlank(plantId) || plantId == null || "null".equals(plantId)) {
								map2.put("plantId", null);
							} else {
								map2.put("plantId", Integer.parseInt(plantId));
							}
							map2.put("subDeviceOrder", String.valueOf(map2.get("subDeviceOrder")));
							map2.put("orgId", orgId);
							map2.put("connTime", System.currentTimeMillis());
							map2.put("valid", validAfter);
							map2.put("status", 1);
							String deviceId = String.valueOf(map2.get("deviceId"));
							// 判断挂载设备sn是否存在
							String originId2 = null;
							if (!StringUtil.isBlank(subDeviceSN)) {
								originId2 = deviceMapper.getSNExist4Update(exMap);
							}
							if (originId2 == null) {
								if (!Util.isNotBlank(deviceId)) {
									deviceMapper.addSubDevice4Update(device);
									// 新建设备待激活入库
									CollectorMessageStructure cms = new CollectorMessageStructure();
									// 数采id、sn、点表id、名称、解析顺序、状态
									if (!StringUtil.isBlank(collId)) {
										cms.setCollectorId(Integer.parseInt(collId));
									}
									cms.setCollectorSn(collSN);
									int modeId = Integer.parseInt(String.valueOf(map2.get("modeId")));
									cms.setDataModel(Integer.parseInt(String.valueOf(map2.get("modeId"))));
									cms.setDeviceId(device.getId());
									CollModel modelName = modelMapper.getModelName(modeId);
									String tableName = modelName.getTableName();
									cms.setTableName(tableName);
									// yc、yx、yk、yt条数
									Map<String, Object> tMap = new HashMap<>();
									tMap.put("tableName", tableName);
									tMap.put("dataMode", modeId);
									List<Map<String, Object>> countMap = cmsMapper.getEachCountforSize(tMap);
									int size = countMap.size();
									Integer ycReal = 0;
									Integer yxReal = 0;
									Integer ykReal = 0;
									Integer ytReal = 0;
									if (size > 0) {
										for (int j = 0; j < size; j++) {
											if ("YC".equals(countMap.get(j).get("type"))) {
												ycReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YX".equals(countMap.get(j).get("type"))) {
												yxReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YK".equals(countMap.get(j).get("type"))) {
												ykReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YT".equals(countMap.get(j).get("type"))) {
												ytReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											}
										}
									}
									if (!StringUtil.isBlank(String.valueOf(map2.get("subDeviceOrder")))) {
										cms.setParseOrder(Integer.parseInt(String.valueOf(map2.get("subDeviceOrder"))));
									} else {
										logger.error("更改数采、更改或新建数采下设备失败:" + logUserName + "_该数采sn：" + collSN
												+ "_该设备sn号：" + subDeviceSN);
										throw new ServiceException(Msg.UPDATE_COLL_ET_DEVICE_FAILD);
									}
									StringBuffer sb = new StringBuffer();
									StringBuffer append = sb.append(ycReal).append(",").append(yxReal).append(",")
											.append(ykReal).append(",").append(ytReal);
									cms.setSize(append.toString());
									cms.setValid("0");
									cmsMapper.addNewStructure(cms);

									if (device.getId() > 0) {
										logger.info("更改数采、更改或新建数采下设备成功,操作人:" + logUserName + "_该数采sn：" + collSN
												+ "_该设备sn号：" + subDeviceSN);
										msg.setCode(Header.STATUS_SUCESS);
										msg.setMsg(Msg.UPDATE_COLL_ET_DEVICE_SUCCESS);
									} else {
										logger.error("更改数采、更改或新建数采下设备失败:" + logUserName + "_该数采sn：" + collSN
												+ "_该设备sn号：" + subDeviceSN);
										throw new ServiceException(Msg.UPDATE_COLL_ET_DEVICE_FAILD);
									}
								} else {
									// 有上级数采id时，更改已有数采下设备信息
									Integer result2 = 0;
									result2 = deviceMapper.updateSubDevice(map2);
									// 修改后逆变器型号、类型id
									device.setId(Integer.parseInt(deviceId));
									// 新建设备待激活入库
									CollectorMessageStructure cms = new CollectorMessageStructure();
									// 数采id、sn、点表id、名称、解析顺序、状态
									if (!StringUtil.isBlank(collId)) {
										cms.setCollectorId(Integer.parseInt(collId));
									}
									cms.setCollectorSn(collSN);
									int modeId = Integer.parseInt(String.valueOf(map2.get("modeId")));
									cms.setDataModel(Integer.parseInt(String.valueOf(map2.get("modeId"))));
									cms.setDeviceId(device.getId());
									CollModel modelName = modelMapper.getModelName(modeId);
									String tableName = modelName.getTableName();
									cms.setTableName(tableName);
									// yc、yx、yk、yt条数
									Map<String, Object> tMap = new HashMap<>();
									tMap.put("tableName", tableName);
									tMap.put("dataMode", modeId);
									List<Map<String, Object>> countMap = cmsMapper.getEachCountforSize(tMap);
									int size = countMap.size();
									Integer ycReal = 0;
									Integer yxReal = 0;
									Integer ykReal = 0;
									Integer ytReal = 0;
									if (size > 0) {
										for (int j = 0; j < size; j++) {
											if ("YC".equals(countMap.get(j).get("type"))) {
												ycReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YX".equals(countMap.get(j).get("type"))) {
												yxReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YK".equals(countMap.get(j).get("type"))) {
												ykReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											} else if ("YT".equals(countMap.get(j).get("type"))) {
												ytReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
											}
										}
									}
									if (!StringUtil.isBlank(String.valueOf(map2.get("subDeviceOrder")))) {
										cms.setParseOrder(Integer.parseInt(String.valueOf(map2.get("subDeviceOrder"))));
									}
									StringBuffer sb = new StringBuffer();
									StringBuffer append = sb.append(ycReal).append(",").append(yxReal).append(",")
											.append(ykReal).append(",").append(ytReal);
									cms.setSize(append.toString());
									cms.setValid("0");
									// cmsMapper.addNewStructure(cms);
									cmsMapper.updatePareseOnUpdate(cms);
									if (result2 > 0) {
										logger.info("更改数采、更改或新建数采下设备成功,操作人:" + logUserName + "_该数采sn：" + collSN
												+ "_该设备sn号：" + subDeviceSN);
										msg.setCode(Header.STATUS_SUCESS);
										msg.setMsg(Msg.UPDATE_COLL_ET_DEVICE_SUCCESS);
									} else {
										logger.error("更改数采、更改或新建数采下设备失败,操作人:" + logUserName + "_该数采sn：" + collSN
												+ "_该设备sn号：" + subDeviceSN);
										throw new ServiceException(Msg.UPDATE_COLL_ET_DEVICE_FAILD);
									}
								}
							} else {
								logger.error(" 数采下设备sn号已存在，编辑失败! 操作人: " + logUserName + "_重复数采id：" + collId);
								throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
							}
						}
					} else if ("2".equals(version)) {
						for (Map<String, Object> map2 : subDevice) {
							String deviceId = String.valueOf(map2.get("deviceId"));
							// 有id就是修改；没有id就是新增
							if (Util.isNotBlank(deviceId)) {
								Map<String, Object> tempMap = new HashMap<String, Object>();
								tempMap.put("deviceName", map2.get("deviceName") + "");
								tempMap.put("deviceModelId", map2.get("modelId") + "");
								tempMap.put("deviceSn", map2.get("deviceSN") + "");
								tempMap.put("deviceType", map2.get("typeId") + "");
								tempMap.put("deviceId", deviceId);
								// 修改设备的一些基本信息
								int status = deviceMapper.updateSomeSubDevice(tempMap);
								if (status > 0) {
									logger.info("修改设备信息成功,操作人:" + logUserName + "  设备id：" + deviceId);
									/** 修改设备的串口和通讯地址 */
									// 首先判断通讯地址的唯一性（同一数采下的同一串口通讯地址不能相同）
									String comId = map2.get("serialPort") + "";
									String slaveId = map2.get("subDeviceOrder") + "";
									tempMap.clear();
									tempMap.put("collId", collId);
									tempMap.put("comId", comId);
									tempMap.put("slaveId", slaveId);
//									CollJsonDevSet collJsonDevSet = collJsonDevSetMapper.getUnique(tempMap);
//									if(collJsonDevSet == null){
									tempMap.put("deviceId", deviceId);
									status = collJsonDevSetMapper.updateSlaveIdAndComId(tempMap);
									if (status > 0) {
										logger.info(" 修改设备的串口和通讯地址成功! 操作人: " + logUserName + "  设备id：" + deviceId);
									} else {
										logger.error(" 修改设备的串口和通讯地址失败! 操作人: " + logUserName + "  设备id：" + deviceId);
										throw new ServiceException(Msg.UPDATE_FAILED);
									}
//									}else{
//										logger.error(" 该通讯地址在该数采的串口下已经存在! 操作人: "+logUserName+" 数采id："+collId+" 串口号："+comId);
//										throw new ServiceException(Msg.SLAVEID_EXISTS);
//									}
								} else {
									logger.error(" 修改设备信息失败! 操作人: " + logUserName + "  设备id：" + deviceId);
									throw new ServiceException(Msg.UPDATE_FAILED);
								}
							} else {
								String deviceModelId = map2.get("modelId") + "";
								String typeId = map2.get("typeId") + "";
								// 获取dataMode
								Map<String, Object> tempMap = new HashMap<String, Object>();
								tempMap.put("deviceModelId", deviceModelId);
								tempMap.put("typeId", typeId);
								int dataModeId = modelMapper.getDateModelId(tempMap);
								// 新增下连设备
								CollDevice device = new CollDevice();
								device.setDeviceName(String.valueOf(map2.get("deviceName")));
								device.setDeviceModelId(Integer.parseInt(String.valueOf(deviceModelId)));
								device.setDeviceSn(String.valueOf(map2.get("deviceSn")));
								device.setDeviceType(Integer.parseInt(String.valueOf(typeId)));
								device.setConnTime(System.currentTimeMillis());
								device.setOrgId(Integer.parseInt(orgId));
								device.setSupid(Integer.parseInt(collId));
								device.setDeviceValid("0");
								device.setDeviceStatus("1");
								device.setDataMode(Integer.parseInt(String.valueOf(dataModeId)));
								device.setConnProtocol(2);
								int status = deviceMapper.addDevice(device);
								if (status > 0) {
									logger.info(StringUtil.appendStr("新增设备成功! 设备id:", deviceId, "操作者:", logUserName));
									// 新增设备和数采的关联关系
									CollJsonDevSet collJsonDevSet = new CollJsonDevSet();
									collJsonDevSet.setCollId(Integer.parseInt(collId));
									collJsonDevSet.setComId(map2.get("serialPort") + "");
									collJsonDevSet.setDevId(device.getId());
									collJsonDevSet.setSlaveId(map2.get("subDeviceOrder") + "");
									collJsonDevSet.setValid("1");
									status = collJsonDevSetMapper.insertCollJsonDevSet(collJsonDevSet);
									if (status > 0) {
										logger.info(StringUtil.appendStr("新增数采和设备间的关系成功! 设备id:", deviceId, "操作者:",
												logUserName));
									} else {
										logger.error(StringUtil.appendStr("新增数采和设备间的关系失败! 设备id:", deviceId, "操作者:",
												logUserName));
										throw new ServiceException(Msg.ADD_FAILED);
									}
								} else {
									logger.error(StringUtil.appendStr("新增设备失败! 设备id:", deviceId, "操作者:", logUserName));
									throw new ServiceException(Msg.ADD_FAILED);
								}
							}
						}
					}
				} else {
					logger.error(" 更改数采信息成功，且挂载设备信息无变动! 操作人: " + logUserName + "_数采id：" + collId);
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.UPDATE_COLL_SANS_DEVICE_SUCCESS);
				}
			} else {
				logger.error(" 更改数采信息失败! 操作人: " + logUserName + "_数采id：" + collId);
				throw new ServiceException(Msg.UPDATE_COLL_SANS_DEVICE_FAILD);
			}

		} else {
			logger.error(" 数采sn号已存在，编辑失败! 操作人: " + logUserName + "_更改数采id为：" + collId);
			throw new ServiceException(Msg.COLLSN_EXIST);
		}
		// 刷新缓存
		systemCache.reInit();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.UPDATE_SUCCUESS);
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updatePointTable(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		User user = session.getAttribute(map.get("tokenId") + "");
//		String insertOrUpdate = map.get("insertOrUpdate")+"";
		String collSn = map.get("sn") + "";
//		String status = null;
//		if("1".equals(insertOrUpdate)){
//			status = addCollector(jsonData,session).getCode();
//		}else if("2".equals(insertOrUpdate)){
//			status = updateCollAndSubDev(jsonData,session).getCode();
//		}
//		if("100".equals(status)){
		List<Map<String, Object>> subDevice = (List<Map<String, Object>>) map.get("subDevice");
		Map<Integer, Map<String, Integer>> resultMap = new HashMap<>();
		if (Util.isNotBlank(subDevice)) {
			Map<String, Integer> tempMap1 = new HashMap<>();
			Map<String, Integer> tempMap2 = new HashMap<>();
			for (Map<String, Object> map2 : subDevice) {
				String comId = map2.get("serialPort") + "";
				String slaveId = map2.get("subDeviceOrder") + "";
				String modelId = map2.get("modelId") + "";
				String typeId = map2.get("typeId") + "";
				// 获取dataMode
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("deviceModelId", modelId);
				paramMap.put("typeId", typeId);
				int dataModeId = modelMapper.getDateModelId(paramMap);
				if ("1".equals(comId)) {
					tempMap1.put(slaveId, dataModeId);
				} else if ("2".equals(comId)) {
					tempMap2.put(slaveId, dataModeId);
				}
			}
			resultMap.put(1, tempMap1);
			resultMap.put(2, tempMap2);
		}
		boolean b = false;
		try {
			b = iYkYtService.dataModelSet(collSn, resultMap);
			logger.info("指令下发代理服务器成功, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collSn);
		} catch (Exception e) {
			logger.error("指令下发代理服务器失败, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collSn);
			throw new ServiceException("保存成功，下发点表失败，请在编辑页面重试");
		}
		if (b) {
			Map<String, Object> reult = RabbitMessage.getReturnDataModeMessage(collSn);
			if (reult != null && !reult.isEmpty()) {
				// TODO
				logger.info(" 服务器回执信息判定成功!");
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.YKYT_SUCCESS);
			} else {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.YKYT_FAILD);
				logger.error("服务器回执信息判定失败!");
			}
		} else {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.YKYT_FAILD);
		}
//		}else{
//			msg.setCode(Header.STATUS_FAILED);
//			msg.setMsg("保存失败");
//		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean deleteCollectors(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		String toDeleteIds = map.get("ids") + "";
		String[] split = toDeleteIds.split(",");
		List<String> idsList = Arrays.asList(split);
		List<String> multiList = new ArrayList<>();
		List<CollDevice> deviceList = deviceMapper.getAllDevice();
		if (deviceList.size() > 0) {
			Map<String, Object> paraMap = new HashMap<>();
			// 获取待删除数采及其下属设备id集合
			if (idsList.size() > 0) {
				for (String id : idsList) {
					List<String> reList = new ArrayList<>();
					reList.add(id);
					reList = ServiceUtil.getTreeSup(deviceList, id, reList);
					multiList.addAll(reList);
				}
			}
			paraMap.put("reList", multiList);
			Integer result = deviceMapper.deleteDevices(paraMap);
			paraMap.clear();
			paraMap.put("cids", idsList);
			Integer result1 = collectorMessageStructureMapper.deleteParseInfoByCollid(paraMap);
			if (result > 0 || result1 > 0) {
				logger.info(" 设备删除成功! 操作人: " + logUserName + "_设备id" + multiList.toString());
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_SUCCUESS);
				// 刷新缓存
				systemCache.reInit();
			} else {
				logger.error(" 设备删除失败! 操作人: " + logUserName + "_设备id" + multiList.toString());
				throw new ServiceException(Msg.DELETE_FAILED);
			}
		} else {
			logger.error(" 暂无设备可查 ");
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(Msg.SANS_DEVICE_2_SEARCH);
		}

		return msg;
	}

	@Override
	public MessageBean getDevicesUnderColl(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String collId = map.get("id") + "";
		String collType = map.get("collType") + "";
		List<String> reList = new ArrayList<>();
		List<CollDevice> deviceList = deviceMapper.getAllDevice();
		// 查出当前数采下设备集合
		reList = ServiceUtil.getTreeSup(deviceList, collId, reList);
		CollDevice coll = deviceMapper.getDeviceById(collId);
		if (reList.size() > 0) {
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("reList", reList);
			List<CollDevice> list = deviceMapper.getDeviceDetails(paraMap);
			Integer count = deviceMapper.getDeviceDetailsPageCount(paraMap);
			List<Map<String, Object>> finalList = new ArrayList<>();
			if (list.size() > 0) {
				if ("1".equals(collType)) {
					for (CollDevice collDevice : list) {
						// 数采下设备名、id 类型、id 型号、id及编号
						Map<String, Object> paramMap = new HashMap<>();
						Map<String, Object> fMap = new HashMap<>();
						fMap.put("deviceId", collDevice.getId());
						String deviceName = collDevice.getDeviceName();
						if (StringUtil.isBlank(deviceName)) {
							fMap.put("deviceName", "");
						} else {
							fMap.put("deviceName", collDevice.getDeviceName());
						}
						String deviceSn = collDevice.getDeviceSn();
						if (StringUtil.isBlank(deviceSn)) {
							fMap.put("deviceSN", "");
						} else {
							fMap.put("deviceSN", collDevice.getDeviceSn());
						}
						Integer dataMode = collDevice.getDataMode();
						CollModel model = modelMapper.getModelName(dataMode);
						fMap.put("modeId", model.getId());
						fMap.put("modeName", model.getModeName());
						Integer deviceModelId = collDevice.getDeviceModelId();
						Integer type = collDevice.getDeviceType();
						paramMap.put("typeId", type);
						String name = typeMapper.getNameById(String.valueOf(type));
						fMap.put("typeId", type);
						fMap.put("typeName", name);
						paramMap.put("deviceType", type);
						paramMap.put("modeId", deviceModelId);
						DeviceDetailStringInverter commonDevice = deviceMapper.getM4Device(paramMap);
						fMap.put("modelId", deviceModelId);
						fMap.put("modelName", commonDevice == null ? null : commonDevice.getModel());
						Integer id = collDevice.getId();
						String pareseOrder = collectorMessageStructureMapper.getPareseOrder(id);
						fMap.put("subDeviceOrder", pareseOrder);

						// 所有设备集合
						List<DeviceType> deviceTypeInfo = typeMapper.getDeviceTypeInfo();
						List<Map<String, Object>> devList = new ArrayList<>();
						for (DeviceType tp : deviceTypeInfo) {
							Map<String, Object> map1 = new HashMap<>();
							map1.put("typeId", tp.getId());
							map1.put("typeName", tp.getTypeName());
							devList.add(map1);
						}
						fMap.put("deviceDefault", devList);

						// 所有设备型号集合
						List<Map<String, Object>> eList = new ArrayList<>();
						// 集中式逆变器
						if (type == 1) {
							List<DeviceDetailCentralInverter> allModelInfo = inverterMappper.getAllModelInfoCen();
							for (DeviceDetailCentralInverter cen : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = cen.getId();
								String modelIdentity = cen.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
							// 汇流箱
						} else if (type == 11) {
							List<DeviceDetailCombinerBox> allModelInfo = deviceDetailCombinerBoxMapper
									.getAllmodelInfoStr();
							for (DeviceDetailCombinerBox box : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = box.getId();
								String modelIdentity = box.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
							// 组串式
						} else if(type == DTC.STRING_INVERTER){
							List<DeviceDetailStringInverter> allModelInfo = strInverterMapper.getAllmodelInfoStr();
							for (DeviceDetailStringInverter cen : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = cen.getId();
								String modelIdentity = cen.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
							//环境监测器
						}else if(type==DTC.ENV_MONITOR){
							List<DeviceDetailEnvMonitor> allModelInfo = deviceDetailEnvMonitorMapper.getAllmodelInfoStr();
							for (DeviceDetailEnvMonitor cen : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = cen.getId();
								String modelIdentity = cen.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
						}

						fMap.put("modelDefault", eList);

						// 根据所选型号选择点表
						Map<String, Object> pMap = new HashMap<>();
						pMap.put("modelId", deviceModelId);
						pMap.put("typeId", type);
						List<CollModel> modes = modelMapper.getModeByModelId(pMap);
						List<Map<String, Object>> eList2 = new ArrayList<>();
						// Map<String,Object> modeMap = new HashMap<>();
						for (CollModel collModel : modes) {
							Map<String, Object> map1 = new HashMap<>();
							String modeName = collModel.getModeName();
							Integer id2 = collModel.getId();
							map1.put("modeId", id2);
							map1.put("modeName", modeName);
							eList2.add(map1);
						}
						fMap.put("modeDefault", eList2);

						finalList.add(fMap);
					}
				} else if ("2".equals(collType)) {
					for (CollDevice collDevice : list) {
						// 数采下设备名、id 类型、id 型号、id及编号
						Map<String, Object> paramMap = new HashMap<>();
						Map<String, Object> fMap = new HashMap<>();
						fMap.put("deviceId", collDevice.getId());
						String deviceName = collDevice.getDeviceName();
						if (StringUtil.isBlank(deviceName)) {
							fMap.put("deviceName", "");
						} else {
							fMap.put("deviceName", collDevice.getDeviceName());
						}
						String deviceSn = collDevice.getDeviceSn();
						if (StringUtil.isBlank(deviceSn)) {
							fMap.put("deviceSN", "");
						} else {
							fMap.put("deviceSN", collDevice.getDeviceSn());
						}
						Integer deviceModelId = collDevice.getDeviceModelId();
						Integer type = collDevice.getDeviceType();
						paramMap.put("typeId", type);
						String name = typeMapper.getNameById(String.valueOf(type));
						fMap.put("typeId", type);
						fMap.put("typeName", name);
						paramMap.put("deviceType", type);
						paramMap.put("modeId", deviceModelId);
						DeviceDetailStringInverter commonDevice = deviceMapper.getM4Device(paramMap);
						fMap.put("modelId", deviceModelId);
						fMap.put("modelName", commonDevice == null ? null : commonDevice.getModel());
						Integer id = collDevice.getId();
						CollJsonDevSet collJsonDevSet = collJsonDevSetMapper.getJsonDevSetById(id);
						if (collJsonDevSet != null) {
							fMap.put("serialPortId", collJsonDevSet.getComId());
							fMap.put("subDeviceOrder", collJsonDevSet.getSlaveId());
						}
						// 由于形式所迫，这段代码暂时写死
						List portList = new ArrayList();
						for (int i = 1; i < 3; i++) {
							Map<String, Object> map1 = new HashMap<>();
							map1.put("serialPortId", i);
							map1.put("serialPortName", "串口" + i);
							portList.add(map1);
						}
						fMap.put("serialPortDefault", portList);
						// 所有设备集合
						List<DeviceType> deviceTypeInfo = typeMapper.getDeviceTypeInfo();
						List<Map<String, Object>> devList = new ArrayList<>();
						for (DeviceType tp : deviceTypeInfo) {
							Map<String, Object> map1 = new HashMap<>();
							map1.put("typeId", tp.getId());
							map1.put("typeName", tp.getTypeName());
							devList.add(map1);
						}
						fMap.put("deviceDefault", devList);

						// 所有设备型号集合
						List<Map<String, Object>> eList = new ArrayList<>();
						if (type == 1) {
							List<DeviceDetailCentralInverter> allModelInfo = inverterMappper.getAllModelInfoCen();
							for (DeviceDetailCentralInverter cen : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = cen.getId();
								String modelIdentity = cen.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
						} else if (type == 11) {
							List<DeviceDetailCombinerBox> allModelInfo = deviceDetailCombinerBoxMapper
									.getAllmodelInfoStr();
							for (DeviceDetailCombinerBox box : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = box.getId();
								String modelIdentity = box.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
						} else {
							List<DeviceDetailStringInverter> allModelInfo = strInverterMapper.getAllmodelInfoStr();
							for (DeviceDetailStringInverter cen : allModelInfo) {
								Map<String, Object> map1 = new HashMap<>();
								Integer id2 = cen.getId();
								String modelIdentity = cen.getModelIdentity();
								map1.put("modelId", id2);
								map1.put("modelName", modelIdentity);
								eList.add(map1);
							}
						}
						fMap.put("modelDefault", eList);
						finalList.add(fMap);
					}
				}
			}
			Map<String, Object> reMap = new HashMap<>();
			reMap.put("draw", String.valueOf(map.get("draw")));
			reMap.put("recordsTotal", count);
			reMap.put("recordsFiltered", count);
			reMap.put("data", finalList);
			reMap.put("maxLength", coll.getMaxNum());
			msg.setBody(reMap);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		} else {
			msg.setBody(null);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.COLL_SANS_DEVICE);
		}

		return msg;
	}

	@Override
	public MessageBean get2ConnDevices(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// String tokenId=map.get("tokenId")+"";
		// 登录用户
		// User user = session.getAttribute(tokenId);
		// String
		// logUserName=StringUtil.appendStrNoBlank(user.getId(),"_",user.getUserName());
		String collId = map.get("id") + "";
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("id", collId);
		// deviceMapper.getSubDevices()
		// 所有设备类型
		List<DeviceType> deviceTypeInfo = typeMapper.getDeviceTypeInfo();
		List<Map<String, Object>> finalList = new ArrayList<>();
		Map<String, Object> typeMap = new HashMap<>();
		typeMap.put("allType", deviceTypeInfo);
		finalList.add(typeMap);
		// 每页条数
		// 起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		paraMap.put("length", length);
		paraMap.put("offset", start);
		List<CollDevice> devices = deviceMapper.getAllDeviceNotColl(paraMap);
		if (devices.size() > 0) {
			for (CollDevice collDevice : devices) {
				Map<String, Object> mm = new HashMap<>();
				// 设备名称
				mm.put("deviceName", collDevice.getDeviceName());
				// 编号
				mm.put("deviceSN", collDevice.getDeviceSn());
				Map<String, Object> paramMap = new HashMap<>();
				Integer deviceType = collDevice.getDeviceType();
				paramMap.put("typeId", deviceType);
				String name = typeMapper.getNameById(String.valueOf(deviceType));
				mm.put("deviceType", name);
				Integer dataMode = collDevice.getDataMode();
				CollModel model = modelMapper.getModelName(dataMode);
				// 电表
				mm.put("modelTableName", model.getModeName());
				// 型号
				Integer deviceModelId = collDevice.getDeviceModelId();
				Integer type = collDevice.getDeviceType();
				paramMap.clear();
				paramMap.put("deviceType", type);
				paramMap.put("modeId", deviceModelId);
				DeviceDetailStringInverter commonDevice = deviceMapper.getM4Device(paramMap);
				mm.put("model", commonDevice == null ? null : commonDevice.getModel());
				finalList.add(mm);
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", devices.size());
		reMap.put("recordsFiltered", devices.size());
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateInEtModue(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 解析前台传入信息
		String deviceId = map.get("deviceId") + "";
		String deviceIdentity = map.get("deviceIdentity") + "";
		String deviceName = map.get("deviceName") + "";
		String connTime = map.get("connTime") + "";
		String modelId = map.get("modelId") + "";
		String typeId = map.get("typeId") + "";
		// 是否为新选择型号
		String hasChanged = map.get("hasChanged") + "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(connTime);
		} catch (ParseException e) {
			logger.error(StringUtil.appendStr(" 解析时间格式发生错误! 操作者:", logUserName));
			e.printStackTrace();
			throw new ServiceException("解析时间格式发生错误");
		}
		Map<String, Object> paramMap1 = new HashMap<>();
		paramMap1.put("deviceId", deviceId);
		paramMap1.put("modelId", modelId);
		paramMap1.put("deviceIdentity", deviceIdentity);
		if (StringUtil.isBlank(deviceName)) {
			paramMap1.put("deviceName", null);
		} else {
			paramMap1.put("deviceName", deviceName);
		}
		if (StringUtil.isBlank(String.valueOf(date.getTime()))) {
			paramMap1.put("connTime", null);
		} else {
			paramMap1.put("connTime", date.getTime());
		}
		// 编辑逆变器信息
		Integer result = deviceMapper.updateInverterInfo(paramMap1);
		// Integer r1 = null;
		if (result > 0) {
			logger.info("更改逆变器信息成功,操作人:" + logUserName + "_该逆变器id：" + deviceId);
			// 0未变 1改变
			if (hasChanged.equals(0)) {
				if (Util.isNotBlank(typeId)) {
					int parseInt = Integer.parseInt(typeId);
					Map<String, Object> pMap = new HashMap<>();
					pMap.put("identity", deviceIdentity);
					pMap.put("modelId", modelId);
					// 编辑逆变器唯一识别码
					if (parseInt == 1 || parseInt == 2) {
						List<Map<String, Object>> subDevice = (List<Map<String, Object>>) map.get("data");
						String deviceId1 = String.valueOf(map.get("deviceId"));
						for (Map<String, Object> module : subDevice) {
							Map<String, Object> eMap = new HashMap<>();
							eMap.put("modelIdentity", module.get("modelIdentity"));
							// eMap.put("moduleId", module.get("moduleId"));
							String moduleId = String.valueOf(module.get("moduleId"));
							if (Util.isNotBlank(moduleId)) {
								eMap.put("moduleId", module.get("moduleId"));
							} else {
								eMap.put("moduleId", null);
							}
							String pvName = String.valueOf(module.get("pvName"));
							if (Util.isNotBlank(pvName)) {
								eMap.put("pvName", module.get("pvName"));
							} else {
								eMap.put("pvName", null);
							}
							// eMap.put("pvName", module.get("pvName"));
							eMap.put("pvId", module.get("pvId"));
							eMap.put("deviceId", deviceId1);
							eMap.put("manufacturer", module.get("manufacturer"));
							eMap.put("modulePower", module.get("modulePower"));
							String count = String.valueOf(module.get("count"));
							if (Util.isNotBlank(count)) {
								eMap.put("count", module.get("count"));
							} else {
								eMap.put("count", null);
							}
							eMap.put("moduleType", module.get("moduleType"));
							String pvId = module.get("pvId") + "";
							// 组串id存在时进行修改
							Integer uR1 = 0;
							Integer uR2 = 0;
							if (Util.isNotBlank(pvId)) {
								// 功率为空 说明该行被置空
								if ("".equals(module.get("modulePower"))) {
									String pid = module.get("pvId") + "";
									Map<String, Object> moduleMap = new HashMap<>();
									moduleMap.put("pid", pid);
									moduleMap.put("moduleId", null);
									moduleMap.put("moduleNum", null);
									uR1 = moduleMapper.delModuleInfo(moduleMap);
								} else {
									uR1 = moduleMapper.updateModuleInfo(eMap);
								}
								// 不存在组串id时进行添加
							} else {
								uR2 = moduleMapper.addNewModule(eMap);
							}
							if (uR1 > 0 || uR2 > 0) {
								logger.info("更改逆变器及其组串信息成功,操作人:" + logUserName + "_该逆变器id：" + module.get("deviceId"));
								msg.setCode(Header.STATUS_SUCESS);
								msg.setMsg(Msg.UPDATE_INVER_ET_MODULE_SUCCESS);
							} else {
								logger.error("更改逆变器及其组串信息失败,操作人:" + logUserName + "_该逆变器id：" + module.get("deviceId"));
								throw new ServiceException(Msg.UPDATE_INVER_ET_MODULE_FAILD);
							}
						}

					} else {
						logger.error(" 更改逆变器信息失败! 操作人: " + logUserName + "_逆变器id：" + deviceId);
						throw new ServiceException(Msg.DEVICE_NO_SUPPORT);
					}

				}
			} else {
				// 更新型号后 保存组件信息
				List<Map<String, Object>> subDevice = (List<Map<String, Object>>) map.get("data");
				String deviceId1 = String.valueOf(map.get("deviceId"));
				// 修改数采型号后 删除已有逆变器
				Integer ii = moduleMapper.delOldModuleInfo(deviceId1);
				for (Map<String, Object> module : subDevice) {
					Map<String, Object> eMap = new HashMap<>();
					String moduleId = String.valueOf(module.get("moduleId"));
					if (Util.isNotBlank(moduleId)) {
						eMap.put("moduleId", module.get("moduleId"));
					} else {
						eMap.put("moduleId", null);
					}
					String pvName = String.valueOf(module.get("pvName"));
					if (Util.isNotBlank(pvName)) {
						eMap.put("pvName", module.get("pvName"));
					} else {
						eMap.put("pvName", null);
					}
					// eMap.put("pvName", module.get("pvName"));
					eMap.put("deviceId", deviceId1);
					eMap.put("modulePower", module.get("modulePower"));
					String count = String.valueOf(module.get("count"));
					if (Util.isNotBlank(count)) {
						eMap.put("count", module.get("count"));
					} else {
						eMap.put("count", null);
					}
					Integer addNewModule = moduleMapper.addNewModule(eMap);
					if (addNewModule > 0) {
						logger.info("更改逆变器及其组串信息成功,操作人:" + logUserName + "_该逆变器id：" + module.get("deviceId"));
						msg.setCode(Header.STATUS_SUCESS);
						msg.setMsg(Msg.UPDATE_INVER_ET_MODULE_SUCCESS);
					} else {
						logger.error("更改逆变器及其组串信息失败,操作人:" + logUserName + "_该逆变器id：" + module.get("deviceId"));
						throw new ServiceException(Msg.UPDATE_INVER_ET_MODULE_FAILD);
					}
				}

			}
			// 刷新缓存
			systemCache.reInit();
		} else {
			logger.error(" 更改逆变器信息失败! 操作人: " + logUserName + "_逆变器id：" + deviceId);
			throw new ServiceException(Msg.UPDATE_INVER_FAILD);
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean addCollector(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 数采型号、编码、数采名称、组织、电站
		String topModelId = map.get("topModelId") + "";
		String sn = map.get("sn") + "";
		String collectorName = map.get("deviceName") + "";
		String orgName = map.get("orgId") + "";
		String plantName = map.get("plantId") + "";
		// 数采最大连接数
		Integer maxNum = Integer.valueOf(map.get("maxLength") + "");
		// 1：只有组织没有电站；2：有组织也有电站；
		Integer serviceType = Integer.parseInt(String.valueOf(map.get("serviceType")));
		// 下联设备
		List<Map<String, Object>> subDevice = (List<Map<String, Object>>) map.get("subDevice");
		// 判断数采sn号是否唯一
		String originId = deviceMapper.getSNExist(sn);
		// 新增数采
		if (originId == null) {
			String version = String.valueOf(sn.charAt(3));
			if (serviceType == 1) {
				CollDevice device = new CollDevice();
				device.setDeviceName(collectorName);
				device.setDeviceModelId(Integer.parseInt(topModelId));
				device.setDeviceSn(sn);
				device.setOrgId(Integer.parseInt(orgName));
				device.setConnTime(System.currentTimeMillis());
				device.setSupid(0);
				device.setDeviceValid("2");
				device.setDeviceStatus("1");
				device.setDeviceType(5);
				device.setConnProtocol(1);
				device.setDataMode(0);
				device.setMaxNum(maxNum);
				deviceMapper.addDevice(device);
				// 新增数采id
				Integer id = device.getId();
				// 新增数采无所属设备
				if (id > 0 && subDevice.size() == 0) {
					logger.info("新建数采成功,操作人:" + logUserName + "_新增数采：" + id);
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ADD_COLL_SUCCESS);
					// 新增数采及所属设备
				} else if (id > 0 && subDevice.size() > 0) {
					if (version.equals("1")) {
						for (int i = 0; i < subDevice.size(); i++) {
							Map<String, Object> map2 = subDevice.get(i);
							CollDevice newSubDevice = new CollDevice();
							newSubDevice.setDeviceName(String.valueOf(map2.get("subDeviceName")));
							// 1、为畅洋数采时表示：设备顺序；2、为自研数采时表示：内存地址；
							String deviceOrder = String.valueOf(map2.get("subDeviceOrder"));
							newSubDevice.setDeviceModelId(Integer.parseInt(String.valueOf(map2.get("modelId"))));
							newSubDevice.setDeviceSn(String.valueOf(map2.get("subDeviceSN")));
							newSubDevice.setDeviceType(Integer.parseInt(String.valueOf(map2.get("typeId"))));
							newSubDevice.setConnTime(System.currentTimeMillis());
							newSubDevice.setOrgId(Integer.parseInt(orgName));
							newSubDevice.setSupid(id);
							newSubDevice.setDeviceValid("2");
							newSubDevice.setDeviceStatus("1");
							newSubDevice.setDataMode(Integer.parseInt(String.valueOf(map2.get("modeId"))));
							newSubDevice.setConnProtocol(2);
							String subDeviceSN = String.valueOf(map2.get("subDeviceSN"));
							String originId2 = null;
							if (!StringUtil.isBlank(subDeviceSN)) {
								originId2 = deviceMapper.getSNExist(subDeviceSN);
							}
							// 判断新建数采下设备sn号是否唯一
							if (originId2 == null) {
								// 新建数采下设备
								deviceMapper.addDevice(newSubDevice);
								// 数采为畅洋版本
								Integer subDeviceId = newSubDevice.getId();
								// 新建设备待激活入库
								CollectorMessageStructure cms = new CollectorMessageStructure();
								// 数采id、sn、点表id、名称、解析顺序、状态
								cms.setCollectorId(id);
								cms.setCollectorSn(device.getDeviceSn());
								Integer dataMode = newSubDevice.getDataMode();
								cms.setDataModel(dataMode);
								cms.setDeviceId(subDeviceId);
								CollModel modelName = modelMapper.getModelName(dataMode);
								String tableName = modelName.getTableName();
								cms.setTableName(tableName);
								// yc、yx、yk、yt条数
								Map<String, Object> tMap = new HashMap<>();
								tMap.put("tableName", tableName);
								tMap.put("dataMode", dataMode);
								List<Map<String, Object>> countMap = cmsMapper.getEachCountforSize(tMap);
								int size = countMap.size();
								Integer ycReal = 0;
								Integer yxReal = 0;
								Integer ykReal = 0;
								Integer ytReal = 0;
								if (size > 0) {
									for (int j = 0; j < size; j++) {
										if ("YC".equals(countMap.get(j).get("type"))) {
											ycReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YX".equals(countMap.get(j).get("type"))) {
											yxReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YK".equals(countMap.get(j).get("type"))) {
											ykReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YT".equals(countMap.get(j).get("type"))) {
											ytReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										}
									}
								}
								if (!StringUtil.isBlank(deviceOrder)) {
									cms.setParseOrder(Integer.parseInt(deviceOrder));
								}
								StringBuffer sb = new StringBuffer();
								StringBuffer append = sb.append(ycReal).append(",").append(yxReal).append(",")
										.append(ykReal).append(",").append(ytReal);
								cms.setSize(append.toString());
								cms.setValid("0");
								cmsMapper.addNewStructure(cms);
								if (subDeviceId > 0 && cms.getId() > 0) {
									logger.info("新建数采下设备成功,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.ADD_DEVICE_UNDER_COLL_SUCCESS);
								} else {
									logger.error("新建数采下设备失败,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
								}
							} else {
								logger.error(" 新建数采下设备sn号已存在，编辑失败! 操作人: " + logUserName + "_该sn：" + subDeviceSN);
								throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
							}
						}
					} else if ("2".equals(version)) {
						for (int i = 0; i < subDevice.size(); i++) {
							Map<String, Object> map2 = subDevice.get(i);
							// 串口号
							String comId = map2.get("serialPort") + "";
							String modelId = map2.get("modelId") + "";
							String typeId = map2.get("typeId") + "";
							// 获取dataMode
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("deviceModelId", modelId);
							paramMap.put("typeId", typeId);
							int dataModeId = modelMapper.getDateModelId(paramMap);
							CollDevice newSubDevice = new CollDevice();
							newSubDevice.setDeviceName(String.valueOf(map2.get("subDeviceName")));
							// 1、为畅洋数采时表示：设备顺序；2、为自研数采时表示：内存地址；
							String deviceOrder = String.valueOf(map2.get("subDeviceOrder"));
							newSubDevice.setDeviceModelId(Integer.parseInt(modelId));
							newSubDevice.setDeviceSn(String.valueOf(map2.get("subDeviceSN")));
							newSubDevice.setDeviceType(Integer.parseInt(typeId));
							newSubDevice.setConnTime(System.currentTimeMillis());
							newSubDevice.setOrgId(Integer.parseInt(orgName));
							newSubDevice.setSupid(id);
							newSubDevice.setDeviceValid("2");
							newSubDevice.setDeviceStatus("1");
							newSubDevice.setDataMode(dataModeId);
							newSubDevice.setConnProtocol(2);
							String subDeviceSN = String.valueOf(map2.get("subDeviceSN"));
							String originId2 = null;
							if (!StringUtil.isBlank(subDeviceSN)) {
								originId2 = deviceMapper.getSNExist(subDeviceSN);
							}
							// 判断新建数采下设备sn号是否唯一
							if (originId2 == null) {
								// 新建数采下设备
								deviceMapper.addDevice(newSubDevice);
								// 数采为畅洋版本
								Integer subDeviceId = newSubDevice.getId();
								// 数采为自研版本
								CollJsonDevSet collJsonDevSet = new CollJsonDevSet();
								collJsonDevSet.setCollId(id);
								collJsonDevSet.setComId(comId);
								collJsonDevSet.setDevId(subDeviceId);
								collJsonDevSet.setSlaveId(deviceOrder);
								collJsonDevSet.setValid("1");
								collJsonDevSetMapper.insertCollJsonDevSet(collJsonDevSet);
								if (subDeviceId > 0 && collJsonDevSet.getId() > 0) {
									logger.info("新建数采下设备成功,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.ADD_DEVICE_UNDER_COLL_SUCCESS);
								} else {
									logger.error("新建数采下设备失败,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
								}
							} else {
								logger.error(" 新建数采下设备sn号已存在，编辑失败! 操作人: " + logUserName + "_该sn：" + subDeviceSN);
								throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
							}
						}
					}
				} else {
					logger.error(" 新建数采失败! 操作人: " + logUserName + "_数采sn：" + sn);
					throw new ServiceException(Msg.ADD_COLL_FAILD);
				}
			} else {
				CollDevice device = new CollDevice();
				device.setDeviceName(collectorName);
				device.setDeviceModelId(Integer.parseInt(topModelId));
				device.setDeviceSn(sn);
				device.setOrgId(Integer.parseInt(orgName));
				device.setPlantId(Integer.parseInt(plantName));
				device.setConnTime(System.currentTimeMillis());
				device.setSupid(0);
				device.setDeviceValid("0");
				device.setDeviceStatus("1");
				device.setDeviceType(5);
				device.setConnProtocol(1);
				device.setDataMode(0);
				device.setMaxNum(maxNum);
				deviceMapper.addDevice(device);
				// 新增数采id
				Integer id = device.getId();
				// 新增数采无所属设备
				if (id > 0 && subDevice.size() == 0) {
					logger.info("新建数采成功,操作人:" + logUserName + "_新增数采：" + id);
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ADD_COLL_SUCCESS);
					// 新增数采及所属设备
				} else if (id > 0 && subDevice.size() > 0) {
					if ("1".equals(version)) {
						for (int i = 0; i < subDevice.size(); i++) {
							Map<String, Object> map2 = subDevice.get(i);
							CollDevice newSubDevice = new CollDevice();
							newSubDevice.setDeviceName(String.valueOf(map2.get("subDeviceName")));
							String deviceOrder = String.valueOf(map2.get("subDeviceOrder"));
							newSubDevice.setDeviceModelId(Integer.parseInt(String.valueOf(map2.get("modelId"))));
							newSubDevice.setDeviceSn(String.valueOf(map2.get("subDeviceSN")));
							newSubDevice.setDeviceType(Integer.parseInt(String.valueOf(map2.get("typeId"))));
							newSubDevice.setConnTime(System.currentTimeMillis());
							newSubDevice.setOrgId(Integer.parseInt(orgName));
							newSubDevice.setPlantId(Integer.parseInt(plantName));
							newSubDevice.setSupid(id);
							newSubDevice.setDeviceValid("0");
							newSubDevice.setDeviceStatus("1");
							newSubDevice.setDataMode(Integer.parseInt(String.valueOf(map2.get("modeId"))));
							newSubDevice.setConnProtocol(2);
							String subDeviceSN = String.valueOf(map2.get("subDeviceSN"));
							String originId2 = null;
							if (!StringUtil.isBlank(subDeviceSN)) {
								originId2 = deviceMapper.getSNExist(subDeviceSN);
							}
							// 判断新建数采下设备sn号是否唯一
							if (originId2 == null) {
								// 新建数采下设备
								deviceMapper.addDevice(newSubDevice);
								Integer subDeviceId = newSubDevice.getId();
								// 新建设备待激活入库
								CollectorMessageStructure cms = new CollectorMessageStructure();
								// 数采id、sn、点表id、名称、解析顺序、状态
								cms.setCollectorId(id);
								cms.setCollectorSn(device.getDeviceSn());
								Integer dataMode = newSubDevice.getDataMode();
								cms.setDataModel(dataMode);
								cms.setDeviceId(subDeviceId);
								CollModel modelName = modelMapper.getModelName(dataMode);
								String tableName = modelName.getTableName();
								cms.setTableName(tableName);
								// yc、yx、yk、yt条数
								Map<String, Object> tMap = new HashMap<>();
								tMap.put("tableName", tableName);
								tMap.put("dataMode", dataMode);
								List<Map<String, Object>> countMap = cmsMapper.getEachCountforSize(tMap);
								int size = countMap.size();
								Integer ycReal = 0;
								Integer yxReal = 0;
								Integer ykReal = 0;
								Integer ytReal = 0;
								if (size > 0) {
									for (int j = 0; j < size; j++) {
										if ("YC".equals(countMap.get(j).get("type"))) {
											ycReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YX".equals(countMap.get(j).get("type"))) {
											yxReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YK".equals(countMap.get(j).get("type"))) {
											ykReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										} else if ("YT".equals(countMap.get(j).get("type"))) {
											ytReal = Integer.parseInt(String.valueOf(countMap.get(j).get("count")));
										}
									}
								}
								if (!StringUtil.isBlank(deviceOrder)) {
									cms.setParseOrder(Integer.parseInt(deviceOrder));
								}
								StringBuffer sb = new StringBuffer();
								StringBuffer append = sb.append(ycReal).append(",").append(yxReal).append(",")
										.append(ykReal).append(",").append(ytReal);
								cms.setSize(append.toString());
								cms.setValid("0");
								cmsMapper.addNewStructure(cms);
								if (subDeviceId > 0 && cms.getId() > 0) {
									logger.info("新建数采下设备成功,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.ADD_DEVICE_UNDER_COLL_SUCCESS);
								} else {
									logger.error("新建数采下设备失败,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
								}
							} else {
								logger.error(" 新建数采下设备sn号已存在，编辑失败! 操作人: " + logUserName + "_该sn：" + subDeviceSN);
								throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
							}
						}
					} else if ("2".equals(version)) {
						for (int i = 0; i < subDevice.size(); i++) {
							Map<String, Object> map2 = subDevice.get(i);
							// 串口号
							String comId = map2.get("serialPort") + "";
							String modelId = map2.get("modelId") + "";
							String typeId = map2.get("typeId") + "";
							// 获取dataMode
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("deviceModelId", modelId);
							paramMap.put("typeId", typeId);
							int dataModeId = modelMapper.getDateModelId(paramMap);
							CollDevice newSubDevice = new CollDevice();
							newSubDevice.setDeviceName(String.valueOf(map2.get("subDeviceName")));
							// 1、为畅洋数采时表示：设备顺序；2、为自研数采时表示：内存地址；
							String deviceOrder = String.valueOf(map2.get("subDeviceOrder"));
							newSubDevice.setDeviceModelId(Integer.parseInt(modelId));
							newSubDevice.setDeviceSn(String.valueOf(map2.get("subDeviceSN")));
							newSubDevice.setDeviceType(Integer.parseInt(typeId));
							newSubDevice.setConnTime(System.currentTimeMillis());
							newSubDevice.setOrgId(Integer.parseInt(orgName));
							newSubDevice.setPlantId(Integer.parseInt(plantName));
							newSubDevice.setSupid(id);
							newSubDevice.setDeviceValid("2");
							newSubDevice.setDeviceStatus("1");
							newSubDevice.setDataMode(dataModeId);
							newSubDevice.setConnProtocol(2);
							String subDeviceSN = String.valueOf(map2.get("subDeviceSN"));
							String originId2 = null;
							if (!StringUtil.isBlank(subDeviceSN)) {
								originId2 = deviceMapper.getSNExist(subDeviceSN);
							}
							// 判断新建数采下设备sn号是否唯一
							if (originId2 == null) {
								// 新建数采下设备
								deviceMapper.addDevice(newSubDevice);
								// 数采为畅洋版本
								Integer subDeviceId = newSubDevice.getId();
								// 数采为自研版本
								CollJsonDevSet collJsonDevSet = new CollJsonDevSet();
								collJsonDevSet.setCollId(id);
								collJsonDevSet.setComId(comId);
								collJsonDevSet.setDevId(subDeviceId);
								collJsonDevSet.setSlaveId(deviceOrder);
								collJsonDevSet.setValid("1");
								collJsonDevSetMapper.insertCollJsonDevSet(collJsonDevSet);
								if (subDeviceId > 0 && collJsonDevSet.getId() > 0) {
									logger.info("新建数采下设备成功,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									msg.setCode(Header.STATUS_SUCESS);
									msg.setMsg(Msg.ADD_DEVICE_UNDER_COLL_SUCCESS);
								} else {
									logger.error("新建数采下设备失败,操作人:" + logUserName + "_该设备sn：" + subDeviceSN);
									throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
								}
							} else {
								logger.error(" 新建数采下设备sn号已存在，编辑失败! 操作人: " + logUserName + "_该sn：" + subDeviceSN);
								throw new ServiceException(Msg.DEVICESN_EXIST_UNDERCOLL);
							}
						}
					}
				} else {
					logger.error(" 新建数采失败! 操作人: " + logUserName + "_数采sn：" + sn);
					throw new ServiceException(Msg.ADD_COLL_FAILD);
				}
			}
			// 刷新缓存
			systemCache.reInit();
		} else {
			logger.error(" 新增数采sn号已存在，新建数采失败! 操作人: " + logUserName + "_数采sn：" + sn);
			throw new ServiceException(Msg.COLLSN_EXIST);
		}
		return msg;
	}

	@Override
	public MessageBean getInverterList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 当前电站所属组织
		String plantId = map.get("plantId") + "";
		String orgId = map.get("orgId") + "";
		String status = map.get("status") + "";
		// 搜索设备名称、厂商、设备状态
		String deviceName = map.get("deviceName") + "";
		String manufacturer = map.get("manufacturer") + "";
		// 每页条数
		// 起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("deviceName", deviceName);
		paraMap.put("manufacturer", manufacturer);
		paraMap.put("status", status);
		paraMap.put("length", length);
		paraMap.put("offset", start);
		paraMap.put("plantId", plantId);
		paraMap.put("orgId", orgId);
		List<CollDevice> inverters = null;
		Integer totalCount = 0;
		if (Util.isNotBlank(plantId)) {
			// 根据电站id得到所有逆变器集合
			inverters = deviceMapper.getInverterList(paraMap);
			totalCount = deviceMapper.getInverterPagePid(paraMap);
		} else {
			// 根据组织id得到逆变器集合
			List<SysOrg> orgList = orgMapper.getAllOrg();
			List<String> reList = new ArrayList<>();
			reList.add(orgId + "");
			reList = ServiceUtil.getTree(orgList, orgId + "", reList);
			paraMap.put("reList", reList);
			inverters = deviceMapper.getInverterListByOid(paraMap);
			totalCount = deviceMapper.getInverterPageOid(paraMap);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> finalList = new ArrayList<>();
		Map<String, Object> reMap = new HashMap<>();
		if (inverters.size() > 0) {
			for (CollDevice collDevice : inverters) {
				Map<String, Object> eMap = new HashMap<>();
				// 逆变器名称、型号、sn号、厂商、功率、所属电站、组织、接入时间、状态
				Integer id = collDevice.getId();
				CollDevice cdevice = deviceMapper.getPidAndOid(id);
				eMap.put("plantId", cdevice.getPlantId());
				eMap.put("orgId", cdevice.getOrgId());
				eMap.put("deviceId", id);
				eMap.put("deviceName", collDevice.getDeviceName());
				String deviceSn = collDevice.getDeviceSn();
				if (StringUtil.isBlank(deviceSn)) {
					eMap.put("sn", "未填写");
				} else {
					eMap.put("sn", deviceSn);
				}
				eMap.put("manufacturer", collDevice.getManufacture());
				eMap.put("power", collDevice.getPower());
				PlantInfo plant = plantInfoMapper.getPlantNameById(collDevice.getPlantId() + "");
				if (plant == null) {
					eMap.put("plantName", "未分配");
				} else {
					eMap.put("plantName", plant.getPlantName());
				}
				SysOrg org = orgMapper.getOrgName(collDevice.getOrgId() + "");
				eMap.put("orgName", org.getOrgName());
				eMap.put("connTime", sdf.format(collDevice.getConnTime()));
				eMap.put("status", collDevice.getDeviceStatus());
				Integer deviceType = collDevice.getDeviceType();
				Integer deviceModelId = collDevice.getDeviceModelId();
				if (deviceType == 2) {
					DeviceDetailStringInverter strInverter = strInverterMapper.getModel(deviceModelId);
					eMap.put("model", strInverter.getModel());
					eMap.put("modelId", deviceModelId);
					eMap.put("typeId", deviceType);
					eMap.put("deviceIdentity", strInverter.getModelIdentity());
					Map<String, Object> typeMap = new HashMap<>();
					typeMap.put("typeId", deviceType);
					eMap.put("deviceType", typeMapper.getNameById(String.valueOf(deviceType)));
					eMap.put("manufacturer", strInverter.getManufacturer());
					eMap.put("power", strInverter.getPower());
					eMap.put("efficace", strInverter.getNominalConversionEfficiency());
					eMap.put("mttpRoute", strInverter.getMpptChannels());
					eMap.put("maxRoute", strInverter.getMaxInputChannels());
				} else {
					DeviceDetailCentralInverter cenInverter = inverterMappper.getModel(deviceModelId);
					eMap.put("model", cenInverter.getModel());
					eMap.put("modelId", deviceModelId);
					eMap.put("typeId", deviceType);
					eMap.put("deviceIdentity", cenInverter.getModelIdentity());
					Map<String, Object> typeMap = new HashMap<>();
					typeMap.put("typeId", deviceType);
					eMap.put("deviceType", typeMapper.getNameById(String.valueOf(deviceType)));
					eMap.put("manufacturer", cenInverter.getManufacturer());
					eMap.put("power", cenInverter.getPower());
					eMap.put("efficace", cenInverter.getNominalConversionEfficiency());
					eMap.put("mttpRoute", cenInverter.getMpptChannels());
					eMap.put("maxRoute", cenInverter.getMaxInputChannels());
				}
				finalList.add(eMap);
			}
		}
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalCount);
		reMap.put("recordsFiltered", totalCount);
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getInverterModelforChoice(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 当前电站所属组织
		String plantId = map.get("plantId") + "";
		String orgId = map.get("orgId") + "";
		String deviceId = map.get("deviceId") + "";
		// 起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("plantId", plantId);
		paraMap.put("orgId", orgId);
		paraMap.put("offset", start);
		paraMap.put("length", length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> finalList = new ArrayList<>();
		Map<String, Object> reMap = new HashMap<>();
		List<DeviceDetailStringInverter> strInverter2 = strInverterMapper.getModelforChoice(paraMap);
		Integer count = strInverterMapper.getInverterCount();
		CollDevice collDevice = deviceMapper.getDeviceById(deviceId);
		for (DeviceDetailStringInverter strInverter : strInverter2) {
			Map<String, Object> eMap = new HashMap<>();
			eMap.put("model", strInverter.getModel());
			eMap.put("modelId", strInverter.getId());
			eMap.put("typeId", 2);
			eMap.put("deviceIdentity", strInverter.getModelIdentity());
			Map<String, Object> typeMap = new HashMap<>();
			typeMap.put("typeId", 2);
			eMap.put("deviceType", typeMapper.getNameById(String.valueOf(2)));
			eMap.put("manufacturer", strInverter.getManufacturer());
			eMap.put("power", strInverter.getPower());
			eMap.put("efficace", strInverter.getNominalConversionEfficiency());
			eMap.put("mttpRoute", strInverter.getMpptChannels());
			eMap.put("maxRoute", strInverter.getMaxInputChannels());
			eMap.put("connTime", sdf.format(collDevice.getConnTime()));
			finalList.add(eMap);
		}
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", count);
		reMap.put("recordsFiltered", count);
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getModuleInfoforChoice(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 搜索设备名称、厂商、设备状态
		String modelIdentity = map.get("modelIdentity") + "";
		String power = map.get("power") + "";
		String manufacturer = map.get("manufacturer") + "";
		// 每页条数、起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		// 所有已绑定型号设备
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("modelIdentity", modelIdentity);
		paraMap.put("manufacturer", manufacturer);
		paraMap.put("power", power);
		paraMap.put("offset", start);
		paraMap.put("length", length);
		List<DeviceDetailModule> list = deviceModuleMapper.getModeleInfoSearch(paraMap);
		// 所有未绑定型号设备
		Integer totalModule = deviceModuleMapper.getModeleInfoSearchPage(paraMap);
		List<Map<String, Object>> finalList = new ArrayList<>();

		if (list.size() > 0) {
			for (DeviceDetailModule module : list) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("moduleIdentity", module.getModelIdentity());
				eMap.put("moduleId", module.getId());
				eMap.put("manufacturer", module.getManufacturer());
				eMap.put("moduleType", module.getSubassemblyType());
				eMap.put("power", module.getPower());
				finalList.add(eMap);
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalModule);
		reMap.put("recordsFiltered", totalModule);
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getModuleInfo(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 设备id
		String deviceId = map.get("deviceId") + "";
		// 每页条数、起始位置
		// int
		// start=Integer.parseInt("null".equals(String.valueOf(map.get("start")))?"0":String.valueOf(map.get("start")));
		// int
		// length=Integer.parseInt("null".equals(String.valueOf(map.get("length")))?"10":String.valueOf(map.get("length")));
		// 所有已绑定型号设备
		List<Map<String, Object>> list = moduleMapper.getEachModeleInfo(deviceId);
		// 所有未绑定型号设备
		Integer totalModule = moduleMapper.getTotalCount(deviceId);
		List<Map<String, Object>> finalList = new ArrayList<>();
		List<String> pIdList = new ArrayList<>();
		if (list.size() > 0) {
			for (Map<String, Object> module : list) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("moduleNum", module.get("modelIdentity"));
				eMap.put("moduleId", module.get("moduleId"));
				eMap.put("pvName", module.get("seriesName"));
				eMap.put("pvId", module.get("id"));
				eMap.put("deviceId", module.get("deviceId"));
				pIdList.add(String.valueOf(module.get("id")));
				eMap.put("manufacturer", module.get("manufacturer"));
				Double power = Double.parseDouble(module.get("power") + "");
				eMap.put("modulePower", power);
				Double count = Double.parseDouble(module.get("moduleNum") + "");
				// BigDecimal b = new BigDecimal(power);
				BigDecimal bg = new BigDecimal(power * count / 1000);
				double dcPower = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				eMap.put("count", count);
				eMap.put("moduleType", module.get("subassemblyType"));
				eMap.put("DCpower", dcPower);
				finalList.add(eMap);
			}
		}
		// 查询已被置空组串信息
		if (totalModule > list.size()) {
			List<String> allId = moduleMapper.getAllDeviceId(deviceId);
			allId.removeAll(pIdList);
			// 被置空组串信息的设备id集合
			List<String> pList = new ArrayList<>();
			pList.addAll(allId);
			Map<String, Object> mMap = new HashMap<>();
			mMap.put("pList", pList);
			List<DeviceSeriesModuleDetail> sList = moduleMapper.getOtherModule(mMap);
			if (sList.size() > 0) {
				for (DeviceSeriesModuleDetail sdmd : sList) {
					Map<String, Object> eem = new HashMap<>();
					eem.put("pvName", sdmd.getSeriesName());
					eem.put("pvId", sdmd.getId());
					eem.put("deviceId", sdmd.getDeviceId());
					eem.put("moduleNum", "");
					eem.put("modulePower", "");
					eem.put("moduleId", "");
					eem.put("count", "");
					eem.put("moduleType", "");
					eem.put("DCpower", "");
					eem.put("manufacturer", "");
					finalList.add(eem);
				}
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", list.size());
		reMap.put("recordsFiltered", list.size());
		reMap.put("data", finalList);
		reMap.put("realCount", totalModule);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean getYCList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 每页条数、起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		// 设备id
		String deviceId = map.get("deviceId") + "";
		String dataModel = deviceMapper.getDataModel(deviceId);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("dataModel", dataModel);
		paraMap.put("offset", start);
		paraMap.put("length", length);
		List<CollModelDetailMqtt> ycs = mqttMapper.getYCList(paraMap);
		Integer totalCount = mqttMapper.getTotalCount(paraMap);
		List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
		if (ycs.size() > 0) {
			for (CollModelDetailMqtt yc : ycs) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("signName", yc.getSignalName());
				eMap.put("dataGain", yc.getDataGain());
				eMap.put("unit", yc.getDataUnit());
				eMap.put("correctionFactor", yc.getCorrectionFactor() == null ? "--" : yc.getCorrectionFactor());
				eMap.put("mixValue", yc.getMinval() == null ? "--" : yc.getMinval());
				eMap.put("maxValue", yc.getMaxval() == null ? "--" : yc.getMaxval());
				eMap.put("vueGroup", yc.getVisibility());
				eMap.put("specialProcess", yc.getSpecialProcess() == null ? "N/A" : yc.getSpecialProcess());
				paraMap.put("guid", yc.getSignalGuid());
				String imId = guidMapper.getImId(paraMap);
				String imName = labelMapper.getImName(imId);
				eMap.put("imName", imName);
				finalList.add(eMap);
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalCount);
		reMap.put("recordsFiltered", totalCount);
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getYXList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 每页条数、起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		// 设备id
		String deviceId = map.get("deviceId") + "";
		String dataModel = deviceMapper.getDataModel(deviceId);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("dataModel", dataModel);
		paraMap.put("offset", start);
		paraMap.put("length", length);
		List<Map<String, String>> yxs = mqttMapper.getYXList(paraMap);
		Integer totalCount = mqttMapper.getTotalYX(paraMap);
		List<Map<String, Object>> fList = new ArrayList<>();
		if (yxs.size() > 0) {
			for (Map<String, String> yxMap : yxs) {
				Map<String, Object> mMap = new HashMap<>();
				mMap.put("signalName", yxMap.get("signalName"));
				mMap.put("statusValue", yxMap.get("statusValue"));
				mMap.put("statusName", yxMap.get("statusName"));
				mMap.put("alarmLevel", yxMap.get("alarmLevel"));
				mMap.put("alarmBitLength", yxMap.get("alarmBitLength"));
				mMap.put("alarmReason", yxMap.get("alarmReason") == null ? "--" : yxMap.get("alarmReason"));
				mMap.put("suggest", yxMap.get("suggest") == null ? "--" : yxMap.get("suggest"));
				if (yxMap.get("startBit") == null) {
					mMap.put("startBit", "--");
				} else {
					mMap.put("startBit", yxMap.get("startBit"));
				}
				if ("1".equals(String.valueOf(yxMap.get("visibility")))) {
					mMap.put("visibility", "是");
				} else if ("0".equals(String.valueOf(yxMap.get("visibility")))) {
					mMap.put("visibility", "否");
				} else {
					mMap.put("visibility", "--");
				}
				if ("1".equals(String.valueOf(yxMap.get("push")))) {
					mMap.put("push", "是");
				} else if ("0".equals(String.valueOf(yxMap.get("push")))) {
					mMap.put("push", "否");
				} else {
					mMap.put("push", "--");
				}
				if ("1".equals(String.valueOf(yxMap.get("alarmType")))) {
					mMap.put("alarmType", "告警");
				} else if ("0".equals(String.valueOf(yxMap.get("alarmType")))) {
					mMap.put("alarmType", "事件");
				} else {
					mMap.put("alarmType", "--");
				}
				if ("1".equals(String.valueOf(yxMap.get("ycAlarm")))) {
					mMap.put("ycAlarm", "是");
				} else if ("0".equals(String.valueOf(yxMap.get("ycAlarm")))) {
					mMap.put("ycAlarm", "否");
				} else {
					mMap.put("ycAlarm", "--");
				}
				if ("1".equals(String.valueOf(yxMap.get("faultPoint")))) {
					mMap.put("faultPoint", "是");
				} else if ("0".equals(String.valueOf(yxMap.get("faultPoint")))) {
					mMap.put("faultPoint", "否");
				} else {
					mMap.put("faultPoint", "--");
				}
				fList.add(mMap);
			}
		}
		Map<String, Object> reMap = new HashMap<>();
		reMap.put("data", fList);
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalCount);
		reMap.put("recordsFiltered", totalCount);
		msg.setBody(reMap);
		return msg;
	}

	@Override
	public MessageBean getDeviceChoiceTree(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 请求类型 0设备类型 1型号 2点表
		String requestType = map.get("requestType") + "";
		// 型号id
		String modelId = map.get("modelId") + "";
		// 设备类型
		String id = map.get("typeId") + "";
		Integer choice = null;
		Map<String, Object> finalMap = new HashMap<>();
		Map<String, Object> paraMap = new HashMap<>();
		if (Util.isNotBlank(requestType)) {
			choice = Integer.parseInt(requestType);
		} else {
			choice = 0;
		}
		List<Map<String, Object>> fList = new ArrayList<>();
		// 所有设备类型
		if (choice == 0) {
			List<DeviceType> deviceTypeInfo = typeMapper.getDeviceTypeInfo();
			if (deviceTypeInfo.size() > 0) {
				for (DeviceType deviceType : deviceTypeInfo) {
					// 暂时只考虑集中、组串逆变器、汇流箱
					Map<String, Object> mm = new HashMap<>();
					Integer id2 = deviceType.getId();
					String typeName = deviceType.getTypeName();
					mm.put("typeId", id2);
					mm.put("typeName", typeName);
					fList.add(mm);
				}
			}
			finalMap.put("data", fList);
			// 可选型号
		} else if (choice == 1) {
			Integer typeId = null;
			if (Util.isNotBlank(id)) {
				typeId = Integer.parseInt(id);
			}
			paraMap.put("deviceType", typeId);
			List<DeviceDetailStringInverter> allModel = deviceMapper.getM4Device4Choice(paraMap);
			if (allModel.size() > 0) {
				for (DeviceDetailStringInverter cInverter : allModel) {
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("modelId", cInverter.getId());
					paramMap.put("model", cInverter.getModelIdentity());
					fList.add(paramMap);
				}
			}
			finalMap.put("data", fList);
		} else {
			Integer id2 = null;
			if (Util.isNotBlank(modelId)) {
				id2 = Integer.parseInt(modelId);
			}
			paraMap.put("id", id2);
			paraMap.put("typeId", id);
			List<CollModel> list = modelMapper.getModel4Choice(paraMap);
			if (list.size() > 0) {
				for (CollModel collModel : list) {
					Map<String, Object> mm = new HashMap<>();
					mm.put("modeId", collModel.getId());
					mm.put("modeName", collModel.getModeName());
					fList.add(mm);
				}
			}
			finalMap.put("data", fList);
		}

		msg.setBody(finalMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getYKList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		// 每页条数、起始位置
		int start = Integer
				.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
		int length = Integer
				.parseInt("null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
		// 设备id
		String deviceId = map.get("deviceId") + "";
		String dataModel = deviceMapper.getDataModel(deviceId);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("dataModel", dataModel);
		paraMap.put("offset", start);
		paraMap.put("length", length);
		List<Map<String, Object>> yks = mqttMapper.getYKList(paraMap);
		Integer totalCount = mqttMapper.getTotalYK(paraMap);
		List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
		Map<String, Object> reMap = new HashMap<>();
		if (yks.size() > 0) {
			for (Map<String, Object> yc : yks) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("signName", yc.get("signalName"));
				eMap.put("statusValue", yc.get("statusValue"));
				eMap.put("mixValue", yc.get("mixValue") == null ? "--" : yc.get("mixValue"));
				eMap.put("maxValue", yc.get("maxValue") == null ? "--" : yc.get("maxValue"));
				eMap.put("dataGain", yc.get("dataGain") == null ? "--" : yc.get("dataGain"));
				String tp = yc.get("realType") + "";
				if ("YK".equals(tp)) {
					eMap.put("realType", "遥控");
				} else if ("YT".equals(tp)) {
					eMap.put("realType", "摇调");
				} else {
					eMap.put("realType", "--");
				}
				finalList.add(eMap);
			}
		}
		reMap.put("draw", String.valueOf(map.get("draw")));
		reMap.put("recordsTotal", totalCount);
		reMap.put("recordsFiltered", totalCount);
		reMap.put("data", finalList);
		msg.setBody(reMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getAllCollNames(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List<DeviceDetailDataCollector> colls = collMapper.getCollModelNames();
		List<Map<String, Object>> finalList = new ArrayList<>();
		Map<String, Object> reMap = new HashMap<>();
		if (colls.size() > 0) {
			for (DeviceDetailDataCollector coll : colls) {
				Map<String, Object> mm = new HashMap<>();
				mm.put("topModelId", coll.getId());
				mm.put("topModelName", coll.getModel());
				finalList.add(mm);
			}
			reMap.put("data", finalList);
			msg.setBody(reMap);
		} else {
			msg.setBody(null);
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean addCamera(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 摄像头名称、型号、编号、组织、电站
		String cameraName = map.get("cameraName") + "";
		String modelId = map.get("modelId") + "";
		String cameraSN = map.get("cameraSN") + "";
		String orgId = map.get("orgId") + "";
		String plantId = map.get("plantId") + "";
		Map<String, Object> paramMap = new HashMap<>();
		if (StringUtil.isBlank(cameraName)) {
			paramMap.put("cameraName", null);
		} else {
			paramMap.put("cameraName", cameraName);
		}
		if (StringUtil.isBlank(modelId)) {
			paramMap.put("modelId", null);
		} else {
			paramMap.put("modelId", modelId);
		}
		if (StringUtil.isBlank(orgId)) {
			paramMap.put("orgId", null);
		} else {
			paramMap.put("orgId", orgId);
		}
		if (StringUtil.isBlank(plantId)) {
			paramMap.put("plantId", null);
		} else {
			paramMap.put("plantId", plantId);
		}
		paramMap.put("cameraSN", cameraSN);
		paramMap.put("connTime", System.currentTimeMillis());
		String cSN = deviceMapper.getSNExist(cameraSN);
		// 判断sn号是否唯一
		if (cSN == null) {
			Integer result = null;
			// 电站下新增摄像头
			if (!StringUtil.isBlank(plantId)) {
				result = deviceMapper.addCamera(paramMap);
				if (result > 0) {
					logger.info("电站新增摄像头成功,操作人:" + logUserName + "_新增摄像sn号：" + cameraSN);
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ADD_SUCCUESS);
				} else {
					logger.error("电站新增摄像头失败,操作人:" + logUserName + "_新增摄像sn号：" + cameraSN);
					throw new ServiceException(Msg.ADD_FAILED);
				}
				// 组织下新增摄像头
			} else if (StringUtil.isBlank(plantId) && Util.isNotBlank(orgId)) {
				result = deviceMapper.addCamera4Org(paramMap);
				if (result > 0) {
					logger.info("组织新增摄像头成功,操作人:" + logUserName + "_新增摄像sn号：" + cameraSN);
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.ADD_SUCCUESS);
				} else {
					logger.error("组织新增摄像头失败,操作人:" + logUserName + "_新增摄像sn号：" + cameraSN);
					throw new ServiceException(Msg.ADD_FAILED);
				}
			} else {
				logger.error("新增摄像头失败,操作人:" + logUserName + "_新增摄像sn号：" + cameraSN);
				throw new ServiceException(Msg.ADD_CAMERA_SANS_ANYID);
			}

		} else {
			logger.error(" 新增摄像头sn号已存在，新增失败! 操作人: " + logUserName + "_数采sn：" + cameraSN);
			throw new ServiceException(Msg.CAMERASN_EXIST);
		}

		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean getCameraList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 查询摄像头列表信息
		List<CollDevice> cameraList4YS = this.getCameraList4YS();
		Integer result = null;
		if (cameraList4YS.size() > 0) {
			// 更新摄像头列表状态
			for (CollDevice collDevice : cameraList4YS) {
				result = deviceMapper.updateCameraStatus(collDevice);
				if (result > 0) {
					logger.error(" 更新摄像头状态成功，更新成功! 操作人: " + logUserName + "_摄像头sn：" + collDevice.getDeviceSn());
					continue;
				} else {
					logger.error(" 更新摄像头状态失败，更新失败! 操作人: " + logUserName + "_摄像头sn：" + collDevice.getDeviceSn());
					throw new ServiceException(Msg.UPDATE_FAILED);
				}
			}
			Map<String, Object> reMap = new HashMap<>();
			// if (result>0) {
			// 当前电站所属组织
			String plantId = map.get("plantId") + "";
			String orgId = map.get("orgId") + "";
			// 搜索设备名称、厂商
			String cameraName = map.get("cameraName") + "";
			String manufacture = map.get("manufacture") + "";
			// 每页条数、起始位置
			int start = Integer
					.parseInt("null".equals(String.valueOf(map.get("start"))) ? "0" : String.valueOf(map.get("start")));
			int length = Integer.parseInt(
					"null".equals(String.valueOf(map.get("length"))) ? "10" : String.valueOf(map.get("length")));
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("deviceName", cameraName);
			paraMap.put("manufacture", manufacture);
			paraMap.put("length", length);
			paraMap.put("offset", start);
			paraMap.put("plantId", plantId);
			paraMap.put("orgId", orgId);
			List<Map<String, Object>> cameras = null;
			Integer totalCount = null;
			if (Util.isNotBlank(plantId)) {
				// 根据电站id得到所有逆变器集合
				cameras = deviceMapper.getCameraListByPid(paraMap);
				totalCount = deviceMapper.getTotalCameraByPid(paraMap);
			} else {
				// 根据组织id得到逆变器集合
				List<SysOrg> orgList = orgMapper.getAllOrg();
				List<String> reList = new ArrayList<>();
				reList.add(orgId + "");
				reList = ServiceUtil.getTree(orgList, orgId + "", reList);
				paraMap.put("reList", reList);
				cameras = deviceMapper.getCameraListByOid(paraMap);
				totalCount = deviceMapper.getTotalCameraByOid(paraMap);
			}
			List<Map<String, Object>> finalList = new ArrayList<>();
			if (cameras.size() > 0) {
				for (Map<String, Object> camera : cameras) {
					Map<String, Object> eMap = new HashMap<>();
					// 摄像头名称、型号、sn号、厂商、功率、所属电站、组织、状态
					eMap.put("cameraId", camera.get("cameraId"));
					eMap.put("cameraName", camera.get("cameraName"));
					eMap.put("cameraSN", camera.get("cameraSN"));
					eMap.put("manufacture", camera.get("manufacture"));
					eMap.put("cameraModel", camera.get("modelName"));
					eMap.put("cameraResolution", camera.get("cameraResolution"));
					eMap.put("channel", 1);
					eMap.put("sn", camera.get("cameraSN"));
					String oid = String.valueOf(camera.get("orgId"));
					SysOrg org = orgMapper.getOrgName(oid);
					eMap.put("orgId", oid);
					eMap.put("orgName", org.getOrgName());
					String pid = String.valueOf(camera.get("plantId"));
					if (Util.isNotBlank(pid)) {
						PlantInfo plant = plantInfoMapper.getPlantNameById(pid);
						if (plant != null) {
							eMap.put("plantId", pid);
							eMap.put("plantName", plant.getPlantName());
						} else {
							eMap.put("plantId", pid);
							eMap.put("plantName", "--");
						}
						// 组织下摄像头暂无电站id时
					} else {
						eMap.put("plantId", "--");
						eMap.put("plantName", "--");
					}
					String cameraStatus = String.valueOf(camera.get("cameraStatus"));
					eMap.put("status", cameraStatus);
					finalList.add(eMap);
				}
			}
			reMap.put("draw", String.valueOf(map.get("draw")));
			reMap.put("recordsTotal", totalCount);
			reMap.put("recordsFiltered", totalCount);
			reMap.put("data", finalList);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.SELECT_SUCCUESS);
			msg.setBody(reMap);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.SANS_CAMERA_2_SEARCH);
			msg.setBody(null);
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean delCamera(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 摄像头id
		String cameraIds = map.get("cameraIds") + "";
		if (Util.isNotBlank(cameraIds)) {
			String[] split = cameraIds.split(",");
			List<String> idList = Arrays.asList(split);
			List<String> idsList = new ArrayList<>();
			idsList.addAll(idList);
			Map<String, Object> eMap = new HashMap<>();
			eMap.put("idsList", idsList);
			Integer re = deviceMapper.delCamera(eMap);
			if (re > 0) {
				logger.info("删除摄像成功,操作人:" + logUserName + "_删除摄像头id：" + idsList.toString());
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_SUCCUESS);
			} else {
				logger.error("删除摄像头失败,操作人:" + logUserName + "_删除摄像头sn号：" + idsList.toString());
				throw new ServiceException(Msg.DELETE_FAILED);
			}
		}
		return msg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public MessageBean updateCamera(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String tokenId = map.get("tokenId") + "";
		// 登录用户
		User user = session.getAttribute(tokenId);
		String logUserName = StringUtil.appendStrNoBlank(user.getId(), "_", user.getUserName());
		// 摄像头id
		String cameraId = map.get("cameraId") + "";
		String cameraName = map.get("cameraName") + "";
		String cameraModelId = map.get("cameraModelId") + "";
		String cameraSN = map.get("cameraSN") + "";
		String orgId = map.get("orgId") + "";
		String plantId = map.get("plantId") + "";
		// 验证设备sn号唯一性
		Map<String, Object> cMap = new HashMap<>();
		cMap.put("id", cameraId);
		cMap.put("sn", cameraSN);
		String cSN = deviceMapper.getSNExist4Update(cMap);
		if (cSN == null) {
			if (StringUtil.isBlank(cameraModelId)) {
				cMap.put("cameraModelId", null);
			} else {
				cMap.put("cameraModelId", cameraModelId);
			}
			if (StringUtil.isBlank(orgId)) {
				cMap.put("orgId", null);
			} else {
				cMap.put("orgId", orgId);
			}
			if (StringUtil.isBlank(cameraName)) {
				cMap.put("cameraName", null);
			} else {
				cMap.put("cameraName", cameraName);
			}
			if (!StringUtil.isBlank(plantId)) {
				cMap.put("plantId", plantId);
			} else {
				cMap.put("plantId", null);
			}
			Integer re = deviceMapper.updateCmaera(cMap);
			if (re > 0) {
				logger.info(" 编辑摄像头信息成功,操作人:" + logUserName + "_编辑摄像头sn号：" + cameraSN);
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPDATE_SUCCUESS);
			} else {
				logger.error(" 编辑摄像头信息失败,操作人:" + logUserName + "_编辑摄像头sn号：" + cameraSN);
				throw new ServiceException(Msg.UPDATE_FAILED);
			}
		} else {
			logger.error(" 填写sn号已存在，编辑摄像头信息失败,操作人:" + logUserName + "_编辑摄像头sn号：" + cameraSN);
			throw new ServiceException(Msg.CAMERASN_EXIST);
		}
		return msg;
	}

	@Override
	public MessageBean getCaModelList(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List<DeviceDetailCamera> models = cameraMapper.getAllType();
		List<Map<String, Object>> fList = new ArrayList<>();
		if (models.size() > 0) {
			for (DeviceDetailCamera cameraModel : models) {
				Map<String, Object> eMap = new HashMap<>();
				eMap.put("modelId", cameraModel.getId());
				eMap.put("modelName", cameraModel.getModelIdentity());
				fList.add(eMap);
			}
		}
		Map<String, Object> fMap = new HashMap<>();
		fMap.put("data", fList);
		msg.setBody(fMap);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MessageBean getLiveHLS(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {

		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String id = map.get("id") + "";
		Map<String, Object> cMapp = new HashMap<>();
		// 根据id获取摄像头序列号、通道号
		String serial = deviceMapper.getCameraSerial(id);
		if (!StringUtil.isBlank(serial)) {
			// 创建默认的httpClient实例.
			CloseableHttpClient httpclient = HttpClients.createDefault();
			// 创建httppost
			HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/live/address/get");
			// 创建参数队列
			List formparams = new ArrayList();
			String token = new String(SystemCache.accessToken);
			formparams.add(new BasicNameValuePair("accessToken", token));
			/*
			 * List<CameraInfo> camera = UpdateYSToken.cameraListInfo(); StringBuffer sb =
			 * new StringBuffer(); for (int i = 0; i < camera.size(); i++) { if
			 * (i!=camera.size()-1) {
			 * sb.append(camera.get(i).getCameraSerial()).append(":"+camera.get(i).
			 * getChannelNum()+","); } else {
			 * sb.append(camera.get(i).getCameraSerial()).append(":"+camera.get(i).
			 * getChannelNum()); } }
			 */
			// formparams.add(new BasicNameValuePair("source", sb.toString()));
			formparams.add(new BasicNameValuePair("source", serial + ":" + 1));
			UrlEncodedFormEntity uefEntity;
			// 存储token实体类
			List<CollDevice> list = new ArrayList<>();
			try {
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				httppost.setEntity(uefEntity);
				CloseableHttpResponse response = httpclient.execute(httppost);
				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String resultStr = EntityUtils.toString(entity, "UTF-8");
						JSONObject json = (JSONObject) JSONObject.parse(resultStr);
						List<Map<String, String>> data = (List<Map<String, String>>) json.get("data");
						if (Util.isNotBlank(data)) {
							for (Map<String, String> hmap : data) {
								CollDevice ci = new CollDevice();
								ci.setHls(String.valueOf(hmap.get("hlsHd")));
								ci.setRtmp(String.valueOf(hmap.get("rtmpHd")));
								list.add(ci);
							}
							msg.setMsg(Msg.SELECT_SUCCUESS);
						} else {
							msg.setMsg(Msg.NOT_FIND_CAMERA_SERRIAL);
						}
					}
				} finally {
					response.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭连接,释放资源
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (Util.isNotBlank(list)) {
				for (CollDevice cameraInfo : list) {
					cMapp.put("hls", cameraInfo.getHls());
					cMapp.put("rtmp", cameraInfo.getRtmp());
				}
			}
			msg.setBody(cMapp);
			msg.setCode(Header.STATUS_SUCESS);

		} else {
			msg.setBody(cMapp);
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg(Msg.NOT_FIND_CAMERA_SERRIAL);
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public List<CollDevice> getCameraList4YS() throws SessionException, ServiceException, SessionTimeoutException {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/camera/list");
		// 创建参数队列
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("Host", "open.ys7.com"));
		formparams.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		String tk = new String(SystemCache.accessToken);
		formparams.add(new BasicNameValuePair("accessToken", tk));
		formparams.add(new BasicNameValuePair("pageStart", "0"));
		formparams.add(new BasicNameValuePair("pageSize", "10"));
		UrlEncodedFormEntity uefEntity;
		// 存储token实体类
		List<CollDevice> list = new ArrayList<>();
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resultStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject json = (JSONObject) JSONObject.parse(resultStr);
					List<Map<String, String>> data = (List<Map<String, String>>) json.get("data");
					// System.out.println(jsonData);
					for (Map<String, String> map : data) {
						CollDevice ci = new CollDevice();
						ci.setDeviceName(map.get("channelName"));
						ci.setDeviceStatus(String.valueOf(map.get("status")));
						ci.setDeviceSn(String.valueOf(map.get("deviceSerial")));
						ci.setChannelNum(1);
						list.add(ci);
					}
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	@Override
	public MessageBean plantIdofOrg4Device(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String orgId = map.get("orgId") + "";
		// List<Map<String,Object>> orgIds = deviceMapper.getPlantIdofOrg(orgId);
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		reList.add(orgId + "");
		reList = ServiceUtil.getTree(orgList, orgId + "", reList);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("reList", reList);
		List<PlantInfo> plantByOrgId = plantInfoMapper.getPlantByOrgIdTree(paraMap);
		List<Map<String, Object>> fList = new ArrayList<>();
		if (plantByOrgId.size() > 0) {
			for (PlantInfo plantInfo : plantByOrgId) {
				Map<String, Object> emap = new HashMap<>();
				emap.put("plantId", plantInfo.getId());
				emap.put("plantName", plantInfo.getPlantName());
				fList.add(emap);
			}
		}
		Map<String, Object> fMap = new HashMap<>();
		fMap.put("data", fList);
		msg.setBody(fMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	public MessageBean plantIdofOrg4Camera(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String orgId = map.get("orgId") + "";
		// 根据组织id得到逆变器集合
		List<SysOrg> orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>();
		reList.add(orgId + "");
		reList = ServiceUtil.getTree(orgList, orgId + "", reList);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("reList", reList);
		List<Map<String, Object>> orgIds = deviceMapper.getPlantIdofOrg4Camera(paraMap);
		Map<String, Object> fMap = new HashMap<>();
		fMap.put("data", orgIds);
		msg.setBody(fMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MessageBean getCameraListYS(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/camera/list");
		// 创建参数队列
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("Host", "open.ys7.com"));
		formparams.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		String tk = new String(SystemCache.accessToken);
		formparams.add(new BasicNameValuePair("accessToken", tk));
		formparams.add(new BasicNameValuePair("pageStart", "0"));
		formparams.add(new BasicNameValuePair("pageSize", "10"));
		UrlEncodedFormEntity uefEntity;
		// 存储token实体类
		List<CollDevice> list = new ArrayList<>();
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resultStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject json = (JSONObject) JSONObject.parse(resultStr);
					List<Map<String, String>> data = (List<Map<String, String>>) json.get("data");
					// System.out.println(jsonData);
					for (Map<String, String> map : data) {
						CollDevice ci = new CollDevice();
						ci.setDeviceName(map.get("channelName"));
						ci.setDeviceStatus(String.valueOf(map.get("status")));
						ci.setDeviceSn(String.valueOf(map.get("deviceSerial")));
						ci.setChannelNum(1);
						list.add(ci);
					}
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		msg.setBody(list);
		return msg;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MessageBean getVedioByTime(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {

		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		String deviceSerial = String.valueOf(map.get("deviceSerial"));
		String startTime = String.valueOf(map.get("startTime"));
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/video/by/time");
		// 创建参数队列
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("Host", "open.ys7.com"));
		formparams.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		String tk = new String(SystemCache.accessToken);
		formparams.add(new BasicNameValuePair("accessToken", tk));
		formparams.add(new BasicNameValuePair("deviceSerial", deviceSerial));
		formparams.add(new BasicNameValuePair("startTime", startTime));
		UrlEncodedFormEntity uefEntity;
		// 存储token实体类
		List<Map<String, String>> list = new ArrayList<>();
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resultStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject json = (JSONObject) JSONObject.parse(resultStr);
					List<Map<String, Object>> data = (List<Map<String, Object>>) json.get("data");
					for (Map<String, Object> map2 : data) {
						Map<String, String> ee = new HashMap<>();
						ee.put("startTime", String.valueOf(map2.get("startTime")));
						list.add(ee);
					}
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		msg.setBody(list);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getDevType(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List resultList = new ArrayList<>();
		// 暂时只考虑两种逆变器
		List<DeviceType> typeList = typeMapper.getDeviceTypeInfo();
		if (Util.isNotBlank(typeList)) {
			for (int i = 0, size = typeList.size(); i < size; i++) {
				DeviceType type = typeList.get(i);
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("id", type.getId() + "");
				tempMap.put("name", type.getTypeName());
				resultList.add(tempMap);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean getModel(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List resultList = new ArrayList<>();
		Map<String, String> map = Util.parseURLCode(jsonData);
		String typeId = map.get("id") + "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deviceType", typeId);
		List<DeviceDetailStringInverter> allModel = deviceMapper.getM4Device4Choice(paramMap);
		if (Util.isNotBlank(allModel)) {
			for (int i = 0, size = allModel.size(); i < size; i++) {
				DeviceDetailStringInverter inverter = allModel.get(i);
				if (inverter != null) {
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("id", inverter.getId() + "");
					tempMap.put("name", inverter.getModelIdentity());
					resultList.add(tempMap);
				}
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}

	@Override
	public MessageBean updatePointTableWx(String jsonData, Session session)
			throws SessionException, ServiceException, SessionTimeoutException {
		Map<String, Object> map = Util.parseURL(jsonData);
		MessageBean msg = new MessageBean();
		User user = session.getAttribute(map.get("tokenId") + "");
		List<Map<String, Object>> collector = (List<Map<String, Object>>) map.get("collector");
		List<String> pointTable = new ArrayList<String>();
		List<String> returnMessage = new ArrayList<String>();
		for (Map<String, Object> map2 : collector) {
			Map<Integer, Map<String, Integer>> resultMap = new HashMap<>();
			String collSn = map2.get("no") + "";
			List<Map<String, Object>> serialPort = (List<Map<String, Object>>) map2.get("serialPort");
			Map<String, Integer> tempMap1 = new HashMap<>();
			Map<String, Integer> tempMap2 = new HashMap<>();
			for (Map<String, Object> map3 : serialPort) {
				String comId = map3.get("com_id") + "";
				List<Map<String, Object>> datas = (List<Map<String, Object>>) map3.get("datas");
				for (Map<String, Object> map4 : datas) {
					String slaveId = map4.get("addr") + "";
					String modelId = map4.get("model_id") + "";
					String typeId = map4.get("type_id") + "";
					// 获取dataMode
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("deviceModelId", modelId);
					paramMap.put("typeId", typeId);
					int dataModeId = modelMapper.getDateModelId(paramMap);
					if ("1".equals(comId)) {
						tempMap1.put(slaveId, dataModeId);
					} else if ("2".equals(comId)) {
						tempMap2.put(slaveId, dataModeId);
					}
				}
			}
			resultMap.put(1, tempMap1);
			resultMap.put(2, tempMap2);
			logger.info("就是这个：" + resultMap);
			boolean b = false;
			try {
				b = iYkYtService.dataModelSet(collSn, resultMap);
				logger.info("指令下发代理服务器成功, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collSn);
			} catch (Exception e) {
				logger.error("指令下发代理服务器失败, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collSn);
//				throw new ServiceException("保存成功,数采"+collSn+"下发点表失败，请在编辑页面重试");
			}
			if (b) {
				Map<String, Object> reult = RabbitMessage.getReturnDataModeMessage(collSn);
				if (reult != null && !reult.isEmpty()) {
					// TODO
					logger.info(" 服务器回执信息判定成功!");
					msg.setCode(Header.STATUS_SUCESS);
					msg.setMsg(Msg.YKYT_SUCCESS);
				} else {
//					msg.setCode(Header.STATUS_FAILED);
//					msg.setMsg(Msg.YKYT_FAILD);
//					logger.error( "服务器回执信息判定失败!");
					returnMessage.add(collSn);
				}
			} else {
//				msg.setCode(Header.STATUS_FAILED);
//				msg.setMsg(Msg.YKYT_FAILD);
				pointTable.add(collSn);
			}
		}
		String pointValue = "";
		if (Util.isNotBlank(pointTable)) {
			for (String string : pointTable) {
				pointValue += string + " ";
			}
			pointValue = "数采" + pointValue + "下发点表失败!";
		}
		String messageValue = "";
		if (Util.isNotBlank(returnMessage)) {
			for (String string : returnMessage) {
				messageValue += string + " ";
			}
			messageValue = "数采" + messageValue + "的服务器回执信息判定失败!";
		}
		if (Util.isNotBlank(pointValue) || Util.isNotBlank(messageValue)) {
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg(pointValue + messageValue);
		} else {
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("点表下发成功");
		}
		return msg;
	}
}
