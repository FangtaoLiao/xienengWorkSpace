package com.synpowertech.dataCollectionJar.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.chainsaw.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpowertech.dataCollectionJar.dao.DeviceMapper;
import com.synpowertech.dataCollectionJar.domain.DataCentralInverter;
import com.synpowertech.dataCollectionJar.domain.DataElectricMeter;
import com.synpowertech.dataCollectionJar.domain.DataStringInverter;
import com.synpowertech.dataCollectionJar.domain.DataTransformer;
import com.synpowertech.dataCollectionJar.domain.DataYx;
import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil4plantStatus;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil4signal;
import com.synpowertech.dataCollectionJar.initialization.RabbitMqProducer;
import com.synpowertech.dataCollectionJar.service.IDataSaveService;
import com.synpowertech.dataCollectionJar.timedTask.DataSaveTask;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: JsonParseUtil
 * @Description: xml解析和封装的工具类，同时队解析后的数据进行处理
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年4月13日下午2:42:29 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class JsonParseUtil implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(JsonParseUtil.class);

	@Autowired
	IDataSaveService dataSaveServiceTemp;
	@Autowired
	DeviceMapper deviceMapperTemp;

	static IDataSaveService dataSaveService;
	static DeviceMapper deviceMapper;

	// 保留六位小数存库，光伏计算使用四位，前台显示两位
	static DecimalFormat decimalFormat;

	// 全局使用
	static JsonFactory jacksonFactory = new JsonFactory();
	static ObjectMapper jacksonMapper = new ObjectMapper();

	public void afterPropertiesSet() throws Exception {
		// 注入变量赋给静态变量
		dataSaveService = dataSaveServiceTemp;
		deviceMapper = deviceMapperTemp;
		// 保留六位小数存库，光伏计算使用四位，前台显示两位
		decimalFormat = new DecimalFormat("#.######");
		if (dataSaveService == null) {
			logger.error("JsonParseUtil's dataSaveService inits unsuccessfully！");
		} else {
			logger.info("JsonParseUtil's dataSaveService inits successfully！");
		}
	}

	/**
	  * @Title:  jsonStr2Map 
	  * @Description:  TODO 解析新数采订阅设备的信号数据入口
	  * @param collsn
	  * @param jsonStr
	  * @throws JsonParseException
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月20日下午5:48:47
	 */
	public static void jsonStr2Map(String collsn, String jsonStr) throws JsonParseException, IOException {
		// 用于日志记录的简化map,Map<YX_0,value>,//原始值日志Map

		logger.debug("start to parse the message");
		Map<String, String> logMap = new HashMap<String, String>();

		// Map<deviceId, Map<signalGuid, value>>
		Map<String, String> timeMap = null;
		// 根据采集节点id找到数采设备id
		Integer dataCollId = CacheMappingUtil.collectorId2devId(collsn);
		if (dataCollId == null) {
			logger.warn("can not find the relevant dataCollector:{}", collsn);
			return;
		}
		String dataColl_id = String.valueOf(dataCollId);

		// TODO 流API解析
		JsonParser jsonParser = jacksonFactory.createParser(jsonStr);

		// redis中时间
		String dataTimeTemp = null;
		//数据时间
		String data_time = null;
		Long dataTime = null;
		//报文类型
		String typeName = null;
		//当前keyName
		String currentFieldName = null;
		//move to object {
		jsonParser.nextToken();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

			// get the current token
			currentFieldName = jsonParser.getCurrentName();

			if ("time".equals(currentFieldName)) {
				// move to next token
				jsonParser.nextToken();
				data_time = jsonParser.getText();
				dataTime = Long.parseLong(data_time);
				// 初始化timeMap
				timeMap = JedisUtil.getMap(dataColl_id) == null ? new HashMap<String, String>() : JedisUtil.getMap(dataColl_id);
				dataTimeTemp = timeMap.get("data_time");
				logger.debug("parsing the message");
				continue;
			}

			if ("type".equals(currentFieldName)) {
				// move to next token
				jsonParser.nextToken();
				typeName = jsonParser.getText();
				continue;
			}

			// *****************判断是否是历史数据分流,//数采时间超前，直接入库***************
			if ((dataTimeTemp != null && Long.valueOf(dataTimeTemp) > dataTime)) {
				// TODO 补采解析，一种是滞后信息，一种是补采信息,0,1,6,11(补采信息)
				//redis数据比报文数据新，只更新sys_time后，数据补采逻辑执行(历史数据补采)
				logger.info("start to parse a history message");
				
				//数据补采逻辑
				//以设备为单位先解析封装，判断数据库中是否有该时刻数据，若有就不解析不存，如没有就解析后存库
				// 当前报文接收时间
				Long sys_time = null;
				String sys_time_str = null;
				// 设备id temp;
				Long devIdTemp = null;
				String devIdStrTemp = null;
				Integer dataModeTemp = null;
				Map<String, String> signalGuidMap = null;

				if ("data".equals(currentFieldName)) {
					// move to [ 或者 { 或者下一个属性
					jsonParser.nextToken();

					// 按信号类型匹配
					switch (typeName) {
					// 全量数据
					case "0":
						jsonHistoryDataParse(collsn, logMap, timeMap, dataCollId, dataColl_id, jsonParser, dataTime,data_time,devIdTemp, devIdStrTemp, dataModeTemp, signalGuidMap,false);
						break;
					// 突发上送，一般是遥信
					case "1":
						jsonHistoryDataParse(collsn, logMap, timeMap, dataCollId, dataColl_id, jsonParser, dataTime, data_time,devIdTemp, devIdStrTemp, dataModeTemp, signalGuidMap,true);
						break;
						// 数采本身告警信息
					case "3":
						connHistoryStatusParse(logMap, timeMap, dataCollId, dataColl_id, jsonParser, data_time, dataTime);
						break;
						// 数采下发点表指令回执
					case "6":
						jsonModelResult2Map(logMap, collsn, dataColl_id, jsonParser, data_time);
						break;
					// TODO 数采补采报文,为避免误操作，暂不入库
					case "11":
						// 当前报文接收时间
						sys_time = TimeUtil4Xml.getMsec();
						sys_time_str = String.valueOf(sys_time);
						timeMap.put("sys_time", sys_time_str);
						//历史数据只给数采连接状态
						timeMap.put("conn_status", "1");
						logMap.put("sys_time", sys_time_str);
						// 更新数据采集器时间
						JedisUtil.setMap(dataColl_id, timeMap);
						logger.info(dataColl_id + "-" + timeMap.toString());
						// logger.info(jsonStr);
						break;
					
					default:
						break;
					}

				}

			} else {
				// TODO*****************************(正常解析)*************************************
				// 不存在该数采或者报文数据比redis数据新，直接解析
				logger.info("start to parse a real-time JsonMessage");
				// 设备id temp;
				Long devIdTemp = null;
				String devIdStrTemp = null;
				Integer dataModeTemp = null;
				Map<String, String> signalGuidMap = null;

				if ("data".equals(currentFieldName)) {
					// move to [ 或者 { 或者下一个属性
					jsonParser.nextToken();

					// 按信号类型匹配
					switch (typeName) {
					// 全量数据
					case "0":
						jsonDataParse(collsn, logMap, timeMap, dataCollId, dataColl_id, jsonParser, data_time,devIdTemp, devIdStrTemp, dataModeTemp, signalGuidMap,false);
						break;
					// 突发上送，一般是遥信
					case "1":
						jsonDataParse(collsn, logMap, timeMap, dataCollId, dataColl_id, jsonParser, data_time,devIdTemp, devIdStrTemp, dataModeTemp, signalGuidMap,true);
						break;
					// 数采本身告警信息
					case "3":
						connStatusParse(logMap, timeMap, dataCollId, dataColl_id, jsonParser, data_time, dataTime);
						break;
						// 数采下发点表指令回执
					case "6":
						jsonModelResult2Map(logMap, collsn, dataColl_id, jsonParser, data_time);
						break;
					
					default:
						// 数采自身设置指令回执
						// 数采校时指令回执
						// 点表信息下发指令回执
						// 目前写进日志的信息Typename:4,5,8,9,10,12,debug
						// logger.info(jsonStr);
						break;
					}

				}

			}
		}
	}

	/** 
	  * @Title:  connStatusParse 
	  * @Description:  TODO 新数采设备状态判断和告警记录生成,响应速度最快，不用去redis对比
	  * @param logMap
	  * @param timeMap
	  * @param dataCollId
	  * @param dataColl_id
	  * @param jsonParser
	  * @param data_time
	  * @param dataTime
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月25日下午6:20:08
	*/
	public static void connStatusParse(Map<String, String> logMap, Map<String, String> timeMap, Integer dataCollId,
			String dataColl_id, JsonParser jsonParser, String data_time, Long dataTime) throws IOException {
		Long sys_time;
		String sys_time_str;
		//数据库告警记录
		List<DataYx> yc4yxDataList = null;
		
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String currentKey = jsonParser.getCurrentName();
			if (currentKey.equals("sort")) {
				jsonParser.nextToken();
				yc4yxDataList = new ArrayList<DataYx>(5);
				
				//告警类别，1表示断电告警，2表示设备断连
				String val = jsonParser.getText();
				if ("1".equals(val)) {
					timeMap.put("conn_status", "0");
					//TODO 告警记录data_yx 中yx_id值：-1：断电告警；-2：设备断连,数采只有断电告警，设备只有通讯中断告警
					yc4yxDataList.add(new DataYx(dataCollId, -1, dataTime,null,"1","0"));
					
					//TODO 其下挂设备也是通讯中断,下挂设备也都是设备断连
					List<Integer> subDevIdList = deviceMapper.selectBySupId(dataCollId);
					if (subDevIdList != null) {
						for (Integer devId : subDevIdList) {
							JedisUtil.setStringInMap(devId.toString(),"conn_status", "0");
							//TODO data_yx 中yx_id值：-2：设备断连
							yc4yxDataList.add(new DataYx(devId, -2, dataTime,null,"1","0"));
						}
					}
					
				}
				
			}
			//仅sort=2的才有；设备断连判断标准：读50次没反回值
			if (currentKey.equals("dev_id")) {
				String comIdStr = jsonParser.getText();
				if (comIdStr != null) {
					//T得到com口和salveId的值,查找到对应设备id,redis置为通讯中断
					Long devId = CacheMappingUtil.slaveId2devId((long)dataCollId, comIdStr);
					if (devId != null) {
						JedisUtil.setStringInMap(devId.toString(),"conn_status", "0");
						//TODO data_yx 中yx_id值：-2：设备断连
						yc4yxDataList.add(new DataYx(devId.intValue(), -2, dataTime,null,"1","0"));
					}
				}
			}
			
		}
		
		// 数据采集器设备数据时间
		timeMap.put("data_time", data_time);
		// 当前报文接收时间
		sys_time = TimeUtil4Xml.getMsec();
		sys_time_str = String.valueOf(sys_time);
		timeMap.put("sys_time", sys_time_str);
		logMap.put("sys_time", sys_time_str);
		// 更新数据采集器时间
		JedisUtil.setMap(dataColl_id, timeMap);
		logger.info(dataColl_id + "-" + timeMap.toString());
		// logger.info(jsonStr);
		
		//TODO 告警入库
		if (yc4yxDataList != null && yc4yxDataList.size() > 0) {
			dataSaveService.yxDataSave(yc4yxDataList);
		}
	}
	
	/**
	  * @Title:  connHistoryStatusParse 
	  * @Description:  TODO 连接状态历史报文解析
	  * @param logMap
	  * @param timeMap
	  * @param dataCollId
	  * @param dataColl_id
	  * @param jsonParser
	  * @param data_time
	  * @param dataTime
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月27日下午4:04:12
	 */
	public static void connHistoryStatusParse(Map<String, String> logMap, Map<String, String> timeMap, Integer dataCollId,
			String dataColl_id, JsonParser jsonParser, String data_time, Long dataTime) throws IOException {
		Long sys_time;
		String sys_time_str;
		//数据库告警记录
		List<DataYx> yc4yxDataList = null;
		
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String currentKey = jsonParser.getCurrentName();
			if (currentKey.equals("sort")) {
				jsonParser.nextToken();
				yc4yxDataList = new ArrayList<DataYx>(5);
				
				//告警类别，1表示断电告警，2表示设备断连
				String val = jsonParser.getText();
				if ("1".equals(val)) {
					//历史状态报文不写redis
					//timeMap.put("conn_status", "0");
					//TODO 告警记录data_yx 中yx_id值：-1：断电告警；-2：设备断连,数采只有断电告警，设备只有通讯中断告警
					yc4yxDataList.add(new DataYx(dataCollId, -1, dataTime,null,"1","0"));
					
					//TODO 其下挂设备也是通讯中断,下挂设备也都是设备断连
					List<Integer> subDevIdList = deviceMapper.selectBySupId(dataCollId);
					if (subDevIdList != null) {
						for (Integer devId : subDevIdList) {
							//历史状态报文不写redis
							//JedisUtil.setStringInMap(devId.toString(),"conn_status", "0");
							//TODO data_yx 中yx_id值：-2：设备断连
							yc4yxDataList.add(new DataYx(devId, -2, dataTime,null,"1","0"));
						}
					}
					
				}
				
			}
			//仅sort=2的才有；设备断连判断标准：读50次没反回值
			if (currentKey.equals("dev_id")) {
				String comIdStr = jsonParser.getText();
				if (comIdStr != null) {
					//T得到com口和salveId的值,查找到对应设备id,redis置为通讯中断
					Long devId = CacheMappingUtil.slaveId2devId((long)dataCollId, comIdStr);
					if (devId != null) {
						//历史状态报文不写redis
						//JedisUtil.setStringInMap(devId.toString(),"conn_status", "0");
						//TODO data_yx 中yx_id值：-2：设备断连
						yc4yxDataList.add(new DataYx(devId.intValue(), -2, dataTime,null,"1","0"));
					}
				}
			}
			
		}
		
		// 历史报文不写数据采集器设备数据时间
		//timeMap.put("data_time", data_time);
		// 当前报文接收时间
		sys_time = TimeUtil4Xml.getMsec();
		sys_time_str = String.valueOf(sys_time);
		//历史报文只更新系统处理时间
		timeMap.put("sys_time", sys_time_str);
		logMap.put("sys_time", sys_time_str);
		// 更新数据采集器时间
		JedisUtil.setMap(dataColl_id, timeMap);
		logger.info(dataColl_id + "-" + timeMap.toString());
		// logger.info(jsonStr);
		
		//TODO 告警入库
		if (yc4yxDataList != null && yc4yxDataList.size() > 0) {
			dataSaveService.yxDataSave(yc4yxDataList);
		}
	}

	/** 
	  * @Title:  jsonDataParse 
	  * @Description:  TODO jsonDataParse是解析新数采全量数据和变位数据的公共方法
	  * @param collsn
	  * @param logMap
	  * @param timeMap
	  * @param dataCollId
	  * @param dataColl_id
	  * @param jsonParser
	  * @param data_time
	  * @param devIdTemp
	  * @param devIdStrTemp
	  * @param dataModeTemp
	  * @param signalGuidMap
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月20日下午5:44:10
	*/
	public static void jsonDataParse(String collsn, Map<String, String> logMap, Map<String, String> timeMap,
			Integer dataCollId, String dataColl_id, JsonParser jsonParser, String data_time, Long devIdTemp,
			String devIdStrTemp, Integer dataModeTemp, Map<String, String> signalGuidMap,Boolean changeable) throws IOException{
		Long sys_time;
		String sys_time_str;
		//告警代码，需和数采厂商双方定义
		//状态值：0-通讯中断；1-通讯正常
		timeMap.put("conn_status", "1");
		// 数据采集器设备数据时间
		timeMap.put("data_time", data_time);
		// 当前报文接收时间
		sys_time = TimeUtil4Xml.getMsec();
		sys_time_str = String.valueOf(sys_time);
		timeMap.put("sys_time", sys_time_str);
		logMap.put("sys_time", sys_time_str);
		// 更新数据采集器时间
		JedisUtil.setMap(dataColl_id, timeMap);
		logger.info(dataColl_id + "-" + timeMap.toString());

		
		// loop till token equal to "]"
		String comTemp = null;
		String slaveIdTemp = null;
		//遥测表遥信数据如果有变位增加存数据库的操作
		List<DataYx> yc4yxDataList = null;
		
		//数组中有数据第一次指针会指向{，空循环一次
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			if ("com".equals(jsonParser.getCurrentName())) {
				jsonParser.nextToken();
				comTemp = jsonParser.getText();
				if (comTemp != null){
					//去掉com
					comTemp = comTemp.substring(3);
				}
				logger.debug(comTemp);
				continue;
			}

			if ("devs".equals(jsonParser.getCurrentName())) {
				// move to [
				jsonParser.nextToken();
				// loop till token equal to "]"
				
				//数组中有数据第一次指针会指向{，空循环一次
				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
					if ("id".equals(jsonParser.getCurrentName())) {
						jsonParser.nextToken();
						slaveIdTemp = jsonParser.getText();
						devIdTemp = CacheMappingUtil.slaveId2devId((long)dataCollId, comTemp + "_" + slaveIdTemp);
						dataModeTemp = CacheMappingUtil.devId2dataMode((int)(long)devIdTemp);
						logger.debug(slaveIdTemp + "-" + devIdTemp + "-" + dataModeTemp);
						//TODO 清空，防止重复提交
						signalGuidMap = null;
						continue;
					}

					if ("regs".equals(jsonParser.getCurrentName())) {
						// move  to "{"
						jsonParser.nextToken();
						String addrTemp = null;
						String sourceVal = null;
						List<String> signalGuidList = null;
						Boolean addrSigNumTemp = false;
						
						// loop till token equal to "}"
						while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
							//缓存中没有查到设备id或者dataMode,不解析存redis
							if (devIdTemp == null || dataModeTemp == null) {
								break;
							}
							
							jsonParser.nextToken();
							//对于value,jsonParse.getCurrentName,就是它的key
							addrTemp = jsonParser.getCurrentName();
							sourceVal = jsonParser.getText();
							logger.debug(addrTemp + "-" + sourceVal);
							
							
							signalGuidList = CacheMappingUtil.dataModel2addrGuid(dataModeTemp, addrTemp);
							//缓存中没有查到信号signalGuid,不存redis
							if (signalGuidList == null) {
								continue;
							}
							logger.debug(signalGuidList.toString());
							
							
							devIdStrTemp = devIdTemp.toString();
							if (signalGuidMap == null) {
								signalGuidMap = JedisUtil.getMap(devIdStrTemp) == null ? new HashMap<String, String>():JedisUtil.getMap(devIdStrTemp);
								logger.debug(signalGuidMap.toString());
							}
							//原始值日志Map
							logMap.put(comTemp + ":" + slaveIdTemp + ":" + addrTemp, sourceVal);
							
				/*			//如果是合成信号的高位地址,先保存值，跳过
							if (CacheMappingUtil.addrHighListContain(dataModeTemp, addrTemp)) {
								addrHFlag = !addrHFlag;
								addrHValTemp = sourceVal;
								addrHGuidListTemp = signalGuidList;
								//以高位为准，get(0)
								addrHSignedNumTemp = addrSigNumTemp;
								continue;
							}
							
							//如果是合成信号的低位地址,则拼接值字符串
							if (addrHFlag) {
								addrHFlag = !addrHFlag; 
								sourceVal = addrHValTemp + sourceVal;
								signalGuidList = addrHGuidListTemp;
								addrSigNumTemp = addrHSignedNumTemp;
							}*/
							
							//TODO 高低位信号判断处理
							//以高位为准，获取第一个guid
							String guidFirst = signalGuidList.get(0);
							logger.debug(guidFirst);
							String signalGuidL = CacheMappingUtil.getSignalGuidL(dataModeTemp, guidFirst);
							String signalGuidH = CacheMappingUtil.getSignalGuidH(dataModeTemp, guidFirst);
							if (StringUtils.isNotBlank(signalGuidL)) {
								//查询低位地址值
								String guidLVal= JedisUtil4signal.getStringInMap(devIdStrTemp, signalGuidL + "-" + data_time);
								//不存在数据时间低位地址值，就在redis中存储高位值
								if (guidLVal == null) {
									JedisUtil4signal.setStringInMap(devIdStrTemp, guidFirst + "-" + data_time, sourceVal);
									continue;
								}
								//合成信号值
								sourceVal = sourceVal + guidLVal;
								//删除redis中低位信息
								JedisUtil4signal.delStringInMap(devIdStrTemp,  signalGuidL + "-" + data_time);
							}else if (StringUtils.isNotBlank(signalGuidH))  {
								//查询高位地址的值
								String guidHVal= JedisUtil4signal.getStringInMap(devIdStrTemp, signalGuidH + "-" + data_time);
								
								//不存在数据时间高位地址值，就在redis中 存储低位值
								if (guidHVal == null) {
									JedisUtil4signal.setStringInMap(devIdStrTemp, guidFirst + "-" + data_time, sourceVal);
									continue;
								}
								//合成信号值
								sourceVal = guidHVal + sourceVal;
								//删除redis中高位信息
								JedisUtil4signal.delStringInMap(devIdStrTemp,  signalGuidH + "-" + data_time);
								//TODO 以高位为准，替换guidList,合成一般就表示一个信号
								signalGuidList = new ArrayList<String>(1);
								signalGuidList.add(signalGuidH);
								//
							}
							
							//以第一个为准
							addrSigNumTemp =  CacheMappingUtil.signedNumJudge(signalGuidList.get(0));
							
							//进制转换
							String  signalValue = RadixProcessUtil.HexStr2Long(sourceVal, addrSigNumTemp).toString();
							logger.debug("signalValue" + "-" + signalValue);
							//寄存器值拆分后的
							String detailSignalVal = null;
							
							
							//是变位上送就存库
							if (changeable) {
								yc4yxDataList = new ArrayList<DataYx>();
							}
							
							//遍历这个信号下所有子信号
							for (String signalGuid : signalGuidList) {
								//特殊处理(遥测表遥信等)，增益修正判断，保留小数，值有效判断，封装Map等
								ycSignalValueProcess(signalGuid,signalValue,detailSignalVal,signalGuidMap,devIdStrTemp,yc4yxDataList,data_time);
							}
							//封装值后提交情况
							logger.debug(devIdTemp + "封装值" + signalGuidMap.toString());
							
						}
						//将一个采集的数据最一个设备数据提交(包含遥测和遥信一起，也可能只有遥测数据)，一个设备提交一次
						if (signalGuidMap != null) {
							signalGuidMap.put("data_time", data_time);
							signalGuidMap.put("sys_time", sys_time_str);
							signalGuidMap.put("conn_status", "1");
							
							JedisUtil.setMap(devIdTemp.toString(), signalGuidMap);
							// TODO 原始报文取消后，使用这个保存格式化原始报文
							//logger.info(devIdTemp + ":" + signalGuidMap.toString());
						}
						
					}
				
				}
				
			}
		}
		//遥测表遥信，变位遥信列表数据存库操作,一条信息提交一次
		if (yc4yxDataList != null && yc4yxDataList.size() > 0) {
			dataSaveService.yxDataSave(yc4yxDataList);
		}
		//该条报文简化日志
		logger.info("the message has ended parsing");
		logger.info(collsn + "-" + data_time + "-" + logMap.toString());
	}

	/** 
	  * @Title:  jsonHistoryDataParse 
	  * @Description:  TODO jsonHistoryDataParse是解析新数采(历史)全量数据和(历史)变位数据的公共方法
	  * @param collsn
	  * @param logMap
	  * @param timeMap
	  * @param dataCollId
	  * @param dataColl_id
	  * @param jsonParser
	  * @param data_time
	  * @param devIdTemp
	  * @param devIdStrTemp
	  * @param dataModeTemp
	  * @param signalGuidMap
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月20日下午5:44:10
	*/
	public static void jsonHistoryDataParse(String collsn, Map<String, String> logMap, Map<String, String> timeMap,
			Integer dataCollId, String dataColl_id, JsonParser jsonParser,Long dataTime, String data_time, Long devIdTemp,
			String devIdStrTemp, Integer dataModeTemp, Map<String, String> signalGuidMap,Boolean changeable) throws IOException{
		Long sys_time;
		String sys_time_str;
		//告警代码，需和数采厂商双方定义
		//状态值：0-通讯中断；1-通讯正常,历史数据只给数采连接状态
		timeMap.put("conn_status", "1");
		// 当前报文接收时间
		sys_time = TimeUtil4Xml.getMsec();
		sys_time_str = String.valueOf(sys_time);
		timeMap.put("sys_time", sys_time_str);
		logMap.put("sys_time", sys_time_str);
		// 更新数据采集器时间
		JedisUtil.setMap(dataColl_id, timeMap);
		logger.info(dataColl_id + "-" + timeMap.toString());

		
		// loop till token equal to "]"
		String comTemp = null;
		String slaveIdTemp = null;
		//遥测表遥信数据如果有变位增加存数据库的操作
		List<DataYx> yc4yxDataList = null;
		
		//数组中有数据第一次指针会指向{，空循环一次
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			if ("com".equals(jsonParser.getCurrentName())) {
				jsonParser.nextToken();
				comTemp = jsonParser.getText();
				if (comTemp != null){
					//去掉com
					comTemp = comTemp.substring(3);
				}
				continue;
			}

			if ("devs".equals(jsonParser.getCurrentName())) {
				// move to [
				jsonParser.nextToken();
				// loop till token equal to "]"
				
				//数组中有数据第一次指针会指向{，空循环一次
				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
					if ("id".equals(jsonParser.getCurrentName())) {
						jsonParser.nextToken();
						slaveIdTemp = jsonParser.getText();
						devIdTemp = CacheMappingUtil.slaveId2devId((long)dataCollId, comTemp + "_" + slaveIdTemp);
						dataModeTemp = CacheMappingUtil.devId2dataMode((int)(long)devIdTemp);
						//TODO 清空，防止重复提交
						signalGuidMap = null;
						continue;
					}

					if ("regs".equals(jsonParser.getCurrentName())) {
						jsonParser.nextToken();
						// loop till token equal to "}"
						String addrTemp = null;
						String sourceVal = null;
						List<String> signalGuidList = null;
						Boolean addrSigNumTemp = false;
						
						while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
							//缓存中没有查到设备id或者dataMode,不解析存redis
							if (devIdTemp == null || dataModeTemp == null) {
								break;
							}
							
							jsonParser.nextToken();
							//对于value,jsonParse.getCurrentName,就是它的key
							addrTemp = jsonParser.getCurrentName();
							sourceVal = jsonParser.getText();
							
							signalGuidList = CacheMappingUtil.dataModel2addrGuid(dataModeTemp, addrTemp);
							//缓存中没有查到信号signalGuid,不存redis
							if (signalGuidList == null) {
								continue;
							}
							
							devIdStrTemp = devIdTemp.toString();
							//signalGuidMap = JedisUtil.getMap(devIdStrTemp) == null ? new HashMap<String, String>():JedisUtil.getMap(devIdStrTemp);
							//和redis没关系，换设备直接new
							if (signalGuidMap == null) {
								signalGuidMap = new HashMap<String, String>();
							}
							
							//原始值日志Map
							logMap.put(comTemp + ":" + slaveIdTemp + ":" + addrTemp, sourceVal);
							
							/*//如果是合成信号的高位地址,先保存值，跳过
							if (CacheMappingUtil.addrHighListContain(dataModeTemp, addrTemp)) {
								addrHFlag = !addrHFlag;
								addrHValTemp = sourceVal;
								addrHGuidListTemp = signalGuidList;
								//以高位为准，get(0)
								addrHSignedNumTemp = addrSigNumTemp;
								continue;
							}
							
							//如果是合成信号的低位地址,则拼接值字符串
							if (addrHFlag) {
								addrHFlag = !addrHFlag; 
								sourceVal = addrHValTemp + sourceVal;
								signalGuidList = addrHGuidListTemp;
								addrSigNumTemp = addrHSignedNumTemp;
							}*/
							
							//TODO 高低位信号判断处理
							//以高位为准，获取第一个guid
							String guidFirst = signalGuidList.get(0);
							String signalGuidL = CacheMappingUtil.getSignalGuidL(dataModeTemp, guidFirst);
							String signalGuidH = CacheMappingUtil.getSignalGuidH(dataModeTemp, guidFirst);
							if (StringUtils.isNotBlank(signalGuidL)) {
								//查询低位地址值
								String guidLVal= JedisUtil4signal.getStringInMap(devIdStrTemp, signalGuidL + "-" + data_time);
								//不存在数据时间低位地址值，就在redis中存储高位值
								if (guidLVal == null) {
									JedisUtil4signal.setStringInMap(devIdStrTemp, guidFirst + "-" + data_time, sourceVal);
									continue;
								}
								//合成信号值
								sourceVal = sourceVal + guidLVal;
								//删除redis中低位信息
								JedisUtil4signal.delStringInMap(devIdStrTemp,  signalGuidL + "-" + data_time);
							}else if (StringUtils.isNotBlank(signalGuidH))  {
								//查询高位地址的值
								String guidHVal= JedisUtil4signal.getStringInMap(devIdStrTemp, signalGuidH + "-" + data_time);
								
								//不存在数据时间高位地址值，就在redis中 存储低位值
								if (guidHVal == null) {
									JedisUtil4signal.setStringInMap(devIdStrTemp, guidFirst + "-" + data_time, sourceVal);
									continue;
								}
								//合成信号值
								sourceVal = guidHVal + sourceVal;
								//删除redis中高位信息
								JedisUtil4signal.delStringInMap(devIdStrTemp,  signalGuidH + "-" + data_time);
								//TODO 以高位为准，替换guidList,合成一般就表示一个信号
								signalGuidList = new ArrayList<String>(1);
								signalGuidList.add(signalGuidH);
								//
							}
							
							//以第一个为准
							addrSigNumTemp =  CacheMappingUtil.signedNumJudge(signalGuidList.get(0));
							
							
							//进制转换
							String  signalValue = RadixProcessUtil.HexStr2Long(sourceVal, addrSigNumTemp).toString();
							//寄存器值拆分后的
							String detailSignalVal = null;
							
							
							//是变位上送就存库
							if (changeable) {
								yc4yxDataList = new ArrayList<DataYx>();
							}
							
							//遍历这个信号下所有子信号
							for (String signalGuid : signalGuidList) {
								//特殊处理(遥测表遥信等)，增益修正判断，保留小数，值有效判断，封装Map等
								ycSignalValueProcess(signalGuid,signalValue,detailSignalVal,signalGuidMap,devIdStrTemp,yc4yxDataList,data_time);
							}
							
						}
				
					}
					
					//将一个采集的一个设备数据提交(包含遥测和遥信一起，也可能只有遥测数据)，一个设备提交一次
					if (signalGuidMap != null) {
						signalGuidMap.put("data_time", data_time);
						signalGuidMap.put("sys_time", sys_time_str);
						//这里不给连接状态，历史数据
						
						//JedisUtil.setMap(devIdTemp.toString(), signalGuidMap);
						
						//获取MapperName
						String modelName = CacheMappingUtil.signalGuid2model(signalGuidMap);
						if (modelName == null) {
							continue;
						}
						//modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
						String mapperName = Table2modelUtil.lowerCaseFirstLatter(modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";
						//补采数据入库
						storeDataOfRecovery(dataTime, signalGuidMap, devIdTemp.toString(), data_time, mapperName);
						
						logger.debug(devIdTemp + ":" + signalGuidMap.toString());
					}
				}
			}
		}
		
		//遥测表遥信，变位遥信列表数据存库操作,一条信息提交一次
		if (yc4yxDataList != null && yc4yxDataList.size() > 0) {
			dataSaveService.yxDataSave(yc4yxDataList);
		}
		
		//该条报文简化日志
		logger.info("the message has ended parsing");
		logger.info(collsn + "-" + data_time + "-" + logMap.toString());
	}

	
	/**
	  * @Title:  jsonResultStr2Map 
	  * @Description:  TODO 遥控遥调指令返回结果解析 封装成Map<deviceId/sessionId, List<Map<signalGuid, value>>>序列化到rabbitMq队列中，
	  * @param collsn
	  * @param jsonStr
	  * @throws JsonParseException
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月16日下午2:24:37
	 */
	public static void jsonResultStr2Map(String collsn, String jsonStr) throws JsonParseException, IOException {
		
		logger.debug("start to parse the ykYtResult message");
		//用于日志记录的简化map,Map<YK_0,result>,//原始值日志Map
		Map<String, String> logMap = new HashMap<String, String>();
		
		//HashMap<String, Object>，第一个key是sessionId,第二个key是data_time，第三个 key是value,值是List<Map<String, String>>
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		// TODO 流API解析
		JsonParser jsonParser = jacksonFactory.createParser(jsonStr);
		
		// 根据采集节点id找到对应数采设备id
		Integer dataCollId = CacheMappingUtil.collectorId2devId(collsn);
		if (dataCollId == null) {
			logger.warn("can not find the relevant dataCollector:{} for the ykYtResultParse",collsn);
			return;
		}

		logMap.put("sys_time", String.valueOf(new Date().getTime()));
		// 数据采集器设备数据时间
		String dataTime = null;

		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			// get the current token
			String fieldname = jsonParser.getCurrentName();
			if ("time".equals(fieldname)) {
				// move to next token
				jsonParser.nextToken();
				dataTime = jsonParser.getText();
				resultMap.put("data_time", dataTime);
				continue;
			}

			if ("type".equals(fieldname)) {
				// move to next token
				jsonParser.nextToken();
				continue;
			}

			if ("ctrl_key".equals(fieldname)) {
				// move to next token
				jsonParser.nextToken();
				String sessionId = jsonParser.getText();
				resultMap.put("sessionId",sessionId);
				logMap.put("sessionId",sessionId);
				continue;
			}
			
			if ("data".equals(fieldname)) {
				// loop till token equal to "}"\
				String currentKey = null;
				Integer newCollId = null;
				Long devId = null;
				
				Map<String, String> signalResultMap = null;
				String execResult = null;
				
				while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
					currentKey = jsonParser.getCurrentName();
					jsonParser.nextToken();
					
					if (currentKey != null) {
						newCollId = CacheMappingUtil.getNewCollId(collsn);
						if (newCollId != null) {
							devId = CacheMappingUtil.slaveId2devId((long)newCollId, currentKey);
							if (devId != null) {
								signalResultMap = new HashMap<String, String>();
								//0执行失败，1执行成功，2数据错误
								execResult = jsonParser.getText();
								switch (execResult) {
								case "0":
									execResult = "failed";
									break;
								case "1":
									execResult = "success";
									break;
								case "2":
									execResult = "dataError";
									break;
								default:
									break;
								}
								
								signalResultMap.put("execResult", execResult);
								logMap.put(currentKey, execResult);
								
								//只返回控制成功的，失败的超时去判断
								if ("success".equals(execResult)) {
									resultList.add(signalResultMap);
								}
								//resultList.add(signalResultMap);
							}
						}
					}
				}
				
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
		
		logger.info(collsn + "-" + dataTime + "-" + logMap.toString());
	}
	
	/**
	  * @Title:  jsonModelResult2Map 
	  * @Description:  TODO 新数采点表设置的回执
	  * @param collsn
	  * @param jsonStr
	  * @throws JsonParseException
	  * @throws IOException: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月27日下午2:42:35
	 */
	public static void jsonModelResult2Map(Map<String, String> logMap, String collSn,
			String dataColl_id, JsonParser jsonParser, String data_time) throws JsonParseException, IOException {
		
		logger.debug("start to parse the dataModelSetResult message");
		//HashMap<Map<field, value>
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String sysTime = String.valueOf(System.currentTimeMillis());
		logMap.put("sys_time", sysTime);
		
		String currentKey = null;
		String orderTime = null;
		String execResult = null;
		
		// loop till token equal to "}"\
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			currentKey = jsonParser.getCurrentName();
			if ("order_time".equals(currentKey)) {
				jsonParser.nextToken();
				orderTime = jsonParser.getText();
				continue;
			}
			
			if ("result".equals(currentKey)) {
				//0设置失败，1设置成功；设置失败时将设置失败的项返回，2数据错误
				execResult = jsonParser.getText();
				switch (execResult) {
				case "0":
					execResult = "failed";
					break;
				case "1":
					execResult = "success";
					break;
				case "2":
					execResult = "dataError";
					break;
				default:
					break;
				}
			}
		}
		
		//只返回控制成功的，失败的超时去判断
		if ("success".equals(execResult)) {
			resultMap.put("orderTime", orderTime);
			resultMap.put("collSn", collSn);
			resultMap.put("dataCollId", dataColl_id);
			resultMap.put("execResult", execResult);
			resultMap.put("dataTime", data_time);
			resultMap.put("sysTime", sysTime);
			
			logMap.put("data_time", data_time);
			logMap.put("orderTime", orderTime);
			logMap.put("collSn_result",collSn + "_" + execResult);
		}
		
		// mybatis批量插入操作的记录(光伏系统实现)
		
		// Rabbit将封装好的操作结果，返回给系统，jackson或commons-lang序列化map(先用json-smart序列化)
		// YkYtResultProducer.getInstance().sendMessage(resultMap);
		logger.debug("resultMap：{}", resultMap.toString());
		logger.debug("the message of ykYtResult has ended parsing");
		
		if (resultMap.size() > 0) {
			RabbitMqProducer.sendMessage(resultMap);
		}
		logger.info(collSn + "-" + data_time + "-" + logMap.toString());
	}

	/**
	  * @Title:  setTimeJson 
	  * @Description:  TODO 新数采服务器主动校时
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日下午2:05:20
	 */
	public static String setTimeJson() {
		Map<String, String> setTimeMap = new LinkedHashMap<String, String>(3);
		long cTime = System.currentTimeMillis();
		long l5 = 5*60*1000;
		//系统时间
		setTimeMap.put("time", String.valueOf(cTime /l5*l5));
		setTimeMap.put("type", "5");
		setTimeMap.put("data", String.valueOf(cTime /l5*l5));
		String setTimeJson = null;
		try {
			setTimeJson = jacksonMapper.writeValueAsString(setTimeMap);
		} catch (JsonProcessingException e) {
			logger.info("setTimeJson error:{}", e.getClass());
		}
		return setTimeJson;
	}
	
	/**
	  * @Title:  setTimeJson 
	  * @Description:  TODO 新数采服务器主动校时到指定时间
	  * @param targetTime
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日下午2:09:24
	 */
	public static String setTimeJson(String targetTime) {
		Map<String, String> setTimeMap = new LinkedHashMap<String, String>(3);
		setTimeMap.put("time", String.valueOf(System.currentTimeMillis()));
		setTimeMap.put("type", "5");
		setTimeMap.put("data", targetTime);
		String setTimeJson = null;
		try {
			setTimeJson = jacksonMapper.writeValueAsString(setTimeMap);
		} catch (JsonProcessingException e) {
			logger.info("setTimeJson error:{}", e.getClass());
		}
		return setTimeJson;
	}
	
	/**
	  * @Title:  map2JsonStr 
	  * @Description:  TODO 封装下发的指令，封装过来的格式 Map<deviceId, Map<field, value>>
	  * @param sessionId
	  * @param collector_id
	  * @param ykYtMap
	  * @return: String
	 * @throws IOException 
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日下午3:43:26
	 */
	public static String map2JsonStr(String sessionId, String collector_id,Map<String,Map<String, String>> ykYtMap) throws IOException {
		
		//①将待封装信息中设备按照com口分类
		Map<String, List<String>> comMap = new HashMap<String, List<String>>();
		Set<String> devIdSet  = ykYtMap.keySet();
		String comIdTemp = null;
		List<String> devListTemp = null;
		for (String devId : devIdSet) {
			String comSlaveId = DeviceCache.devId2slaveIdMap.get(devId);
			if (comSlaveId != null) {
				comIdTemp = comSlaveId.substring(comSlaveId.indexOf("_") + 1, comSlaveId.lastIndexOf("_"));
				devListTemp = comMap.containsKey(comIdTemp) ? comMap.get(comIdTemp) : new ArrayList<String>();
				devListTemp.add(devId);
				comMap.put(comIdTemp, devListTemp);
			}
		}
		
		//②封装
		StringWriter sw = new StringWriter();
		JsonGenerator jsonGenerator = jacksonFactory.createGenerator(sw);
		//①
		jsonGenerator.writeStartObject();
		// "time" 
		jsonGenerator.writeStringField("time", String.valueOf(System.currentTimeMillis()));
		// "type"
		jsonGenerator.writeStringField("type", "7");
		// "ctrl_key" : false
		jsonGenerator.writeStringField("ctrl_key", sessionId);
		// "data"
		jsonGenerator.writeFieldName("data");
		//② [
		jsonGenerator.writeStartArray();
		Set<Entry<String, List<String>>> entrySet = comMap.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			// ③{
			jsonGenerator.writeStartObject();
			//TODO 数采内部强限制了，只能是com1或者com2
			jsonGenerator.writeStringField("com","com" + entry.getKey());
			jsonGenerator.writeFieldName("devs");
			
			List<String> devList = entry.getValue();
			String comSalveId = null;
			// ④[
			jsonGenerator.writeStartArray();
			
			Set<Entry<String, String>> guidEntryTemp = null;
			for (String devId : devList) {
				//{
				jsonGenerator.writeStartObject();
				comSalveId = DeviceCache.devId2slaveIdMap.get(devId);
				
				jsonGenerator.writeStringField("id",comSalveId.substring(comSalveId.lastIndexOf("_") + 1));
				jsonGenerator.writeFieldName("regs");
				//TODO ⑤中心一层{
				jsonGenerator.writeStartObject();
				guidEntryTemp = ykYtMap.get(devId).entrySet();
				
				String addrHTemp = null;
				for (Entry<String, String> guidEntry : guidEntryTemp) {
					addrHTemp = CacheMappingUtil.guidAddrHigh(guidEntry.getKey());
					// TODO 一般不考虑一个信号下发多个地址,如果有，低位地址就是高位地址加1
					// 处于正常值范围就反向计算增益然后封装下发
					if (addrHTemp != null && DataProcessingUtil.valLegitimation(guidEntry.getKey(),Double.parseDouble(guidEntry.getValue()))) {

						// 反向增益和修正计算
						String valueTemp = DataProcessingUtil.gainFactorReverse(guidEntry.getValue(), guidEntry.getKey());
						double valueTarget = Double.valueOf(valueTemp);
						
						// TODO 长度进制处理，默认16位，目前针对单寄存器，只截取最后四位
						String hexString = RadixProcessUtil.Long2HexStr((long) valueTarget);
						int length = hexString.length();
						hexString =  hexString.substring(length - 4);
						// 封装
						//jsonGenerator.writeStringField(addrHTemp, hexString);
						//2018.06.01为适配数采，封装值前加功能码
						String functionCode = CacheMappingUtil.guid2FunctionCode(guidEntry.getKey());
						if (functionCode != null) {
							jsonGenerator.writeStringField(addrHTemp, functionCode + hexString);
						}
					}
				}
				//}
				jsonGenerator.writeEndObject();
				//}
				jsonGenerator.writeEndObject();
			}
			// ]
			jsonGenerator.writeEndArray();
			// }
			jsonGenerator.writeEndObject();
			
		}
		// ]
		jsonGenerator.writeEndArray();
		// }
		jsonGenerator.writeEndObject();
		jsonGenerator.close();
		
		return sw.toString();
	}
	
	
	/**
	  * @Title:  force2obj 
	  * @Description:  强转类型，符合要求就分类存库
	  * @param mapperName
	  * @param deviceId
	  * @param signalGuidMap
	  * @param dataStoreRecords: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月27日下午2:10:08
	 */
	@SuppressWarnings("unchecked")
	public static  void force2obj(String mapperName,String devIdTemp,Map<String, String>signalGuidMap,Object dataStoreRecords,boolean fixedTimeFlag,String data_time) {
		
		//不是固定入库时刻就替换成固定入库时刻
		if (!fixedTimeFlag) {
			signalGuidMap.put("data_time",data_time);
		}
		
		if ("dataElectricMeterMapper".equals(mapperName)) {
			List<DataElectricMeter> dataStoreList = (List<DataElectricMeter>) dataStoreRecords;
			//非空判断
			if (dataStoreList != null) {
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
		}else if ("dataCentralInverterMapper".equals(mapperName)) {
			List<DataCentralInverter> dataStoreList = (List<DataCentralInverter>) dataStoreRecords;
			//非空判断
			if (dataStoreList != null) {
				//获取匹配的数据中最新的记录
				DataCentralInverter dataCentralInverter = dataStoreList.get(0);
				//关键数据(指标可能会变)为空就更新，对逆变器，有功功率，日发电量和总发电量是关键数据
				if (!StringUtils.isNoneBlank(dataCentralInverter.getActivePower().toString(),dataCentralInverter.getDailyEnergy().toString(),dataCentralInverter.getTotalEnergy().toString())) {
					//更新
					DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
					logger.debug("success to update history message");
				}
			} else {
				//没查询到匹配设备id和 data_time的记录，直接入库
				DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
				logger.debug("success to store history message");
			}
		}else if ("dataStringInverterMapper".equals(mapperName)) {
			List<DataStringInverter> dataStoreList =  (List<DataStringInverter>) dataStoreRecords;
			//非空判断
			if (dataStoreList != null) {
				//获取匹配的数据中最新的记录
				DataStringInverter dataStringInverter = dataStoreList.get(0);
				//关键数据(指标可能会变)为空就更新，对逆变器，有功功率，日发电量和总发电量是关键数据
				if (!StringUtils.isNoneBlank(dataStringInverter.getActivePower().toString(),dataStringInverter.getDailyEnergy().toString(),dataStringInverter.getTotalEnergy().toString())) {
					//更新
					DataSaveTask.dataRecoveryAndUpdate(devIdTemp, signalGuidMap);
					logger.info("success to update history message");
				}
			} else {
				//没查询到匹配设备id和 data_time的记录，直接入库
				DataSaveTask.dataRecoveryAndStore(devIdTemp, signalGuidMap);
				logger.info("success to store history message");
			}
			
		}else if ("dataTransformerMapper".equals(mapperName)) {
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
		}
		//TODO 新增设备，如PCS,BMS,箱式变压器数据入库，关键指标确定后增加
		
		
		
		logger.debug(devIdTemp + ":" + signalGuidMap.toString());
	}
	
	
	/**
	  * @Title:  storeDataOfRecovery 
	  * @Description: 补采数据入库,新数采不用考虑是否是整点数据
	  * @param dataTime
	  * @param signalGuidMap
	  * @param devIdTemp
	  * @param data_time
	  * @param mapperName: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月27日下午3:55:19
	 */
	public static void storeDataOfRecovery(Long dataTime,Map<String,String> signalGuidMap,String devIdTemp,String data_time,String mapperName) {
		logger.info("start to store or update the records of history message");
		//判断是否存库
		boolean fixedTimeFlag = true;
		// 获取数据存库记录
		Object dataStoreRecords = dataSaveService.selectByIdAndDataTime(mapperName, Integer.valueOf(devIdTemp),dataTime);

		// 根据mapperName 分类强转,符合要求就存库
		force2obj(mapperName, devIdTemp, signalGuidMap, dataStoreRecords, fixedTimeFlag, data_time);
	}
	
	
	public static void ycSignalValueProcess(String signalGuid,String signalValue,String detailSignalVal,Map<String, String> signalGuidMap,String deviceId,List<DataYx> yc4yxDataList,String data_time) {
		// TODO(待测试) ①信号点值特殊处理方法(前多少位整数，后多少位小数)(拿到方法，遥测参数默认)②判断是否是遥测表遥信(比如中间几位)(拿到参数，起始位，读取终止位)
		String processMethod = CacheMappingUtil.signalGuid2processMethod(signalGuid);
		//detailMap中放的是起始位和读取终止位
		Map<Integer, Integer> detailMap = CacheMappingUtil.signalGuid2processDetail(signalGuid);
		if (processMethod != null ) {
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
				
			}else {
				String  signalValueTemp = ReflectionUtil.specialProcess4Parse(processMethod, signalValue);
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
			Integer signalVal4compare = (int)(signalVal);
			signalValTarget = String.valueOf(signalVal4compare);
			//处于正常值范围就存redis，判断使用原始值，存库用处理后整型  
			if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
				//变位信息对比
				String latestSignalVal = signalGuidMap.get(signalGuid);
				
				//遥信有变位就存库,新数采增加yc4yxDataList判断,全量数据0遥信不入库yc4yxDataList给null,变位遥信数据1才入库
				if (yc4yxDataList != null && latestSignalVal != null && !signalValTarget.equals(latestSignalVal)) {
					//判断
					//关联表查到yx_id存库
					Integer yxId = CacheMappingUtil.signalGuid2yxId(signalGuid, signalValTarget);
					if (yxId != null) {
						//正常遥信status传0或1,其他的传1
						if (signalVal4compare > 1) {
							yc4yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time),signalValTarget,"1","0"));
						} else {
							yc4yxDataList.add(new DataYx(Integer.parseInt(deviceId), yxId, Long.parseLong(data_time),signalValTarget,signalValTarget,"0"));
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
				signalGuidMap.put(signalGuid,signalValTarget);
			}
			
		} else {
			//保留六位小数存库，光伏计算使用四位，前台显示两位
			signalValTarget = decimalFormat.format(signalVal);
			//处于正常值范围就存redis，判断使用原始值，存库用保留六位小数值
			if (DataProcessingUtil.valLegitimation(signalGuid, signalVal)) {
				signalGuidMap.put(signalGuid,signalValTarget);
			}
		}
		
		
	}

	/**
	  * @Title:  setMap2JsonStr 
	  * @Description:  TODO 新数采下发点表的报文封装
	  * @param collSn 数采SN
	  * @param dataModelSetMap 封装的com口,slaveId和点表信息
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月27日下午4:30:42
	 */
	public static String setMap2JsonStr(String collSn, Map<Integer, Map<String, Integer>> dataModelSetMap) throws IOException {

		//②封装
		StringWriter sw = new StringWriter();
		JsonGenerator jsonGenerator = jacksonFactory.createGenerator(sw);
		//①
		jsonGenerator.writeStartObject();
		// "time" 
		jsonGenerator.writeStringField("time", String.valueOf(System.currentTimeMillis()));
		// "type"
		jsonGenerator.writeStringField("type", "6");
		// "data"
		jsonGenerator.writeFieldName("data");
		//② [
		jsonGenerator.writeStartArray();
		Set<Entry<Integer, Map<String, Integer>>> entrySet = dataModelSetMap.entrySet();
		for (Entry<Integer,  Map<String, Integer>> entry : entrySet) {
			// ③{
			jsonGenerator.writeStartObject();
			//TODO 数采内部强限制名称只能是com1或com2
			jsonGenerator.writeStringField("com","com" + entry.getKey().toString());
			jsonGenerator.writeFieldName("devs");
			
			Map<String, Integer> slaveModelMap = entry.getValue();
			// ④[
			jsonGenerator.writeStartArray();
			
			Set<Entry<String, Integer>> slaveModelEntry = slaveModelMap.entrySet();
			for (Entry<String, Integer> slavemodel : slaveModelEntry) {
				//{
				jsonGenerator.writeStartObject();
				jsonGenerator.writeStringField("id",slavemodel.getKey());
				//校验码模式，支持CRC/LRC
				jsonGenerator.writeStringField("verify_mode","CRC");
				//校验码字节序，LH低前高后，HL：高前低后
				jsonGenerator.writeStringField("verify_order","HL");
				
				//分别为：前四位为寄存器地址，中间两位为功能码最后一位是否变位上送（支持变位上送为1，否则为0）
				StringBuffer sbf = new StringBuffer(50);
				//TODO 查询点表，封装值
				Set<String> addrReadSet = CacheMappingUtil.addrReadSet(slavemodel.getValue());
				if (addrReadSet != null) {
					for (String readStr : addrReadSet) {
						sbf.append(readStr + ",");
					}
					//TODO 去掉最后一个逗号
					sbf.deleteCharAt(sbf.length()-1);
				}
				
				jsonGenerator.writeStringField("coll", sbf.toString());
				
				//}
				jsonGenerator.writeEndObject();
			}
			
			// ]
			jsonGenerator.writeEndArray();
			// }
			jsonGenerator.writeEndObject();
			
		}
		// ]
		jsonGenerator.writeEndArray();
		// }
		jsonGenerator.writeEndObject();
		jsonGenerator.close();
		
		return sw.toString();
	}
}
