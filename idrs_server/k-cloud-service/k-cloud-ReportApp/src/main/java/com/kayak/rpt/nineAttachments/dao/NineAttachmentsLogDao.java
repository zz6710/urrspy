package com.kayak.rpt.nineAttachments.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.nineAttachments.model.NineAttachmentsLog;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class NineAttachmentsLogDao extends ComnDao {




    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<NineAttachmentsLog> findNineAttachmentsLogs(SqlParam<NineAttachmentsLog> params) throws Exception {
        String sql = "SELECT  t.prod_cd, t1.prod_nm, t.zip_nm, t.zip_dir, t.direct_zip_nm, t.direct_zip_dir, t.report_send_date, t.report_send_time, " +
                "t.get_file_date, t.get_file_time, t.status,t.is_effective " +
                "FROM nine_attachments_file_log t " +
                "left join   dwd_prd_prd_bas_inf t1 on t.prod_cd=t1.prod_reg_enc" +
                " where 1 = 1 " ;

        if (StringUtils.isNotBlank(params.getModel().getReportSendDate())) {
            sql = sql + " and  report_send_date = '" + params.getModel().getReportSendDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getGetFileDate())) {
            sql = sql + " and  get_file_date = '" + params.getModel().getGetFileDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getStatus())) {
            sql = sql + " and  status = '" + params.getModel().getStatus() + "'";
        }

        return super.findRows(sql, params);
    }


}
