package com.synpower.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.PowerPrice;

@Repository
public interface PowerPriceMapper {
	public PowerPrice getPriceOfPlant(int plantId);
	public int insertPrice(Map map);
	public Map<String, Double> getPowerTotal(Map map);
	public Map<String, Double> getPowerPriceTotal(Map map);
	public String getPriceByPlant(String plantId);
	public int updatePrice(Map<String, Object> paraMap);
	/** 
	  * @Title:  updatePowerPrice 
	  * @Description:  更改电站电价 
	  * @param powerprice
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日下午2:48:05
	*/ 
	public int updatePowerPrice(PowerPrice powerprice);
}