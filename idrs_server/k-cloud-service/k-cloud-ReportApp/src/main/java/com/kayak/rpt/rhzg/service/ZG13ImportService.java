package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.model.ZG13;
import com.kayak.subject.model.PubReq;
import com.kayak.subject.service.RptBusinessBaseTaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@APIDefine(desc = "ZG13文件导入服务", model = ZG13.class)
public class ZG13ImportService extends RptBusinessBaseTaskService implements ExcelImportService<ZG13> {

    private static final Logger log = LoggerFactory.getLogger(ZG13ImportService.class);

    @Autowired
    private ComnDao comnDao;

    @API(desc = "删除文件导入ZG13的数据", params = "id", auth = APIAuth.YES)
    public int deleteZG13Buffer(String dealDate) throws Exception {
        String strSql = "truncate table import_zg13_buffer";
        return comnDao.update(strSql, dealDate).getEffect();
    }

    /**
     * 调用任务处理ZG13临时数据
     */
    public void callTaskDealImportZG13(String dealDate) throws Exception{
        PubReq request = new PubReq();
        request.setTaskId("M094");
        request.setTaskDate(dealDate);
        super.beforeClear(request);
        super.dataModeExConvert(request);
    }

    public void importFile(List<ZG13> ZG13s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO import_zg13_buffer(prod_cd,asset_debt_project,scr_cd,scr_org_nm,scr_org_cd,org_blg_zon,org_blg_industry,org_typ_ecn,org_typ_scale,right_invest_way,right_org_cd,right_org_nm,ccy_cd,amount,amount_cny,right_ccy_cd,right_amount,right_amount_cny,pos_rat,invest_ext_way,bgn_dt,mtu_dt,defer_mtu_dt,report_date,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date) VALUES(? ,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (ZG13 info : ZG13s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,info.getAssetDebtProject());
                    ps.setString(3,info.getScrCd());
                    ps.setString(4,info.getScrOrgNm());
                    ps.setString(5,info.getScrOrgCd());
                    ps.setString(6,info.getOrgBlgZon());
                    ps.setString(7,info.getOrgBlgIndustry());
                    ps.setString(8,info.getOrgTypEcn());
                    ps.setString(9,info.getOrgTypScale());
                    ps.setString(10,info.getRightInvestWay());
                    ps.setString(11,info.getRightOrgCd());
                    ps.setString(12,info.getRightOrgNm());
                    ps.setString(13,info.getCcyCd());
                    ps.setString(14,StringUtils.equals(info.getAmount(),"") ? null : info.getAmount());
                    ps.setString(15,StringUtils.equals(info.getAmountCny(),"") ? null : info.getAmountCny());
                    ps.setString(16,info.getRightCcyCd());
                    ps.setString(17,StringUtils.equals(info.getRightAmount(),"") ? null : info.getRightAmount());
                    ps.setString(18,StringUtils.equals(info.getRightAmountCny(),"") ? null : info.getRightAmountCny());
                    ps.setString(19,StringUtils.equals(info.getPosRat(),"") ? null : info.getPosRat());
                    ps.setString(20,info.getInvestExtWay());
                    ps.setString(21,info.getBgnDt());
                    ps.setString(22,info.getMtuDt());
                    ps.setString(23,info.getDeferMtuDt());
                    ps.setString(24,map.get("dealDate").toString());
                    ps.setString(25,"1");
                    ps.setString(26, "2");//sys_data_source 2
                    ps.setString(27, "1.0");//sys_data_version
                    ps.setString(28, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(29, DateUtil.getNowDate());// imp_date
                    ps.setString(30,map.get("dealDate").toString());
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库import_zg13_buffer 数量：{}耗时: {} ms", ZG13s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入import_zg13_buffer临时表异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
            });


    }
}
