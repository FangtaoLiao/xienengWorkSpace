package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.SysWeather;

public interface SysWeatherMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysWeather record);

    int insertSelective(SysWeather record);

    SysWeather selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysWeather record);

    int updateByPrimaryKey(SysWeather record);
    public int delPlantTodayWeather(Map map);
    /** 
      * @Title:  getPlantNowWeather 
      * @Description:  获取电站实时信息 
      * @param map
      * @return: SysWeather
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月23日上午10:23:12
    */ 
    public SysWeather getPlantNowWeather(Map map);
    /** 
      * @Title:  getPlantThreeWeather 
      * @Description:  获取电站三天天气预报 
      * @param plantId
      * @return: List<SysWeather>
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月23日上午10:43:10
    */ 
    public List<SysWeather> getPlantThreeWeather(int plantId);
}