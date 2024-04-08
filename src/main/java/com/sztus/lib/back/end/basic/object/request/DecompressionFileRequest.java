package com.sztus.lib.back.end.basic.object.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 解压文件请求
 *
 * @author Max
 * @date 2023/05/10
 */
@Getter
@Setter
@NoArgsConstructor
public class DecompressionFileRequest {

    /**
     * FTP/SFTP/OSS/S3
     * 上传的文件名
     */
    private String fileName;

    /**
     * FTP/SFTP/OSS/S3
     */
    private Integer connectionType;

    /**
     * FTP/SFTP/OSS/S3
     * 文件储存地址
     * OSS 不能以/开头 示例: test
     * SFTP 示例: /test
     */
    private String filePath;

    /**
     * 解压路径
     * 当前文件名解压之后文件名不变
     */
    private String decompressionPath;

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
