package com.sztus.lib.back.end.basic.object.response;

import com.sztus.lib.back.end.basic.object.domain.Location;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Austin
 * @date: 2024/4/8 14:39
 */
@Data
public class LocationItemResponse {

    List<Location> locationList = new ArrayList<>();

    List<ItemResponse> itemResponseList = new ArrayList<>();

}
