package cn.maodun.response;


import cn.maodun.model.ErrorCode;

/**
 * 返回值包装接口
 * @author Diamond
 */
public interface FastMvcResponseWrapper {
    /**
     * 正常返回值的包装
     *
     * @param data
     * @return
     */
    Object resultWrapper(Object data);

    /**
     * 错误代码返回值包装
     * @param errorCode
     * @return 包装后的返回值
     */
    default Object errorWrapper(ErrorCode errorCode) {
        return this.errorCodeWrapper(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 错误类型返回值包装
     *
     * @param code      错误码
     * @param message   错误消息
     * @return
     */
    Object errorCodeWrapper(int code, String message);
}
