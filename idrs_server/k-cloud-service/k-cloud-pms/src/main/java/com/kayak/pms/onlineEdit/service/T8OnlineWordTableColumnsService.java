package com.kayak.pms.onlineEdit.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.onlineEdit.dao.T8OnlineWordTableColumnsDao;
import com.kayak.pms.onlineEdit.model.T8OnlineWordTableColumns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@APIDefine(desc = "文档在线编辑表映射服务", model = T8OnlineWordTableColumns.class)
public class T8OnlineWordTableColumnsService {

	@Autowired
	private T8OnlineWordTableColumnsDao t8OnlineWordTableColumnsDao;

	@API(desc = "查询文档在线编辑表映射", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordTableColumns> findT8OnlineWordTableColumns1(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return findT8OnlineWordTableColumns(params);
	}

	@API(desc = "查询文档在线编辑表映射", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordTableColumns> findT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		params.setMakeSql(true);
		return t8OnlineWordTableColumnsDao.findT8OnlineWordTableColumns(params);
	}

	@API(desc = "添加文档在线编辑表映射", params = "table_name,column_name,column_comment,is_disabled,status", auth = APIAuth.YES,operation = APIOperation.INSTER)
	public int addT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		params.getModel().setCrtDate(DateUtil.getNowDate());
		params.getModel().setCrtTime(DateUtil.getNowTime());
		params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
		return t8OnlineWordTableColumnsDao.addT8OnlineWordTableColumns(params).getEffect();
	}
	
	@API(desc = "修改文档在线编辑表映射", params = "id,table_name,column_name,column_comment,is_disabled,status", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public int updateT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		params.getModel().setUpdDate(DateUtil.getNowDate());
		params.getModel().setUpdTime(DateUtil.getNowTime());
		params.getModel().setUpdUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
		return t8OnlineWordTableColumnsDao.updateT8OnlineWordTableColumns(params).getEffect();
	}
	
	@API(desc = "删除文档在线编辑表映射", params = "id,table_name,column_name,column_comment,is_disabled,status", auth = APIAuth.YES,operation = APIOperation.DELETE)
	public int deleteT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return t8OnlineWordTableColumnsDao.deleteT8OnlineWordTableColumns(params).getEffect();
	}

}
