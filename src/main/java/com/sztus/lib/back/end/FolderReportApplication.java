package com.sztus.lib.back.end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Austin
 */
@SpringBootApplication
public class FolderReportApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FolderReportApplication.class);
        springApplication.run(args);
    }
}
