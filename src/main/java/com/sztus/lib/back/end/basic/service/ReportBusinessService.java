package com.sztus.lib.back.end.basic.service;

import com.sztus.lib.back.end.basic.dao.service.ReportService;
import com.sztus.lib.back.end.basic.object.domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class ReportBusinessService {

    @Autowired
    private ReportService reportService;

    public List<Report> listReport() {
        return reportService.list();
    }
}
