package com.kayak.rpt.rhzj.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static void generateEmptyFile(String path, String fileName) throws Exception {

        File file = new File(path, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("创建报送文件失败");
        }
    }

    public static void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //避免中文名字文件乱码
        String fileName = (String) request.getAttribute("doc_name");
        String downLoadPath = (String) request.getAttribute("path");
        File zipFile = new File(downLoadPath);
        try {
            long fileLength = zipFile.length();

            //设置文件名时要将其转化为 “ISO-8859-1” 编码
            String headFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            //下载文件固定步骤
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(headFileName, "utf-8"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            //response输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024 * 100];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zipFile.delete();
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();

        }
    }

}
