package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.dataMerge.model.CustomerDataMergeModel;
import com.kayak.rpt.zz.manage.model.InvestorBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class InvestorBaseMaintainDao extends ComnDao {

    /**
     * 根据查询条件查询全量投资者基本信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<InvestorBaseInfo> queryInvestorBaseInfoByCond (SqlParam<InvestorBaseInfo> params) throws Exception {
        String sql = "select id, bank_code, cust_no, inner_cust_no, ori_cust_no, is_belong, iss_bank_name, iss_bank_code, in_out_sign, iss_country, \n" +
                     "       cust_type, personal_id_type, organization_id_type, other_id_name, id_code, spv_open_bank, other_open_bank, cust_name, sex, risk_level,\n" +
                     "       moble, tel_phone, email, ta_id, channel_code, cust_mark, remark, deal_date, crt_user, upd_user, crt_dt, upd_dt, data_type, \n" +
                     "       cust_name as cust_name_display, id_code as id_code_display, moble as moble_display, tel_phone as tel_phone_display, email as email_display \n" +
                     "  from ods_cust_base_inf b" +
                     " where 1 = 1 and end_dt = '20991231'";
        if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
            sql = sql + " and b.cust_no = '" + params.getModel().getCustNo() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getTaId())) {
            sql = sql + " and b.ta_id = '" + params.getModel().getTaId() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCustType())) {
            sql = sql + " and b.cust_type in (" + SysUtil.inStr(params.getModel().getCustType()) + ") ";
        }
        if (StringUtils.isNotBlank(params.getModel().getIdCode())) {
            sql = sql + " and b.id_code = '" + params.getModel().getIdCode() + "' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getPersonalIdType())) {
            sql = sql + " and b.personal_id_type in (" + SysUtil.inStr(params.getModel().getPersonalIdType()) + ") ";
        }
        if (StringUtils.isNotBlank(params.getModel().getOrganizationIdType())) {
            sql = sql + " and b.organization_id_type in (" + SysUtil.inStr(params.getModel().getOrganizationIdType()) + ") ";
        }
        if (StringUtils.isNotBlank(params.getModel().getDealDate())) {
            sql = sql + " and b.end_dt = '" + params.getModel().getDealDate() + "' ";
        }
        sql += " order by cust_no " ;
        return super.findRows(sql,params);
    }

    /**
     * 插入全量投资者基本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult putInvestorBaseInfo (SqlParam<InvestorBaseInfo> params) throws Exception {
        params.getModel().setCrtUser(String.valueOf(SysUtil.getSysUserParams().get("userid")));
        String sql = "insert into ods_cust_base_inf (bank_code, cust_no, inner_cust_no, ori_cust_no, is_belong, iss_bank_name, iss_bank_code, in_out_sign, iss_country,\n" +
                     "       cust_type, personal_id_type, organization_id_type, other_id_name, id_code, spv_open_bank, other_open_bank, cust_name, sex, risk_level,\n" +
                     "       moble, tel_phone, email, ta_id, channel_code, cust_mark, remark, deal_date, crt_user, upd_user, crt_dt, upd_dt, data_type, strt_dt, end_dt) " +
                     "values ($S{bankCode}, $S{custNo}, $S{innerCustNo}, $S{oriCustNo}, $S{isBelong}, $S{issBankName}, $S{issBankCode}, $S{inOutSign}, $S{issCountry}, $S{custType}, " +
                     "       $S{personalIdType}, $S{organizationIdType}, $S{otherIdName}, $S{idCode}, $S{spvOpenBank}, $S{otherOpenBank}, $S{custName}, $S{sex}, $S{riskLevel}, $S{moble}, " +
                     "       $S{telPhone}, $S{email}, $S{taId}, $S{channelCode}, $S{custMark}, $S{remark}, date_format(sysdate(), '%Y%m%d'), $S{crtUser}, null, date_format(sysdate(), '%Y%m%d'), null," +
                     "       '02', $S{strtDt}, $S{endDt} )";/*02-手工新增*/
        return super.update(sql, params.getModel());
    }

    /**
     * 根据唯一标识更新全量投资者信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateInvestorBaseInfo (SqlParam<InvestorBaseInfo> params) throws Exception {
        params.getModel().setUpdUser(String.valueOf(SysUtil.getSysUserParams().get("userid")));
        String sql = "update ods_cust_base_inf b \n" +
                     "   set b.bank_code = $S{bankCode}, b.cust_no = $S{custNo},b.inner_cust_no = $S{innerCustNo}, \n" +
                     "       b.ori_cust_no = $S{oriCustNo}, b.is_belong = $S{isBelong}, b.iss_bank_name = $S{issBankName}, \n" +
                     "       b.iss_bank_code = $S{issBankCode}, b.in_out_sign = $S{inOutSign}, b.iss_country = $S{issCountry}, \n" +
                     "       b.cust_type = $S{custType}, b.personal_id_type = $S{personalIdType}, b.organization_id_type = $S{organizationIdType}, \n" +
                     "       b.other_id_name = $S{otherIdName}, b.id_code = $S{idCodeDisplay}, b.spv_open_bank = $S{spvOpenBank}, \n" +
                     "       b.other_open_bank = $S{otherOpenBank}, b.cust_name = $S{custNameDisplay}, b.sex = $S{sex}, \n" +
                     "       b.risk_level = $S{riskLevel}, b.moble = $S{mobleDisplay}, b.tel_phone = $S{telPhoneDisplay}, \n" +
                     "       b.email = $S{emailDisplay}, b.ta_id = $S{taId}, b.channel_code = $S{channelCode}, \n" +
                     "       b.cust_mark = $S{custMark}, b.remark = $S{remark}, b.upd_user = $S{updUser}, \n" +
                     "       b.upd_dt = date_format(sysdate(), '%Y%m%d'), b.data_type = $S{dataType} \n" +
                     " where b.id = $S{id} ";
        return super.update(sql, params.getModel());
    }

    /**
     * 判断客户标识是否存在
     * 存在时返回提示信息
     * @param params
     * @return
     * @throws Exception
     */
    public boolean judgeInverstorExists (SqlParam<InvestorBaseInfo> params) throws Exception {
        boolean is_exists = false;
        String querySql = "select * from ods_cust_base_inf where cust_no = '" + params.getModel().getCustNo() + "'";
        if(super.findRows(querySql, params.getModel()).size() > 0){
            is_exists = true;
        }
        return is_exists;
    }

    /**
     * 根据id删除全量投资者基本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteInvestorBaseInfo (SqlParam<CustomerDataMergeModel> params) throws Exception {
        return super.update("delete from ods_cust_base_inf where id = $S{id}", params.getModel());
    }

}
