package com.kayak.utils;

import com.esb.spdbank.ftp.socket.client.EFTClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName EsbFileUtil
 * @Description Esb 文件处理 util
 * @Author zhangzhen
 * @Date 2025/04/18 9:41
 * @Version 1.0
 */
@Slf4j
public class EsbFileUtil {
    /**
     * 从esb 下载所需文件
     *
     * @param serverPath    需要下载的esb文件 路径地址
     * @param clientPath    本服务器下载路径
     * @param tradeCode     交易码
     * @param hostNo        服务器标识,客户端配置文件client.cfg中配置的HOSTNO
     * @return
     */
    public static Boolean esbDownload(String serverPath, String clientPath, String tradeCode, String hostNo) throws Exception {
        String configPath = getConfigPath();
        log.info("读取容器 config路径信息-->" + configPath);
        // cfg配置
        System.setProperty("esb.cfg.path", configPath);
        // 日志编码
        System.setProperty("file.encoding", "GBK");
        EFTClient eftClient = new EFTClient();
        // 是否加密
        boolean encryptRequired = false;
        // 是否校验 MD5
        boolean checkSumREquired = false;
        // 下载文件
        Boolean flag = eftClient.download(hostNo, clientPath, serverPath, tradeCode, encryptRequired, checkSumREquired);
        // 删除临时配置文件
        deleteAllFiles(new File(configPath));

        return flag;
    }

    /**
     * @return String
     */
    public static String getConfigPath() throws Exception {
        try {
            // 创建临时目录
            Path tempDir = Files.createTempDirectory("esb");
            File tempDirFile = tempDir.toFile();
            String tempDirPath = tempDirFile.getAbsolutePath();
            // 将配置文件全部写入到临时路径下
            List<String> folders = new ArrayList<>(Arrays.asList("client", "monitor", "struct"));
            for (String folder : folders) {
                copyEsbFilesToTemp(folder, new File(tempDirPath+"/"+folder));
            }
            return tempDir.toAbsolutePath().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 将 resource/config/esb 下所有文件拷贝到临时路径
     * @param folder
     * @param parent
     * @return String
     */
    public static void copyEsbFilesToTemp(String folder, File parent) throws Exception {
        try {
            // 获取目录下所有文件
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("config/esb/"+folder+"/*");
            // 确保父目录存在
            if (!parent.exists()) {
                parent.mkdirs();
            }
            // 遍历资源并拷贝
            for (Resource resource : resources) {
                try (InputStream is = resource.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(parent, resource.getFilename()))) {
                    FileCopyUtils.copy(is, fos);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除临时路径下的配置文件
     * @param file
     */
    public static void deleteAllFiles(File file) {
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteAllFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
    }

}