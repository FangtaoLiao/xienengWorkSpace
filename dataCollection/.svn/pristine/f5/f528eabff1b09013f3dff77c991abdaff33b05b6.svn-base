package com.synpowertech.dataCollectionJar.service.impl;

import com.synpowertech.dataCollectionJar.dao.*;
import com.synpowertech.dataCollectionJar.domain.*;
import com.synpowertech.dataCollectionJar.service.IDataSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
/**
 * ***************************************************************************
 * @Package: com.synpowertech.utils
 * @ClassName: DataSaveUtil
 * @Description: 变位遥信数据和定时遥测数据批量存库工具类，也提供存库数据查询更新等
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月5日下午12:00:29   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */

@Service
@Transactional(readOnly=true,propagation=Propagation.SUPPORTS)
public class DataSaveServiceImpl implements IDataSaveService {

	@Autowired
	public   DataYxMapper dataYxMapper;
	@Autowired
	public  DataElectricMeterMapper dataElectricMeterMapper;
	@Autowired
	public  DataCentralInverterMapper dataCentralInverterMapper;
	@Autowired
	public  DataStringInverterMapper dataStringInverterMapper;
	@Autowired
	public  DataTransformerMapper dataTransformerMapper;
	@Autowired
	public  DataCombinerBoxMapper dataCombinerBoxMapper;
	@Autowired
	public  DataSourceTransactionManager transactionManager ;
	//储能设备
	@Autowired
	public  DataBatteryMapper dataBatteryMapper;
	@Autowired
	public  DataBmsMapper dataBmsMapper;
	@Autowired
	public  DataPcsMapper dataPcsMapper;
	//箱式逆变器
	@Autowired
	public  DataBoxChangeMapper dataBoxChangeMapper;
	@Autowired
	public DataEnvMonitorMapper dataEnvMonitorMapper;

	/**
	  * @Title:  yxDataSave 
	  * @Description:  变位遥信数据批量存库 
	  * @param yxDataList: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:30:01
	 */
	@Transactional
	public void yxDataSave(List<DataYx> yxDataList) {
		/*for (DataYx dataYx : yxDataList) {
			saveYxData(dataYx);
		}*/
		dataYxMapper.insertBatch(yxDataList);
	}
	
	/**
	  * @Title:  saveYxData 
	  * @Description:  变为遥信存库实现 
	  * @param dataYx: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:30:01
	 */
	@Transactional
	public void saveYxData(DataYx dataYx) {
		dataYxMapper.insertSelective(dataYx);
	}
	

	/**
	  * @Title:  ycDataSave 
	  * @Description:  遥测定时存库实现 
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:30:01
	  * dataYxMapper;
	 */
	@Transactional
	public  void ycDataSave(Object obj,String mapperName) {
		if ("dataElectricMeterMapper".equals(mapperName)) {
			
			dataElectricMeterMapper.insertSelective((DataElectricMeter) obj);
		}else if ("dataCentralInverterMapper".equals(mapperName)) {
			
			dataCentralInverterMapper.insertSelective((DataCentralInverter) obj);
		}else if ("dataStringInverterMapper".equals(mapperName)) {
			
			dataStringInverterMapper.insertSelective((DataStringInverter) obj);
		}else if ("dataTransformerMapper".equals(mapperName)) {
			
			dataTransformerMapper.insertSelective((DataTransformer) obj);
		}else if ("dataCombinerBoxMapper".equals(mapperName)) {
			
			dataCombinerBoxMapper.insertSelective((DataCombinerBox) obj);
			//储能设备
		}else if ("dataBatteryMapper".equals(mapperName)) {
			
			dataBatteryMapper.insertSelective((DataBattery) obj);
		}else if ("dataBmsMapper".equals(mapperName)) {
			
			dataBmsMapper.insertSelective((DataBms) obj);
		}else if ("dataPcsMapper".equals(mapperName)) {
			
			dataPcsMapper.insertSelective((DataPcs) obj);
		}else if ("dataBoxChangeMapper".equals(mapperName)) {

			dataBoxChangeMapper.insertSelective((DataBoxChange) obj);
		} else if ("dataEnvMonitorMapper".equals(mapperName)) {

			dataEnvMonitorMapper.insertSelective((DataEnvMonitor) obj);
		}
	}
	
	/**
	  * @Title:  ycDataUpdate 
	  * @Description: 遥测存库数据更新
	  * @param obj
	  * @param mapperName: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月27日上午11:23:22
	 */
	public  void ycDataUpdate(Object obj,String mapperName) {
		if ("dataElectricMeterMapper".equals(mapperName)) {
			
			dataElectricMeterMapper.updateByPrimaryKeySelective((DataElectricMeter) obj);
		}else if ("dataCentralInverterMapper".equals(mapperName)) {
			
			dataCentralInverterMapper.updateByPrimaryKeySelective((DataCentralInverter) obj);
		}else if ("dataStringInverterMapper".equals(mapperName)) {
			
			dataStringInverterMapper.updateByPrimaryKeySelective((DataStringInverter) obj);
		}else if ("dataTransformerMapper".equals(mapperName)) {
			
			dataTransformerMapper.updateByPrimaryKeySelective((DataTransformer) obj);
		}else if ("dataCombinerBoxMapper".equals(mapperName)) {
			
			dataCombinerBoxMapper.updateByPrimaryKeySelective((DataCombinerBox) obj);
			//储能设备
		}else if ("dataBatteryMapper".equals(mapperName)) {
			
			dataBatteryMapper.updateByPrimaryKeySelective((DataBattery) obj);
		}else if ("dataBmsMapper".equals(mapperName)) {
			
			dataBmsMapper.updateByPrimaryKeySelective((DataBms) obj);
		}else if ("dataPcsMapper".equals(mapperName)) {
			
			dataPcsMapper.updateByPrimaryKeySelective((DataPcs) obj);
		}else if ("dataBoxChangeMapper".equals(mapperName)) {

			dataBoxChangeMapper.updateByPrimaryKeySelective((DataBoxChange) obj);
		} else if ("dataEnvMonitorMapper".equals(mapperName)) {

			dataEnvMonitorMapper.updateByPrimaryKeySelective((DataEnvMonitor) obj);
		}
	}
	
	/**
	  * @Title:  ycDataSaveBatch 
	  * @Description:  遥测定时批量存库实现 ,出错回滚二分插入
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午3:30:01
	  * dataYxMapper;
	 */
	@SuppressWarnings("unchecked")
	//@Transactional 手动commit,处理回滚的数据
	@Transactional (rollbackFor = { Exception.class })  
	public <T> Integer ycDataSaveBatch(List<T> ycRecordList, String mapperName) {
		Integer insertCount = 0;
		try {
			if ("dataElectricMeterMapper".equals(mapperName)) {
				insertCount = dataElectricMeterMapper.insertBatch((List<DataElectricMeter>) ycRecordList);
			} else if ("dataCentralInverterMapper".equals(mapperName)) {

				insertCount = dataCentralInverterMapper.insertBatch((List<DataCentralInverter>) ycRecordList);
			} else if ("dataStringInverterMapper".equals(mapperName)) {

				insertCount = dataStringInverterMapper.insertBatch((List<DataStringInverter>) ycRecordList);
			} else if ("dataTransformerMapper".equals(mapperName)) {

				insertCount = dataTransformerMapper.insertBatch((List<DataTransformer>) ycRecordList);
			} else if ("dataCombinerBoxMapper".equals(mapperName)) {
				
				insertCount = dataCombinerBoxMapper.insertBatch((List<DataCombinerBox>) ycRecordList);
				//储能设备
			}  else if ("dataBatteryMapper".equals(mapperName)) {

				insertCount = dataBatteryMapper.insertBatch((List<DataBattery>) ycRecordList);
			}  else if ("dataBmsMapper".equals(mapperName)) {

				insertCount = dataBmsMapper.insertBatch((List<DataBms>) ycRecordList);
			}  else if ("dataPcsMapper".equals(mapperName)) {

				insertCount = dataPcsMapper.insertBatch((List<DataPcs>) ycRecordList);
				//箱式逆变器
			}   else if ("dataBoxChangeMapper".equals(mapperName)) {

				insertCount = dataBoxChangeMapper.insertBatch((List<DataBoxChange>) ycRecordList);
			} else if ("dataEnvMonitorMapper".equals(mapperName)) {

				insertCount = dataEnvMonitorMapper.insertBatch((List<DataEnvMonitor>) ycRecordList);
		}
		} catch (Exception e) {
			e.printStackTrace();
			//手动回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			//二分思路处理正常数据入库
			if (ycRecordList.size() != 1) {
				List<T> ycRecordList1 = new ArrayList<T>();
				List<T> ycRecordList2 = new ArrayList<T>();
				
				ycRecordList1.addAll(ycRecordList.subList(0, ycRecordList.size()/2));
				ycRecordList2.addAll(ycRecordList.subList(ycRecordList.size()/2,ycRecordList.size()));
				Integer insertCount1 = ycDataSaveBatch(ycRecordList1, mapperName);
				Integer insertCount2 = ycDataSaveBatch(ycRecordList2, mapperName);
				insertCount = insertCount1 + insertCount2;
			} else {
				insertCount = 0;
			}
		}
		return insertCount;
	}
	
	/**
	  * @Title:  selectByIdAndDataTime 
	  * @Description: 查询数据库中相同设备id和数据时间的存库记录
	  * @param mapperName
	  * @param id
	  * @param dataTime
	  * @return: Object
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月26日下午5:40:05
	 */
	public  Object selectByIdAndDataTime(String mapperName,Integer id,Long dataTime) {
		Object obj = null;
		if ("dataElectricMeterMapper".equals(mapperName)) {
			obj = dataElectricMeterMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataCentralInverterMapper".equals(mapperName)) {
			obj = dataCentralInverterMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataStringInverterMapper".equals(mapperName)) {
			obj = dataStringInverterMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataTransformerMapper".equals(mapperName)) {
			obj = dataTransformerMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataCombinerBoxMapper".equals(mapperName)) {
			obj = dataCombinerBoxMapper.selectByIdAndDataTime(id, dataTime);
			//储能设备
		}else if ("dataBatteryMapper".equals(mapperName)) {
			obj = dataBatteryMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataBmsMapper".equals(mapperName)) {
			obj = dataBmsMapper.selectByIdAndDataTime(id, dataTime);
		}else if ("dataPcsMapper".equals(mapperName)) {
			obj = dataPcsMapper.selectByIdAndDataTime(id, dataTime);
			//箱式逆变器
		}else if ("dataBoxChangeMapper".equals(mapperName)) {
			obj = dataBoxChangeMapper.selectByIdAndDataTime(id, dataTime);
		} else if ("dataEnvMonitorMapper".equals(mapperName)) {
			obj = dataEnvMonitorMapper.selectByIdAndDataTime(id, dataTime);
		}
		return obj;
	}
	
}
