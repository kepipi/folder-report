package com.sztus.lib.back.end.basic.type.constant;

/**
 * @author QYP
 * @date 2024/4/7 14:42
 */

public interface FolderReportAction {

    String LIST_FOLDER = "/list-folder";

    String LIST_FILE = "/list-file";

    String LIST_REPORT = "/list-report";

    String SAVE_FOLDER = "/save-folder";

    String UPLOAD_FILE = "/upload-file";

    String DELETE_FILE = "/delete-file";


    String STORAGE_UPLOAD_FILE = "/storage/upload-file";

    String DOWNLOAD_FILE = "/storage/download-file/{fileCode}";



}
