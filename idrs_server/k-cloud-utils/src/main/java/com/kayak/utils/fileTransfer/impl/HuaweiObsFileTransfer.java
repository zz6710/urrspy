package com.kayak.utils.fileTransfer.impl;

import com.kayak.core.util.FileUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author zhangz
 * @create 2025/05/26
 * @since 1.0.0
 */
@Slf4j
public class HuaweiObsFileTransfer implements InitializingBean, FileTransfer {
    private static final String  UTF_8 = "UTF-8";

    private static final String  ISO = "ISO8859-1";

    public static final String SEPARATOR = "/";

    private  String huiweiEndpointUrl ;

    private  String huiweiAccessKey;

    private  String huiweiSecretKey;

    private  String huiweiBucketName;

    private ObsClient obsClient = null;

    public HuaweiObsFileTransfer(String huiweiEndpointUrl, String huiweiAccessKey, String huiweiSecretKey, String huiweiBucketName) throws IOException {
        this.huiweiEndpointUrl=huiweiEndpointUrl;
        this.huiweiAccessKey=huiweiAccessKey;
        this.huiweiSecretKey=huiweiSecretKey;
        this.huiweiBucketName=huiweiBucketName;
        obsClient = initHuaweiObs();
    }

    /**
     * 初始化连接，每次使用需要重新连接
     */
    public ObsClient initHuaweiObs(){
        ObsClient obsClient = new ObsClient(huiweiAccessKey, huiweiSecretKey, huiweiEndpointUrl);
        return obsClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] huiweiAccessKeyBytes = decoder.decode(huiweiAccessKey);
        huiweiAccessKey = new String(huiweiAccessKeyBytes, StandardCharsets.UTF_8);
        byte[] huiweiSecretKeyBytes = decoder.decode(huiweiSecretKey);
        huiweiSecretKey = new String(huiweiSecretKeyBytes, StandardCharsets.UTF_8);
    }

    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        try {
            PutObjectRequest request = new PutObjectRequest(huiweiBucketName,remoteFilePath);
            request.setFile(new File(localFilePath));
            obsClient.putObject(request);
        } catch (Exception ase) {
            log.error("文件{}上传OBS失败，上传文件名为{}", localFilePath, remoteFilePath, ase);
        }
    }

    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        try {
            if(!obsClient.doesObjectExist(huiweiBucketName, remoteFilePath)){
                log.info("OBS下载->{}文件---文件不存在 ",remoteFilePath);
            }
            ObsObject o = obsClient.getObject(huiweiBucketName, remoteFilePath);
            InputStream ins = o.getObjectContent();

            createParentDir(localFilePath);
            File file = new File(localFilePath);
            FileUtil.inputStreamToFile(ins, file);
        } catch (Exception ase) {
            log.error("下载OBS文件{}失败，下载路径为{}", remoteFilePath, localFilePath, ase);
        }
    }

    @Override
    public void deleteFile(String remoteFilePath) throws Exception {
        try {
            obsClient.deleteObject(huiweiBucketName, remoteFilePath);
        } catch (Exception ase) {
            log.error("删除OBS文件失败，桶名为{},文件名为{}", huiweiBucketName, remoteFilePath, ase);
        }
    }

    @Override
    public void disconnect() throws Exception {
        obsClient = initHuaweiObs();
    }

    @Override
    public void uploadFileAndDisconnect(String localFilePath, String remoteFilePath) throws Exception {
        uploadFile(localFilePath, remoteFilePath);
    }

    @Override
    public void downloadFileAndDisconnect(String remoteFilePath, String localFilePath) throws Exception {
        downloadFile(remoteFilePath, localFilePath);
    }

    @Override
    public void deleteFileAndDisconnect(String remoteFilePath) throws Exception {
        deleteFile(remoteFilePath);
    }

    @Override
    public void downDir(File dir, String localFilePath) throws Exception {}

    @Override
    public void downFolderFiles(String remoteFolder, String localFolder) throws Exception {
        // 创建本地目录
        File localDir = new File(localFolder);
        if (!localDir.exists()) {
            localDir.mkdirs();
        }

        // 列举当前远程路径下的所有对象
        ListObjectsRequest listReq = new ListObjectsRequest(huiweiBucketName);
        listReq.setPrefix(remoteFolder); // 过滤前缀（指定文件夹）
        listReq.setDelimiter("/"); // 分隔符，用于区分文件夹和文件
        ObjectListing listing;

        do {
            listing = obsClient.listObjects(listReq);
            // 处理当前路径下的文件（CommonPrefixes是子文件夹，Contents是文件）
            for (ObsObject obsObject : listing.getObjects()) {
                String objectKey = obsObject.getObjectKey();
                // 跳过文件夹本身（避免创建空文件）
                if (objectKey.endsWith("/")) {
                    continue;
                }
                // 计算本地文件路径（截取远程文件夹前缀，保留相对路径）
                String relativePath = objectKey.substring(remoteFolder.length());
                String localFilePath = localFolder + File.separator + relativePath;
                // 下载单个文件
                downloadFile(objectKey, localFilePath);
            }

            // 递归处理子文件夹
            for (String subDirPrefix : listing.getCommonPrefixes()) {
                // 子文件夹本地保存路径
                String subLocalPath = localFolder + File.separator + subDirPrefix.substring(remoteFolder.length());
                // 递归下载子文件夹
                downFolderFiles(subDirPrefix, subLocalPath);
            }

            // 分页处理（若文件过多，需翻页）
            listReq.setMarker(listing.getNextMarker());
        } while (listing.isTruncated()); // isTruncated为true表示还有下一页
    }

    @Override
    public Boolean isFileExists(String remoteFilePath) throws Exception {
        if(!obsClient.doesObjectExist(huiweiBucketName, remoteFilePath)){
            log.info("OBS下载->{}文件---文件不存在 ", remoteFilePath);
            return false ;
        }
        return true ;
    }

    public void createParentDir( String filePath) {
        File file = new File(filePath);
        File parentDir=file.getParentFile();
        if (!parentDir.exists() && !parentDir.isDirectory()) {
            parentDir.mkdirs();
        }
    }
}
