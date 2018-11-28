package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.PointHw60ktlA;

public interface PointHw60ktlAMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointHw60ktlA record);

    int insertSelective(PointHw60ktlA record);

    PointHw60ktlA selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointHw60ktlA record);

    int updateByPrimaryKey(PointHw60ktlA record);
}