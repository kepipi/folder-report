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
@TableName("file_operation_record")
@AllArgsConstructor
@NoArgsConstructor
public class FileOperationRecord implements Serializable {

    private static final long serialVersionUID = 2347325319578634476L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String fileCode;

    private String openId;

    private Long operatedAt;

    private Integer type;

    private String userAgent;

    private String userIp;


}
