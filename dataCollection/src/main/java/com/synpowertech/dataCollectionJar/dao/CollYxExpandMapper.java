package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollYxExpand;

public interface CollYxExpandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollYxExpand record);

    int insertSelective(CollYxExpand record);

    CollYxExpand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollYxExpand record);

    int updateByPrimaryKey(CollYxExpand record);
    
    //查询所有
    List<CollYxExpand> selectAll();
    
    //通过guid查询
    CollYxExpand selectBySignalGuid(String signalGuid);
}