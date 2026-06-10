package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;


//生成运营导航  --axin 20230608

@Slf4j
@Component
@Scope("prototype")
public class OperationTaskService extends BusinessBaseTaskService{

    //生成资产占比
    @StepNo(stepNo = 1)
    protected void stepProcess1(PubReq request) throws Exception{

        log.info(" ###### 运营导航步骤1-资产占比执行开始 ");
        String findSql = "select t.prod_cd prod_code, t.scr_cd asset_code, t.scr_nm asset_name, \n" +
                "t.pos_qty position_vol, t.net_val position_amt, t.pos_dt trade_date, date_format(NOW(),'%Y%m%d') crt_date\n" +
                "from dwd_ast_prd_ast_lbl_pos_dtl t where t.pos_dt = '" + workDate + "' ";

        List<SqlRow> rows = ExeQuery.query(findSql);

        String updateSql = "insert into app_operation_asset_positions (id, prod_code, asset_code, asset_name, trade_date, position_vol, position_amt, crt_date)\n" +
                "values ($AUTOIDS{id}, $S{prod_code}, $S{asset_code}, $S{asset_name}, $S{trade_date}, $S{position_vol}, $S{position_amt}, $S{crt_date})";

        String deleteSql = "delete from app_operation_asset_positions where trade_date = '" + workDate + "'";

        comnDao.doTrans( () ->{
            comnDao.update(deleteSql);
            for (SqlRow row: rows) {
                comnDao.update(updateSql,row);
            }
        });

        log.info(" ###### 运营导航步骤1-资产占比执行结束 ");

    }

    //生成产品规模分析
    @StepNo(stepNo = 2)
    protected void stepProcess2(PubReq request) throws Exception{
        log.info(" ###### 运营导航步骤2-产品规模分析执行开始 ");

        String findSql1 = "select t1.prod_cd prod_code, t2.prod_nm prod_name, sum(t1.tot_amt) existing_scale ,t1.pos_dt trade_date,date_format(NOW(),'%Y%m%d') crt_date " +
                "from dws_ast_prd_lot_bal_dtl_amng t1\n" +
                "inner join DWD_PRD_PRD_BAS_INF t2 on t2.prod_cd = t1.prod_cd \n" +
                "where t1.pos_dt = '" + workDate + "'\n" +
                "group by t1.prod_cd, t2.prod_nm,t1.pos_dt ";


        List<SqlRow> rows1 = ExeQuery.query(findSql1);

        String findSql2 = "select t1.prod_cd prod_code, sum(t1.tot_amt) history_scale from dws_ast_prd_lot_bal_dtl_amng t1\n" +
                "where t1.pos_dt = date_format('" + workDate + "' - interval day('" + workDate + "') day,'%Y%m%d')\n" +
                "group by t1.prod_cd";

        List<SqlRow> rows2 = ExeQuery.query(findSql2);

        String updateSql = "insert into app_operation_prod_scale (id, prod_code, prod_name, prod_code_sub, trade_date, existing_scale, history_scale, crt_date)\n" +
                "values ($AUTOIDS{id}, $S{prod_code}, $S{prod_name}, $S{prod_code_sub}, $S{trade_date}, $S{existing_scale}, $S{history_scale}, $S{crt_date})";

        String deleteSql = "delete from app_operation_prod_scale where trade_date = '" + workDate + "'";

        comnDao.doTrans( () ->{
            comnDao.update(deleteSql);
            for (SqlRow row1 : rows1) {
                for (SqlRow row2 : rows2) {
                    if (row1.getString("prod_code").equals(row2.getString("prod_code"))){
                        row1.put("history_scale",row2.getString("history_scale"));
                        comnDao.update(updateSql,row1);
                    }
                }
            }
        });


        log.info(" ###### 运营导航步骤2-产品规模分析执行结束 ");

    }

    //各机构持有量
    @StepNo(stepNo = 3)
    protected void stepProcess3(PubReq request) throws Exception{
        log.info(" ###### 步骤3-各机构持有量执行开始 ");
        log.info(" ###### #### 待定，这个和军说要从TA取值 ########");
        log.info(" ###### 步骤3-各机构持有量执行结束 ");
    }

    //穿透前后债券分布
    @StepNo(stepNo = 4)
    protected void stepProcess4(PubReq request) throws Exception{
        log.info(" ###### 运营导航步骤4-穿透前后债券分布执行开始 ");
        String findSql1 = "select t1.scr_cd asset_code,sum(t1.prcp_bal) pierce_before,t1.pos_dt trade_date,\n" +
                " t2.bnd_frs_ctg asset_type,t2.eco_frs_typ industry,t2.isu_bnd_rat grade,date_format(NOW(),'%Y%m%d') crt_date\n" +
                "from dwd_ast_prd_ast_lbl_pos_dtl t1 \n" +
                "inner join dwd_ast_bnd_bas_inf t2 on t1.SCR_ID = t2.SCR_ID\n" +
                "where t1.bred_cd = '1' and POS_DT = '" + workDate + "' \n" +
                "group by t1.SCR_ID,t1.scr_cd, t1.pos_dt,t2.bnd_frs_ctg,t2.eco_frs_typ,t2.isu_bnd_rat ";/*穿透前*/

        List<SqlRow> rows1 = ExeQuery.query(findSql1);

        String findSql2 = "select t1.scr_id,sum(t1.pos_amt) pierce_later " +
                "from dws_ast_prd_btm_ast_pos_inf t1 \n" +
                "where t1.bred_cd = '1' and POS_DT = '" + workDate + "' \n" +
                "group by t1.scr_id"; /*穿透后*/

        List<SqlRow> rows2 = ExeQuery.query(findSql2);

        String updateSql = "insert into app_operation_asset_distribute (id, asset_code, grade, industry, asset_type, trade_date, pierce_before,pierce_later, crt_date)\n" +
                "values ($AUTOIDS{id}, $S{asset_code}, $S{grade}, $S{industry}, $S{asset_type}, $S{trade_date}, $S{pierce_before}, $S{pierce_later}, $S{crt_date})";

        String deleteSql = "delete from app_operation_asset_distribute where trade_date = '" + workDate + "'";
        comnDao.doTrans( () ->{
            comnDao.update(deleteSql);
            for (SqlRow row1 : rows1) {
                for (SqlRow row2 : rows2) {
                    if (row1.getString("scr_id").equals(row2.getString("scr_id"))){
                        row1.put("pierce_later",row2.getString("pierce_later"));
                        comnDao.update(updateSql,row1);
                    }
                }
            }
        });

        log.info(" ###### 步骤4-穿透前后债券分布执行结束 ");

    }

}
