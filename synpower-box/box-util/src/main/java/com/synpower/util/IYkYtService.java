package com.synpower.util;

import java.rmi.RemoteException;
import java.util.Map;

public interface IYkYtService {

	/**
	 * @Title: sayHello
	 * @Description: 测试rmi暴露成功与否
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月6日下午2:13:09
	 */
	public String sayHello(String name) throws RemoteException;

	/**
	 * @Title: setTimeXml
	 * @Description: 暴露对指定数采节点下设备的对时方法
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月6日下午2:13:09
	 */
	public boolean setTimeXml(String collector_id) throws RemoteException;

	/**
	 * @Title: ykYt
	 * @Description: 暴露遥控遥调的远程调用方法
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月6日下午2:13:09
	 */
	public boolean ykYt(String sessionId, String collector_id, Map<String, Map<String, String>> ykYtMap)
			throws RemoteException;

	/**
	 * @Title: refreshCache
	 * @Description: 刷新设备缓存的方法，当有维修，报废，或新增后可以手动调用刷新设备缓存(不开放给用户)，
	 * 及时检测到现有设备(删除设备后一定要将对应映射关系设置为无效，然后刷新设备缓存)
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月6日下午2:13:09
	 */
	public boolean refreshCache();
	
	/**
	  * @Title:  dataModelSet 
	  * @Description:  TODO 暴露新数采的点表下发接口
	  * @param collSn 数据采集器SN号
	  * @param dataModelSetMap Map<comId,Map<slaveId，dataModel>>
	  * @return
	  * @throws RemoteException: boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月27日下午4:17:57
	 */
	public boolean dataModelSet(String collSn,Map<Integer, Map<String, Integer>> dataModelSetMap) throws RemoteException;
}