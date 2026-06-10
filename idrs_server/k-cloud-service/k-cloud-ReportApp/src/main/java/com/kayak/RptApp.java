package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
public class RptApp {
    public static void main(String[] args) {
//		System.setProperty("mail.mime.splitlongparameters", "false");
        SpringApplication.run(RptApp.class, args);

    }
}