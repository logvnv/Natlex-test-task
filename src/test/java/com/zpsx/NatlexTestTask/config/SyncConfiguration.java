package com.zpsx.NatlexTestTask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SyncTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Profile("non-async")
public class SyncConfiguration {

    @Bean
    public Executor taskExecutor() {
        return new SyncTaskExecutor();
    }
}