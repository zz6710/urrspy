package com.kayak.dps.direct.dao;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.direct.model.ExFmt;
import com.kayak.dps.direct.model.ExFmtId;
import com.kayak.dps.direct.model.ExSeat;
import com.kayak.dps.direct.model.ExSeatId;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataFileDao extends ComnDao {


    public List getExSeat(String busiCode) throws Exception {
        List exSeats = new Vector();

        String sql = "select fcode,extpid,exmode,fnmfmt,oheader,oitmnm,oitmnmfl,oreccnt,osymbol,oflddef,extab,exfmtid,indexfile,tcode " +
                "from base_ex_seat g where extpid in (" + busiCode +") order by g.extpid ";
        List<SqlRow> sr = super.findRows(sql);

        for (SqlRow sqlRow : sr) {
            ExSeatId exSeatId = new ExSeatId(sqlRow.getString("fcode"),sqlRow.getString("tcode"),sqlRow.getString("extpid"));
            ExSeat ex_seat = new ExSeat(
                    exSeatId,
                    sqlRow.getString("exmode"),
                    sqlRow.getString("fnmfmt"),
                    sqlRow.getString("oheader"),
                    sqlRow.getString("oitmnm"),
                    sqlRow.getString("oitmnmfl"),
                    sqlRow.getString("oreccnt"),
                    sqlRow.getString("osymbol"),
                    sqlRow.getString("oflddef"),
                    sqlRow.getString("extab"),
                    sqlRow.getString("exfmtid")
            );
            ex_seat.setIndexfile(sqlRow.getString("indexfile"));
            exSeats.add(ex_seat);
        }
        return exSeats;
    }


    public List getExFmt(String exFmt) throws Exception {
        List exFmts = new Vector();

        String sql = "select g.exfmtid,g.itmnm,g.itmprc,g.itmscl,g.fld,g.fldpk,g.itmtp,g.itmdsc,g.sn,g.itmdic,g.itmup,'' dictflag,g.itmmem " +
                "from base_ex_fmt g where g.exfmtid = '" + exFmt  +
                "' order by g.exfmtid,g.sn";

        List<SqlRow> sr = super.findRows(sql);
        for (SqlRow sqlRow:sr) {
            ExFmtId exFmtId = new ExFmtId(sqlRow.getString("exfmtid"),sqlRow.getString("itmnm"));
            ExFmt ex_fmt = new ExFmt(
                    exFmtId,
                    sqlRow.getLong("itmprc"),
                    sqlRow.getLong("itmscl"),
                    sqlRow.getString("fld"),
                    sqlRow.getString("fldpk"),
                    sqlRow.getLong("sn")
            );
            ex_fmt.setDictItmtp(sqlRow.getString("itmtp"));
            ex_fmt.setDictflag(sqlRow.getString("dictflag"));
            ex_fmt.setDictItmmem(sqlRow.getString("itmmem"));
            ex_fmt.setDictItmdic(sqlRow.getString("itmdic"));
            ex_fmt.setFmtItmup(sqlRow.getString("itmup"));
            exFmts.add(ex_fmt);
        }

        return exFmts;
    }

    public List<SqlRow> findDataReportByDate(String exTab , String workDate) throws Exception {
        //已复核状态下，报送状态为0、1、2、4的数据都可生成；未复核状态下只生成报送状态为2、4的数据
        String cntsql = "select report_date from base_report_data_audit_results where table_id='"+exTab+"' and audit_date='"+workDate+"' order by report_date";
        return super.findRows(cntsql);
    }

    public List<SqlRow> findTabCount(String exTab , String workDate) throws Exception {
        //已复核状态下，报送状态为0、1、2、4的数据都可生成；未复核状态下只生成报送状态为2、4的数据
        String cntsql="";
        if("app_prod_regist_filing_info".equals(exTab) || "app_prod_issuance_regist_info".equals(exTab) || "app_initial_sub_regist_info".equals(exTab)){
            cntsql="select count(0) cont from " + exTab + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exTab + "' AND REG.theory_report_start_date = ARS.report_date where theory_report_start_date='" + workDate
                    + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status IN ('2') AND sys_data_version='1.0' ";
        }else if("app_asset_debt_register_info".equals(exTab)){
            cntsql="select count(0) cont from " + exTab + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exTab + "' AND REG.theory_report_start_date = ARS.report_date where theory_report_start_date='" + workDate
                    + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status IN ('2') AND data_change_type='0' ";
        }else{
            cntsql = "select count(0) cont from " + exTab + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exTab + "' AND REG.theory_report_start_date = ARS.report_date where theory_report_start_date='" + workDate
                    + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status IN ('2')";
        }
        return super.findRows(cntsql);
    }

    public List<SqlRow> findTabMaxId(String exTab , String workDate,String status) throws Exception {
        //已复核状态下，报送状态为0、1、2、4的数据都可生成；未复核状态下只生成报送状态为2、4的数据
        String cntsql = "select count(1) cnt,min(id) minId, max(id) maxId from " + exTab + " REG where theory_report_start_date='" + workDate
                + "' and sys_data_status='1' AND REG.register_status='"+status+"'";
        return super.findRows(cntsql);
    }

    public List<SqlRow> findFilingDocs() throws Exception {
        String filesql = "select doc_path,doc_name,ident_code from tr_filing_docs t where exists (select 1 from tr_prod_regist_filing_info g where t.register_serno=g.register_serno)";
        return super.findRows(filesql);
    }

    public List<SqlRow> findSql(String sql , Map<String, Object> params) throws Exception {
        return super.findRows(sql,params);
    }

    /**
     * 获取当前日期的报送数据
     * */
    public int getSequence() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        int cnt = 0;
        String sql = "select t.maxid from sys_sequence t  where t.tablename = 'app_zz_file' ";
        List<SqlRow> rs = super.findRows(sql, params);
        if (rs != null && rs.size() > 0) {
            cnt = rs.get(0).getInteger("maxid");
        }
        return cnt;
    }

    /**
     * 获取当前日期的报送数据（中债三期）
     * */
    public int getExSeatRecod(String code ,String workDate) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("workdate", workDate);
        int cnt = 0;
        String sql = "";
        if("201".equals(code)){
            sql = "select count(1) cnt from app_cust_register_info where register_date=$S{workdate} and  register_status in ('0','2')\n" ;
        }else if ("202".equals(code)){
            sql = "select count(1) cnt from app_cust_vol_register_info where hold_date=$S{workdate} and  register_status in ('0','2')";
        }else if ("203".equals(code)){
            sql = "select count(1) cnt from app_cust_trans_info where ack_date=$S{workdate} and  register_status in ('0','2')";
        }
        if (!StringUtil.isNullOrEmpty(sql)) {
            List<SqlRow> rs = super.findRows(sql, params);
            if (rs != null && rs.size() > 0) {
                cnt = rs.get(0).getInteger("cnt");
            }
        }
        return cnt;
    }


    /**
     * 获取当前日期的报送数据（中债一、二期）
     * */
    public int getUnSendData(String tableName ,String workDate) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("work_date", workDate);
        int cnt = 0;
        String sql = "select count(1) cnt from " + tableName + " where theory_report_start_date=$S{work_date} and register_status in ('0','2','4')" ;

        List<SqlRow> rs = super.findRows(sql, params);
        if (ObjectUtil.isNotEmpty(rs)) {
            cnt = rs.get(0).getInteger("cnt");
        }
        return cnt;
    }




    /**
     * 插入文件信息
     * @param zipFileName
     * @throws Exception
     */
    public void insertFileInfoEx(String filetype,String msgtype, String workdate,String report_date, String zipFileName,String batch_no) throws Exception{

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("workdate", workdate);
        params.put("filetype", filetype);
        params.put("msgtype", msgtype); //报文类型
        params.put("status", "5");
        params.put("origfilename", zipFileName); //文件名
        params.put("report_date", report_date);//报送日期
        params.put("batch_no", batch_no);//报送日期
        super.update( "insert into app_zz_file (workdate, filetype, msgtype, origfilename, status,report_date,batch_no)" +
                " values ($S{workdate},$S{filetype},$S{msgtype}, $S{origfilename}, $S{status} ,$S{report_date},$S{batch_no})", params);
    }

    /**
     * 插入文件汇总信息
     * @param filetype
     * @param msgtype
     * @param workdate
     * @param report_date
     * @param batch_no
     * @throws Exception
     */
    public void insertFileInfoSum(String filetype,String msgtype, String workdate,String report_date, String batch_no) throws Exception{

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("workdate", workdate);
        params.put("filetype", filetype);
        params.put("msgtype", msgtype); //报文类型
        params.put("status", "0");
        params.put("batch_no", batch_no);
        params.put("report_date", report_date);//报送日期
        super.update( "insert into app_zz_file_sum (workdate, filetype, msgtype, status,report_date,batch_no) " +
                " values ($S{workdate},$S{filetype},$S{msgtype},$S{status} ,$S{report_date},$S{batch_no})", params);
    }



    /**
     * 删除文件信息
     * @param zipFileName
     * @param filetype
     */
    public void deleteFileInfoEx(String workdate,String msgtype, String filetype, String zipFileName) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("workdate", workdate);
        params.put("filetype", filetype);
        params.put("msgtype", msgtype);
        params.put("origfilename", zipFileName);
        String sql = "delete from app_zz_file t  where t.workdate=$S{workdate} and t.filetype = $S{filetype} \n" +
                "  and t.msgtype = $S{msgtype}  and t.origfilename = $S{origfilename}";
        super.update(sql, params);
    }



    /**
     * 获取当前日期的报送数据
     * */
    public void updaetSequence(int seq) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("maxid", seq);
        super.update("update sys_sequence set maxid = $S{maxid} where tablename = 'app_zz_file'", params);
    }




    /**
     * 按指定exfmtid读取包格式 字段配置列表 select * from base_ex_fmt where exfmtid='ZZ_201'
     *
     * @param exfmtid
     * @return
     * @throws Exception
     */
    public List readFieldList(String exfmtid) throws Exception {
        List listrst = getExFmt(exfmtid);
        if (listrst == null || listrst.size() <= 0)
            throw new Exception("未配置指定的[" + exfmtid + "]数据包格式参数!");
        return listrst;
    }

     /**
     * 插入中债登记编码申请记录
     * @param dataParams
     * @return
     * @throws Exception
     */
     public void updateCodeApplyHistory(Map<String, Object> dataParams) throws Exception {
         super.update( "insert into zz_code_apply_history (PROD_REG_ENC, INNER_CODE, PROD_NM, DIRECT_ZIP_DIR, DIRECT_ZIP_NM,CHEK_RESULT,CHEK_OPINION,SERIAL_NO,CRT_TIME) " +
                 " values ($S{PROD_REG_ENC},$S{INNER_CODE},$S{PROD_NM}, $S{DIRECT_ZIP_DIR}, $S{DIRECT_ZIP_NM}, $S{CHECK_RESULT},$S{CHECK_OPINION},$S{SERIAL_NO}, DATE_FORMAT(NOW(), '%Y%m%d %H:%i:%s'))", dataParams);
     }

    /**
     * 更新产品状态
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdState(Map<String, Object> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status} where prod_code=$S{prod_code} ", dataParams);
    }

    /**
     * 更新产品状态
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdStateByFl(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status} where mother_fund_code=(select ident_code from app_prod_regist_filing_info where register_serno=$S{register_serno} ) ", dataParams);
    }

    /**
     * 更新产品信息
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdInfoByFl(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set data_flag_old=data_flag,data_flag=trim(both ',' from replace(concat(data_flag,','),'0,','')) " +
                " where upd_dt = date_format(now(),'%Y%m%d') " +
                " and exists (select 1 from app_prod_regist_filing_info t1 where t1.ident_code = ods_prod_base_info.prod_code " +
                " and register_status != '3' and report_date  = date_format(now(),'%Y%m%d')) ", dataParams);
    }

    /**
     * 更新产品状态
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdStateByIs(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status} where check_inon=(select prod_code from app_prod_issuance_regist_info where register_serno=$S{register_serno} ) ", dataParams);
    }

    /**
     * 更新产品信息
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdInfoByIs(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set data_flag_old=data_flag,data_flag=trim(both ',' from replace(concat(data_flag,','),'1,','')) " +
                " where upd_dt = date_format(now(),'%Y%m%d') " +
                " and exists (select 1 from app_prod_issuance_regist_info t1 where t1.prod_ident_code = ods_prod_base_info.prod_code " +
                " and register_status != '3' and report_date  = date_format(now(),'%Y%m%d')) ", dataParams);
    }

    /**
     * 更新产品状态
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdStateByIn(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status} where check_inon=(select prod_code from app_initial_sub_regist_info  where register_serno=$S{register_serno} ) ", dataParams);
    }

    /**
     * 更新产品信息
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdInfoByIn(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set data_flag_old=data_flag,data_flag=trim(both ',' from replace(concat(data_flag,','),'2,','')) " +
                " where upd_dt = date_format(now(),'%Y%m%d') " +
                " and exists (select 1 from app_initial_sub_regist_info t1 where t1.prod_code = ods_prod_base_info.check_inon " +
                " and register_status != '3' and report_date  = date_format(now(),'%Y%m%d')) ", dataParams);
    }

    /**
     * 更新产品状态
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdStateByTr(Map<String, String> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status} where check_inon=(select prod_code from app_termination_regist_info where register_serno=$S{register_serno} ) ", dataParams);
    }

    /**
     * 更新产品状态及中债登记编码
     * @param dataParams
     * @return
     * @throws Exception
     */
    public void updateProdEncInfo(Map<String, Object> dataParams) throws Exception {
        super.update( "update ods_prod_base_info set prod_status=$S{prod_status},check_inon=$S{reg_code} where mother_fund_code=$S{prod_code} ", dataParams);
    }

    /**
     * 更新数据状态为报送文件已生成
     * @return
     * @throws Exception
     */
    public void updateDataFileStatus(String extab,String report_date, Long start, Long end) throws Exception {
        String sql = "update "+ extab + " REG " +
                "  set REG.register_status = '5'  where theory_report_start_date='" + report_date
                + "' and sys_data_status='1' AND REG.register_status ='2' "
                + " and id >= " + start + " and id < " + end + " " ;

        super.update( sql, null);
    }

    /**
     * 检查交易登记中资产负债信息是否报送成功
     * @param report_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> dealAssetTradeCheck(String report_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("report_date",report_date);
        String sql="select distinct k.asset_code from app_prod_trans_regist_info k where k.theory_report_start_date=$S{report_date} and k.asset_code is not null and k.asset_code<>'' and k.sys_data_status='1' and k.register_status='2' " +
                "and not exists(select 1 from app_asset_debt_register_info where sys_data_status='1' and register_status='3' and asset_code=k.asset_code)";
        return super.findRows(sql,params);
    }

    /**
     * 检查持仓登记直投中资产负债信息是否报送成功
     * @param report_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> dealAssetHoldCheck(String report_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("report_date",report_date);
        String sql="select distinct k.asset_code from app_asset_regist_info k where k.theory_report_start_date=$S{report_date} and holding_type in('02','03') and mezzanine_number='0' and k.asset_code is not null and k.asset_code<>'' and k.sys_data_status='1' and k.register_status='2' " +
                "and not exists(select 1 from app_asset_debt_register_info where sys_data_status='1' and register_status='3' and asset_code=k.asset_code)";
        return super.findRows(sql,params);
    }

    /**
     * 检查持仓登记中底层资产负债信息是否报送成功
     * @param report_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> dealAssetHoldCheckB(String report_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("report_date",report_date);
        String sql="select distinct k.asset_code from app_asset_regist_info k where k.theory_report_start_date=$S{report_date} and holding_type in('02','03') and mezzanine_number<>'0' and k.asset_code is not null and k.asset_code<>'' and k.sys_data_status='1' and k.register_status='2' " +
                "and not exists(select 1 from app_asset_debt_register_info where sys_data_status='1' and register_status='3' and asset_code=k.asset_code)";
        return super.findRows(sql,params);
    }

    /**
     * 检查底层资产持仓登记中委外资产负债信息是否报送成功
     * @param report_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> dealAssetBTMCheck(String report_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("report_date",report_date);
        String sql="select distinct k.asset_manager_code as asset_code from app_under_asset_regist_info k where k.theory_report_start_date=$S{report_date} and k.asset_manager_code is not null and k.asset_manager_code<>'' and k.sys_data_status='1' and k.register_status='2' " +
                "and not exists(select 1 from app_asset_debt_register_info where sys_data_status='1' and register_status='3' and asset_code=k.asset_manager_code)";
        return super.findRows(sql,params);
    }

    /**
     * 检查底层资产持仓登记中底层资产负债信息是否报送成功
     * @param report_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> dealAssetBTMCheckB(String report_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("report_date",report_date);
        String sql="select distinct k.under_asset_code as asset_code from app_under_asset_regist_info k where k.theory_report_start_date=$S{report_date} and k.under_asset_code is not null and k.under_asset_code<>'' and k.sys_data_status='1' and k.register_status='2' " +
                "and not exists(select 1 from app_asset_debt_register_info where sys_data_status='1' and register_status='3' and asset_code=k.under_asset_code)";
        return super.findRows(sql,params);
    }

}
