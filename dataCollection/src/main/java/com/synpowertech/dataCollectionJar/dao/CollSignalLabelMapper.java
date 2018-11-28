package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.CollSignalLabel;

public interface CollSignalLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollSignalLabel record);

    int insertSelective(CollSignalLabel record);

    CollSignalLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollSignalLabel record);

    int updateByPrimaryKey(CollSignalLabel record);
}