package com.kayak.utils;

import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final String DECLAREPATH = "declfile";//申报文件存储路径

    public static final String TEMPLATEPATH = "template";//模板存储路径

    public static final String DOCUMENTPATH = "document";//文档存储路径

    public static final String CACHEPATH = "cache";//下载缓存路径

    public static final String HISTORYPATH = "history";//历史文件存储路径

    public static final String IMPORT = "import";//数据导入文件存储路径

    /**
     * 默认的文件上传根目录
     */
    public final static String UPLOAD_PATH = "/upload";

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        if (StringUtils.isBlank(path))
            return false;
        return delAllFile(new File(path));
    }
    public static boolean delAllFile(File file) {
        boolean flag = false;
        boolean bool = false;
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            bool = file.delete();
            flag = true;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        assert tempList != null;
        for (String s : tempList) {
            temp = new File(file,s);
            if (temp.isFile()) {
                bool = temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(temp);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    public static boolean delFile(String path) {
        File file = new File(path);
        boolean flag = false;
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        }
        return flag;
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
            throw new Exception(e.getMessage());
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
     * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
     * zip.jar
     * @param sourceFilePath
     *            待压缩的文件路径
     * @param zipFilePath
     *            压缩后存放路径
     * @param fileName
     *            压缩后文件的名称
     * @return flag
     */
    public static boolean fileToZip(String sourceFilePath,String zipFilePath, String fileName, String encode) throws Exception {

        sourceFilePath = sourceFilePath.endsWith(File.separator) ? sourceFilePath : sourceFilePath + File.separator;
        zipFilePath = zipFilePath.endsWith(File.separator) ? zipFilePath : zipFilePath + File.separator;

        File sourceFile = new File(sourceFilePath);
        File zipFile = new File(zipFilePath + fileName);

        if(!zipFile.getParentFile().exists()){
            zipFile.getParentFile().mkdirs();
        }
        if(!sourceFile.exists()){
            sourceFile.mkdirs();
        }
        if (!sourceFile.exists()) {
            throw new Exception("待压缩的文件目录：" + sourceFilePath + " 不存在.");
        }
        if (new File(zipFilePath).exists()) {
            // 如果缓存目录下存在此文档则删除目录下文档删除zip缓存文档
            delAllFile(zipFilePath);
        }
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zos = null;
        try{
            fileOutputStream = new FileOutputStream(zipFile);
            //创建zip输出流
            zos = new ZipOutputStream(fileOutputStream);
            zos.setEncoding(encode);
            compress(zos, sourceFile, sourceFile.getName());
            zos.close();
            return true;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return false;
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
                assert fileList != null;
                if(fileList.length==0){
                    //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    zos.putNextEntry(new ZipEntry(fileName + "/"));
                }else{
                    //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for(File file : fileList){
                        compress(zos, file, fileName + File.separator + file.getName());
                    }
                }
            }else{
                if(!sourceFile.exists()){
                    zos.putNextEntry(new ZipEntry("/"));
                    zos.closeEntry();
                }else{
                    //单个文件，直接将其压缩到zip包中
                    ZipEntry entry = new ZipEntry(fileName);
                    entry.setUnixMode(644);
                    zos.putNextEntry(entry);
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
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
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
        return "";
    }

    public static String fileSaveToLocal(MultipartFile file, String fileName) {
        try {
            //获取本地缓存根目录
            String localPath = getFileStorePath() ;
                    //根据上传文件后缀判断文件类型
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = StringUtils.lowerCase(originalFilename.substring(originalFilename.lastIndexOf(".")));
            switch (fileSuffix) {
                case ".doc":
                case ".docx":
                    localPath = localPath + "word";
                    break;
                case ".xls":
                case ".xlsx":
                    localPath = localPath + "excel";
                    break;
                default:
                    localPath = localPath + "file";
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
    public static String fileSaveToLocal(MultipartFile file) {
        //根据上传文件后缀判断文件类型
        String originalFilename = file.getOriginalFilename();

        return fileSaveToLocal(file, originalFilename);
    }

    /**
     * 将上传文件转存到本地服务器
     *
     * @return 文件本地转存路劲
     */
    public static String fileSaveToLocalForMeeting(MultipartFile file, String fileName, String filePath) {
        try {
            //获取本地缓存根目录
            String localPath = getFileStorePath();
            localPath = localPath + filePath;

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
     * 获取服务器本地下载路径
     * @return 下载路径
     */
    public static String getFileDownloadLocalPath() {
        String localPath = "";
        try {
            localPath = getFileStorePath()  + "download";
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
            localPath = getFileStorePath() + "downloadZip";
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
            localPath = getFileStorePath() + "printTemp";
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
            localPath = getFileStorePath() + File.separator + "prodDocument";
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


    /**
     * 通用的上传接口，上传根目录为/upload
     *
     * @param temFile
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static File upload(MultipartFile temFile) throws Exception {
        return upload(temFile, null);
    }




    /**
     * 根据当前的时间建立文件夹，时间格式yyyyMMdd
     *
     * @param path
     * @param extension 后缀名
     * @return
     */
    private static String buildFilePathByExtension(String path, String extension, String fileName) {
        // 创建文件夹
        return getFilePath(path) + generateFileNameByExtension(extension, fileName);
    }


    /**
     * 获取文件随机目录
     *
     * @param path
     * @return
     */
    private static String getFilePath(String path) {
        // 获取当前日期
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatDate = format.format(new Date());

        // 创建文件夹
        String filePath = path + '/' + formatDate + '/';

        return filePath;
    }


    /**
     * 根据后缀名生成文件名
     *
     * @param extension
     * @return
     */
    private static String generateFileNameByExtension(String extension, String fileName) {
        DateFormat format = new SimpleDateFormat("HHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        String fileNameS = fileName.substring(0, fileName.lastIndexOf("."));
        if (Tools.strIsEmpty(extension)) {
            return fileNameS + formatDate + random;
        } else {
            return fileNameS + formatDate + random + "." + extension;
        }

    }


    public static void download(File file, HttpServletResponse res) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename
        bis = new BufferedInputStream(new FileInputStream(file));
        int i = bis.read(buff);
        while (i != -1) {
            outputStream.write(buff, 0, buff.length);
            outputStream.flush();
            i = bis.read(buff);
        }
    }


    /**
     * 创意文件上传接口
     *
     * @param temFile
     * @param uploadPath 如果uploadPath为空，这使用默认的上传根目录/upload
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static File uploadOriginality(MultipartFile temFile, String uploadPath) throws Exception {

        if (temFile == null) {
            throw new Exception("上传文件为空");
        }

        // 请限制上传文件的大小(M)
        long size = temFile.getSize() / 1024 / 1024;
        if (size > 1024) {
            throw new Exception("上传文件不能大于1G");
        }
        String fileName = temFile.getOriginalFilename().toLowerCase();

        if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                || fileName.endsWith(".html")) {
            throw new Exception("禁止上传脚本");
        }

        if (!Tools.strIsEmpty(uploadPath)) {// 如果有指定上传的文件夹，这使用上传的文件夹作为上传目录
            if (!uploadPath.startsWith("/")) {
                uploadPath = "/" + uploadPath;
            }
        } else {// 使用默认的上传根目录
            uploadPath = UPLOAD_PATH;
        }
//        String path = generateFileNameByOriginality(uploadPath,extension,originalityName,count);
        String path = uploadPath + "/" + temFile.getOriginalFilename().toLowerCase();
        File uploadFile = new File(path);

        uploadFile.mkdirs();

        temFile.transferTo(uploadFile);

        return uploadFile;
    }

    /**
     * 通用的上传接口
     *
     * @param temFile
     * @param uploadPath 如果uploadPath为空，这使用默认的上传根目录/upload
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static File upload(MultipartFile temFile, String uploadPath) throws Exception {

        if (temFile == null) {
            throw new Exception("上传文件为空");
        }

        // 请限制上传文件的大小(M)
        long size = temFile.getSize() / 1024 / 1024;
        if (size > 1024) {
            throw new Exception("上传文件不能大于1G");
        }
        String fileName = temFile.getOriginalFilename().toLowerCase();

        if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                || fileName.endsWith(".html")) {
            throw new Exception("禁止上传脚本");
        }

        String extension = null;
        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if (!Tools.strIsEmpty(uploadPath)) {// 如果有指定上传的文件夹，这使用上传的文件夹作为上传目录
            if (!uploadPath.startsWith("/")) {
                uploadPath = "/" + uploadPath;
            }
        } else {// 使用默认的上传根目录
            uploadPath = UPLOAD_PATH;
        }
        String path = buildFilePathByExtension(uploadPath, extension, fileName);
        File uploadFile = new File(path);

        uploadFile.mkdirs();

        temFile.transferTo(uploadFile);

        return uploadFile;
    }


    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        String zipFilePath = "C:/kkweb/declare/document/axin测试.zip";
        File zipFileDir = new File(zipFilePath);
        File zipFileDir2 = new File("C:/kkweb/declare/document/axin01");
        fos = new FileOutputStream(zipFileDir);
        zos = new ZipOutputStream(new BufferedOutputStream(fos));
        zos.setEncoding("UTF-8");
        FileUtil.compress(zos,zipFileDir2,"axin测试");
    }

}
