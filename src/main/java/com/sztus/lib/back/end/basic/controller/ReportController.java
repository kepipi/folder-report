package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.service.ReportBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.FolderReportAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:35
 */
@RestController
public class ReportController {

    @Autowired
    private ReportBusinessService reportBusinessService;

    @GetMapping(FolderReportAction.LIST_REPORT)
    public Result<List<Report>> listReport() {
        return Result.ok(reportBusinessService.listReport());
    }
}
