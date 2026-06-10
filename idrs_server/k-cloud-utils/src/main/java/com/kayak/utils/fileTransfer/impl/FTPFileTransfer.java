package com.kayak.utils.fileTransfer.impl;

import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.net.ftp.FTPClient;
import java.io.*;

public class FTPFileTransfer implements FileTransfer {
    private FTPClient ftpClient;


    public FTPFileTransfer(String server, String port, String user, String password) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(server, Integer.parseInt(port));
        ftpClient.login(user, password);
    }


    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        try (InputStream inputStream = new FileInputStream(localFilePath)) {
            ftpClient.storeFile(remoteFilePath, inputStream);
        }
    }

    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        createParentDir(localFilePath);
        OutputStream outputStream = new FileOutputStream(localFilePath);
            ftpClient.retrieveFile(remoteFilePath, outputStream);
    }

    @Override
    public void deleteFile(String remoteFilePath) throws Exception{
        // 删除文件
        ftpClient.deleteFile(remoteFilePath);
    }

    @Override
    public void disconnect() throws Exception {
        ftpClient.disconnect();
    }
    @Override
    public void uploadFileAndDisconnect(String localFilePath, String remoteFilePath) throws Exception {
            uploadFileAndDisconnect(localFilePath,remoteFilePath);
            ftpClient.disconnect();
    }

    @Override
    public void downloadFileAndDisconnect(String remoteFilePath, String localFilePath) throws Exception {
        downloadFileAndDisconnect(remoteFilePath,localFilePath);
        ftpClient.disconnect();
    }
    @Override
    public void deleteFileAndDisconnect(String remoteFilePath) throws Exception{
        // 删除文件
        deleteFile(remoteFilePath);
        ftpClient.disconnect();
    }

    @Override
    public void downDir(File dir, String localFilePath) throws Exception {
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

    @Override
    public void downFolderFiles(String remoteFolder,String localFolder) throws Exception {}

    @Override
    public Boolean isFileExists(String remoteFilePath) throws Exception {
        return null;
    }


    public void createParentDir( String filePath) {
        File file = new File(filePath);
        File parentDir=file.getParentFile();
        if (!parentDir.exists() && !parentDir.isDirectory()) {
            parentDir.mkdirs();
        }
    }




}