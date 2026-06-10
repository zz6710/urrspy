package com.kayak.subject.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.dao.DwsG06BIIDerivateInfoDao;
import com.kayak.subject.model.DwsG06BIIDerivateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "g06BII衍生品明细中间表服务", model = DwsG06BIIDerivateInfo.class)
@Slf4j
public class DwsG06BIIDerivateInfoService {

	@Autowired
	private DwsG06BIIDerivateInfoDao dwsG06BIIDerivateInfoDao;

	@API(desc = "查询g06BII衍生品明细中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsG06BIIDerivateInfo> findDwsG06BIIDerivateInfo(SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
		return dwsG06BIIDerivateInfoDao.findDwsG06BIIDerivateInfos(params);
	}
	
	@API(desc = "修改g06BII衍生品明细中间表", auth = APIAuth.YES)
	public String updateDwsG06BIIDerivateInfo (SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
		try {
			dwsG06BIIDerivateInfoDao.updateDwsG06BIIDerivateInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}
	
	@API(desc = "删除g06BII衍生品明细中间表", auth = APIAuth.YES)
	public String deleteDwsG06BIIDerivateInfo (SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
		try {
			dwsG06BIIDerivateInfoDao.deleteDwsG06BIIDerivateInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		}catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}

}
