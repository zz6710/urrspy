package com.kayak.rpt.dataMerge.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.*;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.core.util.Tools;
import com.kayak.rpt.dataMerge.model.CustomerDataMergeModel;
import com.spire.ms.System.Collections.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDataMergeDao extends ComnDao {

    /**
     * 查询客户合并指令
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<CustomerDataMergeModel> getCustomerAccountMergeInfo (SqlParam<CustomerDataMergeModel> params) throws Exception {
        String sql = "select ID, CSTM_DT_F, CSTM_DT_E, CSTM_ACC_F, CSTM_ACC_T, OPT_DT, OPT_TM, OPT_USER_ID, OPT_USER_NM, MRG_STS \n" +
                "  from base_account_merge_order b" +
                " where 1 = 1 ";
        if (StringUtils.isNotBlank(params.getModel().getCstmAccF())) {
            sql = sql + " and b.CSTM_ACC_F = '" + params.getModel().getCstmAccF() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCstmAccT())) {
            sql = sql + " and b.CSTM_ACC_T = '" + params.getModel().getCstmAccT() + "' ";
        }
        sql += "order by OPT_DT desc,OPT_TM desc" ;
        return super.findRows(sql,params);
    }

    /**
     * 插入客户合并指令
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult putCustomerAccountMergeInfo (SqlParam<CustomerDataMergeModel> params) throws Exception {
        params.getModel().setOptUserId(String.valueOf(SysUtil.getSysUserParams().get("JOBNO")));
        params.getModel().setOptUserNm(String.valueOf(SysUtil.getSysUserParams().get("username")));
        String sql = "insert into base_account_merge_order(CSTM_DT_F, CSTM_DT_E, CSTM_ACC_F, CSTM_ACC_T, OPT_DT, OPT_TM, OPT_USER_ID, OPT_USER_NM, MRG_STS )  " +
                "values ($S{cstmDtF}, $S{cstmDtE}, $S{cstmAccF}, $S{cstmAccT}, date_format(sysdate(), '%Y%m%d'), date_format(sysdate(), '%H%i%s'), " +
                "        $S{optUserId}, $S{optUserNm}, '01')";/*新增数据为录入状态01*/
        return super.update(sql, params.getModel());
    }

    /**
     * 判断客户标识是否存在
     * 数量大于2时判断为都存在
     * @param params
     * @return
     * @throws Exception
     */
    public boolean judgeCustNoExists (SqlParam<CustomerDataMergeModel> params) throws Exception {
        boolean is_exists = false;
        String querySql = "select * from ods_cust_base_inf where cust_no in ('"+params.getModel().getCstmAccF()+"','"+params.getModel().getCstmAccT()+"')";
        if(super.findRows(querySql, params.getModel()).size() > 1){
            is_exists = true;
        }
        return is_exists;
    }

    /**
     * 执行客户合并指令:(先修改范围内数据，使需要合并的日期范围内数据合并状态为 0-合并前)
     * order: 1)合并前数据往操作记录表中插数 2)合并后数据往历史表中插数 3)删除历史表中合并前数据 4)将合并后数据状态重置为合并前
     * 1.修改客户交易数据
     * 2.合并母产品持有信息,同一天/同一客户/同一产品
     * 3.合并子产品持有信息,依据同母产品表
     * @param params
     * @return executeResult 0-失败 1-成功
     * @throws Exception
     */
    public boolean doCustomerAccountMerge (SqlParam<CustomerDataMergeModel> params) throws Exception {
        boolean executeResult = false;
        String op_user = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//当前操作人员

        //标记需要处理的合并前数据，便于后续查询筛选
        super.update("update app_cust_trans_info_record a set a.MRG_TYP = '2' " +
                "          where a.cust_no in ('"+params.getModel().getCstmAccF()+"','"+params.getModel().getCstmAccT()+"')" +
                "            and a.ACK_DATE >= '"+params.getModel().getCstmDtF()+"' and a.ACK_DATE <= '"+params.getModel().getCstmDtE()+"' and mrg_typ = '9' ", null);
        /**1.合并投资者明细数据:明细数据只需将合并前的客户号调整为合并后的客户号*/
        //迁移合并前数据至操作记录表
        String dealRemarkSql = "insert into app_cust_trans_info_record (BANK_CODE,TRANS_SERNO,CONTRACT_NO,FNC_TRANS_ACCT_NO,HOST_CUST_NO,CUST_NO,CUST_NAME,DEAL_NO,ACCT_NO,ACCT_BANK_NO,\n" +
                "                ACCT_BANK_NAME,ACCT_LOC_CODE,IS_AGENT,AGENT_BANK_CODE,AGENT_BANK_NAME,AGENT_REGU_CODE,PROD_CODE,BUSI_CODE,BUSI_REGU_CODE,ACK_DATE,\n" +
                "                ACK_TIME,CUR,ACK_AMT,CONVERT_RMB,NAV,ACK_VOL,FEE_AMT,CHANNEL_FLAG,INPUTUSER,REMARK,\n" +
                "                REGISTER_SERNO,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,report_date,prod_code_m,TA_ID,SUMMIT_USER,CREATE_DATE,\n" +
                "                theory_report_start_date,theory_report_end_date,MRG_TYP,ORDER_ID) " +
                "         select BANK_CODE,TRANS_SERNO,CONTRACT_NO,FNC_TRANS_ACCT_NO,HOST_CUST_NO,CUST_NO,CUST_NAME,DEAL_NO,ACCT_NO,ACCT_BANK_NO,\n" +
                "                ACCT_BANK_NAME,ACCT_LOC_CODE,IS_AGENT,AGENT_BANK_CODE,AGENT_BANK_NAME,AGENT_REGU_CODE,PROD_CODE,BUSI_CODE,BUSI_REGU_CODE,ACK_DATE,\n" +
                "                ACK_TIME,CUR,ACK_AMT,CONVERT_RMB,NAV,ACK_VOL,FEE_AMT,CHANNEL_FLAG,INPUTUSER,REMARK,\n" +
                "                REGISTER_SERNO,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,report_date,prod_code_m,TA_ID,'" + op_user + "' as SUMMIT_USER,date_format(sysdate(),'%Y%m%d') as CREATE_DATE,\n" +
                "                theory_report_start_date, theory_report_end_date, '0' as MRG_TYP, $S{id} as ORDER_ID" +
                "           from app_cust_trans_info_record " +
                "          where MRG_TYP = '2' and order_id = $S{id} " ;

        //将迁移后数据插入历史表
        String dealSql = "insert into app_cust_trans_info_record (BANK_CODE,TRANS_SERNO,CONTRACT_NO,FNC_TRANS_ACCT_NO,HOST_CUST_NO,CUST_NO,CUST_NAME,deal_No,ACCT_NO,acct_Bank_No,\n" +
                "       acct_Bank_NAME,ACCT_LOC_CODE,IS_AGENT,AGENT_BANK_CODE,AGENT_BANK_NAME,AGENT_REGU_CODE,PROD_CODE,prod_code_m,ta_id,BUSI_CODE,BUSI_REGU_CODE,ACK_DATE,\n" +
                "       ACK_TIME,CUR,ACK_AMT,CONVERT_RMB,NAV,ACK_VOL,FEE_AMT,CHANNEL_FLAG,INPUTUSER,REMARK,\n" +
                "       register_serno,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,SUMMIT_USER,create_date,theory_report_start_date,theory_report_end_date,report_date,MRG_TYP,ORDER_ID) \n" +
                "select a.BANK_CODE, a.TRANS_SERNO, a.CONTRACT_NO, b.ta_id as FNC_TRANS_ACCT_NO, b.ta_id as HOST_CUST_NO, b.CUST_NO, a.CUST_NAME, a.deal_No, a.ACCT_NO, a.acct_Bank_No, \n" +
                "       a.acct_Bank_NAME, a.ACCT_LOC_CODE, a.IS_AGENT, a.AGENT_BANK_CODE, a.AGENT_BANK_NAME, a.AGENT_REGU_CODE, a.PROD_CODE,a.prod_code_m, b.ta_id, a.BUSI_CODE, a.BUSI_REGU_CODE, a.ACK_DATE,\n" +
                "       a.ACK_TIME, a.CUR, a.ACK_AMT, a.CONVERT_RMB, a.NAV, a.ACK_VOL, a.FEE_AMT, a.CHANNEL_FLAG, a.INPUTUSER, a.REMARK,\n" +
                "       a.register_serno, a.IMP_DATE, a.REGISTER_DATE, a.REGISTER_STATUS, '" + op_user + "' as SUMMIT_USER, a.create_date, a.theory_report_start_date, a.theory_report_end_date, a.report_date, " +
                "       '1' as MRG_TYP, $S{id} as ORDER_ID \n" +
                "  from app_cust_trans_info_record a \n" +
                "  left join (select c.CUST_NO, c.ta_id/*客户同一编号*/ from ods_cust_base_inf c where c.cust_no = $S{cstmAccT}/*识别标识(到)*/) b on 1=1 \n" +
                " where ((a.CUST_NO = $S{cstmAccF}/*识别标识(从)*/ and MRG_TYP = '2') or (a.CUST_NO = $S{cstmAccT}/*识别标识(到)*/ and MRG_TYP = '2')) \n" +
                "   and a.ACK_DATE >= $S{cstmDtF} and a.ACK_DATE <= $S{cstmDtE} and order_id = $S{id} ";/*新增数据为录入状态01*/

        /**2.合并投资者持有数据(母产品):*/
        //标记需要处理的合并前数据，便于后续查询筛选
        super.update("update app_cust_vol_register_info_record a set a.MRG_TYP = '2' " +
                "          where a.cust_no in ('"+params.getModel().getCstmAccF()+"','"+params.getModel().getCstmAccT()+"')" +
                "            and a.hold_date >= '"+params.getModel().getCstmDtF()+"' and a.hold_date <= '"+params.getModel().getCstmDtE()+"' and mrg_typ = '9' ", params.getModel());
        //迁移合并前数据至操作记录表
        String holdRemarkSqlM = "insert into app_cust_vol_register_info_record (SUMMIT_USER,CREATE_DATE,BANK_CODE,PROD_CODE,CUST_NO,HOLD_DATE,CUR,HOLD_VOL,HOLD_AMT," +
                "                CONVERT_RMB,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,REGISTER_SERNO,report_date,prod_code_m,TA_ID,THEORY_REPORT_START_DATE,THEORY_REPORT_END_DATE,MRG_TYP,ORDER_ID) " +
                "         select '" + op_user + "' as SUMMIT_USER,date_format(sysdate(),'%Y%m%d') as CREATE_DATE,BANK_CODE,PROD_CODE,CUST_NO,HOLD_DATE,CUR,HOLD_VOL,HOLD_AMT," +
                "                CONVERT_RMB,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,REGISTER_SERNO,report_date,prod_code_m,TA_ID,THEORY_REPORT_START_DATE,THEORY_REPORT_END_DATE, '0' as mrg_typ, $S{id} as order_id " +
                "           from app_cust_vol_register_info_record " +
                "          where MRG_TYP = '2' and order_id = $S{id} ";

        String holdSqlM = "insert into app_cust_vol_register_info_record (SUMMIT_USER,bank_code,prod_code,prod_code_m,TA_ID,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,\n" +
                "       register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,mrg_typ,ORDER_ID) \n" +
                "select '" + op_user + "' as SUMMIT_USER,c.bank_code, c.prod_code, c.prod_code_m, d.ta_id, c.cust_no, c.hold_date, c.cur, sum(c.hold_vol) as hold_vol, sum(c.hold_amt) as hold_amt, sum(c.convert_rmb) as convert_rmb, \n" +
                "       c.hold_date as imp_date, c.hold_date as register_date, '1' as register_status, CONCAT(DATE_FORMAT(NOW(),'%y%m%d%H%i%s'),UUID_SHORT()) as register_serno, \n" +
                "       DATE_FORMAT(NOW(),'%Y%m%d') as create_date, c.hold_date as theory_report_start_date, c.hold_date as theory_report_end_date, c.hold_date as report_date, '1' as mrg_typ, $S{id} as order_id \n" +
                "  from (\n" +
                "     select a.bank_code, a.prod_code, a.prod_code_m, a.ta_id, a.cust_no, a.hold_date, a.cur, a.hold_vol, a.hold_amt, a.convert_rmb \n" +
                "       from app_cust_vol_register_info_record a \n" +
                "      where a.cust_no = $S{cstmAccT} and MRG_TYP = '2' \n" +
                "        and a.hold_date >= $S{cstmDtF} and a.hold_date <= $S{cstmDtE} and order_id = $S{id} \n" +
                "     union all \n" +
                "     select b.bank_code, b.prod_code, b.prod_code_m, b.ta_id, $S{cstmAccT} as cust_no, b.hold_date, b.cur, b.hold_vol, b.hold_amt, b.convert_rmb \n" +
                "       from app_cust_vol_register_info_record b \n" +
                "      where b.cust_no = $S{cstmAccF} and MRG_TYP = '2' \n" +
                "        and b.hold_date >= $S{cstmDtF} and b.hold_date <= $S{cstmDtE} and order_id = $S{id} \n" +
                "  ) c \n" +
                " left join (select c1.CUST_NO, c1.ta_id/*客户同一编号*/ from ods_cust_base_inf c1 where c1.cust_no = $S{cstmAccT}/*识别标识(到)*/) d on 1=1 \n" +
                "group by c.bank_code, c.prod_code, c.prod_code_m, d.ta_id, c.cust_no, c.hold_date, c.cur ";

        /**3.合并投资者持有数据(子产品)*/
        //标记需要处理的合并前数据，便于后续查询筛选
        super.update("update app_cust_vol_register_sub_info_record a set a.MRG_TYP = '2' " +
                "          where a.CUST_NO in ('"+params.getModel().getCstmAccF()+"','"+params.getModel().getCstmAccT()+"')" +
                "            and a.hold_date >= '"+params.getModel().getCstmDtF()+"' and a.hold_date <= '"+params.getModel().getCstmDtE()+"' and mrg_typ = '9' and order_id = $S{id} ", params.getModel());
        //迁移合并前数据至操作记录表
        String holdRemarkSqlS = "insert into app_cust_vol_register_sub_info_record (bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,\n" +
                "                       imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,TA_ID,SUMMIT_USER,\n" +
                "                       mrg_typ,ORDER_ID) " +
                "                select bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,\n" +
                "                       imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,TA_ID,'" + op_user + "' as SUMMIT_USER,\n" +
                "                       '0' as mrg_typ, $S{id} as order_id " +
                "                  from app_cust_vol_register_sub_info_record " +
                "                 where MRG_TYP = '2' and order_id = $S{id} ";

        String holdSqlS = "insert into app_cust_vol_register_sub_info_record (bank_code,prod_code,prod_code_m,prod_code_s,ta_id,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,\n" +
                "       imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,SUMMIT_USER,report_date,mrg_typ,ORDER_ID)\n" +
                "select c.bank_code, c.prod_code, c.prod_code_m, c.prod_code_s, d.ta_id, c.cust_no, c.hold_date, c.cur, sum(c.hold_vol) as hold_vol, sum(c.hold_amt) as hold_amt, sum(c.convert_rmb) as convert_rmb, \n" +
                "       c.hold_date as imp_date, c.hold_date as register_date, '1' as register_status, CONCAT(DATE_FORMAT(NOW(),'%y%m%d%H%i%s'),UUID_SHORT()) as register_serno, \n" +
                "       DATE_FORMAT(NOW(),'%Y%m%d') as create_date, c.hold_date as theory_report_start_date, c.hold_date as theory_report_end_date, '" + op_user + "' as SUMMIT_USER, " +
                "       c.hold_date as report_date, '1' as mrg_typ, $S{id} as order_id \n" +
                "  from (\n" +
                "     select a.bank_code, a.prod_code, a.prod_code_m, a.prod_code_s, a.ta_id, a.cust_no, a.hold_date, a.cur, a.hold_vol, a.hold_amt, a.convert_rmb \n" +
                "       from app_cust_vol_register_sub_info_record a \n" +
                "      where a.cust_no = $S{cstmAccT} and MRG_TYP = '2' \n" +
                "        and a.hold_date >= $S{cstmDtF} and a.hold_date <= $S{cstmDtE}  and order_id = $S{id} \n" +
                "     union all \n" +
                "     select b.bank_code, b.prod_code, b.prod_code_m, b.prod_code_s, b.ta_id, $S{cstmAccT} as cust_no, b.hold_date, b.cur, b.hold_vol, b.hold_amt, b.convert_rmb \n" +
                "       from app_cust_vol_register_sub_info_record b \n" +
                "      where b.cust_no = $S{cstmAccF} and MRG_TYP = '2' \n" +
                "        and b.hold_date >= $S{cstmDtF} and b.hold_date <= $S{cstmDtE}  and order_id = $S{id} \n" +
                "  ) c \n" +
                " left join (select c1.CUST_NO, c1.ta_id/*客户同一编号*/ from ods_cust_base_inf c1 where c1.cust_no = $S{cstmAccT}/*识别标识(到)*/) d on 1=1 \n " +
                "group by c.bank_code, c.prod_code, c.prod_code_m, c.prod_code_s, d.ta_id, c.cust_no, c.hold_date, c.cur";

        String sqlC = "update base_account_merge_order set mrg_sts = $S{mrgSts} where id = " + params.getModel().getId();
        try {
            super.update(dealRemarkSql, params.getModel());//往操作记录表插入合并前数据
            super.update(dealSql, params.getModel());//往操作记录表插入合并后数据

            super.update(holdRemarkSqlM, params.getModel());
            super.update(holdSqlM, params.getModel());

            super.update(holdRemarkSqlS, params.getModel());
            super.update(holdSqlS, params.getModel());

            params.getModel().setMrgSts("03");//03-合并完成
            super.update(sqlC, params.getModel());
            executeResult = true;
        } catch (Exception e1) {
            executeResult = false;
            /**更新合并记录状态*/
            params.getModel().setMrgSts("99");//99-合并异常
            super.update(sqlC, params.getModel());
            /**记录异常信息,待补充 */

        }
        return executeResult;
    }

    /**
     * 根据id删除合并指令
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteCustomerAccountMergeInfo (SqlParam<CustomerDataMergeModel> params) throws Exception {
        return super.update("delete from base_account_merge_order where id = " + params.getModel().getId(), null);
    }

    /**
     * 查询投资者历史数据表头
     * @return
     * @throws Exception
     */
    public List<String> getHeaderSqlInfo () throws Exception {
        List<String> head_arr = new ArrayList();
        String[] headStr = super.findRow(ExeQuery.queryExeId("INVHISEQ01"), null).getString("header_list").split("\\|");
        for (String head : headStr) {
            head_arr.add(head);
        }
        if(head_arr.size() < 3) {
            throw new Exception("表头配置获取异常:缺少表头信息,请检查语句INVHISEQ01！");
        }
        return head_arr;
    }

}
