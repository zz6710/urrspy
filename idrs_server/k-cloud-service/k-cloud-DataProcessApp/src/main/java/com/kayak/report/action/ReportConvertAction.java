package com.kayak.report.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.report.dao.ReportConvertConfigDao;
import com.kayak.report.model.ReportConvert;
import com.kayak.report.model.ReportConvertConfig;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.ImportTemplateManage;
import com.kayak.dps.app.service.ImportTemplateManageService;
import com.kayak.dps.export.util.ExcelParse;
import com.kayak.report.dao.ReportConvertDao;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping(value = "/reportconvert")
public class ReportConvertAction extends BaseController {

    @Autowired
    private ReportConvertDao reportConvertDao;

    @Autowired
    private ReportConvertConfigDao reportConvertConfigDao;

    @Autowired
    private ImportTemplateManageService importTemplateManageService;

    /**
     * 报表转换导入
     * @param file 上传文件对象
     * @param request 上传请求的参数
     * @param response 响应对象
     * @return
     */
    @RequestMapping(value = "/reportConvertImport.json",produces = { "application/json;charset=UTF-8"})
    public String reportConvertImport( HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
        String reportId = request.getParameter("reportId");
        try {
            ImportTemplateManage importTemplateManage = importTemplateManageService.getTemplateInfoByTableName(reportId);
            if(importTemplateManage != null){
                String upFileName = file.getOriginalFilename();
                int fileIdx = upFileName.lastIndexOf(".");
                String fileType = upFileName.substring(fileIdx + 1).toUpperCase(Locale.ROOT);
                List<Map<String, Object>> readDataList = readExcelFile(file.getInputStream(), 0, 0, 0, true, fileType);
                try {
                    String newFileName = newFileName(importTemplateManage.getTemplateFileName()); //下载模板使用新文件名
                    String filePath = importTemplateManage.getTemplateFilePath() + newFileName;
                    FileTransfer transfer = FileTransferHelpler.getTransfer();
                    transfer.downloadFile(importTemplateManage.getOssFilePath(), filePath);//从OSS下载模板文件
                    String startRow = importTemplateManage.getRowStart();
                    writeDataListToFile(startRow,filePath, convertDataList(reportId, readDataList), fileType); //写数据集合到文件
                    ReportConvert info = new ReportConvert();
                    info.setReportId(reportId);
                    info.setUpFilename(upFileName);
                    info.setConvertFilename(newFileName);
                    info.setConvertFilepath(filePath);
                    reportConvertDao.addReportConvertInfo(info);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {

                }

            }
        }catch (Exception ex){
            throw ex;
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/exportConvertFile.json",produces = { "application/json;charset=UTF-8"})
    public void downloadTempData(HttpServletResponse response) throws Exception{
        Map<String, Object> parameters = RequestSupport.getParameters();
        String id = parameters.get("id").toString();
        ReportConvert info = reportConvertDao.queryReportConvertInfo(id);
        String fileName= info.getConvertFilename(); //下载转换后的文件名
        String filePath = info.getConvertFilepath();  //下载路径
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try{
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("filename",fileName);
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setCharacterEncoding("UTF-8");
            File localFile = new File(filePath);
            response.addHeader("Content-Length",String.valueOf(localFile.length()));
            fileInputStream = new FileInputStream(localFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }
        }catch(Exception ex){
            throw ex;
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }
        }
    }

    /**
     * 进行数据转换（将读取的Excel数据，转换为List<List<String>>数据，方便写入Excel文件）
     * @param reportId 报表ID
     * @param dataList 读取的数据集
     * @return
     */
    private List<List<String>> convertDataList(String reportId, List<Map<String, Object>> dataList) throws Exception{
        if(dataList == null || dataList.size() <= 1) {
            return null;
        }

        List<List<String>> result = new ArrayList<>(); //结果集
        Map<String, Object> headMap = dataList.get(0); //excel标题头配置
        dataList.remove(0); //删除excel标题头

        List<ReportConvertConfig> configList = reportConvertConfigDao.findReportConvertConfig(reportId);
        for(Map<String, Object> row: dataList){ //遍历数据集
            List<String> convertRow = new ArrayList<>();
            for(int i=0;i<configList.size();i++){
                String converValue = ""; //转换后的值
                String sourceColumn = configList.get(i).getSourceColumn();
                if(row.containsKey(sourceColumn)){
                    Object obj = row.get(sourceColumn);
                    if(obj != null){
                        converValue = obj.toString();
                    }
                }
                convertRow.add(converValue);
            }
            result.add(convertRow);
        }
        return result;
    }

    /**
     * 写数据集合到文件
     * @param filePath 文件路径
     * @param dataList 数据集合
     */
    private void writeDataListToFile(String startLine, String filePath, List<List<String>> dataList, String excelType) throws Exception{
        if(dataList == null || dataList.size() == 0) {
            return;
        }
        String sheetName ="报告模板";
        Workbook workbook = null;
        if("XLS".equals(excelType)){
            workbook = new HSSFWorkbook(new FileInputStream(filePath));
        }else{
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        }
        Sheet sheet = workbook.getSheet(sheetName);
        int startRow = Integer.valueOf(startLine);
        for(List<String> dataRow: dataList){
            Row row = sheet.createRow(startRow++);
            for(int i=0; i < dataRow.size(); i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(dataRow.get(i));
            }
        }
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * 新文件名
     * @param fileName 原始文件名
     * @return
     */
    private String newFileName(String fileName){
        String result = "";
        int idx = fileName.lastIndexOf(".");
        String currTime = DateUtil.getNowDate() + DateUtil.getNowTime();
        result = fileName.substring(0, idx) + "-"+currTime + fileName.substring(idx);
        return result;
    }


    /**
     * 读取Excel文件
     * @param inputStream 文件流对象
     * @param sheetNumber 读取的sheet索引
     * @param headerNumber 头行号
     * @param rowStart 开始行号
     * @param closeInputStream  是否关闭数据流
     * @param fileType 文件类型
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    private List<Map<String, Object>> readExcelFile(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream, String fileType) throws IOException, InvalidFormatException {
        List<Map<String, Object>> data = new ArrayList();
        if("XLS".equals(fileType)){
            data = ExcelParse.readExcelData2003(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream);
        }else{
            data = ExcelParse.readExcelData(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream);
        }
        return data;
    }

    /**
     * excel文件导入并进行下载，返回要下载的文件路径
     * @param request 请求对象
     * @param response 响应对象
     * @param file 上传的文件对象
     * @throws Exception
     */
    @RequestMapping(value = "/reportConvertImportDownload.json",produces = { "application/json;charset=UTF-8"})
    public void reportConvertImportDownload( HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
        String reportId = request.getParameter("reportId");
        try {
            ImportTemplateManage importTemplateManage = importTemplateManageService.getTemplateInfoByTableName(reportId);
            if(importTemplateManage != null){
                String upFileName = file.getOriginalFilename();
                int fileIdx = upFileName.lastIndexOf(".");
                String fileType = upFileName.substring(fileIdx + 1).toUpperCase(Locale.ROOT);
                List<Map<String, Object>> readDataList = readExcelFile(file.getInputStream(), 0, 0, 0, true, fileType);
                try {
                    String newFileName = newFileName(importTemplateManage.getTemplateFileName()); //下载模板使用新文件名
                    String filePath = importTemplateManage.getTemplateFilePath() + newFileName;
                    FileTransfer transfer = FileTransferHelpler.getTransfer();
                    transfer.downloadFile(importTemplateManage.getOssFilePath(), filePath);//从OSS下载模板文件
                    String startRow = importTemplateManage.getRowStart();
                    writeDataListToFile(startRow,filePath, convertDataList(reportId, readDataList), fileType); //写数据集合到文件
                    ReportConvert info = new ReportConvert();
                    info.setReportId(reportId);
                    info.setUpFilename(upFileName);
                    info.setConvertFilename(newFileName);
                    info.setConvertFilepath(filePath);
                    reportConvertDao.addReportConvertInfo(info);
                    log.info("经过文件上传后，要下载的文件路径为:" + filePath);
                    byte[] buffer = new byte[1024];
                    FileInputStream fileInputStream = null;
                    BufferedInputStream bufferedInputStream = null;
                    BufferedOutputStream outputStream = null;
                    File localFile = new File(filePath);
                    try{
                        response.setContentType("application/octet-stream;charset=utf-8");
                        response.setHeader("filename", localFile.getName());
                        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(localFile.getName(), "UTF-8"));
                        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                        response.setCharacterEncoding("UTF-8");
                        response.addHeader("Content-Length",String.valueOf(localFile.length()));
                        fileInputStream = new FileInputStream(localFile);
                        bufferedInputStream = new BufferedInputStream(fileInputStream);
                        outputStream = new BufferedOutputStream(response.getOutputStream());
                        int i = bufferedInputStream.read(buffer);
                        while (i != -1) {
                            outputStream.write(buffer, 0, buffer.length);
                            outputStream.flush();
                            i = bufferedInputStream.read(buffer);
                        }
                    }catch(Exception ex){
                        throw ex;
                    }finally {
                        try {
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            if (bufferedInputStream != null) {
                                bufferedInputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (Exception e2) {
                            log.error("io关闭异常[{}]", e2);
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {

                }

            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
