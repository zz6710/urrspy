package com.kayak.pms.connect.file;

import com.kayak.pms.connect.model.SftpConfig;
import com.kayak.pms.connect.service.SftpConfigService;
import com.kayak.pms.connect.utils.FtpUtil;
import com.kayak.pms.connect.utils.SFTPUtil;
import com.kayak.pms.connect.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @program: k-cloud
 * @description: 文件发送Client
 * @author: WangZhenXin
 * @create: 2020-12-30 20:24
 * @memo 备注信息
 */
@Service
public class KFileClient {
    private static final Logger logger = LoggerFactory.getLogger(KFileClient.class);

    @Autowired
    private SftpConfigService sftpConfigService;


    /**
     * 上传文件与本地文件同名时上传方法
     * @param mailbox  信箱参数
     * @param fileLocalPath 文件本地服务器路径
     * @param fileServerPath 文件上传路径
     * @param fileName 上传文件名
     * @return
     * @throws Exception
     */
    public void upload(String mailbox, String fileLocalPath, String fileServerPath, String fileName) throws Exception {
        boolean result = true;
        String method = null;// 传输方式
        String host = null;// 主机名
        int port = 0;// 端口
        String username = null;// 用户名
        String password = null;// 密码
        String privateKey = null;// 公钥
        String charset = null;// 字符集

        File file = new File(fileLocalPath+File.separator+fileName);

        SftpConfig sftpConfigByMailbox = sftpConfigService.getSftpConfigByMailbox(mailbox);
        method = sftpConfigByMailbox.getMethod();
        port = Integer.parseInt(sftpConfigByMailbox.getPort());
        host = sftpConfigByMailbox.getHost();
        username = sftpConfigByMailbox.getUsername();
        password = sftpConfigByMailbox.getPassword();
        privateKey = sftpConfigByMailbox.getPrivateKey();
        charset = sftpConfigByMailbox.getCharset();

        if ("sftp".equals(method)) {
            InputStream is = null;
            try {
                SFTPUtil sftp = null;
                //若连接参数存在私钥则默认使用私钥进行连接
                //logger.info("privateKey:" + privateKey);
                if (StringUtils.isNotEmpty(privateKey)) {
                    //logger.info("1");
                    sftp = new SFTPUtil(username, host, port, privateKey);
                } else {
                    //logger.info("2");
                    sftp = new SFTPUtil(username, password, host, port);
                }
                sftp.login();
                is = new FileInputStream(file);
                sftp.upload(fileServerPath, fileName, is, charset);
                sftp.logout();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else if ("ftp".equals(method)) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                result = FtpUtil.uploadFile(host, port, username, password, fileServerPath, fileName, is);
                logger.info("文件上传是否成功：" + String.valueOf(result));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            logger.error("传输方式填写错误：method[" + method + "]");
            throw new Exception("传输方式填写错误：method[" + method + "]");
        }
    }

    /**
     * 本地文件与上传文件文件名不同上传方法
     * @param mailbox 信箱编码
     * @param fileLocalPath 文件本地路径
     * @param fileServerPath 文件服务器路径
     * @param fileLocalName 文件本地名称
     * @param fileServerName 文件服务器名称
     * @throws Exception
     */
    public void upload(String mailbox, String fileLocalPath, String fileServerPath,String fileLocalName, String fileServerName) throws Exception {
        boolean result = true;
        String method = null;// 传输方式
        String host = null;// 主机名
        int port = 0;// 端口
        String username = null;// 用户名
        String password = null;// 密码
        String privateKey = null;// 公钥
        String charset = null;// 字符集

        File file = new File(fileLocalPath+File.separator+fileLocalName);

        SftpConfig sftpConfigByMailbox = sftpConfigService.getSftpConfigByMailbox(mailbox);
        method = sftpConfigByMailbox.getMethod();
        port = Integer.parseInt(sftpConfigByMailbox.getPort());
        host = sftpConfigByMailbox.getHost();
        username = sftpConfigByMailbox.getUsername();
        password = sftpConfigByMailbox.getPassword();
        privateKey = sftpConfigByMailbox.getPrivateKey();
        charset = sftpConfigByMailbox.getCharset();

        if ("sftp".equals(method)) {
            InputStream is = null;
            try {
                SFTPUtil sftp = null;
                //若连接参数存在私钥则默认使用私钥进行连接
                //logger.info("privateKey:" + privateKey);
                if (StringUtils.isNotEmpty(privateKey)) {
                    //logger.info("1");
                    sftp = new SFTPUtil(username, host, port, privateKey);
                } else {
                    //logger.info("2");
                    sftp = new SFTPUtil(username, password, host, port);
                }
                sftp.login();
                is = new FileInputStream(file);
                sftp.upload(fileServerPath, fileServerName, is, charset);
                sftp.logout();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else if ("ftp".equals(method)) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                result = FtpUtil.uploadFile(host, port, username, password, fileServerPath, fileServerName, is);
                logger.info("文件上传是否成功：" + String.valueOf(result));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            logger.error("传输方式填写错误：method[" + method + "]");
            throw new Exception("传输方式填写错误：method[" + method + "]");
        }
    }

    /**
     * wzx 重构文件上传方法 添加字符集参数  防止中文文件应字符集问题乱码
     *
     * @param mailbox    信箱参数
     * @param file     上传文件
     * @param filePath 上传目录
     * @param fileName 上传文件名
     * @return
     * @throws Exception
     */
    public void upload(String mailbox, File file, String filePath, String fileName) throws Exception {
        boolean result = true;
        String method = null;// 传输方式
        String host = null;// 主机名
        int port = 0;// 端口
        String username = null;// 用户名
        String password = null;// 密码
        String privateKey = null;// 公钥
        String charset = null;// 字符集

        SftpConfig sftpConfigByMailbox = sftpConfigService.getSftpConfigByMailbox(mailbox);
        method = sftpConfigByMailbox.getMethod();
        port = Integer.parseInt(sftpConfigByMailbox.getPort());
        host = sftpConfigByMailbox.getHost();
        username = sftpConfigByMailbox.getUsername();
        password = sftpConfigByMailbox.getPassword();
        privateKey = sftpConfigByMailbox.getPrivateKey();
        charset = sftpConfigByMailbox.getCharset();

        if ("sftp".equals(method)) {
            InputStream is = null;
            try {
                SFTPUtil sftp = null;
                //若连接参数存在私钥则默认使用私钥进行连接
                //logger.info("privateKey:" + privateKey);
                if (StringUtils.isNotEmpty(privateKey)) {
                    //logger.info("1");
                    sftp = new SFTPUtil(username, host, port, privateKey);
                } else {
                    //logger.info("2");
                    sftp = new SFTPUtil(username, password, host, port);
                }
                sftp.login();
                is = new FileInputStream(file);
                sftp.upload(filePath, fileName, is, charset);
                sftp.logout();
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else if ("ftp".equals(method)) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                result = FtpUtil.uploadFile(host, port, username, password, filePath, fileName, is);
                logger.info("文件上传是否成功：" + String.valueOf(result));
            } catch (FileNotFoundException e) {
                throw new Exception(e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } else {
            logger.error("传输方式填写错误：method[" + method + "]");
            throw new Exception("传输方式填写错误：method[" + method + "]");
        }
    }

    /**
     * 文件下载
     *
     * @param mailbox    信箱编号
     * @param remotePath 服务器路径
     * @param fileName   下载文件名
     * @param localPath  本地保存路径
     * @return
     * @throws Exception
     */
    public void download(String mailbox, String remotePath, String fileName, String localPath) throws Exception {
        String method = null;// 传输方式
        String host = null;// 主机名
        int port = 0;// 端口
        String username = null;// 用户名
        String password = null;// 密码
        String privateKey = null;// 公钥
        String charset = null;// 字符集

        SftpConfig sftpConfigByMailbox = sftpConfigService.getSftpConfigByMailbox(mailbox);
        method = sftpConfigByMailbox.getMethod();
        port = Integer.parseInt(sftpConfigByMailbox.getPort());
        host = sftpConfigByMailbox.getHost();
        username = sftpConfigByMailbox.getUsername();
        password = sftpConfigByMailbox.getPassword();
        privateKey = sftpConfigByMailbox.getPrivateKey();
        charset = sftpConfigByMailbox.getCharset();


        if ("sftp".equals(method)) {
            SFTPUtil sftp = null;
            if (StringUtils.isNotEmpty(privateKey)) {
                logger.info("密钥连接");
                sftp = new SFTPUtil(username, host, port, privateKey);
            } else {
                logger.info("密码连接");
                sftp = new SFTPUtil(username, password, host, port);
            }
            sftp.login();
            sftp.download(remotePath, fileName, localPath + "/" + fileName,charset);
            sftp.logout();
        } else if ("ftp".equals(method)) {
            FtpUtil ftp = new FtpUtil();
            boolean flag = ftp.downloadFile(host, port, username, password, remotePath, fileName, localPath);
            logger.info("文件下载是否成功：" + String.valueOf(flag));
        } else {
            logger.error("传输方式填写错误：method[" + method + "]");
        }
    }

    /**
     * 文件打包下载
     * @param mailbox    信箱编号
     * @param remotePath 服务器路径
     * @param zipName   本地路径+本地打包文件名
     * @param localPath  本地保存路径
     * @return
     * @throws Exception
     */
        public void downloadFileDir(String mailbox, String remotePath, String zipName, String localPath) throws Exception {
        String method = null;// 传输方式
        String host = null;// 主机名
        int port = 0;// 端口
        String username = null;// 用户名
        String password = null;// 密码
        String privateKey = null;// 公钥
        String charset = null;// 字符集

        SftpConfig sftpConfigByMailbox = sftpConfigService.getSftpConfigByMailbox(mailbox);
        method = sftpConfigByMailbox.getMethod();
        port = Integer.parseInt(sftpConfigByMailbox.getPort());
        host = sftpConfigByMailbox.getHost();
        username = sftpConfigByMailbox.getUsername();
        password = sftpConfigByMailbox.getPassword();
        privateKey = sftpConfigByMailbox.getPrivateKey();
        charset = sftpConfigByMailbox.getCharset();


        if ("sftp".equals(method)) {
            SFTPUtil sftp = null;
            if (StringUtils.isNotEmpty(privateKey)) {
                logger.info("密钥连接");
                sftp = new SFTPUtil(username, host, port, privateKey);
            } else {
                logger.info("密码连接");
                sftp = new SFTPUtil(username, password, host, port);
            }
            sftp.login();
            sftp.downloadFileDir(remotePath,localPath,charset);
            sftp.logout();
        }
        File file = new File(localPath);
        //文件打包
        ZipUtil.zipCompress(zipName,file);
        //暂不支持ftp有需求再做
    }
}
