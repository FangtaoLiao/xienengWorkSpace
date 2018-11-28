package com.synpower.msg;


import com.synpower.lang.WebsocketException;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 放webSocketSession的地方，目前单机存放，日后负载均衡，则需要用MQ消息反推
 * Created by SP0013 on 2017/10/13.
 */
public class WebSocketSessions extends ConcurrentHashMap<String,WebSocketSession>{
    public void add(String usrId, WebSocketSession session) throws WebsocketException {
        if (StringUtils.isBlank(usrId)) {
            throw new WebsocketException("存放Webscoket用户时ID为空:"+this.getClass().getName());
        }
        this.put(usrId,session);
    }
    public WebSocketSession fetch(String usrId) throws WebsocketException {
        if (StringUtils.isBlank(usrId)) {
            throw new WebsocketException("获取Webscoket用户时ID为空:"+this.getClass().getName());
        }
        return this.get(usrId);
    }
}
