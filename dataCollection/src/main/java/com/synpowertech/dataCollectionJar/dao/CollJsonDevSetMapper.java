package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollJsonComSet;
import com.synpowertech.dataCollectionJar.domain.CollJsonDevSet;

public interface CollJsonDevSetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollJsonDevSet record);

    int insertSelective(CollJsonDevSet record);

    CollJsonDevSet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CollJsonDevSet record);

    int updateByPrimaryKey(CollJsonDevSet record);
    //按采集器id查询新数采
    List<CollJsonDevSet> selectOrderByCollId();
}