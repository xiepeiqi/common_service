package com.xpq.cs.config.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Auther: online
 * @Date: 2019/7/16 11:09
 * @Description: 配置类
 */
@Configuration
public class WebSocketConfig {
	
	/**
	 * 静态变量，用来记录当前在线连接数
	 */
	public static AtomicInteger onlineCount = new AtomicInteger(0);

	/**
	 * 用来存放webSocketSessionId映射的webSocket会话对象
	 */
	public static final ConcurrentHashMap<String, Session> SOCKETID_CLIENT = new ConcurrentHashMap<String, Session>();
	

    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
    /**
     * 定义一个调度任务定时器
     * @return
     * @author xiepeiqi @date 2019年7月22日
     */
	@Bean(name="scheduledExecutor")
	public  ScheduledExecutorService scheduledExecutor() {
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
		return threadPool;
	}
}