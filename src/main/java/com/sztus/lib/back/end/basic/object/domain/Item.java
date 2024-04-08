package com.sztus.lib.back.end.basic.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Austin
 * @date: 2024/4/3 14:45
 */
@Data
@TableName("item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long fileId;

    private String itemName;

    private String quantity;

    private String cleanliness;

    @TableField(value = "`condition`")
    private String condition;

    private String comments;

    private String description;

}
