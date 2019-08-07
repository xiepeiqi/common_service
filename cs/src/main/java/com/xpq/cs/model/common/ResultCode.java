package com.xpq.cs.model.common;

/**
 * 接口返回码
 * @author xiepeiqi @date 2019年8月6日
 */
public enum ResultCode {

    /**
     * 调用成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-1, "未知错误"),
    /**
     * 鉴权错误
     */
    AUTHENTICATION_NOTPASS(4001001, "鉴权错误"),
    /**
     * 非法请求
     */
    AUTHENTICATION_ILLEGAL_REQUEST(4001002, "非法请求"),
    /**
     * token失效
     */
    AUTHENTICATION_TOKEN_OVERDUE(4001003, "token失效"),
    /**
     * 密码重置失败
     */
    AUTHENTICATION_PASSWORD_RESET(4001004, "密码重置失败"),

    /**
     * 参数类型错误
     */
    PARAM_TYPE_ERROR(5001001, "参数类型错误"),
    /**
     * 参数缺失
     */
    PARAM_LACK(5001002, "参数缺少"),
    /**
     * 请求参数值不合法
     */
    PARAM_INVALID(5001003, "请求参数值不合法"),
    /**
     * 请求方式(Get/Post)错误
     */
    REQUEST_METHOD_NOT_SUPPORTED(5001004, "请求方式(Get/Post)错误"),
    /**
     * 请求Content-Type错误
     */
    MEDIA_TYPE_NOT_SUPPORT(5001005,"请求Content-Type错误"),
    /**
     * 请求404
     */
    REQUEST_NOT_FOUND(5001006, "404 NOT FOUND"),
   

    /**
     * 资源错误
     */
    RESOURCE_ERROR(6001001, "资源错误"),


    /**
     * 服务错误
     */
    SERVER_ERROR(7001001, "服务错误");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

}
