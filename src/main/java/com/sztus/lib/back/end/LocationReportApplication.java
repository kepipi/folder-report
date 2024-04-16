package com.sztus.lib.back.end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

/**
 * @author Austin
 */
@SpringBootApplication
@EnableAsync
public class LocationReportApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SpringApplication springApplication = new SpringApplication(LocationReportApplication.class);
        springApplication.run(args);
    }
}
