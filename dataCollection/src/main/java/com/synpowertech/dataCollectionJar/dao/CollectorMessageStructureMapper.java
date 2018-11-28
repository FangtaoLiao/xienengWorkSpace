package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.synpowertech.dataCollectionJar.domain.CollectorMessageStructure;

public interface CollectorMessageStructureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollectorMessageStructure record);

    int insertSelective(CollectorMessageStructure record);

    CollectorMessageStructure selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollectorMessageStructure record);

    int updateByPrimaryKey(CollectorMessageStructure record);
    
    //选择所有加载的设备
    List<CollectorMessageStructure> selectAll();
    
    //选择解析顺序在当前设备之前的
	List<CollectorMessageStructure> selectFormerParseDev(@Param("collectorSn") String collectorSn,@Param("parseOrder") Integer parseOrder);

	//选择采集器sn下设备关联信息
	List<CollectorMessageStructure> selectByCollSn(String collectorSn);
}