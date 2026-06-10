package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.TrCustVolRegisterInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TrCustVolRegisterInfoDao extends ComnDao {

    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfos(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("select id,report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date, " +
                "          register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date ");
        sql.append("    from ").append(DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate()));
        sql.append("   where sys_data_status ='1' ");

        if (Strings.isNotBlank(params.getModel().getMinId())) {
            sql.append(" and id >= ").append(params.getModel().getMinId());
        }
        if (Strings.isNotBlank(params.getModel().getMaxId())) {
            sql.append(" and id < ").append(params.getModel().getMaxId());
        }
        if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
            sql.append(" and register_serno = $S{registerSerno}");
        }
        if (Strings.isNotBlank(params.getModel().getReportDate())) {
            sql.append(" and report_date = $S{reportDate}");
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and register_status in (").append(SysUtil.inStr(params.getModel().getRegisterStatus())).append(")");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and cust_no = '").append(params.getModel().getCustNo()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and prod_code = '").append(params.getModel().getProdCode()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql.append(" and cur in (").append(SysUtil.inStr(params.getModel().getCur())).append(")");
        }
        SqlResult<TrCustVolRegisterInfo> result = super.findRows(sql.toString(), DataSourceProperty.PUB, params);

        /*String sql2 = "select audit_status from base_report_data_audit_results ARS " +
                "where ARS.report_date = $S{reportDate} AND ARS.table_id = 'app_cust_vol_register_info'";
        List<String> audit_status =  super.findRows(String.class, sql2, DataSourceProperty.PUB, new HashMap<>().put("reportDate", params.getModel().getReportDate()));

        if(audit_status.size() > 0){
            for(TrCustVolRegisterInfo m : result.getRows()){
                m.setAuditStatus(audit_status.get(0));
            }
        }*/

        return result;
	}

    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfosID(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("select min(id) min_id, max(id) max_id ");
        sql.append("    from ").append(DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate())).append(" T1 ");
        sql.append("   where T1.sys_data_status ='1' ");

        if (Strings.isNotBlank(params.getModel().getMinId())) {
            sql.append(" and T1.id >= ").append(params.getModel().getMinId());
        }
        if (Strings.isNotBlank(params.getModel().getMaxId())) {
            sql.append(" and T1.id < ").append(params.getModel().getMaxId());
        }
        if (Strings.isNotBlank(params.getModel().getReportDate())) {
            sql.append(" and T1.report_date = $S{reportDate}");
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and T1.register_status = '").append(params.getModel().getRegisterStatus()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and T1.cust_no = '").append(params.getModel().getCustNo()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and T1.prod_code = '").append(params.getModel().getProdCode()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql.append(" and T1.cur = '").append(params.getModel().getCur()).append("'");
        }

        return super.findRows(sql.toString(), DataSourceProperty.PUB, params);
    }
    public int findTrCustVolRegisterInfosCount(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("select count(1) from ")
           .append(DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate()));
        sql.append(" where sys_data_status ='1' ");
        if (Strings.isNotBlank(params.getModel().getMinId())) {
            sql.append(" and id >= ").append(params.getModel().getMinId());
        }
        if (Strings.isNotBlank(params.getModel().getMaxId())) {
            sql.append(" and id < ").append(params.getModel().getMaxId());
        }
        if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
            sql.append(" and register_serno = '").append(params.getModel().getRegisterSerno()).append("'");
        }
        if (Strings.isNotBlank(params.getModel().getReportDate())) {
            sql.append(" and report_date = ").append(params.getModel().getReportDate());
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and register_status = '").append(params.getModel().getRegisterStatus()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and cust_no = '").append(params.getModel().getCustNo()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and prod_code = '").append(params.getModel().getProdCode()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql.append(" and cur = '").append(params.getModel().getCur()).append("'");
        }
        return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
    }
    public int findTrCustVolRegisterInfosFailStatus(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("select count(1) from ")
                .append(DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate()));
        sql.append(" where sys_data_status ='1' and register_status in (0,1) ");
        if (Strings.isNotBlank(params.getModel().getMinId())) {
            sql.append(" and id >= ").append(params.getModel().getMinId());
        }
        if (Strings.isNotBlank(params.getModel().getMaxId())) {
            sql.append(" and id < ").append(params.getModel().getMaxId());
        }
        if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
            sql.append(" and register_serno = '").append(params.getModel().getRegisterSerno()).append("'");
        }
        if (Strings.isNotBlank(params.getModel().getReportDate())) {
            sql.append(" and report_date = ").append(params.getModel().getReportDate());
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and register_status = '").append(params.getModel().getRegisterStatus()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and cust_no = '").append(params.getModel().getCustNo()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and prod_code = '").append(params.getModel().getProdCode()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql.append(" and cur = '").append(params.getModel().getCur()).append("'");
        }
        return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
    }
    public SqlResult<TrCustVolRegisterInfo> findTrCustVolRegisterInfosAndIsError(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return super.findRows(" SELECT report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,is_error" +
                " FROM (SELECT T1.*, (case when t2.register_serno is null then '0' else '1' end) AS IS_ERROR FROM app_cust_vol_register_info  T1 LEFT JOIN  app_cust_vol_register_info_erdesc  T2   ON T2.register_serno = T1.register_serno)  AA", DataSourceProperty.PUB, params);
    }

    public SqlResult<TrCustVolRegisterInfo> findValidateInfos(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        String sql = "select index_code,reason from base_data_validation where validate_table = $S{validateTable} and deal_date = $S{reportDate} and data_id = $S{dataId}" ;
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public UpdateResult addTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return super.update("INSERT INTO "+DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate())+"(report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,sys_data_status) VALUES($S{reportDate},$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},$S{impDate},$S{registerDate},'0',(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),DATE_FORMAT($S{holdDate},'%Y%m%d'), '1')",
                DataSourceProperty.PUB, params.getModel());
    }

    public void addTrCustVolRegisterInfofoBatch(List<Map<String, Object>> mapList) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String sql = "INSERT INTO app_cust_vol_register_info(report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno) VALUES($S{reportDate},$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},$S{impDate},$S{registerDate},'0',(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual))";
        for (Map<String, Object> map : mapList) {
            map.put("impDate", dateStr);
            super.update(sql, DataSourceProperty.PUB, map);
        }
    }

    public UpdateResult updateTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return super.update("UPDATE "+DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate())+" SET report_date=$S{reportDate} ,bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,cust_no=$S{custNo} ,hold_date=$S{holdDate} ,cur=$S{cur} ,hold_vol=$S{holdVol} ,hold_amt=$S{holdAmt} ,convert_rmb=$D{convertRmb} ,imp_date=$S{impDate} ,register_date=$S{registerDate}   WHERE register_serno=$S{registerSerno} ",
                DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult deleteTrCustVolRegisterInfo(SqlParam<TrCustVolRegisterInfo> params) throws Exception {
        return super.update("DELETE FROM "+DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate())+" WHERE register_serno=$S{registerSerno}  ",
                DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult addImportCustVolRegisterInfo(Object param) throws Exception {
        return super.update("INSERT INTO app_cust_vol_register_info(report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,sys_data_status) " +
                                                              "VALUES($S{reportDate},$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{theoryReportStartDate},'1')",
                DataSourceProperty.PUB, param);
    }

    public UpdateResult addImportModifyCustVolRegisterInfo(Object param) throws Exception {
        return super.update("REPLACE INTO app_cust_vol_register_info_modify(report_date,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,sys_data_status) VALUES($S{reportDate},$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{theoryReportStartDate},'1')",
                DataSourceProperty.PUB, param);
    }

    /**
     * 根据客户识别标识进行持有信息更新
     * @param param
     * @return
     * @throws Exception
     */
    public UpdateResult updateTrCustVolRegisterInfoByCustNo(Map<String, Object> param) throws Exception {
        return super.update("update "+DateUtil.getInvTable("app_cust_vol_register_info", (String) param.get("reportDate"))+" set bank_code = $S{bankCode}, hold_date = $S{holdDate}, cur = $S{cur}, hold_vol = $S{holdVol}, hold_amt = $S{holdAmt}, convert_rmb = $D{convertRmb}, sys_data_status = '1' where cust_no = $S{custNo} and prod_code = $S{prodCode}", DataSourceProperty.PUB, param);
    }

    /** 手动确认成功
     * 更新指定日期的数据为报送成功
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateTrCustVolRegisterInfoRegistStatusSuccess(SqlParam<TrCustVolRegisterInfo> params) throws Exception {

        StringBuilder sql = new StringBuilder("");
        sql.append("update ");
        sql.append(DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate())).append(" T1 ");
        sql.append(" set register_status = '3' where  sys_data_status ='1' and  report_date = '");
        sql.append(params.getModel().getReportDate()).append("' ");
//        if (Strings.isNotBlank(params.getModel().getMinId())) {
//            sql.append(" and T1.id >= ").append(params.getModel().getMinId());
//        }
//        if (Strings.isNotBlank(params.getModel().getMaxId())) {
//            sql.append(" and T1.id < ").append(params.getModel().getMaxId());
//        }
//        if (Strings.isNotBlank(params.getModel().getReportDate())) {
//            sql.append(" and T1.report_date = $S{reportDate}");
//        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and T1.register_status = '").append(params.getModel().getRegisterStatus()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and T1.cust_no = '").append(params.getModel().getCustNo()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and T1.prod_code = '").append(params.getModel().getProdCode()).append("'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCur())) {
            sql.append(" and T1.cur = '").append(params.getModel().getCur()).append("'");
        }
        return super.update(sql.toString(), DataSourceProperty.PUB, params);
    }

    public UpdateResult deleteImportCustVolRegisterInfo(Map<String, Object> params) throws Exception {
        return super.update("DELETE FROM app_cust_vol_register_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
    }

    public UpdateResult deleteImportModifyCustVolRegisterInfo(Map<String, Object> params) throws Exception {
        return super.update("DELETE FROM app_cust_vol_register_info_modify where create_date = $S{createDate} ", params);
    }
}
