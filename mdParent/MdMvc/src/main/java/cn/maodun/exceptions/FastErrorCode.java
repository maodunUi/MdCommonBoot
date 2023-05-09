package cn.maodun.exceptions;


import cn.maodun.model.ErrorCode;

/**
 * 通用异常处理
 * @author shanhs
 */
public enum FastErrorCode implements ErrorCode {
    /**
     * 系统未知异常，通用处理
     */
    UNKNOWN_EXCEPTION(500, "系统异常"),
    /**
     * 参数类异常
     */
    INVALID_PARAMS(5003, "参数异常: %s"),
    /**
     * HTTP请求方式错误
     */
    REQUEST_METHOD_NOT_SUPPORT(5004, "请求方式错误: %s"),
    /**
     * 未知的返回值
     */
    INVALID_RESULT(5005, "无效的结果"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(5010, "请求超时"),
    /**
     * 用户授权类异常
     */
    USERNAME_OR_PASSWORD_ERROR(40001, "用户名或密码错误"),
    /**
     * 用户授权过期，token无效异常
     */
    AUTHORIZATION_EXPIRED(40002, "授权已过期，请重新登陆"),
    /**
     * 没有该接口访问权限移除
     */
    NO_PERMISSION_EXCEPTION(40003, "没有该操作的权限"),
    /**
     * 账户已经被锁定异常
     */
    ACCOUNT_LOCKED_EXCEPTION(40004, "账户已被锁定"),
    /**
     * 其他关于权限校验未通过的异常
     */
    OTHER_AUTHORIZATION_EXCEPTION(40005, "权限校验不通过"),

    THIRD_CALL_FAILED(40006, "第三方调用失败 %s"),
    THIRD_BUSINESS_ERROR(40007, "第三方业务异常 %s"),
    TOO_MANY_REQUEST(40008, "请求过于频繁"),
    ;

    private final int code;
    private final String message;

    FastErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
