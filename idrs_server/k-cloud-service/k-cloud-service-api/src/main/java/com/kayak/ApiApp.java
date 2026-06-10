package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiApp {

	public static void main(String[] args) {
		SpringApplication.run(ApiApp.class, args);
	}

}
