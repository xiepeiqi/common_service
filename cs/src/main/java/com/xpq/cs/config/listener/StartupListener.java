package com.xpq.cs.config.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 启动监听器
 * @author xiepeiqi @date 2019年8月6日
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	 @Override
	 public void onApplicationEvent(ContextRefreshedEvent evt) {
		 if(evt.getApplicationContext().getParent() == null){  
			 
		 }
	 }

	
}
