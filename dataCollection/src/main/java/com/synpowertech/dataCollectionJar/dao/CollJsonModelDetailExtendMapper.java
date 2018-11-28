package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollJsonModelDetailExtend;

public interface CollJsonModelDetailExtendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollJsonModelDetailExtend record);

    int insertSelective(CollJsonModelDetailExtend record);

    CollJsonModelDetailExtend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollJsonModelDetailExtend record);

    int updateByPrimaryKey(CollJsonModelDetailExtend record);
    
    //根据点表模型顺序查询全部
    List<CollJsonModelDetailExtend> selectOrderByModel();
}