package com.xpq.cs.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  接口统一回调结果
 * @param <T>
 * @author xiepeiqi @date 2019年8月6日
 */
@ApiModel
public class ApiResult<T> {
	
    @ApiModelProperty(value = "返回码", required = true)
    private int code = 0;
    
    @ApiModelProperty(value = "消息提示", required = true)
    private String msg;
    
    @ApiModelProperty(value = "业务数据", required = true)
    private T data;


    public ApiResult(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 使用编码默认信息
     * @param resultCode 错误编码
     * @param msg    消息
     */
    public ApiResult(ResultCode resultCode) {
        super();
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = null;
    }

    /**
     * 自定义返回msg
     * @param resultCode 错误编码
     * @param msg    消息
     */
    public ApiResult(ResultCode resultCode, String msg) {
        super();
        this.code = resultCode.getCode();
        this.msg = msg;
        this.data = null;
    }


    /**
     * 自定义编码信息
     * @param resultCode
     * @param msg
     * @param data
     */
    public ApiResult(ResultCode resultCode, String msg, T data) {
        super();
        this.code = resultCode.getCode();
        this.msg = msg;
        this.data = data;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
