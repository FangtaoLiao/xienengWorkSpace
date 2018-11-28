package com.synpower.weixin;

import java.io.IOException;
import java.util.Map;

import com.synpower.lang.ServiceException;
import com.synpower.util.UrlUtil;
import com.synpower.util.Util;

import net.sf.json.JSONObject;

/**
 * Created by SP0013 on 2017/12/7.
 */
public class WeixinOper {
    public static Map<String,String> visit(String apiUrl,Map<String,String> paraMap) throws IOException, ServiceException {
        String json = UrlUtil.sendPost(apiUrl,paraMap);
        Map<String,String> weixinRs = Util.stringToMap(json);
        return weixinRs;
    }
    public static Map<String,Object> visitByJson(String apiUrl,JSONObject json1) throws IOException, ServiceException {
        String json = UrlUtil.sendPostJson(apiUrl,json1);
        Map<String,Object> weixinRs = Util.stringToMap1(json);
        return weixinRs;
    }
}
