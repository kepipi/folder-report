package com.sztus.lib.back.end.basic.object.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Austin
 * @date: 2024/4/3 14:45
 */
@Data
@TableName("permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String sn;

}
