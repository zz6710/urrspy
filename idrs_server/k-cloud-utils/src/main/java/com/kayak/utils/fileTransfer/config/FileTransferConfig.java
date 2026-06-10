package com.kayak.utils.fileTransfer.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public  class FileTransferConfig {

    @Value("${unifiedFileStorage.protocol}")
    private String protocol;

    @Value("${unifiedFileStorage.ip}")
    private String ip;

    @Value("${unifiedFileStorage.port}")
    private String port;

    @Value("${unifiedFileStorage.user}")
    private String user;

    @Value("${unifiedFileStorage.password}")
    private String password;

    @Value("${unifiedFileStorage.remoteRootPath}")
    private String remoteRootPath;

    @Value("${unifiedFileStorage.amazon.endpointUrl}")
    private  String amazonEndpointUrl ;

    @Value("${unifiedFileStorage.amazon.accessKey}")
    private  String amazonAwsAccessKey;

    @Value("${unifiedFileStorage.amazon.secretKey}")
    private  String amazonAwsSecretKey;

    @Value("${unifiedFileStorage.amazon.bucketName}")
    private  String amazonAwsBucketName;

}
