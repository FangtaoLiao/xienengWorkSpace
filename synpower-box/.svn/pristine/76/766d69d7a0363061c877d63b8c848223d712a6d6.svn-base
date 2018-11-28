package com.synpower.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpower.bean.PlantInfo;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.service.PlantInfoService;
import com.synpower.util.DownloadUtils;
import com.synpower.util.StringUtil;
import com.synpower.util.Util;

@RestController
@RequestMapping(value="/plantInfowx")
public class WXPlantInfoController extends ErrorHandler{
	@Autowired
	private PlantInfoService plantInfoService;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private ConfigParam configParam;
	@RequestMapping(value="/getPlantType",method=RequestMethod.POST)
	public MessageBean getPlantType(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.getPlantType(jsonData);
	}
	@RequestMapping(value="/deletePlantPhoto",method=RequestMethod.POST)
	public MessageBean deletePlantPhoto(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.deleteWXPlantPhoto(jsonData,getSession());
	}
	@RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String imgUpload(HttpServletRequest request) throws JsonProcessingException {
		//MessageBean msg=new MessageBean();
		//上传路径
		//String uploadPath=File.separator+"etc"+File.separator+"nginx"+ File.separator+"files";
		String uploadPath=configParam.getPlantPhotoURL();
		List<String> path=DownloadUtils.uploadPhoto(request,uploadPath,configParam.getPlantPhotoRequestURL());
		StringBuffer resultBuffer=new StringBuffer();
		if (!Util.isNotBlank(path)) {
			//msg.setMsg("图片上传失败!");
            //msg.setCode(Header.STATUS_FAILED);
		}else{
			
			String plantId=request.getParameter("plantId");
			PlantInfo plantInfo=plantInfoMapper.getPlantById(plantId);
			Map<String, String>map=new HashMap<>(2);
			StringBuffer buffer=new StringBuffer();
			buffer.append(StringUtil.checkBlank(plantInfo.getPlantPhoto()));
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i)).append(";");
				resultBuffer.append(path.get(i)).append(";");
			}
			map.put("url", buffer.toString());
			map.put("plantId", plantId);
			int res=plantInfoMapper.insertPlantPhoto(map);
			//msg.setMsg("图片上传成功!");
	    	//msg.setCode(Header.STATUS_SUCESS);
		}
	    //String path=file.getPath();
       // result = plantManagerServiceImpl.savePlantPhoto(path, request);
        //ObjectMapper mapper = new ObjectMapper();  
        //String json = mapper.writeValueAsString(msg); 
        return resultBuffer.toString();
	}
	@RequestMapping(value="/plantInfo",method=RequestMethod.POST)
	public MessageBean wxplantInfo(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.wxplantInfo(jsonData);
	}
	@RequestMapping(value="/updatePlantInfo",method=RequestMethod.POST)
	public MessageBean updatePlantInfo(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.wxupdatePlantInfo(jsonData,getSession());
	}
	@RequestMapping(value="/insertPlantInfo",method=RequestMethod.POST)
	public MessageBean insertPlantInfo(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.wxinsertPlantInfo(jsonData,getSession());
	}
	@RequestMapping(value="/getDefaultPowerPrice",method=RequestMethod.POST)
	public MessageBean getDefaultPowerPrice(@RequestBody String jsonData) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.getDefaultPowerPrice();
	}
}
