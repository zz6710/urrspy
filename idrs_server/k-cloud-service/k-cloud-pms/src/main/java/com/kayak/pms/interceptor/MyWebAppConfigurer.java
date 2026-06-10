//package com.kayak.pms.interceptor;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
//
//	@Override
//	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		
//		converters.add(responseBodyConverter());
//	}
//	@Bean
//	public HttpMessageConverter responseBodyConverter() {
//		 StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
//
//	return converter;
//
//	}
//
//}
