package com.synpower.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.synpower.bean.AllParam;
import com.synpower.bean.Task;
import com.synpower.dao.TaskMapper;
import com.synpower.msg.session.User;
import com.synpower.service.SystemService;
import com.synpower.service.TaskService;
import com.synpower.util.CacheUtil0;
import com.synpower.util.ServiceUtil;
import com.synpower.util.Util;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    CacheUtil0 cacheUtil0;

    /**
     * @D  派发任务，当同样设备，同样告警信息时，先检查是否派发
     * @author SP0025
     * @param parMap
     * @param user
     * @return
     */
    @Override
    @Transactional
    public boolean distribution(Map<String, Object> parMap, User user) {
        String alarmName = Util.getString(parMap.get("alarmName"));
        Integer alarmId = Util.getInt(parMap.get("alarmId"));
        Integer deviceId = Util.getInt(parMap.get("deviceId"));
        Integer yxId = Util.getInt(parMap.get("yxId"));
        Integer plantId = Util.getInt(parMap.get("plantId"));
        Integer personId = Util.getInt(parMap.get("personId"));
        int userId = Util.getInt(user.getId());
        Task task = new Task();
        long currentTime = System.currentTimeMillis();
        task.setUserUpId(userId);
        task.setUserDownId(personId);
        task.setAlarmId(alarmId);
        task.setYxId(yxId);
        task.setDeviceId(deviceId);
        task.setPlantId(plantId);
        task.setCreateTime(currentTime);
        task.setUpdateTime(currentTime);
        task.setLastModifyUser(userId);
        task.setTaskStatus("0");
        task.setTaskValid("0");
        task.setTaskName(alarmName);
        CacheUtil0 cacheUtil0 = new CacheUtil0();
        try{
              cacheUtil0.hdel(deviceId+"",yxId+"");
        }catch (Exception e){
              System.err.print(e.getMessage());
        }
        AllParam allParam = new AllParam();
        allParam.setDev_id(deviceId);
        allParam.setYx_id(yxId);
        if( taskMapper.selectTasked(allParam)!=null){
            return false;
        }
        return taskMapper.insertSelective(task) == 1 ? true : false;
    }

    @Override
    @Transactional
    public boolean reback(Integer taskId, Integer userId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null)
            return false;
        Integer taskStatus = Util.getInt(task.getTaskStatus());
        if (taskStatus > 2 || taskStatus < 1)
            return false;
        taskStatus -= 1;
        task.setTaskStatus(taskStatus + "");
        task.setLastModifyUser(userId);
        task.setUpdateTime(System.currentTimeMillis());

        cacheUtil0.updateTaskStatus(task.getDeviceId() + "", task.getYxId() + "", taskStatus + "");
        return taskMapper.updateByPrimaryKeySelective(task) == 1 ? true : false;
    }

    @Override
    public boolean delete(Integer taskId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null)
            return false;
        task.setTaskValid(1 + "");
        return taskMapper.updateByPrimaryKeySelective(task) == 1 ? true : false;
    }

    /**
     * @method 获取任务管理列表
     * @param parMap
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public List<AllParam> getList(Map<String, Object> parMap) throws InvocationTargetException, IllegalAccessException {
        AllParam allParam = new AllParam();
        //allParam.setOrderName(Util.get);
        //List<Object> taskList = taskMapper.getTaskList(allParam);
        BeanUtils.populate(allParam,parMap);  //将map中与allParam属性名相同的键将值赋给allParam
        // List list = (List)parMap.get("orderName");
//        Object o = parMap.get("orderName");
//        JSONObject jsonObject = JSONObject.parseObject(o.toString());
//        allParam.setOrderName(jsonObject.getString("orderName"));
//        allParam.setOrder(jsonObject.getString("order"));
        HashMap hashMap = new HashMap();
        hashMap = (HashMap) ServiceUtil.parseOrderName(parMap,hashMap);
        System.out.print(hashMap.get("order"));
        allParam.setOrder((String) hashMap.get("order"));
        allParam.setOrderName((String) hashMap.get("orderName"));
        if(Util.isEmpty(allParam.getLength())){
             allParam.setLength(10);
        }
        if(Util.isEmpty(allParam.getStart())){
            allParam.setStart(0);
        }
        return taskMapper.selectTaskListByStatus(allParam);
    }
}
