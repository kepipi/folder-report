package com.sztus.lib.back.end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Austin
 */
@SpringBootApplication
public class LocationReportApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LocationReportApplication.class);
        springApplication.run(args);
    }
}
