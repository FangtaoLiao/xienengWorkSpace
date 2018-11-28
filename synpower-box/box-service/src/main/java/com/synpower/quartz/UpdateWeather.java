package com.synpower.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysWeather;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysWeatherMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.util.ApiUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.Util;
import com.synpower.util.WeatherUtil;

/*****************************************************************************
 * @Package: com.synpower.quartz
 * ClassName: UpdateWeather
 * @Description: 用于实时更新天气
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年1月22日下午5:29:35   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Component
public class UpdateWeather {
	private Logger logger = Logger.getLogger(UpdateWeather.class);
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired 
	private ConfigParam configParam;
	@Autowired
	private SysWeatherMapper weatherMapper;
	public void updateWeather() throws ServiceException{
		logger.info("更新天气预报开始");
		List<PlantInfo>plantList=plantMapper.getPlantList();
		if (Util.isNotBlank(plantList)) {
			List<PlantInfo> newPlantList = new ArrayList<PlantInfo>();
			int index = 0;
			for (int i = 0, size = plantList.size(); i < size; i++) {
				index++;
				newPlantList.add(plantList.get(i));
				if(index == 8){
					updatePlantsWeather(newPlantList);
					index = 0;
					newPlantList.clear();
				}
			}
			if(plantList.size() % 8 != 0){
	            int a = plantList.size() / 8;
	            List<PlantInfo> newList = new ArrayList<PlantInfo>();
	            for (int i = a*8, size = plantList.size(); i < size; i++) {
	                newList.add(plantList.get(i));
	            }
	            updatePlantsWeather(newList);
	        }
		}
	}
	
	@Transactional
	public void updatePlantsWeather(List<PlantInfo>plantList) throws ServiceException{
		StringBuffer buffer=new StringBuffer();
		List<Integer>list=new ArrayList<>(plantList.size());
		for (int i = 0; i < plantList.size(); i++) {
			PlantInfo plant=plantList.get(i);
			list.add(plant.getId());
			buffer.append(plant.getLoaction()).append("|");
		}
		buffer=buffer.deleteCharAt(buffer.lastIndexOf("|"));
		Map<String, Object>weathersMap=ApiUtil.getWeatherByPlantId(buffer.toString(),configParam.getBaiduAppAk());
		String statu=String.valueOf(weathersMap.get("status"));
		if ("success".equalsIgnoreCase(statu)) {
			long delTime=Util.getBeforDayMin(0);
			long time=System.currentTimeMillis();
			List<Map<String, Object>>result=(List<Map<String, Object>>) weathersMap.get("results");
			Map<String, Object>param=new HashMap<>(2);
			param.put("list", list);
			param.put("dataTime", delTime);
			weatherMapper.delPlantTodayWeather(param);
			logger.info(StringUtil.appendStr("清除历史天气预报成功","电站集合",list));
			for (int i = 0; i < result.size(); i++) {
				List<Map<String, String>>teMaps=(List<Map<String, String>>) result.get(i).get("weather_data");
				updatePlantWeather(teMaps,list,time,i);
			}
			logger.info("更新天气预报完成");
		}else{
			logger.error(StringUtil.appendStr("更新天气预报失败",weathersMap));
		}
	}
	@Transactional
	public void updatePlantWeather(List<Map<String, String>> teMaps,List<Integer>list,long time,int index) {
		int plantId=list.get(index);
		for (int i = 0; i < 3; i++) {
			SysWeather weather=new SysWeather();
			Map<String, String>weatherMap=teMaps.get(i);
			if (i==0) {
				String weatherPic=weatherMap.get("dayPictureUrl");
				weatherPic="/images/weather"+weatherPic.substring(weatherPic.lastIndexOf("/"));
				weather.setDataTime(time);
				weather.setPlantId(plantId);
				weather.setDayPictureUrl(weatherPic);
				weather.setWind(weatherMap.get("wind"));
				weather.setWeather(weatherMap.get("weather"));
				String temperature=weatherMap.get("temperature");
				temperature=temperature.substring(0, temperature.lastIndexOf("℃"));
				weather.setTemperature(temperature);
				String currentTemp=weatherMap.get("date");
				String currentTemp2=StringUtils.split(currentTemp,"：")[1];
				currentTemp2=currentTemp2.substring(0, currentTemp2.lastIndexOf("℃"));
				weather.setCurrentTemperature(currentTemp2);
			}else{
				String weatherPic=weatherMap.get("dayPictureUrl");
				weatherPic="/images/weather"+weatherPic.substring(weatherPic.lastIndexOf("/"));
				weather.setDataTime(Util.getBeforDayMin(-i));
				weather.setPlantId(plantId);
				weather.setDayPictureUrl(weatherPic);
				weather.setWind(weatherMap.get("wind"));
				weather.setWeather(weatherMap.get("weather"));
				String temperature=weatherMap.get("temperature");
				temperature=temperature.substring(0, temperature.lastIndexOf("℃"));
				weather.setTemperature(temperature);
			}
			weatherMapper.insert(weather);
		}
	}
}
