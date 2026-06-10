package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG09Dao;
import com.kayak.rpt.rhzg.model.ZG09;
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
@APIDefine(desc = "资产负债剩余期限信息服务", model = ZG09.class)
public class ZG09Service implements ExcelImportService<ZG09> {

    private static final Logger log = LoggerFactory.getLogger(ZG09Service.class);

    @Autowired
    private ZG09Dao zG09Dao;

    @Autowired
    private ComnDao comnDao;




    @API(desc = "查询资产负债剩余期限信息", auth = APIAuth.YES)
    public SqlResult<ZG09> findZG09s(SqlParam<ZG09> params) throws Exception {
        //params.setMakeSql(true);
        return zG09Dao.findZG09s(params);
    }


    @API(desc = "修改资产负债剩余期限信息",params = "id,isu_org_cd,theory_report_start_date,prod_cate,g000a,g000b,g000c,g000d,g000e,g100a,g100b,g100c,g100d,g100e,g200a,g200b,g200c,g200d,g200e,g300a,g300b,g300c,g300d,g300e,g400a,g400b,g400c,g400d,g400e,g500a,g500b,g500c,g500d,g500e,g600a,g600b,g600c,g600d,g600e,g700a,g700b,g700c,g700d,g700e,g800a,g800b,g800c,g800d,g800e,g0001,g0009,g000a,g000b,g000c,g000d,g000e,g100a,g100b,g100c,g100d,g100e,g200a,g200b,g200c,g200d,g200e,g300a,g300b,g300c,g300d,g300e,g400a,g400b,g400c,g400d,g400e,g500a,g500b,g500c,g500d,g500e,g600a,g600b,g600c,g600d,g600e,g700a,g700b,g700c,g700d,g700e,g800a,g800b,g800c,g800d,g800e,g0001,g0009" , auth = APIAuth.YES)
    public int updateZG09(SqlParam<ZG09> params) throws Exception {
        return zG09Dao.updateZG09(params).getEffect();
    }


    @API(desc = "删除资产负债剩余期限信息", params = "id", auth = APIAuth.YES)
    public int deleteZG09(SqlParam<ZG09> params) throws Exception {
        return zG09Dao.deleteZG09(params).getEffect();
    }


    public void importFile(List<ZG09> zg09s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg09(isu_org_cd,report_date,register_status,prod_cate,g000a,g000b,g000c,g000d,g000e,g100a,g100b,g100c,g100d,g100e,g200a,g200b,g200c,g200d,g200e,g300a,g300b,g300c,g300d,g300e,g400a,g400b,g400c,g400d,g400e,g500a,g500b,g500c,g500d,g500e,g600a,g600b,g600c,g600d,g600e,g700a,g700b,g700c,g700d,g700e,g800a,g800b,g800c,g800d,g800e,g0001,g0002,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,trust_prod) VALUES(? ,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);

            try {
                for (ZG09 info : zg09s){
                    ps.setString(1,info.getIsuOrgCd());
                    ps.setString(2,map.get("beginDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
                    ps.setString(5, StringUtils.equals(info.getG000a(),"") ? null : info.getG000a());
                    ps.setString(6, StringUtils.equals(info.getG000b(),"") ? null : info.getG000b());
                    ps.setString(7, StringUtils.equals(info.getG000c(),"") ? null : info.getG000c());
                    ps.setString(8, StringUtils.equals(info.getG000d(),"") ? null : info.getG000d());
                    ps.setString(9, StringUtils.equals(info.getG000e(),"") ? null : info.getG000e());
                    ps.setString(10, StringUtils.equals(info.getG100a(),"") ? null : info.getG100a());
                    ps.setString(11, StringUtils.equals(info.getG100b(),"") ? null : info.getG100b());
                    ps.setString(12, StringUtils.equals(info.getG100c(),"") ? null : info.getG100c());
                    ps.setString(13, StringUtils.equals(info.getG100d(),"") ? null : info.getG100d());
                    ps.setString(14, StringUtils.equals(info.getG100e(),"") ? null : info.getG100e());
                    ps.setString(15, StringUtils.equals(info.getG200a(),"") ? null : info.getG200a());
                    ps.setString(16, StringUtils.equals(info.getG200b(),"") ? null : info.getG200b());
                    ps.setString(17, StringUtils.equals(info.getG200c(),"") ? null : info.getG200c());
                    ps.setString(18, StringUtils.equals(info.getG200d(),"") ? null : info.getG200d());
                    ps.setString(19, StringUtils.equals(info.getG200e(),"") ? null : info.getG200e());
                    ps.setString(20, StringUtils.equals(info.getG300a(),"") ? null : info.getG300a());
                    ps.setString(21, StringUtils.equals(info.getG300b(),"") ? null : info.getG300b());
                    ps.setString(22, StringUtils.equals(info.getG300c(),"") ? null : info.getG300c());
                    ps.setString(23, StringUtils.equals(info.getG300d(),"") ? null : info.getG300d());
                    ps.setString(24, StringUtils.equals(info.getG300e(),"") ? null : info.getG300e());
                    ps.setString(25, StringUtils.equals(info.getG400a(),"") ? null : info.getG400a());
                    ps.setString(26, StringUtils.equals(info.getG400b(),"") ? null : info.getG400b());
                    ps.setString(27, StringUtils.equals(info.getG400c(),"") ? null : info.getG400c());
                    ps.setString(28, StringUtils.equals(info.getG400d(),"") ? null : info.getG400d());
                    ps.setString(29, StringUtils.equals(info.getG400e(),"") ? null : info.getG400e());
                    ps.setString(30, StringUtils.equals(info.getG500a(),"") ? null : info.getG500a());
                    ps.setString(31, StringUtils.equals(info.getG500b(),"") ? null : info.getG500b());
                    ps.setString(32, StringUtils.equals(info.getG500c(),"") ? null : info.getG500c());
                    ps.setString(33, StringUtils.equals(info.getG500d(),"") ? null : info.getG500d());
                    ps.setString(34, StringUtils.equals(info.getG500e(),"") ? null : info.getG500e());
                    ps.setString(35, StringUtils.equals(info.getG600a(),"") ? null : info.getG600a());
                    ps.setString(36, StringUtils.equals(info.getG600b(),"") ? null : info.getG600b());
                    ps.setString(37, StringUtils.equals(info.getG600c(),"") ? null : info.getG600c());
                    ps.setString(38, StringUtils.equals(info.getG600d(),"") ? null : info.getG600d());
                    ps.setString(39, StringUtils.equals(info.getG600e(),"") ? null : info.getG600e());
                    ps.setString(40, StringUtils.equals(info.getG700a(),"") ? null : info.getG700a());
                    ps.setString(41, StringUtils.equals(info.getG700b(),"") ? null : info.getG700b());
                    ps.setString(42, StringUtils.equals(info.getG700c(),"") ? null : info.getG700c());
                    ps.setString(43, StringUtils.equals(info.getG700d(),"") ? null : info.getG700d());
                    ps.setString(44, StringUtils.equals(info.getG700e(),"") ? null : info.getG700e());
                    ps.setString(45, StringUtils.equals(info.getG800a(),"") ? null : info.getG800a());
                    ps.setString(46, StringUtils.equals(info.getG800b(),"") ? null : info.getG800b());
                    ps.setString(47, StringUtils.equals(info.getG800c(),"") ? null : info.getG800c());
                    ps.setString(48, StringUtils.equals(info.getG800d(),"") ? null : info.getG800d());
                    ps.setString(49, StringUtils.equals(info.getG800e(),"") ? null : info.getG800e());
                    ps.setString(50, StringUtils.equals(info.getG0001(),"") ? null : info.getG0001());
                    ps.setString(51, StringUtils.equals(info.getG0002(),"") ? null : info.getG0002());
                    ps.setString(52, "1");
                    ps.setString(53, "2");//sys_data_source 2
                    ps.setString(54, "1.0");//sys_data_version
                    ps.setString(55, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(56, DateUtil.getNowDate());// imp_date
                    ps.setString(57,map.get("beginDate").toString());
                    ps.setString(58,StringUtils.equals(info.getTrustProd(),"") ? null : info.getTrustProd());
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg09s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资产负债剩余期限信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
            });


    }

    public void deleteZg09ByDate(Object params) throws Exception{
        try {
            zG09Dao.deleteZg09ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    private void solveZG09(ZG09 info) {
        info.setRegisterStatus("0");
        info.setProdCate(info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));
    }

}
