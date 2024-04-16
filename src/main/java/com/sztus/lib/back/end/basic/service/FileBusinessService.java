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

    public SseEmitter aiAnalyse(Long locationId) throws IOException {
        SseEmitter emitter = new SseEmitter();
        // 已经生成过的file不用再次生成
        List<File> fileList = fileService.list(new LambdaQueryWrapper<File>().eq(File::getLocationId, locationId));
        List<Item> items = itemService.list(new LambdaQueryWrapper<Item>().in(Item::getFileId, fileList.stream().map(File::getId).collect(Collectors.toList())));
        Map<Long, List<Item>> itemMap = items.stream().collect(Collectors.groupingBy(Item::getFileId));
        List<File> noAnalyseFile = fileList.stream().filter(t -> !itemMap.containsKey(t.getId())).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(items)) {
            emitter.send(items);
        }

        // 都生成过就直接返回
        if (CollectionUtils.isEmpty(noAnalyseFile)) {
            emitter.complete();
            return emitter;
        }
        for (int i = 0; i < noAnalyseFile.size(); i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                JSONObject data = new JSONObject();
                data.put(JsonKey.FILE_URL, noAnalyseFile.get(finalI).getUrl());
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
                            List<Item> itemList = new ArrayList<>();
                            for (int j = 0; j < itemJsonList.size(); j++) {
                                JSONObject itemJson = itemJsonList.getJSONObject(j);
                                Item item = new Item();
                                item.setFileId(noAnalyseFile.get(finalI).getId());
                                item.setItemName(itemJson.getString("ItemName"));
                                item.setComments(itemJson.getString("Suggested"));
                                item.setQuantity(itemJson.getString("Quantity"));
                                item.setDescription(itemJson.getString("Description"));
                                item.setCondition(ConditionEnum.getTextByValue(itemJson.getString("Condition")));
                                item.setCleanliness(CleanlinessEnum.getTextByValue(itemJson.getString("Cleanliness")));
                                itemList.add(item);
                            }
                            itemService.saveBatch(itemList);
                            System.out.println("发送数据" + itemList);
                            emitter.send(itemList);
                        }
                        // 表示 JSON 解析成功
                        parsingSuccessful = true;
                    } catch (Exception e) {
                        // JSON 解析失败，进行重试
                        retryCount++;
                        log.error("JSON 解析失败，进行重试 times：{}, JSON: {}", retryCount, noAnalyseFile.get(finalI).getUrl() + responseBody, e);
                        if (retryCount == maxRetries) {
                            emitter.completeWithError(e);
                            return;
                        }
                    }
                }
                System.out.println("发送成功 ：" + noAnalyseFile.get(finalI).getUrl());
                if (finalI == noAnalyseFile.size() - 1) {
                    try {
                        emitter.send("");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    emitter.complete(); // 在最后一次迭代时断开链接
                    System.out.println("关闭sse");
                }
            });
        }
        return emitter;
    }

}
