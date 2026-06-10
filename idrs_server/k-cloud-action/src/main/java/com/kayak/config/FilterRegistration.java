package com.kayak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.kayak.core.filter.LocalRequestFilter;
import com.kayak.jwt.filter.JwtFilter;

import java.util.Arrays;

@Configuration
public class FilterRegistration {

	@Value("${rest.config.connectTimeout:10000}")
	private int connectTimeout;
	@Value("${rest.config.readTimeout:30000}")
	private int readTimeout;
//	@Value("${access.allow}")
	private String allow;

	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean(RestTemplate.class)
	RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(connectTimeout);
		httpRequestFactory.setReadTimeout(readTimeout);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 允许cookies跨域
		if (null == allow || "".equals(allow)) {
			config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
		} else {
			String[] allowArr = allow.split(",");
			config.setAllowedOrigins(Arrays.asList(allowArr));// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
		}
		config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
		config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
		config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<CorsFilter>();
		filterRegistrationBean.setFilter(new CorsFilter(source));
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<LocalRequestFilter> filterRegistrationBean() {
		FilterRegistrationBean<LocalRequestFilter> filterRegistrationBean = new FilterRegistrationBean<LocalRequestFilter>();
		LocalRequestFilter localRequestFilter = new LocalRequestFilter();
		filterRegistrationBean.setFilter(localRequestFilter);
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<JwtFilter>();
		JwtFilter jwtFilter = new JwtFilter();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.setOrder(3);
		filterRegistrationBean.addUrlPatterns("*.json", "*.action");
		return filterRegistrationBean;
	}

}
