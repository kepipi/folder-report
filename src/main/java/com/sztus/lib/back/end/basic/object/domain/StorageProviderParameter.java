package com.sztus.lib.back.end.basic.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("storage_provider_parameter")
@AllArgsConstructor
@NoArgsConstructor
public class StorageProviderParameter implements Serializable {

    private static final long serialVersionUID = 3026478859361435460L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long companyId;

    private Long systemId;

    private Long providerId;

    private String parameterKey;

    private String parameterLabel;

    private String parameterValue;

    private String description;

    @TableField(value = "`status`")
    private Integer status;

    private Long createdAt;

    private Long updatedAt;


}
