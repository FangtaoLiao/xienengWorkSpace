package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollDevice;
import com.synpower.bean.DeviceDetailCamera;
import com.synpower.bean.DeviceDetailCentralInverter;
import com.synpower.bean.DeviceDetailStringInverter;
import com.synpower.bean.DeviceSeriesModuleDetail;

@Repository
public interface CollDeviceMapper {

	public int countDeviceByPid(String pId);

	public List<CollDevice> getDevicesByPid(Map<String, Object> paramMap);

	public String getPower(List<Integer> list);

	public Integer getInverterCount(Map<String, Object> map);

	public List<CollDevice> saveShuCai(Map<String, Object> map);

	public int saveShuCai2(CollDevice collDevice);

	public List<CollDevice> getCollDevicesForStorage();

	public List<CollDevice> getDeviceListByPid(String pId);

	public List<CollDevice> listDevicesByPlantId(String plantId);

	public List<CollDevice> listDevicesSort(Map map);

	public List<CollDevice> listDevicesSortWX(Map map);

	public List<CollDevice> listDevicesWX(String plantId);

	public double getTotalPowerForPlant(List<Integer> devices);

	public List<Double> getEachPower(List<Integer> list);

	public Integer getDevCount(String plantId);

	public CollDevice getDeviceById(String id);

	public Integer getDeviceType(String deviceId);

	public Integer getDid(String name, Integer typeId);

	public Integer addDevice(CollDevice device);

	public List<Integer> getDeviceIdByPlants(Map map);

	public List<CollDevice> getDevices(Map<String, Object> parameterMap);

	public List<CollDevice> getDeviceByCon(Map<String, Object> map);

	public String getTopDeviceName(Integer id);

	public String getSupDeviceName(Integer supid);

	public Integer getFatherId(String deviceId);

	public Integer delSubDevice(String deviceId);

	public List<CollDevice> getCollectors(Map<String, Object> parameterMap);

	/**
	 * @Title: getDeviceTypeByIds
	 * @Description: 根据设备集合获取设备类型
	 * @param map
	 * @return: List<Integer>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月14日下午3:53:57
	 */
	public List<Integer> getDeviceTypeByIds(List list);

	public int updateStatus(Map map);

	public Map<String, String> getPlantAndDevice(int deviceId);

	public List<CollDevice> getOrgDevice(Map map);

	public Integer getInverterCount4P(Map<String, Object> parameterMap);

	/**
	 * @Title: getCollectInveter
	 * @Description: 获取数采 下的逆变器数量
	 * @param deviceId
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月9日下午3:47:14
	 */
	public int getCollectInveter(String sn);

	/**
	 * @Title: getDeviceBySn
	 * @Description: 通过SN号获取设备
	 * @param sn
	 * @return: CollDevice
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月10日下午1:56:17
	 */
	public CollDevice getCollectBySn(String sn);

	/**
	 * @Title: getmodelListById
	 * @Description: 根据设备id获取model集合<br>
	 * select DISTINCT data_mode from coll_device
	 *            where
	 *            <foreach collection="list" item="item" open=" id IN (" close=") "
	 *            separator=","> #{item} </foreach> and device_valid=0<br>
	 *            根据设备id得到所有的coll_model id集合
	 * @param list
	 * @return: List<Integer>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月3日下午3:39:18><br> 
	 */
	public List<Integer> getmodelListById(List list);

	/**
	 * @Title: delDevices
	 * @Description: 删除设备
	 * @param list
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月15日下午3:37:46
	 */
	public int delDevices(List list);

	/**
	 * @Title: delCollectorDevice
	 * @Description: 删除数采下关联的设备
	 * @param supId
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月15日下午3:38:45
	 */
	public int delCollectorDevice(int supId);

	/**
	 * @Title: updateDeviceValid
	 * @Description: 更改设备的删除激活状态
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月16日上午10:38:06
	 */
	public int updateDeviceValid(Map map);

	/**
	 * @Title: updateCollectorDeviceValid
	 * @Description: 激活数采下的设备
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月16日上午11:38:06
	 */
	public int updateCollectorDeviceValid(Map map);

	/**
	 * @Title: updateCollectorDeviceInfo
	 * @Description: 激活下连设备并修改设备信息
	 * @param map
	 * @return: int
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年5月14日上午11:09:26
	 */
	public int updateCollectorDeviceInfo(Map map);

	/**
	 * @Title: updateDeviceInfo
	 * @Description: 修改设备信息
	 * @param map
	 * @return: int
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年5月14日下午4:47:47
	 */
	public int updateDeviceInfo(Map map);

	/**
	 * @Title: getAllDevice
	 * @Description: TODO
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午3:04:43
	 */
	public List<CollDevice> getAllDevice();

	/**
	 * @Title: deleteDevices
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午3:21:19
	 */
	public Integer deleteDevices(Map<String, Object> paraMap);

	/**
	 * @Title: getPlantCollector
	 * @Description: 获取电站的数采
	 * @param plantId
	 * @return: List<CollDevice>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月16日下午4:20:34
	 */
	public List<CollDevice> getPlantCollector(int plantId);

	/**
	 * @Title: updateCollectInfo
	 * @Description: 激活数采
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月18日上午10:23:54
	 */
	public int updateCollectInfo(Map map);

	/**
	 * @Title: updateCollectValidAndName
	 * @Description: 激活数采和修改数采名称
	 * @param map
	 * @return: int
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年5月14日上午10:28:27
	 */
	public int updateCollectValidAndName(Map map);

	/**
	 * @Title: updateCollectValid
	 * @Description: 修改数采信息
	 * @param map
	 * @return: int
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年5月14日下午4:24:06
	 */
	public int updateCollectValid(Map map);

	/**
	 * @Title: updateCollectorStatus
	 * @Description: 更新数采状态
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午5:17:13
	 */
	public Integer updateCollectorStatus(Map<String, Object> paraMap);

	/**
	 * @Title: updateCollectorDeviceStatus
	 * @Description: 更新数采下设备状态
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午5:53:43
	 */
	public Integer updateCollectorDeviceStatus(Map<String, Object> paraMap);

	/**
	 * @Title: getDeviceDetails
	 * @Description: 获取设备详情
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月16日下午6:10:38
	 */
	public List<CollDevice> getDeviceDetails(Map<String, Object> paraMap);

	/**
	 * @Title: getM4Device
	 * @Description: TO获取设备型号
	 * @param paramMap
	 * @return: DeviceDetailStringInverter
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月17日上午10:08:02
	 */
	public DeviceDetailStringInverter getM4Device(Map<String, Object> paramMap);

	/**
	 * @param paraMap
	 * @Title: getAllDeviceNotColl
	 * @Description: 获取非数采信息
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月17日上午10:32:51
	 */
	public List<CollDevice> getAllDeviceNotColl(Map<String, Object> paraMap);

	/**
	 * @Title: getInverterList
	 * @Description: 逆变器列表
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月17日下午2:21:13
	 */
	public List<CollDevice> getInverterList(Map<String, Object> paraMap);

	/**
	 * @Title: getDataModel
	 * @Description: 点表
	 * @param deviceId
	 * @return: String
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日上午9:51:30
	 */
	public String getDataModel(String deviceId);

	/**
	 * @Title: getM4Device4Choice
	 * @Description: 层级选择型号
	 * @param paraMap
	 * @return: List<String>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日下午3:43:35
	 */
	public List<DeviceDetailStringInverter> getM4Device4Choice(Map<String, Object> paraMap);

	/**
	 * @Title: jugeSNExist
	 * @Description: 判断sn号是否存在
	 * @param sn
	 * @return: String
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日下午7:41:55
	 */
	public String getSNExist(String sn);

	/**
	 * @Title: addSubDevice
	 * @Description: 增加挂载设备
	 * @param newSubDevice
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日下午8:10:05
	 */
	public Integer addSubDevice(CollDevice newSubDevice);

	/**
	 * @Title: getInverterListByOid
	 * @Description: 组织下逆变器列表
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日上午9:32:47
	 */
	public List<CollDevice> getInverterListByOid(Map<String, Object> paraMap);

	/**
	 * @Title: addSubDevice4Update
	 * @Description: 更新挂载设备
	 * @param map2
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日下午1:38:08
	 */
	public Integer updateSubDevice(Map<String, Object> map2);

	/**
	 * @Title: updateSomeSubDevice
	 * @Description: 修改设备的一些基本信息
	 * @param map2
	 * @return: Integer
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年5月18日上午10:20:31
	 */
	public Integer updateSomeSubDevice(Map map);

	/**
	 * @Title: updateCollectorInfo
	 * @Description: 更新数采
	 * @param paramMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日下午1:57:59
	 */
	public Integer updateCollectorInfo(Map<String, Object> paramMap);

	/**
	 * @Title: jugeSNExist4Update
	 * @Description: TODO
	 * @param exMap
	 * @return: String
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日下午2:48:29
	 */
	public String getSNExist4Update(Map<String, Object> exMap);

	/**
	 * @Title: addSubDevice4Update
	 * @Description: 编辑时新增设备
	 * @param device
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日下午3:06:11
	 */
	public Integer addSubDevice4Update(CollDevice device);

	/**
	 * @Title: getCollectorsByPid
	 * @Description: 电站下数采
	 * @param paramMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月20日下午4:13:44
	 */
	public List<CollDevice> getCollectorsByPid(Map<String, Object> paramMap);

	/**
	 * @Title: updateInverterInfo
	 * @Description: 更新逆变器
	 * @param paramMap1
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月21日下午6:47:33
	 */
	public Integer updateInverterInfo(Map<String, Object> paramMap1);

	/**
	 * @Title: getTotalCount
	 * @Description: TODO
	 * @param deviceId
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月22日下午6:55:01
	 */
	public Integer getTotalCount(String deviceId);

	/**
	 * @Title: getAllDeviceId
	 * @Description: 所有设备id
	 * @return: List<String>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月23日上午9:55:10
	 */
	public List<String> getAllDeviceId();

	/**
	 * @Title: getOtherModule
	 * @Description: 所有型号
	 * @param allId
	 * @return: List<DeviceSeriesModuleDetail>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月23日上午9:58:06
	 */
	public List<DeviceSeriesModuleDetail> getOtherModule(List<String> allId);

	public List<Map<String, Object>> getPlantIdofOrg(String orgId);

	/**
	 * @Title: getTotalCountPage
	 * @Description: TODO
	 * @param paramMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午5:50:13
	 */
	public Integer getTotalCountPage(Map<String, Object> paramMap);

	/**
	 * @Title: getInverterPagePid
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午5:53:49
	 */
	public Integer getInverterPagePid(Map<String, Object> paraMap);

	/**
	 * @Title: getInverterPageOid
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午5:55:16
	 */
	public Integer getInverterPageOid(Map<String, Object> paraMap);

	public List<DeviceDetailCamera> getCameraInfo(Map<String, Object> paraMap);

	/**
	 * @Title: addCamera
	 * @Description: 新增摄像头
	 * @param paramMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月23日下午3:11:30
	 */
	public Integer addCamera(Map<String, Object> paramMap);

	/**
	 * @Title: getCameraListByPid
	 * @Description: 获取电站摄像头列表
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月23日下午3:50:08
	 */
	public List<Map<String, Object>> getCameraListByPid(Map<String, Object> paraMap);

	/**
	 * @Title: getCameraListByOid
	 * @Description: 获取组织摄像头列表
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月23日下午3:53:18
	 */
	public List<Map<String, Object>> getCameraListByOid(Map<String, Object> paraMap);

	/**
	 * @Title: addCamera4Org
	 * @Description: 组织新增摄像头
	 * @param paramMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月24日上午9:56:06
	 */
	public Integer addCamera4Org(Map<String, Object> paramMap);

	/**
	 * @Title: delCamera
	 * @Description: 删除组织摄像头
	 * @param eMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月24日上午11:37:08
	 */
	public Integer delCamera(Map<String, Object> eMap);

	/**
	 * @Title: updateCmaera
	 * @Description: 编辑摄像头
	 * @param cMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月24日下午1:49:53
	 */
	public Integer updateCmaera(Map<String, Object> cMap);

	/**
	 * @Title: getSerialList
	 * @Description: 获取序列号列表
	 * @param pId
	 * @return: List<CameraInfo>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月25日下午3:47:37
	 */
	public List<CollDevice> getSerialList(String pId);

	/**
	 * @Title: getCameraSerial
	 * @Description: 获取序列号、通道号
	 * @param id
	 * @return: Map<String,Object>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月29日上午10:15:49
	 */
	public String getCameraSerial(String id);

	/**
	 * @Title: getPlantIdofOrg
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<Map<String,Object>>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月29日下午3:15:10
	 */
	public List<Map<String, Object>> getPlantIdofOrg4Camera(Map<String, Object> paraMap);

	/**
	 * @Title: getTotalCameraByPid
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午6:04:43
	 */
	public Integer getTotalCameraByPid(Map<String, Object> paraMap);

	/**
	 * @Title: getTotalCameraByOid
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午6:06:53
	 */
	public Integer getTotalCameraByOid(Map<String, Object> paraMap);

	List<DeviceDetailCamera> getAllType();

	/**
	 * @Title: updateCameraStatus
	 * @Description: TODO
	 * @param collDevice
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月10日下午3:44:46
	 */
	public Integer updateCameraStatus(CollDevice collDevice);

	/**
	 * @Title: getPlantDevice
	 * @Description: TODO
	 * @param paramMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月27日下午2:38:23
	 */
	public List<CollDevice> getPlantDevice(Map<String, Object> paramMap);

	/**
	 * @Title: getTotalCountPlant
	 * @Description: TODO
	 * @param paramMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月27日下午2:41:45
	 */
	public Integer getTotalCountPlant(Map<String, Object> paramMap);

	/**
	 * @Title: getInverterListByPidNoCondition
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月1日上午10:40:33
	 */
	public List<CollDevice> getInverterListByPidNoCondition(Map<String, Object> paraMap);

	/**
	 * @Title: getInverterListByOidNoCondition
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月1日上午10:42:03
	 */
	public List<CollDevice> getInverterListByOidNoCondition(Map<String, Object> paraMap);

	/**
	 * @Title: getInverterListByPidNoConditionCount
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月1日下午2:02:03
	 */
	public Integer getInverterListByPidNoConditionCount(Map<String, Object> paraMap);

	/**
	 * @Title: getInverterListByOidNoConditionCount
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月1日下午2:03:38
	 */
	public Integer getInverterListByOidNoConditionCount(Map<String, Object> paraMap);

	/**
	 * @Title: getPidAndOid
	 * @Description: TODO
	 * @param id
	 * @return: CollDevice
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月1日下午3:30:50
	 */
	public CollDevice getPidAndOid(Integer id);

	/**
	 * @Title: updateSubDeviceforUpdate
	 * @Description: TODO
	 * @param device: void
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月7日下午5:29:39
	 */
	public Integer updateSubDeviceforUpdate(CollDevice device);

	/**
	 * @Title: addSubDevice4UpdateOutPid
	 * @Description: TODO
	 * @param device: void
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月8日上午11:03:59
	 */
	public Integer addSubDevice4UpdateOutPid(CollDevice device);

	/**
	 * @Title: updateCollectorInfoOutPid
	 * @Description: TODO
	 * @param paramMap: void
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月8日上午11:25:25
	 */
	public Integer updateCollectorInfoOutPid(Map<String, Object> paramMap);

	/**
	 * @Title: updateSubDeviceOutPid
	 * @Description: TODO
	 * @param map2
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月8日上午11:27:50
	 */
	public Integer updateSubDeviceOutPid(Map<String, Object> map2);

	/**
	 * @Title: getAllDeviceType
	 * @Description: TODO
	 * @return: List<CollDevice>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月8日下午4:44:23
	 */
	public List<CollDevice> getAllDeviceType();

	/**
	 * @Title: getAllDeviceInfoDefault
	 * @Description: TODO
	 * @return: List<DeviceDetailCentralInverter>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月8日下午6:47:34
	 */
	public List<CollDevice> getAllDeviceInfoDefault();

	/**
	 * @Title: getDeviceDetailsPageCount
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年3月12日下午2:03:42
	 */
	public Integer getDeviceDetailsPageCount(Map<String, Object> paraMap);

	public List<CollDevice> listCollectorByPlantId(String plantId);

	public List<CollDevice> listInverterByPlantId(String plantId);

	public List<CollDevice> listCenInverterByPlantId(String plantId);

	public List<CollDevice> listPcsByPlantId(String plantId);

	public List<CollDevice> listBmsByPlantId(String plantId);

	public List<CollDevice> listMeterByPlantId(String plantId);
	
	public List<Map<String,Object>> listEnvMonitorByPlantId(String plantId);

	public List<CollDevice> listConfBoxByPlantId(String plantId);

	public List<CollDevice> listBoxChangeByPlantId(String plantId);
}