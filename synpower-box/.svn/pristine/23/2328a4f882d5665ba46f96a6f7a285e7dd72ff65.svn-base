package com.synpower.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.synpower.lang.ServiceException;
import com.synpower.msg.Msg;

public class PropertiesUtil {
	public static String getValue(String url,String []params,String name) throws IOException {
		Properties prop = new Properties();
	    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(url);
	    
	    prop.load(in);
	    //从配置文件中读取模板字符串替换
	    String msg=MessageFormat.format(prop.getProperty(name),params);
	    return msg;
	}
	/** 
	  * @Title:  getSMSTemplate 
	  * @Description:  获取短信模板 
	  * @param name
	  * @param params
	  * @return: String
	 * @throws ServiceException 
	 * @throws IOException 
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月6日下午2:47:57
	*/ 
	public static String getSMSTemplate(String name,String[] params) throws ServiceException  {  
		Properties pop=new Properties();
		InputStream in=PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			pop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ServiceException(Msg.TYPE_UNKNOWN_ERROR);
		}
	    //从配置文件中读取模板字符串替换
	    return MessageFormat.format(pop.getProperty(name),params);
	}
	public static String getValue(String url,String param) {
		Properties prop = new Properties();
	    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(url);
	    
	    try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return prop.getProperty(param);
	}
}
