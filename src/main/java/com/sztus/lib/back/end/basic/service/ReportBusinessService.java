package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.dao.service.LocationService;
import com.sztus.lib.back.end.basic.dao.service.ReportService;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import com.sztus.lib.back.end.basic.object.request.StorageFileUploadRequest;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.type.enumerate.CleanlinessEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ConditionEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ErrorCode;
import com.sztus.lib.back.end.basic.type.enumerate.StorageError;
import com.sztus.lib.back.end.basic.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class ReportBusinessService {

    @Resource
    private ReportService reportService;
    @Resource
    private LocationService locationService;

    @Resource
    private StorageService storageService;

    public List<Report> listReport(Long housePropertyId) {
        List<Report> reportList = reportService.list(Wrappers.<Report>lambdaQuery().eq(Report::getHousePropertyId, housePropertyId));
        if (CollectionUtils.isEmpty(reportList)) {
            return Collections.emptyList();
        }
        return reportList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void newReport(Long housePropertyId) {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();

        Report report = new Report(null, housePropertyId, null, currentTimestamp);
        reportService.save(report);

        List<Location> saveLocationList = new ArrayList<>();
        saveLocationList.add(new Location(null, report.getId(), "Bedroom", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Living room", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Storage room", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Kitchen", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Toilet", null, currentTimestamp));

        locationService.saveBatch(saveLocationList);

    }

    public List<FileItemDTO> previewReport(Long reportId) {
        List<FileItemDTO> fileItemDTOS = reportService.listFileItem(reportId);
        if (CollectionUtils.isEmpty(fileItemDTOS)) {
            return Collections.emptyList();
        }

        for (FileItemDTO fileItemDTO : fileItemDTOS) {
            fileItemDTO.setCleanliness(CleanlinessEnum.getTextByValue(fileItemDTO.getCleanliness()));
            fileItemDTO.setCondition(ConditionEnum.getTextByValue(fileItemDTO.getCondition()));
        }

        return fileItemDTOS;
    }


    public String uploadPdf(Long reportId, StorageFileUploadRequest storageFileUploadRequest) throws BusinessException {
        StorageFileUploadResponse storageFileUploadResponse = storageService.uploadFileToS3(storageFileUploadRequest);
        Report reports = reportService.getById(reportId);
        if (!ObjectUtils.isEmpty(reports)) {
            reports.setDownloadUrl(storageFileUploadResponse.getFileUrl());
            reportService.saveOrUpdate(reports);
        } else {
            throw new BusinessException(ErrorCode.ABNORMAL_PARAMETER);
        }
        return storageFileUploadResponse.getFileUrl();
    }
}
