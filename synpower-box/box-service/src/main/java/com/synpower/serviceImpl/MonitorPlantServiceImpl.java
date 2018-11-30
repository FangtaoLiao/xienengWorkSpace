package com.synpower.serviceImpl;

import com.synpower.bean.*;
import com.synpower.constant.DTC;
import com.synpower.constant.TimeEXP;
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
import com.synpower.service.*;
import com.synpower.strategy.CommonSerch.CommonSearchContext;
import com.synpower.strategy.CommonSerch.CommonSearchException;
import com.synpower.strategy.CommonSerch.SearchCondition;
import com.synpower.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MonitorPlantServiceImpl implements MonitorPlantService {
    private Logger logger = Logger.getLogger(MonitorPlantServiceImpl.class);
    @Autowired
    private PlantInfoMapper plantMapper;
    @Autowired
    private CollModelDetailMqttMapper mqttMapper;
    @Autowired
    private CollDeviceMapper deviceMapper;
    @Autowired
    private CollYxExpandMapper collYxExpandMapper;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private IYkYtService iYkYtService;
    @Autowired
    private DeviceControlDataMapper controlMapper;
    @Autowired
    private CollYkytExpandMapper ykytExpandMapper;
    @Autowired
    private DataCentralInverterMapper centralInverterMapper;
    @Autowired
    private DataElectricMeterMapper electricMapper;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private StatisticalGainService statisticalGainService;
    @Autowired
    private PowerPriceMapper priceMapper;
    @Autowired
    private SysOrgMapper orgMapper;
    @Autowired
    private SysAddressMapper addMapper;
    @Autowired
    private ScreenService screenImpl;
    @Autowired
    private PlantInfoService plantInfoService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private WXmonitorService wxmonitorService;
    @Autowired
    private DeviceDetailCameraMapper cameraMapper;
    @Autowired
    private PlantReportService plantReportService;
    @Autowired
    private MonitorServiceImpl monitorServiceImpl;
    @Autowired
    private DataPcsMapper pcsMapper;
    @Autowired
    private DataBmsMapper bmsMapper;
    @Autowired
    private PlantTypeBMapper typeBMapper;
    @Autowired
    private EnergyStorageDataMapper energyMapper;
    @Autowired
    private DataBoxChangeMapper boxChangeMapper;
    @Autowired
    private DataEnvMonitorMapper envMonitorMapper;
    @Autowired
    CommonSearchContext commonSearchContext;

    // @Autowired
//	private UpdateWeather updateWeather;
    @Override
    public MessageBean singleBasicInfo(String jsonData) throws ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        PlantInfo plantInfo = plantMapper.getPlantInfo(String.valueOf(map.get("plantId")));
        String pId = map.get("plantId") + "";
        map.clear();
        map.put("address", plantInfo.getPlantAddr() + plantInfo.getPlantFullAddress());
        String[] locaiton = plantInfo.getLoaction().split(",");
        String temp = locaiton[0];
        locaiton[0] = locaiton[1];
        locaiton[1] = temp;
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
        map.put("cameraInfo", cameraMap);
        map.put("img", photos);
        map.put("location", locaiton);
        map.put("name", plantInfo.getPlantName());
        SysUser user = plantInfo.getContactUser();
        map.put("contacts", user == null ? "" : user.getUserName());
        map.put("plantType",
                plantInfo.getPlantTypeAInfo().getTypeName() + " " + plantInfo.getPlantTypeBInfo().getTypeName());
        map.put("contactsTel", user == null ? "" : user.getUserTel());
        map.put("connectTime", TimeUtil.formatTime(plantInfo.getGridConnectedDate(), "yyyy年MM月dd日"));
        map.put("plantArea", plantInfo.getPlantArea() + "平方米");
        map.put("desc", StringUtil.checkBlank(plantInfo.getPlantIntroduction()));
        msg.setBody(map);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    /**
     * @Title: deviceDetail
     * @Description: 设备详情
     * @return: MessageBean
     * @lastEditor: SP0011
     * @lastEdit: 2017年11月25日上午10:36:49
     */
    @Override
    public MessageBean deviceDetail(String jsonData) throws ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String deviceId = String.valueOf(map.get("deviceId"));
        CollDevice device = deviceMapper.getDeviceById(deviceId);
        DeviceDetailStringInverter mode = device.getDeviceDetailStringInverter();
        Map<String, String> basicInfo = new HashMap<>(9);
        map.clear();
        basicInfo.put("connTime", Util.timeFormate(device.getConnTime(), "yyyy-MM-dd"));
        basicInfo.put("deviceId", device.getId() + "");
        basicInfo.put("deviceModel", mode == null ? "" : mode.getModel());
        basicInfo.put("deviceName", device.getDeviceName());
        basicInfo.put("image", mode == null ? "" : mode.getPhotoPath());
        basicInfo.put("mark", mode == null ? "" : mode.getManufacturer());
        basicInfo.put("power",
                mode == null ? "NaN" : Util.isNotBlank(mode.getPower()) ? mode.getPower() + "千瓦" : "NaN");
        basicInfo.put("deviceSN", device.getDeviceSn());
        msg.setBody(basicInfo);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    /**
     * @Title: deviceStatus
     * @Description: 设备 数据 、 状态 、 远程控制
     * @return: MessageBean
     * @lastEditor: SP0011
     * @lastEdit: 2017年11月25日10:41:21
     */
    @Override
    public MessageBean deviceStatus(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String deviceId = String.valueOf(map.get("deviceId"));
        String type = String.valueOf(map.get("type"));
        CollDevice device = deviceMapper.getDeviceById(deviceId);
        int deviceType = device.getDeviceType();
        String dataModel = String.valueOf(device.getDataMode());
        List<List<Object>> reultList = new ArrayList<>();
        /**
         * 数据模块
         */
        if ("0".equals(type)) {
            List<Object> tempList = new ArrayList<>();
            Map<String, String> guidMap = cacheUtil.getMap(deviceId);

            // 此处分为两种 一.从缓存中能取出给设备的点表信息 二.不能从缓存中取出点表信息

            // 一.从缓存中能取出给设备的点表信息
            if (Util.isNotBlank(guidMap)) {
                // 1 2 逆变器数据
                // 9 PCS数据
                // 10 BMS数据
                // 11 汇流箱数据
                // 12 箱变数据
                // 13 环境监测器
                if (DTC.STRING_INVERTER == deviceType || DTC.CENTRAL_INVERTER == deviceType || DTC.PCS == deviceType
                        || DTC.BMS == deviceType || DTC.COMBINER_BOX == deviceType || DTC.BOX_CHANGE == deviceType
                        || DTC.ENV_MONITOR == deviceType) {
                    // dataModel 数据模型
                    // guidMap 设备点表状态集 redis中取出
                    // deviceType 设备类型
                    reultList = getDeviceNum(reultList, dataModel, guidMap, deviceType);

                    // 3 电表
                } else if (DTC.ELECTRIC_METER == deviceType) {
                    String[] params = new String[]{"em_ua", "em_ub", "em_uc", "em_uab", "em_ubc", "em_uca", "em_ia",
                            "em_ib", "em_ic", "em_frequency", "em_active_power", "em_reactive_power",
                            "em_power_factor"};
                    Map<String, Object> paramMap = new ConcurrentHashMap<>();
                    paramMap.put("tableName", "data_electric_meter");
                    paramMap.put("deviceId", deviceId);
                    paramMap.put("list", params);
                    List<CollModelDetailMqtt> powerGuidList = mqttMapper.getSignalValueOfElectric(paramMap);
                    for (CollModelDetailMqtt collModelDetailMqtt : powerGuidList) {
                        Map<String, String> tempMap = new HashMap<>();
                        String guid = collModelDetailMqtt.getSignalGuid();
                        tempMap.put("name", collModelDetailMqtt.getSignalName());
                        String value = guidMap.get(guid);
                        if (value == null || value.isEmpty()) {
                            tempMap.put("value", "-- " + StringUtil.checkBlank(collModelDetailMqtt.getDataUnit()));
                        } else {
                            tempMap.put("value",
                                    value + " " + StringUtil.checkBlank(collModelDetailMqtt.getDataUnit()));
                        }
                        tempList.add(tempMap);
                    }
                    reultList.add(tempList);

                }
                // 二.不能从缓存中取出点表信息及点表数据缓存中为空
                // 1 2 逆变器数据
                // 3 电表
                // 9 PCS数据
                // 10 BMS数据
                // 11 汇流箱数据
                // 12 箱变数据
            } else if (DTC.STRING_INVERTER == deviceType || DTC.CENTRAL_INVERTER == deviceType || DTC.PCS == deviceType
                    || DTC.BMS == deviceType || DTC.COMBINER_BOX == deviceType || DTC.BOX_CHANGE == deviceType
                    || DTC.ENV_MONITOR == deviceType) {
                reultList = getDeviceNum(reultList, dataModel, null, deviceType);
            }
            Map<String, Object> reultMap = new HashMap<>();
            Map<String, String> jedisMap = CacheUtil3.getMapByDb(deviceId, 1);
            if (jedisMap != null) {
                String time = jedisMap.get("sys_time");
                if (!Util.isEmpty(time)) {
                    reultMap.put("curTime", Util.longToDateString(Long.valueOf(time), TimeEXP.exp5));
                }
            }
            reultMap.put("deviceType", deviceType);
            reultMap.put("showData", reultList);
            msg.setBody(reultMap);
            msg.setCode(Header.STATUS_SUCESS);

            /**
             * 状态模块
             */
        } else if ("1".equals(type)) {
            List<CollModelDetailMqtt> yxList = mqttMapper.getYXValueOfDevice(dataModel);
            Map<String, String> mqtt = cacheUtil.getMap(deviceId);
            List<Object> tempList = new ArrayList<>();
            if (mqtt != null && !mqtt.isEmpty()) {
                if (DTC.STRING_INVERTER == deviceType || DTC.CENTRAL_INVERTER == deviceType || DTC.PCS == deviceType
                        || DTC.BMS == deviceType || DTC.COMBINER_BOX == deviceType || DTC.BOX_CHANGE == deviceType
                        || DTC.ENV_MONITOR == deviceType) {
                    tempList = getDeviceYxNum(tempList, yxList, mqtt);
                }
            } else {
                if (DTC.STRING_INVERTER == deviceType || DTC.CENTRAL_INVERTER == deviceType || DTC.PCS == deviceType
                        || DTC.BMS == deviceType || DTC.COMBINER_BOX == deviceType || DTC.BOX_CHANGE == deviceType
                        || DTC.ENV_MONITOR == deviceType) {
                    tempList = getDeviceYxNumIsNull(tempList, yxList);
                }
            }
            msg.setBody(tempList);
            msg.setCode(Header.STATUS_SUCESS);
            /**
             * 远程监控
             */
        } else if ("2".equals(type)) {
            // 获取该设备父类的sn号，判断数采是自研还是畅洋，若是自研value取controlBit字段（如没有则取status_value），是畅洋value则取status_value
            String fatherSn = getDeviceFatherSn(device.getSupid() + "");
            if (Util.isNotBlank(fatherSn)) {
                String version = String.valueOf(fatherSn.charAt(3));
                Map<String, Object> resultMap = new HashMap<>(10);
                List<CollModelDetailMqtt> yt = mqttMapper.getYTValueOfDevice(dataModel);
                List<CollModelDetailMqtt> yk = mqttMapper.getYKValueOfDevice(dataModel);
                List<Object> setBtns = new ArrayList<>();
                List<Object> setDatas = new ArrayList<>();
                if (yt != null && !yt.isEmpty()) {
                    for (int i = 0, size = yt.size(); i < size; i++) {
                        CollModelDetailMqtt ytBean = yt.get(i);
                        Map<String, String> tempMap = new HashMap<>();
                        tempMap.put("label", ytBean.getSignalName());
                        tempMap.put("guid", device.getId().intValue() + "_" + ytBean.getSignalGuid());
                        tempMap.put("unit", ytBean.getDataUnit());
                        tempMap.put("validMax", ytBean.getMaxval() + "");
                        tempMap.put("validMin", ytBean.getMinval() + "");
                        String value = null;
                        DeviceControlData controlData = controlMapper.getValueOfGuid(ytBean.getSignalGuid());
                        if (controlData != null) {
                            value = controlData.getValue() + "";
                        }
                        tempMap.put("value", value);
                        setDatas.add(tempMap);
                    }
                }
                String ykGuid = null, ykValue = null;
                if (yk != null && !yk.isEmpty()) {
                    for (int i = 0, size = yk.size(); i < size; i++) {
                        String ykId = yk.get(i).getSignalGuid();
                        List<CollYkytExpand> ykList = ykytExpandMapper.getValuesOfYK(ykId);
                        List<String> ykGuids = new ArrayList<>();
                        if (ykList != null && !ykList.isEmpty()) {
                            for (CollYkytExpand collYkytExpand : ykList) {
                                Map<String, String> tempMap = new HashMap<>(3);
                                String guid = collYkytExpand.getSignalGuid();
                                tempMap.put("guid", device.getId().intValue() + "_" + guid);
                                String label = collYkytExpand.getSignalName();
                                switch (label) {
                                    case "开机":
                                        tempMap.put("tipTitle", configParam.getBootTipTitle());
                                        tempMap.put("tipContent", configParam.getBootTipContent());
                                        break;
                                    case "关机":
                                        tempMap.put("tipTitle", configParam.getShutdownTipTitle());
                                        tempMap.put("tipContent", configParam.getShutdownTipContent());
                                        break;
                                    case "复位":
                                        tempMap.put("tipTitle", configParam.getShutdownTipTitle());
                                        tempMap.put("tipContent", configParam.getShutdownTipContent());
                                        break;
                                    default:
                                        tempMap.put("tipTitle", "远程操作");
                                        tempMap.put("tipContent", "您将对操作造成的一切后果承担所有责任！");
                                        break;
                                }
                                tempMap.put("label", label);
                                if (version.equals("1")) {
                                    tempMap.put("value", collYkytExpand.getStatusValue() + "");
                                } else if (version.equals("2")) {
                                    String value = collYkytExpand.getControlBit() + "";
                                    if (Util.isNotBlank(value)) {
                                        tempMap.put("value", value);
                                    } else {
                                        tempMap.put("value", collYkytExpand.getStatusValue() + "");
                                    }
                                }

                                setBtns.add(tempMap);
                                ykGuids.add(device.getId().intValue() + "_" + guid);
                            }
                            map.clear();
                            map.put("list", ykGuids);
                            DeviceControlData ykData = controlMapper.getValueOfYKGuid(map);
                            if (ykData != null) {
                                ykGuid = ykData.getSignalGuid();
                                ykValue = String.valueOf(ykData.getValue());
                            }
                        }
                    }
                }
                resultMap.put("ykGuid", ykGuid);
                resultMap.put("ykValue", ykValue);
                resultMap.put("setDatas", setDatas);
                resultMap.put("setBtns", setBtns);
                msg.setBody(resultMap);
                msg.setCode(Header.STATUS_SUCESS);
            } else {
                return MessageBeanUtil.getOkMB();
            }
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.NOT_SUPPORT_TYPE);
        }
        return msg;
    }

    public String getDeviceFatherSn(String supid) {
        CollDevice fatherDevice = deviceMapper.getDeviceById(supid);
        if (fatherDevice == null) {
            return null;
        }
        String newSupid = fatherDevice.getSupid() + "";
        if ("0".equals(newSupid)) {
            return fatherDevice.getDeviceSn();
        } else {
            return getDeviceFatherSn(newSupid);
        }
    }

    /**
     * 封装得到设备状态返回列表的处理方法
     *
     * @param list       返回结果
     * @param dataModel  点表模型
     * @param guidMap    reids中该设备的点表状态集
     * @param deviceType 设备类型码
     * @return
     */
    public List getDeviceNum(List list, String dataModel, Map<String, String> guidMap, int deviceType) {

        // 直流输入
        List<Object> tempList = new ArrayList<>();

        // 交流输入
        List<Object> tempList2 = new ArrayList<>();

        // 其它
        List<Object> tempList3 = new ArrayList<>();

        List<CollModelDetailMqtt> mqttList = mqttMapper.getSignalValueOfInveter(dataModel);
        for (int i = 0; i < mqttList.size(); i++) {
            String guid = mqttList.get(i).getSignalGuid();
            String name = mqttList.get(i).getSignalName();
            String unit = mqttList.get(i).getDataUnit();
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("name", name);

            if (Util.isNotBlank(guidMap)) {
                String value = guidMap.get(guid);
                value = Util.getDecimalFomat2().format(Double.parseDouble(Util.isNotBlank(value) ? value : "0"));
                tempMap.put("value", value + " " + StringUtil.checkBlank(unit));
            } else {
                tempMap.put("value", "-- " + StringUtil.checkBlank(unit));
            }

            // 箱变和环境监测器不分类 只有可见或者不可见
            if (DTC.BOX_CHANGE == deviceType || DTC.ENV_MONITOR == deviceType) {
                // 0标示可见 1表示不可见
                String guidType = mqttList.get(i).getVisibility();
                list.add(tempMap);
                // 企其余设备类型要对可见分类 2标示直流 3表示交楼 4表示其它
            } else {
                // 是否显示,0表示不可见，1表示可见，对遥测2表示直流，3表示交流，4表示其他
                String guidType = mqttList.get(i).getVisibility();
                switch (guidType) {
                    case "2":
                        tempList.add(tempMap);
                        break;
                    case "3":
                        tempList2.add(tempMap);
                        break;
                    default:
                        tempList3.add(tempMap);
                        break;
                }
            }
        }
        if (DTC.BOX_CHANGE != deviceType && DTC.ENV_MONITOR != deviceType) {
            list.add(tempList);
            list.add(tempList2);
            list.add(tempList3);
        }
        return list;
    }

    public List getDeviceYxNum(List list, List<CollModelDetailMqtt> yxList, Map<String, String> mqtt) {
        CollYxExpand collYxExpand = new CollYxExpand();
        for (int i = 0, size = yxList.size(); i < size; i++) {
            CollModelDetailMqtt collModelDetailMqtt = yxList.get(i);
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("name", collModelDetailMqtt.getSignalName());
            String value = mqtt.get(collModelDetailMqtt.getSignalGuid());
            if (!Util.isNotBlank(value)) {
                value = "0";
            }
            if (!"0".equals(value) && !"1".equals(value)) {
                collYxExpand.setSignalGuid(collModelDetailMqtt.getSignalGuid());
                collYxExpand.setStatusValue(Integer.parseInt(value));
                value = String.valueOf(collYxExpandMapper.getYxValueCount(collYxExpand));
            }
            tempMap.put("value", value);
            list.add(tempMap);
        }
        return list;
    }

    public List getDeviceYxNumIsNull(List list, List<CollModelDetailMqtt> yxList) {
        for (int i = 0, size = yxList.size(); i < size; i++) {
            CollModelDetailMqtt collModelDetailMqtt = yxList.get(i);
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("name", collModelDetailMqtt.getSignalName());
            tempMap.put("value", "0");
            list.add(tempMap);
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public MessageBean updateValueOfGuid(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String signalGuidCopy = String.valueOf(map.get("signalGuid"));
        String signalName = String.valueOf(map.get("signalName"));
        int index = signalGuidCopy.indexOf("_");
        String deviceId = signalGuidCopy.substring(0, index);
        String collectId = deviceMapper.getDeviceById(deviceId).getSupid() + "";
        String collectsn = deviceMapper.getDeviceById(collectId).getDeviceSn();
        String signalGuid = signalGuidCopy.substring(index + 1);
        String tokenId = String.valueOf(map.get("tokenId"));
        User user = session.getAttribute(tokenId);
        String value = String.valueOf(map.get("value"));
        CollModelDetailMqtt mqtt = mqttMapper.getDetailByGuid(signalGuid);
        DeviceControlData saveObject = new DeviceControlData();
        saveObject.setOperationTime(System.currentTimeMillis());
        saveObject.setOperator(Integer.parseInt(user.getId()));
        saveObject.setDeviceId(deviceId);
        saveObject.setSignalGuid(deviceId + "_" + signalGuid);
        saveObject.setSignalName(signalName);
        saveObject.setValue(Integer.parseInt(value));
        Map<String, String> param = new HashMap<>(1);
        Map<String, Map<String, String>> params = new HashMap<String, Map<String, String>>();
        param.put(signalGuid, value);
        map.clear();
        params.put(deviceId, param);
        String sessioId = StringUtil.createSessionId();
        boolean b = false;
        try {
            b = iYkYtService.ykYt(sessioId, collectsn, params);
            logger.info("指令下发代理服务器成功, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collectsn + " 设备:"
                    + deviceId + " guid:" + mqtt.getSignalGuid() + " signalName :" + signalName + " value:" + value);
        } catch (Exception e) {
            logger.error("指令下发代理服务器失败, 操作者:" + user.getId() + "_" + user.getUserName() + "数采:" + collectsn + " 设备:"
                    + deviceId + " guid:" + mqtt.getSignalGuid() + " signalName :" + signalName + " value:" + value, e);
            throw new ServiceException(Msg.RMI_ERROR);
        }
        if (b) {
            List<Map<String, String>> reult = RabbitMessage.getReturnMessage(sessioId);
            if (reult != null && !reult.isEmpty()) {
                String resultStatus = reult.get(0).get("execResult");
                if ("success".equals(resultStatus)) {
                    controlMapper.saveControl(saveObject);
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
                logger.error("服务器回执信息判定失败!");
            }
        } else {
            msg.setCode(Header.STATUS_FAILED);
            msg.setMsg(Msg.YKYT_FAILD);
        }
        return msg;
    }

    @Override
    public MessageBean getPlantIncome(String jsonData)
            throws SessionException, ServiceException, SessionTimeoutException, ParseException {
        MessageBean msgTemp = monitorService.getProSearch(jsonData);
        // 柱状图
        Object obj = msgTemp.getBody();
        MessageBean msg = new MessageBean();
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("historyEnergy", obj);
        MessageBean pprTemp = monitorService.getSinglePower(jsonData);
        Map<String, Object> pprMap = (Map<String, Object>) pprTemp.getBody();
        resultMap.put("palntRank", pprMap.get("palntRank"));
        resultMap.put("PPR", pprMap.get("PPR"));
        msg.setBody(resultMap);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getPowerCurrentOfDevice(String jsonData) throws ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String deviceId = String.valueOf(map.get("deviceId"));
        String dataTime = String.valueOf(map.get("dataTime"));
        dataTime = StringUtil.isBlank(dataTime) ? Util.getCurrentDate(1) : dataTime;
        CollDevice device = deviceMapper.getDeviceById(deviceId);
        PlantInfo plantInfo = plantMapper.getPlantInfo(device.getPlantId() + "");
        String[] location = StringUtils.split(plantInfo.getLoaction(), ",");
        int deviceType = device.getDeviceType();
        String riseTime = ServiceUtil.getSunRise(dataTime, Double.parseDouble(location[0]),
                Double.parseDouble(location[1]));
        String setTime = ServiceUtil.getSunSet(dataTime, Double.parseDouble(location[0]),
                Double.parseDouble(location[1]));
        List<String> timeList = ServiceUtil.getStringTimeSegment(riseTime, setTime);
        Map<String, Object> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("dataTime", dataTime);
        // =================ybj
        long startTime = TimeUtil.str2Long(dataTime, "yyyy-MM-dd");
        long endTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, startTime, 1);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        // ===============

        map.clear();
        Object[] xData = timeList.toArray();
        map.put("xData", xData);
        Map<String, Object> yData1 = null;
        Map<String, Object> yData2 = null;
        Map<String, Object> yData3 = null;
        Map<String, Object> yData4 = null;
        List<Object> list = new ArrayList<>(2);
        int nowLocation = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dataTime.equals(sdf.format(new Date()))) {
            String curTime = ServiceUtil.getNowTimeInTimes();
            // 现在时间对应横坐标上第几个点
            nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
        } else {
            nowLocation = timeList.size() - 1;
        }
        if (deviceType == DTC.CENTRAL_INVERTER) {
            List<DataCentralInverter> dataList = centralInverterMapper.getCurrentPowerOfCentralInvnter(param);
            yData1 = ServiceUtil.loadParamForCurrentPowers(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower2(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
            map.put("yName", "功率(千瓦),发电量(度)");
        } else if (deviceType == DTC.STRING_INVERTER) {
            List<DataCentralInverter> dataList = centralInverterMapper.getCurrentPowerOfStringInvnter(param);
            yData1 = ServiceUtil.loadParamForCurrentPowers(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower2(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
            map.put("yName", "功率(千瓦),发电量(度)");
        } else if (deviceType == DTC.ELECTRIC_METER) {
            List<DataElectricMeter> dataList = electricMapper.getCurrentPower(param);
            yData1 = ServiceUtil.loadParamForCurrentPower3(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower4(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
        } else if (deviceType == DTC.PCS) {
            List<DataPcs> dataList = pcsMapper.getVoltagePower(param);
            yData1 = ServiceUtil.loadParamForCurrentPower5(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower6(riseTime, setTime, dataList, nowLocation);
            yData3 = ServiceUtil.loadParamForCurrentPower7(riseTime, setTime, dataList, nowLocation);
            yData4 = ServiceUtil.loadParamForCurrentPower8(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
            list.add(yData3);
            list.add(yData4);
        } else if (deviceType == DTC.BMS) {
            List<DataBms> dataList = bmsMapper.getDischargeCurrent(param);
            yData1 = ServiceUtil.loadParamForCurrentPower9(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower10(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
        } else if (deviceType == DTC.BOX_CHANGE) {
            List<DataBoxChange> dataList = boxChangeMapper.getVoltageAndPower(param);
            yData1 = ServiceUtil.loadParamForCurrentPower11(riseTime, setTime, dataList, nowLocation);
            yData2 = ServiceUtil.loadParamForCurrentPower12(riseTime, setTime, dataList, nowLocation);
            yData3 = ServiceUtil.loadParamForCurrentPower13(riseTime, setTime, dataList, nowLocation);
            yData4 = ServiceUtil.loadParamForCurrentPower14(riseTime, setTime, dataList, nowLocation);
            list.add(yData1);
            list.add(yData2);
            list.add(yData3);
            list.add(yData4);
            map.put("yName", "功率(千瓦),电压(伏)");
            // 环境监测器 ybj
        } else if (deviceType == DTC.ENV_MONITOR) {
            List<Map<String, Object>> dataList = envMonitorMapper.getCurrentRadiationAndTemp(param);
            List<Map<String, Object>> yData = ServiceUtil.loadParamForCurrentPower15(timeList, dataList, nowLocation);
            list.addAll(yData);
            map.put("yName", "总辐射 W/m²,温度℃");
        }
        map.put("yData", list);
        msg.setBody(map);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getCurrentIncomeOfPlant(String jsonData) throws ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = map.get("plantId") + "";
        Map<String, Object> genIncome = new HashMap<>(3);
        Map<String, Object> genIncomMap = statisticalGainService.getNowDayIncome(pId);

        Map<String, Object> todayMap = statisticalGainService.getNowDayIncome(pId);
        genIncome.put("genDay", todayMap.get("power"));
        genIncome.put("genDayUnit", "度");
        genIncome.put("profitDay", todayMap.get("inCome"));
        genIncome.put("profitDayUnit", "元");
        Map<String, Object> tempMap = statisticalGainService.getHistoryIncome(pId);
        genIncome.put("genTotal", tempMap.get("power"));
        genIncome.put("genTotalUnit", "度");

        // 总收益
        genIncome.put("profitTotal", tempMap.get("inCome"));
        genIncome.put("profitTotalUnit", "元");
        // 电站状态
        int plantType = ServiceUtil.getPlantStatus(pId);
        genIncome.put("type", plantType);
        /** 社会贡献 */

        /** 电站最近三天天气 */

        /** 电站容量 */
        msg.setBody(genIncome);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean fuzzySearchPlant(String jsonData, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String plantName = map.get("plantName") + "";
        String tokenId = map.get("tokenId") + "";

        // 过滤出需要的类型
        String plantType = map.get("plantType").toString();
        ArrayList<Integer> tPlantIdList = new ArrayList<>();
        SystemCache.plantTypeMap.get(plantType).forEach(e -> tPlantIdList.add(Integer.valueOf(e)));

        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Integer> reList = plantInfoService.getPlantsForUser(tokenId, session, 1);
        if (reList != null && !reList.isEmpty()) {
            map.clear();
            map.put("list", reList);
            map.put("plantName", plantName);
            List<PlantInfo> plants = plantMapper.getPlantsForSearch(map);
            if (plants != null && !plants.isEmpty()) {
                for (PlantInfo plantInfo : plants) {
                    Integer planId = plantInfo.getId();
                    if (tPlantIdList.contains(planId)) {
                        Map<String, Object> tempMap = new HashMap<>(2);
                        tempMap.put("plantName", plantInfo.getPlantName());
                        tempMap.put("plantId", planId);
                        tempMap.put("singlePlantType",
                                SystemCache.plantTypeList.get(String.valueOf(plantInfo.getId())));
                        resultList.add(tempMap);
                    }
                }
            }
        }

        map.clear();
        map.put("plantList", resultList);
        msg.setBody(map);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    /**
     * @Author lz
     * @Description: 首页排序
     * @param: [jsonData, session]
     * @return: {com.synpower.msg.MessageBean}
     * @Date: 2018/11/30 15:15
     **/
    @Override
    public MessageBean plantDist(String jsonData, Session session)
            throws SessionTimeoutException, SessionException, ServiceException {
        MessageBean msg = new MessageBean();
        Map<String, Object> parMap = Util.parseURL(jsonData);
        String tokenId = Util.getString(parMap.get("tokenId"));
        List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);
        // 电站状态筛选
        int status = Util.getInt(parMap.get("status"));
        if (status != -1) {
            plants = plants.stream().filter(e -> ServiceUtil.getPlantStatus(e + "") == status ? true : false)
                    .collect(Collectors.toList());
        }
        if (Util.isNotBlank(plants)) {
            // id筛选
            // 区域id/模糊查询条件/查询容量范围
            String areaId = String.valueOf(parMap.get("areaId"));
            String condition = String.valueOf(parMap.get("condition"));
            String range = String.valueOf(parMap.get("range"));
            String start = String.valueOf(parMap.get("start"));
            String length = String.valueOf(parMap.get("length"));
            String[] ranges = range.split(",");
            Integer type = 2;
            String typeStr = String.valueOf(parMap.get("type"));
            // 上一级电站id、首次默认为顶级区域id
            if (!Util.isNotBlank(areaId)) {
                areaId = configParam.getLocation();
            }
            if (Util.isNotBlank(typeStr)) {
                type = Integer.parseInt(typeStr);
            }
            Map<String, Object> mMap = new HashMap<>();
            if (type == 2) {
                // 当前区域名
                Map<String, Object> pnameMap = new HashMap<>();
                // 筛选类型
                ServiceUtil.parseOrderName(parMap, mMap);
                pnameMap.put("areaId", areaId);
                pnameMap.put("plantTypeA", 1);
                String name = addMapper.getPnames(pnameMap);
                // String fatherUnique = addMapper.getDetailByName(name);
                mMap.put("fname", name);
                mMap.put("reList", plants);
                mMap.put("condition", condition);
                mMap.put("start", start);
                mMap.put("length", length);
                if (ranges.length == 2) {
                    //首页已单位统一显示为KW
                    mMap.put("sr", Util.getInt(ranges[0]) * 1000);
                    mMap.put("er", Util.getInt(ranges[1]) * 1000);
                } else if (ranges.length == 1) {
                    mMap.put("sr", Util.getInt(ranges[0]) * 1000);
                }
                // if (orderName.equals())
                // 子电站id集合  getChildPids3顺带做容量,地域,关键字的筛选
                // List<Integer> pids = addMapper.getChildPids2(mMap);
                List<Integer> pids = addMapper.getChildPids3(mMap);
                if (CollectionUtils.isEmpty(pids))
                    return MessageBeanUtil.getOkMB();
                HashMap<String, Object> resultMap = commonSerch(pids, mMap);
                resultMap.put("draw", String.valueOf(parMap.get("draw")));
                msg.setBody(resultMap);
            } else {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setBody(null);
            }
        } else {
            Map<String, Object> finalMap = new HashMap<>();
            finalMap.put("plantsInfo", new ArrayList<>());
            finalMap.put("draw", String.valueOf(parMap.get("draw")));
            finalMap.put("recordsTotal", 0);
            finalMap.put("recordsFiltered", 0);
            msg.setMsg("得到区域电站分布信息");
            msg.setBody(finalMap);
            msg.setCode(Header.STATUS_SUCESS);
        }
        return msg;
    }


    /**
     * @Author lz
     * @Description: 排序
     * @param: [pids, orderName]
     * @return: {java.util.HashMap<java.lang.String,java.lang.Object>}
     * @Date: 2018/11/30 15:14
     **/
    public HashMap<String, Object> commonSerch(List<Integer> pids, Map<String, Object> orderName) {
        SearchCondition condition = new SearchCondition();
        //BeanUtils.copyProperties(orderName, condition);
        try {
            BeanUtils.populate(condition, orderName);
        } catch (Exception e) {
            CommonSearchException csE = new CommonSearchException("获取排序条件失败", e);
            logger.error("", csE);
            throw csE;
        }
        return commonSearchContext.orderAndGetInfo(pids, condition);
    }

    @Override
    public MessageBean plantDist(String jsonData, Session session, HttpServletRequest request)
            throws ServiceException, SessionException, SessionTimeoutException {
//		updateWeather.updateWeather();
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String tokenId = map.get("tokenId") + "";
        List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);
        if (Util.isNotBlank(plants)) {
            // 区域id/模糊查询条件/查询容量范围
            String areaId = String.valueOf(map.get("areaId"));
            String condition = String.valueOf(map.get("condition"));
            String range = String.valueOf(map.get("range"));
            String[] ranges = range.split(",");
            String v1 = null;
            String v2 = null;
            Integer type = 2;
            String typeStr = String.valueOf(map.get("type"));
            if (Util.isNotBlank(typeStr)) {
                type = Integer.parseInt(typeStr);
            }
            // 上一级电站id、首次默认为顶级区域id
            if (!Util.isNotBlank(areaId)) {
                areaId = configParam.getLocation();
            }
            Map<String, Object> mMap = new HashMap<>();
            if (type == 2) {
                // 当前区域名
                Map<String, Object> pnameMap = new HashMap<>();
                pnameMap.put("areaId", areaId);
                pnameMap.put("plantTypeA", 1);
                String name = addMapper.getPnames(pnameMap);
                // String fatherUnique = addMapper.getDetailByName(name);
                mMap.put("fname", name);
                mMap.put("reList", plants);
                // 子电站id集合
                List<Integer> pids = addMapper.getChildPids2(mMap);
                // 光伏、储能电站id集合
                List<Integer> plantSolar = new ArrayList<>();
                // List<Integer> plantStored = new ArrayList<>();
                for (int i = 0; i < pids.size(); i++) {
                    PlantInfo plantInfo = plantMapper.getPlantInfo(pids.get(i) + "");
                    Integer plantTypeA = plantInfo.getPlantTypeA();
                    if (plantTypeA == 1) {
                        plantSolar.add(plantInfo.getId());
                    }
                    // else if (plantTypeA == 2) {
                    // plantStored.add(plantInfo.getId());
                    // }
                }
                commonSerch(condition, ranges, plantSolar, v1, v2, msg, name, map, null);
            } else {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setBody(null);
            }
        } else {
            Map<String, Object> finalMap = new HashMap<>();
            finalMap.put("plantsInfo", new ArrayList<>());
            finalMap.put("draw", String.valueOf(map.get("draw")));
            finalMap.put("recordsTotal", 0);
            finalMap.put("recordsFiltered", 0);
            msg.setMsg("得到区域电站分布信息");
            msg.setBody(finalMap);
            msg.setCode(Header.STATUS_SUCESS);
        }
        return msg;
    }

    @Override
    public MessageBean getStoList(String jsonData, Session session, HttpServletRequest request)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String tokenId = map.get("tokenId") + "";
        List<Integer> plants = plantInfoService.getPlantsForUser(tokenId, session, 1);
        if (Util.isNotBlank(plants)) {
            // 区域id/模糊查询条件/查询容量范围
            String areaId = String.valueOf(map.get("areaId"));
            String condition = String.valueOf(map.get("condition"));
            String range = String.valueOf(map.get("range"));
            String[] ranges = range.split(",");
            String v1 = null;
            String v2 = null;
            Integer type = 2;
            String typeStr = String.valueOf(map.get("type"));
            if (Util.isNotBlank(typeStr)) {
                type = Integer.parseInt(typeStr);
            }
            // 上一级电站id、首次默认为顶级区域id
            if (!Util.isNotBlank(areaId)) {
                areaId = configParam.getLocation();
            }
            Map<String, Object> mMap = new HashMap<>();
            if (type == 2) {
                // 当前区域名
                Map<String, Object> pnameMap = new HashMap<>();
                pnameMap.put("areaId", areaId);
                pnameMap.put("plantTypeA", 2);
                String name = addMapper.getPnames(pnameMap);
                // String fatherUnique = addMapper.getDetailByName(name);
                mMap.put("fname", name);
                mMap.put("reList", plants);
                // 子电站id集合
                List<Integer> pids = addMapper.getChildPids2(mMap);
                // 光伏、储能电站id集合
                // List<Integer> plantSolar = new ArrayList<>();
                List<Integer> plantStored = new ArrayList<>();
                for (int i = 0; i < pids.size(); i++) {
                    PlantInfo plantInfo = plantMapper.getPlantInfo(pids.get(i) + "");
                    Integer plantTypeA = plantInfo.getPlantTypeA();
                    // if (plantTypeA == 1) {
                    // plantSolar.add(plantInfo.getId());
                    // } else
                    if (plantTypeA == 2) {
                        plantStored.add(plantInfo.getId());
                    }
                }
                commonSerchForStoredPlant(condition, ranges, plantStored, v1, v2, msg, name, map, null);
                // commonSerch(condition, ranges, pids, v1, v2,msg,name,map,fatherUnique);
            } else {
                msg.setCode(Header.STATUS_SUCESS);
                msg.setBody(null);
            }
        } else {
            Map<String, Object> finalMap = new HashMap<>();
            finalMap.put("plantsInfo", new ArrayList<>());
            finalMap.put("draw", String.valueOf(map.get("draw")));
            finalMap.put("recordsTotal", 0);
            finalMap.put("recordsFiltered", 0);
            msg.setMsg("得到区域电站分布信息");

            msg.setBody(finalMap);
            msg.setCode(Header.STATUS_SUCESS);
        }
        return msg;
    }

    public MessageBean commonSerchForStoredPlant(String condition, String[] ranges, List<Integer> list, String v1,
                                                 String v2, MessageBean msg, String fname, Map<String, Object> oMap, String fatherUnique)
            throws ServiceException {
        // 装载参数所需map、list
        Map<String, Object> searchMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> ListFront = new ArrayList<>();
        // 根据容量、模糊搜索条件查询符号条件电站id集合
        // 有关键字、无容量范围搜索
        if (Util.isNotBlank(condition) && ranges.length == 0) {
            map.put("condition", condition);
            // 通过联系人、电话查询组织id集合
            /*
             * List<Integer> oids = userMapper.getUsers(map); List<Integer> pids = new
             * ArrayList<>(); map.put("oreList", oids); if (oids.size()>0) { pids =
             * userMapper.getPidByUser(map); }
             */
            // 按电站名称、地址查询条件进行查询
            searchMap.put("condition", condition);
            searchMap.put("list", list);
            List<Integer> plist = plantMapper.getPList(searchMap);
            // 取电站名称、地址与联系人搜索并集
            List<Integer> newList = new ArrayList<>();
            // newList.addAll(pids);
            newList.addAll(plist);
            list.retainAll(newList);
            // 无关键字、有容量范围搜索
        } else if (!Util.isNotBlank(condition) && ranges.length != 0) {
            if (ranges.length == 2) {
                v1 = ranges[0];
                v2 = ranges[1];
            } else if (ranges.length == 1) {
                v1 = ranges[0];
            }
            searchMap.put("v1", v1);
            searchMap.put("v2", v2);
            List<PlantInfo> uList = plantMapper.getCapacityUnit2(list);
            List<Integer> plistUnit = new ArrayList<>();
            // 带单位进行范围搜索
            for (PlantInfo plantInfo : uList) {
                searchMap.put("id", plantInfo.getId());
                String unit = plantInfo.getUnit();
                if ("KW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUKW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("MW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUMW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("GW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUGW(searchMap);
                    plistUnit.add(idwithUnit);
                }
            }
            list.retainAll(plistUnit);
            // 有关键字、有容量范围搜索
        } else if (Util.isNotBlank(condition) && ranges.length != 0) {
            // 查询出容量范围内电站id集合
            if (ranges.length == 2) {
                v1 = ranges[0];
                v2 = ranges[1];
            } else if (ranges.length == 1) {
                v1 = ranges[0];
            }
            searchMap.put("v1", v1);
            searchMap.put("v2", v2);
            // 根据容量查询电站id集合
            List<PlantInfo> uList = plantMapper.getCapacityUnit2(list);
            List<Integer> plistUnit = new ArrayList<>();
            for (PlantInfo plantInfo : uList) {
                String unit = plantInfo.getUnit();
                searchMap.put("id", plantInfo.getId());
                if ("KW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUKW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("MW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUMW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("GW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUGW(searchMap);
                    plistUnit.add(idwithUnit);
                }
            }
            // 根据电站名字、联系人查询电站id集合
            map.put("condition", condition);
            /*
             * List<Integer> oids = userMapper.getUsers(map); List<Integer> pids = new
             * ArrayList<>(); if (oids.size()>0) { map.put("oreList", oids); pids =
             * userMapper.getPidByUser(map); }
             */
            Map<String, Object> seMap = new HashMap<>();
            seMap.put("condition", condition);
            seMap.put("list", list);
            List<Integer> plist2 = plantMapper.getPList(seMap);
            // 取电站名称、地址与联系人搜索并集
            List<Integer> newList = new ArrayList<>();
            // newList.addAll(pids);
            newList.addAll(plist2);
            plistUnit.retainAll(newList);
            list.retainAll(plistUnit);
        }
        // 各电站总发电量
        List<Double> powerOutList = new ArrayList<>();
        List<Double> powerInList = new ArrayList<>();
        // 电站状态分类初始化集合
        List<Integer> statusList = new ArrayList<>();
        List<Integer> l0 = new ArrayList<>();
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        List<Integer> l3 = new ArrayList<>();
        List<Integer> l4 = new ArrayList<>();
        // 电站类型分类初始化集合
        List<Integer> typeBList = new ArrayList<>();
        List<Integer> plantIndustry = new ArrayList<>();
        List<Integer> plantFamilly = new ArrayList<>();
        List<Integer> plantForPoor = new ArrayList<>();
        for (Integer pId : list) {
            // 实时充、放电
            Map<String, Double> todayPricePower = statisticalGainService.getNowDayIncome(pId + "");
            Double todayOut = Double.parseDouble(todayPricePower.get("dailyPosEnergy") + "");
            Double todayIn = Double.parseDouble(todayPricePower.get("dailyNegEnergy") + "");
            powerOutList.add(todayOut);
            powerInList.add(todayIn);
            // 电站状态分类存入集合
            statusList.add(ServiceUtil.getPlantStatus(String.valueOf(pId)));
            int plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pId));
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
                case 4:
                    l4.add(pId);
                    break;
                default:
                    break;
            }
            // 电站类型分类存入集合
            Integer typeBId = plantMapper.getPlantTypeB(pId);
            typeBList.add(typeBId);
            switch (typeBId) {
                case 1:
                    plantIndustry.add(pId);
                    break;
                case 2:
                    plantFamilly.add(pId);
                    break;
                case 3:
                    plantForPoor.add(pId);
                    break;
                default:
                    break;
            }
        }
        // 各电站联系人及联系电话
        List<PlantInfo> flist = new ArrayList<>();
        // 区域对象信息
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer totalCount = 0;
        // 每页条数、起始位置
        int start = Integer
                .parseInt("null".equals(String.valueOf(oMap.get("start"))) ? "0" : String.valueOf(oMap.get("start")));
        int length = Integer.parseInt(
                "null".equals(String.valueOf(oMap.get("length"))) ? "10" : String.valueOf(oMap.get("length")));
        // 下钻时 显示下级电站数
        if (list.size() > 0) {
            paramMap.put("reList", list);
            paramMap.put("length", length);
            paramMap.put("offset", start);
            paramMap.put("plantTypeA", 2);
            // 组装SQL查询参数
            paramMap.put("list", list);
            // 查询最终电站列表信息
            flist = plantMapper.getEachPlantInfoPage(paramMap);
            // 数据总条数
            totalCount = plantMapper.getTotalCount(paramMap);
        }
        // 筛选类型
        List<Map<String, String>> tempList = "null".equals(oMap.get("orderName")) ? null
                : "".equals(oMap.get("orderName")) ? null : (List<Map<String, String>>) oMap.get("orderName");

        String[] sortnameArr = null;
        boolean[] typeArr = null;
        if (tempList != null && !tempList.isEmpty()) {
            typeArr = new boolean[tempList.size()];
            sortnameArr = new String[tempList.size()];
            int count2 = 0;
            for (int i = 0; i < tempList.size(); i++) {
                for (String key : tempList.get(i).keySet()) {
                    String value = tempList.get(i).get(key);
                    sortnameArr[count2] = key;
                    if ("desc".equalsIgnoreCase(value)) {
                        typeArr[count2] = false;
                        count2++;
                    } else {
                        typeArr[count2] = true;
                        count2++;
                    }
                }
            }
        }
        // 判断是否按电站状态分类
        String status = oMap.get("status") + "";
        Integer statusId = null;
        if ("".equals(status) || status == null) {
            statusId = 5;
        } else {
            statusId = Integer.parseInt(status);
        }
        // 判断是否按电站类型分类
        String plantType = oMap.get("plantType") + "";
        Integer plantTypeInt = null;
        if (plantType == null || "".equals(plantType) || "null".equals(plantType)) {
            plantTypeInt = 4;
        } else {
            plantTypeInt = Integer.parseInt(plantType);
        }

        List<PlantListOrder> resultList = new ArrayList<>();
        // 无指定电站状态且无指定电站类型排序
        if (statusId == 5 && flist != null && !flist.isEmpty() && plantTypeInt == 4) {
            commSetEle2ForStored(flist, statusList, typeBList, powerOutList, powerInList, resultList, start, length);
            if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                ListUtils.sort(resultList, sortnameArr, typeArr);
            }
            // 指定电站类型 无指定电站状态排序
        } else if (statusId == 5 && flist != null && !flist.isEmpty() && plantTypeInt != 4) {
            switch (plantTypeInt) {
                case 1:
                    if (plantIndustry.size() > 0) {
                        paramMap.put("typeBList", plantIndustry);
                        flist = plantMapper.getPlantsGroupByType(paramMap);
                        commSetEleForStoredByType(flist, statusList, 1, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 2:
                    if (plantFamilly.size() > 0) {
                        paramMap.put("typeBList", plantFamilly);
                        flist = plantMapper.getPlantsGroupByType(paramMap);
                        commSetEleForStoredByType(flist, statusList, 2, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 3:
                    if (plantForPoor.size() > 0) {
                        paramMap.put("typeBList", plantForPoor);
                        flist = plantMapper.getPlantsGroupByType(paramMap);
                        commSetEleForStoredByType(flist, statusList, 3, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                default:
                    break;
            }
            // 指定电站状态 无指定电类型态排序
        } else if (statusId != 5 && flist != null && !flist.isEmpty() && plantTypeInt == 4) {
            switch (statusId) {
                case 0:
                    if (l0.size() > 0) {
                        paramMap.put("statusList", l0);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        commSetEleForStoredByStatus(flist, typeBList, 0, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 1:
                    if (l1.size() > 0) {
                        paramMap.put("statusList", l1);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        commSetEleForStoredByStatus(flist, typeBList, 1, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 2:
                    if (l2.size() > 0) {
                        paramMap.put("statusList", l2);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        commSetEleForStoredByStatus(flist, typeBList, 2, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 3:
                    if (l3.size() > 0) {
                        paramMap.put("statusList", l3);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        commSetEleForStoredByStatus(flist, typeBList, 3, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 4:
                    if (l4.size() > 0) {
                        paramMap.put("statusList", l4);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        commSetEleForStoredByStatus(flist, typeBList, 4, powerOutList, powerInList, resultList, start,
                                length);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                default:
                    break;
            }
            // 指定电站状态 指定电类型态排序
        } else {
            List<Integer> newStatus = new ArrayList<>();
            List<Integer> newType = new ArrayList<>();
            switch (plantTypeInt) {
                case 1:
                    newType.addAll(plantIndustry);
                    break;
                case 2:
                    newType.addAll(plantFamilly);
                    break;
                case 3:
                    newType.addAll(plantForPoor);
                    break;
                default:
                    break;
            }
            switch (statusId) {
                case 0:
                    newStatus.addAll(l0);
                    break;
                case 1:
                    newStatus.addAll(l1);
                    break;
                case 2:
                    newStatus.addAll(l2);
                    break;
                case 3:
                    newStatus.addAll(l3);
                    break;
                case 4:
                    newStatus.addAll(l4);
                    break;
                default:
                    break;
            }
            newStatus.retainAll(newType);
            paramMap.put("statusList", newStatus);
            if (newStatus.size() > 0) {
                flist = plantMapper.getPlantsNoStatusAndType(paramMap);
            } else {
                flist = null;
            }
            commSetEle2NoTypeAndStatusSelected(flist, plantTypeInt, statusId, powerOutList, powerInList, resultList,
                    start, length);
            if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                ListUtils.sort(resultList, sortnameArr, typeArr);
            }
        }
        // 电站对象信息
        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                PlantListOrder plantInfo = resultList.get(i);
                resultMap.put("id", plantInfo.getId());
                resultMap.put("name", plantInfo.getName());
                resultMap.put("addr", plantInfo.getAddr());
                resultMap.put("capacity", plantInfo.getCapacity());
                resultMap.put("location", plantInfo.getLocation());
                String typeName = typeBMapper.getTypeName(plantInfo.getPlantTypeB());
                resultMap.put("plantType", typeName);
                resultMap.put("status", plantInfo.getStatus());
                resultMap.put("disCharToday", plantInfo.getPowerOutToday());
                resultMap.put("charToday", plantInfo.getPowerInToday());
                resultMap.put("powerTodayIn", plantInfo.getPowerInToday());
                resultMap.put("soc", plantInfo.getSoc());
                resultMap.put("sysEfficiency", plantInfo.getSystemEfficace());
                resultMap.put("singlePlantType", plantInfo.getType());
                ListFront.add(resultMap);
                resultMap.put("weather", plantInfoService.getPlantTodayWeather(plantInfo.getId()).getDayPictureUrl());
            }
        }
        finalMap.put("plantsInfo", ListFront);
        finalMap.put("draw", String.valueOf(oMap.get("draw")));
        finalMap.put("recordsTotal", totalCount);
        finalMap.put("recordsFiltered", totalCount);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setMsg("得到区域电站分布信息");
        msg.setBody(finalMap);
        return msg;
    }


    public MessageBean commonSerch(String condition, String[] ranges, List<Integer> list, String v1, String v2,
                                   MessageBean msg, String fname, Map<String, Object> oMap, String fatherUnique) throws ServiceException {
        // 装载参数所需map、list
        Map<String, Object> searchMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> ListFront = new ArrayList<>();
        // 根据容量、模糊搜索条件查询符号条件电站id集合
        // 有关键字、无容量范围搜索
        if (Util.isNotBlank(condition) && ranges.length == 0) {
            map.put("condition", condition);
            // 通过联系人、电话查询组织id集合
            List<Integer> oids = userMapper.getUsers(map);
            List<Integer> pids = new ArrayList<>();
            map.put("oreList", oids);
            if (oids.size() > 0) {
                pids = userMapper.getPidByUser(map);
            }
            // 按电站名称、地址查询条件进行查询
            searchMap.put("condition", condition);
            searchMap.put("list", list);
            List<Integer> plist = plantMapper.getPList(searchMap);
            // 取电站名称、地址与联系人搜索并集
            List<Integer> newList = new ArrayList<>();
            newList.addAll(pids);
            newList.addAll(plist);
            list.retainAll(newList);
            // 无关键字、有容量范围搜索
        } else if (!Util.isNotBlank(condition) && ranges.length != 0) {
            if (ranges.length == 2) {
                v1 = ranges[0];
                v2 = ranges[1];
            } else if (ranges.length == 1) {
                v1 = ranges[0];
            }
            searchMap.put("v1", v1);
            searchMap.put("v2", v2);
            List<PlantInfo> uList = plantMapper.getCapacityUnit2(list);
            List<Integer> plistUnit = new ArrayList<>();
            // 带单位进行范围搜索
            for (PlantInfo plantInfo : uList) {
                searchMap.put("id", plantInfo.getId());
                String unit = plantInfo.getUnit();
                if ("KW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUKW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("MW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUMW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("GW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUGW(searchMap);
                    plistUnit.add(idwithUnit);
                }
            }
            list.retainAll(plistUnit);
            // 有关键字、有容量范围搜索
        } else if (Util.isNotBlank(condition) && ranges.length != 0) {
            // 查询出容量范围内电站id集合
            if (ranges.length == 2) {
                v1 = ranges[0];
                v2 = ranges[1];
            } else if (ranges.length == 1) {
                v1 = ranges[0];
            }
            searchMap.put("v1", v1);
            searchMap.put("v2", v2);
            // 根据容量查询电站id集合
            // List<Integer> plist1 = plantMapper.getPList(searchMap);
            List<PlantInfo> uList = plantMapper.getCapacityUnit2(list);
            List<Integer> plistUnit = new ArrayList<>();
            for (PlantInfo plantInfo : uList) {
                String unit = plantInfo.getUnit();
                searchMap.put("id", plantInfo.getId());
                if ("KW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUKW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("MW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUMW(searchMap);
                    plistUnit.add(idwithUnit);
                } else if ("GW".equalsIgnoreCase(unit)) {
                    Integer idwithUnit = plantMapper.getPListWithUGW(searchMap);
                    plistUnit.add(idwithUnit);
                }
            }
            // 根据电站名字、联系人查询电站id集合
            map.put("condition", condition);
            List<Integer> oids = userMapper.getUsers(map);
            List<Integer> pids = new ArrayList<>();
            if (oids.size() > 0) {
                map.put("oreList", oids);
                pids = userMapper.getPidByUser(map);
            }
            Map<String, Object> seMap = new HashMap<>();
            seMap.put("condition", condition);
            seMap.put("list", list);
            List<Integer> plist2 = plantMapper.getPList(seMap);
            // 取电站名称、地址与联系人搜索并集
            List<Integer> newList = new ArrayList<>();
            newList.addAll(pids);
            newList.addAll(plist2);
            plistUnit.retainAll(newList);
            list.retainAll(plistUnit);
        }
        // 各电站总发电量
        List<Double> powerList = new ArrayList<>();

        List<Double> powerNowList = new ArrayList<>();
        // 电站按顺序进行状态判断
        List<Integer> statusList = new ArrayList<>();
        // 电站状态分类初始化集合0：发电；1：待机；2：故障；3：断连；4：异常
        // 发电状态
        List<Integer> l0 = new ArrayList<>();
        // 待机状态
        List<Integer> l1 = new ArrayList<>();
        // 故障状态
        List<Integer> l2 = new ArrayList<>();
        // 中断状态
        List<Integer> l3 = new ArrayList<>();
        // 异常状态
        List<Integer> l4 = new ArrayList<>();

        for (Integer pId : list) {
            // 历史发电量、今日发电量、电站状态
            Map<String, Object> historyMap = statisticalGainService.getHistoryIncome(String.valueOf(pId));
            Map<String, Object> nowDayIncome = statisticalGainService.getNowDayIncome(String.valueOf(pId));
            BigDecimal bg = new BigDecimal(Double.parseDouble(nowDayIncome.get("power") + ""));
            double dayIncome = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bg2 = new BigDecimal(Double.parseDouble(String.valueOf(historyMap.get("power")))
                    + Double.parseDouble(String.valueOf(nowDayIncome.get("power"))));
            double totalIncome = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            powerNowList.add(dayIncome);
            powerList.add(totalIncome);
            statusList.add(ServiceUtil.getPlantStatus(String.valueOf(pId)));
            int plantStatus = ServiceUtil.getPlantStatus(String.valueOf(pId));
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
                case 4:
                    l4.add(pId);
                    break;
                default:
                    break;
            }
        }
        // 各电站联系人及联系电话
        List<PlantInfo> flist = new ArrayList<>();
        // 区域对象信息
        Map<String, Object> finalMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer totalCount = 0;
        // 每页条数、起始位置
        int start = Integer
                .parseInt("null".equals(String.valueOf(oMap.get("start"))) ? "0" : String.valueOf(oMap.get("start")));
        int length = Integer.parseInt(
                "null".equals(String.valueOf(oMap.get("length"))) ? "10" : String.valueOf(oMap.get("length")));
        // 下钻时 显示下级电站数
        if (list.size() > 0) {
            paramMap.put("reList", list);
            paramMap.put("length", length);
            paramMap.put("offset", start);
            paramMap.put("plantTypeA", 1);
            // 组装SQL查询参数
            paramMap.put("list", list);
            // 查询最终电站列表信息
            flist = plantMapper.getEachPlantInfoPage(paramMap);
            // 数据总条数
            totalCount = plantMapper.getTotalCount(paramMap);
        }
        // 筛选类型
        List<Map<String, String>> tempList = "null".equals(oMap.get("orderName")) ? null
                : "".equals(oMap.get("orderName")) ? null : (List<Map<String, String>>) oMap.get("orderName");
        String[] sortnameArr = null;
        boolean[] typeArr = null;
        String status = oMap.get("status") + "";
        Integer statusId = null;
        if ("".equals(status) || status == null) {
            statusId = 5;
        } else {
            statusId = Integer.parseInt(status);
        }
        if (tempList != null && !tempList.isEmpty()) {
            typeArr = new boolean[tempList.size()];
            sortnameArr = new String[tempList.size()];
            int count2 = 0;
            for (int i = 0; i < tempList.size(); i++) {
                for (String key : tempList.get(i).keySet()) {
                    String value = tempList.get(i).get(key);
                    sortnameArr[count2] = key;
                    if ("desc".equalsIgnoreCase(value)) {
                        typeArr[count2] = false;
                        count2++;
                    } else {
                        typeArr[count2] = true;
                        count2++;
                    }
                }
            }
        }
        List<PlantListOrder> resultList = new ArrayList<>();
        int total = 0;
        // 无电站状态排序
        if (statusId == 5 && flist != null && !flist.isEmpty()) {
            commSetEle2(flist, statusList, powerNowList, powerList, resultList, start, length);
            if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                ListUtils.sort(resultList, sortnameArr, typeArr);
            }
            total = totalCount;
            // 带电站状态0,1,2排序
        } else {
            switch (statusId) {
                case 0:
                    if (l0.size() > 0) {
                        paramMap.put("statusList", l0);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        total = plantMapper.getPlantsByStatus2Count(paramMap);
                        commSetEle(flist, 0, powerNowList, powerList, resultList, start, length, list, l0);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 1:
                    if (l1.size() > 0) {
                        paramMap.put("statusList", l1);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        total = plantMapper.getPlantsByStatus2Count(paramMap);
                        commSetEle(flist, 1, powerNowList, powerList, resultList, start, length, list, l1);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 2:
                    if (l2.size() > 0) {
                        paramMap.put("statusList", l2);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        total = plantMapper.getPlantsByStatus2Count(paramMap);
                        commSetEle(flist, 2, powerNowList, powerList, resultList, start, length, list, l2);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 3:
                    if (l3.size() > 0) {
                        paramMap.put("statusList", l3);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        total = plantMapper.getPlantsByStatus2Count(paramMap);
                        commSetEle(flist, 3, powerNowList, powerList, resultList, start, length, list, l3);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                case 4:
                    if (l4.size() > 0) {
                        paramMap.put("statusList", l4);
                        flist = plantMapper.getPlantsByStatus2(paramMap);
                        total = plantMapper.getPlantsByStatus2Count(paramMap);
                        commSetEle(flist, 4, powerNowList, powerList, resultList, start, length, list, l4);
                        if (resultList != null && !resultList.isEmpty() && sortnameArr != null && sortnameArr.length > 0) {
                            ListUtils.sort(resultList, sortnameArr, typeArr);
                        }
                    } else {
                        flist = null;
                    }
                    break;
                default:
                    break;
            }
        }
        // 电站对象信息
        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                PlantListOrder plantInfo = resultList.get(i);
                resultMap.put("id", plantInfo.getId());
                resultMap.put("name", plantInfo.getName());
                resultMap.put("addr", plantInfo.getAddr());
                resultMap.put("capacity", plantInfo.getCapacity());
                resultMap.put("location", plantInfo.getLocation());
                resultMap.put("status", plantInfo.getStatus());
                resultMap.put("genDay", plantInfo.getGenToday());
                resultMap.put("genTotal", plantInfo.getGenTotal());
                resultMap.put("singlePlantType", plantInfo.getType());
                resultMap.put("ppr", plantInfo.getPpr());
                resultMap.put("contacts", plantInfo.getContacts());
                resultMap.put("contectsTel", plantInfo.getContectsTel());
                ListFront.add(resultMap);
                resultMap.put("weather", plantInfoService.getPlantTodayWeather(plantInfo.getId()).getDayPictureUrl());
            }
        }
        finalMap.put("plantsInfo", ListFront);
        finalMap.put("draw", String.valueOf(oMap.get("draw")));
        finalMap.put("recordsTotal", total);
        finalMap.put("recordsFiltered", total);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setMsg("得到区域电站分布信息");
        msg.setBody(finalMap);
        return msg;
    }

    @Override
    public MessageBean plantOpen(String jsonData, Session session, HttpServletRequest request)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> ListFront = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(jsonData);
        // 当前电站id
        String plantId = map.get("plantId") + "";
        // 当前电站图片
        String photo = plantMapper.getPhoto(plantId);
        // 当前电站经纬度
        String loList = plantMapper.getLocation(plantId);
        String[] location = loList.split(",");
        String temp = location[0];
        location[0] = location[1];
        location[1] = temp;
        // 电站抛物线
        Map<String, Object> currentPower = screenImpl.getCurrentPower(jsonData);
        // 设备状态分类
        Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(Integer.parseInt(plantId));
        int dCount = 0, normal = 0, breakNum = 0, standby = 0, fault = 0;
        Map<String, Integer> status = SystemCache.deviceOfStatus;
        if (devices != null) {
            for (Integer key : devices.keySet()) {
                List<Integer> tempList = devices.get(key);
                if (tempList != null && !tempList.isEmpty()) {
                    dCount += tempList.size();
                    for (Integer deviceId : tempList) {
                        int type = status.containsKey(String.valueOf(deviceId)) ? status.get(String.valueOf(deviceId))
                                : -1;
                        switch (type) {
                            case 0:
                                normal++;
                                break;
                            case 1:
                                standby++;
                                break;
                            case 2:
                                fault++;
                                break;
                            case 3:
                                breakNum++;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        Map<String, Object> cMap = new HashMap<>();
        cMap.put("standby", standby);
        cMap.put("fault", fault);
        cMap.put("break", breakNum);
        cMap.put("normal", normal);
        cMap.put("deviceNum", dCount);
        String[] photos = null;
        if (Util.isNotBlank(photo)) {
            photos = StringUtils.split(photo, ";");
        }
        String pic = null;
        /** 微信小程序默认返回第一张图片 */
        if (Util.isNotBlank(photos)) {
            pic = photos[0];
        }
        cMap.put("image", pic);
        cMap.put("location", location);
        cMap.put("powerCurve", currentPower);
        ListFront.add(cMap);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(ListFront);
        return msg;
    }

    /**
     * 新增list 和 l 两个参数 list为电站列表顺序集合 l为该状态的电站列表集合
     * 应为powerNowList和powerList是list所有电站的列表集 所以我们要将powerNowList和powerList过滤一次 此状态为
     * 0：发电；1：待机；2：故障；3：断连；4：异常
     *
     * @param flist
     * @param j
     * @param powerNowList
     * @param powerList
     * @param resultList
     * @param start
     * @param length
     * @param list         装的是所有pid
     * @param l            装的是该状态pid
     * @author ybj
     * @Description
     * @Date 2018年8月9日 修复bug
     */
    public static void commSetEle(List<PlantInfo> flist, int j, List<Double> powerNowList, List<Double> powerList,
                                  List<PlantListOrder> resultList, int start, int length, List list, List l) {
        // 封装过滤后的powerNowList
        List<Double> powerNowListTemp = new ArrayList<>();
        // 封装过滤后的powerList
        List<Double> powerListTemp = new ArrayList<>();
        // 遍历该状态列表
        for (int i = 0; i < l.size(); i++) {
            // 得到该状态的位置
            int a = list.indexOf(l.get(i));
            powerNowListTemp.add(powerNowList.get(a));
            powerListTemp.add(powerList.get(a));
        }

        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            plantsOrder.setId(plantInfo.getId());
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }
            // 设置状态
            plantsOrder.setStatus(j);

            plantsOrder.setGenToday(powerNowListTemp.get(i + start));
            plantsOrder.setGenTotal(powerListTemp.get(i + start));
            BigDecimal b = new BigDecimal(String.valueOf(plantsOrder.getCapacity()));
            BigDecimal bg = new BigDecimal(powerNowListTemp.get(i + start) / b.doubleValue());
            double ppr = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            plantsOrder.setPpr(ppr);
            SysUser sysUser = plantInfo.getContactUser();
            if (sysUser == null) {
                plantsOrder.setContectsTel("--");
                plantsOrder.setContacts("--");
            } else {
                plantsOrder.setContectsTel(sysUser.getUserTel());
                plantsOrder.setContacts(sysUser.getUserName());
            }
            resultList.add(plantsOrder);
        }
    }

    public void commSetEle2ForStored(List<PlantInfo> flist, List<Integer> statuslist, List<Integer> typeBList,
                                     List<Double> powerOutList, List<Double> powerInList, List<PlantListOrder> resultList, int start,
                                     int length) {
        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            Integer pid = plantInfo.getId();
            plantsOrder.setId(pid);
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }
            // 电站类型b
            plantsOrder.setPlantTypeB(typeBList.get(i + start));
            plantsOrder.setStatus(statuslist.get(i + start));
            plantsOrder.setPowerOutToday(powerOutList.get(i + start));
            plantsOrder.setPowerInToday(powerInList.get(i + start));
            // soc及系统效率
            Map<String, String> socMap = SystemCache.plantSOC;

            String plantSOC = socMap.get(pid + "");
            plantsOrder.setSoc(plantSOC);
            double totalPosEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalPosEnergy");
            double totalNegEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalNegEnergy");
            if (totalNegEnergy == 0.0 || totalNegEnergy == 0) {
                plantsOrder.setSystemEfficace("0");
            } else {
                BigDecimal pos = new BigDecimal(totalPosEnergy);
                BigDecimal neg = new BigDecimal(totalNegEnergy);
                BigDecimal divide = pos.divide(neg, 2, BigDecimal.ROUND_HALF_UP);
                plantsOrder.setSystemEfficace(divide.toString());
            }
            resultList.add(plantsOrder);
        }
    }

    public static void commSetEle2ForStoredSelectByType(List<PlantInfo> flist, int j, List<Double> powerOutList,
                                                        List<Double> powerInList, List<PlantListOrder> resultList, int start, int length) {
        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            plantsOrder.setId(plantInfo.getId());
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }
            plantsOrder.setPlantTypeB(j);
            // plantsOrder.setStatus(j.get(i));
            plantsOrder.setPowerOutToday(powerOutList.get(i + start));
            plantsOrder.setPowerInToday(powerInList.get(i + start));

            resultList.add(plantsOrder);
        }
    }

    public void commSetEleForStoredByStatus(List<PlantInfo> flist, List<Integer> typeBList, int j,
                                            List<Double> powerOutList, List<Double> powerInList, List<PlantListOrder> resultList, int start,
                                            int length) {
        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            plantsOrder.setId(plantInfo.getId());
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }
            plantsOrder.setPlantTypeB(typeBList.get(i + start));
            plantsOrder.setStatus(j);
            plantsOrder.setPowerOutToday(powerOutList.get(i + start));
            plantsOrder.setPowerInToday(powerInList.get(i + start));
            // soc及系统效率
            Integer pid = plantInfo.getId();
            Map<String, String> socMap = SystemCache.plantSOC;
            String plantSOC = socMap.get(pid + "");
            plantsOrder.setSoc(plantSOC);
            double totalPosEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalPosEnergy");
            double totalNegEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalNegEnergy");
            if (totalNegEnergy == 0.0 || totalNegEnergy == 0) {
                plantsOrder.setSystemEfficace("0");
            } else {
                BigDecimal pos = new BigDecimal(totalPosEnergy);
                BigDecimal neg = new BigDecimal(totalNegEnergy);
                BigDecimal divide = pos.divide(neg, 2, BigDecimal.ROUND_HALF_UP);
                plantsOrder.setSystemEfficace(divide.toString());
            }
            resultList.add(plantsOrder);
        }
    }

    public void commSetEleForStoredByType(List<PlantInfo> flist, List<Integer> statusList, int j,
                                          List<Double> powerOutList, List<Double> powerInList, List<PlantListOrder> resultList, int start,
                                          int length) {
        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            plantsOrder.setId(plantInfo.getId());
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }
            plantsOrder.setPlantTypeB(j);
            plantsOrder.setStatus(statusList.get(i + start));
            plantsOrder.setPowerOutToday(powerOutList.get(i + start));
            plantsOrder.setPowerInToday(powerInList.get(i + start));
            // soc及系统效率
            Integer pid = plantInfo.getId();
            Map<String, String> socMap = SystemCache.plantSOC;
            String plantSOC = socMap.get(pid + "");
            plantsOrder.setSoc(plantSOC);
            double totalPosEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalPosEnergy");
            double totalNegEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalNegEnergy");
            if (totalNegEnergy == 0.0 || totalNegEnergy == 0) {
                plantsOrder.setSystemEfficace("0");
            } else {
                BigDecimal pos = new BigDecimal(totalPosEnergy);
                BigDecimal neg = new BigDecimal(totalNegEnergy);
                BigDecimal divide = pos.divide(neg, 2, BigDecimal.ROUND_HALF_UP);
                plantsOrder.setSystemEfficace(divide.toString());
            }
            resultList.add(plantsOrder);
        }
    }

    public void commSetEle2NoTypeAndStatusSelected(List<PlantInfo> flist, int k, int j, List<Double> powerOutList,
                                                   List<Double> powerInList, List<PlantListOrder> resultList, int start, int length) {
        if (flist != null) {
            for (int i = 0; i < flist.size(); i++) {
                PlantListOrder plantsOrder = new PlantListOrder();
                PlantInfo plantInfo = flist.get(i);
                plantsOrder.setId(plantInfo.getId());
                plantsOrder.setType(plantInfo.getPlantTypeA());
                plantsOrder.setName(plantInfo.getPlantName());
                plantsOrder.setAddr(plantInfo.getPlantAddr());
                plantsOrder.setLocation(plantInfo.getLoaction());
                String unit = plantInfo.getUnit();
                if (unit.equalsIgnoreCase("MW")) {
                    plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
                } else if (unit.equalsIgnoreCase("GW")) {
                    plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
                } else {
                    plantsOrder.setCapacity(plantInfo.getCapacity());
                }
                plantsOrder.setPlantTypeB(k);
                plantsOrder.setStatus(j);
                plantsOrder.setPowerOutToday(powerOutList.get(i + start));
                plantsOrder.setPowerInToday(powerInList.get(i + start));
                // soc及系统效率
                Integer pid = plantInfo.getId();
                Map<String, String> socMap = SystemCache.plantSOC;
                String plantSOC = socMap.get(pid + "");
                plantsOrder.setSoc(plantSOC);
                double totalPosEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalPosEnergy");
                double totalNegEnergy = (double) statisticalGainService.getNowDayIncome(pid + "").get("totalNegEnergy");
                if (totalNegEnergy == 0.0 || totalNegEnergy == 0) {
                    plantsOrder.setSystemEfficace("0");
                } else {
                    BigDecimal pos = new BigDecimal(totalPosEnergy);
                    BigDecimal neg = new BigDecimal(totalNegEnergy);
                    BigDecimal divide = pos.divide(neg, 2, BigDecimal.ROUND_HALF_UP);
                    plantsOrder.setSystemEfficace(divide.toString());
                }
                resultList.add(plantsOrder);
            }
        }
    }

    public void commSetEle2(List<PlantInfo> flist, List<Integer> statuslist, List<Double> powerNowList,
                            List<Double> powerList, List<PlantListOrder> resultList, int start, int length) {
        for (int i = 0; i < flist.size(); i++) {
            PlantListOrder plantsOrder = new PlantListOrder();
            PlantInfo plantInfo = flist.get(i);
            plantsOrder.setId(plantInfo.getId());
            plantsOrder.setType(plantInfo.getPlantTypeA());
            plantsOrder.setName(plantInfo.getPlantName());
            plantsOrder.setAddr(plantInfo.getPlantAddr());
            plantsOrder.setLocation(plantInfo.getLoaction());
            String unit = plantInfo.getUnit();
            if (unit.equalsIgnoreCase("MW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000);
            } else if (unit.equalsIgnoreCase("GW")) {
                plantsOrder.setCapacity(plantInfo.getCapacity() * 1000000);
            } else {
                plantsOrder.setCapacity(plantInfo.getCapacity());
            }

            plantsOrder.setStatus(statuslist.get(i + start));
            plantsOrder.setGenToday(powerNowList.get(i + start));
            plantsOrder.setGenTotal(powerList.get(i + start));
            BigDecimal b = new BigDecimal(String.valueOf(plantsOrder.getCapacity()));
            BigDecimal bg = new BigDecimal(powerNowList.get(i + start) / b.doubleValue());
            double ppr = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            plantsOrder.setPpr(ppr);
            SysUser sysUser = plantInfo.getContactUser();
            if (sysUser == null) {
                plantsOrder.setContectsTel("--");
                plantsOrder.setContacts("--");
            } else {
                plantsOrder.setContectsTel(sysUser.getUserTel());
                plantsOrder.setContacts(sysUser.getUserName());
            }
            resultList.add(plantsOrder);
        }
    }

    @Override
    public MessageBean getMultiEtGenProfit(String jsonData, Session session, HttpServletRequest request)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        String genDay = null;
        String profitDay = null;
        // 多电站今日发电量及收益
        MessageBean mb2 = wxmonitorService.getDynamic(jsonData, session, null);
        Map<String, Object> body2 = (Map<String, Object>) mb2.getBody();
        Map<String, Object> ele = (Map<String, Object>) body2.get("genPro");
        // 今日发电单位
        String genUnity = ele.get("genUToday") + "";
        // 今日发电
        genDay = ele.get("genToday") + genUnity;
        // 今日收益单位
        String proUnity = ele.get("proUToday") + "";
        // 今日收益
        profitDay = ele.get("proToday") + proUnity;
        Map<String, Object> eMap = new HashMap<>();
        eMap.put("genDay", genDay);
        eMap.put("profitDay", profitDay);
        msg.setBody(eMap);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getPlantPower(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        Map<String, Object> map = Util.parseURL(jsonData);
        MessageBean msg = new MessageBean();
        PlantInfo plantInfo = plantMapper.getPlantInfo(map.get("plantId") + "");
        // 判断电站类型
        Integer plantTypeA = plantInfo.getPlantTypeA();
        String pId = plantInfo.getId() + "";
        String nowTime = String.valueOf(map.get("dataTime"));
        if (!Util.isNotBlank(nowTime)) {
            nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
        }
        Map<String, Object> result;
        try {
            result = statisticalGainService.getPowerAndDataTime(pId, nowTime, plantInfo);
        } catch (ParseException e) {
            throw new ServiceException(Msg.TYPE_TIME_ERROR);
        }
        msg.setBody(result);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }
    /*
     * public MessageBean plantDist(String jsonData, Session session) throws
     * ServiceException, SessionException, SessionTimeoutException{ Map<String,
     * Object> map = Util.parseURL(jsonData); map.put("serviceType",
     * map.get("type"));
     *//** 获取地图上下钻之后的电站信息及区域信息 */

    /*
     * MessageBean msg=new MessageBean(); List<PlantListOrder>resultList=new
     * ArrayList<>(); Map<String, Object>resultMap=new HashMap<>(1); List<Integer>
     * plantList=plantReportService.getPlantListByArea(map, session);
     * if(Util.isNotBlank(plantList)){
     */

    /**
     * 开始获取电站id集合
     *//*
     * List<Integer>plantIds=null; if (Util.isNotBlank(plantList)) { List<PlantInfo>
     * plantSqlList=plantMapper.getPlantByIds(plantList); if
     * (Util.isNotBlank(plantSqlList)) { for (PlantInfo plantInfo : plantSqlList) {
     * String plantId=String.valueOf(plantInfo.getId()); PlantListOrder
     * plantOrder=new PlantListOrder(); plantOrder.setId(plantInfo.getId());
     * plantOrder.setName(plantInfo.getPlantName());
     * plantOrder.setStatus(ServiceUtil.getPlantStatus(plantId));
     * plantOrder.setAddr(plantInfo.getPlantAddr()); String unit =
     * plantInfo.getUnit(); double capacity=plantInfo.getCapacity(); if
     * ("MW".equalsIgnoreCase(unit)) { capacity=Util.multFloat(capacity, 1000, 0);
     * }else if ("GW".equalsIgnoreCase(unit)) { capacity=Util.multFloat(capacity,
     * 1000000, 0); } plantOrder.setCapacity(capacity); SysUser
     * contactUser=plantInfo.getContactUser();
     * plantOrder.setContacts(contactUser==null?"--":contactUser.getUserName());
     * plantOrder.setContectsTel(contactUser==null?"--":contactUser.getUserTel());
     * Map<String,
     * Object>genIncomMap=statisticalGainService.getNowDayIncome(plantId); //今日发电
     * String
     * genDay="null".equals(genIncomMap.get("power")+"")?"0":genIncomMap.get("power"
     * )+""; double today=Util.roundDouble(Double.parseDouble(genDay),2);
     * plantOrder.setGenToday( today); Map<String,
     * Double>historyMap=priceMapper.getHistoryIncome(plantId); double
     * total=Util.roundDouble("null".equals(plantOrder.getGenToday()+"")?0.0:
     * Util.addFloat(historyMap==null?0:historyMap.get("power"),Double.parseDouble(
     * genDay), 0)); plantOrder.setGenTotal(total);
     * plantOrder.setPpr(Util.divideDouble(genDay, capacity, 2));
     * plantOrder.setWeather(plantInfoService.getPlantTodayWeather(plantInfo.getId()
     * ).getDayPictureUrl()); plantOrder.setLocation(plantInfo.getLoaction());
     * resultList.add(plantOrder); } resultMap.put("plantsInfo", resultList); }else
     * { resultMap.put("plantsInfo", null); } }else { resultMap.put("plantsInfo",
     * null); } } msg.setBody(resultMap); msg.setCode(Header.STATUS_SUCESS); return
     * msg; }
     */
    @Override
    public MessageBean plantStatusPower(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = map.get("plantId") + "";
        Map<String, Object> genIncome = new HashMap<>(3);
        PlantInfo plantInfo = plantMapper.getPlantInfo(String.valueOf(map.get("plantId")));
        Double capacity = plantInfo.getCapacity();
        String unit = plantInfo.getUnit();
        String activcePower = statisticalGainService.getNowDayIncome(pId).get("activcePower") + "";
        double activePower = 0.0;
        if (Util.isNotBlank(activcePower)) {
            activePower = Double.valueOf(statisticalGainService.getNowDayIncome(pId).get("activcePower") + "");
        }
        if (unit.equalsIgnoreCase("MW")) {
            activePower = Util.divideDouble(activePower, 1000, 1);
        } else if (unit.equalsIgnoreCase("GW")) {
            activePower = Util.divideDouble(activePower, 1000000, 1);
        }
        int plantType = ServiceUtil.getPlantStatus(pId);
        genIncome.put("status", ServiceUtil.getPlantStatusString(plantType));
        genIncome.put("type", plantType);
        genIncome.put("capacity", capacity);
        genIncome.put("capacityUnit", unit);
        genIncome.put("activePower", activePower);
        genIncome.put("activePowerUnit", unit);
        msg.setBody(genIncome);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean singlePlantDistribution(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = map.get("plantId") + "";
        Map<String, Object> genIncome = new HashMap<>(3);
        Map<String, Object> hisMap = statisticalGainService.getHistoryIncome(pId);
        /** 社会贡献 */
        // 得到累计发电量
        Double totalPower = (Double) hisMap.get("power");
        // 从配置文件中获取二氧化碳减排参数
        String CO2 = configParam.getCo2();
        // 从配置文件中获取等效植树量参数
        String tree = configParam.getTree();
        // 从配置文件中获取标准煤节约参数
        String coal = configParam.getCoal();
        Double numCo2 = Util.multFloat(totalPower, Double.valueOf(CO2), 1);
        Double numTree = Util.multFloat(totalPower, Double.valueOf(tree), 1);
        Double numCoal = Util.multFloat(totalPower, Double.valueOf(coal), 1);
        List<Object> contribution = new ArrayList<Object>();
        Map<String, Object> co2Map = new HashMap<String, Object>();
        co2Map.put("name", "CO²减排");
        co2Map.put("value", numCo2);
        co2Map.put("unit", "kg");
        contribution.add(co2Map);
        Map<String, Object> treeMap = new HashMap<String, Object>();
        treeMap.put("name", "等效植树");
        treeMap.put("value", Util.roundDoubleToInt(numTree));
        treeMap.put("unit", "棵");
        contribution.add(treeMap);
        Map<String, Object> coalMap = new HashMap<String, Object>();
        coalMap.put("name", "节约标准煤");
        coalMap.put("value", numCoal);
        coalMap.put("unit", "kg");
        contribution.add(coalMap);
        genIncome.put("contribution", contribution);
        /** 电站最近三天天气 */
        // genIncome.put("recentWeather",plantInfoService.getPlantThreeWeather(Integer.parseInt(pId))
        // );
        /** 电站容量 */
        msg.setBody(genIncome);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean singlePlantStatus(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = map.get("plantId") + "";
        Map<String, Object> genIncome = new HashMap<>(3);
        int plantType = ServiceUtil.getPlantStatus(pId);
        genIncome.put("status", ServiceUtil.getPlantStatusString(plantType));
        genIncome.put("type", plantType);
        msg.setBody(genIncome);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getPlantName(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        PlantInfo plantInfo = plantMapper.getPlantInfo(String.valueOf(map.get("plantId")));
        String pId = map.get("plantId") + "";
        map.put("name", plantInfo.getPlantName());
        msg.setBody(map);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getCollectorList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listCollectorByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
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
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getInverterList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listInverterByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                temMap.put("power", "NaN");
                temMap.put("genDay", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
                temMap.put("genDay", monitorServiceImpl.getNowdayIncomeOfDevice(deviceId));
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
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String guid = guidMap.get(deviceId).get("power");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
                    temMap.put("power", "NaN");
                } else {
                    temMap.put("power",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getCenInverterList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listCenInverterByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                temMap.put("power", "NaN");
                temMap.put("genDay", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
                temMap.put("genDay", monitorServiceImpl.getNowdayIncomeOfDevice(deviceId));
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
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String guid = guidMap.get(deviceId).get("power");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
                    temMap.put("power", "NaN");
                } else {
                    temMap.put("power",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getPCSList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listPcsByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                temMap.put("power", "NaN");
                temMap.put("doe", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
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
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String guid = guidMap.get(deviceId).get("power");
                String energyGuid = guidMap.get(deviceId).get("posEnergy");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
                    temMap.put("power", "NaN");
                } else {
                    temMap.put("power",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                }
                if (currentParam == null || currentParam.isEmpty() || energyGuid == null
                        || StringUtils.isBlank(energyGuid)) {
                    temMap.put("doe", "NaN");
                } else {
                    temMap.put("doe",
                            currentParam.containsKey(energyGuid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(energyGuid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getBMSList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listBmsByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                temMap.put("SOC", "NaN");
                temMap.put("resEle", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
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
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String guid = guidMap.get(deviceId).get("soc");
                String energyGuid = guidMap.get(deviceId).get("energy");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)
                        || energyGuid == null || StringUtils.isBlank(energyGuid)) {
                    temMap.put("soc", "NaN");
                    temMap.put("resEle", "NaN");
                } else {
                    temMap.put("soc",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                    temMap.put("resEle",
                            currentParam.containsKey(energyGuid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(energyGuid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getMeterList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listMeterByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                // 正向电量
                temMap.put("peq", "NaN");
                // 反向电量
                temMap.put("neq", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
                int status = deviceOfStatus.get(deviceId);
                switch (status) {
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
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String guid = guidMap.get(deviceId).get("posEnergy");
                String energyGuid = guidMap.get(deviceId).get("negEnergy");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)
                        || energyGuid == null || StringUtils.isBlank(energyGuid)) {
                    temMap.put("peq", "NaN");
                    temMap.put("neq", "NaN");
                } else {
                    temMap.put("peq",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                    temMap.put("neq",
                            currentParam.containsKey(energyGuid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(energyGuid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getConfBoxList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listConfBoxByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                // 电压
                temMap.put("voltage", "NaN");
                // 电流
                temMap.put("elecCurrent", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
                int status = deviceOfStatus.get(deviceId);
                switch (status) {
                    case 0:
                        temMap.put("type", 0);
                        temMap.put("status", "正常");
                        break;
                    case 2:
                        temMap.put("type", 2);
                        temMap.put("status", "故障");
                        break;
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    case 3:
                        temMap.put("type", 3);
                        temMap.put("status", "通讯中断");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                // 电压点位
                String guid = guidMap.get(deviceId).get("voltage");
                // 电流点位
                String elecGuid = guidMap.get(deviceId).get("electricCurrent");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
                    temMap.put("voltage", "NaN");
                } else {
                    temMap.put("voltage",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                }
                if (currentParam == null || currentParam.isEmpty() || elecGuid == null
                        || StringUtils.isBlank(elecGuid)) {
                    temMap.put("elecCurrent", "NaN");
                } else {
                    temMap.put("elecCurrent",
                            currentParam.containsKey(elecGuid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(elecGuid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsFiltered", size);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getBoxChangeList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<CollDevice> deviceList = deviceMapper.listBoxChangeByPlantId(plantId);
        int size = deviceList.size();
        if (Util.isNotBlank(deviceList)) {
            Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
            for (int i = 0; i < size; i++) {
                Map<String, Object> temMap = new HashMap<String, Object>();
                CollDevice collDevice = deviceList.get(i);
                temMap.put("deviceType", collDevice.getDeviceType());
                temMap.put("id", collDevice.getId());
                temMap.put("name", collDevice.getDeviceName());
                // 电压
                temMap.put("voltage", "NaN");
                // 有功功率
                temMap.put("activePower", "NaN");
                String deviceId = String.valueOf(collDevice.getId());
                int status = deviceOfStatus.get(deviceId);
                switch (status) {
                    case 0:
                        temMap.put("type", 0);
                        temMap.put("status", "正常");
                        break;
                    case 2:
                        temMap.put("type", 2);
                        temMap.put("status", "故障");
                        break;
                    case 4:
                        temMap.put("type", 4);
                        temMap.put("status", "异常");
                        break;
                    case 3:
                        temMap.put("type", 3);
                        temMap.put("status", "通讯中断");
                        break;
                    default:
                        break;
                }
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                // 电压点位
                String guid = guidMap.get(deviceId).get("voltage");
                // 有功功率点位
                String elecGuid = guidMap.get(deviceId).get("power");
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                if (currentParam == null || currentParam.isEmpty() || guid == null || StringUtils.isBlank(guid)) {
                    temMap.put("voltage", "NaN");
                } else {
                    temMap.put("voltage",
                            currentParam.containsKey(guid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(guid)))
                                    : "NaN");
                }
                if (currentParam == null || currentParam.isEmpty() || elecGuid == null
                        || StringUtils.isBlank(elecGuid)) {
                    temMap.put("activePower", "NaN");
                } else {
                    temMap.put("activePower",
                            currentParam.containsKey(elecGuid)
                                    ? Util.getDecimalFomat2().format(Double.parseDouble(currentParam.get(elecGuid)))
                                    : "NaN");
                }
                resultList.add(temMap);
            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", resultList);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getEnviroDetectorList(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        String plantId = map.get("plantId") + "";
        List<Map<String, Object>> result = deviceMapper.listEnvMonitorByPlantId(plantId);
        int size = result != null ? result.size() : 0;
        if (Util.isNotBlank(result)) {
            for (Map<String, Object> device : result) {
                Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;
                String deviceId = String.valueOf(device.get("id"));
                int status = deviceOfStatus.get(deviceId);
                switch (status) {
                    case 0:
                        device.put("type", 0);
                        device.put("status", "正常");
                        break;
                    case 3:
                        device.put("type", 3);
                        device.put("status", "通讯中断");
                        break;
                    default:
                        break;
                }
                Map<String, String> currentParam = cacheUtil.getMap(deviceId);
                Map<String, Map<String, String>> guidMap = SystemCache.devicesGuid;
                String radiationTotalOne = guidMap.get(deviceId).get("radiation_total_one");
                // 瞬间总辐射
                String itr = currentParam.get(radiationTotalOne);
                Object roundItr = null;
                if (itr != null) {
                    roundItr = Util.roundDouble(Double.parseDouble(itr), 2);
                } else {
                    roundItr = "--";
                }
                device.put("itr", roundItr);

            }
        }
        Map<String, Object> data = new HashMap<String, Object>(3);
        data.put("data", result);
        data.put("recordsTotal", size);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(data);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getBatCap(String jsonData) throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String plantId = String.valueOf(map.get("plantId"));
        // 电站总容量 当前容量
        Map<String, Object> finalMap = new HashMap<>();
        // Double bmsCapacity=0.0;
        Double bmsLeftCapacity = 0.0;
        Map<String, Object> capacityPercent = bmsMapper.getCapacityPercent(plantId);
        double totalCapaD = 0.0;
        if (Util.isNotBlank(capacityPercent)) {
            String totalCapa = capacityPercent.get("totalCapa") + "";
            if (Util.isNotBlank(totalCapa)) {
                BigDecimal bg = new BigDecimal(totalCapa);
                totalCapaD = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        if (totalCapaD == 0.0) {
            PlantInfo plant = plantMapper.getPlantById(plantId);
            String unit = plant.getUnit();
            if ("mwh".equalsIgnoreCase(unit)) {
                totalCapaD = Util.multFloat(plant.getCapacity(), 1000.0, 1);
            } else {
                totalCapaD = plant.getCapacity();
            }
        }
        Map<Integer, List<Integer>> deviceMap = SystemCache.deviceOfPlant.get(Integer.parseInt(plantId));
        if (Util.isNotBlank(deviceMap)) {
            List<Integer> devieList = deviceMap.get(10);
            if (Util.isNotBlank(devieList)) {
                for (int i = 0; i < devieList.size(); i++) {
                    Integer integer = devieList.get(i);
                    Map<String, String> map2 = SystemCache.devicesGuid.get(integer + "");

                    double power2 = 0.0;
                    if (Util.isNotBlank(map2) && cacheUtil.exist(String.valueOf(integer))) {
                        Map<String, String> dataMap = cacheUtil.getMap(String.valueOf(integer));
                        /*
                         * if (map2.containsKey("capacity")&&map2.get("capacity")!=null&&dataMap.
                         * containsKey("capacity")) { if (Util.isNotBlank(dataMap.get("capacity"))) {
                         * power1 = Double.parseDouble(dataMap.get("capacity")); } } bmsCapacity +=
                         * power1;
                         */
                        if (map2.containsKey("energy") && map2.get("energy") != null && dataMap.containsKey("energy")) {
                            if (Util.isNotBlank(dataMap.get("energy"))) {
                                power2 = Double.parseDouble(dataMap.get("energy"));
                            }
                        }
                        bmsLeftCapacity += power2;
                    }

                }
            }
        }
        /*
         * double bmsCapacity = 0.0; double bmsLeftCapacity = 0.0; if
         * (Util.isNotBlank(bms_capacity)) { bmsCapacity =
         * Double.parseDouble(bms_capacity); } if (Util.isNotBlank(bms_left_capacity)) {
         * bmsCapacity = Double.parseDouble(bms_left_capacity); }
         */
        finalMap.put("batCap", totalCapaD);
        finalMap.put("batCapUnit", "度");
        finalMap.put("curCap", bmsLeftCapacity);
        finalMap.put("curCapUnit", "度");
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(finalMap);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getSingleEneProfit(String jsonData)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = map.get("plantId") + "";
        Map<String, Object> genIncome = new HashMap<>(5);
        Map<String, Double> todayMap = statisticalGainService.getNowDayIncome(pId);
        double dailyNegEnergy = 0.0;
        double dailyPosEnergy = 0.0;
        double dailyPrice = 0.0;
        double totalPrice = 0.0;
        if (Util.isNotBlank(todayMap)) {
            if (todayMap.containsKey("dailyNegEnergy")) {
                dailyNegEnergy = (double) todayMap.get("dailyNegEnergy");
            }
            if (todayMap.containsKey("dailyPosEnergy")) {
                dailyPosEnergy = (double) todayMap.get("dailyPosEnergy");
            }
            if (todayMap.containsKey("dailyPrice")) {
                dailyPrice = (double) todayMap.get("dailyPrice");
            }
            if (todayMap.containsKey("totalPrice")) {
                totalPrice = (double) todayMap.get("totalPrice");
            }
        }
        todayMap.get("dailyPrice");
        // 今日充电、单位
        genIncome.put("chargeDay", dailyNegEnergy);
        genIncome.put("chargeDayUnit", "度");
        // 今日放电、单位
        genIncome.put("dischargeDay", dailyPosEnergy);
        genIncome.put("dischargeDayUnit", "度");
        // 今日收益、单位
        genIncome.put("profitDay", dailyPrice);
        genIncome.put("profitDayUnit", "元");
        // 总收益、单位
        genIncome.put("genTotal", totalPrice);
        genIncome.put("genTotalUnit", "元");

        msg.setBody(genIncome);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    public static void main(String[] args) {
        double[] d = {2.0, 2.0, 2.0, 2.0};
        double power1 = 0.0;
        for (double e : d) {
            power1 += e;
        }
        System.out.println(power1);
    }

    @Override
    public MessageBean getCNTopo(String jsonData) throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        String pId = String.valueOf(map.get("plantId"));
        Map<String, Object> finalMap = new HashMap<>();
        // Map<String,Object> eleMap = pcsMapper.getBatAndGridPower(pId);
        Map<Integer, List<Integer>> deviceMap = SystemCache.deviceOfPlant.get(Integer.parseInt(pId));
        Double powerD = 0.0;
        Double powerBat = 0.0;
        if (Util.isNotBlank(deviceMap)) {
            List<Integer> devieList = deviceMap.get(9);
            if (Util.isNotBlank(devieList)) {
                for (int i = 0; i < devieList.size(); i++) {
                    Integer integer = devieList.get(i);
                    Map<String, String> map2 = SystemCache.devicesGuid.get(integer + "");
                    double power1 = 0.0;
                    double power2 = 0.0;
                    if (Util.isNotBlank(map2) && cacheUtil.exist(String.valueOf(integer))) {
                        Map<String, String> dataMap = cacheUtil.getMap(String.valueOf(integer));
                        if (map2.containsKey("power") && map2.get("power") != null && dataMap.containsKey("power")) {
                            if (Util.isNotBlank(dataMap.get("power"))) {
                                power1 = Double.parseDouble(dataMap.get("power"));
                            }
                        }
                        powerD += power1;
                        if (map2.containsKey("powerBat") && map2.get("powerBat") != null
                                && dataMap.containsKey("powerBat")) {
                            if (Util.isNotBlank(dataMap.get("powerBat"))) {
                                power2 = Double.parseDouble(dataMap.get("powerBat"));
                            }
                        }
                        powerBat += power2;
                    }

                }
            }
        }
        if (powerD == 0.0) {
            finalMap.put("gridPower", "0kW");
        } else {
            finalMap.put("gridPower", powerD + "kW");
        }
        if (powerBat == 0.0) {
            finalMap.put("batPower", "0kW");
        } else {
            finalMap.put("batPower", powerBat + "kW");
        }
        msg.setBody(finalMap);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setMsg(Msg.SELECT_SUCCUESS);
        return msg;
    }

    @Override
    public MessageBean getSingleElecPro(String jsonData)
            throws SessionException, ServiceException, SessionTimeoutException, ParseException {
        MessageBean msgTemp = monitorService.getProSearch2(jsonData);
        // 柱状图
        Object obj = msgTemp.getBody();
        MessageBean msg = new MessageBean();
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("historyEnergy", obj);
        msg.setBody(obj);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getWeatherInfo(String jsonData)
            throws SessionException, ServiceException, SessionTimeoutException, ParseException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(jsonData);
        int plantId = Integer.parseInt(String.valueOf(map.get("plantId")));
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(plantInfoService.getPlantThreeWeather(plantId));
        return msg;
    }

    @Override
    public MessageBean getSinglePowerCurve(String jsonData)
            throws SessionException, ServiceException, SessionTimeoutException, ParseException {
        Map<String, Object> map = Util.parseURL(jsonData);
        MessageBean msg = new MessageBean();
        PlantInfo plantInfo = plantMapper.getPlantInfo(map.get("plantId") + "");
        String pId = plantInfo.getId() + "";
        String nowTime = String.valueOf(map.get("dataTime"));
        if (!Util.isNotBlank(nowTime)) {
            nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
        }
        /*
         * //获取电站设备集合 List<CollDevice>deviceList=deviceMapper.getDeviceListByPid(pId);
         * //按照设备类型分类 Map<Integer,
         * List<Integer>>deviceMap=ServiceUtil.classifyDeviceForPlant(deviceList);
         */
        // 将对应电站的对应时间点的功率装载到时间点位上
        // 功率
        Map<String, Object> result;
        try {
            result = statisticalGainService.getPowerAndDataTime(pId, nowTime, plantInfo);
        } catch (ParseException e) {
            throw new ServiceException(Msg.TYPE_TIME_ERROR);
        }
        List<Map<String, Object>> finalList = new ArrayList<>();
        String[] yData1 = (String[]) result.get("yData1");
        Object[] xData = (Object[]) result.get("xData");
        Map<String, Object> nameMap = new HashMap<>();
        nameMap.put("name", "充放电功率");
        nameMap.put("type", "line");
        nameMap.put("value", yData1);
        finalList.add(nameMap);
        Map<String, Object> finalMap = new HashMap<>();
        finalMap.put("unit", result.get("unit"));
        finalMap.put("xData", xData);
        finalMap.put("yData", finalList);
        msg.setBody(finalMap);
        msg.setCode(Header.STATUS_SUCESS);
        return msg;
    }

    @Override
    public MessageBean getPlantOpen(String jsonData, Session session, HttpServletRequest request)
            throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException,
            ParseException {
        MessageBean msg = new MessageBean();
        List<Map<String, Object>> ListFront = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = Util.parseURL(jsonData);
        // 当前电站id
        String plantId = map.get("plantId") + "";
        // 当前电站图片
        String photo = plantMapper.getPhoto(plantId);
        // 当前电站经纬度
        String loList = plantMapper.getLocation(plantId);
        String[] location = loList.split(",");
        String temp = location[0];
        location[0] = location[1];
        location[1] = temp;
        // 电站抛物线
        Map<String, Object> msgCurve = getPowerCurveForMultiStored(jsonData);
        // 设备状态分类
        Map<Integer, List<Integer>> devices = SystemCache.deviceOfPlant.get(Integer.parseInt(plantId));
        int dCount = 0, normal = 0, breakNum = 0, standby = 0, fault = 0;
        Map<String, Integer> status = SystemCache.deviceOfStatus;
        if (devices != null) {
            for (Integer key : devices.keySet()) {
                List<Integer> tempList = devices.get(key);
                if (tempList != null && !tempList.isEmpty()) {
                    dCount += tempList.size();
                    for (Integer deviceId : tempList) {
                        int type = status.containsKey(String.valueOf(deviceId)) ? status.get(String.valueOf(deviceId))
                                : -1;
                        switch (type) {
                            case 0:
                                normal++;
                                break;
                            case 1:
                                standby++;
                                break;
                            case 2:
                                fault++;
                                break;
                            case 3:
                                breakNum++;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        Map<String, Object> cMap = new HashMap<>();
        cMap.put("standby", standby);
        cMap.put("fault", fault);
        cMap.put("break", breakNum);
        cMap.put("normal", normal);
        cMap.put("deviceNum", dCount);
        String[] photos = null;
        if (Util.isNotBlank(photo)) {
            photos = StringUtils.split(photo, ";");
        }
        String pic = null;
        /** 微信小程序默认返回第一张图片 */
        if (Util.isNotBlank(photos)) {
            pic = photos[0];
        }
        cMap.put("image", pic);
        cMap.put("location", location);
        cMap.put("powerCurve", msgCurve);
        ListFront.add(cMap);
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(ListFront);
        return msg;
    }

    public Map<String, Object> getPowerCurveForMultiStored(String jsonData)
            throws SessionException, ServiceException, SessionTimeoutException, ParseException {
        Map<String, Object> map = Util.parseURL(jsonData);
        MessageBean msg = new MessageBean();
        PlantInfo plantInfo = plantMapper.getPlantInfo(map.get("plantId") + "");
        String pId = plantInfo.getId() + "";
        String nowTime = String.valueOf(map.get("dataTime"));
        if (!Util.isNotBlank(nowTime)) {
            nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
        }
        String today = Util.getCurrentTimeStr("yyyy-MM-dd");
        String[] location = StringUtils.split(plantInfo.getLoaction(), ",");
        // String
        // riseTime=ServiceUtil.getSunRise(nowTime,Double.parseDouble(location[0]),
        // Double.parseDouble(location[1]));
        // String setTime=ServiceUtil.getSunSet(nowTime,Double.parseDouble(location[0]),
        // Double.parseDouble(location[1]));
        // List<String>timeList=ServiceUtil.getStringTimeSegment(riseTime,setTime);
        List<String> timeList = ServiceUtil.getStringTimeSegment("00:00", "23:59");
        Map<String, Object> activePower = new HashMap<>(6);
        // 获取电站设备集合
        List<CollDevice> deviceList = deviceMapper.getDeviceListByPid(pId);
        // 按照设备类型分类
        Map<Integer, List<Integer>> deviceMap = ServiceUtil.classifyDeviceForPlant(deviceList);
        // 将对应电站的对应时间点的功率装载到时间点位上
        // 功率
        String curTime = timeList.get(0);
        int nowLocation = 0;
        if (nowTime.equals(today)) {
            curTime = ServiceUtil.getNowTimeInTimes();
            nowLocation = ServiceUtil.getNowLocationInTimes(curTime, timeList);
        } else {
            curTime = "";
            nowLocation = timeList.size() - 1;
        }
        List<Map<String, Object>> result;
        try {
            result = statisticalGainService.getPowerAndDataTimeForMulti("00:00", "23:59", deviceMap, pId, nowLocation,
                    nowTime);
        } catch (ParseException e) {
            throw new ServiceException(Msg.TYPE_TIME_ERROR);
        }
        /*
         * String[]yData1=(String[]) result.get("yData1"); //单位 String
         * unit=String.valueOf(result.get("unit")); //电站类型 Integer plantTypeA =
         * plantInfo.getPlantTypeA(); if (plantTypeA==1) { if
         * (nowLocation<yData1.length) { String
         * str=String.valueOf(statisticalGainService.getNowDayIncome(pId).get(
         * "activcePower")); double
         * power="".equals(StringUtil.checkBlank(str))?0:Double.parseDouble(str); if
         * ("兆瓦".equals(unit)) { yData1[nowLocation]=String.valueOf(new
         * BigDecimal(power/1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
         * }else if ("吉瓦".equals(unit)) { yData1[nowLocation]=String.valueOf(new
         * BigDecimal(power/1000000).setScale(2,
         * BigDecimal.ROUND_HALF_UP).doubleValue()); } } double
         * value=Double.valueOf(yData1[nowLocation]); //当前时间
         * activePower.put("curTime",curTime); double
         * maxValue=Double.parseDouble(result.get("maxValue")+""); if (maxValue==0.0) {
         * maxValue=0.5; } if (maxValue<value) { maxValue=value; } String[]
         * yData2=ServiceUtil.getParabola(yData1,maxValue); //抛物线
         * activePower.put("yData2",yData2); //当前抛物线高度 activePower.put("curData2",
         * yData2[nowLocation]); } else if(plantTypeA==2) {
         *
         * }
         */
        /**/
        activePower.put("yData", result);
        // 时间轴
        Object[] xData = timeList.toArray();
        activePower.put("xData", xData);
        // 当前时间
        // activePower.put("unit", unit);
        return activePower;
    }

    @Override
    public MessageBean getPvTopo(String str, Session session)
            throws ServiceException, SessionException, SessionTimeoutException {
        MessageBean msg = new MessageBean();
        Map<String, Object> map = Util.parseURL(str);
        Integer plantId = Integer.valueOf(map.get("plantId") + "");
        Map<String, Object> resultMap = new HashMap<>();
        Map<Integer, List<Integer>> deviceMap = SystemCache.deviceOfPlant.get(plantId);
        Double activePower = 0.0;
        Double mpptPower = 0.0;

        // 得到所有设备状态 如果设备断连 不需要加上功率
        Map<String, Integer> deviceOfStatus = SystemCache.deviceOfStatus;

        if (Util.isNotBlank(deviceMap)) {
            // 集中式逆变器
            List<Integer> centralInverterList = deviceMap.get(1);
            if (Util.isNotBlank(centralInverterList)) {
                for (int i = 0, size = centralInverterList.size(); i < size; i++) {
                    String deviceId = centralInverterList.get(i) + "";

                    Integer deviceStatus = deviceOfStatus.get(deviceId);
                    // 如果设备断连不需要加上功率
                    if (deviceStatus != null && deviceStatus.intValue() != 3) {

                        Map<String, String> map2 = SystemCache.devicesGuid.get(deviceId);
                        String activeGuid = map2.get("power");
                        String mpptGuid = map2.get("dcPower");
                        Map<String, String> centralInverterMap = cacheUtil.getMap(deviceId);
                        if (centralInverterMap != null && !centralInverterMap.isEmpty() && activeGuid != null
                                && !StringUtils.isBlank(activeGuid)) {
                            if (centralInverterMap.containsKey(activeGuid)) {
                                activePower = Util.addFloat(activePower,
                                        Double.valueOf(centralInverterMap.get(activeGuid)), 0);
                            }
                        }
                        if (centralInverterMap != null && !centralInverterMap.isEmpty() && mpptGuid != null
                                && !StringUtils.isBlank(mpptGuid)) {
                            if (centralInverterMap.containsKey(mpptGuid)) {
                                mpptPower = Util.addFloat(mpptPower, Double.valueOf(centralInverterMap.get(mpptGuid)),
                                        0);
                            }
                        }
                    }
                }
            }
            // 组串式逆变器
            List<Integer> stringInverterList = deviceMap.get(2);
            if (Util.isNotBlank(stringInverterList)) {
                for (int i = 0, size = stringInverterList.size(); i < size; i++) {
                    String deviceId = stringInverterList.get(i) + "";
                    Map<String, String> map2 = SystemCache.devicesGuid.get(deviceId);
                    String activeGuid = map2.get("power");
                    String mpptGuid = map2.get("mpptPower");
                    Map<String, String> centralInverterMap = cacheUtil.getMap(deviceId);
                    Integer deviceStatus = deviceOfStatus.get(deviceId);
                    // 如果设备断连不需要加上功率
                    if (deviceStatus != null && deviceStatus.intValue() != 3) {
                        if (centralInverterMap != null && !centralInverterMap.isEmpty() && activeGuid != null
                                && !StringUtils.isBlank(activeGuid)) {
                            if (centralInverterMap.containsKey(activeGuid)) {
                                activePower = Util.addFloat(activePower,
                                        Double.valueOf(centralInverterMap.get(activeGuid)), 0);
                            }
                        }
                        if (centralInverterMap != null && !centralInverterMap.isEmpty() && mpptGuid != null
                                && !StringUtils.isBlank(mpptGuid)) {
                            if (centralInverterMap.containsKey(mpptGuid)) {
                                mpptPower = Util.addFloat(mpptPower, Double.valueOf(centralInverterMap.get(mpptGuid)),
                                        0);
                            }
                        }
                    }
                }
            }
        }
        if (activePower == 0.0) {
            resultMap.put("activePower", "0kW");
        } else {
            resultMap.put("activePower", activePower + "kW");
        }
        if (mpptPower == 0.0) {
            resultMap.put("mpptPower", "0kW");
        } else {
            resultMap.put("mpptPower", mpptPower + "kW");
        }
        msg.setCode(Header.STATUS_SUCESS);
        msg.setBody(resultMap);
        return msg;
    }

}
