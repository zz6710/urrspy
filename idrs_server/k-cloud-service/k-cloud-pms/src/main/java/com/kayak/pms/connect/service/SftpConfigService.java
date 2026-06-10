package com.kayak.pms.connect.service;

import com.kayak.pms.connect.dao.SftpConfigDao;
import com.kayak.pms.connect.model.SftpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: k-cloud
 * @description: sftp连接服务类
 * @author: WangZhenXin
 * @create: 2020-12-31 11:46
 * @memo 备注信息
 */
@Service
public class SftpConfigService {
    private static final Logger logger = LoggerFactory.getLogger(SftpConfigService.class);

    @Autowired
    private SftpConfigDao sftpConfigDao;

    private List<SftpConfig> sftpConfigList = new ArrayList<>();

    boolean isInit = false;

    /**
     * 在项目启动时加载配置文件信息
     */
    @PostConstruct
    public void init() {
        synchronized (SftpConfigService.class) {
            if (!isInit) {
                try {
                    loadSftpConfig();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.error("初始化Sftp配置异常");
                }
            }
            isInit = true;

        }

    }

    /**
     * 初始化配置文件列表
     */
    public void loadSftpConfig(){
        sftpConfigList.clear();
        try {
            sftpConfigList = sftpConfigDao.getAllSftpConfig();
        } catch (Exception e) {
            logger.error("初始化sftp配置失败", e);
        }
    }

    /**
     * 通过邮箱名称获取配置信息
     * @param mailBox 信箱代码
     * @return 配置信息
     */
    public SftpConfig getSftpConfigByMailbox(String mailBox) throws Exception {
        SftpConfig sftpConfigs = null;
        try {
             sftpConfigs = sftpConfigList.stream().filter(sftpConfig -> sftpConfig.getMailbox().equals(mailBox)).collect(Collectors.toList()).get(0);
        }catch (Exception e){
            throw new Exception("信箱信息不存在或信箱信息错误");
        }
        return sftpConfigs;
    }





}
