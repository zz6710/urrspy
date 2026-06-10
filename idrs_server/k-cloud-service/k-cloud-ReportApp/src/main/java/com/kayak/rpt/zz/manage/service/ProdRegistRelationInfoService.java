package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.ProdRegistRelationInfoDao;
import com.kayak.rpt.zz.manage.model.ProdRegistRelationInfo;

@Service
@APIDefine(desc = "产品登记编码服务", model = ProdRegistRelationInfo.class)
public class ProdRegistRelationInfoService {

	@Autowired
	private ProdRegistRelationInfoDao prodRegistRelationInfoDao;

	@API(desc = "查询产品登记编码信息", auth = APIAuth.YES)
	public SqlResult<ProdRegistRelationInfo> findProdRegistRelationInfos(SqlParam<ProdRegistRelationInfo> params) throws Exception {
//		params.setMakeSql(true);
		return prodRegistRelationInfoDao.findProdRegistRelationInfos(params);
	}

	@API(desc = "添加产品登记编码", params = "prod_code,reg_code,workdate,remark", auth = APIAuth.YES)
	public int addProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return prodRegistRelationInfoDao.addProdRegistRelationInfo(params).getEffect();
	}
	
	@API(desc = "修改产品登记编码", params = "prod_code,reg_code,workdate,remark", auth = APIAuth.YES)
	public int updateProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return prodRegistRelationInfoDao.updateProdRegistRelationInfo(params).getEffect();
	}
	
	@API(desc = "删除产品登记编码", params = "prod_code,reg_code,workdate,remark", auth = APIAuth.YES)
	public int deleteProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return prodRegistRelationInfoDao.deleteProdRegistRelationInfo(params).getEffect();
	}

}
