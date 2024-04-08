package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.request.*;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.service.StorageService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.enumerate.StorageError;
import com.sztus.lib.back.end.basic.utils.ConvertUtil;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(LocationReportAction.UPLOAD_MULTIPART_FILE)
    public Result<StorageFileUploadResponse> uploadFileToS3Frontend(
            HttpServletRequest httpServletRequest,
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path,
            @RequestParam(value = "mode", required = false) Integer mode,
            @RequestParam(value = "acl", required = false) Integer acl,
            @RequestParam(value = "contentType", required = false) String contentType,
            @RequestParam(value = "domainName", required = false) String domainName
    ) {
        try {

            String fileBody = ConvertUtil.streamToString(file.getInputStream());

            if (StringUtils.isBlank(fileBody)) {
                return Result.fail(new BusinessException(StorageError.INVALID_REQUEST_PARAMETER));
            }

            StorageFileUploadRequest uploadFileRequest = new StorageFileUploadRequest();
            uploadFileRequest.setFileBody(fileBody);
            uploadFileRequest.setFileName(file.getOriginalFilename());
            uploadFileRequest.setMode(mode);
            uploadFileRequest.setPath(path);
            uploadFileRequest.setAcl(acl);
            uploadFileRequest.setContentType(contentType);
            uploadFileRequest.setDomainName(domainName);

            StorageFileUploadResponse storageFileUploadResponse = storageService.uploadFileToS3(uploadFileRequest);


            return Result.ok(storageFileUploadResponse);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


}
