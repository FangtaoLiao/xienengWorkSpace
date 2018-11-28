package com.synpower.mobile.msg;

import com.synpower.lang.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 手机号码集
 * Created by SP0013 on 2017/11/2.
 */
public class TelSet extends HashSet<String>{
    /**
     * 加了一个手机号的验证
     * @param e
     * @return
     */
    public boolean addElements(String e) throws ServiceException  {
        if(StringUtils.isBlank(e)) {
            throw new ServiceException("手机号不能为空");
        }
        String rex = "^1[3|4|5|6|7|8|9][0-9]{9}$";
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(e);
        boolean flag = matcher.matches();
        if(!flag) {
            throw new ServiceException("请输入正确手机号:"+e);
        }
        return super.add(e);
    }
}
