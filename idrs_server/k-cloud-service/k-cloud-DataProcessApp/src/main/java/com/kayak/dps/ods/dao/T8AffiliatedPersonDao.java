package com.kayak.dps.ods.dao;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.T8AffiliatedPerson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8AffiliatedPersonDao extends ComnDao {

	public SqlResult<T8AffiliatedPerson> findT8AffiliatedPersons(SqlParam<T8AffiliatedPerson> params) throws Exception {
		String sql=" SELECT id,name_operson,effective_date,expiry_date,affiliated_type,trutee_name " +
				" FROM dwd_affiliated_person " +
				" where 1=1 " ;
		if (StringUtils.isNotBlank(params.getModel().getNameOperson()) && !StringUtils.equals(params.getModel().getNameOperson(),"")) {
			sql += " and name_operson like '%$U{nameOperson}%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getTruteeName()) && !StringUtils.equals(params.getModel().getTruteeName(),"")) {
			sql += " and trutee_name like '%$U{truteeName}%' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getEffectiveDateStart()) && StringUtils.isNotBlank(params.getModel().getEffectiveDateEnd())){
			sql = sql + " and effective_date between  '" + params.getModel().getEffectiveDateStart() + "'  and '" + params.getModel().getEffectiveDateEnd() + "' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getExpiryDateStart()) && StringUtils.isNotBlank(params.getModel().getExpiryDateEnd())){
			sql = sql + " and expiry_date  between '" + params.getModel().getExpiryDateStart() + "'  and '" + params.getModel().getExpiryDateEnd() + "' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getAffiliatedType()) && !StringUtils.equals(params.getModel().getAffiliatedType(),"")) {
			sql = sql + " and affiliated_type = $S{affiliatedType}  ";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		return super.update("INSERT INTO dwd_affiliated_person(name_operson,effective_date,expiry_date,affiliated_type,trutee_name) VALUES($S{nameOperson},$S{effectiveDate},$S{expiryDate},$S{affiliatedType},$S{truteeName})",
				DataSourceProperty.PUB,params.getModel());
	}

	public int addT8AffiliatedPerson(T8AffiliatedPerson t8AffiliatedPerson) throws Exception {
		return super.update("INSERT INTO dwd_affiliated_person(id,name_operson,effective_date,expiry_date) VALUES($AUTOIDS{id},$S{nameOperson},$S{effectiveDate},$S{expiryDate})",
				DataSourceProperty.PUB,t8AffiliatedPerson).getEffect();
	}
//	public  List<SqlRow> selectT8AffiliatedPerson(String s) throws Exception {
//		return super.findRows(SqlRow.class,"SELECT id,name_operson,effective_date,expiry_date FROM dwd_affiliated_person where name_operson=$S{s} ",0,s);
//	}
    public UpdateResult updateT8AffiliatedPersonS(String oldDate) throws Exception {
	return super.update("UPDATE dwd_affiliated_person SET expiry_date=$S{oldDate}  WHERE  expiry_date='' ",DataSourceProperty.PUB,oldDate);
}

	public UpdateResult updateT8AffiliatedPersonValS(String oldDate) throws Exception {
		return super.update("UPDATE dwd_affiliated_person SET expiry_date=$S{oldDate}  WHERE  expiry_date is not null ",DataSourceProperty.PUB,oldDate);
	}

	public UpdateResult updateT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		return super.update("UPDATE dwd_affiliated_person SET name_operson=$S{nameOperson} ,effective_date=$S{effectiveDate} ,expiry_date=$S{expiryDate},affiliated_type=$S{affiliatedType},trutee_name=$S{truteeName}  WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		return super.update("DELETE FROM dwd_affiliated_person WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}
	//查询关联法人最大生效日
	public SqlResult<T8AffiliatedPerson> findDesc(SqlParam<T8AffiliatedPerson> params) throws Exception{

		return super.findRows("select effective_date\n" +
				"from dwd_affiliated_person\n" +
				"order by effective_date desc\n" +
				"limit 0,1 ",DataSourceProperty.PUB, params);
	}


	public void importT8AffiliatedPerson(List<T8AffiliatedPerson> t8AffiliatedPeople) throws Exception {
		for (T8AffiliatedPerson t8AffiliatedPerson : t8AffiliatedPeople) {
			super.update("INSERT INTO dwd_affiliated_person(name_operson,effective_date,expiry_date,affiliated_type,trutee_name) VALUES($S{nameOperson},(case when $S{effectiveDate}='' then null else DATE_FORMAT($S{effectiveDate}, '%Y%m%d') end) ,(case when $S{expiryDate}='' then null else DATE_FORMAT($S{expiryDate}, '%Y%m%d') end),$S{affiliatedType},$S{truteeName})",
					DataSourceProperty.PUB,t8AffiliatedPerson).getEffect();
		}
	}

	public int overlapCheckAdd(SqlParam<T8AffiliatedPerson> params) throws Exception {
		SqlRow sqlRow = super.findRow("select count(*) count from dwd_affiliated_person where name_operson = '"+params.getParams().get("nameOperson")+"' and affiliated_type = '"+params.getParams().get("affiliatedType")+"' and ((effective_date>='"+params.getParams().get("effectiveDate")+"' and effective_date<='"+params.getParams().get("expiryDate")+"') or (effective_date>='"+params.getParams().get("effectiveDate")+"' and expiry_date<='"+params.getParams().get("expiryDate")+"') or (expiry_date>='"+params.getParams().get("effectiveDate")+"' and expiry_date<='"+params.getParams().get("expiryDate")+"') or(effective_date <= '"+params.getParams().get("effectiveDate")+"' and expiry_date >= '"+params.getParams().get("effectiveDate")+"') or (effective_date <= '"+params.getParams().get("expiryDate")+"' and expiry_date >= '"+params.getParams().get("expiryDate")+"' ))", params);
		return sqlRow.getInteger("count");
	}

	public int overlapCheckEdit(SqlParam<T8AffiliatedPerson> params) throws Exception {
		SqlRow sqlRow = super.findRow("select count(*) count from dwd_affiliated_person where name_operson = '"+params.getParams().get("nameOperson")+"' and ((effective_date>='"+params.getParams().get("effectiveDate")+"'  and affiliated_type = '"+params.getParams().get("affiliatedType")+"' and effective_date<='"+params.getParams().get("expiryDate")+"') or (effective_date>='"+params.getParams().get("effectiveDate")+"' and expiry_date<='"+params.getParams().get("expiryDate")+"') or (expiry_date>='"+params.getParams().get("effectiveDate")+"' and expiry_date<='"+params.getParams().get("expiryDate")+"') or(effective_date <='"+params.getParams().get("effectiveDate")+"' and expiry_date >= '"+params.getParams().get("effectiveDate")+"') or (effective_date <= '"+params.getParams().get("expiryDate")+"' and expiry_date >= '"+params.getParams().get("expiryDate")+"' )) and id != '"+params.getParams().get("id")+"'", params);
        return sqlRow.getInteger("count");
	}

	public void handelBaseData(Map<String, Object> params) throws Exception {
		//查询需要处理的任务数据{// P189、P190、P191// }
		List<SqlRow> sqlRes = super.findRows("select ps.sqlStr from base_port_sql_info ps where ps.task_id  in('P189','P190','P191') order  by ps.exe_order",DataSourceProperty.PUB);
		DaoUtil.doTrans(() -> {
			for (SqlRow sqlRow : sqlRes) {
				if (StringUtils.isNotBlank(sqlRow.getString("sqlStr"))) {
					super.update(sqlRow.getString("sqlStr"), DataSourceProperty.PUB, params);
				}
			}
		});
	}
}
