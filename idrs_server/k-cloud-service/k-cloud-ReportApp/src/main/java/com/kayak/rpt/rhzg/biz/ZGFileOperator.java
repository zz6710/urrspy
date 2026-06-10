package com.kayak.rpt.rhzg.biz;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.rhzj.util.MyZipCompressing;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ZGFileOperator {

    private static final Logger log = LoggerFactory.getLogger(ZGFileOperator.class);
    private final String SPLIT = "|";

    @Autowired
    private ComnDao comnDao;



    /**
     * 根据portCode生成数据文件与数据校验文件
     * @param params 查询条件
     * @param fileName 文件名(带路径)
     * @param shortFileName 文件名（不带路径）
     * @param portCode   接口代码
     * @throws Exception
     */
    public void creatFile(Map<String, Object> params, String fileName, String shortFileName, String portCode) throws Exception{
        log.info("生成文件【{}】开始" , fileName);
        StringBuilder sb = new StringBuilder();
        PrintStream p = new PrintStream(new FileOutputStream(fileName), false, "UTF-8");
        //文件行数
        Integer num = 0;

        try {
            //获取文件的字段排列顺序
            List<SqlRow> fieldOrder = getFieldOrder(portCode);
            //获取对应的查询sql
            String sql = getSql(fieldOrder, portCode);
            //查询对应的文件内容
            List<SqlRow> rows = comnDao.findRows(sql, params);
            num = rows.size();
            //打印到文件中
            printRows(sb,p,fieldOrder,rows);
            p.close();
        } catch (Exception e) {
            log.error("生成文件失败：{}" , fileName);
            throw new Exception("生成文件失败",e);
        } finally {
            if(p != null){
                try {
                    p.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("生成文件【{}】结束" , fileName);

        String logFileName = fileName.replace(".dat",".log");
        log.info("生成文件【{}】开始" , logFileName);
        PrintStream logP = new PrintStream(new FileOutputStream(logFileName), false, "UTF-8");
        FileInputStream stream = new FileInputStream(fileName);
        try {
            File datFile = new File(fileName);
            //文件大小
            Long fileSize = datFile.length();
            //文件创建时间
            String creatTime = getCreateTime(datFile);
            //计算MD5
            String md5Hex = DigestUtils.md5Hex(stream);
            stream.close();

            sb.setLength(0);
            sb.append(shortFileName).append("\n")
                    .append(md5Hex).append("\n")
                    .append(fileSize).append("\n")
                    .append(creatTime).append("\n")
                    .append(num);
            logP.print(sb.toString());
            logP.close();
        } catch (IOException e) {
            log.error("生成文件失败：{}" , logFileName);
            throw new Exception("生成文件失败",e);
        } finally {
            if(logP != null){
                try {
                    logP.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(stream != null){
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("生成文件【{}】结束" , logFileName);

    }


    /**
     * 按照fieldOrder字段顺序打印资管产品基本信息文件行数据
     * @param sb
     * @param p
     * @param fieldOrder 字段顺序
     * @param rows
     */
    public void printRows(StringBuilder sb, PrintStream p, List<SqlRow> fieldOrder, List<SqlRow> rows){
        for (int i = 0; i < rows.size(); i++) {
            SqlRow row = rows.get(i);
            sb.setLength(0);
            for (int j = 0; j < fieldOrder.size(); j++) {
                if((fieldOrder.size()-1 ) == j){
                    sb.append(row.getString(Objects.toString(fieldOrder.get(j).get("field_code"))));
                }else {
                    sb.append(row.getString(Objects.toString(fieldOrder.get(j).get("field_code")))).append(SPLIT);
                }
            }
            p.print(sb.toString());
            if(i != (rows.size() -1)){
                p.print("\n");
            }
        }
    }




    /**
     * 生成压缩文件，文件有两个：数据文件与数据校验文件
     *
     * @param fileName 文件名
     * @throws Exception
     */
    public void creatZipFile(String fileName) throws Exception{
        String  zipFileName = fileName.replace(".dat",".zip");
        try {

            log.info("生成压缩文件开始：{}" , zipFileName);
            List<File> fileList = new ArrayList<>();
            fileList.add(new File(fileName));
            fileList.add(new File(fileName.replace(".dat",".log")));
            MyZipCompressing.zipMutipleFiles(zipFileName, fileList);
            log.info("生成压缩文件结束：{}" , zipFileName);
        } catch (IOException e) {
            log.error("生成压缩文件【{}】失败", zipFileName,e);
            throw new Exception("生成压缩文件失败",e);
        }
    }

    /**
     * 获取文件的创建时间
     * @param file 带路径的完整名
     * @return
     */
    public String getCreateTime(File file) throws Exception {
        if(file == null){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileTime t = null;

        try {
            t = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            log.error("获取文件创建时间失败：{}" , file.getAbsoluteFile());
            throw new Exception("获取文件创建时间失败",e);
        }

        return dateFormat.format(t.toMillis());

    }

    /**
     * 根据不同的portCode拼装sql，查询表记录
     * @param fieldOrder  字段顺序
     * @return
     */
    public String getSql(List<SqlRow> fieldOrder, String portCode) {

        String sql = "";
        for (int i = 0; i < fieldOrder.size(); i++) {
            if((fieldOrder.size()-1 ) == i){
                sql += Objects.toString(fieldOrder.get(i).get("field_code"));
            }else {
                sql += fieldOrder.get(i).get("field_code") + ",";
            }
        }

        if(RHZGTableStrEnum.ZG01.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg01 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date between $S{beginDate} and $S{queryDate}) and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG02.getProtCode().equals(portCode)){
            sql=sql.replace("UNT_NAV,","FORMAT(UNT_NAV,8) UNT_NAV,");
            sql=sql.replace("UNT_NAV_CNY,","FORMAT(UNT_NAV_CNY,8) UNT_NAV_CNY,");
            sql = "SELECT " + sql + " FROM app_pbc_report_zg02 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date between $S{beginDate} and $S{queryDate}) and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG03.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg03 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date between $S{beginDate} and $S{queryDate}) and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG04.getProtCode().equals(portCode)){
            sql=sql.replace("NETVAL_PROD_END_NAV,","FORMAT(NETVAL_PROD_END_NAV,8) NETVAL_PROD_END_NAV,");
            sql=sql.replace("NETVAL_PROD_END_NAV_CNY,","FORMAT(NETVAL_PROD_END_NAV_CNY,8) NETVAL_PROD_END_NAV_CNY,");
            sql=sql.replace("NETVAL_PROD_END_ACM_NAV,","FORMAT(NETVAL_PROD_END_ACM_NAV,8) NETVAL_PROD_END_ACM_NAV,");
            sql=sql.replace("NETVAL_PROD_END_ACM_NAV_CNY,","FORMAT(NETVAL_PROD_END_ACM_NAV_CNY,8) NETVAL_PROD_END_ACM_NAV_CNY,");
            sql = "SELECT " + sql + " FROM app_pbc_report_zg04 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')  and sys_data_status = '1'  ";
        }
        if(RHZGTableStrEnum.ZG05.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg05 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG06.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg06 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG07.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg07 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG08.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg08 WHERE (prod_cd like '%$U{prodCd}%' or $S{prodCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG09.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg09 WHERE (isu_org_cd like '%$U{prodCd}%' or $S{isuOrgCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG10.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg10 WHERE (isu_org_cd like '%$U{prodCd}%' or $S{isuOrgCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG11.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg11 WHERE (isu_org_cd like '%$U{prodCd}%' or $S{isuOrgCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG12.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg12 WHERE (prod_cd like '%$U{prodCd}%' or $S{isuOrgCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.ZG13.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_pbc_report_zg13 WHERE (prod_cd like '%$U{prodCd}%' or $S{isuOrgCd} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '') and sys_data_status = '1' ";
        }
        if(RHZGTableStrEnum.InterbankDepositInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_interbank_deposit_info WHERE (deposit_acco_code like '%$U{depositAccoCode}%' or $S{depositAccoCode} = '') and (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }
        if(RHZGTableStrEnum.InterbankDepositAmountInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_interbank_deposit_amount_info WHERE (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }
        if(RHZGTableStrEnum.BondInvestInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_bond_invest_info WHERE (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }
        if(RHZGTableStrEnum.BondInvestAmountInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_bond_invest_amount_info WHERE (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }
        if(RHZGTableStrEnum.SpvInvestAmountInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_spv_invest_amount_info WHERE (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }
        if(RHZGTableStrEnum.SpvInvestInfo.getProtCode().equals(portCode)){
            sql = "SELECT " + sql + " FROM app_spv_invest_info WHERE (report_date like concat($S{reportDate},'%') or $S{reportDate} = '')";
        }

        return sql;
    }

    /**
     * 获取文件字段顺序
     * @param portCode
     * @return
     * @throws Exception
     */
    public List<SqlRow> getFieldOrder(String portCode) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("portCode",portCode);
        //获取字段顺序
        String sql = "SELECT field_code,field_seq FROM base_port_field_manage WHERE port_code = $S{portCode} order by field_seq asc";
        List<SqlRow> fieldOrder = comnDao.findRows(sql, params);
        return fieldOrder;
    }

}
