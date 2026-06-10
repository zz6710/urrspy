package com.kayak.dps.direct.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.direct.model.dto.IndexCodeDTO;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CheckDataDao extends ComnDao {



    public List<SqlRow> getExFmt(String exFmtId) throws Exception {

        String sql = "select g.exfmtid,g.itmnm,g.itmprc,g.itmscl,g.fld,g.fldpk,g.itmtp,g.itmdsc,g.sn,t2.dictflag " +
                "     from base_ex_fmt g left join base_ex_dict t2 on g.itmnm=t2.itmnm where g.exfmtid = 'ZZ_" + exFmtId + "'" +
                " order by g.exfmtid,g.sn";

        return super.findRows(sql);
    }


    public List<SqlRow> findRegisterInfoIdCode() throws Exception {
        String sql_check_rep = " select aa.id_code, aa.cnt from (select  t.id_code, count(1) cnt from " +
                "app_cust_register_info t where t.id_code is not null group by t.personal_id_type,t.organization_id_type, t.id_code,t.data_type) aa " +
                "where aa.cnt > 1  and aa.id_code is not null and aa.id_code <> '' ";
        return super.findRows(sql_check_rep);
    }


    public String findRegisterInfoIdCodeByType() throws Exception {
        String sql_check_rep = "  select t.id_code from app_cust_register_info t  where exists (select 1 from app_cust_register_info_h tt "
                + " where t.cust_name = tt.cust_name  and t.id_code = tt.id_code and (t.personal_id_type = tt.personal_id_type or t.organization_id_type = tt.organization_id_type " +
                " and tt.data_type = '01' )) and t.data_type = '01' ";
        List<SqlRow> idCode = super.findRows(sql_check_rep);
        String idCodes = "";
        for (SqlRow sqlRow : idCode) {
            idCodes += sqlRow.getString("id_code");
            idCodes += "," ;
        }
        return idCodes ;
    }

    public List<SqlRow> findRegisterDataType() throws Exception {
        String sql_check = "select aa.data_type,aa.cust_no,aa.cnt from( select t.data_type,t.cust_no,count(1) cnt " +
                "from app_cust_register_info  t group by t.data_type ,t.cust_no) aa  where aa.cnt > 1 ";
        return super.findRows(sql_check);
    }

    public List<SqlRow> findSql(String sql) throws Exception {
        return super.findRows(sql);
    }


    public List<SqlRow> findTransInfo(String workdate) throws Exception {
        String sql = " select trim(t.bank_code) bank_code,\n" +
                "            t.trans_serno,\n" +
                "            t.contract_no,\n" +
                "            t.fnc_trans_acct_no,\n" +
                "            t.host_cust_no,\n" +
                "            t.cust_no,\n" +
                "            t.cust_name,\n" +
                "            t.acct_no,\n" +
                "            t.acct_loc_code,\n" +
                "            t.is_agent,\n" +
                "            t.agent_bank_code,\n" +
                "            t.agent_bank_name,\n" +
                "            t.agent_regu_code,\n" +
                "            t.prod_code,\n" +
                "            t.busi_code,\n" +
                "            t.busi_regu_code,\n" +
                "            trim(t.ack_date) ack_date,\n" +
                "            trim(t.ack_time) ack_time,\n" +
                "            trim(t.cur) cur,\n" +
                "            convert(t.ack_amt,decimal(25,2)) as ack_amt,\n" +
                "            convert(t.convert_rmb,decimal(25,2)) as convert_rmb,\n" +
                "            t.nav,\n" +
                "            convert(t.ack_vol,decimal(28,5)) as ack_vol,\n" +
                "            convert(t.fee_amt,decimal(25,2)) as fee_amt,\n" +
                "            t.channel_flag,\n" +
                "            t.inputuser,\n" +
                "            t.remark,\n" +
                "            t.register_serno,p.reg_code  from app_cust_trans_info   t\n" +
                "                       JOIN  app_prod_reg_relation  p ON  t.prod_code = p.reg_code\n" +
                "                 where  t.register_status in ('0', '2')\n" +
                "                      and t.ack_date=$S{workdate}\n" +
                "                 order by t.register_serno asc";

        return super.findRows(sql , workdate);
    }

    public List<SqlRow> findRegisterInfo(String workdate) throws Exception {
        String sql = "select \n" +
                "\t\t\t\tt1.bank_code,t1.is_belong,t1.iss_bank_name,t1.iss_bank_code,t1.in_out_sign,\n" +
                "\t\t\t\tt1.iss_country,t1.data_type,t1.ori_cust_no,t1.cust_no,t1.cust_type,\n" +
                "\t\t\t\tt1.personal_id_type,t1.organization_id_type,t1.other_id_name,t1.id_code,\n" +
                "\t\t\t\tt1.spv_open_bank,t1.other_open_bank,t1.cust_name,\n" +
                "\t\t\t\tt1.sex,t1.risk_level,trim(t1.moble) moble,t1.tel_phone,t1.email,t1.remark,\n" +
                "\t\t\t\tt1.register_serno,t1.theory_report_start_date,t1.register_date\n" +
                "\t\t\tfrom app_cust_register_info t1\n" +
                "\t\t\twhere t1.register_date=$S{workdate}\n" +
                "\t\t\tand  t1.register_status in ('0','2')\n" +
                "\t\t\torder by t1.register_serno asc";

        return super.findRows(sql , workdate);
    }

    public List<SqlRow> findVolRegisterInfo(String workdate) throws Exception {
        String sql = "select \n" +
                "\t\t\t\tt1.bank_code,t1.prod_code,t1.hold_date,\n" +
                "\t\t\t\tt1.cust_no,convert(t1.hold_amt,decimal(25,2)) hold_amt,convert(t1.convert_rmb,decimal(25,2)) convert_rmb,\n" +
                "\t\t\t\tconvert(t1.hold_vol,decimal(25,4)) hold_vol,t1.register_serno,t1.register_date,t1.theory_report_start_date,t1.cur\n" +
                "\t\t\tfrom app_cust_vol_register_info t1\n" +
                "\t\t\twhere t1.hold_date=$S{workdate}\n" +
                "\t\t\tand t1.register_status in ('0','2')\n" +
                "\t\t\torder by t1.register_serno asc";

        return super.findRows(sql , workdate);
    }


    /**
     * 检查识别标识是否存在身份信息中
     * @return
     */
    public String checkCustInfo(String Custno) throws Exception {
        String desc = "";
        int nu=0;
        String sql = " select  count(1) as nu from ( select CUST_NO from app_cust_register_info where CUST_NO= '"+Custno+"'"+" and data_type='01' "
                +" union all select CUST_NO from app_cust_register_info_h where CUST_NO='"+Custno+"'"+" and data_type='01' ) a" ;
        List<SqlRow> rs = super.findRows(sql);
        for (SqlRow sqlRow :rs ) {
            nu= sqlRow.getInteger("nu");
        }
        if(nu==0){
            desc="识别标识未登记!";
        }
        return desc;
    }


    /**
     * 检查识别标识是否存在身份信息中
     * @return
     */
    public String checkCustno(String Custno) throws Exception{
        String desc = "";
        int nu=0;
        String sql = " select  count(1) as nu from ( select CUST_NO from app_cust_register_info where CUST_NO= '"+Custno+"'"
                +" union all select CUST_NO from app_cust_register_info_h where CUST_NO='"+Custno+"') a" ;
        List<SqlRow> rs = super.findRows(sql);
        if (rs != null && rs.size() > 0) {
            nu= rs.get(0).getInteger("nu");
        }
        if(nu==0){
            desc="识别标识未登记!";
        }
        return desc;
    }

    /**
     * 检查产品登记编码是否存在
     */
    public boolean checkZZProdCode(String prod_code) throws Exception {
        String checkFlag =  SysUtil.getSystemParamsByParaid("app_ifchekCust");
        boolean ifEx = false;
        if("1".equals(checkFlag)){
            String sql = "select 1 from app_prod_reg_relation t where t.reg_code = '" +prod_code+"'" ;
            List<SqlRow> rs = super.findRows(sql);

            if (rs != null && rs.size() > 0) {
                ifEx = true;
            }
        }else{
            ifEx = true;
        }
        return ifEx;
    }








    public void deleteRegisterInfo(String workDate) throws Exception {
        String sql_del = "delete from app_cust_register_info_erdesc where theory_report_start_date='" + workDate +"'";
        super.update(sql_del);
    }
    public void deleteVolRgInfo(String workDate) throws Exception {
        String sql_del = "delete from app_cust_vol_register_info_erdesc where theory_report_start_date='" + workDate +"'";
        super.update(sql_del);
    }
    public void deleteTransInfo(String workDate) throws Exception {
        String sql_del = "delete from app_cust_trans_info_erdesc where theory_report_start_date='" + workDate + "'";
        super.update(sql_del);
    }


    public List<SqlRow> findTransInfoSerno() throws Exception {
        String sql_check_rep = "select t.register_serno,t.cnt from  (select r.register_serno,count(1) cnt from app_cust_trans_info r group by r.register_serno)t where t.cnt>1";
        return super.findRows(sql_check_rep);
    }

    public List<SqlRow> findTransInfoBankCode() throws Exception {
        String sql_check_rep = "select bank_code,prod_code,trans_serno,cnt from (select bank_code,prod_code,trans_serno,count(1) cnt " +
                " from app_cust_trans_info group by bank_code,prod_code,trans_serno  ) a where cnt>1";
        return super.findRows(sql_check_rep);
    }

    public List<SqlRow> findTransInfoProdCode() throws Exception {
        String sql_check_rep = "select prod_code,trans_serno,bank_code , cnt from (select prod_code,trans_serno,bank_code ,count(1) cnt from" +
                " (select prod_code,trans_serno,bank_code from app_cust_trans_info union all select prod_code,trans_serno,bank_code " +
                " from app_cust_trans_info_h where register_status='1') aa group by prod_code,trans_serno,bank_code) a where cnt>1 ";
        return super.findRows(sql_check_rep);
    }



    public void insertInfoErrorDesc(Map<String , Object> param) throws Exception{

        super.doTrans(() -> {
            String sql_del = "delete from app_cust_register_info_erdesc where register_serno=$S{register_serno}  and  theory_report_start_date=$S{theory_report_start_date} ";
            String sql = "insert into app_cust_register_info_erdesc ("
                    + "bank_code_desc,is_belong_desc,iss_bank_name_desc,iss_bank_code_desc,in_out_sign_desc,"
                    + "iss_country_desc,data_type_desc,ori_cust_no_desc,cust_no_desc,cust_type_desc,"
                    + "personal_id_type_desc,organization_id_type_desc,other_id_name_desc,id_code_desc,"
                    + "spv_open_bank_desc,other_open_bank_desc,cust_name_desc,"
                    + "sex_desc,risk_level_desc,moble_desc,tel_phone_desc,email_desc,remark_desc,register_serno,theory_report_start_date"
                    + ") values ("
                    + "$S{bank_code_desc},$S{is_belong_desc},$S{iss_bank_name_desc},$S{iss_bank_code_desc},$S{in_out_sign_desc},"
                    + "$S{iss_country_desc},$S{data_type_desc},$S{ori_cust_no_desc},$S{cust_no_desc},$S{cust_type_desc},"
                    + "$S{personal_id_type_desc},$S{organization_id_type_desc},$S{other_id_name_desc},$S{id_code_desc},"
                    + "$S{spv_open_bank_desc},$S{other_open_bank_desc},$S{cust_name_desc},"
                    + "$S{sex_desc},$S{risk_level_desc},$S{moble_desc},$S{tel_phone_desc},$S{email_desc},$S{remark_desc},$S{register_serno},$S{theory_report_start_date})";

            super.update(sql_del, param);
            super.update(sql, param);

        });
    }

    public void insertVolErrorDesc(Map<String , Object> param) throws Exception{

        super.doTrans(() -> {
            String sql_del = "delete from app_cust_vol_register_info_erdesc where register_serno=$S{register_serno}  and  theory_report_start_date=$S{theory_report_start_date} ";
            String sql = "insert into app_cust_vol_register_info_erdesc ("
                    + "bank_code_desc,prod_code_desc,cust_no_desc," +
                    "hold_date_desc,cur_desc,hold_vol_desc,hold_amt_desc," +
                    "convert_rmb_desc,theory_report_start_date,register_serno) values ("
                    + "$S{bank_code_desc},$S{prod_code_desc},$S{cust_no_desc}," +
                    "$S{hold_date_desc},$S{cur_desc},$S{hold_vol_desc}," +
                    "$S{hold_amt_desc},$S{convert_rmb_desc},$S{theory_report_start_date},$S{register_serno})";

            super.update(sql_del, param);
            super.update(sql, param);

        });
    }

    public void insertDetailErrorDesc(Map<String,Object> param) throws Exception{
        super.doTrans(() -> {
            String sql_del = "delete from app_cust_trans_info_erdesc where theory_report_start_date=$S{theory_report_start_date} ";
            String sql = "insert into app_cust_trans_info_erdesc ("
                    +"theory_report_start_date,register_serno,bank_code_desc,trans_serno_desc,contract_no_desc,"
                    +"fnc_trans_acct_no_desc,host_cust_no_desc,cust_no_desc,cust_name_desc,"
                    +"acct_no_desc,acct_loc_code_desc,is_agent_desc,agent_bank_code_desc,"
                    +"agent_bank_name_desc,agent_regu_code_desc,prod_code_desc,busi_code_desc,"
                    +"busi_regu_code_desc,ack_date_desc,ack_time_desc,cur_desc,ack_amt_desc,"
                    +"convert_rmb_desc,nav_desc,ack_vol_desc,fee_amt_desc,channel_flag_desc,"
                    +"inputuser_desc,remark_desc,register_serno_desc"
                    + ") values ("
                    +"$S{theory_report_start_date},$S{register_serno},$S{bank_code_desc},$S{trans_serno_desc},$S{contract_no_desc},"
                    +"$S{fnc_trans_acct_no_desc},$S{host_cust_no_desc},$S{cust_no_desc},$S{cust_name_desc},"
                    +"$S{acct_no_desc},$S{acct_loc_code_desc},$S{is_agent_desc},$S{agent_bank_code_desc},"
                    +"$S{agent_bank_name_desc},$S{agent_regu_code_desc},$S{prod_code_desc},$S{busi_code_desc},"
                    +"$S{busi_regu_code_desc},$S{ack_date_desc},$S{ack_time_desc},$S{cur_desc},$S{ack_amt_desc},"
                    +"$S{convert_rmb_desc},$S{nav_desc},$S{ack_vol_desc},$S{fee_amt_desc},$S{channel_flag_desc},"
                    +"$S{inputuser_desc},$S{remark_desc},$S{register_serno_desc})";

            super.update(sql_del,param);
            super.update(sql,param);
        });
    }

    /**
     * 查询数据日期未通过校验的指标
     * @return
     * @throws Exception
     */

    public List<Object> findUnPassIndexCode(IndexCodeDTO indexCodeDTO) throws Exception {

        // 查询一、二期表需要报送的数据所属校验指标
        String sql = "select VALIDATE_RESULT from base_data_validation ";
        List<SqlRow> sqlRows = super.findRows(sql, indexCodeDTO);

        return sqlRows.stream().map(o -> o.get("index_code")).collect(Collectors.toList());
    }


    /**
     * 查询数据日期需要校验的指标
     * @return
     * @throws Exception
     */

    public List<IndexCodeDTO> findRequiredIndexCode(IndexCodeDTO indexCodeDTO) throws Exception {

        // 查询一、二期表需要报送的数据所属校验指标
        String sql = "select index_code, report_table, relation_tables, tables_relationships " +
                "from base_reportdata_index_config where report_table = LOWER($S{reportTable}) and is_effect = '01'";
        List<SqlRow> sqlRows = super.findRows(sql, indexCodeDTO);

        return sqlRows.stream().map(
                o -> new IndexCodeDTO()
                        .setIndexCode(String.valueOf(o.get("index_code")))
                        .setDealDate(indexCodeDTO.getDealDate())
                        .setReportTable(indexCodeDTO.getReportTable())
                        .setRelationTables(String.valueOf(o.get("relation_tables")))
                        .setTableRelationships(String.valueOf(o.get("tables_relationships")))
        ).collect(Collectors.toList());
    }

    /**
     * 获取指标类型
     * @param report_table
     * @return
     * @throws Exception
     */
    public String getReportType(String report_table) throws Exception {
        String report_type = "02";
        String sql = "select report_catgory as report_type from base_report_info where report_table='"+report_table+"'";
        List<SqlRow> sqlRows = super.findRows(sql);
        for (SqlRow sqlRow : sqlRows){
            report_type=  sqlRow.getString("report_type");
        }
        return report_type;
    }

}
