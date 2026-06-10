package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.APIOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.T8DisclosureNoticeChannelDao;
import com.kayak.pms.disclosureControl.model.T8DisclosureNoticeChannel;

@Service
@APIDefine(desc = "公告渠道信息表服务", model = T8DisclosureNoticeChannel.class)
public class T8DisclosureNoticeChannelService {

	@Autowired
	private T8DisclosureNoticeChannelDao t8DisclosureNoticeChannelDao;

	@API(desc = "查询公告渠道信息表信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8DisclosureNoticeChannel> findT8DisclosureNoticeChannels(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		//params.setMakeSql(true);
		return t8DisclosureNoticeChannelDao.findT8DisclosureNoticeChannels(params);
	}

	/*@API(desc = "添加公告渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO,operation = APIOperation.INSTER)
	public int addT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return t8DisclosureNoticeChannelDao.addT8DisclosureNoticeChannel(params).getEffect();
	}
	
	@API(desc = "修改公告渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO,operation = APIOperation.UPDATE)
	public int updateT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return t8DisclosureNoticeChannelDao.updateT8DisclosureNoticeChannel(params).getEffect();
	}
	
	@API(desc = "删除公告渠道信息表", params = "id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name", auth = APIAuth.NO,operation = APIOperation.DELETE)
	public int deleteT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return t8DisclosureNoticeChannelDao.deleteT8DisclosureNoticeChannel(params).getEffect();
	}*/

}
