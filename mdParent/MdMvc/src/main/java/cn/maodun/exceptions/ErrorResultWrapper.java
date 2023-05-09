package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import lombok.Data;

/**
 * 异常结果集包装类
 * @author Diamond
 */
@Data
public class ErrorResultWrapper implements ErrorCode {
    private int code;
    private String message;
    private Object errorData;

    public ErrorResultWrapper() {}

    public ErrorResultWrapper(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResultWrapper(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
