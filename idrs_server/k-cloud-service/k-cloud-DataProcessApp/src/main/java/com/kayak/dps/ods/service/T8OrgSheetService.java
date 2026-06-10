package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.model.T8OrgSheet;
import com.kayak.dps.check.util.NextVersionUtil;
import com.kayak.dps.ods.dao.T8OrgSheetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service
@APIDefine(desc = "机构信息服务", model = T8OrgSheet.class)
public class T8OrgSheetService {

	@Autowired
	private T8OrgSheetDao t8OrgSheetDao;

	@Resource(name = "assetCollectionService")
	private AssetCollectionService assetCollectionService;

	@API(desc = "查询机构信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<T8OrgSheet> findT8OrgSheets(SqlParam<T8OrgSheet> params) throws Exception {
		return t8OrgSheetDao.findT8OrgSheets(params);
	}

	@API(desc = "新增机构信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addT8OrgSheet(SqlParam<T8OrgSheet> params) throws Exception {
		try {
			//校验唯一性
			Map<String, Object> param = new HashMap<>();
			param.put("checkTableName","ods_org_info");
			param.put("checkValue",params.getModel().getOrgNbrExt());
			param.put("checkKey","ORG_NBR_EXT");
			if(assetCollectionService.isOnlyForOne(param)>0){
				return RequestSupport.updateReturnJson(false,  "该机构已存在！", null).toString();
			}
			params.getModel().setDealDate(DateUtil.getNowDate());
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			t8OrgSheetDao.addT8OrgSheet(params);
			return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
		}
	}

	@API(desc = "补录机构信息",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateT8OrgSheet(SqlParam<T8OrgSheet> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setVersion(NextVersionUtil.getNextVersion(params.getModel().getVersion()));
			t8OrgSheetDao.updateT8OrgBaseSheetBl(params);
			return RequestSupport.updateReturnJson(true, "补录成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "补录失败！", null).toString();
		}
	}

	@API(desc = "修改机构信息",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateT8OrgBaseSheet(SqlParam<T8OrgSheet> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
			params.getModel().setDealDate(DateUtil.getNowDate());
			t8OrgSheetDao.updateT8OrgBaseSheet(params);
			return RequestSupport.updateReturnJson(true, "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}

	@API(desc = "删除机构信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public String deleteT8OrgSheet(SqlParam<T8OrgSheet> params) throws Exception {
		try {
			t8OrgSheetDao.deleteT8OrgSheet(params);
			return RequestSupport.updateReturnJson(true, "删除成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "删除失败！", null).toString();
		}
	}

	/**
	 * 查询机构名称
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8OrgSheet> findOrgNm(SqlParam<T8OrgSheet> params) throws Exception {
		return t8OrgSheetDao.findOrgNm(params);
	}

	/**
	 * 查询机构名称及代码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8OrgSheet> findOrgNmAll(SqlParam<T8OrgSheet> params) throws Exception {
		params.setLimit(500);
		return t8OrgSheetDao.findOrgNmAll(params);
	}

	/**
	 * 查询机构其他信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8OrgSheet> findOrgInfo(SqlParam<T8OrgSheet> params) throws Exception {
		return t8OrgSheetDao.findOrgInfo(params);
	}

	@API(desc = "机构信息详情", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public void detailT8OrgSheets(SqlParam<T8OrgSheet> params) throws Exception {}

}
