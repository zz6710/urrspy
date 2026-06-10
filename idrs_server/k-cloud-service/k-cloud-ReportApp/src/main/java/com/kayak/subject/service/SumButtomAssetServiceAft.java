package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.SumButtomAssetDaoAft;
import com.kayak.subject.model.SumButtomAssetAft;
import com.kayak.subject.model.SumButtomAssetAftExcl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "底层估值明细表（调整后）服务", model = SumButtomAssetAft.class)
public class SumButtomAssetServiceAft implements ExcelImportService<SumButtomAssetAftExcl> {

	@Autowired
	private ComnDao comnDao;

	@Autowired
	private SumButtomAssetDaoAft sumButtomAssetDaoAft;

	@API(desc = "查询底层估值明细表（调整后）信息", auth = APIAuth.YES)
	public SqlResult<SumButtomAssetAft> findSumButtomAssetAfts(SqlParam<SumButtomAssetAft> params) throws Exception {
		return sumButtomAssetDaoAft.findSumButtomAssetAfts(params);
	}

	@API(desc = "添加底层估值明细表（调整后）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int addSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return sumButtomAssetDaoAft.addSumButtomAssetAft(params).getEffect();
	}
	
	@API(desc = "修改底层估值明细表（调整后）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int updateSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return sumButtomAssetDaoAft.updateSumButtomAssetAft(params).getEffect();
	}
	
	@API(desc = "删除底层估值明细表（调整后）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int deleteSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return sumButtomAssetDaoAft.deleteSumButtomAssetAft(params).getEffect();
	}

	public String importSumButtomAssetAft(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入底层估值明细表（调整后）【{}】开始",file.getOriginalFilename());
		final SumButtomAssetServiceAft sumButtomAssetServiceAft = this;
		ExcelImportListener<SumButtomAssetAftExcl> excelImportListener = new ExcelImportListener<SumButtomAssetAftExcl>(params) {
			@Override
			protected ExcelImportService<SumButtomAssetAftExcl> getImportService() { return sumButtomAssetServiceAft; }
		};

		String inputDate = (String) params.get("inputDate");
		SumButtomAssetAft param = new SumButtomAssetAft();
		param.setInputDate(inputDate);
		//先删后插
		sumButtomAssetDaoAft.deleteSumButtomAssetAftByInputDate(param);
		log.info("删除 dwd_sum_buttom_asset 表数据，日期为：{}", inputDate);

		try {
			EasyExcel.read(file.getInputStream())
					.head(SumButtomAssetAftExcl.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		createImportLog(inputDate);
		// 更新完后调用语句更新下
		comnDao.update(ExeQuery.queryExeId("G06EU001"), params);
		log.info("导入底层估值明细表（调整后）【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Override
	public void importFile(List<SumButtomAssetAftExcl> list, Map map) throws Exception {
		long startTime = System.currentTimeMillis();

		comnDao.doTrans(() -> {
			PreparedStatement ps = getPreparedStatement(list);
			try {
				String reportDate = (String) map.get("inputDate");
				for (SumButtomAssetAftExcl sumButtomAssetAftExcl : list) {
					resolveSumButtomAssetAft(sumButtomAssetAftExcl);
					sumButtomAssetAftExcl.setInputDate(reportDate);
					ps.setString(1, sumButtomAssetAftExcl.getInputDate());
					ps.setString(2, sumButtomAssetAftExcl.getIcode());
					ps.setString(3, sumButtomAssetAftExcl.getInamec1());
					ps.setString(4, sumButtomAssetAftExcl.getInamec2());
					ps.setString(5, sumButtomAssetAftExcl.getBottomCode());
					ps.setString(6, sumButtomAssetAftExcl.getItemName());
					ps.setString(7, sumButtomAssetAftExcl.getComcode());
					ps.setString(8, sumButtomAssetAftExcl.getCost());
					ps.setString(9, sumButtomAssetAftExcl.getAmount());
					ps.setString(10, sumButtomAssetAftExcl.getCurrency());
					ps.setString(11, sumButtomAssetAftExcl.getG06Type());
					ps.setString(12, sumButtomAssetAftExcl.getRatLevel());
					ps.setString(13, sumButtomAssetAftExcl.getIsPppPart());
					ps.setString(14, sumButtomAssetAftExcl.getIsMktBtsRlt());
					ps.setString(15, sumButtomAssetAftExcl.getIsGovFncPart());
					ps.setString(16, sumButtomAssetAftExcl.getIsRealSetate());
					ps.setString(17, sumButtomAssetAftExcl.getIsFncStk());
					ps.setString(18, sumButtomAssetAftExcl.getIsFncBnd());
					ps.setString(19, sumButtomAssetAftExcl.getIsFncScdBnd());
					ps.setString(20, sumButtomAssetAftExcl.getIsFncTsfBnd());
					ps.setString(21, sumButtomAssetAftExcl.getIsOthBnkTls());
					ps.setString(22, sumButtomAssetAftExcl.getIsGovSpcBnd());
					ps.setString(23, sumButtomAssetAftExcl.getExchangeRate());
					ps.setString(24, sumButtomAssetAftExcl.getInvValRate());
					ps.setString(25, sumButtomAssetAftExcl.getIsCshMng());
					ps.setString(26, sumButtomAssetAftExcl.getInvValRateCsh());
					ps.setString(27, sumButtomAssetAftExcl.getNonGrtRate());
					ps.setString(28, sumButtomAssetAftExcl.getNonGrtAmt());
					ps.setString(29, sumButtomAssetAftExcl.getManageMode());
					ps.setString(30, sumButtomAssetAftExcl.getMark());
					ps.setString(31, sumButtomAssetAftExcl.getPenInvF());
					ps.setString(32, sumButtomAssetAftExcl.getInvValRatePen());
					ps.setString(33, sumButtomAssetAftExcl.getPerPenInvF());
					ps.setString(34, sumButtomAssetAftExcl.getInvValRatePerPen());
					ps.setString(35, sumButtomAssetAftExcl.getHkInv());
					ps.setString(36, sumButtomAssetAftExcl.getQdiiInv());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入dwd_sum_buttom_asset异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}

	private PreparedStatement getPreparedStatement(List<SumButtomAssetAftExcl> list) throws Exception {
		String batchSql = "insert into dwd_sum_buttom_asset (input_date,i_code,i_name_c1,i_name_c2,bottom_code,item_name,import_date,comcode,cost,amount,currency,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_real_setate,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,is_csh_mng,inv_val_rate_csh,non_grt_rate,non_grt_amt,manage_mode,mark,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,hk_inv,qdii_inv) " +
				" values (?, ?, ?, ?, ?, ?, date_format(now() ,'%Y%m%d'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		if (list == null || list.isEmpty()) {
			throw new Exception("Excl无数据");
		}

		Connection connection = comnDao.getConnection();
		PreparedStatement ps = connection.prepareStatement(batchSql);
		return ps;
	}

	private void resolveSumButtomAssetAft(SumButtomAssetAftExcl sumButtomAssetAftExcl){
		sumButtomAssetAftExcl.setG06Type(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getG06Type()) ? sumButtomAssetAftExcl.getG06Type().split(" ")[0] : sumButtomAssetAftExcl.getG06Type());
		sumButtomAssetAftExcl.setIsPppPart(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsPppPart()) ? sumButtomAssetAftExcl.getIsPppPart().split(" ")[0] : sumButtomAssetAftExcl.getIsPppPart());
		sumButtomAssetAftExcl.setIsMktBtsRlt(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsMktBtsRlt()) ? sumButtomAssetAftExcl.getIsMktBtsRlt().split(" ")[0] : sumButtomAssetAftExcl.getIsMktBtsRlt());
		sumButtomAssetAftExcl.setIsGovFncPart(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsGovFncPart()) ? sumButtomAssetAftExcl.getIsGovFncPart().split(" ")[0] : sumButtomAssetAftExcl.getIsGovFncPart());
		sumButtomAssetAftExcl.setIsRealSetate(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsRealSetate()) ? sumButtomAssetAftExcl.getIsRealSetate().split(" ")[0] : sumButtomAssetAftExcl.getIsRealSetate());
		sumButtomAssetAftExcl.setIsFncStk(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsFncStk()) ? sumButtomAssetAftExcl.getIsFncStk().split(" ")[0] : sumButtomAssetAftExcl.getIsFncStk());
		sumButtomAssetAftExcl.setIsFncBnd(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsFncBnd()) ? sumButtomAssetAftExcl.getIsFncBnd().split(" ")[0] : sumButtomAssetAftExcl.getIsFncBnd());
		sumButtomAssetAftExcl.setIsFncScdBnd(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsFncScdBnd()) ? sumButtomAssetAftExcl.getIsFncScdBnd().split(" ")[0] : sumButtomAssetAftExcl.getIsFncScdBnd());
		sumButtomAssetAftExcl.setIsFncTsfBnd(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsFncTsfBnd()) ? sumButtomAssetAftExcl.getIsFncTsfBnd().split(" ")[0] : sumButtomAssetAftExcl.getIsFncTsfBnd());
		sumButtomAssetAftExcl.setIsOthBnkTls(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsOthBnkTls()) ? sumButtomAssetAftExcl.getIsOthBnkTls().split(" ")[0] : sumButtomAssetAftExcl.getIsOthBnkTls());
		sumButtomAssetAftExcl.setIsGovSpcBnd(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsGovSpcBnd()) ? sumButtomAssetAftExcl.getIsGovSpcBnd().split(" ")[0] : sumButtomAssetAftExcl.getIsGovSpcBnd());
		sumButtomAssetAftExcl.setIsCshMng(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getIsCshMng()) ? sumButtomAssetAftExcl.getIsCshMng().split(" ")[0] : sumButtomAssetAftExcl.getIsCshMng());
		if (StringUtils.isNotEmpty(sumButtomAssetAftExcl.getRatLevel())) {
			String rateLevel = "";
			String value = sumButtomAssetAftExcl.getRatLevel();
			if (value.contains("/")) {
				String[] valueArr = value.split("/");
				for (String str : valueArr) {
					if (StringUtils.isEmpty(rateLevel)) {
						rateLevel = str.split(" ")[0];
					} else {
						rateLevel += "/" + str.split(" ")[0];
					}
				}
			} else if (value.contains(",")) {
				String[] valueArr = value.split(",");
				for (String str : valueArr) {
					if (StringUtils.isEmpty(rateLevel)) {
						rateLevel = str.split(" ")[0];
					} else {
						rateLevel += "," + str.split(" ")[0];
					}
				}
			} else {
				rateLevel = value.split(" ")[0];
			}
			sumButtomAssetAftExcl.setRatLevel(rateLevel);
		}
		sumButtomAssetAftExcl.setManageMode(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getManageMode()) ? sumButtomAssetAftExcl.getManageMode().split(" ")[0] : sumButtomAssetAftExcl.getManageMode());
		sumButtomAssetAftExcl.setPenInvF(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getPenInvF()) ? sumButtomAssetAftExcl.getPenInvF().split(" ")[0] : sumButtomAssetAftExcl.getPenInvF());
		sumButtomAssetAftExcl.setPerPenInvF(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getPerPenInvF()) ? sumButtomAssetAftExcl.getPerPenInvF().split(" ")[0] : sumButtomAssetAftExcl.getPerPenInvF());
		sumButtomAssetAftExcl.setHkInv(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getHkInv()) ? sumButtomAssetAftExcl.getHkInv().split(" ")[0] : sumButtomAssetAftExcl.getHkInv());
		sumButtomAssetAftExcl.setQdiiInv(StringUtils.isNotEmpty(sumButtomAssetAftExcl.getQdiiInv()) ? sumButtomAssetAftExcl.getQdiiInv().split(" ")[0] : sumButtomAssetAftExcl.getQdiiInv());

	}

	public void createImportLog(String reportDate) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("create_date", DateUtil.getNowDate());
		params.put("summit_user", SysUtil.getSysUserParamValue("sys_user_userid"));
		params.put("create_time", DateUtil.getNowTime());
		params.put("input_dt", reportDate);
		params.put("table_nm", "dwd_sum_buttom_asset");
		String sql = "insert into G06_mezzanine_remark(create_date,summit_user,create_time,input_dt,table_nm)values($S{create_date},$S{summit_user},$S{create_time},$S{input_dt},$S{table_nm})";
		comnDao.update(sql,params);
	}
}
