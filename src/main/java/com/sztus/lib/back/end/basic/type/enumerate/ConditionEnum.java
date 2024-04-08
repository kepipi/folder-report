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
public enum ConditionEnum {

    NEW("0", "New"),


    GOOD("1", "Good"),


    FAIR("2", "Fair"),

    POOR("3", "Poor"),

    VERY_POOR("4", "Very Poor");;

    ConditionEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(String value) {
        for (ConditionEnum taskTypeEnum : ConditionEnum.values()) {
            if (taskTypeEnum.value.equals(value)) {
                return taskTypeEnum.getText();
            }
        }
        return "";
    }

    public String getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    private final String value;
    private final String text;

}
