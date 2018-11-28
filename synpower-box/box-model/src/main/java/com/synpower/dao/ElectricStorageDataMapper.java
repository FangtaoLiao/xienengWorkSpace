package com.synpower.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.ElectricStorageData;
import com.synpower.bean.EnergyStorageData;

@Repository
public interface ElectricStorageDataMapper {

	/**
	 * @Author lz <<<<<<< .working
	 * @Description 能效 获取时间段内的用电量 electric_storage_data
	 * @Date 9:18 2018/8/23
	 * @Param []
	 * @return java.util.Map
	 **/
	public Map<String, Object> getEMonByplantIdAndTime(Map paramMap);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月22日下午4:28:41
	 * @Description -_-能效系统 获取峰谷电量统计
	 * @param map
	 * @return
	 */
	public Map<String, Double> getElecKPI(Map map);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月24日上午11:20:35
	 * @Description -_-得到无功能量
	 * @param map
	 * @return
	 */
	public Map<String, Object> getTotalEnergy(Map map);

	/**
	 * @Author lz
	 * @Description 通过电站id，起始时间和表达式获取按日月年分组的用电及电费
	 * @Date 11:39 2018/8/27
	 * @Param [parMap]
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	public List<Map<String, Object>> getEUKByPlantIdAndTime(Map parMap);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月29日下午2:44:02
	 * @Description -_-删除
	 * @param map
	 * @return
	 */
	public int deteleDataForStotage(Map map);
	
	/**
	 * 
	 * @author ybj
	 * @date 2018年8月29日下午2:46:45
	 * @Description -_-
	 * @param list
	 * @return
	 */
	public int insertDataForStotage(List<ElectricStorageData> list);

}