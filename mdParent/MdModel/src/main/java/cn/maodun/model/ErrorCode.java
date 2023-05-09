package cn.maodun.model;

/**
 * 异常编码接口，用于抛出异常是使用
 * @author Diamond
 */
public interface ErrorCode {
    /**
     * 异常消息
     * @return
     */
    String getMessage();

    /**
     * 异常错误码
     * @return
     */
    int getCode();
}
