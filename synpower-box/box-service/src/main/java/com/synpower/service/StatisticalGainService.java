package com.synpower.service;

import com.synpower.bean.PlantInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface StatisticalGainService {
	/** 
	  * @Title:  getPowerAndDataTime 
	  * @Description:  将功率放到对应时间点位上 
	  * @param riseTime
	  * @param setTime
	  * @param deviceMap
	  * @param pId
	  * @return: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月17日下午2:39:15
	*/ 
	public Map<String, Object> getPowerAndDataTime(String riseTime,String setTime,Map<Integer, List<Integer>>deviceMap,String pId,int location);
	/** 
	  * @Title:  getNowDayIncome 
	  * @Description:  根据电站算当日收益 
	  * @param pId
	  * @return: double
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月17日下午2:39:03
	*/ 
	public Map getNowDayIncome(String pId);
	/** 
	  * @Title:  getPowerAndDataTime 
	  * @Description:  TODO 
	  * @param riseTime
	  * @param setTime
	  * @param deviceMap
	  * @param pId
	  * @param currTime
	  * @return: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月29日下午4:00:47
	*/ 
	public Map<String, Object> getPowerAndDataTime(String pId,String time,PlantInfo plantInfo)
													throws ParseException;
	/** 
	  * @Title:  getHistoryIncome 
	  * @Description:  电站历史收益 
	  * @param pId
	  * @return: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月8日下午4:35:16
	*/ 
	public Map<String, Object> getHistoryIncome(String pId);

	/**
	 * @Author lz
	 * @Description: 多加入日收益, 并格式化
	 * @param: [pId]
	 * @return: {java.util.Map<java.lang.String,java.lang.Object>}
	 * @Date: 2018/11/8 20:15
	 **/
	public Map<String, Object> getHistoryIncome2(String pId);
	/**
	 * 
	  * @Title:  getPowerAndDataTimeForMulti 
	  * @Description:  TODO 
	  * @param string
	  * @param string2
	  * @param deviceMap
	  * @param pId
	  * @param nowLocation
	  * @param nowTime
	  * @return: Map<String,Object>
	 * @throws ParseException 
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年4月3日上午9:44:24
	 */
	public List<Map<String, Object>> getPowerAndDataTimeForMulti(String string, String string2,Map<Integer, List<Integer>> deviceMap, String pId, int nowLocation, String nowTime) throws ParseException;
}
