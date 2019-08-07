package com.xpq.cs.util;

import com.xpq.cs.model.common.ApiResult;
import com.xpq.cs.model.common.ResultCode;

/**
 * 返回Result 的工具类
 * @author xiepeiqi @date 2019年8月6日
 */
public class ApiResultUtils {
	
    /**
     * 自定义成功返回的message和data
     * @param message
     * @param data
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> result = new ApiResult(ResultCode.SUCCESS.getCode(), message, data);
        return result;
    }

    /**
     * 自定义成功返回的data
     * @param data
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
        return result;
    }

    /**
     *  自定义message,data默认为null
     * @param message
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> successWithNullData(String message) {
        ApiResult<T> result = new ApiResult(ResultCode.SUCCESS.getCode(), message, null);
        return result;
    }

    /**
     * 直接返回操作成功，data 为null
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> success() {
        ApiResult<T> result = new ApiResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
        return result;
    }

    /**
     * 自定义错误错误message
     * @param errorCode
     * @param message
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */

    public static <T> ApiResult<T> error(int errorCode, String message) {
        ApiResult<T> result = new ApiResult(errorCode, message, null);
        return result;
    }

    /**
     * 自定义错误错误message 及返回data
     * @param errorCode
     * @param message
     * @param data
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> error(int errorCode, String message, T data) {
        ApiResult<T> result = new ApiResult(errorCode, message, data);
        return result;
    }
    
    /**
     * 返回错误信息
     * @param resultCode
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    public static <T> ApiResult<T> error(ResultCode resultCode) {
        ApiResult<T> result = new ApiResult(resultCode.getCode(), resultCode.getMsg(), null);
        return result;
    }
    
    /**
     * 返回错误信息
     * @param msg
     * @return
     * @author xiepeiqi @date 2019年8月7日
     */
    public static <T> ApiResult<T> failed(String msg) {
    	ApiResult<T> result = new ApiResult(ResultCode.SERVER_ERROR, msg, null);
    	return result;
    }

}
