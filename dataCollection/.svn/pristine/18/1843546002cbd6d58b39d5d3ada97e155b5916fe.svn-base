package com.synpowertech.dataCollectionJar.service.impl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.synpowertech.dataCollectionJar.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.Main2Start;
import com.synpowertech.dataCollectionJar.initialization.MqttMessagePublisher;
import com.synpowertech.dataCollectionJar.service.IYkYtService;
import com.synpowertech.dataCollectionJar.timedTask.MqttClientActiveSubTask;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.rmi
 * @ClassName: YkYtServiceImpl
 * @Description: RMI暴露遥控遥调等方法的实现
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月6日下午2:16:50   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
@Service
public class YkYtServiceImpl  implements IYkYtService {

	// 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常
    public YkYtServiceImpl() throws RemoteException {
        super();
    }
    final Logger logger = LoggerFactory.getLogger(YkYtServiceImpl.class);

    /**
  	  * @Title:  sayHello
  	  * @Description: 测试rmi暴露成功与否
  	  * @lastEditor:  SP0010
  	  * @lastEdit:  2017年11月6日下午2:13:09
  	 */
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }

    /**
	  * @Title:  setTimeXml
	  * @Description: 暴露对指定数采节点下设备的对时方法
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月6日下午2:13:09
	 */
	public boolean setTimeXml(String collector_id) throws RemoteException {
		boolean setTimeResult = MqttMessagePublisher.setTime(XmlParseUtil.setTimeXml(collector_id));
		return setTimeResult;
	}

	/**
	  * @Title:  ykYt
	  * @Description: 暴露遥控遥调的远程调用方法，只对有效和正常状态设备有效
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月6日下午2:13:09
	 */
	public boolean ykYt(String sessionId,String collector_id, Map<String, Map<String, String>> ykYtMap) throws RemoteException {
//		logger.info("sessionId：" + sessionId);
//		String map2XmlStr = XmlParseUtil.map2XmlStr(sessionId,collector_id, ykYtMap);
//		logger.info("targetXml：!" + map2XmlStr);
//		
//		
        boolean ykYtReuslt = false;

        //TODO 区分新旧数采
        if (DeviceCache.oldCollSnIdMap.containsKey(collector_id)) {
            ykYtReuslt = MqttMessagePublisher.ykYt(XmlParseUtil.map2XmlStr(sessionId, collector_id, ykYtMap));
        }

        //新数采控制
        if (DeviceCache.newCollSnIdMap.containsKey(collector_id)) {
            Integer clientIdx = CacheMappingUtil.collSn2ClientIdx(collector_id);
            if (clientIdx != null) {
                String map2JsonStr = null;
                try {
                    map2JsonStr = JsonParseUtil.map2JsonStr(sessionId, collector_id, ykYtMap);
                    ykYtReuslt = DeviceCache.clientList.get(clientIdx).publishCtrlMsg(collector_id, map2JsonStr);
                } catch (Exception e) {
                    logger.info("newCollector ykYt error:{},{}", collector_id + "_" + sessionId, e.getClass());
                }
                logger.info(collector_id + ":" + map2JsonStr);
            }
        }


//		boolean ykYtReuslt = MqttMessagePublisher.ykYt(map2XmlStr);

        //读取模拟结果
/*		SAXReader reader = new SAXReader(); // User.hbm.xml表示你要解析的xml文档
		Document document = null;

		try {
			document = reader.read(new File("数采1模拟报文/ykytResultMaqTest.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 转化成xml字符串
		StringWriter sw = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");

		try {
			XMLWriter xmlWriter = new XMLWriter(sw, format);
			xmlWriter.write(document);

		} catch (IOException e) {
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
			}
		}

		String xmlStrTemp = sw.toString();

		// 替换成当前时间
		String xmlTimeStr = TimeUtil4Xml.getXmlTimeStr();
		String xmlStr = xmlStrTemp.replaceAll("当前时间", xmlTimeStr).replaceAll("sessionId", sessionId).replaceAll("采集器", collector_id);
		String targetStr = null;
		
		Set<Entry<String, Map<String, String>>> entrySet = ykYtMap.entrySet();
		for (Entry<String, Map<String, String>> entry : entrySet) {
			Map<String, String> value = entry.getValue();
			Set<Entry<String, String>> entrySet2 = value.entrySet();
			for (Entry<String, String> entry2 : entrySet2) {
				String[] strings = entry2.getKey().split("_");
				targetStr = xmlStr.replaceAll("目标点", strings[2]).replaceAll("目标值", entry2.getValue());
			}
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//模拟返回遥控遥调结果
		try {
			XmlParseUtil.xmlResultStr2Map(targetStr);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        return ykYtReuslt;
    }

    /**
     * @Title: refreshCache
     * @Description: 刷新设备缓存的方法，当有维修，报废，或新增后可以手动刷新设备缓存，及时检测到现有设备
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月6日下午2:13:09
     */
    public boolean refreshCache() {
        try {
            ThreadPool.executeXmlParse_task(() -> DeviceCache.refreshDeviceCache());
        }catch (Exception e){
            logger.error("Refresh failed {}",e.getMessage());
            return false;
        }
        //boolean refreshCacheResult = DeviceCache.refreshDeviceCache();
        return true;
    }

    /**
     *
     */
    public boolean dataModelSet(String collSn, Map<Integer, Map<String, Integer>> dataModelSetMap) throws RemoteException {

        boolean modelSetResult = false;
        //新数采控制
        if (DeviceCache.newCollSnIdMap.containsKey(collSn)) {
            //获取订阅该数采的客户端索引
            Integer clientIdx = CacheMappingUtil.collSn2ClientIdx(collSn);
            if (clientIdx != null) {
                String setMap2JsonStr = null;
                try {
                    setMap2JsonStr = JsonParseUtil.setMap2JsonStr(collSn, dataModelSetMap);
                    modelSetResult = DeviceCache.clientList.get(clientIdx).publishCtrlMsg(collSn, setMap2JsonStr);
                } catch (IndexOutOfBoundsException e) {
                    //TODO 索引越界,尝试刷新缓存后再下发
                    DeviceCache.refreshDeviceCache();
                    MqttClientActiveSubTask.refreshClients();
                    try {
                        modelSetResult = DeviceCache.clientList.get(clientIdx).publishCtrlMsg(collSn, setMap2JsonStr);
                    } catch (MqttException e1) {
                        logger.info("newCollector ykYt error:{},{}", collSn, e.getClass());
                    }
                } catch (Exception e) {
                    logger.info("newCollector ykYt error:{},{}", collSn, e.getClass());
                }
                logger.info(collSn + ":" + setMap2JsonStr);
            }
        }
        return modelSetResult;
    }
}