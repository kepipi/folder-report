package com.sztus.lib.back.end.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.request.BatchUploadFileUrlRequest;
import com.sztus.lib.back.end.basic.type.constant.JsonKey;
import com.sztus.lib.back.end.basic.type.enumerate.CleanlinessEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ConditionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
@Slf4j
public class FileBusinessService {


    @Resource
    private FileService fileService;

    @Resource
    private ResetTemplateService resetTemplateService;

    @Resource
    private ItemService itemService;


    private static final String AI_URL = "http://ec2-13-42-55-109.eu-west-2.compute.amazonaws.com:8080/imageDescription";

    public List<File> listFile(Long locationId) {
        return fileService.list(new LambdaQueryWrapper<File>().eq(File::getLocationId, locationId));
    }

    public void deleteFile(Long fileId) {
        fileService.removeById(fileId);
    }

    public List<Item> aiAnalyse(List<BatchUploadFileUrlRequest> requestList) {
        List<Item> itemList = new ArrayList<>();
        List<Long> fileIdList = new ArrayList<>();
        for (BatchUploadFileUrlRequest request : requestList) {
            fileIdList.add(request.getFileId());
            JSONObject data = new JSONObject();
            data.put(JsonKey.FILE_URL, request.getUrl());
            String responseBody = resetTemplateService.doPostByRequestBody(AI_URL, data.toJSONString());
            JSONObject responseJson = JSONObject.parseObject(responseBody);
            String description = responseJson.getString("Description");
            JSONArray itemJsonList = null;
            try {
                itemJsonList = JSON.parseArray(description);
            } catch (Exception e) {
                log.warn("json格式异常 json：{}", request.getUrl() + responseBody);
                description = "[" + description + "]";
                itemJsonList = JSON.parseArray(description);
            }
            if (!itemJsonList.isEmpty()) {
                for (int i = 0; i < itemJsonList.size(); i++) {
                    JSONObject itemJson = itemJsonList.getJSONObject(i);
                    Item item = new Item();
                    item.setFileId(request.getFileId());
                    item.setItemName(itemJson.getString("ItemName"));
                    item.setComments(itemJson.getString("Suggested"));
                    item.setQuantity(itemJson.getString("Quantity"));
                    item.setDescription(itemJson.getString("Description"));
                    item.setCondition(ConditionEnum.getTextByValue(itemJson.getString("Condition")));
                    item.setCleanliness(CleanlinessEnum.getTextByValue(itemJson.getString("Cleanliness")));
                    itemList.add(item);
                }
            }
        }
        if (!CollectionUtils.isEmpty(fileIdList)) {
            itemService.remove(new LambdaQueryWrapper<Item>().in(Item::getFileId, fileIdList));
        }

        if (!CollectionUtils.isEmpty(itemList)) {
            itemService.saveBatch(itemList);
        }
        return itemList;
    }
}
