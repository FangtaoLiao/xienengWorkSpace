package com.synpower.strategy.CommonSerch;

import com.synpower.dao.PlantInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class StrategyDefault extends CommonSearchStrategy {

    @Autowired
    PlantInfoMapper plantInfoMapper;
    @Override
    public List<Integer> orderAndGetList() {
        return plantIdList;
    }
}
