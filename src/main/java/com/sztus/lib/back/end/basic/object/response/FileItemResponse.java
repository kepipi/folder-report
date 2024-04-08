package com.sztus.lib.back.end.basic.object.response;

import com.sztus.lib.back.end.basic.object.view.FileView;
import com.sztus.lib.back.end.basic.object.view.ItemView;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Leon
 */
@Data
@SuperBuilder
public class FileItemResponse {

    private List<FileView> fileList;

    private List<ItemView> itemList;

}
