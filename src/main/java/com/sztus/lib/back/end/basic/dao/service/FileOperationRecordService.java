package com.sztus.lib.back.end.basic.dao.service;


import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.FileOperationRecordMapper;
import com.sztus.lib.back.end.basic.object.annotation.DsWriter;
import com.sztus.lib.back.end.basic.object.domain.FileOperationRecord;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-05-10
 */
@Service
public class FileOperationRecordService extends ServiceImpl<FileOperationRecordMapper, FileOperationRecord> {
    @DsWriter
    @DSTransactional
    public void saveFileOperationRecord(FileOperationRecord fileOperationRecord) {
        saveOrUpdate(fileOperationRecord);
    }
}
