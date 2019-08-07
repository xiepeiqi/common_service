package com.xpq.cs.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * token工具类
 * @author xiepeiqi @date 2019年2月28日
 */
public class TokenUtils {
	
	/**
	 * 获取token
	 * @param model
	 * @param minute 有效时长,分钟
	 * @return
	 * @author xiepeiqi @date 2019年4月29日
	 */
	public static String createToken(Object model ,Integer minute) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MINUTE, minute);
		return (DigestUtils.md5Hex(JSON.toJSONString(model))+"_"+instance.getTimeInMillis()).toUpperCase();
	}
	
	/**
	 * 判断token是否可用
	 * @param token
	 * @return 0：成功，1：密钥已失效，请重新登陆，2：密钥无效
	 * @author xiepeiqi @date 2019年4月29日
	 */
	public static Integer tokenStatus(String token) {
		if (StringUtils.isBlank(token)) {
			return 2;
		}
		String[] split = token.split("_");
		if (split.length==2) {
			if (Long.parseLong(split[1])>= new Date().getTime()) {
				return 0;
			}else {
				return 1;
			}
		}else {
			return 2;
		}
	}
}
