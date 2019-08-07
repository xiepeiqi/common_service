package com.xpq.cs.config.websocket;

import java.util.Map;

import lombok.Data;

/**
 * 接受webSocket前端调用数据
 * @author xiepeiqi @date 2019年7月17日
 */
@Data
public class WSRequest {
	
	private String type;//类型
	
	private Map<String, Object> data;//返回的数据内容
	
}
