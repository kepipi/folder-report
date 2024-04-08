package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.dao.service.LocationService;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.object.response.ItemResponse;
import com.sztus.lib.back.end.basic.object.response.LocationItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class LocationBusinessService {

    @Resource
    private LocationService locationService;
    @Resource
    private FileService fileService;
    @Resource
    private ItemService itemService;

    
    public LocationItemResponse listLocation(Long reportId) {
        LocationItemResponse locationItemResponse = new LocationItemResponse();
        List<Location> locationList = locationService.list(Wrappers.<Location>lambdaQuery().eq(Location::getReportId, reportId));
        locationItemResponse.setLocationList(locationList);
        //根据location获取file
        List<File> fileList = fileService.list(Wrappers.<File>lambdaQuery()
                .in(File::getLocationId, locationList.stream().map(Location::getId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(fileList)) {
            return locationItemResponse;
        }
        //根据file获取item
        List<Item> itemList = itemService.list(Wrappers.<Item>lambdaQuery()
                .in(Item::getFileId, fileList.stream().map(File::getId).collect(Collectors.toList())));
        //用于组装数据返回前端
        Map<Long, Location> locationMap = locationList.stream().collect(Collectors.toMap(Location::getId, location -> location));
        Map<Long, List<File>> fileLocationMap = fileList.stream().collect(Collectors.groupingBy(File::getLocationId));
        Map<Long, List<Item>> itemMap = itemList.stream().collect(Collectors.groupingBy(Item::getFileId));

        List<ItemResponse> itemResponses = new ArrayList<>();
        //将所有的file根据location分组，location下的所有file对应的所有item进行返回
        fileLocationMap.forEach((locationId, files) -> {
            ItemResponse itemResponse = new ItemResponse();
            Location location = locationMap.get(locationId);
            itemResponse.setLocationName(location.getName());
            List<Item> itemResponseList = new ArrayList<>();
            for (File file : files) {
                List<Item> items = itemMap.get(file.getId());
                itemResponseList.addAll(items);
            }
            itemResponse.setItemList(itemResponseList);
            itemResponses.add(itemResponse);
        });
        locationItemResponse.setItemResponseList(itemResponses);
        return locationItemResponse;
    }

    public void saveLocation(Location location) {
        locationService.saveOrUpdate(location);
    }
}
