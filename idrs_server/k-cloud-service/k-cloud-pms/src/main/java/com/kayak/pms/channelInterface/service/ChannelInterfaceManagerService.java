package com.kayak.pms.channelInterface.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.channelInterface.dao.ChannelInterfaceManagerDao;
import com.kayak.pms.channelInterface.dao.ChannelParamSettingDao;
import com.kayak.pms.channelInterface.model.ChannelInterfaceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@APIDefine(desc = "渠道接口配置服务", model = ChannelInterfaceManager.class)
public class ChannelInterfaceManagerService {

	@Autowired
	private ChannelInterfaceManagerDao channelInterfaceManagerDao;

	@Autowired
	private ChannelParamSettingDao channelParamSettingDao;

	@API(desc = "查询渠道接口配置信息", auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagers(SqlParam<ChannelInterfaceManager> params) throws Exception {
		//params.setMakeSql(true);
		return channelInterfaceManagerDao.findChannelInterfaceManagers(params);
	}

	@API(desc = "添加渠道接口配置", auth = APIAuth.NO)
	public String addChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		//添加之前首选判断是否该渠道已经存在重复接口
		List<ChannelInterfaceManager> list = channelInterfaceManagerDao.findChannelInterfaceManagerByNo(params.getModel().getChannelNo(),params.getModel().getInterfaceNo());
		if (CollectionUtil.isNotEmpty(list))
			return RequestSupport.updateReturnJson(false,"该接口编码已经存在",null).toString();
		params.getModel().setCrtUser((String)SysUtil.getSysUserParamValue("sys_user_username"));
		params.getModel().setCrtTime(DateUtil.getTimestamp19());
		UpdateResult result =  channelInterfaceManagerDao.addChannelInterfaceManager(params);
		if (result.getEffect() > 0)
			return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
		return RequestSupport.updateReturnJson(false,"操作失败",null).toString();

	}
	
	@API(desc = "修改渠道接口配置", auth = APIAuth.NO)
	public int updateChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return channelInterfaceManagerDao.updateChannelInterfaceManager(params).getEffect();
	}
	
	@API(desc = "删除渠道接口配置", auth = APIAuth.NO)
	public String deleteChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		AtomicInteger flag = new AtomicInteger(0);
		DaoUtil.doTrans(() -> {
			UpdateResult  result =  channelInterfaceManagerDao.deleteChannelInterfaceManager(params);
			flag.set(result.getEffect());
			channelParamSettingDao.deleteChannelParamSetting(params.getModel().getChannelNo(),params.getModel().getInterfaceNo());
		});
		if (flag.get()>0)
			return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
		return RequestSupport.updateReturnJson(false,"操作失败",null).toString();

	}


	@API(desc = "查询渠道接口配置", auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagerByNo(SqlParam<ChannelInterfaceManager> params) throws Exception {
		List<ChannelInterfaceManager> list = channelInterfaceManagerDao.findChannelInterfaceManagerByNo(params.getModel().getChannelNo(),params.getModel().getInterfaceNo());
		SqlResult<ChannelInterfaceManager> result = new SqlResult<>();
		result.setResults(list.size());
		result.setRows(list);
		result.setDesensitized(false);
		return result;
	}

	@API(desc = "查询全部渠道", auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceManager> findAllChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return channelInterfaceManagerDao.findAllChannelInterfaceManager(params);
	}

	@API(desc = "根据渠道code和参数模板查询渠道接口" , auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagerByChannelNo(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return channelInterfaceManagerDao.findChannelInterfaceManagerByChannelNo(params);
	}
	@API(desc = "根据渠道code查询渠道接口" , auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagerByChannelNo1(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return channelInterfaceManagerDao.findChannelInterfaceManagerByChannelNo1(params);
	}
}
