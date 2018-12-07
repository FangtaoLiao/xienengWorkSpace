package com.synpower.web;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.TaskService;
import com.synpower.util.MessageBeanUtil;
import com.synpower.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.MacSpi;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @author SP0025
 * @Description 任务管理
 * @Version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年12月3日下午2:30:05   SP0025             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@RestController
@RequestMapping(value = "/duty")
public class DutyController extends ErrorHandler {
    private Logger log = Logger.getLogger(DutyController.class);
    @Autowired
    private TaskService taskService;

    @RequestMapping("/distribution")
    public MessageBean distribution(@RequestBody String json) throws SessionTimeoutException, SessionException {
        boolean flag = taskService.distribution(getJsonMap(), getUser());
        return getMB(flag, "请检查该任务是否已派发，或操作失误");
    }

    @RequestMapping("/reback")
    public MessageBean reback(@RequestBody String json) throws SessionTimeoutException, SessionException {
        boolean flag = taskService.reback(Util.getInt(getJsonValue4Key("id")), Util.getInt(getUser().getId()));
        return getMB(flag, "任务回退失败");
    }

    @RequestMapping("/delete")
    public MessageBean delete(@RequestBody String json) {
        boolean flag = taskService.delete(Util.getInt(getJsonValue4Key("id")));
        return getMB(flag, "任务删除失败");
    }

    /**
     * @author SP0025
     * @method 已完成列表
     * @param json
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/distributed")
    public MessageBean distributed(@RequestBody String json) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> jsonMap = getJsonMap();
        jsonMap.put("taskStatus", 2);
        return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
    }

    /**
     * @mehtod 处理中列表
     * @param json
     * @author SP0025
     * @return
     */
    @RequestMapping("/distributing")
    public MessageBean distributing(@RequestBody String json) {
        try {
            Map<String, Object> jsonMap = getJsonMap();
            jsonMap.put("taskStatus", 1);
            return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
        }catch (Exception e){
            log.debug(e.getMessage());
            return MessageBeanUtil.getOkMB("操作失败");
        }
    }
    @RequestMapping("/getDutys")
    public MessageBean getDutys(@RequestBody String json) {
        try {
            Map<String, Object> jsonMap = getJsonMap();
            jsonMap.put("taskStatus", 1);
            return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
        }catch (Exception e){
            log.debug(e.getMessage());
            return MessageBeanUtil.getOkMB("操作失败");
        }
    }
    /**
     * @author SP0025
     * @method 未派发列表
     * @param json
     * @return
     */
    @RequestMapping(value = "/notDistributed" )
    public MessageBean notDistributed(@RequestBody String json){
       try {
           Map<String, Object> jsonMap = getJsonMap();
           jsonMap.put("taskStatus", 0);
           return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
       }catch (Exception e){
           log.debug(e.getMessage());
           e.printStackTrace();
           return MessageBeanUtil.getOkMB("操作失败");
       }
    }

}
