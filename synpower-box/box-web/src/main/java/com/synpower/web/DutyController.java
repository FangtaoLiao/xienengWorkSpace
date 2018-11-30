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
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


@RestController
@RequestMapping(value = "/duty")
public class DutyController extends ErrorHandler {
    private Logger log = Logger.getLogger(DutyController.class);
    @Autowired
    private TaskService taskService;

    @RequestMapping("/distribution")
    public MessageBean distribution(@RequestBody String json) throws SessionTimeoutException, SessionException {
        boolean flag = taskService.distribution(getJsonMap(), getUser());
        return getMB(flag, "任务下发失败");
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
    @RequestMapping("/notDistributing")
    public MessageBean notDistributing(@RequestBody String json) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> jsonMap = getJsonMap();
        jsonMap.put("taskStatus",0);
        return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
    }
    @RequestMapping("/distributed")
    public MessageBean distributed(@RequestBody String json) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> jsonMap = getJsonMap();
        jsonMap.put("taskStatus",2);
        return MessageBeanUtil.getOkMB(taskService.getList(jsonMap));
    }

}
