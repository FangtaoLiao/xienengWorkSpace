package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.CollModel;

public interface CollModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CollModel record);

    int insertSelective(CollModel record);

    CollModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollModel record);

    int updateByPrimaryKey(CollModel record);

    //选着所有有效的点表的id
	List<Integer> selectAllId();
}