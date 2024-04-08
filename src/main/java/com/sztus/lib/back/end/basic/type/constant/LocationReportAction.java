package com.sztus.lib.back.end.basic.type.constant;

/**
 * @author QYP
 * @date 2024/4/7 14:42
 */

public interface LocationReportAction {

    String LIST_LOCATION = "/list-location";

    String LIST_FILE = "/list-file";

    String LIST_REPORT = "/list-report";

    String SAVE_FOLDER = "/save-location";

    String UPLOAD_FILE = "/upload-file";

    String DELETE_FILE = "/delete-file";

    String AI_ANALYSE = "/ai-analyse";


    String STORAGE_UPLOAD_FILE = "/storage/upload-file";

    String DOWNLOAD_FILE = "/storage/download-file/{fileCode}";



}
