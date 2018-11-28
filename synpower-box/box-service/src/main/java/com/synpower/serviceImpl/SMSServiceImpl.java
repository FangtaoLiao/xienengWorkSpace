package com.synpower.serviceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.CollDevice;
import com.synpower.bean.InventerStorageData;
import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysUser;
import com.synpower.dao.CollDeviceMapper;
import com.synpower.dao.ElectricStorageDataMapper;
import com.synpower.dao.InventerStorageDataMapper;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.PowerPriceMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysUserMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.mobile.MobileUtil;
import com.synpower.mobile.msg.GenVcode;
import com.synpower.mobile.msg.MobileMsg;
import com.synpower.mobile.msg.TelSet;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.MonitorService;
import com.synpower.service.PlantInfoService;
import com.synpower.service.SMSService;
import com.synpower.service.StatisticalGainService;
import com.synpower.service.WXmonitorService;
import com.synpower.util.CacheUtil;
import com.synpower.util.ServiceUtil;
import com.synpower.util.TimeUtil;
import com.synpower.util.Util;

@Service
public class SMSServiceImpl implements SMSService{
	private Logger logger = Logger.getLogger(SMSServiceImpl.class);
	@Autowired
	private MobileUtil mobileUtil;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private SysOrgMapper orgMapper;
    @Autowired
    private PlantInfoMapper plantMapper;
    @Autowired
    private StatisticalGainService statisticalGainService;
    @Autowired
    private PowerPriceMapper priceMapper;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private WXmonitorService wxmonitorService;
    @Autowired
    private PlantInfoService plantInfoService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ElectricStorageDataMapper electricMapper;
    @Autowired
    private CollDeviceMapper deviceMapper;
    @Autowired
	private InventerStorageDataMapper inventerMapper;
    
	@Override
	public MessageBean sendValid(String jsonData, Session session) throws ServiceException, SmsException, IOException, SessionException, SessionTimeoutException {
		MessageBean msg=new MessageBean();
		//解析前台参数
		Map<String, Object>map=Util.parseURL(jsonData);
		String telNo=map.get("telNo")+"";
		User u=session.getAttribute(map.get("tokenId")+"");
		String uId=u.getId()+"_"+u.getUserName();
		logger.info(" 开始下发验证码,telNo: "+telNo+" operator: "+uId);
		boolean exit=cacheUtil.exist(telNo);
		String validate=null;
		if (exit) {
			//如果验证码未过期则从Redis中读取
			validate=cacheUtil.getString(telNo);
		}else{
			//如果验证码过期或者未生成,则生成验证码存入Redis中
			GenVcode code=new GenVcode();
			validate=code.randomCode();
			//过期时间为15分钟
			cacheUtil.setEx(telNo,15*60,validate);
		}
		//根据模板组装短信内容
		String  content=ServiceUtil.getValidateSMS(validate);
		//组装发送短信参数
		MobileMsg smsContend=new MobileMsg();
		smsContend.setSendTime("");
		smsContend.setContent(content);
		TelSet set=new TelSet();
		set.add(telNo);
		smsContend.setTelNo(set);
		//发送短信
		int status=mobileUtil.sendMsg(smsContend);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg(mobileUtil.getResponseCode().get(status));
		Map<String, String>body=new HashMap<>(1);
		body.put("validate", validate);
		msg.setBody(body);
		logger.info(" 验证码下发成功! telNo: "+telNo+" validate: "+validate+" operator: "+uId);
		return msg;
	}
	
	
	/*public void sendDailyReport()
			throws ServiceException, SmsException, IOException, SessionException, SessionTimeoutException {
		//按联系人划分电站id
		List<Map<String, Object>> pList = plantMapper.getToutPlants();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (pList.size()>0) {
			for (int i = 0; i < pList.size(); i++) {
				String pids = pList.get(i).get("pids")+"";
				String contacts = pList.get(i).get("contacts")+"";
				String[] split = pids.split(",");
				//联系人所属电站id
				List<String> asList = Arrays.asList(split);
				//联系人电话
				SysUser sysUser = userMapper.getUserjoinPlant(contacts);
				String userTel = sysUser.getUserTel();
				logger.info(" 开始发送日报! 电话号码为: "+userTel);
				Map<String, String> powerPriceMap = new HashMap<String,String>();
				// 存放查询出来的发电量的数据
				Map<String, Double> powerMap = new TreeMap<String, Double>();
				// 存放查询出来的收益的数据
				Map<String, Double> priceMap = new TreeMap<String, Double>();
				List<Double> powerResult = new ArrayList<Double>();
				List<Double> priceResult = new ArrayList<Double>();
				powerPriceMap.put("date", "%Y-%m-%d");
				powerPriceMap.put("sdf", "yyyy-MM-dd");
				//昨天的正确日期格式
				String today = sdf.format(new Date());
				String specifiedDayBefore = TimeUtil.getSpecifiedDayBefore(today,1);
				//初始化各参数
				powerMap.put(specifiedDayBefore, 0.0);
				priceMap.put(specifiedDayBefore, 0.0);
				String content = null;
				String genYestarDay = null;
				String profitYestarDay = null;
				String genTotal = null;
				String profitTotal = null;
				//判断单电站或者多电站
				if (asList.size()>1) {
					List<PlantInfo> plantList = plantMapper.getPlantByIds(asList);
					for (PlantInfo plantInfo : plantList) {
						powerPriceMap.put("plantId", plantInfo.getId()+"");
						powerPriceMap.put("startTime", specifiedDayBefore);
						List<Map<String, Object>> GeneratingList = electricMapper.getPowerPriceByDay2(powerPriceMap);
						for (Map<String, Object> map2 : GeneratingList) {
							Double oldNum = powerMap.get(map2.get("time")+"");
							powerMap.put(map2.get("time")+"", Util.addFloat(oldNum, Double.valueOf(map2.get("sum")+""), 0));
							Double oldPrice = priceMap.get(map2.get("time")+"");
							priceMap.put(map2.get("time")+"", Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice")+""), 0));
						}
					}
					powerResult.add(Double.valueOf(powerMap.get(specifiedDayBefore)));
					priceResult.add(Double.valueOf(priceMap.get(specifiedDayBefore)));
					//多电站总发电量及收益
					MessageBean mb1 = this.getMultiProfit(asList);
					Map<String,Object> body1 = (Map<String, Object>) mb1.getBody();
					genTotal = body1.get("totalPower")+"";
					profitTotal = body1.get("totalPrice")+"";
					Map<String, String> setRightUnit = this.setRightUnit(powerResult, priceResult, genYestarDay, profitYestarDay);
					//昨日发电量及收益
					genYestarDay = setRightUnit.get("genYestarDay");
					profitYestarDay = setRightUnit.get("profitYestarDay");
					//组装短信参数
					String []dailyReportMulti = new String[]{specifiedDayBefore,genYestarDay,profitYestarDay,genTotal,profitTotal};
					content = ServiceUtil.getDailyReportMulti(dailyReportMulti);
				} else if(asList.size()==1) {
					powerPriceMap.put("plantId", asList.get(0));
					powerPriceMap.put("startTime", specifiedDayBefore);
					List<Map<String, Object>> GeneratingList = electricMapper.getPowerPriceByDay2(powerPriceMap);
					for (Map<String, Object> map2 : GeneratingList) {
						Double oldNum = powerMap.get(map2.get("time")+"");
						powerMap.put(map2.get("time")+"", Util.addFloat(oldNum, Double.valueOf(map2.get("sum")+""), 0));
						Double oldPrice = priceMap.get(map2.get("time")+"");
						priceMap.put(map2.get("time")+"", Util.addFloat(oldPrice, Double.valueOf(map2.get("sumPrice")+""), 0));
					}
					powerResult.add(Double.valueOf(powerMap.get(specifiedDayBefore)));
					priceResult.add(Double.valueOf(priceMap.get(specifiedDayBefore)));
					//总发电及收益
					String pId = asList.get(0)+"";
					Map<String, Object> powerEtIncome = this.getPowerEtIncome(pId);
					String plantName = plantMapper.getPNameByPId(pId);
					genTotal = powerEtIncome.get("genTotal")+"";
					profitTotal = powerEtIncome.get("profitTotal")+"";
					Map<String, String> setRightUnit = this.setRightUnit(powerResult, priceResult, genYestarDay, profitYestarDay);
					//昨日发电量及收益
					genYestarDay = setRightUnit.get("genYestarDay");
					profitYestarDay = setRightUnit.get("profitYestarDay");
					String []dailyReportSingle = new String[]{specifiedDayBefore,plantName,genYestarDay,profitYestarDay,genTotal,profitTotal};
					//根据模板组装短信内容
					content = ServiceUtil.getDailyReportSingle(dailyReportSingle);
				}
				//组装发送短信参数
				MobileMsg smsContend=new MobileMsg();
				smsContend.setSendTime("");
				smsContend.setContent(content);
				TelSet set=new TelSet();
				set.add(userTel);
				smsContend.setTelNo(set);
				//发送短信
				int status=mobileUtil.sendMsg(smsContend);
				String msg = mobileUtil.getResponseCode().get(status);
				logger.info(" 日报发送完成! 电话号码为: "+userTel);
			}
		}
	}*/
	
	/**
	 * 
	  * @Title:  getMultiProfit 
	  * @Description:  多电站发电及收益 
	  * @param plantIdList
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年2月25日下午3:08:56
	 */
	public MessageBean getMultiProfit(List<String> plantIdList){
		
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		if(Util.isNotBlank(plantIdList)){
			List<Integer> statusList = new ArrayList<Integer>();
			//查询出当前公司下所有的电站
			List<PlantInfo> plantList = plantMapper.getPlantByIds(plantIdList);
			Map<String, Double> historyMap = new HashMap<String, Double>(5);
			historyMap.put("totalPower", 0.0);
			historyMap.put("totalPrice", 0.0);
			historyMap.put("todayPower", 0.0);
			historyMap.put("todayPrice", 0.0);
			for (PlantInfo plantInfo : plantList) {
				statusList.add(ServiceUtil.getPlantStatus(plantInfo.getId()+""));
				int isAmmeter = 0;
				int isInverter =0;
				List<CollDevice> deviceList = deviceMapper.listDevicesByPlantId(plantInfo.getId()+"");
				for (CollDevice collDevice : deviceList) {
					//记录是否有电表
					if(collDevice.getDeviceType() == 3){
						isAmmeter++;
					}
					if(collDevice.getDeviceType() == 1 || collDevice.getDeviceType() == 2){
						isInverter++;
					}
				}
				//先判断有没有电表，若没有再判断有没有逆变器
				if(isAmmeter != 0){
					
				}else{
					if(isInverter != 0){
						Map<String, Object> todayPricePower = statisticalGainService.getNowDayIncome(plantInfo.getId()+"");
						List<InventerStorageData> inventerList = inventerMapper.getEnergyPrice(plantInfo.getId()+"");
						for (InventerStorageData inventerStorageData : inventerList) {
							historyMap.put("totalPower", Util.addFloat(historyMap.get("totalPower"), inventerStorageData.getTotalEnergy(), 0));
							historyMap.put("totalPrice", Util.addFloat(historyMap.get("totalPrice"), inventerStorageData.getTotalPrice(), 0));
							if(todayPricePower.get("power") != "null" && !todayPricePower.get("power").equals("")){
								if(historyMap.containsKey("totalPower")){
									historyMap.put("totalPower", Util.addFloat(historyMap.get("totalPower"), Double.valueOf(todayPricePower.get("power")+""), 0));
									historyMap.put("todayPower", Util.addFloat(historyMap.get("todayPower"), Double.valueOf(todayPricePower.get("power")+""), 0));
								}
								if(todayPricePower.get("inCome") != "null" && !todayPricePower.get("inCome").equals("")){
									if(historyMap.containsKey("totalPrice")){
										historyMap.put("totalPrice", Util.addFloat(historyMap.get("totalPrice"), Double.valueOf(todayPricePower.get("inCome")+""), 0));
										historyMap.put("todayPrice", Util.addFloat(historyMap.get("todayPrice"), Double.valueOf(todayPricePower.get("inCome")+""), 0));
									}
								}
							}
						}
					}
				}
			}
			resultMap.put("totalPower", Util.getRetainedDecimal(historyMap.get("totalPower")));
			resultMap.put("totalPowerUnit", "度");
			resultMap.put("totalPrice", Util.getRetainedDecimal(historyMap.get("totalPrice")));
			resultMap.put("totalPriceUnit","元");
			resultMap.put("genDay", Util.getRetainedDecimal(historyMap.get("todayPower")));
			resultMap.put("genDayUnit", "度");
			resultMap.put("profitDay", Util.getRetainedDecimal(historyMap.get("todayPrice")));
			resultMap.put("profitDayUnit", "元");
			//正常发电
			int gen = 0;
			//待机
			int standby = 0;
			//故障
			int fault = 0;
			//中断、断链
			int breaks = 0;
			for (Integer integer : statusList) {
				switch (integer) {
				case 0:
					gen++;
					break;
				case 1:
					standby++;
					break;
				case 2:
					fault++;
					break;
				case 3:
					breaks++;
					break;
				default:
					break;
				}
			}
			//组装前台数据
			resultMap.put("gen", gen);
			resultMap.put("standby", standby);
			resultMap.put("fault", fault);
			resultMap.put("break", breaks);
			resultMap.put("unit", "座");
			resultMap.put("time", System.currentTimeMillis());
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
			msg.setMsg(Msg.SELECT_SUCCUESS);
		}else{
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}
		return msg;
	}
	
	/**
	 *  
	  * @Title:  setRightUnit
	  * @Description:  收益、发电单位换算  
	  * @param powerResult
	  * @param priceResult
	  * @param genYestarDay
	  * @param profitYestarDay
	  * @return: Map<String,String>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月11日下午3:02:06
	 */
	public Map<String,String> setRightUnit(List<Double> powerResult,List<Double> priceResult,String genYestarDay,String profitYestarDay){
		if(Collections.max(powerResult) > 1000000){
			//当数据过大的时候进行单位的换算
			for (int i2 = 0; i2 < powerResult.size(); i2++) {
				powerResult.set(i2, Util.divideDouble(powerResult.get(i2), 1000000, 1));
			}
			genYestarDay = Util.getRetainedDecimal(Collections.max(powerResult))+"GWh";
		//判断发电量的最大值是否超过一万，若超过则单位换算
		}else if(Collections.max(powerResult) > 10000){
			//当数据过大的时候进行单位的换算
			for (int i2 = 0; i2 < powerResult.size(); i2++) {
				powerResult.set(i2, Util.divideDouble(powerResult.get(i2), 1000, 1));
			}
			genYestarDay = Util.getRetainedDecimal(Collections.max(powerResult))+"MWh";
		}else{
			for (int i2 = 0; i2 < powerResult.size(); i2++) {
				powerResult.set(i2, Util.getRetainedDecimal(powerResult.get(i2)));
			}
			genYestarDay = Util.getRetainedDecimal(Collections.max(powerResult))+"kWh";
		}
		
		//判断收益的最大值是否超过一万，若超过则单位换算
		if(Collections.max(priceResult) > 10000){
			//当数据过大的时候进行单位的换算
			for (int i2 = 0; i2 < priceResult.size(); i2++) {
				priceResult.set(i2, Util.divideDouble(priceResult.get(i2), 10000, 1));
			}
			profitYestarDay = Util.getRetainedDecimal(Collections.max(priceResult))+"万元";
		}else{
			for (int i2 = 0; i2 < priceResult.size(); i2++) {
				priceResult.set(i2, Util.getRetainedDecimal(priceResult.get(i2)));
			}
			profitYestarDay = Util.getRetainedDecimal(Collections.max(priceResult))+"元";
		}
		Map<String,String> uMap = new HashMap<>();
		uMap.put("genYestarDay", genYestarDay);
		uMap.put("profitYestarDay", profitYestarDay);
		return uMap;
	}

	/*
	public Map<String,Object> getPowerEtIncome(String pId) throws ServiceException{
		Map<String, Object>genIncome=new HashMap<>(3);
		Map<String, Object>genIncomMap = statisticalGainService.getNowDayIncome(pId);
		Map<String, Object>changeMap=ServiceUtil.changeUnit(new String[]{"度","万度","亿度"},Double.parseDouble("null".equals(genIncomMap.get("power")+"")?"0":genIncomMap.get("power")+""));
		//今日发电
		String genDay=changeMap.get("value")+"";
		genIncome.put("genDay", Util.roundDouble(Double.parseDouble(genDay))+""+changeMap.get("unit"));
		//今日发电单位	
		//今日收益
		//单位换算
		changeMap.clear();
		changeMap=ServiceUtil.changeUnit(new String[]{"元","万元","亿"},Double.parseDouble("null".equals(genIncomMap.get("inCome")+"")?"0":genIncomMap.get("inCome")+""),10000);
		String profitDay=changeMap.get("value")+"";
		genIncome.put("profitDay",Util.roundDouble(Double.parseDouble(profitDay))+""+changeMap.get("unit"));
		//今日单位
		Map<String, Double>historyMap=priceMapper.getHistoryIncome(pId);
		changeMap.clear();
		changeMap=ServiceUtil.changeUnit(new String[]{"度","万度","亿度"},historyMap.get("power"),1000);
		genIncome.put("genTotal",Util.roundDouble(("null".equals(genIncome.get("genDay")+"")?0.0: Util.addFloat(Double.parseDouble (changeMap.get("value")+""),Double.parseDouble( genDay), 0)))+""+ changeMap.get("unit"));
		changeMap.clear();
		changeMap=ServiceUtil.changeUnit(new String[]{"元","万元","亿"},historyMap.get("price"),10000);
		//总收益
		genIncome.put("profitTotal",Util.roundDouble( ("null".equals(genIncome.get("profitDay")+"")?0.0:Util.addFloat(Double.parseDouble (changeMap.get("value")+""),Double.parseDouble(profitDay), 0)))+""+changeMap.get("unit"));
		//电站状态
		int plantType=ServiceUtil.getPlantStatus(pId);
		genIncome.put("status", ServiceUtil.getPlantStatusString(plantType));
		genIncome.put("type", plantType);
		return genIncome;
	}*/


	
}
