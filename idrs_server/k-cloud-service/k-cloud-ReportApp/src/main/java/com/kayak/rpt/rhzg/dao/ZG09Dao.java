package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG09;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG09Dao extends ComnDao {




    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG09> findZG09s(SqlParam<ZG09> params) throws Exception {
        String sql = "SELECT id,isu_org_cd,report_date,theory_report_start_date,prod_cate,g000a,g000b,g000c,g000d,g000e,g100a,g100b,g100c,g100d,g100e,g200a,g200b,g200c,g200d,g200e,g300a,g300b,g300c,g300d,g300e,g400a,g400b,g400c,g400d,g400e,g500a,g500b,g500c,g500d,g500e,g600a,g600b,g600c,g600d,g600e,g700a,g700b,g700c,g700d,g700e,g800a,g800b,g800c,g800d,g800e,g0001,g0002,trust_prod FROM app_pbc_report_zg09 where sys_data_status ='1' ";
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

    public UpdateResult updateZG09(SqlParam<ZG09> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg09 SET isu_org_cd=$S{isuOrgCd} ,theory_report_start_date=$S{theoryReportStartDate} ,prod_cate=$S{prodCate},g000a=if($S{g000a}='',null,$S{g000a}),g000b=if($S{g000b}='',null,$S{g000b}),g000c=if($S{g000c}='',null,$S{g000c}),g000d=if($S{g000d}='',null,$S{g000d}),g000e=if($S{g000e}='',null,$S{g000e}),g100a=if($S{g100a}='',null,$S{g100a}),g100b=if($S{g100b}='',null,$S{g100b}),g100c=if($S{g100c}='',null,$S{g100c}),g100d=if($S{g100d}='',null,$S{g100d}),g100e=if($S{g100e}='',null,$S{g100e}),g200a=if($S{g200a}='',null,$S{g200a}),g200b=if($S{g200b}='',null,$S{g200b}),g200c=if($S{g200c}='',null,$S{g200c}),g200d=if($S{g200d}='',null,$S{g200d}),g200e=if($S{g200e}='',null,$S{g200e}),g300a=if($S{g300a}='',null,$S{g300a}),g300b=if($S{g300b}='',null,$S{g300b}),g300c=if($S{g300c}='',null,$S{g300c}),g300d=if($S{g300d}='',null,$S{g300d}),g300e=if($S{g300e}='',null,$S{g300e}),g400a=if($S{g400a}='',null,$S{g400a}),g400b=if($S{g400b}='',null,$S{g400b}),g400c=if($S{g400c}='',null,$S{g400c}),g400d=if($S{g400d}='',null,$S{g400d}),g400e=if($S{g400e}='',null,$S{g400e}),g500a=if($S{g500a}='',null,$S{g500a}),g500b=if($S{g500b}='',null,$S{g500b}),g500c=if($S{g500c}='',null,$S{g500c}),g500d=if($S{g500d}='',null,$S{g500d}),g500e=if($S{g500e}='',null,$S{g500e}),g600a=if($S{g600a}='',null,$S{g600a}),g600b=if($S{g600b}='',null,$S{g600b}),g600c=if($S{g600c}='',null,$S{g600c}),g600d=if($S{g600d}='',null,$S{g600d}),g600e=if($S{g600e}='',null,$S{g600e}),g700a=if($S{g700a}='',null,$S{g700a}),g700b=if($S{g700b}='',null,$S{g700b}),g700c=if($S{g700c}='',null,$S{g700c}),g700d=if($S{g700d}='',null,$S{g700d}),g700e=if($S{g700e}='',null,$S{g700e}),g800a=if($S{g800a}='',null,$S{g800a}),g800b=if($S{g800b}='',null,$S{g800b}),g800c=if($S{g800c}='',null,$S{g800c}),g800d=if($S{g800d}='',null,$S{g800d}),g800e=if($S{g800e}='',null,$S{g800e}),g0001=if($S{g0001}='',null,$S{g0001}),g0002=if($S{g0002}='',null,$S{g0002}),trust_prod=if($S{trustProd}='',null,$S{trustProd})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG09(SqlParam<ZG09> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg09 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZg09ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg09 where report_date = $S{beginDate} ", params);
    }

    public UpdateResult addZg09(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg09(isu_org_cd,theory_report_start_date,register_status,prod_cate,g000a,g000b,g000c,g000d,g000e,g100a,g100b,g100c,g100d,g100e,g200a,g200b,g200c,g200d,g200e,g300a,g300b,g300c,g300d,g300e,g400a,g400b,g400c,g400d,g400e,g500a,g500b,g500c,g500d,g500e,g600a,g600b,g600c,g600d,g600e,g700a,g700b,g700c,g700d,g700e,g800a,g800b,g800c,g800d,g800e,g0001,g0002) VALUES($S{isuOrgCd} ,$S{theoryReportStartDate} ,$S{registerStatus},$S{prodCate},if($S{g000a}='',null,$S{g000a}),if($S{g000b}='',null,$S{g000b}),if($S{g000c}='',null,$S{g000c}),if($S{g000d}='',null,$S{g000d}),if($S{g000e}='',null,$S{g000e}),if($S{g100a}='',null,$S{g100a}),if($S{g100b}='',null,$S{g100b}),if($S{g100c}='',null,$S{g100c}),if($S{g100d}='',null,$S{g100d}),if($S{g100e}='',null,$S{g100e}),if($S{g200a}='',null,$S{g200a}),if($S{g200b}='',null,$S{g200b}),if($S{g200c}='',null,$S{g200c}),if($S{g200d}='',null,$S{g200d}),if($S{g200e}='',null,$S{g200e}),if($S{g300a}='',null,$S{g300a}),if($S{g300b}='',null,$S{g300b}),if($S{g300c}='',null,$S{g300c}),if($S{g300d}='',null,$S{g300d}),if($S{g300e}='',null,$S{g300e}),if($S{g400a}='',null,$S{g400a}),if($S{g400b}='',null,$S{g400b}),if($S{g400c}='',null,$S{g400c}),if($S{g400d}='',null,$S{g400d}),if($S{g400e}='',null,$S{g400e}),if($S{g500a}='',null,$S{g500a}),if($S{g500b}='',null,$S{g500b}),if($S{g500c}='',null,$S{g500c}),if($S{g500d}='',null,$S{g500d}),if($S{g500e}='',null,$S{g500e}),if($S{g600a}='',null,$S{g600a}),if($S{g600b}='',null,$S{g600b}),if($S{g600c}='',null,$S{g600c}),if($S{g600d}='',null,$S{g600d}),if($S{g600e}='',null,$S{g600e}),if($S{g700a}='',null,$S{g700a}),if($S{g700b}='',null,$S{g700b}),if($S{g700c}='',null,$S{g700c}),if($S{g700d}='',null,$S{g700d}),if($S{g700e}='',null,$S{g700e}),if($S{g800a}='',null,$S{g800a}),if($S{g800b}='',null,$S{g800b}),if($S{g800c}='',null,$S{g800c}),if($S{g800d}='',null,$S{g800d}),if($S{g800e}='',null,$S{g800e}),if($S{g0001}='',null,$S{g0001}),if($S{g0002}='',null,$S{g0002}))", params);
    }



}
