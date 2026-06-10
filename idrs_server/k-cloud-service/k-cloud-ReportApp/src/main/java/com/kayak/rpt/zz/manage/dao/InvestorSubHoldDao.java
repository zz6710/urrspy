package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.manage.model.InvestorSubHoldInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class InvestorSubHoldDao extends ComnDao {

    /**
     * 根据查询条件查询投资者持有(子产品)信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<InvestorSubHoldInfo> queryInvestorSubHoldInfoByCond (SqlParam<InvestorSubHoldInfo> params, String tbName) throws Exception {
        String sql = "select b.id, b.bank_code, b.prod_code, b.prod_code_m, b.prod_code_s, b.cust_no, b.hold_date, b.cur, b.hold_vol, b.hold_amt,\n" +
                     "       b.convert_rmb, b.imp_date, b.register_date, b.register_status, b.register_serno, b.create_date, b.theory_report_start_date, b.theory_report_end_date, b.report_date, \n" +
                     "       b.TA_ID, b.cust_type, b.channel_flag as channel_code, b.personal_id_type, b.organization_id_type, b.other_id_name, b.id_code \n" +
                     "  from " + tbName + " b \n" +
                     " where 1 = 1 ";
        if (StringUtils.isNotBlank(params.getModel().getHoldDate())) {
            sql = sql + " and b.hold_date = '" + params.getModel().getHoldDate() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getTaId())) {
            sql = sql + " and b.ta_id = '" + params.getModel().getTaId() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCodeS())) {
            sql = sql + " and b.prod_code_s = '" + params.getModel().getProdCodeS() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql = sql + " and b.prod_code = '" + params.getModel().getProdCode() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql = sql + " and b.cust_no = '" + params.getModel().getCustNo() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustType())) {
            sql = sql + " and b.cust_type in (" + SysUtil.inStr(params.getModel().getCustType()) + ") ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql = sql + " and b.cur in (" + SysUtil.inStr(params.getModel().getCur()) + ") ";
        }
        if (StringUtils.isNotBlank(params.getModel().getChannelCode())) {
            sql = sql + " and b.channel_flag = '" + params.getModel().getChannelCode() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql = sql + " and b.register_status = '" + params.getModel().getRegisterStatus() + "' ";
        }
        sql += " order by b.hold_date desc, b.cust_no, b.prod_code_s " ;
        return super.findRows(sql,params);
    }

    /**
     * 根据查询条件查询投资者持有(子产品)合并前后信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<InvestorSubHoldInfo> queryInvestorSubHoldRemark (SqlParam<InvestorSubHoldInfo> params) throws Exception {
        String sql = "select b.id, b.bank_code, b.prod_code, b.prod_code_m, b.prod_code_s, b.cust_no, b.hold_date, b.cur, b.hold_vol, b.hold_amt,\n" +
                "       b.convert_rmb, b.imp_date, b.register_date, b.register_status, b.register_serno, b.create_date, b.theory_report_start_date, b.theory_report_end_date, b.report_date, \n" +
                "       b.TA_ID, c.cust_type, c.channel_code, c.personal_id_type, c.organization_id_type, c.other_id_name, c.id_code \n" +
                "  from app_cust_vol_register_sub_info_record b \n" +
                "  left join ods_cust_base_inf c on b.cust_no = c.cust_no " +
                " where order_id = '" + params.getParamsDirect().get("order_id") + "' and MRG_TYP = '" + params.getParamsDirect().get("mrg_typ") + "' ";
        sql += " order by b.hold_date desc, b.cust_no, b.prod_code_s " ;
        return super.findRows(sql,params);
    }

    /**
     * 插入投资者持有(子产品)信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult putInvestorSubHoldInfo (SqlParam<InvestorSubHoldInfo> params) throws Exception {
        String inSql = "insert into app_cust_vol_register_sub_info (bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,\n" +
                     "       imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,TA_ID) \n" +
                     "values ($S{bankCode}, $S{prodCode}, $S{prodCodeM}, $S{prodCodeS}, $S{custNo}, $S{holdDate}, $S{cur}, $S{holdVol}, $S{holdAmt}, $S{convertRmb},\n" +
                     "       $S{impDate}, $S{registerDate}, $S{registerStatus}, $S{registerSerno}, date_format(sysdate(), '%Y%m%d'), $S{theoryReportStartDate}, $S{theoryReportEndDate}, $S{holdDate}, $S{taId} )";
        return super.update(inSql, params.getModel());
    }

    /**
     * 插入投资者持有(子产品)变更信息
     * @param param
     * @return
     * @throws Exception
     */
    public UpdateResult putModifyInvestorSubHoldInfo (Object param) throws Exception {
        String inSql = "replace into app_cust_vol_register_sub_info_modify (bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,\n" +
                "       imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,TA_ID) \n" +
                "values ($S{bankCode}, $S{prodCode}, $S{prodCodeM}, $S{prodCodeS}, $S{custNo}, $S{holdDate}, $S{cur}, $S{holdVol}, $S{holdAmt}, $S{convertRmb},\n" +
                "       $S{impDate}, $S{registerDate}, $S{registerStatus}, $S{registerSerno}, date_format(sysdate(), '%Y%m%d'), $S{theoryReportStartDate}, $S{theoryReportEndDate}, $S{reportDate}, $S{taId} )";
        return super.update(inSql, DataSourceProperty.PUB, param);
    }

    /**
     * 根据唯一标识更新投资者持有(子产品)信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateInvestorSubHoldInfo (SqlParam<InvestorSubHoldInfo> params) throws Exception {
        String updSql = "update app_cust_vol_register_sub_info b \n" +
                     "   set b.bank_code = $S{bankCode}, \n" +
                     "       b.prod_code = $S{prodCode}, \n" +
                     "       b.prod_code_m = $S{prodCodeM}, \n" +
                     "       b.prod_code_s = $S{prodCodeS}, \n" +
                     "       b.cust_no = $S{custNo}, \n" +
                     "       b.hold_date = $S{holdDate}, \n" +
                     "       b.cur = $S{cur}, \n" +
                     "       b.hold_vol = $S{holdVol}, \n" +
                     "       b.hold_amt = $S{holdAmt}, \n" +
                     "       b.convert_rmb = $S{convertRmb}, \n" +
                     "       b.imp_date = $S{impDate}, \n" +
                     "       b.register_date = $S{registerDate}, \n" +
                     "       b.report_date = $S{holdDate}, \n" +
                     "       b.ta_id = $S{taId} \n" +
                     " where b.id = $S{id} ";
        return super.update(updSql, params.getModel());
    }

    /**
     * 判断客户标识是否存在
     * 存在时返回提示信息
     * @param params
     * @return
     * @throws Exception
     */
    public boolean judgeInverstorExists (SqlParam<InvestorSubHoldInfo> params) throws Exception {
        boolean is_exists = false;
        String querySql = "select * from ods_cust_base_inf where cust_no = '" + params.getModel().getCustNo() + "'";
        if(super.findRows(querySql, params.getModel()).size() > 0){
            is_exists = true;
        }
        return is_exists;
    }

    /**
     * 根据id删除合并指令
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteInvestorSubHoldInfo (SqlParam<InvestorSubHoldInfo> params) throws Exception {
        return super.update("delete from app_cust_vol_register_sub_info where id = $S{id}", params.getModel());
    }

    public UpdateResult deleteModifyInvestorSubHoldInfo (Map<String, Object> params) throws Exception {
        return super.update("delete from app_cust_vol_register_sub_info_modify where create_date = $S{createDate}", params);
    }

}
