package com.kayak.sqlinfo.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.service.GraphqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.rmi.CORBA.Util;
import java.io.File;
import java.util.Map;
import java.util.Set;

@Service
public class SqlInfoService {

    private static final Logger log = LoggerFactory.getLogger(GraphqlService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Object requestPostForm(String appName, String url, Map<String, Object> params) throws Exception {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            keys.stream().forEach(key -> postParameters.add(key, params.get(key)));
        }

        return restTemplate.postForEntity(
                "http://" + appName + url,  new HttpEntity<>(postParameters, requestHeaders), Object.class
        ).getBody();
    }

    public Object reportData(String appName, String url, Map<String, Object> params) throws Exception {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            keys.stream().forEach(key -> postParameters.add(key, params.get(key)));
        }

        return restTemplate.postForEntity(
                "http://" + appName + url,  new HttpEntity<>(postParameters, requestHeaders), Object.class
        ).getBody();
    }

    public void importDps(String appName, String url, Map<String, Object> params, MultipartFile[] files) throws Exception {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));

       for (MultipartFile file : files) {
           ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
               @Override
               public String getFilename() {
                   return file.getOriginalFilename();
               }
               @Override
               public long contentLength() {
                   return file.getSize();

               }
           };
           params.put("files", fileAsResource);
       }

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        params.forEach(postParameters :: add);

        restTemplate.postForEntity(
                "http://" + appName + url,  new HttpEntity<>(postParameters, requestHeaders), Object.class
        ).getBody();
    }

}
