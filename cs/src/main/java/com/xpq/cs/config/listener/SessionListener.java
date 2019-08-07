package com.xpq.cs.config.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.xpq.cs.util.LogUtils;

/**
 * 会话监听器
 * @author xiepeiqi @date 2019年8月6日
 */
@Component
@WebListener
public class SessionListener implements HttpSessionListener {

	public SessionListener() {
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//设置session失效期为token失效期的2倍
		se.getSession().setMaxInactiveInterval(1800*2);
		LogUtils.getBussinessLogger().info("session创建：{}", se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		LogUtils.getBussinessLogger().info("session销毁：{}", se.getSession().getId());
		/** 移除登陆信息 */
	}

}