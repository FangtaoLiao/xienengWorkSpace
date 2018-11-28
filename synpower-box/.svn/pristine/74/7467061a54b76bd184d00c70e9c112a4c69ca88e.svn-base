package com.synpower.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.synpower.msg.session.User;
import com.synpower.serviceImpl.DeviceServiceImpl;

/*****************************************************************************
 * @Package: com.synpower.config
 * ClassName: WebSocketInterceptor
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月22日上午11:04:32   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public class WebSocketInterceptor implements HandshakeInterceptor {
	private Logger logger = Logger.getLogger(WebSocketInterceptor.class);
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest  = (ServletServerHttpRequest) serverHttpRequest;
			HttpServletRequest httpRequest = servletRequest.getServletRequest();
			String sn = httpRequest.getParameter("sn");
			map.put("sn",sn);
			map.put("connTime", System.currentTimeMillis());
		}
		return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}

