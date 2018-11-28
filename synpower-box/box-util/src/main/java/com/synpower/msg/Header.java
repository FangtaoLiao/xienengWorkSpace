package com.synpower.msg;

/**
 * 响应信息必要常量
 * @author PC043
 *
 */
public class Header {
	public final static String STATUS_SUCESS = "100";//成功
	public final static String STATUS_FAILED = "101";//失败
	public final static String STATUS_FATAL_ERROR = "102";//致命错误
	public final static String STATUS_NULL_ERROR = "103";//空异常
	public final static String STATUS_LOGIN_TIMEOUT_ERROR="104";//登陆超时
	public final static String STATUS_LOGIN_NO_REGITSER="105";//用户没有注册
	public final static String STATUS_WEIXIN_ERROR="106";//微信异常
	public final static String STATUS_SCAN_CODE="107";//已扫码


	
}
