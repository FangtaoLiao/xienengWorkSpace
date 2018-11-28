package com.synpower.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.PlantReportService;
import com.synpower.util.SystemCache;
import com.synpower.util.Util;

@RestController
@RequestMapping(value="/report")
public class PlantReportController extends ErrorHandler{
	@Autowired
	PlantReportService plantReportService;
	
	@RequestMapping(value="/fuzzySearchPlant",method=RequestMethod.POST)
	public MessageBean fuzzySearchPlant(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.fuzzySearchPlant(jsonData,getSession());
	}
	@RequestMapping(value="/runReportLine",method=RequestMethod.POST)
	public MessageBean runReportLine(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
			return plantReportService.runReportLine(jsonData);
	}
	@RequestMapping(value="/electricReportLine",method=RequestMethod.POST)
	public MessageBean electricReportLine(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.electricReportLine(jsonData);
	}
	@RequestMapping(value="/runReportTable",method=RequestMethod.POST)
	public MessageBean runReportTable(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		Map<String, Object>map=Util.parseURL(jsonData);
		String plantId=String.valueOf(map.get("plantId"));
		String plantType=SystemCache.plantTypeList.get(plantId);
		if ("1".equals(plantType)) {
			return plantReportService.runReportTable(jsonData);
		}else  {
			return plantReportService.storageRunReportsTable(jsonData,getSession());
		}
	} 
	@RequestMapping(value="/electricReportTable",method=RequestMethod.POST)
	public MessageBean electricReportTable(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.electricReportTable(jsonData);
	} 
	@RequestMapping(value="/getAreaDistribution",method=RequestMethod.POST)
	public MessageBean getAreaDistribution(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.getAreaDistribution(Util.parseURL(jsonData),getSession());
	}
	@RequestMapping(value="/saveUserSet",method=RequestMethod.POST)
	public MessageBean saveUserSet(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.saveUserSet(jsonData,getSession());
	}
	@RequestMapping(value="/runReportsLine",method=RequestMethod.POST)
	public MessageBean runReportsLine(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.runReportsLine(jsonData,getSession());
	}
	@RequestMapping(value="/runReportsTable",method=RequestMethod.POST)
	public MessageBean runReportsTable(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.runReportsTable(jsonData,getSession());
	}
	@RequestMapping(value="/fuzzySearchPlantByAreaId",method=RequestMethod.POST)
	public MessageBean fuzzySearchPlantByAreaId(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.fuzzySearchPlantByAreaId(jsonData,getSession());
	}
	@RequestMapping(value="/savePersonalSet",method=RequestMethod.POST)
	public MessageBean savePersonalSet(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.savePersonalSet(jsonData,getSession());
	}
	@RequestMapping(value="/getDeviceForPlant",method=RequestMethod.POST)
	public MessageBean getDeviceForPlant(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.getDeviceForPlant(jsonData,getSession());
	}
	@RequestMapping(value="/getYXGuidForInventers",method=RequestMethod.POST)
	public MessageBean getYXGuidForInventers(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.getYXGuidForInventers(jsonData,getSession());
	}
	@RequestMapping(value="/getHistoryLine",method=RequestMethod.POST)
	public MessageBean getHistoryLine(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.getHistoryLine(jsonData,getSession());
	}
	@RequestMapping(value="/getHistoryTime",method=RequestMethod.POST)
	public MessageBean getHistoryTime(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.getHistoryTime(jsonData,getSession());
	}
	@RequestMapping(value="/reviewData",method=RequestMethod.POST)
	public MessageBean reviewData(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.reviewData(jsonData,getSession());
	}
	@RequestMapping(value="storageRunReportsTable",method=RequestMethod.POST)
	public MessageBean storageRunReportsTable(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.storageRunReportsTable(jsonData,getSession());
	}
}
