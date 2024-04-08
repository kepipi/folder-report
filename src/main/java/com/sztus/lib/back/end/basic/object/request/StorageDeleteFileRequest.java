package com.sztus.lib.back.end.basic.object.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StorageDeleteFileRequest extends DeleteFileRequest {

    private String fileCode;
}
