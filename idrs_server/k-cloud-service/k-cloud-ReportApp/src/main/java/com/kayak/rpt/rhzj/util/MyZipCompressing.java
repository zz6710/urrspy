package com.kayak.rpt.rhzj.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 程序实现了ZIP压缩。共分为2部分 ： 压缩（compression）与解压（decompression）
 * <p>
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。
 * <p>
 * 在本段代码中，实现的是压缩部分；解压部分见本包中Decompression部分。
 *
 * @author HAN
 */

public class MyZipCompressing {

    public MyZipCompressing() {
    }

    public static void zip(String zipFileName, File inputFile) throws Exception {
        System.out.println("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, inputFile.getName());
        out.close(); // 输出流关闭  
        System.out.println("压缩完成");
    }

    private static void zip(ZipOutputStream out, File f, String base
    ) throws Exception { // 方法重载
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("压缩失败");
        }
    }

    /**
     * 压缩文件删除源文件
     *
     * @param zipFileName
     * @param fileList
     * @throws IOException
     */
    public static void zipMutipleFiles(String zipFileName, List<File> fileList) throws IOException {
        if (fileList.size() < 0) return;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        for (int i = 0; i < fileList.size(); i++) {
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
     * 压缩文件不删除源文件
     *
     * @param zipFileName
     * @param fileList
     * @throws IOException
     */
    public static void zipMultipleFiles2(String zipFileName, List<File> fileList) throws IOException {
        if (fileList.size() < 0) return;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        for (int i = 0; i < fileList.size(); i++) {
            out.putNextEntry(new ZipEntry(fileList.get(i).getName())); // 创建zip压缩进入点base
            FileInputStream in = new FileInputStream(fileList.get(i));
            int b;
            while ((b = in.read()) != -1) {
                out.write(b); // 将字节流写入当前zip目录
            }
            in.close(); // 输入流关闭
        }
        out.close();
    }
}