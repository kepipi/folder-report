package com.sztus.lib.back.end.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.dao.service.FolderService;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.object.domain.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class FolderBusinessService {

    @Autowired
    private FolderService folderService;

    @Autowired
    private FileService fileService;

    public List<Folder> listFolder(Long housePropertyId) {
        return folderService.list(new LambdaQueryWrapper<Folder>().eq(Folder::getHousePropertyId, housePropertyId));
    }

    public List<File> listFile(Long folderId) {
        return fileService.list(new LambdaQueryWrapper<File>().eq(File::getFolderId, folderId));
    }
}
