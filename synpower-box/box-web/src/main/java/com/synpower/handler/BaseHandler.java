package com.synpower.handler;

import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.IdGenFactory;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.SessionFactroy;
import com.synpower.msg.session.User;
import com.synpower.util.MessageBeanUtil;
import com.synpower.util.ThreadLocalUtil;
import com.synpower.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by SP0013 on 2017/10/23.
 */
public class BaseHandler {
    @Autowired
    protected SessionFactroy sessionFactroy;
    @Autowired
    protected IdGenFactory idGenFactory;
    protected Long generateId(){
        return idGenFactory.create().nextId();
    }
    protected Session getSession() throws SessionException {
        return  sessionFactroy.createSession();
    }

    //获取当前登录用户
    protected User getUser() throws SessionException, SessionTimeoutException {
        String token = Util.getString(getJsonValue4Key("tokenId"));
        if (Util.isEmpty(token))
            return null;
        return getSession().getAttribute(token);
    }

    /**
     * @Author lz
     * @Description: 获取当前json参数
     * @return: {java.util.Map<java.lang.String,java.lang.Object>}
     * @Date: 2018/11/23 13:42
     **/
    public Map<String, Object> getJsonMap() {
        return ThreadLocalUtil.getMap();
    }

    public Object getJsonValue4Key(String key) {
        Map<String, Object> jsonMap = getJsonMap();
        if (jsonMap == null)
            return null;
        return jsonMap.get(key);
    }

    public MessageBean getMB(boolean flag, String errMag) {
        MessageBean msgBean;
        if (flag) {
            msgBean = MessageBeanUtil.getOkMB();
        } else {
            msgBean = MessageBeanUtil.getErrorMB(errMag);
        }
        return msgBean;
    }
}
