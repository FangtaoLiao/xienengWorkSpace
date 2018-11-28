package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceSeriesModuleDetail;

@Repository
public interface DeviceSeriesModuleDetailMapper {
	/** 
	  * @Title:  getModuleCountForDevices 
	  * @Description:  根据设备获取组件 
	  * @param list
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月21日下午2:48:02
	*/ 
	public int getModuleCountForDevices(List<Integer> list);

	/** 
	  * @Title:  getEachModeleInfo 
	  * @Description:  TODO 
	  * @param deviceId
	  * @return: List<DeviceSeriesModuleDetail>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月17日下午4:00:10
	*/
	public List<Map<String, Object>> getEachModeleInfo(String deviceId);

	/** 
	  * @Title:  updateModuleInfo 
	  * @Description:  TODO 
	  * @param eMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月22日上午10:54:57
	*/
	public Integer updateModuleInfo(Map<String, Object> eMap);

	/** 
	  * @Title:  addNewModule 
	  * @Description:  TODO 
	  * @param eMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月22日上午11:37:08
	*/
	public Integer addNewModule(Map<String, Object> eMap);

	/** 
	  * @Title:  delModuleInfo 
	  * @Description:  TODO 
	  * @param moduleMap
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月22日下午6:28:55
	*/
	public Integer delModuleInfo(Map<String, Object> moduleMap);

	/** 
	  * @Title:  getTotalCount 
	  * @Description:  TODO 
	  * @param deviceId
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日上午10:10:18
	*/
	public Integer getTotalCount(String deviceId);

	/** 
	  * @Title:  getAllDeviceId 
	  * @Description:  TODO 
	  * @return: List<String>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日上午10:12:51
	*/
	public List<String> getAllDeviceId();

	/** 
	  * @Title:  getAllDeviceId 
	  * @Description:  TODO 
	  * @param deviceId
	  * @return: List<String>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日上午10:15:17
	*/
	public List<String> getAllDeviceId(String deviceId);

	/** 
	  * @Title:  getOtherModule 
	  * @Description:  TODO 
	  * @param allId
	  * @return: List<DeviceSeriesModuleDetail>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日上午10:19:51
	*/
	public List<DeviceSeriesModuleDetail> getOtherModule(Map<String, Object> mMap);

	/** 
	  * @Title:  delOldModuleInfo 
	  * @Description:  TODO 
	  * @param deviceId1
	  * @return: Integer
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月9日下午5:58:39
	*/
	public Integer delOldModuleInfo(String deviceId1);

}