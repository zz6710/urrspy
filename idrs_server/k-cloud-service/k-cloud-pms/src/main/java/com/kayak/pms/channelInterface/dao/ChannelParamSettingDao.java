package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelParamSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelParamSettingDao extends ComnDao {

	public SqlResult<ChannelParamSetting> findChannelParamSettings(SqlParam<ChannelParamSetting> params) throws Exception {
		return super.findRows("SELECT id,channel_no,interface_no,field_name,field_dict,other_field,remark,crt_user,crt_time,field FROM t8_channel_param_setting order by id+0 asc", params);
	}

	public UpdateResult addChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		return super.update("INSERT INTO t8_channel_param_setting(id,channel_no,interface_no,field_name,field_dict,other_field,remark,crt_user,crt_time,field) VALUES($AUTOIDS{id},$S{channelNo},$S{interfaceNo},$S{fieldName},$S{fieldDict},$S{otherField},$S{remark},$S{crtUser},$S{crtTime},$S{field})",
				params.getModel());
	}
	
	public UpdateResult updateChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		return super.update("UPDATE t8_channel_param_setting SET channel_no=$S{channelNo} ,interface_no=$S{interfaceNo} ,field_name=$S{fieldName} ,field_dict=$S{fieldDict} ,other_field=$S{otherField} ,remark=$S{remark} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime},field = $S{field}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteChannelParamSetting(SqlParam<ChannelParamSetting> params) throws Exception {
		return super.update("DELETE FROM t8_channel_param_setting WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteChannelParamSetting(String channelNo,String interfaceNo) throws Exception {
		return super.update("DELETE FROM t8_channel_param_setting WHERE  channel_no = '"+channelNo+"' and interface_no = '"+interfaceNo+"'",
				channelNo);
	}

	public List<ChannelParamSetting> findChannelParamSettingsByCondition(ChannelParamSetting params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,channel_no,interface_no,field_name,field_dict,other_field,remark,crt_user,crt_time,field FROM t8_channel_param_setting where 1=1 ");
		if (StringUtils.isNotBlank(params.getChannelNo()))
			sql.append(" and channel_no = '").append(params.getChannelNo()).append("'");
		if (StringUtils.isNotBlank(params.getInterfaceNo()))
			sql.append(" and interface_no = '").append(params.getInterfaceNo()).append("'");
		if (StringUtils.isNotBlank(params.getField()))
			sql.append(" and field = '").append(params.getField()).append("'");
		sql.append(" order by id+0 asc");
		return super.findRows(ChannelParamSetting.class,sql.toString(),0,params);
	}

	//查询数据字典
	public SqlRow getChanelDict(String dict,String itemkey) throws Exception {
		String sql = "select dict,field,item_key,item_val,other_field,other_item_key,other_item_value from t8_channel_dict_item where dict = '"+dict+"' and item_key = '"+itemkey+"'";
		return super.findRow(sql,dict);
	}

	public SqlRow findChannelParamSettingByInterfaceNo(String interfaceNo,String field) throws Exception {
		String sql = "select interface_no interfaceNo,field_name fieldName,field_dict fieldDict,other_field otherField,field from t8_channel_param_setting where interface_no = '"+interfaceNo+"' and field = '"+field+"'";
		return super.findRow(sql,interfaceNo);
	}
}
