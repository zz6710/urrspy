package com.kayak.pms.basePublish.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureDataSetDao;
import com.kayak.pms.basePublish.model.DisclosureDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "数据集视转换服务", model = DisclosureDataSet.class)
public class DisclosureDataSetService {

	@Autowired
	private DisclosureDataSetDao disclosureDataSetDao;

	@API(desc = "查询数据集视转换信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<DisclosureDataSet> findDisclosureDataSetsAuth(SqlParam<DisclosureDataSet> params) throws Exception {
		return findDisclosureDataSets(params);
	}

	@API(desc = "查询数据集视转换信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureDataSet> findDisclosureDataSets(SqlParam<DisclosureDataSet> params) throws Exception {
		params.setMakeSql(true);
		return disclosureDataSetDao.findDisclosureDataSets(params);
	}

	@API(desc = "新增数据集视转换",  auth = APIAuth.YES, operation = APIOperation.INSTER)
	public int addDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setCrtDate(date);
		params.getModel().setCrtTime(time);
		params.getModel().setCrtUserId(userid);
		params.getModel().setCrtUserName(username);
		params.getModel().setStatus("C");//状态默认为新建
		return disclosureDataSetDao.addDisclosureDataSet(params).getEffect();
	}

	@API(desc = "修改数据集视转换", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		params.getModel().setUpdUserId(userid);
		params.getModel().setUpdUserName(username);
		return disclosureDataSetDao.updateDisclosureDataSet(params).getEffect();
	}

	@API(desc = "删除数据集视转换", auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		return disclosureDataSetDao.deleteDisclosureDataSet(params).getEffect();
	}

	/**
	 * 功能：启用数据集视
	 * 作者：rennannan
	 * 日期：20210528
	 *
	 * @param
	 * @return
	 */
	@API(desc = "启用数据集视转换", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int enableDisclosureData(SqlParam<DisclosureDataSet> params) throws Exception {
		params.getModel().setStatus("N");
		return disclosureDataSetDao.updateDisclosureDataStatus(params.getModel());
	}

	/**
	 * 功能：停用数据集视
	 * 作者：rennannan
	 * 日期：20210528
	 *
	 * @param
	 * @return
	 */
	@API(desc = "启用数据集视转换", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int unableDisclosureData(SqlParam<DisclosureDataSet> params) throws Exception {
		params.getModel().setStatus("P");
		return disclosureDataSetDao.updateDisclosureDataStatus(params.getModel());
	}

}
