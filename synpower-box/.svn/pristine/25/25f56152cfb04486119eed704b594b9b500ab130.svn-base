package com.synpower.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class LocalConfigure extends PropertyPlaceholderConfigurer {
	private static final Logger log = LoggerFactory.getLogger(LocalConfigure.class);
	private static final Map<String, String> initParams = new HashMap<String, String>();
	private static Set<String> reWrite = new HashSet<String>();
	private static String address;

	static {
		try {
			address = InetAddress.getLocalHost().getHostAddress();
			initParams.put("serverIp", address);
		} catch (UnknownHostException e) {
			log.error("read localhost address error:", e);
		}
		String keys = "nodeIp,nodeRegisterCenter,nodeContext,ssoServerName,ssoClientName,rpcServer,jdbc.url,jmsUri";
		for (String k : keys.split(",")) {
			reWrite.add(k);
		}
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (reWrite.contains(propertyName)) {
			String newVal = propertyValue.replace(propertyName + "_localhost",	address);
			initParams.put(propertyName, newVal);
			return newVal;
		}
		initParams.put(propertyName, propertyValue);

		return propertyValue;
	}

	public static final String getValue(String key) {
		return initParams.get(key);
	}

	public static String getLocalAddress() {
		return address;
	}
}
