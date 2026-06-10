package com.kayak.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * @program: k-cloud
 * @description: 文件压缩工具类
 * @author: WangZhenXin
 * @create: 2021-01-07 14:38
 * @memo 备注信息
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
    private static final int  BUFFER_SIZE = 2 * 1024;

    public static void zipCompress(String zipSavePath, File sourceFile){
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zos = null;
        try{
            fileOutputStream = new FileOutputStream(zipSavePath);
            //创建zip输出流
            zos = new ZipOutputStream(fileOutputStream);
            zos.setEncoding("GBK");
            compress(zos, sourceFile, sourceFile.getName());
            zos.close();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (zos != null) {
                try {
                    zos.flush();
                    zos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 功能：压缩文件
     * 作者:rennannan
     * 日期：20210721
     *
     * @param zipSavePath   压缩文件保存路径
     * @param sourceFile    源文件
     * @param keepStructure 是否保存原目录结构
     */
    public static void FileCompress(String zipSavePath, File sourceFile, boolean keepStructure) {
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zos = null;
        try {
            fileOutputStream = new FileOutputStream(zipSavePath);
            //创建zip输出流
            zos = new ZipOutputStream(fileOutputStream);
            zos.setEncoding("GBK");
            compressFile(zos, sourceFile, sourceFile.getName(), keepStructure);
            zos.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (zos != null) {
                try {
                    zos.flush();
                    zos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @param rootPath      解压路径
     * @param unZipFileName 需要解压的zip文件名
     * @return key-文件路径 value-文件名
     */
    public static Map<String, String> unZipCompress(String rootPath, String unZipFileName) {
        ZipFile readfile = null;
        Map<String, String> returnMap = new HashMap<>();
        try {
            readfile = new ZipFile(unZipFileName, "GBK");
            Enumeration takeentrie = readfile.getEntries();
            ZipEntry zipEntry = null;
            File file = new File(rootPath);
            //文件夹不存在的话创建文件夹
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            while (takeentrie.hasMoreElements()) {
                zipEntry = (ZipEntry) takeentrie.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        int index = entryName.lastIndexOf("\\");
                        int indexOf =entryName.indexOf("\\");
                        if (index != -1) {
                            File createDirectory = new File(rootPath + File.separator + entryName.substring(indexOf, index));
                            createDirectory.mkdirs();
                        }else {
                            index = entryName.lastIndexOf("/");
                            indexOf =entryName.indexOf("/");
                            if (index != -1) {
                                File createDirectory = new File(rootPath + File.separator + entryName.substring(indexOf, index));
                                createDirectory.mkdirs();
                            }
                        }
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        int indexOf =entryName.indexOf("\\");
                        if (index != -1) {
                            File createDirectory = new File(rootPath + File.separator + entryName.substring(indexOf, index));
                            createDirectory.mkdirs();
                        }else {
                            index = entryName.lastIndexOf("/");
                            indexOf =entryName.indexOf("/");
                            if (index != -1) {
                                File createDirectory = new File(rootPath + File.separator + entryName.substring(indexOf, index));
                                createDirectory.mkdirs();
                            }
                        }
                        File unpackfile = new File(rootPath + File.separator + entryName.substring(indexOf, entryName.length()));
                        in = readfile.getInputStream(zipEntry);
                        out = new FileOutputStream(unpackfile);
                        int c;
                        byte[] by = new byte[1024];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                        returnMap.put(entryName,entryName.substring(indexOf+1,index+1));
                    }
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new IOException("解压失败：" + ex.toString());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (readfile != null) {
                try {
                    readfile.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return returnMap;
    }

    /**
     * 递归打包文件
     * @param zos zip流
     * @param sourceFile 打包文件夹
     * @param fileName 文件名
     */
    private static void compress(ZipOutputStream zos, File sourceFile, String fileName){
        try {
            if(sourceFile.isDirectory()){
                //如果是文件夹，取出文件夹中的文件（或子文件夹）
                File[] fileList = sourceFile.listFiles();
                if(fileList.length==0){
                    //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    zos.putNextEntry(new ZipEntry(fileName + "/"));
                }else{
                    //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for(File file : fileList){
                        compress(zos, file, fileName + "/" +file.getName());
                    }
                }
            }else{
                if(!sourceFile.exists()){
                    zos.putNextEntry(new ZipEntry("/"));
                    zos.closeEntry();
                }else{
                    //单个文件，直接将其压缩到zip包中
                    zos.putNextEntry(new ZipEntry(URLDecoder.decode(fileName,"UTF-8")));
                    FileInputStream fis = new FileInputStream(sourceFile);
                    byte[] buf = new byte[1024];
                    int len;
                    //将源文件写入到zip文件中
                    while((len=fis.read(buf))!=-1){
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 功能:递归打包文件
     * 作者：rennannan
     * 日期：20210721
     *
     * @param zos        zip流
     * @param sourceFile 打包文件夹
     * @param fileName   文件名
     *                   keepStructure:是否保留原来目录结构
     */
    private static void compressFile(ZipOutputStream zos, File sourceFile, String fileName, boolean keepStructure) {
        try {
            if (sourceFile.isDirectory()) {
                //如果是文件夹，取出文件夹中的文件（或子文件夹）
                File[] fileList = sourceFile.listFiles();
                if (fileList.length == 0) {
                    //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    zos.putNextEntry(new ZipEntry(fileName + "/"));
                } else {
                    //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for (File file : fileList) {
                        if (keepStructure) {
                            compress(zos, file, fileName + "/" + file.getName());
                        } else {
                            compress(zos, file, file.getName());
                        }

                    }
                }
            } else {
                if (!sourceFile.exists()) {
                    zos.putNextEntry(new ZipEntry("/"));
                    zos.closeEntry();
                } else {
                    //单个文件，直接将其压缩到zip包中
                    zos.putNextEntry(new ZipEntry(URLDecoder.decode(fileName, "UTF-8")));
                    FileInputStream fis = new FileInputStream(sourceFile);
                    byte[] buf = new byte[1024];
                    int len;
                    //将源文件写入到zip文件中
                    while ((len = fis.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     * false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));

            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));

                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }


    
    public static void conmpress(File zipFile,File Backup) throws Exception{
        FileOutputStream zipfos = null;
        java.util.zip.ZipOutputStream zipOs = null;
        CheckedOutputStream cos = null;
        try {
            zipfos = new FileOutputStream(zipFile);
            cos = new CheckedOutputStream(zipfos, new CRC32());
            zipOs = new java.util.zip.ZipOutputStream(cos);
            com.kayak.core.util.FileUtil.compress(Backup, zipOs, "");
            if (zipFile.exists()) {
                String systems = System.getProperty("os.name");
                if(systems.toLowerCase().startsWith("win")){

                }else{
                    // 写完的日志文件权限改为400
                    try {
                        //linux上才可以运行,windows上需要装cygwin并且把cygwin的bin目录加到环境变量的path中才可以
                        Runtime.getRuntime().exec("chmod 400 -R " + zipFile);
                    } catch (IOException e) {
                        logger.error("set archive file:{} permision catch an error: {}", zipFile, e);
                    }
                }
            }
        } finally {
            if (null != zipOs) {
                zipOs.close();
            }
            if (null != cos) {
                cos.close();
            }
            if (null != zipfos) {
                zipfos.close();
            }
        }
    }


    /**
     * 将图片压缩到指定大小以内
     *
     * @param maxSize 目的图片大小
     * @return
     * @author CY
     * @date 2020年11月18日
     */
    public static void compressUnderSize(File imageFile, long maxSize, File zipFile) throws IOException {
        byte[] data = getByteByPic(imageFile);
        byte[] imgData = Arrays.copyOf(data, data.length);

        while (imgData.length > maxSize*10){
            try {
                imgData = compress(imgData, 0.1);
            } catch (IOException e) {
                throw new IllegalStateException("压缩图片过程中出错,请及时联系管理员!", e);
            }
        }
        while (imgData.length > maxSize){
            try {
                imgData = compress(imgData, 0.7);
            } catch (IOException e) {
                throw new IllegalStateException("压缩图片过程中出错,请及时联系管理员!", e);
            }
        }
        byteToImage(imgData, zipFile);
    }

    /**
     * 获取图片文件字节
     *
     * @param imageFile
     * @return
     * @throws IOException
     * @author CY
     * @date 2020年11月18日
     */
    public static byte[] getByteByPic(File imageFile) throws IOException {
        InputStream inStream = new FileInputStream(imageFile);
        BufferedInputStream bis = new BufferedInputStream(inStream);
        BufferedImage bm = ImageIO.read(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String imageUrl = imageFile.getAbsolutePath();
        String type = imageUrl.substring(imageUrl.length() - 3);
        ImageIO.write(bm, type, bos);
        bos.flush();
        byte[] data = bos.toByteArray();
        return data;
    }

    /**
     * 按照宽高比例压缩
     *
     * @param srcImgData 待压缩图片输入流
     * @param scale 压缩刻度
     * @return
     * @throws IOException
     * @author CY
     * @date 2020年11月18日
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
        int width = (int) (bi.getWidth() * scale); // 源图宽度
        int height = (int) (bi.getHeight() * scale); // 源图高度
        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        g.drawImage(image, 0, 0, null); // 绘制处理后的图
        g.dispose();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ImageIO.write(tag, "JPEG", bOut);
        return bOut.toByteArray();
    }

    /**
     * byte数组转图片
     *
     * @param data
     * @param path
     * @author CY
     * @date 2020年11月18日
     */
    public static void byteToImage(byte[] data, File zipFile) {
        if (data.length < 3)
            return;
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(zipFile);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
