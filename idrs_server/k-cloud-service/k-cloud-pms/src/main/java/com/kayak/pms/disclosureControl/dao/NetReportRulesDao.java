package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.disclosureControl.model.NetReportRules;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class NetReportRulesDao extends ComnDao {

    public SqlResult<NetReportRules> findNetReportRules(SqlParam<NetReportRules> params) throws Exception {
        String sql = "select t.id,t.operation_mode,t.regular_open_cycle,t.report_rules,t.report_freq,t.length_freq,t.specific_date,\n" +
                "case when t.report_freq = '1'\n" +
                "\tthen (select itemval from sys_dict_item where itemkey = t.specific_date and dict = 'week')\n" +
                "\twhen t.report_freq = '2'\n" +
                "\tthen (select itemval from sys_dict_item where itemkey = t.specific_date and dict = 'month')\n" +
                "\telse\n" +
                "\tt.specific_date\n" +
                "end specific_date_val,\n" +
                "case when t.report_rules = '3' \n" +
                "\tthen CONCAT('每',t.length_freq,(select itemval from sys_dict_item where itemkey = t.report_freq and dict = 'report_freq')) \n" +
                "end report_freq_val, \n" +
                "t.report_date,t.report_month,t.report_confirm_date,t.is_base_day,t.crt_date,t.crt_time,t.crt_user,t.upd_date,t.upd_time,t.upd_user\n " +
                "from idb_net_report_rules t where 1 = 1 ";

        NetReportRules n = params.getModel();
        if (StringUtils.isNotBlank(n.getOperationMode())){
            sql += "and t.operation_mode = $S{operationMode} ";
        }
        if (StringUtils.isNotBlank(n.getRegularOpenCycle())){
            sql += "and t.regular_open_cycle = $S{regularOpenCycle}";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult addNetReportRules(NetReportRules params) throws Exception {
        return super.update("INSERT INTO idb_net_report_rules(id,operation_mode,regular_open_cycle,report_rules,report_freq,length_freq,specific_date,report_date,report_month,report_confirm_date,is_base_day,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) " +
                        "VALUES($AUTOIDS{id},$S{operationMode},$S{regularOpenCycle},$S{reportRules},$S{reportFreq},$S{lengthFreq},$S{specificDate},$S{reportDate},$S{reportMonth},$S{reportConfirmDate},$S{isBaseDay},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
                params);
    }

    public UpdateResult updateNetReportRules(NetReportRules params) throws Exception {
        return super.update("UPDATE idb_net_report_rules SET " +
                        "operation_mode = $S{operationMode} , \n" +
                        "regular_open_cycle = $S{regularOpenCycle} , \n" +
                        "report_rules = $S{reportRules} , \n" +
                        "report_freq = $S{reportFreq} , \n" +
                        "length_freq = $S{lengthFreq} , \n" +
                        "specific_date = $S{specificDate} , \n" +
                        "report_date = $S{reportDate} , \n" +
                        "report_month = $S{reportMonth} , \n" +
                        "report_confirm_date = $S{reportConfirmDate} , \n" +
                        "is_base_day = $S{isBaseDay} , \n" +
                        "crt_date = $S{crtDate} , \n" +
                        "crt_time = $S{crtTime} , \n" +
                        "crt_user = $S{crtUser} , \n" +
                        "upd_date = $S{updDate} , \n" +
                        "upd_time = $S{updTime} , \n" +
                        "upd_user = $S{updUser}  \n " +
                        "WHERE id=$S{id} ",
                params);
    }

    public UpdateResult deleteNetReportRules(NetReportRules params) throws Exception {
        return super.update("DELETE FROM idb_net_report_rules WHERE id=$S{id} ",
                params);
    }
}
