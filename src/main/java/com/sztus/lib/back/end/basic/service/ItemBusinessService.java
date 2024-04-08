package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.ItemService;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import com.sztus.lib.back.end.basic.object.response.FileItemResponse;
import com.sztus.lib.back.end.basic.object.view.FileView;
import com.sztus.lib.back.end.basic.object.view.ItemView;
import com.sztus.lib.back.end.basic.type.enumerate.CleanlinessEnum;
import com.sztus.lib.back.end.basic.type.enumerate.ConditionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leon
 */
@Service
@RequiredArgsConstructor
public class ItemBusinessService {

    private final FileService fileService;

    private final ItemService itemService;

    public FileItemResponse listFileItem(Long locationId) {
        List<FileView> fileList = new ArrayList<>();
        List<ItemView> itemList = new ArrayList<>();
        List<FileItemDTO> fileItemDTOS = fileService.listFileItem(locationId);

        if (CollectionUtils.isNotEmpty(fileItemDTOS)) {
            Map<Long, List<FileItemDTO>> fileIdAndItemsMap = fileItemDTOS.stream().collect(Collectors.groupingBy(FileItemDTO::getFileId));
            fileIdAndItemsMap.forEach((fileId, items) -> {
                fileList.add(FileView.builder()
                        .fileId(fileId)
                        .fileName(items.get(0).getFileName())
                        .url(items.get(0).getUrl())
                        .build());

                itemList.addAll(items.stream().map(item -> ItemView.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .cleanliness(CleanlinessEnum.getTextByValue(item.getCleanliness()))
                        .condition(ConditionEnum.getTextByValue(item.getCondition()))
                        .comments(item.getComments())
                        .build()).collect(Collectors.toList()));
            });
        }

        return FileItemResponse.builder()
                .fileList(fileList)
                .itemList(itemList)
                .build();
    }

    public void deleteItem(Long id) {
        itemService.removeById(id);
    }


    public void saveItem(Item item) {
        itemService.saveOrUpdate(item);
    }
}
