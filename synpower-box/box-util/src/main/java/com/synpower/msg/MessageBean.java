package com.synpower.msg;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 
 * @author PC043
 *
 */
public class MessageBean {

	/**
	 * 响应代码，100为正常，102为业务不允许或者业务逻辑出错，103为未知异常
	 */
	private String code;
	private Object body;
	/**
	 * 后台响应具体信息
	 */
	private String msg;

	public MessageBean() {
		super();
	}

	public MessageBean(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public MessageBean(String code, Object body, String msg) {
		super();
		this.code = code;
		this.body = body;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "MessageBean [code=" + code + ", body=" + body + ", msg=" + msg + "]";
	}

}
