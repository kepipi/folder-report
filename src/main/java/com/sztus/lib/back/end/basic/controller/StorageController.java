package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.dao.service.FileMappingService;
import com.sztus.lib.back.end.basic.dao.service.FileOperationRecordService;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.FileOperationRecord;
import com.sztus.lib.back.end.basic.object.request.*;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.service.StorageService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.FolderReportAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class StorageController {

    private final StorageService storageService;
    private final FileOperationRecordService fileOperationRecordService;

    public StorageController(StorageService storageService, FileMappingService fileMappingService, FileOperationRecordService fileOperationRecordService) {
        this.storageService = storageService;
        this.fileMappingService = fileMappingService;
        this.fileOperationRecordService = fileOperationRecordService;
    }

    @PostMapping(FolderReportAction.STORAGE_UPLOAD_FILE)
    public Result<StorageFileUploadResponse> uploadFileToS3Backend(
            @RequestBody @Valid StorageFileUploadRequest request
    ) throws BusinessException {
        return Result.ok(storageService.uploadFileToS3(request));
    }



    private void syncSaveFileOperationRecord(String ipAddress, String userAgent, String openId, String fileCode, Integer type) {
        CompletableFuture.runAsync(() -> {
            FileOperationRecord fileOperationRecord = new FileOperationRecord();
            fileOperationRecord.setFileCode(fileCode);
            fileOperationRecord.setOpenId(openId);
            fileOperationRecord.setOperatedAt(System.currentTimeMillis());
            fileOperationRecord.setType(type);
            fileOperationRecord.setUserIp(ipAddress);
            fileOperationRecord.setUserAgent(userAgent);
            fileOperationRecordService.saveFileOperationRecord(fileOperationRecord);
        });
    }
}
