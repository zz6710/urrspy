package com.kayak.rpt.rhzy.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.rpt.rhzg.service.ZG04Service;
import com.kayak.rpt.rhzy.dao.InterbankDepositInfoDao;
import com.kayak.rpt.rhzy.model.InterbankDepositInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "存量同业存款信息服务", model = InterbankDepositInfo.class)
public class InterbankDepositInfoService implements ExcelImportService<InterbankDepositInfo> {
	private static final Logger log = LoggerFactory.getLogger(InterbankDepositInfoService.class);

	@Autowired
	private InterbankDepositInfoDao interbankDepositInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询存量同业存款信息信息", auth = APIAuth.YES)
	public SqlResult<InterbankDepositInfo> findInterbankDepositInfos(SqlParam<InterbankDepositInfo> params) throws Exception {
		return interbankDepositInfoDao.findInterbankDepositInfos(params);
	}
	
	@API(desc = "修改存量同业存款信息", params = "id,msg_typ,report_date,prod_cd,prod_nm,isu_org_cd,isu_org_nm,prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt,clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr,ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f,ownership_trust_f,base_open_info_f,change_reason,back1,back2,back3,back4,back5,register_status,theory_report_start_date,theory_report_end_date,sys_data_source,sys_data_status,sys_data_version,trust_prod_type,prod_extension_f", auth = APIAuth.YES)
	public int updateInterbankDepositInfo(SqlParam<InterbankDepositInfo> params) throws Exception {
		return interbankDepositInfoDao.updateInterbankDepositInfo(params).getEffect();
	}
	
	@API(desc = "删除存量同业存款信息", params = "id,msg_typ,report_date,prod_cd,prod_nm,isu_org_cd,isu_org_nm,prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt,clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr,ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f,ownership_trust_f,base_open_info_f,change_reason,back1,back2,back3,back4,back5,register_status,theory_report_start_date,theory_report_end_date,sys_data_source,sys_data_status,sys_data_version,trust_prod_type,prod_extension_f", auth = APIAuth.YES)
	public int deleteInterbankDepositInfo(SqlParam<InterbankDepositInfo> params) throws Exception {
		return interbankDepositInfoDao.deleteInterbankDepositInfo(params).getEffect();
	}

	public void deleteInterbankDepositInfoByDate(Object params) throws Exception{
		try {
			interbankDepositInfoDao.deleteInterbankDepositInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void importFile(List<InterbankDepositInfo> interbankDepositInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_interbank_deposit_info`(`report_date`, `org_code`, `inner_org_code`, `busi_type`, `cntr_id_type`, `cntr_code`, `deposit_acco_code`, `deposit_protocol_code`, `protocol_start_date`, `protocol_end_date`, `cur`, `deposit_balance`, `deposit_balance_rmb`, `rate_level`, `deposit_type`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (InterbankDepositInfo info : interbankDepositInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode() == null ? null : info.getInnerOrgCode());
					ps.setString(4,info.getBusiType());
					ps.setString(5,info.getCntrIdType());
					ps.setString(6,info.getCntrCode());
					ps.setString(7,info.getDepositAccoCode());
					ps.setString(8,info.getDepositProtocolCode());
					ps.setString(9,info.getProtocolStartDate());
					ps.setString(10,info.getProtocolEndDate());
					ps.setString(11,info.getCur());
					ps.setString(12,info.getDepositBalance());
					ps.setString(13,info.getDepositBalanceRmb());
					ps.setString(14,info.getRateLevel());
					ps.setString(15,info.getDepositType());
					ps.addBatch();

				}
				ps.executeBatch();

				log.info(" ##### 批量入库{}耗时: {} ms", interbankDepositInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入资管产品存续期募集信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();

			}
		});
	}
}
