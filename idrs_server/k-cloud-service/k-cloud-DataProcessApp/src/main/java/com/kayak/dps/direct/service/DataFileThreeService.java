package com.kayak.dps.direct.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.direct.dao.DataFileDao;
import com.kayak.dps.direct.model.ExFmt;
import com.kayak.dps.direct.model.ExSeat;
import com.kayak.dps.direct.util.DirectParams;
import com.kayak.dps.direct.util.DirectUtils;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class DataFileThreeService {

    @Autowired
    private ComnDao comnDao;

    @Autowired
    public DataFileDao dataFileDao;


    private List<ExSeat> exSeatList;

    private static final int EXECUTOR_TIMEOUT = 100;


    /**
     * 导出指定创建人、接收人、日期等指定条件数据至指定目录
     *
     * 参考：协议文件里面的：附录-->数据文件
     *
     * @throws Exception
     */
    public String exportData(List<ExSeat> exSeatList,String report_date,String busiCode,String report_table) throws Exception {
        long div_limit_p =  Integer.parseInt(DirectParams.divLimit);
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
        String reportNo = "";
        //循环写文件
        for (int i = 0; i < exSeatList.size(); i++) {
            ExSeat exseat = exSeatList.get(i);
            /* 读取字段信息配置 */
            exseat.setFieldList(dataFileDao.readFieldList(exseat.getExfmtid()));
            int totalcnt = 0;
            // 读取记录数量
            List<SqlRow> sqlRows = dataFileDao.findTabMaxId(report_table, report_date,"2");
            Long minId = sqlRows.get(0).getLong("minId");
            Long maxId = sqlRows.get(0).getLong("maxId");
            Long cnt = sqlRows.get(0).getLong("cnt");
            Long recod = maxId - minId + 1;
            if(maxId == 0)
                continue;
            // 总记录数小于步长时，直接取最大ID值，只生成一个文件
            long div_limit = cnt < div_limit_p && div_limit_p < maxId ? maxId : div_limit_p;
            Long size = recod / div_limit + 1;

            //获取更新数据状态为报送文件已生成，三期只更新201身份信息
            if(busiCode.equals("201")){
                for (int j = 0; j < size; j++) {
                    Long start = minId + div_limit * j;
                    dataFileDao.updateDataFileStatus(exseat.getExtab(), report_date, start , start + div_limit);
                }
            }
            // 多线程生成文件
            int maxPoolSize = Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051113"));
            ExecutorService executorService = new ThreadPoolExecutor(maxPoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000));
            List<Future<?>> futures = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                //String fileIndexId = String.format("%02d", maxId);
                String fileIndexId = getBatchCode(DateUtil.getNowDate(),exseat.getExfmtid());
                String filename = DirectUtils.parseFileName(exseat.getFnmfmt(), exseat.getId().getExtpid(), fileIndexId,DirectParams.bankCode, DateUtil.getNowDate());
                String msgType="REG.001."+ busiCode;
                if("".equals(reportNo)){
                    dataFileDao.insertFileInfoSum(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(),report_date,fileIndexId);
                    reportNo=fileIndexId;
                }
                String finalReportNo = reportNo;
                Long start = minId + div_limit*j;
                Future<?> future = executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //exseat.setFilename(filename);
                            //需要传入起始行及当前需要的总量
                            Long procRecord = writeDataFile(exseat, filename, start , div_limit, div_limit_p, report_date, recod,report_table,busiCode);

                            String zipFileName = DirectUtils.parseZipFileName(DirectParams.bankCode,exseat.getId().getExtpid(),DateUtil.getNowDate(),fileIndexId,".zip");
                            if(procRecord>0L){ //没有报送数据，不生成文件， 因此不需要压缩文件
                                //压缩文件
                                DirectUtils.zipFile(DirectUtils.getFilePath(), zipFileName, filename);

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

                                //防止文件过大，每次生成压缩文件后进行删除原文件 LPC
                                File file = new File(DirectUtils.getFilePath()+filename);
                                file.delete();

                                // TODO 此处需要插入文件信息
                                dataFileDao.deleteFileInfoEx(DateUtil.getNowDate(), msgType, exseat.getId().getExtpid(),zipFileName);
                                dataFileDao.insertFileInfoEx(exseat.getId().getExtpid(), msgType, DateUtil.getNowDate(),report_date, zipFileName, finalReportNo);
                            }

                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }

                    }
                });
                futures.add(future);

                /*totalcnt += div_limit;

                if(recod <= totalcnt){
                    break;
                }*/
            }

            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get(); // 等待每个任务完成
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // 关闭线程池
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(EXECUTOR_TIMEOUT, TimeUnit.MINUTES)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(EXECUTOR_TIMEOUT, TimeUnit.MINUTES)) {
                        log.error("线程池未能在指定时间内关闭，部分任务可能未完成");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executorService.shutdownNow();
            }
        }
        return reportNo;
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



    /**
     * 生成数据文件
     *
     * @param exseat
     *            当前包配置参数
     * @param div_limit  //分文件数据量
     * @param start   //查询起始行
     * @return 数据文件记录数
     * @throws Exception
     */
    private long writeDataFile(ExSeat exseat, String filename, long start, long div_limit, long div_limit_p, String report_date, long recordCnt,String report_table,String busiCode) throws Exception {
        long end = start + div_limit;
        filename = DirectUtils.getFilePath() + filename;
        HashMap<String, Object> params = new HashMap<String, Object>();
        /* 生成sql查询语句 */
        StringBuffer sqlbuf = new StringBuffer("select ");
        for (int i = 0; i < exseat.getFieldList().size(); i++) {
            ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
            if (!exfmt.getFld().equals("*")) {//lpw 20180831 将拼接的字段判断金额字段进行to_char
                //System.out.println("=============================="+exfmt.getFld());
                if("HOLD_AMT".equals(exfmt.getFld().toUpperCase()) ||"CONVERT_RMB".equals(exfmt.getFld().toUpperCase())
                        ||"ACK_AMT".equals(exfmt.getFld().toUpperCase()) ||"FEE_AMT".equals(exfmt.getFld().toUpperCase()) ){
                    sqlbuf.append("convert("+exfmt.getFld()+",decimal(25,2))"+ exfmt.getFld());

                }else if("HOLD_VOL".equals(exfmt.getFld().toUpperCase()) || "ACK_VOL".equals(exfmt.getFld().toUpperCase())){
                    sqlbuf.append("convert("+exfmt.getFld()+",decimal(28,5))"+ exfmt.getFld());
                }else if("NAV".equals(exfmt.getFld().toUpperCase()) ){
                    sqlbuf.append("convert("+exfmt.getFld()+",decimal(16,5))"+ exfmt.getFld());
                }else if("HOLD_DATE".equals(exfmt.getFld().toUpperCase()) || "ACK_DATE".equals(exfmt.getFld().toUpperCase())){
                    sqlbuf.append("DATE_FORMAT("+exfmt.getFld()+",'%Y-%m-%d')"+ exfmt.getFld());
                }else{
                    sqlbuf.append(exfmt.getFld());
                }

                if (i < exseat.getFieldList().size() - 1)
                    sqlbuf.append(",");
            }
        }
        /*String swhere = " where theory_report_start_date='" + report_date+"'";

        if (exseat.getId().getExtpid().contains("201")) {
            swhere = " where register_date='" + DirectParams.workDate+"'";
        }else if (exseat.getId().getExtpid().contains("202")) {
            swhere = " where hold_date='" + DirectParams.workDate+"'";
        }else if (exseat.getId().getExtpid().contains("203")) {
            swhere = " where ack_date='" + DirectParams.workDate+"'";
        }

        if ((exseat.getId().getExtpid().contains("201")) || (exseat.getId().getExtpid().contains("202")) || (exseat.getId().getExtpid().contains("203"))) {
            swhere = swhere + "  and sys_data_status='1' and register_status in ('2') ";
        }*/
        String selsql = sqlbuf.toString();
        if (selsql.charAt(selsql.length() - 1) == ',')
            selsql = selsql.substring(0, selsql.length() - 1);
        //selsql += " from " + exseat.getExtab() + swhere;
        String swhere = "";
        if("201".equals(busiCode)){
            swhere = " from " + report_table + " REG where theory_report_start_date='" + report_date
                    + "' and sys_data_status='1' AND REG.register_status IN ('5') "
                    + " and id >= " + start + " and id < " + end + " " ;
        }else{
            swhere = " from " + report_table + " REG where theory_report_start_date='" + report_date
                    + "' and sys_data_status='1' AND REG.register_status IN ('2') "
                    + " and id >= " + start + " and id < " + end + " " ;
        }
        selsql += swhere;
        /*if (exseat.getId().getExtpid().contains("202")) {
            selsql = selsql + " order by ID" ;
        }else if (exseat.getId().getExtpid().contains("201")) {
            selsql = selsql + "  order by data_type, cust_no, register_serno ";
        }else if (exseat.getId().getExtpid().contains("203")) {
            selsql = selsql + " order by register_serno ";
        }*/

        //String cntsql = "select count(0) cont " + swhere;
//        log.info("sql[select]=" + selsql);
//        log.info("sql[count]=" + cntsql);

        //使用dom4j生成xbrl
        OutputFormat format = null;
        XMLWriter writer = null;
        long procRecord = 0; // 实处理记录数
        //long recordCnt = 0; // 文件记录数
        long count = 0;      //当前文件记录数
        try {
            /* 读取记录数 */
            /*List<SqlRow> st = dataFileDao.findSql(cntsql, null);
            if (st != null || st.size() > 0)
                recordCnt = st.get(0).getInteger("cont");

            if(recordCnt==0L){
                return procRecord;   //没有报送数据，不生成文件，
            }*/
            //modify by 百万分文件改造
            /*if(start + div_limit >= recordCnt){
                count = recordCnt - start;
            }else{
                count = div_limit;
            }*/


            /* 打开文件准备写入 */
            format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(new FileOutputStream(filename), format);
            if (recordCnt > 0L) {
//                log.info("开始写文件文件名：" + filename + " 编码：" + format.getEncoding());
                StringBuffer cdata = new StringBuffer(DirectParams.LINE_ENDERSTR);
                for (int i = 0; i < exseat.getFieldList().size(); i++) {
                    ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
                    if(i+1 == exseat.getFieldList().size()){
                        cdata.append("wemax_" + exfmt.getId().getItmnm() );
                    }else{
                        cdata.append("wemax_" + exfmt.getId().getItmnm() + DirectParams.LOADFILE_SPLITER);
                    }
                }
                cdata.append(DirectParams.LINE_ENDERSTR);

                /* 开始导出记录 因考虑数据量过大，需要分页处理 */
                int read_limit =  Integer.parseInt(DirectParams.readLimit);

                int cnt = 0;

                while(true){
                    params.put("start", cnt);
                    params.put("limit", read_limit);
                    String selsql_page = selsql +"limit "+cnt+" , "+ read_limit;
                    List<SqlRow> set = dataFileDao.findSql(selsql_page, params);
                    for (SqlRow sqlRow:set) {

                        procRecord++;
                        //count ++;
                        for (int i = 0; i < exseat.getFieldList().size(); i++) {
                            Object fieldval = null;
                            ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
                            if (!exfmt.getFld().equals("*")){
                                fieldval = sqlRow.get(exfmt.getFld());
                                //log.info(exfmt.getDictflag());
                                /*if("1".equals(exfmt.getDictflag()) && fieldval!=null && "ACCT_LOC_CODE".equals(exfmt.getFld())){
                                    fieldval = fieldval + " " + DirectUtils.getDictName(DirectParams.DICT_PRE+"buy_place".toLowerCase(),
                                            (String) fieldval);
                                }else if("1".equals(exfmt.getDictflag()) && fieldval!=null && "BUSI_REGU_CODE".equals(exfmt.getFld())){
                                    fieldval = fieldval + " " + DirectUtils.getDictName(DirectParams.DICT_PRE+"agent_regu_code".toLowerCase(),
                                            (String) fieldval);
                                }
                                else if("1".equals(exfmt.getDictflag()) && fieldval!=null && "IS_AGENT".equals(exfmt.getFld())){
                                    fieldval = fieldval + " " + DirectUtils.getDictName(DirectParams.DICT_PRE+"is_belong".toLowerCase(),
                                            (String) fieldval);
                                }*/
                                if(exfmt.getDictItmdic() != null && !"".equals(exfmt.getDictItmdic()) && fieldval!=null){
                                    fieldval = fieldval + " " + DirectUtils.getDictName(exfmt.getDictItmdic(),
                                            (String) fieldval);
                                }


                            }
                            fieldval = fieldval==null?"":fieldval;
                            /*if ("REGISTER_SERNO".equals(exfmt.getFld()) && fieldval.toString().length() > 2) {
                                fieldval = fieldval.toString().substring(2);
                            }*/
                            if (exfmt.getDictItmtp() != null && exfmt.getDictItmtp().equals(DirectParams.FLDTYPE_NUMBER)) {
                                if (fieldval == null || fieldval.toString().trim().equals("")) {
                                    fieldval = "0";
                                } else {
                                    BigDecimal bd = new BigDecimal(fieldval.toString());
                                    fieldval = bd.toPlainString();
                                    //log.info(fieldval+"=============");
                                }
                            }
                            if(i+1 == exseat.getFieldList().size()){
                                cdata.append(fieldval);
                            }else{
                                cdata.append(fieldval + DirectParams.LOADFILE_SPLITER);
                            }
                        }
                        cdata.append(DirectParams.LINE_ENDERSTR);
                    }
                    cnt += read_limit;
                    if(div_limit_p <= cnt){//读取完毕，退出循环
                        break;
                    }
                }
                //System.out.println("========cdata========="+cdata.capacity());
                Document doc = CreateXBRL(DirectParams.workDate, DirectParams.bankCode,
                        exseat, cdata.toString());
                writer.write(doc);
            }

            return procRecord;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("导出数据文件[" + exseat.getFilename() + "]失败:" + ex.getMessage() + procRecord);
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 创建xbrl文件
     * @param workdate
     * @param bank_code
     * @param exseat
     * @param cdata
     * @return
     */
    private Document CreateXBRL(String workdate, String bank_code, ExSeat exseat,String cdata){

        String doc_id = "C-D-"+DirectParams.preWorkDate+"-"+workdate;

        Document doc = DocumentHelper.createDocument();
        Element xbrl = doc.addElement("xbrli:xbrl");

        xbrl.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        xbrl.addAttribute("xsi:schemaLocation", "http://xbrl.org/2006/xbrldi http://www.xbrl.org/2006/xbrldi-2006.xsd");
        xbrl.addNamespace("xbrldi", "http://xbrl.org/2006/xbrldi");
        xbrl.addNamespace("chinawealth_entry_point", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth/chinawealth_entry_point");
        xbrl.addNamespace("link", "http://www.xbrl.org/2003/linkbase");
        xbrl.addNamespace("cas", "http://xbrl.mof.gov.cn/taxonomy/2015-03-31/cas");
        xbrl.addNamespace("num", "http://www.xbrl.org/dtr/type/numeric");
        xbrl.addNamespace("nonnum", "http://www.xbrl.org/dtr/type/non-numeric");
        xbrl.addNamespace("xbrldt", "http://xbrl.org/2005/xbrldt");
        xbrl.addNamespace("ifrs-full", "http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full");
        xbrl.addNamespace("rol_chinawealth_2016-04-30", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth/rol_chinawealth_2016-04-30");
        xbrl.addNamespace("wemax", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/cas/chinawealth");
        xbrl.addNamespace("wei", "http://www.chinawealth.com.cn/wei/2016-04-30");
        xbrl.addNamespace("xbrli", "http://www.xbrl.org/2003/instance");
        xbrl.addNamespace("iso4217", "http://www.xbrl.org/2003/iso4217");
        xbrl.addNamespace("xlink", "http://www.w3.org/1999/xlink");

        Element schemaRef = xbrl.addElement("link:schemaRef");
        schemaRef.addAttribute("xlink:type", "simple");
        schemaRef.addAttribute("xlink:href", "http://www.chinawealth.com.cn/taxonomy/2016-04-30/chinawealth/chinawealth_entry_point_2016-04-30.xsd");

        Element context = xbrl.addElement("xbrli:context");
        context.addAttribute("id", doc_id);
        Element entity = context.addElement("xbrli:entity");
        Element identifier = entity.addElement("xbrli:identifier");
        identifier.addAttribute("scheme", "http://www.pbc.gov.cn/").addText(bank_code);
        Element period = context.addElement("xbrli:period");
        Element startDate = period.addElement("xbrli:startDate");
        startDate.addText(DirectUtils.format(DirectParams.preWorkDate, "YYYY-MM-DD"));
        Element endDate = period.addElement("xbrli:endDate");
        endDate.addText(DirectUtils.format(workdate, "YYYY-MM-DD"));

        Element info = null;

        if("201".equals(exseat.getId().getExtpid())){
            info = xbrl.addElement("wemax:InvestorsInformationRegistrationCSVDataExplanatory");
            info.addAttribute("contextRef", doc_id);
            info.addCDATA(cdata);
        }else if ("202".equals(exseat.getId().getExtpid())) {
            info = xbrl.addElement("wemax:InvestorsInvestedInformationRegistrationCsvDataExplanatory");
            info.addAttribute("contextRef", doc_id);
            info.addCDATA(cdata);
        }else if ("203".equals(exseat.getId().getExtpid())) {
            info = xbrl.addElement("wemax:InvestorsBusinessDetailsInformationRegistrationCsvDataExplanatory");
            info.addAttribute("contextRef", doc_id);
            info.addCDATA(cdata);
        }
        return doc;
    }



















}
