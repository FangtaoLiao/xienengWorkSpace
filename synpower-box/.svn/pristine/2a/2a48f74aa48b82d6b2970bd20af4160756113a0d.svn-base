package com.synpower.handle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.synpower.lang.ServiceException;
import com.synpower.msg.Msg;
import com.synpower.util.WebsocketUtil;




/*****************************************************************************
 * @Package: com.synpower.handle
 * ClassName: WebSocketPushHandler
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月19日上午9:53:50   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@Service
public class WebSocketPushHandler implements WebSocketHandler{
	private Logger logger = Logger.getLogger(WebSocketPushHandler.class);
	public static final List<WebSocketSession>users=Collections.synchronizedList(new ArrayList<>());
	//用户进入系统监听
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info(session.getAttributes().get("sn")+"webSocket 成功进入了系统。。。");
		users.add(session);
	}

	//
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	    //将消息进行转化，因为是消息是json数据，可能里面包含了发送给某个人的信息，所以需要用json相关的工具类处理之后再封装成TextMessage，
	   // 我这儿并没有做处理，消息的封装格式一般有{from:xxxx,to:xxxxx,msg:xxxxx}，来自哪里，发送给谁，什么消息等等	    
	    //TextMessage msg = (TextMessage)message.getPayload();
	    //给所有用户群发消息
	    //sendMessagesToUsers(msg);
	    //给指定用户群发消息
	   // sendMessageToUser(userId,msg);
		logger.info("websocket recive "+session.getAttributes().get("sn")+": "+message);

	}
        
        //后台错误信息处理方法
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

	}

	//用户退出后的处理，不如退出之后，要将用户信息从websocket的session中remove掉，这样用户就处于离线状态了，也不会占用系统资源
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		if(session.isOpen()){
			session.close();
		}
		users.remove(session);
		logger.info(session.getAttributes().get("sn")+" webSocket 安全退出了系统");
		
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
	 * 给所有的用户发送消息
	 * @throws ServiceException 
	 */
	public void sendMessagesToUsers(TextMessage message) throws ServiceException{
		for(WebSocketSession user : users){
			try {
			    //isOpen()在线就发送
				if(user.isOpen()){
					user.sendMessage(message);
				}
			} catch (IOException e) {
				throw new ServiceException(Msg.TYPE_UNKNOWN_ERROR);
			}
		}
	}
	
	/**
	 * 发送消息给指定的用户
	 * @throws ServiceException 
	 */
	public void sendMessageToUser(String userId,TextMessage message) {
		//List<WebSocketSession>users=users;
		for (int i = 0; i < users.size(); i++) {
			WebSocketSession user=users.get(i);
			String sn=String.valueOf(user.getAttributes().get("sn"));
			if(sn.equals(userId)){
				try {
				    //isOpen()在线就发送
					if(user.isOpen()){
						user.sendMessage(message);
					}
				} catch (IOException e) {
					
				}
			}
		}
	}
	
}