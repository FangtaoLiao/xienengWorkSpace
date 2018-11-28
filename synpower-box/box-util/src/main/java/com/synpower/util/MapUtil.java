package com.synpower.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.chainsaw.Main;
import org.springframework.beans.factory.annotation.Autowired;

import com.synpower.lang.ConfigParam;
import com.synpower.lang.ServiceException;

public class MapUtil {
	@Autowired
	private static ConfigParam  configParam;
	/** 
	  * @Title:  getLatitude 
	  * @Description:  支持多个路线查找(驾车方案)
	  * 			        坐标格式为:纬度,经度|纬度,经度
	  * 			   origins,destinations:  以"|"分割，最多传50个点，且起终点乘积不超过50
	  * @param origins
	  * @param destinations
	  * @return
	  * @throws ServiceException: Map<String,String>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月14日下午2:20:05
	*/ 
	public static Map<String, String> getLatitude(String origins,String destinations) throws ServiceException {
		Map<String, String> map=null;
		try {
			// 将地址转换成utf-8的16进制
			//address = URLEncoder.encode(address, "UTF-8");
			// 如果有代理，要设置代理，没代理可注释
			// System.setProperty("http.proxyHost","192.168.172.23");
			// System.setProperty("http.proxyPort","3209");
		//	URL resjson = new URL("https://api.map.baidu.com/direction/v2/transit?origin=30.574827,104.06932&destination=30.66342,104.072329&ak=" + BAIDU_APP_KEY);
			URL resjson = new URL("https://api.map.baidu.com/routematrix/v2/driving?"
										+ "origins="+origins
										+"&destinations="+destinations
										+ "&ak=KcRMTPDl7odbTEtZOTAiLGTNT0dwlTHy");
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			map=Util.stringToMap(str);
			if ("成功".equals(String.valueOf(map.get("message")))) {
				return map;
			}else{
				throw new ServiceException("");
			}
		} catch (Exception e) {
			throw new ServiceException("");
		}
	}
    /** 
      * @Title:  locationBDToTX 
      * @Description:  百度转腾讯坐标 
      * @param lat
      * @param lon
      * @return: String
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月15日下午4:31:02
    */ 
    public static String locationBDToTX(double lat, double lon){  
        double tx_lat;  
        double tx_lon;  
        double x_pi=3.14159265358979324;  
        double x = lon - 0.0065, y = lat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);  
        tx_lon = z * Math.cos(theta);  
        tx_lat = z * Math.sin(theta);  
        return tx_lat+","+tx_lon;  
    }
    /** 
      * @Title:  locationTXToBD 
      * @Description:  腾讯坐标转百度地图 
      * @param lat
      * @param lon
      * @return: String
      * @lastEditor:  SP0011
      * @lastEdit:  2018年1月15日下午4:31:32
    */ 
    public static String locationTXToBD(double lat, double lon){  
        double bd_lat;  
        double bd_lon;  
        double x_pi=3.14159265358979324;  
        double x = lon, y = lat;  
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);  
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);  
        bd_lon = z * Math.cos(theta) + 0.0065;  
        bd_lat = z * Math.sin(theta) + 0.006;  

        System.out.println("bd_lat:"+bd_lat);  
        System.out.println("bd_lon:"+bd_lon);  
        return bd_lon+","+bd_lat;  
    }  
	public static void main(String[] args) throws ServiceException {
/*		String start="30.574827,104.06932";
		String end="30.66342,104.072329";
		System.out.println(getLatitude(start, end));*/
		System.out.println(locationBDToTX(30.36748093795755, 102.89915972360397));
	}
	
}
