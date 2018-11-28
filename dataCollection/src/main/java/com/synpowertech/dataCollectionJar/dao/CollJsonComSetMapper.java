package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollJsonComSet;

public interface CollJsonComSetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollJsonComSet record);

    int insertSelective(CollJsonComSet record);

    CollJsonComSet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CollJsonComSet record);

    int updateByPrimaryKey(CollJsonComSet record);
  
}