package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.CollYkytExpand;

public interface CollYkytExpandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollYkytExpand record);

    int insertSelective(CollYkytExpand record);

    CollYkytExpand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollYkytExpand record);

    int updateByPrimaryKey(CollYkytExpand record);
}