package com.synpower.service;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface PovertyService {
	public MessageBean getIncreaseIncome(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean getPoorHouseholdsInfo(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean getNewsList(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean getNewsDetail(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
}
