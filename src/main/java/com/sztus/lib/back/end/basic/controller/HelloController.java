package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.dao.service.ReportService;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.type.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author AustinWang
 */
@RestController
public class HelloController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/hello")
    public Result<List<Report>> hello() {
        return Result.ok(reportService.getbyId(2L));
    }

    @GetMapping("/hello2")
    public Result<String> hello2() {
        return Result.ok("hello2");
    }
}
