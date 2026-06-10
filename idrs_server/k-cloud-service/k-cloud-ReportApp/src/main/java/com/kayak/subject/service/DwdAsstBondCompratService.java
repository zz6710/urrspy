package com.kayak.subject.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.subject.model.PubReq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.DwdAsstBondCompratDao;
import com.kayak.subject.model.DwdAsstBondComprat;

@Service
@APIDefine(desc = "主体评级服务", model = DwdAsstBondComprat.class)
public class DwdAsstBondCompratService extends RptBusinessBaseTaskService {

	private static Logger log = LoggerFactory.getLogger(DwdAsstBondCompratService.class);

	@Autowired
	private ComnDao comnDao;

	@Autowired
	private DwdAsstBondCompratDao dwdAsstBondCompratDao;

	@API(desc = "查询主体评级信息", auth = APIAuth.YES)
	public SqlResult<DwdAsstBondComprat> findDwdAsstBondComprats(SqlParam<DwdAsstBondComprat> params) throws Exception {
		params.setMakeSql(true);
		return dwdAsstBondCompratDao.findDwdAsstBondComprats(params);
	}

	@API(desc = "添加主体评级", params = "id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date", auth = APIAuth.NO)
	public int addDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		return dwdAsstBondCompratDao.addDwdAsstBondComprat(params).getEffect();
	}

	@API(desc = "修改主体评级", params = "id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date", auth = APIAuth.NO)
	public int updateDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		params.getModel().setUpdateDate(DateUtil.getNowDate());
		params.getModel().setUpdateTime(DateUtil.getNowTime());
		params.getModel().setSummitUser(SysUtil.getLoginUserid());
		params.getModel().setFlag("1");
		return dwdAsstBondCompratDao.updateDwdAsstBondComprat(params).getEffect();
	}

	@API(desc = "删除主体评级", params = "id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date", auth = APIAuth.NO)
	public int deleteDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		return dwdAsstBondCompratDao.deleteDwdAsstBondComprat(params).getEffect();
	}

	@API(desc = "重新生成报表主体评级", params = "id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date", auth = APIAuth.YES)
	public String buildDwsAssetBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		try {
			String dealDate = params.getModel().getDealDate();
			String taskIds = SysUtil.getSystemParamsByParaid("90000051801");

			if (StringUtils.isNotEmpty(taskIds)) {
				String[] taskId = taskIds.split(",");

				for (String task : taskId) {
					PubReq request = new PubReq();
					request.setTaskId(task);
					request.setTaskDate(dealDate);
					super.beforeClear(request);
					super.dataModeExConvert(request);
				}
			}

			return RequestSupport.updateReturnJson(true,"重新生成报表成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"重新生成报表失败！" + e.getMessage(),null).toString();
		}
	}

}
