package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author XueJ
 * @version 1.0.0
 * @ClassName ImportTemplateManage.java
 * @Description TODO
 * @createTime 2023年08月11日 19:30:00
 */
public class ImportTemplateUploadModel {

    /*
     本地文件根目录
    */
    String localPath;
    /*
     /远端sftp文件服务器根路径
    */
    String remotePath;
    /*
     /远端服务器ip地址
    */
    String lineIp;
    /*
     /远端服务器用户名
    */
    String sftpUserName;
    /*
     /远端服务器密码
    */
    String sftpUserPwd;


    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLineIp() {
        return lineIp;
    }

    public void setLineIp(String lineIp) {
        this.lineIp = lineIp;
    }

    public String getSftpUserName() {
        return sftpUserName;
    }

    public void setSftpUserName(String sftpUserName) {
        this.sftpUserName = sftpUserName;
    }

    public String getSftpUserPwd() {
        return sftpUserPwd;
    }

    public void setSftpUserPwd(String sftpUserPwd) {
        this.sftpUserPwd = sftpUserPwd;
    }
}
