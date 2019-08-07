package com.xpq.cs.config.handle;


import static com.xpq.cs.model.common.ResultCode.MEDIA_TYPE_NOT_SUPPORT;
import static com.xpq.cs.model.common.ResultCode.PARAM_LACK;
import static com.xpq.cs.model.common.ResultCode.PARAM_TYPE_ERROR;
import static com.xpq.cs.model.common.ResultCode.REQUEST_METHOD_NOT_SUPPORTED;
import static com.xpq.cs.model.common.ResultCode.REQUEST_NOT_FOUND;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSON;
import com.xpq.cs.model.common.ApiResult;
import com.xpq.cs.model.common.ResultCode;
import com.xpq.cs.util.ApiResultUtils;
import com.xpq.cs.util.LogUtils;

/**
 * 异常捕获处理类
 * @author xiepeiqi @date 2019年8月6日
 */
@ControllerAdvice
public class ExceptionHandler {
	
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult<String> exceptionHandle(HttpServletRequest req, Exception e) {
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ApiResultUtils.error(REQUEST_METHOD_NOT_SUPPORTED);
        }else if (e instanceof NoHandlerFoundException) {
            return ApiResultUtils.error(REQUEST_NOT_FOUND);
        }  else if (e instanceof MissingServletRequestParameterException) {
            return ApiResultUtils.error(PARAM_LACK);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            return ApiResultUtils.error(PARAM_TYPE_ERROR);
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            return ApiResultUtils.error(MEDIA_TYPE_NOT_SUPPORT);
        } else if (e instanceof BusinessException) {
            //业务逻辑错误
            BusinessException ce = (BusinessException) e;
            LogUtils.getExceptionLogger().error("BusinessException: code = {},msg = {}", ce.getCode(), ce.getMessage());
            return ApiResultUtils.error(ce.getCode(), ce.getMessage());
        }
        //1.javax.validation:参数验证失败：比如不能为空的，传递了空的参数过来，通过注解@Valid @NotBlank进行限制参数,
        else if (e instanceof BindException) {
            LogUtils.getExceptionLogger().error("ERROR:exceptionHandle:BindException = {}", ((BindException) e).getBindingResult());
            //具体处理：
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (bindingResult != null) {
                return disposeBindResult(bindingResult);
            } else {
                return ApiResultUtils.error(ResultCode.PARAM_INVALID);
            }
        }
        //2.@NotNull 参数验证
        else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) e;
            LogUtils.getExceptionLogger().error("ERROR:exceptionHandle:ConstraintViolationException = {}", violationException.toString());
            Iterator iterator = violationException.getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                ConstraintViolationImpl constraintViolation = (ConstraintViolationImpl) iterator.next();
                String msg = constraintViolation.getMessageTemplate();
                return ApiResultUtils.error(ResultCode.PARAM_INVALID.getCode(), msg);
            } else {
                return ApiResultUtils.error(ResultCode.PARAM_INVALID);
            }
            //3.@Range 范围值验证
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = exception.getBindingResult();
            LogUtils.getExceptionLogger().error("INFO:exception:MethodArgumentNotValidException:{}", exception.getMessage());
            return disposeBindResult(bindingResult);
        }

        e.printStackTrace();
        //未知错误
        StringBuilder sb = new StringBuilder();
        sb.append("Exception msg:").append(e.getMessage()).append("   ");
        sb.append("Exception type:").append(e.getClass());
        LogUtils.getExceptionLogger().error("ERROR: exception = {}", sb.toString());
        return ApiResultUtils.error(ResultCode.UNKNOWN_ERROR);
    }

    /**
     * 校验前端参数是否合法
     * @param bindingResult
     * @return
     * @author xiepeiqi @date 2019年8月6日
     */
    private ApiResult<String> disposeBindResult(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            HashMap<String, String> hashMap = new LinkedHashMap<>();
            for (FieldError fieldError : fieldErrors) {
                hashMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            /**先把值传回去给前端*/
            return new ApiResult<String>(ResultCode.PARAM_TYPE_ERROR, JSON.toJSONString(hashMap.values()));

        } else {
            return ApiResultUtils.error(ResultCode.PARAM_INVALID);
        }


    }


}