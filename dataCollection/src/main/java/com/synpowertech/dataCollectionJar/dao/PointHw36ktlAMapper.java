package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.PointHw36ktlA;

public interface PointHw36ktlAMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointHw36ktlA record);

    int insertSelective(PointHw36ktlA record);

    PointHw36ktlA selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointHw36ktlA record);

    int updateByPrimaryKey(PointHw36ktlA record);
}