package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;

/**
 * 异常结果包装接口
 *
 * @author Diamond
 */
public interface ExceptionWrapperAdvice {

    /**
     * 针对错误码和错误信息进行包装
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 错误结果包装
     */
    default ErrorResultWrapper doWrapper(int code, String message) {
        return new ErrorResultWrapper(code, message);
    }

    /**
     * 针对ErrorCode接口实现类进行包装
     *
     * @param errorCode 错误码和错误信息
     * @return 错误结果包装
     */
    default ErrorResultWrapper doWrapper(ErrorCode errorCode) {
        return new ErrorResultWrapper(errorCode);
    }

    /**
     * 针对ErrorCode接口，以及扩展错误消息进行包装
     *
     * @param errorCode 错误码错误信息
     * @param message   扩展错误信息（会拼接到errorCode的message里面)
     * @return 错误结果包装
     */
    default ErrorResultWrapper doWrapperWithMessage(ErrorCode errorCode, String message) {
        return new ErrorResultWrapper(errorCode.getCode(), String.format(errorCode.getMessage(), message));
    }
}
