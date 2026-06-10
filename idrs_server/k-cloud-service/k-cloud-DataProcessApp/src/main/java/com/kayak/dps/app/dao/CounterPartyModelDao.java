package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.CounterPartyModel;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
public class CounterPartyModelDao extends ComnDao {

    public UpdateResult deleteCounterPartyModel(SqlParam<CounterPartyModel> params) throws Exception {
        String sql = " delete from ods_supply_counter_party where COUNTER_PARTY_CD = $S{counterPartyCd}";
        return super.update(sql, DataSourceProperty.PUB, params.getModel());
    }


    public UpdateResult addCounterPartyModel(SqlParam<CounterPartyModel> params) throws Exception {
        return super.update("INSERT INTO `dpb`.`dwd_ast_itts_rival_inf`(`RIVAL_CD`, `ORG_NBR_EXT`, `RIVAL_SHT_NM`, `RIVAL_NM`, `CRT_DT`) VALUES ($S{rivalCd}, $S{orgNbrExt}, $S{rivalShtNm}, $S{rivalNm},  DATE_FORMAT(NOW(), '%Y%m%d')) ",
                DataSourceProperty.PUB,params.getModel());
    }

    public SqlResult<CounterPartyModel> findCounterPartyModel(SqlParam<CounterPartyModel> params) throws Exception{
        String sql = " select a.id,a.org_cd,a.counter_party_cd,a.counter_party_sht_nm,a.counter_party_type,a.counter_party_nm,a.spv_org_enc,a.spv_prod_reg_enc,a.remark,a.`version`,a.spv_pban_enc,a.enterp_scale,a.register_area " +
                " from ods_supply_counter_party a where 1=1 ";

        if (Strings.isNotBlank(params.getModel().getCounterPartyNm())) {
            sql += " and a.counter_party_nm like  '%" + params.getModel().getCounterPartyNm() + "%' ";
        }
        if (Strings.isNotBlank(params.getModel().getCounterPartyType())) {
            sql += " and a.counter_party_type like  '%" + params.getModel().getCounterPartyType() + "%' ";
        }
        if (Strings.isNotBlank(params.getModel().getCounterPartyCd())) {
            sql += " and a.counter_party_cd  = '"+params.getModel().getCounterPartyCd() +"' ";
        }
        if (Strings.isNotBlank(params.getModel().getEnterpScale())) {
            sql += " and a.enterp_scale  = '"+params.getModel().getEnterpScale() +"' ";
        }
        if (Strings.isNotBlank(params.getModel().getRegisterArea())) {
            sql += " and a.register_area  = '"+params.getModel().getRegisterArea() +"' ";
        }
        if (Strings.isNotBlank(params.getModel().getRegisterAreaFu()) && "1".equals(params.getModel().getRegisterAreaFu())) {
            sql += " and ifnull(a.register_area, '') = '' ";
        }
        if (Strings.isNotBlank(params.getModel().getEnterpScaleFu()) && "1".equals(params.getModel().getEnterpScaleFu())) {
            sql += " and a.counter_party_cd in (select c.prty_stnd_cd from  dws_ast_prd_itm_bal_dtl a" +
                    "    inner join dwd_ast_bnd_bas_inf c on a.scr_cd =c.scr_cd and a.mrkt_typ = c.trx_mkt " +
                    "    where a.ctg_cd ='A4500' and a.amt_bal <> 0 and (isu_org_typ_siz is null or isu_org_typ_siz ='') )";
        }
        return super.findRows(sql,
                DataSourceProperty.PUB, params);

    }

    public UpdateResult updateCounterPartyModel(SqlParam<CounterPartyModel> params) throws Exception {
        return super.update("update ods_supply_counter_party set org_cd = $S{orgCd},org_nm = $S{orgNm},counter_party_cd = $S{counterPartyCd},counter_party_sht_nm = $S{counterPartyShtNm},counter_party_nm = $S{counterPartyNm},spv_org_enc = $S{spvOrgEnc},spv_prod_reg_enc = $S{spvProdRegEnc},remark = $S{remark},version = (version + 1),upd_dt =  DATE_FORMAT(NOW(), '%Y%m%d'),counter_party_type=$S{counterPartyType},spv_pban_enc=$S{spvPbanEnc}, " +
                        " enterp_scale_flag = (case when enterp_scale_flag = '0' and enterp_scale = $S{enterpScale} then '0' else '1' end), " +
                        " register_area_flag = (case when register_area_flag = '0' and register_area = $S{registerArea} then '0' else '1' end), " +
                        " enterp_scale=$S{enterpScale},register_area=$S{registerArea} where id = $S{id}",
                DataSourceProperty.PUB, params.getModel());
    }
    public SqlResult<CounterPartyModel> findOrgCdAndNm(SqlParam<CounterPartyModel> params) throws Exception {
        String sql = "SELECT DISTINCT ORG_CD ,ORG_NM   FROM ods_supply_counter_party where ORG_CD like '%$U{orgCd}%' or ORG_NM like '%$U{orgCd}%'";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }


    public void updateImportData(List<CounterPartyModel> reportPCDS) throws Exception {
        for (int i = 1; i < reportPCDS.size(); i++) {
            CounterPartyModel counterParty = reportPCDS.get(i);
            if(StringUtils.equals(counterParty.getCounterPartyNm(),""))
                continue;
            if(counterParty.getCounterPartyNm().contains(".0")) counterParty.setCounterPartyNm(counterParty.getCounterPartyNm().substring(0,counterParty.getCounterPartyNm().indexOf(".0")));
            if(counterParty.getSpvOrgEnc().contains(".0")) counterParty.setSpvOrgEnc(counterParty.getSpvOrgEnc().substring(0,counterParty.getSpvOrgEnc().indexOf(".0")));
            if(counterParty.getSpvProdRegEnc().contains(".0")) counterParty.setSpvProdRegEnc(counterParty.getSpvProdRegEnc().substring(0,counterParty.getSpvProdRegEnc().indexOf(".0")));
            if(counterParty.getSpvPbanEnc().contains(".0")) counterParty.setSpvPbanEnc(counterParty.getSpvPbanEnc().substring(0,counterParty.getSpvPbanEnc().indexOf(".0")));
            super.update("update ods_supply_counter_party set spv_org_enc = $S{spvOrgEnc},spv_prod_reg_enc = $S{spvProdRegEnc},spv_pban_enc = $S{spvPbanEnc},version = (version + 1),upd_dt =  DATE_FORMAT(NOW(), '%Y%m%d'),enterp_scale=$S{enterpScale} where counter_party_nm = $S{counterPartyNm} and counter_party_type=(select itemkey from sys_dict_item where dict ='org_type' and itemval= $S{counterPartyType} LIMIT 1)", DataSourceProperty.PUB, counterParty);
        }
    }

    /*更新企业规模*/
    public void updateImportEnterpScale(List<CounterPartyModel> reportPCDS) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "replace into ods_supply_counter_party (counter_party_cd,counter_party_nm,csld_soc_crd_cd,enterp_scale,register_area,enterp_scale_flag,register_area_flag) values (?,?,?,?,?,'1','1')";
            this.doTrans(() -> {
                Connection connection = this.getConnection();
                try (PreparedStatement ps = connection.prepareStatement(batchSql)) {

                    for (CounterPartyModel counterParty : reportPCDS) {
                        if (StringUtils.equals(counterParty.getCounterPartyCd(), ""))
                            continue;
                        ps.setString(1, counterParty.getCounterPartyCd());
                        ps.setString(2, counterParty.getCounterPartyNm());
                        ps.setString(3, counterParty.getCounterPartyCd());
                        ps.setString(4, counterParty.getEnterpScale() == null ? null : counterParty.getEnterpScale().split(" ")[0]);
                        ps.setString(5, counterParty.getRegisterArea() == null ? null : counterParty.getRegisterArea().split(" ")[0]);
                        ps.addBatch();
                    }
                    ps.executeBatch();

                    log.info(" ##### 批量入库{}耗时: {} ms", reportPCDS.size(), System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    log.error("导入交易对手信息异常!", e);
                    throw new Exception(e.getMessage());
                }
            });
    }

}
