package com.synpower.strategy.CommonSerch;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class StrategyCapacity extends CommonSearchStrategy {

    @Override
    public List<Integer> orderAndGetList() {
        //容量已经在一开始就做了排序过滤,重写orderAndGetInfo,不再次排序
        return plantIdList;
    }

    //@Override
    //protected List<Map<String, Object>> orderAndGetInfo() {
    //    return addWeather2Info(getInfo(plantIdList));
    //}
}
