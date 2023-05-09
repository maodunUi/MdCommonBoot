package cn.maodun.exceptions;

import lombok.Data;

import java.io.Serializable;

/**
 * 字段参数校验错误异常包装类
 * @author Diamond
 */
@Data
public class FieldErrorDTO implements Serializable {

    /**
     * 字段名
     */
    private String name;
    /**
     * 错误内容
     */
    private String message;

    public FieldErrorDTO() {}

    public FieldErrorDTO(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + name + "]: " + message;
    }
}
