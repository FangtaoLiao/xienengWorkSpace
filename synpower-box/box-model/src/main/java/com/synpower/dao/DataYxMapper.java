package com.synpower.dao;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataYx;
@Repository
public interface DataYxMapper {
	public DataYx getDataByYxId(String id);
}