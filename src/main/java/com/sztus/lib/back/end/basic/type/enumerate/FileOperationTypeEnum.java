package com.sztus.lib.back.end.basic.type.enumerate;


import com.sztus.lib.back.end.basic.object.base.BaseEnum;

/**
 * @author Max
 * @date 2023/05/10
 */
public enum FileOperationTypeEnum implements BaseEnum {
    //FileOperationTypeEnum
    UPLOAD("UPLOAD", 95042001),
    DOWNLOAD("DOWNLOAD", 95042002),
    DELETE("DELETE", 95042003),
    PREVIEW("PREVIEW", 95042004),
    ;

    FileOperationTypeEnum(String text, Integer value) {
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
