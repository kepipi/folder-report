package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.request.BatchUploadFileUrlRequest;
import com.sztus.lib.back.end.basic.object.request.StorageFileUploadRequest;
import com.sztus.lib.back.end.basic.object.response.LocationItemResponse;
import com.sztus.lib.back.end.basic.service.FileBusinessService;
import com.sztus.lib.back.end.basic.service.LocationBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import com.sztus.lib.back.end.basic.type.enumerate.StorageError;
import com.sztus.lib.back.end.basic.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:35
 */
@RestController
public class LocationController {

    @Resource
    private LocationBusinessService locationBusinessService;

    @Resource
    private FileBusinessService fileBusinessService;

    @GetMapping(LocationReportAction.LIST_LOCATION)
    public Result<LocationItemResponse> listLocation(@RequestParam Long reportId) {
        return Result.ok(locationBusinessService.listLocation(reportId));
    }

    @GetMapping(LocationReportAction.LIST_FILE)
    public Result<List<File>> listFile(@RequestParam Long locationId) {
        return Result.ok(fileBusinessService.listFile(locationId));
    }

    @PostMapping(LocationReportAction.SAVE_LOCATION)
    public Result<String> saveLocation(@RequestBody Location location) {
        locationBusinessService.saveLocation(location);
        return Result.ok();
    }

    @PostMapping(LocationReportAction.UPLOAD_FILE)
    public Result<File> uploadFile(HttpServletRequest httpServletRequest,
                                   @RequestParam("locationId") Long locationId,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("path") String path,
                                   @RequestParam(value = "mode", required = false) Integer mode,
                                   @RequestParam(value = "acl", required = false) Integer acl,
                                   @RequestParam(value = "contentType", required = false) String contentType,
                                   @RequestParam(value = "domainName", required = false) String domainName) {
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

            return Result.ok(locationBusinessService.uploadFile(locationId, uploadFileRequest));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @DeleteMapping(LocationReportAction.DELETE_FILE)
    public Result<String> deleteFile(@RequestParam Long fileId) {
        fileBusinessService.deleteFile(fileId);
        return Result.ok();
    }


    @DeleteMapping(LocationReportAction.DELETE_LOCATION)
    public Result<String> deleteLocation(@RequestParam Long locationId) {
        locationBusinessService.deleteLocation(locationId);
        return Result.ok();
    }

    @GetMapping(LocationReportAction.AI_ANALYSE)
    public SseEmitter aiAnalyse(@RequestParam Long locationId, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Type", "text/event-stream");
        response.setHeader("Connection", "keep-alive");
        return fileBusinessService.aiAnalyse(locationId);
    }
}
