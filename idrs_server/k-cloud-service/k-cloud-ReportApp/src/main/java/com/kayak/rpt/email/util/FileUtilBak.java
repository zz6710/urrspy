package com.kayak.rpt.email.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtilBak {

    private static final Logger log = LoggerFactory.getLogger(FileUtilBak.class);


    public static final String FILE_PATCH = "D:" + File.separator + "dis";

    //信披模板路径
    public static final String TEMP_PATCH = "TEMP_PATH";
    //信披文档路径
    public static final String DOC_PATCH = "DOC_PATH";

    //信披缓存路径
//    public static final String CACHE_PATCH = "CACHE_PATCH/" + DateUtil.getTimestamp17();
//
//    public static final String buildCopyFilePath(){
//        return FILE_PATCH + File.separator + "xpTemp" + File.separator + DateUtil.getTimestamp17();
//    }


    /**
     * 获取资源文件列表
     *
     * @param pattern
     * @return
     */
    public static Resource[] getResources(String pattern) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            return resolver.getResources(pattern);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取文件输入流
     *
     * @param path
     * @return
     */
    public static InputStream getFileInputStream(String path) {
        Resource resource = new ClassPathResource(path);

        if (resource.exists()) {
            try {
                return resource.getInputStream();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }


    /**
     * 获取文件真实路径
     *
     * @param filename
     *            文件名称（可以包含相对路径）
     * @param sc
     *            如果值传null则返回相对于classpath目录的文件路径，有传sc则返回相对于sc上下文环境的文件路径
     * @return
     */
    public static String getRealPath(String filename, ServletContext sc) {
        File file = getFile(filename, sc);
        if (file != null && file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取文件对象
     *
     * @param filename
     *            文件名称（可以包含相对路径）
     * @param sc
     *            如果值传null则从相对于classpath目录的路径去获取文件，有传sc则在相对于sc上下文环境的文件路径去获取文件
     * @return
     */
    public static File getFile(String filename, ServletContext sc) {
        log.debug("获取文件：" + filename);
        String pathname = getFilePath(filename, sc);
        log.debug("文件路径：" + pathname);
        File f;
        if (pathname != null) {
            f = new File(pathname);
            if (f.exists())// 如果成功取得文件就返回
                return f;
        }
        // 如果以上方法取不到文件，则尝试另外的方法
        if (sc != null) {
            if (StringUtils.isNotBlank(pathname)) {
                pathname = sc.getRealPath(filename);
                log.debug("文件路径2：" + pathname);
                if (StringUtils.isNotBlank(pathname)) {
                    f = new File(pathname);
                    if (f.exists())
                        return f;
                }
            }
        }
        log.error("文件不存在：" + filename);
        return null;
    }

    /**
     * 获取文件真实路径
     *
     * @param filename
     *            文件名称（可以包含相对路径）
     * @param sc
     *            如果值传null则返回相对于classpath目录的文件路径，有传sc则返回相对于sc上下文环境的文件路径
     * @return
     */
    public static String getFilePath(String filename, ServletContext sc) {
        if (filename.startsWith("classpath:")) {
            filename = filename.replace("classpath:", "");
            try {
                return FileUtilBak.class.getResource(filename).getFile();
            } catch (Exception e) {
                log.info("class.getResource exception: " + e.getMessage(), e);
                return filename;
            }
        }
        if (sc == null) {
            try {
                return FileUtilBak.class.getResource(filename).getFile();
            } catch (Exception e) {
                log.info("class.getResource exception: " + e.getMessage(), e);
                return filename;
            }
        }
        try {
            URL url = sc.getResource(filename);
            if (url != null)
                return url.getFile();
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 压缩文件或目录
     *
     * @param oldFile 要压缩的文件
     * @param zipOut  压缩文件流
     * @param baseDir baseDir
     * @throws IOException
     */
    public static void compress(File oldFile, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (oldFile.isDirectory()) {
            compressDirectory(oldFile, zipOut, baseDir);
        } else {
            compressFile(oldFile, zipOut, baseDir);
        }
    }
    /**
     * 压缩目录
     *
     * @param dir     要压缩的目录
     * @param zipOut  压缩文件流
     * @param baseDir baseDir
     * @throws IOException
     */
    public static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            compress(file, zipOut, baseDir );
        }
    }
    /**
     * 压缩文件
     *
     * @param oldFile 要压缩的文件
     * @param zipOut  压缩文件流
     * @param baseDir baseDir
     * @throws IOException
     */
    public static void compressFile(File oldFile, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!oldFile.exists()) {
            return;
        }
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(oldFile));
            ZipEntry zipEntry = new ZipEntry(baseDir + oldFile.getName());
            zipOut.putNextEntry(zipEntry);
            int count;
            byte data[] = new byte[4096];
            while ((count = bis.read(data, 0, 4096)) != -1) {
                zipOut.write(data, 0, count);
            }
        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    //拷贝目录下的子文件   ignore 是否忽略当前目录
    public static void copyDir(File srcDir,File targetDir,Boolean ignore) throws IOException {
        if (srcDir != null && targetDir != null) {
            if (srcDir.isDirectory()) {
                if (!ignore) {
                    //拷贝文件夹
                    targetDir = new File(targetDir, srcDir.getName());
                    boolean b = targetDir.mkdirs();
                }
                //拷贝文件
                String[] list = srcDir.list();
                if (list != null && list.length > 0) {
                    for (String s : list) {
                        //源文件
                        File srcFile = new File(srcDir, s);
                        File targetFile = new File(targetDir, s);
                        if (srcFile.isDirectory()) {
                            ignore = false;
                            //递归调用
                            copyDir(srcFile, targetDir,ignore);
                        } else {
                            //生成文件
                            targetFile.createNewFile();
                            //开始拷贝
                            copyFile(srcFile, targetFile);
                        }
                    }
                }
            }
        }
    }

    public static void copyFile(File sourceFile,File targetFile) throws IOException {
        FileInputStream in = new FileInputStream(sourceFile);
        FileOutputStream out = new FileOutputStream(targetFile);
        byte[] bytes = new byte[1024 * 1024];
        int length = -1;
        while ((length = in.read(bytes)) != -1) {
            out.write(bytes, 0, length);
        }
        out.flush();
        out.close();
        in.close();
    }





    /**
     * 将content写入指定文件里，如果文件不存在，则创建
     *
     * @param pathName 要写入文件的全路径和全名称
     * @param content  要写入的内容
     * @param append   是否将内容追加到文件末，否则复盖文件内容
     * @param charset  编码格式
     * @return 写入成功返回true
     */
    public static Boolean writeFile(String pathName, String content, Boolean append, String charset) throws IOException {
        if(pathName == null){
            return false;
        }
        return writeFile(new File(pathName), content, append, charset);
    }

    public static Boolean writeFile(File file, String content, Boolean append, String charset) throws IOException {
        boolean bool;
        //文件夹不存在的话创建文件夹
        createNewFile(file);

        try (FileOutputStream fos = new FileOutputStream(file,append); Writer w = new OutputStreamWriter(fos, charset)) {

            w.write(content);
            bool = true;
        } catch (Exception e) {
            bool = false;
        }
        return bool;

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





    public static Boolean createNewFile(String file) throws IOException {
        if(file == null){
            return false;
        }
        return createNewFile(new File(file));
    }
    public static Boolean createNewFile(File file) throws IOException {
        File localPathFile = file.getParentFile();
        //文件夹不存在的话创建文件夹
        if (!localPathFile.exists() && !localPathFile.isDirectory()) {
            localPathFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return true;
    }


    public static String encode(String str) throws Exception {
        return URLEncoder.encode(str, "UTF-8")
                .replace("+", "%20") // 替换加号，因为某些浏览器可能不会自动解码它
                .replaceAll("\"", "%22"); // 替换双引号
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
                log.error("文件不存在！！,文件路径:{}", path);
                throw new Exception("文件不存在!!");
            }
            // 设置Content-Disposition头，确保没有HTTP响应拆分漏洞
            String headerValue = "attachment; filename=" + encode(file.getName());
            response.setHeader("Content-Disposition", headerValue);
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
            log.error(e.getMessage());
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
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


    /**
     * MultipartFile 转 File
     * @param multipartFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile) throws Exception {

        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            outputStream = new FileOutputStream(file);
            write(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void write(InputStream inputStream, OutputStream outputStream) {
        byte[] buffer = new byte[4096];
        try {
            int count = inputStream.read(buffer, 0, buffer.length);
            while (count != -1) {
                outputStream.write(buffer, 0, count);
                count = inputStream.read(buffer, 0, buffer.length);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
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
            filePath = getLocalPathPrefix(filePath);
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
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取本地文件前缀
     * @param filePath
     * @return
     */
    public static String getLocalPathPrefix(String filePath) {
        String os = System.getProperty("os.name");
        if(os.toLowerCase(Locale.ROOT).startsWith("win")){
            filePath = "D:" + filePath;
        }
        if(filePath.contains("D:D:")){
            filePath = filePath.replace("D:D:","D:");
        }
        return filePath;
    }

    /**
     * 文件的最后修改时间
     * @param file 文件
     * @return 返回最后修改时间
     * axin
     */
    public static String getFileModifiedDate(File file){
        long time = file.lastModified();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }



    /**
     * 获取文件大小
     * @param size 文件大小
     * @return 返回大小
     * axin
     */
    public static String getSizeStrByLength(long size){
        DecimalFormat format = new DecimalFormat("0.00");
        if(size >= 1024.0*1024*1024){//GB
            return format.format(size/1024.0/1024.0/1024.0)+"GB";
        }else if(size>=1024.0*1024){//MB
            return format.format(size/1024.0/1024.0)+"MB";
        }else if(size>=1024){//KB
            return format.format(size/1024.0)+"KB";
        }else{
            return format.format(size)+"B";
        }
    }


    /**
     * 遍历文件信息
     * @param file
     * @return
     * axin
     */
    public static List<Map<String , Object>> getFileInfo(File file, Map<String, Object> map){

        List<Map<String , Object>> fileInfos = new ArrayList<>();
        //遍历文件
        File[] firstFiles = file.listFiles();
        if(firstFiles == null){
            return fileInfos;
        }

        for(File f : firstFiles){
            Map<String ,Object> m = new HashMap<>();
            m.put("parentId",map.get("id"));
            m.put("parentPath",map.get("filePath"));
            m.put("parentName",map.get("pathName"));
            //文件名
            m.put("fileName",f.getName());
            //绝对路劲
            m.put("filePath", f.getAbsolutePath());
            //文件大小
            m.put("fileSize",getSizeStrByLength(f.length()));
            //文件修改时间
            m.put("fileDate",getFileModifiedDate(f));
            //文件标识(是否目录)
            m.put("isDir",f.isDirectory() ? "1" : "0");

            //目录的情况递归回去
            if(f.isDirectory()){
                m.put("fileInfo", getFileInfo(f, map));
                //重置大小
                m.put("fileSize",getSizeStrByLength(FileUtils.sizeOfDirectory(f)));
            }
            fileInfos.add(m);
        }

        return fileInfos;
    }


    /*public static String getFilePath(String filePath, String date){

        if(filePath.toLowerCase().contains("HHMMSSNNN".toLowerCase())){
            String time = DateUtil.getWholeTime();
            filePath = filePath.replaceAll("HHMMSSNNN", time);
            filePath = filePath.replaceAll("hhmmssnnn", time);
        }

        if(filePath.toLowerCase().contains("HHMMSS".toLowerCase())){
            String time = DateUtil.getNowTime();
            filePath = filePath.replaceAll("HHMMSS", time);
            filePath = filePath.replaceAll("hhmmss", time);
        }

        if(filePath.toLowerCase().contains("YYYYMMDD".toLowerCase())){
            if(!PublicUtils.isNotEmpty(date)){
                date = DateUtil.getNowDate();
            }else{
                date = date.replace("-","");
            }
            filePath = filePath.replaceAll("YYYYMMDD", date);
            filePath = filePath.replaceAll("yyyymmdd", date);
            filePath = filePath.replaceAll("yyyyMMdd", date);
        }


        return filePath;
    }

    public static boolean deleteFile(String filePath, String fileName, boolean forceDelete) {
        return deleteFile(filePath + File.separator + fileName, forceDelete);
    }

    public static boolean deleteFile(String fileFullPath, boolean forceDelete) {
        File file = new File(fileFullPath);
        boolean delete = file.delete();
        if (delete || !forceDelete) {
            return delete;
        }
        System.gc();
        return file.delete();
    }

    public static void createTarFile(List<File> fileList,String tarFilePath) throws IOException{
        File tarFile = new File(tarFilePath);
        try(FileOutputStream fos = new FileOutputStream(tarFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(bos)){
            for (File file: fileList) {
                TarArchiveEntry entry = new TarArchiveEntry(file);
                entry.setName(file.getName());
                tarOut.putArchiveEntry(entry);

                try(FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis)){
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1){
                        tarOut.write(buffer,0,len);
                    }
                }
                tarOut.closeArchiveEntry();
            }
        }catch (IOException e){
            log.error(e.getMessage());
            throw e;
        }
    }*/

    /**
     * 获取压缩包第一级文件数量
     * @param zipFilePath
     * @param charset
     * @return
     */
    public static int getItemZipCountByName(String zipFilePath, String charset){
        int fileCount = 0;
        Map<String, Object> map = new HashMap<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath, Charset.forName(charset))) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                map.put(entry.getName().substring(0, entry.getName().lastIndexOf(".")), entry.getSize());
            }
            fileCount = map.size();
        } catch (IOException e) {
            log.error("获取压缩包第一级文件数量出错，错误：[{}]", e);
        }
        return fileCount;
    }
}
