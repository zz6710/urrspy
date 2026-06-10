package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.DataSupplementModel;
import com.kayak.dps.ods.dao.DataSupplementDao;
import com.kayak.dps.valtabimp.model.OdsReadAssetsReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "DataSupplement服务", model = DataSupplementModel.class)
public class DataSupplementService {

	@Autowired
	private DataSupplementDao dataSupplementDao;

	@API(desc = "查询报送信息", auth = APIAuth.YES)
	public SqlResult<DataSupplementModel> findDataSupplements(SqlParam<DataSupplementModel> params) throws Exception {
		return dataSupplementDao.findDataSupplements(params);
	}

	@API(desc = "添加DataSupplement", params = "report_date,balance_assets,financial_assets,remark,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public String addDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
			params.getModel().setCrtDate(DateUtil.getNowDate());
			params.getModel().setCrtTime(DateUtil.getNowTime());
			dataSupplementDao.addDataSupplement(params).getEffect();
			return RequestSupport.updateReturnJson(true, "增加成功", null).toString();
	}

	@API(desc = "修改DataSupplement", params = "report_date,balance_assets,financial_assets,remark,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public String updateDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
			params.getModel().setUpdDate(DateUtil.getNowDate());
			params.getModel().setUpdTime(DateUtil.getNowTime());
			dataSupplementDao.updateDataSupplement(params).getEffect();
			return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}
	
	@API(desc = "删除DataSupplement", params = "report_date,balance_assets,financial_assets,remark,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int deleteDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
		return dataSupplementDao.deleteDataSupplement(params).getEffect();
	}

	@API(desc = "唯一校验", auth = APIAuth.NO)
	public SqlResult<DataSupplementModel> findOnlyDataSupplements(SqlParam<DataSupplementModel> params) throws Exception {
		return dataSupplementDao.findOnlyDataSupplements(params);
	}
}
