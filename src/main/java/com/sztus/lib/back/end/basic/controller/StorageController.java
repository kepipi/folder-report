package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.request.*;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.service.StorageService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }


    @PostMapping(LocationReportAction.STORAGE_UPLOAD_FILE)
    public Result<StorageFileUploadResponse> uploadFileToS3Backend(
            @RequestBody @Valid StorageFileUploadRequest request
    ) throws BusinessException {
        return Result.ok(storageService.uploadFileToS3(request));
    }


}
