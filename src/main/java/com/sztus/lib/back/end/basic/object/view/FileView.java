package com.sztus.lib.back.end.basic.object.view;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Leon
 */
@Data
@SuperBuilder
public class FileView {

    private Long fileId;

    private String fileName;

    private String url;

}
