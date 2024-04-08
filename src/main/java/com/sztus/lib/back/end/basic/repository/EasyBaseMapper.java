package com.sztus.lib.back.end.basic.repository;


import com.github.yulichang.base.MPJBaseMapper;

import java.util.Collection;

public interface EasyBaseMapper<T> extends MPJBaseMapper<T> {
    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);
}
