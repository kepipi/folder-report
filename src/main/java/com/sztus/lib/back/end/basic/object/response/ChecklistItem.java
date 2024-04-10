package com.sztus.lib.back.end.basic.object.response;

import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Leon
 */
@Data
@SuperBuilder
public class ChecklistItem {

    private String fileName;

    private String url;

    private List<FileItemDTO> fileItems;

}
