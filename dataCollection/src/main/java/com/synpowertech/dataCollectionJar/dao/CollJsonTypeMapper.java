package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.CollJsonType;

public interface CollJsonTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollJsonType record);

    int insertSelective(CollJsonType record);

    CollJsonType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CollJsonType record);

    int updateByPrimaryKey(CollJsonType record);
}