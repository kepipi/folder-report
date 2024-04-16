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
    ABNORMAL_PARAMETER(-10003, "Abnormal parameter"),
    IMAGE_RECOGNITION_FAILED(-10004, "Image recognition failed"),
    ;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
