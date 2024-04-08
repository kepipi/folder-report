package com.sztus.lib.back.end.basic.object.dto;

import lombok.Data;

/**
 * @author Leon
 */
@Data
public class FileItemDTO {

    private Long fileId;

    private String fileName;

    private String url;

    private Long itemId;

    private String itemName;

    private String quantity;

    private String cleanliness;

    private String condition;

    private String comments;

}
