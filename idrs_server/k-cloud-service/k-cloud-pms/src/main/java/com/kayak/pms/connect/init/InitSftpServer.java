package com.kayak.pms.connect.init;

import com.kayak.config.ConfigUitl;
import com.kayak.pms.connect.service.SftpConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @program: k-cloud
 * @description: 初始化sftp配置类
 * @author: WangZhenXin
 * @create: 2020-12-31 10:56
 * @memo 备注信息
 */
@Service
public class InitSftpServer {
    private static final Logger logger = LoggerFactory.getLogger(InitSftpServer.class);

    boolean isInit = false;

    @Autowired
    private SftpConfigService sftpConfigService;

    @PostConstruct
    public void init() {
        synchronized (InitSftpServer.class) {
            if (!isInit) {
                try {
                    initServer();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            isInit = true;
        }
    }

    private void initServer() throws Exception {
        // 监听模型变化，刷新网关缓存，以及graphQL配置
        ConfigUitl.addNacosConfigListener("k-pms-sftp", (String config) -> {
            try {
                sftpConfigService.loadSftpConfig();
                logger.info("sftp服务配置刷新");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        });
    }

}
