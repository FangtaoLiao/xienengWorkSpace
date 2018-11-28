package com.synpower.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysPoorDetail;
import com.synpower.bean.SysPoorEvent;
import com.synpower.bean.SysPoorEventDetails;
import com.synpower.bean.SysPoorInfo;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysOrgMapper;
import com.synpower.dao.SysPoorDetailMapper;
import com.synpower.dao.SysPoorEventDetailsMapper;
import com.synpower.dao.SysPoorEventMapper;
import com.synpower.dao.SysPoorInfoMapper;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;
import com.synpower.service.PovertyService;
import com.synpower.util.ServiceUtil;
import com.synpower.util.Util;

@Service
public class PovertyServiceImpl implements PovertyService{
	private Logger logger = Logger.getLogger(AlarmServiceImpl.class);
	@Autowired
	private SysPoorInfoMapper poorInfoMapper;
	@Autowired
	private SysOrgMapper orgMapper;
	@Autowired
	private SysPoorDetailMapper poorDetailMapper;
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired
	private SysPoorEventMapper poorEventMapper;
	@Autowired
	private SysPoorEventDetailsMapper poorEventDetailsMapper;
	@Override
	public MessageBean getIncreaseIncome(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<>(3); 
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId")+"");
		//得到所有组织列表
		List<SysOrg>orgList=orgMapper.getAllOrg();
		SysOrg org=((SysOrg)u.getUserOrg());
		List<String>reList=new ArrayList<>();
		//查出当前登录人的组织id有哪些
		reList.add(((SysOrg)u.getUserOrg()).getId()+"");
		reList = ServiceUtil.getTree(orgList, org.getId()+"",reList);
		List<SysPoorInfo> poorInfoList = poorInfoMapper.getPoorInfo(reList);
		double IncreaseIncomeNum = 0;
		double InvestmentAmount = 0;
		if(Util.isNotBlank(poorInfoList)){
			for (int i = 0,size = poorInfoList.size(); i < size; i++) {
				SysPoorInfo poorInfo = poorInfoList.get(i);
				IncreaseIncomeNum = Util.addFloat(IncreaseIncomeNum, poorInfo.getIncreaseIncome(), 0);
				InvestmentAmount = Util.addFloat(InvestmentAmount, poorInfo.getInvest(), 0);
			}
		}
		if(IncreaseIncomeNum > 10000){
			IncreaseIncomeNum = Util.divideDouble(IncreaseIncomeNum, 10000, 1);
			resultMap.put("IncreaseIncomeNum", IncreaseIncomeNum);
			resultMap.put("IncreaseIncomeUnit", "万元");
		}else{
			resultMap.put("IncreaseIncomeNum", IncreaseIncomeNum);
			resultMap.put("IncreaseIncomeUnit", "元");
		}
		if(InvestmentAmount > 10000){
			InvestmentAmount = Util.divideDouble(InvestmentAmount, 10000, 1);
			resultMap.put("InvestmentAmount", InvestmentAmount+"万元");
		}else{
			resultMap.put("InvestmentAmount", InvestmentAmount+"元");
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
	@Override
	public MessageBean getPoorHouseholdsInfo(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> resultMap = new HashMap<>(3); 
		Map<String, Object> map = Util.parseURL(jsonData);
		User u = session.getAttribute(map.get("tokenId")+"");
		//得到所有组织列表
		List<SysOrg>orgList=orgMapper.getAllOrg();
		SysOrg org=((SysOrg)u.getUserOrg());
		List<String>reList=new ArrayList<>();
		//查出当前登录人的组织id有哪些
		reList.add(((SysOrg)u.getUserOrg()).getId()+"");
		reList = ServiceUtil.getTree(orgList, org.getId()+"",reList);
		List<SysPoorInfo> poorInfoList = poorInfoMapper.getPoorInfo(reList);
		double poorTotal = 0;
		double completeNum = 0;
		if(Util.isNotBlank(poorInfoList)){
			for (int i = 0,size = poorInfoList.size(); i < size; i++) {
				SysPoorInfo poorInfo = poorInfoList.get(i);
				poorTotal = Util.addFloat(poorTotal, poorInfo.getTotalNum()==null?0:poorInfo.getTotalNum(), 0);
				completeNum = Util.addFloat(completeNum, poorInfo.getCoverNum()==null?0:poorInfo.getCoverNum(), 0);
			}
		}
		resultMap.put("poorTotal", poorTotal);
		resultMap.put("completeNum", completeNum);
		List resultList = new ArrayList<>();
		List<SysPoorDetail> detailList = poorDetailMapper.getAllPoorDetail(reList);
		if(Util.isNotBlank(detailList)){
			for (int i = 0,size = detailList.size(); i < size; i++) {
				SysPoorDetail detail = detailList.get(i);
				Map<String, Object> tempMap = new HashMap<String, Object>(4);
				tempMap.put("income", detail.getIncreaseIncome());
				tempMap.put("name", detail.getName());
				tempMap.put("number", detail.getPopulation()==null?0:detail.getPopulation());
				PlantInfo plant = plantMapper.getPlantById(detail.getPlantId()+"");
				tempMap.put("plantName", plant.getPlantName());
				resultList.add(tempMap);
			}
		}
		resultMap.put("detailInfo", resultList);
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
	@Override
	public MessageBean getNewsList(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		List resultList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
		List<SysPoorEvent> eventList = poorEventMapper.getEvent();
		if(Util.isNotBlank(eventList)){
			for (int i = 0,size = eventList.size(); i < size; i++) {
				SysPoorEvent event = eventList.get(i);
				Map<String, Object> map = new HashMap<>(5);
				map.put("id", event.getId());
				map.put("abstract", event.getIntroduction());
				map.put("img", event.getPhotos());
				map.put("title", event.getTitle());
				map.put("time", sdf.format(event.getTime()));
				resultList.add(map);
			}
		}
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultList);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
	@Override
	public MessageBean getNewsDetail(String jsonData, Session session)
			throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg = new MessageBean();
		Map<String, Object> map = Util.parseURL(jsonData);
		Integer id = Integer.valueOf(String.valueOf(map.get("id")));
		SysPoorEventDetails sysPoorEventDetails = poorEventDetailsMapper.selectByPrimaryKey(id);
		SysPoorEvent sysPoorEvent = poorEventMapper.selectByPrimaryKey(id);
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("datail", sysPoorEventDetails.getContent());
		resultMap.put("title", sysPoorEvent.getTitle());
		msg.setCode(Header.STATUS_SUCESS);
		msg.setBody(resultMap);
		msg.setMsg(Msg.SELECT_SUCCUESS);
		return msg;
	}
}
