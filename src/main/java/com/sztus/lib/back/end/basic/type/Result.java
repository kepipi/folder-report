package com.sztus.lib.back.end.basic.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.base.BaseError;
import com.sztus.lib.back.end.basic.type.enumerate.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应结果类
 *
 * @author AustinWang
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String message;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public static <T> Result<T> ok() {
        return restResult(null, ErrorCode.SUCCESS, ErrorCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> ok(T data) {
        return restResult(data, ErrorCode.SUCCESS, ErrorCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> ok(T data, String message) {
        return restResult(data, ErrorCode.SUCCESS, message);
    }

    public static <T> Result<T> fail() {
        return restResult(null, ErrorCode.FAILURE, ErrorCode.FAILURE.getMessage());
    }


    public static <T> Result<T> fail(String message) {
        return restResult(null, ErrorCode.FAILURE, message);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(data, ErrorCode.FAILURE, ErrorCode.FAILURE.getMessage());
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(data, ErrorCode.FAILURE, message);
    }

    public static <T> Result<T> fail(BusinessException e) {
        return restResult(null, e.getCode(), e.getMessage());
    }


    public static <T> Result<T> fail(BaseError error, T data) {
        return restResult(data, error.getCode(), error.getMessage());
    }

    public static <T> Result<T> fail(BaseError error) {
        return restResult(null, error.getCode(), error.getMessage());
    }

    public static <T> Result<T> fail(int code, String message) {
        return restResult(null, code, message);
    }

    public static <T> Result<T> fail(String message, T data) {
        return restResult(data, ErrorCode.FAILURE, message);
    }

    private static <T> Result<T> restResult(T data, int code, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMessage(message);
        return apiResult;
    }

    private static <T> Result<T> restResult(T data, BaseError error, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(error.getCode());
        apiResult.setData(data);
        apiResult.setMessage(message);
        return apiResult;
    }

}
