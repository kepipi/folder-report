package com.sztus.lib.back.end.basic.type.enumerate;

import com.google.common.collect.Maps;
import com.sztus.lib.back.end.basic.object.base.BaseEnum;

import java.util.Map;
import java.util.Objects;

/**
 * @author daniel
 * @date 2020/11/28
 */
public enum ConnectionTypeEnum implements BaseEnum {

    /**
     * 错误枚举定义
     */
    UNKNOWN("UNKNOWN", -1),

    /**
     * aws s3 连接方式
     */
    S3("S3", 10001),

    /**
     * sftp 连接方式
     */
    SFTP("SFTP", 10002),

    /**
     * ftp 连接方式
     */
    FTP("FTP", 10003),

    /**
     * oss 连接方式
     */
    OSS("OSS", 10004);

    ConnectionTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    private final String text;
    private final Integer value;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    private static final Map<Integer, ConnectionTypeEnum> valueToEnumMap = Maps.newHashMapWithExpectedSize(ConnectionTypeEnum.values().length);

    static {
        for (ConnectionTypeEnum e : ConnectionTypeEnum.values()) {
            valueToEnumMap.put(e.getValue(), e);
        }
    }

    public static ConnectionTypeEnum getByValue(int i) {
        ConnectionTypeEnum e = valueToEnumMap.get(i);
        return Objects.isNull(e) ? UNKNOWN : e;
    }

}