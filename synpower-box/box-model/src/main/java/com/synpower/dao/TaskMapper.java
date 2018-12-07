package com.synpower.dao;

import com.synpower.bean.AllParam;
import com.synpower.bean.Task;

import java.util.List;
import java.util.Map;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    List<AllParam> selectTaskListByStatus(AllParam allParam);

    AllParam selectTasked(AllParam allParam);
}