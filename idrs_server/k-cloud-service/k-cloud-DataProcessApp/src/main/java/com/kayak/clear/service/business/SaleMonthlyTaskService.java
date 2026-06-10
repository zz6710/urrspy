package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
public class SaleMonthlyTaskService extends BusinessBaseTaskService{
    private String mark = "0";
    /**月度销售数据调整
     *
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        log.info(" ###### 月度销售数据调整执行开始 ");
        Map<String,Object> params = new HashMap<>();
        params.put("deal_date",workDate);
        double digits = Double.parseDouble(SysUtil.getSystemParamsByParaid("80000050"));
        delAjustLog(params);
        //调整月度销售数据（投资者类型）
        comnDao.doTrans( () ->{
            dealSaleInvestor(params,digits);
            if("0".equals(mark)){
                updProdSaleData(params);
            }
        });
        if("0".equals(mark)){
            //调整月度销售数据（地区及销售渠道）
            comnDao.doTrans( () ->{
                adjustSaleData(params);
                delProdSaleData(params);
            });
        }
        log.info(" ###### 月度销售数据调整执行结束 ");

    }

    /**
     * 获取G06与月度销售数据差额（按投资这类型）
     * @param params
     * @return
     * @throws Exception
     */
    private void dealSaleInvestor(Map<String,Object> params,double dights) throws Exception{
        log.info(" ###### 月度销售数据调整执行开始--按投资者类型 ");
        //获取G06销售金额、净销售金额及持有余额
        String sqlA="select data_value from app_rpt_g06_01 where row_id='3' and column_id='2' and report_date=$S{deal_date}";
        String sqlB="select data_value from app_rpt_g06_01 where row_id='3' and column_id='3' and report_date=$S{deal_date}";
        String sqlC="select data_value from app_rpt_g06_01 where row_id='3' and column_id='6' and report_date=$S{deal_date}";
        //获取销售月度销售总额
        String sqlD="select ifnull(sum(sale_total_money),0) as sale_total_money," +
                "       ifnull(sum(sale_net_money),0) as sale_net_money,    " +
                "       ifnull(sum(hold_babance),0) as hold_babance         " +
                "  from app_prod_sale_investor " +
                " where trade_date=$S{deal_date} ";
        BigDecimal sale_total_money = new BigDecimal(0);
        BigDecimal sale_net_money= new BigDecimal(0);
        BigDecimal hold_babance= new BigDecimal(0);
        List<SqlRow> listA = comnDao.findRows(sqlA,params);
        if(listA.size()>0){
            sale_total_money =listA.get(0).getBigDecimal("data_value");
        }
        List<SqlRow> listB = comnDao.findRows(sqlB,params);
        if(listB.size()>0){
            sale_net_money =listB.get(0).getBigDecimal("data_value");
        }
        List<SqlRow> listC = comnDao.findRows(sqlC,params);
        if(listC.size()>0){
            hold_babance =listC.get(0).getBigDecimal("data_value");
        }
        List<SqlRow> listD = comnDao.findRows(sqlD,params);
        if(listD.size()>0){
            sale_total_money = sale_total_money.subtract(listD.get(0).getBigDecimal("sale_total_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            sale_net_money = sale_net_money.subtract(listD.get(0).getBigDecimal("sale_net_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            hold_babance = hold_babance.subtract(listD.get(0).getBigDecimal("hold_babance")).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        if(Math.abs(sale_total_money.doubleValue())>dights || Math.abs(sale_net_money.doubleValue())>dights || Math.abs(hold_babance.doubleValue())>dights){
            mark = "1";
            return;
        }else{
            mark = "0";
        }
        if(sale_total_money.doubleValue()!=0 || sale_net_money.doubleValue()!=0 || hold_babance.doubleValue()!=0){
            params.put("sale_total_money",sale_total_money);
            params.put("sale_net_money",sale_net_money);
            params.put("hold_babance",hold_babance);
            adjustSaleInvestor(params);
        }
    }

    /**
     * 月度销售数据调整（按投资这类型）
     * @param params
     * @throws Exception
     */
    private void adjustSaleInvestor(Map<String,Object> params) throws Exception{
        //获取当前产品销售金额最大记录
        String tqlA = "select id,investor_type as data_type,sale_total_money from app_prod_sale_investor "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc='Z7003220000009' "+
                " order by sale_total_money desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlB = "select id,investor_type as data_type,sale_net_money from app_prod_sale_investor "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc='Z7003220000009' "+
                " order by abs(sale_net_money) desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlC = "select id,investor_type as data_type,hold_babance from app_prod_sale_investor "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc='Z7003220000009' "+
                " order by hold_babance desc limit 1 ";
        String tqlD="update app_prod_sale_investor set sale_total_money=$S{adj_data_value} where id=$S{id}";
        String tqlE="update app_prod_sale_investor set sale_net_money=$S{adj_data_value} where id=$S{id}";
        String tqlF="update app_prod_sale_investor set hold_babance=$S{adj_data_value} where id=$S{id}";
        params.put("prod_reg_enc","Z7003220000009");
        params.put("table_type","1");
        if(Double.parseDouble(params.get("sale_total_money").toString())!=0){
            updSaleTotalMoney(tqlA,tqlD,params);
        }
        if(Double.parseDouble(params.get("sale_net_money").toString())!=0){
            updSaleNetMoney(tqlB,tqlE,params);
        }
        if(Double.parseDouble(params.get("hold_babance").toString())!=0){
            updHoldBalance(tqlC,tqlF,params);
        }
        log.info(" ###### 月度销售数据调整执行结束--按投资者类型 ");
    }

    /**
     * 月度销售数据调整（按地区及销售渠道）
     * @param params
     * @return
     * @throws Exception
     */
    private void adjustSaleData(Map<String,Object> params) throws Exception{
        BigDecimal sale_total_money = new BigDecimal(0.0);
        BigDecimal sale_net_money= new BigDecimal(0.0);
        BigDecimal hold_babance= new BigDecimal(0.0);
        String sqlProd="select prod_reg_enc,sale_total_money,sale_net_money,hold_babance from tmp_prod_sale_inf where trade_date=$S{deal_date} ";
        List<SqlRow> prodList = comnDao.findRows(sqlProd,params);
        for(SqlRow prodInfo : prodList) {
            params.put("prod_reg_enc",prodInfo.getString("prod_reg_enc"));
            sale_total_money = prodInfo.getBigDecimal("sale_total_money");
            sale_net_money = prodInfo.getBigDecimal("sale_net_money");
            hold_babance = prodInfo.getBigDecimal("hold_babance");
            dealSaleRegion(params,sale_total_money,sale_net_money,hold_babance);
            dealSaleChannel(params,sale_total_money,sale_net_money,hold_babance);
        }
    }
    /**
     * 获取产品销售数据与月度销售数据差额（按地区）
     * @param params
     * @return
     * @throws Exception
     */
    private void dealSaleRegion(Map<String,Object> params,BigDecimal sale_total_money,BigDecimal sale_net_money,BigDecimal hold_babance) throws Exception{
        log.info(" ###### 月度销售数据调整执行开始--按地区 ");
        //获取销售月度销售总额
        String querySql="select ifnull(sum(sale_total_money),0) as sale_total_money," +
                "           ifnull(sum(sale_net_money),0) as sale_net_money,    " +
                "           ifnull(sum(hold_babance),0) as hold_babance         " +
                "      from app_prod_sale_region                            " +
                "     where trade_date=$S{deal_date} and prod_reg_enc=$S{prod_reg_enc} ";
        List<SqlRow> saleList = comnDao.findRows(querySql,params);
        if(saleList.size()>0){
            sale_total_money = sale_total_money.subtract(saleList.get(0).getBigDecimal("sale_total_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            sale_net_money = sale_net_money.subtract(saleList.get(0).getBigDecimal("sale_net_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            hold_babance = hold_babance.subtract(saleList.get(0).getBigDecimal("hold_babance")).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        if(sale_total_money.doubleValue()!=0 || sale_net_money.doubleValue()!=0 || hold_babance.doubleValue()!=0){
            params.put("sale_total_money",sale_total_money);
            params.put("sale_net_money",sale_net_money);
            params.put("hold_babance",hold_babance);
            adjustSaleRegion(params);
        }

    }

    /**
     * 月度销售数据调整（按地区）
     * @param params
     * @throws Exception
     */
    private void adjustSaleRegion(Map<String,Object> params) throws Exception{
        //获取当前产品销售金额最大记录
        String tqlA = "select id,region_type as data_type,sale_total_money from app_prod_sale_region "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by sale_total_money desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlB = "select id,region_type as data_type,sale_net_money from app_prod_sale_region "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by abs(sale_net_money) desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlC = "select id,region_type as data_type,hold_babance from app_prod_sale_region "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by hold_babance desc limit 1 ";

        String tqlD="update app_prod_sale_region set sale_total_money=$S{adj_data_value} where id=$S{id}";
        String tqlE="update app_prod_sale_region set sale_net_money=$S{adj_data_value} where id=$S{id}";
        String tqlF="update app_prod_sale_region set hold_babance=$S{adj_data_value} where id=$S{id}";

        params.put("table_type","2");
        if(Double.parseDouble(params.get("sale_total_money").toString())!=0){
            updSaleTotalMoney(tqlA,tqlD,params);
        }
        if(Double.parseDouble(params.get("sale_net_money").toString())!=0){
            updSaleNetMoney(tqlB,tqlE,params);
        }
        if(Double.parseDouble(params.get("hold_babance").toString())!=0){
            updHoldBalance(tqlC,tqlF,params);
        }
        log.info(" ###### 月度销售数据调整执行结束--按地区 ");
    }

    /**
     * 获取产品销售数据与月度销售数据差额（按销售渠道）
     * @param params
     * @return
     * @throws Exception
     */
    private void dealSaleChannel(Map<String,Object> params,BigDecimal sale_total_money,BigDecimal sale_net_money,BigDecimal hold_babance) throws Exception{
        log.info(" ###### 月度销售数据调整执行开始--按销售渠道 ");
        //获取销售月度销售总额
        String querySql="select ifnull(sum(sale_total_money),0) as sale_total_money," +
                "           ifnull(sum(sale_net_money),0) as sale_net_money,    " +
                "           ifnull(sum(hold_babance),0) as hold_babance         " +
                "      from app_prod_sale_channel                           " +
                "     where trade_date=$S{deal_date} and prod_reg_enc=$S{prod_reg_enc} ";
        List<SqlRow> saleList = comnDao.findRows(querySql,params);
        if(saleList.size()>0){
            sale_total_money = sale_total_money.subtract(saleList.get(0).getBigDecimal("sale_total_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            sale_net_money = sale_net_money.subtract(saleList.get(0).getBigDecimal("sale_net_money")).setScale(2,BigDecimal.ROUND_HALF_UP);
            hold_babance = hold_babance.subtract(saleList.get(0).getBigDecimal("hold_babance")).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        if(sale_total_money.doubleValue()!=0 || sale_net_money.doubleValue()!=0 || hold_babance.doubleValue()!=0){
            params.put("sale_total_money",sale_total_money);
            params.put("sale_net_money",sale_net_money);
            params.put("hold_babance",hold_babance);
            adjustSaleChannel(params);
        }
    }

    /**
     * 月度销售数据调整（按销售渠道）
     * @param params
     * @throws Exception
     */
    private void adjustSaleChannel(Map<String,Object> params) throws Exception{
        //获取当前产品销售金额最大记录
        String tqlA = "select id,sale_org_code as data_type,sale_total_money from app_prod_sale_channel "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by sale_total_money desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlB = "select id,sale_org_code as data_type,sale_net_money from app_prod_sale_channel "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by abs(sale_net_money) desc limit 1 ";
        //获取当前产品净销售金额最大记录
        String tqlC = "select id,sale_org_code as data_type,hold_babance from app_prod_sale_channel "+
                " where trade_date=$S{deal_date} "+
                "   and prod_reg_enc=$S{prod_reg_enc} "+
                " order by hold_babance desc limit 1 ";

        String tqlD="update app_prod_sale_channel set sale_total_money=$S{adj_data_value} where id=$S{id}";
        String tqlE="update app_prod_sale_channel set sale_net_money=$S{adj_data_value} where id=$S{id}";
        String tqlF="update app_prod_sale_channel set hold_babance=$S{adj_data_value} where id=$S{id}";
        params.put("table_type","3");
        if(Double.parseDouble(params.get("sale_total_money").toString())!=0){
            updSaleTotalMoney(tqlA,tqlD,params);
        }
        if(Double.parseDouble(params.get("sale_net_money").toString())!=0){
            updSaleNetMoney(tqlB,tqlE,params);
        }
        if(Double.parseDouble(params.get("hold_babance").toString())!=0){
            updHoldBalance(tqlC,tqlF,params);
        }
        log.info(" ###### 月度销售数据调整执行结束--按销售渠道 ");
    }

    /**
     * 数据调整日志生成
     * @param params
     * @throws Exception
     */
    private void dataAjustLog(Map<String,Object> params) throws Exception{
        //记录调整前调整后的数据
        String logSql="insert into base_prod_sal_adjust_log(table_type,sale_id,prod_reg_enc,data_type,adj_field,data_value,adj_data_value,trade_date)values " +
                      "($S{table_type},$S{id},$S{prod_reg_enc},$S{data_type},$S{adj_field},$S{data_value},$S{adj_data_value},$S{deal_date}) ";
        comnDao.update(logSql,params);
    }

    /**
     * 更新销售总金额
     * @param sql
     * @param params
     * @return
     */
    private Map<String,Object> updSaleTotalMoney(String tql,String sql,Map<String,Object> params) throws Exception{
        BigDecimal adj_sale_total_money = new BigDecimal(0.0);
        List<SqlRow> totalList = comnDao.findRows(tql,params);
        if(totalList.size()>0){
            params.put("id",totalList.get(0).getString("id"));
            params.put("data_type",totalList.get(0).getString("data_type"));
            params.put("adj_field","1");
            adj_sale_total_money = totalList.get(0).getBigDecimal("sale_total_money").add(new BigDecimal(params.get("sale_total_money").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
            params.put("data_value",totalList.get(0).getBigDecimal("sale_total_money").toString());
            params.put("adj_data_value",adj_sale_total_money.toString());
            comnDao.update(sql,params);
            dataAjustLog(params);
        }
        return params;
    }

    /**
     * 更新销售净金额
     * @param sql
     * @param params
     * @return
     */
    private Map<String,Object> updSaleNetMoney(String tql,String sql,Map<String,Object> params) throws Exception{
        BigDecimal adj_sale_net_money = new BigDecimal(0.0);
        List<SqlRow> netList = comnDao.findRows(tql,params);
        if(netList.size()>0){
            params.put("id",netList.get(0).getString("id"));
            params.put("data_type",netList.get(0).getString("data_type"));
            params.put("adj_field","2");
            adj_sale_net_money = netList.get(0).getBigDecimal("sale_net_money").add(new BigDecimal(params.get("sale_net_money").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
            params.put("data_value",netList.get(0).getBigDecimal("sale_net_money").toString());
            params.put("adj_data_value",adj_sale_net_money.toString());
            comnDao.update(sql,params);
            dataAjustLog(params);
        }
        return params;
    }

    /**
     * 更新销售持仓余额
     * @param sql
     * @param params
     * @return
     */
    private Map<String,Object> updHoldBalance(String tql,String sql,Map<String,Object> params) throws Exception{
        BigDecimal adj_hold_babance= new BigDecimal(0.0);
        List<SqlRow> holdList = comnDao.findRows(tql,params);
        if(holdList.size()>0){
            params.put("id",holdList.get(0).getString("id"));
            params.put("data_type",holdList.get(0).getString("data_type"));
            params.put("adj_field","3");
            adj_hold_babance = holdList.get(0).getBigDecimal("hold_babance").add(new BigDecimal(params.get("hold_babance").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
            params.put("data_value",holdList.get(0).getBigDecimal("hold_babance").toString());
            params.put("adj_data_value",adj_hold_babance.toString());
            comnDao.update(sql,params);
            dataAjustLog(params);
        }
        return params;
    }

    /**
     * 生成产品销售数据汇总
     * @param params
     * @throws Exception
     */
    private void updProdSaleData(Map<String,Object> params) throws Exception{
        //生成调整后产品销售汇总数
        String updsql="insert into tmp_prod_sale_inf " +
                "select prod_reg_enc," +
                "       sum(sale_total_money) as sale_total_money," +
                "       sum(sale_net_money) as sale_net_money," +
                "       sum(hold_babance) as hold_babance," +
                "       trade_date " +
                "  from app_prod_sale_investor " +
                " where trade_date=$S{deal_date} " +
                " group by prod_reg_enc,trade_date ";
        comnDao.update(updsql,params);
    }

    /**
     * 删除产品销售数据汇总
     * @param params
     * @throws Exception
     */
    private void delProdSaleData(Map<String,Object> params) throws Exception{
        //清除产品月度销售信息
        String delsql = "delete from tmp_prod_sale_inf where trade_date=$S{deal_date}";
        comnDao.update(delsql,params);
    }

    /**
     * 清除数据调整日志生成
     * @param params
     * @throws Exception
     */
    private void delAjustLog(Map<String,Object> params) throws Exception{
        //记录调整前调整后的数据
        String logSql="delete from base_prod_sal_adjust_log where trade_date=$S{deal_date} ";
        comnDao.update(logSql,params);
    }

}
