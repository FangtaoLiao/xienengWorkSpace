package com.synpower.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface MonitorService {
	
	public MessageBean getBasicInfo(String str,Session session)throws SessionException, ServiceException, SessionTimeoutException;

	public MessageBean getPriceBarByTime(String str,Session session)throws SessionException,ServiceException,SessionTimeoutException, ParseException;

	public MessageBean getCurveSearchDate(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;
	
	public MessageBean getProSearchDate(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;
	
	public MessageBean getProSearch(String str) throws SessionException,ServiceException,SessionTimeoutException, ParseException;

	public MessageBean getSinglePower(String str) throws SessionException,ServiceException,SessionTimeoutException;
	
	public MessageBean getPlantStatus(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;
	
	public MessageBean getTodayInfo(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;

	public MessageBean getSingelDevicess(String str,Session session,HttpServletRequest request)throws SessionException,ServiceException,SessionTimeoutException;

	public MessageBean getProSearchWX(String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public MessageBean getPriceBarByTimeWx(String str, Session session)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public Map<String, Double> getPowerPriceTotal(List<String> list);
	
	public  String getNowdayIncomeOfDevice(String deviceId);

	MessageBean getProSearch2(String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException;
	public MessageBean getTodayChargeEle(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;
	public MessageBean getTotalPhotovoltaic(String str,Session session) throws SessionException,ServiceException,SessionTimeoutException;
	public MessageBean getElectricityProfit(String str,Session session)throws SessionException,ServiceException,SessionTimeoutException, ParseException;
}
