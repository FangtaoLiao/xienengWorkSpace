package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.CollJsonDatacollSet;

public interface CollJsonDatacollSetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollJsonDatacollSet record);

    int insertSelective(CollJsonDatacollSet record);

    CollJsonDatacollSet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CollJsonDatacollSet record);

    int updateByPrimaryKey(CollJsonDatacollSet record);
}