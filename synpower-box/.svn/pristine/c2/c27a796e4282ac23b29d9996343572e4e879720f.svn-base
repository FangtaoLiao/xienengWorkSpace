package com.synpower.mobile;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SmsException;
import com.synpower.mobile.msg.MobileMsg;
import com.synpower.mobile.msg.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Service
public class MobileUtil {
    private static ResponseCode responseCode;
    static {
        responseCode = new ResponseCode();
        responseCode.put(100,"发送成功");
        responseCode.put(-1,"账号未注册");
        responseCode.put(-2,"其他错误");
        responseCode.put(-3,"帐号或密码错误");
        responseCode.put(-5,"余额不足，请充值");
        responseCode.put(-7,"提交信息末尾未加签名，请添加中文的企业签名【 】");
        responseCode.put(-6,"定时发送时间不是有效的时间格式");
        responseCode.put(-8,"发送内容需在1到300字之间");
        responseCode.put(-9,"发送号码为空");
        responseCode.put(-9,"发送号码为空");
        responseCode.put(-10,"定时时间不能小于系统当前时间");
        responseCode.put(-10,"定时时间不能小于系统当前时间");
        responseCode.put(-11,"屏蔽手机号码");
        responseCode.put(-100,"IP黑名单");
        responseCode.put(-102,"账号黑名单");
        responseCode.put(-103,"IP未导白");
    }

    /**
     * 短信用户名
     */
    @Value("#{propertyConfigurer4Config['corpId']}")
    private String corpId;
    /**
     * 短信密码
     */
    @Value("#{propertyConfigurer4Config['pwd']}")
	private String pwd;
    /**
     * 短信访问地址
     */
    @Value("#{propertyConfigurer4Config['urlLink']}")
	private	String urlLink;

    public static ResponseCode getResponseCode() {
        return responseCode;
    }

    public static void setResponseCode(ResponseCode responseCode) {
        MobileUtil.responseCode = responseCode;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public MobileUtil(){
	}

    /**
     * 发送短信
     * @param mobileMsg
     * @return 信息
     * @throws SmsException
     * @throws IOException
     * @throws ServiceException
     */
    public int sendMsg(MobileMsg mobileMsg) throws SmsException,ServiceException, IOException {
    	Map<Integer, String>resMap=new HashMap<>(1);
        SmsOper smsOper = new SmsOper();
        int code = smsOper.sendSMS(getCorpId(),getPwd(),getUrlLink(),mobileMsg);
        if(code < 0) {
            throw new SmsException(responseCode.get(code));
        }
        return code;
    }
}
