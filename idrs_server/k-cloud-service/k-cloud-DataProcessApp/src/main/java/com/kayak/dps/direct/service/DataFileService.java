package com.kayak.dps.direct.service;


import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jcraft.jsch.ChannelSftp;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.BigDecimalUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.dao.DataFileDao;
import com.kayak.dps.direct.enums.DataFileDirectEnum;
import com.kayak.dps.direct.enums.DataFileEnum;
import com.kayak.dps.direct.model.ExFmt;
import com.kayak.dps.direct.model.ExSeat;
import com.kayak.dps.direct.util.DataFileUtil;
import com.kayak.dps.direct.util.DirectParams;
import com.kayak.dps.direct.util.DirectUtils;
import com.kayak.dps.ods.util.FileZipUtils;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Strings;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataFileService {
    @Autowired
    private ComnDao comnDao;
    @Autowired
    public DataFileDao dataFileDao;

    /**
     * 导出指定创建人、接收人、日期等指定条件数据至指定目录
     * 参考：协议文件里面的：附录-->数据文件
     *            数据文件前缀
     * @throws Exception
     */
    public String exportData(List<ExSeat> exSeatList,String report_date,String busiCode) throws Exception {
        String zipFileName = "";
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
        String[] busiCodeArr = busiCode.split(",");
        String reportNo="";
        // 对于配置的数据文件名的预处理及配置数据字段的提取
        for (int i = 0; i < exSeatList.size(); i++) {
            //分页文件起始
            ExSeat exseat = exSeatList.get(i);
            int divLimit = Integer.parseInt(DirectParams.divLimit2);
            //分文件写入直连数据,一二期默认单文件数量是100000
            if ("ZZ_113".equals(exseat.getExfmtid())) {//产品申报登记单独处理
                reportNo = dealZZ113File(exseat,report_date);
                return reportNo;
            }
            // 读取记录数量
            List<SqlRow> sqlRows = dataFileDao.findTabCount(exseat.getExtab(), report_date);
            Integer recordCnt = sqlRows.get(0).getInteger("cont");
            if(recordCnt == 0)
                continue;
            if(!"117".equals(busiCode)){
                //获取更新数据状态为报送文件已生成
                String updateSql = DirectUtils.createUpdateSql(exseat,report_date);
                HashMap<String, Object> params = new HashMap<>();
                params.put("workdate", DirectParams.workDate);
                comnDao.update(updateSql,params);
            }else{
                divLimit =  Integer.parseInt(DirectParams.divLimit);
            }
            for (int j = 1,k = 0; j <= recordCnt; j++) {
                if (j % divLimit == 0 || j == recordCnt) {
                    k++;
                    //解析数据文件名称
                    List<String> zipFileNameList = new ArrayList<>();
                    String batchCode=getBatchCode(DateUtil.getNowDate(),exseat.getExfmtid());
                    // 插入文件信息
                    String msgType = "REG.001." + busiCodeArr[i];
                    if("".equals(reportNo)){
                        dataFileDao.insertFileInfoSum(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(),report_date,batchCode);
                        reportNo=batchCode;
                    }
                    String filename = DirectUtils.parseFileName(exseat.getFnmfmt(), exseat.getId().getExtpid(), batchCode,
                            DirectParams.bankCode, DateUtil.getNowDate());
                    exseat.setFilename(filename);
                    zipFileNameList.add(filename);
                    //读取字段信息配置
                    exseat.setFieldList(dataFileDao.readFieldList(exseat.getExfmtid()));
                    log.info("开始导出:" + exseat.getFilename() + "...");
                    //报送数据写入
                    exseat.setLeft((k - 1) * divLimit);
                    exseat.setRight(divLimit);
                    if (DataFileEnum.EXTPID_118.getValue().equals(exseat.getId().getExtpid())) {
                        writeTxtDataFile(exseat,report_date);
                    } else {
                        writeDataFile(exseat,report_date);
                    }
                    // 压缩文件名
                    zipFileName = DirectUtils.parseZipFileName(DirectParams.bankCode, exseat.getId().getExtpid(),
                            DateUtil.getNowDate(),batchCode, ".zip");

                    DirectUtils.zipFile(DirectUtils.getFilePath(), zipFileName, zipFileNameList);

                    FileTransfer transfer =  FileTransferHelpler.getTransfer();
                    transfer.uploadFileAndDisconnect(DirectUtils.getFilePath()+File.separator+zipFileName,remotePath+"/zzfile/send/"+zipFileName);
                    File okFile = new File(DirectUtils.getFilePath()+zipFileName.replace(".zip",".ok"));
                    if (okFile.exists()) {
                        okFile.delete(); // 先删除旧文件（可选）
                    }
                    try {
                        okFile.createNewFile();
                        transfer.uploadFileAndDisconnect(okFile.getAbsolutePath(),remotePath+"/zzfile/send/"+okFile.getName());
                        log.info("{} 文件生成完毕 ",zipFileName.replace(".zip",".ok"));
                    } catch (IOException e) {
                        throw new Exception("创建ok文件时出错 !");
                    }
                    dataFileDao.deleteFileInfoEx(DateUtil.getNowDate(), msgType, exseat.getId().getExtpid(), zipFileName);
                    dataFileDao.insertFileInfoEx(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(),report_date,zipFileName, reportNo);
                }
            }

        }
        return reportNo;
    }


    /**
     * 直连报送单文件数据量（分页阈值）
     *
     * @param extTab 表名
     */
    public int getDivLimit(String extTab) {
        return DataFileEnum.APP_SUB_PRD_NAV_INF.getValue().equals(extTab) ? 1000000 : 100000;
    }

    /**
     * 一二期118生成.txt数据文件
     *
     * @param exseat 当前包配置参数
     */
    private void writeTxtDataFile(ExSeat exseat,String report_date) throws Exception {
        String filename = DirectUtils.getFilePath() + exseat.getFilename();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                Files.newOutputStream(Paths.get(filename)), "GBK"))) {
            // 生成表头
            writer.write("WDEP\n");
            writer.write("1.0\n");
            writer.write("20\n");
            writer.write("AppSheetSerialNo\n");
            writer.write("OrgCode\n");
            writer.write("ProdRegCode\n");
            writer.write("NetValRegType\n");
            writer.write("ProdSubShareCode\n");
            writer.write("NetValCcy\n");
            writer.write("NetValReg\n");
            writer.write("NetValRegObvRmb\n");
            writer.write("NetValRegReach\n");
            writer.write("NetValRegTotal\n");
            writer.write("NetValRegObvRmbTotal\n");
            writer.write("NetValRegReinstate\n");
            writer.write("NetValRegObvRmbReinstate\n");
            writer.write("NetValRegEstmBasis\n");
            writer.write("NetValRegShare\n");
            writer.write("NetValDate\n");
            writer.write("PublishDate\n");
            writer.write("NetValRegOtstndBal\n");
            writer.write("NetValRegObvRmbOtstndBal\n");
            writer.write("Rem\n");
            // 创建sql
            String sql = DirectUtils.createSql(exseat,report_date);


            sql =sql + " limit "+exseat.getLeft()+", "+exseat.getRight();
            HashMap<String, Object> params = new HashMap<>();
            // 添加上个工作日及分页参数
            params.put("workdate", DirectParams.workDate);
            // 查询净值登记表满足需要的数据
            List<SqlRow> rows = dataFileDao.findSql(sql, params);
            writer.write(String.valueOf(rows.size()));
            writer.newLine();
            // 写入查询出的数据
            for (SqlRow row : rows) {
                // 校验金额
                String untNav = row.getString("NAV");
                String untNavCny = row.getString("RMB_NAV");
                String acmNav = row.getString("TOTAL_NAV");
                String acmNavCny = row.getString("RMB_TOTAL_NAV");
                String adjustedNav = row.getString("FQ_NAV");
                String adjustedCny = row.getString("RMB_FQ_NAV");
                String lot = row.getString("SHARE");
                String susLot = row.getString("REMAIN_BAL");
                String susLotCny = row.getString("RMB_REMAIN_BAL");
                // 日期校验
                String[] dates = validateAndFormatDates(row.getString("NAV_DATE"), row.getString("DISCLOSURE_DATE"));
                try {
                    Map<String, String> requiredFields = new HashMap<>();
                    requiredFields.put("isu_org_cd", row.getString("BANK_CODE"));
                    requiredFields.put("prod_reg_enc", row.getString("PROD_REG_ENC"));
                    requiredFields.put("register_type", row.getString("NAV_REG_TYPE"));
                    requiredFields.put("ccy", row.getString("CNY"));
                    requiredFields.put("val_source", row.getString("NAV_CAL_TYPE"));
                    // 拼接内容
                    String line = row.getString("REGISTER_SERNO") + "\u0007" +
                            row.getString("BANK_CODE") + "\u0007" +
                            row.getString("PROD_REG_ENC") + "\u0007" +
                            (DataFileEnum.REGISTER_TYPE_01.getValue().equals(row.getString("NAV_REG_TYPE")) ? "01 母产品" : "02 子份额") + "\u0007" +
                            (DataFileEnum.REGISTER_TYPE_01.getValue().equals(row.getString("NAV_REG_TYPE")) ? " " : row.getString("SON_SHARE_CODE")) + "\u0007" +
                            row.getString("CNY") + "\u0007" +
                            untNav + "\u0007" +
                            untNavCny + "\u0007" +
                            ("".equals(row.getString("DJ_NAV")) ? "" : new BigDecimal(row.getString("DJ_NAV")).stripTrailingZeros().toPlainString()) + "\u0007" +
                            acmNav + "\u0007" +
                            acmNavCny + "\u0007" +
                            adjustedNav + "\u0007" +
                            adjustedCny + "\u0007" +
                            (DataFileEnum.VAL_SOURCE_01.getValue().equals(row.getString("NAV_CAL_TYPE")) ? "01 当日估值表结果" :
                                    DataFileEnum.VAL_SOURCE_02.getValue().equals(row.getString("NAV_CAL_TYPE")) ? "02 取上一估值日结果" : "03 其他") + "\u0007" +
                            lot + "\u0007" +
                            dates[0] + "\u0007" +
                            (dates[1] == null ? "" : dates[1]) + "\u0007" +
                            susLot + "\u0007" +
                            susLotCny + "\u0007" +
                            row.getString("DETAILS");
                    writer.write(line);
                    writer.newLine();
                } catch (Exception e) {
                    log.error("行校验失败:{}", e.getMessage());
                    throw new Exception("行校验失败: " + e.getMessage());
                }
            }
            writer.write("WDEPEND");


        } catch (Exception e) {
            log.error("导出.txt数据文件[{}]失败:{}", exseat.getFilename(), e.getMessage());
            throw new Exception("导出.txt文件[" + exseat.getFilename() + "]失败: " + e.getMessage());
        }
    }

    /**
     * 日期校验
     *
     * @param navDt 净值日期
     * @param publishDate 披露日期
     */
    public static String[] validateAndFormatDates(String navDt, String publishDate) {
        // 定义格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        if (navDt == null || navDt.trim().isEmpty()) {
            throw new IllegalArgumentException("净值日期不能为空");
        }
        try {
            LocalDate parsedNavDt = LocalDate.parse(navDt.trim(), inputFormatter);
            // 格式化净值日期
            String formattedNavDt = parsedNavDt.format(outputFormatter);
            // 处理非必填的披露日期
            String formattedPublishDate = Optional.ofNullable(publishDate)
                    .filter(s -> !s.trim().isEmpty())
                    .map(s -> LocalDate.parse(s.trim(), inputFormatter).format(outputFormatter))
                    .orElse(null);
            return new String[]{formattedNavDt, formattedPublishDate};
        } catch (Exception e) {
            log.error("日期格式错误：{}", e.getMessage());
            throw new IllegalArgumentException("日期格式错误：" + (e.getMessage().contains("navDt") ? navDt : publishDate));
        }
    }

    /**
     * 一二期生成数据文件
     *
     * @param exseat 当前包配置参数
     * @return 数据文件记录数
     * @throws Exception
     */
    private void writeDataFile(ExSeat exseat,String report_date) throws Exception {

        String filename = DirectUtils.getFilePath() + exseat.getFilename();

        // 使用dom4j生成xbrl
        OutputFormat format = null;
        XMLWriter writer = null;
        long procRecord = 0; // 实处理记录数
        // 文件记录数
        long recordCnt = 0;
        try {
            /* 打开文件准备写入 */
            format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(Files.newOutputStream(Paths.get(filename)), format);

            // 创建xbrl对象
            Document doc = DocumentHelper.createDocument();
                Element xbrl = xmlHead(DirectParams.bankCode, doc);
            //创建并写入数据
            createData(exseat, recordCnt, xbrl,report_date);
                // Element info = CreateXBRL(xbrl, exseat, duration_id, instant_id);
                writer.write(doc);
        } catch (Exception ex) {
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + ex.getMessage() + procRecord);
        } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    private void writeDataFile113(String tmpPath,ExSeat exseat,SqlRow row) throws Exception {

        String filename =tmpPath +File.separator+ exseat.getFilename();

        // 使用dom4j生成xbrl
        OutputFormat format = null;
        XMLWriter writer = null;
        long procRecord = 0; // 实处理记录数
        // 文件记录数
        long recordCnt = 0;
        try {
            /* 打开文件准备写入 */
            format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(Files.newOutputStream(Paths.get(filename)), format);

            // 创建xbrl对象
            Document doc = DocumentHelper.createDocument();
            Element xbrl = xmlHead(DirectParams.bankCode, doc);
            String wmHead = "wemax:FilingRegistrationElementsOfPublicOrPrivateProductTuple";
            //创建并写入数据
            createBody113(exseat, xbrl, wmHead,row);
            // Element info = CreateXBRL(xbrl, exseat, duration_id, instant_id);
            writer.write(doc);
        } catch (Exception ex) {
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + ex.getMessage() + procRecord);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    private Element xmlHead(String bank_code, Document doc) {
//        String doc_id = "C-D-" + ToolsEx.add(DirectParams.work, -1) + "-" + DirectParams.preWorkDate;
        String doc_id = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        // String instant_id = "C-D-" + pre_date + "-" + DirectParams.preWorkDate;
        Element xbrl = doc.addElement("xbrli:xbrl");

        xbrl.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        xbrl.addAttribute("xsi:schemaLocation", "http://xbrl.org/2006/xbrldi http://www.xbrl.org/2006/xbrldi-2006.xsd");
        xbrl.addNamespace("xbrldi", "http://xbrl.org/2006/xbrldi");
        xbrl.addNamespace("chinawealth_entry_point","http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth/chinawealth_entry_point");
        xbrl.addNamespace("link", "http://www.xbrl.org/2003/linkbase");
        xbrl.addNamespace("cas", "http://xbrl.mof.gov.cn/taxonomy/2015-03-31/cas");
        xbrl.addNamespace("num", "http://www.xbrl.org/dtr/type/numeric");
        xbrl.addNamespace("nonnum", "http://www.xbrl.org/dtr/type/non-numeric");
        xbrl.addNamespace("xbrldt", "http://xbrl.org/2005/xbrldt");
        xbrl.addNamespace("ifrs-full", "http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full");
        xbrl.addNamespace("wemax", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth");
        xbrl.addNamespace("wemax-ret", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth/returnInfo");
        xbrl.addNamespace("wemax-unit", "http://www.chinawealth.com.cn/chinawealth/2016-04-30/custom/unit");
        xbrl.addNamespace("wei", "http://www.chinawealth.com.cn/wei/2016-04-30");
        xbrl.addNamespace("xbrli", "http://www.xbrl.org/2003/instance");
        xbrl.addNamespace("iso4217", "http://www.xbrl.org/2003/iso4217");
        xbrl.addNamespace("xlink", "http://www.w3.org/1999/xlink");

        Element schemaRef = xbrl.addElement("link:schemaRef");
        schemaRef.addAttribute("xlink:type", "simple");
        schemaRef.addAttribute("xlink:href","http://www.chinawealth.com.cn/taxonomy/2016-04-30/chinawealth/chinawealth_entry_point_2016-04-30.xsd");

        Element context = xbrl.addElement("xbrli:context");
        context.addAttribute("id", doc_id);
        Element entity = context.addElement("xbrli:entity");
        Element identifier = entity.addElement("xbrli:identifier");
        identifier.addAttribute("scheme", "http://www.pbc.gov.cn/").addText(bank_code);
        Element period = context.addElement("xbrli:period");
        Element startDate = period.addElement("xbrli:startDate");
        startDate.addText(DirectUtils.format(DirectParams.preWorkDate, "YYYY-MM-DD"));
        Element endDate = period.addElement("xbrli:endDate");
        endDate.addText(DirectUtils.format(DirectParams.preWorkDate, "YYYY-MM-DD"));
        return xbrl;
    }

    /**
     * 报送数据创建入口方法
     * @param exseat 报送类
     * @param recordCnt
     * @param xbrl
     * @throws Exception
     */
    private void createData(ExSeat exseat, long recordCnt, Element xbrl,String report_date) throws Exception {
        String wmHead = "";
        String extpid = exseat.getId().getExtpid();
        if ("113".equals(extpid)) {
            wmHead = "wemax:FilingRegistrationElementsOfPublicOrPrivateProductTuple";
        } else if ("114".equals(extpid)) {
            wmHead = "wemax:IssueRegistrationForPublicOrPrivateProductTuple";
        } else if ("111".equals(extpid)) {
            wmHead = "wemax:TerminationRegistrationTuple";
        } else if ("104".equals(extpid)) {
            wmHead = "wemax:InitialSubscriptionRegistrationTuple";
            createBody104(exseat, recordCnt, xbrl, wmHead,report_date);
            return;
        } else if ("105".equals(extpid)) {
            wmHead = "wemax:SubsequentSubscriptionRegistrationTuple";
            createBody105(exseat, recordCnt, xbrl, wmHead,report_date);
            return;
        } else if ("106".equals(extpid)) {
            wmHead = "wemax:AssetsAndLiabilitiesRegistrationTuple";
        } else if ("108".equals(extpid)) {
            wmHead = "wemax:TransactionRegistrationTuple";
        } else if ("109".equals(extpid)) {
            wmHead = "wemax:ValuationRegistrationTuple";
        } else if ("110".equals(extpid)) {
            wmHead = "wemax:UnderlyingAssetsLiabilitiesRegistrationTuple";
        } else if ("112".equals(extpid)) {
            wmHead = "wemax:PractitionerRegistrationTuple";
        } else if ("115".equals(extpid)) {
            wmHead = "wemax:ProductStatusRegistrationTuple";
        } else if ("117".equals(extpid)) {
            wmHead = "wemax:ProductInvestedAssetsLiabilitiesRegistrationCSVDataExplanator";
            createBody117(exseat, recordCnt, xbrl, wmHead,report_date);
            return;
        }
        try {
            createBody(exseat, xbrl, wmHead,report_date);
        } catch (Exception ex) {
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + ex.getMessage());
        }
    }

    private void createBody117(ExSeat exseat, long recordCnt,Element xbrl, String abstact,String report_date) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String duration_id = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        params.put("workdate", DirectParams.workDate);
        /* 生成sql查询语句 */
        String selsql = DirectUtils.createSql(exseat,report_date);
        List<String> dataValList = new ArrayList<>();
        List<ExFmt> fieldList = exseat.getFieldList();
        String tupleText = fieldList.stream().map(exFmt -> "|wemax_" + exFmt.getId().getItmnm()).collect(Collectors.joining()).replaceFirst("\\|", "") + "\n";

        //最外层标签
        Element tuple = xbrl.addElement(abstact);
        tuple.addAttribute("contextRef", duration_id);
        selsql = selsql + " limit " + exseat.getLeft() + ", " + exseat.getRight();
        List<SqlRow> set = dataFileDao.findSql(selsql, params);
        for (SqlRow sqlRow : set) {
            String dataVal = "";
            for (int i = 0; i < fieldList.size(); i++) {
                ExFmt exfmt = fieldList.get(i);
                String fieldVal = String.valueOf(sqlRow.get(exfmt.getFld()));
                if (ObjectUtil.isEmpty(fieldVal) || "null".equals(fieldVal) || "NULL".equals(fieldVal)) {
                    dataVal = dataVal + "|";
                    continue;
                }
                // 指标值转换，币种不用添加中文
                if (ObjectUtil.isAllNotEmpty(exfmt.getDictItmdic(), fieldVal) && !"tr_cur".equalsIgnoreCase(exfmt.getDictItmdic())) {
                    String dictKey = DirectUtils.getDictNameSys(exfmt.getDictItmdic(), (String) fieldVal);
                    fieldVal += " " + dictKey;
                }
                if (exfmt.getDictItmtp().equals(DirectParams.DATE_RANG)) {
                    fieldVal = Tools.dateFormat(String.valueOf(fieldVal));
                }
                String handleField = handleField(fieldVal, exfmt);
                if (ObjectUtil.isEmpty(exfmt.getDictItmdic())) {
                    //字符串处理
                    if ((ObjectUtil.isEmpty(exfmt.getItmscl()) || exfmt.getItmscl() == 0L) && handleField.length() > exfmt.getItmprc().intValue()) {
                        handleField = handleField.substring(handleField.length() - exfmt.getItmprc().intValue());
                    }
                    //数值类型处理
                    else if (ObjectUtil.isNotEmpty(exfmt.getItmscl()) && exfmt.getItmscl() > 0L){
                        handleField = new BigDecimal(handleField).setScale(exfmt.getItmscl().intValue(), BigDecimal.ROUND_HALF_DOWN).toString();
                    }
                }
                dataVal = dataVal + "|" + handleField;
            }
            dataValList.add(dataVal.replaceFirst("\\|", ""));
        }
        tupleText = "\n" + tupleText + dataValList.stream().collect(Collectors.joining("\n")) + "\n";
        tuple.addCDATA(tupleText);
    }

    private int createBody105(ExSeat exseat, long recordCnt, Element xbrl, String abstact,String report_date)throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String doc_id = "C-I-" + DirectParams.preWorkDate;

        // 添加identifier区域
        Element context = xbrl.addElement("xbrli:context");
        context.addAttribute("id", doc_id);
        Element entity = context.addElement("xbrli:entity");
        Element identifier = entity.addElement("xbrli:identifier");
        identifier.addAttribute("scheme", "http://www.pbc.gov.cn/").addText(DirectParams.bankCode);
        Element period = context.addElement("xbrli:period");
        Element endDate = period.addElement("xbrli:instant");
        endDate.addText(DirectUtils.format(DirectParams.preWorkDate, "YYYY-MM-DD"));

        String duration_id = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        // String instant_id = "C-D-" + pre_date + "-" + DirectParams.preWorkDate;

        params.put("workdate", report_date);
        HashMap<String, Element> unit = new HashMap<String, Element>();

        /* 生成sql查询语句 */
//        String selsql = createSql(exseat);
        String selsql = DirectUtils.createSql(exseat,report_date);

        // 开始导出记录 因考虑数据量过大，需要分页处理
        int read_limit = Integer.parseInt(DirectParams.readLimit);
        Integer cnt = 0;
        Integer count = 0;
        idn = 0;
        while (true) {

            params.put("startline", cnt);
            params.put("endline", read_limit + cnt);
            selsql=selsql+"limit "+cnt+" , "+(read_limit + cnt);
            List<SqlRow> set = dataFileDao.findSql(selsql, params);
            // SqlResult set = comnDao.doQueryBySqlid(SqlUtil.getDataSourceSys(), sqlId,
            // params);sqlQuery
            count = 0;
            for (SqlRow sqlRow :set) {
                idn++;
                Element tuple = xbrl.addElement(abstact);

                count++;
                String uncur = "";
                for (int i = 0; i < exseat.getFieldList().size(); i++) {
                    ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
                        if ("CurrencySubscriptionAndRedemptionTuple".equals(exfmt.getId().getItmnm())) {
                        List<String> rus = Arrays.asList(sqlRow.getString("CCY_AND_PCH_RDM").split(";"));
                        for (String ccyStr : rus) {
                            String[] ccyArr = ccyStr.split(",");
                            String cur = ccyArr[0];
                            String sub_amt_lass_period = ccyArr[1];
                            String cur_principal_period = ccyArr[2];
                            String cur_pay_period = ccyArr[3];
                                idn++;
                                Element fieldTuple = tuple.addElement("wemax:CurrencySubscriptionAndRedemptionTuple");
                                idn++;
                                Element fieldcur = fieldTuple.addElement("wemax:Currency");
                                fieldcur.addAttribute("id", "f" + idn);
                                fieldcur.addAttribute("contextRef", duration_id);
                                fieldcur.addText(cur);
                                idn++;
                                Element fieldAmt = fieldTuple
                                        .addElement("wemax:SubscribedAmountOverLatestSubscriptionPeriodCNY");
                                fieldAmt.addAttribute("unitRef", "CNY");
                                fieldAmt.addAttribute("decimals", "2");
                                fieldAmt.addAttribute("id", "f" + idn);
                                fieldAmt.addAttribute("contextRef", duration_id);
                            BigDecimal bd = new BigDecimal(sub_amt_lass_period)
                                        .setScale(2, BigDecimal.ROUND_HALF_DOWN);
                                fieldAmt.addText(bd.toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");

                                idn++;
                                Element fieldprincipal = fieldTuple.addElement(
                                        "wemax:RedeemedPrincipalAndInterestPaymentOverLatestSubscriptionPeriodCNY");
                                fieldprincipal.addAttribute("unitRef", "CNY");
                                fieldprincipal.addAttribute("decimals", "2");
                                fieldprincipal.addAttribute("id", "f" + idn);
                                fieldprincipal.addAttribute("contextRef", duration_id);
                            BigDecimal bd1 = new BigDecimal(cur_principal_period)
                                        .setScale(2, BigDecimal.ROUND_HALF_DOWN);
                                fieldprincipal.addText(bd1.toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");

                                idn++;
                                Element fieldpay = fieldTuple
                                        .addElement("wemax:RedeemedInterestPaymentOverLatestSubscriptionPeriodCNY");
                                fieldpay.addAttribute("unitRef", "CNY");
                                fieldpay.addAttribute("decimals", "2");
                                fieldpay.addAttribute("id", "f" + idn);
                                fieldpay.addAttribute("contextRef", duration_id);
                            BigDecimal bd2 = new BigDecimal(cur_pay_period)
                                        .setScale(2, BigDecimal.ROUND_HALF_DOWN);
                                fieldpay.addText(bd2.toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");
                            }
                        continue;
                    }
                    if (exfmt.getId().getItmnm().equals("*")) {
                        uncur = sqlRow.getString(exfmt.getFld());
                        continue;
                    }
                    String fieldVal = String.valueOf(sqlRow.get(exfmt.getFld()));
                    if ("CCY_AND_PCH_RDM".equals(Strings.toUpperCase(exfmt.getFld()))) {
                        continue;
                    }
                    addField(fieldVal, exfmt, tuple, xbrl, unit, uncur);
                } // end for
            } // end while

            cnt += read_limit;

            if (cnt >= recordCnt) {// 读取完毕，退出循环
                break;
            }
        }

        return count;
    }

    private Integer idn;
    //多个分级数据项映射
    private Map<String, Element> upExFmtMap;

    /**
     * 定制
     * @param exseat 报送文件信息
     * @param xbrl 数据项构造
     * @param abstact 元结点名称
     * @return
     * @throws Exception
     */
    private void createBody(ExSeat exseat, Element xbrl, String abstact,String report_date)throws Exception {
        idn = 1;
        try {
            //文件头信息处理
            HashMap<String, Element> unit = new HashMap<>();
           // xbrl.addElement("xbrli:unit").addAttribute("id", "CNY").addElement("xbrli:measure").addText("iso4217:CNY");

            /* 生成sql查询语句 */
            HashMap<String, Object> params = new HashMap<>();
            params.put("workdate", DirectParams.preWorkDate);
            String selsql = DirectUtils.createSql(exseat,report_date);

            // 开始导出记录 因考虑数据量过大，需要分页处理
            selsql = selsql + " limit " + exseat.getLeft() + ", " + exseat.getRight();
            List<SqlRow> set = dataFileDao.findSql(selsql, params);
            List<ExFmt> exFmtList = exseat.getFieldList();
            for (SqlRow sqlRow : set) {
                //数据换行，清空上级结点信息
                upExFmtMap = new HashMap<>();
                Element tuple = xbrl.addElement(abstact);
                for (int i = 0; i < exFmtList.size(); i++) {
                    ExFmt exfmt = exFmtList.get(i);
                    if(sqlRow.get(exfmt.getFld())==null){
                        continue;
                    }
                    String fieldVal = String.valueOf(sqlRow.get(exfmt.getFld()));
                    //去掉空数据
                    if (ObjectUtil.isEmpty(fieldVal)) {
                        continue;
                    }
                    //处理特殊情况，多条多选数据字段
//                    if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE_T)) {
//                        String[] fieldStr = fieldVal.split(";");
//                        for (String fieldStrVal : fieldStr) {
//                            initField(exfmt, fieldStrVal, tuple, xbrl, unit);
//                        }
//                        idn++;
//                        continue;
//                    }
                    initField(exfmt, fieldVal, tuple, xbrl, unit);
                    idn++;
                    }
                }
        } catch (Exception e) {
            log.error("导出数据文件[" + exseat.getFilename() + "]失败:" + e.getMessage());
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + e.getMessage());
                }
            }
    /**
     * 定制
     * @param exseat 报送文件信息
     * @param xbrl 数据项构造
     * @param abstact 元结点名称
     * @return
     * @throws Exception
     */
    private void createBody113(ExSeat exseat, Element xbrl, String abstact,SqlRow sqlRow)throws Exception {
        idn = 1;
        try {
            //文件头信息处理
            HashMap<String, Element> unit = new HashMap<>();
            xbrl.addElement("xbrli:unit").addAttribute("id", "CNY").addElement("xbrli:measure").addText("iso4217:CNY");
            // 开始导出记录
//            List<SqlRow> set = dataFileDao.findSql(selsql, params);
            List<ExFmt> exFmtList = exseat.getFieldList();
//            for (SqlRow sqlRow : set) {
            //数据换行，清空上级结点信息
            upExFmtMap = new HashMap<>();
            Element tuple = xbrl.addElement(abstact);
            for (int i = 0; i < exFmtList.size(); i++) {
                ExFmt exfmt = exFmtList.get(i);
                if(sqlRow.get(exfmt.getFld())==null){
                    continue;
                }
                String fieldVal = String.valueOf(sqlRow.get(exfmt.getFld()));
                //去掉空数据
                if (ObjectUtil.isEmpty(fieldVal)) {
                    continue;
                }
                initField(exfmt, fieldVal, tuple, xbrl, unit);
                idn++;
            }
  //      }
        } catch (Exception e) {
            log.error("导出数据文件[" + exseat.getFilename() + "]失败:" + e.getMessage());
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + e.getMessage());
        }
    }
    /**
     *
     * @param exfmt 字段行总信息
     * @param fieldVal 字段值
     * @param xbrl 文件构成信息
     * @param tuple 文件构成信息
     * @param unit 数据单位信息
     */
    private void initField(ExFmt exfmt, String fieldVal, Element tuple, Element xbrl, HashMap<String, Element> unit) throws Exception {
        //分级数据项父节点不为空，属于分级结点，集合处理
        if (ObjectUtil.isNotEmpty(exfmt.getFmtItmUp())) {
            //无上级结点缓存
            if (upExFmtMap.get(exfmt.getFmtItmUp()) == null) {
                upExFmtMap.put(exfmt.getFmtItmUp(), tuple.addElement("wemax:" + exfmt.getFmtItmUp()));
            }
            //多选定长字符，默认按照“,”多选分割
            if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE)) {
                String[] dictTStr = fieldVal.split(",");
                for(int k=0;k<dictTStr.length;k++){
                    if(k > 0){
                        upExFmtMap.put(exfmt.getFmtItmUp(), tuple.addElement("wemax:" + exfmt.getFmtItmUp()));
                    }
                    idn++;
                    DataFileUtil.addField(dictTStr[k], exfmt, upExFmtMap.get(exfmt.getFmtItmUp()), xbrl, unit, idn);
                }
                return;
            }
            if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE_T)) {
                String[] dictTStr = fieldVal.split(";");
                for (String dictT : dictTStr) {
                    idn++;
                    DataFileUtil.addField(dictT, exfmt, upExFmtMap.get(exfmt.getFmtItmUp()), xbrl, unit, idn);
                }
                return;
            }
            if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE_TF)) {
                String[] fieldValTStr = fieldVal.split(";");
                for (String itm : fieldValTStr) {
                    idn++;
                    DataFileUtil.addField(itm, exfmt, upExFmtMap.get(exfmt.getFmtItmUp()), xbrl, unit, idn);
                }
                return;
            }
            DataFileUtil.addField(fieldVal, exfmt, upExFmtMap.get(exfmt.getFmtItmUp()), xbrl, unit, idn);
            return;
        }

        //多选定长字符，默认按照“,”多选分割
        if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE) || exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_TUPLE_TF)) {
            String[] dictTStr = fieldVal.split(",");
            for (String dictT : dictTStr) {
                DataFileUtil.addField(dictT, exfmt, tuple, xbrl, unit, idn);
            }
            return;
        }
        DataFileUtil.addField(fieldVal, exfmt, tuple, xbrl, unit, idn);
    }

//    private int createBody(ExSeat exseat, Element xbrl, String abstact)throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("workdate", DirectParams.preWorkDate);
//        HashMap<String, Element> unit = new HashMap<String, Element>();
//        /* 生成sql查询语句 */
//        String selsql = DirectUtils.createSql(exseat);
//
//        // 开始导出记录 因考虑数据量过大，需要分页处理
//        Integer count = 0;
//        idn = 0;
//        selsql=selsql+" limit "+ exseat.getLeft() +", " + exseat.getRight();
//        List<SqlRow> set = dataFileDao.findSql(selsql, params);
//
//        List<ExFmt> exFmtList = exseat.getFieldList();
//        //报送文件前缀处理
//        if (!"112".equals(exseat.getId().getExtpid())) {
//            xbrl.addElement("xbrli:unit").addAttribute("id", "CNY").addElement("xbrli:measure").addText("iso4217:CNY");
//        }
//        count = 0;
//        for (SqlRow sqlRow :set) {
//            idn++;
//            Element tuple = xbrl.addElement(abstact);
//            count++;
//            String uncur = "";
//            for (int i = 0; i < exFmtList.size(); i++) {
//                ExFmt exfmt = exFmtList.get(i);
//                //多个分级数据项映射
//                Map<String, Element> upExFmtMap = new HashMap<>();
//                Object fieldVal = sqlRow.get(exfmt.getFld());
//                if (exfmt.getId().getItmnm().equals("Currency")) {
//                    uncur = String.valueOf(fieldVal);
//                }
//                //分级数据项父节点不为空，属于分级结点，集合处理
//                if (ObjectUtil.isNotEmpty(exfmt.getFmtItmUp())) {
//                    //无上级结点缓存
//                    if (upExFmtMap.get(exfmt.getFmtItmUp()) == null) {
//                        upExFmtMap.put(exfmt.getFmtItmUp(), tuple.addElement("wemax:" + exfmt.getFmtItmUp()));
//                    }
//                    DataFileUtil.addField(fieldVal, exfmt, upExFmtMap.get(exfmt.getFmtItmUp()), xbrl, unit, idn);
//                    continue;
//                }
//                DataFileUtil.addField(fieldVal, exfmt, tuple, xbrl, unit, idn);
//            }
//        }
//
//        return count;
//    }

    private int createBody104(ExSeat exseat, long recordCnt, Element xbrl, String abstact,String report_date) throws Exception{
        HashMap<String, Object> params = new HashMap<String, Object>();
        // Element context = null;
        // String pre_date = ;
        String duration_id = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        // String instant_id = "C-D-" + pre_date + "-" + DirectParams.preWorkDate;
        params.put("workdate", report_date);
        HashMap<String, Element> unit = new HashMap<String, Element>();
        // List<Element> data = new ArrayList<Element>();
        /*
         * Element PURE = null; Element CNY = null; Element Person = null; Element Month
         * = null; Element Day = null; Element Product = null; Element Count = null;
         * Element MonthPerCOunt = null;
         */
        /* 生成sql查询语句 */
        String selsql = DirectUtils.createSql(exseat,report_date);
        // 开始导出记录 因考虑数据量过大，需要分页处理
        int read_limit = Integer.parseInt(DirectParams.readLimit);
        int cnt = 0;
        int count = 0;
        idn = 0;
        while (true) {

            params.put("startline", cnt);
            params.put("endline", read_limit + cnt);
            selsql=selsql+"limit "+cnt+" , "+(read_limit + cnt);
            // SqlResult set = comnDao.query(sqlId, params);
            List<SqlRow> set = dataFileDao.findSql(selsql, params);
            count = 0;
            for (SqlRow sqlRow :set) {
                idn++;
                Element tuple = xbrl.addElement(abstact);

                count++;

                for (int i = 0; i < exseat.getFieldList().size(); i++) {

                    ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
                    // 产品销售区域及募集金额
                        if ("ProductAreaAndSubscriptionAmountTuple".equals(exfmt.getId().getItmnm())) {
                            HashMap<String, Object> param = new HashMap<String, Object>();
                            param.put("prod_code", sqlRow.get("prod_code"));
                            param.put("register_serno", sqlRow.get("register_serno"));
                        List<String> rus = Arrays.asList(sqlRow.getString("ZON_CLC_AMT").split(";"));
                        for (String clcStr : rus) {
                            String[] clcArr = clcStr.split(",");
                            if (clcArr.length < 2) {
                                continue;
                            }
                            String subscriptAmtRegion = clcArr[0];
                            String prodSalesRegion = clcArr[1];
                                idn++;
                                Element fieldTuple = tuple.addElement("wemax:ProductAreaAndSubscriptionAmountTuple");
                                idn++;
                                Element fieldProd = fieldTuple.addElement("wemax:ProductSalesRegion");
                                fieldProd.addAttribute("id", "f" + idn);
                                fieldProd.addAttribute("contextRef", duration_id);
                            fieldProd.addText(subscriptAmtRegion);
                                idn++;
                                Element fieldAmt = fieldTuple.addElement("wemax:SubscriptionAmountByRegion");
                                fieldAmt.addAttribute("unitRef", "CNY");
                                fieldAmt.addAttribute("decimals", "2");
                                fieldAmt.addAttribute("id", "f" + idn);
                                fieldAmt.addAttribute("contextRef", duration_id);

                            fieldAmt.addText(new BigDecimal(prodSalesRegion)
                                        .setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");

                            }
                        continue;
                        } else if ("SubscriptionCurrencyAndSubscriptionAmountTuple".equals(exfmt.getId().getItmnm())) {
                        // TODO: 2023/5/8 认购币种及金额，表中只有prod_ccy（产品币种字段）
                            HashMap<String, Object> param = new HashMap<String, Object>();
                            param.put("prod_code", sqlRow.get("prod_code"));
                            param.put("register_serno", sqlRow.get("register_serno"));
                        List<String> rus = Arrays.asList(sqlRow.getString("PROD_CCY").split(";"));

                        for (String ccyStr : rus) {
                            String[] ccyArr = ccyStr.split(",");
                            if (ccyArr.length < 3) {
                                continue;
                            }
                            String cur = ccyArr[0];
                            String scriptAmt = ccyArr[1];
                            String convertRmb = ccyArr[2];
                                idn++;
                                Element fieldTuple = tuple
                                        .addElement("wemax:SubscriptionCurrencyAndSubscriptionAmountTuple");
                                idn++;
                                Element fieldAmt = fieldTuple.addElement("wemax:SubscriptionAmount");
                                fieldAmt.addAttribute("unitRef", cur);
                                fieldAmt.addAttribute("decimals", "2");
                                fieldAmt.addAttribute("id", "f" + idn);
                                fieldAmt.addAttribute("contextRef", duration_id);
                            fieldAmt.addText(new BigDecimal(scriptAmt)
                                        .setScale(2, RoundingMode.HALF_DOWN).toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:" + cur);
                                idn++;
                                Element fieldRmb = fieldTuple.addElement("wemax:SubscriptionAmountCNY");
                                fieldRmb.addAttribute("unitRef", "CNY");
                                fieldRmb.addAttribute("decimals", "2");
                                fieldRmb.addAttribute("id", "f" + idn);
                                fieldRmb.addAttribute("contextRef", duration_id);
                            fieldRmb.addText(new BigDecimal(convertRmb)
                                        .setScale(2, RoundingMode.HALF_DOWN).toPlainString());
                                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");
                            }
                        continue;
                    }
                    String fieldVal = String.valueOf(sqlRow.get(exfmt.getFld()));
                    addField(fieldVal, exfmt, tuple, xbrl, unit, "");
                } // end for
                // cdata.append(LINE_ENDERSTR);
            } // end while

            cnt += read_limit;

            if (cnt >= recordCnt) {// 读取完毕，退出循环
                break;
            }
        }
        // xbrl.elements().addAll(data);
        return count;
    }

    private void addField(String fieldVal, ExFmt exfmt, Element tuple, Element xbrl, HashMap<String, Element> unit,
                          String uncur) throws Exception {
        String duration_id = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        String instant_id = "C-I-" + DirectParams.preWorkDate;

        if (ObjectUtil.isEmpty(fieldVal) || "null".equals(fieldVal) || "NULL".equals(fieldVal)) {
            return;
        }
        Object[] fieldvals = null;


        fieldvals = new Object[] { fieldVal };

        for (Object fieldval : fieldvals) {
            // 指标值转换，币种不用添加中文
            if (ObjectUtil.isAllNotEmpty(exfmt.getDictItmdic(), fieldval) && !"tr_cur".equalsIgnoreCase(exfmt.getDictItmdic())) {
                String dictKey = DirectUtils.getDictNameSys(exfmt.getDictItmdic(), (String) fieldval);
                //暂时用是否包含空格来判断值域是否需要加上原始值
                fieldval += " " + dictKey;
        }
            idn++;
            Element field = null;
                field = tuple.addElement("wemax:" + exfmt.getId().getItmnm());
            if (exfmt.getDictItmmem() != null && (exfmt.getDictItmmem().equals(DirectParams.INSTANT_DATE))) {
                field.addAttribute("contextRef", instant_id);
                if (!unit.containsKey("context"))
                    unit.put("context", DirectUtils.createInstantId(xbrl, instant_id));
            } else {
                field.addAttribute("contextRef", duration_id);

            }
            field.addAttribute("id", "f" + idn);
            if (exfmt.getDictItmtp() != null) {
                if (exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_NUMBER)) {

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "PURE");
                    DirectUtils.addUnit(xbrl, unit, "xbrli:pure");

                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_CUR)) {

                    DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "CNY");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_PERSON)) {

                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Person");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "Person");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_MONTH)) {
                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Month");
                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "Month");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_DAY)) {

                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Day");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "Day");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_PRODUCT)) {

                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Product");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "Product");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_COUNT)) {

                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Count");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "Count");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.UNIT_MONTHPERCOUNT)) {

                    DirectUtils.addUnit(xbrl, unit, "wemax-unit:Month/wemax-uinit:Count");

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), "MonthPerCount");
                }
                if (exfmt.getDictItmtp().equals(DirectParams.GIVEN_CUR)) {

                    DirectUtils.addUnit(xbrl, unit, "iso4217:" + uncur);

                    fieldval = DirectUtils.addNumField(fieldVal, field, exfmt.getItmscl(), uncur);
                }
            }

            field.addText(handleField(fieldval, exfmt));
        }

    }

    /**
     * 不同的报表直连报送定制不同的参数处理
     * @param fieldval
     * @param exfmt
     * @return
     * @throws ParseException
     */
    public String handleField(Object fieldval, ExFmt exfmt) {
        String fieldStr = String.valueOf(fieldval);
        try {
            // 登记流水号统一减少两位
            /*if (ObjectUtil.isNotEmpty(fieldval) && "REGISTER_SERNO".equals(exfmt.getFld()) && fieldStr.length() > 2) {
                fieldStr = fieldStr.substring(2);
            }*/

            // 估值日期、理财产品实际终止日期等转为yyyy-MM-dd的格式
            if (EnumUtil.contains(DataFileDirectEnum.class, exfmt.getFld())) {
                fieldStr = Tools.dateFormat(fieldStr);
            }

            // FLD_115的杠杆率需要除以100
            if ("ZZ_115".equals(exfmt.getId().getExfmtid()) && "RATE".equals(exfmt.getFld())) {
                BigDecimal fieldDecimal = new BigDecimal(0);
                if (ObjectUtil.isNotEmpty(fieldval) && NumberUtil.isNumber((CharSequence) fieldval)) {
                    fieldDecimal = BigDecimalUtil.getBigDecimal(fieldval);
                }
                fieldStr = String.valueOf(fieldDecimal.divide(new BigDecimal(100)));
            }
        } catch (Exception e) {
            log.error("直连特殊数据处理异常", e);
        }
        return fieldStr;
    }
    public String getBatchCode(String nowDate,String portCode) throws Exception {
        int newBctchCode=1;
        Map<String,String> params=new HashMap<>();
        params.put("deal_date",nowDate);
        params.put("port_code",portCode);
        String sql = "SELECT  port_code, batch_code, deal_date FROM base_file_batch_code " +
                "where deal_date=$S{deal_date} and port_code=$S{port_code}";
        List<SqlRow> list = comnDao.findRows(sql, params);
        if(CollectionUtils.isEmpty(list)){//当日没有生成文件，则批次号记录1
            comnDao.update("INSERT INTO base_file_batch_code (port_code, batch_code, deal_date) " +
                    "VALUES($S{port_code},1,$S{deal_date})",params);
        }else{
            newBctchCode=list.get(0).getInteger("batch_code")+1;
            comnDao.update("update base_file_batch_code set " +
                            "batch_code="+newBctchCode+
                            " where deal_date=$S{deal_date} and port_code=$S{port_code}",
                    params);
        }
        return newBctchCode>99?String.format("%03d",newBctchCode):String.format("%02d",newBctchCode);
    }
    public Map<String, String> queryPs() throws Exception {
        String qdeal = "  select t.id,t.config_describe,t.config_name,"+
                " t.config_code,t.config_type,t.status "+
                " from base_port_config_info t "+
                " where t.config_type = 'JDFJZL' and t.status ='1'" +
                " and t.config_name in('SFTP_IP','USERNAMES','PASSWORD')";
        List<SqlRow> rs= comnDao.findRows(qdeal);
        Map<String, String> map= new HashMap<>();
        for(SqlRow r:rs){
            map.put(r.getString("config_name"),r.getString("config_code"));
        }
        return map;
    }
    /***
     * remotePath目录下是九大附件的文件例如 01、02、03、04，每个文件夹内是每种附件的文件，提取文件夹名放到key，value存这个文件夹下所有的文件，并下载文件
     */
    public Map<String,List<String>>  getFiles(String remotePath, String temPath,String zipPath,String filePath) throws Exception {
        Map<String,List<String>> resultMap = new HashMap<>();

        try {
            FileTransfer transfer =  FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remotePath,temPath);
            FileZipUtils.unZipFile(temPath,zipPath);
            File rootDirectory = new File(zipPath);
            if (rootDirectory.exists() && rootDirectory.isDirectory()) {
                File[] allItems = rootDirectory.listFiles();
                for (File item : allItems) {
                    File[] files = item.listFiles();
                    List<String> fileNames = new ArrayList<>();
                    for (File file : files) {
                        if (file.isFile()) {
                            fileNames.add(file.getName());
                            this.moveFile(file.getAbsolutePath(),filePath+file.getName());
                        }
                    }
                    resultMap.put(item.getName(),fileNames);
                }
            }
        } catch (Exception e) {
            log.error("文件下载oss失败", e);
        }
        return resultMap;

    }

    public String dealZZ113File(ExSeat exseat,String report_date) throws Exception {
        String zipFileName = "";
        String batchCode="";
        String filename="";
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");

        String temPath =  DirectUtils.getFilePath();
        exseat.setFieldList(dataFileDao.readFieldList(exseat.getExfmtid()));
        Map<String, Object> params = new HashMap<>();
        String selsql = DirectUtils.createSql(exseat,report_date);
        String updateSql = DirectUtils.createUpdateSql(exseat,report_date);
        params.put("workdate", DirectParams.workDate);
        List<SqlRow> set = dataFileDao.findSql(selsql, params);
        String docName = "";
        String reportNo = "";
        if(set.size()>0){
            comnDao.update(updateSql,params);
        }
        for (SqlRow row:set) {
            //产品申报登记一个产品一个压缩包
            List<String> zipFileNameList = new ArrayList<>();
            batchCode=getBatchCode(DateUtil.getNowDate(),"ZZ_113");
            // 插入文件信息
            String msgType = "REG.001.113";
            if("".equals(reportNo)){
                dataFileDao.insertFileInfoSum(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(),report_date,batchCode);
                reportNo=batchCode;
            }
            //解析数据文件名称
            filename = DirectUtils.parseFileName(exseat.getFnmfmt(), exseat.getId().getExtpid(), batchCode,
                    DirectParams.bankCode,DateUtil.getNowDate());
            exseat.setFilename(filename);
            exseat.setFieldList(dataFileDao.readFieldList(exseat.getExfmtid()));
            zipFileNameList.add(filename);
            //生成文件之前，先删除目标文件夹下所有文件和目录
            File file = new File(temPath+row.getString("IDENT_CODE"));
            if (!file.exists()) {
                file.mkdirs();
            }
            deleteNonZipFilesAndEmptyDirs(file);
            //获取九大附件名称，并且下载到本地
            Map<String,List<String>> fileMaps=getFiles(remotePath+"/zzfile/NineAttachments/"+row.getString("IDENT_CODE")+".zip",temPath+"/zzfile/"+row.getString("IDENT_CODE")+".zip",
                    temPath+"/zzfile/"+row.getString("IDENT_CODE"),temPath+row.getString("IDENT_CODE")+"/");
            if(fileMaps.size()>0){
                for (Map.Entry<String, List<String>> entry : fileMaps.entrySet()) {
                    docName=String.join(";" , entry.getValue());
                    if(StringUtils.equals(entry.getKey(),"01")){//报告主文件
                        row.put("MAIN_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"02")){//理财产品可行性评估报告
                        row.put("FEASY_ASS_REPORT",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"03")){//内部审核文件
                        row.put("INTER_AUDIT_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"04")){//对理财产品投资管理人、托管人、投资顾问等相关方的尽职调查文件
                        row.put("DUE_DILIGENCR_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"05")){//与理财产品投资管理人、托管人、投资顾问等相关方签署的法律文件
                        row.put("LEGAL_DOC_SIFNED",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"06")){//理财产品销售文件
                        row.put("PROD_SALE_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"07")){//理财产品说明书
                        row.put("PROD_SPECIFI",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"08")){//理财产品宣传材料
                        row.put("PROD_MARK_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"09")){//流动性风险评估文件
                        row.put("LIQUIDITY_RISK_DOC",docName);
                    }
                    if(StringUtils.equals(entry.getKey(),"99")){//其他材料
                        row.put("OTHER_DOC",docName);
                    }

                    for(String itm:entry.getValue()){
                        zipFileNameList.add(itm);
                    }
                }
            }
            log.info("开始导出:" + exseat.getFilename() + "...");
            //报送数据写入
            writeDataFile113(temPath+row.getString("IDENT_CODE"),exseat,row);
            try {
                // 压缩文件名
                zipFileName = DirectUtils.parseZipFileName(DirectParams.bankCode, exseat.getId().getExtpid(),
                        DateUtil.getNowDate(),batchCode, ".zip");
                DirectUtils.zipFile(temPath+row.getString("IDENT_CODE")+File.separator, zipFileName, zipFileNameList);
                log.info("获得压缩文件： {}",temPath+row.getString("IDENT_CODE")+File.separator+ zipFileName);
                FileTransfer transfer =  FileTransferHelpler.getTransfer();
                transfer.uploadFileAndDisconnect(temPath+row.getString("IDENT_CODE")+File.separator+zipFileName,remotePath+"/zzfile/send/"+zipFileName);
                File okFile = new File(temPath+zipFileName.replace(".zip",".ok"));
                if (okFile.exists()) {
                    okFile.delete(); // 先删除旧文件
                }
                try {
                    okFile.createNewFile();
                    transfer.uploadFileAndDisconnect(okFile.getAbsolutePath(),remotePath+"/zzfile/send/"+okFile.getName());
                    log.info("{} 文件生成完毕 ",zipFileName.replace(".zip",".ok"));
                } catch (IOException e) {
                    throw new Exception("创建ok文件时出错 !");
                }
                dataFileDao.deleteFileInfoEx(DateUtil.getNowDate(), msgType, exseat.getId().getExtpid(), zipFileName);
                dataFileDao.insertFileInfoEx(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(), report_date,zipFileName, reportNo);
            } catch (Exception e) {
                log.error("获取远端服务器文件失败：", e);
                throw new Exception("申报登记直连文件生成失败："+e.getMessage());
            }
        }
        return reportNo;
    }
    private void deleteNonZipFilesAndEmptyDirs(File dir) throws Exception {
        if (dir == null || !dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                try {
                    file.delete();
                    log.info("已删除文件: " + file.getAbsolutePath());
                } catch (Exception e) {
                    log.info("删除文件失败: " + file.getAbsolutePath());
                    throw new Exception("删除文件失败: " + file.getAbsolutePath());
                }
            } else if (file.isDirectory()) {
                // 递归清理子目录
                deleteNonZipFilesAndEmptyDirs(file);

                // 清理子目录后，如果为空则删除
                if (file.list() != null && file.list().length == 0) {
                    if (file.delete()) {
                        log.info("已删除空文件夹: {}", file.getAbsolutePath());
                    } else {
                        throw new Exception("删除空文件夹失败: "+ file.getAbsolutePath());
                    }
                }
            }
        }
    }
    public  void moveFile(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);

        // 检查源文件是否存在
        if (!Files.exists(sourcePath)) {
            throw new IOException("源文件不存在: " + source);
        }

        // 如果目标目录不存在，则创建
        Path parentDir = destinationPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        log.info("源文件：{},移动后的新文件：{}",source,destination);
        // 移动文件，如果目标文件已存在则替换
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

}
