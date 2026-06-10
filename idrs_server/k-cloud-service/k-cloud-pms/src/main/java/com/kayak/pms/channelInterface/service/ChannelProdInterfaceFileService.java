package com.kayak.pms.channelInterface.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.channelInterface.dao.ChannelProdInterfaceFileDao;
import com.kayak.pms.channelInterface.model.ChannelProdInterfaceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品渠道对接文件服务", model = ChannelProdInterfaceFile.class)
public class ChannelProdInterfaceFileService {

	@Autowired
	private ChannelProdInterfaceFileDao channelProdInterfaceFileDao;

	@API(desc = "查询产品渠道对接文件信息", auth = APIAuth.NO)
	public SqlResult<ChannelProdInterfaceFile> findChannelProdInterfaceFiles(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return channelProdInterfaceFileDao.findChannelProdInterfaceFiles1(params);
	}

	@API(desc = "添加产品渠道对接文件", auth = APIAuth.NO)
	public int addChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return channelProdInterfaceFileDao.addChannelProdInterfaceFile(params).getEffect();
	}
	
	@API(desc = "修改产品渠道对接文件", auth = APIAuth.NO)
	public int updateChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return channelProdInterfaceFileDao.updateChannelProdInterfaceFile(params).getEffect();
	}
	
	@API(desc = "删除产品渠道对接文件", auth = APIAuth.NO)
	public int deleteChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return channelProdInterfaceFileDao.deleteChannelProdInterfaceFile(params).getEffect();
	}

}
