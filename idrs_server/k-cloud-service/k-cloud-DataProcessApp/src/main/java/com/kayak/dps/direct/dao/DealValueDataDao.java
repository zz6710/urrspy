package com.kayak.dps.direct.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DealValueDataDao extends ComnDao {

    //日期内估值系统托送过来的产品
    public List<SqlRow> findProdInfo(String workDate) throws Exception {

        String sql = "select DISTINCT vc_cpdm prod_code from ods_fa_gzb where D_YWRQ = '" + workDate + "' order by vc_cpdm";

        return super.findRows(sql);

    }

    //查询产品下的估值数据
    public List<SqlRow> findValueDataByProdCode(String workDate,String prodCode) throws Exception {

        String sql = "select t1.prod_cd,    -- 产品代码\n" +
                "t1.pos_dt,            -- 持仓日期\n" +
                "t1.scr_id ,  -- 证券编号截取\n" +
                "t1.scr_cd ,          -- 证券代码\n" +
                "t1.scr_nm ,          -- 证券名称\n" +
                "t1.bred_cd  ,          -- 品种\n" +
                "t1.trx_mkt_cd  ,         -- 市场代码\n" +
                "t2.inv_ctg_cd ,       -- 投资分类\n" +
                "t1.ccy_cd  ,\t\t    -- 币种\n" +
                "(case when t2.bred_cd = 1  -- 如果为债券 ，需要*100\n" +
                "   then sum(case when t2.acct_prj_cd in('1','2','3') then t1.l_sl else 0 end)*100\n" +
                "   else sum(case when t2.acct_prj_cd in('1','2','3') then t1.l_sl else 0 end) end) pos_qty,       -- 持仓数量\n" +
                "   sum(case when t2.acct_prj_cd in('1','2','3') then t1.en_cb else 0 end) prcp_bal,               -- 本金余额\n" +
                "   sum(case when t2.acct_prj_cd in('4') then t1.en_cb else 0 end) amrz_cst_bal,                   -- 利息调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('6') then t1.en_cb else 0 end) amrz_cst_bal,                   -- 利息调整调整余额  \n" +
                "   sum(case when t2.acct_prj_cd in('1','2','3') then t1.en_gzzz else 0 end) fair_val_bal,         -- 公允价值调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('7') then t1.en_cb else 0 end) dval_rdy,                       -- 减值准备调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('10') then t1.en_cb else 0 end) pay_fee_bal,                   -- 应付费用调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('11') then t1.en_cb else 0 end) unpay_tax_bal,                 -- 未缴税费调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('12') then t1.en_cb else 0 end) pay_tax_bal,                   -- 应缴税费调整余额\n" +
                "   sum(case when t2.acct_prj_cd in('1','2','3') then t1.en_hqjz else 0 end) valamount,            -- 行情\n" +
                "\t sum(case when t2.acct_prj_cd in('1','2','3') then t1.en_cb else 0 end)  stl_amt                -- 结算金额\n" +
                "from (select t.vc_cpdm prod_cd,    -- 产品代码\n" +
                "t.d_ywrq pos_dt,            -- 持仓日期\n" +
                "t.vc_kmdm scr_id,  -- 证券编号截取\n" +
                "substring(t.vc_kmdm ,1 , length(t.vc_kmdm) - instr(reverse(t.vc_kmdm),'.')) account_code,\n" +
                "substring_index(t.vc_kmdm,'.',-1) scr_cd,          -- 证券代码最后一个小数点以后的数据\n" +
                "t.vc_kmmc scr_nm,          -- 证券名称\n" +
                "t.l_zqlb  bred_cd,          -- 品种\n" +
                "t.vc_scdm trx_mkt_cd ,         -- 市场代码\n" +
                "t.vc_jsbz  ccy_cd,\t\t    -- 币种\n" +
                "t.l_sl,\n" +
                "t.en_cb,\n" +
                "en_gzzz,\n" +
                "en_hqjz\n" +
                "from ods_fa_gzb t\n" +
                "where t.deal_date= '"+workDate+"'\n" +
                " and t.vc_cpdm='"+prodCode+"'\n" +
                " and t.l_leaf='1') t1 left join dwd_ast_item_inf t2 on t1.account_code = t2.itm_cd\n" +
                "group by t1.prod_cd,t1.pos_dt,t1.ccy_cd,t1.bred_cd,t1.scr_id,t1.scr_cd,t1.scr_nm,t2.inv_ctg_cd,t1.trx_mkt_cd,t2.fnd_dir";

        return super.findRows(sql);

    }




    //插入今日数据信息
    public void insertValueData (List<Map<String, Object>> list , String workDate) throws Exception {

        String crt_dt = DateUtil.getNowDate();

        doTrans(() -> {

            for (Map<String ,Object> map : list) {

                map.put("crt_dt",crt_dt);

                String deSqlVal = " delete from dwd_ast_prd_ast_lbl_pos_dtl where pos_dt = $S{workDate}";

                String inSqlVal = "insert into dwd_ast_prd_ast_lbl_pos_dtl (prod_cd,pos_dt,scr_id,scr_cd,scr_nm,bred_cd,trx_mkt_cd,inv_ctg_cd,ccy_cd,pos_qty,prcp_bal,fair_val_bal,amrz_cst_bal,dval_rdy,acr_intr_bal,pay_fee_bal,unpay_tax_bal,pay_tax_bal,stl_amt,crt_dt,upd_dt) values " +
                        " ($S{prod_cd},$S{pos_dt},$S{scr_id},$S{scr_cd},$S{scr_nm},$S{bred_cd},$S{trx_mkt_cd},$S{inv_ctg_cd},$S{ccy_cd},$S{pos_qty},$S{prcp_bal},$S{fair_val_bal},$S{amrz_cst_bal},$S{dval_rdy},$S{acr_intr_bal},$S{pay_fee_bal},$S{unpay_tax_bal},$S{pay_tax_bal},$S{stl_amt},$S{crt_dt},$S{crt_dt}) ";

                super.update(deSqlVal,workDate);

                super.update(inSqlVal,map);

            }

        });

    }


    public void addSPVNetInfo(String workDate) throws Exception {

        //资管计划
        String sqlMNG = "slelect t1.* from dwd_ast_prd_ast_lbl_pos_dtl t1 inner join DWD_AST_AST_MNG_PLAN_INF t2 where t1.pos_dt = $S{workDate}";

        //非标
        String sqlNSTD = "slelect t1.* from dwd_ast_prd_ast_lbl_pos_dtl t1 inner join DWD_AST_NSTD_AST_INF t2 where t1.pos_dt = $S{workDate}";

        List<SqlRow> rows = super.findRows(sqlMNG);
        rows.addAll(super.findRows(sqlNSTD));

        String crt_dt = DateUtil.getNowDate();
        doTrans(() -> {
            for (SqlRow row : rows) {

                row.put("crt_dt",crt_dt);

                String deSqlNet = "delete from dwd_ast_mng_plan_val_inf where val_dt = $S{workDate}";

                String inSqlNet = "insert into dwd_ast_mng_plan_val_inf (scr_id,scr_cd,val_dt,unt_val,crt_dt,upd_dt) " +
                        " values ($S{scr_id},$S{scr_cd},$S{pos_dt},$S{valamount},$S{crt_dt},$S{crt_dt})";

                super.update(deSqlNet,workDate);

                super.update(inSqlNet,rows);
            }
        });


    }




}
