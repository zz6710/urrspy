package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
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
public class CusPosTaskService extends BusinessBaseTaskService{
    /**客户持仓销售数据调整
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        log.info(" ###### 客户持仓数据调整执行开始 ");
        Map<String,Object> params = new HashMap<>();
        params.put("deal_date",workDate);
        comnDao.doTrans( () ->{
            dealCustPosInfo(params);
        });
        log.info(" ###### 客户持仓数据调整执行结束 ");

    }

    /**
     * 获取需要调整的产品客户信息
     * @param params
     * @return
     * @throws Exception
     */
    private void dealCustPosInfo(Map<String,Object> params) throws Exception{
        String sql="select prod_reg_enc,cust_id from dwd_ast_cust_lot_pos_dtl " +
                "    where deal_date=$S{deal_date}  " +
                "    group by prod_reg_enc,cust_id" +
                "    having count(1)>1 ";
        List<SqlRow> custlist = comnDao.findRows(sql,params);
        for (SqlRow custInfo : custlist) {
            params.put("prod_reg_enc",custInfo.getString("prod_reg_enc"));
            params.put("cust_id",custInfo.getString("cust_id"));
            adjustCustPosInfo(params);
        }
    }

    /**
     * 客户持仓销售数据调整
     * @param params
     * @throws Exception
     */
    private void adjustCustPosInfo(Map<String,Object> params) throws Exception{
            String sql="select isu_org_cd,zon_cd,cust_typ,cur,sal_cd,tot_lot,tot_amt,cnv_tot_amt from dwd_ast_cust_lot_pos_dtl " +
                    "     where deal_date=$S{deal_date} " +
                    "       and prod_reg_enc=$S{prod_reg_enc} " +
                    "       and cust_id=$S{cust_id} ";
            String delsql="delete from dwd_ast_cust_lot_pos_dtl " +
                    "        where deal_date=$S{deal_date} " +
                    "          and prod_reg_enc=$S{prod_reg_enc} " +
                    "          and cust_id=$S{cust_id} ";
            String addsql="insert into dwd_ast_cust_lot_pos_dtl(isu_org_cd,prod_reg_enc,cust_id,zon_cd,cust_typ,cur,pos_dt,tot_lot,tot_amt,cnv_tot_amt,deal_date,sal_cd) " +
                          "values($S{isu_org_cd},$S{prod_reg_enc},$S{cust_id},$S{zon_cd},$S{cust_typ},$S{cur},$S{deal_date},$S{tot_lot},$S{tot_amt},$S{cnv_tot_amt},$S{deal_date},$S{sal_cd})";
        List<SqlRow> listA = comnDao.findRows(sql,params);
        if(listA.size()>0){
           params.put("isu_org_cd",listA.get(0).getString("isu_org_cd"));
           params.put("zon_cd",listA.get(0).getString("zon_cd"));
           params.put("cust_type",listA.get(0).getString("cust_typ"));
           params.put("cur",listA.get(0).getString("cur"));
           params.put("sal_cd",listA.get(0).getString("sal_cd"));
           BigDecimal tot_lot = new BigDecimal(0);
           BigDecimal tot_amt = new BigDecimal(0);
           BigDecimal cnv_tot_amt = new BigDecimal(0);
           for (SqlRow custInf : listA) {
                tot_lot = tot_lot.add(custInf.getBigDecimal("tot_lot")).setScale(2,BigDecimal.ROUND_HALF_UP);
                tot_amt = tot_amt.add(custInf.getBigDecimal("tot_amt")).setScale(2,BigDecimal.ROUND_HALF_UP);
                cnv_tot_amt = cnv_tot_amt.add(custInf.getBigDecimal("cnv_tot_amt")).setScale(2,BigDecimal.ROUND_HALF_UP);
           }
           params.put("tot_lot",tot_lot);
           params.put("tot_amt",tot_amt);
           params.put("cnv_tot_amt",cnv_tot_amt);
           comnDao.update(delsql,params);
           comnDao.update(addsql,params);
        }
    }
}
