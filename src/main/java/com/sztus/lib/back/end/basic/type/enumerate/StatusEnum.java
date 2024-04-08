package com.sztus.lib.back.end.basic.type.enumerate;

import com.sztus.lib.back.end.basic.object.base.BaseEnum;

/**
 * 状态枚举
 * State enumeration definition
 * Create an enumeration definition
 *
 * @author sheldon
 * @date 2024-04-07
 */
public enum StatusEnum implements BaseEnum {

    /**
     * The unknown
     */
    UNKNOWN(0, "Unknown"),

    /**
     * To enable the
     */
    ENABLE(1, "Enable"),

    /**
     * disable
     */
    DISABLE(-1, "Disable");

    StatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    private final Integer value;
    private final String text;

}
