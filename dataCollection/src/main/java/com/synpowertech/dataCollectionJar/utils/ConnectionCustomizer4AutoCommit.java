package com.synpowertech.dataCollectionJar.utils;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: ConnectionCustomizer4AutoCommit
 * @Description: 复写AbstractConnectionCustomizer的onCheckOut方法，实现c3p0不自动提交
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年12月5日上午11:09:23 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class ConnectionCustomizer4AutoCommit extends AbstractConnectionCustomizer {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionCustomizer4AutoCommit.class);
	
	public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {
		super.onCheckOut(c, parentDataSourceIdentityToken);
		// 设置不自动提交
		c.setAutoCommit(false);
		logger.debug("success to set autoCommit false！");
	}

}