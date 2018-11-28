package com.synpower.strategy.CommonSerch;

import com.synpower.util.Util;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class StrategyPpr extends CommonSearchStrategy {
    @Override
    public List<Integer> orderAndGetList() {
        //这里自己重写orderAndGetInfo避免重复计算
        return null;
    }

    @Override
    protected List<Map<String, Object>> orderAndGetInfo() {
        return orderAndGetInfoFromInfo(SearchCondition.CON_PPR);
    }


    private double getPPr(double genDay, double capacity) {
        return Util.divideDouble(genDay, capacity, 1);
    }
}
