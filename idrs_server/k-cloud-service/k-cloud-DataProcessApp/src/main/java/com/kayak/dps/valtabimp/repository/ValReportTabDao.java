package com.kayak.dps.valtabimp.repository;

import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.valtabimp.model.ValReportTab;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ValReportTabDao extends ComnDao {

	public SqlResult<ValReportTab> findValReportTabs(SqlParam<ValReportTab> params) throws Exception {
		return super.findRows("SELECT id, id t8_val_reporttab_id ,reporttab_name,note as remark,inputuser,crt_date,crt_time FROM base_fa_reporttab order by id desc", params);
	}

	public UpdateResult addValReportTab(SqlParam<ValReportTab> params) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		return super.update("INSERT INTO base_fa_reporttab(reporttab_name,note,inputuser,crt_date,crt_time) VALUES($S{reporttabName},$S{remark},(SELECT loginname FROM sys_user WHERE userid = '"+userid+"' limit 1),date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'))",
				params.getModel());
	}
	
	public UpdateResult updateValReportTab(SqlParam<ValReportTab> params) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		return super.update("UPDATE base_fa_reporttab SET reporttab_name=$S{reporttabName} ,note=$S{remark} ,inputuser=(SELECT loginname FROM sys_user WHERE userid = '"+userid+"' limit 1) ,crt_date=date_format(CURDATE(),'%Y%m%d') ,crt_time=date_format(CURTIME(),'%H%i%s')  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteValReportTab(String id) throws Exception {
		return super.update("DELETE FROM base_fa_reporttab WHERE  id=$I{id} ", id);
	}

}
