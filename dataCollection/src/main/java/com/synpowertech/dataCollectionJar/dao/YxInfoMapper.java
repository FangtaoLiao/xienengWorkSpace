package com.synpowertech.dataCollectionJar.dao;

import com.synpowertech.dataCollectionJar.domain.YxInfo;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface YxInfoMapper {
 public  List<YxInfo> getInfo();
}
