package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.basePublish.model.DpbReportInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @BelongsProject: idrs3
 * @BelongsPackage: com.kayak.pms.basePublish.dao
 * @Author: wangchenglin
 * @CreateTime: 2023/02/15  22:21
 * @Description:
 * @Version: 1.0
 */
@Repository
public class DpbReportInfoDao extends ComnDao {

    //基础数据补录查询
    public SqlResult<DpbReportInfo> findSubmitRemind(SqlParam<DpbReportInfo> params) throws Exception {
        String sql = "select  page,holding_date,count(*) data_num\n" +
                "from(\n" +
                "/*债券信息补录*/\n" +
                "select distinct\n" +
                " '1'/*债券信息补录*/ page,wc.POS_DT as holding_date,wc.SCR_ID \n" +
                "from\n" +
                "ods_supply_bond_bas_inf mb\n" +
                "join dwd_ast_bnd_bas_inf da on da.SCR_ID = mb.SCR_ID\n" +
                "left join dws_ast_prd_ast_lbl_weigh_cost wc on wc.SCR_ID = mb.SCR_ID\n" +
                "where wc.BRED_CD ='4' and wc.POS_DT = '"+params.getModel().getHoldingDate()+"' and mb.version = '0' \n" +
                "union all\n" +
                "/*基金信息补录*/\n" +
                "select distinct\n" +
                " '2'/*基金信息补录*/ page,wc.POS_DT as holding_date,wc.SCR_ID \n" +
                "from ods_supply_fund_bas_inf fb\n" +
                "join ods_ast_fnd_bas_inf ma on ma.SCR_ID = fb.SCR_ID\n" +
                "left join dws_ast_prd_ast_lbl_weigh_cost wc on wc.SCR_ID = fb.SCR_ID\n" +
                "where wc.BRED_CD in ('8','9') and wc.POS_DT = '"+params.getModel().getHoldingDate()+"' and fb.version = '0'\n" +
                "union all\n" +
                "/*股票信息补录*/\n" +
                "select distinct\n" +
                " '3'/*股票信息补录*/ page,wc.POS_DT as holding_date,wc.SCR_ID \n" +
                "from\n" +
                "ods_supply_asharedescription t\n" +
                "left join dws_ast_prd_ast_lbl_weigh_cost wc on wc.SCR_ID = t.SCR_ID\n" +
                "where wc.BRED_CD ='6' and wc.POS_DT = '"+params.getModel().getHoldingDate()+"' and t.version = '0'\n" +
                "union all\n" +
                "/*非标债券信息补录*/\n" +
                "select distinct\n" +
                "'4'/*非标债券信息补录*/ page,wc.POS_DT as holding_date,wc.SCR_ID \n" +
                "from\n" +
                "ods_supply_nstd_ast_inf fb\n" +
                "join ods_ast_nstd_ast_inf ma  on ma.SCR_ID = fb.SCR_ID\n" +
                "left join dws_ast_prd_ast_lbl_weigh_cost wc on wc.SCR_ID = fb.SCR_ID\n" +
                "where wc.BRED_CD in ('2','5') and wc.POS_DT = '"+params.getModel().getHoldingDate()+"'  and fb.version = '0'\n" +
                "union all\n" +
                "/*资产管理产品补录*/\n" +
                "select distinct\n" +
                " '5'/*资产管理产品补录*/ page,wc.POS_DT as holding_date,wc.SCR_ID \n" +
                "from\n" +
                "ods_supply_ast_mng_plan_inf t5 \n" +
                "join ods_ast_ast_mng_plan_inf ma on ma.SCR_ID = t5.SCR_ID "+
                "left join dws_ast_prd_ast_lbl_weigh_cost wc on wc.SCR_ID = t5.SCR_ID\n" +
                "where wc.BRED_CD ='10' and wc.POS_DT = '"+params.getModel().getHoldingDate()+"' and t5.version = '0'\n" +
                "union all\n" +
                "/*交易对手*/\n" +
                "select distinct\n" +
                "\t '7'/*交易对手信息补录*/ page,t.POS_DT as holding_date,t.SCR_ID \n" +
                "from\n" +
                "\tdwd_ast_prd_ast_lbl_pos_dtl t\n" +
                "left join dwd_evt_prd_inv_trx_dtl t1 on concat(t.SCR_CD, '.', t.TRX_MKT_CD, '.', t.BRED_CD) = t1.SCR_ID=t1.SCR_ID  and t1.BUS_TYP_CD in ('12') \n" +
                "where t.BRED_CD in ('3','12') and t.POS_DT = '"+params.getModel().getHoldingDate()+"')a where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getReportCatgory()))
            sql += " and a.page = $S{reportCatgory}";
        sql += " group by page,holding_date";
        return super.findRows(sql,params);
    }

    //指标校验提醒查询
    public SqlResult<DpbReportInfo> findIndicatorCheckRemind(SqlParam<DpbReportInfo> params) throws Exception {
        String sql = "select re.table_name,bd.validate_table,bd.validate_result,bd.create_date,bd.deal_date,re.report_catgory,count(*) as data_num " +
                " from base_data_validation bd left join base_report_info re on re.report_table =bd.validate_table " +
                " where 1=1";
        if (StringUtils.isNotBlank(params.getModel().getReportCatgory()))
            sql += " and re.report_catgory = $S{reportCatgory}";
        if (StringUtils.isNotBlank(params.getModel().getValidateResult())) {
            sql += " and bd.validate_result = $S{validateResult}";
        }
        if (StringUtils.isNotBlank(params.getModel().getCreateDate())) {
            sql += " and bd.create_date = $S{createDate}";
        }
        sql += " group by re.table_name,bd.validate_table,bd.validate_result,bd.create_date,bd.deal_date,re.report_catgory order by bd.validate_result";
       return super.findRows(sql,params);
    }
}
