package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.BaseSubjectMap;
import org.springframework.stereotype.Repository;

@Repository
public class BaseSubjectMapDao extends ComnDao {

	public SqlResult<BaseSubjectMap> findBaseSubjectMaps(SqlParam<BaseSubjectMap> params) throws Exception {
		String sql = "SELECT a.id,a.report_name,a.account_code,a.asst_3_knd,a.asst_cd,a.remark,a.inputuser,a.crt_date,a.crt_time,a.upd_date,a.upd_time," +
				" b.itemval as asst_3_knd_name," +
				" a.ctg_cd as ctg_cd1," +
				" c.itemval as ctg_cd," +
				" d.ACCOUNT_NAME" +
				" FROM base_subject_map a" +
				" left join sys_dict_item b on b.dict = 'asst_3_knd' and a.asst_3_knd = b.itemkey" +
				" left join sys_dict_item c on c.dict = 'ctg_cd' and a.ctg_cd = c.itemkey" +
				" left join base_element_account d on a.account_code = d.ACCOUNT_CODE" +
				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getAccountCode())) {
			sql += " and a.account_code like '%" + params.getModel().getAccountCode() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getAccountName())) {
			sql += " and d.account_name like '%" + params.getModel().getAccountName() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getAsst3Knd())) {
			sql += " and a.asst_3_knd like '%" + params.getModel().getAsst3Knd() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getReportName())) {
			sql += " and a.report_name like '%" + params.getModel().getReportName() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getCtgCd())) {
			sql += " and a.ctg_cd like '%" + params.getModel().getCtgCd() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getAsstCd())) {
			sql += " and a.asst_cd like '%" + params.getModel().getAsstCd() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getAsst3KndName())) {
			sql += " and b.itemval like '%" + params.getModel().getAsst3KndName() +"%'";
		}
		sql += " order by crt_date desc, crt_time desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		return super.update("INSERT INTO base_subject_map(report_name,account_code,asst_3_knd,ctg_cd,asst_cd,remark,inputuser,crt_date,crt_time,upd_date,upd_time) VALUES($S{reportName},$S{accountCode},$S{asst3Knd},$S{ctgCd},$S{asstCd},$S{remark},$S{inputuser},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		return super.update("UPDATE base_subject_map SET report_name=$S{reportName} ,account_code=$S{accountCode} ,asst_3_knd=$S{asst3Knd} ,ctg_cd=$S{ctgCd1} ,asst_cd=$S{asstCd} ,remark=$S{remark} ,inputuser=$S{inputuser} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime} WHERE id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		return super.update("DELETE FROM base_subject_map WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult truncateBaseSubjectMap(BaseSubjectMap params) throws Exception {
		return super.update("TRUNCATE table base_subject_map",
				params);
	}

}
