package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Folder;
import com.sztus.lib.back.end.basic.service.FileBusinessService;
import com.sztus.lib.back.end.basic.service.FolderBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.FolderReportAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:35
 */
@RestController
public class FolderController {

    @Autowired
    private FolderBusinessService folderBusinessService;

    @Autowired
    private FileBusinessService fileBusinessService;

    @GetMapping(FolderReportAction.LIST_FOLDER)
    public Result<List<Folder>> listFolder(@RequestParam Long housePropertyId) {
        return Result.ok(folderBusinessService.listFolder(housePropertyId));
    }

    @GetMapping(FolderReportAction.LIST_FILE)
    public Result<List<File>> listFile(@RequestParam Long folderId) {
        return Result.ok(folderBusinessService.listFile(folderId));
    }
}
