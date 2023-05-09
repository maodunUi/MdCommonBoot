package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import feign.FeignException;

/**
 * 自定义业务异常
 * @author Diamond
 */
public class AppException  {

    /**
     *
     */
    private static final long serialVersionUID = 2404372373182554123L;
    private int code;
    private String message;

    public AppException() {
        this(FastErrorCode.UNKNOWN_EXCEPTION);
    }

    private AppException(String message) {
        super(200, message);
    }

    public AppException(ErrorCode code) {
        super(200, code == null ? FastErrorCode.UNKNOWN_EXCEPTION.getMessage() : code.getMessage());
        code = code == null ? FastErrorCode.UNKNOWN_EXCEPTION : code;
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public AppException(ErrorCode code, Exception e) {
        this(code);
        this.addSuppressed(e);
    }

    public AppException(ErrorCode code, String exMsg) {
        this(code);
        this.message = String.format(code.getMessage(), exMsg);
    }

    public AppException(int code, String exMsg) {
        super(200, exMsg);
        this.code = code;
        this.message = exMsg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
