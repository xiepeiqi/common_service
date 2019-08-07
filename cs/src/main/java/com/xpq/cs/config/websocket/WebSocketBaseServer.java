package com.xpq.cs.config.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * webSocket提供公共方法类
 * @author xiepeiqi @date 2019年7月22日
 */
@Slf4j
public class WebSocketBaseServer {

	public static int getOnlineCount() {
		return WebSocketConfig.onlineCount.get();
	}

	public static void addOnlineCount() {
		WebSocketConfig.onlineCount.incrementAndGet();
	}

	public static void subOnlineCount() {
		if (WebSocketConfig.onlineCount.decrementAndGet() < 0) {// 避免出现负数
			WebSocketConfig.onlineCount.incrementAndGet();
		}
	}
	
	/**
	 * 发送给某个用户
	 * @param map
	 * @param sessionId
	 * @param message
	 * @author xiepeiqi @date 2019年7月22日
	 */
	public static void sendMessage(ConcurrentHashMap<String, Session> map,String sessionId,String message) {
		try {
			map.get(sessionId).getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("webSocket发送给某个用户失败。sessionId:[{}],errorMessage:[{}],sendMessage:[{}]",sessionId,e.getMessage(),message);
		}
	}
	
	/**
	 * 发送给所有用户
	 * @param map
	 * @param message
	 * @author xiepeiqi @date 2019年7月22日
	 */
	public static void sendMessage(ConcurrentHashMap<String, Session> map,String message) {
		for (Session session : map.values()) {
			session.getAsyncRemote().sendText(message);//异步发送不阻塞
			//getBasicRemote().sendText(message);这个是同步发送
		}
	}
}
