package com.xpq.cs.config.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.xpq.cs.model.common.ApiResult;
import com.xpq.cs.model.common.ResultCode;
import com.xpq.cs.util.IpUtils;
import com.xpq.cs.util.LogUtils;
import com.xpq.cs.util.TokenUtils;

/**
 * 请求拦截器
 *
 * @author xiepeiqi
 * @date 2018年8月8日 @time 下午4:51:43
 */
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        LogUtils.getBussinessLogger().info("http>>>>>>\n Method={},ClientIp={},RequestURL={},Protocol={},CharacterEncoding={},SessionId:{},QueryString={}\n http<<<<<<",
                request.getMethod(), IpUtils.getClientIp(request), request.getRequestURL(), request.getProtocol(), request.getCharacterEncoding(),
                request.getSession().getId(), request.getQueryString());
        //return true;
        if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Basic ")) {
            //Authorization: Basic 12345678
            String[] split = request.getHeader("Authorization").split("\\s");
            if (split != null && split.length == 2 && split[0].equals("Basic") && split[1].equals("12345678")) {
            	Integer status = TokenUtils.tokenStatus(split[1]);
            	if (status==0) {
					return true;
				}else if (status==1) {
					//token失效
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200);
					response.getWriter().append(JSON.toJSONString(new ApiResult(ResultCode.AUTHENTICATION_TOKEN_OVERDUE, "密钥失效,请重新登陆！")));
					return false;
				}else{
					//token无效
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200);
					response.getWriter().append(JSON.toJSONString(new ApiResult(ResultCode.AUTHENTICATION_NOTPASS, "密钥无效，请重新登陆！")));
					return false;
				}
            }
        }
        //非法请求
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().append(JSON.toJSONString(new ApiResult(ResultCode.AUTHENTICATION_ILLEGAL_REQUEST, "请登陆！")));
        return false;
    }

}
