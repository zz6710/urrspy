package com.kayak.dps.app.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

public class SftpUtils {

    private static Logger log = LoggerFactory.getLogger(SftpUtils.class);


    /**
     *  通过SFTP上传文件到服务器,端口默认为22
     * @param ip  远端IP
     * @param username  用户名
     * @param password  密码
     * @param remotePath  远端上传/下载目录
     * @param localPath  本地上传/下载目录
     * @param fileName  文件名(本地文件名和远端文件名保持一致)
     * @throws Exception
     */
    public static void putFile(String ip,String username,String password,String remotePath,String localPath,String fileName) throws Exception{
        log.info(" >>>>> SFTP开始上传文件");
        log.info(" >>>>> 远程地址："+ip);
        log.info(" >>>>> 远程目录："+remotePath);
        log.info(" >>>>> 本地路径："+localPath);
        log.info(" >>>>> 文件名："+fileName);
        ChannelSftp sftp = null;
        File file = new File(localPath + fileName);

        try(FileInputStream is = new FileInputStream(file);) {
            sftp = SftpUtils.connect(ip, 22, username, password, remotePath);
            sftp.cd(remotePath);
            sftp.put(is, file.getName());

        } catch (Exception e){
            log.error(" >>>>> SFTP上传文件失败：", e);
            throw e;
        }finally{
            if(sftp!=null){
                SftpUtils.disconnect(sftp);
            }
        }
        log.info(" >>>>> SFTP上传文件完成");
    }

    /**
     * 连接sftp服务器
     * @param ip
     *            主机
     * @param port
     *            端口
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     */
    public static ChannelSftp connect(String ip, int port, String username, String password, String path) throws Exception {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            log.debug("连接FTP服务器：ip='" + ip + "',port='" + port + "'");
            jsch.getSession(username, ip, port);
            Session sshSession = jsch.getSession(username, ip, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            createDir(path, sftp);
            sftp.cd(path);
        } catch (Exception e) {
            log.error("连接SFTP服务器发生异常。ip='" + ip + "',port='" + port + "'", e);
            try {
                if (sftp != null && sftp.isConnected()) {
                    sftp.disconnect();
                    sftp = null;
                }
            } catch (Exception ex) {
                throw new Exception("连接SFTP服务器发生异常。ip='" + ip + "',port='" + port + "'", e);
            }
            throw new Exception(e.getMessage());
        }
        return sftp;
    }


    public static void disconnect(ChannelSftp sftp){
        if(null != sftp) {
            sftp.disconnect();
            try {
                if(null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个文件目录
     * @throws Exception
     */
    public static void createDir(String createpath, ChannelSftp sftp) throws Exception {
        try {
            if (isDirExist(createpath, sftp)) {
                sftp.cd(createpath);
                return;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(), sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    log.info("创建目录[" + filePath.toString() + "]成功");
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                    log.info("当前远程目录[" + filePath.toString() + "]");
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
            throw new Exception("创建路径错误：" + createpath);
        }
    }

    /**
     * 判断目录是否存在
     */
    public static boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     *  通过SFTP下载文件到本地,端口默认为22
     * @param ip  远端IP
     * @param username  用户名
     * @param password  密码
     * @param remotePath  远端上传/下载目录
     * @param localPath  本地上传/下载目录
     * @param fileName  文件名(本地文件名和远端文件名保持一致)
     * @throws Exception
     */
    public static void getFile(String ip,String username,String password,String remotePath,String localPath,String fileName) throws Exception{
        log.info(" >>>>> SFTP开始下载文件");
        log.info(" >>>>> 远程地址：{}", ip);
        log.info(" >>>>> 远程目录：{}", remotePath);
        log.info(" >>>>> 本地路径：{}", localPath);
        log.info(" >>>>> 文件名：{}", fileName);
        ChannelSftp sftp = null;
        try {
            sftp = SftpUtils.connect(ip, 22, username, password, remotePath);
            if(!SftpUtils.isFileExist(sftp, remotePath, fileName)){
                throw new RuntimeException("服务器文件不存在: " + remotePath + fileName);
            }
            sftp.get(remotePath + fileName, new FileOutputStream(new File(localPath + fileName)));
        } catch (Exception e){
            log.error(">>>>> 文件下载失败：", e);
            throw e;
        }finally{
            if(sftp!=null){
                SftpUtils.disconnect(sftp);
            }
        }
        log.info(" >>>>> SFTP下载文件完成");
    }
    /**
     *  通过SFTP下载文件到本地,端口默认为22
     * @param ip  远端IP
     * @param username  用户名
     * @param password  密码
     * @param remotePath  远端上传/下载目录
     * @param localPath  本地上传/下载目录
     * @param fileName  文件名(本地文件名和远端文件名保持一致)
     * @throws Exception
     */
    public static void getSplitFiles(String ip,String username,String password,String remotePath,String localPath,String fileName ) throws Exception{
        log.info(" >>>>> SFTP开始下载文件");
        log.info(" >>>>> 远程地址：{}", ip);
        log.info(" >>>>> 远程目录：{}", remotePath);
        log.info(" >>>>> 本地路径：{}", localPath);
        log.info(" >>>>> 文件名：{}", fileName);

        String matchName=fileName.replace(".","*.");//模糊查找
        ChannelSftp sftp = null;
        FileOutputStream fos=null;
        try {
            ArrayList<String> fileNames=new ArrayList<String>();
            sftp = SftpUtils.connect(ip, 22, username, password, remotePath);
            sftp.cd(remotePath);
            Vector<ChannelSftp.LsEntry > entrys= sftp.ls(matchName);
            for(int i=0;i<entrys.size();i++){
                ChannelSftp.LsEntry lsEntry=entrys.get(i);
                fileNames.add(lsEntry.getFilename());
            }
            for(String newFileName:fileNames){
                try{
                    fos=new FileOutputStream(new File(localPath+newFileName));
                    sftp.get(remotePath + newFileName,fos );
                    log.info(" >>>>> SFTP下载文件"+localPath + newFileName+"完成");

                }catch (Exception e){
                    throw e;
                }finally {
                    if(fos!=null){
                        fos.close();
                    }
                }
            }
            if(fileNames.size()==0){
                throw new Exception("FTP服务器文件不存在: " + remotePath + fileName);
            }
        } catch (Exception e){
            log.error(">>>>> 文件下载失败：", e);
            throw e;
        }finally{
            if(sftp!=null){
                SftpUtils.disconnect(sftp);
            }
        }
    }

    public static boolean isFileExist(ChannelSftp sftp, String directory, final String fileName) {
        Vector<ChannelSftp.LsEntry> enties;
        boolean isExist = false;
        try {
            enties = sftp.ls(directory);
            for(ChannelSftp.LsEntry entry : enties) {
                if(entry.getFilename().equals(fileName)) {
                    isExist = true;
                }
            }
            return isExist;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
