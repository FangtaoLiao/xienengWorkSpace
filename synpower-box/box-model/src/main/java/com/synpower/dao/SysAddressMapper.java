package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysAddress;

@Repository
public interface SysAddressMapper {

	public SysAddress getBackInfo();
	
	public List<SysAddress> getNextInfo(String dId);

	public List<SysAddress> getPlantId(String dId);
	
	public List<SysAddress> getAdrForScreen(List<Integer> list);

	public List<Integer> getPids(Map<String, Object> map);

	public List<Integer> getFid(String areaId);

	public String getFname(List<Integer> fid);
	
	public String getRoot();

	public List<Map<String, Object>> getPcount(Map<String, Object> map);

	public List<Integer> getActualids(Map<String, Object> map);

	public Map<String, Object> getNextcount(List<Integer> acids);

	public List<SysAddress> getNextCount(Map<String, Object> map);

	public String getFix(String areaId);

	public List<SysAddress> getNext2Count(Map<String, Object> map);

	public String getPnames(Map<String, Object> areaMap);

	public List<Integer> getpidss(String areaName);

	public List<Integer> getCurrId(String fname);

	public List<Map<String, Object>> getEachCount(Map<String, Object> eMap);

	public List<String> getCurrId2(String name);

	public List<SysAddress> getUniqueS(String fname);

	public Integer getTopArea(String areaId);

	public String getDetailByName(String fname);

	public List<SysAddress> getAreaDetail(Map<String, Object> mMap);

	public List<Integer> getChildPids(Map<String, Object> mMap);

	public List<Map<String, Object>> getLastArea(Map<String, Object> eMap);
	
	public SysAddress getAreaByName(String name);
	
	public List<PlantInfo> getPlantsByArea(Map map);
	
	public List<Integer>getPlantsIdByArea(Map map);

	public List<String> getAreaNames(Map<String, Object> mm);

	public List<PlantInfo> getSubPlants2(Map<String, Object> map);

	public List<SysAddress> getAllAreas();

	public List<Integer> getActualid(String plantArea);

	public List<PlantInfo> getSubwithCondition(Map<String, Object> map);

	public List<Integer> getChildPidsws(Map<String, Object> map);

	public String getFidByLocation(String kid);

	public String getFnameById(Integer fatherId);

	public List<Integer> getFid2(Map<String, Object> areaEleMap);

	public List<Integer> getFatherPlants(Integer fatherId);

	public List<Integer> getFidByDetail(Map<String, Object> areaMap);
	
	public List<SysAddress> getAreaByPlantId(List list);
	
	public List<PlantInfo> getPlantsByAreaName(String name);

	public String getDetailById(Map<String, Object> areaMap);

	public List<Integer> getChildPids2(Map<String, Object> mMap);
	/** 
	  * @Title:  getPlantArea 
	  * @Description:  获取电站的各级经纬度 
	  * @param plantId
	  * @return: List<SysAddress>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月16日下午4:42:09
	*/ 
	public List<SysAddress>getPlantArea(int plantId);
	/** 
	  * @Title:  delPlantAddr 
	  * @Description:  删除电站各级经纬度 
	  * @param plantId
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日上午11:32:08
	*/ 
	public int delPlantAddr(int plantId);
	/** 
	  * @Title:  insertPlantAddr 
	  * @Description:  插入电站的各级经纬度 
	  * @param sysAddress
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日下午2:11:31
	*/ 
	public int insertPlantAddr(SysAddress sysAddress);

	/** 
	  * @Title:  getHasChile 
	  * @Description:  TODO 
	  * @param areaEleMap
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月26日上午9:49:14
	*/
	public List<String> getHasChile(Map<String, Object> areaEleMap);

	/** 
	  * @Title:  jugeHasChild 
	  * @Description:  TODO 
	  * @param areaEleMap
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月26日上午9:51:36
	*/
	public String getJugeHasChild(Map<String, Object> areaEleMap);

	/** 
	  * @Title:  getCurrId3 
	  * @Description:  TODO 
	  * @param location
	  * @return: List<String>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月26日下午3:41:05
	*/
	public List<String> getCurrId3(String location);

	public List<Integer> getChildPids3(Map<String, Object> mMap);
}