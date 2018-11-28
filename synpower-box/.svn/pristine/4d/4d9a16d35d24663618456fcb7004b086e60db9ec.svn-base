package com.synpower.dao;

import java.util.List;

import com.synpower.bean.SysPowerPriceDetail;

public interface SysPowerPriceDetailMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(SysPowerPriceDetail record);

	int insertSelective(SysPowerPriceDetail record);

	SysPowerPriceDetail selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysPowerPriceDetail record);

	int updateByPrimaryKey(SysPowerPriceDetail record);

	public List<SysPowerPriceDetail> getPlantPriceDetail(String priceId);

	public List<SysPowerPriceDetail> getPlantPriceDetailByPlantId(int plantId);
}