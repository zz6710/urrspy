package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG10Dao;
import com.kayak.rpt.rhzg.model.ZG10;
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
@APIDefine(desc = "债券等资产配置情况信息服务", model = ZG10.class)
public class ZG10Service implements ExcelImportService<ZG10> {

    private static final Logger log = LoggerFactory.getLogger(ZG10Service.class);

    @Autowired
    private ZG10Dao zG10Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询债券等资产配置情况信息", auth = APIAuth.YES)
    public SqlResult<ZG10> findZG10s(SqlParam<ZG10> params) throws Exception {
        //params.setMakeSql(true);
        return zG10Dao.findZG10s(params);
    }


    @API(desc = "修改债券等资产配置情况信息",params = "id,isu_org_cd,theory_report_start_date,prod_cate,h10000,h15000,h15100,h15200,h15300,h16000,h16100,h16200,h16300,h20000,h21000,h22000,h23000,h30000,h31000,h32000,h33000,h34000,h40000,h41000,h42000,h43000,h44000,h45000,h46000,h46100,h47000,h47100,h47200,h47300,h47400,h47500,h47600,h47700,h47800,h47900,h47a00,h48000,h50000,h51000,h52000,h53000,h54000,h55000,h56000,h57000,h58000,h59000,h5a000" , auth = APIAuth.YES)
    public int updateZG10(SqlParam<ZG10> params) throws Exception {
        return zG10Dao.updateZG10(params).getEffect();
    }


    @API(desc = "删除债券等资产配置情况信息", params = "id", auth = APIAuth.YES)
    public int deleteZG10(SqlParam<ZG10> params) throws Exception {
        return zG10Dao.deleteZG10(params).getEffect();
    }

    public void importFile(List<ZG10> zg10s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg10(ISU_ORG_CD,REPORT_DATE,PROD_CATE,h10000,h15000,h15100,h15200,h15300,h16000,h16100,h16200,h16300,h20000,h21000,h22000,h23000,h30000,h31000,h32000,h33000,h34000,h40000,h41000,h42000,h43000,h44000,h45000,h46000,h46100,h47000,h47100,h47200,h47300,h47400,h47500,h47600,h47700,h47800,h47900,h47a00,h48000,h50000,h51000,h52000,h53000,h54000,h55000,h5b000,h57000,h58000,h59000,h5a000,REGISTER_STATUS,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,trust_prod) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG10 info : zg10s){
                    ps.setString(1,info.getIsuOrgCd());
                    ps.setString(2,map.get("beginDate").toString());
                    ps.setString(3,info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
                    ps.setString(4, StringUtils.equals(info.getH10000(),"") ? null : info.getH10000());
                    ps.setString(5, StringUtils.equals(info.getH15000(),"") ? null : info.getH15000());
                    ps.setString(6, StringUtils.equals(info.getH15100(),"") ? null : info.getH15100());
                    ps.setString(7, StringUtils.equals(info.getH15200(),"") ? null : info.getH15200());
                    ps.setString(8, StringUtils.equals(info.getH15300(),"") ? null : info.getH15300());
                    ps.setString(9, StringUtils.equals(info.getH16000(),"") ? null : info.getH16000());
                    ps.setString(10, StringUtils.equals(info.getH16100(),"") ? null : info.getH16100());
                    ps.setString(11, StringUtils.equals(info.getH16200(),"") ? null : info.getH16200());
                    ps.setString(12, StringUtils.equals(info.getH16300(),"") ? null : info.getH16300());
                    ps.setString(13, StringUtils.equals(info.getH20000(),"") ? null : info.getH20000());
                    ps.setString(14, StringUtils.equals(info.getH21000(),"") ? null : info.getH21000());
                    ps.setString(15, StringUtils.equals(info.getH22000(),"") ? null : info.getH22000());
                    ps.setString(16, StringUtils.equals(info.getH23000(),"") ? null : info.getH23000());
                    ps.setString(17, StringUtils.equals(info.getH30000(),"") ? null : info.getH30000());
                    ps.setString(18, StringUtils.equals(info.getH31000(),"") ? null : info.getH31000());
                    ps.setString(19, StringUtils.equals(info.getH32000(),"") ? null : info.getH32000());
                    ps.setString(20, StringUtils.equals(info.getH33000(),"") ? null : info.getH33000());
                    ps.setString(21, StringUtils.equals(info.getH34000(),"") ? null : info.getH34000());
                    ps.setString(22, StringUtils.equals(info.getH40000(),"") ? null : info.getH40000());
                    ps.setString(23, StringUtils.equals(info.getH41000(),"") ? null : info.getH41000());
                    ps.setString(24, StringUtils.equals(info.getH42000(),"") ? null : info.getH42000());
                    ps.setString(25, StringUtils.equals(info.getH43000(),"") ? null : info.getH43000());
                    ps.setString(26, StringUtils.equals(info.getH44000(),"") ? null : info.getH44000());
                    ps.setString(27, StringUtils.equals(info.getH45000(),"") ? null : info.getH45000());
                    ps.setString(28, StringUtils.equals(info.getH46000(),"") ? null : info.getH46000());
                    ps.setString(29, StringUtils.equals(info.getH46100(),"") ? null : info.getH46100());
                    ps.setString(30, StringUtils.equals(info.getH47000(),"") ? null : info.getH47000());
                    ps.setString(31, StringUtils.equals(info.getH47100(),"") ? null : info.getH47100());
                    ps.setString(32, StringUtils.equals(info.getH47200(),"") ? null : info.getH47200());
                    ps.setString(33, StringUtils.equals(info.getH47300(),"") ? null : info.getH47300());
                    ps.setString(34, StringUtils.equals(info.getH47400(),"") ? null : info.getH47400());
                    ps.setString(35, StringUtils.equals(info.getH47500(),"") ? null : info.getH47500());
                    ps.setString(36, StringUtils.equals(info.getH47600(),"") ? null : info.getH47600());
                    ps.setString(37, StringUtils.equals(info.getH47700(),"") ? null : info.getH47700());
                    ps.setString(38, StringUtils.equals(info.getH47800(),"") ? null : info.getH47800());
                    ps.setString(39, StringUtils.equals(info.getH47900(),"") ? null : info.getH47900());
                    ps.setString(40, StringUtils.equals(info.getH47a00(),"") ? null : info.getH47a00());
                    ps.setString(41, StringUtils.equals(info.getH48000(),"") ? null : info.getH48000());
                    ps.setString(42, StringUtils.equals(info.getH50000(),"") ? null : info.getH50000());
                    ps.setString(43, StringUtils.equals(info.getH51000(),"") ? null : info.getH51000());
                    ps.setString(44, StringUtils.equals(info.getH52000(),"") ? null : info.getH52000());
                    ps.setString(45, StringUtils.equals(info.getH53000(),"") ? null : info.getH53000());
                    ps.setString(46, StringUtils.equals(info.getH54000(),"") ? null : info.getH54000());
                    ps.setString(47, StringUtils.equals(info.getH55000(),"") ? null : info.getH55000());
                    ps.setString(48, StringUtils.equals(info.getH5b000(),"") ? null : info.getH5b000());
                    ps.setString(49, StringUtils.equals(info.getH57000(),"") ? null : info.getH57000());
                    ps.setString(50, StringUtils.equals(info.getH58000(),"") ? null : info.getH58000());
                    ps.setString(51, StringUtils.equals(info.getH59000(),"") ? null : info.getH59000());
                    ps.setString(52, StringUtils.equals(info.getH5a000(),"") ? null : info.getH5a000());
                    ps.setString(53, "1");
                    ps.setString(54,"1");
                    ps.setString(55, "2");//sys_data_source 2
                    ps.setString(56, "1.0");//sys_data_version
                    ps.setString(57, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(58, DateUtil.getNowDate());// imp_date
                    ps.setString(59,map.get("beginDate").toString());
                    ps.setString(60, StringUtils.equals(info.getTrustProd(),"") ? null : info.getTrustProd());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", zg10s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入资产负债剩余期限信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg10ByDate(Object params) throws Exception{
        try {
            zG10Dao.deleteZg10ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    private void solveZG10(ZG10 info) {
        info.setRegisterStatus("0");
        info.setProdCate(info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));
    }

}
