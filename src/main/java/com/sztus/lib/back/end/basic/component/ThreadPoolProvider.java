package com.sztus.lib.back.end.basic.component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class ThreadPoolProvider {

    @Bean(name = "aiAnalyseThreadPool")
    public ThreadPoolExecutor aiAnalyseThreadPool() {
        return new ThreadPoolExecutor(
                10,
                20,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("connect-log-pool-%d")
                        .setDaemon(true)
                        .build()
        );
    }

}
