package com.kayak.schedule.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileZipUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileZipUtils.class);
    /**
     * 将指定文件进行压缩
     * @param zipFileName 压缩存放绝对路径
     * @param fileList  文件集合需要压缩的文件，不能是文件夹
     * @throws IOException
     */
    public static void zipMutipleFiles(String zipFileName, List<File> fileList) throws IOException
    {
        if(fileList.size()<0) return;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        for (int i = 0; i < fileList.size(); i++)
        {
            out.putNextEntry(new ZipEntry(fileList.get(i).getName())); // 创建zip压缩进入点base
            FileInputStream in = new FileInputStream(fileList.get(i));
            int b;
            while ((b = in.read()) != -1) {
                out.write(b); // 将字节流写入当前zip目录
            }
            in.close(); // 输入流关闭
            fileList.get(i).delete();
        }
        out.close();
    }

    /**
     * 压缩文件或文件夹
     * @param zipFileName 压缩文件的绝对路径
     * @param inputFile  文件或文件夹对象
     * @throws Exception
     */
    public static void zip(String zipFileName, File inputFile) throws Exception {
        logger.info("压缩中......");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, inputFile.getName());
        out.flush();
        out.close(); // 输出流关闭
        logger.info("压缩完成......");
    }

    private static void zip(ZipOutputStream out, File f, String base
    ) throws Exception  { // 方法重载
        try{
            if (f.isDirectory()) {
                File[] fl = f.listFiles();
                if (fl.length == 0) {
                    out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                }
                for (int i = 0; i < fl.length; i++) {
                    zip(out, fl[i], base + "/" + fl[i].getName()); // 递归遍历子文件夹
                }
            } else {
                out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
                FileInputStream in = new FileInputStream(f);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b); // 将字节流写入当前zip目录
                }
                in.close(); // 输入流关闭
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            throw new Exception("压缩失败");
        }
    }
  
}
