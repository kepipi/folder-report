package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sztus.lib.back.end.basic.dao.mapper.FileMapper;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Item;
import com.sztus.lib.back.end.basic.object.dto.FileItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Cooper
 * @date 2024/4/7 14:34
 */
@Service
public class FileService extends ServiceImpl<FileMapper, File> {

    public List<FileItemDTO> listFileItem(Long locationId) {
        return getBaseMapper().selectJoinList(FileItemDTO.class, new MPJLambdaWrapper<File>()
                .selectAs(File::getId, "fileId")
                .selectAs(File::getName, "fileName")
                .select(File::getUrl)
                .selectAs(Item::getId, "itemId")
                .select(Item::getItemName, Item::getQuantity, Item::getCleanliness, Item::getCondition, Item::getComments)
                .innerJoin(Item.class, Item::getFileId, File::getId)
                .eq(File::getLocationId, locationId));
    }

}
