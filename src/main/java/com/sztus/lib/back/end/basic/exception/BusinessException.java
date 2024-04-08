package com.sztus.lib.back.end.basic.exception;

import com.sztus.lib.back.end.basic.object.base.BaseError;
import com.sztus.lib.back.end.basic.type.constant.CodeConst;

/**
 * @author Austin
 */
public class BusinessException extends Exception {

    private Integer code;
    private String message;

    public BusinessException() {}

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

    public BusinessException(String message) {
        super(message, null, false, false);
        this.code = CodeConst.FAILURE;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        this(message);

        this.code = code;
        this.message = message;
    }

    /**
     * 仅包含message，不记录栈异常，高性能
     *
     * @param baseError 参照错误枚举定义
     */
    public BusinessException(BaseError baseError) {
        this(baseError.getMessage());

        this.code = baseError.getCode();
        this.message = baseError.getMessage();
    }

}
