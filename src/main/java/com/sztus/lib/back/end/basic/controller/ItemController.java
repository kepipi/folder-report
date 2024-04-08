package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.response.FileItemResponse;
import com.sztus.lib.back.end.basic.service.ItemBusinessService;
import com.sztus.lib.back.end.basic.type.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Austin
 * @date: 2024/4/8 14:27
 */
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemBusinessService itemBusinessService;

    @GetMapping("/list-file-item")
    public Result<FileItemResponse> listFileItem(@RequestParam Long locationId) {
        return Result.ok(itemBusinessService.listFileItem(locationId));
    }
    
}
