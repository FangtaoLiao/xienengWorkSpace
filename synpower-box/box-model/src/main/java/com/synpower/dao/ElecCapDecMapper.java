package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.ElecCapDec;

public interface ElecCapDecMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(ElecCapDec record);

	int insertSelective(ElecCapDec record);

	ElecCapDec selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ElecCapDec record);

	int updateByPrimaryKey(ElecCapDec record);

	/**
	 * @Author lz
	 * @Description 查询该公司一年的申报
	 * @Date 16:10 2018/8/23
	 * @Param [map]
	 * @return java.util.Map
	 **/
	Map<String, Object> selectByPlantAndYear(Map map);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月27日下午2:47:47
	 * @Description -_-得到某平台的在某几年内的申报容量
	 * @param map
	 * @return
	 */
	List<ElecCapDec> getYearCapByPlantId(Map map);
	/**
	 * @Author lz
	 * @Description 查询所有数据
	 * @Date 12:26 2018/8/28
	 * @Param []
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	List<Map<String, Object>>selectAll();
}