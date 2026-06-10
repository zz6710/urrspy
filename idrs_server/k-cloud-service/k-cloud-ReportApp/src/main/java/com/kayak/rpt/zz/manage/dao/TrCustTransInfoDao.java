package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.TrCustTransInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import io.micrometer.core.instrument.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TrCustTransInfoDao extends ComnDao {

    public SqlResult<TrCustTransInfo> findTrCustTransInfos(SqlParam<TrCustTransInfo> params) throws Exception {
        String sql = "select T1.id,T1.report_date,create_date,theory_report_start_date,theory_report_end_date ,bank_code,trans_serno,contract_no," +
                "            fnc_trans_acct_no,host_cust_no,cust_no,cust_name,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code," +
                "            is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time," +
                "            cur,son_share_code,spe_channel_flag,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,register_serno,imp_date,register_date," +
                "            register_status,cust_name as cust_name_display,acct_no as acct_no_display" +
                "    from " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " T1" +
                "   where T1.sys_data_status ='1' ";

        if (StringUtils.isNotBlank(params.getModel().getMinId())) {
            sql += " and T1.id >= " + params.getModel().getMinId();
        }
        if (StringUtils.isNotBlank(params.getModel().getMaxId())) {
            sql += " and T1.id < " + params.getModel().getMaxId();
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql += " and T1.report_date = $S{reportDate} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql += " and T1.register_status in (" + SysUtil.inStr(params.getModel().getRegisterStatus()) + ")";
        }
        if (StringUtils.isNotBlank(params.getModel().getHostCustNo())) {
            sql += " and T1.host_cust_no = '" + params.getModel().getHostCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql += " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBusiCode())) {
            sql += " and T1.busi_code in (" + SysUtil.inStr(params.getModel().getBusiCode()) + ")";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and T1.prod_code = '" + params.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankCode())) {
            sql += " and T1.agent_bank_code = '" + params.getModel().getAgentBankCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankName())) {
            sql += " and T1.agent_bank_name = '" + params.getModel().getAgentBankName() + "'";
        }
        return super.findRows(sql, params);
    }

    public int findTrCustTransInfosCount(SqlParam<TrCustTransInfo> params) throws Exception {
        String sql = "select count(1) " +
                "  from " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " T1" +
                "  where T1.sys_data_status ='1' ";

        if (StringUtils.isNotBlank(params.getModel().getMinId())) {
            sql += " and T1.id >= " + params.getModel().getMinId();
        }
        if (StringUtils.isNotBlank(params.getModel().getMaxId())) {
            sql += " and T1.id < " + params.getModel().getMaxId();
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql += " and T1.report_date = " + params.getModel().getReportDate();
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql += " and T1.register_status in (" + SysUtil.inStr(params.getModel().getRegisterStatus()) + ")";
        }
        if (StringUtils.isNotBlank(params.getModel().getHostCustNo())) {
            sql += " and T1.host_cust_no = '" + params.getModel().getHostCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql += " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBusiCode())) {
            sql += " and T1.busi_code = '" + params.getModel().getBusiCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and T1.prod_code = '" + params.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankCode())) {
            sql += " and T1.agent_bank_code = '" + params.getModel().getAgentBankCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankName())) {
            sql += " and T1.agent_bank_name = '" + params.getModel().getAgentBankName() + "'";
        }
        return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
    }
    public int findTrCustTransInfosFailStatus(SqlParam<TrCustTransInfo> params) throws Exception {
        String sql = "select count(1) " +
                "  from " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " T1" +
                "  where T1.sys_data_status ='1' and T1.register_status in (0,1) ";

        if (StringUtils.isNotBlank(params.getModel().getMinId())) {
            sql += " and T1.id >= " + params.getModel().getMinId();
        }
        if (StringUtils.isNotBlank(params.getModel().getMaxId())) {
            sql += " and T1.id < " + params.getModel().getMaxId();
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql += " and T1.report_date = " + params.getModel().getReportDate();
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql += " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getHostCustNo())) {
            sql += " and T1.host_cust_no = '" + params.getModel().getHostCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql += " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBusiCode())) {
            sql += " and T1.busi_code = '" + params.getModel().getBusiCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and T1.prod_code = '" + params.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankCode())) {
            sql += " and T1.agent_bank_code = '" + params.getModel().getAgentBankCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankName())) {
            sql += " and T1.agent_bank_name = '" + params.getModel().getAgentBankName() + "'";
        }
        return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
    }
    public SqlResult<TrCustTransInfo> findTrCustTransInfosID(SqlParam<TrCustTransInfo> params) throws Exception {
        String sql = "select min(id) min_id, max(id) max_id " +
                "   from " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " T1" +
                "  where T1.sys_data_status ='1' ";

        if (StringUtils.isNotBlank(params.getModel().getMinId())) {
            sql += " and T1.id >= " + params.getModel().getMinId();
        }
        if (StringUtils.isNotBlank(params.getModel().getMaxId())) {
            sql += " and T1.id < " + params.getModel().getMaxId();
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql += " and T1.report_date = $S{reportDate} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql += " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getHostCustNo())) {
            sql += " and T1.host_cust_no = '" + params.getModel().getHostCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql += " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBusiCode())) {
            sql += " and T1.busi_code = '" + params.getModel().getBusiCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and T1.prod_code = '" + params.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankCode())) {
            sql += " and T1.agent_bank_code = '" + params.getModel().getAgentBankCode() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankName())) {
            sql += " and T1.agent_bank_name = '" + params.getModel().getAgentBankName() + "'";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<TrCustTransInfo> findTrCustTransInfosAndIsError(SqlParam<TrCustTransInfo> params) throws Exception {
        return super.findRows("SELECT report_date,bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,register_serno,imp_date,register_date,register_status,is_error FROM   (  SELECT T1.*, (case when t2.register_serno is null then '0' else '1' end) AS IS_ERROR FROM app_cust_trans_info  T1 LEFT JOIN  app_cust_trans_info_erdesc  T2   ON T2.register_serno = T1.register_serno)  AA", params);
    }

    public SqlResult<TrCustTransInfo> findValidateInfos(SqlParam<TrCustTransInfo> params) throws Exception {
        String sql = "select index_code,reason from base_data_validation where validate_table = $S{validateTable} and deal_date = $S{reportDate} and data_id = $S{dataId}" ;
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public UpdateResult addTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        return super.update("INSERT INTO "+DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate())+"(report_date,create_date, bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,theory_report_start_date, sys_data_status) " +
                        "VALUES($S{reportDate},date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{transSerno},$S{contractNo},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{dealNo},$S{acctNo},$S{acctBankNo},$S{acctBankName},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{ackDate}, '1')",
                DataSourceProperty.PUB, params.getModel());
    }

    public void addTrCustTransInfofoBatch(List<Map<String, Object>> mapList) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String sql = "INSERT INTO app_cust_trans_info(report_date,create_date, bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,register_serno,imp_date,register_date,register_status,theory_report_start_date,sys_data_status) " +
                "VALUES($S{reportDate},date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{transSerno},$S{contractNo},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{dealNo},$S{acctNo},$S{acctBankNo},$S{acctBankName},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{theoryReportStartDate}, '1')";
        for (Map<String, Object> map : mapList) {
            map.put("impDate", dateStr);
            super.update(sql, DataSourceProperty.PUB, map);
        }
    }

    public UpdateResult updateTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        return super.update("UPDATE " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) +
                        "           SET report_date=$S{reportDate} ,bank_code=$S{bankCode} ,trans_serno=$S{transSerno} ,contract_no=$S{contractNo} , " +
                        "               fnc_trans_acct_no=$S{fncTransAcctNo} ,host_cust_no=$S{hostCustNo} ,deal_No= $S{dealNo}, cust_no=$S{custNo} , " +
                        "               acct_Bank_No =$S{acctBankNo} , acct_Bank_Name =$S{acctBankName} ,acct_no=$S{acctNoDisplay} ," +
                        "               acct_loc_code=$S{acctLocCode} ,is_agent=$S{isAgent} ,agent_bank_code=$S{agentBankCode} , " +
                        "               agent_bank_name=$S{agentBankName} ,agent_regu_code=$S{agentReguCode} ,prod_code=$S{prodCode} ,busi_code=$S{busiCode} ," +
                        "               busi_regu_code=$S{busiReguCode} ,ack_date=$S{ackDate} ,ack_time=$S{ackTime} ,cur=$S{cur} ," +
                        "               ack_amt=$D{ackAmt} ,convert_rmb=$D{convertRmb} ,nav=$D{nav} ,ack_vol=$D{ackVol} ," +
                        "               fee_amt=$D{feeAmt} ,channel_flag=$S{channelFlag} ,inputuser=$S{inputuser} ,remark=$S{remark}, son_share_code=$S{sonShareCode}, spe_channel_flag=$S{speChannelFlag} ," +
                        "               imp_date=$S{impDate} ,register_date=$S{registerDate} ,acct_bank_no=$S{acctBankNo}," +
                        "               acct_bank_name=$S{acctBankName} " +
                        " WHERE id = $S{id}  ",
                DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult deleteTrCustTransInfo(SqlParam<TrCustTransInfo> params) throws Exception {
        return super.update("DELETE FROM "+DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate())+" WHERE id = $S{id}  ",
                DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult addImportCustTransInfo(Object param) throws Exception {
        return super.update("INSERT INTO app_cust_trans_info(report_date, create_date, bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,theory_report_start_date,sys_data_status) " +
                        "VALUES($S{reportDate},date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{transSerno},$S{contractNo},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{dealNo},$S{acctNo},$S{acctBankNo},$S{acctBankName},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$S{theoryReportStartDate},'1')",
                DataSourceProperty.PUB, param);
    }

    public UpdateResult addImportModifyCustTransInfo(Object param) throws Exception {
        return super.update("REPLACE INTO app_cust_trans_info_modify(report_date, create_date, bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,deal_No,acct_no,acct_Bank_No,acct_Bank_Name,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,theory_report_start_date,sys_data_status) " +
                        "VALUES($S{reportDate},date_format(CURDATE(),'%Y%m%d'),$S{bankCode},$S{transSerno},$S{contractNo},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{dealNo},$S{acctNo},$S{acctBankNo},$S{acctBankName},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$S{theoryReportStartDate},'1')",
                DataSourceProperty.PUB, param);
    }

    /**
     * 对导入的交易明细信息进行更新操作
     * @param param
     * @return
     * @throws Exception
     */
    public UpdateResult updateImportCustTransInfoByCustNo(Map<String, Object> param) throws Exception {
        return super.update("update "+DateUtil.getInvTable("app_cust_trans_info", (String) param.get("reportDate"))+" set bank_code = $S{bankCode}, contract_no = $S{contractNo}, fnc_trans_acct_no = $S{fncTransAcctNo}, host_cust_no = $S{hostCustNo}, deal_No = $S{dealNo}, acct_no = $S{acctNo}, acct_Bank_No = $S{acctBankNo}, acct_Bank_Name = $S{acctBankName}, acct_loc_code = $S{acctLocCode}, is_agent = $S{isAgent}, agent_bank_code = $S{agentBankCode}, agent_bank_name = $S{agentBankName}, agent_regu_code = $S{agentReguCode}, prod_code = $S{prodCode}, busi_code = $S{busiCode}, busi_regu_code = $S{busiReguCode}, ack_date = $S{ackDate}, ack_time = $S{ackTime}, cur = $S{cur}, ack_amt = $D{ackAmt}, convert_rmb = $D{convertRmb}, nav = $D{nav}, ack_vol = $D{ackVol}, fee_amt = $D{feeAmt}, channel_flag = $S{channelFlag}, inputuser = $S{inputuser}, remark = $S{remark}, son_share_code = $S{sonShareCode}, spe_channel_flag = $S{speChannelFlag}, sys_data_status = '1' where cust_no = $S{custNo} and trans_serno = $S{transSerno} and report_date = $S{reportDate}",
                DataSourceProperty.PUB, param);
    }

    /** 手动确认成功
     * 更新指定日期的数据为报送成功
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateCustTransInfoRegistStatusSuccess(SqlParam<TrCustTransInfo> params) throws Exception {
        StringBuilder sql = new StringBuilder("update ");
        sql.append(DateUtil.getInvTable("app_cust_trans_info",  params.getModel().getReportDate())).append(" T1 ");
        sql.append(" set  register_status = '3' where sys_data_status ='1' and report_date = '");
        sql.append(params.getModel().getReportDate()).append("' ");
//        if (StringUtils.isNotBlank(params.getModel().getMinId())) {
//            sql.append(" and T1.id >= " + params.getModel().getMinId());
//        }
//        if (StringUtils.isNotBlank(params.getModel().getMaxId())) {
//            sql.append(" and T1.id < " + params.getModel().getMaxId());
//        }
//        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
//            sql.append(" and T1.report_date = $S{reportDate} ");
//        }
        if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
            sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getHostCustNo())) {
            sql.append(" and T1.host_cust_no = '" + params.getModel().getHostCustNo() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql.append(" and T1.cust_no = '" + params.getModel().getCustNo() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getBusiCode())) {
            sql.append(" and T1.busi_code = '" + params.getModel().getBusiCode() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append(" and T1.prod_code = '" + params.getModel().getProdCode() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankCode())) {
            sql.append(" and T1.agent_bank_code = '" + params.getModel().getAgentBankCode() + "'");
        }
        if (StringUtils.isNotBlank(params.getModel().getAgentBankName())) {
            sql.append(" and T1.agent_bank_name = '" + params.getModel().getAgentBankName() + "'");
        }

        return super.update(sql.toString(),DataSourceProperty.PUB, params);
    }

    public UpdateResult deleteImportCustTransInfo(Map<String, Object> params) throws Exception {
        return super.update("DELETE FROM app_cust_trans_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
    }

    public UpdateResult deleteImportModifyCustTransInfo(Map<String, Object> params) throws Exception {
        return super.update("DELETE FROM app_cust_trans_info_modify where create_date = $S{createDate} ", params);
    }
}
