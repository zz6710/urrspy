package com.kayak.utils;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Vector;

/**
 * @program: k-cloud
 * @description: sftp文件传输工具类
 * @author: WangZhenXin
 * @create: 2020-12-30 20:11
 * @memo 备注信息
 */
public class SFTPUtil {
    private static final Logger logger = LoggerFactory.getLogger(SFTPUtil.class);

    private ChannelSftp sftp;

    private Session session;
    /** SFTP 登录用户名*/
    private String username;
    /** SFTP 登录密码*/
    private String password;
    /** 私钥 */
    private String privateKey;
    /** SFTP 服务器地址IP地址*/
    private String host;
    /** SFTP 端口*/
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SFTPUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SFTPUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SFTPUtil(){}


    /**
     * 连接sftp服务器
     * @throws JSchException
     */
    public void login() throws JSchException {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }


    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     * @param directory  服务器的基础路径
     * @param directory  上传到该目录
     * @param sftpFileName  sftp端文件名
     * @param input   输入流
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException, NoSuchFieldException, IllegalAccessException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String [] dirs=directory.split("/");
            String tempPath="";
            for(String dir:dirs){
                if(null== dir || "".equals(dir)) continue;
                tempPath+="/"+dir;
                try{
                    sftp.cd(tempPath);
                }catch(SftpException ex){
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        sftp.put(input, sftpFileName);  //上传文件
    }

    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     * @param directory  服务器的基础路径
     * @param directory  上传到该目录
     * @param sftpFileName  sftp端文件名
     * @param input   输入流
     */
    public void upload(String directory, String sftpFileName, InputStream input,String charset) throws SftpException, NoSuchFieldException, IllegalAccessException {
        Class<ChannelSftp> c = ChannelSftp.class;
        Field server_version = c.getDeclaredField("server_version");
        server_version.setAccessible(true);
        server_version.set(sftp, 2);
        sftp.setFilenameEncoding(charset);
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String [] dirs=directory.split("/");
            String tempPath="";
            for(String dir:dirs){
                if(null== dir || "".equals(dir)) continue;
                tempPath+="/"+dir;
                try{
                    sftp.cd(tempPath);
                }catch(SftpException ex){
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        sftp.put(input, sftpFileName);  //上传文件
    }


    /**
     * 下载文件。
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @throws IOException
     */
    public void download(String directory, String downloadFile, String saveFile, String charset) throws SftpException {
        FileOutputStream fileOutputStream = null;
        try {
            Class<ChannelSftp> c = ChannelSftp.class;
            Field server_version = c.getDeclaredField("server_version");
            server_version.setAccessible(true);
            server_version.set(sftp, 2);
            sftp.setFilenameEncoding(charset);
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            File file = new File(saveFile);
            File fileParent = file.getParentFile();
            if (!fileParent.exists() || !fileParent.isFile()) {
                fileParent.mkdirs();
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFile, fileOutputStream);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 下载文件夹中的所有文件
     * @param directory 文件夹路径
     * @param localPath 本地存储路径
     * @param charset 字符集
     */
    public void downloadFileDir(String directory, String localPath,String charset){
        try {
            Class<ChannelSftp> c = ChannelSftp.class;
            Field server_version = c.getDeclaredField("server_version");
            server_version.setAccessible(true);
            server_version.set(sftp, 2);
            sftp.setFilenameEncoding(charset);
            Vector vector = sftp.ls(directory);
            for (Object o : vector) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) o;
                String filename = lsEntry.getFilename();
                //vector会遍历到文件路径的../目录和./目录要过滤
                if ("..".equals(filename) || ".".equals(filename)){
                    continue;
                }
                download(directory,filename,localPath+"/"+filename,charset);
            }
        } catch (SftpException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);

        return fileData;
    }


    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException{
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }


    /**
     * 列出目录下的文件
     * @param directory 要列出的目录
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

}
