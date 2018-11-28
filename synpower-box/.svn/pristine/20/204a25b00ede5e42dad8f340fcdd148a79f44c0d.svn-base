package com.synpower.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.synpower.bean.CollDevice;
import com.synpower.bean.DeviceDetailCamera;
import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;
public class ApiUtil {
	public static Map<String, Object> getWeatherByPlantId(String location,String ak) throws ServiceException{
		StringBuffer strBuf;
		String weatherUrl = "";//获取配置文件的接口链接
		//根据电站id获取电站经纬度
		/*
		 * 
		 * 补充代码
		 * 
		 * 
		 */
		
		weatherUrl = "http://api.map.baidu.com/telematics/v3/weather?output=json&ak="+ak+"&location="
		 +location;	
		strBuf = new StringBuffer();		
		try{
			URL url = new URL(weatherUrl);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));//转码。
			String line = null;
			while ((line = reader.readLine()) != null)
			    strBuf.append(line + " ");
				reader.close();
		}catch(MalformedURLException e) {
			e.printStackTrace(); 
		}catch(IOException e){
			e.printStackTrace(); 
		}	
		return Util.parseURL(strBuf.toString());
	}
	/** 
	  * @Title:  getLocation 
	  * @Description:  根据地址获取经纬度 
	  * @param adrName
	  * @param ak
	  * @return
	  * @throws ServiceException: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午4:29:11
	*/ 
	/** 
	  * @Title:  getLocation 
	  * @Description:  TODO 
	  * @param adrName
	  * @param ak
	  * @return
	  * @throws ServiceException: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午5:01:11
	*/ 
	public static Map<String, Object> getLocation(String addrName,String ak) throws ServiceException{
		StringBuffer strBuf;
		String apiUrl = null;//获取配置文件的接口链接
		//根据电站id获取电站经纬度
		/*
		 * 
		 * 补充代码
		 * 
		 * 
		 */
		
		apiUrl = "http://api.map.baidu.com/geocoder/v2/?&output=json&address="+addrName+"&ak="+ak;
		//apiUrl=" http://api.map.baidu.com/geocoder?output=json&location=30.36748093795755,102.89915972360397&key=37492c0ee6f924cb5e934fa08c6b1676";
		strBuf = new StringBuffer();		
		try{
			URL url = new URL(apiUrl);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));//转码。
			String line = null;
			while ((line = reader.readLine()) != null)
			    strBuf.append(line + " ");
				reader.close();
		}catch(MalformedURLException e) {
			e.printStackTrace(); 
		}catch(IOException e){
			e.printStackTrace(); 
		}	
		return Util.parseURL(strBuf.toString());
	}
	 /** 
	  * @Title:  locationBDToTX 
	  * @Description:  TODO 
	  * @param lat 纬度
	  * @param lng 经度
	  * @return
	  * @throws ServiceException: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午5:01:40
	*/ 
	public static Map<String, Object> locationBDToTX(String lat, String lng,String ak) throws ServiceException{  
			StringBuffer strBuf;
			String apiUrl = null;
			apiUrl="http://apis.map.qq.com/ws/coord/v1/translate?locations="+lat+","+lng+"&type=3&key="+ak;
			strBuf = new StringBuffer();		
			try{
				URL url = new URL(apiUrl);
				URLConnection conn = url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));//转码。
				String line = null;
				while ((line = reader.readLine()) != null)
				    strBuf.append(line + " ");
					reader.close();
			}catch(MalformedURLException e) {
				e.printStackTrace(); 
			}catch(IOException e){
				e.printStackTrace(); 
			}	
			return Util.parseURL(strBuf.toString());
	 }
	
	
	
	
	
	public static void main(String[] args) throws ServiceException {
		//System.out.println(locationBDToTX("39.92103485311","116.3748578526","45IBZ-YM5KQ-SLG56-GVOSX-PDUSF-TUF4M"));
		//lng=104.03876626306119, lat=30.592620398051746
		//System.out.println(getLocation("中国", "t5uryEXGfrHPNNGbgam7eEl2"));
		List<CollDevice> camera = new ArrayList<>();
		CollDevice c1 = new CollDevice();
		c1.setDeviceSn("ser1");
		c1.setChannelNum(1);
		CollDevice c2 = new CollDevice();
		c2.setDeviceSn("ser2");
		c2.setChannelNum(2);
		camera.add(c2);
		camera.add(c1);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < camera.size(); i++) {
			if (i!=camera.size()-1) {
				sb.append(camera.get(i).getDeviceSn()).append(":"+camera.get(i).getChannelNum()+",");
			} else {
				sb.append(camera.get(i).getDeviceSn()).append(":"+camera.get(i).getChannelNum());

			}
		}
		System.out.println(sb.toString());
	}
}
