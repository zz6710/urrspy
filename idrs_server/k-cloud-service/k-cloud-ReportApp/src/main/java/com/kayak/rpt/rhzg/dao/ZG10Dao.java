package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG10;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG10Dao extends ComnDao {






    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG10> findZG10s(SqlParam<ZG10> params) throws Exception {
        String sql = "SELECT id,isu_org_cd,report_date,theory_report_start_date,prod_cate,h10000,h15000,h15100,h15200,h15300,h16000,h16100,h16200,h16300,h20000,h21000,h22000,h23000,h30000,h31000,h32000,h33000,h34000,h40000,h41000,h42000,h43000,h44000,h45000,h46000,h46100,h47000,h47100,h47200,h47300,h47400,h47500,h47600,h47700,h47800,h47900,h47a00,h48000,h50000,h51000,h52000,h53000,h54000,h55000,h5b000,h57000,h58000,h59000,h5a000,trust_prod FROM app_pbc_report_zg10 where sys_data_status ='1' ";
        if (StringUtils.isNotBlank(params.getModel().getIsuOrgCd())) {
            sql = sql + " and  isu_org_cd = '" + params.getModel().getIsuOrgCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCate())) {
            sql = sql + " and  prod_cate = '" + params.getModel().getProdCate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  id = '" + params.getModel().getId() + "'";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG10(SqlParam<ZG10> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg10 SET isu_org_cd=$S{isuOrgCd} ,theory_report_start_date=$S{theoryReportStartDate} ,prod_cate=$S{prodCate},h10000=if($S{h10000}='',null,$S{h10000}),h15000=if($S{h15000}='',null,$S{h15000}),h15100=if($S{h15100}='',null,$S{h15100}),h15200=if($S{h15200}='',null,$S{h15200}),h15300=if($S{h15300}='',null,$S{h15300}),h16000=if($S{h16000}='',null,$S{h16000}),h16100=if($S{h16100}='',null,$S{h16100}),h16200=if($S{h16200}='',null,$S{h16200}),h16300=if($S{h16300}='',null,$S{h16300}),h20000=if($S{h20000}='',null,$S{h20000}),h21000=if($S{h21000}='',null,$S{h21000}),h22000=if($S{h22000}='',null,$S{h22000}),h23000=if($S{h23000}='',null,$S{h23000}),h30000=if($S{h30000}='',null,$S{h30000}),h31000=if($S{h31000}='',null,$S{h31000}),h32000=if($S{h32000}='',null,$S{h32000}),h33000=if($S{h33000}='',null,$S{h33000}),h34000=if($S{h34000}='',null,$S{h34000}),h40000=if($S{h40000}='',null,$S{h40000}),h41000=if($S{h41000}='',null,$S{h41000}),h42000=if($S{h42000}='',null,$S{h42000}),h43000=if($S{h43000}='',null,$S{h43000}),h44000=if($S{h44000}='',null,$S{h44000}),h45000=if($S{h45000}='',null,$S{h45000}),h46000=if($S{h46000}='',null,$S{h46000}),h46100=if($S{h46100}='',null,$S{h46100}),h47000=if($S{h47000}='',null,$S{h47000}),h47100=if($S{h47100}='',null,$S{h47100}),h47200=if($S{h47200}='',null,$S{h47200}),h47300=if($S{h47300}='',null,$S{h47300}),h47400=if($S{h47400}='',null,$S{h47400}),h47500=if($S{h47500}='',null,$S{h47500}),h47600=if($S{h47600}='',null,$S{h47600}),h47700=if($S{h47700}='',null,$S{h47700}),h47800=if($S{h47800}='',null,$S{h47800}),h47900=if($S{h47900}='',null,$S{h47900}),h47a00=if($S{h47a00}='',null,$S{h47a00}),h48000=if($S{h48000}='',null,$S{h48000}),h50000=if($S{h50000}='',null,$S{h50000}),h51000=if($S{h51000}='',null,$S{h51000}),h52000=if($S{h52000}='',null,$S{h52000}),h53000=if($S{h53000}='',null,$S{h53000}),h54000=if($S{h54000}='',null,$S{h54000}),h55000=if($S{h55000}='',null,$S{h55000}),h5b000=if($S{h5b000}='',null,$S{h5b000}),h57000=if($S{h57000}='',null,$S{h57000}),h58000=if($S{h58000}='',null,$S{h58000}),h59000=if($S{h59000}='',null,$S{h59000}),h5a000=if($S{h5a000}='',null,$S{h5a000}),trust_prod=if($S{trustProd}='',null,$S{trustProd})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG10(SqlParam<ZG10> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg10 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZg10ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg10 where report_date = $S{beginDate} ", params);
    }

    public UpdateResult addZg10(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg10(isu_org_cd,theory_report_start_date,register_status,prod_cate,h10000,h11000,h11100,h11200,h11300,h12000,h12100,h12200,h12300,h13000,h13100,h13200,h13300,h14000,h14100,h14200,h14300,h20000,h21000,h22000,h23000,h30000,h31000,h32000,h33000,h34000,h40000,h41000,h42000,h43000,h44000,h45000,h46000,h46100,h47000,h47100,h47200,h47300,h47400,h47500,h47600,h47700,h47800,h47900,h47a00,h48000,h50000,h51000,h52000,h53000,h54000,h55000,h56000,h57000,h58000,h59000,h5a000) VALUES($S{isuOrgCd} ,$S{theoryReportStartDate} ,$S{registerStatus},$S{prodCate},if($S{h10000}='',null,$S{h10000}),if($S{h11000}='',null,$S{h11000}),if($S{h11100}='',null,$S{h11100}),if($S{h11200}='',null,$S{h11200}),if($S{h11300}='',null,$S{h11300}),if($S{h12000}='',null,$S{h12000}),if($S{h12100}='',null,$S{h12100}),if($S{h12200}='',null,$S{h12200}),if($S{h12300}='',null,$S{h12300}),if($S{h13000}='',null,$S{h13000}),if($S{h13100}='',null,$S{h13100}),if($S{h13200}='',null,$S{h13200}),if($S{h13300}='',null,$S{h13300}),if($S{h14000}='',null,$S{h14000}),if($S{h14100}='',null,$S{h14100}),if($S{h14200}='',null,$S{h14200}),if($S{h14300}='',null,$S{h14300}),if($S{h20000}='',null,$S{h20000}),if($S{h21000}='',null,$S{h21000}),if($S{h22000}='',null,$S{h22000}),if($S{h23000}='',null,$S{h23000}),if($S{h30000}='',null,$S{h30000}),if($S{h31000}='',null,$S{h31000}),if($S{h32000}='',null,$S{h32000}),if($S{h33000}='',null,$S{h33000}),if($S{h34000}='',null,$S{h34000}),if($S{h40000}='',null,$S{h40000}),if($S{h41000}='',null,$S{h41000}),if($S{h42000}='',null,$S{h42000}),if($S{h43000}='',null,$S{h43000}),if($S{h44000}='',null,$S{h44000}),if($S{h45000}='',null,$S{h45000}),if($S{h46000}='',null,$S{h46000}),if($S{h46100}='',null,$S{h46100}),if($S{h47000}='',null,$S{h47000}),if($S{h47100}='',null,$S{h47100}),if($S{h47200}='',null,$S{h47200}),if($S{h47300}='',null,$S{h47300}),if($S{h47400}='',null,$S{h47400}),if($S{h47500}='',null,$S{h47500}),if($S{h47600}='',null,$S{h47600}),if($S{h47700}='',null,$S{h47700}),if($S{h47800}='',null,$S{h47800}),if($S{h47900}='',null,$S{h47900}),if($S{h47a00}='',null,$S{h47a00}),if($S{h48000}='',null,$S{h48000}),if($S{h50000}='',null,$S{h50000}),if($S{h51000}='',null,$S{h51000}),if($S{h52000}='',null,$S{h52000}),if($S{h53000}='',null,$S{h53000}),if($S{h54000}='',null,$S{h54000}),if($S{h55000}='',null,$S{h55000}),if($S{h56000}='',null,$S{h56000}),if($S{h57000}='',null,$S{h57000}),if($S{h58000}='',null,$S{h58000}),if($S{h59000}='',null,$S{h59000}),if($S{h5a000}='',null,$S{h5a000}))", params);
    }




}
