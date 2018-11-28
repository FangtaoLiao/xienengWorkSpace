package com.synpower.dao;

import java.util.List;

import com.synpower.bean.SysPowerPrice;

public interface SysPowerPriceMapper {
	public int deleteByPrimaryKey(Integer id);

    public int insert(SysPowerPrice record);

    public  int insertSelective(SysPowerPrice record);

    public  SysPowerPrice selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(SysPowerPrice record);

    public int updateByPrimaryKey(SysPowerPrice record);
    
    public List<SysPowerPrice> getCnPlantPrice(String plantType);
    public SysPowerPrice getPlantPrice(String priceId);
}