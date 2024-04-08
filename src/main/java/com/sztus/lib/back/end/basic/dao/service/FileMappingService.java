package com.sztus.lib.back.end.basic.dao.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.FileMappingMapper;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.annotation.DsWriter;
import com.sztus.lib.back.end.basic.object.domain.FileMapping;
import com.sztus.lib.back.end.basic.type.enumerate.StorageError;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-05-10
 */
@Service
public class FileMappingService extends ServiceImpl<FileMappingMapper, FileMapping> {
    public FileMapping getFileMappingByCode(String fileCode) throws BusinessException {

        return Optional.ofNullable(getOne(new LambdaQueryWrapper<FileMapping>().eq(FileMapping::getFileCode, fileCode)))
                .orElseThrow(() -> new BusinessException(StorageError.FILE_MAPPING_IS_NOT_EXIST));
    }

    @DsWriter
    public void saveFileMapping(FileMapping fileMapping) {
        saveOrUpdate(fileMapping);
    }

    @DsWriter
    public void deleteByFileCode(String fileCode) {
        remove(new LambdaQueryWrapper<FileMapping>().eq(FileMapping::getFileCode, fileCode));
    }

    public FileMapping getFileMappingByFileNameAndPath(String fileName, String relativePath) {
        return getOne(new LambdaQueryWrapper<FileMapping>().eq(FileMapping::getFileName, fileName).eq(FileMapping::getRelativePath, relativePath));
    }
}
