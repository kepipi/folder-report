package com.sztus.lib.back.end.basic.type.enumerate;

import com.sztus.lib.back.end.basic.object.base.BaseEnum;

/**
 * @author Max
 * @date 2023/05/10
 */
public enum StorageModeEnum implements BaseEnum {
    //StorageModeEnum
    CREATE("CREATE", 95041001),
    REPLACE("REPLACE", 95041002),
    ;

    StorageModeEnum(String text, Integer value) {
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
