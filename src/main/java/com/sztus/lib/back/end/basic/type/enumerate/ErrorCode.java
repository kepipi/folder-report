package com.sztus.lib.back.end.basic.type.enumerate;


import com.sztus.lib.back.end.basic.object.base.BaseError;

/**
 * 错误代码
 *
 * @author Austin
 */
public enum ErrorCode implements BaseError {
    //ErrorCode
    SUCCESS(1, "Success"),
    UNKNOWN(0, "Unknown"),
    FAILURE(-1, "Failure"),
    INVALID_TOKEN(-10001, "Invalid token"),
    PARAMETER_INCOMPLETE(-10002, "Parameter incomplete"),
    ;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
