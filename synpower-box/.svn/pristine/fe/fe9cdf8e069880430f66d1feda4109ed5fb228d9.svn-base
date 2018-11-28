package com.synpower.service;

import java.util.Map;

import com.synpower.lang.EnergyMonitorException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.User;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author ybj
 * @date 2018年8月21日
 * @Description -_-能效-实时监测
 */
public interface EnergyMonitorService {
	/**
	 * 月风谷电量统计
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SessionTimeoutException
	 * @throws SessionException
	 */
	public List<Map<String, Object>> getElecKPI(User u, long curr, long next) throws SessionException, SessionTimeoutException, ServiceException;

	/**
	 * 电站用电统计
	 *
	 * @return
     * @param user
     */
	public HashMap<String, Object> getUseElec(User user) throws SessionTimeoutException, SessionException, EnergyMonitorException;
	/**
	 * @Author lz
	 * @Description 单个电表用电统计
	 * @Date 12:42 2018/8/22
	 * @Param [elecId]
	 * @return com.synpower.msg.MessageBean
	 *@param elecId */
	public HashMap<String, Double> getUseElecByElecId(Integer elecId,Integer plantId) throws EnergyMonitorException;

	/**
	 * 用电负荷趋势
	 * 
	 * @return
	 * @throws SessionTimeoutException 
	 * @throws SessionException 
	 * @throws ServiceException 
	 */
	public Map<String, Object> getElecLoadKpi(User u,long startTime,long endTime) throws SessionException, SessionTimeoutException, ServiceException;

	/**
	 * 用电量趋势
	 * 
	 * @return
     * @param user
     * @param parMap
     */
	public HashMap<String, Object> getElecUseKpi(User user, Map<String, Object> parMap) throws SessionTimeoutException, SessionException, EnergyMonitorException;

	/**
	 * 电能质量
	 * 
	 * @return
	 * @throws SessionTimeoutException 
	 * @throws SessionException 
	 */
	public Map<String, Object> getElecQualityKPI(User u) throws SessionException, SessionTimeoutException;

	/**
	 * 获取企业基本信息
	 * 
	 * @return
	 * @throws SessionTimeoutException 
	 * @throws SessionException 
	 */
	public Map<String, Object> getEnterpriseInfo(User u) throws SessionException, SessionTimeoutException;
}
