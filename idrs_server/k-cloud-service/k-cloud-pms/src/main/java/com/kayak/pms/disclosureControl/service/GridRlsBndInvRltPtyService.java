package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridFbassetHoldFronttenDao;
import com.kayak.pms.disclosureControl.dao.GridRlsBndInvRltPtyDao;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldFrontten;
import com.kayak.pms.disclosureControl.model.GridRlsBndInvRltPty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "发行证券数据服务", model = GridRlsBndInvRltPty.class)
public class GridRlsBndInvRltPtyService {

	@Autowired
	private GridRlsBndInvRltPtyDao gridRlsBndInvRltPtyDao;


	@API(desc = "发行证券数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridRlsBndInvRltPty> findGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params) throws Exception {
		params.setMakeSql(true);
		return gridRlsBndInvRltPtyDao.findGridRlsBndInvRltPty(params);
	}

	@API(desc = "发行证券数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params) throws Exception {
		return gridRlsBndInvRltPtyDao.updateGridRlsBndInvRltPty(params);
	}

	@API(desc = "发行证券数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params) throws Exception {
		return gridRlsBndInvRltPtyDao.deleteGridRlsBndInvRltPty(params);
	}

}
