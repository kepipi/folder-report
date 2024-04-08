package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sztus.lib.back.end.basic.dao.mapper.ReportMapper;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;


import com.sztus.lib.back.end.basic.dao.mapper.FileMapper;

@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> {

    public List<Report> getbyId(Long id) {
        return list(Wrappers.<Report>lambdaQuery().eq(Report::getId, id));
    }

    public List<FileItemDTO> listFileItem(Long reportId) {
        return getBaseMapper().selectJoinList(FileItemDTO.class, new MPJLambdaWrapper<Report>()
                .select(File::getUrl)
                .select(Item::getItemName, Item::getQuantity, Item::getCleanliness, Item::getCondition, Item::getComments)
                .innerJoin(Location.class, Location::getReportId, Report::getId)
                .innerJoin(File.class, File::getLocationId, Location::getId)
                .innerJoin(Item.class, Item::getFileId, File::getId)
                .eq(Report::getId, reportId));
    }

}
