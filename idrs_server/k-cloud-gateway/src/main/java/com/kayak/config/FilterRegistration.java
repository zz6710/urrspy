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
import com.kayak.login.filter.JwtFilter;
import com.kayak.login.service.LoginBaseService;
import com.kayak.login.service.LoginService;

import java.util.Arrays;

@Configuration
public class FilterRegistration {

	@Value("${rest.config.connectTimeout:10000}")
	private int connectTimeout;
	@Value("${rest.config.readTimeout:30000}")
	private int readTimeout;

	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean(RestTemplate.class)
	RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(connectTimeout);
		httpRequestFactory.setReadTimeout(readTimeout);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		return restTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(LoginService.class)
	LoginBaseService loginService() {
		LoginBaseService loginService = new LoginBaseService();
		return loginService;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 允许cookies跨域
		config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
		config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
		config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
		config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许
		config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers","err"));

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
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		JwtFilter jwtFilter = new JwtFilter();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.setOrder(3);
		filterRegistrationBean.addInitParameter("unfilter-page",
				"/valtabImpdata/valTabImport.action,/getResultData.json,/flow/reload.json,/commUpdate/QuartzInfo/updateQuartzInfo.json,/commUpdate/QuartzInfo/updateStatusOnStop.json,/commUpdate/QuartzInfo/updateStatusOnEnable.json,/logout.json,/jwt/loginAuth.json,/jwt/login.json,/jwt/resetPwd.json,/graphql/reloadConfig.json,/graphql/commQuery.json,/graphql/commUpdate.json,/getLoginConfig.json,/heathy.json,/queryState.json,/privilege/getResult.json,/privilege/downFile.json,/jwt/setH5Token.json,/verify/verifyCode.action");
		filterRegistrationBean.addUrlPatterns("*.json", "*.action","/transmit/*");
		return filterRegistrationBean;
	}

}
