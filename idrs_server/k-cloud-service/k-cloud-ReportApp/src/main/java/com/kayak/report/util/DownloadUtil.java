package com.kayak.report.util;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.system.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
/**
 * 描述：文件下载util
 *
 * @author zhaojie
 */
public  class DownloadUtil {

    private static final Logger log = LoggerFactory.getLogger(DownloadUtil.class);

    /**
     *
     * @param osPath  linux路径
     * @param winPath win路径
     * @author zhaojie
     */
    public static String init(String osPath, String winPath) {
        String rootPath = "";
        String system = System.getProperty("os.name");
        try {
            if (system.toLowerCase().startsWith("win")) {
                rootPath = SysUtil.getSystemParamsByParaid(winPath);
            } else {
                rootPath = SysUtil.getSystemParamsByParaid(osPath);
            }
            if(!rootPath.substring(rootPath.length()-1).equals("/")){
                rootPath = rootPath + "/";
            }
            File f = new File(rootPath);
            if(!f.exists()) f.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }

        return rootPath;
    }

    /**
     *
     * @param downLoadPath 下载文件全路径
     * @param fName 下载文件名
     * @param response
     * @author zhaojie
     */
    public static void downloadFile(String downLoadPath, String fName, HttpServletResponse response) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        log.info("下载文件名称:" + fName);
        try {
            log.info("下载文件路径：" + downLoadPath);
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fName, "utf-8"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024 * 100];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

}