package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.FieldSignalGuidMapping;

public interface FieldSignalGuidMappingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FieldSignalGuidMapping record);

    int insertSelective(FieldSignalGuidMapping record);

    FieldSignalGuidMapping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FieldSignalGuidMapping record);

    int updateByPrimaryKey(FieldSignalGuidMapping record);
    
    //查询所有
    List<FieldSignalGuidMapping> selectAll();
    
    //通过guid查询
    FieldSignalGuidMapping selectBySignalGuid(String signalGuid);
}