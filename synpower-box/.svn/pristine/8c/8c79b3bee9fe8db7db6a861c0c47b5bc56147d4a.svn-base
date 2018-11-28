package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.DataEnvMonitor;

public interface DataEnvMonitorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataEnvMonitor record);

    int insertSelective(DataEnvMonitor record);

    DataEnvMonitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataEnvMonitor record);

    int updateByPrimaryKey(DataEnvMonitor record);
    /**
     * 
     * @author ybj
     * @date 2018年11月7日 下午5:45:34
     * @Description -_-
     * @param map
     * @return
     */
    List<Map<String,Object>> getCurrentRadiationAndTemp(Map map);
}