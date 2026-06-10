package com.kayak.subject.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.DwdSpvInvPrdRftDao;
import com.kayak.subject.model.DwdSpvInvPrdRft;

import java.util.List;

@Service
@APIDefine(desc = "人行特定目的载体产品代码映射表服务", model = DwdSpvInvPrdRft.class)
public class DwdSpvInvPrdRftService {

	@Autowired
	private DwdSpvInvPrdRftDao dwdSpvInvPrdRftDao;

	@API(desc = "查询人行特定目的载体产品代码映射表信息", auth = APIAuth.YES)
	public SqlResult<DwdSpvInvPrdRft> findDwdSpvInvPrdRfts(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		params.setMakeSql(true);
		return dwdSpvInvPrdRftDao.findDwdSpvInvPrdRfts(params);
	}

	@API(desc = "添加人行特定目的载体产品代码映射表", params = "spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time", auth = APIAuth.NO)
	public String addDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		try {
			List<DwdSpvInvPrdRft> l = dwdSpvInvPrdRftDao.findDwdSpvInvPrdRftsSpvCode(params).getRows();
			if (l.size() > 0) {
				return RequestSupport.updateReturnJson(false,"该特定目的载体代码已存在！",null).toString();
			}
			List<DwdSpvInvPrdRft> l1 = dwdSpvInvPrdRftDao.findDwdSpvInvPrdRftsAmpsCode(params).getRows();
			if (l1.size() > 0) {
				return RequestSupport.updateReturnJson(false,"该资管产品统计编码已存在！",null).toString();
			}
			params.getModel().setCreateDate(DateUtil.getNowDate());
			params.getModel().setCreateTime(DateUtil.getNowTime());
			params.getModel().setDataFrom("01");
			dwdSpvInvPrdRftDao.addDwdSpvInvPrdRft(params).getEffect();
			return RequestSupport.updateReturnJson(true,"新增成功！",null).toString();
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(false,"新增失败！",null).toString();
		}
	}

	@API(desc = "修改人行特定目的载体产品代码映射表", params = "spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time", auth = APIAuth.NO)
	public String updateDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		try {
			List<DwdSpvInvPrdRft> l1 = dwdSpvInvPrdRftDao.findDwdSpvInvPrdRftsAmpsCodeBySpvCode(params).getRows();
			if (l1.size() > 0) {
				return RequestSupport.updateReturnJson(false,"该资管产品统计编码已存在！",null).toString();
			}
			params.getModel().setUpdateDate(DateUtil.getNowDate());
			params.getModel().setUpdateTime(DateUtil.getNowTime());
			dwdSpvInvPrdRftDao.updateDwdSpvInvPrdRft(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}

	@API(desc = "删除人行特定目的载体产品代码映射表", params = "spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time", auth = APIAuth.NO)
	public int deleteDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return dwdSpvInvPrdRftDao.deleteDwdSpvInvPrdRft(params).getEffect();
	}

}
