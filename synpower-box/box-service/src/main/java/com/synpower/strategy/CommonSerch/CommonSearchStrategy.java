package com.synpower.strategy.CommonSerch;

import com.synpower.bean.PlantInfo;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.lang.ServiceException;
import com.synpower.service.PlantInfoService;
import com.synpower.service.StatisticalGainService;
import com.synpower.util.ServiceUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Author lz
 * @Description: 页面排序策略接口
 * @Date: 2018/11/6 15:08
 **/
public abstract class CommonSearchStrategy {
    private Logger log = Logger.getLogger(CommonSearchStrategy.class);
    @Autowired
    protected PlantInfoMapper plantInfoMapper;
    @Autowired
    protected PlantInfoService plantInfoService;
    @Autowired
    protected StatisticalGainService statisticalGainService;

    protected List<Integer> plantIdList;
    protected SearchCondition condition;
    protected int total = 0;

    //排序获取返回结果 数据库中排序时使用
    protected List<Map<String, Object>> orderAndGetInfo() {
        return addWeather2Info(getInfo(getSubList(orderAndGetList(), condition)));
    }


    //id排序  由数据库排序时使用
    public abstract List<Integer> orderAndGetList();

    //id分页
    protected <T> List<T> getSubList(List<T> plantList, SearchCondition condition) {
        total = plantList.size();
        int start = condition.getStart();
        int end = start + condition.getLength();
        end = end < total ? end : total;
        return plantList.subList(start, end);
    }

    //排序获取返回结果   实时数据用
    protected List<Map<String, Object>> orderAndGetInfoFromInfo(String con) {
        List<Map<String, Object>> resultList = getInfo(plantIdList);
        if (SearchCondition.ORDER_UP.equals(condition.getOrder())) {
            //升序
            Collections.sort(resultList, Comparator.comparingDouble(e -> Util.getDouble(e.get(con))));
        } else {
            //降序
            Collections.sort(resultList, Comparator.comparingDouble(e -> -Util.getDouble(e.get(con))));
        }
        return addWeather2Info(getSubList(resultList, condition));
    }


    protected List<Map<String, Object>> getInfo(List<Integer> plantIdList) {
        List<Map<String, Object>> ListFront = new ArrayList<>();
        //一次性查所有id,排序会乱
        //PlantInfo plantInfo = plantInfoMapper.getPlantById(plantIdList.get(0) + "");
        for (Integer id : plantIdList) {
            String sId = id + "";
            PlantInfo plantInfo = plantInfoMapper.getPlantById(sId);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", plantInfo.getId());
            resultMap.put("name", plantInfo.getPlantName());
            resultMap.put("addr", plantInfo.getPlantAddr());
            //显示单位统一为KW
            resultMap.put("capacity", plantInfo.getCapacityBase() / 1000);
            resultMap.put("location", plantInfo.getLoaction());
            resultMap.put("status", ServiceUtil.getPlantStatus(sId));
            Map<String, Object> historyIncome = statisticalGainService.getHistoryIncome2(sId);
            //日发电量
            double currPower = Util.getDouble(historyIncome.get("currPower"));
            resultMap.put("genDay", historyIncome.get("currPower"));
            //总发电量
            resultMap.put("genTotal", historyIncome.get("totalPower"));
            resultMap.put("singlePlantType", SystemCache.plantTypeList.get(sId));

            resultMap.put("ppr", plantInfo.getPpr(currPower));
            String contacts = plantInfo.getContacts();
            resultMap.put("contacts", contacts == null ? "--" : contacts);
            String contectsTel = plantInfo.getContectsTel();
            resultMap.put("contectsTel", contectsTel == null ? "--" : contectsTel);
            //try {
            //    resultMap.put("weather", plantInfoService.getPlantTodayWeather(id).getDayPictureUrl());
            //} catch (ServiceException e) {
            //    e.printStackTrace();
            //    log.error(new CommonSearchException("获取天气失败"), e);
            //}
            ListFront.add(resultMap);
        }
        return ListFront;
    }

    protected List<Map<String, Object>> addWeather2Info(List<Map<String, Object>> plantList) {
        for (Map<String, Object> plantMap : plantList) {
            int id = Util.getInt(plantMap.get("id"));
            try {
                plantMap.put("weather", plantInfoService.getPlantTodayWeather(id).getDayPictureUrl());
            } catch (ServiceException e) {
                e.printStackTrace();
                log.error("CommonSearch 获取电站天气失败id:" + id, e);
            }
        }
        return plantList;
    }

    public void setPlantIdList(List<Integer> plantIdList) {
        this.plantIdList = plantIdList;
    }

    public void setCondition(SearchCondition condition) {
        this.condition = condition;
    }

    public int getTotal() {
        return total;
    }
}
