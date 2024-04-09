package com.sztus.lib.back.end.basic.type.constant;

/**
 * @author QYP
 * @date 2024/4/7 14:42
 */

public interface LocationReportAction {

    String LIST_LOCATION = "/list-location";

    String LIST_FILE = "/list-file";

    String LIST_REPORT = "/list-report";

    String SAVE_LOCATION = "/save-location";

    String UPLOAD_FILE = "/upload-file";

    String DELETE_FILE = "/delete-file";

    String DELETE_LOCATION = "/delete-location";

    String AI_ANALYSE = "/ai-analyse";


    String STORAGE_UPLOAD_FILE = "/storage/upload-file";

    String UPLOAD_MULTIPART_FILE = "/storage/upload-multipart-file";


    String NEW_REPORT = "/new-report";
    String LIST_ITEMS_BY_REPORT = "/list-items-by-report";

    String SAVE_ITEM = "/save-item";

    String DELETE_ITEM = "/delete-item";

    String UPLOAD_REPORT = "/upload-report";
}
