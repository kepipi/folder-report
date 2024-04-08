package com.sztus.lib.back.end.basic.object.request;

import lombok.Data;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 18:13
 */

@Data
public class BatchUploadFileUrlRequest {

    public String url;

    public Long fileId;
}
