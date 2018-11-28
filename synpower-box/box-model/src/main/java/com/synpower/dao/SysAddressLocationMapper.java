package com.synpower.dao;

import com.synpower.bean.SysAddressLocation;

public interface SysAddressLocationMapper {
    /** 
      * @Title:  saveAddrLocation 
      * @Description:  添加数据 
      * @param sysAddressLocation
      * @return: int
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月15日下午4:15:14
    */ 
    public int saveAddrLocation(SysAddressLocation sysAddressLocation);
    
    /** 
      * @Title:  getLocationByName 
      * @Description:  根据地图名字获取经纬度 
      * @return: SysAddressLocation
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月15日下午4:15:40
    */ 
    public SysAddressLocation getLocationByName(String areaName);
}