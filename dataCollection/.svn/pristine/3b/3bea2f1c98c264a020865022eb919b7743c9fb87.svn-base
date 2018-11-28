package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataBms;
import org.apache.ibatis.annotations.Param;

public interface DataBmsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataBms record);

    int insertSelective(DataBms record);

    DataBms selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataBms record);

    int updateByPrimaryKey(DataBms record);

	int insertBatch(List<DataBms> ycRecordList);

	List<DataBms> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
}