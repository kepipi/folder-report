package com.sztus.lib.back.end.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.request.BatchUploadFileUrlRequest;
import com.sztus.lib.back.end.basic.type.constant.JsonKey;
import com.sztus.lib.back.end.basic.type.enumerate.CleanlinessEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ConditionEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    public List<Item> aiAnalyse(Long locationId) throws BusinessException {
        // 已经生成过的file不用再次生成
        List<File> fileList = fileService.list(new LambdaQueryWrapper<File>().eq(File::getLocationId, locationId));
        List<Item> items = itemService.list(new LambdaQueryWrapper<Item>().in(Item::getFileId, fileList.stream().map(File::getId).collect(Collectors.toList())));
        Map<Long, List<Item>> itemMap = items.stream().collect(Collectors.groupingBy(Item::getFileId));
        List<File> noAnalyseFile = fileList.stream().filter(t -> !itemMap.containsKey(t.getId())).collect(Collectors.toList());

        // 都生成过就直接返回
        if (CollectionUtils.isEmpty(noAnalyseFile)) {
            return items;
        }

        List<Item> itemList = new ArrayList<>();
        for (File request : noAnalyseFile) {
            JSONObject data = new JSONObject();
            data.put(JsonKey.FILE_URL, request.getUrl());
            String responseBody = null;
            boolean parsingSuccessful = false;
            int retryCount = 0;
            // 设置最大重试次数
            int maxRetries = 5;
            while (!parsingSuccessful && retryCount < maxRetries) {
                try {
                    responseBody = resetTemplateService.doPostByRequestBody(AI_URL, data.toJSONString());
                    JSONObject responseJson = JSONObject.parseObject(responseBody);
                    String description = responseJson.getString("Description");
                    JSONArray itemJsonList = JSON.parseArray(description);
                    if (!itemJsonList.isEmpty()) {
                        for (int i = 0; i < itemJsonList.size(); i++) {
                            JSONObject itemJson = itemJsonList.getJSONObject(i);
                            Item item = new Item();
                            item.setFileId(request.getId());
                            item.setItemName(itemJson.getString("ItemName"));
                            item.setComments(itemJson.getString("Suggested"));
                            item.setQuantity(itemJson.getString("Quantity"));
                            item.setDescription(itemJson.getString("Description"));
                            item.setCondition(ConditionEnum.getTextByValue(itemJson.getString("Condition")));
                            item.setCleanliness(CleanlinessEnum.getTextByValue(itemJson.getString("Cleanliness")));
                            itemList.add(item);
                        }
                    }
                    // 表示 JSON 解析成功
                    parsingSuccessful = true;
                } catch (Exception e) {
                    // JSON 解析失败，进行重试
                    retryCount++;
                    log.error("JSON 解析失败，进行重试 times：{}, JSON: {}", retryCount, request.getUrl() + responseBody);
                    if (retryCount == maxRetries) {
                        throw new BusinessException(ErrorCode.IMAGE_RECOGNITION_FAILED);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(itemList)) {
            itemService.saveBatch(itemList);
            items.addAll(itemList);
        }
        return items;
    }
}
