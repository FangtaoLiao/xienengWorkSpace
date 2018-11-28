package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollDeviceLocation;
import com.synpowertech.dataCollectionJar.domain.Device;

public interface DeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
    
    //查询所有
    List<Device> selectAll();
    
    //查询所有设备id用于定时判断redis中设备通讯状态缓存
    List<Integer> selectAllDevId();
    
    //查询所有数采,无关状态
    List<Device> selectAllDataColls();
    
    //查询所有数采,状态启用的
    List<Device> selectDataCollectors();
    
    //查询畅洋数采
    List<Device> selectOldDataColls();
    
    //查询新的自研数采
    List<Device> selectNewDataColls();
    
    //查询设备归属的电站经纬度实体
    List<CollDeviceLocation> selectDeviceLocation();
    
    //查询所有示例电站的数采id
    List<Integer> selectExampleColl();
    
    //查询所有示例电站的逆变器id
    List<Integer> selectExampleInverter();
    
    //查询所有使用60ktl_a点表的设备
    List<Integer> select60ktlA();

    //查询数采的所有子设备
	List<Integer> selectBySupId(Integer supId);
    
}