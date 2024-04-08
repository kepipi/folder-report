package com.sztus.lib.back.end.basic.object.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ice
 */
@Getter
@Setter
@NoArgsConstructor
public class DeleteFileRequest {

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
     * 文件储存地址
     * OSS 不能以/开头 示例: test
     * SFTP 示例: /test
     */
    private String filePath;

    /**
     * OSS/S3
     * 桶名
     */
    private String bucketName;

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

}
