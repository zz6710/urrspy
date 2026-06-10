package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.NonStandInfoModel;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.NonStandInfoModelDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "非标信息服务", model = NonStandInfoModel.class)
public class NonStandInfoModelService {

	@Autowired
	private NonStandInfoModelDao nonStandInfoModelDao;

	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	private static final int ONE = 1;
	private static final int ZERO = 0;

	@API(desc = "查询非标信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<NonStandInfoModel> findNonStandInfoModels(SqlParam<NonStandInfoModel> params) throws Exception {
		return nonStandInfoModelDao.findNonStandInfoModels(params);
	}


	@API(desc = "查询非标债权信息id与名称", auth = APIAuth.NO)
	public SqlResult<NonStandInfoModel> findNonStandInfoIdAndNm(SqlParam<NonStandInfoModel> params) throws Exception {
		return nonStandInfoModelDao.findNonStandInfoIdAndNm(params);
	}

	@API(desc = "新增非标信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_nstd_bas_inf");
			param.put("scrId",params.getModel().getScrId());
			if(assetCollectionService.isOnlyOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "该非标资产已存在！", null).toString();
			}
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtTime(DateUtil.getNowTime());
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			nonStandInfoModelDao.addNonStandInfoModel(params).getEffect();
			return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
		}catch (Exception e) {
			log.error("新增失败！");
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
		}


	}

	@API(desc = "修改非标信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			nonStandInfoModelDao.updateNonStandInfoModel(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}catch (Exception e) {
			log.error("修改失败！");
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}

	}

	@API(desc = "补录非标信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateNonStandInfoModelBl(SqlParam<NonStandInfoModel> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			nonStandInfoModelDao.updateNonStandInfoModelBl(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
		}catch (Exception e) {
			log.error("补录失败！");
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
		}

	}
	
	@API(desc = "删除非标信息",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public String deleteNonStandInfoModel(SqlParam<NonStandInfoModel> params) throws Exception {
		try {
			DaoUtil.doTrans(() -> {
				nonStandInfoModelDao.deleteNonStandInfoModel(params).getEffect();
			});
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		}catch (Exception e) {
			log.error("删除失败！");
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
		}
	}


	@API(desc = "根据文档类型获取模板类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPTypeByDocType(SqlParam<NonStandInfoModel> param) throws Exception {

		String docType = param.getModel().getCbndFrsCtg();
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getXPTypeByDocType(docType);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getUPDTypeByDocType(SqlParam<NonStandInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
		params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getUPDTypeByDocType(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}


	//查询是否有非标债权信息
	private Boolean existNonStandInfo(SqlParam<NonStandInfoModel> params,int i) throws Exception {
		SqlRow sqlRow = nonStandInfoModelDao.existNonStandInfo(params);
		if (i == ONE) {
			// 如果是更新 查询数量-1
			long con = (long) sqlRow.get("con");
			return con - ONE > ZERO;
		} else {
			long con = (long) sqlRow.get("con");
			return con > ZERO;
		}
	}

	@API(desc = "中债一级查询中债二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getUPDTypeByDoc(SqlParam<NonStandInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
		params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getUPDTypeByDoc(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);
		return sqlRowSqlResult;
	}

	@API(desc = "人行一级查询人行二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getPbnkScdByPbnkFrs(SqlParam<NonStandInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pbnkFrsCtg",param.getModel().getPbnkFrsCtg());
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getPbnkScdByPbnkFrs(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);
		return sqlRowSqlResult;
	}

	@API(desc = "人行二级查询人行三级",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getPbnkTrdByPbnkScd(SqlParam<NonStandInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pbnkScdCtg",param.getModel().getPbnkScdCtg());
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getPbnkTrdByPbnkScd(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);
		return sqlRowSqlResult;
	}

	@API(desc = "表内外城商行投资分类",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getInvestTypeDict(SqlParam<NonStandInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
		List<SqlRow> tempTypeByDocType = nonStandInfoModelDao.getInvestTypeDict(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);
		return sqlRowSqlResult;
	}


	@API(desc = "导出非标信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void nonExcelDownloadAction(SqlParam<NonStandInfoModel> params) throws Exception {}
}
