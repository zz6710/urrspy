package com.kayak.dps.direct.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.model.DataReportManage;
import org.springframework.stereotype.Repository;

@Repository
public class DataReportManageDao extends ComnDao {

	public SqlResult<DataReportManage> findDataReportManages(SqlParam<DataReportManage> params) throws Exception {
		StringBuilder sql = new StringBuilder("select id,workdate,filetype,msgtype, origfilename,status ," +
				"  fileid,filename,successcount,failedcount,totalcount,errorcode,errortext,crt_time" +
                "  from app_zz_file where 1=1 ");
		if (Tools.isNotEmpty(params.getModel().getWorkdate())) {
			sql.append(" and workdate = '").append(params.getModel().getWorkdate()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getFiletype())) {
			sql.append(" and filetype = '").append(params.getModel().getFiletype()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getOrigfilename())) {
			sql.append(" and origfilename = '").append(params.getModel().getOrigfilename()).append("'");
		}
       sql.append(" order by id desc ");
		return super.findRows(sql.toString(), params);
	}

}
