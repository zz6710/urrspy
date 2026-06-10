package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.subject.model.DwsProdTTRDBefOri;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class DwsProdTTRDBefOriDao extends ComnDao {

	public SqlResult<DwsProdTTRDBefOri> findDwsProdTTRDBefOris(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return super.findRows("SELECT id,product_code,i_code as icode,asset_third_type,i_name as iname,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,csh_mng_f,hldn_qntt,pen_inv_f,per_pen_inv_f,hk_inv,qdii_inv,recvbl_prnc FROM dws_prod_ttrd_bef_g06a2_ori", params);
	}

	public UpdateResult addDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return super.update("INSERT INTO dws_prod_ttrd_bef_g06a2_ori(id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,csh_mng_f,pen_inv_f,per_pen_inv_f) VALUES($AUTOIDI{id},$S{productCode},$S{icode},$S{assetThirdType},$S{iname},$D{amount},$D{changerate},$D{investedamountcny},$S{orgClassific},$S{orderfreemanage},$S{newClassific},$S{isoverdue},$S{cashtodomain},$S{vcintfund},$S{govintfund},$S{isnostandard},$S{bondrating},$S{specialbond},$S{inmarketornot},$S{cashtogovernment},$D{cashtopublic},$D{cashtorelateproduct},$D{moneyofoverdueasset},$D{moneyofproperty},$S{secondlevelcaptialbond},$S{continuebondforever},$S{seniorbond},$S{convertbond},$S{otherbanksupplementtools},$S{assettype},$S{dealDate},$S{reportDate},$S{cshMngF},$S{penInvF},$S{perPenInvF})",
				params.getModel());
	}
	
	public UpdateResult updateDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return super.update("UPDATE dws_prod_ttrd_bef_g06a2_ori SET product_code=$S{productCode} ,i_code=$S{icode} ,asset_third_type=$S{assetThirdType} ,i_name=$S{iname} ,amount=$D{amount} ,changerate=$D{changerate} ,investedamountcny=$D{investedamountcny} ,org_classific=$S{orgClassific} ,orderfreemanage=$S{orderfreemanage} ,new_classific=$S{newClassific} ,isoverdue=$S{isoverdue} ,cashtodomain=$S{cashtodomain} ,vcintfund=$S{vcintfund} ,govintfund=$S{govintfund} ,isnostandard=$S{isnostandard} ,bondrating=$S{bondrating} ,specialbond=$S{specialbond} ,inmarketornot=$S{inmarketornot} ,cashtogovernment=$S{cashtogovernment} ,cashtopublic=$D{cashtopublic} ,cashtorelateproduct=$D{cashtorelateproduct} ,moneyofoverdueasset=$D{moneyofoverdueasset} ,moneyofproperty=$D{moneyofproperty} ,secondlevelcaptialbond=$S{secondlevelcaptialbond} ,continuebondforever=$S{continuebondforever} ,seniorbond=$S{seniorbond} ,convertbond=$S{convertbond} ,otherbanksupplementtools=$S{otherbanksupplementtools} ,assettype=$S{assettype} ,deal_date=$S{dealDate} ,report_date=$S{reportDate} ,csh_mng_f=$S{cshMngF},pen_inv_f=$S{penInvF},per_pen_inv_f=$S{perPenInvF} WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return super.update("DELETE FROM dws_prod_ttrd_bef_g06a2_ori WHERE  id=$I{id} ",
				params.getModel());
	}

	public ResultSet findFsfaProductGzb(Statement statement, SqlParam<DwsProdTTRDBefOri> params, String querySql) throws Exception {
		querySql = querySql.replaceAll("\\$S\\{deal_date\\}", params.getModel().getDealDate());
		log.info("执行查询估值数据库:" + querySql);
		return statement.executeQuery(querySql);
	}

	public void addFsfaProductGzb(List<Map<String, String>> resultList) throws Exception {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;

		// 选择主数据源
		try (AutoCloseable ac = daoService.selectDataSource(0)) {
			// 1、先删除该表数据
			daoService.update("truncate stg_t10_prdc_hldn_tbl_rj");
			String username = String.valueOf(SysUtil.getSysUserParams().get("username"));

			// 2、批量插入日间产品估值表
			Connection connection = daoService.getConnection();
			ps = connection.prepareStatement("INSERT INTO stg_t10_prdc_hldn_tbl_rj (deal_date,dt_dt,prdc_cd,prdc_nm,pe_idnt,pntr_idnt,etru_otr_prtf_cd,etru_otr_prtf_nm,itm_cd,itm_nm,scrt_cd,scrt_nm,intr_cd,stnd_intr_cd,mrkt_cd,accn_clss,hldn_qntt,hldn_qntt_last,cst_prc,hldn_cst,qttn_prc,mrkt_vl,flt_prc_mrkt_vl,fll_prc_mrkt_vl,cmbn_net_asst_vl,crrn,spcf_itm_cd,dlst_infr,rcvb_intr,intr_adjs,vltn_add,dscn_or_prmm,depr_rdy_one_stg,depr_rdy_scnd_stg,depr_rdy_thr_stg,sll_srvc_chrg_rtrn,cst_adjs,recvbl_prnc,recvbl_intr,bad_dbt_rdy_one_stg,bad_dbt_rdy_scnd_stg,bad_dbt_rdy_thr_stg,assc_itm_cd,offs,intr_dvdn_incm,cptl_gn,flt_prft_and_lss,tdy_yld,tdy_yld_rt,opn_a_pstn_dt,gng_pblc_stt,trdn_mrkt,vltn_intr_cd,cmpl_itm_nm,itm_lvl,mst_dtls_itm_asst_cd,mst_dtls_itm_asst_nm,is_dtld_itm,asst_lblt_idnt,asst_clss,is_mtch_to_mrkt_dt,cntr,exst_prd,stnd_socrco,dtsrc_cd,gnrt_tm,etl_tbl_nm,uppr_itm_cd,gzb_stt,opt_naem) " +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			Connection connection1 = daoService.getConnection();
			ps1 = connection1.prepareStatement("REPLACE INTO dwd_prod_fsfa_confirm (deal_date,dt_dt,prdc_cd,prdc_nm,gnrt_tm,gzb_stt,opt_naem) " +
					" VALUES (?,?,?,?,?,?,?)");

			for (Map<String, String> result : resultList) {
				ps.setString(1,  result.get("deal_date"));
				ps.setString(2,  result.get("dt_dt"));
				ps.setString(3,  result.get("prdc_cd"));
				ps.setString(4,  result.get("prdc_nm"));
				ps.setString(5,  result.get("pe_idnt"));
				ps.setString(6,  result.get("pntr_idnt"));
				ps.setString(7,  result.get("etru_otr_prtf_cd"));
				ps.setString(8,  result.get("etru_otr_prtf_nm"));
				ps.setString(9,  result.get("itm_cd"));
				ps.setString(10, result.get("itm_nm"));
				ps.setString(11, result.get("scrt_cd"));
				ps.setString(12, result.get("scrt_nm"));
				ps.setString(13, result.get("intr_cd"));
				ps.setString(14, result.get("stnd_intr_cd"));
				ps.setString(15, result.get("mrkt_cd"));
				ps.setString(16, result.get("accn_clss"));
				ps.setString(17, result.get("hldn_qntt"));
				ps.setString(18, result.get("hldn_qntt_last"));
				ps.setString(19, result.get("cst_prc"));
				ps.setString(20, result.get("hldn_cst"));
				ps.setString(21, result.get("qttn_prc"));
				ps.setString(22, result.get("mrkt_vl"));
				ps.setString(23, result.get("flt_prc_mrkt_vl"));
				ps.setString(24, result.get("fll_prc_mrkt_vl"));
				ps.setString(25, result.get("cmbn_net_asst_vl"));
				ps.setString(26, result.get("crrn"));
				ps.setString(27, result.get("spcf_itm_cd"));
				ps.setString(28, result.get("dlst_infr"));
				ps.setString(29, result.get("rcvb_intr"));
				ps.setString(30, result.get("intr_adjs"));
				ps.setString(31, result.get("vltn_add"));
				ps.setString(32, result.get("dscn_or_prmm"));
				ps.setString(33, result.get("depr_rdy_one_stg"));
				ps.setString(34, result.get("depr_rdy_scnd_stg"));
				ps.setString(35, result.get("depr_rdy_thr_stg"));
				ps.setString(36, result.get("sll_srvc_chrg_rtrn"));
				ps.setString(37, result.get("cst_adjs"));
				ps.setString(38, result.get("recvbl_prnc"));
				ps.setString(39, result.get("recvbl_intr"));
				ps.setString(40, result.get("bad_dbt_rdy_one_stg"));
				ps.setString(41, result.get("bad_dbt_rdy_scnd_stg"));
				ps.setString(42, result.get("bad_dbt_rdy_thr_stg"));
				ps.setString(43, result.get("assc_itm_cd"));
				ps.setString(44, result.get("offs"));
				ps.setString(45, result.get("intr_dvdn_incm"));
				ps.setString(46, result.get("cptl_gn"));
				ps.setString(47, result.get("flt_prft_and_lss"));
				ps.setString(48, result.get("tdy_yld"));
				ps.setString(49, result.get("tdy_yld_rt"));
				ps.setString(50, result.get("opn_a_pstn_dt"));
				ps.setString(51, result.get("gng_pblc_stt"));
				ps.setString(52, result.get("trdn_mrkt"));
				ps.setString(53, result.get("vltn_intr_cd"));
				ps.setString(54, result.get("cmpl_itm_nm"));
				ps.setString(55, result.get("itm_lvl"));
				ps.setString(56, result.get("mst_dtls_itm_asst_cd"));
				ps.setString(57, result.get("mst_dtls_itm_asst_nm"));
				ps.setString(58, result.get("is_dtld_itm"));
				ps.setString(59, result.get("asst_lblt_idnt"));
				ps.setString(60, result.get("asst_clss"));
				ps.setString(61, result.get("is_mtch_to_mrkt_dt"));
				ps.setString(62, result.get("cntr"));
				ps.setString(63, result.get("exst_prd"));
				ps.setString(64, result.get("stnd_socrco"));
				ps.setString(65, result.get("dtsrc_cd"));
				ps.setString(66, result.get("gnrt_tm"));
				ps.setString(67, result.get("etl_tbl_nm"));
				ps.setString(68, result.get("uppr_itm_cd"));
				ps.setString(69, result.get("is_cnfr"));
				ps.setString(70, username);
				ps.addBatch();

				ps1.setString(1,  result.get("deal_date"));
				ps1.setString(2, StringUtils.replace(result.get("dt_dt"), "-", ""));
				ps1.setString(3,  result.get("prdc_cd"));
				ps1.setString(4,  result.get("prdc_nm"));
				ps1.setString(5,  result.get("gnrt_tm"));
				ps1.setString(6,  result.get("is_cnfr"));
				ps1.setString(7,  username);
				ps1.addBatch();
			}

			ps.executeBatch();
			ps1.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (ps1 != null) {
				ps1.close();
			}
		}
	}

}
