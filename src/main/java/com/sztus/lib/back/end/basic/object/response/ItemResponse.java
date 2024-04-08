package com.sztus.lib.back.end.basic.object.response;

import com.sztus.lib.back.end.basic.object.domain.Item;
import lombok.Data;

import java.util.List;

/**
 * @author: Austin
 * @date: 2024/4/8 14:40
 */
@Data
public class ItemResponse {

    String locationName;

    List<Item> itemList;

}
