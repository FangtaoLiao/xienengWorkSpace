package com.synpower.dao;

import com.synpower.bean.ElecPowerFactor;

public interface ElecPowerFactorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ElecPowerFactor record);

    int insertSelective(ElecPowerFactor record);

    ElecPowerFactor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ElecPowerFactor record);

    int updateByPrimaryKey(ElecPowerFactor record);
}