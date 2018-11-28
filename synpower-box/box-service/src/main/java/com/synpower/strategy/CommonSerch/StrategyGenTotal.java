package com.synpower.strategy.CommonSerch;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class StrategyGenTotal extends CommonSearchStrategy {
    @Override
    public List<Integer> orderAndGetList() {
        return null;
    }

    @Override
    protected List<Map<String, Object>> orderAndGetInfo() {
        return orderAndGetInfoFromInfo(SearchCondition.CON_GENTOTAL);
    }

}
