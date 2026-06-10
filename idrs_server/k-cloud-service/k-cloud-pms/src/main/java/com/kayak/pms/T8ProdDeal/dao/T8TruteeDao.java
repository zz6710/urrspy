package com.kayak.pms.T8ProdDeal.dao;

import com.kayak.core.sql.SqlRow;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kayak.base.dao.ComnDao;


@Repository
public class T8TruteeDao extends ComnDao {

	
	
	public SqlRow queryTruteeByCode(Map<String,Object> param) throws Exception  {
		
		return super.findRow("select trutee_name from t8_trutee_info where trutee_code = $S{truteeCode}", param);
	}

}
