package com.kayak.dps.app.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.service.business.BusinessBaseTaskService;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.dao.DwsAssetA1413DepStrucDao;
import com.kayak.dps.app.model.DwsAssetA1413DepStruc;

import java.util.List;

@Service
@APIDefine(desc = "A1413存款期限结构及相关业务情况补录表服务", model = DwsAssetA1413DepStruc.class)
public class DwsAssetA1413DepStrucService {

	private static Logger log = LoggerFactory.getLogger(DwsAssetA1413DepStrucService.class);

	@Autowired
	private DwsAssetA1413DepStrucDao dwsAssetA1413DepStrucDao;

	@Autowired
	private ModelDataToGenService modelDataToGenService;

	@Autowired
	BusinessBaseTaskService businessBaseTaskService;

	@API(desc = "查询A1413存款期限结构及相关业务情况补录表信息", auth = APIAuth.YES)
	public SqlResult<DwsAssetA1413DepStruc> findDwsAssetA1413DepStrucs(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		params.setMakeSql(true);
		return dwsAssetA1413DepStrucDao.findDwsAssetA1413DepStrucs(params);
	}

	@API(desc = "添加A1413存款期限结构及相关业务情况补录表", params = "id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date", auth = APIAuth.NO)
	public String addDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		try {
			SqlResult<DwsAssetA1413DepStruc> sqlResult = dwsAssetA1413DepStrucDao.findDwsAssetA1413DepStrucsByActDt(params);
			if (CollectionUtil.isNotEmpty(sqlResult.getRows())) {
				return RequestSupport.updateReturnJson(false,"已存在" + params.getModel().getActDt() + "数据，请重新录入！",null).toString();
			}

			dwsAssetA1413DepStrucDao.addDwsAssetA1413DepStruc(params).getEffect();
			return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"新增失败!" + e.getMessage(),null).toString();
		}
	}
	
	@API(desc = "修改A1413存款期限结构及相关业务情况补录表", params = "id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date", auth = APIAuth.NO)
	public int updateDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return dwsAssetA1413DepStrucDao.updateDwsAssetA1413DepStruc(params).getEffect();
	}
	
	@API(desc = "删除A1413存款期限结构及相关业务情况补录表", params = "id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date", auth = APIAuth.NO)
	public int deleteDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		return dwsAssetA1413DepStrucDao.deleteDwsAssetA1413DepStruc(params).getEffect();
	}

	@API(desc = "重新生成报表A1413存款期限结构及相关业务情况补录表", params = "id,org_one,org_one_two,org_two_thr,org_thr,dom_one,dom_one_two,dom_two_thr,dom_thr,cra_dep,cra_inv,act_dt,deal_date", auth = APIAuth.YES)
	public String buildDwsAssetA1413DepStruc(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		try {
			//校验唯一性
			log.info("---------- " + "R107报表数据生成:任务 开始执行 -----------");
			String base_date = dwsAssetA1413DepStrucDao.findBaseDate(params).getRows().get(0).getActDt();
			modelDataToGenService.DwdToAppDataProcessGenMethod("R107", base_date);

			log.info("---------- " + "R107报表数据生成:任务 执行结束 -----------");
			return RequestSupport.updateReturnJson(true,"重新生成报表成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"重新生成报表失败！" + e.getMessage(),null).toString();
		}
	}

	@API(desc = "根据日期范围生成净值信息",auth = APIAuth.YES)
	public String rangeInsertNavInfo(SqlParam<DwsAssetA1413DepStruc> params) throws Exception {
		try {
			String dateRange = params.getModel().getDateRange();
			String[] keyArr = dateRange.split("\\,");
			List<String> dateArr = DateUtil.calNatureRangeDateListByBorder(keyArr[0], keyArr[1], "02");//获取日期List

			for (String date : dateArr) {
				PubReq r1 = new PubReq();
				r1.setTaskDate(date);
				r1.setTaskId("M095");
				// PubReq r2 = new PubReq();
				// r2.setTaskDate(date);
				// r2.setTaskId("R012");
				businessBaseTaskService.dataModeNavCalConvert(r1);// 净值日历
				businessBaseTaskService.dataModeNavInfoConvert(r1);// 净值信息
				// businessBaseTaskService.dataModeFqNavConvert(r2); // 复权净值
			}
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}

}
