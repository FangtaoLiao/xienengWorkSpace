package com.synpower.handler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synpower.msg.Msg;
import com.synpower.util.StringUtil;

/*****************************************************************************
 * @Package: com.synpower.handler ClassName: RequestHander
 * @Description: request 重写
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年2月26日下午3:10:36 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Component
public class RequestHander extends ErrorHandler implements Filter {

	private static Logger logger = Logger.getLogger(RequestHander.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		/** 获取请求的URL */
		String requestURL = req.getRequestURI();
		/** 解析请求URL的最后一部分 */
		String tempURL = requestURL.substring(requestURL.lastIndexOf("/") + 1);
		char[] params = StringUtils.split(tempURL, ".")[0].toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (int i = params.length - 1; i >= 0; i--) {
			buffer.append(params[i]);
		}
		/** 拼接跳转的URL地址 */
		String forwardURL = requestURL.substring(0, requestURL.lastIndexOf("/") + 1) + buffer.toString() + ".do";
		/** 截掉项目名称 跳转时会自动拼接项目名字 */
		String perjectName = req.getContextPath();
		if (forwardURL.contains(req.getContextPath())) {
			forwardURL = forwardURL.substring(forwardURL.indexOf(perjectName) + perjectName.length());
		}
		try {
			request.getRequestDispatcher(forwardURL).forward(req, rep);
			logger.info(StringUtil.appendStr(requestURL, "重定向到", forwardURL));
		} catch (ServletException e) {
			logger.error(StringUtil.appendStr(requestURL, Msg.NOT_MAPPING_REQUEST));
		}
	}

	@Override
	public void destroy() {

	}

}
