package com.kayak.dps.export.util;

import com.alibaba.druid.util.StringUtils;
import com.kayak.core.system.SysUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述：导出文件util
 *
 * @author zhaojie
 */
public  class ExportExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExportExcelUtil.class);

    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;
    private static File file;
    private static int maxRowNum = 0;
    private static XSSFCellStyle cellStyle;


    public  static String getRootPath(Map<String,String> pathParams) {
        String rootPath = "";
        String system = System.getProperty("os.name");
        try {
            if (system.toLowerCase().startsWith("win")) {
                rootPath = SysUtil.getSystemParamsByParaid("70000010002");
            } else {
                rootPath = SysUtil.getSystemParamsByParaid("70000010009");
            }
            if(!rootPath.substring(rootPath.length()-1).equals("/")){
                rootPath = rootPath + "/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }

        return rootPath;
    }

    public static int getMaxRowNum() {
        try {
            maxRowNum = SysUtil.getSystemParamsByParaid("70000010010").isEmpty() ? 0 : Integer.parseInt(SysUtil.getSystemParamsByParaid("70000010010"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxRowNum;
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

    //创建sheet页
    public static void setSheet(String sheetName) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }

    public static void getCellStyle() {
        cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
    }

    //创建表头
    public static void createHead(List<String> headList) {
        getCellStyle();
        //创建表头，也就是第一行
        row = sheet.createRow(0);
        for (int i = 0; i < headList.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headList.get(i));
        }
    }

    //创建表内容
    public static void createContent(List<List<String>> contentList) {
        //创建表内容，从第二行开始
        for (int i = 0; i < contentList.size(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < contentList.get(i).size(); j++) {
                row.createCell(j).setCellValue(contentList.get(i).get(j));
            }
        }
    }

    //写入文件
    public static void writeToFile(String filePath){
        log.info("数据开始写入excel文件,路径:{}",filePath);
        file = new File(filePath);
        FileOutputStream fos = null;
        //将文件保存到指定的位置
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            log.info("数据写入excel成功,路径:{}",filePath);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("数据写入excel失败,路径:{},错误原因{},{}",filePath,e.getMessage(),e);
        }finally {
            try {
                workbook.close();
                if(fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downLoadExcel(String fName, HttpServletResponse response) {
        try {
            OutputStream output=response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; downFileName ="+ URLEncoder.encode(fName, "utf-8"));
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.flush();
            output.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
    }

    /**生成zip*/
    public static void createZip(List<Map<String, String>> filePaths, String zipFilePath, Boolean keepDirStructure) {
        log.info("数据下载生成zip文件;zip路径:{},需要压缩文件集合:{}",zipFilePath,filePaths);
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (int i = 0; i < filePaths.size(); i++) {
                String relativePath = filePaths.get(i).get("filePath");
                String relativeName = filePaths.get(i).get("fileName");
                if (StringUtils.isEmpty(relativePath)) {
                    continue;
                }
                File sourceFile = new File(relativePath);
                if (sourceFile == null || !sourceFile.exists()) {
                    continue;
                }
                fis = new FileInputStream(sourceFile);
                if (keepDirStructure != null && keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(relativePath));
                } else {
                    zos.putNextEntry(new ZipEntry(relativeName));
                }
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
            if(!zipFile.exists())
                zipFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("数据文件生成zip失败,报错原因:{},报错日志{}",e.getMessage(),e);
        } finally {
            try {
                if(zos != null) zos.close();
                if(fis != null) fis.close();
                if(fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**清理文件夹*/
    public static void deleteFolder(File file){
        for (File subFile : file.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }
}