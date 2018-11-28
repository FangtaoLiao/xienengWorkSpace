package com.synpowertech.dataCollectionJar.initialization;

import java.rmi.server.RemoteServer;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.initialization
 * @ClassName: SecurityInterceptor
 * @Description: 用于rmi方法调用白名单放行
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月10日下午5:29:24   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class SecurityInterceptor implements MethodInterceptor {
    
	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	@SuppressWarnings("rawtypes")
	private Set allowedIps;
    

    @SuppressWarnings("rawtypes")
	public Set getAllowedIps() {
		return allowedIps;
	}

	@SuppressWarnings("rawtypes")
	public void setAllowedIps(Set allowedIps) {
		this.allowedIps = allowedIps;
		logger.info("RMI service inits succefully！");
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable{
        String clientHost = RemoteServer.getClientHost();
        if (allowedIps != null && allowedIps.contains(clientHost)) {
            return methodInvocation.proceed();
        } else {
			//logger.warn("unexcepted IP：{}",clientHost);
            throw new SecurityException("unexcepted IP:" + clientHost);
        }
    }

}