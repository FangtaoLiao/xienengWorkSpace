package com.synpower.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.ReportOrder;
import com.synpower.bean.ReportOrderStoredOnly;
import com.synpower.bean.StorageReportOrder;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.service.ExportService;
import com.synpower.service.PlantReportService;
import com.synpower.util.DownloadUtils;
import com.synpower.util.Util;

@Service
public class ExportServiceImpl implements ExportService{
	private Logger logger = Logger.getLogger(ExportServiceImpl.class);
	@Autowired
	private PlantReportService plantReportService;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Override
	public void runReportExport(HttpServletRequest request, HttpServletResponse response,Session session) throws ServiceException, ServletException, IOException, SessionException, SessionTimeoutException {
	String tokenId = null,plantId = null,dimension = null,
		orderName = null,countTime = null,range = null,
		startTime=null;
	 try{
		 tokenId=request.getParameter("tokenId");
		 plantId=request.getParameter("plantId");
		 dimension=request.getParameter("dimension");
    	 orderName=request.getParameter("orderName");
    	 range=request.getParameter("range");
	 }catch (Exception e) {
		
	 }
	 Map<String, Object>map=new HashMap<>(6);
	 map.put("tokenId", tokenId);
	 map.put("plantId", plantId);
	 map.put("dimension", dimension);
	 map.put("orderName", orderName);
	 map.put("range", range);
	 String jsonData=Util.MapToJson(map);
	 PlantInfo plantInfo = plantMapper.getPlantInfo(plantId);
	 Integer plantTypeA = plantInfo.getPlantTypeA();
	 if (plantTypeA==1) {
		 MessageBean tempMsg=plantReportService.runReportTable(jsonData);
		 List<Map<String, Object>> exportList=new ArrayList<>();
		 map=(Map<String,Object>)tempMsg.getBody();
		 List<ReportOrder>dataList=(List<ReportOrder>) map.get("data");
		 for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object>tempMap=new HashMap<>(8);
			ReportOrder data=dataList.get(i);
			tempMap.put("time", data.getTime());
			tempMap.put("gen", data.getGen());
			tempMap.put("income", data.getIncome());
			tempMap.put("plantRank", data.getPlantRank());
			tempMap.put("ppr", data.getPpr());
			tempMap.put("coal", data.getCoal());
			tempMap.put("tree", data.getTree());
			tempMap.put("co2", data.getCo2());
			exportList.add(tempMap);
		 }
		 Map<String, Object>tempMap=new HashMap<>(8);
		 tempMap.put("time","电站名称" );
		 tempMap.put("gen", map.get("plantName"));
		 tempMap.put("income", "电站容量");
		 tempMap.put("plantRank", map.get("capacity"));
		 tempMap.put("ppr", "统计时间");
		 tempMap.put("coal", range);
		 exportList.add(0, tempMap);
		 String fileName="电站运行报表";
		 String[] paramList=new String[]{"time","gen","income","plantRank","ppr","coal","tree","co2"};
		 String[] titleList=new String[]{"时间","发电量(kWh)","收益估算(元)","单KW发电量","等效利用小时数(h)","节约标准煤(t)","等效植树(棵)","CO2减排(t)"};
	    DownloadUtils.exportPlantExcel(request, response, exportList, Arrays.asList(titleList), fileName,Arrays.asList(paramList));
	} else if(plantTypeA==2) {
		MessageBean tempMsg = plantReportService.storageRunReportsTable(jsonData, session);
		List<Map<String, Object>> exportList=new ArrayList<>();
		 map=(Map<String,Object>)tempMsg.getBody();
		 List<StorageReportOrder>dataList=(List<StorageReportOrder>) map.get("data");
		 for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object>tempMap=new HashMap<>(5);
			StorageReportOrder data=dataList.get(i);
			tempMap.put("time", data.getTime());
			tempMap.put("charge", data.getCharge());
			tempMap.put("discharge", data.getDischarge());
			tempMap.put("income", data.getIncome());
			tempMap.put("conversion", data.getConversion());
			exportList.add(tempMap);
		 }
		 Map<String, Object>tempMap=new HashMap<>(5);
		 tempMap.put("time","电站名称" );
		 tempMap.put("charge", map.get("plantName"));
		 tempMap.put("discharge", "电站容量");
		 tempMap.put("income", map.get("capacity"));
		 tempMap.put("conversion", "统计时间    "+range);
		 exportList.add(0, tempMap);
		 String fileName="电站运行报表";
		 String[] paramList=new String[]{"time","charge","discharge","income","conversion"};
		 String[] titleList=new String[]{"时间","充电量(度)","放电量(度)","收益(元)","转换效率"};
	    DownloadUtils.exportPlantExcelStoredOnly(request, response, exportList, Arrays.asList(titleList), fileName,Arrays.asList(paramList));
	}
	 
	}

	@Override
	public void runReportsExport(HttpServletRequest request, HttpServletResponse response,Session session) throws ServiceException, ServletException, IOException, SessionException, SessionTimeoutException {
	String tokenId = null,areaId = null,dimension = null,orderName = null,range = null;
	 try{
		 tokenId=request.getParameter("tokenId");
		 areaId=request.getParameter("areaId");
		 dimension=request.getParameter("dimension");
    	 orderName=request.getParameter("orderName");
    	 range=request.getParameter("range");
	 }catch (Exception e) {
		
	 }
	 Map<String, Object>map=new HashMap<>(6);
	 map.put("tokenId", tokenId);
	 map.put("areaId", areaId);
	 map.put("dimension", dimension);
	 map.put("orderName", orderName);
	 map.put("range", range);
	 String jsonData=Util.MapToJson(map);
	 MessageBean tempMsg=plantReportService.runReportsTable(jsonData, session);
	 List<Map<String, Object>> exportList=new ArrayList<>();
	 map=(Map<String,Object>)tempMsg.getBody();
	 List<ReportOrder>dataList=(List<ReportOrder>) map.get("data");
	 for (int i = 0; i < dataList.size(); i++) {
		Map<String, Object>tempMap=new HashMap<>(8);
		ReportOrder data=dataList.get(i);
		tempMap.put("plantName", data.getName());
		tempMap.put("capacity", data.getCapacity());
		tempMap.put("inventer", data.getInventerNum() );
		tempMap.put("gen", data.getGen());
		tempMap.put("income", data.getIncome());
		tempMap.put("plantRank", data.getPlantRank());
		tempMap.put("ppr", data.getPpr());
		tempMap.put("coal", data.getCoal());
		tempMap.put("tree", data.getTree());
		tempMap.put("co2", data.getCo2());
		exportList.add(tempMap);
	 }
	 Map<String, Object>tempMap=new HashMap<>(8);
	 tempMap.put("plantName","组织归属" );
	 tempMap.put("capacity", map.get("orgName"));
	 tempMap.put("inventer", "区域");
	 tempMap.put("gen", map.get("areaName"));
	 tempMap.put("income", "统计时间");
	 tempMap.put("plantRank", range);
	 exportList.add(0, tempMap);
	 String fileName="电站对比报表";
	 String[] paramList=new String[]{"plantName","capacity","inventer","gen","income","plantRank","ppr","coal","tree","co2"};
	 String[] titleList=new String[]{"电站名称","电站容量","逆变器数量","发电量(kWh)","收益估算(元)","单KW发电量","等效利用小时数(h)","节约标准煤(t)","等效植树(棵)","CO2减排(t)"};
    DownloadUtils.exportPlantExcel(request, response, exportList, Arrays.asList(titleList), fileName,Arrays.asList(paramList));
	}
	@Override
	public void eletriRunReportExport(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException, ServletException, IOException {
		String tokenId = null,plantId = null,dimension = null,
				orderName = null,countTime = null,range = null,
				startTime=null;
			 try{
				 tokenId=request.getParameter("tokenId");
				 plantId=request.getParameter("plantId");
				 dimension=request.getParameter("dimension");
		    	 orderName=request.getParameter("orderName");
		    	 range=request.getParameter("range");
			 }catch (Exception e) {
				
			 }
			 Map<String, Object>map=new HashMap<>(6);
			 map.put("tokenId", tokenId);
			 map.put("plantId", plantId);
			 map.put("dimension", dimension);
			 map.put("orderName", orderName);
			 map.put("range", range);
			 String jsonData=Util.MapToJson(map);
			 //MessageBean tempMsg=plantReportService.runReportTable(jsonData);
			 MessageBean tempElectri = plantReportService.electricReportTable(jsonData);
			 List<Map<String, Object>> exportList=new ArrayList<>();
			 //map=(Map<String,Object>)tempMsg.getBody();
			 map = (Map<String, Object>)tempElectri.getBody();
			 List<ReportOrderStoredOnly>dataList=(List<ReportOrderStoredOnly>) map.get("data");
			 for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object>tempMap=new HashMap<>(7);
				ReportOrderStoredOnly data=dataList.get(i);
				tempMap.put("time", data.getTime());
				tempMap.put("electriNumUsed", data.getElectriNumUsed());
				tempMap.put("electriMoneyUsed", data.getElectriMoneyUsed());
				tempMap.put("electriNumOn", data.getElectriNumOn());
				tempMap.put("electriMoneyOn", data.getElectriMoneyOn());
				tempMap.put("electriNumOff", data.getElectriNumOff());
				tempMap.put("electriMoneyOff", data.getElectriMoneyOff());
				exportList.add(tempMap);
			 }
			 Map<String, Object>tempMap=new HashMap<>(7);
			 tempMap.put("time","电站名称" );
			 tempMap.put("electriMoneyUsed", map.get("plantName"));
			 tempMap.put("electriNumOn", "电站容量");
			 tempMap.put("electriMoneyOn", map.get("capacity"));
			 tempMap.put("electriNumOff", "统计时间");
			 tempMap.put("electriMoneyOff", range);
			 
			 exportList.add(0, tempMap);
			 String fileName="电费报表";
			 String[] paramList=new String[]{"time","electriNumUsed","electriMoneyUsed","electriNumOn","electriMoneyOn","electriNumOff","electriMoneyOff"};
			 String[] titleList=new String[]{"时间","用电量(度)","用电电费(元)","上网电量(度)","上网费用(元)","下网电量(度)","下网费用(元)"};
		    DownloadUtils.exportPlantExcelForElectric(request, response, exportList, Arrays.asList(titleList), fileName,Arrays.asList(paramList));
		
	}

}
