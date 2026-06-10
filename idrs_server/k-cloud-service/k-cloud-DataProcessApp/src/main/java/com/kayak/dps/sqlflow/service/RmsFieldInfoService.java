package com.kayak.dps.sqlflow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.sqlflow.dao.RmsFieldInfoDao;
import com.kayak.dps.sqlflow.model.RmsFieldInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "表字段基础信息服务", model = RmsFieldInfo.class)
@RefreshScope
public class RmsFieldInfoService {

	@Autowired
	private RmsFieldInfoDao rmsFieldInfoDao;
	@Value("${lineage.excludeColumn}")
	private String excludeColumn;

	@API(desc = "查询表字段基础信息信息", auth = APIAuth.YES)
	public SqlResult<RmsFieldInfo> findRmsFieldInfos(SqlParam<RmsFieldInfo> params) throws Exception {
		return rmsFieldInfoDao.findRmsFieldInfos(params);
	}

	@API(desc = "添加表字段基础信息", params = "id,table_field_id,table_info_id,field_name,field_data_type,field_comment,field_index", auth = APIAuth.NO)
	public int addRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return rmsFieldInfoDao.addRmsFieldInfo(params).getEffect();
	}

	@API(desc = "修改表字段基础信息", params = "id,table_field_id,table_info_id,field_name,field_data_type,field_comment,field_index", auth = APIAuth.NO)
	public int updateRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return rmsFieldInfoDao.updateRmsFieldInfo(params).getEffect();
	}

	@API(desc = "删除表字段基础信息", params = "id,table_field_id,table_info_id,field_name,field_data_type,field_comment,field_index", auth = APIAuth.NO)
	public int deleteRmsFieldInfo(SqlParam<RmsFieldInfo> params) throws Exception {
		return rmsFieldInfoDao.deleteRmsFieldInfo(params).getEffect();
	}

	/**
	 * 更新字段信息
	 * @param database 数据库名
	 * @throws Exception
	 */
	public void updateRmsTableField(String database) throws Exception {
		rmsFieldInfoDao.truncateRmsFieldInfo();
		rmsFieldInfoDao.insertRmsFieldInfoFromSchema(database);
		rmsFieldInfoDao.deleteTwoDimensionalFiledInfo(database); //删除二维报表字段信息
		rmsFieldInfoDao.insertTwoDimensionalFiledInfo(database); //插入二维报表字段信息
	}

	/**
	 *
	 * @param tableInfoId
	 * @return
	 * @throws Exception
	 */
	public List<RmsFieldInfo> findFieldNameByTableInfoId(String tableInfoId) throws Exception {
		return rmsFieldInfoDao.findFieldNameByTableInfoId(tableInfoId, excludeColumn);
	}

}
