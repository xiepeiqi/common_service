package com.xpq.cs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xpq.cs.model.constant.LogEnum;


/**
 *  本地日志参考类
 * @author xiepeiqi @date 2019年8月6日
 */
public class LogUtils {
 
 
    /**
     * 获取业务日志logger,info级别
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static Logger getBussinessLogger() {
        return LoggerFactory.getLogger(LogEnum.BUSSINESS.getCategory());
    }
 
    /**
     * 获取平台日志logger,info级别
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static Logger getPlatformLogger() {
        return LoggerFactory.getLogger(LogEnum.PLATFORM.getCategory());
    }
 
    /**
     * 获取调试的logger，debug级别
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static Logger getDBLogger() {
        return LoggerFactory.getLogger(LogEnum.DB.getCategory());
    }
 
 
    /**
     * 获取异常日志logger，error级别
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static Logger getExceptionLogger() {
        return LoggerFactory.getLogger(LogEnum.EXCEPTION.getCategory());
    }
 
 
}
