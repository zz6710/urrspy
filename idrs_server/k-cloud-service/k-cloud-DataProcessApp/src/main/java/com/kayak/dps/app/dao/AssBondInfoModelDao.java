package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.AssBondInfoModel;
import com.kayak.dps.app.model.AssFundInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class AssBondInfoModelDao extends ComnDao {

  

    public void addAssBondInfoModel(SqlParam<AssBondInfoModel> params) throws Exception {
        String sql = "update ods_supply_bond_bas_inf t \n" +
                "set \n" +
                "t.TRX_PLA = $S{trxPla},/**交易流通场所*/\n" +
                "t.CBND_FRS_CTG = $S{cbndFrsCtg},/**债券一级分类*/\n" +
                "t.CBND_SCD_CTG = $S{cbndScdCtg},/**中债二级分类*/\n" +
                "t.PBNK_FRS_CTG = $S{pbnkFrsCtg},/**人行一级分类*/\n" +
                "t.PBNK_SCD_CTG = $S{pbnkScdCtg},/**人行二级分类*/\n" +
                "t.PBNK_TRD_CTG = $S{pbnkTrdCtg},/**人行三级分类*/\n" +
                "t.SPC_TYPE = $S{spcType},/**具体类别*/\n" +
                "t.MAIN_RAT = $S{mainRat},/**主体评级*/\n" +
                "t.G06_TYPE = $S{g06Type},/**G06分类*/\n" +
                "t.ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno},/**发行机构类型（按技术领域划分）*/\n" +
                "t.ISU_ORG_TYP_ECN = $S{isuOrgTypEcn},/**发行机构类型（按经济类型划分）*/\n" +
                "t.ISU_ORG_TYP_SIZ = $S{isuOrgTypSiz},/**企业规模*/\n" +
                "t.ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz},/**发行机构类型（按规模划分）*/\n" +
                "t.ECO_FRS_TYP = $S{ecoFrsTyp},/**发行机构所属行业（一级分类）*/\n" +
                "t.ISU_ORG_BLG_IDT = $S{isuOrgBlgIdt},/**发行机构所属行业（二级分类）*/\n" +
                "t.REG_TRST_ORG = $S{regTrstOrg},/**登记托管机构*/\n" +
                "t.ISSU_PROVINCE = $S{issuProvince},/**发行省*/\n" +
                "t.ISSU_CITY = $S{issuCity},/**发行市*/\n" +
                "t.UPD_DT = date_format(CURDATE(), '%Y%m%d'),\n" +
                "t.version = t.version + 1\n" +
                "where \n" +
                "t.SCR_ID = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB,params.getModel());
    }

    /**
     * 债券信息导入
     * @param assBondInfoModelList
     */
    public void importAssBondInfo(List<AssBondInfoModel> assBondInfoModelList) throws Exception {
        long startTime = System.currentTimeMillis();
        String lastMonthEndDay = DateUtils.getLastDayOfLastMonth(DateUtils.getDateTimeFormat(new Date()));//获取当前系统日期
        //债券预处理层更新
        String executeStrUpdate = "update ods_supply_bond_bas_inf " +
                "                     set ISU_ORG_TYP_SIZ= (select itemkey from sys_dict_item where dict='debtor_type' and itemval = trim(?) limit 1)," +
                "                         ECO_FRS_TYP = (select itemkey from sys_dict_item where dict='isuOrgBlgIdt01' and itemval = trim(?) limit 1)," +
                "                         ISSU_PROVINCE = (select itemkey from sys_dict_item where dict='pbc_prvc_area' and itemval like CONCAT('%',trim(?),'%') limit 1)," +
                "                         ISSU_CITY =  (select itemkey from sys_dict_item where dict='pbc_city_area_det' and itemval = trim(?) limit 1)," +
                "                         CRT_DT = DATE_FORMAT(NOW(), '%Y%m%d')," +
                "                         PBNK_SCD_CTG = ?," +
                "                         G06_TYPE = ?," +
                "                         version=(version+1)  " +
                "                   where SCR_ID = ?";
        //债券债项评级更新
        String executeStrRe1 = "replace into DWD_AST_BND_CRD_RAT(SCR_ID,SCR_CD,RAT_DT,RAT_TYP,CRD_RAT,RAT_ORG_CD,CRD_RAT_CMT,CRT_DT,UPD_DT,deal_date) " +
                "                values (?,?,?,?,?,?,?,?,?,?) ";
        //债券主体评级更新
        String executeStrRe2 = "replace into DWD_AST_BND_MAIN_RAT(CMP_NM,RAT_DT,RAT_TYP,CRD_RAT,RAT_ORG_CD,CRD_RAT_CMT,CRT_DT,UPD_DT,deal_date) " +
                "                values (?,?,?,?,?,?,?,?,?) ";
        //批量处理
        super.doTrans(() -> {
            Connection connection = super.getConnection();
            PreparedStatement psUpdate = connection.prepareStatement(executeStrUpdate);//更新债券补录信息
            PreparedStatement psRe1 = connection.prepareStatement(executeStrRe1);//更新债项评级
            PreparedStatement psRe2 = connection.prepareStatement(executeStrRe2);//更新主体评级

            try{
                for (AssBondInfoModel abim : assBondInfoModelList) {
                    String scrId = abim.getScrId();
                    String trxPla = abim.getTrxPla();//交易流通场所
                    if(StringUtils.isNotBlank(scrId)){
                        if(scrId.contains(".")){
                            scrId = windCodeToScrId(scrId);//将wind代码转换scr_id
                            abim.setScrId(scrId);
                            psUpdate.setString(1, abim.getIsuOrgTypSiz());//企业规模
                            psUpdate.setString(2, abim.getEcoFrsTyp());//国民经济一级分类
                            psUpdate.setString(3, abim.getIssuProvince());//发行省
                            psUpdate.setString(4, abim.getIssuCity());//发行市
                            psUpdate.setString(5, abim.getPbnkScdCtg());//人行二级分类
                            psUpdate.setString(6, abim.getG06Type());//G06分类
                            psUpdate.setString(7, scrId);//债券id
                            psUpdate.addBatch();

                            if(!abim.getBondRat().equals("") || abim.getBondRat() == null){
                                psRe1.setString(1, scrId);//债券id
                                psRe1.setString(2, scrId.substring(0,scrId.indexOf(".")));//债券代码
                                psRe1.setString(3, lastMonthEndDay);//评级日期:上月最后一日
                                psRe1.setString(4, "1");//评级类型 1：长期评级
                                psRe1.setString(5, abim.getBondRat());//债项评级
                                psRe1.setString(6, "2");//评级机构，默认为2，只需在纳入统计的评级机构中任意一家即可
                                psRe1.setString(7, "债券信息维护导入债项评级信息，日期:"+lastMonthEndDay);//评级说明
                                psRe1.setString(8, lastMonthEndDay);//创建日期
                                psRe1.setString(9, lastMonthEndDay);//更新日期
                                psRe1.setString(10, lastMonthEndDay);//处理日期
                                psRe1.addBatch();
                            }

                            if(!abim.getMainRat().equals("") || abim.getMainRat() == null){
                                psRe2.setString(1, abim.getIsu());//主体名称
                                psRe2.setString(2, lastMonthEndDay);//评级日期
                                psRe2.setString(3, "1");//评级类型
                                psRe2.setString(4, abim.getMainRat());//主体类型
                                psRe2.setString(5, "2");//评级机构
                                psRe2.setString(6, "债券信息维护导入主体评级信息，日期:"+lastMonthEndDay);//评级说明
                                psRe2.setString(7, lastMonthEndDay);//创建日期
                                psRe2.setString(8, lastMonthEndDay);//更新日期
                                psRe2.setString(9, lastMonthEndDay);//处理日期
                                psRe2.addBatch();
                            }

                        }else{//资产要素维护走下面方法
                            if(StringUtils.isNotBlank(trxPla)){
                                if(StringUtils.equals(trxPla,"01")){
                                    abim.setScrId(scrId+".3.4");
                                }else if(StringUtils.equals(trxPla,"03")){
                                    abim.setScrId(scrId+".1.4");
                                }else if(StringUtils.equals(trxPla,"04")){
                                    abim.setScrId(scrId+".2.4");
                                }
                                super.update("update ods_supply_bond_bas_inf set CBND_SCD_CTG = $S{cbndScdCtg},trx_pla = $S{trxPla},spc_type = $S{spcType},MAIN_RAT = $S{mainRat},ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz},ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno},ISU_ORG_TYP_ECN = $S{isuOrgTypEcn},ISU_ORG_BLG_IDT = $S{isuOrgBlgIdt},REG_TRST_ORG = $S{regTrstOrg},version=(version+1)  where  SCR_ID = $S{scrId}", abim);
                            }
                        }
                    }
                }
                psUpdate.executeBatch();
                psRe1.executeBatch();
                psRe2.executeBatch();

            } catch (Exception e) {
                throw new Exception("债券信息维护导入异常: " + e.getMessage());
            } finally {
                psUpdate.close();
                psRe1.close();
                psRe2.close();
            }

        });

    }

    /**
     * 将wind代码转换成scr_id
     * @param windCode
     * @return
     * @throws Exception
     */
    public String windCodeToScrId (String windCode) throws Exception {
        String scrId = windCode;
        String tempStr1 = scrId.substring(0,scrId.indexOf("."));
        String tempStr2 = scrId.substring(scrId.indexOf(".")+1);
        if(StringUtils.equals(tempStr2,"SH")){
            scrId= tempStr1+".1.4";
        }else if(StringUtils.equals(tempStr2,"SZ")){
            scrId= tempStr1+".2.4";
        }else if(StringUtils.equals(tempStr2,"IB")){
            scrId= tempStr1+".3.4";
        }
        return scrId;
    }

    public List<SqlRow> findIssuCityDict(Map<String, Object> params) throws Exception {
        String sql = "select itemkey VALUE,itemval TEXT    FROM sys_dict_item   WHERE dict = 'pbc_city_area_det' ";
        if(StringUtils.isNotBlank((String) params.get("issuProvince"))){
            sql += " and substr(itemkey,1,3) =  substr($S{issuProvince},1,3) and itemval like '%市'";
        }
        sql += " ORDER BY itemkey+0 ";
        List<SqlRow> s = super.findRows(sql ,DataSourceProperty.PUB,params);
        return s;
    }
}
