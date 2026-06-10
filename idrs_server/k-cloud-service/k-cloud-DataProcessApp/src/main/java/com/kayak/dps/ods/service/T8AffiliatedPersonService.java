package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.model.T8AffiliatedPerson;
import com.kayak.dps.ods.dao.T8AffiliatedPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "T8AffiliatedPerson服务", model = T8AffiliatedPerson.class)
public class T8AffiliatedPersonService {

	@Autowired
	private T8AffiliatedPersonDao t8AffiliatedPersonDao;

	@API(desc = "查询法人关联信息", auth = APIAuth.YES)
	public SqlResult<T8AffiliatedPerson> findT8AffiliatedPersons(SqlParam<T8AffiliatedPerson> params) throws Exception {
		params.setMakeSql(false);
		return t8AffiliatedPersonDao.findT8AffiliatedPersons(params);
	}

	@API(desc = "添加T8AffiliatedPerson", params = "id,name_operson,effective_date,expiry_date", auth = APIAuth.NO)
	public String addT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		if(t8AffiliatedPersonDao.overlapCheckAdd(params)>0){
			return RequestSupport.updateReturnJson(false, "新增失败，生效日期区间存在重叠！", null).toString();
		}else{
			t8AffiliatedPersonDao.addT8AffiliatedPerson(params).getEffect();
			return RequestSupport.updateReturnJson(true, "增加成功", null).toString();
		}
	}

	@API(desc = "修改T8AffiliatedPerson", params = "id,name_operson,effective_date,expiry_date", auth = APIAuth.NO)
	public String updateT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		if(t8AffiliatedPersonDao.overlapCheckEdit(params)>0){
			return RequestSupport.updateReturnJson(false, "修改失败，生效日期区间存在重叠！", null).toString();
		}else{
			t8AffiliatedPersonDao.updateT8AffiliatedPerson(params).getEffect();
			return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
		}
	}
	
	@API(desc = "删除T8AffiliatedPerson", params = "id,name_operson,effective_date,expiry_date", auth = APIAuth.NO)
	public int deleteT8AffiliatedPerson(SqlParam<T8AffiliatedPerson> params) throws Exception {
		return t8AffiliatedPersonDao.deleteT8AffiliatedPerson(params).getEffect();
	}
	@API(desc = "关联法人批量导入", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public void importT8ProdPrice(SqlParam<T8AffiliatedPerson> t8AffiliatedPerson){
		//此方法只作用于权限控制
	}
	@API(desc = "查询法人关联信息", auth = APIAuth.NO)
	public SqlResult<T8AffiliatedPerson> findDesc(SqlParam<T8AffiliatedPerson> params) throws Exception {
		params.setMakeSql(true);
		return t8AffiliatedPersonDao.findDesc(params);
	}

}
