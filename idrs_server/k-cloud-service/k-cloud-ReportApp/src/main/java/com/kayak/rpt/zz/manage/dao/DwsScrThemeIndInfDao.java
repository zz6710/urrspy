package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.DwsScrThemeIndInf;
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
public class DwsScrThemeIndInfDao extends ComnDao {

	public SqlResult<DwsScrThemeIndInf> findDwsScrThemeIndInfs(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return super.findRows("SELECT id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time FROM dws_scr_theme_ind_inf", params);
	}

	public UpdateResult addDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return super.update("INSERT INTO dws_scr_theme_ind_inf(id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$S{reportDate},$S{scrCd},$S{assDebtType},$D{amount},$S{isfintech},$S{isgreen},$S{isinclusive},$S{ispension},$S{isdigital},$S{ispollution},$S{cmpBlgZon},$S{proBlgZon},$S{cmpNm},$S{cmpSocialCd},$S{cmpBlgFintech},$S{fintechTyp1},$S{fintechTyp2},$S{fintechTyp3},$S{fintechTyp4},$S{fintechTyp5},$S{fintechTyp6},$S{fintechTyp7},$S{fintechTyp8},$S{cmpBlgGreen},$S{cmpBlgInclusive},$S{inclusiveTyp1},$S{inclusiveTyp2},$S{cmpBlgPension},$S{cmpBlgDigital},$S{digitalTyp1},$S{digitalTyp2},$S{dealDate},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return super.update("UPDATE dws_scr_theme_ind_inf SET report_date=$S{reportDate} ,scr_cd=$S{scrCd} ,ass_debt_type=$S{assDebtType} ,amount=$D{amount} ,isfintech=$S{isfintech} ,isgreen=$S{isgreen} ,isinclusive=$S{isinclusive} ,ispension=$S{ispension} ,isdigital=$S{isdigital} ,ispollution=$S{ispollution} ,cmp_blg_zon=$S{cmpBlgZon} ,pro_blg_zon=$S{proBlgZon} ,cmp_nm=$S{cmpNm} ,cmp_social_cd=$S{cmpSocialCd} ,cmp_blg_fintech=$S{cmpBlgFintech} ,fintech_typ1=$S{fintechTyp1} ,fintech_typ2=$S{fintechTyp2} ,fintech_typ3=$S{fintechTyp3} ,fintech_typ4=$S{fintechTyp4} ,fintech_typ5=$S{fintechTyp5} ,fintech_typ6=$S{fintechTyp6} ,fintech_typ7=$S{fintechTyp7} ,fintech_typ8=$S{fintechTyp8} ,cmp_blg_green=$S{cmpBlgGreen} ,cmp_blg_inclusive=$S{cmpBlgInclusive} ,inclusive_typ1=$S{inclusiveTyp1} ,inclusive_typ2=$S{inclusiveTyp2} ,cmp_blg_pension=$S{cmpBlgPension} ,cmp_blg_digital=$S{cmpBlgDigital} ,digital_typ1=$S{digitalTyp1} ,digital_typ2=$S{digitalTyp2} ,upd_date=DATE_FORMAT(NOW(),'%Y%m%d') ,upd_time=DATE_FORMAT(NOW(),'%H%i%s')  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return super.update("DELETE FROM dws_scr_theme_ind_inf WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importDwsScrThemeIndInf(List<DwsScrThemeIndInf> dwsScrThemeIndInfs, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM dws_scr_theme_ind_inf WHERE report_date=$S{reportDate} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO dws_scr_theme_ind_inf(report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				for (DwsScrThemeIndInf dwsScrThemeIndInf : dwsScrThemeIndInfs) {
					preparedStatement.setString(1, (String) params.get("reportDate"));
					preparedStatement.setString(2, dwsScrThemeIndInf.getScrCd());
					preparedStatement.setString(3, dwsScrThemeIndInf.getAssDebtType());
					String amount = dwsScrThemeIndInf.getAmount();
					preparedStatement.setBigDecimal(4, StringUtils.isEmpty(amount) ? null : new BigDecimal(amount.trim()));
					preparedStatement.setString(5, dwsScrThemeIndInf.getIsfintech());
					preparedStatement.setString(6, dwsScrThemeIndInf.getIsgreen());
					preparedStatement.setString(7, dwsScrThemeIndInf.getIsinclusive());
					preparedStatement.setString(8, dwsScrThemeIndInf.getIspension());
					preparedStatement.setString(9, dwsScrThemeIndInf.getIsdigital());
					preparedStatement.setString(10, dwsScrThemeIndInf.getIspollution());
					preparedStatement.setString(11, dwsScrThemeIndInf.getCmpBlgZon());
					preparedStatement.setString(12, dwsScrThemeIndInf.getProBlgZon());
					preparedStatement.setString(13, dwsScrThemeIndInf.getCmpNm());
					preparedStatement.setString(14, dwsScrThemeIndInf.getCmpSocialCd());
					preparedStatement.setString(15, dwsScrThemeIndInf.getCmpBlgFintech());
					preparedStatement.setString(16, dwsScrThemeIndInf.getFintechTyp1());
					preparedStatement.setString(17, dwsScrThemeIndInf.getFintechTyp2());
					preparedStatement.setString(18, dwsScrThemeIndInf.getFintechTyp3());
					preparedStatement.setString(19, dwsScrThemeIndInf.getFintechTyp4());
					preparedStatement.setString(20, dwsScrThemeIndInf.getFintechTyp5());
					preparedStatement.setString(21, dwsScrThemeIndInf.getFintechTyp6());
					preparedStatement.setString(22, dwsScrThemeIndInf.getFintechTyp7());
					preparedStatement.setString(23, dwsScrThemeIndInf.getFintechTyp8());
					preparedStatement.setString(24, dwsScrThemeIndInf.getCmpBlgGreen());
					preparedStatement.setString(25, dwsScrThemeIndInf.getCmpBlgInclusive());
					preparedStatement.setString(26, dwsScrThemeIndInf.getInclusiveTyp1());
					preparedStatement.setString(27, dwsScrThemeIndInf.getInclusiveTyp2());
					preparedStatement.setString(28, dwsScrThemeIndInf.getCmpBlgPension());
					preparedStatement.setString(29, dwsScrThemeIndInf.getCmpBlgDigital());
					preparedStatement.setString(30, dwsScrThemeIndInf.getDigitalTyp1());
					preparedStatement.setString(31, dwsScrThemeIndInf.getDigitalTyp2());
					preparedStatement.setString(32, DateUtil.getNowDate());
					preparedStatement.setString(33, DateUtil.getNowDate());
					preparedStatement.setString(34, DateUtil.getNowTime());
					preparedStatement.setString(35, DateUtil.getNowDate());
					preparedStatement.setString(36, DateUtil.getNowTime());
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
