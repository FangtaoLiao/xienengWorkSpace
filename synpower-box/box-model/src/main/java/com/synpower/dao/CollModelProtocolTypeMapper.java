package com.synpower.dao;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollModelProtocolType;
@Repository
public interface CollModelProtocolTypeMapper {

	public String getIdByName(String connProtocol);
	
}