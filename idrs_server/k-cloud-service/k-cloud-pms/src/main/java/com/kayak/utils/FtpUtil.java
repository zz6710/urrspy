package com.kayak.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @program: k-cloud
 * @description: FTP文件发送工具类
 * @author: WangZhenXin
 * @create: 2020-12-30 20:28
 * @memo 备注信息
 */
public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    
    /**
     * Description: 向FTP服务器上传文件
     * @param host FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param filePath FTP服务器文件存放路径。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String host, int port, String username, String password,
                                     String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.error("ftp连接失败！host:" + host + ",port:" + port + ",username:" + username + ",password:" + password);
                ftp.disconnect();
                return result;
            }
            //切换到上传目录
            if(filePath.contains(":")) {
                //目录存在，则直接进入该目录  如：D:/....
                logger.info(filePath);
                ftp.changeWorkingDirectory(filePath);
            }else{
                if (!ftp.changeWorkingDirectory(filePath)) {
                    //如果目录不存在创建目录
                    String[] dirs = filePath.split("/");
                    String tempPath = "";
                    for (String dir : dirs) {
                        if (null == dir || "".equals(dir)) continue;
                        tempPath += "/" + dir;
                        if (!ftp.changeWorkingDirectory(tempPath)) {  //进不去目录，说明该目录不存在
                            if (!ftp.makeDirectory(tempPath)) { //创建目录
                                //如果创建文件目录失败，则返回
                                logger.info("创建文件目录"+tempPath+"失败");
                                return result;
                            } else {
                                //目录存在，则直接进入该目录
                                ftp.changeWorkingDirectory(tempPath);
                            }
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(filename, input)) {
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                }
            }
        }
        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     * @param host FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());

                    File fileParent = localFile.getParentFile();
                    if(!fileParent.exists() || !fileParent.isFile()){
                        boolean mkdirs = fileParent.mkdirs();
                        logger.info(localPath+"文件夹创建:" + (mkdirs?"成功":"失败"));
                    }

                    if (!localFile.exists()) {
                        boolean newFile = localFile.createNewFile();
                        logger.info(fileName+"文件新建:" + (newFile?"成功":"失败"));
                    }

                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
        return result;
    }
}
