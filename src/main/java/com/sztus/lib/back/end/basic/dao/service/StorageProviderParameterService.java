package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.StorageProviderParameterMapper;
import com.sztus.lib.back.end.basic.object.domain.StorageProviderParameter;
import com.sztus.lib.back.end.basic.type.constant.GlobalConst;
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
public class StorageProviderParameterService extends ServiceImpl<StorageProviderParameterMapper, StorageProviderParameter> {

    public List<StorageProviderParameter> getProviderParameterList(List<Long> storageProviderIdList) {
        return list(
                new LambdaQueryWrapper<StorageProviderParameter>()
                        .in(StorageProviderParameter::getProviderId, storageProviderIdList)
                        .eq(StorageProviderParameter::getCompanyId, GlobalConst.INT_ONE)
                        .eq(StorageProviderParameter::getSystemId, GlobalConst.INT_ONE)
        );
    }
}
