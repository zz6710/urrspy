package com.kayak.dps.utils;

import com.kayak.core.system.SysUtil;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final String DECLAREPATH = "declfile";//申报文件存储路径

    public static final String TEMPLATEPATH = "template";//模板存储路径

    public static final String DOCUMENTPATH = "document";//文档存储路径

    public static final String CACHEPATH = "cache";//下载缓存路径

    public static final String HISTORYPATH = "history";//历史文件存储路径

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
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
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

    /**
     * 读取文件
     *
     * @param pathName 要写入文件的全路径和全名称
     * @param charset  编码格式
     * @return 写入成功返回true
     */
    public static String readFile(String pathName, String charset) {
        if(pathName == null){
            return "";
        }
        return readFile(new File(pathName), charset);
    }

    public static String readFile(File file, String charset) {
        String content;
        try (InputStream fr = new FileInputStream(file); BufferedReader input = new BufferedReader(new InputStreamReader(fr, charset))) {
            StringBuilder buffer = new StringBuilder();
            String text;
            while ((text = input.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(text);
            }
            content = buffer.toString();
        } catch (Exception e) {
            content = "";
        }


        return content;
    }
}
