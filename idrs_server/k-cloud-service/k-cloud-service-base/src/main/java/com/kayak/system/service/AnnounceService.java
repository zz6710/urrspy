package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.AnnounceDao;
import com.kayak.system.model.Announce;
import com.kayak.system.model.AnnounceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
@APIDefine(desc = "公告服务", model = Announce.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class AnnounceService {

	private final AnnounceDao announceDao;

	@Value("${upload.path}")
	private String uploadPath;

	@API(desc = "查询公告列表")
	public SqlResult<Announce> find(SqlParam<Announce> param) throws Exception {
		param.setMakeSql(true);
		return announceDao.find(param);
	}


	@API(desc = "首页查看公告列表")
	public SqlResult<AnnounceInfo> show(SqlParam<AnnounceInfo> param) throws Exception {
		Date now = new Date();
		AnnounceInfo model = param.getModel();
		String nowStr = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
		model.setEffectiveDate(nowStr);
		model.setInvalidDate(nowStr);
		return announceDao.show(param);
	}

	@API(desc = "添加公告", operation = APIOperation.INSTER)
	public String add(SqlParam<AnnounceInfo> param) throws Exception {
		AnnounceInfo model = param.getModel();
		model.setCreateuserid((String)SysUtil.getSysUserParamValue("sys_user_userid"));
		model.setCreateuserName((String)SysUtil.getSysUserParamValue("sys_user_username"));
		Date now = new Date();
		model.setCreatedate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
		model.setCreatetime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));

		announceDao.add(param);
		return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
	}


	@API(desc = "修改公告", operation = APIOperation.UPDATE)
	public String edit(SqlParam<AnnounceInfo> param) throws Exception {
		String annid = param.getModel().getAnnid();
		Announce oldAnnounce = announceDao.get(annid);

		AnnounceInfo newAnnounce = param.getModel();
		newAnnounce.setEdituserid((String)SysUtil.getSysUserParamValue("sys_user_userid"));
		Date now = new Date();
		newAnnounce.setEditdate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
		newAnnounce.setEdittime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));

		announceDao.edit(param);

		String oldAnnfilecode = oldAnnounce.getAnnfilecode();
		if (Tools.isNotBlank(oldAnnfilecode) && !newAnnounce.getAnnfilecode().equals(oldAnnfilecode)) {
			deleteFile(oldAnnounce.getAnnfilepath());
		}

		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}

	@API(desc = "删除公告")
	public String delete(SqlParam<Announce> param) throws Exception {
		String annid = param.getModel().getAnnid();
		Announce announce = announceDao.get(annid);
		if (announce == null) {
			return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
		}
		announceDao.delete(annid);

		deleteFile(announce.getAnnfilepath());
		return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
	}

	private void deleteFile(String path) {
		if (Tools.isBlank(path)) {
			return;
		}
		try {
			File file = new File(uploadPath + path);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			log.error("删除文件{}失败：", path, e);
		}
	}
}
