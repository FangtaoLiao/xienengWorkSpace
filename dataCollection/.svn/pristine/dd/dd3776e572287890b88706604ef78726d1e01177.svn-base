package com.synpowertech.dataCollectionJar.timedTask;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.synpowertech.dataCollectionJar.domain.DataYx;
import com.synpowertech.dataCollectionJar.initialization.DeviceCache;
import com.synpowertech.dataCollectionJar.initialization.JedisUtil;
import com.synpowertech.dataCollectionJar.service.IDataSaveService;

/**
 * ***************************************************************************
 *
 * @version -----------------------------------------------------------------------------
 * VERSION           TIME                BY           CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 * 1.0    2018年4月26日上午9:54:01   SP0010             create
 * -----------------------------------------------------------------------------
 * @Package: com.synpowertech.dataCollectionJar.timedTask
 * @ClassName: JudgeConnStatusTask
 * @Description: TODO 更新设备通讯状态任务
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 * ****************************************************************************
 */
public class JudgeConnStatusTask implements Job, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JudgeConnStatusTask.class);

    @Autowired
    IDataSaveService dataSaveServiceTemp;

    static IDataSaveService dataSaveService;

    public void afterPropertiesSet() throws Exception {
        dataSaveService = dataSaveServiceTemp;
        logger.info("DataSaveTask for JudgeConnStatus  inits succefully！");
    }

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("JudgeConnStatusTask start!");
        if (DeviceCache.devIdList.size() > 0) {
            String connStatus = null;
            String sysTimeStr = null;
            //数据库告警记录
            List<DataYx> yc4yxDataList = new ArrayList<DataYx>(5);
            for (Integer devId : DeviceCache.devIdList) {
                //是否为不判断状态的特殊设备
                boolean isSDev = DeviceCache.devJoinIdList.contains(devId);
                connStatus = JedisUtil.getStringInMap(devId.toString(), "conn_status");
                //已是断连状态跳过，恢复状态由信息解析写入
                if (connStatus == null) {
                    continue;
                }
                if ("0".equals(connStatus) && !isSDev) {
                    continue;
                }
                //若是正常通讯状态，超时判定为通讯中断，写一次告警记录
                if (isSDev) {
                    if ("1".equals(connStatus)) {
                        continue;
                    }
                    //特殊设备强制把状态设置为通讯中
                    JedisUtil.setStringInMap(devId.toString(), "conn_status", "1");
                }
                if ("1".equals(connStatus)) {
                    sysTimeStr = JedisUtil.getStringInMap(devId.toString(), "sys_time");
                    //dataTimeStr = JedisUtil.getStringInMap(devId.toString(), "sys_time");
                    if (sysTimeStr != null) {
                        //更新时间差大于6分钟，判定为设备通讯中断 6*60*1000
                        if ((System.currentTimeMillis() - Long.valueOf(sysTimeStr)) > 360000) {
                            //非特殊设备更改状态为通讯中断
                            JedisUtil.setStringInMap(devId.toString(), "conn_status", "0");
                            //插入告警记录,都是断连
                            yc4yxDataList.add(new DataYx(devId, -2, Long.valueOf(sysTimeStr), null, "1", "0"));
                        }
                    }
                }
            }

            if (yc4yxDataList.size() > 0) {
                dataSaveService.yxDataSave(yc4yxDataList);
            }
        }

        logger.info("JudgeConnStatusTask ends!");
    }

}
