package com.kayak.utils.fileTransfer.impl;

import com.jcraft.jsch.*;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class SFTPFileTransfer implements FileTransfer {
    private static Logger log = LoggerFactory.getLogger(SFTPFileTransfer.class);
    private Session session;
    private ChannelSftp channel;

    public SFTPFileTransfer(String server, String port, String user, String password) throws Exception {
        this.connect(server,Integer.parseInt(port),user,password);
    }

    public SFTPFileTransfer() {
    }

    public void connect(String server, int port, String user, String password) throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(user, server, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
    }



    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        channel.put(localFilePath, remoteFilePath);
    }


    @Override
    public void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        createParentDir(localFilePath);
        InputStream inputStream = channel.get(remoteFilePath);

        FileOutputStream outputStream = new FileOutputStream(new File(localFilePath));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        inputStream.close();
    }
    @Override
    public void deleteFile(String remoteFilePath) throws Exception{
        // 删除文件
        channel.rm(remoteFilePath);
    }

    @Override
    public void uploadFileAndDisconnect(String localFilePath, String remoteFilePath) throws Exception {
        uploadFile(localFilePath, remoteFilePath);
        channel.disconnect();
        session.disconnect();
    }

    @Override
    public void downloadFileAndDisconnect(String remoteFilePath, String localFilePath) throws Exception {
        downloadFileAndDisconnect(remoteFilePath,localFilePath);
        channel.disconnect();
        session.disconnect();
    }
    @Override
    public void deleteFileAndDisconnect(String remoteFilePath) throws Exception{
        // 删除文件
        deleteFile(remoteFilePath);
        channel.disconnect();
        session.disconnect();
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

    @Override
    public void disconnect() throws Exception {
        channel.disconnect();
        session.disconnect();
    }
    public void createParentDir( String filePath) {
        File file = new File(filePath);
        File parentDir=file.getParentFile();
        if (!parentDir.exists() && !parentDir.isDirectory()) {
            parentDir.mkdirs();
        }
    }


}
