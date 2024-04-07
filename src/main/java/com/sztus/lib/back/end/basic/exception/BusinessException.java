package com.sztus.lib.back.end.basic.exception;

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

}
