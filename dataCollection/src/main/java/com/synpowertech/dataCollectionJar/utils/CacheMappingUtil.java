package com.synpowertech.dataCollectionJar.utils;


//import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.mqttJsonClient.MqttActiveConnectClient;
/**
 * ***************************************************************************
 * @Package: com.synpowertech.utils
 * @ClassName: CacheMappingUtil
 * @Description: 根据解析得到的数据，取出缓存中的值以存redis
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月6日上午11:08:02   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class CacheMappingUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheMappingUtil.class);
	
	/**
	  * @Title:  pid2signalGuidYx 
	  * @Description:  根据采集节点id，类型和pid获取信号唯一id(遥信) 
	  * @param collectorIdType
	  * @param pid
	  * @return: List<String> guid列表
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:08:44
	 */
	public static List<String> pid2signalGuidYx(String collectorIdType,String pid) {
		try {
			List<String> signalGuid = DeviceCache.collector2pidGuidYx.get(collectorIdType).get(Integer.parseInt(pid));
			return signalGuid;
		} catch (Exception e) {
			logger.debug("can not get pid2signalGuidYx:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2signalGuidYc 
	  * @Description:  根据采集节点id，类型和pid获取信号唯一id(遥测) 
	  * @param collectorIdType
	  * @param pid
	  * @return: List<String> guid列表
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:07:58
	 */
	public static List<String> pid2signalGuidYc(String collectorIdType,String pid) {
		try {
			List<String> signalGuid = DeviceCache.collector2pidGuidYc.get(collectorIdType).get(Integer.parseInt(pid));
			
			//60KTL无日发电量60ktl_a_YC_48_0，不解析无效点，由定时任务手动计算添加
			if (signalGuid != null && signalGuid.contains("60ktl_a_YC_48_0")) {
				return null;
			}
			return signalGuid;
		} catch (Exception e) {
			logger.debug("can not get pid2signalGuidYc:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2signalGuidYk 
	  * @Description:  根据采集节点id，类型和pid获取信号唯一id(遥控) 
	  * @param collectorIdType
	  * @param pid
	  * @return: List<String> guid集合
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:09:38
	 */
	public static List<String> pid2signalGuidYk(String collectorIdType,String pid) {
		try {
			List<String> signalGuid = DeviceCache.collector2pidGuidYk.get(collectorIdType).get(Integer.parseInt(pid));
			return signalGuid;
		} catch (Exception e) {
			logger.debug("can not get pid2signalGuidYk:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2signalGuidYt 
	  * @Description:  根据采集节点id，类型和pid获取信号唯一id(遥调) 
	  * @param collectorIdType
	  * @param pid
	  * @return: List<String> guid 列表
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:10:28
	 */
	public static List<String> pid2signalGuidYt(String collectorIdType,String pid) {
		try {
			List<String> signalGuid = DeviceCache.collector2pidGuidYt.get(collectorIdType).get(Integer.parseInt(pid));
			return signalGuid;
		} catch (Exception e) {
			logger.debug("can not get pid2signalGuidYt:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2devIdYx 
	  * @Description:  根据采集节点id,类型和pid获取映射表设备id(遥信) 
	  * @param collectorIdType
	  * @param pid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:12:05
	 */
	public static String pid2devIdYx(String collectorIdType,String pid) {
		
		try {
			int deviceId = DeviceCache.collector2pidDevIdYx.get(collectorIdType).get(Integer.parseInt(pid));
			return Integer.toString(deviceId);
		} catch (Exception e) {
			logger.debug("can not get pid2devIdYx:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2devIdYc 
	  * @Description:  根据采集节点id,类型和pid获取映射表设备id(遥测) 
	  * @param collectorIdType
	  * @param pid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:12:33
	 */
	public static String pid2devIdYc(String collectorIdType,String pid) {
		
		try {
			int deviceId = DeviceCache.collector2pidDevIdYc.get(collectorIdType).get(Integer.parseInt(pid));
			return Integer.toString(deviceId);
		} catch (Exception e) {
			logger.debug("can not get pid2devIdYc:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2devIdYk 
	  * @Description:  根据采集节点id,类型和pid获取映射表设备id(遥控) 
	  * @param collectorIdType
	  * @param pid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:16:08
	 */
	public static String pid2devIdYk(String collectorIdType,String pid) {
		
		try {
			int deviceId = DeviceCache.collector2pidDevIdYk.get(collectorIdType).get(Integer.parseInt(pid));
			return Integer.toString(deviceId);
		} catch (Exception e) {
			logger.debug("can not get pid2devIdYk:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  pid2devIdYt 
	  * @Description:  根据采集节点id,类型和pid获取映射表设备id(遥调)  
	  * @param collectorIdType
	  * @param pid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:16:36
	 */
	public static String pid2devIdYt(String collectorIdType,String pid) {
		
		try {
			int deviceId = DeviceCache.collector2pidDevIdYt.get(collectorIdType).get(Integer.parseInt(pid));
			return Integer.toString(deviceId);
		} catch (Exception e) {
			logger.debug("can not get pid2devIdYt:{}:{}:{}",collectorIdType,pid,e.getClass());
		}
		return null;
	}

	/**
	  * @Title:  pid2modelAttr 
	  * @Description: 根据采集节点id和pid获取要存储的模型名和属性名（遥测定时存库）
	  * @param signalGuid: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月7日下午6:24:22
	 */
	public static Map<String,String> signalGuid2modelAttr(String signalGuid) {
		
		Map<String, String> resultMap;
		try {
			resultMap = DeviceCache.signalGuid2table.get(signalGuid);
			return resultMap;
		} catch (NumberFormatException e) {
			logger.debug("can not get signalGuid2modelAttr:{}:{}",signalGuid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  signalGuid2model 
	  * @Description:  根据封装好的signalGuidMap获取器对应的模型名
	  * @param signalGuidMap
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月26日下午5:19:56
	 */
	public static String signalGuid2model(Map<String, String> signalGuidMap) {
		
		Set<Entry<String, String>> entrySet = signalGuidMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String keyTemp = entry.getKey();
			if ("data_time".equals(keyTemp) || "sys_time".equals(keyTemp)) {
				continue;
			} else {
				try {
					Map<String, String> resultMap = DeviceCache.signalGuid2table.get(keyTemp);
					Set<String> keySet = resultMap.keySet();
					for (String modelName : keySet) {
						//只会遍历一次
						return modelName;
					}
				} catch (Exception e) {
					logger.debug("can not get signalGuid2model:{}:{}",signalGuidMap.toString(),e.getClass());
				}
			}
		}
		
		return null;
	}
	
	
	
	/**
	  * @Title:  signalGuid2yxId 
	  * @Description:  根据映射表id和值找到要存储的遥信id(变位遥信实时存库) 
	  * @param signalGuid
	  * @param yxValue: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月5日上午10:38:44
	 */
	public static Integer signalGuid2yxId(String signalGuid,String yxValue) {
		
		try {
			Integer yxId = DeviceCache.signalGuid2yxId.get(signalGuid).get(Integer.parseInt(yxValue));
			return yxId;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2yxId:{}:{}:{}",signalGuid,yxValue,e.getClass());
		}

		return null;
	}
	
	/**
	  * @Title:  devId2guidPid 
	  * @Description:   根据devId和signalGuid找到对应的pid 
	  * @param devId
	  * @param signalGuid
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:27:23
	 */
	public static Integer devId2guidPid(Integer devId,String signalGuid) {
		
		try {
			Map<String, Integer> guidPidMap = DeviceCache.devId2guidPid.get(devId);
			if (guidPidMap != null) {
				return guidPidMap.get(signalGuid);
			}
		} catch (Exception e) {
			logger.debug("can not get devId2guidPid:{}:{}:{}",devId,signalGuid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  devId2guidCtrlType 
	  * @Description:  根据devId和signalGuid找到对应的控制类型 
	  * @param devId
	  * @param signalGuid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日上午10:26:51
	 */
	public static String devId2guidCtrlType(Integer devId,String signalGuid) {
		
		try {
			String ctrlType = DeviceCache.devId2guidCtrlType.get(devId).get(signalGuid);
			//因为有catch,DeviceCache.devId2guidCtrlType.get(devId)的nullPointException被捕获
			return ctrlType;
		} catch (Exception e) {
			logger.debug("can not get devId2guidCtrlType:{}:{}:{}",devId,signalGuid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  deviceId2collId 
	  * @Description:  根据设备id找到上级采集节点id
	  * @param deviceId: void
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月5日上午10:46:59
	 */
/*	public String deviceId2collId(int deviceId) {

		String collectorId = null;
		try {
			collectorId = DeviceCache.dev2collector.get(deviceId);
		} catch (Exception e) {
			logger.info("can not get the value：{}",e);
		}
		return collectorId;
	}*/
	
	/**
	  * @Title:  collId2devSet 
	  * @Description:  根据采集节点id找到下属的设备列表 
	  * @param collectorId: void
	 * @return 
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月5日上午10:48:26
	 */
/*	public Set<Integer> collId2devSet(String collectorId) {
		
		Set<Integer> devIdSet = null;
		try {
			devIdSet = DeviceCache.collector2devSet.get(collectorId);
		} catch (Exception e) {
			logger.debug("can not get the value：{}",e);
		}
		return devIdSet;
	}*/
	
	
	/**
	  * @Title:  signalGuid2gain 
	  * @Description:找到信号点的增益
	  * @param signalGuid
	  * @return: Float
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月8日下午5:43:27
	 */
	public static Float signalGuid2gain(String signalGuid) {
		
		try {
			Float dataGain = DeviceCache.signalGuid2gain.get(signalGuid);
			return dataGain;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2gain：{}:{}",signalGuid,e.getClass());
		}
		//缓存中没有，返回空
		return null;
	}
	
	/**
	  * @Title:  signalGuid2factor 
	  * @Description: 找到信号点的修正系数
	  * @param signalGuid
	  * @return: Float
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月8日下午5:43:51
	 */
	public static Float signalGuid2factor(String signalGuid) {
		
		try {
			Float factor = DeviceCache.signalGuid2factor.get(signalGuid);
			return factor;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2factor：{}:{}",signalGuid,e.getClass());
		}
		//缓存中没有，返回空
		return null;
	}
	
	/**
	  * @Title:  signalGuid2minVal 
	  * @Description: 找到信号点值范围的最小值
	  * @param signalGuid
	  * @return: Float
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月8日下午5:44:14
	 */
	public static Float signalGuid2minVal(String signalGuid) {
		
		try {
			Float minVal = DeviceCache.signalGuid2minVal.get(signalGuid);
			return minVal;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2minVal：{}:{}",signalGuid,e.getClass());
		}
		//缓存中没有，返回空
		return null;
	}
	
	/**
	  * @Title:  signalGuid2maxVal 
	  * @Description: 找到信号点值范围的最大值
	  * @param signalGuid
	  * @return: Float
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月8日下午5:44:49
	 */
	public static Float signalGuid2maxVal(String signalGuid) {
		
		try {
			Float maxVal = DeviceCache.signalGuid2maxVal.get(signalGuid);
			return maxVal;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2maxVal：{}:{}",signalGuid,e.getClass());
		}
		//缓存中没有，返回空
		return null;
	}
	
	/**
	  * @Title:  collectorId2devId 
	  * @Description:  采集器节点id(name)映射其设备的id
	  * @param collectorId
	  * @return: int
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月16日下午2:01:06
	 */
	public static Integer collectorId2devId(String collectorId) {
		
		try {
			Integer devId = DeviceCache.collectorId2devId.get(collectorId);
			return devId;
		} catch (Exception e) {
			logger.debug("can not get collectorId2devId：{}:{}",collectorId,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  signalGuid2processMethod 
	  * @Description: 根据guid获取特殊处理方法
	  * @param signalGuid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月22日下午6:26:35
	 */
	public static String signalGuid2processMethod(String signalGuid) {
		
		try {
			String MethodName = DeviceCache.signalGuid2processMethod.get(signalGuid);
			return MethodName;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2processMethod：{}:{}",signalGuid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  signalGuid2processDetail 
	  * @Description:  根据guid获取遥信表遥测起始位，截取终止位 
	  * @param signalGuid
	  * @return: Map<Integer,Integer>
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月22日下午6:26:50
	 */
	public static Map<Integer,Integer> signalGuid2processDetail(String signalGuid) {
		try {
			Map<Integer,Integer> detailMap = DeviceCache.signalGuid2processDetail.get(signalGuid);
			return detailMap;
		} catch (Exception e) {
			logger.debug("can not get signalGuid2processDetail：{}:{}",signalGuid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  signalGuid2singleRegister 
	  * @Description: 判断该信号是否是单寄存器信号
	  * @return: boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月4日上午10:54:49
	 */
	public static boolean signalGuid2singleRegister(String signalGuid) {
		boolean contains = false;
		try {
			contains = DeviceCache.signalGuid2singleRegister.contains(signalGuid);
		} catch (Exception e) {
			logger.debug("can not get signalGuid2singleRegister,return false:{}:{}",signalGuid,e.getClass());
		}
		return contains;
	}
	
	/**
	  * @Title:  signalGuid4yc2yx 
	  * @Description:  判断该信号是否是遥测表遥信的信号
	  * @param signalGuid
	  * @return: boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月12日下午2:54:50
	 */
	public static boolean signalGuid4yc2yx(String signalGuid) {
		boolean contains = false;
		try {
			contains = DeviceCache.signalGuid4yc2yx.contains(signalGuid);
		} catch (Exception e) {
			logger.debug("can not get signalGuid4yc2yx,return false:{}:{}",signalGuid,e.getClass());
		}
		return contains;
	}
	
	/**
	  * @Title:  devId2SunRiseSet 
	  * @Description:  判断当前时间是否是设备所在地有效入库时间
	  * @param deviceId
	  * @param timeMills
	  * @return: boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年1月3日下午4:37:02
	 */
	public static boolean devId2SunRiseSet(Integer deviceId, Long timeMillis) {
		boolean valid = false;
		try {
			// devId2SunRiseSet中只包含逆变器和汇流箱
			Long[] sunRiseSetArr = DeviceCache.devId2SunRiseSet.get(deviceId);
			if (sunRiseSetArr == null) {// 查询不到，说明是其他设备，允许入库
				return true;

			} else if (timeMillis >= sunRiseSetArr[0] && timeMillis <= sunRiseSetArr[1]) {// 有效入库时间判断
				valid = true;
			}

		} catch (Exception e) {
			logger.debug("can not get devId2SunRiseSet,return false:{}:{}:{}",deviceId,timeMillis,e.getClass());
		}
		return valid;
	}
	
	/*****************************V3版本新增*****************************/
	
	/**
	  * @Title:  getNewCollId 
	  * @Description: 获取新数采的id
	  * @param collSn
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年3月16日下午3:56:10
	 */
	public static Integer getNewCollId(String collSn) {
		try {
			Integer collId = DeviceCache.newCollSnIdMap.get(collSn);
			return collId;
		} catch (Exception e) {
			logger.debug("can not get getNewCollId,get an Exception:{}:{}",collSn, e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  containOldCollectorId 
	  * @Description:  TODO 旧数据采集节点Sn(String)集合，对时使用，无关状态，还有新数采动态订阅
	  * @param oldCollectorId
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午10:43:43
	 */
	public static Boolean containOldCollectorId(String oldCollectorId) {
		Boolean contain = false;
		try {
			contain = DeviceCache.oldCollectorIdSet.contains(oldCollectorId);
			return contain;
		} catch (Exception e) {
			logger.debug("do not contain oldCollectorId,get an Exception:{}:{}",oldCollectorId, e.getClass());
		}
		return contain;
	}
	
	/**
	  * @Title:  containNewCollectorId 
	  * @Description:  TODO 新数据采集节点Sn(String)集合，对时使用，无关状态，还有新数采动态订阅
	  * @param newCollectorId
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午10:45:54
	 */
	public static Boolean containNewCollectorId(String newCollectorId) {
		Boolean contain = false;
		try {
			contain = DeviceCache.newCollectorIdList.contains(newCollectorId);
			return contain;
		} catch (Exception e) {
			logger.debug("do not contain newCollectorId,get an Exception:{}:{}",newCollectorId, e.getClass());
		}
		return contain;
	}
	
	/**
	  * @Title:  getOldCollSnId 
	  * @Description:  TODO 旧的畅洋数采sn和id对应Map，解析旧数采
	  * @param oldCollSn
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午10:50:08
	 */
	public static Integer getOldCollSnId(String oldCollSn) {
		try {
			Integer collDevId = DeviceCache.oldCollSnIdMap.get(oldCollSn);
			return collDevId;
		} catch (Exception e) {
			logger.debug("can not get oldCollSnId：{}:{}",oldCollSn,e.getClass());
		}
		return null;
	}

	/**
	  * @Title:  signedNumJudge 
	  * @Description:  TODO signalGuid中有符号数缓存,主要用在新数采解析上,通用
	  * @param guid
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午10:54:56
	 */
	public static Boolean signedNumJudge(String guid) {
		Boolean contain = false;
		try {
			contain = DeviceCache.signalGuid2signedNum.contains(guid);
			return contain;
		} catch (Exception e) {
			logger.debug("do not contain signedNum,get an Exception:{}:{}",guid, e.getClass());
		}
		return contain;
	}
	
	/**
	  * @Title:  slaveId2devId 
	  * @Description:  TODO 数采id,comId，salveId找到设备id ，Map<coll_id,Map<comId_slaveId, devId>>
	  * @param collId
	  * @param comIdSlaveId
	  * @return: Long
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:19:57
	 */
	public static Long slaveId2devId(Long collId,String comIdSlaveId) {
		try {
			Long devId = DeviceCache.slaveId2devIdMap.get(collId).get(comIdSlaveId);
			return devId;
		} catch (Exception e) {
			logger.debug("can not get slaveId2devId：{}:{}",collId + "_" + comIdSlaveId ,e.getClass());
		}
		return null;

	}
	
	/**
	  * @Title:  devId2slaveId 
	  * @Description:  TODO 新数采封装下发所需缓存Map<devId,coll_id_comId_slaveId>
	  * @param devId
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:23:04
	 */
	public static String devId2slaveId(Long devId) {
		try {
			String locStr = DeviceCache.devId2slaveIdMap.get(devId);
			return locStr;
		} catch (Exception e) {
			logger.debug("can not get devId2slaveId：{}:{}",devId ,e.getClass());
		}
		return null;

	}
	
	/**
	  * @Title:  devId2dataMode 
	  * @Description:  TODO 新数采，设备和点表对应关系Map<deviceId,dataMode>
	  * @param devId
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:24:38
	 */
	public static Integer devId2dataMode(Integer devId) {
		try {
			Integer dataMode = DeviceCache.devId2dataModeMap.get(devId);
			return dataMode;
		} catch (Exception e) {
			logger.debug("can not get devId2dataMode：{}:{}",devId ,e.getClass());
		}
		return null;

	}
	
	/**
	  * @Title:  dataModel2addrGuid 
	  * @Description:  TODO 新数采解析缓存Map<dataModel, Map<addressId, List<guid>>
	  * @param dataMode
	  * @param addreddId
	  * @return: List<String>
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:27:51
	 */
	public static List<String> dataModel2addrGuid(Integer dataMode,String addressId){
		try {
			List<String> guidList = DeviceCache.dataModel2addrGuid.get(dataMode).get(addressId);
			return guidList;
		} catch (Exception e) {
			logger.debug("can not get dataModel2addrGuid：{}:{}",dataMode + "_" + addressId,e.getClass());
		}
		return null;
	}

	/**
	  * @Title:  addrHighList 
	  * @Description:  TODO 新数采合成点高位地址Map<dataModel,List<addrH>>
	  * @param dataMode
	  * @return: List<String>
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:30:33
	 */
	public static List<String> addrHighList(Integer dataMode){
		try {
			List<String> addrHighList = DeviceCache.addrHighListMap.get(dataMode);
			return addrHighList;
		} catch (Exception e) {
			logger.debug("can not get addrHighList：{}:{}",dataMode,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  addrHighListContain 
	  * @Description:  TODO 新数采合成点高位地址Map<dataModel,List<addrH>>,看是否包含
	  * @param dataMode
	  * @param addrH
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月20日下午4:02:09
	 */
	public static Boolean addrHighListContain(Integer dataMode,String addrH){
		boolean contains = false;
		try {
			contains = DeviceCache.addrHighListMap.get(dataMode).contains(addrH);
			return contains;
		} catch (Exception e) {
			logger.debug("can not get addrHighList：{}:{}",dataMode,e.getClass());
		}
		return contains;
	}
	
	/**
	  * @Title:  addrReadList 
	  * @Description:  TODO 获取点表下发所需的读地址list
	  * @param dataMode
	  * @return: List<String>
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月27日下午5:23:25
	 */
	public static Set<String> addrReadSet(Integer dataMode){
		Set<String> resultSet = null;
		try {
			resultSet = DeviceCache.addrSetMap.get(dataMode);
			return resultSet;
		} catch (Exception e) {
			logger.debug("can not get addrList：{}:{}",dataMode,e.getClass());
		}
		return resultSet;
	}
	
	/**
	  * @Title:  guidAddrHigh 
	  * @Description:  TODO 新数采控制缓存:通过collId和deviceId可反向查找comid和slaveId;guid和高位地址缓存Map<signalGuid,addrHigh>，只包含遥控遥调
	  * @param guid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:41:18
	 */
	public static String guidAddrHigh(String guid){
		try {
			String addrH = DeviceCache.guidAddrHighMap.get(guid);
			return addrH;
		} catch (Exception e) {
			logger.debug("can not get guidAddrHigh：{}:{}",guid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  getSignalGuidL 
	  * @Description:  TODO 一般只在遥测出现,畅洋数采合成点信号高位Map<dataModel,signalGuidH:signalGuidL>,兼容新数采
	  * @param dataMode
	  * @param signalGuidH
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:46:43
	 */
	public static String getSignalGuidL(Integer dataMode,String signalGuidH) {
		try {
			String signalGuidL = DeviceCache.signalGuidHMap.get(dataMode).get(signalGuidH);
			return signalGuidL;
		} catch (Exception e) {
			logger.debug("can not get signalGuidL：{}:{}",dataMode + ":" + signalGuidH,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  getSignalGuidH 
	  * @Description:  TODO 一般只在遥测出现,畅洋数采合成点信号低位Map<dataModel,signalGuidL:signalGuidH>,兼容新数采
	  * @param dataMode
	  * @param signalGuidL
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日上午11:47:24
	 */
	public static String getSignalGuidH(Integer dataMode,String signalGuidL) {
		try {
			String signalGuidH = DeviceCache.signalGuidLMap.get(dataMode).get(signalGuidL);
			return signalGuidH;
		} catch (Exception e) {
			logger.debug("can not get signalGuidH：{}:{}",dataMode + ":" + signalGuidL,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  collSn2ClientIdx 
	  * @Description:  TODO 新数采控制下发所需，collsn和订阅发布客户端下标关系
	  * @param collSn
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月18日下午2:48:42
	 */
	public static Integer collSn2ClientIdx(String collSn) {
		try {
			Integer clientIdx = DeviceCache.collSn2ClientIdx.get(collSn);
			return clientIdx;
		} catch (Exception e) {
			logger.debug("can not get collSn2ClientIdx：{}:{}",collSn,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  guid2FunctionCode 
	  * @Description:  TODO 新数采下发控制指令，查询功能码所需
	  * @param guid
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年6月4日下午2:12:48
	 */
	public static String guid2FunctionCode(String guid) {
		
		try {
			String functionCode = DeviceCache.guidCodeMap.get(guid);
			return functionCode;
		} catch (Exception e) {
			logger.debug("can not get functionCode：{}:{}",guid,e.getClass());
		}
		return null;
	}
	
	/**
	  * @Title:  yxIdAlarmContain 
	  * @Description:  TODO 查看该告警id是否是发生的严重告警
	  * @param yxId
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年6月14日下午5:10:49
	 */
	public static Boolean yxIdAlarmContain(Integer yxId) {
		boolean contains = false;
		try {
			contains = DeviceCache.yxIdAlarmList.contains(yxId);
			return contains;
		} catch (Exception e) {
			logger.debug("can not get yxIdAlarmList：{}:{}",yxId,e.getClass());
		}
		return contains;
	}
	
	/**
	  * @Title:  devId2plantId 
	  * @Description:  TODO 设备id对应的电站id
	  * @param devId
	  * @return: Integer
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年6月14日下午5:16:20
	 */
	public static Integer devId2plantId(Integer devId) {
		Integer plantId = null;
		try {
			plantId = DeviceCache.devId2plantId.get(devId);
			return plantId;
		} catch (Exception e) {
			logger.debug("can not get devId2plantId：{}:{}",devId,e.getClass());
		}
		return plantId;
	}
}
