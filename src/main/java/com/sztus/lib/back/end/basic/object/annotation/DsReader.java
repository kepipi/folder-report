package com.sztus.lib.back.end.basic.object.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * @author: Max.
 * @date: 2023/3/22
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("reader")
public @interface DsReader {
}
