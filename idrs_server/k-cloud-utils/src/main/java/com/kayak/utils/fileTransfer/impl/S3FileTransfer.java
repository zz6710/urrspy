package com.kayak.utils.fileTransfer.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedBackoffStrategies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;

import java.io.*;

@Slf4j
public class S3FileTransfer implements FileTransfer {
    private static final String  UTF_8 = "UTF-8";
    private static final String  ISO = "ISO8859-1";

    public static final String SEPARATOR = "/";
    private AmazonS3 s3=null;

    private  String amazonEndpointUrl ;
    private  String amazonAwsAccessKey;
    private  String amazonAwsSecretKey;
    private  String amazonAwsBucketName;
    /**
     *对象存储重试超时时间
     */
    private  final static  Integer CONNECTION_TIMEOUT=2000;
    /**
     *对象存储异常重试次数
     */
    private  final static  Integer MAX_ERROR_RETRY=3;
    public S3FileTransfer(String amazonEndpointUrl,String amazonAwsAccessKey,String amazonAwsSecretKey,String amazonAwsBucketName) throws IOException {
        this.amazonEndpointUrl=amazonEndpointUrl;
        this.amazonAwsAccessKey=amazonAwsAccessKey;
        this.amazonAwsSecretKey=amazonAwsSecretKey;
        this.amazonAwsBucketName=amazonAwsBucketName;
        s3=initAmazonS3();
    }

    /**
     * 初始化连接，每次使用需要重新连接
     */
    public AmazonS3 initAmazonS3(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(amazonAwsAccessKey , amazonAwsSecretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(CONNECTION_TIMEOUT);
        clientConfig.setRetryPolicy(new RetryPolicy(new SDKS3RetryCondition(),
                new PredefinedBackoffStrategies.SDKDefaultBackoffStrategy(),
                MAX_ERROR_RETRY ,true));
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfig)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                amazonEndpointUrl,
                                Regions.CN_NORTH_1.getName())).withPathStyleAccessEnabled(true)
                .build();

        return s3;
    }
    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        PutObjectResult result= new PutObjectResult();
        try {
            if(!s3.doesBucketExistV2(amazonAwsBucketName)){
                s3.createBucket(amazonAwsBucketName);
            }
            result= s3.putObject(new PutObjectRequest(amazonAwsBucketName,remoteFilePath, new File(localFilePath)));
        } catch (Exception ase) {
            log.error("amazonS3上传文件File模式异常 ", ase);
        }
        log.info("文件从【{}】上传到【{}】 运行结果【{}】",localFilePath,remoteFilePath,result.getMetadata());
    }

    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        createParentDir(localFilePath);
        GetObjectRequest getObjectRequest = new GetObjectRequest(amazonAwsBucketName, remoteFilePath);
        File file =new File(localFilePath);
        if(!file.exists()) file.createNewFile();
        s3.getObject(getObjectRequest, file);
        log.info("文件从【{}】下载到【{}】成功",remoteFilePath,localFilePath);
    }

    /**
     * 递归下载目录下的所有文件及子目录下所有文件
     * @param dir 需要下载的文件目录
     *
     */
    public void downDir(File dir,String localFilePath) throws Exception {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                downDir(new File(dir, children[i]),localFilePath+File.separator+dir.getName());
            }
        }else{
            downloadFile(dir.getAbsolutePath(),localFilePath+File.separator+dir.getName());
        }
    }

    public void downFolderFiles(String remoteFolder,String localFolder) throws Exception {
        // 列出S3目录下的所有对象
        String nextContinuationToken = null;
        do {
            ListObjectsV2Request listRequest = new ListObjectsV2Request()
                    .withBucketName(amazonAwsBucketName)
                    .withPrefix(remoteFolder)
                    .withContinuationToken(nextContinuationToken);
            ListObjectsV2Result listResult = s3.listObjectsV2(listRequest);

            for (S3ObjectSummary objectSummary : listResult.getObjectSummaries()) {
                if (!objectSummary.getKey().endsWith("/")) {
                    // 构建本地文件路径
                    String localFilePath = localFolder + objectSummary.getKey().substring(remoteFolder.length());
                    File localFile = new File(localFilePath);
                    File parentDir = localFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    try (InputStream inputStream = s3.getObject(new GetObjectRequest(amazonAwsBucketName, objectSummary.getKey())).getObjectContent();
                         FileOutputStream outputStream = new FileOutputStream(localFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        log.error("委外估值s3下载文件失败：{}", e.getMessage());
                        continue;
                    }
                }
            }
            nextContinuationToken = listResult.getNextContinuationToken();
        } while (nextContinuationToken != null);
    }

    @Override
    public Boolean isFileExists(String remoteFilePath) throws Exception {
        return s3.doesObjectExist(amazonAwsBucketName, remoteFilePath);
    }

    @Override
    public void deleteFile(String remoteFilePath) throws Exception{
            s3.deleteObject(amazonAwsBucketName, remoteFilePath);
            log.info("文件【{}】删除成功",remoteFilePath,remoteFilePath);
    }

    @Override
    public void uploadFileAndDisconnect(String localFilePath, String remoteFilePath) throws Exception {
        uploadFile( localFilePath,  remoteFilePath);
    }

    @Override
    public void downloadFileAndDisconnect(String remoteFilePath, String localFilePath) throws Exception {
        downloadFile( remoteFilePath,  localFilePath);
    }

    @Override
    public void deleteFileAndDisconnect(String remoteFilePath) throws Exception{
        deleteFile(remoteFilePath);
    }
    @Override
    public void disconnect() throws Exception {
    }
    public void createParentDir( String filePath) {
        File file = new File(filePath);
        File parentDir=file.getParentFile();
        if (!parentDir.exists() && !parentDir.isDirectory()) {
            parentDir.mkdirs();
        }
    }

    private class SDKS3RetryCondition implements RetryPolicy.RetryCondition {

        @Override
        public boolean shouldRetry(AmazonWebServiceRequest amazonWebServiceRequest, AmazonClientException e, int i) {
            return false;
        }
    }
}