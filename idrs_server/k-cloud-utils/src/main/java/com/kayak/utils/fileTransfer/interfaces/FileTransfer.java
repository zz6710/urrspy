package com.kayak.utils.fileTransfer.interfaces;

import java.io.File;

public interface FileTransfer {

    void uploadFile(String localFilePath, String remoteFilePath) throws Exception;

    void downloadFile(String remoteFilePath, String localFilePath) throws Exception;

    void deleteFile(String remoteFilePath) throws Exception;

    void disconnect() throws Exception;

    void uploadFileAndDisconnect(String localFilePath, String remoteFilePath) throws Exception;

    void downloadFileAndDisconnect(String remoteFilePath, String localFilePath) throws Exception;

    void deleteFileAndDisconnect(String remoteFilePath) throws Exception;

    void downDir(File dir, String localFilePath) throws Exception;

    void downFolderFiles(String remoteFolder,String localFolder) throws Exception;

    Boolean isFileExists(String remoteFilePath) throws Exception;
}