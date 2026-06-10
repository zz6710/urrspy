package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG11;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG11Dao extends ComnDao {




    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG11> findZG11s(SqlParam<ZG11> params) throws Exception {
        String sql = "SELECT id,isu_org_cd,report_date,theory_report_start_date,prod_cate,c00000,c01000,c02000,c03000,c04000,c10000,c11000,c12000,c13000,c14000,c20000,c21000,c22000,c23000,c24000,c30000,c31000,c32000,c33000,c34000,c40000,c41000,c42000,c43000,c44000,c50000,c51000,c52000,c53000,c54000,c60000,c61000,c62000,c63000,c64000,c70000,c71000,c72000,c73000,c74000,c80000,c81000,c82000,c83000,c84000,c90000,c91000,c92000,c93000,c94000,ca0000,ca1000,ca2000,ca3000,ca4000,cb0000,cb1000,cb2000,cb3000,cb4000,cc0000,cc1000,cc2000,cc3000,cc4000,cd0000,cd1000,cd2000,cd3000,cd4000,ce0000,ce1000,ce2000,ce3000,ce4000,cf0000,cf1000,cf2000,cf3000,cf4000,cg0000,cg1000,cg2000,cg3000,cg4000,ch0000,ch1000,ch2000,ch3000,ch4000,ci0000,ci1000,ci2000,ci3000,ci4000,cj0000,cj1000,cj2000,cj3000,cj4000,trust_prod FROM app_pbc_report_zg11 where sys_data_status ='1' ";
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

    public UpdateResult updateZG11(SqlParam<ZG11> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg11 SET isu_org_cd=$S{isuOrgCd} ,theory_report_start_date=$S{theoryReportStartDate} ,prod_cate=$S{prodCate},c00000=if($S{c00000}='',null,$S{c00000}),c01000=if($S{c01000}='',null,$S{c01000}),c02000=if($S{c02000}='',null,$S{c02000}),c03000=if($S{c03000}='',null,$S{c03000}),c04000=if($S{c04000}='',null,$S{c04000}),c10000=if($S{c10000}='',null,$S{c10000}),c11000=if($S{c11000}='',null,$S{c11000}),c12000=if($S{c12000}='',null,$S{c12000}),c13000=if($S{c13000}='',null,$S{c13000}),c14000=if($S{c14000}='',null,$S{c14000}),c20000=if($S{c20000}='',null,$S{c20000}),c21000=if($S{c21000}='',null,$S{c21000}),c22000=if($S{c22000}='',null,$S{c22000}),c23000=if($S{c23000}='',null,$S{c23000}),c24000=if($S{c24000}='',null,$S{c24000}),c30000=if($S{c30000}='',null,$S{c30000}),c31000=if($S{c31000}='',null,$S{c31000}),c32000=if($S{c32000}='',null,$S{c32000}),c33000=if($S{c33000}='',null,$S{c33000}),c34000=if($S{c34000}='',null,$S{c34000}),c40000=if($S{c40000}='',null,$S{c40000}),c41000=if($S{c41000}='',null,$S{c41000}),c42000=if($S{c42000}='',null,$S{c42000}),c43000=if($S{c43000}='',null,$S{c43000}),c44000=if($S{c44000}='',null,$S{c44000}),c50000=if($S{c50000}='',null,$S{c50000}),c51000=if($S{c51000}='',null,$S{c51000}),c52000=if($S{c52000}='',null,$S{c52000}),c53000=if($S{c53000}='',null,$S{c53000}),c54000=if($S{c54000}='',null,$S{c54000}),c60000=if($S{c60000}='',null,$S{c60000}),c61000=if($S{c61000}='',null,$S{c61000}),c62000=if($S{c62000}='',null,$S{c62000}),c63000=if($S{c63000}='',null,$S{c63000}),c64000=if($S{c64000}='',null,$S{c64000}),c70000=if($S{c70000}='',null,$S{c70000}),c71000=if($S{c71000}='',null,$S{c71000}),c72000=if($S{c72000}='',null,$S{c72000}),c73000=if($S{c73000}='',null,$S{c73000}),c74000=if($S{c74000}='',null,$S{c74000}),c80000=if($S{c80000}='',null,$S{c80000}),c81000=if($S{c81000}='',null,$S{c81000}),c82000=if($S{c82000}='',null,$S{c82000}),c83000=if($S{c83000}='',null,$S{c83000}),c84000=if($S{c84000}='',null,$S{c84000}),c90000=if($S{c90000}='',null,$S{c90000}),c91000=if($S{c91000}='',null,$S{c91000}),c92000=if($S{c92000}='',null,$S{c92000}),c93000=if($S{c93000}='',null,$S{c93000}),c94000=if($S{c94000}='',null,$S{c94000}),ca0000=if($S{ca0000}='',null,$S{ca0000}),ca1000=if($S{ca1000}='',null,$S{ca1000}),ca2000=if($S{ca2000}='',null,$S{ca2000}),ca3000=if($S{ca3000}='',null,$S{ca3000}),ca4000=if($S{ca4000}='',null,$S{ca4000}),cb0000=if($S{cb0000}='',null,$S{cb0000}),cb1000=if($S{cb1000}='',null,$S{cb1000}),cb2000=if($S{cb2000}='',null,$S{cb2000}),cb3000=if($S{cb3000}='',null,$S{cb3000}),cb4000=if($S{cb4000}='',null,$S{cb4000}),cc0000=if($S{cc0000}='',null,$S{cc0000}),cc1000=if($S{cc1000}='',null,$S{cc1000}),cc2000=if($S{cc2000}='',null,$S{cc2000}),cc3000=if($S{cc3000}='',null,$S{cc3000}),cc4000=if($S{cc4000}='',null,$S{cc4000}),cd0000=if($S{cd0000}='',null,$S{cd0000}),cd1000=if($S{cd1000}='',null,$S{cd1000}),cd2000=if($S{cd2000}='',null,$S{cd2000}),cd3000=if($S{cd3000}='',null,$S{cd3000}),cd4000=if($S{cd4000}='',null,$S{cd4000}),ce0000=if($S{ce0000}='',null,$S{ce0000}),ce1000=if($S{ce1000}='',null,$S{ce1000}),ce2000=if($S{ce2000}='',null,$S{ce2000}),ce3000=if($S{ce3000}='',null,$S{ce3000}),ce4000=if($S{ce4000}='',null,$S{ce4000}),cf0000=if($S{cf0000}='',null,$S{cf0000}),cf1000=if($S{cf1000}='',null,$S{cf1000}),cf2000=if($S{cf2000}='',null,$S{cf2000}),cf3000=if($S{cf3000}='',null,$S{cf3000}),cf4000=if($S{cf4000}='',null,$S{cf4000}),cg0000=if($S{cg0000}='',null,$S{cg0000}),cg1000=if($S{cg1000}='',null,$S{cg1000}),cg2000=if($S{cg2000}='',null,$S{cg2000}),cg3000=if($S{cg3000}='',null,$S{cg3000}),cg4000=if($S{cg4000}='',null,$S{cg4000}),ch0000=if($S{ch0000}='',null,$S{ch0000}),ch1000=if($S{ch1000}='',null,$S{ch1000}),ch2000=if($S{ch2000}='',null,$S{ch2000}),ch3000=if($S{ch3000}='',null,$S{ch3000}),ch4000=if($S{ch4000}='',null,$S{ch4000}),ci0000=if($S{ci0000}='',null,$S{ci0000}),ci1000=if($S{ci1000}='',null,$S{ci1000}),ci2000=if($S{ci2000}='',null,$S{ci2000}),ci3000=if($S{ci3000}='',null,$S{ci3000}),ci4000=if($S{ci4000}='',null,$S{ci4000}),cj0000=if($S{cj0000}='',null,$S{cj0000}),cj1000=if($S{cj1000}='',null,$S{cj1000}),cj2000=if($S{cj2000}='',null,$S{cj2000}),cj3000=if($S{cj3000}='',null,$S{cj3000}),cj4000=if($S{cj4000}='',null,$S{cj4000}),trust_prod=if($S{trustProd}='',null,$S{trustProd})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG11(SqlParam<ZG11> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg11 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZg11ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg11 where report_date = $S{beginDate} ", params);
    }

    public UpdateResult addZg11(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg11(isu_org_cd,theory_report_start_date,register_status,prod_cate,c00000,c01000,c02000,c03000,c04000,c10000,c11000,c12000,c13000,c14000,c20000,c21000,c22000,c23000,c24000,c30000,c31000,c32000,c33000,c34000,c40000,c41000,c42000,c43000,c44000,c50000,c51000,c52000,c53000,c54000,c60000,c61000,c62000,c63000,c64000,c70000,c71000,c72000,c73000,c74000,c80000,c81000,c82000,c83000,c84000,c90000,c91000,c92000,c93000,c94000,ca0000,ca1000,ca2000,ca3000,ca4000,cb0000,cb1000,cb2000,cb3000,cb4000,cc0000,cc1000,cc2000,cc3000,cc4000,cd0000,cd1000,cd2000,cd3000,cd4000,ce0000,ce1000,ce2000,ce3000,ce4000,cf0000,cf1000,cf2000,cf3000,cf4000,cg0000,cg1000,cg2000,cg3000,cg4000,ch0000,ch1000,ch2000,ch3000,ch4000,ci0000,ci1000,ci2000,ci3000,ci4000,cj0000,cj1000,cj2000,cj3000,cj4000) VALUES($S{isuOrgCd} ,$S{theoryReportStartDate} ,$S{registerStatus},$S{prodCate}, if($S{c00000}='',null,$S{c00000}),if($S{c01000}='',null,$S{c01000}),if($S{c02000}='',null,$S{c02000}),if($S{c03000}='',null,$S{c03000}),if($S{c04000}='',null,$S{c04000}),if($S{c10000}='',null,$S{c10000}),if($S{c11000}='',null,$S{c11000}),if($S{c12000}='',null,$S{c12000}),if($S{c13000}='',null,$S{c13000}),if($S{c14000}='',null,$S{c14000}),if($S{c20000}='',null,$S{c20000}),if($S{c21000}='',null,$S{c21000}),if($S{c22000}='',null,$S{c22000}),if($S{c23000}='',null,$S{c23000}),if($S{c24000}='',null,$S{c24000}),if($S{c30000}='',null,$S{c30000}),if($S{c31000}='',null,$S{c31000}),if($S{c32000}='',null,$S{c32000}),if($S{c33000}='',null,$S{c33000}),if($S{c34000}='',null,$S{c34000}),if($S{c40000}='',null,$S{c40000}),if($S{c41000}='',null,$S{c41000}),if($S{c42000}='',null,$S{c42000}),if($S{c43000}='',null,$S{c43000}),if($S{c44000}='',null,$S{c44000}),if($S{c50000}='',null,$S{c50000}),if($S{c51000}='',null,$S{c51000}),if($S{c52000}='',null,$S{c52000}),if($S{c53000}='',null,$S{c53000}),if($S{c54000}='',null,$S{c54000}),if($S{c60000}='',null,$S{c60000}),if($S{c61000}='',null,$S{c61000}),if($S{c62000}='',null,$S{c62000}),if($S{c63000}='',null,$S{c63000}),if($S{c64000}='',null,$S{c64000}),if($S{c70000}='',null,$S{c70000}),if($S{c71000}='',null,$S{c71000}),if($S{c72000}='',null,$S{c72000}),if($S{c73000}='',null,$S{c73000}),if($S{c74000}='',null,$S{c74000}),if($S{c80000}='',null,$S{c80000}),if($S{c81000}='',null,$S{c81000}),if($S{c82000}='',null,$S{c82000}),if($S{c83000}='',null,$S{c83000}),if($S{c84000}='',null,$S{c84000}),if($S{c90000}='',null,$S{c90000}),if($S{c91000}='',null,$S{c91000}),if($S{c92000}='',null,$S{c92000}),if($S{c93000}='',null,$S{c93000}),if($S{c94000}='',null,$S{c94000}),if($S{ca0000}='',null,$S{ca0000}),if($S{ca1000}='',null,$S{ca1000}),if($S{ca2000}='',null,$S{ca2000}),if($S{ca3000}='',null,$S{ca3000}),if($S{ca4000}='',null,$S{ca4000}),if($S{cb0000}='',null,$S{cb0000}),if($S{cb1000}='',null,$S{cb1000}),if($S{cb2000}='',null,$S{cb2000}),if($S{cb3000}='',null,$S{cb3000}),if($S{cb4000}='',null,$S{cb4000}),if($S{cc0000}='',null,$S{cc0000}),if($S{cc1000}='',null,$S{cc1000}),if($S{cc2000}='',null,$S{cc2000}),if($S{cc3000}='',null,$S{cc3000}),if($S{cc4000}='',null,$S{cc4000}),if($S{cd0000}='',null,$S{cd0000}),if($S{cd1000}='',null,$S{cd1000}),if($S{cd2000}='',null,$S{cd2000}),if($S{cd3000}='',null,$S{cd3000}),if($S{cd4000}='',null,$S{cd4000}),if($S{ce0000}='',null,$S{ce0000}),if($S{ce1000}='',null,$S{ce1000}),if($S{ce2000}='',null,$S{ce2000}),if($S{ce3000}='',null,$S{ce3000}),if($S{ce4000}='',null,$S{ce4000}),if($S{cf0000}='',null,$S{cf0000}),if($S{cf1000}='',null,$S{cf1000}),if($S{cf2000}='',null,$S{cf2000}),if($S{cf3000}='',null,$S{cf3000}),if($S{cf4000}='',null,$S{cf4000}),if($S{cg0000}='',null,$S{cg0000}),if($S{cg1000}='',null,$S{cg1000}),if($S{cg2000}='',null,$S{cg2000}),if($S{cg3000}='',null,$S{cg3000}),if($S{cg4000}='',null,$S{cg4000}),if($S{ch0000}='',null,$S{ch0000}),if($S{ch1000}='',null,$S{ch1000}),if($S{ch2000}='',null,$S{ch2000}),if($S{ch3000}='',null,$S{ch3000}),if($S{ch4000}='',null,$S{ch4000}),if($S{ci0000}='',null,$S{ci0000}),if($S{ci1000}='',null,$S{ci1000}),if($S{ci2000}='',null,$S{ci2000}),if($S{ci3000}='',null,$S{ci3000}),if($S{ci4000}='',null,$S{ci4000}),if($S{cj0000}='',null,$S{cj0000}),if($S{cj1000}='',null,$S{cj1000}),if($S{cj2000}='',null,$S{cj2000}),if($S{cj3000}='',null,$S{cj3000}),if($S{cj4000}='',null,$S{cj4000}))", params);
    }






}
