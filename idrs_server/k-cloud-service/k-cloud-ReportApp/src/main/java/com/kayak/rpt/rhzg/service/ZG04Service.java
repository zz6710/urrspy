package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG04Dao;
import com.kayak.rpt.rhzg.model.ZG03;
import com.kayak.rpt.rhzg.model.ZG04;
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
@APIDefine(desc = "资管产品存续期募集信息服务", model = ZG04.class)
public class ZG04Service implements ExcelImportService<ZG04> {

    private static final Logger log = LoggerFactory.getLogger(ZG04Service.class);

    @Autowired
    private ZG04Dao zG04Dao;

    @Autowired
    private ComnDao comnDao;

    @API(desc = "查询资管产品存续期募集信息", auth = APIAuth.YES)
    public SqlResult<ZG04> findZG04s(SqlParam<ZG04> params) throws Exception {
        if (StringUtils.isBlank(params.getModel().getReportDate())) {
            SqlResult<ZG04> zg04=new SqlResult<>();
            zg04.setRows(new ArrayList<>());
            return zg04;
        }
        return zG04Dao.findZG04s(params);
    }


    @API(desc = "修改资管产品存续期募集信息",params = "id,prod_cd,theory_report_start_date,clc_source_zon_cd,clc_source_cust_typ,trans_ccy,cur_pch_amt,cur_pch_amt_cny,cur_pch_lot,cur_call_amt,cur_call_amt_cny,cur_call_lot,end_prod_amt,end_prod_amt_cny,end_prod_lot,netval_prod_end_nav,netval_prod_end_nav_cny,netval_prod_end_acm_nav,netval_prod_end_acm_nav_cny,prod_end_anl_yld,month_end_anl_yld" , auth = APIAuth.YES)
    public int updateZG04(SqlParam<ZG04> params) throws Exception {
        params.getModel().setClcSourceZonCdText(params.getModel().clcSourceZonCdText==null ? null : params.getModel().clcSourceZonCdText.split(":")[0]);
        return zG04Dao.updateZG04(params).getEffect();
    }


    @API(desc = "删除资管产品存续期募集信息", params = "id", auth = APIAuth.YES)
    public int deleteZG04(SqlParam<ZG04> params) throws Exception {
        return zG04Dao.deleteZG04(params).getEffect();
    }

    public void importFile(List<ZG04> zg04s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO `app_pbc_report_zg04`(`PROD_CD`, `report_date`, `REGISTER_STATUS`, `CLC_SOURCE_ZON_CD`, `CLC_SOURCE_CUST_TYP`, `TRANS_CCY`, `CUR_PCH_AMT`, `CUR_PCH_AMT_CNY`, `CUR_PCH_LOT`, `CUR_CALL_AMT`, `CUR_CALL_AMT_CNY`, `CUR_CALL_LOT`, `END_PROD_AMT`, `END_PROD_AMT_CNY`, `END_PROD_LOT`, `NETVAL_PROD_END_NAV`, `NETVAL_PROD_END_NAV_CNY`, `NETVAL_PROD_END_ACM_NAV`, `NETVAL_PROD_END_ACM_NAV_CNY`, `PROD_END_ANL_YLD`,month_end_anl_yld,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG04 info : zg04s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("reportDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getClcSourceZonCd()==null ? null : info.getClcSourceZonCd().split(":")[0]);
                    ps.setString(5,info.getClcSourceCustTyp()==null ? null : info.getClcSourceCustTyp().split(":")[0]);
                    ps.setString(6,info.getTransCcy());
                    ps.setString(7,info.getCurPchAmt());
                    ps.setString(8,info.getCurPchAmtCny());
                    ps.setString(9,info.getCurPchLot());
                    ps.setString(10,info.getCurCallAmt());
                    ps.setString(11,info.getCurCallAmtCny());
                    ps.setString(12,info.getCurCallLot());
                    ps.setString(13,info.getEndProdAmt());
                    ps.setString(14,info.getEndProdAmtCny());
                    ps.setString(15,info.getEndProdLot());
                    ps.setString(16,info.getNetvalProdEndNav());
                    ps.setString(17,info.getNetvalProdEndNavCny());
                    ps.setString(18,info.getNetvalProdEndAcmNav());
                    ps.setString(19,info.getNetvalProdEndAcmNavCny());
                    ps.setString(20,info.getProdEndAnlYld());
                    ps.setString(21,info.getMonthEndAnlYld());
                    ps.setString(22,"1");
                    ps.setString(23, "2");//sys_data_source 2
                    ps.setString(24, "1.0");//sys_data_version
                    ps.setString(25, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(26, DateUtil.getNowDate());// imp_date
                    ps.setString(27,map.get("reportDate").toString());
                    ps.addBatch();

                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg04s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资管产品存续期募集信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }


    public void deleteZg04ByDate(Object params) throws Exception{
        try {
            zG04Dao.deleteZG04ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    @API(desc = "获取地区动态数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> addclcSourceZonCdDict(SqlParam<ZG04> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        List<SqlRow> tempTypeByDocType = zG04Dao.addclcSourceZonCdDict(paramsDirect);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }


}
