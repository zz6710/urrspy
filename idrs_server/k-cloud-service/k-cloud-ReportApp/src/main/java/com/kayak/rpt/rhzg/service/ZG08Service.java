package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG08Dao;
import com.kayak.rpt.rhzg.model.ZG08;
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
@APIDefine(desc = "特定目的载体交易对手明细信息服务", model = ZG08.class)
public class ZG08Service implements ExcelImportService<ZG08> {

    private static final Logger log = LoggerFactory.getLogger(ZG08Service.class);

    @Autowired
    private ZG08Dao zG08Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询特定目的载体交易对手明细信息", auth = APIAuth.YES)
    public SqlResult<ZG08> findZG08s(SqlParam<ZG08> params) throws Exception {
        //params.setMakeSql(true);
        return zG08Dao.findZG08s(params);
    }


    @API(desc = "修改特定目的载体交易对手明细信息",params = "ID,PROD_CD,theory_report_start_date,ASSET_DEBT_PROJECT,COUNTERPART_PROD_TYP,COUNTERPART_ORG_CD,COUNTERPART_PROD_CD,CCY,END_DT_AMT,END_DT_AMT_CNY" , auth = APIAuth.YES)
    public int updateZG08(SqlParam<ZG08> params) throws Exception {
        return zG08Dao.updateZG08(params).getEffect();
    }


    @API(desc = "删除特定目的载体交易对手明细信息", params = "id", auth = APIAuth.YES)
    public int deleteZG08(SqlParam<ZG08> params) throws Exception {
        return zG08Dao.deleteZG08(params).getEffect();
    }


    public void importFile(List<ZG08> zg08s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg08(PROD_CD,report_date,register_status,ASSET_DEBT_PROJECT,COUNTERPART_PROD_TYP,COUNTERPART_ORG_CD,COUNTERPART_PROD_CD,CCY,END_DT_AMT,END_DT_AMT_CNY,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG08 info : zg08s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("beginDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getAssetDebtProject()==null ? null : info.getAssetDebtProject().split(":")[0]);
                    ps.setString(5,info.getCounterpartProdTyp()==null ? null : info.getCounterpartProdTyp().split(":")[0]);
                    ps.setString(6,info.getCounterpartOrgCd());
                    ps.setString(7,info.getCounterpartProdCd());
                    ps.setString(8,info.getCcy());
                    ps.setString(9,info.getEndDtAmt());
                    ps.setString(10,info.getEndDtAmtCny());
                    ps.setString(11,"1");
                    ps.setString(12, "2");//sys_data_source 2
                    ps.setString(13, "1.0");//sys_data_version
                    ps.setString(14, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(15, DateUtil.getNowDate());// imp_date
                    ps.setString(16,map.get("beginDate").toString());
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg08s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入特定目的载体交易对手明细信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg08ByDate(Object params) throws Exception{
        try {
            zG08Dao.deleteZg08ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    private void solveZG08(ZG08 info) {
        info.setRegisterStatus("0");
        info.setAssetDebtProject(info.getAssetDebtProject()==null ? null : info.getAssetDebtProject().split(":")[0]);
        info.setCounterpartProdTyp(info.getCounterpartProdTyp()==null ? null : info.getCounterpartProdTyp().split(":")[0]);
        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));

    }

}
