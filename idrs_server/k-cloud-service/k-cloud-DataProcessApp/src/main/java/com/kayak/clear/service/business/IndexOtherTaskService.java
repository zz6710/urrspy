package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Scope("prototype")
public class IndexOtherTaskService extends BusinessBaseTaskService{
    /**客户持仓销售数据调整
     * @param request
     * @throws Exception
     */
    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        log.info(" ###### 首页提醒服务index_reminder_record开始 ");
        Map<String,Object> params = new HashMap<>();
        params.put("deal_date",workDate);
        comnDao.doTrans( () ->{
            dealCustPosInfo(params);
        });
        log.info(" ###### 首页提醒服务结束 ");

    }

    /**
     * 首页提醒
     * @param params
     * @return
     * @throws Exception
     */
    private void dealCustPosInfo(Map<String,Object> params) throws Exception{
        task1(params);
        task2(params);
        task3(params);
        task4(params);
        task5(params);
        task6(params);
    }

    /**
     * 1 资产代码对比
     * @param params
     * @throws Exception
     */
    private void task1(Map<String,Object> params) throws Exception{
            String sql="INSERT INTO index_reminder_record " +
                    "(keyword, source_table,  remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                    "select t.id , '1',  t.scr_nm , t.old_scr_cd , t.scr_cd , '0', '请到理财中心修改公开市场代码', t.EFFECTIVE_DATE as deal_date from base_asset_code_management t where t.EFFECTIVE_DATE = $S{deal_date} ";
             comnDao.update(sql,params);
    }

    /**
     * 2 资产名称对比
     * @param params
     * @throws Exception
     */
    private void task2(Map<String,Object> params) throws Exception{
        String sql="INSERT INTO index_reminder_record " +
                "(keyword, source_table, remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                "select a.register_serno , '2',  t.i_code, a.cc_name, t.b_name_full, '0', '请到理财中心修改公开市场名称', a.theory_report_start_date from rms_stg_amng_tbnd t inner join app_asset_debt_register_info a on  t.i_code =  a.cc_ident_code  and  t.b_name_full !=a.cc_name where  a.theory_report_start_date  = $S{deal_date} and a.sys_data_status  = '1'  ";
        comnDao.update(sql,params);
    }

    /**
     * 3 机构行业更新（房地产）
     * @param params
     * @throws Exception
     */
    private void task3(Map<String,Object> params) throws Exception{
        String sql="INSERT INTO index_reminder_record " +
                "(keyword, source_table,  remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                " select t.id , '3', t.i_id , t2.itemkey as old_data, t3.itemkey as new_data, '0', '行业信息已变更为房地产', t.deal_date from rms_stg_amng_institution_industry t inner join rms_stg_amng_institution_industry t1 on t.i_id = t1.i_id join    sys_dict_item  t2   on t2.itemval = t1.industry_code join    sys_dict_item  t3   on t3.itemval = t.industry_code  where t.IS_EFFECTIVE = '1' and t.deal_date = $S{deal_date} and t1.id = ( select max(id) from rms_stg_amng_institution_industry where IS_EFFECTIVE = '0') and t.industry_code = 'k' and t1.industry_code != 'k' ";
        comnDao.update(sql,params);
    }

    /**
     * 4 机构行业新增（房地产）
     * @param params
     * @throws Exception
     */
    private void task4(Map<String,Object> params) throws Exception{
        String sql="INSERT INTO index_reminder_record " +
                "(keyword, source_table,  remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                " select t.id , '4', t.i_id , '-' as old_data, t2.itemkey as new_data, '0', '新增的机构行业信息为房地产', t.deal_date from rms_stg_amng_institution_industry t join    sys_dict_item  t2   on t2.itemval = t.industry_code where t.IS_EFFECTIVE = '1' and t.deal_date = $S{deal_date} and t.i_id not in ( select t1.i_id from rms_stg_amng_institution_industry t1 where t1.IS_EFFECTIVE = '0') and t.industry_code = 'k'  ";
        comnDao.update(sql,params);
    }

    /**
     * 5 交易价格不一致
     * @param params
     * @throws Exception
     */
    private void task5(Map<String,Object> params) throws Exception{
        String sql="INSERT INTO index_reminder_record " +
                "(keyword, source_table, remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                "select b.barg_id, '5', b.prod_cd, b.old_data, b.new_data, '0', '交易价格不一致', $S{deal_date} from ( select t.barg_id, t.prod_cd , t.barg_unt_net_prc  as  old_data, t1.barg_unt_net_prc as  new_data from dwd_evt_prd_inv_trx_dtl t left join dwd_evt_prd_inv_trx_dtl t1 on t.prod_cd = t1.prod_cd and t.barg_unt_net_prc != t1.barg_unt_net_prc and t.trx_dt = t1.trx_dt where t.bred_cd = '8' and t.bus_typ_cd in ('05', '18') and t1.bus_typ_cd in ('05', '18') and t.trx_dt = $S{deal_date} ) b  group by b.barg_id ";
        comnDao.update(sql,params);
    }


    /**
     * 6  每月21日  资产名称对比
     * @param params
     * @throws Exception
     */
    private void task6(Map<String,Object> params) throws Exception{

        String  dataStr  =  params.get("deal_date").toString().substring(6);

        if("21".equals(dataStr)){
            String sql="INSERT INTO index_reminder_record " +
                    "(keyword, source_table, remark, old_data, new_data, remind_status, remind_msg, deal_date) " +
                    "select t.id , '6', t.SCR_CD , t.SCR_NM  , t1.b_name_full  , '0', '资产名称不一致', t.deal_date from base_asset_code_management t inner  join  rms_stg_amng_tbnd t1  on t.SCR_CD  = t1.i_code  and  t.SCR_NM  !=t1.b_name_full where  t.EFFECTIVE_DATE <=$S{deal_date}  and  t.EFFECTIVE_DATE  >= (SELECT DATE_FORMAT(DATE_SUB($S{deal_date}, INTERVAL 1 MONTH),'%Y%m%d') ) ";
            comnDao.update(sql,params);
        }

    }


}
