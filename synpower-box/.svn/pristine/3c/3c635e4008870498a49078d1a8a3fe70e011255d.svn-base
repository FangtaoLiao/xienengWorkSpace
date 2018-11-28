package com.synpower.serviceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysConfig;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysScreen;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysConfigMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysScreenMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.SystemService;
import com.synpower.util.DownloadUtils;
import com.synpower.util.ServiceUtil;
import com.synpower.util.StringUtil;
import com.synpower.util.Util;

@Service
public class SystemServiceImpl implements SystemService {
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private PlantInfoMapper plantInfoMapper;
	@Autowired
	private SysScreenMapper screenMapper;
	@Autowired
	private ConfigParam configParam;
	@Autowired
	private PlantInfoServiceImpl plantInfoServiceImpl;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysConfigMapper configMapper;
	@Override
	public MessageBean getSystemInfo(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException{
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		User u = session.getAttribute(tokenId);
		String orgId = String.valueOf(map.get("orgId"));
		//得到所有组织列表
		List<SysOrg>orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>(10);
		//获取当前用户所在组织及其子组织ids
		reList.add(orgId);
		reList = ServiceUtil.getTree(orgList, orgId,reList);
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put("reList", reList);
		SysOrg org = orgMapper.getLogoAndName(orgId);
		if(org == null){
			msg.setCode(Header.STATUS_SUCESS);
			msg.setMsg("该组织没有数据");
			return  msg;
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		SysScreen screen = screenMapper.getScreenByOrgId(orgId);
		String totalCapacity=null;
		if(screen != null){
			if(screen.getPlantAutomaticCalculation() != null && !screen.getPlantAutomaticCalculation().equals("null") && !screen.getPlantAutomaticCalculation().equals("")){
				if(screen.getPlantAutomaticCalculation().equals("0")){
					totalCapacity = screen.getPlantArtificialCapacity()+"";
					totalCapacity = Util.roundDouble(totalCapacity);
					resultMap.put("plantArtificialCapacity", totalCapacity);
					resultMap.put("unit", screen.getCapacityUnit());
					resultMap.put("plantAutomaticCalculation", screen.getPlantAutomaticCalculation());
				}else if(screen.getPlantAutomaticCalculation().equals("1")){
					// 根据组织id查询出电站信息
					List<PlantInfo> list = plantMapper.getPlantByOrgIds(paramMap);
					List<Double> capacityList = new ArrayList<Double>();
					// 将装机容量全部转换成KW为单位的数值
					if(Util.isNotBlank(list)){
						for (PlantInfo plantInfo : list) {
							if(plantInfo.getUnit().equalsIgnoreCase("MW")){
								capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000, 0));
							}else if(plantInfo.getUnit().equalsIgnoreCase("GW")){
								capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000000, 0));
							}else{
								capacityList.add(plantInfo.getCapacity());
							}
						}
					}
					Double totalCapacityD = 0.0;
					// 总和各个组织的装机容量
					for (Double num : capacityList) {
						totalCapacityD = Util.addFloat(totalCapacityD, num, 0);
					}
					totalCapacityD = Util.roundDouble(totalCapacityD);
					Map<String, Object> changeMap = ServiceUtil.changeUnit(new String[]{"kW","MW","GW"},totalCapacityD,1000);
					resultMap.put("plantArtificialCapacity", changeMap.get("value"));
					resultMap.put("unit", changeMap.get("unit"));
					resultMap.put("plantAutomaticCalculation", screen.getPlantAutomaticCalculation());
				}
			}
		}
		resultMap.put("controlName", org.getControlName());
		resultMap.put("controlNum", org.getControlNum());
		resultMap.put("businessName", org.getBusinessName());
		resultMap.put("businessNum", org.getBusinessNum());
		resultMap.put("serviceName", org.getServiceName());
		resultMap.put("serviceNum", org.getServiceNum());
		resultMap.put("roleName", org.getRoleName());
		resultMap.put("roleNum", org.getRoleNum());
		
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg("组织数据查询成功");
		return msg;
	}
	@Override
	public MessageBean updateSystemInfo(String str, Session session)throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId")+"");
		String orgId = String.valueOf(map.get("orgId"));
		if(orgId == null || orgId.equals("null") || orgId.equals("")){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		int id = Integer.parseInt(orgId);
//		int orgId = Integer.parseInt(((SysOrg)u.getUserOrg()).getId()+"");
		SysOrg org = new SysOrg();
		org.setControlName(map.get("controlName")+"");
		org.setControlNum(map.get("controlNum")+"");
		org.setBusinessName(map.get("businessName")+"");
		org.setBusinessNum(map.get("businessNum")+"");
		org.setServiceName(map.get("serviceName")+"");
		org.setServiceNum(map.get("serviceNum")+"");
		org.setRoleName(map.get("roleName")+"");
		org.setRoleNum(map.get("roleNum")+"");
		org.setId(id);
		int result = orgMapper.updateSystemInfo(org);
		Map<String, Object> paramMap = new HashMap<String,Object>(2);
		//是否开启自动计算
		paramMap.put("plantAutomaticCalculation", String.valueOf(map.get("plantAutomaticCalculation")));
		//手动填入的值
		paramMap.put("plantArtificialCapacity", Float.parseFloat(String.valueOf(map.get("plantArtificialCapacity"))));
		paramMap.put("unit", String.valueOf(map.get("unit")));
		paramMap.put("orgId", orgId);
		int autoResult = screenMapper.updateAutoCal(paramMap);
//		PlantInfo plant = new PlantInfo();
//		plant.setPlantAutomaticCalculation(map.get("plantAutomaticCalculation")+"");
//		plant.setPlantArtificialCapacity(Float.parseFloat(map.get("plantArtificialCapacity")+""));
//		plantInfoMapper.updateAutoCal(plant);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setMsg("修改成功");
		return msg;
	}
	@Override
	public MessageBean getSystemBasic(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId")+"");
		String orgId = String.valueOf(map.get("orgId"));
		if(orgId == null || orgId.equals("null") || orgId.equals("")){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		//如果本身没有个性化设置就自动带出上一级设置
		SysOrg org = orgMapper.getOrgById(orgId);
		String systemLogo = org.getSystemLogo();
		String systemName = org.getSystemName();
		String screenLogo = org.getScreenLogo();
		String screenName = org.getScreenName();
		String orgDesc = org.getOrgDesc();
		String orgPhoto = org.getOrgPhoto();
		//获取宣传图片
		String propagandaPhotos = org.getPropagandaPhoto();
		boolean flag1 = false;
		boolean flag3 = false;
		boolean flag6 = false;
		boolean flag7 = false;
		if(!Util.isNotBlank(systemLogo)){
			systemLogo = findSystemLogo(org.getFatherId());
			flag1 = true;
		}
//		if(systemName == null || systemName.equals("null") || systemName.equals("")){
//			systemName = findSystemName(org.getFatherId());
//			flag2 = true;
//		}
		if(!Util.isNotBlank(screenLogo)){
			screenLogo = findScreenLogo(org.getFatherId());
			flag3 = true;
		}
//		if(screenName == null || screenName.equals("null") || screenName.equals("")){
//			screenName = findScreenName(org.getFatherId());
//			flag4 = true;
//		}
//		if(orgDesc == null || orgDesc.equals("null") || orgDesc.equals("")){
//			orgDesc = findOrgDesc(org.getFatherId());
//			flag5 = true;
//		}
		if(!Util.isNotBlank(orgPhoto)){
			orgPhoto = findOrgPhoto(org.getFatherId());
			flag6 = true;
		}
		if(!Util.isNotBlank(propagandaPhotos)){
			propagandaPhotos = findPropagandaPhoto(org.getFatherId());
			flag7 = true;
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("systemLogoFather", flag1);
		resultMap.put("screenLogoFather", flag3);
		resultMap.put("orgPhotoFather", flag6);
		resultMap.put("propagandaPhotoFather", flag7);
		resultMap.put("systemLogo", systemLogo);
		resultMap.put("systemName", systemName);
		resultMap.put("screenLogo", screenLogo);
		resultMap.put("screenName", screenName);
		resultMap.put("orgDesc", orgDesc);
		resultMap.put("orgPhoto", orgPhoto);
		List<String> tempPhotos = new ArrayList<String>();
		if(Util.isNotBlank(propagandaPhotos)){
			String [] propagandaPhoto = propagandaPhotos.split(";");
			tempPhotos = Arrays.asList(propagandaPhoto);
		}
		resultMap.put("propagandaPhoto", tempPhotos);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}
	public String findSystemLogo(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String systemLogo = org.getSystemLogo();
			if(!Util.isNotBlank(systemLogo)){
				return findSystemLogo(org.getFatherId());
			}else{
				return systemLogo;
			}
		}else{
			return result;
		}
	}
	public String findSystemName(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String systemName = org.getSystemName();
			if(!Util.isNotBlank(systemName)){
				return findSystemName(org.getFatherId());
			}else{
				return systemName;
			}
		}else{
			return result;
		}
	}
	public String findScreenLogo(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String screenLogo = org.getScreenLogo();
			if(!Util.isNotBlank(screenLogo)){
				return findScreenLogo(org.getFatherId());
			}else{
				return screenLogo;
			}
		}else{
			return result;
		}
	}
	public String findScreenName(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String screenName = org.getScreenName();
			if(!Util.isNotBlank(screenName)){
				return findScreenName(org.getFatherId());
			}else{
				return screenName;
			}
		}else{
			return result;
		}
	}
	public String findOrgDesc(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String orgDesc = org.getOrgDesc();
			if(!Util.isNotBlank(orgDesc)){
				return findOrgDesc(org.getFatherId());
			}else{
				return orgDesc;
			}
		}else{
			return result;
		}
	}
	public String findOrgPhoto(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String orgPhoto = org.getOrgPhoto();
			if(!Util.isNotBlank(orgPhoto)){
				return findOrgPhoto(org.getFatherId());
			}else{
				return orgPhoto;
			}
		}else{
			return result;
		}
	}
	public String findPropagandaPhoto(int fatherId){
		String result = "";
		if(fatherId != 0){
			SysOrg org = orgMapper.getOrgById(fatherId+"");
			String propagandaPhoto = org.getPropagandaPhoto();
			if(!Util.isNotBlank(propagandaPhoto)){
				return findPropagandaPhoto(org.getFatherId());
			}else{
				return propagandaPhoto;
			}
		}else{
			return result;
		}
	}
	public SysScreen getLogo(String orgId){
		SysOrg org = orgMapper.getOrgFatherId(orgId);
		SysScreen screen = screenMapper.getScreenByOrgId(orgId);
		if(screen == null){
			if(org.getFatherId() != 0){
				return getLogo(org.getFatherId()+"");
			}else{
				return null;
			}
		}else{
			if(screen.getScreenLogo() == null || screen.getSystemLogo() == null || screen.getSystemName() == null){
				if(org.getFatherId() != 0){
					return getLogo(org.getFatherId()+"");
				}else{
					return null;
				}
			}
			return screen;
		}
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean updateSystemBasic(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(String.valueOf(map.get("tokenId")));
		String orgId = String.valueOf(map.get("orgId"));
		if(orgId == null || orgId.equals("null") || orgId.equals("")){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		String systemName = String.valueOf(map.get("systemName"));
		String screenName = String.valueOf(map.get("screenName"));
		String orgDesc = String.valueOf(map.get("orgDesc"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("systemName", systemName);
		//更改大屏名称
		paramMap.put("screenName", screenName);
		paramMap.put("orgId", orgId);
		paramMap.put("orgDesc", orgDesc);
		int re = orgMapper.updateSystemName(paramMap);
		if(re == 0){
			msg.setCode(Header.STATUS_FAILED);
			msg.setMsg("系统名称设置失败！");
			return msg;
		}
		//更改公司简介
//		paramMap.clear();
//		paramMap.put("orgId", orgId);
//		paramMap.put("orgDesc", orgDesc);
//		int result = orgMapper.updateOrgDesc(paramMap);
//		if(re == 0){
//			msg.setCode(Header.STATUS_FAILED);
//			msg.setMsg("公司简介设置失败！");
//			return msg;
//		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody("设置成功！");
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateSystemLogo(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String orgId = request.getParameter("orgId");
		if(orgId == null || orgId.equals("null") || orgId.equals("")){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		String imgUrl = request.getParameter("imgUrl");
		if(null != imgUrl  && !"null".equals(imgUrl) && !"".equals(imgUrl)){
			//将原先的图片设置为空
			int result = orgMapper.updateSystemLogoNull(orgId);
			if(result == 0){
				logger.error(StringUtil.appendStr(" logo置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getSystemPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" logo文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" logo文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getSystemPhotoUrl();
		String iconRequestUrl = configParam.getSystemPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			SysOrg org = orgMapper.getOrgById(orgId);
			if(Util.isNotBlank(org.getSystemLogo())){
				 msg.setCode(Header.STATUS_FAILED);
				 msg.setMsg("只能上传一张图片，请核实后上传");
				 return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("orgId", orgId);
			parameterMap.put("systemLogo", buffer.toString());
			int re1 = orgMapper.updateSystemLogo(parameterMap);
			if(re1 != 0){
				logger.info(StringUtil.appendStr(" logo上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" logo上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateScreenLogo(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String orgId = request.getParameter("orgId");
		if(orgId == null || orgId.equals("null") || orgId.equals("")){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		String imgUrl = request.getParameter("imgUrl");
		if(null != imgUrl && !"null".equals(imgUrl) && !"".equals(imgUrl)){
			//将原先的图片设置为空
			int result = orgMapper.updateScreenLogoNull(orgId);
			if(result == 0){
				logger.error(StringUtil.appendStr(" logo置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getSystemPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" logo文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" logo文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getSystemPhotoUrl();
		String iconRequestUrl = configParam.getSystemPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			SysOrg org = orgMapper.getOrgById(orgId);
			if(Util.isNotBlank(org.getScreenLogo())){
				 msg.setCode(Header.STATUS_FAILED);
				 msg.setMsg("只能上传一张图片，请核实后上传");
				 return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("orgId", orgId);
			parameterMap.put("screenLogo", buffer.toString());
			int re1 = orgMapper.updateScreenLogo(parameterMap);
			if(re1 != 0){
				logger.info(StringUtil.appendStr(" logo上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" logo上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateLoginPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		String imgUrls = request.getParameter("imgUrl");
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String domainName = request.getParameter("domainName");
		if(null != imgUrls && !"".equals(imgUrls) && !"null".equals(imgUrls)){
			SysConfig config = configMapper.getInfoByDomainName(domainName);
			//删除照片路径
			String[] imgArr = StringUtils.split(StringUtil.checkBlank(config.getLoginPhoto()),";");
			String[] imgUrl = imgUrls.split(";");
			if (imgArr.length == 0) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			boolean b=true;
			for (int i = 0; i < imgUrl.length; i++) {
				for (int j = 0; j < imgArr.length; j++) {
					if(imgArr[j] != null){
						if (imgArr[j].equals(imgUrl[i])) {
							imgArr[j]=null;
							b=false;
						}
					}
				}
			}
			if (b) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			String tempUrl="";
			for (int i = 0; i < imgArr.length; i++) {
				if (null!=imgArr[i]) {
					tempUrl+=imgArr[i]+";";
				}
			}
			Map<String, Object> paramMap =new HashMap<String,Object>(2);
			paramMap.put("loginPhoto", tempUrl);
			paramMap.put("domainName", domainName);
			int res = configMapper.updateLoginPhoto(paramMap);
			if (res>0) {
				for (String string : imgUrl) {
					string=configParam.getSystemPhotoUrl()+string.substring(string.lastIndexOf("/"));
					File file=new File(string);
					if (file.exists()) {
						file.delete();
						logger.info("  图片删除成功!   操作人:"+u.getId()+"_"+u.getUserName()) ;
					}
				}
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_PHOTO_SUCCUESS);
			}else{
				logger.error("  图片删除失败!    操作人:"+u.getId()+"_"+u.getUserName()) ;
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
			}
		}
		String uploadPath=configParam.getSystemPhotoUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,uploadPath,configParam.getSystemPhotoRequestUrl());
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrls)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			SysConfig config = configMapper.getInfoByDomainName(domainName);
			String loginPhoto = config.getLoginPhoto();
			Map<String, String>map=new HashMap<>(2);
			StringBuffer buffer=new StringBuffer();
			if(Util.isNotBlank(loginPhoto)){
				String[] arr = loginPhoto.split(";");
				int num = arr.length + path.size();
				if(num > 3){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
				buffer.append(loginPhoto);
			}else{
				if(path.size() > 3){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i)).append(";");
			}
			map.put("loginPhoto", buffer.toString());
			map.put("domainName", domainName);
			int res = configMapper.updateLoginPhoto(map);
			if(res != 0){
				logger.info(StringUtil.appendStr(" 电站图片上传成功! 操作者:",logUserName));
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 电站图片上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		Map<String, String>map=new HashMap<>(2);
		String systemName = request.getParameter("systemName");
		map.put("systemName", systemName);
		map.put("domainName", domainName);
		int nameResult = configMapper.updateSystemName(map);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateLoginLogo(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String imgUrl = request.getParameter("imgUrl");
		String domainName = request.getParameter("domainName");
		if(null != imgUrl && !"null".equals(imgUrl) && !"".equals(imgUrl)){
			//将原先的图片设置为空
			int result = configMapper.updateLoginLogoNull(domainName);
			if(result == 0){
				logger.error(StringUtil.appendStr(" logo置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getSystemPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" logo文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" logo文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getSystemPhotoUrl();
		String iconRequestUrl = configParam.getSystemPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			SysConfig config = configMapper.getInfoByDomainName(domainName);
			if(Util.isNotBlank(config.getLoginLogo())){
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg("只能上传一张图片，请核实后上传");
				return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("domainName", domainName);
			parameterMap.put("loginLogo", buffer.toString());
			int re1 = configMapper.updateLoginLogo(parameterMap);
			if(re1 != 0){
				logger.info(StringUtil.appendStr(" logo上传成功! 操作者:",logUserName));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" logo上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updateQrCode(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		String imgUrl = request.getParameter("imgUrl");
		if(null != imgUrl && !"null".equals(imgUrl) && !"".equals(imgUrl) ){
			//将原先的图片设置为空
			int result = screenMapper.updateQrCodeNull(((SysOrg)(u.getUserOrg())).getId()+"");
			if(result == 0){
				throw new ServiceException(Msg.DELETE_PHOTO_FAILED);
			}
			//数据库操作成功后删除文件中用户的头像图片
			String pathRoot = request.getSession().getServletContext().getRealPath("/");
			File destFile = new File(pathRoot,imgUrl);
			if (destFile.exists()) {
				destFile.delete();
			}else{
				throw new ServiceException(Msg.ICON_NOT_EXISTS);
			}
		}
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MessageBean getSystemContent(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String domainName = String.valueOf(map.get("domainName"));
		SysConfig tempConfig = configMapper.getInfoByDomainName(domainName);
		// 当查询不到此域名信息之后自动增加
		if(tempConfig == null){
			int insertResult = configMapper.insertInfo(domainName);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>(3);
		SysConfig config = configMapper.getInfoByDomainName(domainName);
		if(config != null){
			List<String> loginPhotos = new ArrayList<String>();
			String loginPhoto = config.getLoginPhoto();
			if(Util.isNotBlank(loginPhoto)){
				String [] tempPhoto = loginPhoto.split(";");
				loginPhotos = Arrays.asList(tempPhoto);
			}
			resultMap.put("loginPhoto", loginPhotos);
			resultMap.put("loginLogo", config.getLoginLogo());
			resultMap.put("systemName", config.getSystemName());
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}
	@Override
	public MessageBean getAutomaticCalculation(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String tokenId = String.valueOf(map.get("tokenId"));
		String automaticCalculation = String.valueOf(map.get("automaticCalculation"));
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		// 获取组织id
		String orgId = String.valueOf(map.get("orgId"));
		// 根据组织id获取大屏基本信息
		SysScreen screen = screenMapper.getScreenByOrgId(orgId);
		//得到所有组织列表
		List<SysOrg>orgList = orgMapper.getAllOrg();
		List<String> reList = new ArrayList<>(10);
		//获取当前用户所在组织及其子组织ids
		reList.add(orgId);
		reList = ServiceUtil.getTree(orgList, orgId,reList);
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put("reList", reList);
		String totalCapacity=null;
		if(screen != null){
			if(screen.getPlantAutomaticCalculation() != null && !screen.getPlantAutomaticCalculation().equals("null") && !screen.getPlantAutomaticCalculation().equals("")){
				if(automaticCalculation.equals("0")){
					totalCapacity = screen.getPlantArtificialCapacity()+"";
					totalCapacity = Util.roundDouble(totalCapacity);
					resultMap.put("totalCapacity", totalCapacity);
					resultMap.put("unit", screen.getCapacityUnit());
				}else if(automaticCalculation.equals("1")){
					// 根据组织id查询出电站信息
					List<PlantInfo> list = plantMapper.getPlantByOrgIds(paramMap);
					List<Double> capacityList = new ArrayList<Double>();
					// 将装机容量全部转换成KW为单位的数值
					if(Util.isNotBlank(list)){
						for (PlantInfo plantInfo : list) {
							if(plantInfo.getUnit().equalsIgnoreCase("MW")){
								capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000, 0));
							}else if(plantInfo.getUnit().equalsIgnoreCase("GW")){
								capacityList.add(Util.multFloat(plantInfo.getCapacity(), 1000000, 0));
							}else{
								capacityList.add(plantInfo.getCapacity());
							}
						}
					}
					Double totalCapacityD = 0.0;
					// 总和各个组织的装机容量
					for (Double num : capacityList) {
						totalCapacityD = Util.addFloat(totalCapacityD, num, 0);
					}
					totalCapacityD = Util.roundDouble(totalCapacityD);
					Map<String, Object> changeMap = ServiceUtil.changeUnit(new String[]{"kW","MW","GW"},totalCapacityD,1000);
					resultMap.put("totalCapacity", changeMap.get("value"));
					resultMap.put("unit", changeMap.get("unit"));
				}
			}
			msg.setCode(Header.STATUS_SUCESS);
			msg.setBody(resultMap);
		}else{
			throw new ServiceException("贵公司大屏信息为空");
		}
		return msg;
	}
	@Override
	public MessageBean getSysInfo(String str, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(str);
		String domainName = String.valueOf(map.get("path"));
		SysConfig tempConfig = configMapper.getInfoByDomainName(domainName);
		// 当查询不到此域名信息之后自动增加
		if(tempConfig == null){
			int insertResult = configMapper.insertInfo(domainName);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>(3);
		SysConfig config = configMapper.getInfoByDomainName(domainName);
		if(config != null){
			List<String> loginPhotos = new ArrayList<String>();
			String loginPhoto = config.getLoginPhoto();
			if(Util.isNotBlank(loginPhoto)){
				String [] tempPhoto = loginPhoto.split(";");
				loginPhotos = Arrays.asList(tempPhoto);
			}
			resultMap.put("scrollPic", loginPhotos);
			resultMap.put("logoPic", config.getLoginLogo());
			resultMap.put("logoTxt", config.getSystemName());
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		return msg;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	@Override
	public MessageBean updatePropagandaPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		String imgUrls = request.getParameter("imgUrl");
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String orgId = request.getParameter("orgId");
		if(!Util.isNotBlank(orgId)){
			orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		}
		if(null != imgUrls && !"".equals(imgUrls) && !"null".equals(imgUrls)){
			SysOrg org = orgMapper.getOrgById(orgId);
			//删除照片路径
			String[] imgArr = StringUtils.split(StringUtil.checkBlank(org.getPropagandaPhoto()),";");
			String[] imgUrl = imgUrls.split(";");
			if (imgArr.length == 0) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			boolean b=true;
			for (int i = 0; i < imgUrl.length; i++) {
				for (int j = 0; j < imgArr.length; j++) {
					if(imgArr[j] != null){
						if (imgArr[j].equals(imgUrl[i])) {
							imgArr[j]=null;
							b=false;
						}
					}
				}
			}
			if (b) {
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
				return msg;
			}
			String tempUrl="";
			for (int i = 0; i < imgArr.length; i++) {
				if (null!=imgArr[i]) {
					tempUrl+=imgArr[i]+";";
				}
			}
			Map<String, Object> paramMap =new HashMap<String,Object>(2);
			paramMap.put("propagandaPhoto", tempUrl);
			paramMap.put("orgId", orgId);
			int res = orgMapper.updatePropagandaPhoto(paramMap);
			if (res>0) {
				for (String string : imgUrl) {
					string=configParam.getSystemPhotoUrl()+string.substring(string.lastIndexOf("/"));
					File file=new File(string);
					if (file.exists()) {
						file.delete();
						logger.info("  图片删除成功!   操作人:"+u.getId()+"_"+u.getUserName()) ;
					}
				}
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.DELETE_PHOTO_SUCCUESS);
			}else{
				logger.error("  图片删除失败!    操作人:"+u.getId()+"_"+u.getUserName()) ;
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NOT_FIND_PHOTO);
			}
		}
		String uploadPath=configParam.getSystemPhotoUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,uploadPath,configParam.getSystemPhotoRequestUrl());
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrls)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			SysOrg org = orgMapper.getOrgById(orgId);
			String propagandaPhoto = org.getPropagandaPhoto();
			Map<String, String>map=new HashMap<>(2);
			StringBuffer buffer=new StringBuffer();
			if(Util.isNotBlank(propagandaPhoto)){
				String[] arr = propagandaPhoto.split(";");
				int num = arr.length + path.size();
				if(num > 3){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
				buffer.append(propagandaPhoto);
			}else{
				if(path.size() > 3){
					msg.setCode(Header.STATUS_FAILED);
					msg.setMsg("只能上传3张图片，请核实后上传");
					return msg;
				}
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i)).append(";");
			}
			map.put("propagandaPhoto", buffer.toString());
			map.put("orgId", orgId);
			int res = orgMapper.updatePropagandaPhoto(map);
			if(res != 0){
				logger.info(StringUtil.appendStr(" 宣传图片上传成功! 操作者:",logUserName));
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 宣传图片上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}
	@Override
	public MessageBean getPropagandaPhoto(String str, Session session)throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(str);
		User u = session.getAttribute(map.get("tokenId")+"");
		String orgId = ((SysOrg)(u.getUserOrg())).getId()+"";
		List<String> resultList = new ArrayList<String>();
		SysOrg org = orgMapper.getOrgById(orgId);
		if(org != null){
			//获得组织的宣传图片
			String propagandaPhoto = org.getPropagandaPhoto();
			//如果组织的宣传图片为空的话就找拿父类
			if(Util.isNotBlank(propagandaPhoto)){
				String[] array = propagandaPhoto.split(";");
				resultList = Arrays.asList(array);
			}else{
				propagandaPhoto = findPropagandaPhoto(org.getFatherId());
				if(Util.isNotBlank(propagandaPhoto)){
					String[] array = propagandaPhoto.split(";");
					resultList = Arrays.asList(array);
				}
			}
		}
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public MessageBean updateOrgPhoto(HttpServletRequest request, Session session)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = new MessageBean();
		User u = session.getAttribute(request.getParameter("tokenId"));
		//当前登录人的id和名称
		String logUserName=StringUtil.appendStrNoBlank(u.getId(),"_",u.getUserName());
		String imgUrl = request.getParameter("imgUrl");
		String orgId = request.getParameter("orgId");
		if(Util.isNotBlank(imgUrl)){
			//将原先的图片设置为空
			int result = orgMapper.updateOrgPhotoNull(orgId);
			if(result == 0){
				logger.error(StringUtil.appendStr(" logo置空失败! 操作者:",logUserName));
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg(Msg.NULL_PHOTO_FAILED);
				return msg;
			}
			//数据库操作成功后删除文件中用户的头像图片
			imgUrl=configParam.getSystemPhotoUrl()+imgUrl.substring(imgUrl.lastIndexOf("/"));
			File destFile = new File(imgUrl);
			if (destFile.exists()) {
				logger.info(StringUtil.appendStr(" logo文件删除成功! 操作者:",logUserName));
				destFile.delete();
			}else{
				logger.error(StringUtil.appendStr(" logo文件删除失败! 操作者:",logUserName));
			}
		}
		//删除后重新上传用户更改后的图片
		String pathDir = configParam.getSystemPhotoUrl();
		String iconRequestUrl = configParam.getSystemPhotoRequestUrl();
		List<String> path=DownloadUtils.uploadPhoto(request,pathDir,iconRequestUrl);
		if (!Util.isNotBlank(path)) {
			if(Util.isNotBlank(imgUrl)){
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg("图片删除成功");
			}else{
				msg.setCode(Header.STATUS_SUCESS);
			}
		}else{
			StringBuffer buffer=new StringBuffer();
			SysOrg org = orgMapper.getOrgById(orgId);
			if(Util.isNotBlank(org.getOrgPhoto())){
				msg.setCode(Header.STATUS_FAILED);
				msg.setMsg("只能上传一张图片，请核实后上传");
				return msg;
			}
			for (int i = 0; i < path.size(); i++) {
				buffer.append(path.get(i));
			}
			Map<String, Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("orgId", orgId);
			parameterMap.put("orgPhoto", buffer.toString());
			int re1 = orgMapper.updateOrgPhoto(parameterMap);
			if(re1 != 0){
				logger.info(StringUtil.appendStr(" 组织图片上传成功! 操作者:",logUserName,"图片名称",buffer.toString()));
				msg.setCode(Header.STATUS_SUCESS);
				msg.setMsg(Msg.UPLOAD_SUCCUESS);
			}else{
				logger.error(StringUtil.appendStr(" 组织图片上传失败! 操作者:",logUserName));
				throw new ServiceException(Msg.UPLOAD_FAILED);
			}
		}
		return msg;
	}
}
