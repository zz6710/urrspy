package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.DpsSysParam;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * 文件名: SysParamDao.java
 * 描述:  系统参数表
 * 创建人: zengzt
 * 创建时间:2020年5月23日下午5:46:55
 */
@Repository
public class SysParamDao extends ComnDao{

	public List<SqlRow> queryParam(String paraid) throws Exception {
		return this.queryParam(GlobalConstants.MODULEID, paraid);
	}

	public String queryParamById(String moduleid, String paraid) throws Exception {
		List<SqlRow> result = this.queryParam(moduleid, paraid);
		if(result != null && result.size() == 1){
			return result.get(0).getString("paravalue");
		}
		return "";
	}

	public List<SqlRow> queryParam(String moduleid, String paraid) throws Exception {
		String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay FROM sys_param WHERE paraid=$S{paraid} AND moduleid='"+ moduleid+"' ";
		return super.findRows(sql, paraid);
	}

	public SqlResult<DpsSysParam> queryParam(SqlParam<DpsSysParam> params) throws Exception {
		String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay FROM sys_param WHERE paraid=$S{paraid} AND moduleid='"+GlobalConstants.MODULEID+"' ";
		return super.findRows(sql, params);
	}

    public String queryValueByParaid(String paraid) throws Exception {
        String sql = "SELECT moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay FROM sys_param WHERE paraid=$S{paraid} AND moduleid='"+GlobalConstants.MODULEID+"' ";
        List<SqlRow> rows = super.findRows(sql, paraid);
        if (null != rows && rows.size() > 0) {
            return rows.get(0).getString("paravalue");
        }
        return null;
    }

	/**
	 * 检查当前系统工作日下的清算任务是否已经自动执行
	 * @param params
	 * @return
	 */
	public SqlResult<DpsSysParam> findAutoExec(SqlParam<DpsSysParam> params) throws Exception {
		return super.findRows("SELECT moduleid,paraid,paraname,paravalue,groupparaid,dict,functype,isdisplay,confoption FROM sys_param WHERE paraid ='80000072' AND paravalue='1'",params);
	}

	/**
	 * 自动执行设置
	 * @param params
	 */
	public void updateSysParamAutoExec(SqlParam<DpsSysParam> params) throws Exception {
		//1.查询出当前系统工作日
		String workdate = DateUtil.getSysWordDay();
		//2.设置当前系统工作日下为自动执行
		doTrans(()->{
			String sql="UPDATE sys_param SET paravalue='"+params.getModel().getParavalue()+"' WHERE paraid IN ('80000072')";
			super.update(sql);
			String sql1="UPDATE kbatch_task_exec SET auto_exec='"+params.getModel().getParavalue()+"' WHERE task_date='"+workdate+"' AND exec_status NOT IN ('5','7','8') ";
			super.update(sql1);
		});
	}

}
