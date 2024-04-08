package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.LocationService;
import com.sztus.lib.back.end.basic.object.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class LocationBusinessService {

    @Autowired
    private LocationService locationService;

    
    public List<Location> listLocation(Long reportId) {
        return locationService.list(new LambdaQueryWrapper<Location>().eq(Location::getReportId, reportId));
    }

    public void saveLocation(Location location) {
        locationService.saveOrUpdate(location);
    }
}
