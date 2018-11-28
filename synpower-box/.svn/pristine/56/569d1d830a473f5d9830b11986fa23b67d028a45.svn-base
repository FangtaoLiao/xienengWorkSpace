package com.synpower.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WeatherUtil {
	/**
	 * 和风天气签名生成算法-JAVA版本
	 * 
	 * @param HashMap<String,String>
	 *            params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String
	 *            secret 签名密钥（用户的认证key）
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String, String> params, String secret) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<>(params);
		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起

		StringBuilder baseString = new StringBuilder();
		for (Map.Entry<String, String> param : entrys) {
			// sign参数 和 空值参数 不加入算法
			if (param.getValue() != null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim())
					&& !"".equals(param.getValue().trim())) {
				baseString.append(param.getKey().trim()).append("=").append(param.getValue().trim()).append("&");
			}
		}
		if (baseString.length() > 0) {
			baseString.deleteCharAt(baseString.length() - 1).append(secret);
		}

		return MD5.getMD5Code(baseString.toString());
	}
	public static String getWeather() throws IOException{
		HashMap<String, String> params = new HashMap<String,String>();
		params.put("location", "chengdu");
		params.put("key", "a0175f614a434b738616927ad45b688c");
		String secret = "a0175f614a434b738616927ad45b688c";
		String key = getSignature(params, secret);
		//参数字符串，如果拼接在请求链接之后，需要对中文进行 URLEncode   字符集 UTF-8
        String param = "key=a0175f614a434b738616927ad45b688c&location=chengdu";
        StringBuilder sb = new StringBuilder();
        InputStream    is=null;
        BufferedReader br=null;
        try {
            //接口地址
            String            url        = "https://free-api.heweather.com/s6/weather";
            URL               uri        = new URL(url);
            HttpURLConnection connection= (HttpURLConnection) uri.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("accept", "*/*");
            //发送参数
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(param);
            out.flush();
            //接收结果
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String         line;
            //缓冲逐行读取
            while ( (line = br.readLine()) != null ) {
                sb.append(line);
            }
//            System.out.println(sb.toString());
            return sb.toString();
        }catch ( Exception ignored ){
        	
        }
        finally {
            //关闭流
            try {
                if(is!=null){
                    is.close();
                }
                if(br!=null){
                    br.close();
                }
//                if (out!=null){
//                    out.close();
//                }
            } catch (IOException e2) {
            	
            }
        }
		return param;
	}
	public static void main(String[] args) throws IOException {
		System.out.println(getWeather());
	}
}
