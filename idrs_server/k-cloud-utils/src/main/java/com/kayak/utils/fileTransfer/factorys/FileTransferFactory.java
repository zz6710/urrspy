package com.kayak.utils.fileTransfer.factorys;

import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.impl.*;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;


public   class FileTransferFactory {

    public  static FileTransfer createFileTransfer(FileTransferConfig fileTransferConfig) throws Exception {
        String protocol=fileTransferConfig.getProtocol();//协议
        String ip=fileTransferConfig.getIp();
        String port=fileTransferConfig.getPort();
        String user=fileTransferConfig.getUser();
        String password=fileTransferConfig.getPassword();
        String amazonEndpointUrl=fileTransferConfig.getAmazonEndpointUrl();
        String amazonAwsAccessKey=fileTransferConfig.getAmazonAwsAccessKey();
        String amazonAwsSecretKey=fileTransferConfig.getAmazonAwsSecretKey();
        String amazonAwsBucketName=fileTransferConfig.getAmazonAwsBucketName();

        switch (protocol) {
            case "FTP"://FTP协议
                return new FTPFileTransfer(ip, port, user, password);
            case "SFTP"://SFTP协议
                return new SFTPFileTransfer(ip, port, user, password);
            case "S3"://S3共享存储协议
                return new S3FileTransfer(amazonEndpointUrl, amazonAwsAccessKey, amazonAwsSecretKey, amazonAwsBucketName);
            case "HWOBS"://华为OBS存储
                return new HuaweiObsFileTransfer(amazonEndpointUrl, amazonAwsAccessKey, amazonAwsSecretKey, amazonAwsBucketName);
            case "LOCAL"://本地存储
                return new LocalFileTransfer();
            default:
                throw new IllegalArgumentException("Unsupported protocol");
        }
    }
}
