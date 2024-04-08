package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.service.ReportBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public Result<List<Report>> listReport(@RequestParam("house_property_id") Long housePropertyId) {
        return Result.ok(reportBusinessService.listReport(housePropertyId));
    }


    @PostMapping(LocationReportAction.NEW_REPORT)
    public Result<Void> newReport(@RequestParam("house_property_id") Long housePropertyId) {
        reportBusinessService.newReport(housePropertyId);
        return Result.ok();
    }


}
