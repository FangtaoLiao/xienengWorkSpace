package com.synpower.dao;

import java.util.Map;

import com.synpower.bean.ElecFactorTable;

public interface ElecFactorTableMapper {
	
	int deleteByPrimaryKey(Integer id);

	int insert(ElecFactorTable record);

	int insertSelective(ElecFactorTable record);

	ElecFactorTable selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ElecFactorTable record);

	int updateByPrimaryKey(ElecFactorTable record);

	Map<String, Object> selectByFactor(Map map);

}