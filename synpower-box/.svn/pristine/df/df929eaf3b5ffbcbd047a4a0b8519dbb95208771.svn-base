package com.synpower.strategy.CommonSerch;

import com.synpower.util.SpringUtil;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lz
 * @Description: 页面排序策略环境类
 * @Date: 2018/11/6 15:08
 **/
@Component
public class CommonSearchContext {
    private Logger log = Logger.getLogger(CommonSearchContext.class);

    private CommonSearchStrategy getStrategy(List<Integer> plantIdList, SearchCondition condition) {
        String strategyName = convertStr(condition.getOrderName());
        try {
            //反射方式
            //serchStrategy = (CommonSearchStrategy) Class.forName(strategyName).
            //        getConstructor(List.class).newInstance(list);
            CommonSearchStrategy css = (CommonSearchStrategy) SpringUtil.getBeanById(strategyName);
            css.setPlantIdList(plantIdList);
            css.setCondition(condition);
            return css;
        } catch (Exception e) {
            CommonSearchException csE = new CommonSearchException("CommonSearch策略创建失败!", e);
            log.error("CommonSearch策略策略创建失败!", csE);
            throw csE;
        }
    }

    public HashMap<String, Object> orderAndGetInfo(List<Integer> plantIdList, SearchCondition condition) {
        CommonSearchStrategy serchStrategy = getStrategy(plantIdList, condition);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("plantsInfo",serchStrategy.orderAndGetInfo());
        int total = serchStrategy.getTotal();
        resultMap.put("recordsTotal", total);
        resultMap.put("recordsFiltered", total);
        return resultMap;
    }

    private String convertStr(String orderName) {
        //if (Util.isEmpty(orderName)) {
        //    orderName = "Default";
        //}
        char[] chars = orderName.toCharArray();
        char fChar = chars[0];
        if (fChar >= 'a' && fChar <= 'z') {
            //fChar 单独有一份值
            //fChar -= 32;
            chars[0] -= 32;
        } else if (fChar >= 'A' && fChar <= 'Z') {

        } else {
            CommonSearchException e = new CommonSearchException("CommonSearch策略名非法!");
            log.error("CommonSearch策略名非法!", e);
            throw e;
        }
        //orderName = "com.synpower.strategy.CommonSerch.Strategy" + String.valueOf(chars);
        orderName = "strategy" + String.valueOf(chars);
        return orderName;
    }

    @Test
    public void test1() {
        System.out.println(convertStr("dsadas"));
    }
}
