package com.xpq.cs.config.websocket;


import com.xpq.cs.model.common.ApiResult;

import lombok.Data;

/**
 * 返回给前端调用数据
 * @author xiepeiqi @date 2019年7月17日
 */
@Data
public class WSResult<T> {
	
	private String type;//类型
	
	private ApiResult<T> result;//返回的数据内容

	public WSResult(String type, ApiResult<T> result) {
		super();
		this.type = type;
		this.result = result;
	}

	public WSResult() {
		super();
	}
	
}
