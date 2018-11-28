package com.synpower.msg.session;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synpower.lang.CacheNotFoundException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.util.CacheUtil2;
import com.synpower.util.serializer.KryoSerizilzer;
import com.synpower.util.serializer.SerializerFactory;
import com.synpower.util.serializer.SynpowerSerializer;


/**
 * 用户SESSION
 *
 * @author SP0013
 * @date 2017/10/23
 */
@Component
public class Session {
    @Autowired
    private CacheUtil2 cacheUtil2;
    private Logger logger = Logger.getLogger(KryoSerizilzer.class);
    /**
    *设置用户SESSION
    *@author SP0013
    *@method setAttribute
    *@param tokenId 令牌ID,当前用户
    *@return 
    *@Date 2017/10/26
    */
    public void setAttribute(String tokenId,User val) throws SessionException {
    if (StringUtils.isBlank(tokenId)) {
        throw new SessionException("pls give me a sessionId");
    }

    String serizlizerMsg = getSerializer().serialization(val,val.getClass());
        try {
            cacheUtil2.setEx(tokenId,30*60,serizlizerMsg);
        } catch (ServiceException e) {
            logger.error(e.getMessage(),e);
        }
    }
    /**
    *获取登录用户信息
    *@author SP0013
    *@method getAttribute
    *@param tokenId 令牌ID
    *@return 返回当前登陆用户
    *@Date 2017/10/26
    */
    public User getAttribute(String tokenId) throws SessionException, SessionTimeoutException {
        if (StringUtils.isBlank(tokenId)) {
            throw new SessionException("pls give me a tokenId");
        }
        String serizlizerMsg = null;
        User user = null;
        try {
            serizlizerMsg = cacheUtil2.getEx(tokenId,1800);
            if (StringUtils.isBlank(serizlizerMsg)) {
                throw new SessionTimeoutException("请重新登陆");
            }
            user =  (User)getSerializer().deserialization(serizlizerMsg,User.class,false);
        } catch (ServiceException e) {
            logger.error(e.getMessage(),e);
            throw new SessionException(e.getMessage());
        } catch (CacheNotFoundException e) {
            logger.error(e.getMessage(),e);
            throw new SessionTimeoutException("请重新登陆");
        }
        return user;
    }
    /**
    *删除SESSION
    *@author SP0013
    *@method remove
    *@param tokenId 令牌ID
    *@return 
    *@Date 2017/10/26
    */
    public void remove(String tokenId)throws SessionException {
        if (StringUtils.isBlank(tokenId)) {
            throw new SessionException("pls give me a tokenId");
        }
        try {
            Long count = cacheUtil2.del(tokenId);
            if (count.longValue() ==0 ) {
                throw new SessionException("没有删除任何东西");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(),e);
            throw new SessionException(e.getMessage());
        }
    }
    /**
    *检查该登陆用户是否存在
    *@author SP0013
    *@method isValid
    *@param tokenId
    *@return 返回TRUE则存在
    *@Date 2017/10/26
    */
    public boolean isValid(String tokenId) throws SessionException {
        if (StringUtils.isBlank(tokenId)) {
            logger.error("没有找到tokenId");
            throw new SessionException("没有找到tokenId");
        }
        boolean flag = false;
        try {
            flag = cacheUtil2.exist(tokenId);

        } catch (ServiceException e) {
            throw new SessionException(e.getMessage());
        }
        return  flag;
    }
    /**
    *session信息序列化
    *@author SP0013
    *@method  SynpowerSerializer
    *@param 
    *@return 
    *@Date 2017/10/26
    */
    private SynpowerSerializer getSerializer() {
        SerializerFactory factory = new SerializerFactory();
        return factory.createKryo();
    }
}
