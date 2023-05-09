package cn.maodun.model;

import lombok.Data;

/**
 * 闪回收远程接口基本返回值
 * @author Diamond
 */
@Data
public class ShanhsResult<T> {
    private boolean success;
    private String msg;
    private int code;
    private T data;
    public ShanhsResult(){
        this.success = true;
        this.code = 0;
    }

    public ShanhsResult(T data) {
        this.data = data;
        this.success = true;
    }

    public ShanhsResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.success = false;
        this.msg = errorCode.getMessage();
    }

    public ShanhsResult(int code, String msg) {
        this.code = code;
        this.success = code == 0;
        this.msg = msg;
    }

    public FastResult<T> toFastResult() {
        FastResult<T> shanhsResult = new FastResult<>();
        shanhsResult.setData(data);
        shanhsResult.setCode(code);
        shanhsResult.setMessage(msg);
        return shanhsResult;
    }
}
