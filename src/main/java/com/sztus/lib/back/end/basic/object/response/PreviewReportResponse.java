package com.sztus.lib.back.end.basic.object.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Leon
 */
@Data
@SuperBuilder
public class PreviewReportResponse {

    private String location;

    private List<ChecklistItem> checklistItems;

}
