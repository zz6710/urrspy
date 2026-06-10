package com.kayak.pms.channelInterface.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.channelInterface.dao.ChannelProdFileDetailDao;
import com.kayak.pms.channelInterface.model.ChannelProdFileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "产品渠道对接明细服务", model = ChannelProdFileDetail.class)
public class ChannelProdFileDetailService {

	@Autowired
	private ChannelProdFileDetailDao channelProdFileDetailDao;

	@API(desc = "查询产品渠道对接明细信息", auth = APIAuth.NO)
	public SqlResult<ChannelProdFileDetail> findChannelProdFileDetails(SqlParam<ChannelProdFileDetail> params) throws Exception {
		params.setMakeSql(true);
		return channelProdFileDetailDao.findChannelProdFileDetails(params);
	}

	@API(desc = "添加产品渠道对接明细", auth = APIAuth.YES)
	public int addChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return channelProdFileDetailDao.addChannelProdFileDetail(params).getEffect();
	}
	
	@API(desc = "修改产品渠道对接明细", auth = APIAuth.YES)
	public int updateChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return channelProdFileDetailDao.updateChannelProdFileDetail(params).getEffect();
	}
	
	@API(desc = "删除产品渠道对接明细", auth = APIAuth.YES)
	public int deleteChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return channelProdFileDetailDao.deleteChannelProdFileDetail(params).getEffect();
	}

	@API(desc = "根据对接文件id查询文件内容信息", auth = APIAuth.NO)
	public SqlResult<ChannelProdFileDetail> findChannelProdFileDetails1(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return channelProdFileDetailDao.findChannelProdFileDetails1(params);
	}
}
