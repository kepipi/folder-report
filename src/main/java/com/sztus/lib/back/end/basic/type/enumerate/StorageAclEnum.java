package com.sztus.lib.back.end.basic.type.enumerate;

import com.sztus.lib.back.end.basic.object.base.BaseEnum;
import com.sztus.lib.back.end.basic.object.base.BaseError;

/**
 * @author Max
 * @date 2023/05/10
 */
public enum StorageAclEnum implements BaseEnum {
    //StorageAclEnum
    PRIVATE("PRIVATE", 95040001),
    PUBLIC("PUBLIC", 95040002),
    PUBLIC_NOT_CROSS_DOMAIN("PUBLIC_NOT_CROSS_DOMAIN", 95040003),
    ;

    StorageAclEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private Integer value;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
