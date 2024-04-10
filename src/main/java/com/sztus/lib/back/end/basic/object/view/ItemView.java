package com.sztus.lib.back.end.basic.object.view;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Leon
 */
@Data
@SuperBuilder
public class ItemView {

    private Long itemId;

    private String itemName;

    private String quantity;

    private String cleanliness;

    private String condition;

    private String comments;

    private String description;

}
