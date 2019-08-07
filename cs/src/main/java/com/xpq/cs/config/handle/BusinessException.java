package com.xpq.cs.config.handle;

import com.xpq.cs.model.common.ResultCode;

/**
 * 实际业务异常
 * @author xiepeiqi @date 2019年8月6日
 */
public class BusinessException extends RuntimeException {
	  private  int code = -1;

	    public BusinessException(ResultCode code, String message) {
	        super(message);
	        this.code = code.getCode();
	    }
	    public BusinessException(ResultCode code) {
	        super(code.getMsg());
	        this.code = code.getCode();
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public int getCode() {
	        return code;
	    }
}