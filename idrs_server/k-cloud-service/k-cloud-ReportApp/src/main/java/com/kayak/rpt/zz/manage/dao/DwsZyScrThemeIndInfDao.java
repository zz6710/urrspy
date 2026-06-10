package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.DwsZyScrThemeIndInf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class DwsZyScrThemeIndInfDao extends ComnDao {

	public SqlResult<DwsZyScrThemeIndInf> findDwsZyScrThemeIndInfs(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return super.findRows("SELECT id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time FROM dws_zy_scr_theme_ind_inf", params);
	}

	public UpdateResult addDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return super.update("INSERT INTO dws_zy_scr_theme_ind_inf(id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$S{reportDate},$S{scrCd},$S{assDebtType},$D{amount},$S{endDate},$D{rdmTrm},$S{scrTrm},$S{rateLevel},$S{spcBondF},$S{isoverdue},$D{overdueAmt},$D{accrueAmt},$S{isfintech},$S{isgreen},$S{isinclusive},$S{ispension},$S{isdigital},$S{ispollution},$S{cmpBlgZon},$S{proBlgZon},$S{cmpNm},$S{cmpSocialCd},$S{cmpBlgFintech},$S{fintechTyp1},$S{fintechTyp2},$S{fintechTyp3},$S{fintechTyp4},$S{fintechTyp5},$S{fintechTyp6},$S{fintechTyp7},$S{fintechTyp8},$S{cmpBlgGreen},$S{cmpBlgInclusive},$S{inclusiveTyp1},$S{inclusiveTyp2},$S{cmpBlgPension},$S{cmpBlgDigital},$S{digitalTyp1},$S{digitalTyp2},$S{dealDate},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return super.update("UPDATE dws_zy_scr_theme_ind_inf SET report_date=$S{reportDate} ,scr_cd=$S{scrCd} ,ass_debt_type=$S{assDebtType} ,amount=$D{amount} ,end_date=$S{endDate} ,rdm_trm=$D{rdmTrm} ,scr_trm=$S{scrTrm} ,rate_level=$S{rateLevel} ,spc_bond_f=$S{spcBondF} ,isoverdue=$S{isoverdue} ,overdue_amt=$D{overdueAmt} ,accrue_amt=$D{accrueAmt} ,isfintech=$S{isfintech} ,isgreen=$S{isgreen} ,isinclusive=$S{isinclusive} ,ispension=$S{ispension} ,isdigital=$S{isdigital} ,ispollution=$S{ispollution} ,cmp_blg_zon=$S{cmpBlgZon} ,pro_blg_zon=$S{proBlgZon} ,cmp_nm=$S{cmpNm} ,cmp_social_cd=$S{cmpSocialCd} ,cmp_blg_fintech=$S{cmpBlgFintech} ,fintech_typ1=$S{fintechTyp1} ,fintech_typ2=$S{fintechTyp2} ,fintech_typ3=$S{fintechTyp3} ,fintech_typ4=$S{fintechTyp4} ,fintech_typ5=$S{fintechTyp5} ,fintech_typ6=$S{fintechTyp6} ,fintech_typ7=$S{fintechTyp7} ,fintech_typ8=$S{fintechTyp8} ,cmp_blg_green=$S{cmpBlgGreen} ,cmp_blg_inclusive=$S{cmpBlgInclusive} ,inclusive_typ1=$S{inclusiveTyp1} ,inclusive_typ2=$S{inclusiveTyp2} ,cmp_blg_pension=$S{cmpBlgPension} ,cmp_blg_digital=$S{cmpBlgDigital} ,digital_typ1=$S{digitalTyp1} ,digital_typ2=$S{digitalTyp2} ,upd_date=DATE_FORMAT(NOW(),'%Y%m%d') ,upd_time=DATE_FORMAT(NOW(),'%H%i%s')  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return super.update("DELETE FROM dws_zy_scr_theme_ind_inf WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importDwsZyScrThemeIndInf(List<DwsZyScrThemeIndInf> dwsZyScrThemeIndInfs, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM dws_zy_scr_theme_ind_inf WHERE report_date=$S{reportDate} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO dws_zy_scr_theme_ind_inf(report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				for (DwsZyScrThemeIndInf dwsZyScrThemeIndInf : dwsZyScrThemeIndInfs) {
					preparedStatement.setString(1, (String) params.get("reportDate"));
					preparedStatement.setString(2, dwsZyScrThemeIndInf.getScrCd());
					preparedStatement.setString(3, dwsZyScrThemeIndInf.getAssDebtType());
					String amount = dwsZyScrThemeIndInf.getAmount();
					preparedStatement.setBigDecimal(4, StringUtils.isEmpty(amount) ? null : new BigDecimal(amount.trim()));
					preparedStatement.setString(5, dwsZyScrThemeIndInf.getEndDate());
					String rdmTrm = dwsZyScrThemeIndInf.getRdmTrm();
					preparedStatement.setBigDecimal(6, StringUtils.isEmpty(rdmTrm) ? null : new BigDecimal(rdmTrm.trim()));
					preparedStatement.setString(7, dwsZyScrThemeIndInf.getScrTrm());
					preparedStatement.setString(8, dwsZyScrThemeIndInf.getRateLevel());
					preparedStatement.setString(9, dwsZyScrThemeIndInf.getSpcBondF());
					preparedStatement.setString(10, dwsZyScrThemeIndInf.getIsoverdue());
					String overdueAmt = dwsZyScrThemeIndInf.getOverdueAmt();
					preparedStatement.setBigDecimal(11, StringUtils.isEmpty(overdueAmt) ? null : new BigDecimal(overdueAmt.trim()));
					String accrueAmt = dwsZyScrThemeIndInf.getAccrueAmt();
					preparedStatement.setBigDecimal(12, StringUtils.isEmpty(accrueAmt) ? null : new BigDecimal(accrueAmt.trim()));
					preparedStatement.setString(13, dwsZyScrThemeIndInf.getIsfintech());
					preparedStatement.setString(14, dwsZyScrThemeIndInf.getIsgreen());
					preparedStatement.setString(15, dwsZyScrThemeIndInf.getIsinclusive());
					preparedStatement.setString(16, dwsZyScrThemeIndInf.getIspension());
					preparedStatement.setString(17, dwsZyScrThemeIndInf.getIsdigital());
					preparedStatement.setString(18, dwsZyScrThemeIndInf.getIspollution());
					preparedStatement.setString(19, dwsZyScrThemeIndInf.getCmpBlgZon());
					preparedStatement.setString(20, dwsZyScrThemeIndInf.getProBlgZon());
					preparedStatement.setString(21, dwsZyScrThemeIndInf.getCmpNm());
					preparedStatement.setString(22, dwsZyScrThemeIndInf.getCmpSocialCd());
					preparedStatement.setString(23, dwsZyScrThemeIndInf.getCmpBlgFintech());
					preparedStatement.setString(24, dwsZyScrThemeIndInf.getFintechTyp1());
					preparedStatement.setString(25, dwsZyScrThemeIndInf.getFintechTyp2());
					preparedStatement.setString(26, dwsZyScrThemeIndInf.getFintechTyp3());
					preparedStatement.setString(27, dwsZyScrThemeIndInf.getFintechTyp4());
					preparedStatement.setString(28, dwsZyScrThemeIndInf.getFintechTyp5());
					preparedStatement.setString(29, dwsZyScrThemeIndInf.getFintechTyp6());
					preparedStatement.setString(30, dwsZyScrThemeIndInf.getFintechTyp7());
					preparedStatement.setString(31, dwsZyScrThemeIndInf.getFintechTyp8());
					preparedStatement.setString(32, dwsZyScrThemeIndInf.getCmpBlgGreen());
					preparedStatement.setString(33, dwsZyScrThemeIndInf.getCmpBlgInclusive());
					preparedStatement.setString(34, dwsZyScrThemeIndInf.getInclusiveTyp1());
					preparedStatement.setString(35, dwsZyScrThemeIndInf.getInclusiveTyp2());
					preparedStatement.setString(36, dwsZyScrThemeIndInf.getCmpBlgPension());
					preparedStatement.setString(37, dwsZyScrThemeIndInf.getCmpBlgDigital());
					preparedStatement.setString(38, dwsZyScrThemeIndInf.getDigitalTyp1());
					preparedStatement.setString(39, dwsZyScrThemeIndInf.getDigitalTyp2());
					preparedStatement.setString(40, DateUtil.getNowDate());
					preparedStatement.setString(41, DateUtil.getNowDate());
					preparedStatement.setString(42, DateUtil.getNowTime());
					preparedStatement.setString(43, DateUtil.getNowDate());
					preparedStatement.setString(44, DateUtil.getNowTime());
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				throw e;
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		});
	}

}
