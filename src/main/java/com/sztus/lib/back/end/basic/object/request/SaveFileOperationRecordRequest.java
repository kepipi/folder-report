package com.sztus.lib.back.end.basic.object.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveFileOperationRecordRequest {

    @NotNull
    private String fileCode;

    @NotBlank
    private String openId;

    @NotNull
    private Long operatedAt;

    private Integer type;

    private String userAgent;

    private String userIp;
}
