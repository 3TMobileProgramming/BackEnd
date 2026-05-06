package com.syu.noticeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NoticeapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoticeapiApplication.class, args);
    }
}