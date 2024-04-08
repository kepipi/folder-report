package com.sztus.lib.back.end.basic.object.annotation;


import java.lang.annotation.*;

/**
 * @author: Max.
 * @date: 2023/3/22
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DsReader {
}
