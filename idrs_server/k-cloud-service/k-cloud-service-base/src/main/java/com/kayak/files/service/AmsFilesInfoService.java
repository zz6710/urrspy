package com.kayak.files.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.files.dao.AmsFilesInfoDao;
import com.kayak.files.model.AmsFilesInfo;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.io.File;
import java.util.List;

@Service
@APIDefine(desc = "文件管理服务", model = AmsFilesInfo.class)
@Slf4j
public class AmsFilesInfoService {

	@Autowired
	private AmsFilesInfoDao amsFilesInfoDao;

	@API(desc = "查询文件管理信息", auth = APIAuth.YES)
	public SqlResult<AmsFilesInfo> findAmsFilesInfos(SqlParam<AmsFilesInfo> params) throws Exception {
		return amsFilesInfoDao.findAmsFilesInfos(params);
	}

	@API(desc = "添加文件管理", params = "id,file_name,oss_path,file_type,upload_time,server_path", auth = APIAuth.NO)
	public int addAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		return amsFilesInfoDao.addAmsFilesInfo(params).getEffect();
	}
	
	@API(desc = "修改文件管理", params = "id,file_name,oss_path,file_type,upload_time,server_path", auth = APIAuth.NO)
	public int updateAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		return amsFilesInfoDao.updateAmsFilesInfo(params).getEffect();
	}
	
	@API(desc = "删除文件", params = "id,file_name,oss_path,file_type,upload_time,server_path", auth = APIAuth.NO)
	public int deleteAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		List<AmsFilesInfo> list = findAmsFilesInfoById(params.getModel().getId());
		if (list.isEmpty()) {
			return 0;
		}
		AmsFilesInfo amsFilesInfo = list.get(0);
		String ossPath = amsFilesInfo.getOssPath();
		try {
			FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
			fileTransfer.deleteFileAndDisconnect(ossPath);
		} catch (Exception e) {
			log.error("oss文件删除失败", e);
		}
		File file = new File(amsFilesInfo.getServerPath());
		if (file.exists()) {
			file.delete();
		}
		return amsFilesInfoDao.deleteAmsFilesInfo(params).getEffect();
	}

	public List<AmsFilesInfo> findAmsFilesInfoById(String id) throws Exception {
		return amsFilesInfoDao.findAmsFilesInfoById(id);
	}

}
