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
import com.sztus.lib.back.end.basic.object.response.ChecklistItem;
import com.sztus.lib.back.end.basic.object.response.PreviewReportResponse;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.type.enumerate.ErrorCode;
import com.sztus.lib.back.end.basic.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private FileService fileService;

    @Resource
    private ItemService itemService;

    @Resource
    private StorageService storageService;

    public List<Report> listReport(Long housePropertyId) {
        List<Report> reportList = reportService.list(Wrappers.<Report>lambdaQuery().eq(Report::getHousePropertyId, housePropertyId).orderByDesc(Report::getId));
        if (CollectionUtils.isEmpty(reportList)) {
            return Collections.emptyList();
        }
        return reportList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void newReport(Long housePropertyId) {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        List<Report> list = reportService.list();
        Report report;
        if (CollectionUtils.isEmpty(list)) {
            report = new Report(null, housePropertyId, "Report-1", null, currentTimestamp);
        } else {
            List<String> nameList = list.stream().map(Report::getReportName).sorted().collect(Collectors.toList());
            String lastName = nameList.get(nameList.size() - 1);
            String[] nameSplit = lastName.split("-");
            report = new Report(null, housePropertyId, "Report" + "-" + (Integer.parseInt(nameSplit[1]) + 1), null, currentTimestamp);
        }
        reportService.save(report);

        List<Location> saveLocationList = new ArrayList<>();
        saveLocationList.add(new Location(null, report.getId(), "Bedroom", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Living room", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Storage room", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Kitchen", null, currentTimestamp));
        saveLocationList.add(new Location(null, report.getId(), "Toilet", null, currentTimestamp));

        locationService.saveBatch(saveLocationList);

    }

    public void deleteReport(Long reportId) {
        reportService.removeById(reportId);
        List<Location> locationList = locationService.list(new LambdaQueryWrapper<Location>().eq(Location::getReportId, reportId));
        if (!CollectionUtils.isEmpty(locationList)) {
            locationService.removeBatchByIds(locationList.stream().map(Location::getId).collect(Collectors.toList()));
            List<File> fileList = fileService.list(new LambdaQueryWrapper<File>().in(File::getLocationId, locationList.stream().map(Location::getId).collect(Collectors.toList())));

            if (!CollectionUtils.isEmpty(fileList)) {
                fileService.removeBatchByIds(fileList.stream().map(File::getId).collect(Collectors.toList()));
                List<Item> itemList = itemService.list(new LambdaQueryWrapper<Item>().in(Item::getFileId, fileList.stream().map(File::getId).collect(Collectors.toList())));

                if (!CollectionUtils.isEmpty(itemList)) {
                    itemService.removeBatchByIds(itemList.stream().map(Item::getId).collect(Collectors.toList()));
                }
            }
        }
    }

    public List<PreviewReportResponse> previewReport(Long reportId) {
        List<FileItemDTO> fileItemDTOS = reportService.listFileItem(reportId);
        if (CollectionUtils.isEmpty(fileItemDTOS)) {
            return Collections.emptyList();
        }

        List<PreviewReportResponse> previewReportResponseList = new ArrayList<>();
        Map<String, List<FileItemDTO>> locationNameMap = fileItemDTOS.stream().collect(Collectors.groupingBy(FileItemDTO::getLocationName));
        locationNameMap.forEach((locationName, fileItemDTOList) -> {
            Map<String, List<FileItemDTO>> urlMap = fileItemDTOList.stream().collect(Collectors.groupingBy(FileItemDTO::getUrl));
            List<ChecklistItem> checklistItems = new ArrayList<>();
            urlMap.forEach((url, fileItems) -> {
                checklistItems.add(ChecklistItem.builder()
                        .url(url)
                        .fileName(fileItems.get(0).getFileName())
                        .fileItems(fileItems)
                        .build());
            });
            previewReportResponseList.add(PreviewReportResponse.builder()
                    .location(locationName)
                    .checklistItems(checklistItems)
                    .build());
        });

        return previewReportResponseList;
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
