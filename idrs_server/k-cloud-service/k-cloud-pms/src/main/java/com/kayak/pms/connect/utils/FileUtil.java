package com.kayak.pms.connect.utils;

import com.kayak.core.system.SysUtil;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

/**
 * @program: k-cloud
 * @description: 文件处理工具类
 * @author: WangZhenXin
 * @create: 2020-12-31 17:30
 * @memo 备注信息
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 将上传文件转存到本地服务器
     *
     * @return 文件本地转存路劲
     */
    public static String fileSaveToLocal(MultipartFile file, String fileName) {
        try {
            //获取本地缓存根目录
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            String localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            //根据上传文件后缀判断文件类型
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = StringUtils.lowerCase(originalFilename.substring(originalFilename.lastIndexOf(".")));
            switch (fileSuffix) {
                case ".doc":
                case ".docx":
                    localPath = localPath + separator + "word";
                    break;
                case ".xls":
                case ".xlsx":
                    localPath = localPath + separator + "excel";
                    break;
                default:
                    localPath = localPath + separator + "file";
                    break;
            }
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
            if (localPathFile.isDirectory()) {
                boolean bol = delAllFile(localPath);
                if (bol) {
                    delAllFile(localPath);
                }
            }
            File localFile = new File(localPath, fileName);
            //转存文件
            file.transferTo(localFile);
            return localPath;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将上传文件转存到本地服务器
     *
     * @return 文件本地转存路劲
     */
    public static String fileSaveToLocalForMeeting(MultipartFile file, String fileName, String filePath) {
        try {
            //获取本地缓存根目录
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            String localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            localPath = localPath + separator + filePath;

            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }

            File localFile = new File(localPath, fileName);
            //转存文件
            file.transferTo(localFile);
            return localPath;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param file     文件对象
     * @param fileName 文件名称
     * @param filePath 文件目录
     * @return
     */
    public static String fileSaveToLocal(MultipartFile file, String fileName, String filePath) {
        try {

            File localPathFile = new File(filePath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }

            File localFile = new File(filePath, fileName);
            //转存文件
            file.transferTo(localFile);
            return localFile.getPath();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param file     文件对象
     * @param fileName 文件名称
     * @param filePath 文件目录
     * @return
     */
    public static String fileSaveToLocalAbsolutePath(MultipartFile file, String fileName, String filePath) {
        try {

            File localPathFile = new File(filePath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }

            File localFile = new File(new File(filePath).getAbsolutePath(), fileName);
            //转存文件
            file.transferTo(localFile);
            return localFile.getPath();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将上传文件转存到本地服务器
     *
     * @return 文件本地转存路劲
     */
    public static String fileSaveToLocal(MultipartFile file) {
        try {
            //获取本地缓存根目录
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            String localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            //根据上传文件后缀判断文件类型
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = StringUtils.lowerCase(originalFilename.substring(originalFilename.lastIndexOf(".")));
            switch (fileSuffix) {
                case ".doc":
                case ".docx":
                    localPath = localPath + separator + "word";
                    break;
                case ".xls":
                case ".xlsx":
                    localPath = localPath + separator + "excel";
                    break;
                default:
                    localPath = localPath + separator + "file";
                    break;
            }
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
            if (localPathFile.isDirectory()) {
                boolean bol = delAllFile(localPath);
                if (bol) {
                    delAllFile(localPath);
                }
            }
            File localFile = new File(localPath, originalFilename);
            //转存文件
            file.transferTo(localFile);
            return localFile.getPath();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取服务器本地下载路径
     * @return 下载路径
     */
    public static String getFileDownloadLocalPath() {
        String localPath = "";
        try {
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            if ("\\".equals(separator)) {
                localPath = localPath.replace("/", "\\");
            } else {
                localPath = localPath.replace("\\", "/");
            }
            localPath = localPath + separator + "download";
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
            if (localPathFile.isDirectory()) {
                boolean bol = delAllFile(localPath);
                if (bol) {
                    delAllFile(localPath);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return localPath;
    }

    /**
     * 获取服务器本地打包文件存放路径
     *
     * @return 打包文件存放路径
     */
    public static String getFileZipLocalPath() {
        String localPath = "";
        try {
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            if ("\\".equals(separator)) {
                localPath = localPath.replace("/", "\\");
            } else {
                localPath = localPath.replace("\\", "/");
            }
            localPath = localPath + separator + "downloadZip";
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
            if (localPathFile.isDirectory()) {
                boolean bol = delAllFile(localPath);
                if (bol) {
                    delAllFile(localPath);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return localPath;
    }

    /**
     * 获取服务器本地缓存模板路径,并删除路径下的文件
     *
     * @return 下载路径
     */
    public static String getTempFileLocalPath() {
        String localPath = "";
        try {
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            localPath = localPath + separator + "printTemp";
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
            if (localPathFile.isDirectory()) {
                boolean bol = delAllFile(localPath);
                if (bol) {
                    delAllFile(localPath);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return localPath;
    }

    /**
     * 获取服务器本地缓存产品文档路径
     *
     * @return 下载路径
     */
    public static String getTempStorePath() {
        String localPath = "";
        try {
            String localPathParaid = "";
            String separator = File.separator;
            //windows
            if ("\\".equals(separator)) {
                localPathParaid = "80000080002";
            } else {
                localPathParaid = "80000080003";
            }
            localPath = SysUtil.getSystemParamsByParaid(localPathParaid);
            localPath = localPath + separator + "prodDocument";
            File localPathFile = new File(localPath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return localPath;
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            flag = true;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + tempList[i]);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 保存产品模板数据单本地
     *
     * @param temp_html wordHtml数据
     * @param filePath  本地路径
     * @param fileName  文件名
     */
    public static void printTempFileToLocal(String temp_html, String filePath, String fileName) {
        File file = new File(filePath, fileName);
        //这里是必须要设置编码的，不然导出中文就会乱码。
        ByteArrayInputStream bais = null;
        FileOutputStream fileOutputStream = null;
        try {
            byte[] b = temp_html.getBytes("utf-8");
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(b);
        } catch (IOException e) {
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
        }
    }

    /**
     * 获取文件存放根路径
     *
     * @return
     * @throws Exception
     */
    public static String getFileStorePath() throws Exception {
        String s = "";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            s = "80000080002";
        } else {
            s = "80000080003";
        }

        String path = SysUtil.getSystemParamsByParaid(s);
        return path.endsWith("/") ? path : path + "/";
    }

    /**
     * 文件下载
     *
     * @param response 响应题
     * @param path 文件路径
     * @throws Exception
     */
    public static void downFileToBrowser(HttpServletResponse response, String path) throws Exception {
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
    /**
     * 根据文件流判断图片类型
     * @param fis
     * @return
     */
    public static String getPicType(InputStream fis){
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return "jpg";
            } else if (type.contains("89504E47")) {
                return "png";
            } else if (type.contains("47494638")) {
                return "gif";
            } else if (type.contains("424D")) {
                return "bmp";
            }else if(type.contains("52494646")){
                return "webp";
            }else if(type.contains("49492A00")){
                return "tif";
            } else {
                return "other";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * byte数组转换成16进制字符串
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void CreateSplitZipFile(String path,String SavePath,Long size) {

        /*try {
            net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(SavePath);
            ArrayList filesToAdd = new ArrayList();
            File file = new File(path);
            if(file.exists()){
                File[] files = file.listFiles();
                for(File file1 : files){
                    filesToAdd.add(file1);
                }
            }
            ZipParameters parameters = new ZipParameters();
//            parameters.setCompressionMethod(8);
//            parameters.setCompressionLevel(5);
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            zipFile.createSplitZipFile(filesToAdd, parameters, true, size);
        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }*/
    }
    public static void checkPath(String filePath) {
        File localPathFile = new File(filePath);
        if (!localPathFile.exists() && !localPathFile.isDirectory()) {
            localPathFile.mkdirs();
        }
        if (localPathFile.isDirectory()) {
            boolean bol = delAllFile(filePath);
            if (bol) {
                delAllFile(filePath);
            }
        }
    }


    //方法一，以字节流方式
    //如果输出输入流的创建不在try()里，记得使用close()方法来关闭，否则会造成资源的浪费
    public static void fileCopyOne(File f,File f2){
        try (
                FileInputStream fis=new FileInputStream(f);//创建字节输入流
                FileOutputStream fos=new FileOutputStream(f2);//创建字节输出流
        ){
            byte[] all=new byte[(int)f.length()];
            fis.read(all);//读取文件数据
            fos.write(all);//写入文件数据
            //System.out.println("复制完成");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //方法二，以字符流方式
    public static void fileCopyTwo(File f,File f2){
        try (
                FileReader fr=new FileReader(f);//创建字符输入流
                FileWriter fw=new FileWriter(f2)//创建字符输出流
        ){
            char[] all= new char[(int) f.length()];
            fr.read(all);//读取文件数据
            fw.write(all);//写入文件数据
            //System.out.println("复制完成");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
