package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG01CompareDao;
import com.kayak.rpt.rhzg.dao.ZG01Dao;
import com.kayak.rpt.rhzg.model.ZG01;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@APIDefine(desc = "资管产品基本信息服务", model = ZG01.class)
public class ZG01Service implements ExcelImportService<ZG01> {

    private static final Logger log = LoggerFactory.getLogger(ZG01Service.class);

    @Autowired
    private ZG01Dao zG01Dao;

    @Autowired
    private ComnDao comnDao;
    @Autowired
    private ZG01CompareDao zg01CompareDao;


    @API(desc = "查询资管产品基本信息", auth = APIAuth.YES)
    public SqlResult<ZG01> findZG01s1(SqlParam<ZG01> params) throws Exception {
        //params.setMakeSql(true);
        if (StringUtils.isBlank(params.getModel().getBeginDate())&&StringUtils.isBlank(params.getModel().getReportBeginDate())) {
            SqlResult<ZG01> zg01=new SqlResult<>();
            zg01.setRows(new ArrayList<>());
            return zg01;
        }
        return zG01Dao.findZG01s(params);
    }

    @API(desc = "查询资管产品基本信息及字段变更标识", auth = APIAuth.YES)
    public SqlResult<ZG01> findZG01s(SqlParam<ZG01> params) throws Exception {
        if (StringUtils.isBlank(params.getModel().getBeginDate())&&StringUtils.isBlank(params.getModel().getReportBeginDate())) {
            SqlResult<ZG01> zg01=new SqlResult<>();
            zg01.setRows(new ArrayList<>());
            return zg01;
        }else{
            SqlResult<ZG01> r1=  zG01Dao.findZG01s(params);
            List<ZG01> returnList = new ArrayList<>();
            if(r1 != null && r1.getRows() != null &&r1.getRows().size() > 0){
                List<ZG01> list0 = new ArrayList<>(),list1 = new ArrayList<>();
                list0 = r1.getRows();//原始数据
                StringBuffer prods = new StringBuffer();
                for(int i = 0;i<list0.size();i++){
                    ZG01 ZG01 = list0.get(i);
                    if(i == list0.size()-1){
                        prods.append("'"+ZG01.getProdCd()+"'");
                    }else{
                        prods.append("'"+ZG01.getProdCd()+"',");
                    }
                }

                list1 = zG01Dao.findZG01sByProd(String.valueOf(prods),params).getRows();//指定产品的数据集

                for(int i = 0;i<list0.size();i++){
                    ZG01 ZG01 = list0.get(i);
                    ZG01 prodIssuance2 = zg01CompareDao.compareFlag(ZG01,list1);
                    returnList.add(prodIssuance2);
                }
            }
            r1.setRowsList1(returnList);
            return r1;
        }
    }

    @API(desc = "修改资管产品基本信息",params = "id,msg_typ,theory_report_start_date,prod_cd,prod_nm,isu_org_cd,isu_org_nm,prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt,clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr,ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f,trust_prod_type,base_open_info_f,change_reason,back1,back2,back3,back4,back5" , auth = APIAuth.YES)
    public int updateZG01(SqlParam<ZG01> params) throws Exception {
        return zG01Dao.updateZG01(params).getEffect();
    }



    @API(desc = "删除资管产品基本信息", params = "id", auth = APIAuth.YES)
    public int deleteZG01(SqlParam<ZG01> params) throws Exception {
        return zG01Dao.deleteZG01(params).getEffect();
    }


    public void importFile(List<ZG01> zg01s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg01(msg_typ,report_date,register_status,prod_cd,prod_nm,isu_org_cd,isu_org_nm,prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt,clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr,ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f,trust_prod_type,base_open_info_f,change_reason,back1,back2,back3,back4,back5,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,create_date,theory_report_start_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String batchDelSql = "DELETE FROM app_pbc_report_zg01 where register_status<>'3' and prod_cd= ?";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG01 info : zg01s){

                    ps.setString(1,info.getMsgTyp()==null ? null : info.getMsgTyp().split(":")[0]);
                    ps.setString(2,DateUtil.getNextSysWordDay(info.getFoundDt().replace("-","")));
                    ps.setString(3,"0");
                    ps.setString(4,info.getProdCd());
                    ps.setString(5,info.getProdNm());
                    ps.setString(6,info.getIsuOrgCd());
                    ps.setString(7,info.getIsuOrgNm());
                    ps.setString(8,info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
                    ps.setString(9,info.getProdInvTyp()==null ? null : info.getProdInvTyp().split(":")[0]);
                    ps.setString(10,info.getProdBrnd());
                    ps.setString(11,info.getProdTms());
                    ps.setString(12,info.getIsuOrgProdCd());
                    ps.setString(13,info.getClcCcy());
                    ps.setString(14,info.getCallPrcpCcy());
                    ps.setString(15,info.getCallErnCcy());
                    ps.setString(16,info.getProdClcMth()==null ? null : info.getProdClcMth().split(":")[0]);
                    ps.setString(17,info.getMngMth()==null ? null : info.getMngMth().split(":")[0]);
                    ps.setString(18,info.getProdMod()==null ? null : info.getProdMod().split(":")[0]);
                    ps.setString(19,info.getClcBgnDt());
                    ps.setString(20,info.getClcEndDt());
                    ps.setString(21,info.getIsuOrgEarlyTermF()==null ? null : info.getIsuOrgEarlyTermF().split(":")[0]);
                    ps.setString(22,info.getCustRedemptionF()==null ? null : info.getCustRedemptionF().split(":")[0]);
                    ps.setString(23,info.getProdIncCrdF()==null ? null : info.getProdIncCrdF().split(":")[0]);
                    ps.setString(24,info.getProdIncCrdOrgTyp()==null ? null : info.getProdIncCrdOrgTyp().split(":")[0]);
                    ps.setString(25,info.getProdIncCrdForm()==null ? null : info.getProdIncCrdForm().split(":")[0]);
                    ps.setString(26,info.getDmsTrstOrgCd());
                    ps.setString(27,info.getOvsTrstOrgCnr());
                    ps.setString(28,info.getOvsTrstOrgNm());
                    ps.setString(29,info.getFoundDt());
                    ps.setString(30,info.getChangeDt());
                    ps.setString(31,info.getProdScheduledEndDt());
                    ps.setString(32,info.getEntrustedDuty()==null ? null : info.getEntrustedDuty().split(":")[0]);
                    ps.setString(33,info.getClsfProdF()==null ? null : info.getClsfProdF().split(":")[0]);
                    ps.setString(34,info.getUsufructChangeProdF()==null ? null : info.getUsufructChangeProdF().split(":")[0]);
                    ps.setString(35,info.getCashMngProdF()==null ? null : info.getCashMngProdF().split(":")[0]);
                    ps.setString(36,info.getCbWMngF()==null ? null : info.getCbWMngF().split(":")[0]);
                    ps.setString(37,info.getTrustProdType()==null ? null : info.getTrustProdType().split(":")[0]);
                    ps.setString(38,info.getBaseOpenInfoF()==null ? null : info.getBaseOpenInfoF().split(":")[0]);
                    ps.setString(39,info.getChangeReason());
                    ps.setString(40,info.getBack1()==null ? null : info.getBack1().split(":")[0]);
                    ps.setString(41,info.getBack2());
                    ps.setString(42,info.getBack3()==null ? null : info.getBack3().split(":")[0]);
                    ps.setString(43,info.getBack4());
                    ps.setString(44,info.getBack5());
                    ps.setString(45, "1");//sys_data_status 1
                    ps.setString(46, "2");//sys_data_source 2
                    ps.setString(47, "1.0");//sys_data_version 2
                    ps.setString(48, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(49, DateUtil.getNowDate());// imp_date
                    ps.setString(50, DateUtil.getNowDate());// create_date
                    ps.setString(51, info.getFoundDt().replace("-",""));// theory_report_start_date
                    ps.addBatch();
                }
                ps.executeBatch();


                log.info(" ##### 批量入库{}耗时: {} ms", zg01s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资管产品基本信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg01ByDate(Object params) throws Exception{
        try {
            zG01Dao.deleteZg01ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getNextVersion(String prodCode) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select PROD_CD,format(cast(max(sys_data_version)as decimal(10,1))+0.1,1) as sys_data_version from APP_PBC_REPORT_ZG01 where REGISTER_STATUS ='3' and  PROD_CD =$S{prodCode}";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, prodCode);
            if (sqlRows != null && sqlRows.size() > 0){
                return sqlRows.get(0).getString("sys_data_version");
            }else
                return "1.0";
        }
    }


}
