package com.kayak.login.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@WebFilter(filterName = "rptFilter")
@Order(value = 4)
public class RptFilter implements Filter {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if (!uri.startsWith("/jmreport")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (uri.startsWith("/jmreport") && uri.endsWith("map")) {
            response.sendError(404);
            return;
        }
        if (uri.contains(".js")) {
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        }
        ResponseEntity<byte[]> result;
        // 转发到RptApp
        try {
            result = sendRptAppService(request);
        } catch (RestClientException e) {
            return;
        }
        // 处理返回数据
        byte[] responseBody = result.getBody();
        HttpHeaders resultHeaders = result.getHeaders();
        MediaType contentType = resultHeaders.getContentType();
        if (contentType != null) {
            String contentTypeString = contentType.toString().toLowerCase(Locale.ROOT);
            Charset charset = contentType.getCharset();
            if (charset != null) {
                response.setCharacterEncoding(charset.toString());
            } else if (!contentTypeString.contains("image")) {//解决控件乱码
                contentTypeString = contentTypeString + ";charset=utf-8";
            }
            response.setContentType(contentTypeString);
        }
        if (responseBody != null) {
            response.getOutputStream().write(responseBody);//写入字节流（js、html页面、图片等）
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public ResponseEntity<byte[]> sendRptAppService(HttpServletRequest request) throws IOException {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null && !query.isEmpty()) {
            uri += "?" + query;
        }
        URI url = URI.create("http://RptApp" +uri);
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        MultiValueMap<String, String> headers = parseHeader(request);
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("multipart/form-data;")) {
            // 文件上传
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            MultipartResolver resolver = new StandardServletMultipartResolver();
            MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);;
            MultiValueMap<String, MultipartFile> multiValueMap = multipartHttpServletRequest.getMultiFileMap();
            for (Map.Entry<String, List<MultipartFile>> entries : multiValueMap.entrySet()) {
                for (MultipartFile multipartFile: entries.getValue()) {
                    String fileName = multipartFile.getOriginalFilename();
                    File file = File.createTempFile("spw-", fileName);
                    multipartFile.transferTo(file);
                    FileSystemResource fileSystemResource = new FileSystemResource(file);
                    form.add(entries.getKey(), fileSystemResource);
                }
            }
            Enumeration<String> enumeration = multipartHttpServletRequest.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                String value = multipartHttpServletRequest.getParameter(name);
                form.add(name, value);
            }
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(form, headers);
            return restTemplate.exchange(url, httpMethod, requestEntity, byte[].class);
        } else {
            // 普通请求
            InputStream inputStream = request.getInputStream();
            byte[] body = StreamUtils.copyToByteArray(inputStream);
            RequestEntity requestEntity = new RequestEntity<>(body, headers, httpMethod, url);
            return restTemplate.exchange(requestEntity, byte[].class);
        }
    }

    public MultiValueMap<String, String> parseHeader(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName:
                headerNames) {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headerValue: headerValues) {
                httpHeaders.add(headerName, headerValue);
            }
        }
        return httpHeaders;
    }

}
