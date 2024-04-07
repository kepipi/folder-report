package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.ReportMapper;
import com.sztus.lib.back.end.basic.object.domain.Report;
import org.springframework.stereotype.Service;

import java.util.List;


import com.sztus.lib.back.end.basic.dao.mapper.FileMapper;

@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> {

    public List<Report> getbyId(Long id) {
        return list(Wrappers.<Report>lambdaQuery().eq(Report::getId, id));
    }
}
