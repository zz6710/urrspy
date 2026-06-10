package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureOperationDao extends ComnDao {

	public SqlResult<DisclosureOperation> findDisclosureOperations(SqlParam<DisclosureOperation> params) throws Exception {
		return super.findRows("SELECT id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark FROM idb_disclosure_operation", DataSourceProperty.IDB, params);
	}

    public List<SqlRow> getOperationId(String t8ProdInfoId) throws Exception {
        return super.findRows("SELECT id from idb_disclosure_operation  where t8_prod_info_id=$S{t8ProdInfoId}", DataSourceProperty.IDB, t8ProdInfoId);
    }

	public UpdateResult addDisclosureOperation(SqlParam<DisclosureOperation> params) throws Exception {
		return insertDisOperation(params.getModel());
	}

	public List<SqlRow> getStatus(String t8ProdInfoId) throws Exception {
        return super.findRows("SELECT status from idb_disclosure_operation  where t8_prod_info_id=$S{t8ProdInfoId}", DataSourceProperty.IDB, t8ProdInfoId);
    }

    public int insertDisOperation1(DisclosureOperation disclosureOperation) throws Exception {
        return super.update("INSERT INTO idb_disclosure_operation(id, t8_prod_info_id, operation_type, disclosure_type, roleid, userid, status,deal_table, deal_id, crt_date, crt_time, crt_user_id, crt_user_name, end_date,end_time, remark) " +
                        "VALUES($AUTOIDS{id}, $S{t8ProdInfoId}, $S{operationType}, $S{disclosureType}, $S{roleid}, $S{userid}, $S{status}, $S{dealTable}, $S{dealId}, $S{crtDate}, $S{crtTime}, $S{crtUserId}, $S{crtUserName}, $S{endDate}, $S{endTime},$S{remark})",
				DataSourceProperty.IDB, disclosureOperation).getEffect();
    }
    //更新 信披代办中 投资经理信息
    public Integer updDisOperationInfo(DisclosureOperation disclosureOperation) throws Exception {
        return super.update("update idb_disclosure_operation set crt_date=$S{crtDate}, crt_time=$S{crtTime}, crt_user_id=$S{crtUserId} WHERE deal_id=$S{dealId} and roleid = '14' and userid =$S{userid}",
				DataSourceProperty.IDB, disclosureOperation).getEffect();
    }
    public int deleteUserId(String userId,String prodInfoId) throws Exception {
        return super.update("DELETE FROM idb_disclosure_operation WHERE userid=$S{userid} and t8_prod_info_id ='"+prodInfoId+"'and roleid = '14'",
				DataSourceProperty.IDB, userId).getEffect();
    }
    public int deleteByT8ProdInfoId(String t8ProdInfoId) throws Exception {
        return super.update("delete from idb_disclosure_operation where t8_prod_info_id=$S{t8ProdInfoId} and roleid = '14'", DataSourceProperty.IDB, t8ProdInfoId).getEffect();
    }
    public List<SqlRow> getUserId(String t8DisclosureNoticeId) throws Exception {

        return super.findRows("SELECT userid from idb_disclosure_operation where deal_id=$S{dealId}", DataSourceProperty.IDB, t8DisclosureNoticeId);
    }
    public int updateUserId(String newUserId,String oldUserid) throws Exception {
        return super.update("update idb_disclosure_operation set userid = '"+newUserId+"' where userid= '"+oldUserid+"' and roleid = '14'", DataSourceProperty.IDB, newUserId).getEffect();
    }
    public SqlRow getOperationId(DisclosureOperation disclosureOperation) throws Exception {
        return super.findRow("select id from idb_disclosure_operation where deal_id=$S{dealId} and userid =$S{userid} and roleid = '14' ", DataSourceProperty.IDB, disclosureOperation);
    }

	/**
	 * 功能：插入待办信息
	 * 作者：rennannan
	 * 日期：20210603
	 *
	 * @param ope
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertDisOperation(DisclosureOperation ope) throws Exception {
		/*return super.update("INSERT INTO idb_disclosure_operation(id,t8_prod_info_id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{operationType},$S{disclosureType},$S{roleid},$S{userid},$S{status},$S{dealTable},$S{dealId},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{endDate},$S{endTime},$S{remark})",
				ope);*/
		return super.update("INSERT INTO idb_disclosure_operation(id, t8_prod_info_id, operation_type, disclosure_type, roleid, userid, status,\n" +
						"                                    deal_table, deal_id, crt_date, crt_time, crt_user_id, crt_user_name, end_date,\n" +
						"                                    end_time, remark)\n" +
						"select $AUTOIDS{id}, $S{t8ProdInfoId}, $S{operationType}, $S{disclosureType}, $S{roleid}, $S{userid}, $S{status},\n" +
						"        $S{dealTable}, $S{dealId}, $S{crtDate}, $S{crtTime}, $S{crtUserId}, $S{crtUserName}, $S{endDate}, $S{endTime},\n" +
						"        $S{remark} from dual where not exists(select id from idb_disclosure_operation where t8_prod_info_id = $S{t8ProdInfoId} " +
						"and operation_type = $S{operationType} and disclosure_type = $S{disclosureType} and userid = $S{userid} and deal_id = $S{dealId} and status = '0')",
				DataSourceProperty.IDB, ope);
	}

	/**
	 * 功能：插入待办信息
	 * 作者：rennannan
	 * 日期：20210603
	 *
	 * @param ope
	 * @return
	 * @throws Exception
	 */
	public UpdateResult insertDisOperationForNetVal(DisclosureOperation ope) throws Exception {
		/*return super.update("INSERT INTO idb_disclosure_operation(id,t8_prod_info_id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{operationType},$S{disclosureType},$S{roleid},$S{userid},$S{status},$S{dealTable},$S{dealId},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{endDate},$S{endTime},$S{remark})",
				ope);*/
		return super.update("INSERT INTO idb_disclosure_operation(id, t8_prod_info_id, operation_type, disclosure_type, roleid, userid, status,\n" +
						"                                    deal_table, deal_id, crt_date, crt_time, crt_user_id, crt_user_name, end_date,\n" +
						"                                    end_time, remark)\n" +
						"select $AUTOIDS{id}, $S{t8ProdInfoId}, $S{operationType}, $S{disclosureType}, $S{roleid}, $S{userid}, $S{status},\n" +
						"        $S{dealTable}, $S{dealId}, $S{crtDate}, $S{crtTime}, $S{crtUserId}, $S{crtUserName}, $S{endDate}, $S{endTime},\n" +
						"        $S{remark} from dual where not exists(select id from idb_disclosure_operation where t8_prod_info_id = $S{t8ProdInfoId} " +
						"and operation_type = $S{operationType} and disclosure_type = $S{disclosureType} and userid = $S{userid} and deal_id = $S{dealId} and status = '0')",
				DataSourceProperty.IDB, ope);
	}


	public UpdateResult updateDisclosureOperation(SqlParam<DisclosureOperation> params) throws Exception {
		return super.update("UPDATE idb_disclosure_operation SET operation_type=$S{operationType} ,disclosure_type=$S{disclosureType} ,roleid=$S{roleid} ,userid=$S{userid} ,status=$S{status} ,deal_table=$S{dealTable} ,deal_id=$S{dealId} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,end_date=$S{endDate} ,end_time=$S{endTime} ,remark=$S{remark}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}

	/**
	 * 通过业务id,信披类型和代办类型修改代表状态
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateDisclosureOperation(DisclosureOperation params) throws Exception {
		return super.update("UPDATE idb_disclosure_operation SET status=$S{status} ,end_date=$S{endDate} ,end_time=$S{endTime} ,remark=$S{remark}  WHERE operation_type=$S{operationType} and disclosure_type=$S{disclosureType} and deal_id=$S{dealId}", DataSourceProperty.IDB, params);
	}
	/**
	 * 通过业务id、信披类型、角色id、用户id和代办类型修改代表状态
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateOperation(DisclosureOperation params) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE idb_disclosure_operation SET status=$S{status} ,end_date=$S{endDate} ,end_time=$S{endTime} ,remark=$S{remark}  WHERE operation_type=$S{operationType} and disclosure_type=$S{disclosureType} and deal_id=$S{dealId} ");
		/*and roleid=$S{roleid} and userid=$S{userid} */
		if (StringUtils.isNotBlank(params.getRoleid())) {
			sql.append(" and roleid in ($U{roleid})");
		}
		if (StringUtils.isNotBlank(params.getUserid())) {
			sql.append(" and userid=$S{userid}");
		}
		return super.update(sql.toString(),
				DataSourceProperty.IDB, params);
	}

	/**
	 * 通过业务id、信披类型、角色ids、代办类型修改代表状态
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateOperationByRoleIds(DisclosureOperation params) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE idb_disclosure_operation SET status=$S{status} ,end_date=$S{endDate} ,end_time=$S{endTime} ,remark=$S{remark}  WHERE operation_type=$S{operationType} and disclosure_type=$S{disclosureType} and deal_id=$S{dealId} AND roleid IN ($S{roleIds})");

		return super.update(sql.toString(),
				DataSourceProperty.IDB, params);
	}

	/**
	 * 通过待办类型、信披类型、流水id删除首页待办
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteDisclosureOperation(DisclosureOperation params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_operation WHERE  operation_type=$S{operationType} and disclosure_type=$S{disclosureType} and deal_id=$S{dealId}",
				DataSourceProperty.IDB, params);
	}

	/**
	 * 功能：根据待办类型、信披类型、dealid、用户id删除待办
	 * 作者:rennannan
	 * 日期：20210928
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteOperationS(DisclosureOperation params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_operation WHERE  operation_type=$S{operationType} and disclosure_type=$S{disclosureType} and deal_id=$S{dealId} and userid=$S{userid}",
				DataSourceProperty.IDB, params);
	}

	public List<SqlRow> findDesktopDisclosureOperations() throws Exception {
		String userId = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		String roleIds = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_roleids"));
		List<String> roleList = Arrays.asList(roleIds.split(","));
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		StringBuilder sql = new StringBuilder("select a.operation_type,a.deal_table,\n" +
				"       group_concat(a.deal_id) deal_id,(select itemval from sys_dict_item where dict =  'xp_doc_type' and itemkey = a.disclosure_type) disclosure_type_desc,\n" +
				"       (select itemval from sys_dict_item where dict =  't8_operation_type' and itemkey = a.operation_type) operation_type_desc,a.disclosure_type,a.end_date,a.userid,\n" +
				"       replace((select max(operation_desc) operation_desc from idb_disclosure_operation_set where\n" +
				"               disclosure_type = a.disclosure_type and operation_type = a.operation_type),'${count}', count(a.id)) operationDesc, b.url\n" +
				"from idb_disclosure_operation a left join idb_disclosure_operation_set b on a.operation_type=b.operation_type and a.disclosure_type=b.disclosure_type\n" +
				"where    a.status = '0'\n" +
				"  and a.userid = $S{userId}\n" +
				"group by a.operation_type,a.disclosure_type\n" +
				"union \n" +
				"select\n" +
				"    a.operation_type,\n" +
				"    '' deal_table,\n" +
				"    '' deal_id,\n" +
				"    (select itemval\n" +
				"    from sys_dict_item\n" +
				"    where dict = 'xp_doc_type' and itemkey = a.disclosure_type)   disclosure_type_desc,\n" +
				"    (select itemval\n" +
				"    from sys_dict_item\n" +
				"    where dict = 't8_operation_type' and itemkey = a.operation_type)  operation_type_desc,\n" +
				"    a.disclosure_type,\n" +
				"    '' end_date,\n" +
				"    '' userid,\n" +
				"replace((select max(operation_desc) operation_desc\n" +
				"         from idb_disclosure_operation_set\n" +
				"         where disclosure_type = a.disclosure_type\n" +
				"           and operation_type = a.operation_type), '${count}', count(a.t8_disclosure_notice_id)) operationDesc,\n" +
				"    b.url\n" +
				"from (select temp.*\n" +
				"    from (select a.t8_disclosure_notice_id,\n" +
				"    b.prod_code,\n" +
				"    d.id t8ProdInfoId,\n" +
				"    b.prod_base_date,\n" +
				"    tdr.disclosure_type,\n" +
				"    c.notice_roleid,\n" +
				"    '12' operation_type\n" +
				"    from idb_disclosure_regular_asset a\n" +
				"    join idb_disclosure_notice b on a.t8_disclosure_notice_id = b.id\n" +
				"    LEFT JOIN idb_disclosure_prod_rule tdr ON b.t8_disclosure_rule_id = tdr.id\n" +
				"    left join idb_disclosure_prod_rule c on b.t8_disclosure_rule_id = c.id\n" +
				"    join t8_prod_info d on b.prod_code = d.prod_code\n" +
				"    where a.assets_type = '私募资管产品'\n" +
				"    and a.amount > 0) temp\n" +
				"\n" +
				"    where NOT EXISTS(select id\n" +
				"    from idb_disclosure_asset_bottom\n" +
				"    where prod_code = temp.prod_code\n" +
				"    and data_date = temp.prod_base_date)) a /*临时表a查询的是需要提示首页待办的公告信息*/\n" +
				"    /*关联到对应估值核算人员*/" +
				"    join idb_disclosure_evaluate_emp temp2 on a.prod_code  = temp2.prod_code\n" +
				"    join sys_user temp3 on temp2.emp_no =  temp3.jobno" +
				"    LEFT JOIN idb_disclosure_operation_set b ON a.operation_type = b.operation_type\n" +
				"    AND a.disclosure_type = b.disclosure_type\n" +
				"where temp3.userid = $S{userId}\n" +
				"group by a.operation_type, a.disclosure_type");
		//包含信息披露岗
		if (roleList.contains("8")) {
			sql.append(" union select '13' operation_type,'' deal_table,'' deal_id,'信披报告' disclosure_type_desc,\n" +
					"   (select itemval from sys_dict_item where dict =  't8_operation_type' and itemkey = '13') operation_type_desc,\n" +
					"\t '5' disclosure_type, '' end_date,'' userid,\n" +
					"\t replace((select operation_desc operation_desc from idb_disclosure_operation_set where\n" +
					"\t\t\t\t              disclosure_type = '5' and operation_type = '13'),'${count}', count(version.id)) operationDesc, (select url operation_desc from idb_disclosure_operation_set where\n" +
					"\t\t\t\t              disclosure_type = '5' and operation_type = '13') url\n" +
					"  from idb_disclosure_mod_version version\n" +
					"  join idb_disclosure_mod dmod\n" +
					"\t  on version.t8_disclosure_mod_id=dmod.id\n" +
					" where version.status='0' and version.crt_user_id!=$S{userId}  having count(version.id) > 0\n");
		}
		return super.findRows(sql.toString(), DataSourceProperty.IDB, map);

	}

	/**
	 * 功能：根据业务id、待办类型、信披类型、用户id修改待办状态、办结日期、办结时间
	 * 作者：rennannan
	 * 日期：20210603
	 *
	 * @param ope
	 * @return
	 */
	public int updateOperationStatus(DisclosureOperation ope) throws Exception {
		String sql = " update idb_disclosure_operation " +
				" set status=$S{status}," +
				" end_date=$S{endDate}," +
				" end_time=$S{endTime} " +
				" where operation_type=$S{operationType} " +
				"       and disclosure_type=$S{disclosureType}" +
				" and deal_id=$S{dealId}" +
				" and userid=$S{userid}";
		return super.update(sql, ope).getEffect();
	}


	/**
	 * 功能：根据业务id、待办类型、信披类型、用户id删除待办
	 * 作者：rennannan
	 * 日期：20210603
	 *
	 * @param ope
	 * @return
	 */
	public int deleteByTypeAndDealId(DisclosureOperation ope) throws Exception {
		String sql = " delete from idb_disclosure_operation  " +
				" where operation_type=$S{operationType} " +
				" and disclosure_type=$S{disclosureType}" +
				" and deal_id=$S{dealId}" +
				" and userid=$S{userid}";
		return super.update(sql, DataSourceProperty.IDB, ope).getEffect();
	}

	/**
	 * 功能：根据操作类型和公告id删除待办数据
	 * 作者：rennannan
	 * 日期：20210609
	 *
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	public int deleteByTypeAndDealIds(DisclosureOperation operation) throws Exception {
		/*String sql = "delete from idb_disclosure_operation \n" +
				"\t\t       where operation_type=$S{operationType} \n" +
				"\t\t\t\t\t   and deal_id in(select id from idb_disclosure_notice_process where t8_disclosure_notice_id=$S{dealId})";*/
       if (StringUtils.isNotBlank(operation.getDealId())) {
		   String sql = "delete from idb_disclosure_operation \n" +
				   "\t\t       where operation_type=$S{operationType} \n" +
				   "\t\t\t\t\t   and deal_id in($U{dealId})";
		   return super.update(sql, DataSourceProperty.IDB, operation).getEffect();
	   } else {
       	return 0;
	   }
	}


    /**
	 * 功能：根据待办类型和公告生成日期、信披类型删除待办  已经开始补录的公告的待办不删除
	 * 作者：rennannan
	 * 日期：20210609
	 *
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	public int deleteOpeByTypeAndCrtDate(DisclosureOperation operation) throws Exception {
		StringBuilder sql = new StringBuilder("delete from idb_disclosure_operation  " +
				"       where operation_type=$S{operationType}  " +
				"         and deal_id in(select id " +
				"                  from idb_disclosure_notice_process " +
				"  where t8_disclosure_notice_id in (    select notice.id from idb_disclosure_notice notice " +
				" join idb_disclosure_prod_rule rule " +
				"   on notice.t8_disclosure_rule_id=rule.id" +
				" where notice.crt_date = $S{crtDate}" +
				"   and rule.disclosure_type=$S{disclosureType}");
		if (StringUtils.isNotEmpty(operation.getNotInNoticeIds())) {
			sql.append("and notice.id not in $U{notInNoticeIds}");
		}
		sql.append("))");
		return super.update(sql.toString(), DataSourceProperty.IDB, operation).getEffect();
	}

	/**
	 * 功能：根据待办类型和公告生成日期、信披类型删除待办(没有补录表)
	 * 作者：rennannan
	 * 日期：20211025
	 *
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	public int deleteOpeByNoticeType(DisclosureOperation operation) throws Exception {
		StringBuilder sql = new StringBuilder("delete from idb_disclosure_operation  " +
				"       where operation_type=$S{operationType}  " +
				"         and deal_id in(select notice.id from idb_disclosure_notice notice " +
				" 					      where notice.crt_date = $S{crtDate}" +
				"   				        and disclosure_type=$S{disclosureType}");
		if (StringUtils.isNotEmpty(operation.getNotInNoticeIds())) {
			sql.append("and notice.id not in $U{notInNoticeIds}");
		}
		sql.append(")");
		return super.update(sql.toString(), DataSourceProperty.IDB, operation).getEffect();
	}

	/**
	 * 功能：根据待办类型和公告基准日期、信披类型删除待办(没有补录表)
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	public int deleteOpeByNoticeInfo(DisclosureOperation operation) throws Exception {
		StringBuilder sql = new StringBuilder("delete from idb_disclosure_operation  " +
				"       where operation_type=$S{operationType}  " +
				"         and deal_id in(select notice.id from idb_disclosure_notice notice " +
				" 					      where notice.prod_base_date = $S{crtDate}" +
				"   				        and disclosure_type=$S{disclosureType}");
		if (StringUtils.isNotEmpty(operation.getNotInNoticeIds())) {
			sql.append("and notice.id not in $U{notInNoticeIds}");
		}
		sql.append(")");
		return super.update(sql.toString(), DataSourceProperty.IDB, operation).getEffect();
	}

	/**
	 * 功能：根据信披任务id删除待办
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @param operation
	 * @throws Exception
	 */
	public void deleteOpeByTaskId(DisclosureOperation operation) throws Exception {
		String sql = "delete from idb_disclosure_operation  " +
				"       where operation_type=$S{operationType}  " +
				"         and deal_id in(select notice.id from idb_disclosure_notice notice " +
				" 					      where notice.task_id = $S{taskId})";
		super.update(sql, DataSourceProperty.IDB, operation);
	}

	public SqlResult<Map<String, Object>> findDesktopDisclosureOperationsDetail(Map<String, Object> params) throws Exception {
		SqlResult<Map<String, Object>> sqlResult;
		if (OperationTypeEnum.TWELVE.getVal().equals(params.get("operationType"))) {
			sqlResult = SqlUtils.sqlPackage("select  temp.notice_roleid,'12' operation_type,temp.disclosure_type,temp.disclosure_son_type, '' deal_id,'' deal_table,'/main/pms/disclosureFlow/disclosureAssetPlan' url, temp.prod_code, temp.prod_name from ( select a.t8_disclosure_notice_id,b.prod_code,b.prod_name,b.prod_base_date,c.disclosure_type," +
					"c.disclosure_son_type,c.notice_roleid,c.crt_date " +
					" from idb_disclosure_regular_asset a  join idb_disclosure_notice b on a.t8_disclosure_notice_id = b.id  " +
					" left join idb_disclosure_prod_rule c on b.t8_disclosure_rule_id = c.id " +
					" join idb_disclosure_evaluate_emp d on b.prod_code = d.prod_code " +
					" join sys_user e on d.emp_no = e.jobno" +
					" where a.assets_type='私募资管产品' and a.amount > 0 and e.userid = $S{userid}) temp " +
					"where NOT EXISTS ( select id from idb_disclosure_asset_bottom " +
					"where prod_code = temp.prod_code and data_date = temp.prod_base_date) and temp.disclosure_type = $S{disclosureType} order by temp.crt_date desc", DataSourceProperty.IDB, params, this);
		} else if (OperationTypeEnum.ONE.getVal().equals(params.get("operationType"))) {
			sqlResult = SqlUtils.sqlPackage("select a.operation_type,a.disclosure_type,a.deal_id,a.deal_table,e.disclosure_son_type,b.url,c.prod_code,c.prod_name,c.id t8ProdInfoId " +
					" from idb_disclosure_operation a" +
					" left join idb_disclosure_operation_set b" +
					" on a.operation_type = b.operation_type and a.disclosure_type = b.disclosure_type" +
					" left join t8_prod_info c on a.t8_prod_info_id = c.id " +
					" left join idb_disclosure_prod_task d on a.deal_id= d.id and a.deal_table = 'idb_disclosure_prod_task'\n" +
					"\t\tleft join idb_disclosure_prod_rule e on d.t8_disclosure_rule_id = e.id " +
					"  where a.status = $S{status} and a.userid = $S{userid} and a.operation_type = $S{operationType} and a.disclosure_type = $S{disclosureType} order by a.crt_date desc", DataSourceProperty.IDB, params, this);
		}else if(OperationTypeEnum.TWO.getVal().equals(params.get("operationType"))){
			sqlResult = SqlUtils.sqlPackage("select a.operation_type,a.disclosure_type,a.deal_id,a.deal_table,ifnull(d.disclosure_son_type,'8') disclosure_son_type,b.url,c.prod_code,c.prod_name,c.id t8ProdInfoId " +
					" from idb_disclosure_operation a" +
					" left join idb_disclosure_operation_set b" +
					" on a.operation_type = b.operation_type and a.disclosure_type = b.disclosure_type" +
					" left join t8_prod_info c on a.t8_prod_info_id = c.id " +
					" left join idb_disclosure_notice d on d.id = a.deal_id \n" +
					"  where a.status = $S{status} and a.userid = $S{userid} and a.operation_type = $S{operationType} and a.disclosure_type = $S{disclosureType} order by a.crt_date desc", DataSourceProperty.IDB, params, this);
		}else if(OperationTypeEnum.SEVEN.getVal().equals(params.get("operationType"))){
			//判断是否是净值披露待办
			if(DisclosureTypeEnum.NINE.getVal().equals(params.get("disclosureType"))){
				sqlResult = SqlUtils.sqlPackage("SELECT a.operation_type,a.disclosure_type,a.deal_id,a.deal_table,b.url,tpvt.task_date FROM idb_disclosure_operation a " +
						"LEFT JOIN idb_disclosure_operation_set b ON a.operation_type=b.operation_type AND a.disclosure_type=b.disclosure_type LEFT JOIN t8_prod_net_value_task tpvt " +
						"ON tpvt.id=a.deal_id WHERE a.status = $S{status} and a.userid = $S{userid} and a.operation_type = $S{operationType} and a.disclosure_type = $S{disclosureType} order by tpvt.task_date desc", DataSourceProperty.IDB, params, this);
			}else{
				sqlResult = SqlUtils.sqlPackage("select \ta.operation_type,\n" +
						"\ta.disclosure_type,\n" +
						"\ta.deal_id,\n" +
						"\ta.deal_table,\n" +
						"\te.disclosure_son_type,\n" +
						"\tb.url,\n" +
						"\td.prod_code,\n" +
						"\td.prod_name,\n" +
						"\td.t8_prod_Info_id t8ProdInfoId " +
						" from \tidb_disclosure_operation a\n" +
						"\tLEFT JOIN idb_disclosure_operation_set b ON a.operation_type = b.operation_type\n AND b.operation_type=a.operation_type AND b.disclosure_type=a.disclosure_type" +
						"\tLEFT JOIN idb_disclosure_notice_process f ON a.deal_id = f.id\n" +
						"\tLEFT JOIN idb_disclosure_notice d ON d.id = a.deal_id\n" +
						"\tLEFT JOIN idb_disclosure_prod_rule e ON d.t8_disclosure_rule_id = e.id " +
						"  where a.status = $S{status} and a.userid = $S{userid} and a.operation_type = $S{operationType} and a.disclosure_type = $S{disclosureType} order by a.crt_date desc", DataSourceProperty.IDB, params, this);
			}

		}else {
			 sqlResult = SqlUtils.sqlPackage("select a.operation_type,a.disclosure_type,a.deal_id,a.deal_table,e.disclosure_son_type,b.url,c.prod_code,c.prod_name,c.id t8ProdInfoId " +
					 " from idb_disclosure_operation a" +
					 " left join idb_disclosure_operation_set b" +
					 " on a.operation_type = b.operation_type and a.disclosure_type = b.disclosure_type" +
					 " left join t8_prod_info c on a.t8_prod_info_id = c.id " +
					 " left join idb_disclosure_notice d on a.deal_id= d.id and a.deal_table = 'idb_disclosure_notice'\n" +
					 " left join idb_disclosure_prod_rule e on d.t8_disclosure_rule_id = e.id " +
					 "  where a.status = $S{status} and a.userid = $S{userid} and a.operation_type = $S{operationType} and a.disclosure_type = $S{disclosureType} order by a.crt_date desc", DataSourceProperty.IDB, params, this);
		}
       return sqlResult;
	}

	public void updateStatusByDealId(String deal_table,String dealId,String disclosure_type) throws Exception {
		super.update("UPDATE idb_disclosure_operation SET `status` = '1'  WHERE `deal_id` = '"+dealId+"' and deal_table='"+deal_table+"' and disclosure_type='"+disclosure_type+"'", DataSourceProperty.IDB);
	}

	/**
	 * 通过实体类条件删除待办
	 * @param operation
	 * @throws Exception
	 */
	public void deleteByCondition(DisclosureOperation operation) throws Exception {
		StringBuilder sql = new StringBuilder("delete from idb_disclosure_operation where 1=1 ");
		if (StringUtils.isNotBlank(operation.getDealId())) {
			sql.append(" and deal_id = $S{dealId}");
		}
		if (StringUtils.isNotBlank(operation.getOperationType())) {
			sql.append(" and operation_type = $S{operationType}");
		}
		if (StringUtils.isNotBlank(operation.getDisclosureType())) {
			sql.append(" and disclosure_type = $S{disclosureType}");
		}
		if (StringUtils.isNotBlank(operation.getUserid())) {
			sql.append(" and userid = $S{userid}");
		}
		if (StringUtils.isNotBlank(operation.getRoleid())) {
			sql.append(" and roleid = $S{roleid}");
		}
		if (StringUtils.isNotBlank(operation.getStatus())) {
			sql.append(" and status = $S{status}");
		}
		super.update(sql.toString(), DataSourceProperty.IDB, operation);
	}
}
