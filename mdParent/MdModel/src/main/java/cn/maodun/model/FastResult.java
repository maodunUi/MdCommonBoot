package cn.maodun.model;

import lombok.Data;

/**
 * 结果集封装
 * @author Diamond
 */
@Data
public class FastResult<T> {
    /**
     * 响应成功的默认code
     */
    public static int CODE_SUCCESS = 0;

    public static final FastResult<?> SUCCESS = new FastResult<>();

    private String message;
    private int code = CODE_SUCCESS;
    private T data;

    public FastResult() {
    }

    public FastResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public FastResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public FastResult(int code) {
        this.code = code;
    }

    public FastResult(T data) {
        this.data = data;
    }

    public boolean wasSuccess() {
        return code == CODE_SUCCESS;
    }

    public ShanhsResult<T> toShanhsResult() {
        ShanhsResult<T> shanhsResult = new ShanhsResult<>();
        shanhsResult.setData(data);
        shanhsResult.setCode(code);
        shanhsResult.setMsg(message);
        return shanhsResult;
    }
}
