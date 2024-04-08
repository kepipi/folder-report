package com.sztus.lib.back.end.basic.object.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ICE
 */
@Setter
@Getter
@NoArgsConstructor
public class ConnectionRequest {
    /**
     * S3/SFTP
     */
    private Integer connectionType;
    private String port;
    private String filePath;
    private String host;
    private String userName;
    private String password;
    private String fileName;
    private String fileBody;

}
