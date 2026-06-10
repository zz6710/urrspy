package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.NetValSPVInfoModel;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.NetValSPVInfoModelDao;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
@APIDefine(desc = "净值SPV信息服务", model = NetValSPVInfoModel.class)
public class NetValSPVInfoModelService {

	@Autowired
	private NetValSPVInfoModelDao netValSPVInfoModelDao;


	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	private static final int ONE = 1;
	private static final int ZERO = 0;

	@API(desc = "查询净值SPV信息", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<NetValSPVInfoModel> findNetValSPVInfoModels(SqlParam<NetValSPVInfoModel> params) throws Exception {
		return netValSPVInfoModelDao.findNetValSPVInfoModels(params);
	}

	/**
	 * 查询净值SPV信息代码与名称 去重
	 * @param params
	 * @throws Exception
	 */
	@API(desc = "查询净值SPV信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<NetValSPVInfoModel> findNetValSPVInfoModelsCdAndNm(SqlParam<NetValSPVInfoModel> params) throws Exception {
		params.setMakeSql(false);
		return netValSPVInfoModelDao.findNetValSPVInfoModelsCdAndNm(params);
	}

	@API(desc = "新增净值SPV信息",  auth = APIAuth.YES,operation = APIOperation.INSTER)
	public String addNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_mng_plan_bas_inf");
			param.put("scrId",params.getModel().getScrId());
			if(assetCollectionService.isOnlyOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "该资产管理产品已存在！", null).toString();
			}
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtTime(DateUtil.getNowTime());
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			netValSPVInfoModelDao.addNetValSPVInfoModel(params).getEffect();
			return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
		}
	}
	
	@API(desc = "修改净值SPV信息",  auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public String updateNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			netValSPVInfoModelDao.updateNetValSPVInfoModel(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}
	@API(desc = "补录净值SPV信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public String updateNetValSPVInfoModelBl(SqlParam<NetValSPVInfoModel> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdTime(DateUtil.getNowTime());
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			params.getModel().setDealDate(DateUtil.getNowDate());
			netValSPVInfoModelDao.updateNetValSPVInfoModelBl(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "补录成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
		}
	}
	
	@API(desc = "删除净值SPV信息", auth = APIAuth.YES,operation = APIOperation.DELETE)
	public String deleteNetValSPVInfoModel(SqlParam<NetValSPVInfoModel> params) throws Exception {
		try {
			netValSPVInfoModelDao.deleteNetValSPVInfoModel(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
		}
	}

	@API(desc = "根据文档类型获取模板类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPTypeByDocType(SqlParam<NetValSPVInfoModel> param) throws Exception {

		String docType = param.getModel().getCbndFrsCtg();
		List<SqlRow> tempTypeByDocType = netValSPVInfoModelDao.getXPTypeByDocType(docType);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getUPDTypeByDocType(SqlParam<NetValSPVInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("cbndScdCtg",param.getModel().getCbndScdCtg());
		params.put("cbndFrsCtg",param.getModel().getCbndFrsCtg());
		List<SqlRow> tempTypeByDocType = netValSPVInfoModelDao.getUPDTypeByDocType(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	//查询是否有相同净值SPV信息
	private Boolean existNetValInfo(SqlParam<NetValSPVInfoModel> params,int i) throws Exception {
		SqlRow sqlRow = netValSPVInfoModelDao.existNetValInfo(params);
		if (i == ONE) {
			// 如果是更新 查询数量-1
			long con = (long) sqlRow.get("con");
			return con - ONE > ZERO;
		} else {
			long con = (long) sqlRow.get("con");
			return con > ZERO;
		}
	}


	@API(desc = "人行一级查询人行二级",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getPbnkScdByPbnkFrs(SqlParam<NetValSPVInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pbnkFrsCtg",param.getModel().getPbnkFrsCtg());
		List<SqlRow> tempTypeByDocType = netValSPVInfoModelDao.getPbnkScdByPbnkFrs(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "人行二级查询人行三级",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getPbnkTrdByPbnkScd(SqlParam<NetValSPVInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pbnkScdCtg",param.getModel().getPbnkScdCtg());
		List<SqlRow> tempTypeByDocType = netValSPVInfoModelDao.getPbnkTrdByPbnkScd(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "查询品种数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getBredCdDict(SqlParam<NetValSPVInfoModel> param) throws Exception {
		Map<String, Object> params = new HashMap<>();
		List<SqlRow> tempTypeByDocType = netValSPVInfoModelDao.getBredCdDict(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}
	@API(desc = "导出净值SPV信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void SPVExcelDownloadAction(SqlParam<NetValSPVInfoModel> params) throws Exception {}
	@API(desc = "查询管理人同业机构类型",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<NetValSPVInfoModel>  getMngType(SqlParam<NetValSPVInfoModel> param) throws Exception {
		return netValSPVInfoModelDao.getMngType(param);

	}
}
