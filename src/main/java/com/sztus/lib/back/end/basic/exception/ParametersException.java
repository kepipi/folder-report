package com.sztus.lib.back.end.basic.exception;


import com.sztus.lib.back.end.basic.object.base.BaseError;
import com.sztus.lib.back.end.basic.type.constant.CodeConst;

/**
 * 专门提供的针对于参数校验失败的异常
 *
 * @author Mist_Chen
 */
public class ParametersException extends Exception {

    private static final long serialVersionUID = 6034911709795654716L;

    /**
     * 仅包含message，不记录栈异常，高性能
     *
     * @param message 异常描述
     */
    public ParametersException(String message) {
        super(message, null, false, false);
        this.code = CodeConst.FAILURE;
        this.message = message;
    }

    /**
     * 仅包含message，不记录栈异常，高性能
     *
     * @param code    错误码定义
     * @param message 异常描述
     */
    public ParametersException(Integer code, String message) {
        this(message);

        this.code = code;
        this.message = message;
    }

    /**
     * 仅包含message，不记录栈异常，高性能
     *
     * @param baseError 参照错误枚举定义
     */
    public ParametersException(BaseError baseError) {
        this(baseError.getMessage());

        this.code = baseError.getCode();
        this.message = baseError.getMessage();
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}