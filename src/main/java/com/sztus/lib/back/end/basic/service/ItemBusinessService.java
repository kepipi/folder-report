package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.dao.service.LocationService;
import com.sztus.lib.back.end.basic.dao.service.ReportService;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.domain.Location;
import com.sztus.lib.back.end.basic.object.domain.Report;
import com.sztus.lib.back.end.basic.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class ItemBusinessService {

    @Resource
    private ItemService itemService;

    public void deleteItem(Long id) {
        itemService.removeById(id);
    }


    public void saveItem(Item item) {
        itemService.saveOrUpdate(item);
    }
}
