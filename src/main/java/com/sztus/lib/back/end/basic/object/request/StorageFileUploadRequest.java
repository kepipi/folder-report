package com.sztus.lib.back.end.basic.object.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Max
 */
@Getter
@Setter
public class StorageFileUploadRequest {

    @NotBlank
    private String path;

    private String fileName;

    private String fileBody;

    private Integer acl;

    private Integer mode;

    private String contentType;

    private String domainName;

}
