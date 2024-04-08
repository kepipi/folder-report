package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.request.BatchUploadFileUrlRequest;
import com.sztus.lib.back.end.basic.service.ItemBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.LocationReportAction;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Austin
 * @date: 2024/4/8 14:27
 */
@RestController
public class ItemController {

    @Resource
    private ItemBusinessService itemBusinessService;

    @DeleteMapping(LocationReportAction.DELETE_ITEM)
    public Result<String> deleteItem(@RequestParam Long id) {
        itemBusinessService.deleteItem(id);
        return Result.ok();
    }

    @PostMapping(LocationReportAction.SAVE_ITEM)
    public Result<String> saveItem(@RequestBody Item item) {
        itemBusinessService.saveItem(item);
        return Result.ok();
    }

}
