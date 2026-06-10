package com.kayak.pms.channelInterface.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.channelInterface.dao.ChannelParamSettingDao;
import com.kayak.pms.channelInterface.model.ChannelParamSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "渠道接口字段配置服务", model = ChannelParamSetting.class)
public class ChannelParamSettingService {

	@Autowired
	private ChannelParamSettingDao channelParamSettingDao;

	@API(desc = "查询渠道接口字段配置信息", auth = APIAuth.YES)
	public SqlResult<ChannelParamSetting> findChannelParamSettings(SqlParam<ChannelParamSetting> params) throws Exception {
		params.setMakeSql(true);
		return channelParamSettingDao.findChannelParamSettings(params);
	}

	@API(desc = "添加渠道接口字段配置", auth = APIAuth.NO)
	public String addChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		List<ChannelParamSetting> list = channelParamSettingDao.findChannelParamSettingsByCondition(params.getModel());
		if (CollectionUtil.isNotEmpty(list))
			return RequestSupport.updateReturnJson(false,"该字段配置已存在",null).toString();
		params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
		params.getModel().setCrtTime(DateUtil.getTimestamp19());
		UpdateResult result = channelParamSettingDao.addChannelParamSetting(params);
		if (result.getEffect() > 0)
			return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
		return RequestSupport.updateReturnJson(false,"添加失败",null).toString();
	}
	
	@API(desc = "修改渠道接口字段配置", auth = APIAuth.NO)
	public int updateChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		return channelParamSettingDao.updateChannelParamSetting(params).getEffect();
	}
	
	@API(desc = "删除渠道接口字段配置", auth = APIAuth.NO)
	public int deleteChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		return channelParamSettingDao.deleteChannelParamSetting(params).getEffect();
	}

}
