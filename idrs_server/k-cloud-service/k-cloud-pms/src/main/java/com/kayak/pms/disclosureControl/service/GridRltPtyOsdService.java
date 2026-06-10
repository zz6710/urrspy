package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridRltPtyOsdDao;
import com.kayak.pms.disclosureControl.model.GridRltPtyOsd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "交易对手为关联方数据服务", model = GridRltPtyOsd.class)
public class GridRltPtyOsdService {

	@Autowired
	private GridRltPtyOsdDao gridRltPtyOsdDao;


	@API(desc = "交易对手为关联方数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridRltPtyOsd> findGridRltPtyOsd(SqlParam<GridRltPtyOsd> params) throws Exception {
		params.setMakeSql(true);
		return gridRltPtyOsdDao.findGridRltPtyOsd(params);
	}

	@API(desc = "交易对手为关联方数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridRltPtyOsd(SqlParam<GridRltPtyOsd> params) throws Exception {
		return gridRltPtyOsdDao.updateGridRltPtyOsd(params);
	}

	@API(desc = "交易对手为关联方数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridRltPtyOsd(SqlParam<GridRltPtyOsd> params) throws Exception {
		return gridRltPtyOsdDao.deleteGridRltPtyOsd(params);
	}
	@API(desc = "查询资产代码与资产名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<GridRltPtyOsd> findScrIdAndscrNm(SqlParam<GridRltPtyOsd> params) throws Exception {
		return gridRltPtyOsdDao.findScrIdAndscrNm(params);
	}
}
