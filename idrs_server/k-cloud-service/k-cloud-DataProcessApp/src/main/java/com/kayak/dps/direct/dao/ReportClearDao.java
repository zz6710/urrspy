package com.kayak.dps.direct.dao;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.direct.util.DirectParams;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ReportClearDao extends ComnDao {

    /**
     * 查询文件信息
     * @param busiCode
     * @param workdate
     * @param status
     * @throws Exception
     */
    public List<SqlRow> selectFileInfoEx( String busiCode,String workdate, String status) throws Exception{

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("busicode", busiCode);
        params.put("workdate", workdate);
        params.put("status", status);
        String sql = "select workdate, filetype, systype, msgtype, fileid, filename, origfilename, \n" +
                "\t\t\t\t\tsuccesscount, failedcount, totalcount, errorcode, errortext, status \n" +
                "\t\t\t  from app_zz_file \n" +
                "\t\t     where workdate=$S{workdate} and status in ($U{status}) and \n" +
                "\t\t     filetype = $S{busicode} \n" +
                "\t\t     order by workdate,filetype,origfilename";

        return super.findRows(sql, params);

    }


    //数据归档
    public void dataArchiving(String busiCode , String workDate) throws Exception{
        String sql = "select fcode,extpid,exmode,fnmfmt,oheader,oitmnm,oitmnmfl,oreccnt,osymbol,oflddef,extab,exfmtid,indexfile,tcode " +
                "from base_ex_seat g where extpid = '" + busiCode +"' order by g.extpid ";
        List<SqlRow> sr = super.findRows(sql);
        if (sr == null || sr.size() <= 0){
            throw new Exception("归档"+ busiCode +"时，未取到相关信息");
        }
        String tabName = sr.get(0).getString("extab");
        super.doTrans(() -> {
            String sqlt = "delete from " + tabName + "_h where theory_report_start_date=$S{workDate} ";
            super.update(sqlt, workDate);
            sqlt = "insert into " + tabName + "_h select * from " + tabName + " t where t.theory_report_start_date<=$S{workDate} and t.register_status ='1' ";
            super.update(sqlt, workDate);
            sqlt = "delete from " + tabName + " t where theory_report_start_date<=$S{workDate} and t.register_status ='1' ";
            super.update(sqlt, workDate);

            //数据归档后需要清空错误信息记录表数据
            sqlt = "truncate table " + tabName + "_erdesc ";
            super.update(sqlt);

            //更新文件序号为1
            sqlt = "update sys_sequence set maxid = 1 where tablename = 'app_zz_file' ";
            super.update(sqlt);
        });


    }


    /**
     * 更新文件信息状态
     * @param flag (send - 发送步骤    download - 下载步骤   )
     */
    public void updateFileStatusEx(String workdate,String msgtype, String zipFileName, String fileid, String status, String flag) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        String exeid = "";
        if ("send".equalsIgnoreCase(flag)){
            exeid = "update app_zz_file set fileid=$S{fileid},\n" +
                    "\t\t\t\t\tstatus=$S{status} where workdate=$S{workdate}\n" +
                    " and msgtype=$S{msgtype}  and origfilename = $S{origfilename}";
            params.put("msgtype", msgtype);
            params.put("origfilename", zipFileName);
        }else if("download".equalsIgnoreCase(flag)){
            exeid = "update app_zz_file set status = $S{status}\n" +
                    "\t\t\t where workdate = $S{workdate} and fileid = $S{fileid}";
        }
        params.put("workdate", workdate);
        params.put("fileid", fileid);
        params.put("status", status);
        super.update( exeid, params);
    }



    /**
     * 更新文件信息
     * @throws Exception
     */
    public void updateFileInfoEx(List<Map<String, Object>> params) throws Exception{
        params.forEach(map ->{
            try {
                super.update("update app_zz_file\n" +
                        "\t\t\t   set  systype=$S{sysType},\n" +
                        "\t\t\t\t\tfileid=$S{fileId},\n" +
                        "\t\t\t\t\tfilename=$S{fileName},\n" +
                        "\t\t\t\t\tsuccesscount=$S{successCount},\n" +
                        "\t\t\t\t\tfailedcount=$S{failedCount},\n" +
                        "\t\t\t\t\ttotalcount=$S{totalCount},\n" +
                        "\t\t\t\t\terrorcode=$S{errorCode},\n" +
                        "\t\t\t\t\terrortext=$S{errorText},\n" +
                        "\t\t\t\t\tstatus=$S{status}\n" +
                        "\t\t\t where workdate=$S{workdate}\n" +
                        "\t\t\t   and origfilename = $S{origFileName} ", map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } );
    }





    /**
     * 40逻辑处理
     * @author yangcw
     * @throws Exception
     */
    public void insertOrUpdateProd(Map<String,Object> param) throws Exception{

        super.doTrans( ()-> {
            String sql_del = "DELETE FROM app_prod_reg_relation t WHERE t.prod_code = $S{prod_code} ";
            String sql = "INSERT INTO app_prod_reg_relation (prod_code, reg_code, workdate, remark) "
                    + " values ($S{prod_code}, $S{reg_code}, $S{workDate}, $S{msg_type})" ;

            super.update(sql_del, param);
            super.update(sql, param);
        });
    }


    /**
     * 更新登记状态
     * @param msg_type
     * @throws Exception
     */
    public void updateRegisterStatus(String msg_type, String workDate) throws Exception{

        Map<String, Object> params = new HashMap<>();
        params.put("workdate", workDate);
        params.put("register_status", "1");
        if("WMRS.201.001.01".equals(msg_type) || "byhand".equalsIgnoreCase(msg_type)){
            super.update("update app_cust_register_info set register_status=$S{register_status} where register_date=$S{workdate} and register_status='0' ", params);
        }
        if("WMRS.202.001.01".equals(msg_type) || "byhand".equalsIgnoreCase(msg_type)){
            super.update("update app_cust_vol_register_info set register_status=$S{register_status} where hold_date=$S{workdate} and register_status='0'\n", params);
        }
        if("WMRS.203.001.01".equals(msg_type) || "byhand".equalsIgnoreCase(msg_type)){
            super.update("update app_cust_trans_info set register_status=$S{register_status} where ack_date=$S{workdate} and register_status='0'\n", params);
        }

    }

    /**
     * 查询报送表exfId对应表信息
     * @return
     */
    public Map<String, String> getExSeatMap() throws Exception {
        List <SqlRow> rows = super.findRows("select extpid pid, extab tab from base_ex_seat");
        Map<String, String> ret = new HashMap<>();
        for (SqlRow row : rows) {
            ret.put(row.getString("pid"), row.getString("tab"));
        }
        return ret;
    }

    /**
     * 查询报送表exfId对应表信息
     * @return
     */
    public Map<String,String> getReportTable(String isRegisterFile) throws Exception {
        Map<String, String> ret = new HashMap<>();
        List <SqlRow> rows = super.findRows("select extab tab,exfmtid from base_ex_seat where extpid='"+isRegisterFile+"'");
        for (SqlRow row : rows) {
            ret.put("report_table",row.getString("tab"));
            ret.put("exfmtid",row.getString("exfmtid"));
        }
        return ret;
    }

    /**
     * 获取反馈文件
     * @return
     */
    public int getReultFile(String filetype,String now_date) throws Exception {
        Map<String, String> ret = new HashMap<>();
        int cnt = 0;
        List<SqlRow> rows = super.findRows("select count(1) cnt from app_zz_file where workdate='"+now_date+"' and filetype= '"+filetype+"' and status in('3','5')",ret);
        for (SqlRow sqlRow : rows){
            cnt = sqlRow.getInteger("cnt");
        }
        return cnt;
    }

    /**
     * 获取反馈文件-数据错误过多
     * @return
     */
    public int isReultFileError(String filetype,String now_date,String report_date) throws Exception {
        Map<String, String> ret = new HashMap<>();
        int cnt = 0;
        List<SqlRow> rows = super.findRows("select count(1) cnt from app_zz_file where workdate='"+now_date+"' and report_date='"+report_date+"' and filetype= '"+filetype+"' and status='4' ",ret);
        for (SqlRow sqlRow : rows){
            cnt = sqlRow.getInteger("cnt");
        }
        return cnt;
    }

    /**
     * 更新数据状态
     * @return
     */
    public List<SqlRow> getReportDataDate(String filetype,String now_date) throws Exception {
        Map<String, String> ret = new HashMap<>();
        List<SqlRow> rows = super.findRows("select distinct report_date from app_zz_file where workdate='"+now_date+"' and filetype= '"+filetype+"'",ret);
        return rows;
    }

    /**
     * 更新数据状态
     * @return
     */
    public void updateDataStatus(String report_table,String now_date,String report_date,String status,Long start,Long end) throws Exception {
        String sql="";
        if("app_prod_regist_filing_info".equals(report_table) || "app_initial_sub_regist_info".equals(report_table) || "app_prod_issuance_regist_info".equals(report_table)){
           sql ="update "+report_table+" set register_date='"+now_date+"', register_status='"+status+"' where theory_report_start_date='"+report_date+"' and register_status='5' and sys_data_version='1.0' "
                    + " and id >= " + start + " and id < " + end + " " ;
        }else if("app_asset_debt_register_info".equals(report_table)){
            sql ="update "+report_table+" set register_date='"+now_date+"', register_status='"+status+"' where theory_report_start_date='"+report_date+"' and register_status='5' and data_change_type='0' "
                    + " and id >= " + start + " and id < " + end + " " ;
        }else{
           sql ="update "+report_table+" set register_date='"+now_date+"', register_status='"+status+"' where theory_report_start_date='"+report_date+"' and register_status='5' "
                    + " and id >= " + start + " and id < " + end + " " ;
        }
        super.update(sql);
    }

    /**
     * 更新数据状态
     * @return
     */
    public void updateDataStatusByTr(String real_table,String report_table,String now_date,String report_date,String status,Long start,Long end) throws Exception {

        String sql ="update "+report_table+" k left join "+real_table+"_erno k1 on k.register_serno=k1.register_serno set register_date='"+now_date+"', " +
                " register_status= (case when k1.register_serno is null then '"+status+"' else '4' end)  where theory_report_start_date='"+report_date+"' and register_status='2' "
                + " and k.id >= " + start + " and k.id < " + end + " " ;
        super.update(sql);
    }

    /**
     * 更新反馈文件状态
     * @return
     */
    public void updateFileStatus(String filetype,String now_date,String status,String file_status) throws Exception {
        String sql ="update app_zz_file set status='"+status+"' where workdate='"+now_date+"' and filetype='"+filetype+"' and status='"+file_status+"'";
        super.update(sql);
    }

    /**
     * 更新反馈文件状态
     * @return
     */
    public void updateFileStatusBy(String filetype,String now_date,String report_date,String status,String file_status) throws Exception {
        String sql ="update app_zz_file set status='"+status+"' where workdate='"+now_date+"' and report_date='"+report_date+"' and filetype='"+filetype+"' and status='"+file_status+"'";
        super.update(sql);
    }


    /**
     * 清理错误数据流水号
     * @return
     */
    public void clearReportDataNo(String report_table) throws Exception {
        String sql ="truncate table "+report_table+"_erno ";
        super.update(sql);
    }

    /**
     * 获取一批次文件数据汇总
     * @return
     */
    public List<SqlRow> getReportDataSum(String filetype, String now_date, String report_date, String batch_no) throws Exception {
        String sql ="select sum(ifnull(successcount,0)) successcount,sum(ifnull(failedcount,0)) failedcount,sum(ifnull(totalcount,0)) totalcount from app_zz_file where workdate='"+now_date+"' and filetype= '"+filetype+"' and report_date='"+report_date+"' and report_date='"+batch_no+"' ";
        return super.findRows(sql);
    }

    /**
     * 更新一批次文件数据汇总
     * @return
     */
    public void setReportDataSum(List<SqlRow> dataList,String filetype, String now_date, String report_date, String status,String batch_no) throws Exception {
        if(dataList.size()>0){
            String sql ="update app_zz_file_sum set status='"+status+"',successcount="+dataList.get(0).getBigDecimal("successcount")+",failedcount="+dataList.get(0).getBigDecimal("failedcount")+",totalcount="+dataList.get(0).getBigDecimal("totalcount")+" where " +
                    " workdate='"+now_date+"' and report_date='"+report_date+"' and filetype='"+filetype+"' and batch_no='"+batch_no+"'";
            super.update(sql);
        }
    }

    /**
     * 更新登记状态
     *
     * @param register_filetype
     *
     * @throws
     */
    public void updateExportRegisterStatus(String register_filetype , String workDate) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("workdate", workDate);
        params.put("register_status", "3");
        String[] filetypes = register_filetype.split(",");
        try {
            for (String filetype : filetypes) {
//                super.update("MP000UR" + filetype + "STATUS", params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("更新登记状态失败：" + e.getMessage());
        }

    }


    /**
     * 插入反馈文件错误信息
     * @throws Exception
     */
    public void insertResultError(Map<String,Object> param) throws Exception {
        super.doTrans(() -> {
//            String sql_del = "DELETE FROM app_zz_file_results WHERE register_date='" + param.get("workDate") + "' AND file_type = '" + param.get("fileType") + "'";
            String sql = "insert into app_zz_file_results (register_date, file_type, filename, zz_table, fileno, register_serno, errormsg, errorcode, create_time) "
                    + " values ($S{workDate}, $S{fileType}, $S{fileName}, $S{zzTable}, $S{fileNo}, $S{register_serno}, $S{errorMessage}, $S{errorCode}, $S{createTime})" ;

//            super.update(sql_del, param);
            super.update(sql, param);
        });
    }


    /**
     * 更新非重复错误数据的登记状态
     * @throws Exception
     */
    public void updateErrorRegisterStatus(Map<String,Object> param) throws Exception{
        super.doTrans(() ->{
            String sql = "";
            if("WMRS.201.001.01".equals(param.get("msg_type"))){
                sql = "update app_cust_register_info set register_status='2' where register_date = $S{workDate} and register_serno = $S{register_serno}  ";
            }
            if("WMRS.202.001.01".equals(param.get("msg_type"))){
                sql = "update app_cust_vol_register_info set register_status='2' where hold_date = $S{workDate} and register_serno = $S{register_serno}";
            }
            if("WMRS.203.001.01".equals(param.get("msg_type"))){
                sql = "update app_cust_trans_info set register_status='2' where ack_date = $S{workDate} and register_serno = $S{register_serno}";
            }

            super.update(sql, param);
        });
    }


    /**
     * 更新登记状态(中债一二期)
     * @param tableName
     * @throws Exception
     */
    public void updateStatusOne(String tableName, String workDate) throws Exception{
        Map<String, Object> params = new HashMap<>();
        params.put("work_date", workDate);
        params.put("register_status", "3");
        String sql = "update " + tableName + " set register_status=$S{register_status} where theory_report_start_date=$S{work_date} and register_status in (2,4)";
        super.update(sql, params);
}

    /**
     * 更新非重复数据错误登记状态(中债一二期)
     * @param tableName
     * @throws Exception
     */
    public void updateErrorStatusOne(String tableName, String workDate, Set<String> keys) throws Exception{
        if (ObjectUtil.isEmpty(keys)) {
            return;
        }
        //组装参数
        Map<String, Object> params = new HashMap<>();
        params.put("work_date", workDate);
        params.put("register_status", "4");

        StringBuffer keyStr = new StringBuffer();
        for (String key : keys) {
            keyStr.append("'" + key + "',");
        }
        keyStr.deleteCharAt(keyStr.length() - 1);

        String sql = "update " + tableName + " set register_status=$S{register_status} where" +
                " theory_report_start_date=$S{work_date} " +
                " and SUBSTR(register_serno,3) in (" + keyStr + ")";
        super.update(sql, params);
    }

}
