package com.kayak.dps.ods.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublicFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(PublicFileUtil.class);

    public static List<File> getFilesWithPrefixAndExtension(String directoryPath, String prefix, String extension) {
        File directory = new File(directoryPath);
        List<File> resultList = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(prefix) && file.getName().endsWith(extension)) {
                    resultList.add(file);
                }
            }
        }
        logger.info("在目录：【"+directoryPath+"】下符合【"+prefix+"】开头，"+extension+"结尾的文件共有："+resultList.size()+"个");
        return resultList;
    }

    public static List<File> getFilesWithPrefixAndExtension(String directoryPath, String fileName, String extension, String port_table) {
        File directory = new File(directoryPath);
        List<File> resultList = new ArrayList<>();
        File[] files = directory.listFiles();
        // 规格文件名特殊处理
        String fileName2 ="";
        fileName = fileName.substring(0,fileName.lastIndexOf("-"));
        if("rms_stg_asset_valuation".equals(port_table)||"rms_stg_asset_valuation_obj".equals(port_table)){
            for(File file:files) {
                fileName2 = file.getName().substring(0, file.getName().indexOf("-"));
                if(fileName.equals(fileName2)||file.getName().equals(fileName)){
                    resultList.add(file);
                }
            }
        }
        logger.info("在目录：【"+directoryPath+"】下符合【"+fileName+"】开头，"+extension+"结尾的文件共有："+resultList.size()+"个");
        return resultList;
    }

}
