package com.kayak.pms.T81.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T81.dao.T8ProdDocInfoDao;
import com.kayak.pms.T81.model.T8ProdDocInfo;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品文档关联表服务", model = T8ProdDocInfo.class)
public class T8ProdDocInfoService {

	@Autowired
	private T8ProdDocInfoDao t8ProdDocInfoDao;


	@API(desc = "查询产品文档关联表信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8ProdDocInfo> findT8ProdDocInfos(SqlParam<T8ProdDocInfo> params) throws Exception {
		params.setMakeSql(true);
		return t8ProdDocInfoDao.findT8ProdDocInfos(params);
	}

	public SqlResult<T8ProdDocInfo> getMeetName(SqlParam<T8ProdDocInfo> params) throws Exception {
		params.setMakeSql(false);
		return t8ProdDocInfoDao.getMeetName(params);
	}



	@API(desc = "删除产品文档关联表", auth = APIAuth.YES,operation = APIOperation.DELETE)
	public int deleteT8ProdDocInfo(SqlParam<T8ProdDocInfo> params) throws Exception {
		return t8ProdDocInfoDao.deleteT8ProdDocInfo(params).getEffect();
	}

	/**
	 * 文件类型，产品版本 获取最新版本附件
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String findDocumentByCodeType(Map<String,Object> params) throws Exception {
		List<SqlRow> list = t8ProdDocInfoDao.findDocumentByCodeType((String)params.get("prodinfoId"),(String)params.get("attachmentType"));
		return list==null?null:list.size()>0?list.get(0).getString("filePath"):null;
	}


	public Map<String,Object> findDocByCodeType(String prodInfoId) throws Exception {
		List<SqlRow> list = t8ProdDocInfoDao.findDocByCodeType(prodInfoId);
		return list==null?null:list.size()>0?list.get(0):null;
	}



	//产品文档信息记录表
	@API(desc = "查询产品文档信息记录表信息", auth = APIAuth.YES)
	public SqlResult<T8ProdDocInfo> findT8ProdDocInfoHiss(SqlParam<T8ProdDocInfo> params) throws Exception {
		params.setMakeSql(true);
		return t8ProdDocInfoDao.findT8ProdDocInfoHiss(params);
	}

	@API(desc = "添加产品文档信息记录表", params = "id,t8_prod_info_id,doc_type,distributor_code,t8_trutee_info_id,t8_meet_create_id,t8_print_temp_version_id,doc_version,doc_desc,crt_date,crt_time,crt_user", auth = APIAuth.NO)
	public int addT8ProdDocInfoHis(SqlParam<T8ProdDocInfo> params) throws Exception {
		t8ProdDocInfoDao.addT8ProdDocInfoHis(params.getModel()) ;
		return 1;
	}


}
