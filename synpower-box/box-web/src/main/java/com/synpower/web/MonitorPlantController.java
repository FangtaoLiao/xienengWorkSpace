package com.synpower.web;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.MonitorPlantService;
import com.synpower.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping(value = "/monitor",method=RequestMethod.POST)
public class MonitorPlantController extends ErrorHandler{
	@Autowired
	private MonitorPlantService monitorPlantService;
	@Autowired
	private MonitorService monitorService;
	/** 
	  * @Title:  singleBasicInfo 
	  * @Description:  单电站基本信息 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:36:05
	*/ 
	@RequestMapping(value="/singleBasicInfo",method=RequestMethod.POST)
	public MessageBean singleBasicInfo(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException{
		return monitorPlantService.singleBasicInfo(jsonData);
	}
	@RequestMapping(value="/getBatCap",method=RequestMethod.POST)
	public MessageBean getBatCap(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException, SessionException{
		return monitorPlantService.getBatCap(jsonData);
	}
	@RequestMapping(value="/getSingleEneProfit",method=RequestMethod.POST)
	public MessageBean getSingleEneProfit(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException, SessionException{
		return monitorPlantService.getSingleEneProfit(jsonData);
	}
	@RequestMapping(value="/getCNTopo",method=RequestMethod.POST)
	public MessageBean getCNTopo(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException, SessionException{
		return monitorPlantService.getCNTopo(jsonData);
	}
	@RequestMapping(value="/getSingleElecPro",method=RequestMethod.POST)
	public MessageBean getSingleElecPro(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.getSingleElecPro(jsonData);
	}
	@RequestMapping(value="/getWeatherInfo",method=RequestMethod.POST)
	public MessageBean getWeatherInfo(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.getWeatherInfo(jsonData);
	}
	@RequestMapping(value="/getSinglePowerCurve",method=RequestMethod.POST)
	public MessageBean getSinglePowerCurve(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.getSinglePowerCurve(jsonData);
	}
	/** 
	  * @Title:  deviceDetail 
	  * @Description:  设备基本信息 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:35:57
	*/ 
	@RequestMapping(value="/deviceDetail",method=RequestMethod.POST)
	public MessageBean deviceDetail(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException{
		return monitorPlantService.deviceDetail(jsonData);
	}
	/** 
	  * @Title:  deviceStatus 
	  * @Description:  设备的数据遥控显示
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @throws SessionException 
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:35:44
	*/ 
	@RequestMapping(value="/deviceStatus",method=RequestMethod.POST)
	public MessageBean deviceStatus(@RequestBody String jsonData)throws ServiceException,SessionTimeoutException, SessionException{
		return monitorPlantService.deviceStatus(jsonData,getSession());
	}
	/** 
	  * @Title:  updateValueOfGuid 
	  * @Description:  下发指令
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:35:23
	*/ 
	@RequestMapping(value="/updateValueOfGuid")
	public MessageBean updateValueOfGuid(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.updateValueOfGuid(jsonData,getSession());
	}
	/** 
	  * @Title:  getPowerCurrentOfDevice 
	  * @Description:  设备实时功率曲线 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:35:10
	*/ 
	@RequestMapping(value="/getPowerCurrentOfDevice")
	public MessageBean getPowerCurrentOfDevice(@RequestBody String jsonData)throws ServiceException, SessionException{
		return monitorPlantService.getPowerCurrentOfDevice(jsonData);
	}
	/** 
	  * @Title:  getPowerCountOfPlant 
	  * @Description:  单电站收益柱状图 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException
	  * @throws ParseException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:34:56
	*/ 
	@RequestMapping(value="/getPlantIncome.do")
	public MessageBean getPlantIncome(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.getPlantIncome(jsonData);
	}
	/** 
	  * @Title:  genPPR 
	  * @Description:  计算等效小时利用率 
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException
	  * @throws ParseException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:34:30
	*/ 
	@RequestMapping(value="/genPPR")
	public MessageBean genPPR(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorService.getSinglePower(jsonData);
	}
	/** 
	  * @Title:  getCurrentIncomeOfPlant 
	  * @Description:  电站实时收益及功率 
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException
	  * @throws ParseException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:34:17
	*/ 
	@RequestMapping(value="/getCurrentIncomeOfPlant")
	public MessageBean getCurrentIncomeOfPlant(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.getCurrentIncomeOfPlant(jsonData);
	}
	/** 
	  * @Title:  fuzzySearchPlant 
	  * @Description:  电站模糊搜索 
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException
	  * @throws ParseException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:34:07
	*/ 
	@RequestMapping(value="/fuzzySearchPlant")
	public MessageBean fuzzySearchPlant(@RequestBody String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorPlantService.fuzzySearchPlant(jsonData,getSession());
	}
	
	/**
	 * @Author lz
	 * @Description:重写
	 * @param: [jsonData, request]
	 * @return: {com.synpower.msg.MessageBean}
	 * @Date: 2018/11/6 11:35
	 **/
	@RequestMapping(value="/plantDist",method=RequestMethod.POST)
	public MessageBean plantDist(@RequestBody String jsonData, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException {
		//long start = System.currentTimeMillis();
		//MessageBean messageBean = monitorPlantService.plantDist(jsonData, getSession(), request);
		//return messageBean;
		//long end = System.currentTimeMillis();
		//System.out.println("old=========" + (end - start));
		//MessageBean newmessageBean = );
		//long end2 = System.currentTimeMillis();
		//System.out.println("new===========" + (end2 - end));
		return monitorPlantService.plantDist(jsonData, getSession());
	}

	@RequestMapping(value="/getStoList",method=RequestMethod.POST)
	public MessageBean getStoList(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.getStoList(jsonData,getSession(),request);
	}
	
	/**
	 * 
	  * @Title:  plantOpen 
	  * @Description:  列表展开信息 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月29日下午4:04:15
	 */
	@RequestMapping(value="/plantOpen",method=RequestMethod.POST)
	public MessageBean plantOpen(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.plantOpen(jsonData,getSession(),request);
	}
	
	/**
	 * 
	  * @Title:  getPlantOpen 
	  * @Description:  多电站列表储能展开 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	 * @throws ParseException 
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年4月3日上午9:19:49
	 */
	@RequestMapping(value="/getPlantOpen",method=RequestMethod.POST)
	public MessageBean getPlantOpen(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException, ParseException {
		return monitorPlantService.getPlantOpen(jsonData,getSession(),request);
	}
	
	/**
	 * 
	  * @Title:  getSimplePlist 
	  * @Description:  电站状态、发电量列表
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月22日下午5:34:34
	 */
/*	@RequestMapping(value="/getSimplePlist",method=RequestMethod.POST)
	public MessageBean getSimplePlist(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.getSimplePlist(jsonData,getSession(),request);
	}*/
	@RequestMapping(value="/getMultiEtGenProfit",method=RequestMethod.POST)
	public MessageBean getMultiEtGenProfit(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.getMultiEtGenProfit(jsonData,getSession(),request);
	}
	
	@RequestMapping(value="/getPlantPower",method=RequestMethod.POST)
	public MessageBean getPlantPower(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.getPlantPower(jsonData);
	}
	@RequestMapping(value="/plantStatusPower",method=RequestMethod.POST)
	public MessageBean plantStatusPower(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.plantStatusPower(jsonData);
	}
	@RequestMapping(value="/singlePlantDistribution",method=RequestMethod.POST)
	public MessageBean singlePlantDistribution(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.singlePlantDistribution(jsonData);
	}
	@RequestMapping(value="/singlePlantStatus",method=RequestMethod.POST)
	public MessageBean singlePlantStatus(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.singlePlantStatus(jsonData);
	}
	@RequestMapping(value="/getPlantName",method=RequestMethod.POST)
	public MessageBean getPlantName(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		return monitorPlantService.getPlantName(jsonData);
	}
	/** 
	  * @Title:  getCollectorList 
	  * @Description:  设备监控数据采集器表格
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月14日下午3:11:36
	*/ 
	@RequestMapping(value="/getCollectorList",method=RequestMethod.POST)
	public MessageBean getCollectorList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getCollectorList(str,getSession());
	}
	@RequestMapping(value="/getInverterList",method=RequestMethod.POST)
	public MessageBean getInverterList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getInverterList(str,getSession());
	}
	@RequestMapping(value="/getCenInverterList",method=RequestMethod.POST)
	public MessageBean getCenInverterList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getCenInverterList(str,getSession());
	}
	@RequestMapping(value="/getPCSList",method=RequestMethod.POST)
	public MessageBean getPCSList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getPCSList(str,getSession());
	}
	@RequestMapping(value="/getBMSList",method=RequestMethod.POST)
	public MessageBean getBMSList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getBMSList(str,getSession());
	}
	@RequestMapping(value="/getMeterList",method=RequestMethod.POST)
	public MessageBean getMeterList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getMeterList(str,getSession());
	}
	@RequestMapping(value="/getConfBoxList",method=RequestMethod.POST)
	public MessageBean getConfBoxList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getConfBoxList(str,getSession());
	}
	/** 
	  * @Title:  getBoxChangeList 
	  * @Description:  获取箱变设备列表 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年6月8日上午10:22:40
	*/ 
	@RequestMapping(value="/getBoxChangeList",method=RequestMethod.POST)
	public MessageBean getBoxChangeList(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getBoxChangeList(str,getSession());
	}
	
	@RequestMapping(value="/getEnviroDetectorList",method=RequestMethod.POST)
	public MessageBean getEnviroDetectorList(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getEnviroDetectorList(str,getSession());
	}
	
	@RequestMapping(value="/getPvTopo",method=RequestMethod.POST)
	public MessageBean getPvTopo(@RequestBody String str)throws ServiceException, SessionException, SessionTimeoutException{
		return monitorPlantService.getPvTopo(str,getSession());
	}
}
