package com.sztus.lib.back.end.basic.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file_mapping")
@AllArgsConstructor
@NoArgsConstructor
public class FileMapping implements Serializable {

    private static final long serialVersionUID = 4298858821581887948L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String fileCode;

    private String fileName;

    private String relativePath;

    private String objectUrl;


}
