package com.sztus.lib.back.end.basic.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Austin
 * @date: 2024/4/3 14:45
 */
@Data
@TableName("folder")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long housePropertyId;

    private String name;

    private String description;

    private Long createdAt;

    private Long updatedAt;

}
