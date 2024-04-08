package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import com.sztus.lib.back.end.basic.object.request.StorageFileUploadRequest;
import com.sztus.lib.back.end.basic.service.ReportBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import com.sztus.lib.back.end.basic.type.enumerate.StorageError;
import com.sztus.lib.back.end.basic.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:35
 */
@RestController
public class ReportController {

    @Resource
    private ReportBusinessService reportBusinessService;

    @GetMapping(LocationReportAction.LIST_REPORT)
    public Result<List<Report>> listReport(@RequestParam("housePropertyId") Long housePropertyId) {
        return Result.ok(reportBusinessService.listReport(housePropertyId));
    }


    @PostMapping(LocationReportAction.NEW_REPORT)
    public Result<Void> newReport(@RequestParam("housePropertyId") Long housePropertyId) {
        reportBusinessService.newReport(housePropertyId);
        return Result.ok();
    }

    @GetMapping("/preview-report")
    public Result<List<FileItemDTO>> previewReport(@RequestParam Long reportId) {
        return Result.ok(reportBusinessService.previewReport(reportId));
    }

    @PostMapping(LocationReportAction.UPLOAD_REPORT)
    public Result<String> uploadReport(HttpServletRequest httpServletRequest,
                                       @RequestParam("reportId") Long reportId,
                                       @RequestParam("file") MultipartFile file,
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
            uploadFileRequest.setPath("/report/pdf");
            uploadFileRequest.setAcl(acl);
            uploadFileRequest.setContentType(contentType);
            uploadFileRequest.setDomainName(domainName);

            return Result.ok(reportBusinessService.uploadPdf(reportId, uploadFileRequest));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

}
