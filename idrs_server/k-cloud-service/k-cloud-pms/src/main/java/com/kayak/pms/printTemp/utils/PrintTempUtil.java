package com.kayak.pms.printTemp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

/**
 * @program: k-cloud
 * @description: 文档模板工具类
 * @author: WangZhenXin
 * @create: 2020-12-28 10:17
 * @memo 备注信息
 */
public class PrintTempUtil {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempUtil.class);

    /**
     * 为文件名增加版本信息
     * @param fileName 文件名
     * @param version 版本号
     * @return 带版本号的文件名
     */
    public static String versionToFile(String fileName,String version){
        StringBuilder versionFileName = new StringBuilder();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String fileEntityName = fileName.substring(0, fileName.lastIndexOf("."));
        versionFileName.append(fileEntityName).append(version).append(fileType);
        return versionFileName.toString();
    }

    public static String getRootPath(){
        String classPath = PrintTempUtil.class.getClassLoader().getResource("").getPath();
        String rootPath  = "";
        //windows下
        if("\\".equals(File.separator)){
            rootPath  = classPath.substring(1);
            rootPath = rootPath.replace("/", "\\");
        }
        //linux下
        if("/".equals(File.separator)){
            rootPath = rootPath.replace("\\", "/");
        }
        logger.info("获取项目路径:{}",rootPath);
        return rootPath;
    }
    
    

}
