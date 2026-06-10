package com.kayak.rpt.zz.manage.service;



import com.kayak.rpt.zz.manage.dao.ZonClcInfoDao;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdIssuanceRegistInfo;
import com.kayak.rpt.zz.manage.model.ZonClcInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;


@Service
@APIDefine(desc = "产品募集区域信息服务", model = ZonClcInfo.class)
public class ZonClcInfoService {

	@Autowired
	private ZonClcInfoDao zonClcInfoDao;

	@API(desc = "查询产品募集区域信息信息", auth = APIAuth.NO)
	public SqlResult<ZonClcInfo> findZonClcInfos(SqlParam<ZonClcInfo> params) throws Exception {
		params.setMakeSql(true);
		return zonClcInfoDao.findZonClcInfos(params);
	}

	@API(desc = "添加产品募集区域信息", params = "prod_code,zon_clc,zon_clc_amt", auth = APIAuth.YES)
	public int addZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return zonClcInfoDao.addZonClcInfo(params).getEffect();
	}
	
	@API(desc = "修改产品募集区域信息", params = "prod_code,zon_clc,zon_clc_amt", auth = APIAuth.YES)
	public int updateZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return zonClcInfoDao.updateZonClcInfo(params).getEffect();
	}
	
	@API(desc = "删除产品募集区域信息", params = "prod_code,zon_clc,zon_clc_amt", auth = APIAuth.YES)
	public int deleteZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return zonClcInfoDao.deleteZonClcInfo(params).getEffect();
	}

	public void addImportZonClcInfo(InitialSubRegistInfo initialSubRegistInfo) throws Exception {
		String prodCode = initialSubRegistInfo.getProdCode();
		String zonClcInfo = initialSubRegistInfo.getZonClcInfo();
		String[] splits = zonClcInfo.split(",");
		if (splits.length == 1) {
			ZonClcInfo z = new ZonClcInfo();
			z.setProdCode(prodCode);
			String[] s = splits[0].split(" ");
			z.setZonClc(s[0]);
			z.setZonClcAmt(s[2]);
			zonClcInfoDao.addImportZonClcInfo(z);
		}
		if (splits.length > 1) {
			for (String sp : splits) {
				ZonClcInfo z = new ZonClcInfo();
				z.setProdCode(prodCode);
				String[] s = sp.split(" ");
				z.setZonClc(s[0]);
				z.setZonClcAmt(s[2]);
				zonClcInfoDao.addImportZonClcInfo(z);
			}
		}
	}
}
