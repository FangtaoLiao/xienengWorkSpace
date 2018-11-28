package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.PointHw33ktlA;

public interface PointHw33ktlAMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(PointHw33ktlA record);

    int insertSelective(PointHw33ktlA record);

    PointHw33ktlA selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointHw33ktlA record);

    int updateByPrimaryKey(PointHw33ktlA record);
}