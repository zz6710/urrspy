package com.kayak.pms.basePublish.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.dao.DisclosureModDao;
import com.kayak.pms.basePublish.dao.DisclosureModVersionDao;
import com.kayak.pms.basePublish.model.DisclosureMod;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "信披模板服务", model = DisclosureMod.class)
public class DisclosureModService {

	@Autowired
	private DisclosureModDao disclosureModDao;
	@Autowired
	private DisclosureModVersionDao disclosureModVersionDao;
	@Autowired
	private DisclosureModVersionService disclosureModVersionService;


	@API(desc = "查询信披模板信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<DisclosureMod> findDisclosureMods(SqlParam<DisclosureMod> params) throws Exception {
		return disclosureModDao.findDisclosureMods(params);
	}

	/**
	 * 功能：首页查询待复核定期报告模板信息
	 * 作者：rennannan
	 * 日期：20210915
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询待复核模板", auth = APIAuth.NO)
	public SqlResult<DisclosureMod> findNeedApprovalMods(SqlParam<DisclosureMod> params) throws Exception {
		return disclosureModDao.findNeedApprovalMods(params);
	}

	@API(desc = "模板扫描添加信披模板",auth = APIAuth.NO,operation = APIOperation.INSTER)
	public String addDisclosureMod(DisclosureMod disclosureMod) throws Exception {
		return disclosureModDao.addDisclosureMod(disclosureMod);
	}

	@API(desc = "修改模板名称",auth = APIAuth.YES)
	public String updateDisclosureModName(SqlParam<DisclosureMod> params) throws Exception {
		try {
			Integer count = disclosureModDao.duplicateModName(params);
			if (count>0){
				return RequestSupport.updateReturnJson(false, "模板名称不可重复", null).toString();
			}
			String date = DateUtil.getSysWordDay();
			String time = DateUtil.getNowTime();
			String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
			String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称
			params.getModel().setUpdUserId(userid);
			params.getModel().setUpdUserName(username);
			params.getModel().setUpdDate(date);
			params.getModel().setUpdTime(time);
			disclosureModDao.updateDisclosureModName(params).getEffect();
			return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "操作异常", null).toString();
		}
	}

	@API(desc = "删除信披模板", params = "id,disclosure_type,disclosure_son_type,mod_name,doc_name,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
	public int deleteDisclosureMod(SqlParam<DisclosureMod> params) throws Exception {
		return disclosureModDao.deleteDisclosureMod(params).getEffect();
	}

	/**
	 * 查询是否有启用状态的模板,并删除信披模板
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "删除信披模板", auth = APIAuth.YES)
	public String checkDisclosureMod(SqlParam<DisclosureMod> params) throws Exception {
		Map<String,Object> map = new HashMap<>();
		Integer count = disclosureModDao.checkDisclosureModInTask(params);
		if (count>0){
			return RequestSupport.updateReturnJson(false, "当前模板的版本在“公告版本”中被引用，不允许删除", null).toString();
		}
		count = disclosureModDao.checkDisclosureModInRule(params);
		if (count>0){
			return RequestSupport.updateReturnJson(false, "当前模板的版本在“产品信披规则”中被引用，不允许删除", null).toString();
		}
		count = disclosureModDao.checkDisclosureMod(params);
		if (count>0){
			return RequestSupport.updateReturnJson(false, "当前模板的版本存在启用的版本，不允许删除", null).toString();
		}
		deleteDisclosureMod(params);
		map.put("disclosureModId",params.getModel().getId());
		//转对象实体参数
		SqlParam<DisclosureModVersion> param = new FetcherData<>(map,DisclosureModVersion.class);
		List<SqlRow> delVersionId = disclosureModVersionDao.getVersionId(param.getModel().getDisclosureModId());
		disclosureModVersionDao.deleteDisclosureMod(param);
		for (SqlRow row:delVersionId) {
			disclosureModVersionDao.deleteDisclosureModCol(row.getString("versionId"));
			disclosureModVersionService.deleteForModVersion(row.getString("versionId"));

		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "获取信披变更状态",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> statusChangeList(SqlParam<DisclosureMod> param) throws Exception {
		List<SqlRow> statusList = disclosureModDao.statusChangeList();
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(statusList.size());
		sqlRowSqlResult.setRows(statusList);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "根据文档类型获取模板类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPTypeByDocType(SqlParam<DisclosureMod> param) throws Exception {
		String docType = param.getModel().getDisclosureType();
		List<SqlRow> tempTypeByDocType = disclosureModDao.getXPTypeByDocType(docType);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "获取信披类型(筛掉不用的类型)",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPType(SqlParam<DisclosureMod> param) throws Exception {
		List<SqlRow> tempTypeByDocType = disclosureModDao.getXPType(param);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "获取净值公告基准日期(筛掉不用的类型)",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> queryNetBaseDate(SqlParam<DisclosureMod> param) throws Exception {
		List<SqlRow> tempTypeByDocType = disclosureModDao.queryNetBaseDate(param);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}
	@API(desc = "获取信披类型(筛掉不用的类型及不能绑定产品的类型)",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPTypeInProd(SqlParam<DisclosureMod> param) throws Exception {
		List<SqlRow> tempTypeByDocType = disclosureModDao.getXPTypeInProd(param);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "校验文档模板基础信息是否存在",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public Integer checkXPPrintTemp(DisclosureMod disclosureMod) throws Exception {
		return disclosureModDao.checkXPPrintTemp(disclosureMod);
	}

	public DisclosureMod getDisclosureModById(String modId) throws Exception {
		return disclosureModDao.getDisclosureModById(modId);
	}
	/**
	* @功能描述:处理下一版本号
	* @params:[VNumber]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/9/16
	*/
	public String getNextVersion(String VNumber) throws Exception {
		String newVNumber="";
		if(Tools.isNotEmpty(VNumber) && !VNumber.equals("")){
			String[] data = VNumber.split("V");
			String nowVersion = data[1];
			String[] number = nowVersion.split("\\.");
			String prefix = number[0];
			String suffix = number[1];
			if("9".equals(suffix)){
				Integer pre = Integer.parseInt(prefix)+1;
				newVNumber = "V"+pre+".0";
			}else{
				Integer suf = Integer.parseInt(suffix)+1;
				newVNumber = "V"+prefix+"."+suf;
			}
		}else{
			newVNumber="V1.0";
		}
		return newVNumber;
	}

	/**
	 * 信披模板配置-上传模板权限控制
	 * @return
	 */
	@API(desc = "上传模板",auth = APIAuth.YES)
	public String upLoadRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 信披模板配置-更新模板权限控制
	 * @return
	 */
	@API(desc = "更新模板",auth = APIAuth.YES)
	public String upDateRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 信披模板配置-停用启用权限控制
	 * @return
	 */
	@API(desc = "停用启用",auth = APIAuth.YES)
	public String turnOnRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 信披模板配置-下载模板权限控制
	 * @return
	 */
	@API(desc = "下载模板",auth = APIAuth.YES)
	public String downLoadRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 信披模板配置-字段维护权限控制
	 * @return
	 */
	@API(desc = "字段维护",auth = APIAuth.YES)
	public String geXPbyModIdRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "获取信披类型(筛掉不用的类型及不能绑定产品的类型)",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> getXPTypeInProd2(SqlParam<DisclosureMod> param) throws Exception {
		List<SqlRow> tempTypeByDocType = disclosureModDao.getXPTypeInProd2(param);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}
}
