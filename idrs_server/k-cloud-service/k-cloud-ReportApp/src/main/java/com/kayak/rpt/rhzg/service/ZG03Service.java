package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG03Dao;
import com.kayak.rpt.rhzg.model.ZG02;
import com.kayak.rpt.rhzg.model.ZG03;
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
@APIDefine(desc = "资管产品终止信息服务", model = ZG03.class)
public class ZG03Service implements ExcelImportService<ZG03> {

    private static final Logger log = LoggerFactory.getLogger(ZG03Service.class);

    @Autowired
    private ZG03Dao zG03Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询资管产品终止信息", auth = APIAuth.YES)
    public SqlResult<ZG03> findZG03s(SqlParam<ZG03> params) throws Exception {
        //params.setMakeSql(true);
        if (StringUtils.isBlank(params.getModel().getBeginDate())) {
            SqlResult<ZG03> zg03=new SqlResult<>();
            zg03.setRows(new ArrayList<>());
            return zg03;
        }
        return zG03Dao.findZG03s(params);
    }


    @API(desc = "修改资管产品终止信息",params = "ID,PROD_CD,theory_report_start_date,PROD_REL_END_DT,TRANS_CCY,ISU_ORG_ERN,ISU_ORG_ERN_CNY,CUST_CALL_ERN,CUST_CALL_ERN_CNY,CUST_CALL_ERN_RATE" , auth = APIAuth.YES)
    public int updateZG03(SqlParam<ZG03> params) throws Exception {
        return zG03Dao.updateZG03(params).getEffect();
    }


    @API(desc = "删除资管产品终止信息", params = "id", auth = APIAuth.YES)
    public int deleteZG03(SqlParam<ZG03> params) throws Exception {
        return zG03Dao.deleteZG03(params).getEffect();
    }


    public void importFile(List<ZG03> zg03s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg03(PROD_CD,report_date,register_status,PROD_REL_END_DT,TRANS_CCY,ISU_ORG_ERN,ISU_ORG_ERN_CNY,CUST_CALL_ERN,CUST_CALL_ERN_CNY,CUST_CALL_ERN_RATE,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG03 info : zg03s){

                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("reportDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getProdRelEndDt());
                    ps.setString(5,info.getTransCcy());
                    ps.setString(6,info.getIsuOrgErn());
                    ps.setString(7,info.getIsuOrgErnCny());
                    ps.setString(8,info.getCustCallErn());
                    ps.setString(9,info.getCustCallErnCny());
                    ps.setString(10,info.getCustCallErnRate());
                    ps.setString(11, "1");//sys_data_status 1
                    ps.setString(12, "2");//sys_data_source 2
                    ps.setString(13, "1.0");//sys_data_version
                    ps.setString(14, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(15, DateUtil.getNowDate());// imp_date
                    ps.setString(16,DateUtil.getLastSysWordDay(map.get("reportDate").toString()) );// theory_report_start_date
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg03s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资管产品终止信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg03ByDate(Object params) throws Exception{
        try {
            zG03Dao.deleteZg03ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }



}
