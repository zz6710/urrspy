package com.kayak.subject.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.graphql.model.FetcherData;
import com.kayak.subject.dao.BaseReportReloadLogDao;
import com.kayak.subject.dao.DwsProdTTRDBefOriDao;
import com.kayak.subject.model.BaseReportReloadLog;
import com.kayak.subject.model.DwsProdTTRDBef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.model.DwsProdTTRDBefOri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "资产负载情况穿透前起源服务", model = DwsProdTTRDBefOri.class)
public class DwsProdTTRDBefOriService {

	private final Logger log = LoggerFactory.getLogger(DwsProdTTRDBefOriService.class);

	@Value("${fsfa.dblink.url}")
	private String url;
	@Value("${fsfa.dblink.user}")
	private String user;
	@Value("${fsfa.dblink.password}")
	private String password;

	@Autowired
	private DwsProdTTRDBefOriDao DwsProdTTRDBefOriDao;

	@Autowired
	private BaseReportReloadLogDao baseReportReloadLoDao;

	@Autowired
	private RptBusinessBaseTaskService rptBusinessBaseTaskService;

	@Autowired
	private DwsProdTTRDBefService dwsProdTTRDBefService;

	@API(desc = "查询资产负载情况穿透前起源信息", auth = APIAuth.YES)
	public SqlResult<DwsProdTTRDBefOri> findDwsProdTTRDBefOris(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		params.setMakeSql(true);
		return DwsProdTTRDBefOriDao.findDwsProdTTRDBefOris(params);
	}

	@API(desc = "添加资产负载情况穿透前起源", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,pen_inv_f,per_pen_inv_f", auth = APIAuth.NO)
	public int addDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return DwsProdTTRDBefOriDao.addDwsProdTTRDBefOri(params).getEffect();
	}
	
	@API(desc = "修改资产负载情况穿透前起源", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,pen_inv_f,per_pen_inv_f", auth = APIAuth.NO)
	public int updateDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return DwsProdTTRDBefOriDao.updateDwsProdTTRDBefOri(params).getEffect();
	}
	
	@API(desc = "删除资产负载情况穿透前起源", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date", auth = APIAuth.NO)
	public int deleteDwsProdTTRDBefOri(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		return DwsProdTTRDBefOriDao.deleteDwsProdTTRDBefOri(params).getEffect();
	}

	@API(desc = "导入产品估值表", auth = APIAuth.YES)
	public String impProductGzb(SqlParam<DwsProdTTRDBefOri> params) throws Exception {
		String returnStr = "";
		Connection connection = null;
		Statement statement = null;

		Map<String, Object> map = new HashMap<>();
		map.put("paravalue","1");
		map.put("oldParavalue","0");
		map.put("paraid","90000051803");

		String maxId = "";
		BaseReportReloadLog baseReportReloadLog = new BaseReportReloadLog();
		baseReportReloadLog.setMenuId(params.getModel().getMenuId());
		baseReportReloadLog.setReportDate(params.getModel().getDealDate());
		baseReportReloadLog.setStartDate(DateUtil.getNowDate());
		baseReportReloadLog.setStartTime(DateUtil.getNowTime());
		baseReportReloadLog.setResultStatus("正在"+params.getModel().getButtonName()+"中");
		baseReportReloadLog.setUserName(SysUtil.getLoginUserid());

		if ("1".equals(SysUtil.getSystemParamsByParaid("90000061000"))) {
			return RequestSupport.updateReturnJson(false, "系统清算流程正在执行中，请稍后重试！", null).toString();
		}
		if (rptBusinessBaseTaskService.upTaskStatus(map) > 0) {
			map.put("paravalue","0");
			map.put("oldParavalue","1");
		} else {
			return RequestSupport.updateReturnJson(false, "正在"+params.getModel().getButtonName()+"，请稍后重试", null).toString();
		}

		try {
			baseReportReloadLoDao.addBaseReportReloadLog(baseReportReloadLog);
			List<SqlRow> sqlRows = baseReportReloadLoDao.findIdBaseReportReloadLogs(baseReportReloadLog);
			if (CollectionUtil.isNotEmpty(sqlRows)) {
				maxId = sqlRows.get(0).getString("id");
			}

			// 连接估值核算系统数据库
			Class.forName("oracle.jdbc.OracleDriver");
			connection= DriverManager.getConnection(url, user, password);
			log.info("连接估值库信息:" + connection.getClientInfo().toString());
			statement = connection.createStatement();

			ResultSet resultSet = DwsProdTTRDBefOriDao.findFsfaProductGzb(statement, params, ExeQuery.queryExeId("FSFAEQ001"));
			List<Map<String, String>> resultList = new ArrayList<>();

			while(resultSet.next()) {
				Map result = new HashMap();

				result.put("deal_date",  resultSet.getString("deal_date"));
				result.put("dt_dt",  resultSet.getString("dt_dt"));
				result.put("prdc_cd",  resultSet.getString("prdc_cd"));
				result.put("prdc_nm",  resultSet.getString("prdc_nm"));
				result.put("pe_idnt",  resultSet.getString("pe_idnt"));
				result.put("pntr_idnt",  resultSet.getString("pntr_idnt"));
				result.put("etru_otr_prtf_cd",  resultSet.getString("etru_otr_prtf_cd"));
				result.put("etru_otr_prtf_nm",  resultSet.getString("etru_otr_prtf_nm"));
				result.put("itm_cd",  resultSet.getString("itm_cd"));
				result.put("itm_nm", resultSet.getString("itm_nm"));
				result.put("scrt_cd", resultSet.getString("scrt_cd"));
				result.put("scrt_nm", resultSet.getString("scrt_nm"));
				result.put("intr_cd", resultSet.getString("intr_cd"));
				result.put("stnd_intr_cd", resultSet.getString("stnd_intr_cd"));
				result.put("mrkt_cd", resultSet.getString("mrkt_cd"));
				result.put("accn_clss", resultSet.getString("accn_clss"));
				result.put("hldn_qntt", resultSet.getString("hldn_qntt"));
				result.put("hldn_qntt_last", resultSet.getString("hldn_qntt_last"));
				result.put("cst_prc", resultSet.getString("cst_prc"));
				result.put("hldn_cst", resultSet.getString("hldn_cst"));
				result.put("qttn_prc", resultSet.getString("qttn_prc"));
				result.put("mrkt_vl", resultSet.getString("mrkt_vl"));
				result.put("flt_prc_mrkt_vl", resultSet.getString("flt_prc_mrkt_vl"));
				result.put("fll_prc_mrkt_vl", resultSet.getString("fll_prc_mrkt_vl"));
				result.put("cmbn_net_asst_vl", resultSet.getString("cmbn_net_asst_vl"));
				result.put("crrn", resultSet.getString("crrn"));
				result.put("spcf_itm_cd", resultSet.getString("spcf_itm_cd"));
				result.put("dlst_infr", resultSet.getString("dlst_infr"));
				result.put("rcvb_intr", resultSet.getString("rcvb_intr"));
				result.put("intr_adjs", resultSet.getString("intr_adjs"));
				result.put("vltn_add", resultSet.getString("vltn_add"));
				result.put("dscn_or_prmm", resultSet.getString("dscn_or_prmm"));
				result.put("depr_rdy_one_stg", resultSet.getString("depr_rdy_one_stg"));
				result.put("depr_rdy_scnd_stg", resultSet.getString("depr_rdy_scnd_stg"));
				result.put("depr_rdy_thr_stg", resultSet.getString("depr_rdy_thr_stg"));
				result.put("sll_srvc_chrg_rtrn", resultSet.getString("sll_srvc_chrg_rtrn"));
				result.put("cst_adjs", resultSet.getString("cst_adjs"));
				result.put("recvbl_prnc", resultSet.getString("recvbl_prnc"));
				result.put("recvbl_intr", resultSet.getString("recvbl_intr"));
				result.put("bad_dbt_rdy_one_stg", resultSet.getString("bad_dbt_rdy_one_stg"));
				result.put("bad_dbt_rdy_scnd_stg", resultSet.getString("bad_dbt_rdy_scnd_stg"));
				result.put("bad_dbt_rdy_thr_stg", resultSet.getString("bad_dbt_rdy_thr_stg"));
				result.put("assc_itm_cd", resultSet.getString("assc_itm_cd"));
				result.put("offs", resultSet.getString("offs"));
				result.put("intr_dvdn_incm", resultSet.getString("intr_dvdn_incm"));
				result.put("cptl_gn", resultSet.getString("cptl_gn"));
				result.put("flt_prft_and_lss", resultSet.getString("flt_prft_and_lss"));
				result.put("tdy_yld", resultSet.getString("tdy_yld"));
				result.put("tdy_yld_rt", resultSet.getString("tdy_yld_rt"));
				result.put("opn_a_pstn_dt", resultSet.getString("opn_a_pstn_dt"));
				result.put("gng_pblc_stt", resultSet.getString("gng_pblc_stt"));
				result.put("trdn_mrkt", resultSet.getString("trdn_mrkt"));
				result.put("vltn_intr_cd", resultSet.getString("vltn_intr_cd"));
				result.put("cmpl_itm_nm", resultSet.getString("cmpl_itm_nm"));
				result.put("itm_lvl", resultSet.getString("itm_lvl"));
				result.put("mst_dtls_itm_asst_cd", resultSet.getString("mst_dtls_itm_asst_cd"));
				result.put("mst_dtls_itm_asst_nm", resultSet.getString("mst_dtls_itm_asst_nm"));
				result.put("is_dtld_itm", resultSet.getString("is_dtld_itm"));
				result.put("asst_lblt_idnt", resultSet.getString("asst_lblt_idnt"));
				result.put("asst_clss", resultSet.getString("asst_clss"));
				result.put("is_mtch_to_mrkt_dt", resultSet.getString("is_mtch_to_mrkt_dt"));
				result.put("cntr", resultSet.getString("cntr"));
				result.put("exst_prd", resultSet.getString("exst_prd"));
				result.put("stnd_socrco", resultSet.getString("stnd_socrco"));
				result.put("dtsrc_cd", resultSet.getString("dtsrc_cd"));
				result.put("gnrt_tm", resultSet.getString("gnrt_tm"));
				result.put("etl_tbl_nm", resultSet.getString("etl_tbl_nm"));
				result.put("uppr_itm_cd", resultSet.getString("uppr_itm_cd"));
				result.put("is_cnfr", resultSet.getString("is_cnfr"));

				resultList.add(result);
			}

			if (CollectionUtil.isEmpty(resultList)) {
				rptBusinessBaseTaskService.upTaskStatus(map);

				baseReportReloadLog.setId(maxId);
				baseReportReloadLog.setEndDate(DateUtil.getNowDate());
				baseReportReloadLog.setEndTime(DateUtil.getNowTime());
				baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
				baseReportReloadLog.setResultInfo("未查询到产品估值表数据");
				baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

				return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败，未查询到产品估值表数据", null).toString();
			}
			// 产品估值表数据插入到STG
			DwsProdTTRDBefOriDao.addFsfaProductGzb(resultList);

			// 触发后台清算任务处理逻辑
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("menuId", params.getModel().getMenuId());
			mapData.put("buttonName", params.getModel().getButtonName());
			mapData.put("reportDate", params.getModel().getDealDate());
			FetcherData<DwsProdTTRDBef> mapParams = new FetcherData<>(mapData, DwsProdTTRDBef.class);
			returnStr = dwsProdTTRDBefService.updateTaskAppQuery(mapParams);

			// 根据调用清算任务类的返回信息进行日志写入
			Map<String, Object> returnMap = JSONUtil.toBean(returnStr, Map.class);
			boolean success = (boolean) returnMap.get("success");
			String returnmsg = (String) returnMap.get("returnmsg");

			baseReportReloadLog.setId(maxId);
			baseReportReloadLog.setEndDate(DateUtil.getNowDate());
			baseReportReloadLog.setEndTime(DateUtil.getNowTime());
			baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+(success ? "成功" : "失败"));
			baseReportReloadLog.setResultInfo(returnmsg);
			baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);
		} catch (Exception e) {
			e.printStackTrace();
			rptBusinessBaseTaskService.upTaskStatus(map);

			baseReportReloadLog.setId(maxId);
			baseReportReloadLog.setEndDate(DateUtil.getNowDate());
			baseReportReloadLog.setEndTime(DateUtil.getNowTime());
			baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
			baseReportReloadLog.setResultInfo(e.getMessage());
			baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

			return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败", null).toString();
		} finally {
			// 关闭查询资管数据库资源
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rptBusinessBaseTaskService.upTaskStatus(map);
		return returnStr;
	}

}
