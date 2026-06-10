package com.kayak.files.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.files.model.AmsFilesInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AmsFilesInfoDao extends ComnDao {

	public SqlResult<AmsFilesInfo> findAmsFilesInfos(SqlParam<AmsFilesInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,file_name,oss_path,file_type,upload_time,server_path FROM ams_files_info where 1=1");
		if (Tools.isNotEmpty(params.getModel().getId())) {
			sql.append(" and id = '").append(params.getModel().getId()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getFileName())) {
			sql.append(" and file_name like '%").append(params.getModel().getFileName()).append("%'");
		}
		sql.append(" order by upload_time desc");
		return super.findRows(sql.toString(), params);
	}

	public UpdateResult addAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		return super.update("INSERT INTO ams_files_info(id,file_name,oss_path,file_type,upload_time,server_path) VALUES($AUTOIDS{id},$S{fileName},$S{ossPath},$S{fileType},$S{uploadTime},$S{serverPath})",
				params.getModel());
	}
	
	public UpdateResult updateAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		return super.update("UPDATE ams_files_info SET file_name=$S{fileName} ,oss_path=$S{ossPath} ,file_type=$S{fileType} ,upload_time=$S{uploadTime} ,server_path=$S{serverPath}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteAmsFilesInfo(SqlParam<AmsFilesInfo> params) throws Exception {
		return super.update("DELETE FROM ams_files_info WHERE  id=$S{id} ",
				params.getModel());
	}

	public List<AmsFilesInfo> findAmsFilesInfoById(String id) throws Exception {
		if (Tools.isNotEmpty(id)) {
			Map<String, Object> params = new HashMap<>();
			String sql = "SELECT id,file_name,oss_path,file_type,upload_time,server_path FROM ams_files_info where id='" + id + "'";
			return super.findRows(AmsFilesInfo.class, sql, 0, params);
		}
		return new ArrayList<>();
	}

}
