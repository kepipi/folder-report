package com.sztus.lib.back.end.basic.object.request;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ice
 */
@Setter
@Getter
@NoArgsConstructor
public class UploadFileRequest {

    /**
     * FTP/SFTP/OSS/S3
     */
    private Integer connectionType;

    /**
     * FTP/SFTP/OSS/S3
     * 上传的文件名
     */
    private String fileName;

    /**
     * FTP/SFTP/OSS/S3
     * 上传的文件内容 需要将流转成String,请使用下面的方法
     * ConvertUtil.streamToString(fileBody)
     */
    private String fileBody;

    /**
     * FTP/SFTP/OSS/S3
     * 文件储存地址
     * 不传默认根目录
     * 新目录自动创建
     * OSS 不能以/开头 示例: test
     * SFTP 示例: /test
     */
    private String filePath;


    /**
     * FTP/SFTP
     * 目标服务器端口
     */
    private String port;

    /**
     * FTP/SFTP
     * 目标服务器
     */
    private String host;

    /**
     * FTP/SFTP
     * 服务器用户名
     */
    private String userName;

    /**
     * FTP/SFTP
     * 服务器密码
     */
    private String password;


    /**
     * 分片上传标识
     */
    private String uploadId;

    /**
     * 分片上传时提供分片号
     */
    private Integer current;

    /**
     *
     */
    private JSONArray partETagArray;
}
