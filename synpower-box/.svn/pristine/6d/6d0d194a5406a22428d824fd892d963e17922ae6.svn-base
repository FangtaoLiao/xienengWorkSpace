package com.synpower.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysPowerPrice;
import com.synpower.bean.SysPowerPriceDetail;
import com.synpower.constant.DTC;
import com.synpower.constant.Dimension;
import com.synpower.constant.PTAC;
import com.synpower.constant.PriceType;
import com.synpower.constant.TimeEXP;
import com.synpower.constant.UNIT;
import com.synpower.dao.DataElectricMeterMapper;
import com.synpower.dao.ElecCapDecMapper;
import com.synpower.dao.ElecFactorTableMapper;
import com.synpower.dao.ElectricStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysPowerPriceDetailMapper;
import com.synpower.dao.SysPowerPriceMapper;
import com.synpower.lang.EnergyMonitorException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.session.User;
import com.synpower.service.EnergyMonitorService;
import com.synpower.util.ApproUtil;
import com.synpower.util.SFUtil;
import com.synpower.util.SystemCache;
import com.synpower.util.TimeUtil;
import com.synpower.util.UnitUtil;
import com.synpower.util.Util;

@Service
public class EnergyMonitorServiceImpl implements EnergyMonitorService {

    @Autowired
    private PlantInfoServiceImpl plantInfoService;
    @Autowired
    private DataElectricMeterMapper dataElectricMeterMapper;
    @Autowired
    private ElectricStorageDataMapper electricStorageDataMapper;
    @Autowired
    private SysPowerPriceDetailMapper sysPowerPriceDetailMapper;
    @Autowired
    private SysPowerPriceMapper sysPowerPriceMapper;
    @Autowired
    private ElecCapDecMapper elecCapDecMapper;
    @Autowired
    private PlantInfoMapper plantInfoMapper;
    @Autowired
    private ElecFactorTableMapper elecFactorTableMapper;

    private Logger log = Logger.getLogger(EnergyMonitorServiceImpl.class);

    /**
     * 月峰谷电量统计
     *
     * @return
     * @throws ServiceException
     * @throws SessionTimeoutException
     * @throws SessionException
     * @author ybj
     * @date 2018/8/23
     */

    @Override
    public List<Map<String, Object>> getElecKPI(User u, long curr, long next)
            throws SessionException, SessionTimeoutException, ServiceException {

        // 结果返回集合声明
        List<Map<String, Object>> result = new ArrayList<>();

        int plantId = getPlantId(u);

        if (plantId == -1) {
            return null;
        }
        // 参数map
        Map<String, Object> map = new HashMap<>();

        map.put("plantId", plantId);
        map.put("curr", curr);
        map.put("next", next);

        double jian = 0d;
        double ping = 0d;
        double feng = 0d;
        double gu = 0d;

        // 判断是否是当前月，如果是当前月就直接去缓存中取 缓存中需要取当前月和当天
        boolean isCurrMonth = TimeUtil.isCurrMonth(curr);
        if (isCurrMonth) {
            // 当天
            Map<String, Double> currDay = SystemCache.currentDataOfPlant.get(String.valueOf(plantId));
            jian += currDay.get("yjian");
            feng += currDay.get("yfeng");
            ping += currDay.get("yping");
            gu += currDay.get("ygu");
            // 本月
            Map<String, Double> currMonth = SystemCache.historyDataOfEEPlant.get(plantId).get(0);
            jian += currMonth.get("yjian");
            feng += currMonth.get("yfeng");
            ping += currMonth.get("yping");
            gu += currMonth.get("ygu");

            // 如果不是当前月就从数据库中查询
        } else {
            // 数据库查询当前时间段尖峰平谷
            Map<String, Double> kpiMap = electricStorageDataMapper.getElecKPI(map);

            if (Util.isNotBlank(kpiMap)) {

                // 得到当前月份尖峰平谷值
                jian += Util.getDouble(kpiMap.get("yjian"));
                feng += Util.getDouble(kpiMap.get("yfeng"));
                ping += Util.getDouble(kpiMap.get("yping"));
                gu += Util.getDouble(kpiMap.get("ygu"));
            }
        }

        // 尖峰平谷四舍五入保留两位小数
        jian = ApproUtil.getRoundDouble(jian);
        feng = ApproUtil.getRoundDouble(feng);
        ping = ApproUtil.getRoundDouble(ping);
        gu = ApproUtil.getRoundDouble(gu);

        // 尖
        map.clear();
        map.put("name", "0");
        map.put("value", jian);
        map.put("unit", "度");
        result.add(map);
        // 峰
        map = new HashMap<>();
        map.put("name", "1");
        map.put("value", feng);
        map.put("unit", "度");
        result.add(map);
        // 平
        map = new HashMap<>();
        map.put("name", "2");
        map.put("value", ping);
        map.put("unit", "度");
        result.add(map);
        // 谷
        map = new HashMap<>();
        map.put("name", "3");
        map.put("value", gu);
        map.put("unit", "度");
        result.add(map);

        // 单位转换
        UnitUtil.ConvertElec01(result);

        return result;

    }

    /**
     * @return com.synpower.msg.MessageBean
     * @Author lz
     * @Description 日月年用电统计
     * @Date 15:02 2018/8/23
     * @Param [user]
     **/
    @Override
    public HashMap<String, Object> getUseElec(User user)
            throws SessionTimeoutException, SessionException, EnergyMonitorException {
        int plantId = getPlantId(user);
        if (plantId == -1) {
            throw new EnergyMonitorException("plantId isEmpty userId is" + user.getId());
        }
        // 实际花费+电量
        // HashMap<String, Double> brMap = getEByPlantId(plantId);//查询数据库的实现
        // 缓存实现
        Map<String, Double> dayEnerMap = SystemCache.currentDataOfPlant.get(String.valueOf(plantId));
        Map<String, Double> monEnerMap = SystemCache.historyDataOfEEPlant.get(plantId).get(0);
        Map<String, Double> yearEnerMap = SystemCache.historyDataOfEEPlant.get(plantId).get(1);
        HashMap<String, Double> brMap = new HashMap<>();
        Double dayYg = dayEnerMap.get("yg");
        Double dayPrice = dayEnerMap.get("price");
        brMap.put("dayElec", ApproUtil.getRoundDouble(dayYg));
        brMap.put("dayElecPrice", ApproUtil.getRoundDouble(dayPrice));
        brMap.put("monthElec", monEnerMap.get("yg") + dayYg);
        brMap.put("monthElecPrice", monEnerMap.get("price") + dayPrice);
        brMap.put("yearElec", yearEnerMap.get("yg") + dayYg);
        brMap.put("yearElecPrice", yearEnerMap.get("price") + dayPrice);

        // 基础电费
        String date = Util.getCurrentTimeStr("yyyy-M");
        HashMap<String, Double> bPMap = getYBaseTakeByPalntId(plantId, date);
        brMap.put("monthElecPrice",
                ApproUtil.getRoundDouble(brMap.get("monthElecPrice") + bPMap.get("cbMonthElecPrice")));
        brMap.put("yearElecPrice", ApproUtil.getRoundDouble(brMap.get("yearElecPrice") + bPMap.get("cbYearclecPrice")));
        // TODO 惩罚奖励金额
        HashMap<String, Object> resultMap = new HashMap<>(brMap);

        return resultMap;
    }

    /**
     * @return java.util.HashMap
     * @Author lz
     * @Description 获取某年1月至传入月的基础电费
     * @Date 14:43 2018/8/24
     * @Param [plantId, date]
     **/
    public HashMap<String, Double> getYBaseTakeByPalntId(int plantId, String date) throws EnergyMonitorException {
        String[] dS = date.split("-");
        HashMap<Object, Object> paMap = new HashMap<>();
        paMap.put("plantId", plantId);
        paMap.put("year", dS[0]);
        Double allValue = 0d;
        HashMap<String, Double> resultMap = new HashMap<>();
        for (Integer i = Integer.valueOf(dS[1]); i > 0; i--) {
            Double monValue = 0d;
            try {
                monValue = SystemCache.plantEclecCap.get(String.valueOf(plantId)).get(dS[0]).get(String.valueOf(i))
                        .get(2);
                monValue = ApproUtil.getRoundDouble(monValue);
            } catch (NullPointerException e) {
                log.debug(plantId + "电站没有" + dS[0] + "年" + i + "月的申报容量!");
            }
            if (i == Integer.valueOf(dS[1])) {
                resultMap.put("cbMonthElecPrice", monValue);
            }
            allValue += monValue;
        }
        resultMap.put("cbYearclecPrice", ApproUtil.getRoundDouble(allValue));
        return resultMap;
    }

    /**
     * @return java.util.HashMap
     * @Author lz
     * @Description 获取电站所有设备日月年用电
     * @Date 17:20 2018/8/23
     * @Param [plantId]
     **/
    public HashMap<String, Double> getEByPlantId(Integer plantId) throws EnergyMonitorException {
        // 电站id去获得所有设备
        Map<Integer, List<Integer>> deviceMap = SystemCache.deviceOfPlant.get(Integer.valueOf(plantId));
        if (CollectionUtils.isEmpty(deviceMap)) {
            throw new EnergyMonitorException("deviceMap isEmpty and plantId is " + plantId);
        }
        // 获取电表
        List<Integer> elecList = deviceMap.get(DTC.ELECTRIC_METER);
        if (CollectionUtils.isEmpty(elecList)) {
            throw new EnergyMonitorException("elecList isEmpty and plantId is " + plantId);
        }
        HashMap resultMap = new HashMap<String, Double>();
        // 一个电站日月年的电量及花费
        Double dayElec = 0d;
        Double dayElecPrice = 0d;
        Double monthElec = 0d;
        Double monthElecPrice = 0d;
        Double yearElec = 0d;
        Double yearElecPrice = 0d;
        for (int i = 0; i < elecList.size(); i++) {
            Integer integer = elecList.get(i);
            // 单个设备结果累加
            HashMap<String, Double> aMap = getUseElecByElecId(integer, plantId);
            dayElec += aMap.get("dayElec");
            dayElecPrice += aMap.get("dayElecPrice");
            monthElec += aMap.get("monthElec");
            monthElecPrice += aMap.get("monthElecPrice");
            yearElec += aMap.get("yearElec");
            yearElecPrice += aMap.get("yearElecPrice");
        }
        resultMap.put("dayElec", ApproUtil.getRoundDouble(dayElec));
        resultMap.put("dayElecPrice", ApproUtil.getRoundDouble(dayElecPrice));
        resultMap.put("monthElec", ApproUtil.getRoundDouble(monthElec));
        resultMap.put("monthElecPrice", ApproUtil.getRoundDouble(monthElecPrice));
        resultMap.put("yearElec", ApproUtil.getRoundDouble(yearElec));
        resultMap.put("yearElecPrice", ApproUtil.getRoundDouble(yearElecPrice));
        return resultMap;
    }

    /**
     * @return java.util.HashMap
     * @Author lz
     * @Description 单个设备的日月年用电
     * @Date 17:20 2018/8/23
     * @Param [elecId, plantId]
     **/
    @Override
    public HashMap<String, Double> getUseElecByElecId(Integer elecId, Integer plantId) throws EnergyMonitorException {

        // 获取今日用电
        HashMap<String, Double> dMap = getEByDeviceIdAndTime(elecId, plantId);
        Double dayElec = dMap.get("dayElec");
        Double dayElecPrice = dMap.get("dayElecPrice");
        HashMap resultMap = new HashMap<String, Double>();
        resultMap.put("dayElec", dayElec);
        resultMap.put("dayElecPrice", dayElecPrice);
        // 获取本月用电
        HashMap<String, Double> mMap = getEByplantIdAndTime(plantId, TimeUtil.getMon0(), TimeUtil.getDay0());
        Double monthElec = mMap.get("elec") + dayElec;
        Double monthElecPrice = mMap.get("elecPrice") + dayElecPrice;
        resultMap.put("monthElec", monthElec);
        resultMap.put("monthElecPrice", monthElecPrice);
        // 获取今年用电
        HashMap<String, Double> yMap = getEByplantIdAndTime(plantId, TimeUtil.getYear0(), TimeUtil.getMon0());
        Double yearElec = yMap.get("elec") + monthElec;
        Double yearElecPrice = yMap.get("elecPrice") + monthElecPrice;
        resultMap.put("yearElec", yearElec);
        resultMap.put("yearElecPrice", yearElecPrice);
        return resultMap;
    }

    /**
     * @return java.util.HashMap
     * @Author lz
     * @Description 获取一个设备一天用电
     * @Date 17:17 2018/8/23
     * @Param [elecId, plantId]
     **/
    public HashMap<String, Double> getEByDeviceIdAndTime(Integer elecId, Integer plantId)
            throws EnergyMonitorException {
        HashMap<String, Object> paMap = new HashMap<>();
        // 获取今日用电
        paMap.put("deviceId", elecId);
        paMap.put("startTime", TimeUtil.getDay0());
        Map energyByDeviceIdAndTime = dataElectricMeterMapper.getEnergyByDeviceIdAndTime(paMap);

        // 获取电价
        PlantInfo plantById = plantInfoMapper.getPlantById(plantId.toString());
        SysPowerPrice plantPrice = sysPowerPriceMapper.getPlantPrice(plantById.getPriceId().toString());
        List<SysPowerPriceDetail> detailList = sysPowerPriceDetailMapper
                .getPlantPriceDetail(plantPrice.getId().toString());

        if (CollectionUtils.isEmpty(detailList)) {
            throw new EnergyMonitorException("SysPowerPriceDetail is empty and plantId is " + plantId);
        }
        double jEnergyM = 0;
        double fEnergyM = 0;
        double pEnergyM = 0;
        double gEnergyM = 0;

        for (int i = 0; i < detailList.size(); i++) {
            SysPowerPriceDetail sysPowerPriceDetail = detailList.get(i);
            Integer priceType = sysPowerPriceDetail.getPriceType();
            if (priceType == PriceType.JIAN.getNum()) {
                double jEnergy = (double) energyByDeviceIdAndTime.get(PriceType.JIAN.getDescription());
                jEnergyM = jEnergy * sysPowerPriceDetail.getPosPrice();
            }
            if (priceType == PriceType.FENG.getNum()) {
                double jEnergy = (double) energyByDeviceIdAndTime.get(PriceType.FENG.getDescription());
                fEnergyM = jEnergy * sysPowerPriceDetail.getPosPrice();
            }
            if (priceType == PriceType.PING.getNum()) {
                double jEnergy = (double) energyByDeviceIdAndTime.get(PriceType.PING.getDescription());
                pEnergyM = jEnergy * sysPowerPriceDetail.getPosPrice();
            }
            if (priceType == PriceType.GU.getNum()) {
                double jEnergy = (double) energyByDeviceIdAndTime.get(PriceType.GU.getDescription());
                gEnergyM = jEnergy * sysPowerPriceDetail.getPosPrice();
            }
        }
        Double dayElec = Double.parseDouble(energyByDeviceIdAndTime.get("aEnergy").toString());
        Double dayElecPrice = jEnergyM + fEnergyM + pEnergyM + gEnergyM;

        HashMap<String, Double> resultMap = new HashMap<>();
        resultMap.put("dayElec", dayElec);
        resultMap.put("dayElecPrice", dayElecPrice);
        return resultMap;
    }

    /**
     * @return java.util.HashMap
     * @Author lz
     * @Description 通过电站id和时间获取用电 electricStorageData
     * @Date 20:06 2018/8/23
     * @Param [plantId, startTime, endTime]
     **/
    public HashMap<String, Double> getEByplantIdAndTime(Integer plantId, long startTime, long endTime) {
        HashMap<String, Object> mMap = new HashMap<>();
        HashMap<String, Double> resultMap = new HashMap();
        mMap.put("plantId", plantId);
        mMap.put("startTime", startTime);
        // mMap.put("endTime", endTime);
        Map eMonByplantIdAndTime = electricStorageDataMapper.getEMonByplantIdAndTime(mMap);
        resultMap.put("elec", 0d);
        resultMap.put("elecPrice", 0d);
        if (!CollectionUtils.isEmpty(eMonByplantIdAndTime)) {
            Double monthElec = Double.parseDouble(eMonByplantIdAndTime.get("monthElec").toString());
            Double monthElecPrice = Double.parseDouble(eMonByplantIdAndTime.get("monthElecPrice").toString());
            resultMap.put("elec", monthElec);
            resultMap.put("elecPrice", monthElecPrice);
        }
        return resultMap;
    }

    /**
     * 用电负荷趋势
     *
     * @return
     * @throws SessionTimeoutException
     * @throws SessionException
     * @throws ServiceException
     * @author ybj
     * @date 2018/08/27 10:00
     */
    @Override
    public Map<String, Object> getElecLoadKpi(User u, long startTime, long endTime)
            throws SessionException, SessionTimeoutException, ServiceException {
        // 结果集声明并实力化
        Map<String, Object> result = new HashMap<>();
        String startTimeTemp = Util.longToDateString(startTime, "yyyy-MM-dd");
        String endTimeTemp = Util.longToDateString(endTime, "yyyy-MM-dd");

        result.put("start", startTimeTemp);
        result.put("end", endTimeTemp);
        // 参数map声明实力化
        Map<String, Object> map = new HashMap<>();
        // 返回集中加入横坐标地址和开始结束时间
        List<String> xData = TimeUtil.getminuteInterval(startTime, endTime, 5);
        result.put("xData", xData);

        // 纵坐标数据
        List<Object> yData = new ArrayList<>();
        result.put("yData", yData);

        // 有功功率y坐标
        Map<String, Object> y0 = new HashMap<>();
        // 有功功率值列表
        Double[] d0 = new Double[xData.size()];
        // 初始化
        y0.put("value", d0);
        y0.put("max", 0);
        y0.put("unit", "千瓦");
        y0.put("name", 0);
        d0 = Util.doubleInit(d0);

        // 申报容量y坐标
        Map<String, Object> y1 = new HashMap<>();
        // 申报容量值列表
        Double[] d1 = new Double[xData.size()];
        // 初始化
        y1.put("value", d1);
        y1.put("max", 0);
        y1.put("unit", "千瓦");
        y1.put("name", 1);
        d1 = Util.doubleInit(d1);

        // 负荷率y坐标
        Map<String, Object> y2 = new HashMap<>();
        // 申报容量值列表
        Double[] d2 = new Double[xData.size()];
        // 初始化
        y2.put("value", d2);
        y2.put("max", 0);
        y2.put("unit", "%");
        y2.put("name", 2);
        d2 = Util.doubleInit(d2);

        // 将各自y坐标加入到结果集yData中
        yData.add(y0);
        yData.add(y1);
        yData.add(y2);

        int plantId = getPlantId(u);

        List<Integer> elecsList = getElecsList(plantId);
        // 如果没有点表 就直接返回默认结果
        if (!Util.isNotBlank(elecsList)) {
            return result;
        }

        map.put("devices", elecsList);

        // 参数集中加入开始结束时间
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        // 得到时间点和对应的有功功率值
        List<Map<String, Object>> powerBydeviceList = dataElectricMeterMapper.getPowerBydeviceList(map);

        if (!Util.isNotBlank(powerBydeviceList)) {
            return result;
        }

        // =================================================有功功率

        for (Map<String, Object> power : powerBydeviceList) {
            int indexOf = xData.indexOf(power.get("time"));
            if (indexOf == -1) {
                continue;
            }
            d0[indexOf] = Util.getDouble(power.get("gl"));
        }

        y0.put("max", Util.getMaxDoubleFromArray(d0));

        // =================================================申报容量

        // 得到格式化后的容量申报 Map<year, Map<month,Map<Integer,Double>>
        // 0：申报容量，1：申报单价，2：基础电费
        Map<String, Map<String, Map<Integer, Double>>> decCapFormat = SystemCache.plantEclecCap
                .get(String.valueOf(plantId));

        for (int i = 0; i < xData.size(); i++) {

            long time = TimeUtil.str2Long(xData.get(i),"yyyy-MM-dd HH:mm");
            // 得到年
            String year = String.valueOf(TimeUtil.getYearMonthDay(time, Calendar.YEAR));
            // 得到月
            String month = String.valueOf(TimeUtil.getYearMonthDay(time, Calendar.MONTH));
            // 根据年月得到申报容量

            Double decCap = 0d;

            try {
                decCap = Util.getDouble(decCapFormat.get(year).get(month).get(0));
            } catch (NullPointerException e) {
                log.debug(plantId + "电站没有" + year + "年" + month + "月的申报容量!");
            }

            d1[i] = decCap;

        }

        y1.put("max", Util.getMaxDoubleFromArray(d1));

        // =================================================负荷率

        for (int i = 0; i < xData.size(); i++) {
            if (d1[i] == 0) {
                d2[i] = 0d;
                continue;
            }
            // 四舍五入 保留一位小数
            d2[i] = ApproUtil.getRoundDouble(d0[i] / d1[i] * 100);

        }

        y2.put("max", Util.getMaxDoubleFromArray(d2));

        // =================================================

        // 单位转换
        UnitUtil.ConvertElec02(result);

        // 吧x轴的年份去掉 eg:2018-08-07 12:50 change 08-07 12:50
        for (int i = 0; i < xData.size(); i++) {
            String temp = xData.get(i);
            int indexOf = temp.indexOf('-') + 1;
            xData.set(i, temp.substring(indexOf));
        }

        return result;
    }

    /**
     * @return java.util.HashMap 参数dimension时间维度，startTime，endTime起始时间
     * @Author lz
     * @Description 用电趋势
     * @Date 11:08 2018/8/28
     * @Param [user, parMap]
     **/
    @Override
    public HashMap<String, Object> getElecUseKpi(User user, Map<String, Object> parMap)
            throws SessionTimeoutException, SessionException, EnergyMonitorException {
        // 获取前台参数
        Object dimensions = parMap.get("dimension");
        Integer dimension = (dimensions == null) || (Util.isEmpty(dimensions.toString())) ? null
                : Integer.valueOf(dimensions.toString());

        String startTime = String.valueOf(parMap.get("startTime"));
        String endTime = String.valueOf(parMap.get("endTime"));
        // 获取电站id
        int plantId = getPlantId(user);
        if (plantId == -1) {
            throw new EnergyMonitorException("plant is not exist and user id is " + user.getId());
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        // 当前时间0点
        String cDay = Util.longToDateString(TimeUtil.getDay0(), TimeEXP.exp1);
        // 维度没有或维度为日期时返回各日发电量
        if (dimension == null || dimension == Dimension.DAY) {
            // 电量按日分组表达式
            String exp = "%Y-%m-%d";
            // 没有起止时间时,默认为当前时间至前面三十天，若数据不足三十天则从有数据至当前时间
            if (Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                endTime = cDay;
                startTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, endTime, -30, TimeEXP.exp1);
            }
            // 有开始时间无结束时间，则开始时间向后三十天，若数据不足三十天则从开始时间至当前时间
            if (!Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                endTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, startTime, +30, TimeEXP.exp1);
                if (Util.dateStringToLong(endTime, TimeEXP.exp1) > Util.dateStringToLong(cDay, TimeEXP.exp1)) {
                    endTime = cDay;
                }
            }
            // 无开始时间有结束时间，则结束时间向前三十天，若数据不足三十天则从结束时间至有数据的第一天
            if (Util.isEmpty(startTime) && !Util.isEmpty(endTime)) {
                startTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, endTime, -30, TimeEXP.exp1);
            }
            getDEUByPlantIdAndTime(startTime, endTime, plantId, resultMap, exp);
        }
        // 返回各月发电量
        if (dimension != null && dimension == Dimension.MONTH) {
            // 电量按日分组表达式
            String exp = "%Y-%m";
            if (Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                // 没有起止时间时,默认为当前时间至前面12月，若数据不足12月则从有数据至当前时间
                endTime = cDay;
                startTime = TimeUtil.getNextOrLastTime(Calendar.MONTH, endTime, -12, TimeEXP.exp1);
            } else if (!Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                // 有开始时间无结束时间，则开始时间向后12月，若数据不足12月则从开始时间至当前时间
                endTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, startTime, +12, TimeEXP.exp1);
                if (Util.dateStringToLong(endTime, TimeEXP.exp1) > Util.dateStringToLong(cDay, TimeEXP.exp1)) {
                    endTime = cDay;
                }
            } else if (Util.isEmpty(startTime) && !Util.isEmpty(endTime)) {
                // 无开始时间有结束时间，则结束时间向前12月，若数据不足12月则从结束时间至有数据的第一月
                startTime = TimeUtil.getNextOrLastTime(Calendar.DAY_OF_MONTH, endTime, -12, TimeEXP.exp1);
            } else {
                // 有开始结束时间时
                // 用于矫正查询参数
                String cor = "-01";
                startTime = startTime + cor;
                if (cDay.contains(endTime)) {
                    // 查询当月时
                    endTime = cDay;
                } else {
                    endTime = endTime + cor;
                    endTime = TimeUtil.getNextMonLday(endTime, TimeEXP.exp1);
                }
            }

            HashMap<String, Object> monMap = getDEUByPlantIdAndTime(startTime, endTime, plantId, resultMap, exp);
            monMap.put("start", startTime.substring(0, startTime.lastIndexOf("-")));
            monMap.put("end", endTime.substring(0, startTime.lastIndexOf("-")));

        }
        // 返回各年发电量
        if (dimension != null && dimension == Dimension.YEAR) {
            // 电量按日分组表达式
            String exp = "%Y";
            if (Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                // 没有起止时间时,默认为当前时间至前面2年，若数据不足2年则从有数据至当前时间
                endTime = cDay;
                startTime = TimeUtil.getNextOrLastTime(Calendar.YEAR, endTime, -2, TimeEXP.exp1);
            } else if (!Util.isEmpty(startTime) && Util.isEmpty(endTime)) {
                // 有开始时间无结束时间，则开始时间向后2年，若数据不足2年则从开始时间至当前时间
                endTime = TimeUtil.getNextOrLastTime(Calendar.YEAR, startTime, +2, TimeEXP.exp1);
                if (Util.dateStringToLong(endTime, TimeEXP.exp1) > Util.dateStringToLong(cDay, TimeEXP.exp1)) {
                    endTime = cDay;
                }
            } else if (Util.isEmpty(startTime) && !Util.isEmpty(endTime)) {
                // 无开始时间有结束时间，则结束时间向前2年，若数据不足2年则从结束时间至有数据的第一年
                startTime = TimeUtil.getNextOrLastTime(Calendar.YEAR, endTime, -2, TimeEXP.exp1);
            } else {
                // 有开始结束时间时
                // 用于矫正查询参数
                String cor = "-01-01";
                startTime = startTime + cor;
                if (cDay.contains(endTime)) {
                    // 查询当月时
                    endTime = cDay;
                } else {
                    endTime = endTime + cor;
                    endTime = TimeUtil.getNextYearLday(endTime, TimeEXP.exp1);
                }
            }

            // 清除同期用电
            HashMap<String, Object> yearMap = getDEUByPlantIdAndTime(startTime, endTime, plantId, resultMap, exp);
            yearMap.put("start", startTime.substring(0, startTime.indexOf("-")));
            yearMap.put("end", endTime.substring(0, startTime.indexOf("-")));
            ArrayList list = (ArrayList) yearMap.get("yData");
            list.remove(1);
        }

        return resultMap;
    }

    private HashMap<String, Object> getDEUByPlantIdAndTime(String startTime, String endTime, int plantId,
                                                           HashMap<String, Object> resultMap, String exp) throws EnergyMonitorException {
        // 今年用电
        Map<String, ArrayList> rd1Map = getDEUKByPlantId(plantId, startTime, endTime, exp);
        // 去年同期用电
        Map<String, ArrayList> rd2Map = getDEUKByPlantId(plantId,
                TimeUtil.getNextOrLastTime(Calendar.YEAR, startTime, -1, TimeEXP.exp1),
                TimeUtil.getNextOrLastTime(Calendar.YEAR, endTime, -1, TimeEXP.exp1), exp);

        //
        // 去年同期用电可能为没有，在这里调整没有同期用电的数据为0
        // corHisData(rd1Map, rd2Map);
        // 单位转换后的结果
        HashMap<Integer, HashMap<String, Object>> tMap = transUnitForUseKpi(rd1Map, rd2Map);
        HashMap<String, Object> trd1Map = tMap.get(1);
        HashMap<String, Object> trd2Map = tMap.get(2);
        // x轴数据封装
        ArrayList<Object> xDataList = rd1Map.get("time");
        // y轴数据封装
        ArrayList<Object> yDataList = new ArrayList<>();
        // 今年用电量
        HashMap<String, Object> map0 = new HashMap<>();
        map0.put("name", 0);
        map0.put("unit", trd1Map.get("powerUnit"));
        map0.put("value", trd1Map.get("power"));
        // 去年同期用电量
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", 1);
        map1.put("unit", trd2Map.get("powerUnit"));
        map1.put("value", trd2Map.get("power"));
        // 今年电电费
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", 2);
        map2.put("unit", trd1Map.get("priceUnit"));
        map2.put("value", trd1Map.get("price"));

        yDataList.add(map0);
        yDataList.add(map1);
        yDataList.add(map2);

        resultMap.put("end", endTime);
        resultMap.put("start", startTime);
        resultMap.put("xData", xDataList);
        resultMap.put("yData", yDataList);
        return resultMap;
    }

    /**
     * @return void
     * @Author lz
     * @Description 针对UseKpi的单位转换
     * @Date 14:54 2018/8/27
     * @Param []
     **/
    private HashMap<Integer, HashMap<String, Object>> transUnitForUseKpi(Map<String, ArrayList> rd1Map,
                                                                         Map<String, ArrayList> rd2Map) {
        ArrayList<Double> power1 = rd1Map.get("power");
        ArrayList<Double> price1 = rd1Map.get("price");
        ArrayList<Double> power2 = rd2Map.get("power");
        ArrayList<Double> price2 = rd2Map.get("price");
        //
        int qSize = power1.size() >> 2;
        // 记录电量需要转换进制的个数
        int cpower1 = 0;
        int cpower2 = 0;
        int cpower3 = 0;
        int cpower4 = 0;
        // 记录电量需要转换进制的个数
        int cprice1 = 0;
        int cprice2 = 0;
        int cprice3 = 0;
        int cprice4 = 0;
        for (int i = 0; i < power1.size(); i++) {
            Double apower1 = power1.get(i);
            Double aprice1 = price1.get(i);
            if (apower1 > UNIT.ELEC.getTraH2()) {
                cpower2++;
            }
            if (aprice1 > UNIT.RMB.getTraH2()) {
                cprice2++;
            }
            if (apower1 > UNIT.ELEC.getTraH1()) {
                cpower1++;
            }
            if (apower1 > UNIT.RMB.getTraH1()) {
                cprice1++;
            }

            Double apower2 = power2.get(i);
            Double aprice2 = price2.get(i);
            if (apower2 > UNIT.ELEC.getTraH2()) {
                cpower4++;
            }
            if (aprice2 > UNIT.RMB.getTraH2()) {
                cprice4++;
            }
            if (apower2 > UNIT.ELEC.getTraH1()) {
                cpower3++;
            }
            if (apower2 > UNIT.RMB.getTraH1()) {
                cprice3++;
            }
        }

        HashMap<String, Object> resultMap1 = new HashMap<>(rd1Map);
        HashMap<String, Object> resultMap2 = new HashMap<>(rd2Map);
        resultMap1.put("powerUnit", "度");
        resultMap1.put("priceUnit", "元");
        resultMap2.put("powerUnit", "度");
        resultMap2.put("priceUnit", "元");
        if (cpower2 > qSize || cpower4 > qSize) {
            ArrayList<Double> rPower1 = new ArrayList<>();
            resultMap1.put("powerUnit", "亿度");
            power1.forEach(e -> rPower1.add(e / UNIT.ELEC.getTraH2()));
            power1 = rPower1;

            ArrayList<Double> rPower2 = new ArrayList<>();
            resultMap2.put("powerUnit", "亿度");
            power2.forEach(e -> rPower2.add(e / UNIT.ELEC.getTraH2()));
            power2 = rPower2;
        }
        if (cprice2 > qSize || cprice4 > qSize) {
            ArrayList<Double> rPrice1 = new ArrayList<>();
            resultMap1.put("priceUnit", "亿元");
            price1.forEach(e -> rPrice1.add(e / UNIT.RMB.getTraH2()));
            price1 = rPrice1;

            ArrayList<Double> rPrice2 = new ArrayList<>();
            resultMap2.put("priceUnit", "亿元");
            price2.forEach(e -> rPrice2.add(e / UNIT.RMB.getTraH2()));
            price2 = rPrice2;
        }
        if (cpower1 > qSize || cpower3 > qSize) {
            ArrayList<Double> rPower1 = new ArrayList<>();
            resultMap1.put("powerUnit", "万度");
            power1.forEach(e -> rPower1.add(e / UNIT.ELEC.getTraH1()));
            power1 = rPower1;

            ArrayList<Double> rPower2 = new ArrayList<>();
            resultMap2.put("powerUnit", "万度");
            power2.forEach(e -> rPower2.add(e / UNIT.ELEC.getTraH1()));
            power2 = rPower2;

        }
        if (cprice1 > qSize || cprice3 > qSize) {
            ArrayList<Double> rPrice1 = new ArrayList<>();
            resultMap1.put("priceUnit", "万元");
            price1.forEach(e -> rPrice1.add(e / UNIT.RMB.getTraH1()));
            price1 = rPrice1;

            ArrayList<Double> rPrice2 = new ArrayList<>();
            resultMap2.put("priceUnit", "万元");
            price2.forEach(e -> rPrice2.add(e / UNIT.RMB.getTraH1()));
            price2 = rPrice2;
        }
        resultMap1.put("power", power1);
        resultMap1.put("price", price1);
        resultMap2.put("power", power2);
        resultMap2.put("price", price2);
        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
        resultMap.put(1, resultMap1);
        resultMap.put(2, resultMap2);

        return resultMap;
    }

    /**
     * @return void
     * @Author lz
     * @Description 去年同期用电可能为没有，在这里调整没有同期用电的数据为0
     * @Date 14:25 2018/8/27
     * @Param [rd1Map, rd2Map]
     **/
    private void corHisData(Map<String, ArrayList> rd1Map, Map<String, ArrayList> rd2Map) {
        ArrayList<String> time1 = rd1Map.get("time");
        ArrayList<String> time2 = rd2Map.get("time");
        HashMap<String, Integer> hashMap = new HashMap<>();
        ArrayList powerList = rd2Map.get("power");
        ArrayList priceList = rd2Map.get("price");
        ArrayList power2 = new ArrayList<Double>();
        ArrayList price2 = new ArrayList<Double>();

        for (int i = 0; i < time1.size(); i++) {
            String s = time1.get(i);
            String[] split = s.split("-");
            String time = (Integer.valueOf(split[0]) - 1) + "";
            for (int j = 1; j < split.length; j++) {
                time = time + "-" + split[j];
            }
            hashMap.put(time, i);
            power2.add(0.0);
            price2.add(0.0);
        }
        if (!CollectionUtils.isEmpty(time2)) {
            Iterator iterator = powerList.iterator();
            Iterator iterator1 = priceList.iterator();
            time2.forEach(e -> {
                if (hashMap.get(e) != null) {
                    power2.set(hashMap.get(e), iterator.next());
                    price2.set(hashMap.get(e), iterator1.next());
                }
            });
        }
        rd2Map.put("time", time1);
        rd2Map.put("power", power2);
        rd2Map.put("price", price2);

    }

    /**
     * @return java.util.Map
     * @Author lz
     * @Description 以日为x轴基准统计 根据exp返回对应分组
     * @Date 10:50 2018/8/27
     * @Param [plantId, startTime, endTime]
     **/
    private Map<String, ArrayList> getDEUKByPlantId(int plantId, String startTime, String endTime, String exp) {
        HashMap<String, Object> parMap = new HashMap<>();
        parMap.put("plantId", plantId);
        parMap.put("startTime", Util.dateStringToLong(startTime, TimeEXP.exp1));
        parMap.put("endTime", Util.dateStringToLong(endTime, TimeEXP.exp1));
        parMap.put("exp", exp);
        List<Map<String, Object>> eukByPlantIdAndTime = electricStorageDataMapper.getEUKByPlantIdAndTime(parMap);
        // if (CollectionUtils.isEmpty(eukByPlantIdAndTime)) {
        // throw new EnergyMonitorException("eukByPlantIdAndTime is empty and plantId is
        // " + plantId);
        // }
        // 返回的时间列表
        ArrayList<String> timeArrayList = null;
        // 用于获取值的时间列表
        ArrayList<String> getValueList = null;
        ArrayList<Double> powValueArrayList = new ArrayList<>();
        ArrayList<Double> priceValueArrayList = new ArrayList<>();
        if (exp.equals("%Y-%m-%d")) {
            getValueList = TimeUtil.getTimeList(startTime, endTime, TimeEXP.exp1, Calendar.DAY_OF_MONTH);
            timeArrayList = TimeUtil.getTimeList(startTime, endTime, TimeEXP.exp4, Calendar.DAY_OF_MONTH);
        } else if (exp.equals("%Y-%m")) {
            getValueList = TimeUtil.getTimeList(startTime, endTime, TimeEXP.exp2, Calendar.MONTH);
            timeArrayList = getValueList;
        } else if (exp.equals("%Y")) {
            getValueList = TimeUtil.getTimeList(startTime, endTime, TimeEXP.exp3, Calendar.YEAR);
            timeArrayList = getValueList;
        }
        timeArrayList.forEach((e) -> {
            powValueArrayList.add(0d);
            priceValueArrayList.add(0d);
        });
        // 封装前台所需数据格式
        for (int i = 0; i < eukByPlantIdAndTime.size(); i++) {
            Map<String, Object> map = eukByPlantIdAndTime.get(i);
            String mTime = map.get("fdata_time").toString();
            int flag;
            if ((flag = getValueList.indexOf(mTime)) != -1) {
                double pow = Double.parseDouble(map.get("pow").toString());
                powValueArrayList.set(flag, ApproUtil.getRoundDouble(pow));
                double price = Double.parseDouble(map.get("price").toString());
                priceValueArrayList.set(flag, ApproUtil.getRoundDouble(price));
            }
        }

        Double cdActive = SystemCache.currentDataOfPlant.get(String.valueOf(plantId)).get("yg");
        Double cdPrice = SystemCache.currentDataOfPlant.get(String.valueOf(plantId)).get("price");

        String cDay = Util.dateToDateString(new Date(), TimeEXP.exp1);
        // 查询每天的数据数据且查询了当日时加上当天的数据
        if (endTime.equals(cDay)) {
            if (exp == "%Y-%m-%d") {
                powValueArrayList.set(timeArrayList.size() - 1, ApproUtil.getRoundDouble(cdActive));
                priceValueArrayList.set(timeArrayList.size() - 1, ApproUtil.getRoundDouble(cdPrice));
            } else {
                int pos = powValueArrayList.size() - 1;
                double cPow = powValueArrayList.get(pos) + cdActive;
                double cPrice = priceValueArrayList.get(pos) + cdPrice;
                powValueArrayList.set(pos, ApproUtil.getRoundDouble(cPow));
                priceValueArrayList.set(pos, ApproUtil.getRoundDouble(cPrice));
            }
        }

        HashMap resultMap = new HashMap<String, Object>();
        resultMap.put("time", timeArrayList);
        resultMap.put("power", powValueArrayList);
        resultMap.put("price", priceValueArrayList);
        return resultMap;
    }

    /**
     * 电能质量
     *
     * @return
     * @throws SessionTimeoutException
     * @throws SessionException
     * @author ybj
     * @Date 2018/8/23 20:11
     */
    @Override
    public Map<String, Object> getElecQualityKPI(User u) throws SessionException, SessionTimeoutException {

        // 结果集
        Map<String, Object> result = new HashMap<>();
        // 餐数集
        Map<String, Object> map = new HashMap<>();

        double yg = 0d;
        double wg = 0d;

        // 本月最高功率因数
        double max = 0f;
        // 本月最低功率因数
        double min = 0f;
        // 功率因数
        double powerFactor = 0d;
        // 预估奖励
        double estimateReward = 0d;
        // 预估罚款
        double forecastFines = 0d;

        PlantInfo plantInfo = plantInfoMapper.getPlantById(String.valueOf(getPlantId(u)));

        // 当月用电
        // 有功
        Double ygMonth = SystemCache.historyDataOfEEPlant.get(plantInfo.getId()).get(0).get("yg");
        // 无功
        Double wgMonth = SystemCache.historyDataOfEEPlant.get(plantInfo.getId()).get(0).get("wg");
        // 本月电价
        Double priceMonth = SystemCache.historyDataOfEEPlant.get(plantInfo.getId()).get(0).get("price");
        // 当天用电
        // 有功
        Double ygToday = SystemCache.currentDataOfPlant.get(String.valueOf(plantInfo.getId())).get("yg");
        // 无功
        Double wgToday = SystemCache.currentDataOfPlant.get(String.valueOf(plantInfo.getId())).get("wg");

        Double priceToday = SystemCache.currentDataOfPlant.get(String.valueOf(plantInfo.getId())).get("price");

        yg += ygMonth + ygToday;

        wg += wgMonth + wgToday;

        powerFactor = SFUtil.getPFactor(yg, wg);

        max = Util.getDouble(SystemCache.minAndMinFactorOfEEPlant.get(plantInfo.getId()).get("max"));
        max = ApproUtil.getRoundDouble(max);

        min = Util.getDouble(SystemCache.minAndMinFactorOfEEPlant.get(plantInfo.getId()).get("min"));
        min = ApproUtil.getRoundDouble(min);

        // a 0.90 b 0.85 c 0.8
        String typeTemp = plantInfo.getPlantPowerFactorType();

        String type = "c".equals(typeTemp) ? "c" : "b".equals(typeTemp) ? "b" : "a";

        map.clear();

        map.put("type", type);

        // 一定要保留两位小说并且把它字符串化 因为数据库存的字符串 eg:1.00 0.90
        map.put("factor", String.format("%.2f", powerFactor));

        Map<String, Object> factorMap = elecFactorTableMapper.selectByFactor(map);

        double factor = 0d;
        if (Util.isNotBlank(factorMap)) {
            factor = Util.getDouble(factorMap.get("type"));
        }

        // 大于0是罚款
        if (factor > 0d) {
            forecastFines = Util.getDouble((priceMonth + priceToday) * (Math.abs(factor) / 100));
            // 小于0是奖励
        } else if (factor < 0d) {
            estimateReward = Util.getDouble((priceMonth + priceToday) * (Math.abs(factor) / 100));
        }
        // 等于0 原价 没奖励也没罚款

        // 预估奖励
        result.put("estimateReward", estimateReward);
        // 预估罚款
        result.put("forecastFines", forecastFines);
        // 本月最高功率因数
        result.put("max", max);
        // 本月最低功率因数
        result.put("min", min);
        // 功率因数
        result.put("powerFactor", powerFactor);

        return result;

    }

    /**
     * 获取企业基本信息
     *
     * @return
     * @throws SessionTimeoutException
     * @throws SessionException
     */
    @Override
    public Map<String, Object> getEnterpriseInfo(User u) throws SessionException, SessionTimeoutException {
        int plantId = getPlantId(u);
        PlantInfo plantInfo = plantInfoMapper.getPlantInfo(String.valueOf(plantId));
        Map<String, Object> result = new HashMap<>();
        result.put("addr", plantInfo.getPlantAddr() + plantInfo.getPlantFullAddress());
        result.put("connectTime", Util.longToDateString(plantInfo.getCreateTime(), "yyyy-MM-dd"));
        result.put("description", plantInfo.getPlantIntroduction());
        result.put("enterPriseName", plantInfo.getPlantName());
        String[] photos = Util.isNotBlank(plantInfo.getPlantPhoto()) ? plantInfo.getPlantPhoto().split(";") : null;
        result.put("photos", photos);
        return result;
    }

    /**
     * @param u
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException
     * @author ybj
     * @date 2018年8月23日下午7:54:50
     * @Description -_-根据用户得到该能效电站id
     */
    private int getPlantId(User u) throws SessionException, SessionTimeoutException {

        List<Integer> plantList = plantInfoService.getPlantsForUser(u, 1);

        // 能效电站id 默认为-1标示没有 有的话就把电站id付给它 目前针对每个用户只有一个电站
        int energyEfficPlantId = -1;

        if (Util.isNotBlank(plantList)) {

            for (int plantId : plantList) {

                String plantType = SystemCache.plantTypeList.get(String.valueOf(plantId));

                if (Util.isNotBlank(plantType) && String.valueOf(PTAC.ENERGYEFFICPLANT).equals(plantType)) {

                    energyEfficPlantId = plantId;

                    break;

                }
            }
        }
        if (energyEfficPlantId == -1) {
            log.debug("该用户没有的能效电站！");
        }
        return energyEfficPlantId;

    }

    /**
     * @param plantId
     * @return
     * @throws SessionException
     * @throws SessionTimeoutException
     * @author ybj
     * @date 2018年8月27日下午1:15:00
     * @Description -_-根据用户得到该能效电站点表列表
     */
    public List<Integer> getElecsList(int plantId) throws SessionException, SessionTimeoutException {
        // 得到该电站所有类型-电站类表
        Map<Integer, List<Integer>> types = SystemCache.deviceOfPlant.get(plantId);
        List<Integer> list = null;
        if (Util.isNotBlank(types)) {
            list = types.get(DTC.ELECTRIC_METER);
        }
        // 得到电表列表
        return list;
    }
}
