package com.synpowertech.dataCollectionJar.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: SimplifySignalGuidUtil
 * @Description: 简化接收到信号存库日志
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月23日下午2:57:11   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class SimplifySignalGuidUtil {

	/**
	  * @Title:  simplifyMap 
	  * @Description:  以"_"作为分隔符的guid,只保留信号类型和转发点号，例如"YX_45"
	  * @param signalGuidMap
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年12月23日下午3:14:12
	 */
	@Deprecated
	public static String simplifyMap(Map<String, String> signalGuidMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Set<Entry<String, String>> entrySet = signalGuidMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			
			String keyTemp = entry.getKey();
			//如果data_time和sys_time,直接使用
			if ("sys_time".equals(keyTemp) || "data_time".equals(keyTemp)) {
				resultMap.put(keyTemp, entry.getValue());
			} else {
				String[] signalArr = keyTemp.split("_", 2);
				resultMap.put(signalArr[1], entry.getValue());
			}
			
		}
		return resultMap.toString();
	}
}
