package com.synpower.service;

import com.synpower.bean.AllParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.session.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface TaskService {
    /**
     * @Author lz
     * @Description: 任务派发
     * @param: [parMap]
     * @return: {boolean}
     * @Date: 2018/11/21 16:29
     **/
    boolean distribution(Map<String, Object> parMap, User user);

    /**
     * @Author lz
     * @Description: 任务回退
     * @param: [taskId]
     * @return: {boolean}
     * @Date: 2018/11/21 16:29
     **/
    boolean reback(Integer taskId, Integer userId);

    boolean delete(Integer taskId);

    /**
     * @Author lz
     * @Description:获取任务列表
     * @param: [parMap]
     * @return: {java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     * @Date: 2018/11/21 16:37
     **/
    List<AllParam> getList(Map<String, Object> parMap) throws InvocationTargetException, IllegalAccessException;
}
