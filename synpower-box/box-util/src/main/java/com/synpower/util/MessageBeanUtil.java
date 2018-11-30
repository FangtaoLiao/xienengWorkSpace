package com.synpower.util;

import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;

public class MessageBeanUtil {

	/**
	 * 获取一个成功的messgebean
	 * 
	 * @param msg
	 * @return
	 */
	public static MessageBean getOkMB() {
		MessageBean eMessageBean = new MessageBean();
		eMessageBean.setBody(null);
		eMessageBean.setCode(Header.STATUS_SUCESS);
		eMessageBean.setMsg(null);
		return eMessageBean;
	}

	public static MessageBean getOkMB(String msg) {
		MessageBean okMB = getOkMB();
		okMB.setMsg(msg);
		return okMB;
	}

	public static MessageBean getOkMB(Object body) {
		MessageBean okMB = getOkMB();
		okMB.setBody(body);
		return okMB;
	}

	public static MessageBean getOkMB(String msg, Object body) {
		MessageBean okMB = getOkMB(msg);
		okMB.setBody(body);
		return okMB;
	}

	/**
	 * 获取一个默认失败的messgebean
	 * 
	 * @param msg
	 * @return
	 */
	public static MessageBean getErrorMB(String msg) {
		MessageBean eMessageBean = new MessageBean();
		eMessageBean.setBody(null);
		eMessageBean.setCode(Header.STATUS_FAILED);
		eMessageBean.setMsg(msg);
		return eMessageBean;
	}

	/**
	 * 获取一个自定义的messagebean
	 * @param code
	 * @param msg
	 * @return
	 */
	public static MessageBean getMB(Object body, String code, String msg) {
		MessageBean eMessageBean = new MessageBean();
		eMessageBean.setBody(body);
		eMessageBean.setCode(code);
		eMessageBean.setMsg(msg);
		return eMessageBean;
	}

}
