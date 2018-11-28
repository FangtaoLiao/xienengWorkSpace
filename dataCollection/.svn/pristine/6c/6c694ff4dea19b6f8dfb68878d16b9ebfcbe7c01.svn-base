package com.synpowertech.dataCollectionJar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.synpowertech.dataCollectionJar.domain.DataYx;

//@Service
public interface IDataSaveService {

	/**
	  * @Title:  yxDataSave 
	  * @Description:  变位遥信数据实时存库，批量操作
	  * @param yxDataList: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月6日下午3:00:53
	 */
	void yxDataSave(List<DataYx> yxDataList);

	/**
	  * @Title:  ycDataSave 
	  * @Description: 遥测数据定时存库
	  * @param obj
	  * @param mapperName: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日上午9:39:39
	 */
	void ycDataSave(Object obj, String mapperName);
	
	/**
	  * @Title:  ycDataUpdate 
	  * @Description: 遥测存库数据更新
	  * @param obj
	  * @param mapperName: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月27日上午11:23:22
	 */
	void ycDataUpdate(Object obj,String mapperName);
	
	/**
	  * @Title:  ycDataSaveBatch 
	  * @Description: 遥测数据定时批量存库
	  * @param ycRecordList
	  * @param mapperName: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月6日上午11:22:40
	 */
	<T> Integer ycDataSaveBatch(List<T> ycRecordList, String mapperName);
	
	/**
	  * @Title:  selectByIdAndDataTime 
	  * @Description: 根据数据时间和设备id查询数据存库记录
	  * @param mapperName
	  * @param id
	  * @param dataTime
	  * @return: Object
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月26日下午5:47:17
	 */
	Object selectByIdAndDataTime(String mapperName,Integer id,Long dataTime);

}