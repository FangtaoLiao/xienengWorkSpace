package com.synpower.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface WXmonitorService {
	public MessageBean genCurve(String str,Session session) throws SessionException, ServiceException, SessionTimeoutException, ParseException;
	public MessageBean getStaticInfo(String str,Session session,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException, IOException;
	public MessageBean getDynamic(String str,Session session,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException;
	public MessageBean getPlantMap(String str,Session session) throws SessionException, ServiceException, SessionTimeoutException;
	public MessageBean getMapPlant(String str,Session session) throws SessionException, ServiceException, SessionTimeoutException;
	public MessageBean powerCurve(String str, Session session) throws SessionException, ServiceException, SessionTimeoutException;
	public MessageBean getUserInfo(String str, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	public MessageBean getList(String str, Session session) throws SessionException, ServiceException, SessionTimeoutException;
	public MessageBean getWXPlantList(String str, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	public MessageBean searchPlantByKeyword(String str, Session session) throws SessionException, ServiceException, SessionTimeoutException;
	public Map<String, Object> getUnit(Double value,int compare,int hex,String hexUnit,String unit);
	public  Map<String,Object> getPVStaticInfo(String str, Session session,HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException, IOException;
}
