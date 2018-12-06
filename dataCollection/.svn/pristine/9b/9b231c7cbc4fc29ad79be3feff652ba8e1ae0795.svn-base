package com.synpowertech.dataCollectionJar.utils;

import com.synpowertech.dataCollectionJar.domain.*;
import com.synpowertech.dataCollectionJar.initialization.*;
import com.synpowertech.dataCollectionJar.service.IDataSaveService;
import com.synpowertech.dataCollectionJar.timedTask.DataSaveTask;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * ***************************************************************************
 *
 * @version -----------------------------------------------------------------------------
 * VERSION           TIME                BY           CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 * 1.0    2017年11月16日下午3:53:15   SP0010             create
 * -----------------------------------------------------------------------------
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: XmlParseUtil
 * @Description:xml解析和封装的工具类，同时队解析后的数据进行处理
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 * ****************************************************************************
 */
public class XmlParseUtil implements InitializingBean {

/*	<?xml version="1.0" encoding="UTF-8"?>
	<mqttData>
	  <DataCollector id="ncyt02_dc">
	    <time>2016-11-22T03:39:30Z</time>
	    <type Type="Yc">
	      <yc pId="0" pName="模拟节点_YC0" dchg="F" q="1">0.000000</yc>
	      <yc pId="10" pName="模拟节点_YC10" dchg="F" q="0">3415.010010</yc>
	    </type>
	  </DataCollector>*/

    private static final Logger logger = LoggerFactory.getLogger(XmlParseUtil.class);

    @Autowired
    IDataSaveService dataSaveServiceTemp;

    static IDataSaveService dataSaveService;

    //保留六位小数存库，光伏计算使用四位，前台显示两位
    static DecimalFormat decimalFormat;

    public void afterPropertiesSet() throws Exception {
        //注入变量赋给静态变量
        dataSaveService = dataSaveServiceTemp;
        //保留六位小数存库，光伏计算使用四位，前台显示两位
        decimalFormat = new DecimalFormat("#.######");
        if (dataSaveService == null) {
            logger.error("XmlParseUtil's dataSaveService inits unsuccessfully！");
        } else {
            logger.info("XmlParseUtil's dataSaveService inits successfully！");
        }
    }

    /**
     * @param xmlStr: void
     * @Title: xmlStr2Map
     * @Description: 遥测遥信xml解析， 封装成Map<deviceId, Map<signalGuid, value>>存reids,变位遥信存数据库
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月6日上午9:33:13
     */
    public static void xmlStr2Map(String xmlStr) {
        //用于日志记录的简化map,Map<YX_0,value>,//原始值日志Map

        logger.debug("start to parse the message");
        Map<String, String> logMap = new HashMap<String, String>();

        //Map<deviceId, Map<signalGuid, value>>
        Map<String, String> timeMap = null;
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            logger.error("xmlStr2Map get an exception:{}", e);
            return;
        }
        Element mqttData = doc.getRootElement();
        Element DataCollector = mqttData.element("DataCollector");
        String collector_id = DataCollector.attributeValue("id");
        //根据采集节点id找到数采设备id
        Integer dataCollId = CacheMappingUtil.collectorId2devId(collector_id);
        if (dataCollId == null) {
            logger.warn("can not find the relevant dataCollector:{}", collector_id);
            return;
        }
        String dataColl_id = String.valueOf(dataCollId);

        Element time = DataCollector.element("time");
        String xmlTimeStr = time.getText();
        // 根据需要转化成毫秒字符串

        xmlTimeStr = TimeUtil4Xml.xmlTime2Str(xmlTimeStr);

        String data_time = xmlTimeStr;
        Long dataTime = Long.parseLong(data_time);
        //初始化timeMap
        timeMap = JedisUtil.getMap(dataColl_id) == null ? new HashMap<String, String>() : JedisUtil.getMap(dataColl_id);
        String dataTimeTemp = timeMap.get("data_time");
        logger.debug("parsing the message");
        //dataTime = TimeUtil.getTo5Int(dataTime);
        //*****************判断是否是历史数据分流,//数采时间超前(EMQ重启，数采不重启，对时主题,VPN连接主题丢失)，直接入库***************
        if ((dataTimeTemp != null && Long.valueOf(dataTimeTemp) > dataTime)) {
            data_time = TimeUtil.getTo5Int(Long.parseLong(data_time)) + "";
            //redis数据比报文数据新，只更新sys_time后，数据补采逻辑执行(历史数据补采)
            logger.info("start to parse a history message");
            //当前报文接收时间
            Long sys_time = TimeUtil4Xml.getMsec();
            String sys_time_str = String.valueOf(sys_time);
            timeMap.put("sys_time", sys_time_str);
            //历史数据只更新数采连接状态
            timeMap.put("conn_status", "1");
            logMap.put("sys_time", sys_time_str);
            //更新数据采集器中系统时间
            JedisUtil.setMap(dataColl_id, timeMap);
            logger.debug(dataColl_id + "-" + timeMap.toString());

            //数据补采逻辑
            //以设备为单位先解析封装，判断数据库中是否有该时刻数据，若有就不解析不存，如没有就解析后存库
            @SuppressWarnings("unchecked")
            List<Element> elements = DataCollector.elements("type");
            if (elements == null) {
                return;
            }
            for (Element type : elements) {
                String typeName = type.attributeValue("Type");
                @SuppressWarnings("unchecked")
                List<Element> detailTypes = type.elements(typeName.toLowerCase());
                if (detailTypes == null) {
                    continue;
                }
                //设备id temp;
                String devIdTemp = null;
                //补采的signalGuidMap不能从redis取，要原始封装，可能一个设备的信息在两条信息中
                Map<String, String> signalGuidMap = null;

                if (!"Yx".equals(typeName)) {
                    //遥测表遥信数据如果有变位增加存数据库的操作
                    List<DataYx> yc4yxDataList = new ArrayList<DataYx>();
                    for (Element detailType : detailTypes) {

                        //"0"表示数据采集正常
                        if ("0".equals(detailType.attributeValue("q"))) {
                            String pId = detailType.attributeValue("pId");
                            //根据电站id,数采地址，采集器id,pid去确定设备id，确定pid对应参数名全局id，参数名去对应数据库字段
                            String deviceId = CacheMappingUtil.pid2devIdYc(collector_id + "_YC", pId);
                            List<String> signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", pId);

                            //缓存中没有查到设备id,signalGuid,不存redis
                            if (deviceId == null || signalGuidList == null) {
                                continue;
                            }
                            //TODO 增加dataMode获取
                            Integer dataMode = CacheMappingUtil.devId2dataMode(Integer.valueOf(deviceId));
                            if (dataMode == null) {
                                continue;
                            }

                            if (devIdTemp == null) {
                                devIdTemp = deviceId;
                                //signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>():JedisUtil.getMap(deviceId);
                                //和redis没关系，换设备直接new
                                signalGuidMap = new HashMap<String, String>();
                            } else if (!deviceId.equals(devIdTemp)) {
                                //先更新一次上一个设备状态
                                signalGuidMap.put("data_time", data_time);
                                signalGuidMap.put("sys_time", sys_time_str);
                                //这里不加连接状态，历史数据

                                //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                                Set<String> keySet = signalGuidMap.keySet();
                                boolean singleRegisterFlag = false;
                                for (String str : keySet) {
                                    boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                                    if (isSingleRegister) {
                                        singleRegisterFlag = true;
                                        break;
                                    }
                                }
                                //有单寄存器就存库
                                if (singleRegisterFlag) {
                                    //获取MapperName
                                    String modelName = CacheMappingUtil.signalGuid2model(signalGuidMap);
                                    if (modelName == null) {
                                        continue;
                                    }
                                    //modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
                                    String mapperName = Table2modelUtil.lowerCaseFirstLatter(modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";
                                    //补采数据入库
                                    storeDataOfRecovery(dataTime, signalGuidMap, devIdTemp, data_time, mapperName);

                                    //JedisUtil.setMap(devIdTemp, signalGuidMap);
                                    logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                                }
                                devIdTemp = deviceId;

                                //signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>():JedisUtil.getMap(deviceId);
                                //和redis没关系，换设备直接new
                                signalGuidMap = new HashMap<String, String>();
                            }

                            String signalValue = detailType.getTextTrim();

                            //TODO 高低位信号判断处理
                            //以高位为准，获取第一个guid
                            String guidFirst = signalGuidList.get(0);
                            String signalGuidL = CacheMappingUtil.getSignalGuidL(dataMode, guidFirst);
                            String signalGuidH = CacheMappingUtil.getSignalGuidH(dataMode, guidFirst);
                            if (StringUtils.isNotBlank(signalGuidL)) {
                                //查询低位地址值
                                String guidLVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                                //不存在数据时间低位地址值，就在redis中存储高位值
                                if (guidLVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(signalValue) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(guidLVal)) : String.valueOf(sigDoubleVal - Double.valueOf(guidLVal));
                                //删除redis中低位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                            } else if (StringUtils.isNotBlank(signalGuidH)) {
                                //查询高位地址的值
                                String guidHVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidH + "-" + data_time);

                                //不存在数据时间高位地址值，就在redis中 存储低位值
                                if (guidHVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(guidHVal) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(signalValue)) : String.valueOf(sigDoubleVal - Double.valueOf(signalValue));
                                //删除redis中高位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidH + "-" + data_time);
                                //TODO 以高位为准，替换guidList
                                signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", String.valueOf(Integer.parseInt(pId) - 1));
                            }

                            //寄存器值拆分后的
                            String detailSignalVal = null;
                            //原始值日志Map
                            logMap.put("YC_" + pId, signalValue);
                            //遍历这个信号下所有子信号
                            for (String signalGuid : signalGuidList) {
                                //特殊处理(遥测表遥信等)，增益修正判断，保留小数，值有效判断，封装Map等
                                ycSignalValueProcess(signalGuid, signalValue, detailSignalVal, signalGuidMap, deviceId, yc4yxDataList, data_time);
                            }
                        }
                    }

                    //遥测表遥信，变位遥信列表数据存库操作
                    if (yc4yxDataList.size() > 0) {
                        dataSaveService.yxDataSave(yc4yxDataList);
                    }

                } else {
                    //遥信数据增加存数据库的操作,补采的遥信，只存库变位的遥信
                    List<DataYx> yxDataList = new ArrayList<DataYx>();
                    for (Element detailType : detailTypes) {
                        //"0"表示数据采集正常
                        if ("0".equals(detailType.attributeValue("q"))) {
                            String pId = detailType.attributeValue("pId");
                            //根据电站id,数采地址，采集器id,pid去确定设备id，确定pid对应参数名id，参数名去对应数据库字段

                            String deviceId = CacheMappingUtil.pid2devIdYx(collector_id + "_YX", pId);
                            List<String> signalGuidList = CacheMappingUtil.pid2signalGuidYx(collector_id + "_YX", pId);

                            //缓存中没有查到设备id,signalGuid,不存redis
                            if (deviceId == null || signalGuidList == null) {
                                continue;
                            }
                            //TODO 增加dataMode获取
                            Integer dataMode = CacheMappingUtil.devId2dataMode(Integer.valueOf(deviceId));
                            if (dataMode == null) {
                                continue;
                            }

                            //畅洋遥信一般就一位，只有一个子信号
                            String signalGuid = signalGuidList.get(0);

                            //缓存中没有查到设备id,field,不存redis，也不存数据库，关联表查到yx_id存库
                            Integer yxId = CacheMappingUtil.signalGuid2yxId(signalGuid, detailType.getTextTrim());

                            if (devIdTemp == null) {
                                devIdTemp = deviceId;
                                //signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>():JedisUtil.getMap(deviceId);
                                //和redis没关系，换设备直接new
                                signalGuidMap = new HashMap<String, String>();
                            } else if (!deviceId.equals(devIdTemp)) {
                                //先更新一次上一个设备状态(这里mapping可能有遥测和遥信)
                                //每个设备加一个数据时间给去前端判断通讯状况
                                signalGuidMap.put("data_time", data_time);
                                signalGuidMap.put("sys_time", sys_time_str);
                                //这里不加连接状态，历史数据

                                //补采的遥信，只再数据库存变位的，总召的不存
                                //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                                Set<String> keySet = signalGuidMap.keySet();
                                boolean singleRegisterFlag = false;
                                for (String str : keySet) {
                                    boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                                    if (isSingleRegister) {
                                        singleRegisterFlag = true;
                                        break;
                                    }
                                }
                                //有单寄存器就存,只记录日志，不存库
                                if (singleRegisterFlag) {

                                    //JedisUtil.setMap(devIdTemp, signalGuidMap);
                                    logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                                }

                                devIdTemp = deviceId;
                                //signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>():JedisUtil.getMap(deviceId);
                                //和redis没关系，换设备直接new
                                signalGuidMap = new HashMap<String, String>();
                            }

                            String signalValue = detailType.getTextTrim();
                            //原始值日志Map
                            logMap.put("YX_" + pId, signalValue);

                            //TODO 高低位信号判断处理
                            //以高位为准，获取第一个guid
                            String guidFirst = signalGuidList.get(0);
                            String signalGuidL = CacheMappingUtil.getSignalGuidL(dataMode, guidFirst);
                            String signalGuidH = CacheMappingUtil.getSignalGuidH(dataMode, guidFirst);
                            if (StringUtils.isNotBlank(signalGuidL)) {
                                //查询低位地址值
                                String guidLVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                                //不存在数据时间低位地址值，就在redis中存储高位值
                                if (guidLVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(signalValue) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(guidLVal)) : String.valueOf(sigDoubleVal - Double.valueOf(guidLVal));
                                //删除redis中低位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                            } else if (StringUtils.isNotBlank(signalGuidH)) {
                                //查询高位地址的值
                                String guidHVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidH + "-" + data_time);

                                //不存在数据时间高位地址值，就在redis中 存储低位值
                                if (guidHVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(guidHVal) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(signalValue)) : String.valueOf(sigDoubleVal - Double.valueOf(signalValue));
                                //删除redis中高位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidH + "-" + data_time);
                                //TODO 以高位为准，替换guidList
                                signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", String.valueOf(Integer.parseInt(pId) - 1));
                            }


                            //增益和修正计算，然后值范围判断，正常就存库
                            double signalVal = DataProcessingUtil.gainFactor(detailType.getTextTrim(), signalGuid);
                            //处于正常值范围就存redis
                            if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
                                signalGuidMap.put(signalGuid, String.valueOf((int) signalVal));
                                // 如果遥信详情映射表映射到值，同时是变位数据，就添加到列表存库
                                if (yxId != null && ("T".equals(detailType.attributeValue("dchg")) || "T".equals(detailType.attributeValue("d")))) {
                                    //yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time),String.valueOf((int)signalVal)));
                                    String statusValue4compare = String.valueOf((int) signalVal);
                                    yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time), statusValue4compare, statusValue4compare, "0"));
                                }
                            }
                        }
                    }
                    //变位遥信列表数据存库操作
                    //每个设备加一个数据时间给去前端判断通讯状况
                    if (yxDataList.size() > 0) {
                        dataSaveService.yxDataSave(yxDataList);
                    }
                }

                //将一个采集的数据最后剩余的一个设备数据部分提交(包含遥测和遥信一起，也可能只有遥测数据)
                if (signalGuidMap != null) {
                    signalGuidMap.put("data_time", data_time);
                    signalGuidMap.put("sys_time", sys_time_str);
                    //这里不加连接状态，历史数据

                    //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                    Set<String> keySet = signalGuidMap.keySet();
                    boolean singleRegisterFlag = false;
                    for (String str : keySet) {
                        boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                        if (isSingleRegister) {
                            singleRegisterFlag = true;
                            break;
                        }
                    }
                    //有单寄存器就存数据库
                    if (singleRegisterFlag) {
                        // 获取MapperName
                        String modelName = CacheMappingUtil.signalGuid2model(signalGuidMap);
                        if (modelName == null) {
                            continue;
                        }
                        //modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
                        String mapperName = Table2modelUtil.lowerCaseFirstLatter(modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";
                        //补采数据入库
                        storeDataOfRecovery(dataTime, signalGuidMap, devIdTemp, data_time, mapperName);

                        //JedisUtil.setMap(devIdTemp, signalGuidMap);
                        logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                    }

                }
            }

        } else {
            //*****************************(正常解析)*************************************
            //不存在该数采或者报文数据比redis数据新，直接解析
            logger.info("start to parse a real-time message");
            // 数据采集器设备数据时间
            //timeMap.put("data_time", data_time);
            //将时间调整为5min的整数
            data_time = TimeUtil.getTo5Int(Long.parseLong(data_time)) + "";
            timeMap.put("data_time", String.valueOf(TimeUtil.getTo5Int(dataTime)));
            //当前报文接收时间
            Long sys_time = TimeUtil4Xml.getMsec();
            String sys_time_str = String.valueOf(sys_time);
            timeMap.put("sys_time", sys_time_str);
            timeMap.put("conn_status", "1");
            logMap.put("sys_time", sys_time_str);
            //更新数据采集器时间
            JedisUtil.setMap(dataColl_id, timeMap);
            logger.info(dataColl_id + "-" + timeMap.toString());

            //保留六位小数存库，光伏计算使用四位，前台显示两位
            //DecimalFormat decimalFormat = new DecimalFormat("#.######");

            @SuppressWarnings("unchecked")
            List<Element> elements = DataCollector.elements("type");
            if (elements == null) {
                return;
            }
            for (Element type : elements) {
                String typeName = type.attributeValue("Type");
                @SuppressWarnings("unchecked")
                List<Element> detailTypes = type.elements(typeName.toLowerCase());
                if (detailTypes == null) {
                    continue;
                }
                //设备id temp;
                String devIdTemp = null;
                Map<String, String> signalGuidMap = null;


                if (!"Yx".equals(typeName)) {

                    //遥测表遥信数据如果有变位增加存数据库的操作
                    List<DataYx> yc4yxDataList = new ArrayList<>();
                    for (Element detailType : detailTypes) {

                        //"0"表示数据采集正常
                        if ("0".equals(detailType.attributeValue("q"))) {
                            String pId = detailType.attributeValue("pId");
                            //根据电站id,数采地址，采集器id,pid去确定设备id，确定pid对应参数名全局id，参数名去对应数据库字段
                            String deviceId = CacheMappingUtil.pid2devIdYc(collector_id + "_YC", pId);
                            List<String> signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", pId);

                            //缓存中没有查到设备id,signalGuid,不存redis
                            if (deviceId == null || signalGuidList == null) {
                                continue;
                            }

                            //TODO 增加dataMode获取
                            Integer dataMode = CacheMappingUtil.devId2dataMode(Integer.valueOf(deviceId));
                            if (dataMode == null) {
                                continue;
                            }

                            if (devIdTemp == null) {
                                devIdTemp = deviceId;
                                signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<>() : JedisUtil.getMap(deviceId);
                            } else if (!deviceId.equals(devIdTemp)) {
                                //先更新一次上一个设备状态
                                signalGuidMap.put("data_time", data_time);
                                signalGuidMap.put("sys_time", sys_time_str);
                                signalGuidMap.put("conn_status", "1");

                                //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                                Set<String> keySet = signalGuidMap.keySet();
                                boolean singleRegisterFlag = false;
                                for (String str : keySet) {
                                    boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                                    if (isSingleRegister) {
                                        singleRegisterFlag = true;
                                        break;
                                    }
                                }
                                //有单寄存器就存redis
                                if (singleRegisterFlag) {
                                    JedisUtil.setMap(devIdTemp, signalGuidMap);
                                    logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                                }
                                devIdTemp = deviceId;

                                signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>() : JedisUtil.getMap(deviceId);
                            }

                            String signalValue = detailType.getTextTrim();

                            //TODO 高低位信号判断处理
                            //以高位为准，获取第一个guid
                            String guidFirst = signalGuidList.get(0);
                            String signalGuidL = CacheMappingUtil.getSignalGuidL(dataMode, guidFirst);
                            String signalGuidH = CacheMappingUtil.getSignalGuidH(dataMode, guidFirst);
                            if (StringUtils.isNotBlank(signalGuidL)) {
                                //查询低位地址值
                                String guidLVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                                //不存在数据时间低位地址值，就在redis中存储高位值
                                if (guidLVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(signalValue) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(guidLVal)) : String.valueOf(sigDoubleVal - Double.valueOf(guidLVal));
                                //删除redis中低位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                            } else if (StringUtils.isNotBlank(signalGuidH)) {
                                //查询高位地址的值
                                String guidHVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidH + "-" + data_time);

                                //不存在数据时间高位地址值，就在redis中 存储低位值
                                if (guidHVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(guidHVal) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(signalValue)) : String.valueOf(sigDoubleVal - Double.valueOf(signalValue));
                                //删除redis中高位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidH + "-" + data_time);

                                //TODO 以高位为准，替换guidList
                                signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", String.valueOf(Integer.parseInt(pId) - 1));
                            }

                            //寄存器值拆分后的
                            String detailSignalVal = null;
                            //原始值日志Map
                            logMap.put("YC_" + pId, signalValue);
                            //遍历这个信号下所有子信号
                            for (String signalGuid : signalGuidList) {
                                //特殊处理(遥测表遥信等)，增益修正判断，保留小数，值有效判断，封装Map等
                                ycSignalValueProcess(signalGuid, signalValue, detailSignalVal, signalGuidMap, deviceId, yc4yxDataList, data_time);
                            }
                        }
                    }
                    //遥测表遥信，变位遥信列表数据存库操作
                    if (yc4yxDataList.size() > 0) {
                        dataSaveService.yxDataSave(yc4yxDataList);
                    }

                } else {
                    //遥信数据增加存数据库的操作
                    List<DataYx> yxDataList = new ArrayList<>();
                    for (Element detailType : detailTypes) {
                        //"0"表示数据采集正常
                        if ("0".equals(detailType.attributeValue("q"))) {
                            String pId = detailType.attributeValue("pId");
                            //根据电站id,数采地址，采集器id,pid去确定设备id，确定pid对应参数名id，参数名去对应数据库字段

                            String deviceId = CacheMappingUtil.pid2devIdYx(collector_id + "_YX", pId);
                            List<String> signalGuidList = CacheMappingUtil.pid2signalGuidYx(collector_id + "_YX", pId);

                            //缓存中没有查到设备id,signalGuid,不存redis
                            if (deviceId == null || signalGuidList == null) {
                                continue;
                            }
                            //TODO 增加dataMode获取
                            Integer dataMode = CacheMappingUtil.devId2dataMode(Integer.valueOf(deviceId));
                            if (dataMode == null) {
                                continue;
                            }

                            //一般来说(不谈合成信号)畅洋数采遥信就只有一位，只能表示一个信号;后续有其他再加判断和排序
                            String signalGuid = signalGuidList.get(0);

                            //缓存中没有查到设备id,field,不存redis，也不存数据库，关联表查到yx_id存库
                            Integer yxId = CacheMappingUtil.signalGuid2yxId(signalGuid, detailType.getTextTrim());

                            if (devIdTemp == null) {
                                devIdTemp = deviceId;
                                signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>() : JedisUtil.getMap(deviceId);
                            } else if (!deviceId.equals(devIdTemp)) {
                                //先更新一次上一个设备状态(这里mapping可能有遥测和遥信)
                                //每个设备加一个数据时间给去前端判断通讯状况
                                signalGuidMap.put("data_time", data_time);
                                signalGuidMap.put("sys_time", sys_time_str);
                                signalGuidMap.put("conn_status", "1");

                                //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                                Set<String> keySet = signalGuidMap.keySet();
                                boolean singleRegisterFlag = false;
                                for (String str : keySet) {
                                    boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                                    if (isSingleRegister) {
                                        singleRegisterFlag = true;
                                        break;
                                    }
                                }
                                //有单寄存器就存redis
                                if (singleRegisterFlag) {
                                    JedisUtil.setMap(devIdTemp, signalGuidMap);
                                    logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                                }
                                devIdTemp = deviceId;
                                signalGuidMap = JedisUtil.getMap(deviceId) == null ? new HashMap<String, String>() : JedisUtil.getMap(deviceId);
                            }

                            String signalValue = detailType.getTextTrim();
                            //原始值日志Map
                            logMap.put("YX_" + pId, signalValue);

                            //TODO 高低位信号判断处理
                            //以高位为准，获取第一个guid
                            String guidFirst = signalGuidList.get(0);
                            String signalGuidL = CacheMappingUtil.getSignalGuidL(dataMode, guidFirst);
                            String signalGuidH = CacheMappingUtil.getSignalGuidH(dataMode, guidFirst);
                            if (StringUtils.isNotBlank(signalGuidL)) {
                                //查询低位地址值
                                String guidLVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                                //不存在数据时间低位地址值，就在redis中存储高位值
                                if (guidLVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(signalValue) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(guidLVal)) : String.valueOf(sigDoubleVal - Double.valueOf(guidLVal));
                                //删除redis中低位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidL + "-" + data_time);
                            } else if (StringUtils.isNotBlank(signalGuidH)) {
                                //查询高位地址的值
                                String guidHVal = JedisUtil4signal.getStringInMap(devIdTemp, signalGuidH + "-" + data_time);

                                //不存在数据时间高位地址值，就在redis中 存储低位值
                                if (guidHVal == null) {
                                    JedisUtil4signal.setStringInMap(devIdTemp, guidFirst + "-" + data_time, signalValue);
                                    continue;
                                }
                                //合成信号值
                                double sigDoubleVal = Double.valueOf(guidHVal) * 65536;
                                signalValue = sigDoubleVal >= 0 ? String.valueOf(sigDoubleVal + Double.valueOf(signalValue)) : String.valueOf(sigDoubleVal - Double.valueOf(signalValue));
                                //删除redis中高位信息
                                JedisUtil4signal.delStringInMap(devIdTemp, signalGuidH + "-" + data_time);
                                //TODO 以高位为准，替换guidList
                                signalGuidList = CacheMappingUtil.pid2signalGuidYc(collector_id + "_YC", String.valueOf(Integer.parseInt(pId) - 1));
                            }

                            //增益和修正计算，然后值范围判断，正常就存库
                            double signalVal = DataProcessingUtil.gainFactor(detailType.getTextTrim(), signalGuid);


                            //处于正常值范围就存redis
                            if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
                                signalGuidMap.put(signalGuid, String.valueOf((int) signalVal));
                                // 如果遥信详情映射表映射到值，同时是变位数据，就添加到列表存库
                                if (yxId != null && ("T".equals(detailType.attributeValue("dchg")) || "T".equals(detailType.attributeValue("d")))) {
                                    //yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time),String.valueOf((int)signalVal)));
                                    String statusValue4compare = String.valueOf((int) signalVal);
                                    yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time), statusValue4compare, statusValue4compare, "0"));

                                    //TODO 增加电站状态，0表示正常，1表示异常和故障(针对单个遥信，只有老版本实时的才有)
                                    if (CacheMappingUtil.yxIdAlarmContain(yxId)) {
                                        Integer plantId = CacheMappingUtil.devId2plantId(Integer.parseInt(deviceId));
                                        if (plantId != null) {
                                            JedisUtil4plantStatus.setString(plantId.toString(), "1");
                                        }
                                    }
                                }

                            }
                        }

                    }
                    //变位遥信列表数据存库操作
                    //每个设备加一个数据时间给去前端判断通讯状况
                    if (yxDataList.size() > 0) {
                        dataSaveService.yxDataSave(yxDataList);
                    }
                }

                //将一个采集的数据最后剩余的一个设备数据部分提交(包含遥测和遥信一起，也可能只有遥测数据)
                if (signalGuidMap != null) {
                    signalGuidMap.put("data_time", data_time);
                    signalGuidMap.put("sys_time", sys_time_str);
                    signalGuidMap.put("conn_status", "1");

                    //加入单寄存器判断，如果一个单寄存器信号点都没有，则是数采计算式信号，设备已经掉线,不更新
                    Set<String> keySet = signalGuidMap.keySet();
                    boolean singleRegisterFlag = false;
                    for (String str : keySet) {
                        boolean isSingleRegister = CacheMappingUtil.signalGuid2singleRegister(str);
                        if (isSingleRegister) {
                            singleRegisterFlag = true;
                            break;
                        }
                    }
                    //有单寄存器就存redis
                    if (singleRegisterFlag) {
                        JedisUtil.setMap(devIdTemp, signalGuidMap);
                        logger.debug(devIdTemp + ":" + signalGuidMap.toString());
                    }

                }
            }
        }
        //该条报文简化日志
        logger.info("the message has ended parsing");
        logger.info(collector_id + "-" + time.getText() + "-" + logMap.toString());

    }


    /**
     * @param xmlStr
     * @throws IOException: void
     * @Title: xmlResultStr2Map
     * @Description: 遥控遥调返回结果解析，封装成Map<deviceId, List<Map<signalGuid, value>>>序列化到rabbitMq队列中，
     * 第一个是数采设备id,map中封装sessionId
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月6日上午9:34:45
     */
    public static void xmlResultStr2Map(String xmlStr) throws IOException {

        logger.debug("start to parse the ykYtResult message");
        //用于日志记录的简化map,Map<YK_0,result>,//原始值日志Map
        Map<String, String> logMap = new HashMap<String, String>();

        //HashMap<String, Object>，第一个key是sessionId,第二个key是data_time，第三个 key是value,值是List<Map<String, String>>
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            logger.error("xmlResultStr2Map get an exception:{}", e);
        }
        Element mqttData = doc.getRootElement();
        Element session = mqttData.element("Session");
        String sessionId = session.attributeValue("id");

        // 根据session和pid去查询数据库对应记录，更改操作结果状态(光伏系统操作)

        Element dataCollector = session.element("DataCollector");
        String collector_id = dataCollector.attributeValue("id");

        // 根据采集节点id找到对应数采设备id
        Integer dataCollId = CacheMappingUtil.collectorId2devId(collector_id);
        if (dataCollId == null) {
            logger.warn("can not find the relevant dataCollector:{} for the ykYtResultParse", collector_id);
            return;
        }

        Element time = dataCollector.element("time");
        String xmlTimeStr = time.getText();
        // 根据需要是否转化成毫秒字符串
        xmlTimeStr = TimeUtil4Xml.xmlTime2Str(xmlTimeStr);
        String data_time = xmlTimeStr;
        // 数据采集器设备数据时间
        resultMap.put("data_time", data_time);
        resultMap.put("sessionId", sessionId);
        logMap.put("sessionId", sessionId);
        logMap.put("sys_time", String.valueOf(new Date().getTime()));

        @SuppressWarnings("unchecked")
        List<Element> elements = dataCollector.elements("type");
        if (elements == null) {
            return;
        }
        for (Element type : elements) {
            String typeName = type.attributeValue("Type");
            @SuppressWarnings("unchecked")
            List<Element> detailTypes = type.elements(typeName.toLowerCase());
            if (detailTypes == null) {
                continue;
            }

            // 设备id temp;
            @SuppressWarnings("unused")
            String devIdTemp = null;
            // 字段结果映射
            Map<String, String> signalGuidMap = null;

            for (Element detailType : detailTypes) {
                // 返回结果没有数据品质q,但是结果有failed1，failed2,failed3
                // <yk pId="0" val="1"
                // ExecTime="2016-10-25T16:39:39Z">success</yk>

                //转化成毫秒字符串
                String execTime = TimeUtil4Xml.xmlTime2Str(detailType.attributeValue("ExecTime"));
                //String execTime = detailType.attributeValue("ExecTime");
                String execResult = detailType.getTextTrim();

                String pId = detailType.attributeValue("pId");

                // 根据电站id,数采地址，采集器id,pid去确定设备id，确定pid对应参数id，采集类型
                String deviceId = null;
                List<String> signalGuidList = null;
                if ("yk".equals(typeName.toLowerCase())) {
                    deviceId = CacheMappingUtil.pid2devIdYk(collector_id + "_YK", pId);
                    signalGuidList = CacheMappingUtil.pid2signalGuidYk(collector_id + "_YK", pId);
                } else {
                    deviceId = CacheMappingUtil.pid2devIdYt(collector_id + "_YT", pId);
                    signalGuidList = CacheMappingUtil.pid2signalGuidYt(collector_id + "_YT", pId);
                }

                // 缓存中没有查到设备id,signalGuidList,不存
                if (deviceId == null || signalGuidList == null) {
                    continue;
                }

                //一般情况下对遥控遥调而言，signalGuidList中就一个signalGuid，多个的遇见再考虑加判断和list中顺序
                String signalGuid = signalGuidList.get(0);

                String valTemp = detailType.attributeValue("val");

                //增益和修正计算，将遥控遥调期望目标值返回
                double signalVal = DataProcessingUtil.gainFactor(valTemp, signalGuid);
                //保留六位小数存库，光伏计算使用四位，前台显示两位
                //DecimalFormat decimalFormat = new DecimalFormat("#.######");
                String val = decimalFormat.format(signalVal);
                //原始信息日志
                logMap.put(typeName.toUpperCase() + "_" + pId, val + "_" + execResult);


                signalGuidMap = new HashMap<String, String>();
                signalGuidMap.put("pId", pId);
                signalGuidMap.put("signalGuid", signalGuid);
                signalGuidMap.put("sessionId", sessionId);
                signalGuidMap.put("operation_type", typeName.toLowerCase());
                signalGuidMap.put("targetVal", val);
                signalGuidMap.put("execTime", execTime);
                signalGuidMap.put("execResult", execResult);
                //只返回控制成功的，失败的超时去判断
                if ("success".equals(execResult)) {
                    resultList.add(signalGuidMap);
                }
                // resultList.add(signalGuidMap);
            }
        }

        if (resultList.size() > 0) {
            // 将xml中最后一个设备状态存到map中
            resultMap.put("value", resultList);

            // mybatis批量插入操作的记录(光伏系统实现)

            // Rabbit将封装好的操作结果，返回给系统，jackson或commons-lang序列化map(先用json-smart序列化)
            // YkYtResultProducer.getInstance().sendMessage(resultMap);
            logger.debug("resultMap：{}", resultMap.toString());
            logger.debug("the message of ykYtResult has ended parsing");

            RabbitMqProducer.sendMessage(resultMap);
        }

        logger.info(collector_id + "-" + time.getText() + "-" + logMap.toString());
    }

	/*<?xml version="1.0" encoding="UTF-8"?>
	<mqttData>
	  <Session id="10">
	  <DataCollector id="ncyt02_dc">
	  	<time>2016-11-22T03:39:30Z</time>
	    <type Type="Yk">
	      <yk pId="0">1</yk>
	    </type>
	    <type Type="Yt">
	      <yt pId="0">30.00</yt>
	    </type>
	  </DataCollector>
	  </Session>
	</mqttData>*/

    /**
     * @param sessionId
     * @param collector_id
     * @param ykYtMap
     * @Title: map2XmlStr
     * @Description: 封装下发的指令，封装过来的格式 Map<deviceId, Map<field, value>>
     * @return: String
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月6日上午11:09:44
     */
    public static String map2XmlStr(String sessionId, String collector_id, Map<String, Map<String, String>> ykYtMap) {

        Document doc = DocumentHelper.createDocument();

        Element mqttData = doc.addElement("mqttData");
        Element session = mqttData.addElement("Session");
        session.addAttribute("id", sessionId);

        Element dataCollector = session.addElement("DataCollector");
        dataCollector.addAttribute("id", collector_id);

        dataCollector.addElement("time").setText(TimeUtil4Xml.getXmlTimeStr());

        Element ykType = dataCollector.addElement("type");
        ykType.addAttribute("Type", "Yk");

        Element ytType = dataCollector.addElement("type");
        ytType.addAttribute("Type", "Yt");

        Set<Entry<String, Map<String, String>>> ykYtEntrySet = ykYtMap.entrySet();
        for (Entry<String, Map<String, String>> ykYtEntry : ykYtEntrySet) {
            Integer deviceId = Integer.parseInt(ykYtEntry.getKey());
            Map<String, String> signalGuidMap = ykYtEntry.getValue();

            Set<Entry<String, String>> signalGuidEntrySet = signalGuidMap.entrySet();
            for (Entry<String, String> signalGuidEntry : signalGuidEntrySet) {
                String signalGuid = signalGuidEntry.getKey();
                String value = signalGuidEntry.getValue();

                //根据电站id,采集器设备id，采集节点name(id)，设备id,设备参数,找到pid(设备唯一id和参数确定pid,其他参数都可以获取,还需要参数对应采集类型，才能封装)
                Integer pId = CacheMappingUtil.devId2guidPid(deviceId, signalGuid);
                String detailType = CacheMappingUtil.devId2guidCtrlType(deviceId, signalGuid);

                if (pId == null || detailType == null) {
                    continue;
                }

                if ("yk".equalsIgnoreCase(detailType)) {

                    //处于正常值范围就反向计算增益然后封装下发
                    if (DataProcessingUtil.valLegitimation(signalGuid, Double.parseDouble(value))) {

                        //反向增益和修正计算
                        String valueTemp = DataProcessingUtil.gainFactorReverse(value, signalGuid);
                        double valueTarget = Double.valueOf(valueTemp);
                        //封装
                        ykType.addElement("yk").addAttribute("pId", String.valueOf(pId)).setText(String.valueOf((int) valueTarget));

                    }
                } else {
                    //特殊处理(V0不做)：①遥调一个信号是多个寄存器(找到映射的多个寄存器PID,分开过分别下发)②一个寄存器中间几位表示遥调(数据库ykyt扩展表加控制位长度)

                    //处于正常值范围就反向计算增益然后封装下发
                    if (DataProcessingUtil.valLegitimation(signalGuid, Double.parseDouble(value))) {
                        //反向增益和修正计算
                        String valueTemp = DataProcessingUtil.gainFactorReverse(value, signalGuid);
                        double valueTarget = Double.valueOf(valueTemp);
                        //封装,下发给寄存器的也是int
                        ytType.addElement("yt").addAttribute("pId", String.valueOf(pId)).setText(String.valueOf((int) valueTarget));
                    }

                }
            }
        }

        //封装完毕，检查是否遥控或遥调子节点存在与否，不存在就删除(xPath语法)
        if (ykType.selectNodes("//DataCollector/type[1]/yk").size() == 0) {
            dataCollector.remove(ykType);
        }
        if (ytType.selectNodes("//DataCollector/type[2]/yt").size() == 0) {
            dataCollector.remove(ytType);
        }

        //转化成xml字符串
        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");

        try {
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            xmlWriter.write(doc);

        } catch (IOException e) {
            logger.error("xmlWriter get an exception:{}", e);
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                logger.error("get an exception of closing StringWriter:{}", e);
            }
        }

        logger.debug("map2XmlStr parses successfully:{}", sw.toString());
        return sw.toString();
    }

/*	<mqttData>
	  <DataCollector id="ncyt02_dc">
	    <type Type="SetTime">

	    </type>
	  </DataCollector>
	</mqttData>*/

    /**
     * @param collector_id
     * @Title: setTimeXml
     * @Description: 封装与采集节点对时指令
     * @return: String
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月6日上午11:34:34
     */
    public static String setTimeXml(String collector_id) {
        Document doc = DocumentHelper.createDocument();

        Element mqttData = doc.addElement("mqttData");
        Element dataCollector = mqttData.addElement("DataCollector");
        dataCollector.addAttribute("id", collector_id);

        Element type = dataCollector.addElement("type");
        type.addAttribute("Type", "SetTime");
        Calendar cal = Calendar.getInstance();

        type.addElement("ST").addAttribute("Time", "Year").setText(String.valueOf(cal.get(Calendar.YEAR)));
        type.addElement("ST").addAttribute("Time", "Mon").setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
        type.addElement("ST").addAttribute("Time", "Day").setText(String.valueOf(cal.get(Calendar.DATE)));
        type.addElement("ST").addAttribute("Time", "Hour").setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        type.addElement("ST").addAttribute("Time", "Min").setText(String.valueOf(cal.get(Calendar.MINUTE)));
        type.addElement("ST").addAttribute("Time", "Sec").setText(String.valueOf(cal.get(Calendar.SECOND)));
        type.addElement("ST").addAttribute("Time", "MilliSec").setText(String.valueOf(cal.get(Calendar.MILLISECOND)));

        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");

        try {
            XMLWriter xmlWriter = new XMLWriter(sw, format);
            xmlWriter.write(doc);
        } catch (IOException e) {
            logger.error("xmlWriter get an exception:{}", e);
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                logger.error("get an exception of closing StringWriter:{}", e);
            }
        }

        logger.info("setTimeXml parses successfully:{}", sw.toString());
        return sw.toString();
    }


    /**
     * @param mapperName
     * @param signalGuidMap
     * @param dataStoreRecords: void
     * @Title: force2obj
     * @Description: 强转类型，符合要求就分类存库
     * @lastEditor: SP0010
     * @lastEdit: 2017年12月27日下午2:10:08
     */
    @SuppressWarnings("unchecked")
    public static void force2obj(String mapperName, String devIdTemp, Map<String, String> signalGuidMap, Object dataStoreRecords, boolean fixedTimeFlag, String data_time) {

        //不是固定入库时刻就替换成固定入库时刻
        if (!fixedTimeFlag) {
            signalGuidMap.put("data_time", data_time);
        }

        if ("dataElectricMeterMapper".equals(mapperName)) {
            List<DataElectricMeter> dataStoreList = (List<DataElectricMeter>) dataStoreRecords;
            //非空判断
            if (!CollectionUtils.isEmpty(dataStoreList)) {
                //获取匹配的数据中最新的记录
                DataElectricMeter dataElectricMeter = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新
                if (!StringUtils.isNoneBlank(dataElectricMeter.getEmActivePower().toString())) {
                    //更新
                    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                    logger.debug("success to update history message");
                }
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.debug("success to store history message");
            }
        } else if ("dataCentralInverterMapper".equals(mapperName)) {
            List<DataCentralInverter> dataStoreList = (List<DataCentralInverter>) dataStoreRecords;
            //非空判断
            //if (dataStoreList != null) {
            if (!CollectionUtils.isEmpty(dataStoreList)) {
                //获取匹配的数据中最新的记录
                DataCentralInverter dataCentralInverter = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新，对逆变器，有功功率，日发电量和总发电量是关键数据
                if (!StringUtils.isNoneBlank(dataCentralInverter.getActivePower().toString(), dataCentralInverter.getDailyEnergy().toString(), dataCentralInverter.getTotalEnergy().toString())) {
                    //更新
                    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                    logger.debug("success to update history message");
                }
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.debug("success to store history message");
            }
        } else if ("dataStringInverterMapper".equals(mapperName)) {
            List<DataStringInverter> dataStoreList = (List<DataStringInverter>) dataStoreRecords;
            //非空判断
            if (!CollectionUtils.isEmpty(dataStoreList)) {
                //获取匹配的数据中最新的记录
                DataStringInverter dataStringInverter = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新，对逆变器，有功功率，日发电量和总发电量是关键数据
                if (!StringUtils.isNoneBlank(dataStringInverter.getActivePower().toString(), dataStringInverter.getDailyEnergy().toString(), dataStringInverter.getTotalEnergy().toString())) {
                    //更新
                    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                    logger.info("success to update history message");
                }
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.info("success to store history message");
            }

        } else if ("dataTransformerMapper".equals(mapperName)) {
            List<DataTransformer> dataStoreList = (List<DataTransformer>) dataStoreRecords;
            //非空判断
            if (dataStoreList != null) {
                //获取匹配的数据中最新的记录
                DataTransformer dataTransformer = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新
                if (!StringUtils.isNoneBlank(dataTransformer.getActivePower().toString())) {
                    //更新
                    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                    logger.debug("success to update history message");
                }
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.debug("success to store history message");
            }
        } else if ("dataCombinerBoxMapper".equals(mapperName)) {
            List<DataCombinerBox> dataStoreList = (List<DataCombinerBox>) dataStoreRecords;
            //非空判断
            //if (dataStoreList != null) {
            if (!CollectionUtils.isEmpty(dataStoreList)) {
                //获取匹配的数据中最新的记录
                DataCombinerBox dataCombinerBox = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新
                //if (!StringUtils.isNoneBlank(dataCombinerBox.getDailyEnergy().toString())) {
                //修改其空指针异常
                if (!StringUtils.isNoneBlank(String.valueOf(dataCombinerBox.getDailyEnergy()))) {
                    //更新
                    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                    logger.debug("success to update history message");
                }
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.debug("success to store history message");
            }
        } else if ("dataEnvMonitorMapper".equals(mapperName)) {
            List<DataEnvMonitor> dataStoreList = (List<DataEnvMonitor>) dataStoreRecords;
            //非空判断
            //if (dataStoreList != null) {
            if (!CollectionUtils.isEmpty(dataStoreList)) {
                //获取匹配的数据中最新的记录
                //DataEnvMonitor dataEnvMonitor = dataStoreList.get(0);
                //关键数据(指标可能会变)为空就更新
                //if (!StringUtils.isNoneBlank(dataCombinerBox.getDailyEnergy().toString())) {
                // 修改其空指针异常
                //TODO 暂未确定关键数据
                //if (!StringUtils.isNoneBlank(String.valueOf(dataEnvMonitor.getDailyEnergy()))) {
                //    //更新
                //    DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
                //    logger.debug("success to update history message");
                //}
            } else {
                //没查询到匹配设备id和 data_time的记录，直接入库
                DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
                logger.debug("success to store history message");
            }
        }
        //TODO 新增设备，如PCS,BMS,箱式变压器数据入库，关键指标确定后增加


        logger.debug(devIdTemp + ":" + signalGuidMap.toString());
    }


    /**
     * @param dataTime
     * @param signalGuidMap
     * @param devIdTemp
     * @param data_time
     * @param mapperName:   void
     * @Title: storeDataOfRecovery
     * @Description: 补采数据入库
     * @lastEditor: SP0010
     * @lastEdit: 2017年12月27日下午3:55:19
     */
    public static void storeDataOfRecovery(Long dataTime, Map<String, String> signalGuidMap, String devIdTemp, String data_time, String mapperName) {
        logger.info("start to store or update the records of history message");
        //判断是否存库
        boolean fixedTimeFlag = true;
        if (dataTime % TimeUtil4Xml.DIVISOR == 0) {
            //获取数据存库记录
            Object dataStoreRecords = dataSaveService.selectByIdAndDataTime(mapperName, Integer.valueOf(devIdTemp), dataTime);

            //根据mapperName 分类强转,符合要求就存库
            force2obj(mapperName, devIdTemp, signalGuidMap, dataStoreRecords, fixedTimeFlag, data_time);

        } else {
            //时间周期不在整五分钟
            fixedTimeFlag = false;
            //获取数据存库记录,前一个存库周期
            Long earlierDataTime = TimeUtil4Xml.getEarlierDataStoreMsec(dataTime);
            Object dataStoreRecords = dataSaveService.selectByIdAndDataTime(mapperName, Integer.valueOf(devIdTemp), earlierDataTime);
            //判断是否存在记录
            //根据mapperName 分类强转,符合要求就存库
            force2obj(mapperName, devIdTemp, signalGuidMap, dataStoreRecords, fixedTimeFlag, earlierDataTime.toString());

            //获取数据存库记录,后一个存库周期
            Long laterDataTime = TimeUtil4Xml.getLaterDataStoreMsec(dataTime);
            Object dataStoreRecordsLater = dataSaveService.selectByIdAndDataTime(mapperName, Integer.valueOf(devIdTemp), earlierDataTime);
            //判断是否存在记录
            //根据MapperName 分类强转,符合要求就存库
            force2obj(mapperName, devIdTemp, signalGuidMap, dataStoreRecordsLater, fixedTimeFlag, laterDataTime.toString());
        }

    }


    public static void ycSignalValueProcess(String signalGuid, String signalValue, String detailSignalVal, Map<String, String> signalGuidMap, String deviceId, List<DataYx> yc4yxDataList, String data_time) {
        // TODO(待测试) ①信号点值特殊处理方法(前多少位整数，后多少位小数)(拿到方法，遥测参数默认)②判断是否是遥测表遥信(比如中间几位)(拿到参数，起始位，读取终止位)
        String processMethod = CacheMappingUtil.signalGuid2processMethod(signalGuid);
        //detailMap中放的是起始位和读取终止位
        Map<Integer, Integer> detailMap = CacheMappingUtil.signalGuid2processDetail(signalGuid);
        if (processMethod != null) {
            //判断是遥测特殊处理(含小数)还是遥测表遥信
            if (detailMap != null) {
                Set<Entry<Integer, Integer>> entrySet = detailMap.entrySet();
                //只会循环一次
                Integer startBit = null;
                Integer endBit = null;
                for (Entry<Integer, Integer> entry : entrySet) {
                    startBit = entry.getKey();
                    endBit = entry.getValue();
                }
                //正常数据都会不等于
                if (startBit != endBit) {
                    String signalValueTemp = ReflectionUtil.specialProcess4Parse(processMethod, signalValue, startBit, endBit);
                    detailSignalVal = signalValueTemp;
                }

            } else {
                String signalValueTemp = ReflectionUtil.specialProcess4Parse(processMethod, signalValue);
                detailSignalVal = signalValueTemp;
            }
        } else {
            //特殊处理方法是空，但是遥测表遥信这种，截取中间几位detailMap不是空
            if (detailMap != null) {
                //截取方法是ycProcess4yx,直接调用
                Set<Entry<Integer, Integer>> entrySet = detailMap.entrySet();
                //只会循环一次
                Integer startBit = null;
                Integer endBit = null;
                for (Entry<Integer, Integer> entry : entrySet) {
                    startBit = entry.getKey();
                    endBit = entry.getValue();
                }
                //正常数据都会不等于
                if (startBit != endBit) {
                    String signalValueTemp = DataProcessingUtil.ycProcess4yx(signalValue, startBit, endBit);
                    detailSignalVal = signalValueTemp;
                }

            } else {
                //无特殊处理方法，无截取位数
                detailSignalVal = signalValue;
            }
        }

        //增益和修正计算，然后值范围判断，正常就存库

        double signalVal = DataProcessingUtil.gainFactor(detailSignalVal, signalGuid);

        //遥测表遥信判断
        String signalValTarget = null;
        if (CacheMappingUtil.signalGuid4yc2yx(signalGuid)) {
            //遥信就变成Integer
            Integer signalVal4compare = (int) (signalVal);
            signalValTarget = String.valueOf(signalVal4compare);
            //处于正常值范围就存redis，判断使用原始值，存库用处理后整型
            if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
                //变位信息对比
                String latestSignalVal = signalGuidMap.get(signalGuid);
                //遥信有变位就存库
                if (latestSignalVal != null && !signalValTarget.equals(latestSignalVal)) {
                    //判断
                    //关联表查到yx_id存库
                    Integer yxId = CacheMappingUtil.signalGuid2yxId(signalGuid, signalValTarget);
                    if (yxId != null) {
                        DataYx dataYx;
                        //正常遥信status传0或1,其他的传1
                        if (signalVal4compare > 1) {
                            yc4yxDataList.add(dataYx = new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time), signalValTarget, "1", "0"));
                        } else {
                            yc4yxDataList.add(dataYx = new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time), signalValTarget, signalValTarget, "0"));
                        }
                        // yxId是告警则存redis
                        if (DeviceCache.alramLevelMap.get(yxId) != null) {
                            JedisUtil4OPS.addAlarm(dataYx);
                        }
                        //TODO 增加电站状态，0表示正常，1表示异常和故障
                        if (CacheMappingUtil.yxIdAlarmContain(yxId)) {
                            Integer plantId = CacheMappingUtil.devId2plantId(Integer.parseInt(deviceId));
                            if (plantId != null) {
                                JedisUtil4plantStatus.setString(plantId.toString(), "1");
                            }
                        }
                    }
                }
                //实时值存库
                signalGuidMap.put(signalGuid, signalValTarget);
            }

        } else {
            //保留六位小数存库，光伏计算使用四位，前台显示两位
            signalValTarget = decimalFormat.format(signalVal);
            //处于正常值范围就存redis，判断使用原始值，存库用保留六位小数值
            if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
                signalGuidMap.put(signalGuid, signalValTarget);
            }
        }


    }

}



