package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataBms;


@Repository
public interface DataBmsMapper {
	public List<DataBms> getDischargeCurrent(Map map);

	public Map<String, Object> getCapacityPercent(String plantId);
}