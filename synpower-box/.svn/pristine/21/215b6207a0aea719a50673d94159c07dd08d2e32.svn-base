package com.synpower.quartz;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.synpower.bean.CollDevice;
import com.synpower.bean.DeviceDetailCamera;
import com.synpower.dao.PlantInfoMapper;
import com.synpower.dao.SysWeatherMapper;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
import com.synpower.util.ApiUtil;
import com.synpower.util.PropertiesUtil;
import com.synpower.util.SystemCache;

/*****************************************************************************
 * @Package: com.synpower.quartz
 * ClassName: UpdateWeather
 * @Description: 更新萤石云token
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年1月22日下午5:29:35   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
//@Component
public class UpdateYSToken {
	private Logger logger = Logger.getLogger(UpdateYSToken.class);
	@Autowired
	private PlantInfoMapper plantMapper;
	@Autowired 
	private ConfigParam configParam;
	@Autowired
	private SysWeatherMapper weatherMapper;
	
	/**
	 * 
	  * @Title:  getYS7Token 
	  * @Description:  获取萤石云token 
	  * @return
	  * @throws ServiceException: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月24日下午4:55:37
	 */
	public  void updateYS7Token() throws ServiceException {
		//萤石key、secret
		String key = PropertiesUtil.getValue("config.properties", "YS_APP_KEY");
		String secret = PropertiesUtil.getValue("config.properties", "YS_APP_SECRET");
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/token/get");
		// 创建参数队列
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("Host", "open.ys7.com"));
		formparams.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		formparams.add(new BasicNameValuePair("appKey", key));
		formparams.add(new BasicNameValuePair("appSecret", secret));
		UrlEncodedFormEntity uefEntity;
		//存储token实体类
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resultStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonData = (JSONObject) JSONObject.parse(resultStr);
					Map<String,String> data = (Map<String, String>) jsonData.get("data");
					SystemCache.accessToken=new String(data.get("accessToken"));
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	  * @Title:  cameraStatusReel 
	  * @Description:  查询摄像头列表
	  * @return
	  * @throws ServiceException: List<CameraInfo>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月24日下午6:22:18
	 */
	@SuppressWarnings("unchecked")
	public static List<CollDevice> cameraListInfo() throws ServiceException {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost("https://open.ys7.com/api/lapp/camera/list");
		// 创建参数队列
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("Host", "open.ys7.com"));
		formparams.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		String token=new String(SystemCache.accessToken);
		formparams.add(new BasicNameValuePair("accessToken", token));
		formparams.add(new BasicNameValuePair("pageStart", "0"));
		formparams.add(new BasicNameValuePair("pageSize", "10"));
		UrlEncodedFormEntity uefEntity;
		//存储token实体类
		List<CollDevice> list = new ArrayList<>();
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String resultStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonData = (JSONObject) JSONObject.parse(resultStr);
					List<Map<String,String>> data = (List<Map<String, String>>) jsonData.get("data");
					//System.out.println(jsonData);
					for (Map<String, String> map : data) {
						CollDevice ci = new CollDevice();
						ci.setDeviceName(map.get("deviceName"));
						ci.setDeviceStatus(String.valueOf(map.get("status")));
						ci.setDeviceSn(String.valueOf(map.get("deviceSerial")));
						ci.setChannelNum(1);
						list.add(ci);
					}
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
}
	
