package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.request.BatchUploadFileUrlRequest;
import com.sztus.lib.back.end.basic.service.FileBusinessService;
import com.sztus.lib.back.end.basic.service.LocationBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.FolderReportAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:35
 */
@RestController
public class LocationController {

    @Autowired
    private LocationBusinessService locationBusinessService;

    @Autowired
    private FileBusinessService fileBusinessService;

    @GetMapping(FolderReportAction.LIST_LOCATION)
    public Result<List<Location>> listFolder(@RequestParam Long reportId) {
        return Result.ok(locationBusinessService.listLocation(reportId));
    }

    @GetMapping(FolderReportAction.LIST_FILE)
    public Result<List<File>> listFile(@RequestParam Long locationId) {
        return Result.ok(fileBusinessService.listFile(locationId));
    }

    @PostMapping(FolderReportAction.SAVE_FOLDER)
    public Result<String> saveFolder(@RequestBody Location location) {
        locationBusinessService.saveLocation(location);
        return Result.ok();
    }

    @PostMapping(FolderReportAction.UPLOAD_FILE)
    public Result<String> uploadFile() {
        //todo
        return Result.ok();
    }

    @DeleteMapping(FolderReportAction.DELETE_FILE)
    public Result<String> deleteFile(@RequestParam Long fileId) {
        fileBusinessService.deleteFile(fileId);
        return Result.ok();
    }

    @PostMapping(FolderReportAction.AI_ANALYSE)
    public Result<String> aiAnalyse(@RequestBody BatchUploadFileUrlRequest request) throws IOException {
        fileBusinessService.aiAnalyse(request.fileUrlList);
        return Result.ok();
    }
}
