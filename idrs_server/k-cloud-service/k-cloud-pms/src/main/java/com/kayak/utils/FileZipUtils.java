package com.kayak.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    /**
     * 文件下载
     *
     * @param response 响应题
     * @param path 文件路径
     * @throws Exception
     */
    public static void downFile(HttpServletResponse response, String path) throws Exception {
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            File file = new File(path);
            if (file.isDirectory() || !file.exists()) {
                logger.error("文件不存在！！,文件路径:{}", path);
                throw new Exception("文件不存在!!");
            }
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
