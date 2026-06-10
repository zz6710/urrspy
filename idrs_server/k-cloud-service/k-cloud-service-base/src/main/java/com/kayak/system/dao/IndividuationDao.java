package com.kayak.system.dao;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.Individuation;

@Repository
public class IndividuationDao extends ComnDao {

	public SqlResult<Individuation> findIndividuations(SqlParam<Individuation> params) throws Exception {
		return super.findRows(
				"SELECT theme_color,menu_color,menu_min,menu_bg_show,menu_bg FROM sys_user_individuation WHERE userid = $S{userid}",
				params);
	}

	public void updateIndividuation(SqlParam<Individuation> params) throws Exception {
		super.doTrans(() -> {
			super.update("DELETE FROM sys_user_individuation WHERE userid = $S{userid}", params.getModel());
			super.update(
					"INSERT INTO sys_user_individuation(userid,theme_color,menu_color,menu_min,menu_bg_show,menu_bg) VALUES($S{userid},$S{themeColor},$S{menuColor},$S{menuMin},$S{menuBgShow},$S{menuBg})",
					params.getModel());
		});
	}

}
