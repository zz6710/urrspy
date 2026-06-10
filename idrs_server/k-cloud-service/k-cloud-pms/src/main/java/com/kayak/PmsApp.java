package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
public class PmsApp {

	public static void main(String[] args) {
		System.setProperty("mail.mime.splitlongparameters", "false");
		SpringApplication.run(PmsApp.class, args);
	}
}