package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollectorMessageStructure;

@Repository
public interface CollectorMessageStructureMapper {
	/** 
	  * @Title:  delCollectorParse 
	  * @Description: 删掉与指定数采相关联的解析记录
	  * @param collectorId
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月16日上午10:49:14
	*/ 
	public int delCollectorParse(int collectorId);
	/** 
	  * @Title:  delAnyParse 
	  * @Description:  删掉指定数采的部分解析记录 
	  * @param collectorId
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月16日上午10:49:47
	*/ 
	public int delAnyParse(Map map);
	
	/** 
	  * @Title:  getDelDeviceIds 
	  * @Description:  获取指定数采被删除的设备 
	  * @param collectorId
	  * @return: List<Integer>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月16日上午11:24:42
	*/ 
	public List<Integer>getDelDeviceIds(int collectorId);
	/** 
	  * @Title:  getEachCount4Size 
	  * @Description:  yc、yx、yk、yt size 
	  * @param tMap
	  * @return: Map<String,Object>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月6日上午9:59:31
	*/
	public Map<String, Object> getEachCount4Size(Map<String, Object> tMap);
	/** 
	  * @Title:  addNewStructure 
	  * @Description:  新添解析数据 
	  * @param cms: void
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月6日上午10:31:00
	*/
	public Integer addNewStructure(CollectorMessageStructure cms);
	/** 
	  * @Title:  deleteParseInfoByCollid 
	  * @Description:  删除解析数据
	  * @param paraMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月7日上午11:16:04
	*/
	public Integer deleteParseInfoByCollid(Map<String, Object> paraMap);
	/** 
	  * @Title:  getEachCountforSize 
	  * @Description:  TODO 
	  * @param tMap
	  * @return: Map<String,Object>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月5日下午2:47:00
	*/
	public List<Map<String, Object>> getEachCountforSize(Map<String, Object> tMap);
	/** 
	  * @Title:  getPareseOrder 
	  * @Description:  TODO 
	  * @param id
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月6日下午4:53:10
	*/
	public String getPareseOrder(Integer id);
	/** 
	  * @Title:  delSubDeviceforParse 
	  * @Description:  TODO 
	  * @param delMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月7日上午11:43:55
	*/
	public Integer delSubDeviceforParse(Map<String, Object> delMap);
	/** 
	  * @Title:  updatePareseStatusCollectorChange 
	  * @Description:  TODO 
	  * @param paraMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月7日下午4:26:02
	*/
	public Integer updatePareseStatusCollectorChange(Map<String, Object> paraMap);
	/** 
	  * @Title:  updatePareseOnUpdate 
	  * @Description:  TODO 
	  * @param cms: void
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月7日下午6:01:37
	*/
	public Integer updatePareseOnUpdate(CollectorMessageStructure cms);
}