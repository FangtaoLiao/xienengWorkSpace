package com.synpower.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.synpower.lang.ServiceException;
import com.synpower.weixin.WeixinOper;

import net.sf.json.JSONObject;

public class WxPushUtil {
	public static Map<String,String> getAccessToken(Map<String,String> paraMap){
		Map<String,String> weixinRs = null;
//		Map<String,String> paraMap = new HashMap<>();
//		paraMap.put("appid","wx207e73ae2c9d9ab3");
//		paraMap.put("secret","bb75f98ca7281a8bea441fcafd90cca3");
//		paraMap.put("grant_type","client_credential");
		String url = "https://api.weixin.qq.com/cgi-bin/token?";
		try {
			weixinRs = WeixinOper.visit(url,paraMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weixinRs;
	}
	public static Map<String, Object> let(String accessToken){
		Map<String,Object> weixinRs = null;
		Map<String,Object> paraMap = new HashMap<>();
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();
		WxTemplate template = new WxTemplate();
		template.setTouser("obeMj0c1YSIR9-k9hFVHopMcKNcI");
		template.setTemplate_id("QdKA2NYYBmnXwz_dL63iFLhL6o9Nr9sRayzpMWFNXPM");
		template.setForm_id("1523863562286");
		TemplateData first = new TemplateData();
		first.setColor("#173177");
		first.setValue("四川省成都市环球中心");
		m.put("keyword1", first);
		TemplateData name = new TemplateData();
		name.setColor("#173177");
		name.setValue("2018年04月01日 10:30");
		m.put("keyword2", name);
		TemplateData remark = new TemplateData();
		remark.setColor("#173177");
		remark.setValue("分布式光伏管理系统");
		m.put("keyword3", remark);
		template.setData(m);
		JSONObject json = JSONObject.fromObject((template));
		System.out.println(json);
		paraMap.put("value", template);
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+accessToken;
		try {
			weixinRs = visit1(url,json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weixinRs;
	}
	public static Map<String,Object> visit1(String apiUrl,JSONObject json1) throws IOException, ServiceException {
        String json = UrlUtil.sendPostJson(apiUrl,json1);
        Map<String,Object> weixinRs = Util.stringToMap1(json);
        return weixinRs;
    }
	public static void main(String[] args) throws IOException {
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("appid","wx207e73ae2c9d9ab3");
		paraMap.put("secret","bb75f98ca7281a8bea441fcafd90cca3");
		paraMap.put("grant_type","client_credential");
		Map<String, String> result = getAccessToken(paraMap);
		String access = result.get("access_token");
		System.out.println(access);
		Map<String, Object> m = let(access);
		System.out.println(m.get("errcode"));
//		int a = Integer.valueOf(m.get("errcode"));
		int a = (int) m.get("errcode");
		String b = (String)m.get("errmsg");
		System.out.println(a+"   "+b);
	}
}
