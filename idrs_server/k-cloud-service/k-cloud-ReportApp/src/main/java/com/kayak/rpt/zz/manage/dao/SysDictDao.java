package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.zz.manage.model.SysDict;
import org.springframework.stereotype.Repository;

@Repository
public class SysDictDao extends ComnDao {

	public SqlRow qry(SysDict params) throws Exception {
		return super.findRow("select  itemkey  from  sys_dict_item  where  DICT =  $S{dict}  and  itemval    = $S{itemval}  ", params);
	}

}
