package com.xpq.cs.config.websocket;


import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.xpq.cs.config.handle.BusinessException;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义的一个业务websocket类，给浏览器连接
 * 
 * @author xiepeiqi @date 2019年7月22日
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{type}")
public class WebSocketBusinessServer extends WebSocketBaseServer {

	@OnOpen
	public void OnOpen(@PathParam("type") String type, Session session) {
		WebSocketConfig.SOCKETID_CLIENT.put(session.getId(),session);
		addOnlineCount(); // 在线数加1
		log.info("ws/{} 有新连接加入！当前连接数为:[{}],socketSessionId:[{}]",type, getOnlineCount(), session.getId());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void OnClose(@PathParam("type") String type, Session session) {
		WebSocketConfig.SOCKETID_CLIENT.remove(session.getId());
		subOnlineCount(); // 在线数减1
		log.info("ws/{} 有一连接关闭！当前在线连接数为:[{}],关闭的socketSessionId:[{}]",type, getOnlineCount(), session.getId());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(@PathParam("type") String type, String message, Session session) {

	}

	/**
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		WebSocketConfig.SOCKETID_CLIENT.remove(session.getId());
		// 自定义异常无需打印
		if (error instanceof BusinessException) {
			log.error("ws 发生错误:{},{}", ((BusinessException) error).getCode(), error.getMessage());
		} else {
			log.error("ws 发生错误:{}", error.getMessage());
			error.printStackTrace();
		}
	}

}