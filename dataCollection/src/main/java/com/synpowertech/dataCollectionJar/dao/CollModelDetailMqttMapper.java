package com.synpowertech.dataCollectionJar.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synpowertech.dataCollectionJar.domain.AddressIdGuid;
import com.synpowertech.dataCollectionJar.domain.CollModelDetailMqtt;

public interface CollModelDetailMqttMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollModelDetailMqtt record);

    int insertSelective(CollModelDetailMqtt record);

    CollModelDetailMqtt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollModelDetailMqtt record);

    int updateByPrimaryKey(CollModelDetailMqtt record);
    
    //查询所有
    List<CollModelDetailMqtt> selectAll();
    
    //查询特定点表的所有遥控遥调
    List<CollModelDetailMqtt> selectAllYkYt(Integer dataModel);
    
    //查询所有遥测
    List<CollModelDetailMqtt> selectAllYc();
    
    //查询所有遥信
    List<CollModelDetailMqtt> selectAllYx();
    
    //查询所有遥控
    List<CollModelDetailMqtt> selectAllYk();
    
    //查询所有遥调
    List<CollModelDetailMqtt> selectAllYt();
    
    //通过guid查询
    CollModelDetailMqtt selectBySignalGuid(String signalGuid);

    //指定点表指定数采信号类型，指定寄存器地址顺序的signalGuid集合
	//List<String> selectGuidByParams(@Param("dataModel") Integer dataModel,@Param("signalType") String signalType,@Param("addressId") Integer addressId);
	
	//指定点表指定数采信号类型
	List<AddressIdGuid> selectByModelType(@Param("dataModel") Integer dataModel,@Param("signalType") String signalType);
}