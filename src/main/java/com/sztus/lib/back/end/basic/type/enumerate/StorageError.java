package com.sztus.lib.back.end.basic.type.enumerate;

import com.sztus.lib.back.end.basic.object.base.BaseError;

/**
 * @author Max
 * @date 2023/05/10
 */
public enum StorageError implements BaseError {
    //StorageError
    INVALID_REQUEST_PARAMETER(-95040001, "Invalid request parameter."),
    FILE_UPLOAD_FAILURE(-95040002, "File upload failure."),
    FILE_DOWNLOAD_FAILURE(-95040003, "File download failure."),
    PROVIDER_IS_NOT_EXISTED(-95040004, "Provider is not existed"),

    PROVIDER_IS_DISABLED(-95040005, "Provider is disabled"),
    CONNECTION_PARAMETER_IS_NOT_EXISTED(-95040005, "Connection parameter is not existed"),
    FAIL_TO_CREATE_FILE_MAPPING(-95040006, "Fail to create file mapping"),
    FILE_MAPPING_IS_NOT_EXIST(-95040007, "File mapping is not exist"),
    FAIL_TO_CREATE_FILE_OPERATION_RECORD(-95040008, "Fail to create file operation record"),
    INVALID_ACCESS_TOKEN(-95040009, "Invalid access token"),
    FAIL_TO_DELETE_FILE(-95040010, "Fail to delete file"),
    FILE_PATH_IS_WRONG(-95040011, "File path cannot contain these symbols : *?\"<>| "),

    UNABLE_TO_FIND_A_SUITABLE_CONNECTION(-95040012, "Unable to find a suitable connection"),
    ;


    private final Integer code;
    private final String message;

    StorageError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
