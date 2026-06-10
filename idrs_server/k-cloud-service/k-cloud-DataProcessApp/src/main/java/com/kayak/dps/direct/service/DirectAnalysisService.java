package com.kayak.dps.direct.service;


import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelSftp;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.dao.DataFileDao;
import com.kayak.dps.direct.dao.ReportClearDao;
import com.kayak.dps.direct.enums.DirectTableCodeEnum;
import com.kayak.dps.direct.util.DirectAnalysisUtil;
import com.kayak.dps.direct.util.DirectParams;
import com.kayak.dps.direct.util.DirectUtils;
import com.kayak.dps.ods.util.FileZipUtils;
import com.kayak.dps.ods.util.zz.SFtpHelper;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DirectAnalysisService {
    @Autowired
    public ComnDao comnDao;
    @Autowired
    public ReportClearDao reportClearDao;
    @Autowired
    public DataFileDao dataFileDao;

    public void analysisWpResult (Map<String, Object> params) throws Exception {
        // 从 params 中获取 result 对象（JSON 字符串）
        String resultJson = (String) params.get("result");
        String dealMsg="";
        log.info("result: {}", resultJson);
        // 使用 Jackson 解析 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(resultJson);
        JsonNode exchangeInfoNode = rootNode.path("bodys")
                .path("bodyInExts")
                .path("exchangeInfo");
        // 提取 senderBusiSerialNo 的值
        String senderBusiSerialNo = exchangeInfoNode.path("senderBusiSerialNo").asText();
        long sendTime = rootNode.path("header").path("sendTime").asLong();
        String fdiProcStatus= exchangeInfoNode.path("fdiProcStatus").asText();
        String rtnMsg=rootNode.path("bodys").path("rtnMsg").asText();
        String fdiErrMsg= exchangeInfoNode.has("fdiErrMsg")?exchangeInfoNode.path("fdiErrMsg").asText():"";
        if(StringUtils.isNotBlank(fdiErrMsg)){
            dealMsg=fdiErrMsg;
        }else{
            dealMsg=rtnMsg;
        }
        // 将时间戳转换为日期格式
        Date date = new Date(sendTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        log.info("文件序列号: {}", senderBusiSerialNo);
        String nowDate= DateUtil.getNowDate();
        String path="";
        String property = System.getProperty("os.name");
        if (property.toLowerCase().startsWith("win")) {
            path = CacheUtil.getSystemParam("90000040001");
        } else {
            path = CacheUtil.getSystemParam("90000040002");
        }
        path += "/archiving/";
        path=path.replace("[deal_date]",nowDate);
        // 拼接路径
        String targetDirectory = path +  senderBusiSerialNo+"/compress";
        log.info("文件目录: {}", targetDirectory);
        String fileName="";
        List<Map<String, String>> list = queryPs();
        for(Map<String, String> map : list) {
            fileName=getwpFileName(map,targetDirectory);
            if(StringUtils.isNotBlank(fileName)){
                log.info("扫描到zip文件: {}", fileName);
                Map<String, Object> logParams = new HashMap<>();
                logParams.put("tableName",getPortCdFromApi(fileName));
                logParams.put("filePath",targetDirectory);
                logParams.put("fileName",fileName);
                logParams.put("ip", Objects.toString(map.get("SFTP_IP")));
                logParams.put("createTime", formattedDate);
                logParams.put("rtnMsg",dealMsg);
                logParams.put("dealDate",nowDate);
                logParams.put("sendType","1");
                logParams.put("dealStatus",fdiProcStatus);
                String insertSql = "INSERT INTO wp_files_log (table_name, file_path, file_name, ip, create_time, rtn_msg,deal_date,send_type,deal_status) " +
                        "VALUES($S{tableName}, $S{filePath}, $S{fileName}, $S{ip}, $S{createTime}, $S{rtnMsg},$S{dealDate},$S{sendType},$S{dealStatus})";
                comnDao.update(insertSql,logParams);
                break;
            }
        }
        log.info("扫描个人养老金文件结束: {}", fileName);
    }

    /**
     * 理财直连回调函数调用入口
     * @param params
     * @throws Exception
     */
    public void analysisDirectResult (Map<String, Object> params) throws Exception {
        log.info("理财直连回调函数调用入口，原始参数为params: {}", params);
        // 从 params 中获取 result 对象（JSON 字符串）
        Map<String,String> returnMap = new HashMap<>();
        String resultJson = "";

        Object result = params.get("result");
        Class<?> clazz = result.getClass();
        log.info("result的类型为："+clazz);
        if(result instanceof Map){
            returnMap =  (Map<String,String>) result;
        }else if(result instanceof LinkedHashMap){
            returnMap =  (Map<String,String>) result;
        }else if (result instanceof String){
            resultJson = (String) result;
            log.info("resultJson: {}", resultJson);
            returnMap = Tools.str2map(resultJson);
        }

        String sendFileType = returnMap.get("sendFileType");

        // 提取 参数所有可以使用 的值：文件名称、文件全路径
        String fileName = returnMap.get("fileName");

        String fileFullPath = SysUtil.getSystemParamsByParaid("90000051312")+"/zzfile/receive/"+DateUtil.getNowDate()+"/";

        // 拼接路径
        String targetDirectory = fileFullPath +  fileName;
//        //下载到本地路径解压读取
        String localReceiveDirectoryPath = SysUtil.getSystemParamsByParaid("app_localpath");
        log.info("文件目录: {}", targetDirectory);
        File dir = new File(localReceiveDirectoryPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        List<Map<String, String>> list =  queryPs();
        // 从S3现在获取当前日期文件夹下面的所有文件，并且逐个解析
        Map<String, String> configMap = list.get(0);
        FileTransferConfig config = new FileTransferConfig();
        config.setProtocol("S3");
        config.setAmazonEndpointUrl( configMap.get("ENDPOINT_URL"));
        config.setAmazonAwsAccessKey( configMap.get("ACCESS_KEY"));
        config.setAmazonAwsSecretKey( configMap.get("SECRET_KEY"));
        config.setAmazonAwsBucketName(configMap.get("BUCKET_NAME") );

        FileTransfer transfer = FileTransferHelpler.getTransfer(config);
        transfer.downloadFileAndDisconnect(targetDirectory, localReceiveDirectoryPath+fileName);

        log.info("扫描到zip文件: {}", fileName);
        Map<String, Object> logParams = new HashMap<>();
        logParams.put("WORKDATE",DateUtil.getNowDate());
        logParams.put("FILETYPE", sendFileType.split("\\.")[2]);
        if("REG.200.005".equals(sendFileType) || "REG.200.006".equals(sendFileType) || "REG.200.008".equals(sendFileType)){
            logParams.put("FILEID", returnMap.get("pushSn"));
            logParams.put("MSGTYPE", sendFileType);
            logParams.put("FILENAME",returnMap.get("fileName"));
            logParams.put("TOTALCOUNT",returnMap.get("TotalCount"));
            logParams.put("ERRORCODE",returnMap.get("ErrorCode"));
            logParams.put("ERRORTEXT",returnMap.get("ErrorText"));
            logParams.put("STATUS","1");
            String updateSql = "insert into app_zz_file(REPORT_DATE,WORKDATE,FILEID,FILENAME,TOTALCOUNT,ERRORCODE,ERRORTEXT,FILETYPE,MSGTYPE,STATUS,CRT_TIME)values($S{WORKDATE},$S{WORKDATE},$S{FILEID},$S{FILENAME},$S{TOTALCOUNT}" +
                    ",$S{ERRORCODE},$S{ERRORTEXT},$S{FILETYPE},$S{MSGTYPE},$S{STATUS},date_format(now(),'%Y%m%d %H:%i:%s'))";
            comnDao.update(updateSql,logParams);
        }else{
            logParams.put("FILEID", returnMap.get("fdiBusiSerialNo"));
            logParams.put("FILENAME",returnMap.get("fileName"));
            logParams.put("ORIGFILENAME",returnMap.get("origFileName"));
            logParams.put("SUCCESSCOUNT",returnMap.get("succDataNum"));
            logParams.put("FAILEDCOUNT",returnMap.get("failDataNum"));
            logParams.put("TOTALCOUNT",returnMap.get("fileDataNum"));
            logParams.put("ERRORCODE",returnMap.get("errorCode"));
            logParams.put("ERRORTEXT",returnMap.get("errorText"));
            logParams.put("STATUS","3");
            String updateSql = "UPDATE app_zz_file SET FILEID = $S{FILEID},FILENAME = $S{FILENAME},SUCCESSCOUNT = $S{SUCCESSCOUNT},FAILEDCOUNT = $S{FAILEDCOUNT}," +
                    "TOTALCOUNT = $S{TOTALCOUNT},ERRORCODE = $S{ERRORCODE},ERRORTEXT = $S{ERRORTEXT},STATUS = $S{STATUS},CRT_TIME = date_format(now(),'%Y%m%d %H:%i:%s') WHERE FILETYPE = $S{FILETYPE} AND ORIGFILENAME = $S{ORIGFILENAME}" ;
            comnDao.update(updateSql,logParams);
        }

        //理财产品监管局审阅结果反馈(REG.200.006),因为传输的参数结构的不同，所以需要在此处做独立处理
        if ("REG.200.006".equals(sendFileType)) {
            parseRegisCodeRein(localReceiveDirectoryPath, fileName, returnMap, fileFullPath);
            return;
        }

       // 解析下载到本地的zip文件
       // 针对118净值登记传送文件类型为txt的特殊处理
        if (sendFileType.equals("REG.001.118") || "REG.200.008".equals(sendFileType)){
           parseNetValueRegisFile(localReceiveDirectoryPath,fileName, returnMap, sendFileType,DateUtil.getNowDate());
        } else {
           parseDirectLocalFile(localReceiveDirectoryPath,fileName, returnMap, sendFileType,DateUtil.getNowDate());
        }
    }

    private void parseRegisCodeRein(String filePath, String fileName, Map<String,String> exchangeInfoNode, String fileFullPath) throws Exception {
        log.info("解析理财产品监管局审阅结果反馈文件开始 {}", exchangeInfoNode);
        String xlsxFileName = fileName.replace("zip", "xlsx");
        Map<String, Object> dataParams = new HashMap<>();

        //解压文件
        FileZipUtils.unZipFile(filePath + "/" + fileName, filePath);

        try (FileInputStream file = new FileInputStream(filePath + "/" + xlsxFileName);
             Workbook workbook = new XSSFWorkbook(file)) {
            // 获取Sheet1工作表
            Sheet sheet = workbook.getSheet("Sheet1");
            if (sheet == null) {
                log.error("sheet1工作表不存在");
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                // 跳过空行
                if (row == null) {
                    continue;
                }
                // 检查该行是否为空（所有单元格都为空）
                if (isRowEmpty(row)) {
                    continue;
                }

                //解析xlsx文件获取所需数据
                dataParams.put("DIRECT_ZIP_DIR", fileFullPath);
                dataParams.put("DIRECT_ZIP_NM", xlsxFileName);
                dataParams.put("SERIAL_NO", exchangeInfoNode.get("pushSn").toString());
                dataParams.put("PROD_NM", getCellValue(row, 0));
                dataParams.put("INNER_CODE", getCellValue(row, 1));
                dataParams.put("PROD_REG_ENC", getCellValue(row, 2));
                dataParams.put("CHECK_RESULT", getCellValue(row, 3));
                dataParams.put("CHECK_OPINION", getCellValue(row, 4));
                //落表
                String prod_reg_enc=getCellValue(row, 2);
                if(prod_reg_enc == null||prod_reg_enc.isEmpty()){
                    dataParams.put("prod_code", getCellValue(row, 1));
                    dataParams.put("prod_status","08");
                    dataFileDao.updateProdState(dataParams);
                    dataFileDao.updateCodeApplyHistory(dataParams);
                }else{
                    dataFileDao.updateCodeApplyHistory(dataParams);
                }
            }
        }
    }

    private static String getCellValue(Row row, int colIndex) {
        return row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                String value = cell.getStringCellValue().trim();
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 118净值登记txt文件解析
     * @param filePath
     * @param sendFileType
     * @throws Exception
     */
    private void parseNetValueRegisFile(String filePath, String fileName, Map<String,String> returnMap, String sendFileType,String report_date) throws Exception {
        log.info("解析txt传送文件开始 {}", returnMap);
        String txtFileName = fileName.replace("zip", "txt");
        String reportCode = sendFileType.split("\\.")[2];
        String tableName = DirectTableCodeEnum.getTableNameByMapCode(reportCode);
        //解压文件
        FileZipUtils.unZipFile(filePath + "/" + fileName, filePath);
        List<String> errorSerNumberList = new ArrayList<>();
        List<Map<String,Object>> errorMapList = new ArrayList<>();
        AtomicLong fileNo = new AtomicLong(0);
        if(sendFileType.equals("REG.200.008")){
            int num = 1;
            String[] custInfo;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(filePath + "/" + txtFileName)), "GBK"))) {
                String line;
                while((line = reader.readLine()) != null) {
                    if(num>8 && !"WDEPEND".equals(line)){
                        custInfo=line.split("\u0007");
                        Map<String, Object> tparam = new HashMap<>();
                        tparam.put("dlv_cod",custInfo[0]);
                        tparam.put("invstr_id_flag",custInfo[1]);
                        tparam.put("invstr_no",custInfo[2]);
                        tparam.put("cip_gcw_acct_no",custInfo[3]);
                        errorMapList.add(tparam);
                    }
                    num =num+1;
                    if (errorMapList != null && errorMapList.size()>=5000) {
                        batchInsertRcvCustData(errorMapList);
                        errorMapList.clear();
                    }
                }
                if(errorMapList.size()>0){
                    batchInsertRcvCustData(errorMapList);
                }
            }catch (IOException e) {
                log.error("解析身份信息登记反馈文件不存在{}", filePath + "/" + txtFileName);
            }
            log.info("解析身份信息登记反馈文件: {}", sendFileType);
        }else{
            int err_num = Integer.parseInt(returnMap.get("failDataNum"));
            if(err_num<=Integer.parseInt(DirectParams.app_fail_num)) {
                Pattern pattern = Pattern.compile("登记流水号为(\\d+)[^：]*：(.*)");
                // 使用BufferedReader读取TXT文件
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(Files.newInputStream(Paths.get(filePath + "/" + txtFileName)), "GBK"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String errorMsg = "";
                        if(line.matches("\\d+")){//是纯数字的行，代表数据是成功的
                            continue;
                        }
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String registrationSerialNumber = matcher.group(1);
                            errorSerNumberList.add(registrationSerialNumber);
                            errorMsg =  matcher.group(2);

                        }else {
                            continue;
                        }

                        if(errorMapList.size()<=1000){
                            Map<String, Object> dataParams = new HashMap<>();
                            dataParams.put("REGISTER_DATE", DateUtil.getNowDate());
                            dataParams.put("FILE_TYPE", sendFileType);
                            dataParams.put("FILENAME", fileName);
                            dataParams.put("FILENO", returnMap.get("fdiBusiSerialNo").toString());
                            dataParams.put("REGISTER_SERNO", matcher.group(1));
                            dataParams.put("ERRORCODE", returnMap.get("errorCode").toString());
                            dataParams.put("ERRORMSG",errorMsg);
                            dataParams.put("SYS_TABLE_NAME",tableName);
                            errorMapList.add(dataParams);
                        }
                        if (errorSerNumberList != null && errorSerNumberList.size()>=5000) {
                            batchUpdateDirectData(errorSerNumberList, tableName, "4");
                            errorSerNumberList.clear();
                        }
                    }
                } catch (IOException e) {
                    log.error("解析净值登记传送文件不存在{}", filePath + "/" + txtFileName);
                }
                if (errorSerNumberList != null && errorSerNumberList.size() < 5000) {
                    batchUpdateDirectData(errorSerNumberList, tableName, "4");
                }
                batchInsertRcvData(errorMapList);
                String updateSql = "UPDATE app_zz_file SET STATUS ='1' WHERE ORIGFILENAME ='" + returnMap.get("origFileName") + "'";
                comnDao.update(updateSql);
            }else{
                String updateSql = "UPDATE app_zz_file SET STATUS ='4' WHERE ORIGFILENAME ='"+returnMap.get("origFileName")+"'";
                comnDao.update(updateSql);
            }
            log.info("解析净值登记传送文件结束: {}", sendFileType);
        }
    };

    /**
     * 登记编码回灌
     * @param filePath
     * @param zipFileName
     * @param exchangeInfoNode
     * @param sendFileType
     * @param report_date
     * @throws Exception
     */
    private void parseDirectLocalFile(String filePath, String zipFileName, Map<String,String> exchangeInfoNode,String sendFileType,String report_date) throws Exception {
        log.info("主逻辑处理开始: {}", exchangeInfoNode);
        String xmlFileName = zipFileName.replace("zip", "xml");
        String reportCode = sendFileType.split("\\.")[2];
        String tableName = DirectTableCodeEnum.getTableNameByMapCode(reportCode);
        log.info("reportCode:----"+reportCode+";tableName-----------:"+tableName);
        //解压文件
        FileZipUtils.unZipFile(filePath+ zipFileName, filePath);

        //解析文件
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File(filePath + xmlFileName));
        //获取根节点元素对象
        Element root = document.getRootElement();

        //获取所有中债十三张表的编码
        List<String> mapCodeList = Arrays.stream(DirectTableCodeEnum.values()).map(DirectTableCodeEnum::getMapCode).collect(Collectors.toList());

        //根据反馈报文类型编码决定处理逻辑
        //公共数据获取
        Map<String, Object> dataParams = new HashMap<>();
        Element contextElement = root.element("context");
        String identifier = contextElement.element("entity").element("identifier").getText();
        String startDate = contextElement.element("period").element("startDate").getText();
        String endDate = contextElement.element("period").element("endDate").getText();

        dataParams.put("filePath", filePath);
        dataParams.put("zipFileName", zipFileName);
        dataParams.put("identifier", identifier);
        dataParams.put("startDate", startDate);
        dataParams.put("endDate", endDate);


        //产品登记编码回灌主动推送
        if (sendFileType.equals("REG.200.005")) {
            //解析xml文件获取所需数据
            List<Element> prodInformations = root.elements("ProductRegistrationCodeInformationTuple");
            if (prodInformations != null && !prodInformations.isEmpty()) {
                for (Element information : prodInformations) {
                    dataParams.put("prod_code", information.elementText("PreIdentificationCode"));
                    dataParams.put("reg_code", information.elementText("ProductRegistrationCode"));
                    dataParams.put("workdate", DateUtil.getNowDate());
                    comnDao.update("insert into app_prod_reg_relation (prod_code, reg_code,file_name, workdate,report_date,crt_time) " +
                            " values ($S{prod_code},$S{reg_code},$S{zipFileName},$S{workdate},$S{workdate},date_format(now(),'%Y%m%d %H:%i:%s'))", dataParams);
                    dataParams.put("prod_status","02");
                    dataFileDao.updateProdEncInfo(dataParams);
                }
            }
        }
        //理财中心信息登记接口(此处控制一下，防止后续需求修改）
        else if (mapCodeList.contains(reportCode)) {
            List<String> correctSerNumberList = new ArrayList<>();
            List<String> errorSerNumberList = new ArrayList<>();
            List<Map<String,Object>> errorMapList = new ArrayList<>();
            Pattern p=Pattern.compile("^(\\d{1,40})");
            //1.成功信息
            if("113".equals(reportCode) || "114".equals(reportCode) || "104".equals(reportCode) || "111".equals(reportCode)){
                List<Element> correctInformationTuple = root.elements("CorrectInformationTuple");
                if (correctInformationTuple != null && !correctInformationTuple.isEmpty()){
                    for(Element correctInformation : correctInformationTuple){
                        correctSerNumberList.add(correctInformation.elementText("RegistrationSerialNumber"));
                        //3.根据反馈报文更新产品的状态
                        if(correctSerNumberList != null){
                            updateProdState(correctSerNumberList,reportCode);
                        }
                    }
                }
            }
            //文件名
            String instanceFileName = root.elementText("InstanceFileName");;
            int err_num = Integer.parseInt(exchangeInfoNode.get("failDataNum"));
            if(err_num<=Integer.parseInt(DirectParams.app_fail_num)) {
                //2.失败信息
                List<Element> errorInformationTuple = root.elements("ErrorInformationTuple");
                if (errorInformationTuple != null && !errorInformationTuple.isEmpty()) {
                    for (Element errorInformation : errorInformationTuple) {
                        errorSerNumberList.add(errorInformation.elementText("RegistrationSerialNumber"));
                        if(errorMapList.size()<=1000){
                            Map<String,Object> tparam = new HashMap<>();
                            tparam.put("REGISTER_DATE", DateUtil.getNowDate());
                            tparam.put("FILE_TYPE", sendFileType);
                            tparam.put("FILENAME", exchangeInfoNode.get("fileName"));
                            tparam.put("FILENO", errorInformation.elementText("InstanceFileNo"));
                            tparam.put("REGISTER_SERNO", errorInformation.elementText("RegistrationSerialNumber"));
                            tparam.put("ERRORMSG", errorInformation.elementText("ErrorMessage"));
                            tparam.put("ERRORCODE", exchangeInfoNode.get("errorCode"));
                            tparam.put("SYS_TABLE_NAME", tableName);
                            errorMapList.add(tparam);
                        }
                        if (errorSerNumberList != null && errorSerNumberList.size()>=5000) {
                            if("REG.001.202".equals(sendFileType) || "REG.001.203".equals(sendFileType)){
                                batchInsertDirectData(errorSerNumberList,tableName);
                            }else if("REG.001.117".equals(sendFileType)){
                                batchInsertDirectData(errorSerNumberList,tableName);
                            }else{
                                batchUpdateDirectData(errorSerNumberList,tableName,"4");
                            }
                            errorSerNumberList.clear();
                        }

                    }
                }
                if (errorSerNumberList != null && errorSerNumberList.size() < 5000) {
                    if("REG.001.202".equals(sendFileType) || "REG.001.203".equals(sendFileType)){
                        batchInsertDirectData(errorSerNumberList,tableName);
                    }else if("REG.001.117".equals(sendFileType)){
                        batchInsertDirectData(errorSerNumberList,tableName);
                    }else{
                        batchUpdateDirectData(errorSerNumberList,tableName,"4");
                    }
                }
                batchInsertRcvData(errorMapList);
                String updateSql = "UPDATE app_zz_file SET STATUS ='1' WHERE ORIGFILENAME ='" + exchangeInfoNode.get("origFileName") + "'";
                comnDao.update(updateSql);
            }else{
                String updateSql = "UPDATE app_zz_file SET STATUS ='4' WHERE ORIGFILENAME ='"+exchangeInfoNode.get("origFileName")+"'";
                comnDao.update(updateSql);
            }
        }
        log.info("主逻辑处理结束: {}", sendFileType);
    }

    /**
     * 更新产品状态
     * @param sernoList
     * @param reportCode
     */
    public void updateProdState(List<String> sernoList,String reportCode)  throws Exception {
        Map<String, String> params = new HashMap<>();
        String prod_status = "";
        if("113".equals(reportCode)){
            prod_status = "01";
        }else if("114".equals(reportCode)){
            prod_status = "04";
        }else if("104".equals(reportCode)){
            prod_status = "06";
        }else if("111".equals(reportCode)){
            prod_status = "07";
        }else{
            return;
        }
        if(sernoList.size()>0){
            if("113".equals(reportCode)){
                dataFileDao.updateProdInfoByFl(params);
            }else if("114".equals(reportCode)){
                dataFileDao.updateProdInfoByIs(params);
            }else if("104".equals(reportCode)){
                dataFileDao.updateProdInfoByIn(params);
            }
        }
        for (String register_serno : sernoList){
            params.put("register_serno",register_serno);
            params.put("prod_status",prod_status);
            if("113".equals(reportCode)){
                dataFileDao.updateProdStateByFl(params);
            }
            if("114".equals(reportCode)){
                dataFileDao.updateProdStateByIs(params);
            }
            if("104".equals(reportCode)){
                dataFileDao.updateProdStateByIn(params);
            }
            if("111".equals(reportCode)){
                dataFileDao.updateProdStateByTr(params);
            }

        }
    }
    /**
     * 批量更新中债十三张表的数据（适用于数据报送）
     * @throws Exception
     */
    public void batchUpdateDirectData(List<String> registSerNumberList,String tableName,String dataStatus) throws Exception {
        long startTime = System.currentTimeMillis();
        String sysDate = DateUtil.getNowDate();
        if (registSerNumberList == null || registSerNumberList.isEmpty()){
            return;
        }
        String batchSql = "UPDATE " + tableName + " SET REGISTER_DATE='"+sysDate+"' ,REGISTER_STATUS = '" + dataStatus + "' WHERE register_serno = ?";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (String registSerNumber : registSerNumberList) {
                    ps.setString(1, registSerNumber);
                    ps.addBatch();
                }
                if(registSerNumberList.size() == 1){
                    ps.executeUpdate();
                }else{
                    ps.executeBatch();
                }

                log.info(" ##### 批量入库{}耗时: {} ms", registSerNumberList.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("更新报送异常数据!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
            }
        });

    }

    /**
     * 新增流水号
     * @throws Exception
     */
    public void batchInsertDirectData(List<String> registSerNumberList,String tableName) throws Exception {
        long startTime = System.currentTimeMillis();
        if (registSerNumberList == null || registSerNumberList.isEmpty()){
            return;
        }
        String batchSql = "insert into " + tableName + "_erno(register_serno) values(?) ";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (String registSerNumber : registSerNumberList) {
                    ps.setString(1, registSerNumber);
                    ps.addBatch();
                }
                if(registSerNumberList.size() == 1){
                    ps.executeUpdate();
                }else{
                    ps.executeBatch();
                }

                log.info(" ##### 批量入库{}耗时: {} ms", registSerNumberList.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("更新报送异常数据!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
            }
        });

    }

    /**
     * 新增客户反馈信息
     * @throws Exception
     */
    public void batchInsertRcvCustData(List<Map<String,Object>> errorMapList) throws Exception {
        long startTime = System.currentTimeMillis();
        if (errorMapList == null || errorMapList.isEmpty()){
            return;
        }
        String batchSql = "insert into app_cust_result_info(dlv_cod,invstr_id_flag,invstr_no,cip_gcw_acct_no) values(?,?,?,?) ";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (Map<String,Object> tparam : errorMapList) {
                    ps.setString(1,tparam.get("dlv_cod").toString());
                    ps.setString(2,tparam.get("invstr_id_flag").toString());
                    ps.setString(3,tparam.get("invstr_no").toString());
                    ps.setString(4,tparam.get("cip_gcw_acct_no").toString());
                    ps.addBatch();
                }
                if(errorMapList.size() == 1){
                    ps.executeUpdate();
                }else{
                    ps.executeBatch();
                }

                log.info(" ##### 批量入库{}耗时: {} ms", errorMapList.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("更新报送异常数据!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
            }
        });

    }

    /**
     * 批量更新中债
     * @throws Exception
     */
    public void batchInsertRcvData(List<Map<String,Object>> errorMapList) throws Exception {
        long startTime = System.currentTimeMillis();
        if (errorMapList == null || errorMapList.isEmpty()){
            return;
        }
        String batchSql = "insert into app_zz_file_results (REGISTER_DATE, FILE_TYPE, FILENAME, FILENO, REGISTER_SERNO,ERRORMSG,ERRORCODE,SYS_TABLE_NAME,CRT_TIME)values(?,?,?,?,?,?,?,?,date_format(now(),'%Y%m%d %H:%i:%s'))";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (Map<String,Object> tparam : errorMapList) {
                    ps.setString(1,tparam.get("REGISTER_DATE").toString());
                    ps.setString(2,tparam.get("FILE_TYPE").toString());
                    ps.setString(3,tparam.get("FILENAME").toString());
                    ps.setString(4,tparam.get("FILENO").toString());
                    ps.setString(5,tparam.get("REGISTER_SERNO").toString());
                    ps.setString(6,tparam.get("ERRORMSG").toString());
                    ps.setString(7,tparam.get("ERRORCODE").toString());
                    ps.setString(8,tparam.get("SYS_TABLE_NAME").toString());
                    ps.addBatch();
                }
                if(errorMapList.size() == 1){
                    ps.executeUpdate();
                }else{
                    ps.executeBatch();
                }

                log.info(" ##### 批量入库{}耗时: {} ms", errorMapList.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("中债反馈信息处理异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
            }
        });

    }


    public String getwpFileName(Map<String, String> map,String path) throws Exception {
        String fileName="";
        ChannelSftp sftpChannel = null;
        try {
            /*sftpChannel= SFtpHelper.connectForRead(
                    Objects.toString(map.get("SFTP_IP")), 22,
                    AESUtils.AESDecrypted(Objects.toString(map.get("USERNAMES"))),
                    AESUtils.AESDecrypted(Objects.toString(map.get("PASSWORD"))), path);
            if(SFtpHelper.isDirExist(path, sftpChannel)){
                sftpChannel.cd(path);
                Vector<ChannelSftp.LsEntry> files = sftpChannel.ls("*.zip");
                // 获取第一个 .zip 文件的文件名
                if (files != null && !files.isEmpty()) {
                    fileName = files.get(0).getFilename();
                }
            }*/
        }catch (Exception e){
            log.error(">>>>> 文件读取失败：", e);
            throw new Exception("服务器连接失败:" + e.getMessage());
        }finally{
            if(sftpChannel!=null){
                SFtpHelper.disconnect(sftpChannel);
            }
        }
        return fileName;
    }

    public static String getPortCdFromApi(String fileName) {
        String apiCode = "";
        String portCode = "";
        // 找到第一个 "-" 的位置
        int firstHyphenIndex = fileName.indexOf('-');

        // 如果找不到 "-"，返回 null
        if (firstHyphenIndex == -1) {
            return null;
        }

        // 找到第二个 "-" 的位置
        int secondHyphenIndex = fileName.indexOf('-', firstHyphenIndex + 1);

        // 如果找不到第二个 "-"，返回 null
        if (secondHyphenIndex == -1) {
            return null;
        }

        // 提取第一个 "-" 和第二个 "-" 之间的部分
        apiCode=fileName.substring(firstHyphenIndex + 1, secondHyphenIndex);
        if(StringUtils.equals(apiCode,"reg008001")){
            portCode="app_prd_ctlt_info";
        }
        if(StringUtils.equals(apiCode,"reg008002")){
            portCode="app_wp_prd_nav_info";
        }
        if(StringUtils.equals(apiCode,"reg008003")){
            portCode="app_wp_prd_trd_info";
        }
        if(StringUtils.equals(apiCode,"reg008004")){
            portCode="app_wp_prd_pos_info";
        }
        return portCode;
    }

    public List<Map<String, String>> queryPs() throws Exception {
        String qdeal = "  select t.id,t.config_describe,t.config_name,"+
                " t.config_code,t.config_type,t.status "+
                " from base_port_config_info t "+
                " where t.config_type = 'SJZT' and t.status ='1'" +
                " and t.config_name in ('ACCESS_KEY','SECRET_KEY','BUCKET_NAME','ENDPOINT_URL')";
        List<SqlRow> rs= comnDao.findRows(qdeal);
        List<Map<String, String>> list= rs.stream().collect(Collectors.groupingBy(row->row.getString("config_type")))
                .entrySet().stream().map(entry -> {
                    // 将每个分组的config_name和config_code存入Map
                    return entry.getValue().stream().collect(Collectors.toMap(
                            row -> row.getString("config_name"), // key: config_name
                            row -> row.getString("config_code")  // value: config_code
                    ));
                })
                // 收集所有Map到List中
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 获取结果文件
     * @throws Exception
     */
    public void getResultFile(String isRegisterFile) throws Exception{
        int err_cnt = 0;
        String error_msg = "";
        String[] busiCode = isRegisterFile.split(",");
        //查询表对应isRegisterFile设定数值
        Map<String, String> exSeatMap = reportClearDao.getExSeatMap();
        for (String busiC:busiCode) {
            if ("1".equals(DirectParams.connFlag)) {
                //查询已经获取的消息
                //此处重新定义文件状态  0-未处理     1-发送成功      2-接收成功   3-处理失败(目前定义仅发送失败时存在该状态) 4-消息获取失败（目前定义仅接收文件时存在该状态） 6-文件下载成功    5-文件下载失败
                List<Map<String, Object>> selectinfo = new ArrayList<Map<String, Object>>();
                //存放FileId
                HashSet fileIdSet = new HashSet();
                List<SqlRow> selectInfo1 = reportClearDao.selectFileInfoEx(busiC, DirectParams.preWorkDate, "1");

                if(selectInfo1 != null && selectInfo1.size() > 0){
                    //获取消息
                    List<Map<String, Object>> result_list = DirectUtils.getRegisterMsg("20" , DirectParams.preWorkDate);
                    reportClearDao.updateFileInfoEx(result_list);//更新文件信息
                    //循环解析反馈文件
                    for (int i = 0; i < result_list.size(); i++) {
                        selectinfo.add(result_list.get(i));
                        fileIdSet.add(result_list.get(i).get("fileId"));
                    }
                }

                //获取下载失败的文件(测试时出现文件下载失败，抛出异常，此时需要再查询下状态为2的文件)
                List<SqlRow> selectinfo3 = reportClearDao.selectFileInfoEx(busiC, DirectParams.preWorkDate, "2,5");
                for (int i = 0; i < selectinfo3.size(); i++) {
                    if (!fileIdSet.contains(selectinfo3.get(i).get("fileid"))) {
                        selectinfo.add(selectinfo3.get(i));
                    }
                }

                String resultPath = DirectParams.localfilePath + "/result" + File.separator;
                File f = new File(resultPath);
                if (!f.exists()) {
                    if (!f.mkdirs()) {
                        throw new Exception("生成保存出错，文件路径"+DirectParams.localfilePath+"/result"+File.separator+"不存在！");
                    }
                }
                //循环解析反馈文件
                for (int i = 0; i < selectinfo.size(); i++) {
                    Map<String, Object> result = selectinfo.get(i);
                    if ("2".equals(result.get("status")) || "5".equals(result.get("status"))) {// 文件处理成功、文件下载失败才导入
                        //下载文件
                        //downloadResuleFile(result, resultPath,DirectParams.preWorkDate);
                        //解析反馈文件
                        String msgType = (String) result.get("msgType_");
                        err_cnt += parseResultFile(resultPath, (String) result.get("fileName"), msgType, busiC, exSeatMap);
                    }
                }
                //报备文件有问题，请查看反馈信息
                if (err_cnt > 0) {
                    throw new Exception("报备信息有误，请查看反馈文件解析信息");
                }
                //检查文件是否都处理完成
                for (int i = 0; i < selectinfo.size(); i++) {
                    Map<String, Object> result = selectinfo.get(i);
                    if (!"0000".equals(result.get("errorCode"))) {
                        error_msg += result.get("errorText") + ",";
                    }
                }

                //理财中心文件解析有问题， 直接报错
                if (!"".equals(error_msg)) {
                    throw new Exception(error_msg);
                }

                //业务校验,存在未处理完成的文件不允许执行后续的步骤
                String status2 = "1,4,5";
                List<SqlRow> selectinfo2 = reportClearDao.selectFileInfoEx(busiC, DirectParams.preWorkDate, status2);
                if (selectinfo2.size() > 0) {
                    throw new Exception("存在未处理或处理失败的文件:[" + selectinfo2.size() + "]个");
                }
                //  此处需要检查当天是否存在未报送成功的数据(app_cust_register_info app_cust_vol_register_info)
                int cnt = dataFileDao.getUnSendData(exSeatMap.get(busiC), DirectParams.preWorkDate);  //register_filetype
                if (cnt > 0) {
                    throw new Exception("存在未报送或报送失败的数据:[" + cnt + "]条");
                }
            }
        }
    }

    /**
     * 解析结果文件
     * @throws Exception
     */
    public int parseResultFile(String filepath, String zipfilename, String msg_type, String busiC, Map<String, String> exSeatMap) throws Exception{
        //创建时间准备
        String createTime = cn.hutool.core.date.DateUtil.now();

        int err_cnt = 0;
        String filename = zipfilename.replace("zip", "xml");

        //解压文件
        DirectUtils.unZipFile(filepath+zipfilename, filepath);

        //解析文件
        SAXReader reader = new SAXReader();  //创建SAXReader对象
        Document document = reader.read(new File(filepath+filename));  	//读取文件 转换成Document
        Element root = document.getRootElement();  //获取根节点元素对象

        Map<String,Object> param = new HashMap<>();
        param.put("fileType",msg_type);

        //一二期当前返回信息
        if ("WMRS.001.001.01".equalsIgnoreCase(msg_type)) {
            //错误返回的数据行
            Set<String> errorKeys = DirectAnalysisUtil.getErrorPrimaryKeyList(root,"RegistrationSerialNumber");
            //数据更新为报送完成
            reportClearDao.updateStatusOne(exSeatMap.get(busiC) ,DirectParams.preWorkDate);
            //更新错误的数据状态
            if (ObjectUtil.isNotEmpty(errorKeys)) {
                reportClearDao.updateErrorStatusOne(exSeatMap.get(busiC) ,DirectParams.preWorkDate, errorKeys);
            }
        }

        //modify by yangcw  40类消息处理
        if ("WMRS.002.001.01".equalsIgnoreCase(msg_type)) {
            List<Element> prodInformations = root.elements("ProductRegistrationCodeInformationTuple");
            for (int i = 0; i < prodInformations.size(); i++) {
                Element prod_info = prodInformations.get(i);
                String reg_code = prod_info.elementText("ProductRegistrationCode");
                //20180905 判断取值
                String prod_code = "";

                if(prod_info.elementText("ProductCode") != null && !"".equals(prod_info.elementText("ProductCode"))  ) {
                    prod_code = prod_info.elementText("ProductCode");
                }else{
                    prod_code = prod_info.elementText("PreIdentificationCode");
                }

                err_cnt ++;

                param.put("prod_code",prod_code);
                param.put("reg_code",reg_code);
                param.put("workDate",DirectParams.preWorkDate);
                param.put("msg_type",msg_type);
                param.put("createTime",createTime);

                reportClearDao.insertOrUpdateProd(param);
            }
        }else {
            Element correctInformation = root.element("CorrectInformationTuple");//成功信息
            List<Element> errorInformations = root.elements("ErrorInformationTuple");
            //更新数据状态
            reportClearDao.updateRegisterStatus(exSeatMap.get(busiC) ,DirectParams.preWorkDate);
            if(errorInformations!=null && !errorInformations.isEmpty()){
                //获取错误信息
                for (int i = 0; i < errorInformations.size(); i++) {
                    Element error_info = errorInformations.get(i);
                    //String instanceFileName = error_info.elementText("InstanceFileName");
                    String instanceFileName = filename;
                    String instanceFileNo = error_info.elementText("InstanceFileNo");
                    String registrationSerialNumber = error_info.elementText("RegistrationSerialNumber");
                    String errorMessage = error_info.elementText("ErrorMessage");
                    String errorCode = "0000";
                    param.put("msg_type",msg_type);
                    param.put("workDate",DirectParams.preWorkDate);
                    param.put("register_serno",registrationSerialNumber);

                    if("WMRS.201.001.01".equals(msg_type) && (org.apache.commons.lang.StringUtils.isBlank(errorMessage) || (!errorMessage.contains("投资者信息已登记") && !errorMessage.contains("投资者信息重复")))){
                        err_cnt ++;
                        errorCode = "9999";
                        reportClearDao.updateErrorRegisterStatus(param);
                    }
                    if(("WMRS.202.001.01".equals(msg_type)||"WMRS.203.001.01".equals(msg_type)) && (org.apache.commons.lang.StringUtils.isBlank(errorMessage) || !errorMessage.contains("系统中已存在"))){
                        err_cnt ++;
                        errorCode = "9999";
                        reportClearDao.updateErrorRegisterStatus(param);
                    }
                    //截取表id
                    String tableId = instanceFileName.substring(instanceFileName.indexOf("-") + 1, instanceFileName.indexOf("-") + 4);

                    param.put("fileName",instanceFileName);
                    param.put("zzTable",tableId + "_" + DirectParams.dict_name_sys.get("zz_table_ch_name").get(tableId));
                    param.put("fileNo",instanceFileNo);
                    param.put("instanceFileName",instanceFileName);
                    param.put("instanceFileNo",instanceFileNo);
                    param.put("errorMessage",errorMessage);
                    param.put("errorCode",errorCode);
                    param.put("createTime",createTime);


                    reportClearDao.insertResultError(param);
                }

            }
        }
        return err_cnt;
    }
}
