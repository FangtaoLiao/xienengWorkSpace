package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceDetailCamera;
import com.synpower.bean.CollDevice;

/*****************************************************************************
 * @Package: com.synpower.dao
 * ClassName: CameraInfoMapper
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年1月26日下午4:23:26   SP0007             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
/*****************************************************************************
 * @Package: com.synpower.dao
 * ClassName: CameraInfoMapper
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年1月26日下午4:23:28   SP0007             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Repository
public interface DeviceDetailCameraMapper {

	List<DeviceDetailCamera> getAllType();

	/** 
	  * @Title:  countCamera 
	  * @Description: 获取满足条件的摄像头数量 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月26日下午4:23:43
	*/ 
	public int countCamera(Map map);
	public List<DeviceDetailCamera> listCamera(Map map);
	public int updateStatus(Map map);
	public int updateEquipmentBasic(Map map);
	public int updatePhotoNull(String deviceId);
	public int insertCameraPhoto(Map map);
	public List getCameraInfo(Map map);
	public int insertCamera(List list);
	public int insertCameraByBean(DeviceDetailCamera camera);
	public DeviceDetailCamera getCameraById(String deviceId);
}