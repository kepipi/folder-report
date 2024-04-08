package com.sztus.lib.back.end.basic.dao.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.StorageProviderMapper;
import com.sztus.lib.back.end.basic.object.domain.StorageProvider;
import com.sztus.lib.back.end.basic.type.enumerate.StatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-05-10
 */
@Service
public class StorageProviderService extends ServiceImpl<StorageProviderMapper, StorageProvider> {

    public List<StorageProvider> getProviderListByStatus(Integer status) {
        return list(new LambdaQueryWrapper<StorageProvider>().eq(StorageProvider::getStatus, status));
    }

    public StorageProvider getProviderByName(String providerName) {
        return getOne(
                new LambdaQueryWrapper<StorageProvider>()
                        .eq(StorageProvider::getProviderName, providerName)
                        .eq(StorageProvider::getStatus, StatusEnum.ENABLE.getValue())
        );
    }
}
