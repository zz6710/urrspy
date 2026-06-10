package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwdAsstBondComprat;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwdAsstBondCompratDao extends ComnDao {

	public SqlResult<DwdAsstBondComprat> findDwdAsstBondComprats(SqlParam<DwdAsstBondComprat> params) throws Exception {
		String sql = "SELECT id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date FROM dwd_asst_bond_comprat where 1=1 ";
		if ("1".equals(params.getModel().getMultRating())) {
			sql += " AND instr(rating, '/')>0";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		return super.update("INSERT INTO dwd_asst_bond_comprat(id,comy_cd,comy_name,rating,rating_comp,annt_dt,flag,summit_user,update_date,update_time,deal_date) VALUES($AUTOIDI{id},$S{comyCd},$S{comyName},$S{rating},$S{ratingComp},$S{anntDt},$S{flag},$S{summitUser},$S{updateDate},$S{updateTime},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		return super.update("UPDATE dwd_asst_bond_comprat SET comy_cd=$S{comyCd} ,comy_name=$S{comyName} ,rating=$S{rating} ,rating_comp=$S{ratingComp} ,annt_dt=$S{anntDt} ,flag=$S{flag} ,summit_user=$S{summitUser} ,update_date=$S{updateDate} ,update_time=$S{updateTime} ,deal_date=$S{dealDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwdAsstBondComprat(SqlParam<DwdAsstBondComprat> params) throws Exception {
		return super.update("DELETE FROM dwd_asst_bond_comprat WHERE  id=$I{id} ",
				params.getModel());
	}

}
