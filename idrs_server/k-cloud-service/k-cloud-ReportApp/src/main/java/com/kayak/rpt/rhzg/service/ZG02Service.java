package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG02Dao;
import com.kayak.rpt.rhzg.model.ZG01;
import com.kayak.rpt.rhzg.model.ZG02;
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
@APIDefine(desc = "资管产品初始募集信息服务", model = ZG02.class)
public class ZG02Service implements ExcelImportService<ZG02> {

    private static final Logger log = LoggerFactory.getLogger(ZG02Service.class);

    @Autowired
    private ZG02Dao zG02Dao;

    @Autowired
    private ComnDao comnDao;



    @API(desc = "查询资管产品初始募集信息", auth = APIAuth.YES)
    public SqlResult<ZG02> findZG02s(SqlParam<ZG02> params) throws Exception {
        //params.setMakeSql(true);
        if (StringUtils.isBlank(params.getModel().getBeginDate())) {
            SqlResult<ZG02> zg02=new SqlResult<>();
            zg02.setRows(new ArrayList<>());
            return zg02;
        }
        return zG02Dao.findZG02s(params);
    }


    @API(desc = "修改资管产品初始募集信息",params = "id,prod_cd,theory_report_start_date,clc_ccy,clc_source_zon_cd,clc_source_cust_typ,clc_amt_begin,clc_amt_begin_cny,clc_lot_begin" , auth = APIAuth.YES)
    public int updateZG02(SqlParam<ZG02> params) throws Exception {
        params.getModel().setClcSourceZonCdText(params.getModel().clcSourceZonCdText==null ? null : params.getModel().clcSourceZonCdText.split(":")[0]);
        return zG02Dao.updateZG02(params).getEffect();
    }


    @API(desc = "删除资管产品初始募集信息", params = "id", auth = APIAuth.YES)
    public int deleteZG02(SqlParam<ZG02> params) throws Exception {
        return zG02Dao.deleteZG02(params).getEffect();
    }




    public void deleteZg02ByDate(Object params) throws Exception{
        try {
            zG02Dao.deleteZg02ByDate(params);
        } catch (Exception e) {
           throw e;
        }
    }



    public void importFile(List<ZG02> zg02s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg02(prod_cd,report_date,register_status,clc_ccy,clc_source_zon_cd,clc_source_cust_typ,clc_amt_begin,clc_amt_begin_cny,clc_lot_begin,unt_nav,unt_nav_cny,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,create_date,theory_report_start_date,isu_org_prod_cd) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG02 info : zg02s){

                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("reportDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getClcCcy());
                    ps.setString(5,info.getClcSourceZonCd()==null ? null : info.getClcSourceZonCd().split(":")[0]);
                    ps.setString(6,info.getClcSourceCustTyp()==null ? null : info.getClcSourceCustTyp().split(":")[0]);
                    ps.setString(7,info.getClcAmtBegin());
                    ps.setString(8,info.getClcAmtBeginCny());
                    ps.setString(9,info.getClcLotBegin());
                    ps.setString(10,info.getUntNav());
                    ps.setString(11,info.getUntNavCny());
                    ps.setString(12, "1");//sys_data_status 1
                    ps.setString(13, "2");//sys_data_source 2
                    ps.setString(14, "1.0");//sys_data_version
                    ps.setString(15, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(16, DateUtil.getNowDate());// imp_date
                    ps.setString(17, DateUtil.getNowDate());// create_date
                    ps.setString(18,DateUtil.getLastSysWordDay(map.get("reportDate").toString()) );// theory_report_start_date
                    ps.setString(19,getOrgProdCd(info.getProdCd()));// isu_org_prod_cd
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg02s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资管产品初始募集信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    @API(desc = "获取地区动态数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> addclcSourceZonCdDict(SqlParam<ZG02> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        List<SqlRow> tempTypeByDocType = zG02Dao.addclcSourceZonCdDict(paramsDirect);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    public static String getOrgProdCd(String prodCode) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select prod_cd from dwd_prd_prd_bas_inf where  pbc_cd =$S{prodCode}";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, prodCode);
            if (sqlRows != null && sqlRows.size() > 0){
                return sqlRows.get(0).getString("prod_cd");
            }else
                return null;
        }
    }




}
