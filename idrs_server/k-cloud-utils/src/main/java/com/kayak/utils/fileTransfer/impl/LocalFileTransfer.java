package com.kayak.utils.fileTransfer.impl;

import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
@Slf4j
public class LocalFileTransfer implements FileTransfer {

    public LocalFileTransfer() throws IOException {
    }
    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        createParentDir(remoteFilePath);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(localFilePath);
            outputStream = new FileOutputStream(remoteFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        log.info("文件从【{}】上传到【{}】成功",localFilePath,remoteFilePath);
    }

    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        createParentDir(localFilePath);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(remoteFilePath);
            outputStream = new FileOutputStream(localFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
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

    public void downFolderFiles(String remoteFolder,String localFolder) throws Exception {}

    @Override
    public Boolean isFileExists(String remoteFilePath) throws Exception {
        return null;
    }

    @Override
    public void deleteFile(String remoteFilePath) throws Exception{
        // 删除文件
        File file =new File(remoteFilePath);
         file.delete();
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

}