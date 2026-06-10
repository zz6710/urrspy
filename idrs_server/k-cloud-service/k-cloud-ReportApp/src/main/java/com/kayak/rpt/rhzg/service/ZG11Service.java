package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG11Dao;
import com.kayak.rpt.rhzg.model.ZG11;
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
@APIDefine(desc = "企业债券分行业和企业规模情况信息服务", model = ZG11.class)
public class ZG11Service implements ExcelImportService<ZG11> {

    private static final Logger log = LoggerFactory.getLogger(ZG11Service.class);

    @Autowired
    private ZG11Dao zG11Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询企业债券分行业和企业规模情况信息", auth = APIAuth.YES)
    public SqlResult<ZG11> findZG11s(SqlParam<ZG11> params) throws Exception {
        //params.setMakeSql(true);
        return zG11Dao.findZG11s(params);
    }


    @API(desc = "修改企业债券分行业和企业规模情况信息",params = "id,isu_org_cd,theory_report_start_date,prod_cate,c00000,c01000,c11000,c03000,c04000,c10000,c11000,c12000,c13000,c14000,c20000,c21000,c22000,c23000,c24000,c30000,c31000,c32000,c33000,c34000,c40000,c41000,c42000,c43000,c44000,c50000,c51000,c52000,c53000,c54000,c60000,c61000,c62000,c63000,c64000,c70000,c71000,c72000,c73000,c74000,c80000,c81000,c82000,c83000,c84000,c90000,c91000,c92000,c93000,c94000,ca0000,ca1000,ca2000,ca3000,ca4000,cb0000,cb1000,cb2000,cb3000,cb4000,cc0000,cc1000,cc2000,cc3000,cc4000,cd0000,cd1000,cd2000,cd3000,cd4000,ce0000,ce1000,ce2000,ce3000,ce4000,cf0000,cf1000,cf2000,cf3000,cf4000,cg0000,cg1000,cg2000,cg3000,cg4000,ch0000,ch1000,ch2000,ch3000,ch4000,ci0000,ci1000,ci2000,ci3000,ci4000,cj0000,cj1000,cj2000,cj3000,cj4000" , auth = APIAuth.YES)
    public int updateZG11(SqlParam<ZG11> params) throws Exception {
        return zG11Dao.updateZG11(params).getEffect();
    }


    @API(desc = "删除企业债券分行业和企业规模情况信息", params = "id", auth = APIAuth.YES)
    public int deleteZG11(SqlParam<ZG11> params) throws Exception {
        return zG11Dao.deleteZG11(params).getEffect();
    }


    public void importFile(List<ZG11> zg11s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg11(isu_org_cd,report_date,register_status,prod_cate,c00000,c01000,c02000,c03000,c04000,c10000,c11000,c12000,c13000,c14000,c20000,c21000,c22000,c23000,c24000,c30000,c31000,c32000,c33000,c34000,c40000,c41000,c42000,c43000,c44000,c50000,c51000,c52000,c53000,c54000,c60000,c61000,c62000,c63000,c64000,c70000,c71000,c72000,c73000,c74000,c80000,c81000,c82000,c83000,c84000,c90000,c91000,c92000,c93000,c94000,ca0000,ca1000,ca2000,ca3000,ca4000,cb0000,cb1000,cb2000,cb3000,cb4000,cc0000,cc1000,cc2000,cc3000,cc4000,cd0000,cd1000,cd2000,cd3000,cd4000,ce0000,ce1000,ce2000,ce3000,ce4000,cf0000,cf1000,cf2000,cf3000,cf4000,cg0000,cg1000,cg2000,cg3000,cg4000,ch0000,ch1000,ch2000,ch3000,ch4000,ci0000,ci1000,ci2000,ci3000,ci4000,cj0000,cj1000,cj2000,cj3000,cj4000,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,trust_prod) VALUES(? ,? ,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (ZG11 info : zg11s){
                    ps.setString(1,info.getIsuOrgCd());
                    ps.setString(2,map.get("beginDate").toString());
                    ps.setString(3,"1");
                    ps.setString(4,info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
                    ps.setString(5, StringUtils.equals(info.getC00000(),"") ? null : info.getC00000());
                    ps.setString(6, StringUtils.equals(info.getC01000(),"") ? null : info.getC01000());
                    ps.setString(7, StringUtils.equals(info.getC02000(),"") ? null : info.getC02000());
                    ps.setString(8, StringUtils.equals(info.getC03000(),"") ? null : info.getC03000());
                    ps.setString(9, StringUtils.equals(info.getC04000(),"") ? null : info.getC04000());
                    ps.setString(10, StringUtils.equals(info.getC10000(),"") ? null : info.getC10000());
                    ps.setString(11, StringUtils.equals(info.getC11000(),"") ? null : info.getC11000());
                    ps.setString(12, StringUtils.equals(info.getC12000(),"") ? null : info.getC12000());
                    ps.setString(13, StringUtils.equals(info.getC13000(),"") ? null : info.getC13000());
                    ps.setString(14, StringUtils.equals(info.getC14000(),"") ? null : info.getC14000());
                    ps.setString(15, StringUtils.equals(info.getC20000(),"") ? null : info.getC20000());
                    ps.setString(16, StringUtils.equals(info.getC21000(),"") ? null : info.getC21000());
                    ps.setString(17, StringUtils.equals(info.getC22000(),"") ? null : info.getC22000());
                    ps.setString(18, StringUtils.equals(info.getC23000(),"") ? null : info.getC23000());
                    ps.setString(19, StringUtils.equals(info.getC24000(),"") ? null : info.getC24000());
                    ps.setString(20, StringUtils.equals(info.getC30000(),"") ? null : info.getC30000());
                    ps.setString(21, StringUtils.equals(info.getC31000(),"") ? null : info.getC31000());
                    ps.setString(22, StringUtils.equals(info.getC32000(),"") ? null : info.getC32000());
                    ps.setString(23, StringUtils.equals(info.getC33000(),"") ? null : info.getC33000());
                    ps.setString(24, StringUtils.equals(info.getC34000(),"") ? null : info.getC34000());
                    ps.setString(25, StringUtils.equals(info.getC40000(),"") ? null : info.getC40000());
                    ps.setString(26, StringUtils.equals(info.getC41000(),"") ? null : info.getC41000());
                    ps.setString(27, StringUtils.equals(info.getC42000(),"") ? null : info.getC42000());
                    ps.setString(28, StringUtils.equals(info.getC43000(),"") ? null : info.getC43000());
                    ps.setString(29, StringUtils.equals(info.getC44000(),"") ? null : info.getC44000());
                    ps.setString(30, StringUtils.equals(info.getC50000(),"") ? null : info.getC50000());
                    ps.setString(31, StringUtils.equals(info.getC51000(),"") ? null : info.getC51000());
                    ps.setString(32, StringUtils.equals(info.getC52000(),"") ? null : info.getC52000());
                    ps.setString(33, StringUtils.equals(info.getC53000(),"") ? null : info.getC53000());
                    ps.setString(34, StringUtils.equals(info.getC54000(),"") ? null : info.getC54000());
                    ps.setString(35, StringUtils.equals(info.getC60000(),"") ? null : info.getC60000());
                    ps.setString(36, StringUtils.equals(info.getC61000(),"") ? null : info.getC61000());
                    ps.setString(37, StringUtils.equals(info.getC62000(),"") ? null : info.getC62000());
                    ps.setString(38, StringUtils.equals(info.getC63000(),"") ? null : info.getC63000());
                    ps.setString(39, StringUtils.equals(info.getC64000(),"") ? null : info.getC64000());
                    ps.setString(40, StringUtils.equals(info.getC70000(),"") ? null : info.getC70000());
                    ps.setString(41, StringUtils.equals(info.getC71000(),"") ? null : info.getC71000());
                    ps.setString(42, StringUtils.equals(info.getC72000(),"") ? null : info.getC72000());
                    ps.setString(43, StringUtils.equals(info.getC73000(),"") ? null : info.getC73000());
                    ps.setString(44, StringUtils.equals(info.getC74000(),"") ? null : info.getC74000());
                    ps.setString(45, StringUtils.equals(info.getC80000(),"") ? null : info.getC80000());
                    ps.setString(46, StringUtils.equals(info.getC81000(),"") ? null : info.getC81000());
                    ps.setString(47, StringUtils.equals(info.getC82000(),"") ? null : info.getC82000());
                    ps.setString(48, StringUtils.equals(info.getC83000(),"") ? null : info.getC83000());
                    ps.setString(49, StringUtils.equals(info.getC84000(),"") ? null : info.getC84000());
                    ps.setString(50, StringUtils.equals(info.getC90000(),"") ? null : info.getC90000());
                    ps.setString(51, StringUtils.equals(info.getC91000(),"") ? null : info.getC91000());
                    ps.setString(52, StringUtils.equals(info.getC92000(),"") ? null : info.getC92000());
                    ps.setString(53, StringUtils.equals(info.getC93000(),"") ? null : info.getC93000());
                    ps.setString(54, StringUtils.equals(info.getC94000(),"") ? null : info.getC94000());
                    ps.setString(55, StringUtils.equals(info.getCa0000(),"") ? null : info.getCa0000());
                    ps.setString(56, StringUtils.equals(info.getCa1000(),"") ? null : info.getCa1000());
                    ps.setString(57, StringUtils.equals(info.getCa2000(),"") ? null : info.getCa2000());
                    ps.setString(58, StringUtils.equals(info.getCa3000(),"") ? null : info.getCa3000());
                    ps.setString(59, StringUtils.equals(info.getCa4000(),"") ? null : info.getCa4000());
                    ps.setString(60, StringUtils.equals(info.getCb0000(),"") ? null : info.getCb0000());
                    ps.setString(61, StringUtils.equals(info.getCb1000(),"") ? null : info.getCb1000());
                    ps.setString(62, StringUtils.equals(info.getCb2000(),"") ? null : info.getCb2000());
                    ps.setString(63, StringUtils.equals(info.getCb3000(),"") ? null : info.getCb3000());
                    ps.setString(64, StringUtils.equals(info.getCb4000(),"") ? null : info.getCb4000());
                    ps.setString(65, StringUtils.equals(info.getCc0000(),"") ? null : info.getCc0000());
                    ps.setString(66, StringUtils.equals(info.getCc1000(),"") ? null : info.getCc1000());
                    ps.setString(67, StringUtils.equals(info.getCc2000(),"") ? null : info.getCc2000());
                    ps.setString(68, StringUtils.equals(info.getCc3000(),"") ? null : info.getCc3000());
                    ps.setString(69, StringUtils.equals(info.getCc4000(),"") ? null : info.getCc4000());
                    ps.setString(70, StringUtils.equals(info.getCd0000(),"") ? null : info.getCd0000());
                    ps.setString(71, StringUtils.equals(info.getCd1000(),"") ? null : info.getCd1000());
                    ps.setString(72, StringUtils.equals(info.getCd2000(),"") ? null : info.getCd2000());
                    ps.setString(73, StringUtils.equals(info.getCd3000(),"") ? null : info.getCd3000());
                    ps.setString(74, StringUtils.equals(info.getCd4000(),"") ? null : info.getCd4000());
                    ps.setString(75, StringUtils.equals(info.getCe0000(),"") ? null : info.getCe0000());
                    ps.setString(76, StringUtils.equals(info.getCe1000(),"") ? null : info.getCe1000());
                    ps.setString(77, StringUtils.equals(info.getCe2000(),"") ? null : info.getCe2000());
                    ps.setString(78, StringUtils.equals(info.getCe3000(),"") ? null : info.getCe3000());
                    ps.setString(79, StringUtils.equals(info.getCe4000(),"") ? null : info.getCe4000());
                    ps.setString(80, StringUtils.equals(info.getCf0000(),"") ? null : info.getCf0000());
                    ps.setString(81, StringUtils.equals(info.getCf1000(),"") ? null : info.getCf1000());
                    ps.setString(82, StringUtils.equals(info.getCf2000(),"") ? null : info.getCf2000());
                    ps.setString(83, StringUtils.equals(info.getCf3000(),"") ? null : info.getCf3000());
                    ps.setString(84, StringUtils.equals(info.getCf4000(),"") ? null : info.getCf4000());
                    ps.setString(85, StringUtils.equals(info.getCg0000(),"") ? null : info.getCg0000());
                    ps.setString(86, StringUtils.equals(info.getCg1000(),"") ? null : info.getCg1000());
                    ps.setString(87, StringUtils.equals(info.getCg2000(),"") ? null : info.getCg2000());
                    ps.setString(88, StringUtils.equals(info.getCg3000(),"") ? null : info.getCg3000());
                    ps.setString(89, StringUtils.equals(info.getCg4000(),"") ? null : info.getCg4000());
                    ps.setString(90, StringUtils.equals(info.getCh0000(),"") ? null : info.getCh0000());
                    ps.setString(91, StringUtils.equals(info.getCh1000(),"") ? null : info.getCh1000());
                    ps.setString(92, StringUtils.equals(info.getCh2000(),"") ? null : info.getCh2000());
                    ps.setString(93, StringUtils.equals(info.getCh3000(),"") ? null : info.getCh3000());
                    ps.setString(94, StringUtils.equals(info.getCh4000(),"") ? null : info.getCh4000());
                    ps.setString(95, StringUtils.equals(info.getCi0000(),"") ? null : info.getCi0000());
                    ps.setString(96, StringUtils.equals(info.getCi1000(),"") ? null : info.getCi0000());
                    ps.setString(97, StringUtils.equals(info.getCi2000(),"") ? null : info.getCi2000());
                    ps.setString(98, StringUtils.equals(info.getCi3000(),"") ? null : info.getCi3000());
                    ps.setString(99, StringUtils.equals(info.getCi4000(),"") ? null : info.getCi4000());
                    ps.setString(100, StringUtils.equals(info.getCj0000(),"") ? null : info.getCj0000());
                    ps.setString(101, StringUtils.equals(info.getCj1000(),"") ? null : info.getCj1000());
                    ps.setString(102, StringUtils.equals(info.getCj2000(),"") ? null : info.getCj2000());
                    ps.setString(103, StringUtils.equals(info.getCj3000(),"") ? null : info.getCj3000());
                    ps.setString(104, StringUtils.equals(info.getCj4000(),"") ? null : info.getCj4000());
                    ps.setString(105,"1");
                    ps.setString(106, "2");//sys_data_source 2
                    ps.setString(107, "1.0");//sys_data_version
                    ps.setString(108, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(109, DateUtil.getNowDate());// imp_date
                    ps.setString(110,map.get("beginDate").toString());
                    ps.setString(111, StringUtils.equals(info.getTrustProd(),"") ? null : info.getTrustProd());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", zg11s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入企业债券分行业和企业规模情况信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }


    public void deleteZg11ByDate(Object params) throws Exception{
        try {
            zG11Dao.deleteZg11ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }
    
    private void solveZG11(ZG11 info) {
        info.setRegisterStatus("0");
        info.setProdCate(info.getProdCate()==null ? null : info.getProdCate().split(":")[0]);
        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));
    }

}
