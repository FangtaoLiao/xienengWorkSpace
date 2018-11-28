package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataBoxChange;

@Repository
public interface DataBoxChangeMapper {
	public List<DataBoxChange> getVoltageAndPower(Map map);
}