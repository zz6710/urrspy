package com.kayak.pms.channelInterface.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.channelInterface.dao.ChannelInterfaceTaskInfoDao;
import com.kayak.pms.channelInterface.model.ChannelInterfaceTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "渠道接口任务详情服务", model = ChannelInterfaceTaskInfo.class)
public class ChannelInterfaceTaskInfoService {

	@Autowired
	private ChannelInterfaceTaskInfoDao channelInterfaceTaskInfoDao;

	@API(desc = "查询渠道接口任务详情信息", auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceTaskInfo> findChannelInterfaceTaskInfos(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		params.setMakeSql(true);
		return channelInterfaceTaskInfoDao.findChannelInterfaceTaskInfos(params);
	}

	@API(desc = "添加渠道接口任务详情", auth = APIAuth.NO)
	public int addChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return channelInterfaceTaskInfoDao.addChannelInterfaceTaskInfo(params).getEffect();
	}
	
	@API(desc = "修改渠道接口任务详情", auth = APIAuth.NO)
	public int updateChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return channelInterfaceTaskInfoDao.updateChannelInterfaceTaskInfo(params).getEffect();
	}
	
	@API(desc = "删除渠道接口任务详情", auth = APIAuth.NO)
	public int deleteChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return channelInterfaceTaskInfoDao.deleteChannelInterfaceTaskInfo(params).getEffect();
	}

}
