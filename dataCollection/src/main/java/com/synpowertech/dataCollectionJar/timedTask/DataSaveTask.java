package com.synpowertech.dataCollectionJar.timedTask;

import com.synpowertech.dataCollectionJar.domain.*;
import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.initialization.SynConstant;
import com.synpowertech.dataCollectionJar.service.IDataSaveService;
import com.synpowertech.dataCollectionJar.utils.CacheMappingUtil;
import com.synpowertech.dataCollectionJar.utils.ReflectionUtil;
import com.synpowertech.dataCollectionJar.utils.Table2modelUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.Map.Entry;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.TimedTask
 * @ClassName: DataSaveTask
 * @Description: 定时将设备的遥测数据按设备的不同类别存入数据库不同表
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月6日下午2:53:35 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class DataSaveTask implements Job, InitializingBean {

	@Autowired
	IDataSaveService dataSaveServiceTemp;

	static IDataSaveService dataSaveService;

	private static final Logger logger = LoggerFactory.getLogger(DataSaveTask.class);

	// 批量插入队列
	List<DataCentralInverter> dataCentralInverterList;
	List<DataElectricMeter> dataElectricMeterList;
	List<DataStringInverter> dataStringInverterList;
	List<DataTransformer> dataTransformerList;
	List<DataCombinerBox> dataCombinerBoxList;
	// 储能设备
	List<DataBattery> dataBatteryList;
	List<DataBms> dataBmsList;
	List<DataPcs> dataPcsList;
	// 变压器
	List<DataBoxChange> dataBoxChangeList;
	List<DataEnvMonitor> dataEnvMonitorList;

	public void afterPropertiesSet() throws Exception {
		dataSaveService = dataSaveServiceTemp;
		logger.info("DataSaveTask inits succefully！");
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		Map<Integer, Integer> devId2plantIdMap = DeviceCache.devId2plantId;
		if (devId2plantIdMap == null) {
			return;
		}
		Set<Entry<Integer, Integer>> entrySet = devId2plantIdMap.entrySet();

		// 批量插入队列
		dataCentralInverterList = new ArrayList<DataCentralInverter>();
		dataElectricMeterList = new ArrayList<DataElectricMeter>();
		dataStringInverterList = new ArrayList<DataStringInverter>();
		dataTransformerList = new ArrayList<DataTransformer>();
		dataCombinerBoxList = new ArrayList<DataCombinerBox>();
		// 储能设备
		dataBatteryList = new ArrayList<DataBattery>();
		dataBmsList = new ArrayList<DataBms>();
		dataPcsList = new ArrayList<DataPcs>();
		// 箱式变压器
		dataBoxChangeList = new ArrayList<DataBoxChange>();
		//
		dataEnvMonitorList = new ArrayList<>();

		Long currentTimeMillis = System.currentTimeMillis();
		// 遍历设备
		for (Entry<Integer, Integer> entry : entrySet) {

			Object objTemp = null;
			Integer devIdTemp = entry.getKey();
			// 在当地日出日落时间之内的设备可继续入库流程(误差放宽一个小时)，
			// devId2SunRiseSet中只有逆变器id和汇流箱id,没查询到即为其他设备，也要入库
			if (!CacheMappingUtil.devId2SunRiseSet(devIdTemp, currentTimeMillis)) {
				continue;
			}

			// Map<signal_guid,value>
			Map<String, String> redisMap = getRedisData(devIdTemp);

			// 跳过空，跳过数据采集器
			if (redisMap == null || redisMap.size() == 2) {
				continue;
			}

			Set<Entry<String, String>> mappingEntry = redisMap.entrySet();
			if (mappingEntry == null) {
				continue;
			}
			logger.debug("redisMap：" + mappingEntry.toString());
			// 只创建一次实例
			boolean flag = true;
			String data_time = null;
			String modelName = null;
			for (Entry<String, String> entry2 : mappingEntry) {
				Map<String, String> signalGuid2modelAttr = null;

				// 是时间就保存起来,拿到对象再设置进去(一般是guid)
				String attribute = entry2.getKey();
				String valueTemp = entry2.getValue();
				if ("data_time".equals(attribute)) {
					data_time = valueTemp;
					continue;
				}

				// 跳过sys_time
				if ("sys_time".equals(attribute)) {
					continue;
				}
				// 跳过conn_status
				if ("conn_status".equals(attribute)) {
					continue;
				}

				if (flag) {
					// 通过mappingId获取tableName
					signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(attribute);

					if (signalGuid2modelAttr == null) {
						continue;
					}

					Set<String> keySet = signalGuid2modelAttr.keySet();
					logger.debug("keySET:" + keySet.toString());
					for (String str : keySet) {
						// 只会循环一次
						modelName = str;
					}

					// 跳过存库映射不存在的情况
					if (modelName == null) {
						continue;
					}
					// 获取实例
					objTemp = ReflectionUtil.instance4name(modelName);
					if (objTemp != null) {
						flag = false;
						// 新建实例后，将设备id,电站id,数据时间封装进对象
						ReflectionUtil.setAttr(objTemp, "deviceId", devIdTemp);
						ReflectionUtil.setAttr(objTemp, "plant", entry.getValue());

					}

					// 获取对应的属性，并赋值给对象
					Collection<String> values = signalGuid2modelAttr.values();
					for (String str : values) {
						// 只会遍历一次
						ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(valueTemp));
					}

				} else {
					// 通过mappingId获取tableName
					signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(attribute);
					if (signalGuid2modelAttr == null) {
						continue;
					}
					// 获取对应的属性，并赋值给对象
					Collection<String> values = signalGuid2modelAttr.values();
					for (String str : values) {
						// 只会遍历一次
						ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(valueTemp));
					}
				}
			}

			if (data_time != null && objTemp != null) {
				ReflectionUtil.setAttr(objTemp, "dataTime", Long.valueOf(data_time));
			}
			// 单个存库操作
			// 跳过存库映射不存在对象
			if (modelName == null) {
				continue;
			}
			// modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
			String mapperName = Table2modelUtil.lowerCaseFirstLatter(
					modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";
			logger.info("mapperName" + mapperName);
			// 遥测要插入数据库对象分类存入list
			classify4obj(objTemp, mapperName);

			// 存库List到达设定size就执行一次
			logger.debug(dataCentralInverterList.size() + "查看size" + dataCombinerBoxList.size());
			ycDataSave4List(dataCentralInverterList, dataElectricMeterList, dataStringInverterList, dataTransformerList,
					dataCombinerBoxList, dataBatteryList, dataBmsList, dataPcsList, dataBoxChangeList, dataEnvMonitorList);
		}
		// 最后再执行一次存库
		logger.debug(dataCentralInverterList.size() + "查看size" + dataCombinerBoxList.size());
		lastYcDataSave4List(dataCentralInverterList, dataElectricMeterList, dataStringInverterList, dataTransformerList,
				dataCombinerBoxList, dataBatteryList, dataBmsList, dataPcsList, dataBoxChangeList, dataEnvMonitorList);
		logger.info("dataSaveTask ends!");
	}

	/**
	 * @Title: classify4obj
	 * @Description: 遥测要插入数据库对象分类存入list
	 * @param obj
	 * @param     mapperName: void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年12月6日上午10:31:48
	 */
	public void classify4obj(Object obj, String mapperName) {
		if ("dataElectricMeterMapper".equals(mapperName)) {
			dataElectricMeterList.add((DataElectricMeter) obj);
		} else if ("dataCentralInverterMapper".equals(mapperName)) {
			dataCentralInverterList.add((DataCentralInverter) obj);
		} else if ("dataStringInverterMapper".equals(mapperName)) {
			dataStringInverterList.add((DataStringInverter) obj);
		} else if ("dataTransformerMapper".equals(mapperName)) {
			dataTransformerList.add((DataTransformer) obj);
		} else if ("dataCombinerBoxMapper".equals(mapperName)) {
			dataCombinerBoxList.add((DataCombinerBox) obj);
			// 储能设备
		} else if ("dataBatteryMapper".equals(mapperName)) {
			dataBatteryList.add((DataBattery) obj);
		} else if ("dataBmsMapper".equals(mapperName)) {
			dataBmsList.add((DataBms) obj);
		} else if ("dataPcsMapper".equals(mapperName)) {
			dataPcsList.add((DataPcs) obj);
			// 箱式变压器
		} else if ("dataBoxChangeMapper".equals(mapperName)) {
			dataBoxChangeList.add((DataBoxChange) obj);
		} else if ("dataEnvMonitorMapper".equals(mapperName)) {
			dataEnvMonitorList.add((DataEnvMonitor) obj);
		}
	}

	/**
	 * @Title: ycDataSave4List
	 * @Description: //存库List到达设定size就执行一次
	 * @param dataCentralInverterList
	 * @param dataElectricMeterList
	 * @param dataStringInverterList
	 * @param dataBoxChangeList
	 * @param dataCombinerBoxList
	 * @param dataPcsList
	 * @param dataBmsList
	 * @param dataBatteryList
	 * @param                         dataTransformerList: void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年12月6日上午11:33:58
	 */
	public void ycDataSave4List(
			List<DataCentralInverter> dataCentralInverterList,
			List<DataElectricMeter> dataElectricMeterList, List<DataStringInverter> dataStringInverterList,
			List<DataTransformer> dataTransformerList, List<DataCombinerBox> dataCombinerBoxList,
			List<DataBattery> dataBatteryList, List<DataBms> dataBmsList, List<DataPcs> dataPcsList,
			List<DataBoxChange> dataBoxChangeList, List<DataEnvMonitor> dataEnvMonitorList) {
		if (dataCentralInverterList.size() != 0 && dataCentralInverterList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataCentralInverterList, "dataCentralInverterMapper");
			dataCentralInverterList = new ArrayList<DataCentralInverter>();
		}
		if (dataElectricMeterList.size() != 0 && dataElectricMeterList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataElectricMeterList, "dataElectricMeterMapper");
			dataElectricMeterList = new ArrayList<DataElectricMeter>();
		}
		if (dataStringInverterList.size() != 0 && dataStringInverterList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataStringInverterList, "dataStringInverterMapper");
			dataStringInverterList = new ArrayList<DataStringInverter>();
		}
		if (dataTransformerList.size() != 0 && dataTransformerList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataTransformerList, "dataTransformerMapper");
			dataTransformerList = new ArrayList<DataTransformer>();
		}
		if (dataCombinerBoxList.size() != 0 && dataCombinerBoxList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataCombinerBoxList, "dataCombinerBoxMapper");
			dataCombinerBoxList = new ArrayList<DataCombinerBox>();
		}
		// 储能设备
		if (dataBatteryList.size() != 0 && dataBatteryList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataBatteryList, "dataBatteryMapper");
			dataBatteryList = new ArrayList<DataBattery>();
		}
		if (dataBmsList.size() != 0 && dataBmsList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataBmsList, "dataBmsMapper");
			dataBmsList = new ArrayList<DataBms>();
		}
		if (dataPcsList.size() != 0 && dataPcsList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataPcsList, "dataPcsMapper");
			dataPcsList = new ArrayList<DataPcs>();
		}
		// 箱式逆变器
		if (dataBoxChangeList.size() != 0 && dataBoxChangeList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataBoxChangeList, "dataBoxChangeMapper");
			dataBoxChangeList = new ArrayList<DataBoxChange>();
		}
		// 环境监测仪
		if (dataEnvMonitorList.size() != 0 && dataEnvMonitorList.size() % SynConstant.YcRecordListSize == 0) {
			dataSaveService.ycDataSaveBatch(dataEnvMonitorList, "dataEnvMonitorMapper");
			dataEnvMonitorList = new ArrayList<>();
		}
	}

	/**
	 * @Title: lastYcDataSave4List
	 * @Description: 存库List未到达设定size或者最后部分执行一次
	 * @param dataCentralInverterList
	 * @param dataElectricMeterList
	 * @param dataStringInverterList
	 * @param dataCombinerBoxList
	 * @param dataPcsList
	 * @param dataBmsList
	 * @param dataBatteryList
	 * @param dataBoxChangeList
	 * @param                         dataTransformerList: void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年12月6日下午1:49:08
	 */
	public void lastYcDataSave4List(
			List<DataCentralInverter> dataCentralInverterList,
			List<DataElectricMeter> dataElectricMeterList, List<DataStringInverter> dataStringInverterList,
			List<DataTransformer> dataTransformerList, List<DataCombinerBox> dataCombinerBoxList,
			List<DataBattery> dataBatteryList, List<DataBms> dataBmsList, List<DataPcs> dataPcsList,
			List<DataBoxChange> dataBoxChangeList, List<DataEnvMonitor> dataEnvMonitorList) {
		if (dataCentralInverterList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataCentralInverterList, "dataCentralInverterMapper");
			if (insertCount != dataCentralInverterList.size()) {
				logger.error("the number of inserting into data_central_inverter unsuccessfully is:{} ",
						dataCentralInverterList.size() - insertCount);
			}
			dataCentralInverterList.clear();
		}
		if (dataElectricMeterList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataElectricMeterList, "dataElectricMeterMapper");
			if (insertCount != dataElectricMeterList.size()) {
				logger.error("the number of inserting into data_electric_meter unsuccessfully is:{} ",
						dataElectricMeterList.size() - insertCount);
			}
			dataElectricMeterList.clear();
		}
		if (dataStringInverterList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataStringInverterList, "dataStringInverterMapper");
			if (insertCount != dataStringInverterList.size()) {
				logger.error("the number of inserting into data_string_inverter unsuccessfully is:{} ",
						dataStringInverterList.size() - insertCount);
			}
			dataStringInverterList.clear();
		}
		if (dataTransformerList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataTransformerList, "dataTransformerMapper");
			if (insertCount != dataTransformerList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataTransformerList.size() - insertCount);
			}
			dataTransformerList.clear();
		}
		if (dataCombinerBoxList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataCombinerBoxList, "dataCombinerBoxMapper");
			if (insertCount != dataCombinerBoxList.size()) {
				logger.error("the number of inserting into data_combiner_bix unsuccessfully is:{} ",
						dataCombinerBoxList.size() - insertCount);
			}
			dataCombinerBoxList.clear();
		}
		// 储能设备
		if (dataBatteryList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataBatteryList, "dataBatteryMapper");
			if (insertCount != dataBatteryList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataBatteryList.size() - insertCount);
			}
			dataBatteryList.clear();
		}
		if (dataBmsList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataBmsList, "dataBmsMapper");
			if (insertCount != dataBmsList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataBmsList.size() - insertCount);
			}
			dataBmsList.clear();
		}
		if (dataPcsList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataPcsList, "dataPcsMapper");
			if (insertCount != dataPcsList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataPcsList.size() - insertCount);
			}
			dataPcsList.clear();
		}
		// 箱式逆变器
		if (dataBoxChangeList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataBoxChangeList, "dataBoxChangeMapper");
			if (insertCount != dataBoxChangeList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataBoxChangeList.size() - insertCount);
			}
			dataBoxChangeList.clear();
		}

		// 环境检测仪
		if (dataEnvMonitorList.size() != 0) {
			Integer insertCount = dataSaveService.ycDataSaveBatch(dataEnvMonitorList, "dataEnvMonitorMapper");
			if (insertCount != dataEnvMonitorList.size()) {
				logger.error("the number of inserting into data_transformer unsuccessfully is:{} ",
						dataEnvMonitorList.size() - insertCount);
			}
			dataEnvMonitorList.clear();
		}
	}

	/**
	 * @Title: getRedisData
	 * @Description: 获取redis中设备参数和值得键值对
	 * @param deviceId
	 * @return: Map<String,String>
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月7日下午3:03:43
	 */
	private Map<String, String> getRedisData(Integer deviceId) {
		return JedisUtil.getMap(String.valueOf(deviceId));
	}

	/**
	 * @Title: dataRecoveryAndStore
	 * @Description: 数据补采入库方法，放在定时任务类中简化注入
	 * @param deviceId
	 * @param          signalGuidMap: void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年12月25日下午5:57:35
	 */
	public static void dataRecoveryAndStore(String deviceId, Map<String, String> signalGuidMap) {
		// 获取归属电站id
		Integer plantId = DeviceCache.devId2plantId.get(Integer.valueOf(deviceId));

		Object objTemp = null;

		// 跳过空，跳过数据采集器
		if (signalGuidMap == null || signalGuidMap.size() == 1) {
			return;
		}

		Set<Entry<String, String>> mappingEntry = signalGuidMap.entrySet();
		if (mappingEntry == null) {
			return;
		}
		logger.debug("signalGuidMap：" + mappingEntry.toString());

		boolean flag = true;
		String data_time = null;
		String modelName = null;
		for (Entry<String, String> entry2 : mappingEntry) {
			Map<String, String> signalGuid2modelAttr = null;

			// 是时间就保存起来,拿到对象再设置进去(一般是guid)
			if ("data_time".equals(entry2.getKey())) {
				data_time = entry2.getValue();
				continue;
			}

			// 跳过sys_time
			if ("sys_time".equals(entry2.getKey())) {
				continue;
			}
			// 跳过conn_status
			if ("conn_status".equals(entry2.getKey())) {
				continue;
			}

			if (flag) {
				// 通过mappingId获取tableName
				signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(entry2.getKey());

				if (signalGuid2modelAttr == null) {
					continue;
				}

				Set<String> keySet = signalGuid2modelAttr.keySet();
				logger.debug("keySET:" + keySet.toString());
				for (String str : keySet) {
					// 只会循环一次
					modelName = str;
				}

				// 跳过存库映射不存在的情况
				if (modelName == null) {
					continue;
				}
				// 获取实例
				objTemp = ReflectionUtil.instance4name(modelName);
				if (objTemp != null) {
					flag = false;
					// 新建实例后，将设备id,电站id,数据时间封装进对象
					ReflectionUtil.setAttr(objTemp, "deviceId", deviceId);
					ReflectionUtil.setAttr(objTemp, "plant", plantId);

				}

				// 获取对应的属性，并赋值给对象
				Collection<String> values = signalGuid2modelAttr.values();
				for (String str : values) {
					// 只会遍历一次
					ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(entry2.getValue()));

				}

			} else {
				// 通过mappingId获取tableName
				signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(entry2.getKey());
				if (signalGuid2modelAttr == null) {
					continue;
				}
				// 获取对应的属性，并赋值给对象
				Collection<String> values = signalGuid2modelAttr.values();
				for (String str : values) {
					// 只会遍历一次
					ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(entry2.getValue()));

				}
			}
		}

		if (data_time != null && objTemp != null) {
			ReflectionUtil.setAttr(objTemp, "dataTime", Long.valueOf(data_time));
		}
		// 单个存库操作
		// 跳过存库映射不存在对象
		if (modelName == null) {
			return;
		}
		// modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
		String mapperName = Table2modelUtil.lowerCaseFirstLatter(
				modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";

		// 遥测要插入数据库对象分类入库
		dataSaveService.ycDataSave(objTemp, mapperName);
		logger.info("dataRecovery of deviceId:{}", deviceId);
	}

	/**
	 * @Title: dataRecoveryAndUpdate
	 * @Description: 数据补采更新入库方法，放在定时任务类中简化注入
	 * @param deviceId
	 * @param          signalGuidMap: void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年12月25日下午5:57:35
	 */
	public static void dataRecoveryAndUpdate(String deviceId, Map<String, String> signalGuidMap) {
		// 获取归属电站id
		int plantId = DeviceCache.devId2plantId.get(deviceId);

		Object objTemp = null;

		// 跳过空，跳过数据采集器
		if (signalGuidMap == null || signalGuidMap.size() == 1) {
			return;
		}

		Set<Entry<String, String>> mappingEntry = signalGuidMap.entrySet();
		if (mappingEntry == null) {
			return;
		}
		logger.debug("signalGuidMap：" + mappingEntry.toString());

		boolean flag = true;
		String data_time = null;
		String modelName = null;
		for (Entry<String, String> entry2 : mappingEntry) {
			Map<String, String> signalGuid2modelAttr = null;

			// 是时间就保存起来,拿到对象再设置进去(一般是guid)
			if ("data_time".equals(entry2.getKey())) {
				data_time = entry2.getValue();
				continue;
			}

			// 跳过sys_time
			if ("sys_time".equals(entry2.getKey())) {
				continue;
			}
			// 跳过conn_status
			if ("conn_status".equals(entry2.getKey())) {
				continue;
			}

			if (flag) {
				// 通过mappingId获取tableName
				signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(entry2.getKey());

				if (signalGuid2modelAttr == null) {
					continue;
				}

				Set<String> keySet = signalGuid2modelAttr.keySet();
				logger.debug("keySET:" + keySet.toString());
				for (String str : keySet) {
					// 只会循环一次
					modelName = str;
				}

				// 跳过存库映射不存在的情况
				if (modelName == null) {
					continue;
				}
				// 获取实例
				objTemp = ReflectionUtil.instance4name(modelName);
				if (objTemp != null) {
					flag = false;
					// 新建实例后，将设备id,电站id,数据时间封装进对象
					ReflectionUtil.setAttr(objTemp, "deviceId", deviceId);
					ReflectionUtil.setAttr(objTemp, "plant", plantId);

				}

				// 获取对应的属性，并赋值给对象
				Collection<String> values = signalGuid2modelAttr.values();
				for (String str : values) {
					// 只会遍历一次
					ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(entry2.getValue()));

				}

			} else {
				// 通过mappingId获取tableName
				signalGuid2modelAttr = CacheMappingUtil.signalGuid2modelAttr(entry2.getKey());
				if (signalGuid2modelAttr == null) {
					continue;
				}
				// 获取对应的属性，并赋值给对象
				Collection<String> values = signalGuid2modelAttr.values();
				for (String str : values) {
					// 只会遍历一次
					ReflectionUtil.setAttr(objTemp, str, Float.parseFloat(entry2.getValue()));

				}
			}
		}

		if (data_time != null) {
			ReflectionUtil.setAttr(objTemp, "dataTime", Long.valueOf(data_time));
		}
		// 单个存库操作
		// 跳过存库映射不存在对象
		if (modelName == null) {
			return;
		}
		// modelName是全限定名，去掉com.synpowertech.domain.dataCollectionJar.
		String mapperName = Table2modelUtil.lowerCaseFirstLatter(
				modelName.substring("com.synpowertech.domain.dataCollectionJar.".length())) + "Mapper";

		// 遥测要更新数据库对象分类入库
		dataSaveService.ycDataUpdate(objTemp, mapperName);
		logger.info("dataRecovery of deviceId:{}", deviceId);
	}

}
