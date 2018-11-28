package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.PointHw36ktlCJson;

public interface PointHw36ktlCJsonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointHw36ktlCJson record);

    int insertSelective(PointHw36ktlCJson record);

    PointHw36ktlCJson selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointHw36ktlCJson record);

    int updateByPrimaryKey(PointHw36ktlCJson record);
}