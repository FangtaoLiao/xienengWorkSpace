package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.PlantInfo;

public interface PlantInfoMapper {
	PlantInfo findName(String id);

//	Integer updatePlantInfo(String str);

	/**
	  * @Title:  countPlantInfo
	  * @Description:  计算符合要求的电站总数
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:42:54
	*/
	public int countPlantInfo(Map map);
	/**
	  * @Title:  listPlantInfo
	  * @Description:  电站设置模块中电站列表显示
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:41:51
	*/
	public List<PlantInfo> listPlantInfo(Map parameterMap);
	/**
	  * @Title:  getPlantInfo
	  * @Description:  电站设置模块中的电站基本信息
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:43:41
	*/
	public PlantInfo getPlantInfo(String id);
	/**
	  * @Title:  updatePlantInfo
	  * @Description:  电站设置模块中的修改电站信息
	  * @param str
	  * @return
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:44:37
	*/
	public int updatePlantInfo(PlantInfo plant);
	public int insertPlantInfo(PlantInfo plant);

	/**
	  * @Title:  getPlantTotal
	  * @Description:  查询符合条件的电站总数
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日上午9:53:10
	*/
	public int getPlantTotal(Map map);
	/**
	  * @Title:  getTotalCapacity
	  * @Description:  查询符合条件的装机总数
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日上午9:53:30
	*/
	public String getTotalCapacity(Map map);
	public List<Map<String,Object>> listPlantDistribution(Map map);
	public List<Map<String,Object>> listPlantDistributionByPlantId(List list);
	public int updatePlantAddr(Map map);
	public int insertPlantAddr(Map map);
	public PlantInfo getPlantNameById(String id);

	/**
	  * @Title:  getPlantDistribution
	  * @Description:  TODO
	  * @param orgId
	  * @return: PlantInfo
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月8日下午3:00:43
	*/
	public PlantInfo getPlantDistribution(String orgId);

	/**
	  * @Title:  getTotalArea
	  * @Description:  TODO
	  * @param parameterMap
	  * @return: String
	  * @lastEditor:  SP0012
	*/
	public String getTotalArea(Map map);

	/**
	  * @Title:  getCapacityList
	  * @Description:  TODO
	  * @param parameterMap
	  * @return: List<Object>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月13日上午11:10:58
	*/
	public List<String> getCapacityList(Map<String, Object> map);

	/**
	  * @Title:  getAllPlant
	  * @Description:  TODO
	  * @param newMap
	  * @return: List<Map<String,Object>>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月15日下午2:14:26
	*/
	public List<Map<String, Object>> getAllPlant(Map<String, Object> newMap);

	/**
	  * @Title:  getIdByName
	  * @Description:  TODO
	  * @param plantName
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月16日上午11:07:34
	*/
	public String getIdByName(String plantName);
	public List<PlantInfo> getScreenPlantCount(Map map);

	/**
	  * @Title:  getAreaPlant
	  * @Description:  TODO
	  * @param map2
	  * @return: PlantInfo
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月17日下午2:05:41
	*/
	public List<PlantInfo> getAreaPlant(Map<String, Object> newMap);

	/**
	  * @Title:  getNowPlants
	  * @Description:  获取当前区域电站
	  * @param newMap
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月18日下午3:17:06
	*/
	public List<PlantInfo> getNowPlants(Map<String, Object> newMap);
	public Map<String, String> getCreateTime(Map map);
	public Map<String, String> getCreateTimeByMon(Map map);
	public Map<String, String> getCreateTimeByYear(Map map);
	public Map<String, String> getCreateTimeById(Map map);
	public List<PlantInfo> getNowPlants(List list);

	public List<Integer> getPlantsForOrg(List list);

	public List<Integer> getPList(Map<String, Object> searchMap);

	public List<Integer> getPListOutCon(Map<String, Object> searchMap);

	public List<PlantInfo> getEachPlantInfo(List<Integer> list);

	public String getLocation(String plantId);

	public List<PlantInfo>getPlantByName(Map map);

	public int getPlantCount2(List<Integer> list);

	public List<PlantInfo> getEachPlantInfo(Map<String, Object> paramMap);

	public List<PlantInfo> getCapacityUnit(List<String> list);

	/**
	  * @Title:  fuzzySearchPlant
	  * @Description: 电站模糊搜索 匹配范围(电站名字 电站联系人名字 电站联系人电话 电站地址)
	  * @param plantName
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月6日下午1:50:20
	*/
	public List<PlantInfo> getPlantsForSearch(Map map);

	public List<Integer> getPlantsForUser(List list);

	public List<PlantInfo>getPlantByIds(List list);
	public List<PlantInfo> getPlantByOrgId(String orgId);
	public PlantInfo getPlantById(String plantId);

	public List<PlantInfo> getPlantsByStatus(Map<String, Object> paramMap);

	public List<PlantInfo> getEachPlantInfoPage(Map<String, Object> paramMap);

	public List<PlantInfo> getPlantsByCon(Map<String, Object> parameterMap);

	public List<PlantInfo> getPlantsByStatus2(Map<String, Object> paramMap);
	public int getPlantsByStatus2Count(Map<String, Object> paramMap);
	public List<PlantInfo> getPlantByOrgIds(Map map);
	public String getPNameByPId(String pId);

	public String getPhoto(String plantId);

	public int insertPlantPhoto(Map parameterMap);

	public int deletePlantPhoto(Map parameterMap);

	public List<Map<String, Object>> getToutPlants();

	public String getPlantType(String string);
	/**
	  * @Title:  updatePlantInfoSelective
	  * @Description:  更改电站信息
	  * @param plantInfo
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日上午10:43:24
	*/
	public int updatePlantInfoSelective(PlantInfo plantInfo);
	/**
	  * @Title:  insertPlantSelective
	  * @Description:  新建电站
	  * @param plantInfo
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日下午5:46:04
	*/
	public int insertPlantSelective(PlantInfo plantInfo);
	public int updatePlantPhotoNull(String plantId);
	/**
	  * @Title:  getPlantList
	  * @Description:  获取所有未删除的电站
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月22日下午5:37:15
	*/
	public List<PlantInfo>getPlantList();

	/**
	  * @Title:  getPlantInfoByPage
	  * @Description:  TODO
	  * @param eleMap
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日下午2:01:40
	*/
	public List<PlantInfo> getPlantInfoByPage(Map<String, Object> eleMap);

	/**
	  * @Title:  getCapacityUnit2
	  * @Description:  电站容量单位
	  * @param list
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日上午11:33:44
	*/
	public List<PlantInfo> getCapacityUnit2(List<Integer> list);

	/**
	  * @Title:  getPListWithUnit
	  * @Description:  带单位kw查询电站id
	  * @param searchMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日上午11:45:21
	*/
	public Integer getPListWithUKW(Map<String, Object> searchMap);

	/**
	  * @Title:  getPListWithUMW
	  * @Description:  带单位Mw查询电站id
	  * @param searchMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日上午11:49:30
	*/
	public Integer getPListWithUMW(Map<String, Object> searchMap);

	/**
	  * @Title:  getPListWithUGW
	  * @Description:  TODO
	  * @param searchMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日下午1:34:46
	*/
	public Integer getPListWithUGW(Map<String, Object> searchMap);

	/**
	  * @Title:  getTotalCount
	  * @Description:  TODO
	  * @param paramMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月1日下午5:28:04
	*/
	public Integer getTotalCount(Map<String, Object> paramMap);

	/**
	  * @Title:  getPlantByOrgIdTree
	  * @Description:  TODO
	  * @param paraMap
	  * @return: List<PlantInfo>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月10日下午3:05:49
	*/
	public List<PlantInfo> getPlantByOrgIdTree(Map<String, Object> paraMap);

	/**
	  * @Title:  getPlantIdsByType
	  * @Description:  获取电站类型的id集合
	  * @return: Map<String,String>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月13日上午11:31:57
	*/
	public List<Map<Integer, String>> getPlantIdsByType();
	public List<PlantInfo> getPlantLocation(String location);
	public PlantInfo getPlantInfoByMap(Map<String, Object> ele);
	public Integer getPlantTypeB(Integer pId);
	public List<PlantInfo> getPlantsGroupByType(Map<String, Object> paramMap);
	public List<PlantInfo> getPlantsForSearchStoredOnly(Map<String, Object> map);
	public List<PlantInfo> getPlantByIdsStoredOnly(List<Integer> plantIdList);
	public List<PlantInfo> getPlantByIdsSolarOnly(List<Integer> plantIdList);
	public List<PlantInfo> getPlantsNoStatusAndType(Map<String, Object> paramMap);
	public int updatePlantPrice(PlantInfo plantInfo);

	public List<Integer> getDefautIdList();
}