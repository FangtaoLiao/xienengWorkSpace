package com.synpower.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

@Repository
@Scope(scopeName="singleton")
public class WebsocketUtil {
	private Logger logger = Logger.getLogger(WebsocketUtil.class);
	private   List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	public List<WebSocketSession> getSession(){
		if (sessions==null) {
			sessions = new ArrayList<WebSocketSession>();
		}
		return this.sessions;
	}
	private  WebsocketUtil(){
		logger.info(" webSocket 消息队列初始化");
	};
}
