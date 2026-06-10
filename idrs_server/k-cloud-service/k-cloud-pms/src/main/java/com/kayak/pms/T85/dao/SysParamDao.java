package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T85.model.SysParam;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 文件名: SysParamDao.java
 * 描述:  系统参数表
 * 创建人: zengzt
 * 创建时间:2020年5月23日下午5:46:55
 */
@Repository
public class SysParamDao extends ComnDao{

	public List<SqlRow> queryParam(String paraid) throws Exception {
		
		String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay FROM sys_param WHERE paraid=$S{paraid} AND moduleid='"+GlobalConstants.MODULEID+"' ";
		
		return super.findRows(sql, paraid);

	}

	public SqlResult<SysParam> queryParam(SqlParam<SysParam> params) throws Exception {
		
		String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay FROM sys_param WHERE paraid=$S{paraid} AND moduleid='"+GlobalConstants.MODULEID+"' ";
		
		return super.findRows(sql, params);
	}

	//查询日志转存备份路径
	public SqlRow findBackupPath(String paraid,String moduleid) throws Exception {

		String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay " +
				"FROM sys_param WHERE paraid= '"+paraid+"' AND moduleid= '"+moduleid+"' ";
		return super.findRow(sql,null);
	}
	
}
