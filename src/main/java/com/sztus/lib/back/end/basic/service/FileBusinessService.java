package com.sztus.lib.back.end.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.type.constant.JsonKey;
import com.sztus.lib.back.end.basic.type.enumerate.CleanlinessEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ConditionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
@Slf4j
public class FileBusinessService {

    @Autowired
    @Qualifier("aiAnalyseThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

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

    public SseEmitter aiAnalyse(Long locationId) {
        SseEmitter emitter = new SseEmitter();
        threadPoolExecutor.execute(() -> {
            try {
                List<File> fileList = fileService.list(new LambdaQueryWrapper<File>().eq(File::getLocationId, locationId));
                if (CollectionUtils.isEmpty(fileList)) {
                    emitter.complete();
                    return;
                }
                Map<Long, List<Item>> itemMap = itemService.list(new LambdaQueryWrapper<Item>()
                                .in(Item::getFileId, fileList.stream().map(File::getId).collect(Collectors.toList())))
                        .stream().collect(Collectors.groupingBy(Item::getFileId));
                fileList.stream().filter(file -> !itemMap.containsKey(file.getId()))
                        .forEach(file -> processFile(file, emitter));
            } catch (Exception e) {
                emitter.completeWithError(e);
                log.error("Error occurred during AI analysis: {}", e.getMessage());
            }
        });
        return emitter;
    }

    private void processFile(File file, SseEmitter emitter) {
        JSONObject data = new JSONObject();
        data.put(JsonKey.FILE_URL, file.getUrl());
        String responseBody;
        boolean parsingSuccessful = false;
        int retryCount = 0;
        int maxRetries = 5; // 设置最大重试次数
        while (!parsingSuccessful && retryCount < maxRetries) {
            try {
                responseBody = resetTemplateService.doPostByRequestBody(AI_URL, data.toJSONString());
                JSONArray itemJsonList = JSONObject.parseObject(responseBody).getJSONArray("Description");
                List<Item> itemList = itemJsonList.stream()
                        .map(itemJson -> extractItem(file.getId(), (JSONObject) itemJson))
                        .collect(Collectors.toList());
                itemService.saveBatch(itemList);
                emitter.send(itemList);
                log.info("Processing completed for file: {}", file.getUrl());
                parsingSuccessful = true; // 表示解析成功，跳出重试循环
            } catch (Exception e) {
                log.error("Error occurred during processing file: {}, retrying... Attempt: {}", file.getUrl(), retryCount + 1, e);
                retryCount++; // 增加重试次数
                if (retryCount == maxRetries) {
                    emitter.completeWithError(e); // 达到最大重试次数后完成 SseEmitter 并报错
                }
            }
        }
    }

    private Item extractItem(Long fileId, JSONObject itemJson) {
        Item item = new Item();
        item.setFileId(fileId);
        item.setItemName(itemJson.getString("ItemName"));
        item.setComments(itemJson.getString("Suggested"));
        item.setQuantity(itemJson.getString("Quantity"));
        item.setDescription(itemJson.getString("Description"));
        item.setCondition(ConditionEnum.getTextByValue(itemJson.getString("Condition")));
        item.setCleanliness(CleanlinessEnum.getTextByValue(itemJson.getString("Cleanliness")));
        return item;
    }

}
